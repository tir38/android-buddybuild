package com.buddybuild.rest;

import com.buddybuild.core.Build;
import com.google.gson.annotations.SerializedName;

public class BuildResponseBody {

    // TODO add test

    @SerializedName("_id")
    private String id;

    @SerializedName("build_status")
    private Boolean buildStatus;

    @SerializedName("build_number")
    private Integer buildNumber;

    @SerializedName("commit_info")
    private CommitInfo commitInfo;

    Build toBuild() {
        return new Build.Builder()
                .author(commitInfo.author)
                .branch(commitInfo.branch)
                .buildNumber(buildNumber)
                .buildStatus(buildStatus)
                .commitMessage(commitInfo.commitMessage)
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
