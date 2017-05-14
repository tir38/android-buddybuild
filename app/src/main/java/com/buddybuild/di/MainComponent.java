package com.buddybuild.di;

import com.buddybuild.rest.RestModule;
import com.buddybuild.ui.AppsFragment;
import com.buddybuild.ui.LoginFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for main package
 */
@Singleton
@Component(modules = {MainModule.class, RestModule.class})
public interface MainComponent {

    void inject(AppsFragment appsFragment);

    void inject(LoginFragment loginFragment);
}

