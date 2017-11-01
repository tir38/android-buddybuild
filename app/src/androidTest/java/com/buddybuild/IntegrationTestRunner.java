package com.buddybuild;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Subclass of {@link AndroidJUnitRunner} that allows setting {@link Application} for use during integration tests
 */
public final class IntegrationTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader classLoader, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        return super.newApplication(classLoader,
                MockBuddyBuildApplication.class.getName(),
                context);
    }
}
