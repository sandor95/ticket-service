package hu.otp.ticket.service.ticket.api.reservation;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Optional;

import hu.otp.ticket.service.ticket.api.model.ReservationLock;
import hu.otp.ticket.service.ticket.api.persistentlayer.ReservationLockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationLockServiceImplTest {

    private static final long EVENT_ID = 1L;
    private static final Long USER_ID = 2L;
    private static final String TRANSACTION_ID = "my-lock-transaction";

    @Mock
    private ReservationLockRepository lockRepository;

    @InjectMocks
    private ReservationLockServiceImpl reservationLockService;

    @Test
    void shouldLockWhenLockNotExistsForUserAndEvent() {
        when(lockRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ReservationLock result = reservationLockService.lock(EVENT_ID, USER_ID, TRANSACTION_ID);

        assertNotNull(result);
        assertEquals(EVENT_ID, result.getEventId());
        assertEquals(USER_ID, result.getUserId());
        assertEquals(TRANSACTION_ID, result.getTransactionId());
        assertNotNull(result.getCreatedAt());

        verify(lockRepository, times(1)).save(any());
        verifyNoMoreInteractions(lockRepository);
    }

    @Test
    void shouldUnlockWhenLockExists() {
        ReservationLock lock = ReservationLock.builder()
                                            .eventId(EVENT_ID)
                                            .userId(USER_ID)
                                            .transactionId(TRANSACTION_ID)
                                            .createdAt(ZonedDateTime.now())
                                            .build();

        when(lockRepository.findByUserIdAndEventId(eq(USER_ID), eq(EVENT_ID))).thenReturn(Optional.of(lock));

        reservationLockService.unlock(lock);

        verify(lockRepository, times(1)).delete(eq(lock));
        verifyNoMoreInteractions(lockRepository);
    }

    @Test
    void shouldDoNothingWhenLockNotExists() {
        ReservationLock lock = ReservationLock.builder()
                                            .eventId(EVENT_ID)
                                            .userId(USER_ID)
                                            .transactionId(TRANSACTION_ID)
                                            .createdAt(ZonedDateTime.now())
                                            .build();

        when(lockRepository.findByUserIdAndEventId(eq(USER_ID), eq(EVENT_ID))).thenReturn(Optional.empty());

        reservationLockService.unlock(lock);

        verify(lockRepository, times(1)).findByUserIdAndEventId(eq(USER_ID), eq(EVENT_ID));
        verifyNoMoreInteractions(lockRepository);
    }
}