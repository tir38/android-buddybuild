package com.buddybuild.rest;

import android.app.Application;

/**
 * An {@link Application} to be used in the instrumented test apk that sets up Dagger dependencies for testing.
 */
public class MockApplication extends Application {

    private MockComponent mockComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mockComponent = DaggerMockComponent.builder()
                .mockModule(new MockModule(this))
                .restModule(new RestModule())
                .build();
    }

    public MockComponent getMockComponent() {
        return mockComponent;
    }
}
