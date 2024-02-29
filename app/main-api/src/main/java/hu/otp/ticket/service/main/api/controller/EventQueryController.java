package hu.otp.ticket.service.main.api.controller;

import static hu.otp.ticket.service.Const.USER_ID_PARAM;
import static hu.otp.ticket.service.Const.X_USER_TOKEN;

import java.util.List;
import java.util.Map;

import hu.otp.ticket.service.main.api.common.TokenValidationService;
import hu.otp.ticket.service.main.api.exception.TokenValidationException;
import hu.otp.ticket.service.ticket.api.client.model.EventDTO;
import hu.otp.ticket.service.ticket.api.client.model.EventDetailsDTO;
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
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/events")
public class EventQueryController {

    private final TokenValidationService tokenValidationService;

    private final TicketApiClient ticketApiClient;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "All event list", useReturnTypeSchema = true)
    })
    @Operation(operationId = "getAllEvents",
            description = "All available events from Partner",
            parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token",
                                    content = @Content(schema = @Schema(implementation = String.class))),
                          @Parameter(name = USER_ID_PARAM, in = ParameterIn.QUERY, description = "Logged user ID",
                                    content = @Content(schema = @Schema(implementation = Long.class)))}
    )
    @GetMapping("/all")
    public List<EventDTO> getAllEvents(@RequestHeader Map<String, String> headers,
                                       @RequestParam(USER_ID_PARAM) Long userId) throws TokenValidationException {
        String token = headers.get(X_USER_TOKEN);
        tokenValidationService.validateToken(userId, token);
        return ticketApiClient.getAllEvents(userId, token);
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "The details of the requested event", useReturnTypeSchema = true)
    })
    @Operation(operationId = "getEvent",
            description = "Event details from Partner",
            parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token",
                            content = @Content(schema = @Schema(implementation = String.class))),
                          @Parameter(name = USER_ID_PARAM, in = ParameterIn.QUERY, description = "Logged user ID")}
    )
    @GetMapping("/{id}/details")
    public EventDetailsDTO getEvent(@RequestHeader Map<String, String> headers,
                                    @PathVariable("id") Long eventId,
                                    @RequestParam(USER_ID_PARAM) Long userId) throws TokenValidationException {
        String token = headers.get(X_USER_TOKEN);
        tokenValidationService.validateToken(userId, token);
        return ticketApiClient.getEventDetails(userId, eventId, token);
    }
}