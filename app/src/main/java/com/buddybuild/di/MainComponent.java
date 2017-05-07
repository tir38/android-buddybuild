package com.buddybuild.di;


import com.buddybuild.ui.AppsActivity;
import com.buddybuild.ui.AppsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for main package
 */
@Singleton
@Component(modules = {MainModule.class})
public interface MainComponent {
    void inject(AppsActivity activity);

    void inject(AppsFragment appsFragment);
}

