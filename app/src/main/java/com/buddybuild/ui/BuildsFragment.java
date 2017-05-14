package com.buddybuild.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;
import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;

import java.util.ArrayList;
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
    @BindView(R.id.fragment_builds_recyclerview)
    RecyclerView recyclerView;

    private List<App> apps;
    private Unbinder unbinder;
    private String appId;
    private BuildsAdapter buildsAdapter;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        buildsAdapter = new BuildsAdapter();
        recyclerView.setAdapter(buildsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        coordinator.getApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apps -> BuildsFragment.this.apps = apps);
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

        coordinator.getBranches(appId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(branches -> buildsAdapter.setBranches(branches));
    }

    private static class BuildsAdapter extends RecyclerView.Adapter<BuildsViewHolder> {

        private List<Branch> branches = new ArrayList<>();

        @Override
        public BuildsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_branch, parent, false);
            return new BuildsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BuildsViewHolder holder, int position) {
            holder.bind(branches.get(position));
        }

        @Override
        public int getItemCount() {
            return branches.size();
        }

        public void setBranches(List<Branch> branches) {
            this.branches = branches;
            notifyDataSetChanged();
        }
    }

    private static class BuildsViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;

        public BuildsViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.list_item_branch_name);
        }

        public void bind(Branch branch) {
            // temp solution to visualize data
            StringBuilder temp = new StringBuilder();
            for (Build build : branch.getBuilds()) {
                temp.append(build.getBuildNumber());
                temp.append(", ");
            }

            nameTextView.setText(branch.getName() + " : " + temp);
        }
    }
}
