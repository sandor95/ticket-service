package hu.otp.partner.reservation.exception;

import hu.otp.partner.reservation.model.ReservationDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@Priority(10)
@RestControllerAdvice(basePackages = "hu.otp.partner.reservation")
public class ReservationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ReservationException.class)
    @ApiResponse(responseCode = "422", description = "Reservation request was rejected!")
    public ResponseEntity<ReservationDTO> handleReservationException(ReservationException exception) {
        log.warn("Reservation exception occurred!", exception);
        ReservationDTO reservationDTO = new ReservationDTO(null, exception.getReservationError().errorCode, false);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(reservationDTO);
    }
}