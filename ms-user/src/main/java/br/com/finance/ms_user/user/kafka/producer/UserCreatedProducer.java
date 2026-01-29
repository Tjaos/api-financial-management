package br.com.finance.ms_user.user.kafka.producer;

import br.com.finance.ms_user.user.kafka.event.UserCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedProducer {

    private static final String TOPIC = "User.created";

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public UserCreatedProducer(KafkaTemplate<String, UserCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(UserCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId().toString(), event);
    }
}
