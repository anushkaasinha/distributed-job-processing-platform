
package com.example.jobprocessor.repository;

import com.example.jobprocessor.entity.Job;
import com.example.jobprocessor.entity.JobStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    long countByStatus(JobStatus status);

    List<Job> findByStatus(JobStatus status);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Job j
        SET j.status = com.example.jobprocessor.entity.JobStatus.PROCESSING,
            j.startedAt = :startedAt
        WHERE j.id = :jobId
          AND j.status = com.example.jobprocessor.entity.JobStatus.QUEUED
    """)
    int claimJob(
            @Param("jobId") Long jobId,
            @Param("startedAt") LocalDateTime startedAt
    );
}
