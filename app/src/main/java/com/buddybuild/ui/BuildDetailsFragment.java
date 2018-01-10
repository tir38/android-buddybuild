package com.buddybuild.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.databinding.FragmentBuildDetailsBinding;
import com.buddybuild.ui.viewmodel.BuildViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class BuildDetailsFragment extends Fragment {

    private static final String ARG_BUILD_ID = "ARG_BUILD_ID";

    @Inject
    protected Coordinator coordinator;

    public static BuildDetailsFragment newInstance(String buildId) {
        Bundle args = new Bundle();
        args.putString(ARG_BUILD_ID, buildId);
        BuildDetailsFragment fragment = new BuildDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BuddyBuildApplication application = (BuddyBuildApplication) context.getApplicationContext();
        application.getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        BuildViewModel viewModel = new BuildViewModel();

        FragmentBuildDetailsBinding binding = FragmentBuildDetailsBinding.inflate(inflater, container, false);
        binding.setViewmodel(viewModel);

        String buildId = getArguments().getString(ARG_BUILD_ID);
        coordinator.getBuild(buildId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewModel::setBuild,
                        Timber::e);

        return binding.getRoot();
    }
}
