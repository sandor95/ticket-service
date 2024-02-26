package hu.otp.ticket.service.core.api.payment.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentError {
    USER_CARD_RELATION_MISMATCH(10100),
    CARD_LIMIT_EXCEEDS(10101),
    CARD_EXPIRED(10102),
    CARD_NOT_FOUND(10103),
    INVALID_PARAMETERS(10104),
    CURRENCY_MISMATCH(10105),
    SERVER_ERROR(10106);

    public final Integer errorCode;
}