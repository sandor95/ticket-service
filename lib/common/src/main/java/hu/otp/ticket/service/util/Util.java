package hu.otp.ticket.service.util;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Util {

    private static final Long RESERVATION_ID_BASE_STEP = 2L;

    private static final AtomicLong reservationIdCounter = new AtomicLong(0L);

    Util() {
        throw new UnsupportedOperationException("Cannot initialize utility class");
    }

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static Long nextReservationId() {
        return reservationIdCounter.addAndGet(RESERVATION_ID_BASE_STEP);
    }

    public static ZonedDateTime sysdate() {
        return ZonedDateTime.now();
    }
}