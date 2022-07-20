package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sales {

    @SerializedName("data")
    @Expose
    private SalesVo data;

    public SalesVo getData() {
        return data;
    }

    public void setData(SalesVo data) {
        this.data = data;
    }
}
