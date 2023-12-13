package com.poledisplayapp.models;

public class StationModel {
    String Station_No;
    String DisplayType;
    String app_name;
    String PoleDisplay;
    String store_nickname;
    String IndustryType;
    String OpenTime;
    String CloseTime;


    public String getOpenTime() {
        return OpenTime;
    }

    public void setOpenTime(String openTime) {
        OpenTime = openTime;
    }

    public String getCloseTime() {
        return CloseTime;
    }

    public void setCloseTime(String closeTime) {
        CloseTime = closeTime;
    }

    public String getIndustryType() {
        return IndustryType;
    }

    public void setIndustryType(String industryType) {
        IndustryType = industryType;
    }

    public String getStation_No() {
        return Station_No;
    }

    public StationModel setStation_No(String station_No) {
        Station_No = station_No;
        return this;
    }

    public String getDisplayType() {
        return DisplayType;
    }

    public StationModel setDisplayType(String displayType) {
        DisplayType = displayType;
        return this;
    }

    public String getApp_name() {
        return app_name;
    }

    public StationModel setApp_name(String app_name) {
        this.app_name = app_name;
        return this;
    }

    public String getPoleDisplay() {
        return PoleDisplay;
    }

    public void setPoleDisplay(String poleDisplay) {
        PoleDisplay = poleDisplay;
    }

    public String getStore_nickname() {
        return store_nickname;
    }

    public void setStore_nickname(String store_nickname) {
        this.store_nickname = store_nickname;
    }
}
