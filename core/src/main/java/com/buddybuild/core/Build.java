package com.buddybuild.core;

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

    private Build(String id, Boolean buildStatus, String branch, Integer buildNumber, String author, String commitMessage) {
        this.id = id;
        this.buildStatus = buildStatus;
        this.branch = branch;
        this.buildNumber = buildNumber;
        this.author = author;
        this.commitMessage = commitMessage;
    }

    public static class Builder {
        private String id;
        private Boolean buildStatus;
        private String branch;
        private Integer buildNumber;
        private String author;
        private String commitMessage;

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

        public Build build() {
            return new Build(id, buildStatus, branch, buildNumber, author, commitMessage);
        }

    }
}
