package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DepotData implements Serializable {

    @SerializedName("depot_id")
    @Expose
    public String depotId;
    @SerializedName("depot_name")
    @Expose
    public String depotName;
    @SerializedName("agent_id")
    @Expose
    public String agentId;
    @SerializedName("agentname")
    @Expose
    public String agentName;
    @SerializedName("creditday")
    @Expose
    public String creditDay;


    public DepotData() {
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

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCreditDay() {
        return creditDay;
    }

    public void setCreditDay(String creditDay) {
        this.creditDay = creditDay;
    }
}