package com.buddybuild;

import com.buddybuild.rest.ApiConstants;

import io.appflate.restmock.RESTMockServer;

/**
 * Implementation of {@link ApiConstants} that returns values for accessing {@link RESTMockServer}
 */
public class RestMockApiConstants implements ApiConstants {
    @Override
    public String getBaseDashboardUrl() {
        return RESTMockServer.getUrl();
    }
}
