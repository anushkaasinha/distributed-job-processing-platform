
package com.example.jobprocessor.outbox;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public OutboxService(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public void markAsPublished(Long eventId) {

        OutboxEvent event = outboxRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Outbox event not found: " + eventId
                        ));

        event.setPublished(true);
        event.setPublishedAt(LocalDateTime.now());

        outboxRepository.save(event);
    }
}
