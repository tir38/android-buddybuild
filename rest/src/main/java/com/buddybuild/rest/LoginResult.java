package com.buddybuild.rest;

public enum LoginResult {
    SUCCESS,
    UNKNOWN_EMAIL,
    EMAIL_PASSWORD_MISMATCH,
    THROTTLE_LIMIT,
    UNKNOWN_FAILURE,
}
