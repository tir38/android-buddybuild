package com.buddybuild.rest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Retrofit interface for https://api.buddybuild.com/v1/
 */
interface ApiWebService {

    // https://api.buddybuild.com/v1/apps
    @GET("apps")
    Single<Response<List<AppResponseBody>>> getApps();

    // https://api.buddybuild.com/v1/apps/:app_id/branches
    @GET("apps/{app_id}/branches")
    Single<Response<List<BranchResponseBody>>> getBranchesForApp(@Path("app_id") String appId);

    // https://api.buddybuild.com/v1/apps/:app_id/builds
    @GET("apps/{app_id}/builds")
    Single<Response<List<BuildResponseBody>>> getBuildsForApp(@Path("app_id") String appId);
}
