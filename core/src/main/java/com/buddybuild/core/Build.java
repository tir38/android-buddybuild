package com.buddybuild.core;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;

import lombok.Getter;

/**
 * A model of a build
 */
public class Build {

    @Getter
    private String id;

    @Getter
    private Status buildStatus;

    @Getter
    private String branchName;

    @Getter
    private Integer buildNumber;

    @Getter
    private String author;

    @Getter
    private String commitMessage;

    @Getter
    private ZonedDateTime createTime; // TODO add tests

    @Getter
    private ZonedDateTime startTime; // TODO add tests

    @Getter
    private ZonedDateTime finishTime; // TODO add tests

    /**
     * @return most recent build event: finished, started, then created... or null
     */
    public ZonedDateTime getMostRecentBuildEvent() {
        if (finishTime != null) {
            return finishTime;
        }
        if (startTime != null) {
            return startTime;
        }
        return createTime;
    }

    /**
     * @return Duration of how long build was queued. Will return null if either create time or start time are null
     */
    public Duration getQueuedDuration() {
        if (createTime == null || startTime == null) {
            return null;
        }

        return Duration.between(createTime, startTime);
    }

    /**
     * @return Duration of how long build was building. Will return null if either finish time or start time are null
     */
    public Duration getBuildDuration() {
        if (finishTime == null || startTime == null) {
            return null;
        }

        return Duration.between(startTime, finishTime);
    }

    private Build(String id, Status buildStatus, String branchName, Integer buildNumber, String author, String
            commitMessage, ZonedDateTime createTime, ZonedDateTime startTime, ZonedDateTime finishTime) {
        this.id = id;
        this.buildStatus = buildStatus;
        this.branchName = branchName;
        this.buildNumber = buildNumber;
        this.author = author;
        this.commitMessage = commitMessage;
        this.createTime = createTime;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public static class Builder {
        private String id;
        private Status buildStatus;
        private String branchName;
        private Integer buildNumber;
        private String author;
        private String commitMessage;
        private ZonedDateTime createTime;
        private ZonedDateTime startTime;
        private ZonedDateTime finishTime;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder buildStatus(Status buildStatus) {
            this.buildStatus = buildStatus;
            return this;
        }

        public Builder branchName(String branchName) {
            this.branchName = branchName;
            return this;
        }

        public Builder buildNumber(Integer buildNumber) {
            this.buildNumber = buildNumber;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder commitMessage(String commitMessage) {
            this.commitMessage = commitMessage;
            return this;
        }

        // TODO add tests
        public Builder createTime(ZonedDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder startTime(ZonedDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder finishTime(ZonedDateTime finishTime) {
            this.finishTime = finishTime;
            return this;
        }

        public Build build() {
            return new Build(id, buildStatus, branchName, buildNumber, author, commitMessage, createTime, startTime,
                    finishTime);
        }
    }

    public enum Status {
        CANCELLED,
        FAILED,
        QUEUED,
        RUNNING,
        SUCCESS
    }
}
