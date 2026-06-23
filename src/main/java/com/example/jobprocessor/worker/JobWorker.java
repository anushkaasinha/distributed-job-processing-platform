package com.example.jobprocessor.worker;

import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.entity.JobStatus;
import com.example.jobprocessor.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobWorker {

    private final JobRepository jobRepository;

    public JobWorker(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void processJobs() {

        List<Job> jobs = jobRepository.findAll();

        for (Job job : jobs) {

            if (job.getStatus() == JobStatus.QUEUED) {

                System.out.println("Processing Job: " + job.getId());

                job.setStatus(JobStatus.PROCESSING);
                jobRepository.save(job);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                job.setStatus(JobStatus.COMPLETED);
                jobRepository.save(job);

                System.out.println("Completed Job: " + job.getId());
            }
        }
    }
}