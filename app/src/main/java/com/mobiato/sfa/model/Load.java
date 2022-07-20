package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Load implements Serializable {

    @SerializedName("loadid")
    @Expose
    private String load_no;
    @SerializedName("sub_loadid")
    @Expose
    private String sub_loadId;
    @SerializedName("route_id")
    @Expose
    private String routeId;
    @SerializedName("load_date")
    @Expose
    private String del_date;
    public String is_verified;
    public String is_uploaded;
    @SerializedName("loaditems")
    @Expose
    private ArrayList<Item> loadItems;

    public String getLoad_no() {
        return load_no;
    }

    public void setLoad_no(String load_no) {
        this.load_no = load_no;
    }

    public String getSub_loadId() {
        return sub_loadId;
    }

    public void setSub_loadId(String sub_loadId) {
        this.sub_loadId = sub_loadId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getDel_date() {
        return del_date;
    }

    public void setDel_date(String del_date) {
        this.del_date = del_date;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getIs_uploaded() {
        return is_uploaded;
    }

    public void setIs_uploaded(String is_uploaded) {
        this.is_uploaded = is_uploaded;
    }

    public ArrayList<Item> getLoadItems() {
        return loadItems;
    }

    public void setLoadItems(ArrayList<Item> loadItems) {
        this.loadItems = loadItems;
    }
}

