package com.buddybuild;

import com.buddybuild.rest.ApiConstants;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module to provide mock web constants
 */
@Module
public class MockWebConstantsModule {
    @Provides
    public ApiConstants provideApiConstants() {
        return new RestMockApiConstants();
    }
}

