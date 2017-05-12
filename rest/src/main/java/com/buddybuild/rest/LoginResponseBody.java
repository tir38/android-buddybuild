package com.buddybuild.rest;

import com.google.gson.annotations.SerializedName;

class LoginResponseBody {
    @SerializedName("token")
    private String sessionToken;

    String getSessionToken() {
        return sessionToken;
    }
}
