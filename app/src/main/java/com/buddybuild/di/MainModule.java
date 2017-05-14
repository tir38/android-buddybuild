package com.buddybuild.di;

import android.content.Context;

import com.buddybuild.Coordinator;
import com.buddybuild.LiveCoordinator;
import com.buddybuild.rest.RestCoordinator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private Context context;

    public MainModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    Coordinator provideCoordinator(RestCoordinator restCoordinator) {
        return new LiveCoordinator(restCoordinator);
    }
}
