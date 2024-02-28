package hu.otp.ticket.service.main.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import hu.otp.ticket.service.core.api.client.api.TokenValidationControllerApi;
import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoreApiClientTest {

    static final String TOKEN = "my-secret-token-1";

    static final Long USER_ID = 12345678L;

    @Mock
    TokenValidationControllerApi tokenValidationApi;

    @InjectMocks
    CoreApiClient coreApiClient;

    @Test
    void shouldCallCoreApiWhenTokenValidationCalled() {
        TokenValidationResultDTO expectedResult = new TokenValidationResultDTO().result("SUCCESS");
        when(tokenValidationApi.validate(eq(USER_ID), eq(TOKEN))).thenReturn(expectedResult);

        TokenValidationResultDTO actualResponse = coreApiClient.validateToken(USER_ID, TOKEN);

        assertNotNull(actualResponse);
        assertNull(actualResponse.getErrorCode());
        assertEquals(expectedResult.getResult(), actualResponse.getResult());
    }
}