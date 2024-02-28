package hu.otp.ticket.service.ticket.api.partnerquery;

import static hu.otp.ticket.service.Const.X_USER_TOKEN;

import java.util.List;
import java.util.Map;

import hu.otp.ticket.service.partner.client.model.EventDTO;
import hu.otp.ticket.service.partner.client.model.EventDataDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDataDTO;
import hu.otp.ticket.service.ticket.api.common.CommonService;
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
@RequestMapping("/partner/event")
public class EventQueryController {

    private static final String USER_ID_PARAM = "userid";

    private final CommonService commonService;

    private final EventQueryService eventQueryService;

    private final EventConverter eventConverter;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "All event list", useReturnTypeSchema = true)
    })
    @Operation(operationId = "getAllEvents",
            description = "All available events from Partner",
            parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token",
                            content = @Content(schema = @Schema(implementation = String.class))),
                          @Parameter(name = USER_ID_PARAM, in = ParameterIn.QUERY, description = "Logged user ID")}
    )
    @GetMapping("/all")
    public List<EventDTO> getAllEvents(@RequestHeader Map<String, String> headers,
                                       @RequestParam(USER_ID_PARAM) Long userId) {
        commonService.validateToken(userId, headers.get(X_USER_TOKEN));
        EventDataDTO allEvents = eventQueryService.getAllEventsFromPartner();
        return eventConverter.convertToInternalDTO(allEvents);
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
    @GetMapping("/{id}")
    public EventDetailsDTO getEvent(@RequestHeader Map<String, String> headers,
                                    @PathVariable("id") Long eventId,
                                    @RequestParam(USER_ID_PARAM) Long userId) {
        commonService.validateToken(userId, headers.get(X_USER_TOKEN));
        EventDetailsDataDTO event = eventQueryService.getEvent(eventId);
        return eventConverter.convertToInternalDTO(event);
    }
}