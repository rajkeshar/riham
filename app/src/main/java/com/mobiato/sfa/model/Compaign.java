package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Compaign implements Serializable {

    @SerializedName("Compaign_id")
    @Expose
    private String compaignId;

    @SerializedName("feedback")
    @Expose
    private String comment;
    @SerializedName("compaignImage1")
    @Expose
    private String compaign_Image1;
    @SerializedName("compaign_Image2")
    @Expose
    private String compaign_Image2;
    @SerializedName("compaign_Image3")
    @Expose
    private String compaign_Image3;
    @SerializedName("compaign_Image4")
    @Expose
    private String compaign_Image4;
    @SerializedName("customer_id")
    @Expose
    private String CustomerId;

    public String getCompaignId() {
        return compaignId;
    }

    public void setCompaignId(String compaignId) {
        this.compaignId = compaignId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCompaign_Image1() {
        return compaign_Image1;
    }

    public void setCompaign_Image1(String compaign_Image1) {
        this.compaign_Image1 = compaign_Image1;
    } public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getCompaign_Image2() {
        return compaign_Image2;
    }

    public void setCompaign_Image2(String compaign_Image2) {
        this.compaign_Image2 = compaign_Image2;
    }

    public String getCompaign_Image3() {
        return compaign_Image3;
    }

    public void setCompaign_Image3(String compaign_Image3) {
        this.compaign_Image3 = compaign_Image3;
    }

    public String getCompaign_Image4() {
        return compaign_Image4;
    }

    public void setCompaign_Image4(String compaign_Image4) {
        this.compaign_Image4 = compaign_Image4;
    }
}