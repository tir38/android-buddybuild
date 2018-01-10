package com.buddybuild.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


/**
 * Dialog fragment which displays a single message and an "OK" button
 */
public final class MessageDialogFragment extends DialogFragment {

    private static final String ARGS_MESSAGE_ID = "ARGS_MESSAGE_ID";

    public static MessageDialogFragment newInstance(@StringRes int messageId) {

        Bundle args = new Bundle();
        args.putInt(ARGS_MESSAGE_ID, messageId);
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int messageResId = getArguments().getInt(ARGS_MESSAGE_ID);
        return new AlertDialog.Builder(getContext())
                .setMessage(messageResId)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dismiss())
                .create();
    }
}
