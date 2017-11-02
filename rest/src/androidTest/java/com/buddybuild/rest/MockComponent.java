package com.buddybuild.rest;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;

/**
 * Dagger {@link Component} to setup {@link Module}s for integration testing
 */
@Singleton
@Component(modules = {RestModule.class, MockModule.class})
interface MockComponent {
    void inject(IntegrationTests integrationTests);
}