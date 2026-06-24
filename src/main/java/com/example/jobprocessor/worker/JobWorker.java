package com.example.jobprocessor.worker;

import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.entity.JobStatus;
import com.example.jobprocessor.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class JobWorker {

    private final JobRepository jobRepository;
    private final Random random = new Random();

    public JobWorker(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void processJobs() {

        List<Job> jobs = jobRepository.findByStatus(JobStatus.QUEUED);

        for (Job job : jobs) {

            System.out.println("Processing Job: " + job.getId());

            job.setStatus(JobStatus.PROCESSING);
            jobRepository.save(job);

            try {
                Thread.sleep(3000);

                boolean failed = random.nextBoolean();

                if (failed) {

                    job.setRetryCount(job.getRetryCount() + 1);

                    if (job.getRetryCount() >= job.getMaxRetries()) {

                        job.setStatus(JobStatus.FAILED);

                        System.out.println(
                                "Job Failed Permanently: " + job.getId()
                        );

                    } else {

                        job.setStatus(JobStatus.QUEUED);

                        System.out.println(
                                "Retrying Job: " + job.getId()
                                        + " Retry Count: "
                                        + job.getRetryCount()
                        );
                    }

                } else {

                    job.setStatus(JobStatus.COMPLETED);

                    System.out.println(
                            "Completed Job: " + job.getId()
                    );
                }

                jobRepository.save(job);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}