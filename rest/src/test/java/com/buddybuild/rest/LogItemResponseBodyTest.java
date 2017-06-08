package com.buddybuild.rest;

import com.buddybuild.core.LogItem;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Tests for {@link LogItemResponseBody}
 */
public final class LogItemResponseBodyTest {

    private List<LogItemResponseBody> response;

    @Test
    public void shouldConvertToLogItem() throws Exception {
        // arrange
        String json = JsonStringReader.readJsonSampleFromFile("log_item/valid_log_items");
        Gson gson = new Gson();
        LogItemResponseBody[] body = gson.fromJson(json, LogItemResponseBody[].class);
        response = Arrays.asList(body);

        // act
        LogItem logItem0 = response.get(0).toLogItem();
        LogItem logItem1 = response.get(1).toLogItem();

        // assert
        assertThat(logItem0.getLevel()).isEqualTo(LogItem.Level.CT);
        assertThat(logItem0.getMessage()).isEqualTo("=== Securing and preparing your Build Environment ===");
        // TODO assert timestamp

        assertThat(logItem1.getLevel()).isEqualTo(LogItem.Level.CI);
    }
}
