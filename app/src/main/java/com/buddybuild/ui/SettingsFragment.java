package com.buddybuild.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Fragment for displaying app settings
 */
public class SettingsFragment extends Fragment {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.fragment_settings_sign_out_button)
    protected Button signOutButton;

    @Inject
    protected Coordinator coordinator;

    private Unbinder unbinder;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BuddyBuildApplication) context.getApplicationContext()).getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupToolbar();

        if (coordinator.isLoggedIn()) {
            signOutButton.setVisibility(View.VISIBLE);
            String signOutString = getString(R.string.sign_out_email, coordinator.getLoggedInUserEmail());
            signOutButton.setText(signOutString);
        } else {
            signOutButton.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragment_settings_sign_out_button)
    protected void onSignoutCicked() {
        coordinator.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((success, throwable) -> {
                    if (success) {
                        Intent intent = LoginActivity.newIntent(getContext());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    Timber.e("something went wrong trying to log out");
                });
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> getActivity().finish());
        toolbar.setTitle(R.string.settings);
    }
}
