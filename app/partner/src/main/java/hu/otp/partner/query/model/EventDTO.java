package hu.otp.partner.query.model;

import lombok.Builder;

@Builder
public record EventDTO(Long eventId, String title, String location, Long startTimeStamp, Long endTimeStamp) {
}