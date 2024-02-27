package hu.otp.ticket.service.journal.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.time.ZonedDateTime;

import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalDbModel;
import hu.otp.ticket.service.journal.model.JournalType;
import hu.otp.ticket.service.journal.persistentlayer.JournalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JournalServiceImplTest {

    @Mock
    JournalRepository journalRepository;

    @InjectMocks
    JournalServiceImpl journalService;

    @Captor
    ArgumentCaptor<JournalDbModel> dbModelCaptor;

    @Test
    void shouldSaveJournal() {
        Journal journal = Journal.builder()
                                .content("My journal Content")
                                .timestamp(ZonedDateTime.now())
                                .application("APP-NAME")
                                .user("johnny")
                                .type(JournalType.TOKEN_VALIDATION)
                                .build();
        when(journalRepository.save(any())).thenReturn(null);

        journalService.saveJournal(journal);

        verify(journalRepository, times(1)).save(dbModelCaptor.capture());
        assertEquals(1, dbModelCaptor.getAllValues().size());
        JournalDbModel savedJournal = dbModelCaptor.getValue();
        assertEquals(journal.getApplication(), savedJournal.getApplication());
        assertEquals(journal.getContent(), savedJournal.getContent());
        assertEquals(journal.getTimestamp(), savedJournal.getTimestamp());
        assertEquals(journal.getType(), savedJournal.getType());
        assertEquals(journal.getUser(), savedJournal.getUser());
    }
}