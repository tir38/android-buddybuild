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
    RestCoordinator provideWebserviceWrapper(WebService webService, DashboardWebService dashboardWebService, TokenStore tokenStore) {
        return new RestCoordinator(webService, dashboardWebService, tokenStore);
    }

    @Provides
    @Singleton
    WebService provideWebservice(OkHttpClient okHttpClient) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.buddybuild.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        return builder.client(okHttpClient)
                .build()
                .create(WebService.class);
    }

    @Provides
    @Singleton
    DashboardWebService provideDashboardWebService(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://dashboard.buddybuild.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        return builder.client(okHttpClient)
                .build()
                .create(DashboardWebService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(TokenStore tokenStore) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // add access sessionToken
        okHttpClientBuilder.addInterceptor(chain -> {
            String tokenString = "Bearer " + tokenStore.getToken();
            Request request = chain.request();
            request = request.newBuilder()
                    .header("Authorization", tokenString)
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

        return okHttpClientBuilder.build();
    }

    @Provides
    @Singleton
    TokenStore provideTokenStore() {
        return new LiveTokenStore();
    }
}
