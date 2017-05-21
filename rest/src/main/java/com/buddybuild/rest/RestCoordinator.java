package com.buddybuild.rest;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Coordinates all requests to the BuddyBuild REST API
 */
public class RestCoordinator {

    private ApiWebService apiWebService;
    private DashboardWebService dashboardWebService;
    private TokenStore tokenStore;

    RestCoordinator(ApiWebService apiWebService, DashboardWebService dashboardWebService, TokenStore tokenStore) {
        this.apiWebService = apiWebService;
        this.dashboardWebService = dashboardWebService;
        this.tokenStore = tokenStore;
    }

    /**
     * @return a {@link Single} which emits a list of {@link App}s
     */
    public Single<List<App>> getApps() {
        return apiWebService
                .getApps()
                .map(response -> {
                    List<App> apps = new ArrayList<>();
                    if (response.isSuccessful()) {
                        List<AppResponseBody> appResponseBodies = response.body();
                        for (AppResponseBody appResponseBody : appResponseBodies) {
                            App app = appResponseBody.toApp();
                            if (app != null) {
                                apps.add(app);
                            }
                        }
                        return apps;
                    } else {
                        return apps; // for now on failure just emit empty list
                    }
                });
    }

    /**
     * Get all open {@link Branch}es for a given {@link App}
     *
     * @param appId ID of the {@link App}
     * @return Single of List<Branch>
     */
    public Single<List<Branch>> getBranches(String appId) {
        return apiWebService
                .getBranchesForApp(appId)
                .map(response -> {
                    List<Branch> branches = new ArrayList<>();
                    if (response.isSuccessful()) {
                        List<BranchResponseBody> branchResponseBodies = response.body();
                        for (BranchResponseBody branchResponseBody : branchResponseBodies) {
                            Branch branch = branchResponseBody.toBranch();
                            if (branch != null) {
                                branches.add(branch);
                            }
                        }
                        return branches;
                    } else {
                        return branches; // for now on failure just emit empty list
                    }
                });
    }

    /**
     * Get up to five most recent {@link Build}s for a given {@link Branch} name
     *
     * @param appId      ID of the {@link App}
     * @param branchName name of  the {@link Branch}
     * @return Single of List<Build>
     */
    public Single<List<Build>> getBuildsForBranch(final String appId, final String branchName) {
        return apiWebService.getBuildsForBranch(appId, branchName)
                .map(response -> {
                    List<Build> builds = new ArrayList<>();
                    if (response.isSuccessful()) {
                        for (BuildResponseBody buildResponseBody : response.body()) {
                            builds.add(buildResponseBody.toBuild());
                        }
                    }
                    return builds; // for now emit empty list on failure
                });
    }

    /**
     * Login to BuddyBuild API
     *
     * @param email    user's email
     * @param password user's password
     * @return a {@link Single} that emits true if success, and false if failure
     */
    public Single<Boolean> login(String email, String password) {
        return dashboardWebService
                .login(new LoginRequestBody(email, password))
                .map(response -> {
                    if (response.isSuccessful()) {
                        tokenStore.setToken(response.body().getSessionToken());
                    }
                    return response.isSuccessful();
                });
    }
}
