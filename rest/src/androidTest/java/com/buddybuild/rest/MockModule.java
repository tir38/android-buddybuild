package com.buddybuild.rest;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.appflate.restmock.RESTMockServer;

/**
 * Dagger module to provide mocks for integration tests
 */
@Module
class MockModule {

    private Context context;

    MockModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    ApiConstants provideApiConstants() {
        return RESTMockServer::getUrl;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return context;
    }
}

