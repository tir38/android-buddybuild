package com.buddybuild.rest;

import com.google.gson.annotations.SerializedName;

class LoginRequestBody {

    @SerializedName("email")
    protected String email;
    @SerializedName("password")
    protected String password;

    LoginRequestBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
