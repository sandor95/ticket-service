package hu.otp.partner.reservation;

import static hu.otp.partner.reservation.exception.ReservationError.*;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.common.model.Ticket;
import hu.otp.partner.persistentlayer.EventPersistentLayerGateway;
import hu.otp.partner.persistentlayer.TicketPersistentLayerGateway;
import hu.otp.partner.reservation.exception.ReservationException;
import hu.otp.ticket.service.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ReservationService {

    private final TicketPersistentLayerGateway ticketGateway;

    private final EventPersistentLayerGateway eventGateway;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Long reserveSeat(Long eventId, String seatCode) throws ReservationException {
        checkEvent(eventId);
        Ticket ticket = ticketGateway.getTicketByEventIdAndSeatCode(eventId, seatCode)
                        .orElseThrow(() -> new ReservationException(INVALID_SEAT_ID));
        checkSeatIsNotReserved(ticket);
        log.info("Seat reservation started for event id: {}, ticket id: {} and seat code: {}", eventId, ticket.getId(), seatCode);
        Long reservationId = generateReservationId();
        log.debug("Successful reservation ID generation ({}) for event: {} ", reservationId, eventId);
        ticket.setReservationId(reservationId);
        ticketGateway.saveTicket(ticket);
        // NOTE: itt lehetne még naplózni, de csúnya lenne a partner naplóját becsatornázni a közösbe
        log.info("Successful ticket reservation! Reservation ID: {}", reservationId);
        return reservationId;
    }

    private void checkEvent(Long eventId) throws ReservationException {
        Event event = eventGateway.getById(eventId).orElseThrow(() -> new ReservationException(INVALID_EVENT_ID));
        if (Util.sysdate().isAfter(event.getEndTime())) {
            throw new ReservationException(EVENT_ALREADY_FINISHED);
        }
    }

    private void checkSeatIsNotReserved(Ticket ticket) throws ReservationException {
        if (ticket.getReservationId() != null) {
            throw new ReservationException(SEAT_RESERVED);
        }
    }

    private Long generateReservationId() {
        // NOTE: ez elég fapad módja a unique reservationId generálásnak, de ez a rendszer csak szimuláció :)
        Long reservationId = Util.nextReservationId();
        while (ticketGateway.isReservationIdAlreadyExists(reservationId)) {
            reservationId = Util.nextReservationId();
        }
        return reservationId;
    }
}