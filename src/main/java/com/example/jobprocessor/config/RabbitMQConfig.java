package com.example.jobprocessor.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String JOB_QUEUE = "jobQueue";

    public static final String DEAD_LETTER_QUEUE = "deadLetterQueue";

    @Bean
    public Queue jobQueue() {
        return new Queue(JOB_QUEUE, true);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }
}