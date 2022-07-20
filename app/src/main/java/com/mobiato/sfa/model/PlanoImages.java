package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlanoImages implements Serializable {

    @SerializedName("distribution_tool_id")
    @Expose
    private String DistributionToolId;
    @SerializedName("image1")
    @Expose
    private String Image1;
    @SerializedName("image2")
    @Expose
    private String Image2;
    @SerializedName("image3")
    @Expose
    private String Image3;

    @SerializedName("image4")
    @Expose
    private String Image4;
    @SerializedName("distribution_tool_name")
    @Expose
    private String DISTRIBUTION_TOOL_NAME;
    public String getDistributionToolId() {
        return DistributionToolId;
    }

    public void setDistributionToolId(String DistributionToolId) {
        this.DistributionToolId = DistributionToolId;
    }
    public String getDISTRIBUTION_TOOL_NAME() {
        return DISTRIBUTION_TOOL_NAME;
    }

    public void setDISTRIBUTION_TOOL_NAME(String DISTRIBUTION_TOOL_NAME) {
        this.DISTRIBUTION_TOOL_NAME = DISTRIBUTION_TOOL_NAME;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String Image1) {
        this.Image1 = Image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String Image2) {
        this.Image2 = Image2;
    }

    public String getImage3() {
        return Image3;
    }

    public void setImage3(String Image3) {
        this.Image3 = Image3;
    }

    public String getImage4() {
        return Image4;
    }

    public void setImage4(String Image4) {
        this.Image4 = Image4;
    }
}