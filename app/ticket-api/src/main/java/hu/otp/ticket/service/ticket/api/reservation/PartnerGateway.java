package hu.otp.ticket.service.ticket.api.reservation;

import java.util.List;
import java.util.Optional;

import hu.otp.ticket.service.partner.client.PartnerClient;
import hu.otp.ticket.service.partner.client.model.*;
import hu.otp.ticket.service.ticket.api.partnerquery.EventConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Component
public class PartnerGateway {

    private final PartnerClient partnerClient;

    private final EventConverter eventConverter;

    public Optional<List<EventDTO>> getAllEvents() {
        log.debug("Querying Partner for events...");
        EventDataDTO response = partnerClient.getEvents();
        Optional<List<EventDTO>> result = Optional.empty();
        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            result = Optional.ofNullable(eventConverter.convertToInternalDTO(response));
        }
        log.debug("All events query was successful!");
        return result;
    }

    public Optional<EventDetailsDTO> getEvent(Long eventId) {
        log.debug("Querying Partner for event details, ID: {}...", eventId);
        EventDetailsDataDTO response = partnerClient.getEvent(eventId);
        Optional<EventDetailsDTO> result = Optional.empty();
        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            result = Optional.ofNullable(eventConverter.convertToInternalDTO(response));
        }
        log.debug("Event query was successful!");
        return result;
    }

    public Long reserve(String eventId, String seatCode) {
        log.debug("Reservation sent to Partner's event: eventId{}, seatCode: {}", eventId, seatCode);
        ReservationDTO response = partnerClient.reserveSeat(eventId, seatCode);
        Long result = null;
        if (response != null && Boolean.TRUE.equals(response.getSuccess())) {
            result = response.getReservationId();
        }
        log.debug("Reservation result: {}, Partner's event: eventId{}, seatCode: {}", result != null, eventId, seatCode);
        return result;
    }
}