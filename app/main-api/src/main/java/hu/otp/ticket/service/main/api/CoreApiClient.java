package hu.otp.ticket.service.main.api;

import hu.otp.ticket.service.core.api.client.api.TokenValidationControllerApi;
import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class CoreApiClient {

    private final TokenValidationControllerApi tokenValidationApi;

    public TokenValidationResultDTO validateToken(Long userId, String token) {
        return tokenValidationApi.validate(userId, token);
    }
}