package com.buddybuild.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Internal
 */
class LoginErrorResponseBody {

    @SerializedName("message")
    private String message;

    // TODO add test
    public String getMessage() {
        return message;
    }
}
