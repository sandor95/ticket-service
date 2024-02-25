package hu.otp.ticket.service.core.api.tokenvalidation.validator;

import static hu.otp.ticket.service.core.api.exception.TokenError.EXPIRED_OR_INVALID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import hu.otp.ticket.service.core.api.exception.TokenError;
import hu.otp.ticket.service.core.api.exception.TokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserTokenValidatorImplTest {

    private static final String VALID_DECODED_TOKEN = "dGVzenQuY2VjaWxpYUBvdHBtb2JpbC5jb20mMzAwMCZFNjg1NjA4NzJCREIyREYyRkZFN0FEQzA5MTc1NTM3OA==";

    private static final String VALID_ENCODED_TOKEN = "teszt.cecilia@otpmobil.com&3000&E68560872BDB2DF2FFE7ADC091755378";

    private static final long VALID_USER_ID = 1L;

    @Mock
    UserTokenValidator formalValidator;

    @Mock
    UserTokenValidator businessValidator;

    UserTokenValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserTokenValidatorImpl(formalValidator, businessValidator);
    }

    @Test
    void shouldPassValidationWithValidToken() throws TokenException {
        assertDoesNotThrow(() -> underTest.validate(VALID_USER_ID, VALID_DECODED_TOKEN, null));

        verify(formalValidator, times(1)).validate(eq(VALID_USER_ID), eq(VALID_DECODED_TOKEN), eq(VALID_ENCODED_TOKEN));
        verify(businessValidator, times(1)).validate(eq(VALID_USER_ID), eq(VALID_DECODED_TOKEN), eq(VALID_ENCODED_TOKEN));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenTokenIsBlank(String rawtoken) {
        TokenException exception = assertThrows(TokenException.class, () -> underTest.validate(VALID_USER_ID, rawtoken, null));
        assertEquals(TokenError.EMPTY, exception.getTokenError());
    }

    @ParameterizedTest
    @NullSource
    @CsvSource("-12")
    void shouldThrowExceptionWhenUserIdIsInvalid(Long userId) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> underTest.validate(userId, VALID_DECODED_TOKEN, null));
        assertTrue(exception.getMessage().startsWith("Invalid user ID:"));
    }

    @Test
    void shouldThrowExceptionWhenFormalValidationFails() throws TokenException {
        String invalidToken = "base64Powa==";
        doThrow(new TokenException(EXPIRED_OR_INVALID)).when(formalValidator).validate(eq(VALID_USER_ID), eq(invalidToken), any());

        TokenException exception = assertThrows(TokenException.class, () -> underTest.validate(VALID_USER_ID, invalidToken, null));
        assertEquals(EXPIRED_OR_INVALID, exception.getTokenError());
    }

    @Test
    void shouldThrowExceptionWhenBusinessValidationFails() throws TokenException {
        String invalidToken = "base64Powa==";
        doThrow(new TokenException(EXPIRED_OR_INVALID)).when(businessValidator).validate(eq(VALID_USER_ID), eq(invalidToken), any());

        TokenException exception = assertThrows(TokenException.class, () -> underTest.validate(VALID_USER_ID, invalidToken, null));
        assertEquals(EXPIRED_OR_INVALID, exception.getTokenError());
    }
}