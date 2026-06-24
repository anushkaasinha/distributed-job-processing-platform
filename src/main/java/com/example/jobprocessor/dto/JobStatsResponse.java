package com.example.jobprocessor.dto;

public class JobStatsResponse {

    private long queued;
    private long processing;
    private long completed;
    private long failed;

    public JobStatsResponse(long queued,
                            long processing,
                            long completed,
                            long failed) {
        this.queued = queued;
        this.processing = processing;
        this.completed = completed;
        this.failed = failed;
    }

    public long getQueued() {
        return queued;
    }

    public long getProcessing() {
        return processing;
    }

    public long getCompleted() {
        return completed;
    }

    public long getFailed() {
        return failed;
    }
}