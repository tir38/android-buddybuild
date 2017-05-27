package com.buddybuild.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class BuildDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_BUILD_ID = "EXTRA_BUILD_ID";

    public static Intent newIntent(Context context, String buildId) {
        Intent intent = new Intent(context, BuildDetailActivity.class);
        intent.putExtra(EXTRA_BUILD_ID, buildId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String buildId = getIntent().getExtras().getString(EXTRA_BUILD_ID);
        return BuildDetailPagerFragment.newInstance(buildId);
    }
}
