package com.workhard.movieonline.handler;

/**
 * Created by TrungKD on 2/25/2017.
 * http://stackoverflow.com/questions/7992216/android-fragment-handle-back-button-press
 */
public interface OnBackClickListener {
    /**
     * If a fragment doesn't intercept the back pressed event,
     * The activity need to finish. For example, main menu fragment such as
     * Home, Genre, History, Favourite, Setting, Twitter, Facebook are fragments
     *
     * @return boolean of interception status
     */
    boolean onBackClick();
}
