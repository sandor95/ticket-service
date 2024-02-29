package hu.otp.ticket.service.ticketapi.client;

import hu.otp.ticket.service.ticket.api.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketApiClientConfiguration {

    @Bean
    public ApiClient coreApiConfigClient(@Value("${ticket-api.url}") String coreApiUrl,
                                         @Value("${ticket-api.timeout:60000}") Long timeout) {
        return new ApiClient()
                .setBasePath(coreApiUrl)
                .setWaitTimeMillis(timeout);
    }
}