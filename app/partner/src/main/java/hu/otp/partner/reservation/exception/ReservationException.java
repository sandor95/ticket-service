package hu.otp.partner.reservation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReservationException extends Exception {

    private final ReservationError reservationError;

    @Override
    public String getMessage() {
        return String.format("%s - %s", reservationError, reservationError.errorCode);
    }
}