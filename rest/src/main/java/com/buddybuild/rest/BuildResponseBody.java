package com.buddybuild.rest;

import com.buddybuild.core.Build;
import com.buddybuild.rest.util.DateTimeUtil;
import com.google.gson.annotations.SerializedName;


public class BuildResponseBody {
    @SerializedName("_id")
    private String id;

    @SerializedName("build_status")
    private Boolean buildStatus; // TODO this is actually a string

    @SerializedName("build_number")
    private Integer buildNumber;

    @SerializedName("commit_info")
    private CommitInfo commitInfo;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("started_at")
    private String startedAt;

    @SerializedName("finished_at")
    private String finishedAt;

    Build toBuild() {
        return new Build.Builder()
                .author(commitInfo.author)
                .branch(commitInfo.branch)
                .buildNumber(buildNumber)
                .buildStatus(buildStatus)
                .commitMessage(commitInfo.commitMessage)
                .createTime(DateTimeUtil.toZonedDateTime(createdAt))
                .startTime(DateTimeUtil.toZonedDateTime(startedAt))
                .finishTime(DateTimeUtil.toZonedDateTime(finishedAt))
                .id(id)
                .build();
    }

    private static class CommitInfo {
        @SerializedName("branch")
        private String branch;

        @SerializedName("author")
        private String author;

        @SerializedName("message")
        private String commitMessage;
    }
}
