package hu.otp.ticket.service.ticket.api.persistentlayer;

import java.util.Optional;

import hu.otp.ticket.service.ticket.api.model.ReservationLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationLockRepository extends JpaRepository<ReservationLock, Long> {

    Optional<ReservationLock> findByUserIdAndEventId(Long userId, Long eventId);
}