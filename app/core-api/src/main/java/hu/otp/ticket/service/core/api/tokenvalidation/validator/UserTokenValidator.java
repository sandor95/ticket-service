package hu.otp.ticket.service.core.api.tokenvalidation.validator;

import hu.otp.ticket.service.core.api.exception.TokenException;

public interface UserTokenValidator {

    void validate(Long assumedUserId, String encodedToken, String decodedToken) throws TokenException;
}