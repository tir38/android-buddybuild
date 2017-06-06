package com.buddybuild.rest;

import com.buddybuild.core.Build;
import com.google.gson.annotations.SerializedName;

import timber.log.Timber;

/**
 * Internal
 */
class BuildResponseBody {

    @SerializedName("_id")
    private String id;

    @SerializedName("build_status")
    private String buildStatus;

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
                .branchName(commitInfo.branchName)
                .buildNumber(buildNumber)
                .buildStatus(getStatus())
                .commitMessage(commitInfo.commitMessage)
                .createTime(DateTimeUtil.toZonedDateTime(createdAt))
                .startTime(DateTimeUtil.toZonedDateTime(startedAt))
                .finishTime(DateTimeUtil.toZonedDateTime(finishedAt))
                .id(id)
                .build();
    }

    private static class CommitInfo {
        @SerializedName("branch")
        private String branchName;

        @SerializedName("author")
        private String author;

        @SerializedName("message")
        private String commitMessage;
    }

    /**
     * Convert server response string into build status
     *
     * @return
     */
    private Build.Status getStatus() {

        switch (buildStatus) {
            case "cancelled":
                return Build.Status.CANCELLED;
            case "failed":
                return Build.Status.FAILED;
            case "queued":
                return Build.Status.QUEUED;
            case "success":
                return Build.Status.SUCCESS;
            case "running":
                return Build.Status.RUNNING;
            default:
                Timber.e("unknown build status: %s", buildStatus);
                return null;
        }
    }
}
