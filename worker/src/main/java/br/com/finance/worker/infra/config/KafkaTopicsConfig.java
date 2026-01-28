package br.com.finance.worker.infra.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic transactionDlqTopic(){
        return TopicBuilder.name("transaction.dlq")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
