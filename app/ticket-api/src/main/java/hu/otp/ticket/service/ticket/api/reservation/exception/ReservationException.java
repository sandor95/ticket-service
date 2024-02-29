package hu.otp.ticket.service.ticket.api.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationException extends Exception {

    private final ReservationError reservationError;

    private final String transactionId;
}