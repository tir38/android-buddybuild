package com.buddybuild.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;
import com.buddybuild.core.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment for displaying list of builds
 */
public class BuildsFragment extends Fragment {

    @Inject
    protected Coordinator coordinator;

    @BindView(R.id.fragment_builds_textview)
    protected TextView textview;

    private List<App> apps;
    private Unbinder unbinder;
    private String appId;

    public static BuildsFragment newInstance() {
        return new BuildsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BuddyBuildApplication application = (BuddyBuildApplication) context.getApplicationContext();
        application.getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_builds, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        coordinator.getApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apps -> {
                    BuildsFragment.this.apps = apps;
                    updateUi();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Set the {@link App} displayed by this fragment
     *
     * @param appId
     */
    public void setApp(String appId) {
        this.appId = appId;
        updateUi();
    }

    private void updateUi() {
        if (apps != null) {
            if (appId == null) {
                // no appId set yet, just display the first app
                textview.setText(apps.get(0).getName());
            } else {
                for (App app : apps) {
                    if (app.getId().equals(appId)) {
                        textview.setText(app.getName());
                    }
                }
            }
        }
    }
}
