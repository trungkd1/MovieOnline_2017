package com.workhard.movieonline.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungKD on 4/27/2017.
 */
public class Refresh {
    @SerializedName("popular")
    private String popular;

    @SerializedName("trending")
    private String trending;

    @SerializedName("lastupdate")
    private String lastUpdate;

    @SerializedName("toppick")
    private String topPick;

    @SerializedName("latestMovies")
    private String latestMovies;

    @SerializedName("latestTvShows")
    private String latestTvShows;

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }

    public String getTrending() {
        return trending;
    }

    public void setTrending(String trending) {
        this.trending = trending;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getTopPick() {
        return topPick;
    }

    public void setTopPick(String topPick) {
        this.topPick = topPick;
    }

    public String getLatestMovies() {
        return latestMovies;
    }

    public void setLatestMovies(String latestMovies) {
        this.latestMovies = latestMovies;
    }

    public String getLatestTvShows() {
        return latestTvShows;
    }

    public void setLatestTvShows(String latestTvShows) {
        this.latestTvShows = latestTvShows;
    }

    public Refresh(String popular, String trending, String lastUpdate, String topPick, String latestMovies, String latestTvShows) {
        this.popular = popular;
        this.trending = trending;
        this.lastUpdate = lastUpdate;
        this.topPick = topPick;
        this.latestMovies = latestMovies;
        this.latestTvShows = latestTvShows;
    }
}
