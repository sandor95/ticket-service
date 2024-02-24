package hu.otp.partner.persistentlayer;

import java.util.Optional;

import hu.otp.partner.common.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
            select t from Ticket t
            join t.event e
            join fetch t.seat s
            where e.id = :eventId
            and s.seatCode = :seatCode
        """)
    Optional<Ticket> findByEventIdAndSeatId(Long eventId, String seatCode);

    boolean existsByReservationId(Long reservationId);
}