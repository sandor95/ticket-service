package hu.otp.ticket.service.ticket.api.partnerquery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import hu.otp.ticket.service.partner.client.model.EventDTO;
import hu.otp.ticket.service.partner.client.model.EventDataDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDataDTO;
import org.junit.jupiter.api.Test;

class EventConverterTest {

    final EventConverter converter = new EventConverter();

    @Test
    void shouldExtractDataFromDTO() {
        long expectedEventId = 1L;
        EventDTO data = new EventDTO();
        data.setEventId(expectedEventId);
        EventDataDTO wrapper = new EventDataDTO();
        wrapper.setData(List.of(data));

        List<EventDTO> actualData = converter.convertToInternalDTO(wrapper);

        assertEquals(1, actualData.size());
        assertEquals(expectedEventId, actualData.get(0).getEventId());
    }

    @Test
    void shouldExtractDataFromDetailsDto() {
        long expectedEventId = 123L;
        EventDetailsDTO data = new EventDetailsDTO();
        data.setEventId(expectedEventId);
        EventDetailsDataDTO wrapper = new EventDetailsDataDTO();
        wrapper.setData(data);

        EventDetailsDTO actualData = converter.convertToInternalDTO(wrapper);

        assertNotNull(actualData);
        assertEquals(expectedEventId, actualData.getEventId());
    }
}