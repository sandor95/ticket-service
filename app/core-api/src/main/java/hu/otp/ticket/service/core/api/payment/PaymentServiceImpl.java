package hu.otp.ticket.service.core.api.payment;

import static hu.otp.ticket.service.core.api.payment.exception.PaymentError.*;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import hu.otp.ticket.service.core.api.model.BankCard;
import hu.otp.ticket.service.core.api.model.Payment;
import hu.otp.ticket.service.core.api.model.PaymentLock;
import hu.otp.ticket.service.core.api.model.User;
import hu.otp.ticket.service.core.api.payment.exception.PaymentException;
import hu.otp.ticket.service.core.api.persistentlayer.BankCardGateway;
import hu.otp.ticket.service.core.api.persistentlayer.PaymentRepository;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import hu.otp.ticket.service.util.Util;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserGateway userGateway;

    private final BankCardGateway bankCardGateway;

    private final PaymentRepository paymentRepository;

    private final PaymentLockService lockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Payment pay(@NotNull Payment payment) throws PaymentException {
        log.info("Payment internal transaction started with ID: {}", payment.getPaymentTransactionId());
        String cardId = payment.getCard().getCardId();
        String paymentTransactionId = payment.getPaymentTransactionId();
        User user = getUserWithCard(payment, cardId);
        BankCard bankCard = user.getCards().stream().filter(card -> Objects.equals(cardId, card.getCardId())).findFirst().get();
        log.info("User found with bank card");
        checkCardIsValid(bankCard, paymentTransactionId);
        checkCurrencyMatch(payment, bankCard);
        checkAmount(payment, bankCard);
        log.info("Executing payment: {}", paymentTransactionId);
        return executePaymentTransaction(payment, user, bankCard);
    }

    private User getUserWithCard(Payment payment, String cardId) throws PaymentException {
        return userGateway.getUserWithBankCardByCardId(payment.getUser().getId(), cardId)
                .orElseThrow(() -> new PaymentException(USER_CARD_RELATION_MISMATCH, payment.getPaymentTransactionId()));
    }

    private void checkCardIsValid(BankCard bankCard, String paymentTransactionId) throws PaymentException {
        ZonedDateTime sysdateMonth = truncateToStarOfMonth(Util.sysdate());
        ZonedDateTime cardExpirationMonth = truncateToStarOfMonth(bankCard.getValidTo());
        if (sysdateMonth.isAfter(cardExpirationMonth)) {
            throw new PaymentException(CARD_EXPIRED, paymentTransactionId);
        }
    }

    private ZonedDateTime truncateToStarOfMonth(ZonedDateTime time) {
        return time.with(ChronoField.DAY_OF_MONTH, 1L).truncatedTo(ChronoUnit.DAYS);
    }

    private void checkCurrencyMatch(Payment payment, BankCard bankCard) throws PaymentException {
        if (bankCard.getCurrency() != payment.getCurrency()) {
            throw new PaymentException(CURRENCY_MISMATCH, payment.getPaymentTransactionId());
        }
    }

    private Payment executePaymentTransaction(Payment payment, User user, BankCard bankCard) throws PaymentException {
        Payment newPayment = payment.toBuilder().transactionTime(Util.sysdate()).build();
        PaymentLock lock = lockService.lock(user.getId(), bankCard.getCardId(), newPayment.getPaymentTransactionId());
        Long originalAmount = bankCard.getAmount();
        Payment savedPayment;
        try {
            long newAmount = bankCard.getAmount() - newPayment.getAmount();
            bankCard.setAmount(newAmount);
            newPayment.setTransactionTime(lock.getCreatedAt());
            bankCardGateway.save(bankCard);
            savedPayment = paymentRepository.save(newPayment);
        } catch (Exception ex) {
            // on a transaction rollback the DB data will be unchanged
            // but the fetched entity would have been modified which is inconsistent
            bankCard.setAmount(originalAmount);
            throw new PaymentException(SERVER_ERROR, newPayment.getPaymentTransactionId(), ex);
        } finally {
            lockService.unlock(lock);
        }
        return savedPayment;
    }

    private void checkAmount(Payment payment, BankCard bankCard) throws PaymentException {
        if (bankCard.getAmount() - payment.getAmount() < 0) {
            throw new PaymentException(CARD_LIMIT_EXCEEDS, payment.getPaymentTransactionId());
        }
    }
}