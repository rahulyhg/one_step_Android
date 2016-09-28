package com.os.onestep.utilities.dialogutils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.os.onestep.R;

public class DialogUtils {

    public static AlertDialog alertDialog;

    public static void showalert(String Msg, final Context context) {
        if (alertDialog == null || !alertDialog.isShowing()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(context.getResources().getString(R.string.app_name));
            alertDialogBuilder.setMessage(Msg).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
