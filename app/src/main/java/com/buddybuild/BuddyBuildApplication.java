package com.buddybuild;

import android.support.multidex.MultiDexApplication;

import com.buddybuild.di.DaggerMainComponent;
import com.buddybuild.di.MainComponent;
import com.buddybuild.di.MainModule;

public class BuddyBuildApplication extends MultiDexApplication {

    private MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();
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
