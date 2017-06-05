package com.buddybuild.ui.viewmodel;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;

import com.buddybuild.core.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Unit tests for {@link BuildStatusViewModel}
 */
@RunWith(AndroidJUnit4.class)
public final class BuildStatusViewModelTest {

    private Context context;
    private BuildStatusViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        context = getTargetContext();
        viewModel = new BuildStatusViewModel();
    }

    @Test
    public void shouldReturnStatus() throws Exception {
        // arrange
        List<Pair<Build, String>> testCases = new ArrayList<>();
        testCases.add(new Pair<>(new Build.Builder().buildStatus(Build.Status.CANCELLED).build(), "Cancelled"));
        testCases.add(new Pair<>(new Build.Builder().buildStatus(Build.Status.FAILED).build(), "Failed"));
        testCases.add(new Pair<>(new Build.Builder().buildStatus(Build.Status.QUEUED).build(), "Queued"));
        testCases.add(new Pair<>(new Build.Builder().buildStatus(Build.Status.SUCCESS).build(), "Success"));
        testCases.add(new Pair<>(new Build.Builder().buildStatus(Build.Status.RUNNING).build(), "Running"));

        // assert
        for (Pair<Build, String> testCase : testCases) {
            viewModel.setBuild(testCase.first);
            assertThat(viewModel.getStatus(context)).isEqualTo(testCase.second);
        }
    }

    @Test
    public void shouldReturnStatusIcon() throws Exception {
        // TODO how to test?
    }

    @Test
    public void shouldReturnStarted() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);
        Build build = new Build.Builder().startTime(oneHourAgo).build();
        viewModel.setBuild(build);

        // assert
        assertThat(viewModel.getStarted(context)).isEqualTo("1 hour ago");
    }

    @Test
    public void shouldReturnQueuedFor() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);
        Build build = new Build.Builder().startTime(now).createTime(oneHourAgo).build();
        viewModel.setBuild(build);

        // assert
        assertThat(viewModel.getQueueDuration(context)).isEqualTo("1h 0m 0s");
    }

    @Test
    public void shouldReturnBuildDuration() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);
        Build build = new Build.Builder().finishTime(now).startTime(oneHourAgo).build();
        viewModel.setBuild(build);

        // assert
        assertThat(viewModel.getBuildDuration(context)).isEqualTo("1h 0m 0s");
    }
}
