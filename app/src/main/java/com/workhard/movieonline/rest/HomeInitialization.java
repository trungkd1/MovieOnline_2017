package com.workhard.movieonline.rest;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.workhard.movieonline.adapter.MovieAdapter;
import com.workhard.movieonline.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoanNguyen on 30/04/2017
 */

public abstract class HomeInitialization {
    public interface Callback {
        void onGettingStarted();
        void onGettingSuccess(List<Movie> movieList, MovieAdapter adapter);
        void onGettingFail(Throwable t, MovieAdapter adapter);
    }

    protected Callback callback;
    protected MovieAdapter adapter;
    protected RecyclerView recyclerView;
    protected IBaseApi baseApi;
    protected Activity context;

    HomeInitialization(Callback callback, RecyclerView recyclerView,
                       IBaseApi baseApi, Activity context,
                       MovieAdapter.OnMovieClickListener listener) {
        if (callback == null
                || recyclerView == null
                || baseApi == null
                || context == null) {
            throw new NullPointerException();
        }

        this.callback = callback;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null
                || !(adapter instanceof MovieAdapter)
                || adapter.getItemCount() <= 0) {
            this.adapter = new MovieAdapter(context, new ArrayList<Movie>(), listener);
            recyclerView.setAdapter(this.adapter);
        } else {
            this.adapter = (MovieAdapter) adapter;
        }

        this.recyclerView = recyclerView;
        this.baseApi = baseApi;
        this.context = context;
    }

    public abstract void getData(int pageNum);
}