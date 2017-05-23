package com.buddybuild.rest.util;

import android.support.annotation.Nullable;

import org.threeten.bp.ZonedDateTime;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    @Nullable
    public static ZonedDateTime toZonedDateTime(@Nullable String input) {
        if (input == null || input.equals("")) {
            return null;
        }
        return ZonedDateTime.parse(input);
    }
}
