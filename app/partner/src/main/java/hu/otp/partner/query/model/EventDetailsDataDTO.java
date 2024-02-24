package hu.otp.partner.query.model;

import hu.otp.partner.common.model.DataDTO;
import lombok.Getter;

@Getter
public class EventDetailsDataDTO extends DataDTO<EventDetailsDTO> {

    public EventDetailsDataDTO(EventDetailsDTO data, boolean success) {
        super(data, success);
    }
}