package com.workhard.movieonline.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class Episode implements DialogItem {
    @SerializedName("alias")
    private String alias;
    @SerializedName("episode")
    private int episode;
    private String serialAlias;
    private int showWebView;
    private String streaming;
    @SerializedName("title")
    private String title;
    private int viewed;

    public Episode(String alias, int episode, String serialAlias, int showWebView, String streaming, String title, int viewed) {
        this.alias = alias;
        this.episode = episode;
        this.serialAlias = serialAlias;
        this.showWebView = showWebView;
        this.streaming = streaming;
        this.title = title;
        this.viewed = viewed;
    }

    public String getAlias() {
        return alias;
    }

    public int getEpisode() {
        return episode;
    }

    public String getSerialAlias() {
        return serialAlias;
    }

    public int getShowWebView() {
        return showWebView;
    }

    public String getStreaming() {
        return streaming;
    }

    public String getTitle() {
        return title;
    }

    public int getViewed() {
        return viewed;
    }
}
