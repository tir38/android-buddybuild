package com.buddybuild.di;

import android.content.Context;

import com.buddybuild.Coordinator;
import com.buddybuild.DemoCoordinator;
import com.buddybuild.DemoManager;
import com.buddybuild.LiveCoordinator;
import com.buddybuild.rest.RestCoordinator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class MainModule {

    private Context context;

    private DemoCoordinator demoCoordinator;
    private LiveCoordinator liveCoordinator;

    public MainModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    Coordinator provideCoordinator(RestCoordinator restCoordinator, DemoManager demoManager) {
        // Since we switch back and forth between live and demo, we can't use an @Singleton annotation.
        // So we have to mimic the singleton pattern ourselves.
        if (demoManager.isDemoMode()) {
            if (demoCoordinator == null) {
                Timber.d("DI: creating demo coordinator");
                demoCoordinator = new DemoCoordinator(demoManager);
            }
            Timber.d("DI: returning demo coordinator");
            return demoCoordinator;
        } else {
            if (liveCoordinator == null) {
                Timber.d("DI: creating live coordinator");
                liveCoordinator = new LiveCoordinator(restCoordinator, context);
            }

            Timber.d("DI: returning live coordinator");
            return liveCoordinator;
        }
    }

    @Provides
    @Singleton
    DemoManager provideDemoManager() {
        return new DemoManager();
    }
}
