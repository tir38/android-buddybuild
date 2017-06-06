package com.buddybuild.rest;

import android.support.annotation.Nullable;

import org.threeten.bp.ZonedDateTime;

/**
 * Internal
 */
class DateTimeUtil {

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
