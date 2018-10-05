package com.workhard.movieonline.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.R;
import com.workhard.movieonline.enums.HomePagerEnum;
import com.workhard.movieonline.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungKD on 2/21/2017.
 */
public class HomePagerAdapter extends PagerAdapter {
    private Activity activity;
    private Logger logger = LoggerManager.getLogger(HomePagerAdapter.class);
    private List<View> viewList;
    private Callback   callback;

    public interface Callback {
        void onFirstViewAdded();
    }

    public HomePagerAdapter(Activity activity, Callback callback) {
        this.activity = activity;
        viewList = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        if (validateListSize()) {
            return viewList.get(position);
        }

        HomePagerEnum  homePagerEnum = HomePagerEnum.values()[position];
        LayoutInflater inflater      = LayoutInflater.from(activity);
        ViewGroup      layout        = (ViewGroup) inflater.inflate(homePagerEnum.getLayoutResId(), collection, false);
        initMovieAdapter(layout);

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
        return HomePagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        HomePagerEnum homePagerEnum = HomePagerEnum.values()[position];
        return activity.getString(homePagerEnum.getTitleResId());
    }

    /**
     * Get added layout list to bind data into a similar layout.
     *
     * @return
     */
    public List<View> getViewList() {
        return viewList;
    }

    private boolean validateListSize() {
        return viewList != null && viewList.size() >= HomePagerEnum.values().length;
    }

    /**
     * Add empty adapter to fix an error: No adapter attached; skipping layout
     *
     * @param viewGroup
     */
    private void initMovieAdapter(ViewGroup viewGroup) {
        RecyclerView recyclerView = (RecyclerView) viewGroup.findViewById(R.id.rv_home_movie_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);

        MovieAdapter adapter = new MovieAdapter(activity, new ArrayList<Movie>(), null);
        recyclerView.setAdapter(adapter);
    }
}
