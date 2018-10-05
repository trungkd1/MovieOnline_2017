package com.workhard.movieonline.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

/**
 * Created by TrungKD on 2/18/2017.
 */
public class VolumeVerticalSeekBar extends SeekBar {
    private Logger logger = LoggerManager.getLogger();
    private AudioManager audioManager;

    public VolumeVerticalSeekBar(Context context) {
        super(context);
        initSeekBar(context);
    }

    public VolumeVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSeekBar(context);
    }

    public VolumeVerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSeekBar(context);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    private OnSeekBarChangeListener onChangeListener;

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    private int lastProgress = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onChangeListener.onStartTrackingTouch(this);
            case MotionEvent.ACTION_MOVE:
                super.onTouchEvent(event);
                int progress = getMax() - (int) (getMax() * event.getY() / getHeight());

                // Ensure progress stays within boundaries
                if (progress < 0) {
                    progress = 0;
                }
                if (progress > getMax()) {
                    progress = getMax();
                }
                setProgress(progress);  // Draw progress
                if (progress != lastProgress) {
                    // Only enact listener if the progress has actually changed
                    lastProgress = progress;
                    onChangeListener.onProgressChanged(this, progress, true);
                }

                onSizeChanged(getWidth(), getHeight(), 0, 0);
                setPressed(true);
                setSelected(true);
                break;
            case MotionEvent.ACTION_UP:
                onChangeListener.onStopTrackingTouch(this);
                setPressed(false);
                setSelected(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(event);
                setPressed(false);
                setSelected(false);
                break;
        }
        return true;
    }

    private void initSeekBar(Context context) {
        if (context == null) {
            throw new NullPointerException("Context is null!");
        }
        try {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int maxVolumeLevel = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int curVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            setMax(maxVolumeLevel);
            setProgress(curVolumeLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(super.getWidth(), super.getHeight(), 0, 0);
    }

    public void updateProgress() {
        int curVolumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setProgress(curVolumeLevel);
    }
}
