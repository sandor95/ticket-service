package hu.otp.ticket.service.ticket.api.persistentlayer;

import hu.otp.ticket.service.ticket.api.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}