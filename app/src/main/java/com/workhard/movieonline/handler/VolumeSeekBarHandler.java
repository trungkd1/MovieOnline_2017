package com.workhard.movieonline.handler;

import android.os.Handler;
import android.os.Message;

import com.workhard.movieonline.widget.VolumeVerticalSeekBar;

import java.lang.ref.WeakReference;

/**
 * Created by TrungKD on 2/18/2017.
 */
public class VolumeSeekBarHandler extends Handler {
    private final WeakReference<VolumeVerticalSeekBar> mView;
    public static final int SHOW_VOLUME_SEEK_BAR = 1;

    public VolumeSeekBarHandler(VolumeVerticalSeekBar view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void handleMessage(Message msg) {
        VolumeVerticalSeekBar view = mView.get();
        if (view == null) {
            return;
        }

        int pos = 0;
        switch (msg.what) {
            case SHOW_VOLUME_SEEK_BAR:
                pos++;
                view.updateProgress();
                if (view.isShown()) {
                    msg = obtainMessage(SHOW_VOLUME_SEEK_BAR);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
                break;
        }
    }
}
