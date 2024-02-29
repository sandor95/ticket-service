package hu.otp.ticket.service.ticket.api.reservation;

import static hu.otp.ticket.service.ticket.api.reservation.exception.ReservationError.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import hu.otp.ticket.service.core.api.client.model.PaymentRequestDTO;
import hu.otp.ticket.service.core.api.client.model.PaymentResponseDTO;
import hu.otp.ticket.service.coreapi.client.CoreApiClient;
import hu.otp.ticket.service.partner.client.model.EventDTO;
import hu.otp.ticket.service.partner.client.model.EventDetailsDTO;
import hu.otp.ticket.service.partner.client.model.SeatDTO;
import hu.otp.ticket.service.ticket.api.model.Reservation;
import hu.otp.ticket.service.ticket.api.model.ReservationLock;
import hu.otp.ticket.service.ticket.api.persistentlayer.ReservationGateway;
import hu.otp.ticket.service.ticket.api.reservation.exception.ReservationError;
import hu.otp.ticket.service.ticket.api.reservation.exception.ReservationException;
import hu.otp.ticket.service.ticket.api.reservation.model.ReservationDTO;
import hu.otp.ticket.service.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ReservationServiceImpl implements ReservationService {

    private final PartnerGateway partnerGateway;

    private final CoreApiClient coreApiClient;

    private final ReservationLockService lockService;

    private final ReservationGateway reservationGateway;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void checkSeatAvailable(ReservationDTO dto) throws ReservationException {
        log.debug("Checking {} seat is available for event: {}", dto.getSeatCode(), dto.getEventId());
        checkEvent(dto);
        EventDetailsDTO eventDetails = partnerGateway.getEvent(dto.getEventId())
                                .orElseThrow(() -> new ReservationException(EVENT_NOT_EXISTS, dto.getTransactionId()));
        checkSeatAvailable(eventDetails, dto);
    }

    private void checkSeatAvailable(EventDetailsDTO eventDetails, ReservationDTO dto) throws ReservationException {
        if (eventDetails.getSeats() != null) {
            SeatDTO seat = eventDetails.getSeats()
                                        .stream()
                                        .filter(seatDTO -> StringUtils.equalsIgnoreCase(seatDTO.getId(), dto.getSeatCode()))
                                        .findFirst()
                                        .orElseThrow(() -> new ReservationException(SEAT_NOT_EXISTS, dto.getTransactionId()));
            if (Boolean.TRUE.equals(seat.getReserved())) {
                throw new ReservationException(SEAT_RESERVED, dto.getTransactionId());
            }
            dto.setAmount(seat.getPrice().longValue());
            dto.setCurrency(seat.getCurrency());
        } else {
            throw new ReservationException(SEAT_NOT_EXISTS, dto.getTransactionId());
        }
    }

    private void checkEvent(ReservationDTO dto) throws ReservationException {
        List<EventDTO> events = partnerGateway.getAllEvents()
                                          .orElseThrow(() -> new ReservationException(EVENT_NOT_EXISTS, dto.getTransactionId()));
        EventDTO eventDTO = events.stream()
                                .filter(event -> Objects.equals(event.getEventId(), dto.getEventId()))
                                .findFirst()
                                .orElseThrow(() -> new ReservationException(EVENT_NOT_EXISTS, dto.getTransactionId()));
        checkEventHasNotStarted(eventDTO, dto);
    }

    private void checkEventHasNotStarted(EventDTO eventDTO, ReservationDTO dto) throws ReservationException {
        ZonedDateTime sysdate = Util.sysdate();
        ZonedDateTime eventStart = Util.parseFromUtc(eventDTO.getStartTimeStamp());
        Duration duration = Duration.between(sysdate, eventStart);
        if (duration.isNegative()) {
            throw new ReservationException(EVENT_STARTED, dto.getTransactionId());
        }
        dto.setEventName(eventDTO.getTitle());
        dto.setStartTime(eventStart);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Reservation reserve(ReservationDTO dto) throws ReservationException {
        log.info("Seat reservation started for eventId: {}, userId: {}, transaction: {}",
                dto.getEventId(), dto.getUserId(), dto.getTransactionId());
        ReservationLock lock = lockService.lock(dto.getEventId(), dto.getUserId(), dto.getTransactionId());
        log.debug("Reservation lock was successfully saved!");
        Reservation reservation;
        try {
            reserveViaPartnerApi(dto);
            payViaCoreApi(dto);
            reservation = saveReservation(dto);
        } finally {
            lockService.unlock(lock);
        }
        return reservation;
    }

    private void payViaCoreApi(ReservationDTO dto) throws ReservationException {
        log.debug("Calling core-api to pay reservation...");
        PaymentRequestDTO request = buildPaymentRequest(dto);
        PaymentResponseDTO response = coreApiClient.pay(request, dto.getToken());
        if (response == null || !Boolean.TRUE.equals(response.getSuccess()) || response.getErrorCode() != null) {
            log.warn("Payment failure, response: {}", response);
            Integer coreApiErrorCode = response != null ? response.getErrorCode() : null;
            ReservationError ticketApiError = getErrorFromCoreApiError(coreApiErrorCode);
            throw new ReservationException(ticketApiError, dto.getTransactionId());
        }
        log.info("Successful payment for seat reservation with transactionId: {}", dto.getTransactionId());
    }

    private static PaymentRequestDTO buildPaymentRequest(ReservationDTO dto) {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setAmount(dto.getAmount());
        paymentRequest.setCurrency(dto.getCurrency());
        paymentRequest.setCardId(dto.getCardId());
        paymentRequest.setUserId(dto.getUserId());
        paymentRequest.setPaymentTransactionId(dto.getTransactionId());
        return paymentRequest;
    }

    private void reserveViaPartnerApi(ReservationDTO dto) throws ReservationException {
        log.debug("Calling Partner to reserve seat...");
        Long reservationId = partnerGateway.reserve(dto.getEventId().toString(), dto.getSeatCode());
        if (reservationId == null) {
            throw new ReservationException(UNKNOWN_ERROR, dto.getTransactionId());
        }
        dto.setReservationId(reservationId);
        log.info("Reservation has been approved by Partner with reservationID: {}", reservationId);
    }

    private Reservation saveReservation(ReservationDTO dto) {
        Reservation reservation = createReservation(dto);
        reservation = reservationGateway.save(reservation);
        return reservation;
    }

    private Reservation createReservation(ReservationDTO dto) {
        return Reservation.builder()
                        .eventId(dto.getEventId())
                        .seatCode(dto.getSeatCode())
                        .userId(dto.getUserId())
                        .status("NEW") // this status has been placed supporting for future features
                        .transactionId(dto.getTransactionId())
                        .build();
    }
}