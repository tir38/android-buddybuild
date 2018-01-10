package com.buddybuild.ui;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buddybuild.BuddyBuildApplication;
import com.buddybuild.Coordinator;
import com.buddybuild.R;
import com.buddybuild.core.LogItem;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

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
 * Fragment for displaying the logs from a single build
 */
public final class LogFragment extends Fragment {

    private static final String ARG_BUILD_ID = "ARG_BUILD_ID";

    @Inject
    protected Coordinator coordinator;

    @BindView(R.id.fragment_logs_recyclerview)
    protected RecyclerView logsRecyclerView;
    @BindView(R.id.progress_indicator)
    protected View progressIndicator;
    @BindView(R.id.fragment_logs_no_logs_textview)
    protected TextView noLogsTextView;

    private ProgressIndicatorDelegate progressIndicatorDelegate;
    private Unbinder unbinder;

    private final LifecycleProvider<Lifecycle.Event> lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);

    public static LogFragment newInstance(String buildId) {
        Bundle args = new Bundle();
        args.putString(ARG_BUILD_ID, buildId);
        LogFragment fragment = new LogFragment();
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
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        unbinder = ButterKnife.bind(this, view);

        logsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LogAdapter adapter = new LogAdapter();
        logsRecyclerView.setAdapter(adapter);

        progressIndicatorDelegate = new ProgressIndicatorDelegate(getContext());
        progressIndicatorDelegate.setProgressIndicator(progressIndicator);

        String buildId = getArguments().getString(ARG_BUILD_ID);
        progressIndicatorDelegate.fadeInProgress();
        coordinator.getLogs(buildId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindUntilEvent(Lifecycle.Event.ON_PAUSE))
                .subscribe(
                        logs -> progressIndicatorDelegate.fadeOutProgress(
                                () -> {
                                    if (logs.isEmpty()) {
                                        noLogsTextView.setVisibility(View.VISIBLE);
                                        logsRecyclerView.setVisibility(View.GONE);
                                    } else {
                                        noLogsTextView.setVisibility(View.GONE);
                                        logsRecyclerView.setVisibility(View.VISIBLE);
                                        adapter.updateLogs(logs);
                                    }
                                }),
                        error -> progressIndicatorDelegate.fadeOutProgress(() -> Timber.e(error)));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        progressIndicatorDelegate = null;
    }

    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        private List<LogItem> logs = new ArrayList<>();

        @Override
        public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_log, parent, false);
            return new LogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            holder.bind(logs.get(position), position + 1);
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }

        private void updateLogs(List<LogItem> logs) {
            this.logs.clear();
            this.logs.addAll(logs);
            notifyDataSetChanged();
        }
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {

        private final TextView msgTextView;
        private final TextView lineNumberTextView;

        private final int blue;
        private final int purple;
        private final int red;
        private final int white;
        private final int yellow;

        private LogViewHolder(View itemView) {
            super(itemView);
            msgTextView = (TextView) itemView.findViewById(R.id.list_item_log_msg_textview);
            lineNumberTextView = (TextView) itemView.findViewById(R.id.list_item_log_line_number_text);

            blue = ContextCompat.getColor(itemView.getContext(), R.color.bb_log_blue);
            purple = ContextCompat.getColor(itemView.getContext(), R.color.bb_log_purple);
            red = ContextCompat.getColor(itemView.getContext(), R.color.bb_log_red);
            white = ContextCompat.getColor(itemView.getContext(), R.color.white);
            yellow = ContextCompat.getColor(itemView.getContext(), R.color.bb_log_yellow);

        }

        private void bind(LogItem logItem, int lineNumber) {
            msgTextView.setText(logItem.getMessage());
            lineNumberTextView.setText(String.valueOf(lineNumber));

            switch (logItem.getLevel()) {
                case CI:
                    msgTextView.setTextColor(white);
                    break;
                case CT:
                    msgTextView.setTextColor(blue);
                    break;
                case CC:
                    msgTextView.setTextColor(purple);
                    break;
                case CE:
                    msgTextView.setTextColor(red);
                    break;
                case CW:
                    msgTextView.setTextColor(yellow);
                    break;
                default:
                    throw new RuntimeException("unknown level");
            }
        }
    }
}
