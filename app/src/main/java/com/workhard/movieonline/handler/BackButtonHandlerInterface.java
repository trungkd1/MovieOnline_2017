package com.workhard.movieonline.handler;

/**
 * Created by TrungKD on 2/25/2017.
 * http://stackoverflow.com/questions/7992216/android-fragment-handle-back-button-press
 */
public interface BackButtonHandlerInterface {
    void addBackClickListener(OnBackClickListener onBackClickListener);
    void removeBackClickListener(OnBackClickListener onBackClickListener);
    void removeAllBackClickListener();
}
