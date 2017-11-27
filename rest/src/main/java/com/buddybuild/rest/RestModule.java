package com.buddybuild.rest;

import android.content.Context;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

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

    @Provides
    @Singleton
    RestCoordinator provideRestCoordinator(DashboardWebService dashboardWebService, TokenStore tokenStore,
                                           ApiConstants apiConstants) {
        return new LiveRestCoordinator(dashboardWebService, tokenStore, apiConstants);
    }

    @Provides
    @Singleton
    DashboardWebService provideDashboardWebService(ApiConstants apiConstants, OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(apiConstants.getBaseDashboardUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        Retrofit retrofit = builder.client(okHttpClient)
                .build();

        return retrofit.create(DashboardWebService.class);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(TokenStore tokenStore, Context context) {
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
                // TODO can we use Timber here?
                Log.w(TAG, "received non-json response");
                return null;
            }
        });

        // add logging interceptor
        // (since this will use system Log (not Timber) we have to manually disable logging in release builds)
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }

        // add cookie jar to store cookies for request
        ClearableCookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        okHttpClientBuilder.cookieJar(cookieJar);

        return okHttpClientBuilder.build();
    }

    @Provides
    @Singleton
    TokenStore provideTokenStore() {
        return new LiveTokenStore();
    }
}
