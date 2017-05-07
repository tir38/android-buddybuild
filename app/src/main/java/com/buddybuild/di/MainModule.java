package com.buddybuild.di;

import android.content.Context;

import com.buddybuild.BuildConfig;
import com.buddybuild.Coordinator;
import com.buddybuild.LiveCoordinator;
import com.buddybuild.rest.WebService;

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

@Module
public class MainModule {

    private Context context;

    public MainModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    Coordinator provideCoordinator(WebService webService) {
        return new LiveCoordinator(webService);
    }

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
                    .header("Authorization", "Bearer " + BuildConfig.ACCESS_TOKEN)
                    .build();
            return chain.proceed(request);
        });

        // add logging interceptor
        // (since this will use system Log (not Timber) we have to manually disable logging in release builds)
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
        }

        return builder.client(okHttpClientBuilder.build())
                .build()
                .create(WebService.class);
    }
}
