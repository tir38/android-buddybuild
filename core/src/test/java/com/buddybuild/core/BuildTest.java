package com.buddybuild.core;

import org.junit.Test;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BuildTest {

    @Test
    public void shouldCreateFromBuilder() throws Exception {
        // arrange
        String buildId = "1";
        String author = "test author";
        String branch = "test branch";
        String commitMessage = "test commit message";
        Integer buildNumber = 123;
        Boolean buildStatus = true;
        ZonedDateTime createTime = ZonedDateTime.now().minus(3, ChronoUnit.HOURS);
        ZonedDateTime startTime = ZonedDateTime.now().minus(2, ChronoUnit.HOURS);
        ZonedDateTime finishTime = ZonedDateTime.now().minus(1, ChronoUnit.HOURS);

        // act
        Build build = new Build.Builder()
                .id(buildId)
                .author(author)
                .branch(branch)
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
        assertThat(build.getBranch()).isEqualTo(branch);
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
}
