package com.workhard.movieonline.rest;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.workhard.movieonline.adapter.MovieAdapter;
import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.rest.model.ResponseMovieList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by HoanNguyen on 30/04/2017.
 */

public class HomeTrendingInitialization extends HomeInitialization {

    public HomeTrendingInitialization(Callback callback, RecyclerView recyclerView,
                                      IBaseApi baseApi, Activity context,
                                      MovieAdapter.OnMovieClickListener listener) {
        super(callback, recyclerView, baseApi, context, listener);
    }

    @Override
    public void getData(int pageNum) {
        callback.onGettingStarted();
        ApiInitialization apiInitialization = new ApiInitialization(context);
        Call<ResponseMovieList> call = baseApi.getTrending(
                apiInitialization.getDeviceToken(),
                apiInitialization.getDeviceId(),
                apiInitialization.getOsType(),
                apiInitialization.getLanguage(),
                apiInitialization.getHardware(),

                String.valueOf(pageNum));

        call.enqueue(new retrofit2.Callback<ResponseMovieList>() {
            @Override
            public void onResponse(Call<ResponseMovieList> call, Response<ResponseMovieList> response) {
                List<Movie> trendingList = response.body().getData();
                callback.onGettingSuccess(trendingList, adapter);
            }

            @Override
            public void onFailure(Call<ResponseMovieList> call, Throwable t) {
                callback.onGettingFail(t, adapter);
            }
        });
    }
}