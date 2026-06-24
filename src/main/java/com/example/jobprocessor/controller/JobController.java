package com.example.jobprocessor.controller;

import com.example.jobprocessor.dto.CreateJobRequest;
import com.example.jobprocessor.dto.JobStatsResponse;
import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Job APIs",
    description = "Distributed Job Processing APIs"
)
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "Create a new job")
    @PostMapping
    public Job createJob(
            @RequestBody CreateJobRequest request) {

        return jobService.createJob(request);
    }

    @Operation(summary = "Get all jobs")
    @GetMapping
    public List<Job> getAllJobs() {

        return jobService.getAllJobs();
    }

    @Operation(summary = "Get queue metrics and analytics")
    @GetMapping("/stats")
    public JobStatsResponse getStats() {

        return jobService.getStats();
    }
}