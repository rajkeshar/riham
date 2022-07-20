package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RouteData implements Serializable {

    @SerializedName("depot_id")
    @Expose
    public String depotId;
    @SerializedName("depot_name")
    @Expose
    public String depotName;
    @SerializedName("route_id")
    @Expose
    public String route_id;
    @SerializedName("routename")
    @Expose
    public String routename;
    @SerializedName("routecode")
    @Expose
    public String routecode;


    public RouteData() {
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getRoutecode() {
        return routecode;
    }

    public void setRoutecode(String routecode) {
        this.routecode = routecode;
    }
}