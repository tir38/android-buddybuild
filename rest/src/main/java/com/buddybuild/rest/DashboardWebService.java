package com.buddybuild.rest;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface DashboardWebService {

    // https://dashboard.buddybuild.com/auth/local/login
    @POST("auth/local/login")
    Single<Response<LoginResponseBody>> login(@Body LoginRequestBody loginRequestBody);
}
