package com.workhard.movieonline.listener;

import android.content.Context;
import android.widget.SeekBar;

import com.workhard.movieonline.handler.BrightnessSeekBarHandler;
import com.workhard.movieonline.handler.OnKeepMovieControllerShowing;
import com.workhard.movieonline.widget.PlaybackControlView;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class BrightnessSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    private Context context;
    private BrightnessSeekBarHandler handler;
    private OnKeepMovieControllerShowing callback;

    public BrightnessSeekBarChangeListener(Context context, BrightnessSeekBarHandler handler, OnKeepMovieControllerShowing callback) {
        if (context == null) {
            throw new NullPointerException("Context is null");
        }

        if (handler == null) {
            throw new NullPointerException("Handle is null");
        }

        this.context = context;
        this.handler = handler;
        this.callback = callback;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        android.provider.Settings.System.putInt(
                context.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        callback.onKeepShowing(3600000);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        callback.onKeepShowing(PlaybackControlView.DEFAULT_SHOW_TIMEOUT_MS);
    }
}
