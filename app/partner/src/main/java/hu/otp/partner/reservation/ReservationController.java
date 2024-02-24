package hu.otp.partner.reservation;

import hu.otp.partner.reservation.exception.ReservationException;
import hu.otp.partner.reservation.model.ReservationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ReservationController {

    private final ReservationValidator reservationValidator;

    private final ReservationService reservationService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seat reservation was successful", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "User and/or session values are invalid, details in response body"),
        @ApiResponse(responseCode = "422", description = "Seat reservation failed, details in response body")
    })
    @Operation(operationId = "reserve", description = "Reserve seat for event.")
    @PostMapping("/reserve/event/{eventId}/seat/{seatId}")
    public ReservationDTO reserve(@PathVariable String eventId, @PathVariable String seatId) throws ReservationException {
        log.debug("Formal validation started for seat reservation");
        reservationValidator.validateRequestParameters(eventId, seatId);
        log.debug("Formal validation was successful! ");
        log.info("Reservation process started!");
        Long reservationId = reservationService.reserveSeat(Long.parseLong(eventId), seatId.toUpperCase());
        log.info("Successful seat reservation! Reservation ID: {}", reservationId);
        return new ReservationDTO(reservationId, null, true);
    }
}