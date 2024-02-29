package hu.otp.ticket.service.main.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenValidationException extends Exception {

    private final Integer errorCode;

    @Override
    public String getMessage() {
        return "Error code: [" + errorCode + "]";
    }
}