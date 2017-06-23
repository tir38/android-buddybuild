package com.buddybuild.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class SettingsActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }
}
