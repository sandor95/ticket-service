package hu.otp.ticket.service.core.api.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

import hu.otp.ticket.service.core.api.model.PaymentLock;
import hu.otp.ticket.service.core.api.persistentlayer.PaymentLockRepository;
import hu.otp.ticket.service.util.Util;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentLockServiceImplTest {

    static final String PAYMENT_TRANSACTION_ID = "abc-123";
    static final long USER_ID = 34648345234L;
    static final String CARD_ID = "cardId12";
    static final ZonedDateTime SYSDATE = ZonedDateTime.now();

    @Captor
    ArgumentCaptor<PaymentLock> lockCaptor;

    @Mock
    PaymentLockRepository lockRepository;

    @InjectMocks
    PaymentLockServiceImpl lockService;

    @Test
    void shouldLockHappyPath() {
        // given
        PaymentLock mockedLockObject = PaymentLock.builder()
                                            .userId(USER_ID)
                                            .transactionId(PAYMENT_TRANSACTION_ID)
                                            .cardId(CARD_ID)
                                            .createdAt(SYSDATE)
                                            .build();
        when(lockRepository.save(any())).thenReturn(mockedLockObject);

        try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
            utilities.when(Util::sysdate).thenReturn(SYSDATE);

            // when
            PaymentLock persistedLockObject = lockService.lock(USER_ID, CARD_ID, PAYMENT_TRANSACTION_ID);

            // then
            assertEquals(mockedLockObject, persistedLockObject);
            verify(lockRepository, times(1)).save(lockCaptor.capture());
            verifyNoMoreInteractions(lockRepository);

            assertEquals(1, lockCaptor.getAllValues().size());
            PaymentLock actualLock = lockCaptor.getValue();
            assertEquals(USER_ID, actualLock.getUserId());
            assertEquals(CARD_ID, actualLock.getCardId());
            assertEquals(PAYMENT_TRANSACTION_ID, actualLock.getTransactionId());
            assertEquals(SYSDATE, actualLock.getCreatedAt());
        }
    }

    @Test
    void shouldThrowExceptionWhenDatabaseConstraintValidationOccurred() {
        // given
        when(lockRepository.save(any())).thenThrow(new ConstraintViolationException(Collections.emptySet()));

        try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
            utilities.when(Util::sysdate).thenReturn(SYSDATE);
            // when
            assertThrows(ConstraintViolationException.class, () -> lockService.lock(USER_ID, CARD_ID, PAYMENT_TRANSACTION_ID));
        }

        // then
        verify(lockRepository, times(1)).save(lockCaptor.capture());
        verifyNoMoreInteractions(lockRepository);

        assertEquals(1, lockCaptor.getAllValues().size());
        PaymentLock actualLock = lockCaptor.getValue();
        assertEquals(USER_ID, actualLock.getUserId());
        assertEquals(CARD_ID, actualLock.getCardId());
        assertEquals(PAYMENT_TRANSACTION_ID, actualLock.getTransactionId());
        assertEquals(SYSDATE, actualLock.getCreatedAt());
    }

    @Test
    void shouldUnlockWhenLockIsPresentInDatabase() {
        // given
        PaymentLock mockedLockObject = PaymentLock.builder()
                .userId(USER_ID)
                .transactionId(PAYMENT_TRANSACTION_ID)
                .cardId(CARD_ID)
                .createdAt(SYSDATE)
                .build();
        when(lockRepository.findByUserIdAndCardIdAndTransactionId(eq(USER_ID), eq(CARD_ID), eq(PAYMENT_TRANSACTION_ID)))
                .thenReturn(Optional.of(mockedLockObject));

        // when
        lockService.unlock(mockedLockObject);

        // then
        verify(lockRepository, times(1)).findByUserIdAndCardIdAndTransactionId(eq(USER_ID), eq(CARD_ID), eq(PAYMENT_TRANSACTION_ID));
        verify(lockRepository, times(1)).delete(eq(mockedLockObject));
        verifyNoMoreInteractions(lockRepository);
    }

    @Test
    void shouldDoNothingWhenLockIsNotPresentInDatabase() {
        // given
        PaymentLock mockedLockObject = PaymentLock.builder()
                .userId(USER_ID)
                .transactionId(PAYMENT_TRANSACTION_ID)
                .cardId(CARD_ID)
                .createdAt(SYSDATE)
                .build();
        when(lockRepository.findByUserIdAndCardIdAndTransactionId(eq(USER_ID), eq(CARD_ID), eq(PAYMENT_TRANSACTION_ID)))
                .thenReturn(Optional.empty());

        // when
        lockService.unlock(mockedLockObject);

        // then
        verify(lockRepository, times(1)).findByUserIdAndCardIdAndTransactionId(eq(USER_ID), eq(CARD_ID), eq(PAYMENT_TRANSACTION_ID));
        verifyNoMoreInteractions(lockRepository);
    }
}