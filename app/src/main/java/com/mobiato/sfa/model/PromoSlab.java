package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromoSlab implements Serializable {

    @SerializedName("lower_qty")
    @Expose
    public String lower_qty;
    @SerializedName("upper_qty")
    @Expose
    public String upper_qty;
    @SerializedName("free_qty")
    @Expose
    public String free_qty;

    public String getLower_qty() {
        return lower_qty;
    }

    public void setLower_qty(String lower_qty) {
        this.lower_qty = lower_qty;
    }

    public String getUpper_qty() {
        return upper_qty;
    }

    public void setUpper_qty(String upper_qty) {
        this.upper_qty = upper_qty;
    }

    public String getFree_qty() {
        return free_qty;
    }

    public void setFree_qty(String free_qty) {
        this.free_qty = free_qty;
    }
}
