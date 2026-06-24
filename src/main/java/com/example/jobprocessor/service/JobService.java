package com.example.jobprocessor.service;
import java.util.List;
import com.example.jobprocessor.dto.CreateJobRequest;
import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.repository.JobRepository;
import org.springframework.stereotype.Service;
import com.example.jobprocessor.entity.JobStatus;
import com.example.jobprocessor.entity.Priority;
import com.example.jobprocessor.dto.JobStatsResponse;

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

    return new JobStatsResponse(
            queued,
            processing,
            completed,
            failed
    );
}
}