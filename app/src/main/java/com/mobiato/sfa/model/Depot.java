package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Depot implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("depot_code")
    @Expose
    private String depot_code;
    @SerializedName("depot_name")
    @Expose
    private String depot_name;
    @SerializedName("route_id")
    @Expose
    private String route_id;
    @SerializedName("routename")
    @Expose
    private String routename;
    @SerializedName("routecode")
    @Expose
    private String routecode;
    @SerializedName("agent_id")
    @Expose
    private String agent_id;
    @SerializedName("depot_manager")
    @Expose
    private String depot_manager;
    private String isSelect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepot_code() {
        return depot_code;
    }

    public void setDepot_code(String depot_code) {
        this.depot_code = depot_code;
    }

    public String getDepot_name() {
        return depot_name;
    }

    public void setDepot_name(String depot_name) {
        this.depot_name = depot_name;
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

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getDepot_manager() {
        return depot_manager;
    }

    public void setDepot_manager(String depot_manager) {
        this.depot_manager = depot_manager;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }
}