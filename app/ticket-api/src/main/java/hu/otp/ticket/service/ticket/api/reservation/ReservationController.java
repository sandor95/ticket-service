package hu.otp.ticket.service.ticket.api.reservation;

import static hu.otp.ticket.service.Const.X_USER_TOKEN;
import static hu.otp.ticket.service.ticket.api.TicketApiApplication.APP_NAME;

import java.util.Map;

import hu.otp.ticket.service.journal.JournalService;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.ticket.api.common.CommonService;
import hu.otp.ticket.service.ticket.api.model.Reservation;
import hu.otp.ticket.service.ticket.api.reservation.exception.ReservationException;
import hu.otp.ticket.service.ticket.api.reservation.model.ReservationDTO;
import hu.otp.ticket.service.ticket.api.reservation.model.ReservationResponseDTO;
import hu.otp.ticket.service.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ReservationController {

    private static final String USER_ID_PARAM = "userid";

    private static final String CARD_ID_PARAM = "cardid";

    private final CommonService commonService;

    private final ReservationService reservationService;

    private final JournalService journalService;

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seat reservation was successful", useReturnTypeSchema = true)
    })
    @Operation(operationId = "reserveSeat",
            description = "Reserve a seat for an event",
            parameters = {@Parameter(name = X_USER_TOKEN, in = ParameterIn.HEADER, description = "User token",
                            content = @Content(schema = @Schema(implementation = String.class))),
                          @Parameter(name = "eventId", in = ParameterIn.PATH, description = "Event ID"),
                          @Parameter(name = "seatCode", in = ParameterIn.PATH, description = "Seat code at the event"),
                          @Parameter(name = USER_ID_PARAM, in = ParameterIn.QUERY, description = "Logged user ID")}
    )
    @PostMapping("/reserve/event/@{eventId}/seat/@{seatCode}")
    public ReservationResponseDTO reserve(@RequestHeader Map<String, String> headers,
                                          @PathVariable Long eventId,
                                          @PathVariable String seatCode,
                                          @RequestParam(USER_ID_PARAM) Long userId,
                                          @RequestParam(CARD_ID_PARAM) String cardId) throws ReservationException {
        String transactionId = Util.randomUuid();
        log.info("Seat reservation requested for seat {} at event {} for userId {}. Transaction started with Id: {}",
                seatCode, eventId, userId, transactionId);
        String token = headers.get(X_USER_TOKEN);
        commonService.validateToken(userId, token);
        ReservationDTO reservationDTO = buildInternalDTO(userId, cardId, eventId, seatCode, transactionId, token);
        reservationService.checkSeatAvailable(reservationDTO);
        Reservation reservation = reservationService.reserve(reservationDTO);
        ReservationResponseDTO response = buildReservationResponse(reservation, reservationDTO);
        saveJournal(reservationDTO);
        return response;
    }

    private ReservationDTO buildInternalDTO(Long userId, String cardId, Long eventId, String seatCode,
                                            String transactionId, String token) {
        return ReservationDTO.builder()
                            .userId(userId)
                            .cardId(cardId)
                            .eventId(eventId)
                            .seatCode(seatCode)
                            .transactionId(transactionId)
                            .token(token)
                            .build();
    }

    private ReservationResponseDTO buildReservationResponse(Reservation reservation, ReservationDTO dto) {
        return ReservationResponseDTO.builder()
                                    .eventName(dto.getEventName())
                                    .seatCode(reservation.getSeatCode())
                                    .transactionId(reservation.getTransactionId())
                                    .startTime(dto.getStartTime())
                                    .success(true)
                                    .build();
    }

    private void saveJournal(ReservationDTO reservationDTO) {
        String contentSkeleton = "Seat reservation was successful for eventId: %s, seatCode: %s! Deatils: %s";
        Journal journal = Journal.builder()
                .application(APP_NAME)
                .type(JournalType.SEAT_RESERVATION)
                .timestamp(Util.sysdate())
                .user(reservationDTO.getUserId().toString())
                .content(String.format(contentSkeleton, reservationDTO.getUserId(), reservationDTO.getSeatCode(), reservationDTO))
                .build();
        journalService.save(journal);
    }
}