package com.example.jobprocessor.repository;

import com.example.jobprocessor.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}