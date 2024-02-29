package hu.otp.ticket.service.main.api.common;

import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import hu.otp.ticket.service.coreapi.client.CoreApiClient;
import hu.otp.ticket.service.main.api.exception.TokenValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class TokenValidationServiceImpl implements TokenValidationService {

    private final CoreApiClient coreApiClient;

    @Override
    public void validateToken(Long userId, String token) throws TokenValidationException {
        TokenValidationResultDTO validationResultDTO = coreApiClient.validateToken(userId, token);
        if (validationResultDTO == null || !StringUtils.equalsIgnoreCase(validationResultDTO.getResult(), "SUCCESS")
                || validationResultDTO.getErrorCode() != null) {
            throw new TokenValidationException(validationResultDTO != null ? validationResultDTO.getErrorCode() : null);
        }
    }
}