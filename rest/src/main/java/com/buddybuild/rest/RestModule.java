package com.buddybuild.rest;

import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger {@link Module} for the "rest" Gradle module
 */
@Module
public class RestModule {

    private static final String TAG = RestModule.class.getSimpleName();
    public static final String DASHBOARD_URL = "https://dashboard.buddybuild.com/";

    @Provides
    @Singleton
    RestCoordinator provideWebserviceWrapper(ApiWebService apiWebService, DashboardWebService dashboardWebService,
                                             TokenStore tokenStore) {
        return new RestCoordinator(apiWebService, dashboardWebService, tokenStore);
    }

    @Provides
    @Singleton
    ApiWebService provideApiWebservice(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.buddybuild.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        return builder.client(okHttpClient)
                .build()
                .create(ApiWebService.class);
    }

    @Provides
    @Singleton
    DashboardWebService provideDashboardWebService(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(DASHBOARD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        Retrofit retrofit = builder.client(okHttpClient)
                .build();

        return retrofit.create(DashboardWebService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(TokenStore tokenStore) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // add session token
        okHttpClientBuilder.addInterceptor(chain -> {
            String tokenString = "Bearer " + tokenStore.getToken();
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Authorization", tokenString)
                    .addHeader("Accept", "application/json")
                    .build();
            return chain.proceed(request);
        });

        okHttpClientBuilder.addInterceptor(chain -> {
            Response response = chain.proceed(chain.request());
            String contentType = response.header("content-type");
            if (contentType.contains("json")) {
                return response;
            } else {
                Log.w(TAG, "received non-json response");
                return null;
            }
        });

        // add logging interceptor
        // (since this will use system Log (not Timber) we have to manually disable logging in release builds)
//        if (BuildConfig.DEBUG) { // TODO fix
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
