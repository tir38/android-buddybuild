package com.buddybuild.rest;

import com.buddybuild.core.LogItem;
import com.google.gson.annotations.SerializedName;

/**
 * Internal
 */
class LogItemResponseBody {

    @SerializedName("level")
    private String level;
    @SerializedName("message")
    private String message;
    @SerializedName("timestamp")
    private String timestamp; // TODO we never use this. Should we remove it?

    LogItem toLogItem() {
        String trimmedMsg = message.startsWith("\n") ? message.substring(2) : message;
        return new LogItem(DateTimeUtil.toZonedDateTime(timestamp), trimmedMsg, getLogLevel());
    }

    /**
     * @return conversion from server string to {@link LogItem.Level} enum
     */
    private LogItem.Level getLogLevel() {
        switch (level) {
            case "cc":
                return LogItem.Level.CC;
            case "ce":
                return LogItem.Level.CE;
            case "ci":
                return LogItem.Level.CI;
            case "ct":
                return LogItem.Level.CT;
            case "cw":
                return LogItem.Level.CW;
            default:
                throw new IllegalStateException("unknown log level: " + level);
        }
    }
}
