package hu.otp.partner.reservation;

import static hu.otp.partner.knownobject.Events._30Y_CONCERT;
import static hu.otp.partner.knownobject.Tickets._30Y_CONCERT_S2;
import static hu.otp.partner.knownobject.Tickets._30Y_CONCERT_S3;
import static hu.otp.partner.reservation.exception.ReservationError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import hu.otp.partner.common.model.Event;
import hu.otp.partner.common.model.Ticket;
import hu.otp.partner.persistentlayer.EventPersistentLayerGateway;
import hu.otp.partner.persistentlayer.TicketPersistentLayerGateway;
import hu.otp.partner.reservation.exception.ReservationException;
import hu.otp.ticket.service.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final Event EVENT = _30Y_CONCERT;

    private static final ZonedDateTime BEFORE_EVENT_TIME = EVENT.getStartTime().plusHours(-2L);

    private static final ZonedDateTime DURING_EVENT_TIME = EVENT.getStartTime().plusHours(2L);

    private static final ZonedDateTime AFTER_EVENT_TIME = EVENT.getEndTime().plusHours(2L);

    private static final Long EVENT_ID = EVENT.getId();

    private static final String SEAT_CODE = _30Y_CONCERT_S2.getSeat().getSeatCode();

    @Mock
    TicketPersistentLayerGateway ticketGateway;

    @Mock
    EventPersistentLayerGateway eventGateway;

    @InjectMocks
    ReservationService service;

    Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = Ticket.builder()
                        .event(_30Y_CONCERT_S2.getEvent())
                        .price(_30Y_CONCERT_S2.getPrice())
                        .currency(_30Y_CONCERT_S2.getCurrency())
                        .seat(_30Y_CONCERT_S2.getSeat())
                        .build();
    }

    @ParameterizedTest
    @MethodSource("momentsWhenTicketReservationAllowed")
    void shouldReserveWhenSeatIsAvailable(ZonedDateTime sysdate) throws ReservationException {
        // given
        Long expectedReservationId = 345324L;
        when(eventGateway.getById(eq(EVENT_ID))).thenReturn(Optional.of(EVENT));
        when(ticketGateway.getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(SEAT_CODE))).thenReturn(Optional.of(ticket));
        when(ticketGateway.isReservationIdAlreadyExists(eq(expectedReservationId))).thenReturn(false);

        try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
            utilities.when(Util::nextReservationId).thenReturn(expectedReservationId);
            utilities.when(Util::sysdate).thenReturn(sysdate);

            // when
            Long actualReservationId = service.reserveSeat(EVENT_ID, SEAT_CODE);

            // then
            assertEquals(expectedReservationId, actualReservationId);
        }

        verify(eventGateway, times(1)).getById(eq(EVENT_ID));
        verify(ticketGateway, times(1)).getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(SEAT_CODE));
        verify(ticketGateway, times(1)).isReservationIdAlreadyExists(eq(expectedReservationId));
        verify(ticketGateway, times(1)).saveTicket(eq(ticket));
        verifyNoMoreInteractions(ticketGateway);
        verifyNoMoreInteractions(eventGateway);
    }

    @Test
    void shouldReserveWhenSeatIsAvailable_MultipleTryDuringReservationIdGeneration() throws ReservationException {
        // given
        Long reservedId1 = 123L;
        Long reservedId2 = 321L;
        Long expectedReservationId = 678654332L;
        when(eventGateway.getById(eq(EVENT_ID))).thenReturn(Optional.of(EVENT));
        when(ticketGateway.getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(SEAT_CODE))).thenReturn(Optional.of(ticket));
        when(ticketGateway.isReservationIdAlreadyExists(any())).thenReturn(true).thenReturn(true).thenReturn(false);

        try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
            utilities.when(Util::nextReservationId).thenReturn(reservedId1).thenReturn(reservedId2).thenReturn(expectedReservationId);
            utilities.when(Util::sysdate).thenReturn(BEFORE_EVENT_TIME);

            // when
            Long actualReservationId = service.reserveSeat(EVENT_ID, SEAT_CODE);

            // then
            assertEquals(expectedReservationId, actualReservationId);
        }

        verify(eventGateway, times(1)).getById(eq(EVENT_ID));
        verify(ticketGateway, times(1)).getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(SEAT_CODE));
        verify(ticketGateway, times(1)).isReservationIdAlreadyExists(eq(reservedId1));
        verify(ticketGateway, times(1)).isReservationIdAlreadyExists(eq(reservedId2));
        verify(ticketGateway, times(1)).isReservationIdAlreadyExists(eq(expectedReservationId));
        verify(ticketGateway, times(1)).saveTicket(eq(ticket));
        verifyNoMoreInteractions(ticketGateway);
        verifyNoMoreInteractions(eventGateway);
    }

    @Test
    void shouldThrowExceptionWhenEventIsFinished() {
        // given
        String seatCode = ticket.getSeat().getSeatCode();
        when(eventGateway.getById(eq(EVENT_ID))).thenReturn(Optional.of(EVENT));

        try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
            utilities.when(Util::sysdate).thenReturn(AFTER_EVENT_TIME);

            // when
            ReservationException exception = assertThrows(ReservationException.class, () -> service.reserveSeat(EVENT_ID, seatCode));
            assertEquals(EVENT_ALREADY_FINISHED, exception.getReservationError());
        }

        // then
        verify(eventGateway, times(1)).getById(eq(EVENT_ID));
        verifyNoMoreInteractions(ticketGateway);
        verifyNoMoreInteractions(eventGateway);
    }

    @Test
    void shouldThrowExceptionWhenEventIsNotExist() {
        // given
        String seatCode = ticket.getSeat().getSeatCode();
        when(eventGateway.getById(eq(EVENT_ID))).thenReturn(Optional.empty());

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> service.reserveSeat(EVENT_ID, seatCode));
        assertEquals(INVALID_EVENT_ID, exception.getReservationError());

        // then
        verify(eventGateway, times(1)).getById(eq(EVENT_ID));
        verifyNoMoreInteractions(ticketGateway);
        verifyNoMoreInteractions(eventGateway);
    }

    @Test
    void shouldThrowExceptionWhenSeatIsNotExist() {
        // given
        String seatCode = ticket.getSeat().getSeatCode();
        when(eventGateway.getById(eq(EVENT_ID))).thenReturn(Optional.of(EVENT));
        when(ticketGateway.getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(seatCode))).thenReturn(Optional.empty());

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> service.reserveSeat(EVENT_ID, seatCode));

        // then
        assertEquals(INVALID_SEAT_ID, exception.getReservationError());
        verify(eventGateway, times(1)).getById(eq(EVENT_ID));
        verify(ticketGateway, times(1)).getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(seatCode));
        verifyNoMoreInteractions(ticketGateway);
        verifyNoMoreInteractions(eventGateway);
    }

    @Test
    void shouldThrowExceptionWhenSeatIsReserved() {
        // given
        String seatCode = ticket.getSeat().getSeatCode();
        when(eventGateway.getById(eq(EVENT_ID))).thenReturn(Optional.of(EVENT));
        when(ticketGateway.getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(seatCode))).thenReturn(Optional.of(_30Y_CONCERT_S3));

        // when
        ReservationException exception = assertThrows(ReservationException.class, () -> service.reserveSeat(EVENT_ID, seatCode));

        // then
        assertEquals(SEAT_RESERVED, exception.getReservationError());
        verify(eventGateway, times(1)).getById(eq(EVENT_ID));
        verify(ticketGateway, times(1)).getTicketByEventIdAndSeatCode(eq(EVENT_ID), eq(seatCode));
        verifyNoMoreInteractions(ticketGateway);
        verifyNoMoreInteractions(eventGateway);
    }

    public static Stream<Arguments> momentsWhenTicketReservationAllowed() {
        return Stream.of(
                Arguments.of(BEFORE_EVENT_TIME),
                Arguments.of(DURING_EVENT_TIME)
        );
    }
}