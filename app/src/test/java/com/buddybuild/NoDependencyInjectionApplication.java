package com.buddybuild;

import com.buddybuild.di.MainComponent;
import com.buddybuild.ui.AppsFragment;
import com.buddybuild.ui.BuildDetailPagerFragment;
import com.buddybuild.ui.BuildDetailsFragment;
import com.buddybuild.ui.BuildsFragment;
import com.buddybuild.ui.LogFragment;
import com.buddybuild.ui.LoginFragment;
import com.buddybuild.ui.OverviewActivity;
import com.buddybuild.ui.SettingsFragment;

/**
 * Extension of {@link BuddyBuildApplication} that does NOT setup three-ten-abp and does NOT setup Dagger D.I.
 */
public class NoDependencyInjectionApplication extends BuddyBuildApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected MainComponent createComponent() {
        return new MainComponent() {

            @Override
            public void inject(AppsFragment appsFragment) {
                // no-op
            }

            @Override
            public void inject(LoginFragment loginFragment) {
                // no-op
            }

            @Override
            public void inject(OverviewActivity overviewActivity) {
                // no-op
            }

            @Override
            public void inject(BuildsFragment buildsFragment) {
                // no-op
            }

            @Override
            public void inject(LogFragment logFragment) {
                // no-op
            }

            @Override
            public void inject(BuildDetailPagerFragment buildDetailPagerFragment) {
                // no-op
            }

            @Override
            public void inject(BuildDetailsFragment buildDetailsFragment) {
                // no-op
            }

            @Override
            public void inject(SettingsFragment settingsFragment) {
                // no-op
            }
        };
    }

    @Override
    protected void setupThreeTenLib() {
        // do nothing, do not call super
    }
}
