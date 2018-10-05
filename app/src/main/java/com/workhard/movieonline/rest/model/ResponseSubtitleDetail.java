package com.workhard.movieonline.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungKD on 5/3/2017
 */
public class ResponseSubtitleDetail extends BaseResponse {
    @SerializedName("srt")
    String srt;

    @SerializedName("vtt")
    String vtt;

    @SerializedName("encoding")
    String encoding;

    public String getSrt() {
        return srt;
    }

    public void setSrt(String srt) {
        this.srt = srt;
    }

    public String getVtt() {
        return vtt;
    }

    public void setVtt(String vtt) {
        this.vtt = vtt;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}