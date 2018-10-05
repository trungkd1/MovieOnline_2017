package com.workhard.movieonline.rest.model;

import com.google.gson.annotations.SerializedName;
import com.workhard.movieonline.model.Episode;
import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.model.Server;

import java.util.List;

/**
 * Created by HoanNguyen on 29/04/2017.
 */

public class MovieDetailData {
    @SerializedName("serial")
    private Movie info;

    @SerializedName("servers")
    private List<Server> serverList;

    @SerializedName("episodes")
    private List<Episode> episodeList;

    public Movie getInfo() {
        return info;
    }

    public void setInfo(Movie info) {
        this.info = info;
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }
}