package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChannellType implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("outlet_channel_code")
    @Expose
    public String outlet_channel_code;
    @SerializedName("outlet_channel")
    @Expose
    public String outlet_channel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutlet_channel_code() {
        return outlet_channel_code;
    }

    public void setOutlet_channel_code(String outlet_channel_code) {
        this.outlet_channel_code = outlet_channel_code;
    }

    public String getOutlet_channel() {
        return outlet_channel;
    }

    public void setOutlet_channel(String outlet_channel) {
        this.outlet_channel = outlet_channel;
    }
}
