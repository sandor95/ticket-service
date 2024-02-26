package hu.otp.ticket.service.core.api.payment;

import hu.otp.ticket.service.core.api.model.BankCard;
import hu.otp.ticket.service.core.api.model.Payment;
import hu.otp.ticket.service.core.api.model.User;
import hu.otp.ticket.service.core.api.payment.exception.PaymentError;
import hu.otp.ticket.service.core.api.payment.exception.PaymentException;
import hu.otp.ticket.service.core.api.payment.model.PaymentRequestDTO;
import hu.otp.ticket.service.core.api.payment.model.PaymentResponseDTO;
import hu.otp.ticket.service.core.api.persistentlayer.BankCardGateway;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class PaymentConverter {

    private final UserGateway userGateway;

    private final BankCardGateway bankCardGateway;

    @Transactional(readOnly = true)
    public Payment convertToEntity(PaymentRequestDTO dto) throws PaymentException {
        return Payment.builder()
                    .user(getUser(dto))
                    .paymentTransactionId(dto.paymentTransactionId())
                    .card(getCard(dto))
                    .amount(dto.amount())
                    .currency(dto.currency())
                    .build();
    }

    private User getUser(PaymentRequestDTO dto) throws PaymentException {
        return userGateway.getUserById(dto.userId())
                        .orElseThrow(() -> new PaymentException(PaymentError.USER_CARD_RELATION_MISMATCH));
    }

    private BankCard getCard(PaymentRequestDTO dto) throws PaymentException {
        return bankCardGateway.getByCardId(dto.cardId())
                        .orElseThrow(() -> new PaymentException(PaymentError.USER_CARD_RELATION_MISMATCH));
    }

    public PaymentResponseDTO convertToDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                                .paymentTransactionId(payment.getPaymentTransactionId())
                                .success(payment.getErrorCode() == null)
                                .errorCode(payment.getErrorCode())
                                .build();
    }
}