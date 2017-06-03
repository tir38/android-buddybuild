package com.buddybuild.rest;

import com.buddybuild.core.App;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AppResponseBodyTest {

    @Test
    public void shouldMapToApp() throws Exception {
        // arrange
        String json = JsonStringReader.readJsonSampleFromFile("app/valid_apps_response");
        Gson gson = new Gson();
        AppResponseBody[] array = gson.fromJson(json, AppResponseBody[].class);
        List<AppResponseBody> appResponseBody = Arrays.asList(array);

        // act
        App actualApp1 = appResponseBody.get(0).toApp();
        App actualApp2 = appResponseBody.get(1).toApp();

        // assert
        assertThat(actualApp1.getId()).isEqualTo("58a4e07838704cb2eacd7ce4");
        assertThat(actualApp1.getName()).isEqualTo("2048 iOS App");
        assertThat(actualApp1.getPlatform()).isEqualTo(App.Platform.IOS);

        assertThat(actualApp2.getPlatform()).isEqualTo(App.Platform.ANDROID);
    }
}