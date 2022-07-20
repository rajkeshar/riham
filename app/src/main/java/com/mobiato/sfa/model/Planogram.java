package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Planogram implements Serializable {

    @SerializedName("planogram_id")
    @Expose
    private String planogramId;
    @SerializedName("planogram_name")
    @Expose
    private String planogramName;
    @SerializedName("valid_date_from")
    @Expose
    private String validDateFrom;
    @SerializedName("valid_date_to")
    @Expose
    private String validDateTo;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("plano_images")
    @Expose
    private ArrayList<PlanoImages> Images;



    public String getPlanogramId() {
        return planogramId;
    }

    public void setPlanogramId(String planogramId) {
        this.planogramId = planogramId;
    }

    public String getPlanogramName() {
        return planogramName;
    }

    public void setPlanogramName(String planogramName) {
        this.planogramName = planogramName;
    }

    public String getValidDateFrom() {
        return validDateFrom;
    }

    public void setValidDateFrom(String validDateFrom) {
        this.validDateFrom = validDateFrom;
    }

    public String getValidDateTo() {
        return validDateTo;
    }

    public void setValidDateTo(String validDateTo) {
        this.validDateTo = validDateTo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public ArrayList<PlanoImages> getPlanogram() {
        return Images;
    }

    public void setPlanogram(ArrayList<PlanoImages> Images) {
        this.Images = Images;
    }

}