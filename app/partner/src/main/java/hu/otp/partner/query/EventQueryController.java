package hu.otp.partner.query;


import java.util.List;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.exception.NoEntityFoundException;
import hu.otp.partner.query.model.EventDataDTO;
import hu.otp.partner.query.model.EventDetailsDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventQueryController {

    private final EventQueryService queryService;
    private final EventConverter converter;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "All available events", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "User and/or session values are invalid, details in response body")
    })
    @Operation(operationId = "getEvents", description = "Fetch all available events.")
    @GetMapping("/getEvents")
    public EventDataDTO getEvents() {
        log.debug("Listing all events...");
        List<Event> allEvents = queryService.getAllEvents();
        EventDataDTO eventListDTO = converter.convertToEventListDTO(allEvents);
        log.debug("All events count: " + allEvents.size());
        log.trace("GetEvents response: " + eventListDTO.toString());
        return eventListDTO;
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Event details", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "User and/or session values are invalid, details in response body")
    })
    @Operation(operationId = "getEvent", description = "Fetch event details.")
    @GetMapping("/getEvent/{id}")
    public EventDetailsDataDTO getEvent(@PathVariable Long id) throws NoEntityFoundException {
        log.info("Getting event details...");
        Event event = queryService.getById(id);
        EventDetailsDataDTO eventDetailsListDTO = converter.convertToEventDetailsListDTO(event);
        log.trace("GetEvents response: " + eventDetailsListDTO.toString());
        return eventDetailsListDTO;
    }
}