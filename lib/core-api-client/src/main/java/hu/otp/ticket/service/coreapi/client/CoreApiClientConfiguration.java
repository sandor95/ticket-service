package hu.otp.ticket.service.coreapi.client;

import hu.otp.ticket.service.core.api.client.ApiClient;
import hu.otp.ticket.service.core.api.client.api.TokenValidationControllerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreApiClientConfiguration {

    @Bean
    public TokenValidationControllerApi tokenValidationApi(@Value("${core-api.url}") String coreApiUrl,
                                                           @Value("${core-api.timeout:60000}") Long timeout) {
        ApiClient apiClient = new ApiClient()
                            .setBasePath(coreApiUrl)
                            .setWaitTimeMillis(timeout);
        return new TokenValidationControllerApi(apiClient);
    }
}