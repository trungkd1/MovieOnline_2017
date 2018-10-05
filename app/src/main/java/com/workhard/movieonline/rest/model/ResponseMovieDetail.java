package com.workhard.movieonline.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HoanNguyen on 29/04/2017.
 */

public class ResponseMovieDetail extends BaseResponse {
    @SerializedName("data")
    private MovieDetailData data;

    public MovieDetailData getData() {
        return data;
    }

    public void setData(MovieDetailData data) {
        this.data = data;
    }
}
