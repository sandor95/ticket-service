package hu.otp.ticket.service.ticket.api.reservation.model;

import java.time.ZonedDateTime;

import lombok.Builder;

@Builder
public record ReservationResponseDTO(String transactionId,
                                     String eventName,
                                     String seatCode,
                                     ZonedDateTime startTime,
                                     ZonedDateTime endTime,
                                     boolean success,
                                     Integer errorCode) {
}