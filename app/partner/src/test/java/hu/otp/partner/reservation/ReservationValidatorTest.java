package hu.otp.partner.reservation;

import static hu.otp.partner.reservation.exception.ReservationError.INVALID_EVENT_ID;
import static hu.otp.partner.reservation.exception.ReservationError.INVALID_SEAT_ID;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import hu.otp.partner.reservation.exception.ReservationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ReservationValidatorTest {

    final ReservationValidator validator = new ReservationValidator();

    public static Stream<Arguments> validEventAndSeatIDs() {
        return Stream.of(
                Arguments.of("12", "S21"),
                Arguments.of("1", "2"),
                Arguments.of("123456789", "2"),
                Arguments.of(String.valueOf(Long.MAX_VALUE), "Q1")
        );
    }

    public static Stream<Arguments> invalidLongIDs() {
        return Stream.of(
                Arguments.of("-1"),
                Arguments.of("this is not an ID"),
                Arguments.of("0"),
                Arguments.of("1.1"),
                Arguments.of("1,1"),
                Arguments.of("111111111111111111111111111111")
        );
    }

    @ParameterizedTest
    @MethodSource("validEventAndSeatIDs")
    void shouldPassWithValidIDs(String eventId, String seatId) {
        assertDoesNotThrow(() -> validator.validateRequestParameters(eventId, seatId));
    }

    @ParameterizedTest
    @MethodSource("invalidLongIDs")
    void shouldThrowExceptionWhenEventIdIsInvalid(String eventId) {
        ReservationException exception = assertThrows(ReservationException.class, () -> validator.validateRequestParameters(eventId, "1"));
        assertEquals(INVALID_EVENT_ID, exception.getReservationError());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenSeatIdIsInvalid(String seatId) {
        ReservationException exception = assertThrows(ReservationException.class, () -> validator.validateRequestParameters("1", seatId));
        assertEquals(INVALID_SEAT_ID, exception.getReservationError());
    }
}