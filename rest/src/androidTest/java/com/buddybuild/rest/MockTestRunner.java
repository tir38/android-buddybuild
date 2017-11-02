package com.buddybuild.rest;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Subclass of {@link AndroidJUnitRunner} that allows setting {@link Application} for use during tests
 */
public final class MockTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader classLoader, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        return super.newApplication(classLoader, MockApplication.class.getName(), context);
    }
}
