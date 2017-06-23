package com.buddybuild.rest;

import android.support.annotation.Nullable;

class LiveTokenStore implements TokenStore {

    private String token;

    @Nullable
    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
