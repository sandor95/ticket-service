package hu.otp.ticket.service.ticket.api.partnerquery;

import java.util.List;

import hu.otp.ticket.service.partner.client.model.EventDTO;
import hu.otp.ticket.service.partner.client.model.EventDataDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDataDTO;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

    public List<EventDTO> convertToInternalDTO(EventDataDTO eventDataDTO) {
        // NOTE: in an ideal world the partner's DTO would be mapped to an internal DTO :)
        return eventDataDTO.getData();
    }

    public EventDetailsDTO convertToInternalDTO(EventDetailsDataDTO eventDetailsDataDTO) {
        // NOTE: in an ideal world the partner's DTO would be mapped to an internal DTO :)
        return eventDetailsDataDTO.getData();
    }
}