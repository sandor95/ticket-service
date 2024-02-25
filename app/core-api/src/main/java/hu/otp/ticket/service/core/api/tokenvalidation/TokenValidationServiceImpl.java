package hu.otp.ticket.service.core.api.tokenvalidation;

import static hu.otp.ticket.service.core.api.tokenvalidation.model.ValidationResult.FAILURE;
import static hu.otp.ticket.service.core.api.tokenvalidation.model.ValidationResult.SUCCESS;

import hu.otp.ticket.service.core.api.exception.TokenException;
import hu.otp.ticket.service.core.api.tokenvalidation.model.TokenValidationResultDTO;
import hu.otp.ticket.service.core.api.tokenvalidation.validator.UserTokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TokenValidationServiceImpl implements TokenValidationService {

    private final UserTokenValidator tokenValidator;

    @Autowired
    public TokenValidationServiceImpl(@Qualifier("tokenValidator") UserTokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    @Transactional(readOnly = true)
    public TokenValidationResultDTO validate(Long userId, String token) {
        log.info("Token validation started for token: {}", token);
        TokenValidationResultDTO result = validateToken(userId, token);
        log.info("Token validation finished with result: {}", result.result());
        return result;
    }

    private TokenValidationResultDTO validateToken(Long userId, String token) {
        TokenValidationResultDTO result;
        try {
            tokenValidator.validate(userId, token, null);
            result = buildSuccessResponse();
        } catch (TokenException e) {
            log.warn("Token validation failed: {}", e.getTokenError(), e);
            result = buildFailureResponse(e);
        }
        return result;
    }

    private TokenValidationResultDTO buildSuccessResponse() {
        return TokenValidationResultDTO.builder()
                                    .result(SUCCESS)
                                    .build();
    }

    private TokenValidationResultDTO buildFailureResponse(TokenException exception) {
        return TokenValidationResultDTO.builder()
                                    .result(FAILURE)
                                    .errorCode(exception.getTokenError().errorCode)
                                    .build();
    }
}