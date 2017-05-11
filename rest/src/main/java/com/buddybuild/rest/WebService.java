package com.buddybuild.rest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;


public interface WebService {

    // https://api.buddybuild.com/v1/apps
    @GET("apps")
    Observable<Response<List<AppResponse>>> getApps();
}
