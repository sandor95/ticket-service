package hu.otp.ticket.service.main.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@OpenAPIDefinition(info = @Info(title = "OTP Mobil - Ticket Service MAIN-API definition",
        description = "Ticket Service public api module.",
        contact = @Contact(name = "OTP Mobil Kft.", email = "info@otpmobil.com"), version = "v1"))
@ComponentScan("hu.otp.ticket.service.*")
@SpringBootApplication
public class MainApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApiApplication.class, args);
    }
}