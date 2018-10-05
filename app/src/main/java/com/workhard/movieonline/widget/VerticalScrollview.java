package com.workhard.movieonline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

/**
 * Created by TrungKD on 2/28/2017.
 */
public class VerticalScrollview extends ScrollView {
    Logger logger = LoggerManager.getLogger(VerticalScrollview.class);

    public VerticalScrollview(Context context) {
        super(context);
    }

    public VerticalScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                logger.i("onInterceptTouchEvent: DOWN super false");
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                return false; // redirect MotionEvents to ourself

            case MotionEvent.ACTION_CANCEL:
                logger.i("onInterceptTouchEvent: CANCEL super false");
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_UP:
                logger.i("onInterceptTouchEvent: UP super false");
                return false;

            default:
                logger.i("onInterceptTouchEvent: " + action);
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        logger.i("onTouchEvent. action: " + ev.getAction());
        return true;
    }
}
