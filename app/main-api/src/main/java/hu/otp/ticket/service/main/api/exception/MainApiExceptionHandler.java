package hu.otp.ticket.service.main.api.exception;

import static hu.otp.ticket.service.main.api.MainApiApplication.APP_NAME;

import hu.otp.ticket.service.exception.BaseExceptionHandler;
import hu.otp.ticket.service.journal.JournalService;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.model.ErrorMessage;
import hu.otp.ticket.service.util.Util;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Priority(5)
@RestControllerAdvice(basePackages = "hu.otp.ticket.service")
public class MainApiExceptionHandler extends BaseExceptionHandler {

    private final JournalService journalService;

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Error occurred",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ErrorMessage> handleUnhandledException(Exception exception) {
        log.error("Unhandled exception", exception);
        String uuid = Util.randomUuid();
        ErrorMessage errorMessage = ErrorMessage.builder().id(uuid).message("Error").build();
        saveJournal(errorMessage, exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(TokenValidationException.class)
    @ApiResponse(responseCode = "400", description = "Invalid user credentials",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
    public ResponseEntity<ErrorMessage> handleTokenValidationException(TokenValidationException exception) {
        log.warn("Token validation failed!", exception);
        String uuid = Util.randomUuid();
        ErrorMessage errorMessage = ErrorMessage.builder()
                                                .id(uuid)
                                                .message("Wrong credentials [" + exception.getErrorCode() + "]")
                                                .build();
        saveJournal(errorMessage, exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    private void saveJournal(ErrorMessage errorMessage, Exception exception) {
        Journal journal = Journal.builder()
                .application(APP_NAME)
                .type(JournalType.TOKEN_VALIDATION)
                .timestamp(Util.sysdate())
                .content(createJournalContent(errorMessage, exception))
                .build();
        journalService.save(journal);
    }

    private String createJournalContent(ErrorMessage errorMessage, Exception exception) {
        return "Exception occurred: ".concat(exception.getMessage()).concat("]\n")
                .concat("Response: ").concat(errorMessage.toString());
    }
}