package com.buddybuild.di;

import com.buddybuild.rest.ApiConstants;
import com.buddybuild.rest.LiveApiConstants;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module to provide web constants
 */
@Module
class WebConstantsModule {
    @Provides
    ApiConstants provideApiConstants() {
        return new LiveApiConstants();
    }
}
