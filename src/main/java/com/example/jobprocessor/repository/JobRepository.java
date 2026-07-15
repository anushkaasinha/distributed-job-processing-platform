package com.example.jobprocessor.repository;

import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(JobStatus status);

    List<Job> findByStatusOrderByPriorityAsc(
            JobStatus status
    );
    

    long countByStatus(JobStatus status);
}