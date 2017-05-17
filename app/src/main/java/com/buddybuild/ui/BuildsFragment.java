package com.buddybuild.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
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

    private Unbinder unbinder;
    private BranchBuildsAdapter adapter;
    private List<BranchItem> branchItems;

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
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_builds, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        branchItems = new ArrayList<>();
        adapter = new BranchBuildsAdapter(branchItems);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Set the {@link App} displayed by this fragment
     *
     * @param appId the ID of the {@link App} to display
     */
    public void setApp(String appId) {

        coordinator.getBranches(appId)
                .map(branches -> {
                    List<BranchItem> branchItems = new ArrayList<>();
                    for (Branch branch : branches) {
                        branchItems.add(new BranchItem(branch));
                    }
                    return branchItems;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(branchItems -> {
                    // Because of how ExpandableRecyclerAdapter works,
                    // the data is maintained OUTSIDE the adapter,
                    // contrary to normal pattern.
                    this.branchItems.clear();
                    this.branchItems.addAll(branchItems);
                    adapter.notifyParentDataSetChanged(true);
                });
    }

    private static class BranchBuildsAdapter extends ExpandableRecyclerAdapter<BranchItem,
            BuildItem, BranchViewHolder, BuildViewHolder> {

        private BranchBuildsAdapter(@NonNull List<BranchItem> branchItems) {
            super(branchItems);
        }

        @NonNull
        @Override
        public BranchViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
            View view = LayoutInflater.from(parentViewGroup.getContext())
                    .inflate(R.layout.list_item_branch, parentViewGroup, false);
            return new BranchViewHolder(view);
        }

        @NonNull
        @Override
        public BuildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
            View view = LayoutInflater.from(childViewGroup.getContext())
                    .inflate(R.layout.list_item_build, childViewGroup, false);
            return new BuildViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull BranchViewHolder branchViewHolder,
                                           int parentPosition,
                                           @NonNull BranchItem branchItem) {
            branchViewHolder.bind(branchItem);
        }

        @Override
        public void onBindChildViewHolder(@NonNull BuildViewHolder buildViewHolder,
                                          int parentPosition,
                                          int childPosition,
                                          @NonNull BuildItem buildItem) {

            buildViewHolder.bind(buildItem);
        }
    }

    private static class BranchViewHolder extends ParentViewHolder {

        private final TextView nameTextView;

        private BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.list_item_branch_name);
        }

        private void bind(BranchItem branchItem) {
            nameTextView.setText(branchItem.branch.getName());
        }
    }

    private static class BuildViewHolder extends ChildViewHolder {

        private final TextView nameTextView;

        private BuildViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.list_item_build_name);
        }

        private void bind(BuildItem buildItem) {
            nameTextView.setText(String.valueOf(buildItem.build.getBuildNumber()));
        }
    }

    /**
     * Wraps a {@link Branch} for use with ExpandableRecyclerView
     */
    private class BranchItem implements Parent<BuildItem> {
        private final Branch branch;

        private BranchItem(Branch branch) {
            this.branch = branch;
        }

        @Override
        public List<BuildItem> getChildList() {
            // TODO should I lazy create this only ONCE?
            List<BuildItem> buildItems = new ArrayList<>();
            for (Build build : branch.getBuilds()) {
                buildItems.add(new BuildItem(build));
            }
            return buildItems;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    /**
     * Wraps a {@link Build} for use with ExpandableRecyclerView
     */
    private class BuildItem {
        private final Build build;

        private BuildItem(Build build) {
            this.build = build;
        }
    }
}
