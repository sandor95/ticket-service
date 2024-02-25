package hu.otp.ticket.service.core.api.validator;

import static hu.otp.ticket.service.Const.ENCODING;

import java.util.Base64;

import hu.otp.ticket.service.core.api.exception.TokenError;
import hu.otp.ticket.service.core.api.exception.TokenException;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("tokenValidator")
public class UserTokenValidatorImpl implements UserTokenValidator {

    public static final String SEPARATOR = "&";

    public static final Integer NUMBER_OF_DATA = 3;

    public static final Integer INDEX_OF_EMAIL = 0;

    public static final Integer INDEX_OF_USER_ID = 1;

    public static final Integer INDEX_OF_DEVICE_HASH = 2;

    private final UserTokenValidator formalValidator;

    private final UserTokenValidator businessValidator;

    @Autowired
    public UserTokenValidatorImpl(@Qualifier("tokenFormalValidator") UserTokenValidator formalValidator,
                                  @Qualifier("tokenBusinessValidator") UserTokenValidator businessValidator) {
        this.formalValidator = formalValidator;
        this.businessValidator = businessValidator;
    }

    @Override
    public void validate(Long assumedUserId, String encodedToken, @Nullable String decodedToken) throws TokenException {
        checkUserId(assumedUserId);
        checkIsNotBlank(encodedToken);
        String rawToken = decodeToken(encodedToken);
        formalValidator.validate(assumedUserId, encodedToken, rawToken);
        businessValidator.validate(assumedUserId, encodedToken, rawToken);
    }

    private void checkUserId(Long userId) {
        if (userId == null || userId < 1) {
            throw new IllegalArgumentException("Invalid user ID: [" + userId + "]");
        }
    }

    private void checkIsNotBlank(String token) throws TokenException {
        if (StringUtils.isBlank(token)) {
            throw new TokenException(TokenError.EMPTY);
        }
    }

    private String decodeToken(String token) {
        byte[] decodedBytes = Base64.getDecoder().decode(token.getBytes(ENCODING));
        return new String(decodedBytes, ENCODING);
    }

}