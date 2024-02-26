package hu.otp.ticket.service.core.api.payment.model;

import lombok.Builder;

@Builder
public record PaymentResponseDTO(boolean success, Integer errorCode, String paymentTransactionId) {
}