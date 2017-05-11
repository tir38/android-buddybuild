package com.buddybuild.rest;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface DashboardWebService {

    // https://dashboard.buddybuild.com/auth/local/login
    @POST("auth/local/login")
    Observable<Response<LoginResponseBody>> login(@Body LoginRequestBody loginRequestBody);
}
