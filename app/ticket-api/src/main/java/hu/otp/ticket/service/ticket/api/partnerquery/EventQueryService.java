package hu.otp.ticket.service.ticket.api.partnerquery;

import hu.otp.ticket.service.partner.client.model.EventDataDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDataDTO;

public interface EventQueryService {

    EventDataDTO getAllEventsFromPartner();

    EventDetailsDataDTO getEvent(Long eventId);
}