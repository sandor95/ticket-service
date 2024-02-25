package hu.otp.ticket.service.core.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenException extends Exception {

    private final TokenError tokenError;
}