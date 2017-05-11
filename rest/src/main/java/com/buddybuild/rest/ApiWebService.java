package com.buddybuild.rest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;


/**
 * Retrofit interface for https://api.buddybuild.com/v1/
 */
interface ApiWebService {

    // https://api.buddybuild.com/v1/apps
    @GET("apps")
    Single<Response<List<AppResponseBody>>> getApps();
}
