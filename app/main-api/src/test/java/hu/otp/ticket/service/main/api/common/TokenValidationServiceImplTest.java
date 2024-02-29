package hu.otp.ticket.service.main.api.common;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.stream.Stream;

import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import hu.otp.ticket.service.coreapi.client.CoreApiClient;
import hu.otp.ticket.service.main.api.exception.TokenValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenValidationServiceImplTest {

    static final Long USER_ID = 1L;

    static final String VALID_TOKEN = "validToken";

    static final String INVALID_TOKEN = "invalidToken";

    @Mock
    private CoreApiClient coreApiClient;

    @InjectMocks
    private TokenValidationServiceImpl tokenValidationService;

    @Test
    void shouldRunWhenTokenIsValid() {
        TokenValidationResultDTO validationResult = createValidationResult("SUCCESS", null);
        when(coreApiClient.validateToken(eq(USER_ID), eq(VALID_TOKEN))).thenReturn(validationResult);

        assertDoesNotThrow(() -> tokenValidationService.validateToken(USER_ID, VALID_TOKEN));

        verify(coreApiClient, times(1)).validateToken(eq(USER_ID), eq(VALID_TOKEN));
        verifyNoMoreInteractions(coreApiClient);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("failedResults")
    void shouldThrowExceptionWhenTokenIsInvalid(TokenValidationResultDTO validationResult) {
        when(coreApiClient.validateToken(eq(USER_ID), eq(INVALID_TOKEN))).thenReturn(validationResult);

        assertThrows(TokenValidationException.class, () -> tokenValidationService.validateToken(USER_ID, INVALID_TOKEN));

        verify(coreApiClient, times(1)).validateToken(eq(USER_ID), eq(INVALID_TOKEN));
        verifyNoMoreInteractions(coreApiClient);
    }

    public static Stream<Arguments> failedResults() {
        return Stream.of(
                Arguments.of(createValidationResult(null, null)),
                Arguments.of(createValidationResult("asd", null)),
                Arguments.of(createValidationResult("FAILURE", null)),
                Arguments.of(createValidationResult("FAILURE", 951)),
                Arguments.of(createValidationResult("SUCCESS", 159))
        );
    }

    private static TokenValidationResultDTO createValidationResult(String result, Integer errorCode) {
        TokenValidationResultDTO resultDTO = new TokenValidationResultDTO();
        resultDTO.setResult(result);
        resultDTO.setErrorCode(errorCode);
        return resultDTO;
    }
}