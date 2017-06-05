package com.buddybuild.core;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
public class AppTest {

    @Test
    public void shouldHaveId() throws Exception {
        // act
        App app = new App("1", "name", App.Platform.ANDROID);

        // assert
        assertThat(app.getId()).isEqualTo("1");
    }

    @Test
    public void shouldHaveName() throws Exception {
        // act
        App app = new App("1", "name", App.Platform.ANDROID);

        // assert
        assertThat(app.getName()).isEqualTo("name");
    }

    @Test
    public void shouldHavePlatform() throws Exception {
        // act
        App app = new App("1", "name", App.Platform.ANDROID);

        // assert
        assertThat(app.getPlatform()).isEqualTo(App.Platform.ANDROID);
    }
}
