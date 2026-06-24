package com.example.jobprocessor.dto;

public class JobStatsResponse {

    private long queued;
    private long processing;
    private long completed;
    private long failed;
    private double successRate;
    private long averageProcessingTimeMs;

    public JobStatsResponse(
            long queued,
            long processing,
            long completed,
            long failed,
            double successRate,
            long averageProcessingTimeMs) {

        this.queued = queued;
        this.processing = processing;
        this.completed = completed;
        this.failed = failed;
        this.successRate = successRate;
        this.averageProcessingTimeMs = averageProcessingTimeMs;
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

    public double getSuccessRate() {
        return successRate;
    }

    public long getAverageProcessingTimeMs() {
        return averageProcessingTimeMs;
    }
}