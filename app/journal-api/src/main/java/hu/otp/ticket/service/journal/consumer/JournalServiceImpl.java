package hu.otp.ticket.service.journal.consumer;

import hu.otp.ticket.service.journal.model.Journal;
import hu.otp.ticket.service.journal.model.JournalDbModel;
import hu.otp.ticket.service.journal.persistentlayer.JournalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class JournalServiceImpl implements JournalService {

    private final JournalRepository journalRepository;

    @Transactional
    @Override
    public void saveJournal(Journal journal) {
        JournalDbModel dbModel = buildDbModel(journal);
        journalRepository.save(dbModel);
    }

    private JournalDbModel buildDbModel(Journal journal) {
        return JournalDbModel.builder()
                            .user(journal.getUser())
                            .application(journal.getApplication())
                            .timestamp(journal.getTimestamp())
                            .type(journal.getType())
                            .content(journal.getContent())
                            .build();
    }
}