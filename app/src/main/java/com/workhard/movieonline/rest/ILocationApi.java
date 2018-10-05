package com.workhard.movieonline.rest;

import com.workhard.movieonline.rest.model.ResponseUserLocation;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by TrungKD on 4/24/2017
 */
public interface ILocationApi {
    @GET(".")
    Call<ResponseUserLocation> getLocation();
}
