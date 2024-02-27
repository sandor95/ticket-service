package hu.otp.ticket.service.journal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @RabbitListener(queues = "${spring.rabbitmq.stream.topic-key}")
    public void listenJournalQueue(String rawJournal) {
        log.info("Received rawJournal entity: {}", rawJournal);
        try {
            Journal journal = objectMapper.readValue(rawJournal, Journal.class);
            log.info("Received time: " + journal.getTimestamp());
        } catch (JsonProcessingException e) {
            log.error("Exception occurred.", e);
        }
    }
}