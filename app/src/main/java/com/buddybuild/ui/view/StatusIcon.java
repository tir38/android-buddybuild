package com.buddybuild.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.buddybuild.R;
import com.buddybuild.core.Build;

/**
 * Custom view for displaying {@link Build.Status} as an icon
 */
public class StatusIcon extends AppCompatImageView {
    public StatusIcon(Context context) {
        super(context);
    }

    public StatusIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStatus(Build.Status status) {
        Drawable icon = null;
        switch (status) {
            case CANCELLED:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_canceled);
                break;
            case FAILED:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_failed);
                break;
            case QUEUED:
                // TODO
                break;
            case RUNNING:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_running);
                break;
            case SUCCESS:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_success);
                break;

            default:
                throw new IllegalStateException("unknown status: " + status.toString());
        }
        if (icon != null) {
            setBackgroundDrawable(icon);
        }
    }
}
