package com.example.jobprocessor.entity;

import jakarta.persistence.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.example.jobprocessor.entity.JobStatus;


@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobType;
    @Enumerated(EnumType.STRING)
      private JobStatus status;
    @Enumerated(EnumType.STRING)
   private Priority priority;

    public Job() {
    }

    public Long getId() {
        return id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

   public JobStatus getStatus() {
    return status;
}

public void setStatus(JobStatus status) {
    this.status = status;
}
  public Priority getPriority() {
    return priority;
}

public void setPriority(Priority priority) {
    this.priority = priority;
}
}