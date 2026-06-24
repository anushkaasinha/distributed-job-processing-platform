package com.example.jobprocessor.service;

import com.example.jobprocessor.dto.CreateJobRequest;
import com.example.jobprocessor.dto.JobStatsResponse;
import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.entity.JobStatus;
import com.example.jobprocessor.entity.Priority;
import com.example.jobprocessor.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job createJob(CreateJobRequest request) {

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

        return jobRepository.save(job);
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
                    totalProcessingTime
                            / completedJobs.size();
        }

        double successRate = 0;

        if ((completed + failed) > 0) {

            successRate =
                    ((double) completed
                            / (completed + failed))
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