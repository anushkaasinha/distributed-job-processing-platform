package com.example.jobprocessor.controller;

import com.example.jobprocessor.dto.CreateJobRequest;
import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.service.JobService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Job createJob(@RequestBody CreateJobRequest request) {
        return jobService.createJob(request);
    }
    @GetMapping
public List<Job> getAllJobs() {
    return jobService.getAllJobs();
}
}