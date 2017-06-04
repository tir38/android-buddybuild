package com.buddybuild.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.buddybuild.R;

import org.threeten.bp.Duration;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import timber.log.Timber;

public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Compute "x ago" (i.e. 3 days ago, 4 hours ago, 5 weeks ago)
     * starting with most recent
     * <p>
     * 0 - 59 seconds -> "a few seconds ago"
     * 0 - 59 minutes ago
     * 0 - 23 hours ago
     * 1 - 6 days ago
     * 1 - 5 weeks ago
     * 1 - 11 months ago
     * 1 - infinite years ago
     *
     * @param zonedDateTime
     * @param context
     * @return
     */
    @NonNull
    public static String ago(@NonNull ZonedDateTime zonedDateTime, Context context) {
        //noinspection ConstantConditions
        if (zonedDateTime == null) {
            throw new IllegalArgumentException("zonedDateTime cannot be null");
        }

        ZonedDateTime now = ZonedDateTime.now();

        if (now.isBefore(zonedDateTime)) {
            // something is screwy with the device's time stamp.
            return "";
        }

        long secondsBetween = ChronoUnit.SECONDS.between(zonedDateTime, now);
        if (secondsBetween < 59) {
            return context.getResources().getString(R.string.a_few_seconds_ago);
        }

        int minutesBetween = (int) ChronoUnit.MINUTES.between(zonedDateTime, now);
        if (minutesBetween < 60) {
            return context.getResources().getQuantityString(R.plurals.minutes_ago, minutesBetween, minutesBetween);
        }

        int hoursBetween = (int) ChronoUnit.HOURS.between(zonedDateTime, now);
        if (hoursBetween < 24) {
            return context.getResources().getQuantityString(R.plurals.hours_ago, hoursBetween, hoursBetween);
        }

        int daysBetween = (int) ChronoUnit.DAYS.between(zonedDateTime, now);
        if (daysBetween < 7) {
            return context.getResources().getQuantityString(R.plurals.days_ago, daysBetween, daysBetween);
        }

        int weeksBetween = (int) ChronoUnit.WEEKS.between(zonedDateTime, now);
        if (weeksBetween < 5) {
            return context.getResources().getQuantityString(R.plurals.weeks_ago, weeksBetween, weeksBetween);
        }

        int monthsBetween = (int) ChronoUnit.MONTHS.between(zonedDateTime, now);
        if (monthsBetween < 12) {
            return context.getResources().getQuantityString(R.plurals.months_ago, monthsBetween, monthsBetween);
        }

        int yearsBetween = (int) ChronoUnit.YEARS.between(zonedDateTime, now);
        if (yearsBetween >= 1) {
            return context.getResources().getQuantityString(R.plurals.years_ago, yearsBetween, yearsBetween);
        }

        Timber.w("unhandled time: now %s, time in question %s", now, zonedDateTime);
        return "";
    }


    /**
     * @param duration
     * @param context
     * @return
     */
    @NonNull
    public static String duration(@NonNull Duration duration, Context context) {
        long allDays = duration.toDays();
        int wholeDays = (int) Math.floor(allDays);
        long allHours = duration.toHours();
        int remainderHours = (int) Math.floor(allHours - (allDays * 24));
        long allMinutes = duration.toMinutes();
        int remainderMinutes = (int) Math.floor(allMinutes - (allHours * 60));
        long allSeconds = duration.getSeconds();
        int remainderSeconds = (int) Math.floor(allSeconds - (allMinutes * 60));

        // build string
        String days = "";
        String hours = "";
        String minutes = "";
        String seconds = "";

        if (wholeDays > 0) {
            days = context.getString(R.string.days, wholeDays) + " ";
        }
        if (remainderHours > 0 || wholeDays > 0) {
            hours = context.getString(R.string.hours, remainderHours) + " ";
        }
        if (remainderMinutes > 0 || remainderHours > 0 || wholeDays > 0) {
            minutes = context.getString(R.string.minutes, remainderMinutes) + " ";
        }
        if (remainderSeconds >= 0 || remainderMinutes > 0 || remainderHours > 0 || wholeDays > 0) {
            seconds = context.getString(R.string.seconds, remainderSeconds);
        }
        return (days + hours + minutes + seconds);
    }
}


