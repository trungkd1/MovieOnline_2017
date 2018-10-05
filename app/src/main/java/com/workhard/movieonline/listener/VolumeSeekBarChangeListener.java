package com.workhard.movieonline.listener;

import android.content.Context;
import android.media.AudioManager;
import android.widget.SeekBar;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.handler.OnKeepMovieControllerShowing;
import com.workhard.movieonline.handler.VolumeSeekBarHandler;
import com.workhard.movieonline.widget.PlaybackControlView;

/**
 * Created by TrungKD on 2/18/2017.
 */
public class VolumeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    private Logger logger = LoggerManager.getLogger();

    private AudioManager         audioManager;
    private VolumeSeekBarHandler handler;

    OnKeepMovieControllerShowing callback;

    public VolumeSeekBarChangeListener(Context context, VolumeSeekBarHandler handler, OnKeepMovieControllerShowing callback) {
        if (context == null) {
            throw new NullPointerException("Context is null!");
        }

        if (handler == null) {
            throw new NullPointerException("Handle is null!");
        }
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.handler = handler;
        this.callback = callback;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Set system volume value.
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO: push your implement source code
        // By removing these pending progress messages we make sure
        // that a) we won't update the progress while the user adjusts
        // the seekbar and b) once the user is done dragging the thumb
        // we will post one of these messages to the queue again and
        // this ensures that there will be exactly one message queued up.
        handler.removeMessages(VolumeSeekBarHandler.SHOW_VOLUME_SEEK_BAR);
        callback.onKeepShowing(3600000);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO: push your implement source code
        // Ensure that progress is properly updated in the future,
        // the call to show() does not guarantee this because it is a
        // no-op if we are already showing.
        handler.sendEmptyMessage(VolumeSeekBarHandler.SHOW_VOLUME_SEEK_BAR);
        callback.onKeepShowing(PlaybackControlView.DEFAULT_SHOW_TIMEOUT_MS);
    }
}
