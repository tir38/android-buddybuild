package com.buddybuild.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buddybuild.App;
import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment for displaying list of {@link App}s
 */
public class AppsFragment extends Fragment {


    @Inject
    protected Coordinator coordinator;

    @BindView(R.id.fragment_apps_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_apps_toolbar)
    Toolbar toolbar;


    private Unbinder unbinder;
    private AppsAdapter adapter;

    public static AppsFragment newInstance() {
        return new AppsFragment();
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
        View view = inflater.inflate(R.layout.fragment_apps, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AppsAdapter();
        recyclerView.setAdapter(adapter);

        toolbar.setTitle(R.string.buddybuild);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        coordinator.login(ADD IN)
                .doOnNext(aBoolean -> getApps()) // TODO hacky
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aBoolean -> Toast.makeText(getContext(), "boolean", Toast.LENGTH_SHORT).show());

    }

    private void getApps() {
        coordinator.getApps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apps -> adapter.update(apps));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class AppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

        private List<App> apps = new ArrayList<>();

        @Override
        public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_app, parent, false);
            return new AppViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AppViewHolder holder, int position) {
            holder.bind(apps.get(position));
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
    }

    private static class AppViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final ImageView platformIconView;
        private final Drawable androidIcon;
        private final Drawable iosIcon;

        private AppViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.list_item_app_name);
            platformIconView = (ImageView) itemView.findViewById(R.id.list_item_platform_icon);
            androidIcon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_android_24dp);
            iosIcon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_apple_24dp);

        }

        private void bind(App app) {
            nameTextView.setText(app.getName());

            switch (app.getPlatform()) {
                case ANDROID:
                    platformIconView.setImageDrawable(androidIcon);
                    break;
                case IOS:
                    platformIconView.setImageDrawable(iosIcon);
                    break;
                default:
                    throw new IllegalStateException("unknown platform");
            }
        }
    }
}
