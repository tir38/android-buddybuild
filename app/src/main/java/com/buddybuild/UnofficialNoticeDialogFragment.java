package com.buddybuild;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

/**
 * Dialog Fragment that shows info about unofficial status.
 */
public class UnofficialNoticeDialogFragment extends DialogFragment {

    public static UnofficialNoticeDialogFragment newInstance() {
        Bundle args = new Bundle();
        UnofficialNoticeDialogFragment fragment = new UnofficialNoticeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.unofficial_notice_title)
                .setMessage(R.string.unofficial_notice_message)
                .setPositiveButton(R.string.stay, (dialog, which) -> {
                    dismiss();
                })
                .setNegativeButton(R.string.leave, (dialog, which) -> {
                    // API 21 way
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().finishAndRemoveTask();
                        return;
                    }

                    // API 16 way
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getActivity().finishAffinity();
                        return;
                    }

                    // API <= 15 way
                    getActivity().finish();
                })
                .setNeutralButton(R.string.more_info, (dialog, which) -> {
                    // do nothing; we don't want this button to dismiss dialog, so we override behaviour in onResume()
                })
                .create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button neutralButton = alertDialog.getButton(Dialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(view -> {
                String url = "https://github.com/tir38/android-buddybuild";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            });
        }
    }
}
