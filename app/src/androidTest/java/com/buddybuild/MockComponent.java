package com.buddybuild;

import com.buddybuild.di.MainComponent;
import com.buddybuild.di.MainModule;
import com.buddybuild.rest.RestModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for the androidTest build
 */
@Singleton
@Component(modules = {MainModule.class, RestModule.class, MockWebConstantsModule.class})
public interface MockComponent extends MainComponent {
    // not injecting into any other places, yet
}