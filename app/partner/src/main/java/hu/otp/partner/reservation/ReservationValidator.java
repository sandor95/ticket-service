package hu.otp.partner.reservation;

import static hu.otp.partner.reservation.exception.ReservationError.INVALID_EVENT_ID;
import static hu.otp.partner.reservation.exception.ReservationError.INVALID_SEAT_ID;

import hu.otp.partner.reservation.exception.ReservationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator {

    public void validateRequestParameters(String eventId, String seatId) throws ReservationException {
        checkEventId(eventId);
        checkSeatId(seatId);
    }

    private void checkEventId(String numAsString) throws ReservationException {
        if (!StringUtils.isNumeric(numAsString)) {
            throw new ReservationException(INVALID_EVENT_ID);
        }
        try {
            long id = Long.parseLong(numAsString);
            if (id < 1) {
                throw new ReservationException(INVALID_EVENT_ID);
            }
        } catch (NumberFormatException nfe) {
            throw new ReservationException(INVALID_EVENT_ID);
        }
    }

    private void checkSeatId(String seatId) throws ReservationException {
        if (StringUtils.isBlank(seatId)) {
            throw new ReservationException(INVALID_SEAT_ID);
        }
    }
}