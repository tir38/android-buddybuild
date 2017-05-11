package com.buddybuild.rest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger {@link Module} for the "rest" Gradle module
 */
@Module
public class RestModule {

    @Provides
    @Singleton
    WebService provideWebservice() {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.buddybuild.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // add access token
        okHttpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            request = request.newBuilder()
                    .header("Authorization", "Bearer FOOBAR")
                    .build();
            return chain.proceed(request);
        });

        // add logging interceptor
        // (since this will use system Log (not Timber) we have to manually disable logging in release builds)
//        if (BuildConfig.DEBUG) { // TODO figure out how to disable logging when in this module
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
//        }

        return builder.client(okHttpClientBuilder.build())
                .build()
                .create(WebService.class);
    }
}
