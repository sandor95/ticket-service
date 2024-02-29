package hu.otp.ticket.service.ticket.api.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    private static final ZonedDateTime SYSDATE = ZonedDateTime.now();
    private static final Long EVENT_ID = 1L;
    private static final Long USER_ID = 2L;
    private static final String CARD_ID = "C1234";
    private static final String TRANSACTION_ID = "my-secret-transaction";
    private static final String SEAT_CODE = "S7";
    private static final String TOKEN = "this-is-a-very-secure-token";
    public static final int PRICE = 50;
    public static final String CURRENCY = "HUF";
    public static final long RESERVATION_ID = 123L;
    public static final int PAYMENT_ERROR_CODE = 1234;

    @Mock
    private PartnerGateway partnerGateway;

    @Mock
    private CoreApiClient coreApiClient;

    @Mock
    private ReservationLockService lockService;

    @Mock
    private ReservationGateway reservationGateway;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    public static Stream<Arguments> coreApiFailurePaymentResponses() {
        PaymentResponseDTO successResponseWithErrorCode = createPaymentResponse(true);
        successResponseWithErrorCode.setErrorCode(PAYMENT_ERROR_CODE);
        return Stream.of(
                Arguments.of(createPaymentResponse(null)),
                Arguments.of(createPaymentResponse(false)),
                Arguments.of(successResponseWithErrorCode)
        );
    }

    @Test
    void shouldDoNothingWhenSeatIsNotReserved() throws ReservationException {
        // given
        String expectedEventTitle = "event title";
        ReservationDTO reservationDTO = createReservationDTO(null, null);
        EventDTO eventDTO = createEventDTO();
        eventDTO.setTitle(expectedEventTitle);
        EventDetailsDTO eventDetails = createEventDetails(false);
        ZonedDateTime expectedStarTime = Util.parseFromUtc(eventDTO.getStartTimeStamp());
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));
        when(partnerGateway.getEvent(eq(EVENT_ID))).thenReturn(Optional.of(eventDetails));

        // when
        reservationService.checkSeatAvailable(reservationDTO);

        // then
        assertEquals(PRICE, reservationDTO.getAmount());
        assertEquals(CURRENCY, reservationDTO.getCurrency());
        assertEquals(expectedEventTitle, reservationDTO.getEventName());
        assertEquals(expectedStarTime, reservationDTO.getStartTime());
        verify(partnerGateway, times(1)).getAllEvents();
        verify(partnerGateway, times(1)).getEvent(eq(EVENT_ID));
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenSeatIsReserved() {
        // given
        EventDTO eventDTO = createEventDTO();
        EventDetailsDTO eventDetails = createEventDetails(true);
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));
        when(partnerGateway.getEvent(eq(EVENT_ID))).thenReturn(Optional.of(eventDetails));

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.SEAT_RESERVED, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verify(partnerGateway, times(1)).getEvent(eq(EVENT_ID));
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenSeatNotExists() {
        // given
        EventDTO eventDTO = createEventDTO();
        EventDetailsDTO eventDetails = createEventDetails(true);
        eventDetails.getSeats().get(0).setId("some-other-code");
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));
        when(partnerGateway.getEvent(eq(EVENT_ID))).thenReturn(Optional.of(eventDetails));

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.SEAT_NOT_EXISTS, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verify(partnerGateway, times(1)).getEvent(eq(EVENT_ID));
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenSeatListIsEmpty() {
        // given
        EventDTO eventDTO = createEventDTO();
        EventDetailsDTO eventDetails = createEventDetails(true);
        eventDetails.setSeats(Collections.emptyList());
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));
        when(partnerGateway.getEvent(eq(EVENT_ID))).thenReturn(Optional.of(eventDetails));

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.SEAT_NOT_EXISTS, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verify(partnerGateway, times(1)).getEvent(eq(EVENT_ID));
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenSeatListIsNull() {
        // given
        EventDTO eventDTO = createEventDTO();
        EventDetailsDTO eventDetails = createEventDetails(true);
        eventDetails.setSeats(null);
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));
        when(partnerGateway.getEvent(eq(EVENT_ID))).thenReturn(Optional.of(eventDetails));

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.SEAT_NOT_EXISTS, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verify(partnerGateway, times(1)).getEvent(eq(EVENT_ID));
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenEventDetailsResponseIsEmpty() {
        // given
        EventDTO eventDTO = createEventDTO();
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));
        when(partnerGateway.getEvent(eq(EVENT_ID))).thenReturn(Optional.empty());

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.EVENT_NOT_EXISTS, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verify(partnerGateway, times(1)).getEvent(eq(EVENT_ID));
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenEventHasStarted() {
        // given
        EventDTO eventDTO = createEventDTO();
        eventDTO.setStartTimeStamp(SYSDATE.plusMinutes(-1L).toInstant().toEpochMilli());
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.EVENT_STARTED, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenEventNotExists() {
        // given
        EventDTO eventDTO = createEventDTO();
        eventDTO.setEventId(1234567L);
        eventDTO.setStartTimeStamp(SYSDATE.plusMinutes(-1L).toInstant().toEpochMilli());
        when(partnerGateway.getAllEvents()).thenReturn(Optional.of(List.of(eventDTO)));

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.EVENT_NOT_EXISTS, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldThrowExceptionWhenAllEventsResponseIsEmpty() {
        // given
        when(partnerGateway.getAllEvents()).thenReturn(Optional.empty());

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.checkSeatAvailable(createReservationDTO(null, null)));

        // then
        assertEquals(ReservationError.EVENT_NOT_EXISTS, exception.getReservationError());
        assertEquals(TRANSACTION_ID, exception.getTransactionId());
        verify(partnerGateway, times(1)).getAllEvents();
        verifyNoMoreInteractions(partnerGateway);
    }

    @Test
    void shouldReturnWithReservationDetailsAfterSuccessfulSeatReservation() throws ReservationException {
        // given
        ArgumentCaptor<PaymentRequestDTO> paymentCaptor = ArgumentCaptor.forClass(PaymentRequestDTO.class);
        ReservationLock lock = new ReservationLock();
        when(lockService.lock(EVENT_ID, USER_ID, TRANSACTION_ID)).thenReturn(lock);

        PaymentResponseDTO paymentResponse = createPaymentResponse(true);
        when(coreApiClient.pay(any(PaymentRequestDTO.class), eq(TOKEN))).thenReturn(paymentResponse);

        when(partnerGateway.reserve(EVENT_ID.toString(), SEAT_CODE)).thenReturn(RESERVATION_ID);

        when(reservationGateway.save(any(Reservation.class))).thenAnswer(iom -> iom.getArgument(0));

        // when
        ReservationDTO reservationDTO = createReservationDTO((long) PRICE, CURRENCY);
        Reservation result = reservationService.reserve(reservationDTO);

        // then
        assertNotNull(result);
        assertEquals(RESERVATION_ID, reservationDTO.getReservationId());
        verify(lockService, times(1)).lock(EVENT_ID, USER_ID, TRANSACTION_ID);
        verify(coreApiClient, times(1)).pay(paymentCaptor.capture(), eq(TOKEN));
        verify(partnerGateway, times(1)).reserve(EVENT_ID.toString(), SEAT_CODE);
        verify(reservationGateway, times(1)).save(any(Reservation.class));
        verify(lockService, times(1)).unlock(lock);
        verifyNoMoreInteractions(lockService);
        verifyNoMoreInteractions(coreApiClient);
        verifyNoMoreInteractions(partnerGateway);
        verifyNoMoreInteractions(reservationGateway);

        assertEquals(1, paymentCaptor.getAllValues().size());
        PaymentRequestDTO capturedPayment = paymentCaptor.getValue();
        assertEquals(PRICE, capturedPayment.getAmount());
        assertEquals(CURRENCY, capturedPayment.getCurrency());
        assertEquals(CARD_ID, capturedPayment.getCardId());
        assertEquals(USER_ID, capturedPayment.getUserId());
        assertEquals(TRANSACTION_ID, capturedPayment.getPaymentTransactionId());
    }

    @Test
    void shouldThrowExceptionWhenSeatReservationFailsAtPartner() {
        // given
        ReservationLock lock = new ReservationLock();
        when(lockService.lock(EVENT_ID, USER_ID, TRANSACTION_ID)).thenReturn(lock);

        when(partnerGateway.reserve(EVENT_ID.toString(), SEAT_CODE)).thenReturn(null);

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.reserve(createReservationDTO((long) PRICE, CURRENCY)));

        // then
        assertEquals(ReservationError.UNKNOWN_ERROR, exception.getReservationError());
        verify(lockService, times(1)).lock(EVENT_ID, USER_ID, TRANSACTION_ID);
        verify(partnerGateway, times(1)).reserve(EVENT_ID.toString(), SEAT_CODE);
        verify(lockService, times(1)).unlock(lock);
        verifyNoMoreInteractions(lockService);
        verifyNoMoreInteractions(partnerGateway);
        verifyNoInteractions(coreApiClient);
        verifyNoInteractions(reservationGateway);
    }

    @ParameterizedTest
    @NullSource
    @MethodSource("coreApiFailurePaymentResponses")
    void shouldThrowExceptionWhenPaymentFailsAtCoreApi(PaymentResponseDTO coreApiResponse) {
        // given
        ReservationLock lock = new ReservationLock();
        when(lockService.lock(EVENT_ID, USER_ID, TRANSACTION_ID)).thenReturn(lock);

        when(coreApiClient.pay(any(PaymentRequestDTO.class), eq(TOKEN))).thenReturn(coreApiResponse);
        when(partnerGateway.reserve(EVENT_ID.toString(), SEAT_CODE)).thenReturn(RESERVATION_ID);

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> reservationService.reserve(createReservationDTO((long) PRICE, CURRENCY)));

        // then
        assertEquals(ReservationError.UNKNOWN_ERROR, exception.getReservationError());
        verify(lockService, times(1)).lock(EVENT_ID, USER_ID, TRANSACTION_ID);
        verify(coreApiClient, times(1)).pay(any(PaymentRequestDTO.class), eq(TOKEN));
        verify(partnerGateway, times(1)).reserve(EVENT_ID.toString(), SEAT_CODE);
        verify(lockService, times(1)).unlock(lock);
        verifyNoMoreInteractions(lockService);
        verifyNoMoreInteractions(coreApiClient);
        verifyNoMoreInteractions(partnerGateway);
        verifyNoInteractions(reservationGateway);
    }

    private ReservationDTO createReservationDTO(Long amount, String currency) {
        return ReservationDTO.builder()
                            .eventId(EVENT_ID)
                            .userId(USER_ID)
                            .cardId(CARD_ID)
                            .amount(amount)
                            .currency(currency)
                            .transactionId(TRANSACTION_ID)
                            .seatCode(SEAT_CODE)
                            .token(TOKEN)
                            .build();
    }

    private EventDTO createEventDTO() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventId(EVENT_ID);
        eventDTO.startTimeStamp(SYSDATE.plusDays(1).toInstant().toEpochMilli());
        return eventDTO;
    }

    private EventDetailsDTO createEventDetails(boolean seatReserved) {
        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setId(SEAT_CODE);
        seatDTO.setReserved(seatReserved);
        seatDTO.setPrice(PRICE);
        seatDTO.setCurrency(CURRENCY);

        EventDetailsDTO eventDetailsDTO = new EventDetailsDTO();
        eventDetailsDTO.setSeats(Collections.singletonList(seatDTO));

        return eventDetailsDTO;
    }

    private static PaymentResponseDTO createPaymentResponse(Boolean success) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setSuccess(success);
        if (success != null && !success) {
            response.setErrorCode(PAYMENT_ERROR_CODE);
        }
        return response;
    }
}