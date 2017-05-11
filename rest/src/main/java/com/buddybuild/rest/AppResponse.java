package com.buddybuild.rest;

import com.buddybuild.App;
import com.google.gson.annotations.SerializedName;

import timber.log.Timber;

public class AppResponse {

    private static final String IOS_SERVER_STRING = "ios";
    private static final String ANDROID_SERVER_STRING = "android";

    @SerializedName("_id")
    protected String id;
    @SerializedName("app_name")
    protected String name;
    @SerializedName("platform")
    protected String platformString;

    /**
     * Map response to model object
     *
     * @return {@link App} if json is complete, or null if incomplete
     */
    public App toApp() {
        if (id == null || name == null || platformString == null) {
            Timber.w("incomplete json");
            return null;
        }

        App.Platform platform;
        if (platformString.equals(IOS_SERVER_STRING)) {
            platform = App.Platform.IOS;
        } else if (platformString.equals(ANDROID_SERVER_STRING)) {
            platform = App.Platform.ANDROID;
        } else {
            Timber.w("unknown platform: %s", platformString);
            return null;
        }

        return new App(id, name, platform);
    }
}
