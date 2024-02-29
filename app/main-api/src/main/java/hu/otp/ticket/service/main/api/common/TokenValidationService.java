package hu.otp.ticket.service.main.api.common;

import hu.otp.ticket.service.main.api.exception.TokenValidationException;

public interface TokenValidationService {

    void validateToken(Long userId, String token) throws TokenValidationException;
}