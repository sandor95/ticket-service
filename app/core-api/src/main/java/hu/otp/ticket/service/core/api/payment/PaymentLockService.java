package hu.otp.ticket.service.core.api.payment;

import hu.otp.ticket.service.core.api.model.PaymentLock;

public interface PaymentLockService {

    PaymentLock lock(Long userId, String cardId, String paymentTransactionId);

    void unlock(PaymentLock lock);
}