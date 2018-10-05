package com.workhard.movieonline.model;

/**
 * Created by TrungKD on 3/21/2017.
 */
public class MovieInfo {
    private String header;
    private String content;

    public MovieInfo(String header, String content) {
        this.header = header;
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }
}
