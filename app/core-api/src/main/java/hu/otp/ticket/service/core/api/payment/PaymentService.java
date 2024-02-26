package hu.otp.ticket.service.core.api.payment;

import hu.otp.ticket.service.core.api.model.Payment;
import hu.otp.ticket.service.core.api.payment.exception.PaymentException;

public interface PaymentService {

    Payment pay(Payment payment) throws PaymentException;
}