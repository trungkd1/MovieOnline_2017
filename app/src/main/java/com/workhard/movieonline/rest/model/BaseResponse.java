package com.workhard.movieonline.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HoanNguyen on 29/04/2017.
 */

public class BaseResponse {
    @SerializedName("status")
    protected String status;

    @SerializedName("refresh")
    protected Refresh refresh;

    public boolean getStatus() {
        return "ok".equals(status) || "success".equals(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Refresh getRefresh() {
        return refresh;
    }

    public void setRefresh(Refresh refresh) {
        this.refresh = refresh;
    }
}
