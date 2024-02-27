package hu.otp.ticket.service.journal.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.otp.ticket.service.journal.model.Journal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class QueueListener {

    private final ObjectMapper objectMapper;

    private final JournalService journalService;

    @RabbitListener(queues = "${spring.rabbitmq.stream.topic-key}")
    public void listenJournalQueue(String rawJournal) {
        log.trace("Received rawJournal entity: {}", rawJournal);
        try {
            Journal journal = objectMapper.readValue(rawJournal, Journal.class);
            journalService.saveJournal(journal);
        } catch (Exception e) {
            log.error("Exception occurred during new journal record processing", e);
        }
    }
}