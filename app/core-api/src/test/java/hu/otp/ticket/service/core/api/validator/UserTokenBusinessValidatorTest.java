package hu.otp.ticket.service.core.api.validator;

import static hu.otp.ticket.service.Const.ENCODING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Base64;
import java.util.Optional;

import hu.otp.ticket.service.core.api.exception.TokenError;
import hu.otp.ticket.service.core.api.exception.TokenException;
import hu.otp.ticket.service.core.api.model.Token;
import hu.otp.ticket.service.core.api.model.User;
import hu.otp.ticket.service.core.api.persistentlayer.DeviceGateway;
import hu.otp.ticket.service.core.api.persistentlayer.TokenGateway;
import hu.otp.ticket.service.core.api.persistentlayer.UserGateway;
import hu.otp.ticket.service.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserTokenBusinessValidatorTest {

    private static final Long USER_ID = 123L;

    private static final String DEVICE_HASH = "A1B2C3";

    private static final String EMAIL = "email@domain.com";

    private static final String VALID_RAW_TOKEN = EMAIL + "&" + USER_ID + "&" + DEVICE_HASH;

    private static final String VALID_ENCODED_TOKEN = new String(Base64.getEncoder().encode(VALID_RAW_TOKEN.getBytes(ENCODING)), ENCODING);

    private static final User USER = User.builder().id(USER_ID).email(EMAIL).build();

    private static final Token VALID_TOKEN = Token.builder().validTo(Util.sysdate().plusDays(1)).build();

    private static final Token EXPIRED_TOKEN = Token.builder().validTo(Util.sysdate().plusMinutes(-2)).build();

    @Mock
    UserGateway userGateway;

    @Mock
    TokenGateway tokenGateway;

    @Mock
    DeviceGateway deviceGateway;

    @InjectMocks
    UserTokenBusinessValidator validator;

    @Test
    void shouldPassWithValidData() {
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.of(USER));
        when(tokenGateway.getTokenByTokenValue(eq(VALID_ENCODED_TOKEN))).thenReturn(Optional.of(VALID_TOKEN));
        when(deviceGateway.isDeviceAssignedToUser(eq(USER_ID), eq(DEVICE_HASH))).thenReturn(true);

        assertDoesNotThrow(() -> validator.validate(USER_ID, VALID_ENCODED_TOKEN, VALID_RAW_TOKEN));

        verify(userGateway, times(1)).getUserById(any());
        verify(tokenGateway, times(1)).getTokenByTokenValue(any());
        verify(deviceGateway, times(1)).isDeviceAssignedToUser(any(), any());
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(tokenGateway);
        verifyNoMoreInteractions(deviceGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.empty());

        TokenException exception = assertThrows(TokenException.class, () -> validator.validate(USER_ID, VALID_ENCODED_TOKEN, VALID_RAW_TOKEN));

        assertEquals(TokenError.EXPIRED_OR_INVALID, exception.getTokenError());
        verify(userGateway, times(1)).getUserById(any());
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(tokenGateway);
        verifyNoMoreInteractions(deviceGateway);
    }

    @Test
    void shouldThrowExceptionWhenTokenNotFound() {
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.of(USER));
        when(tokenGateway.getTokenByTokenValue(eq(VALID_ENCODED_TOKEN))).thenReturn(Optional.empty());

        TokenException exception = assertThrows(TokenException.class, () -> validator.validate(USER_ID, VALID_ENCODED_TOKEN, VALID_RAW_TOKEN));

        assertEquals(TokenError.EXPIRED_OR_INVALID, exception.getTokenError());
        verify(userGateway, times(1)).getUserById(any());
        verify(tokenGateway, times(1)).getTokenByTokenValue(any());
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(tokenGateway);
        verifyNoMoreInteractions(deviceGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserAndDeviceNotMatches() {
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.of(USER));
        when(tokenGateway.getTokenByTokenValue(eq(VALID_ENCODED_TOKEN))).thenReturn(Optional.of(VALID_TOKEN));
        when(deviceGateway.isDeviceAssignedToUser(eq(USER_ID), eq(DEVICE_HASH))).thenReturn(false);

        TokenException exception = assertThrows(TokenException.class, () -> validator.validate(USER_ID, VALID_ENCODED_TOKEN, VALID_RAW_TOKEN));

        assertEquals(TokenError.EXPIRED_OR_INVALID, exception.getTokenError());
        verify(userGateway, times(1)).getUserById(any());
        verify(tokenGateway, times(1)).getTokenByTokenValue(any());
        verify(deviceGateway, times(1)).isDeviceAssignedToUser(any(), any());
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(tokenGateway);
        verifyNoMoreInteractions(deviceGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserEmailNotMatches() {
        User otherUser = User.builder().id(USER_ID).email("some.valid@email.com").build();
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.of(otherUser));
        when(tokenGateway.getTokenByTokenValue(eq(VALID_ENCODED_TOKEN))).thenReturn(Optional.of(VALID_TOKEN));
        when(deviceGateway.isDeviceAssignedToUser(eq(USER_ID), eq(DEVICE_HASH))).thenReturn(true);

        TokenException exception = assertThrows(TokenException.class, () -> validator.validate(USER_ID, VALID_ENCODED_TOKEN, VALID_RAW_TOKEN));

        assertEquals(TokenError.EXPIRED_OR_INVALID, exception.getTokenError());
        verify(userGateway, times(1)).getUserById(any());
        verify(tokenGateway, times(1)).getTokenByTokenValue(any());
        verify(deviceGateway, times(1)).isDeviceAssignedToUser(any(), any());
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(tokenGateway);
        verifyNoMoreInteractions(deviceGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserNotMatches() {
        User otherUser = User.builder().id(123456789L).email(EMAIL).build();
        when(userGateway.getUserById(any())).thenReturn(Optional.of(otherUser));
        when(tokenGateway.getTokenByTokenValue(eq(VALID_ENCODED_TOKEN))).thenReturn(Optional.of(VALID_TOKEN));
        when(deviceGateway.isDeviceAssignedToUser(eq(USER_ID), eq(DEVICE_HASH))).thenReturn(true);

        TokenException exception = assertThrows(TokenException.class, () -> validator.validate(USER_ID, VALID_ENCODED_TOKEN, VALID_RAW_TOKEN));

        assertEquals(TokenError.EXPIRED_OR_INVALID, exception.getTokenError());
        verify(userGateway, times(1)).getUserById(any());
        verify(tokenGateway, times(1)).getTokenByTokenValue(any());
        verify(deviceGateway, times(1)).isDeviceAssignedToUser(any(), any());
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(tokenGateway);
        verifyNoMoreInteractions(deviceGateway);
    }

    @Test
    void shouldThrowExceptionWhenTokenExpired() {
        when(userGateway.getUserById(eq(USER_ID))).thenReturn(Optional.of(USER));
        when(tokenGateway.getTokenByTokenValue(eq(VALID_ENCODED_TOKEN))).thenReturn(Optional.of(EXPIRED_TOKEN));
        when(deviceGateway.isDeviceAssignedToUser(eq(USER_ID), eq(DEVICE_HASH))).thenReturn(true);

        TokenException exception = assertThrows(TokenException.class, () -> validator.validate(USER_ID, VALID_ENCODED_TOKEN, VALID_RAW_TOKEN));

        assertEquals(TokenError.EXPIRED_OR_INVALID, exception.getTokenError());
        verify(userGateway, times(1)).getUserById(any());
        verify(tokenGateway, times(1)).getTokenByTokenValue(any());
        verify(deviceGateway, times(1)).isDeviceAssignedToUser(any(), any());
        verifyNoMoreInteractions(userGateway);
        verifyNoMoreInteractions(tokenGateway);
        verifyNoMoreInteractions(deviceGateway);
    }
}