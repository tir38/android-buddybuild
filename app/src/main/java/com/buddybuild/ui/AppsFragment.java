package com.buddybuild.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;
import com.buddybuild.core.App;
import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Fragment for displaying list of {@link App}s
 */
public class AppsFragment extends Fragment {

    @Inject
    protected Coordinator coordinator;

    @BindView(R.id.fragment_apps_recyclerview)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private AppsAdapter adapter;

    private Callbacks callbacks;

    public static AppsFragment newInstance() {
        return new AppsFragment();
    }

    interface Callbacks {
        void onAppClicked(String appId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BuddyBuildApplication application = (BuddyBuildApplication) context.getApplicationContext();
        application.getComponent().inject(this);

        try {
            callbacks = (Callbacks) context;
        } catch (ClassCastException e) {
            Timber.e(e, "context does not implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apps, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AppsAdapter();
        recyclerView.setAdapter(adapter);
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.line_divider);
        recyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        coordinator.getApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apps -> adapter.update(apps),
                        Timber::e);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

        private List<App> apps = new ArrayList<>();
        private int selectedItem;

        @Override
        public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_app, parent, false);
            return new AppViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AppViewHolder holder, int position) {
            holder.itemView.setSelected(selectedItem == position);
            holder.bind(apps.get(position), v -> {
                callbacks.onAppClicked(apps.get(position).getId());
                notifyItemChanged(selectedItem);
                selectedItem = holder.getAdapterPosition();
                notifyItemChanged(selectedItem);
            });
        }

        @Override
        public int getItemCount() {
            return apps.size();
        }

        private void update(List<App> apps) {
            this.apps.clear();
            this.apps.addAll(apps);
            notifyDataSetChanged();
        }

        class AppViewHolder extends RecyclerView.ViewHolder {

            private final TextView nameTextView;
            private final Button platformButton;
            private View itemView;

            private AppViewHolder(View itemView) {
                super(itemView);
                nameTextView = (TextView) itemView.findViewById(R.id.list_item_app_name);
                platformButton = (Button) itemView.findViewById(R.id.list_item_app_platform_button);
                this.itemView = itemView;
            }

            private void bind(App app, View.OnClickListener onClickListener) {
                itemView.setOnClickListener(onClickListener);

                nameTextView.setText(app.getName());

                switch (app.getPlatform()) {
                    case ANDROID:
                        platformButton.setText(getString(R.string.android));
                        break;
                    case IOS:
                        platformButton.setText(getString(R.string.ios));
                        break;
                    default:
                        throw new IllegalStateException("unknown platform");
                }
            }
        }
    }
}
