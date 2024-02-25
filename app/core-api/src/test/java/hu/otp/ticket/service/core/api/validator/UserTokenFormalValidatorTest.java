package hu.otp.ticket.service.core.api.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import hu.otp.ticket.service.core.api.exception.TokenError;
import hu.otp.ticket.service.core.api.exception.TokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserTokenFormalValidatorTest {

    private static final String VALID_TOKEN = "teszt.cecilia@otpmobil.com&3000&E68560872BDB2DF2FFE7ADC091755378";

    private static final long VALID_USER_ID = 3000L;

    final UserTokenValidator validator = new UserTokenFormalValidator();

    @Test
    void shouldPassWhenCalledWithValidToken() {
        assertDoesNotThrow(() -> validator.validate(VALID_USER_ID, null, VALID_TOKEN));
    }

    @ParameterizedTest
    @MethodSource("validData")
    void shouldPassWhenCalledWithData(Long userId, String rawToken) {
        assertDoesNotThrow(() -> validator.validate(userId, null, rawToken));
    }

    @ParameterizedTest
    @MethodSource("invalidData")
    void shouldThrowExceptionWhenMissingDataFromConcatenatedString(String rawToken) {
        assertTokenExpiredOrInvalid(rawToken);
    }

    private void assertTokenExpiredOrInvalid(String token) {
        TokenException exception = assertThrows(TokenException.class, () -> validator.validate(VALID_USER_ID, null, token));
        assertEquals(TokenError.EXPIRED_OR_INVALID, exception.getTokenError());
    }

    public static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of("&123&4A5B6C"),                // missing email
                Arguments.of("email@domain.com&&4A5B6C"),   // missing user ID
                Arguments.of("email@domain.com&123&"),      // missing device hash
                Arguments.of("emaildomain.com&123&4A5B6C"), // invalid email format
                Arguments.of("email@domain.com& &4A5B6C"),  // blank user ID
                Arguments.of("email@domain.com&NF&4A5B6C"), // invalid user ID (letter)
                Arguments.of("email@domain.com&-21&4A5B6C"),// invalid user ID (negative number)
                Arguments.of("email@domain.com&11111111111111111111111111111111111111111111111&4A5B6C"), // invalid user ID (value is greater than Long.MAX_VALUE)
                Arguments.of("email@domain.com&123& "),     // blank device hash
                Arguments.of("email@domain.com&123&QQ$QQ"), // invalid device hash
                Arguments.of("email@domain.com&123&8!88#8") // invalid device hash
        );
    }

    public static Stream<Arguments> validData() {
        return Stream.of(
                Arguments.of(123L, "email-name@test.hu&123&4A5B6C"),
                Arguments.of(4894894984L, "developer1234@outlook.com&4894894984&4A5B6C"),
                Arguments.of(756324234L, "support_team@company.com&756324234&4A5B6C"),
                Arguments.of(654984186L, "webmaster789@example.net&654984186&4A5B6C"),
                Arguments.of(21894184974L, "sOMe.exotic-suppa.cool-3mail@pro-portal.de&21894184974&1K9FSDG45435634DFSGD6FD8G4668ES4F8541GSEG4")
        );
    }
}