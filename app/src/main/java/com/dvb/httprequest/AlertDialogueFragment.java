package com.dvb.httprequest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by dmitrybondarenko on 25.12.17.
 */

public class AlertDialogueFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_message))
                .setPositiveButton(context.getString(R.string.error_ok_button_text), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}