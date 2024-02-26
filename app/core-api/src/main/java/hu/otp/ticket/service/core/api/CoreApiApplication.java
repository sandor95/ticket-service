package hu.otp.ticket.service.core.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@OpenAPIDefinition(info = @Info(title = "OTP Mobil - Ticket Service CORE-API definition",
        description = "Ticket Service core module.",
        contact = @Contact(name = "OTP Mobil Kft.", email = "info@otpmobil.com"), version = "v1"))
@EnableTransactionManagement
@SpringBootApplication
public class CoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApiApplication.class, args);
    }
}