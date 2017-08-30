package com.buddybuild.rest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for https://dashboard.buddybuild.com/
 * <p>
 * Internal
 */
interface DashboardWebService {

    // https://dashboard.buddybuild.com/auth/local/login
    @POST("auth/local/login")
    Single<Response<LoginResponseBody>> login(@Body LoginRequestBody loginRequestBody);

    // https://dashboard.buddybuild.com/api/apps
    @GET("api/apps")
    Single<Response<List<AppResponseBody>>> getApps();

    // https://dashboard.buddybuild.com/api/branches?app_id=APP_ID
    @GET("api/branches")
    Single<Response<List<BranchResponseBody>>> getBranchesForApp(@Query("app_id") String appId);

    // https://dashboard.buddybuild.com/api/builds?app_id=APP_ID&branch=master&limit=1
    @GET("api/builds")
    Single<Response<List<BuildResponseBody>>> getBuildsForBranch(@Query("app_id") String appId,
                                                                 @Query("branch") String branch,
                                                                 @Query("limit") int limit);

    // https://dasboard.buddybuild.com/api/builds/BUILD_NUMBER/logs
    @GET("api/builds/{build_id}/logs")
    Single<Response<List<LogItemResponseBody>>> getLogs(@Path("build_id") String buildId);
}
