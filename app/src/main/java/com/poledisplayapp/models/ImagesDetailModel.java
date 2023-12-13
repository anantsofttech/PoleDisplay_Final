package com.poledisplayapp.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
//        "AltTag",
//        "BackgroundColor",
//        "BannerId",
//        "BannerNo",
//        "BannerStatus",
//        "Caption",

        "BannerType",
        "id",
        "Image",
        "ImageNo",
        "imagepath",
        "StoreNo",

//        "status",
//        "type",

})
public class ImagesDetailModel {

//    @JsonProperty("AltTag")
//    private Object altTag;
//    @JsonProperty("BackgroundColor")
//    private String backgroundColor;
//    @JsonProperty("BannerId")
//    private Integer bannerId;
//    @JsonProperty("BannerNo")
//    private Integer bannerNo;
//    @JsonProperty("BannerStatus")
//    private Boolean bannerStatus;
//    @JsonProperty("Caption")
//    private Object caption;

    @JsonProperty("BannerType")
    private String bannerType;
    @JsonProperty("id")
    private String id;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("ImageNo")
    private String imageNo;
    @JsonProperty("imagepath")
    private Object imagepath;
    @JsonProperty("StoreNo")
    private String storeNo;

//    @JsonProperty("status")
//    private Object status;
//    @JsonProperty("type")
//    private Object type;
//    @JsonIgnore
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

//    @JsonProperty("AltTag")
//    public Object getAltTag() {
//        return altTag;
//    }
//
//    @JsonProperty("AltTag")
//    public void setAltTag(Object altTag) {
//        this.altTag = altTag;
//    }
//
//    @JsonProperty("BackgroundColor")
//    public String getBackgroundColor() {
//        return backgroundColor;
//    }
//
//    @JsonProperty("BackgroundColor")
//    public void setBackgroundColor(String backgroundColor) {
//        this.backgroundColor = backgroundColor;
//    }
//
//    @JsonProperty("BannerId")
//    public Integer getBannerId() {
//        return bannerId;
//    }
//
//    @JsonProperty("BannerId")
//    public void setBannerId(Integer bannerId) {
//        this.bannerId = bannerId;
//    }
//
//    @JsonProperty("BannerNo")
//    public Integer getBannerNo() {
//        return bannerNo;
//    }
//
//    @JsonProperty("BannerNo")
//    public void setBannerNo(Integer bannerNo) {
//        this.bannerNo = bannerNo;
//    }
//
//    @JsonProperty("BannerStatus")
//    public Boolean getBannerStatus() {
//        return bannerStatus;
//    }
//
//    @JsonProperty("BannerStatus")
//    public void setBannerStatus(Boolean bannerStatus) {
//        this.bannerStatus = bannerStatus;
//    }

    //    @JsonProperty("Caption")
//    public Object getCaption() {
//        return caption;
//    }
//
//    @JsonProperty("Caption")
//    public void setCaption(Object caption) {
//        this.caption = caption;
//    }

    @JsonProperty("BannerType")
    public String getBannerType() {
        return bannerType;
    }

    @JsonProperty("BannerType")
    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Image")
    public String getImage() {
        return image;
    }

    @JsonProperty("Image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("ImageNo")
    public String getImageNo() {
        return imageNo;
    }

    @JsonProperty("ImageNo")
    public void setImageNo(String imageNo) {
        this.imageNo = imageNo;
    }

    @JsonProperty("imagepath")
    public Object getImagepath() {
        return imagepath;
    }

    @JsonProperty("imagepath")
    public void setImagepath(Object imagepath) {
        this.imagepath = imagepath;
    }

    @JsonProperty("StoreNo")
    public String getStoreNo() {
        return storeNo;
    }

    @JsonProperty("StoreNo")
    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }


//    @JsonProperty("status")
//    public Object getStatus() {
//        return status;
//    }
//
//    @JsonProperty("status")
//    public void setStatus(Object status) {
//        this.status = status;
//    }
//
//    @JsonProperty("type")
//    public Object getType() {
//        return type;
//    }
//
//    @JsonProperty("type")
//    public void setType(Object type) {
//        this.type = type;
//    }

//    @JsonAnyGetter
//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }

}
