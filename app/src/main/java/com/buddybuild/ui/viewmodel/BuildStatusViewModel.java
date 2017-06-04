package com.buddybuild.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.buddybuild.R;
import com.buddybuild.core.Build;
import com.buddybuild.utils.DateUtils;

import java.util.Locale;

public class BuildStatusViewModel extends BaseObservable {

    private Build build;

    public void setBuild(Build build) {
        this.build = build;
        notifyChange();
    }

    public String getStatus(Context context) {
        if (build == null) {
            return context.getString(R.string.empty_value_placeholder);
        }
        int resId;
        switch (build.getBuildStatus()) {
            case CANCELLED:
                resId = R.string.cancelled;
                break;
            case FAILED:
                resId = R.string.failed;
                break;
            case QUEUED:
                resId = R.string.queued;
                break;
            case RUNNING:
                resId = R.string.running;
                break;
            case SUCCESS:
                resId = R.string.success;
                break;
            default:
                throw new IllegalStateException("unknown status");
        }
        String string = context.getString(resId);
        return string.substring(0, 1).toUpperCase(Locale.getDefault())
                + string.substring(1); // capitalize first letter
    }

    public Drawable getStatusIcon(Context context) {
        if (build == null) {
            return null;
        }
        int resId;
        switch (build.getBuildStatus()) {
            case CANCELLED:
                resId = R.drawable.ic_canceled;
                break;
            case FAILED:
                resId = R.drawable.ic_failed;
                break;
            case QUEUED:
                resId = R.drawable.ic_queued;
                break;
            case RUNNING:
                resId = R.drawable.ic_running;
                break;
            case SUCCESS:
                resId = R.drawable.ic_success;
                break;
            default:
                throw new IllegalStateException("unknown status");
        }
        return ContextCompat.getDrawable(context, resId);
    }

    public String getStarted(Context context) {
        if (build == null
                || build.getStartTime() == null) {
            return context.getString(R.string.empty_value_placeholder);
        }
        return DateUtils.ago(build.getStartTime(), context);
    }

    public String getQueueDuration(Context context) {
        if (build == null
                || build.getQueuedDuration() == null
                || build.getQueuedDuration().toMinutes() < 1) {
            return context.getString(R.string.empty_value_placeholder);
        }
        return DateUtils.duration(build.getQueuedDuration(), context);
    }

    public String getBuildDuration(Context context) {
        if (build == null || build.getBuildDuration() == null) {
            return context.getString(R.string.empty_value_placeholder);
        }
        return DateUtils.duration(build.getBuildDuration(), context);
    }
}
