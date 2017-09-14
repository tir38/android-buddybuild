package com.buddybuild.core;


import org.threeten.bp.ZonedDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A single log line in a log
 */
@EqualsAndHashCode
public class LogItem {

    @Getter
    private final ZonedDateTime time; // TODO do we really need this?

    @Getter
    private final String message;

    @Getter
    private final Level level;

    /**
     * @param time    timestamp of log item, required
     * @param message the contents of the log, required
     * @param level   the log level, required
     */
    public LogItem(ZonedDateTime time, String message, Level level) {
        this.time = time;
        this.message = message;
        this.level = level;
    }

    public enum Level {
        CI,
        CT,
        CC,
        CE,
        CW
    }
}
