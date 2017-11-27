package com.buddybuild.rest;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;
import com.buddybuild.core.LogItem;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

class LiveRestCoordinator implements RestCoordinator {

    // Buddybuild supplied error messages (subject to change w/out notice)
    private static final String UNKNOWN_EMAIL_MESSAGE = "unknown email address";
    private static final String MISMATCHED_PASSWORD_MESSAGE = "email and password you entered do not match";
    private static final String THROTTLE_LIMIT_MESSAGE = "throttle limit for login";
    private static final int BUILD_COUNT_LIMIT = 5;

    private DashboardWebService dashboardWebService;
    private TokenStore tokenStore;
    private final ApiConstants apiConstants;

    LiveRestCoordinator(DashboardWebService dashboardWebService, TokenStore tokenStore, ApiConstants apiConstants) {
        this.dashboardWebService = dashboardWebService;
        this.tokenStore = tokenStore;
        this.apiConstants = apiConstants;
    }

    @Override
    public Single<List<App>> getApps() {
        return dashboardWebService
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

    @Override
    public Single<List<Branch>> getBranches(String appId) {
        return dashboardWebService
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

    @Override
    public Single<List<Build>> getBuildsForBranch(final String appId, final String branchName) {
        return dashboardWebService.getBuildsForBranch(appId, branchName, BUILD_COUNT_LIMIT)
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

    @Override
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
                        return new LoginResult(LoginResult.Result.SUCCESS, response.body().getSessionToken());
                    }

                    // convert errorBody
                    Converter<ResponseBody, LoginErrorResponseBody> converter
                            = new Retrofit.Builder()
                            .baseUrl(apiConstants.getBaseDashboardUrl())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .responseBodyConverter(LoginErrorResponseBody.class, new Annotation[0]);

                    LoginErrorResponseBody errorBody = converter.convert(response.errorBody());

                    int code = response.code();
                    String errorMessage = errorBody.getMessage();
                    Timber.d("%d: %s", code, errorMessage);
                    if (errorMessage == null || errorMessage.isEmpty()) {
                        return new LoginResult(LoginResult.Result.UNKNOWN_FAILURE, null);
                    }

                    switch (code) {
                        case 401:
                            if (errorMessage.toLowerCase(Locale.getDefault()).contains(UNKNOWN_EMAIL_MESSAGE)) {
                                return new LoginResult(LoginResult.Result.UNKNOWN_EMAIL, null);
                            }
                            if (errorMessage.toLowerCase(Locale.getDefault()).contains(MISMATCHED_PASSWORD_MESSAGE)) {
                                return new LoginResult(LoginResult.Result.EMAIL_PASSWORD_MISMATCH, null);
                            }
                            if (errorMessage.toLowerCase(Locale.getDefault()).contains(THROTTLE_LIMIT_MESSAGE)) {
                                return new LoginResult(LoginResult.Result.THROTTLE_LIMIT, null);
                            }
                            return new LoginResult(LoginResult.Result.UNKNOWN_FAILURE, null);

                        default:
                            return new LoginResult(LoginResult.Result.UNKNOWN_FAILURE, null);
                    }
                });
    }

    @Override
    public void setLoginToken(String token) {
        tokenStore.setToken(token);
    }

    @Override
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
