package com.buddybuild.rest;

import com.buddybuild.App;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Coordinates all requests to the BuddyBuild REST API
 */
public class RestCoordinator {

    private WebService webService;
    private DashboardWebService dashboardWebService;
    private TokenStore tokenStore;

    RestCoordinator(WebService webService, DashboardWebService dashboardWebService, TokenStore tokenStore) {
        this.webService = webService;
        this.dashboardWebService = dashboardWebService;
        this.tokenStore = tokenStore;
    }

    /**
     * TODO
     *
     * @return
     */
    public Observable<List<App>> getApps() {
        return webService.getApps().map(response -> {
            List<App> apps = new ArrayList<>();
            if (response.isSuccessful()) {
                List<AppResponse> appResponses = response.body();
                for (AppResponse appResponse : appResponses) {
                    App app = appResponse.toApp();
                    if (app != null) {
                        apps.add(appResponse.toApp());
                    }
                }
                return apps;
            } else {
                return apps; // TODO handle nonsuccess
            }
        });
    }

    /**
     * TODO
     *
     * @param email
     * @param password
     * @return
     */
    public Observable<Boolean> login(String email, String password) {
        return dashboardWebService.login(new LoginRequestBody(email, password))
                .map(response -> {
                    if (response.isSuccessful()) {
                        tokenStore.setToken(response.body().getSessionToken());
                    }
                    return response.isSuccessful();
                });
    }
}
