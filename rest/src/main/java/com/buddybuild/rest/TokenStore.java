package com.buddybuild.rest;

import android.support.annotation.Nullable;

interface TokenStore {
    /**
     * TODO
     *
     * @return
     */
    @Nullable
    String getToken();

    /**
     * TODO
     *
     * @param token
     */
    void setToken(String token);

}
