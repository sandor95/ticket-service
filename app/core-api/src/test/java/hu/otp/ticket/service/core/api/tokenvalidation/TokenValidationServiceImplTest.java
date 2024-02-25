package hu.otp.ticket.service.core.api.tokenvalidation;

import static hu.otp.ticket.service.core.api.tokenvalidation.model.ValidationResult.FAILURE;
import static hu.otp.ticket.service.core.api.tokenvalidation.model.ValidationResult.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import hu.otp.ticket.service.core.api.exception.TokenError;
import hu.otp.ticket.service.core.api.exception.TokenException;
import hu.otp.ticket.service.core.api.tokenvalidation.model.TokenValidationResultDTO;
import hu.otp.ticket.service.core.api.tokenvalidation.validator.UserTokenValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenValidationServiceImplTest {

    @Mock
    UserTokenValidator tokenValidator;

    @InjectMocks
    TokenValidationServiceImpl validationService;

    @Test
    void shouldReturnSuccessfulResultWhenTokenIsValid() throws TokenException {
        // given
        String token = "some-token";
        Long userId = 123L;
        TokenValidationResultDTO expectedResult = TokenValidationResultDTO.builder().result(SUCCESS).build();
        doNothing().when(tokenValidator).validate(eq(userId), eq(token), any());

        // when
        TokenValidationResultDTO actualResult = validationService.validate(userId, token);

        // then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldReturnFailureResultWhenTokenIsInvalid() throws TokenException {
        // given
        String token = "some-token";
        Long userId = 123L;
        TokenValidationResultDTO expectedResult = TokenValidationResultDTO.builder().result(FAILURE).errorCode(10051).build();
        doThrow(new TokenException(TokenError.EXPIRED_OR_INVALID)).when(tokenValidator).validate(eq(userId), eq(token), any());

        // when
        TokenValidationResultDTO actualResult = validationService.validate(userId, token);

        // then
        assertEquals(expectedResult, actualResult);
    }
}