package hu.otp.ticket.service.core.api.payment.exception;

import lombok.Getter;

@Getter
public class PaymentException extends Exception {

    private final PaymentError paymentError;

    private final String paymentTransactionId;

    public PaymentException(PaymentError paymentError) {
        this(paymentError, null);
    }

    public PaymentException(PaymentError paymentError, String paymentTransactionId) {
        this.paymentError = paymentError;
        this.paymentTransactionId = paymentTransactionId;
    }

    public PaymentException(PaymentError paymentError, String paymentTransactionId, Exception exception) {
        super(exception);
        this.paymentError = paymentError;
        this.paymentTransactionId = paymentTransactionId;
    }
}