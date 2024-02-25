package hu.otp.ticket.service.core.api.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenError {
    EMPTY(10050),
    EXPIRED_OR_INVALID(10051);

    public final Integer errorCode;
}