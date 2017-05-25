package com.buddybuild.core;

import java.util.List;

import lombok.Getter;

/**
 * A log for an entire {@link Build}
 */
public final class Log {

    /**
     * Get list of {@link LogItem} in order with oldes LogItem first
     */
    @Getter
    private final List<LogItem> logs;

    public Log(List<LogItem> logs) {
        // TODO sort list before saving
        this.logs = logs;
    }

    // TODO custom equals?
}
