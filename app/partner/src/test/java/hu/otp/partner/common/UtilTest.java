package hu.otp.partner.common;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import hu.otp.partner.knownobject.Dates;
import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void shouldGenerateRandomUUID() {
        assertNotNull(Util.randomUuid());
    }

    @Test
    void shouldGenerateReservationId() {
        Long firstId = Util.nextReservationId();
        Long secondId = Util.nextReservationId();

        assertTrue(firstId > 1);
        assertTrue(secondId > 3);
        assertTrue(firstId < secondId);
    }

    @Test
    void shouldThrowExceptionWhenConstructorCalled() {
        assertThrows(UnsupportedOperationException.class, Util::new);

        LocalDateTime dateTime = LocalDateTime.of(2024, 5, 9, 20, 0, 0);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, Dates.BUDAPEST_ZONE_ID);
        System.out.println(dateTime + " in epoch seconds: " + zonedDateTime.toEpochSecond());
    }
}