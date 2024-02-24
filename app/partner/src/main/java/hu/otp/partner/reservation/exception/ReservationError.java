package hu.otp.partner.reservation.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationError {

    INVALID_EVENT_ID(90001),
    INVALID_SEAT_ID(90002),
    SEAT_RESERVED(90010),
    EVENT_ALREADY_FINISHED(90090);

    public final Integer errorCode;

}