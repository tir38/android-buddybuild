package com.buddybuild.utils;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.assertj.core.api.Java6Assertions.assertThat;
/**
 * Unit tests for {@link DateUtils}
 */
@RunWith(AndroidJUnit4.class)
public final class DateUtilsTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = getTargetContext();
    }

    @Test
    public void shouldReturnAFewSecondsAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(3, ChronoUnit.SECONDS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("a few seconds ago");
    }

    @Test
    public void shouldReturnMinuteAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(1, ChronoUnit.MINUTES);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("1 minute ago");
    }

    @Test
    public void shouldReturnMinutesAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(59, ChronoUnit.MINUTES);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("59 minutes ago");
    }

    @Test
    public void shouldReturnHourAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(1, ChronoUnit.HOURS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("1 hour ago");
    }

    @Test
    public void shouldReturnHoursAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(23, ChronoUnit.HOURS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("23 hours ago");
    }

    @Test
    public void shouldReturnDayAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(1, ChronoUnit.DAYS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("1 day ago");
    }

    @Test
    public void shouldReturnDaysAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(6, ChronoUnit.DAYS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("6 days ago");
    }

    @Test
    public void shouldReturnWeekAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(1, ChronoUnit.WEEKS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("1 week ago");
    }

    @Test
    public void shouldReturnWeeksAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(4, ChronoUnit.WEEKS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("4 weeks ago");
    }

    @Test
    public void shouldReturnMonthAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(1, ChronoUnit.MONTHS)
                .minus(8, ChronoUnit.DAYS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("1 month ago");
    }

    @Test
    public void shouldReturnMonthsAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(11, ChronoUnit.MONTHS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("11 months ago");
    }

    @Test
    public void shouldReturnYearAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(1, ChronoUnit.YEARS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("1 year ago");
    }

    @Test
    public void shouldReturnYearsAgo() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime testTime = now.minus(5, ChronoUnit.YEARS);

        // act
        String output = DateUtils.ago(testTime);

        // assert
        assertThat(output).isEqualTo("5 years ago");
    }

    @Test
    public void shouldReturnDuration() throws Exception {
        // arrange
        List<Pair<Duration, String>> testCases = new ArrayList<>();
        testCases.add(new Pair<>(Duration.ofDays(1), "1d 0h 0m 0s"));

        for (Pair<Duration, String> testCase : testCases) {
            Duration duration = testCase.first;
            String expectedString = testCase.second;

            // act
            String actualString = DateUtils.duration(duration, context);

            // assert
            assertThat(actualString).isEqualTo(expectedString);
        }

    }
}
