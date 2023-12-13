package com.poledisplayapp.models;

import com.google.gson.annotations.SerializedName;

public class Result {

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @SerializedName("result")
    private String result;

    public String getWebstore() {
        return webstore;
    }

    public void setWebstore(String webstore) {
        this.webstore = webstore;
    }

    @SerializedName("WebServer")
    private String webstore;


}
