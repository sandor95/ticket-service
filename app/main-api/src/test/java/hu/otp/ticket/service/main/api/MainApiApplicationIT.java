package hu.otp.ticket.service.main.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootTest(classes = MainApiApplicationIT.class)
class MainApiApplicationIT {

    @Autowired
    ApplicationContext context;

    @Test
    void shouldStartContext() {
        assertNotNull(context);
    }
}