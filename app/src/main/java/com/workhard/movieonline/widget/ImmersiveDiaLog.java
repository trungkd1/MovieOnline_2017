package com.workhard.movieonline.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by TrungKD on 2/19/2017.
 */
public abstract class ImmersiveDiaLog extends Dialog {
    protected Activity activity;

    public ImmersiveDiaLog(Context context) {
        super(context);
    }

    /**
     * An hack used to show the dialogs in Immersive Mode (that is with the NavBar hidden). To
     * obtain this, the method makes the dialog not focusable before showing it, change the UI
     * visibility of the window like the owner activity of the dialog and then (after showing it)
     * makes the dialog focusable again.
     * <p>
     * Refer: http://stackoverflow.com/questions/22794049/how-to-maintain-the-immersive-mode-in-dialogs
     */
    @Override
    public void show() {
        // Set the dialog to not focusable.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        copySystemUiVisibility();
        // Show the dialog with NavBar hidden.
        super.show();
        // Set the dialog to focusable again.
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * Copy the visibility of the Activity that has started the dialog {@link activity}. If the
     * activity is in Immersive mode the dialog will be in Immersive mode too and vice versa.
     * <p>
     * Refer: http://stackoverflow.com/questions/22794049/how-to-maintain-the-immersive-mode-in-dialogs
     */
    @SuppressLint("NewApi")
    private void copySystemUiVisibility() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    activity.getWindow().getDecorView().getSystemUiVisibility());
        }
    }
}
