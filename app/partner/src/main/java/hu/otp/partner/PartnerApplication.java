package hu.otp.partner;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@OpenAPIDefinition(info = @Info(title = "PARTNER definition",
        description = "A hypothetical application that provides information about events and allows users to book tickets.",
        contact = @Contact(name = "Partner Kft.", email = "info@partner.hu"), version = "v1"))
@EnableTransactionManagement
@SpringBootApplication
public class PartnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartnerApplication.class, args);
    }
}