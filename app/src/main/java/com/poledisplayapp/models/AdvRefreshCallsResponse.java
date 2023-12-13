package com.poledisplayapp.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "OuterModule")
public class AdvRefreshCallsResponse {
    public ArrayList<AdvRefreshCalls> getAdvRefreshCalls() {
        return advRefreshCalls;
    }

    public void setAdvRefreshCalls(ArrayList<AdvRefreshCalls> advRefreshCalls) {
        this.advRefreshCalls = advRefreshCalls;
    }

    @ElementList(inline = true)
    ArrayList<AdvRefreshCalls> advRefreshCalls;
}