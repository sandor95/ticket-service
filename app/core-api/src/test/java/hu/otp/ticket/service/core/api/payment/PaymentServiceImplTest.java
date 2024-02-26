package hu.otp.ticket.service.core.api.payment;

import static hu.otp.ticket.service.core.api.payment.exception.PaymentError.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import hu.otp.ticket.service.core.api.model.BankCard;
import hu.otp.ticket.service.core.api.model.Payment;
import hu.otp.ticket.service.core.api.model.PaymentLock;
import hu.otp.ticket.service.core.api.model.User;
import hu.otp.ticket.service.core.api.payment.exception.PaymentException;
import hu.otp.ticket.service.core.api.persistentlayer.BankCardGateway;
import hu.otp.ticket.service.core.api.persistentlayer.PaymentRepository;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import hu.otp.ticket.service.model.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    static final ZonedDateTime SYSDATE = ZonedDateTime.now().with(ChronoField.DAY_OF_MONTH, 5L);

    static final PaymentLock LOCK = PaymentLock.builder().createdAt(SYSDATE).build();

    static final String PAYMENT_TRANSACTION_ID = "abc-123";

    static final long USER_ID = 34648345234L;

    static final String CARD_ID = "cardId12";
    public static final long CARD_AMOUNT = 12_000L;

    @Mock
    UserGateway userGateway;

    @Mock
    BankCardGateway bankCardGateway;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    PaymentLockService lockService;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    void shouldPayWhenPaymentIsValid() throws PaymentException {
        // given
        Payment expectedPayment = createPayment(USER_ID, CARD_ID, 2_000L, true);
        User user = expectedPayment.getUser();
        BankCard bankCard = expectedPayment.getCard();
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        when(userGateway.getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID))).thenReturn(Optional.of(user));
        when(lockService.lock(eq(USER_ID), eq(CARD_ID), eq(PAYMENT_TRANSACTION_ID))).thenReturn(LOCK);
        when(paymentRepository.save(any())).thenReturn(expectedPayment);

        // when
        Payment actualPayment = paymentService.pay(expectedPayment);

        // then
        assertNotNull(actualPayment);
        assertEquals(10_000L, bankCard.getAmount());

        verify(userGateway, times(1)).getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID));
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        verify(bankCardGateway, times(1)).save(eq(bankCard));
        verify(lockService, times(1)).unlock(eq(LOCK));
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(lockService);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoMoreInteractions(bankCardGateway);

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(user, savedPayment.getUser());
        assertEquals(bankCard, savedPayment.getCard());
        assertEquals(PAYMENT_TRANSACTION_ID, savedPayment.getPaymentTransactionId());
        long sysdateDiff = ChronoUnit.SECONDS.between(SYSDATE, savedPayment.getTransactionTime());
        assertTrue(sysdateDiff < 2);
    }

    @Test
    void shouldThrowExceptionWhenCardIsInvalid() {
        // given
        Payment payment = createPayment(USER_ID, CARD_ID, 2_000L, false);
        User user = payment.getUser();
        BankCard bankCard = payment.getCard();

        when(userGateway.getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID))).thenReturn(Optional.of(user));

        // when
        PaymentException exception = assertThrows(PaymentException.class, () -> paymentService.pay(payment));

        // then
        assertEquals(CARD_EXPIRED, exception.getPaymentError());
        assertEquals(CARD_AMOUNT, bankCard.getAmount());

        verify(userGateway, times(1)).getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID));
        verifyNoMoreInteractions(userGateway);
        verifyNoInteractions(lockService);
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(bankCardGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWithGivenId() {
        // given
        Long randomUserId = 1234L;
        Payment payment = createPayment(randomUserId, CARD_ID, 2_000L, true);
        BankCard bankCard = payment.getCard();

        when(userGateway.getUserWithBankCardByCardId(eq(randomUserId), eq(CARD_ID))).thenReturn(Optional.empty());

        // when
        PaymentException exception = assertThrows(PaymentException.class, () -> paymentService.pay(payment));

        // then
        assertEquals(USER_CARD_RELATION_MISMATCH, exception.getPaymentError());
        assertEquals(CARD_AMOUNT, bankCard.getAmount());

        verify(userGateway, times(1)).getUserWithBankCardByCardId(eq(randomUserId), eq(CARD_ID));
        verifyNoMoreInteractions(userGateway);
        verifyNoInteractions(lockService);
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(bankCardGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWithGivenCardId() {
        // given
        String randomCardId = "randomCardId";
        Payment payment = createPayment(USER_ID, randomCardId, 2_000L, true);
        BankCard bankCard = payment.getCard();

        when(userGateway.getUserWithBankCardByCardId(eq(USER_ID), eq(randomCardId))).thenReturn(Optional.empty());

        // when
        PaymentException exception = assertThrows(PaymentException.class, () -> paymentService.pay(payment));

        // then
        assertEquals(USER_CARD_RELATION_MISMATCH, exception.getPaymentError());
        assertEquals(CARD_AMOUNT, bankCard.getAmount());

        verify(userGateway, times(1)).getUserWithBankCardByCardId(eq(USER_ID), eq(randomCardId));
        verifyNoMoreInteractions(userGateway);
        verifyNoInteractions(lockService);
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(bankCardGateway);
    }

    @Test
    void shouldThrowExceptionWhenCurrencyNotMatches() {
        // given
        Payment payment = createPayment(USER_ID, CARD_ID, 2_000L, true);
        User user = payment.getUser();
        BankCard bankCard = payment.getCard();
        payment.setCurrency(null);

        when(userGateway.getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID))).thenReturn(Optional.of(user));

        // when
        PaymentException exception = assertThrows(PaymentException.class, () -> paymentService.pay(payment));

        // then
        assertEquals(CURRENCY_MISMATCH, exception.getPaymentError());
        assertEquals(CARD_AMOUNT, bankCard.getAmount());

        verify(userGateway, times(1)).getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID));
        verifyNoMoreInteractions(userGateway);
        verifyNoInteractions(lockService);
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(bankCardGateway);
    }

    @Test
    void shouldThrowExceptionWhenLimitExceeds() {
        // given
        Payment payment = createPayment(USER_ID, CARD_ID, 200_000L, true);
        User user = payment.getUser();
        BankCard bankCard = payment.getCard();

        when(userGateway.getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID))).thenReturn(Optional.of(user));

        // when
        PaymentException exception = assertThrows(PaymentException.class, () -> paymentService.pay(payment));

        // then
        assertEquals(CARD_LIMIT_EXCEEDS, exception.getPaymentError());
        assertEquals(CARD_AMOUNT, bankCard.getAmount());

        verify(userGateway, times(1)).getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID));
        verifyNoMoreInteractions(userGateway);
        verifyNoInteractions(lockService);
        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(bankCardGateway);
    }

    @Test
    void shouldThrowExceptionWhenServerErrorOccurred() {
        // given
        Payment payment = createPayment(USER_ID, CARD_ID, 2_000L, true);
        User user = payment.getUser();
        BankCard bankCard = payment.getCard();

        when(userGateway.getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID))).thenReturn(Optional.of(user));
        when(lockService.lock(eq(USER_ID), eq(CARD_ID), eq(PAYMENT_TRANSACTION_ID))).thenReturn(LOCK);
        doThrow(RuntimeException.class).when(bankCardGateway).save(any());

        // when
        PaymentException exception = assertThrows(PaymentException.class, () -> paymentService.pay(payment));

        // then
        assertEquals(SERVER_ERROR, exception.getPaymentError());
        assertEquals(CARD_AMOUNT, bankCard.getAmount());

        verify(userGateway, times(1)).getUserWithBankCardByCardId(eq(USER_ID), eq(CARD_ID));
        verify(lockService, times(1)).lock(eq(USER_ID), eq(CARD_ID), eq(PAYMENT_TRANSACTION_ID));
        verify(bankCardGateway, times(1)).save(eq(bankCard));
        verify(lockService, times(1)).unlock(eq(LOCK));
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(lockService);
        verifyNoInteractions(paymentRepository);
        verifyNoMoreInteractions(bankCardGateway);
    }

    private Payment createPayment(Long userId, String cardId, Long requestedAmount, boolean isCardValid) {
        User user = User.builder().id(userId).build();
        BankCard bankCard = BankCard.builder()
                .cardId(cardId)
                .amount(CARD_AMOUNT)
                .currency(Currency.HUF)
                .validTo(SYSDATE.with(ChronoField.DAY_OF_MONTH, 1L).plusMonths(isCardValid ? 1 : -1))
                .build();
        user.setCards(Set.of(bankCard));

        return Payment.builder()
                .user(user)
                .card(bankCard)
                .paymentTransactionId(PaymentServiceImplTest.PAYMENT_TRANSACTION_ID)
                .amount(requestedAmount)
                .currency(Currency.HUF)
                .build();
    }
}