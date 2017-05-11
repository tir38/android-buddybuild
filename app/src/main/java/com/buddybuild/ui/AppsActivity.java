package com.buddybuild.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class AppsActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, AppsActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return AppsFragment.newInstance();
    }
}
