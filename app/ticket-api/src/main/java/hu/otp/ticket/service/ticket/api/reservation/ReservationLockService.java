package hu.otp.ticket.service.ticket.api.reservation;

import hu.otp.ticket.service.ticket.api.model.ReservationLock;

public interface ReservationLockService {

    ReservationLock lock(Long eventId, Long userId, String transactionId);

    void unlock(ReservationLock lock);
}