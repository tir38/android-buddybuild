package com.buddybuild;

import android.app.Application;

import com.buddybuild.di.DaggerMainComponent;
import com.buddybuild.di.MainComponent;
import com.buddybuild.di.MainModule;
import com.buddybuild.sdk.BuddyBuild;
import com.jakewharton.threetenabp.AndroidThreeTen;

import timber.log.Timber;

public class BuddyBuildApplication extends Application {

    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();

        AndroidThreeTen.init(this);

        setupLogging();

        BuddyBuild.setup(this);
    }

    public MainComponent getComponent() {
        return component;
    }

    protected MainComponent createComponent() {
        return DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .build();
    }

    private void setupLogging() {
        // we want to log in Debug builds
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // TODO crash reporting tree
        }
    }

}
