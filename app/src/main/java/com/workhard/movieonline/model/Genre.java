package com.workhard.movieonline.model;

/**
 * Created by TrungKD on 2/19/2017.
 */
public class Genre {
    // Genry id
    private String alias;
    // Genry image url
    private String image;
    // Genry name
    private String name;

    public Genre(String alias, String image, String name) {
        this.alias = alias;
        this.image = image;
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
