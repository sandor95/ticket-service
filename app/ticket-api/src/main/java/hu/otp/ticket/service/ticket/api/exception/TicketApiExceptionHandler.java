package hu.otp.ticket.service.ticket.api.exception;

import hu.otp.ticket.service.exception.BaseExceptionHandler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Priority(5)
@RestControllerAdvice(basePackages = "hu.otp.ticket.service.ticket.api")
public class TicketApiExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(InvalidUserException.class)
    @ApiResponse(responseCode = "401", description = "Invalid user")
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException exception) {
        log.error("Unsuccessful user token validation", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}