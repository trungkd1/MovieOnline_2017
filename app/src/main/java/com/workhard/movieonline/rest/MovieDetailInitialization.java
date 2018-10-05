package com.workhard.movieonline.rest;

import android.app.Activity;

import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.rest.IBaseApi;
import com.workhard.movieonline.rest.model.ResponseMovieDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HoanNguyen on 29/04/2017
 */

public class MovieDetailInitialization {

    private Callback callback;
    private IBaseApi baseApi;
    private Movie movie;
    private Activity context;

    public interface Callback {
        void onStartGetMovieDetail();

        void onMovieDetailGettingSuccess(ResponseMovieDetail responseMovieDetail);

        void onMovieDetailGettingFail(Throwable throwable);
    }

    public MovieDetailInitialization(IBaseApi baseApi, Callback callback, Movie movie, Activity context) {
        this.baseApi = baseApi;
        this.callback = callback;
        this.movie = movie;
        this.context = context;
    }

    public void initMovieDetail() {
        if (callback == null || baseApi == null || movie == null || context == null) {
            return;
        }

        callback.onStartGetMovieDetail();
        ApiInitialization apiInitialization = new ApiInitialization(context);
        Call<ResponseMovieDetail> call = baseApi.getMovieDetail(
                apiInitialization.getDeviceToken(),
                apiInitialization.getDeviceId(),
                apiInitialization.getOsType(),
                apiInitialization.getLanguage(),
                apiInitialization.getHardware(),

                movie.getAlias());
        call.enqueue(new retrofit2.Callback<ResponseMovieDetail>() {
            @Override
            public void onResponse(Call<ResponseMovieDetail> call, Response<ResponseMovieDetail> response) {
                callback.onMovieDetailGettingSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ResponseMovieDetail> call, Throwable t) {
                callback.onMovieDetailGettingFail(t);
            }
        });
    }
}