package com.buddybuild.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;
import com.buddybuild.core.App;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OverviewActivity extends AppCompatActivity implements AppsFragment.Callbacks {

    private static final String TAG_MASTER_FRAGMENT = "TAG_MASTER_FRAGMENT";
    private static final String TAG_DETAIL_FRAGMENT = "TAG_DETAIL_FRAGMENT";

    @Inject
    protected Coordinator coordinator;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private String selectedAppId;
    private List<App> apps;

    public static Intent newIntent(Context context) {
        return new Intent(context, OverviewActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overview);

        ((BuddyBuildApplication) getApplication()).getComponent().inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_overview);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings_menu_item:
                    Intent intent = SettingsActivity.newIntent(OverviewActivity.this);
                    startActivity(intent);
                    return true;

                default:
                    throw new RuntimeException("unknown menu item");
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawerLayout != null) {
            // setup drawer view
            NavigationView navigationView = (NavigationView) findViewById(R.id.master_fragment_container);
            navigationView.setNavigationItemSelectedListener(item -> true);

            // setup menu icon
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        // insert builds fragment into detail container
        BuildsFragment buildsFragment
                = (BuildsFragment) fragmentManager.findFragmentByTag(TAG_DETAIL_FRAGMENT);
        if (buildsFragment == null) {
            buildsFragment = BuildsFragment.newInstance();

            fragmentManager.beginTransaction()
                    .add(R.id.detail_fragment_container, buildsFragment, TAG_DETAIL_FRAGMENT)
                    .commit();
        }

        // insert apps fragment into master container (i.e. nav view)
        AppsFragment appsFragment
                = (AppsFragment) fragmentManager.findFragmentByTag(TAG_MASTER_FRAGMENT);

        if (appsFragment == null) {
            appsFragment = AppsFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.master_fragment_container, appsFragment, TAG_MASTER_FRAGMENT)
                    .commit();
        }

        coordinator.getApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apps -> {
                            OverviewActivity.this.apps = apps;
                            updateBuildsFragment(apps.get(0).getId());
                            updateToolbar();
                        },
                        Timber::e);
    }

    @Override
    public void onAppClicked(String appId) {
        selectedAppId = appId;

        updateBuildsFragment(appId);

        updateToolbar();

        // Close the navigation drawer
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
    }

    private void updateBuildsFragment(String appId) {
        BuildsFragment buildsFragment =
                (BuildsFragment) getSupportFragmentManager().findFragmentByTag(TAG_DETAIL_FRAGMENT);
        buildsFragment.setApp(appId);
    }

    private void updateToolbar() {
        if (selectedAppId != null) {
            for (App app : apps) {
                if (app.getId().equals(selectedAppId)) {
                    toolbar.setTitle(app.getName());
                    toolbar.setSubtitle(app.getPlatform().prettyString());
                }
            }
        } else {
            // if none selected, just display the top one
            App app = apps.get(0);
            toolbar.setTitle(app.getName());
            toolbar.setSubtitle(app.getPlatform().prettyString());
        }
    }
}
