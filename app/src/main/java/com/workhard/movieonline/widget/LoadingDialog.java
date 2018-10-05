package com.workhard.movieonline.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

import com.workhard.movieonline.R;

/**
 * Created by HoanNguyen on 01/05/2017.
 */

public class LoadingDialog {
    private static LoadingDialog instance;
    private ProgressDialog progressDialog;

    private LoadingDialog() {

    }

    public synchronized static LoadingDialog getInstance() {
        if (instance == null) {
            instance = new LoadingDialog();
        }
        return instance;
    }

    public void show(Context context) {
        progressDialog = new ProgressDialog(context, R.style.LoadingDialogTheme);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
        progressDialog.show();
    }

    public void show(Activity context, boolean isFullScreen) {
        if (isFullScreen) {
            progressDialog = new ProgressDialog(context, R.style.LoadingDialogTheme);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            // Set the dialog to not focusable.
            progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            progressDialog.show();
            copySystemUiVisibility(context);
        } else {
            show(context);
        }
    }

    public void hide() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * Copy the visibility of the Activity that has started the dialog {@link Context}. If the
     * activity is in Immersive mode the dialog will be in Immersive mode too and vice versa.
     * <p>
     * Refer: http://stackoverflow.com/questions/22794049/how-to-maintain-the-immersive-mode-in-dialogs
     */
    @SuppressLint("NewApi")
    private void copySystemUiVisibility(Activity context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            progressDialog.getWindow().getDecorView().setSystemUiVisibility(
                    context.getWindow().getDecorView().getSystemUiVisibility());
        }
    }
}