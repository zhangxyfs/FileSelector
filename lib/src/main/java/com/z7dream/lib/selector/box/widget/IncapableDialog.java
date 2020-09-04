package com.z7dream.lib.selector.box.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.z7dream.lib.selector.R;

public class IncapableDialog extends DialogFragment {
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_MESSAGE = "extra_message";

    public IncapableDialog() {
    }

    public static IncapableDialog newInstance(String title, String message) {
        IncapableDialog dialog = new IncapableDialog();
        Bundle args = new Bundle();
        args.putString("extra_title", title);
        args.putString("extra_message", message);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = this.getArguments().getString("extra_title");
        String message = this.getArguments().getString("extra_message");
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        builder.setPositiveButton(R.string.button_ok, (dialog, which) -> dialog.dismiss());
        return builder.create();
    }
}
