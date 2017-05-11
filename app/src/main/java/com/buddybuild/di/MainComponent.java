package com.buddybuild.di;

import com.buddybuild.rest.RestModule;
import com.buddybuild.ui.AppsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for main package
 */
@Singleton
@Component(modules = {MainModule.class, RestModule.class})
public interface MainComponent {

    void inject(AppsFragment appsFragment);
}

