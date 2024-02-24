package hu.otp.partner.query.model;

import java.util.Collection;

import hu.otp.partner.common.model.DataDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventDataDTO extends DataDTO<Collection<EventDTO>> {

    public EventDataDTO(Collection<EventDTO> data, boolean success) {
        super(data, success);
    }
}