package hu.otp.ticket.service.ticket.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.otp.ticket.service.journal.ExternalJournalService;
import hu.otp.ticket.service.journal.JournalService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JournalConfiguration {

    @Bean
    public JournalService journalService(@Value("${spring.rabbitmq.stream.routing-key}") String routingKey,
                                         @Value("${spring.rabbitmq.stream.topic-key}") String topicKey,
                                         RabbitTemplate rabbitTemplate) {
        ObjectMapper objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .findAndRegisterModules();
        return new ExternalJournalService(objectMapper, rabbitTemplate, routingKey, topicKey);
    }
}