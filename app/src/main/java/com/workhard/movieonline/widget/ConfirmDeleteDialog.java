package com.workhard.movieonline.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.telecom.Call;
import android.view.View;
import android.widget.TextView;

import com.workhard.movieonline.R;

import org.w3c.dom.Text;

/**
 * Created by HoanNguyen on 19/03/2017.
 */

public class ConfirmDeleteDialog extends Dialog {
    private Callback callback;

    public interface Callback {
        public void onOkButtonClick();
    }

    public ConfirmDeleteDialog(Context context, Callback callback, String title, String msg) {
        super(context);
        this.callback = callback;
        initView(title, msg);
    }

    private void initView(String title, String msg) {
        setContentView(R.layout.dialog_confirm_delete);
        // Set transparent backgroud to avoid child over parent corners.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle = (TextView) findViewById(R.id.tv_confirm_title);
        tvTitle.setText(title);

        TextView tvContent = (TextView) findViewById(R.id.tv_confirm_content);
        tvContent.setText(msg);

        TextView btnNo = (TextView) findViewById(R.id.btn_no);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TextView btnYes = (TextView) findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onOkButtonClick();
                }
                dismiss();
            }
        });

        setCancelable(false);
    }
}
