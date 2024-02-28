package hu.otp.ticket.service.partner.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PartnerApiClientConfiguration {

    @Bean
    public ApiClient partnerApiConfigClient(@Value("${partner-api.url}") String coreApiUrl,
                                         @Value("${partner-api.timeout:60000}") Long timeout) {
        return new ApiClient()
                .setBasePath(coreApiUrl)
                .setWaitTimeMillis(timeout);
    }
}