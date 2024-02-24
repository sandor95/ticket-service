package hu.otp.partner.common;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Util {

    private  static final long RANDOM_BOUND = 10L;

    private static final Long RESERVATION_ID_BASE_STEP = 2L;

    private static final Random rnd = new Random();

    private static final AtomicLong reservationIdCounter = new AtomicLong(1L);

    Util() {
        throw new UnsupportedOperationException("Cannot initialize utility class");
    }

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static Long nextReservationId() {
        long delta = rnd.nextLong(RANDOM_BOUND) + RESERVATION_ID_BASE_STEP;
        return reservationIdCounter.addAndGet(delta);
    }

    public static ZonedDateTime sysdate() {
        return ZonedDateTime.now();
    }
}