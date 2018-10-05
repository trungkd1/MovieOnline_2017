package com.workhard.movieonline.enums;

import com.workhard.movieonline.R;

/**
 * Created by TrungKD on 2/27/2017.
 */
public enum MovieDetailPagerEnum {
    EPISODE(R.string.title_episode, R.layout.fragment_movie_detail_tab_episode),
    INFORMATION(R.string.title_information, R.layout.fragment_movie_detail_tab_info),
    MORE_LIKE(R.string.title_more_like, R.layout.fragment_movie_detail_tab_more_like);

    private int mTitleResId;
    private int mLayoutResId;

    MovieDetailPagerEnum(int titleResId, int layoutResId) {
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