package com.buddybuild.rest;

import com.google.gson.Gson;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
public class LoginRequestBodyTest {

    @Test
    public void shouldSerializeToJson() throws Exception {
        // arrange
        LoginRequestBody loginRequestBody
                = new LoginRequestBody("john@buddybuild.com", "password1234");

        // act
        Gson gson = new Gson();
        String json = gson.toJson(loginRequestBody);

        // assert
        assertThat(json).isEqualTo("{\"email\":\"john@buddybuild.com\",\"password\":\"password1234\"}");
    }
}
