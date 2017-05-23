package com.buddybuild.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.buddybuild.R;

/**
 * Custom {@link AppCompatButton} that implements BuddyBuild's button style
 */
public class BuddyBuildButton extends AppCompatButton {
    public BuddyBuildButton(Context context) {
        this(context, null);
    }

    public BuddyBuildButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuddyBuildButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.bb_button_blue_bg);

        int padding = (int) getResources().getDimension(R.dimen.bb_button_padding);
        setPadding(padding, padding, padding, padding);

        int textColor;
        if (isEnabled()) {
            textColor = ContextCompat.getColor(getContext(), R.color.bb_button_blue_enabled_text_color);
        } else {
            textColor = ContextCompat.getColor(getContext(), R.color.bb_button_blue_disabled_text_color);
        }
        setTextColor(textColor);
    }
}
