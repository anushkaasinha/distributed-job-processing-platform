package com.example.jobprocessor.service;
import java.util.List;
import com.example.jobprocessor.dto.CreateJobRequest;
import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.repository.JobRepository;
import org.springframework.stereotype.Service;
import com.example.jobprocessor.entity.JobStatus;
import com.example.jobprocessor.entity.Priority;

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

        job.setJobType(request.getJobType());
        job.setPriority(
     Priority.valueOf(
        request.getPriority().toUpperCase()
    )
    );
        job.setStatus(JobStatus.QUEUED);

        return jobRepository.save(job);
    }
}