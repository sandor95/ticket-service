package hu.otp.ticket.service.main.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class ServiceTest {

    @Test
    void shouldDoNothing() {
        Service service = new Service();
        ZonedDateTime zonedDateTime = service.dummy();
        assertNotNull(zonedDateTime);
    }
}