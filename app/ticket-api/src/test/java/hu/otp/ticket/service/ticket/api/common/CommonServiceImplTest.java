package hu.otp.ticket.service.ticket.api.common;

import static hu.otp.ticket.service.ticket.api.TicketApiApplication.APP_NAME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;

import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import hu.otp.ticket.service.coreapi.client.CoreApiClient;
import hu.otp.ticket.service.journal.JournalService;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.ticket.api.exception.InvalidUserException;
import hu.otp.ticket.service.util.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommonServiceImplTest {

    private static final String SUCCESS_RESULT = "SUCCESS";

    private static final String FAILURE_RESULT = "FAILURE";

    private static final Long USER_ID = 1L;

    private static final String VALID_TOKEN = "validToken";

    private static final String INVALID_TOKEN = "invalidToken";

    private static final Integer ERROR_CODE = 10105;

    @Mock
    private CoreApiClient coreApiClient;

    @Mock
    private JournalService journalService;

    @InjectMocks
    private CommonServiceImpl commonService;


    @Test
    void shouldRunWhenTokenValidationWasSuccessful() {
        TokenValidationResultDTO validationResult = new TokenValidationResultDTO();
        validationResult.setResult(SUCCESS_RESULT);

        when(coreApiClient.validateToken(eq(USER_ID), eq(VALID_TOKEN))).thenReturn(validationResult);

        commonService.validateToken(USER_ID, VALID_TOKEN);

        verify(coreApiClient, times(1)).validateToken(eq(USER_ID), eq(VALID_TOKEN));
        verify(journalService, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTokenValidationFails_ResultFailure() {
        TokenValidationResultDTO validationResult = new TokenValidationResultDTO();
        validationResult.setResult(FAILURE_RESULT);
        validationResult.setErrorCode(ERROR_CODE);
        ZonedDateTime sysdate = ZonedDateTime.now();
        Journal journal = buildJournal(validationResult, sysdate);

        when(coreApiClient.validateToken(eq(USER_ID), eq(INVALID_TOKEN))).thenReturn(validationResult);

        try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
            utilities.when(Util::sysdate).thenReturn(sysdate);

            assertThrows(InvalidUserException.class, () -> commonService.validateToken(USER_ID, INVALID_TOKEN));
        }

        verify(coreApiClient, times(1)).validateToken(eq(USER_ID), eq(INVALID_TOKEN));
        verify(journalService).save(eq(journal));
    }

    @Test
    void shouldThrowExceptionWhenTokenValidationFails_ResultSuccessWithErrorCode() {
        TokenValidationResultDTO validationResult = new TokenValidationResultDTO();
        validationResult.setResult(SUCCESS_RESULT);
        validationResult.setErrorCode(ERROR_CODE);
        ZonedDateTime sysdate = ZonedDateTime.now();
        Journal journal = buildJournal(validationResult, sysdate);

        when(coreApiClient.validateToken(eq(USER_ID), eq(INVALID_TOKEN))).thenReturn(validationResult);

        try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
            utilities.when(Util::sysdate).thenReturn(sysdate);

            assertThrows(InvalidUserException.class, () -> commonService.validateToken(USER_ID, INVALID_TOKEN));
        }

        verify(coreApiClient, times(1)).validateToken(eq(USER_ID), eq(INVALID_TOKEN));
        verify(journalService).save(eq(journal));
    }

    private static Journal buildJournal(TokenValidationResultDTO validationResult, ZonedDateTime sysdate) {
        String contentSkeleton = "Token validation failed! User id: [%s], token: [%s]\nResult:\n%s";
        String content = String.format(contentSkeleton, USER_ID, INVALID_TOKEN, validationResult);
        return Journal.builder()
                    .application(APP_NAME)
                    .user(String.valueOf(USER_ID))
                    .type(JournalType.TOKEN_VALIDATION)
                    .content(content)
                    .timestamp(sysdate)
                    .build();
    }
}