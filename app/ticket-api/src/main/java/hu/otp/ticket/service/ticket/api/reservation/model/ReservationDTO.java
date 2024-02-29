package hu.otp.ticket.service.ticket.api.reservation.model;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReservationDTO {

    private Long userId;

    private String cardId;

    private Long eventId;

    private String eventName;

    private ZonedDateTime startTime;

    private String seatCode;

    private Long amount;

    private String currency;

    private String transactionId;

    private Long reservationId;

    private String token;
}