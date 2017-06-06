package com.buddybuild.rest;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;
import com.buddybuild.core.LogItem;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Coordinates all requests to the BuddyBuild REST API
 */
public class RestCoordinator {

    // Buddybuild supplied error messages (subject to change w/out notice)
    private static final String UNKNOWN_EMAIL_MESSAGE = "unknown email address";
    private static final String MISMATCHED_PASSWORD_MESSAGE = "email and password you entered do not match";
    private static final String THROTTLE_LIMIT_MESSAGE = "throttle limit for login";


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
     * @return a {@link Single} that emits a {@link LoginResult}
     */
    public Single<LoginResult> login(String email, String password) {
        return dashboardWebService
                .login(new LoginRequestBody(email, password))
                .doOnSuccess(response -> {
                    if (response.isSuccessful()) {
                        tokenStore.setToken(response.body().getSessionToken());
                    }
                })
                .map(response -> {
                    if (response.isSuccessful()) {
                        return LoginResult.SUCCESS;
                    }

                    // convert errorBody
                    Converter<ResponseBody, LoginErrorResponseBody> converter
                            = new Retrofit.Builder()
                            .baseUrl(RestModule.DASHBOARD_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .responseBodyConverter
                                    (LoginErrorResponseBody.class, new Annotation[0]);

                    LoginErrorResponseBody errorBody = converter.convert(response.errorBody());

                    int code = response.code();
                    String errorMessage = errorBody.getMessage();
                    Timber.d("%d: %s", code, errorMessage);
                    if (errorMessage == null || errorMessage.isEmpty()) {
                        return LoginResult.UNKNOWN_FAILURE;
                    }

                    switch (code) {
                        case 401:
                            if (errorMessage.toLowerCase().contains(UNKNOWN_EMAIL_MESSAGE)) {
                                return LoginResult.UNKNOWN_EMAIL;
                            }
                            if (errorMessage.toLowerCase().contains(MISMATCHED_PASSWORD_MESSAGE)) {
                                return LoginResult.EMAIL_PASSWORD_MISMATCH;
                            }
                            if (errorMessage.toLowerCase().contains(THROTTLE_LIMIT_MESSAGE)) {
                                return LoginResult.THROTTLE_LIMIT;
                            }
                            return LoginResult.UNKNOWN_FAILURE;

                        default:
                            return LoginResult.UNKNOWN_FAILURE;
                    }
                });
    }

    /**
     * Get Logs for a single {@link Build}
     *
     * @param buildId ID of the build
     * @return a {@link Single} that emits list of logs
     */
    public Single<List<LogItem>> getLog(String buildId) {
        return dashboardWebService.getLogs(buildId)
                .map(listResponse -> {
                    List<LogItem> logs = new ArrayList<>();
                    if (listResponse.isSuccessful()) {
                        List<LogItemResponseBody> body = listResponse.body();
                        for (LogItemResponseBody item : body) {
                            logs.add(item.toLogItem());
                        }
                    }
                    return logs;
                });
    }
}
