package com.workhard.movieonline.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.R;
import com.workhard.movieonline.adapter.HomePagerAdapter;
import com.workhard.movieonline.adapter.MovieAdapter;
import com.workhard.movieonline.base.BaseFragment;
import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.rest.HomeInitialization;
import com.workhard.movieonline.rest.HomeLastUpdateInitialization;
import com.workhard.movieonline.rest.HomePopularInitialization;
import com.workhard.movieonline.rest.HomeTrendingInitialization;
import com.workhard.movieonline.rest.model.ResponseMovieList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TrungKD on 2/20/2017
 */
public class HomeFragment extends BaseFragment implements MovieAdapter.OnMovieClickListener, HomeInitialization.Callback {
    private Logger logger = LoggerManager.getLogger(HomeFragment.class);
    private int curPosition = 0;
    private int prevPosition = 0;
    private HomePagerAdapter adapter;

    private MovieAdapter trendingAdapter;
    private MovieAdapter lastUpdateAdapter;

    public static final int HOME_FRAGMENT_CODE = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void setupActionBar() {
        activity.enableNavIcon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutID(), null);
        setupTabLayout(activity, rootView);
        showActionBar();
        setupActionBar();
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setupActionBar();
        }
    }

    private void setupTabLayout(Activity activity, View rootView) {
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        adapter = new HomePagerAdapter(activity, firstPageLoadedListener);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(listener);
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            curPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Finished scrolling.
            if (state == 0) {
                if (curPosition != prevPosition) {
                    // Load correspond data.
                    loadData(curPosition);
                    prevPosition = curPosition;
                }
            }
        }
    };

    HomePagerAdapter.Callback firstPageLoadedListener = new HomePagerAdapter.Callback() {
        @Override
        public void onFirstViewAdded() {
            loadData(curPosition);
        }
    };

    private void loadData(int position) {
        List<View> viewList = adapter.getViewList();
        View tabView = viewList.get(position);
        initTabView(tabView, position);
    }

    private void initTabView(View tabView, int position) {
        RecyclerView recyclerView = (RecyclerView) tabView.findViewById(R.id.rv_home_movie_list);

        if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() > 0) {
            return;
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        HomeInitialization initialization;

        switch (position) {
            case 0:
                initialization = new HomePopularInitialization(
                        this, recyclerView, baseApi, activity, HomeFragment.this);
                break;
            case 1:
                initialization = new HomeTrendingInitialization(
                        this, recyclerView, baseApi, activity, HomeFragment.this);
                break;
            case 2:
                initialization = new HomeLastUpdateInitialization(
                        this, recyclerView, baseApi, activity, HomeFragment.this);
                break;
            default:
                initialization = new HomePopularInitialization(
                        this, recyclerView, baseApi, activity, HomeFragment.this);
                break;
        }

        initialization.getData(1);
    }

    @Override
    public void onMovieClick(Movie movie) {
        openMovieDetailFragment(movie);
    }

    private void openMovieDetailFragment(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setTargetFragment(HomeFragment.this, HOME_FRAGMENT_CODE);
        fragment.setMovie(movie);
        activity.addFragment(fragment, fragment.getTag());
    }

    @Override
    public void onBookmarkClick(Movie movie) {

    }

    @Override
    public boolean onBackClick() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HOME_FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            reloadData(curPosition);
        }
    }

    private void reloadData(int curPosition) {
        List<View> viewList = adapter.getViewList();
        View tabView = viewList.get(curPosition);

        RecyclerView recyclerView = (RecyclerView) tabView.findViewById(R.id.rv_home_movie_list);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Clear all data
        // Re-get data
    }

    @Override
    public void onGettingStarted() {
        // TODO: show loading
    }

    @Override
    public void onGettingSuccess(List<Movie> movieList, MovieAdapter adapter) {
        adapter.addMovieList(movieList);
        adapter.notifyDataSetChanged();
        // TODO: hide loading
    }

    @Override
    public void onGettingFail(Throwable t, MovieAdapter adapter) {
        logger.e(t.toString());
        // TODO: hide loading
    }
}