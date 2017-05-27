package com.buddybuild.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buddybuild.R;


public final class BuildDetailsFragment extends Fragment {

    private static final String ARG_BUILD_ID = "ARG_BUILD_ID";

    public static BuildDetailsFragment newInstance(String buildId) {
        Bundle args = new Bundle();
        args.putString(ARG_BUILD_ID, buildId);
        BuildDetailsFragment fragment = new BuildDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_details, container, false);
        return view;
    }
}
