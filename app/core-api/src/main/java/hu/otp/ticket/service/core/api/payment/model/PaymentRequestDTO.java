package hu.otp.ticket.service.core.api.payment.model;

import hu.otp.ticket.service.model.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PaymentRequestDTO(Long userId, String cardId, Long amount,
                                @Schema(implementation = String.class) Currency currency,
                                String paymentTransactionId) {
}