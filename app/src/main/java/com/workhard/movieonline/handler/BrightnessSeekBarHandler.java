package com.workhard.movieonline.handler;

import android.os.Handler;
import android.os.Message;

import com.workhard.movieonline.widget.BrightnessVerticalSeekBar;
import com.workhard.movieonline.widget.VolumeVerticalSeekBar;

import java.lang.ref.WeakReference;

/**
 * Created by TrungKD on 2/18/2017.
 */
public class BrightnessSeekBarHandler extends Handler {
    private final WeakReference<BrightnessVerticalSeekBar> mView;
    public static final int SHOW_BRIGHTNESS_SEEK_BAR = 1;

    public BrightnessSeekBarHandler(BrightnessVerticalSeekBar view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void handleMessage(Message msg) {
        BrightnessVerticalSeekBar view = mView.get();
        if (view == null) {
            return;
        }

        int pos = 0;
        switch (msg.what) {
            case SHOW_BRIGHTNESS_SEEK_BAR:
                pos++;
                view.updateProgress();
                if (view.isShown()) {
                    msg = obtainMessage(SHOW_BRIGHTNESS_SEEK_BAR);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
                break;
        }
    }
}
