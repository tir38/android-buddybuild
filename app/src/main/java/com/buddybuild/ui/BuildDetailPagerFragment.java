package com.buddybuild.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;
import com.buddybuild.core.Build;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Fragment to host other fragments in view pager to display build details
 */
public final class BuildDetailPagerFragment extends Fragment {

    private static final String ARG_BUILD_ID = "ARG_BUILD_ID";

    @Inject
    protected Coordinator coordinator;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.fragment_build_detail_view_pager)
    protected ViewPager viewPager;
    @BindView(R.id.fragment_build_detail_tablayout)
    protected TabLayout tabLayout;

    private Unbinder unbinder;

    public static BuildDetailPagerFragment newInstance(String buildId) {
        Bundle args = new Bundle();
        args.putString(ARG_BUILD_ID, buildId);
        BuildDetailPagerFragment fragment = new BuildDetailPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BuddyBuildApplication application = (BuddyBuildApplication) context.getApplicationContext();
        application.getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_detail_pager, container, false);
        unbinder = ButterKnife.bind(this, view);

        String buildId = getArguments().getString(ARG_BUILD_ID);

        coordinator.getBuild(buildId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateUi,
                        Timber::e);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return BuildDetailsFragment.newInstance(buildId);
                    case 1:
                        return LogFragment.newInstance(buildId);
                    default:
                        throw new IllegalStateException("unknown pager adapter position: " + position);
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.details);
                    case 1:
                        return getString(R.string.logs);
                    default:
                        throw new IllegalStateException("unknown pager adapter position: " + position);
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateUi(Build build) {
        toolbar.setTitle(build.getBranchName());
        toolbar.setSubtitle(getString(R.string.build_number, build.getBuildNumber()));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> getActivity().finish());
    }
}
