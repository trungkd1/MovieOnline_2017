package com.workhard.movieonline.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class Movie {
    @SerializedName("name")
    private String name;

    @SerializedName("alias")
    private String alias;

    @SerializedName("cover")
    private String cover;

    @SerializedName("description")
    private String description;

    @SerializedName("director")
    private String director;

    @SerializedName("star")
    private String star;

    @SerializedName("tvshow")
    private boolean isTvShow;

    @SerializedName("rating")
    private float rating;

    @SerializedName("tags")
    private String tags;

    @SerializedName("subtitle")
    private boolean subtitle;

    @SerializedName("duration")
    private String duration;

    @SerializedName("releaseDate")
    private String releaseDate;

    @SerializedName("trailer")
    private boolean trailer;

    @SerializedName("quality")
    private String quality;

    @SerializedName("serviceList")
    private List<Server> serviceList;

    @SerializedName("year")
    private int year;

    @SerializedName("episodesCount")
    private int episodesCount;

    @SerializedName("noplayUrl")
    private String noplayUrl;

    private boolean isBookmark;

    public Movie() {

    }

    public Movie(String alias, String cover, String description,
                 String director, String duration, int episodesCount,
                 String name, String noplayUrl, String quality,
                 float rating, String releaseDate, List<Server> serviceList,
                 String star, boolean subtitle, String tags,
                 boolean trailer, boolean isTvShow, int year) {
        this.alias = alias;
        this.cover = cover;
        this.description = description;
        this.director = director;
        this.duration = duration;
        this.episodesCount = episodesCount;
        this.name = name;
        this.noplayUrl = noplayUrl;
        this.quality = quality;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.serviceList = serviceList;
        this.star = star;
        this.subtitle = subtitle;
        this.tags = tags;
        this.trailer = trailer;
        this.isTvShow = isTvShow;
        this.year = year;
    }

    public String getAlias() {
        return alias;
    }

    public String getCover() {
        return cover;
    }

    public String getDescription() {
        return description;
    }

    public String getDirector() {
        return director;
    }

    public String getDuration() {
        return duration;
    }

    public int getEpisodesCount() {
        return episodesCount;
    }

    public String getName() {
        return name;
    }

    public String getNoplayUrl() {
        return noplayUrl;
    }

    public String getQuality() {
        return quality;
    }

    public float getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Server> getServiceList() {
        return serviceList;
    }

    public String getStar() {
        return star;
    }

    public boolean getSubtitle() {
        return subtitle;
    }

    public String getTags() {
        return tags;
    }

    public boolean isTrailer() {
        return trailer;
    }

    public boolean isTvShow() {
        return isTvShow;
    }

    public int getYear() {
        return year;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setEpisodesCount(int episodesCount) {
        this.episodesCount = episodesCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoplayUrl(String noplayUrl) {
        this.noplayUrl = noplayUrl;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setServiceList(List<Server> serviceList) {
        this.serviceList = serviceList;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setSubtitle(boolean subtitle) {
        this.subtitle = subtitle;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setTrailer(boolean trailer) {
        this.trailer = trailer;
    }

    public void setTvShow(boolean tvShow) {
        isTvShow = tvShow;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
}
