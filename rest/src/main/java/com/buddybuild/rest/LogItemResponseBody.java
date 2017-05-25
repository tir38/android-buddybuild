package com.buddybuild.rest;

import com.buddybuild.core.LogItem;
import com.buddybuild.rest.util.DateTimeUtil;
import com.google.gson.annotations.SerializedName;

public final class LogItemResponseBody {

    @SerializedName("level")
    private String level;
    @SerializedName("message")
    private String message;
    @SerializedName("timestamp")
    private String timestamp;

    public LogItem toLogItem() {
        return new LogItem(DateTimeUtil.toZonedDateTime(timestamp), message, getLogLevel());
    }

    /**
     * @return conversion from server string to {@link LogItem.Level} enum
     */
    // TODO add tests
    private LogItem.Level getLogLevel() {
        switch (level) {
            case "ci":
                return LogItem.Level.CI;
            default:
                throw new IllegalStateException("unknown log level: " + level);
        }

    }

}
