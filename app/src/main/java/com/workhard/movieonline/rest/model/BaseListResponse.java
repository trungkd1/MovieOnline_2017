package com.workhard.movieonline.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TrungKD on 4/27/2017
 */
public abstract class BaseListResponse<T> extends BaseResponse {
    @SerializedName("more")
    protected boolean more;

    @SerializedName("total")
    protected int total;

    @SerializedName("data")
    protected List<T> data;

    @SerializedName("showPopupPlayer")
    protected boolean showPopupPlayer;

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isShowPopupPlayer() {
        return showPopupPlayer;
    }

    public void setShowPopupPlayer(boolean showPopupPlayer) {
        this.showPopupPlayer = showPopupPlayer;
    }
}