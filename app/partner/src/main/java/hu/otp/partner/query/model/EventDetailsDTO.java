package hu.otp.partner.query.model;

import java.util.Collection;

import lombok.Builder;
import lombok.ToString;

@Builder
public record EventDetailsDTO(Long eventId, Collection<SeatDTO> seats) {
}