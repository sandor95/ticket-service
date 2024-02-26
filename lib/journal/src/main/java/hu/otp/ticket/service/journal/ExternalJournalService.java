package hu.otp.ticket.service.journal;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
@RequiredArgsConstructor
public class ExternalJournalService implements JournalService {

    private final ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;

    private final String routingKey;

    private final String topicKey;

    @Override
    public void save(Journal journal) {
        try {
            String rawJournal = objectMapper.writeValueAsString(journal);
            log.debug("Sending journal to MQ...");
            rabbitTemplate.convertAndSend(routingKey, topicKey, rawJournal);
            log.debug("Journal message sending was successful");
        } catch (Exception ex) {
            log.error("Error occurred during sending journal content", ex);
        }
    }
}