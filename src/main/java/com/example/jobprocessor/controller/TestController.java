
package com.example.jobprocessor.controller;

import com.example.jobprocessor.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final RabbitTemplate rabbitTemplate;

    public TestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/duplicate/{jobId}")
    public String publishDuplicate(@PathVariable Long jobId) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.JOB_QUEUE,
                jobId
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.JOB_QUEUE,
                jobId
        );

        return "Published duplicate messages for Job ID: " + jobId;
    }
}
