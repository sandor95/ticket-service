package hu.otp.ticket.service.core.api.payment.exception;

import hu.otp.ticket.service.core.api.payment.model.PaymentResponseDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@Priority(10)
@RestControllerAdvice(basePackages = "hu.otp.ticket.service.core.api.payment")
public class PaymentExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {PaymentException.class})
    @ApiResponse(responseCode = "422", description = "HTTP request cannot be processed due validation error")
    public ResponseEntity<PaymentResponseDTO> handlePaymentException(PaymentException ex, WebRequest request) {
        log.warn("Payment failed [{}]! Request: {}", ex.getPaymentTransactionId(), request, ex);
        PaymentResponseDTO responseDTO = buildFailureResultDTO(ex);
        return new ResponseEntity<>(responseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private PaymentResponseDTO buildFailureResultDTO(PaymentException ex) {
        return PaymentResponseDTO.builder()
                                .errorCode(ex.getPaymentError().errorCode)
                                .paymentTransactionId(ex.getPaymentTransactionId())
                                .success(false)
                                .build();
    }
}