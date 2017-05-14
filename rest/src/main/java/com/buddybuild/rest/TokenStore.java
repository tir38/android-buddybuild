package com.buddybuild.rest;

import android.support.annotation.Nullable;

interface TokenStore {

    @Nullable
    String getToken();

    void setToken(String token);
}
