package hu.otp.partner.query;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.common.model.Ticket;
import hu.otp.partner.query.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    public EventDataDTO convertToEventListDTO(List<Event> events) {
        Set<EventDTO> eventDTOS;
        if (CollectionUtils.isEmpty(events)) {
            eventDTOS = Collections.emptySet();
        } else {
            eventDTOS = events.stream()
                    .map(this::convertEventToDTO)
                    .collect(Collectors.toUnmodifiableSet());
        }
        return new EventDataDTO(eventDTOS, true);
    }

    public EventDetailsDataDTO convertToEventDetailsListDTO(Event event) {
        EventDetailsDTO data = event != null ? convertEventToDetailsDTO(event) : null;
        return new EventDetailsDataDTO(data, true);
    }

    private EventDetailsDTO convertEventToDetailsDTO(Event event) {
        return EventDetailsDTO.builder()
                            .eventId(event.getId())
                            .seats(extractSeats(event))
                            .build();
    }

    private Collection<SeatDTO> extractSeats(Event event) {
        return event.getTickets().stream()
                                .map(this::extractSeat)
                                .collect(Collectors.toUnmodifiableSet());
    }

    private SeatDTO extractSeat(Ticket ticket) {
        return SeatDTO.builder()
                    .id(ticket.getSeat().getSeatCode())
                    .price(ticket.getPrice())
                    .currency(ticket.getCurrency())
                    .reserved(ticket.getReservationId() != null)
                    .build();
    }

    private EventDTO convertEventToDTO(Event event) {
        return EventDTO.builder()
                        .eventId(event.getId())
                        .title(event.getTitle())
                        .location(event.getLocation())
                        .startTimeStamp(event.getStartTime().toInstant().toEpochMilli())
                        .endTimeStamp(event.getEndTime().toInstant().toEpochMilli())
                        .build();
    }
}