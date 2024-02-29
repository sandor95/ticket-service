package hu.otp.ticket.service.ticket.api.reservation.exception;

import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationError {

    EVENT_NOT_EXISTS(20001),
    SEAT_NOT_EXISTS(20002),
    SEAT_RESERVED(20010),
    EVENT_STARTED(20011),
    PARTNER_NOT_AVAILABLE(20404),
    CARD_LIMIT(10101),
    CARD_EXPIRED(10102),
    UNKNOWN_ERROR(20999);

    public final Integer errorCode;

    private static final Map<ReservationError, Set<Integer>> CORE_API_ERRORS_MAPPING = Map.of(
            ReservationError.UNKNOWN_ERROR, Set.of(10100, 10103, 10104, 10105, 10106),
            ReservationError.CARD_LIMIT, Set.of(10101),
            ReservationError.CARD_EXPIRED, Set.of(10102)
    );

    public static ReservationError getErrorFromCoreApiError(Integer coreApiErrorCode) {
        if (coreApiErrorCode == null) {
            return UNKNOWN_ERROR;
        }
        return CORE_API_ERRORS_MAPPING.entrySet().stream()
                .filter(entry -> entry.getValue().contains(coreApiErrorCode))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }
}