package com.example.jobprocessor.consumer;

import com.example.jobprocessor.config.RabbitMQConfig;
import com.example.jobprocessor.service.JobService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class JobConsumer {

    private final JobService jobService;

    public JobConsumer(JobService jobService) {
        this.jobService = jobService;
    }

    @RabbitListener(queues = RabbitMQConfig.JOB_QUEUE,concurrency = "3")
    public void receiveJob(Long jobId) {

       System.out.println(
        Thread.currentThread().getName()
                + " received Job "
                + jobId
);

        jobService.processJob(jobId);
    }
}