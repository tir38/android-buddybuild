package com.buddybuild.rest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;


// TODO rename to ApiWebService
interface WebService {

    // https://api.buddybuild.com/v1/apps
    @GET("apps")
    Single<Response<List<AppResponse>>> getApps();
}
