package hu.otp.partner.query;

import static hu.otp.partner.knownobject.Events.*;
import static hu.otp.partner.knownobject.Tickets.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.query.model.EventDTO;
import hu.otp.partner.query.model.EventDataDTO;
import hu.otp.partner.query.model.EventDetailsDataDTO;
import hu.otp.partner.query.model.SeatDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class EventConverterTest {

    final EventConverter converter = new EventConverter();

    @Test
    void shouldConvertEntityToDto() {
        EventDataDTO eventListDTO = converter.convertToEventListDTO(List.of(_30Y_CONCERT, BETON_HOFI_CONCERT));

        assertNotNull(eventListDTO);
        assertEquals(2, eventListDTO.getData().size());
        assertTrue(eventListDTO.isSuccess());

        EventDTO actual30Y = findEvent(eventListDTO, _30Y_CONCERT.getId());
        EventDTO actualBetonHofi = findEvent(eventListDTO, BETON_HOFI_CONCERT.getId());
        assertEquals(_30Y_CONCERT_DTO, actual30Y);
        assertEquals(BETON_HOFI_CONCERT_DTO, actualBetonHofi);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnEmptyDataWhenCalledWithNullOrEmptyList(List<Event> events) {
        EventDataDTO eventListDTO = converter.convertToEventListDTO(events);

        assertNotNull(eventListDTO);
        assertTrue(eventListDTO.isSuccess());
        assertTrue(eventListDTO.getData().isEmpty());
    }


    @Test
    void shouldConvertEntityToDetailsDto() {
        EventDetailsDataDTO detailsListDTO = converter.convertToEventDetailsListDTO(_30Y_CONCERT);

        assertNotNull(detailsListDTO);
        assertNotNull(detailsListDTO.getData());
        assertTrue(detailsListDTO.isSuccess());

        SeatDTO seat1 = findSeat(detailsListDTO, TICKET_S1_DTO.id());
        SeatDTO seat2 = findSeat(detailsListDTO, TICKET_S2_DTO.id());
        SeatDTO seat3 = findSeat(detailsListDTO, TICKET_S3_DTO.id());
        SeatDTO seat4 = findSeat(detailsListDTO, TICKET_S4_DTO.id());

        assertEquals(TICKET_S1_DTO, seat1);
        assertEquals(TICKET_S2_DTO, seat2);
        assertEquals(TICKET_S3_DTO, seat3);
        assertEquals(TICKET_S4_DTO, seat4);
    }

    @Test
    void shouldReturnNullDataWhenCalledWithNullEvent() {
        EventDetailsDataDTO detailsListDTO = converter.convertToEventDetailsListDTO(null);

        assertNotNull(detailsListDTO);
        assertTrue(detailsListDTO.isSuccess());
        assertNull(detailsListDTO.getData());
    }

    private EventDTO findEvent(EventDataDTO eventListDTO, Long eventId) {
        return eventListDTO.getData().stream()
                            .filter(e -> e.eventId().equals(eventId))
                            .findFirst()
                            .orElseThrow(() -> new AssertionError("Event not found with ID: " + eventId));
    }

    private SeatDTO findSeat(EventDetailsDataDTO detailsListDTO, String seatCode) {
        return detailsListDTO.getData().seats().stream()
                            .filter(seat -> seat.id().equals(seatCode))
                            .findFirst()
                            .orElseThrow(() -> new AssertionError("Seat not found with ID: " + seatCode));
    }
}