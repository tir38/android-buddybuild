package com.buddybuild.rest;

import com.buddybuild.core.App;
import com.google.gson.annotations.SerializedName;

import timber.log.Timber;

/**
 * Internal
 */
class AppResponseBody {

    private static final String IOS_SERVER_STRING = "ios";
    private static final String ANDROID_SERVER_STRING = "android";

    @SerializedName("_id")
    protected String id;
    @SerializedName("metadata")
    protected MetaData metaData;

    /**
     * Map response to model object
     *
     * @return {@link App} if json is complete, or null if incomplete
     */
    App toApp() {
        if (id == null) {
            Timber.e("id is null");
            return null;
        }
        if (metaData.name == null) {
            Timber.e("name is null");
            return null;
        }
        if (metaData.platformString == null) {
            Timber.e("platform string is null");
            return null;
        }

        App.Platform platform;
        if (metaData.platformString.equals(IOS_SERVER_STRING)) {
            platform = App.Platform.IOS;
        } else if (metaData.platformString.equals(ANDROID_SERVER_STRING)) {
            platform = App.Platform.ANDROID;
        } else {
            Timber.e("unknown platform: %s", metaData.platformString);
            return null;
        }

        return new App(id, metaData.name, platform);
    }

    private static class MetaData {
        @SerializedName("display_name")
        protected String name;
        @SerializedName("platform")
        protected String platformString;
    }
}
