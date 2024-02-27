package hu.otp.ticket.service.journal;

import static hu.otp.ticket.service.Const.BUDAPEST_ZONE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

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
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class ExternalJournalServiceTest {

    static final String ROUTING_KEY = "ROUTE-1";

    static final String TOPIC_KEY = "TOPIC-12";

    @Mock
    RabbitTemplate rabbitTemplate;

    @Captor
    ArgumentCaptor<String> rabbitMessageCaptor;

    final ObjectMapper objectMapper = new ObjectMapper()
                                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                                    .findAndRegisterModules();

    JournalService journalService;

    @BeforeEach
    void setUp() {
        journalService = new ExternalJournalService(objectMapper, rabbitTemplate, ROUTING_KEY, TOPIC_KEY);
    }

    @Test
    void shouldSendJournalAsString() {
        String expectedRawJournal = "{\"user\":\"myUser\",\"application\":\"APP\",\"timestamp\":\"2024-02-27T20:23:21+01:00\""
                                    + ",\"type\":\"SEAT_RESERVATION\",\"content\":\"my string content\"}";
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 27, 20, 23, 21);
        ZonedDateTime timestamp = ZonedDateTime.of(dateTime, BUDAPEST_ZONE_ID);
        Journal journal = Journal.builder()
                                .application("APP")
                                .user("myUser")
                                .timestamp(timestamp)
                                .type(JournalType.SEAT_RESERVATION)
                                .content("my string content")
                                .build();

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), rabbitMessageCaptor.capture());

        journalService.save(journal);

        assertEquals(1, rabbitMessageCaptor.getAllValues().size());
        assertEquals(expectedRawJournal, rabbitMessageCaptor.getValue());
    }

    @Test
    void shouldDoNothingWhenExceptionOccurredDuringSending() {
        String expectedRawJournal = "{\"user\":\"myUser\",\"application\":\"APP\",\"timestamp\":\"2024-02-27T20:23:21+01:00\""
                + ",\"type\":\"SEAT_RESERVATION\",\"content\":\"my string content\"}";
        LocalDateTime dateTime = LocalDateTime.of(2024, 2, 27, 20, 23, 21);
        ZonedDateTime timestamp = ZonedDateTime.of(dateTime, BUDAPEST_ZONE_ID);
        Journal journal = Journal.builder()
                .application("APP")
                .user("myUser")
                .timestamp(timestamp)
                .type(JournalType.SEAT_RESERVATION)
                .content("my string content")
                .build();

        doThrow(RuntimeException.class).when(rabbitTemplate).convertAndSend(anyString(), anyString(), rabbitMessageCaptor.capture());

        journalService.save(journal);

        assertEquals(1, rabbitMessageCaptor.getAllValues().size());
        assertEquals(expectedRawJournal, rabbitMessageCaptor.getValue());
    }
}