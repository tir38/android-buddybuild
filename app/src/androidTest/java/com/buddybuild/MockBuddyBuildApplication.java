package com.buddybuild;

import com.buddybuild.di.DaggerMainComponent;
import com.buddybuild.di.MainComponent;
import com.buddybuild.di.MainModule;

/**
 * Subclass of {@link BuddyBuildApplication} that allows injection of different Dagger components for testing
 */
public class MockBuddyBuildApplication extends BuddyBuildApplication {
    @Override
    protected MainComponent createComponent() {
        return DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .build();
    }
}
