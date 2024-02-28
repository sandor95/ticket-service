package hu.otp.ticket.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class DummyTest {

    @Test
    void shouldTestDummy() {
        Dummy dummy = new Dummy();
        assertNotNull(dummy.dummy());
    }
}