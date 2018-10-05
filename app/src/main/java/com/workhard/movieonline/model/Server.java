package com.workhard.movieonline.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TrungKD on 2/19/2017
 */
public class Server implements DialogItem {
    @SerializedName("serverName")
    private String serverName;

    @SerializedName("master")
    private boolean master;

    @SerializedName("serverId")
    private String serverId;

    public Server(String serverName, boolean master, String serverId) {
        this.serverName = serverName;
        this.master = master;
        this.serverId = serverId;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
