package com.buddybuild.rest;

import android.support.annotation.Nullable;

/**
 * A value class for holding the results of a login attempt. May contain optional token.
 */
public final class LoginResult {

    private final Result result;
    private final String token;

    LoginResult(Result result, @Nullable String token) {
        this.result = result;
        this.token = token;
    }

    public Result getResult() {
        return result;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    public enum Result {
        SUCCESS,
        UNKNOWN_EMAIL,
        EMAIL_PASSWORD_MISMATCH,
        THROTTLE_LIMIT,
        UNKNOWN_FAILURE,
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginResult that = (LoginResult) o;

        if (result != that.result) return false;
        return token != null ? token.equals(that.token) : that.token == null;
    }

    @Override
    public int hashCode() {
        int result1 = result != null ? result.hashCode() : 0;
        result1 = 31 * result1 + (token != null ? token.hashCode() : 0);
        return result1;
    }
}