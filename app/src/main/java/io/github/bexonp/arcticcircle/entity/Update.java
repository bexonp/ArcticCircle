package io.github.bexonp.arcticcircle.entity;

/**
 * Created by Bexon Pak on 2017/09/01.
 */

public class Update {
    private String serverVersion;
    private String requestUrl;
    private String updateMsg;
    private String title;

    public Update(String serverVersion, String requestUrl, String updateMsg, String title) {
        this.serverVersion = serverVersion;
        this.requestUrl = requestUrl;
        this.updateMsg = updateMsg;
        this.title = title;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getUpdateMsg() {
        return updateMsg;
    }

    public void setUpdateMsg(String updateMsg) {
        this.updateMsg = updateMsg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
