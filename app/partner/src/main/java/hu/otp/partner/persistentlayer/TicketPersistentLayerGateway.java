package hu.otp.partner.persistentlayer;

import java.util.Optional;

import hu.otp.partner.common.model.Ticket;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class TicketPersistentLayerGateway {

    private final TicketRepository ticketRepository;

    public Optional<Ticket> getTicketByEventIdAndSeatCode(@NotNull Long eventId, @NotNull String seatcode) {
        return ticketRepository.findByEventIdAndSeatId(eventId, seatcode);
    }

    public boolean isReservationIdAlreadyExists(Long reservationId) {
        return ticketRepository.existsByReservationId(reservationId);
    }

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }
}