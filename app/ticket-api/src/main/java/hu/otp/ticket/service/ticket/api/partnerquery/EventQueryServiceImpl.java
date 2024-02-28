package hu.otp.ticket.service.ticket.api.partnerquery;

import hu.otp.ticket.service.partner.client.PartnerClient;
import hu.otp.ticket.service.partner.client.model.EventDataDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Service
public class EventQueryServiceImpl implements EventQueryService {

    private final PartnerClient partnerClient;

    @Override
    public EventDataDTO getAllEventsFromPartner() {
        log.debug("Querying Partner for events...");
        EventDataDTO events = partnerClient.getEvents();
        log.debug("All events query was successful!");
        log.trace("All events: {}", events);
        return events;
    }

    @Override
    public EventDetailsDataDTO getEvent(Long eventId) {
        log.debug("Querying Partner for event details... [id: {}]", eventId);
        EventDetailsDataDTO event = partnerClient.getEvent(eventId);
        log.debug("Event detail query was successful! [id: {}]", eventId);
        log.trace("Event details: {}", event);
        return event;
    }
}