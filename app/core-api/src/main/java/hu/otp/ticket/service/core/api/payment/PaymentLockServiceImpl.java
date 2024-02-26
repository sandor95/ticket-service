package hu.otp.ticket.service.core.api.payment;

import java.util.Optional;

import hu.otp.ticket.service.core.api.model.PaymentLock;
import hu.otp.ticket.service.core.api.persistentlayer.PaymentLockRepository;
import hu.otp.ticket.service.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PaymentLockServiceImpl implements PaymentLockService {

    private final PaymentLockRepository lockRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public PaymentLock lock(Long userId, String cardId, String paymentTransactionId) {
        PaymentLock paymentLock = createLock(userId, cardId, paymentTransactionId);
        log.debug("Locking for user id: {}, cardId: {}", userId, cardId);
        return lockRepository.save(paymentLock);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void unlock(PaymentLock paymentLock) {
        Optional<PaymentLock> lock = lockRepository.findByUserIdAndCardIdAndTransactionId(paymentLock.getUserId(),
                                                                                            paymentLock.getCardId(),
                                                                                            paymentLock.getTransactionId());
        lock.ifPresent(lockRepository::delete);
        log.debug("Unlocking for user id: {}, cardId: {}", paymentLock.getUserId(), paymentLock.getCardId());
    }

    private PaymentLock createLock(Long userId, String cardId, String paymentTransactionId) {
        return PaymentLock.builder()
                        .cardId(cardId)
                        .userId(userId)
                        .transactionId(paymentTransactionId)
                        .createdAt(Util.sysdate())
                        .build();
    }
}