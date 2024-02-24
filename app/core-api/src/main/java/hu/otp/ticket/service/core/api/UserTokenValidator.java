package hu.otp.ticket.service.core.api;

public interface UserTokenValidator {

    void validate(String token) throws TokenException;
}