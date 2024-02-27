package hu.otp.ticket.service.journal.consumer;

import hu.otp.ticket.service.journal.model.Journal;

public interface JournalService {

    void saveJournal(Journal journal);
}