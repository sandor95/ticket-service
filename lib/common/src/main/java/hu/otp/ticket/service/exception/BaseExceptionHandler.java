package hu.otp.ticket.service.exception;

import hu.otp.ticket.service.model.ErrorMessage;
import hu.otp.ticket.service.util.Util;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ApiResponse(responseCode = "400", description = "Request content is not valid")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String errorId = Util.randomUuid();
        log.warn("Request method argument is not valid! Error ID: " + errorId, ex);
        return buildRequestNotReadableMessage(errorId);
    }

    @Override
    @ApiResponse(responseCode = "400", description = "HTTP message not readable")
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        String errorId = Util.randomUuid();
        log.warn("HTTP request not readable! [" + errorId + "]", ex);
        return buildRequestNotReadableMessage(errorId);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Server error occured")
    public ResponseEntity<ErrorMessage> handleUnhandledException(Exception exception) {
        String errorId = Util.randomUuid();
        String message = "Unexpected error occurred! [" + errorId + "]";
        log.error(message, exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .id(errorId)
                .message("Server error occurred!")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    private ResponseEntity<Object> buildRequestNotReadableMessage(String errorId) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .message("Request not processable!")
                .id(errorId)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}