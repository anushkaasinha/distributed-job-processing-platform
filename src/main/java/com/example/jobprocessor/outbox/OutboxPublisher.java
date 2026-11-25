
package com.example.jobprocessor.outbox;

import com.example.jobprocessor.config.RabbitMQConfig;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxPublisher(
            OutboxRepository outboxRepository,
            RabbitTemplate rabbitTemplate) {

        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                outboxRepository
                        .findTop100ByPublishedFalseOrderByCreatedAtAsc();

        for (OutboxEvent event : events) {

            try {

                System.out.println(
                        "Publishing outbox event: " + event.getId()
                );

                CorrelationData correlationData =
                        new CorrelationData(
                                event.getId().toString()
                        );

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.JOB_QUEUE,
                        event.getJobId(),
                        correlationData
                );

                CorrelationData.Confirm confirm =
                        correlationData.getFuture().get();

                if (confirm.isAck()) {

                    markAsPublished(event.getId());

                    System.out.println(
                            "RabbitMQ confirmed Job: "
                                    + event.getJobId()
                    );

                } else {

                    System.err.println(
                            "RabbitMQ rejected Job: "
                                    + event.getJobId()
                                    + " Reason: "
                                    + confirm.getReason()
                    );
                }

            } catch (Exception e) {

                System.err.println(
                        "Failed to publish event: "
                                + event.getId()
                );

                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void markAsPublished(Long eventId) {

        OutboxEvent event =
                outboxRepository.findById(eventId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Outbox event not found: " + eventId
                                ));

        event.setPublished(true);
        event.setPublishedAt(LocalDateTime.now());

        outboxRepository.save(event);
    }
}
