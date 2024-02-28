package hu.otp.ticket.service.ticket.api.common;

import static hu.otp.ticket.service.ticket.api.TicketApiApplication.APP_NAME;

import hu.otp.ticket.service.core.api.client.model.TokenValidationResultDTO;
import hu.otp.ticket.service.coreapi.client.CoreApiClient;
import hu.otp.ticket.service.journal.JournalService;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.ticket.api.exception.InvalidUserException;
import hu.otp.ticket.service.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CommonServiceImpl implements CommonService {

    private static final String TOKEN_VALID_RESULT = "SUCCESS";

    private final CoreApiClient coreApiClient;

    private final JournalService journalService;

    @Override
    public void validateToken(Long userId, String token) {
        log.debug("User token validation...");
        TokenValidationResultDTO validationResultDTO = coreApiClient.validateToken(userId, token);
        if (isValidationFailed(validationResultDTO)) {
            saveJournal(userId, token, validationResultDTO);
            log.warn("Token validation failed! Result: {}", validationResultDTO);
            throw new InvalidUserException();
        }
        log.debug("User token validation was successful");
    }

    private void saveJournal(Long userId, String token, TokenValidationResultDTO validationResultDTO) {
        String contentSkeleton = "Token validation failed! User id: [%s], token: [%s]\nResult:\n%s";
        Journal journal = Journal.builder()
                                .application(APP_NAME)
                                .user(String.valueOf(userId))
                                .type(JournalType.TOKEN_VALIDATION)
                                .content(String.format(contentSkeleton, userId, token, validationResultDTO))
                                .timestamp(Util.sysdate())
                                .build();
        journalService.save(journal);
    }

    private boolean isValidationFailed(TokenValidationResultDTO validationResultDTO) {
        return !(TOKEN_VALID_RESULT.equalsIgnoreCase(validationResultDTO.getResult()))
                || validationResultDTO.getErrorCode() != null;
    }
}