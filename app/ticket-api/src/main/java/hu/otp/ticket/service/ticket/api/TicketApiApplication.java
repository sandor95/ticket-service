package hu.otp.ticket.service.ticket.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@OpenAPIDefinition(info = @Info(title = "OTP Mobil - Ticket Service TICKET-API definition",
        description = "Ticket Service ticket module.",
        contact = @Contact(name = "OTP Mobil Kft.", email = "info@otpmobil.com"), version = "v1"))
@EnableRabbit
@EnableTransactionManagement
@ComponentScan("hu.otp.ticket.service.*")
@SpringBootApplication
public class TicketApiApplication {

    public static final String APP_NAME = "TICKET-API";

    public static void main(String[] args) {
        SpringApplication.run(TicketApiApplication.class, args);
    }
}