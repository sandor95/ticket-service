package hu.otp.ticket.service.core.api.tokenvalidation;

import hu.otp.ticket.service.core.api.tokenvalidation.model.TokenValidationResultDTO;

public interface TokenValidationService {

    TokenValidationResultDTO validate(Long userId, String token);
}