package hu.otp.ticket.service.journal.consumer;

import static hu.otp.ticket.service.Const.BUDAPEST_ZONE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueueListenerTest {

    @Mock
    JournalService journalService;

    @Captor
    ArgumentCaptor<Journal> journalCaptor;

    final ObjectMapper objectMapper = new ObjectMapper()
                                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                                    .findAndRegisterModules();

    QueueListener queueListener;

    @BeforeEach
    void setUp() {
        queueListener = new QueueListener(objectMapper, journalService);
    }

    @Test
    void shouldSendJournalAsString() {
        String rawJournal = "{\"user\":\"myUser\",\"application\":\"APP\",\"timestamp\":\"2024-02-27T20:23:21+01:00\""
                + ",\"type\":\"SEAT_RESERVATION\",\"content\":\"my string content\"}";
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 27, 20, 23, 21);
        ZonedDateTime timestamp = ZonedDateTime.of(dateTime, BUDAPEST_ZONE_ID);
        Journal expectedJournal = Journal.builder()
                .application("APP")
                .user("myUser")
                .timestamp(timestamp)
                .type(JournalType.SEAT_RESERVATION)
                .content("my string content")
                .build();

        doNothing().when(journalService).saveJournal(any());

        queueListener.listenJournalQueue(rawJournal);

        verify(journalService, times(1)).saveJournal(journalCaptor.capture());
        assertEquals(1, journalCaptor.getAllValues().size());
        assertEquals(expectedJournal.getContent(), journalCaptor.getValue().getContent());
    }

    @Test
    void shouldDoNothingWhenExceptionOccurredDuringSending() {
        String rawJournal = "{\"user\":\"myUser\",\"application\":\"APP\",\"timestamp\":\"2024-02-27T20:23:21+01:00\""
                + ",\"type\":\"SEAT_RESERVATION\",\"content\":\"my string content\"}";
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 27, 20, 23, 21);
        ZonedDateTime timestamp = ZonedDateTime.of(dateTime, BUDAPEST_ZONE_ID);
        Journal expectedJournal = Journal.builder()
                                        .application("APP")
                                        .user("myUser")
                                        .timestamp(timestamp)
                                        .type(JournalType.SEAT_RESERVATION)
                                        .content("my string content")
                                        .build();

        doThrow(RuntimeException.class).when(journalService).saveJournal(any());

        queueListener.listenJournalQueue(rawJournal);

        verify(journalService, times(1)).saveJournal(journalCaptor.capture());
        assertEquals(1, journalCaptor.getAllValues().size());
        assertEquals(expectedJournal.getContent(), journalCaptor.getValue().getContent());
    }
}