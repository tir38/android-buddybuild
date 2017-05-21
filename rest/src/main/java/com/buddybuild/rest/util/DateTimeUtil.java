package com.buddybuild.rest.util;

import org.threeten.bp.ZonedDateTime;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static ZonedDateTime toZonedDateTime(String input) {
        return ZonedDateTime.parse(input);
    }
}
