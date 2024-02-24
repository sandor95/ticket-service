package hu.otp.ticket.service.core.api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import hu.otp.ticket.service.core.api.persistentlayer.DeviceGateway;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserTokenValidatorImplTest {

    private static final String VALID_TOKEN = "dGVzenQuY2VjaWxpYUBvdHBtb2JpbC5jb20mMzAwMCZFNjg1NjA4NzJCREIyREYyRkZFN0FEQzA5MTc1NTM3OA==";

    @Mock
    DeviceGateway deviceGateway;

    @Mock
    UserGateway userGateway;

    @InjectMocks
    UserTokenValidatorImpl validator;

    @Test
    void shouldPassValidationWithValidToken() {
        assertDoesNotThrow(() -> validator.validate(VALID_TOKEN));
    }
}