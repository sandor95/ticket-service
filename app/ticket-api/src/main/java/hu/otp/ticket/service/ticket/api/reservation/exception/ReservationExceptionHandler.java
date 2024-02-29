package hu.otp.ticket.service.ticket.api.reservation.exception;

import hu.otp.ticket.service.exception.BaseExceptionHandler;
import hu.otp.ticket.service.ticket.api.reservation.model.ReservationResponseDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@Priority(3)
@RestControllerAdvice(basePackages = "hu.otp.ticket.service.ticket.api.reservation")
public class ReservationExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(value = {ReservationException.class})
    @ApiResponse(responseCode = "400", description = "HTTP request cannot be processed due validation error")
    public ResponseEntity<ReservationResponseDTO> handlePaymentException(ReservationException ex, WebRequest request) {
        log.warn("Reservation failed [{}], error code: {}! Request: {}",
                ex.getTransactionId(), ex.getReservationError().errorCode, request, ex);
        ReservationResponseDTO responseDTO = buildFailureResultDTO(ex);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    private ReservationResponseDTO buildFailureResultDTO(ReservationException ex) {
        return ReservationResponseDTO.builder()
                                    .errorCode(ex.getReservationError().errorCode)
                                    .transactionId(ex.getTransactionId())
                                    .success(false)
                                    .build();
    }
}