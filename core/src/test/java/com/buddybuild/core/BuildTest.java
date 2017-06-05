package com.buddybuild.core;

import org.junit.Test;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import static org.assertj.core.api.Java6Assertions.assertThat;
/**
 * Unit tests for {@link Build}
 */
public final class BuildTest {

    @Test
    public void shouldCreateFromBuilder() throws Exception {
        // arrange
        String buildId = "1";
        String author = "test author";
        String branch = "test branch";
        String commitMessage = "test commit message";
        Integer buildNumber = 123;

        Build.Status buildStatus = Build.Status.RUNNING;
        ZonedDateTime createTime = ZonedDateTime.now().minus(3, ChronoUnit.HOURS);
        ZonedDateTime startTime = ZonedDateTime.now().minus(2, ChronoUnit.HOURS);
        ZonedDateTime finishTime = ZonedDateTime.now().minus(1, ChronoUnit.HOURS);

        // act
        Build build = new Build.Builder()
                .id(buildId)
                .author(author)
                .branchName(branch)
                .commitMessage(commitMessage)
                .buildNumber(buildNumber)
                .buildStatus(buildStatus)
                .createTime(createTime)
                .startTime(startTime)
                .finishTime(finishTime)
                .build();

        // assert
        assertThat(build.getId()).isEqualTo(buildId);
        assertThat(build.getAuthor()).isEqualTo(author);
        assertThat(build.getBranchName()).isEqualTo(branch);
        assertThat(build.getCommitMessage()).isEqualTo(commitMessage);
        assertThat(build.getBuildNumber()).isEqualTo(buildNumber);
        assertThat(build.getBuildStatus()).isEqualTo(buildStatus);
        assertThat(build.getCreateTime()).isEqualTo(createTime);
        assertThat(build.getStartTime()).isEqualTo(startTime);
        assertThat(build.getFinishTime()).isEqualTo(finishTime);
    }

    @Test
    public void ifFinishedEventIsNonNull_ShouldReturnAsMostRecentEvent() throws Exception {
        // arrange
        ZonedDateTime createTime = ZonedDateTime.now().minus(3, ChronoUnit.HOURS);
        ZonedDateTime startTime = ZonedDateTime.now().minus(2, ChronoUnit.HOURS);
        ZonedDateTime finishTime = ZonedDateTime.now().minus(1, ChronoUnit.HOURS);

        Build build = new Build.Builder()
                .createTime(createTime)
                .startTime(startTime)
                .finishTime(finishTime)
                .build();
        // act
        ZonedDateTime mostRecentBuildEvent = build.getMostRecentBuildEvent();

        // assert
        assertThat(mostRecentBuildEvent).isEqualTo(finishTime);
    }

    @Test
    public void ifFinishedEventIsNull_ShouldReturnStartedAsMostRecentEvent() throws Exception {
        // arrange
        ZonedDateTime createTime = ZonedDateTime.now().minus(3, ChronoUnit.HOURS);
        ZonedDateTime startTime = ZonedDateTime.now().minus(2, ChronoUnit.HOURS);

        Build build = new Build.Builder()
                .createTime(createTime)
                .startTime(startTime)
                .finishTime(null)
                .build();
        // act
        ZonedDateTime mostRecentBuildEvent = build.getMostRecentBuildEvent();

        // assert
        assertThat(mostRecentBuildEvent).isEqualTo(startTime);
    }

    @Test
    public void ifFinishedAndStartedEventIsNull_ShouldReturnCreatedAsMostRecentEvent() throws Exception {
        // arrange
        ZonedDateTime createTime = ZonedDateTime.now().minus(3, ChronoUnit.HOURS);

        Build build = new Build.Builder()
                .createTime(createTime)
                .startTime(null)
                .finishTime(null)
                .build();
        // act
        ZonedDateTime mostRecentBuildEvent = build.getMostRecentBuildEvent();

        // assert
        assertThat(mostRecentBuildEvent).isEqualTo(createTime);
    }


    @Test
    public void shouldComputeQueuedDuration() throws Exception {
        // arrange
        int hours = 2;
        int minutes = 3;
        ZonedDateTime startTime = ZonedDateTime.now();
        ZonedDateTime createdTime = startTime.minus(hours, ChronoUnit.HOURS).minus(minutes, ChronoUnit.MINUTES);
        Build build = new Build.Builder()
                .startTime(startTime)
                .createTime(createdTime)
                .build();

        Duration expectedDuration = Duration.ofHours(hours).plusMinutes(minutes);

        // act
        Duration buildDuration = build.getQueuedDuration();

        // assert
        assertThat(buildDuration).isEqualTo(expectedDuration);
    }

    @Test
    public void shouldComputeBuildDuration() throws Exception {
        // arrange
        int hours = 1;
        int minutes = 2;
        ZonedDateTime finishTime = ZonedDateTime.now();
        ZonedDateTime startTime = finishTime.minus(hours, ChronoUnit.HOURS).minus(minutes, ChronoUnit.MINUTES);
        Build build = new Build.Builder()
                .startTime(startTime)
                .finishTime(finishTime)
                .build();

        Duration expectedDuration = Duration.ofHours(hours).plusMinutes(minutes);

        // act
        Duration buildDuration = build.getBuildDuration();

        // assert
        assertThat(buildDuration).isEqualTo(expectedDuration);
    }
}
