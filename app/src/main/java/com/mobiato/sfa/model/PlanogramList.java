package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlanogramList implements Serializable {

    @SerializedName("planogram_id")
    @Expose
    private String planogramId;
    @SerializedName("planogram_name")
    @Expose
    private String planogramName;
    @SerializedName("DISTRIBUTION_TOOL_ID")
    @Expose
    private String distribution_tool_id;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("IMAGE1")
    @Expose
    private String image1;
    @SerializedName("IMAGE2")
    @Expose
    private String image2;
    @SerializedName("IMAGE3")
    @Expose
    private String image3;
    @SerializedName("IMAGE4")
    @Expose
    private String image4;
    @SerializedName("FRONT_IMAGE")
    @Expose
    private String front_image;
    @SerializedName("BACK_IMAGE")
    @Expose
    private String back_image;
    @SerializedName("COMMENT")
    @Expose
    private String comment;
    @SerializedName("distribution_tool_name")
    @Expose
    private String Distribution_Tool_Name;

    public String getPlanogramId() {
        return planogramId;
    }

    public void setPlanogramId(String planogramId) {
        this.planogramId = planogramId;
    }

    public String getDistribution_Tool_Name() {
        return Distribution_Tool_Name;
    }

    public void setDistribution_Tool_Name(String Distribution_Tool_Name) {
        this.Distribution_Tool_Name = Distribution_Tool_Name;
    }


    public String getPlanogramName() {
        return planogramName;
    }

    public void setPlanogramName(String planogramName) {
        this.planogramName = planogramName;
    }

    public String getDistribution_tool_id() {
        return distribution_tool_id;
    }

    public void setDistribution_tool_id(String distribution_tool_id) {
        this.distribution_tool_id = distribution_tool_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getFront_image() {
        return front_image;
    }

    public void setFront_image(String front_image) {
        this.front_image = front_image;
    }

    public String getBack_image() {
        return back_image;
    }

    public void setBack_image(String back_image) {
        this.back_image = back_image;
    }


}