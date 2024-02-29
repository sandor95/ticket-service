package hu.otp.ticket.service.ticket.api.reservation;

import hu.otp.ticket.service.ticket.api.model.ReservationLock;
import hu.otp.ticket.service.ticket.api.persistentlayer.ReservationLockRepository;
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
public class ReservationLockServiceImpl implements ReservationLockService {

    private final ReservationLockRepository lockRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public ReservationLock lock(Long eventId, Long userId, String transactionId) {
        ReservationLock lock = createLock(eventId, userId, transactionId);
        log.debug("Locking for user id: {}, eventId: {}", userId, eventId);
        return lockRepository.save(lock);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void unlock(ReservationLock lock) {
        lockRepository.findByUserIdAndEventId(lock.getUserId(), lock.getEventId()).ifPresent(lockRepository::delete);
        log.debug("Unlocking for user id: {}, eventId: {}", lock.getUserId(), lock.getEventId());
    }

    private ReservationLock createLock(Long eventId, Long userId, String transactionId) {
        return ReservationLock.builder()
                            .eventId(eventId)
                            .userId(userId)
                            .transactionId(transactionId)
                            .createdAt(Util.sysdate())
                            .build();
    }
}