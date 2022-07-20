package com.mobiato.sfa.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ReportBarData implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
