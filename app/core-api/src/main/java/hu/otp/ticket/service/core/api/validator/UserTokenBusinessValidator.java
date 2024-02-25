package hu.otp.ticket.service.core.api.validator;

import static hu.otp.ticket.service.core.api.validator.UserTokenValidatorImpl.*;

import java.util.Objects;

import hu.otp.ticket.service.core.api.exception.TokenError;
import hu.otp.ticket.service.core.api.exception.TokenException;
import hu.otp.ticket.service.core.api.model.Token;
import hu.otp.ticket.service.core.api.model.User;
import hu.otp.ticket.service.core.api.persistentlayer.DeviceGateway;
import hu.otp.ticket.service.core.api.persistentlayer.TokenGateway;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import hu.otp.ticket.service.util.Util;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PACKAGE)
@Component("tokenBusinessValidator")
class UserTokenBusinessValidator implements UserTokenValidator {

    private final UserGateway userGateway;

    private final TokenGateway tokenGateway;

    private final DeviceGateway deviceGateway;

    @Override
    public void validate(Long assumedUserId, String encodedToken, String decodedToken) throws TokenException {
        String[] values = decodedToken.split(SEPARATOR);
        Long userIdFromToken = Long.parseLong(values[INDEX_OF_USER_ID]);
        String email = values[INDEX_OF_EMAIL];
        String deviceHash = values[INDEX_OF_DEVICE_HASH];

        User user = userGateway.getUserById(userIdFromToken)
                                .orElseThrow(() -> new TokenException(TokenError.EXPIRED_OR_INVALID));
        Token token = tokenGateway.getTokenByTokenValue(encodedToken)
                                    .orElseThrow(() -> new TokenException(TokenError.EXPIRED_OR_INVALID));
        boolean isDeviceRelatedToUser = deviceGateway.isDeviceAssignedToUser(userIdFromToken, deviceHash);
        boolean userNotMatches = !Objects.equals(assumedUserId, user.getId());
        boolean emailNotMatches = !StringUtils.equals(user.getEmail(), email);
        boolean isTokenExpired = Util.sysdate().isAfter(token.getValidTo());

        if (userNotMatches || emailNotMatches || !isDeviceRelatedToUser || isTokenExpired) {
            throw new TokenException(TokenError.EXPIRED_OR_INVALID);
        }
    }
}