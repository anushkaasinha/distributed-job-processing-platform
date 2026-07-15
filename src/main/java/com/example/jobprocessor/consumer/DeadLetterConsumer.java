package com.example.jobprocessor.consumer;

import com.example.jobprocessor.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterConsumer {

    @RabbitListener(
            queues = RabbitMQConfig.DEAD_LETTER_QUEUE
    )
    public void receiveFailedJob(Long jobId) {

        System.out.println(
                "Dead Letter Queue received Job: "
                        + jobId
        );

        System.out.println(
                "Manual investigation required."
        );
    }
}