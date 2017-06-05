package com.buddybuild.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.buddybuild.R;

import io.reactivex.functions.Action;


/**
 * Delegate for showing/hiding progress indicator
 */
public final class ProgressIndicatorDelegate {

    private final int shortAnimationDuration;
    private final int progressIndicatorColor;
    private View progressIndicator;

    /**
     * @param context Will NOT maintain reference to Context
     */
    public ProgressIndicatorDelegate(Context context) {
        shortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        progressIndicatorColor = context.getResources().getColor(R.color.bb_progress_indicator_color);
    }

    /**
     * @param view View containing a {@link ProgressBar} with id R.id.progress_bar}.
     *             May pass in ProgressBar view itself or parent.
     */
    public void setProgressIndicator(View view) {
        if (view == null) {
            throw new RuntimeException("view cannot be null");
        }

        progressIndicator = view;
        // we only need to set color manually on pre-lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

            if (progressBar == null) {
                throw new RuntimeException("View hierarchy must contain ProgressBar");
            }

            progressBar.getIndeterminateDrawable().setColorFilter(progressIndicatorColor, PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * Fade in the progress indicator
     */
    public void fadeInProgress() {
        if (progressIndicator == null) {
            throw new RuntimeException("progressIndicator must be set before fading in/out");
        }

        progressIndicator.setAlpha(0f);
        progressIndicator.setVisibility(View.VISIBLE);
        progressIndicator.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressIndicator.setAlpha(1f); // we need to "keep" the view visible after animation completes
                    }
                });
    }

    /**
     * Fade out the progress indicator. Once complete run an optional Action0
     *
     * @param afterAnimationAction
     */
    public void fadeOutProgress(@Nullable Action afterAnimationAction) {
        if (progressIndicator == null) {
            throw new RuntimeException("progressIndicator must be set before fading in/out");
        }

        progressIndicator.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressIndicator.setAlpha(0f);
                        progressIndicator.setVisibility(View.GONE);
                        if (afterAnimationAction != null)
                            try {
                                afterAnimationAction.run();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                    }
                });
    }
}
