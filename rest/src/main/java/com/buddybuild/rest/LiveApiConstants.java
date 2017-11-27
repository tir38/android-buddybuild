package com.buddybuild.rest;

/**
 * Implementation of {@link ApiConstants} for working with our production buddybuild server
 */
public class LiveApiConstants implements ApiConstants {
    static final String DASHBOARD_URL = "https://dashboard.buddybuild.com/";

    @Override
    public String getBaseDashboardUrl() {
        return DASHBOARD_URL;
    }
}
