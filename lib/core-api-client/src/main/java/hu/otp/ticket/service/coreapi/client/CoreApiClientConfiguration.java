package hu.otp.ticket.service.coreapi.client;

import hu.otp.ticket.service.core.api.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreApiClientConfiguration {

    @Bean
    public ApiClient coreApiConfigClient(@Value("${core-api.url}") String coreApiUrl,
                                         @Value("${core-api.timeout:60000}") Long timeout) {
        return new ApiClient()
                .setBasePath(coreApiUrl)
                .setWaitTimeMillis(timeout);
    }
}