package hu.otp.ticket.service.core.api.exception;

import hu.otp.ticket.service.core.api.payment.exception.PaymentException;
import hu.otp.ticket.service.core.api.payment.model.PaymentResponseDTO;
import hu.otp.ticket.service.exception.BaseExceptionHandler;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Priority(5)
@RestControllerAdvice(basePackages = "hu.otp.ticket.service.core.api")
public class CoreApiExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(PaymentException.class)
    @ApiResponse(responseCode = "400", description = "Payment was rejected, details in response body",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))})
    public ResponseEntity<PaymentResponseDTO> handleUnhandledException(PaymentException exception) {
        log.error("{} payment transaction has been rejected due {}",
                exception.getPaymentTransactionId(), exception.getPaymentError(), exception);
        PaymentResponseDTO responseDTO = PaymentResponseDTO.builder()
                                                            .errorCode(exception.getPaymentError().errorCode)
                                                            .paymentTransactionId(exception.getPaymentTransactionId())
                                                            .success(false)
                                                            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }
}