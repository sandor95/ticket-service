package hu.otp.ticket.service.core.api.persistentlayer;

import java.util.Optional;

import hu.otp.ticket.service.core.api.model.PaymentLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLockRepository extends JpaRepository<PaymentLock, Long> {

    Optional<PaymentLock> findByUserIdAndCardIdAndTransactionId(Long userId, String cardId, String transactionId);
}