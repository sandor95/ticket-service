package hu.otp.ticket.service.ticket.api.partnerquery;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import hu.otp.ticket.service.partner.client.PartnerClient;
import hu.otp.ticket.service.partner.client.model.EventDataDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDataDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventQueryServiceImplTest {

    @Mock
    private PartnerClient partnerClient;

    @InjectMocks
    private EventQueryServiceImpl eventQueryService;

    @Test
    void shouldQueryAllEventsFromPartner() {
        EventDataDTO mockEventData = new EventDataDTO();
        when(partnerClient.getEvents()).thenReturn(mockEventData);

        EventDataDTO result = eventQueryService.getAllEventsFromPartner();

        assertSame(mockEventData, result);
    }

    @Test
    void shouldQueryEventDetailsFromPartner() {
        Long eventId = 1L;
        EventDetailsDataDTO mockEventDetailsData = new EventDetailsDataDTO();
        when(partnerClient.getEvent(eventId)).thenReturn(mockEventDetailsData);

        EventDetailsDataDTO result = eventQueryService.getEvent(eventId);

        assertSame(mockEventDetailsData, result);
    }
}