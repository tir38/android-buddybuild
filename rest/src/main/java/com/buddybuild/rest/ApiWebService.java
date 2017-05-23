package com.buddybuild.rest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Retrofit interface for https://api.buddybuild.com/v1/
 */
interface ApiWebService {

    // http://docs.buddybuild.com/docs/list-apps
    // https://api.buddybuild.com/v1/apps
    @GET("apps")
    Single<Response<List<AppResponseBody>>> getApps();

    // http://docs.buddybuild.com/docs/list-branches
    // e.g. https://api.buddybuild.com/v1/apps/123abc/branches
    @GET("apps/{app_id}/branches")
    Single<Response<List<BranchResponseBody>>> getBranchesForApp(@Path("app_id") String appId);

    // http://docs.buddybuild.com/docs/list-builds
    // e.g. https://api.buddybuild.com/v1/apps/123abc/builds
    @GET("apps/{app_id}/builds")
    Single<Response<List<BuildResponseBody>>> getBuildsForApp(@Path("app_id") String appId);

    // e.g. https://api.buddybuild.com/v1/apps/123abc/builds/?branch=master
    @GET("apps/{app_id}/builds")
    Single<Response<List<BuildResponseBody>>> getBuildsForBranch(@Path("app_id") String appId,
                                                                 @Query("branch") String branch);
}
