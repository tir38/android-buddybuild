package com.buddybuild.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.buddybuild.R;

/**
 * Custom {@link AppCompatButton} that implements BuddyBuild's white button style
 */
public class BuddyBuildWhiteButton extends AppCompatButton {
    public BuddyBuildWhiteButton(Context context) {
        this(context, null);
    }

    public BuddyBuildWhiteButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuddyBuildWhiteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() { // TODO blue vs. white should not be two diff views, but one view w/ two states
        setBackgroundResource(R.drawable.bb_button_white_bg);

        int padding = (int) getResources().getDimension(R.dimen.bb_button_padding);
        setPadding(padding, padding, padding, padding);

        int textColor;
        if (isEnabled()) {
            textColor = ContextCompat.getColor(getContext(), R.color.white);
        } else {
            textColor = ContextCompat.getColor(getContext(), R.color.white60percentTransparent);
        }
        setTextColor(textColor);
    }
}
