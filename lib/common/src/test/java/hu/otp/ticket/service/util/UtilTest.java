package hu.otp.ticket.service.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.ZonedDateTime;

import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void shouldGenerateRandomUUID() {
        assertTrue(StringUtils.isNotBlank(Util.randomUuid()));
    }

    @Test
    void shouldGenerateReservationId() {
        Long firstId = Util.nextReservationId();
        Long secondId = Util.nextReservationId();

        assertEquals(2, firstId);
        assertEquals(4, secondId);
    }

    @Test
    void shouldThrowExceptionWhenConstructorCalled() {
        assertThrows(UnsupportedOperationException.class, Util::new);
    }

    @Test
    void shouldReturnActualTime() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime sysdate = Util.sysdate();

        assertNotNull(sysdate);
        assertTrue(Duration.between(now, sysdate).toMillis() < 400L);
    }
}