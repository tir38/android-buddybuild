package com.buddybuild.rest;

import com.google.gson.Gson;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
public class LoginResponseBodyTest {

    @Test
    public void shouldHaveToken() throws Exception {
        // arrange
        String json = JsonStringReader.readJsonSampleFromFile("login/valid_login_response");
        Gson gson = new Gson();

        // act
        LoginResponseBody loginResponseBody = gson.fromJson(json, LoginResponseBody.class);

        // assert
        assertThat(loginResponseBody.getSessionToken()).isEqualTo("some returned token");
    }
}
