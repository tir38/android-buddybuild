package com.buddybuild.di;

import com.buddybuild.rest.RestModule;
import com.buddybuild.ui.AppsFragment;
import com.buddybuild.ui.BuildDetailPagerFragment;
import com.buddybuild.ui.BuildDetailsFragment;
import com.buddybuild.ui.BuildsFragment;
import com.buddybuild.ui.LogFragment;
import com.buddybuild.ui.LoginFragment;
import com.buddybuild.ui.OverviewActivity;
import com.buddybuild.ui.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger component for main package
 */
@Singleton
@Component(modules = {MainModule.class, RestModule.class, WebConstantsModule.class})
public interface MainComponent {

    void inject(AppsFragment appsFragment);

    void inject(LoginFragment loginFragment);

    void inject(OverviewActivity overviewActivity);

    void inject(BuildsFragment buildsFragment);

    void inject(LogFragment logFragment);

    void inject(BuildDetailPagerFragment buildDetailPagerFragment);

    void inject(BuildDetailsFragment buildDetailsFragment);

    void inject(SettingsFragment settingsFragment);
}

