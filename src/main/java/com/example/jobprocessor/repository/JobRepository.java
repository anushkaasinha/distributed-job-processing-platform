package com.example.jobprocessor.repository;

import com.example.jobprocessor.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.jobprocessor.entity.JobStatus;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(JobStatus status);
}
