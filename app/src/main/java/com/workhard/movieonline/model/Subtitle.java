package com.workhard.movieonline.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class Subtitle implements DialogItem {
    @SerializedName("language")
    private String language;

    @SerializedName("title")
    private String title;

    @SerializedName("donwload")
    private String downloadURL;

    @SerializedName("rating")
    private int    rating;

    @SerializedName("subtitleId")
    private String subtitleId;

    public Subtitle(String subtitleId, String language, String title, int rating, String downloadURL) {
        this.subtitleId = subtitleId;
        this.language = language;
        this.title = title;
        this.rating = rating;
        this.downloadURL = downloadURL;
    }

    public String getSubtitleId() {
        return subtitleId;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public int getRating() {
        return rating;
    }

    public String getDownloadURL() {
        return downloadURL;
    }
}
