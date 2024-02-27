package hu.otp.ticket.service.journal.persistentlayer;

import hu.otp.ticket.service.journal.model.JournalDbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<JournalDbModel, Long> {
}