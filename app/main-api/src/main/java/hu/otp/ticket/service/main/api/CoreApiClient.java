package hu.otp.ticket.service.main.api;

import hu.otp.ticket.service.core.api.client.ApiClient;
import hu.otp.ticket.service.core.api.client.api.TokenValidationControllerApi;
import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoreApiClient {

    private final TokenValidationControllerApi tokenValidationApi;

    @Autowired
    public CoreApiClient(@Value("${core-api.url}") String coreApiUrl, @Value("${core-api.timeout:60000}") Long timeout) {
        ApiClient apiClient = new ApiClient()
                            .setBasePath(coreApiUrl)
                            .setWaitTimeMillis(timeout);
        tokenValidationApi = new TokenValidationControllerApi(apiClient);
    }

    public TokenValidationResultDTO validateToken(Long userId, String token) {
        return tokenValidationApi.validate(userId, token);
    }
}