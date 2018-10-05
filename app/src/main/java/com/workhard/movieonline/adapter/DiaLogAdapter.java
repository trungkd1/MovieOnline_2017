package com.workhard.movieonline.adapter;

import android.widget.BaseAdapter;

import com.workhard.movieonline.model.DialogItem;

/**
 * Created by TrungKD on 2/19/2017.
 */
public abstract class DiaLogAdapter <T extends DialogItem> extends BaseAdapter {
    public abstract T getItem(int position);
}
