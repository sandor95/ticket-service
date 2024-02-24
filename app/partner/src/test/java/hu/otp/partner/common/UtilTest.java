package hu.otp.partner.common;

import static hu.otp.ticket.service.Const.BUDAPEST_ZONE_ID;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, BUDAPEST_ZONE_ID);
        System.out.println(dateTime + " in epoch seconds: " + zonedDateTime.toEpochSecond());
    }
}