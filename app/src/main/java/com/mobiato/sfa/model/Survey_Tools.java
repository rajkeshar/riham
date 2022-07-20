package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Survey_Tools implements Serializable {

    @SerializedName("survey_id")
    @Expose
    private String Survey_Id;
    @SerializedName("asset_id")
    @Expose
    private String Asset_id;
    @SerializedName("distribution_type")
    @Expose
    private String Distribution_Type;
    @SerializedName("cust_id")
    @Expose
    private String CustomerID;
    @SerializedName("questions")
    @Expose
    private ArrayList<Stock_Questions> Questions;


    public String getSurvey_Id() {
        return Survey_Id;
    }

    public void setSurvey_Id(String Survey_Id) {
        this.Survey_Id = Survey_Id;
    }

    public String getAsset_id() {
        return Asset_id;
    }

    public void setAsset_id(String Asset_id) {
        this.Asset_id = Asset_id;
    }

    public String getDistribution_Type() {
        return Distribution_Type;
    }

    public void setDistribution_Type(String Distribution_Type) {
        this.Distribution_Type = Distribution_Type;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public ArrayList<Stock_Questions> getQuestions() {
        return Questions;
    }

    public void setQuestions(ArrayList<Stock_Questions> Questions) {
        this.Questions = Questions;
    }

}