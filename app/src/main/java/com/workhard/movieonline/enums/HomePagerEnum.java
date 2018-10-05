package com.workhard.movieonline.enums;

import com.workhard.movieonline.R;

/**
 * Created by TrungKD on 2/27/2017.
 */
public enum HomePagerEnum {
    POPULAR(R.string.title_popular, R.layout.fragment_home_movie_list),
    TRENDING(R.string.title_trending, R.layout.fragment_home_movie_list),
    LAST_UPDATE(R.string.title_last_update, R.layout.fragment_home_movie_list);

    private int mTitleResId;
    private int mLayoutResId;

    HomePagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
