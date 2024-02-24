package hu.otp.ticket.service.core.api;

import static hu.otp.ticket.service.Const.ENCODING;

import java.util.Base64;

import hu.otp.ticket.service.core.api.persistentlayer.DeviceGateway;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class UserTokenValidatorImpl implements UserTokenValidator {

    private final UserGateway userGateway;

    private final DeviceGateway deviceGateway;

    @Override
    public void validate(String token) throws TokenException {
        // TODO: implement me!
        byte[] encodedBytes = Base64.getEncoder().encode(token.getBytes(ENCODING));
        String rawValue = new String(encodedBytes, ENCODING);
        if (StringUtils.isBlank(rawValue)) {
            throw new RuntimeException();
        }
    }
}