package hu.otp.ticket.service.main.api.controller;

import static hu.otp.ticket.service.Const.*;

import java.util.Map;

import hu.otp.ticket.service.main.api.common.TokenValidationService;
import hu.otp.ticket.service.main.api.exception.TokenValidationException;
import hu.otp.ticket.service.ticket.api.client.model.ReservationResponseDTO;
import hu.otp.ticket.service.ticketapi.client.TicketApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ReservationController {

    private final TokenValidationService tokenValidationService;

    private final TicketApiClient ticketApiClient;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seat reservation was successful", useReturnTypeSchema = true)
    })
    @Operation(operationId = "reserveSeat",
            description = "Reserve a seat for an event",
            parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token",
                                content = @Content(schema = @Schema(implementation = String.class))),
                          @Parameter(name = EVENT_ID_PARAM, in = ParameterIn.QUERY, description = "Event ID",
                                content = @Content(schema = @Schema(implementation = Long.class))),
                          @Parameter(name = SEAT_CODE_PARAM, in = ParameterIn.QUERY, description = "Seat code at the event",
                                content = @Content(schema = @Schema(implementation = String.class))),
                          @Parameter(name = USER_ID_PARAM, in = ParameterIn.QUERY, description = "Logged user ID",
                                content = @Content(schema = @Schema(implementation = Long.class))),
                          @Parameter(name = CARD_ID_PARAM, in = ParameterIn.QUERY, description = "Seat code for the event",
                                content = @Content(schema = @Schema(implementation = String.class)))}
    )
    @PostMapping("/reservation")
    public ReservationResponseDTO reserve(@RequestHeader Map<String, String> headers,
                                          @RequestParam(EVENT_ID_PARAM) Long eventId,
                                          @RequestParam(SEAT_CODE_PARAM) String seatCode,
                                          @RequestParam(USER_ID_PARAM) Long userId,
                                          @RequestParam(CARD_ID_PARAM) String cardId) throws TokenValidationException {
        String token = headers.get(X_USER_TOKEN);
        tokenValidationService.validateToken(userId, token);
        return ticketApiClient.reserveSeat(eventId, seatCode, userId, cardId, token);
    }
}