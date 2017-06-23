package com.buddybuild.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Internal
 */
class LoginErrorResponseBody {

    @SerializedName("message")
    private String message;

    String getMessage() {
        return message;
    }
}
