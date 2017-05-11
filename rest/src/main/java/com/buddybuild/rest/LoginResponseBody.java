package com.buddybuild.rest;

import com.google.gson.annotations.SerializedName;

class LoginResponseBody {
    @SerializedName("token")
    protected String sessionToken;

    public String getSessionToken() {
        return sessionToken;
    }
}
