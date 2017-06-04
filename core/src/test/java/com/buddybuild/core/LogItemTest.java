package com.buddybuild.core;

import org.junit.Test;
import org.threeten.bp.ZonedDateTime;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.threeten.bp.temporal.ChronoUnit.MINUTES;

public final class LogItemTest {

    @Test
    public void shouldHaveAllFields() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        String msg = "test message";
        LogItem.Level level = LogItem.Level.CI;

        // act
        LogItem logItem = new LogItem(now, msg, level);

        // assert
        assertThat(logItem.getTime()).isEqualTo(now);
        assertThat(logItem.getMessage()).isEqualTo(msg);
        assertThat(logItem.getLevel()).isEqualTo(level);
    }

    @Test
    public void twoLogItemsAreEqualIffTheirFieldsAreEqual() throws Exception {
        // arrange
        ZonedDateTime now = ZonedDateTime.now();
        String msg = "test message";
        LogItem.Level level = LogItem.Level.CI;

        ZonedDateTime someOtherTime = now.minus(1, MINUTES);
        String someOtherMessage = "some other test message";

        // act
        LogItem logItem1 = new LogItem(now, msg, level);
        LogItem logItem2 = new LogItem(now, msg, level);
        LogItem logItem3 = new LogItem(someOtherTime, msg, level);
        LogItem logItem4 = new LogItem(now, someOtherMessage, level);

        // assert
        assertThat(logItem1).isEqualTo(logItem2);
        assertThat(logItem1).isNotEqualTo(logItem3);
        assertThat(logItem1).isNotEqualTo(logItem4);
    }
}
