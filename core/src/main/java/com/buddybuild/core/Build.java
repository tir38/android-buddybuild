package com.buddybuild.core;

import org.threeten.bp.ZonedDateTime;

import lombok.Getter;

/**
 * A model of a build
 */
public class Build {

    @Getter
    private String id;

    @Getter
    private Boolean buildStatus;

    @Getter
    private String branch;

    @Getter
    private Integer buildNumber;

    @Getter
    private String author;

    @Getter
    private String commitMessage;

    @Getter
    private ZonedDateTime createTime;

    @Getter
    private ZonedDateTime startTime;

    @Getter
    private ZonedDateTime finishTime;

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

    private Build(String id, Boolean buildStatus, String branch, Integer buildNumber, String author, String
            commitMessage, ZonedDateTime createTime, ZonedDateTime startTime, ZonedDateTime finishTime) {
        this.id = id;
        this.buildStatus = buildStatus;
        this.branch = branch;
        this.buildNumber = buildNumber;
        this.author = author;
        this.commitMessage = commitMessage;
        this.createTime = createTime;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public static class Builder {
        private String id;
        private Boolean buildStatus;
        private String branch;
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

        public Builder buildStatus(Boolean buildStatus) {
            this.buildStatus = buildStatus;
            return this;
        }

        public Builder branch(String branch) {
            this.branch = branch;
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
            return new Build(id, buildStatus, branch, buildNumber, author, commitMessage, createTime, startTime,
                    finishTime);
        }
    }
}
