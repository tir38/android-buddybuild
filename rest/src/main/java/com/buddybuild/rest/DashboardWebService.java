package com.buddybuild.rest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Retrofit interface for https://dashboard.buddybuild.com/
 *
 * Internal
 */
interface DashboardWebService {

    // https://dashboard.buddybuild.com/auth/local/login
    @POST("auth/local/login")
    Single<Response<LoginResponseBody>> login(@Body LoginRequestBody loginRequestBody);

    // e.g. https://dasboard.buddybuild.com/api/builds/{:build_number}/logs
    @GET("api/builds/{build_id}/logs")
    Single<Response<List<LogItemResponseBody>>> getLogs(@Path("build_id") String buildId);
}
