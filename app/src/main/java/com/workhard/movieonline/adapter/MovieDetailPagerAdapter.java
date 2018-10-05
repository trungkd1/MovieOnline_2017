package com.workhard.movieonline.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.enums.MovieDetailPagerEnum;
import com.workhard.movieonline.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungKD on 2/21/2017.
 */
public class MovieDetailPagerAdapter extends PagerAdapter {
    private Context mContext;
    private Logger logger = LoggerManager.getLogger(MovieDetailPagerAdapter.class);
    private List<View> viewList;
    private Callback   callback;

    public interface Callback {
        void onFirstViewAdded();
    }

    public MovieDetailPagerAdapter(Context context, Callback callback) {
        mContext = context;
        viewList = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        boolean isFullView = validateListSize();
        if (isFullView) {
            return viewList.get(position);
        }

        MovieDetailPagerEnum movieDetailPagerEnum = MovieDetailPagerEnum.values()[position];
        LayoutInflater       inflater             = LayoutInflater.from(mContext);
        ViewGroup            layout               = (ViewGroup) inflater.inflate(movieDetailPagerEnum.getLayoutResId(), collection, false);

        collection.addView(layout);
        viewList.add(layout);

        if (position == 0 && callback != null) {
            callback.onFirstViewAdded();
        }

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        if (validateListSize()) {
            return;
        }

        collection.removeView((View) view);
        viewList.remove((View) view);
    }

    @Override
    public int getCount() {
        return MovieDetailPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MovieDetailPagerEnum homePagerEnum = MovieDetailPagerEnum.values()[position];
        return mContext.getString(homePagerEnum.getTitleResId());
    }

    /*private int mCurrentPosition = 0;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
            View            tabView = (View) object;
            CustomViewPager pager   = (CustomViewPager) container;
            if (tabView != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(tabView);
            }
        }
    }*/

    /**
     * Get added layout list to bind data into a similar layout.
     *
     * @return
     */
    public List<View> getViewList() {
        return viewList;
    }

    private boolean validateListSize() {
        return viewList != null && viewList.size() >= MovieDetailPagerEnum.values().length;
    }
}
