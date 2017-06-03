package com.buddybuild;

import android.app.Application;

import com.buddybuild.di.DaggerMainComponent;
import com.buddybuild.di.MainComponent;
import com.buddybuild.di.MainModule;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class BuddyBuildApplication extends Application {

    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();

        AndroidThreeTen.init(this);
    }

    public MainComponent getComponent() {
        return component;
    }

    protected MainComponent createComponent() {
        return DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .build();
    }
}
