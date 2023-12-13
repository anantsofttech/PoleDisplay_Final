package com.poledisplayapp.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "InnerModule", strict = false)
public class AdvRefreshCalls {

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRequested() {
        return requested;
    }

    public void setRequested(String requested) {
        this.requested = requested;
    }

    public String getSignid() {
        return Signid;
    }

    public void setSignid(String signid) {
        Signid = signid;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    @Element(name = "ResponseXml")
    private String response;

    @Element(name = "RequestXml")
    private String requested;

    @Element(name = "SignID")
    private String Signid;

    @Element(name = "ErrorMessage")
    private String ErrorMessage;

    @Element(name = "DeviceID")
    private String DeviceID;

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    @Element(name = "CreateDate")
    private String CreateDate;


}
