
package com.example.jobprocessor.service;

import com.example.jobprocessor.config.RabbitMQConfig;
import com.example.jobprocessor.dto.CreateJobRequest;
import com.example.jobprocessor.dto.JobStatsResponse;
import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.entity.JobStatus;
import com.example.jobprocessor.entity.Priority;
import com.example.jobprocessor.outbox.OutboxEvent;
import com.example.jobprocessor.outbox.OutboxRepository;
import com.example.jobprocessor.repository.JobRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    private final Random random = new Random();

    public JobService(
            JobRepository jobRepository,
            OutboxRepository outboxRepository,
            RabbitTemplate rabbitTemplate) {

        this.jobRepository = jobRepository;
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Transactional Outbox implementation
    @Transactional
    public Job createJob(CreateJobRequest request) {

        // 1. Create a new job
        Job job = new Job();

        job.setCreatedAt(LocalDateTime.now());
        job.setJobType(request.getJobType());

        job.setPriority(
                Priority.valueOf(
                        request.getPriority().toUpperCase()
                )
        );

        job.setStatus(JobStatus.QUEUED);
        job.setRetryCount(0);
        job.setMaxRetries(3);

        // 2. Save job in MySQL
        Job savedJob = jobRepository.save(job);

        // 3. Create an outbox event
        OutboxEvent event = new OutboxEvent();

        event.setJobId(savedJob.getId());
        event.setEventType("JOB_CREATED");
        event.setPublished(false);
        event.setCreatedAt(LocalDateTime.now());

        // 4. Save outbox event in MySQL
        outboxRepository.save(event);

        // 5. Return saved job
        return savedJob;
    }

    // Process job with consumer idempotency
    public void processJob(Long jobId) {

        // 1. Atomically claim the job
        LocalDateTime startedAt = LocalDateTime.now();

        int updatedRows = jobRepository.claimJob(jobId, startedAt);

        // 2. If no row was updated, another consumer
        // has already claimed or completed this job
        if (updatedRows == 0) {

            System.out.println(
                    "Job " + jobId
                            + " is already claimed or processed. Skipping."
            );

            return;
        }

        // 3. Fetch the claimed job
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found: " + jobId));

        System.out.println(
                Thread.currentThread().getName()
                        + " processing "
                        + job.getPriority()
                        + " Priority Job "
                        + job.getId()
        );

        try {

            Thread.sleep(1000);

            boolean failed = random.nextBoolean();

            if (failed) {

                job.setRetryCount(job.getRetryCount() + 1);

                if (job.getRetryCount() >= job.getMaxRetries()) {

                    job.setStatus(JobStatus.FAILED);

                    jobRepository.save(job);

                    rabbitTemplate.convertAndSend(
                            RabbitMQConfig.DEAD_LETTER_QUEUE,
                            job.getId()
                    );

                    System.out.println(
                            "Job moved to Dead Letter Queue: "
                                    + job.getId()
                    );

                } else {

                    job.setStatus(JobStatus.QUEUED);

                    jobRepository.save(job);

                    System.out.println(
                            "Retrying Job: "
                                    + job.getId()
                                    + " Retry Count: "
                                    + job.getRetryCount()
                    );

                    rabbitTemplate.convertAndSend(
                            RabbitMQConfig.JOB_QUEUE,
                            job.getId()
                    );

                    System.out.println(
                            "Job sent back to RabbitMQ for retry."
                    );
                }

            } else {

                job.setStatus(JobStatus.COMPLETED);

                job.setCompletedAt(LocalDateTime.now());

                long processingTime =
                        Duration.between(
                                job.getStartedAt(),
                                job.getCompletedAt()
                        ).toMillis();

                System.out.println(
                        "Processing Time: "
                                + processingTime
                                + " ms"
                );

                System.out.println(
                        "Completed Job: "
                                + job.getId()
                );

                jobRepository.save(job);
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public JobStatsResponse getStats() {

        long queued =
                jobRepository.countByStatus(JobStatus.QUEUED);

        long processing =
                jobRepository.countByStatus(JobStatus.PROCESSING);

        long completed =
                jobRepository.countByStatus(JobStatus.COMPLETED);

        long failed =
                jobRepository.countByStatus(JobStatus.FAILED);

        List<Job> completedJobs =
                jobRepository.findByStatus(JobStatus.COMPLETED);

        long totalProcessingTime = 0;

        for (Job job : completedJobs) {

            if (job.getStartedAt() != null &&
                    job.getCompletedAt() != null) {

                totalProcessingTime +=
                        Duration.between(
                                job.getStartedAt(),
                                job.getCompletedAt()
                        ).toMillis();
            }
        }

        long averageProcessingTime = 0;

        if (!completedJobs.isEmpty()) {

            averageProcessingTime =
                    totalProcessingTime / completedJobs.size();
        }

        double successRate = 0;

        if ((completed + failed) > 0) {

            successRate =
                    ((double) completed /
                            (completed + failed))
                            * 100;
        }

        return new JobStatsResponse(
                queued,
                processing,
                completed,
                failed,
                successRate,
                averageProcessingTime
        );
    }
}
