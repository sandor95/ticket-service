package hu.otp.ticket.service.core.api.tokenvalidation.validator;

import static hu.otp.ticket.service.core.api.exception.TokenError.EXPIRED_OR_INVALID;

import hu.otp.ticket.service.core.api.exception.TokenException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component("tokenFormalValidator")
class UserTokenFormalValidator implements UserTokenValidator {

    @Override
    public void validate(Long assumedUserId, String encodedToken, String decodedToken) throws TokenException {
        String[] values = StringUtils.split(decodedToken, UserTokenValidatorImpl.SEPARATOR);
        formalValidation(values);
    }

    private void formalValidation(String[] values) throws TokenException {
        checkNumberOfDataBlocks(values);
        validateEmail(values[UserTokenValidatorImpl.INDEX_OF_EMAIL]);
        validateUserId(values[UserTokenValidatorImpl.INDEX_OF_USER_ID]);
        validateDeviceHash(values[UserTokenValidatorImpl.INDEX_OF_DEVICE_HASH]);
    }

    private void checkNumberOfDataBlocks(String[] values) throws TokenException {
        if (values.length != UserTokenValidatorImpl.NUMBER_OF_DATA) {
            throw new TokenException(EXPIRED_OR_INVALID);
        }
    }

    private void validateEmail(String email) throws TokenException {
        boolean isValid = EmailValidator.getInstance().isValid(email);
        if (!isValid) {
            throw new TokenException(EXPIRED_OR_INVALID);
        }
    }

    private void validateUserId(String rawId) throws TokenException {
        checkIsNumeric(rawId);
        checkValidLong(rawId);
    }

    private void checkIsNumeric(String rawId) throws TokenException {
        if (!StringUtils.isNumeric(rawId)) {
            throw new TokenException(EXPIRED_OR_INVALID);
        }
    }

    private void checkValidLong(String rawId) throws TokenException {
        try {
            Long.parseLong(rawId);
        } catch (NumberFormatException nfe) {
            throw new TokenException(EXPIRED_OR_INVALID);
        }
    }

    private void validateDeviceHash(String deviceHash) throws TokenException {
        checkIsAlphanumeric(deviceHash);
        // should check the length of the hash?
    }

    private void checkIsAlphanumeric(String deviceHash) throws TokenException {
        if (!StringUtils.isAlphanumeric(deviceHash)) {
            throw new TokenException(EXPIRED_OR_INVALID);
        }
    }
}