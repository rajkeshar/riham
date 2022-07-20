package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Compititor implements Serializable {

    @SerializedName("compititor_id")
    @Expose
    private String compititorId;

    @SerializedName("compititor_company_name")
    @Expose
    private String compititorCompanyName;

    @SerializedName("compititor_brand")
    @Expose
    private String compititor_brand;
    @SerializedName("compititor_item_name")
    @Expose
    private String Compititor_ItemName;
    @SerializedName("compititor_price")
    @Expose
    private String COMPITITOR_Price;
    @SerializedName("compititor_promotion")
    @Expose
    private String Compititor_Promotion;
    @SerializedName("compititor_notes")
    @Expose
    private String Compititor_Notes;
    @SerializedName("compititor_Image1")
    @Expose
    private String Compititor_Image1;
    @SerializedName("compititor_Image2")
    @Expose
    private String Compititor_Image2;
    @SerializedName("compititor_Image3")
    @Expose
    private String Compititor_Image3;
    @SerializedName("compititor_Image4")
    @Expose
    private String Compititor_Image4;
//    @SerializedName("customer_Category")
//    @Expose
//    private String customerCategory;

    public String getCompititorId() {
        return compititorId;
    }

    public void setCompititorId(String compititorId) {
        this.compititorId = compititorId;
    }

    public String getCompititorCompanyName() {
        return compititorCompanyName;
    }

    public void setCompititorCompanyName(String compititorCompanyName) {
        this.compititorCompanyName = compititorCompanyName;
    }

    public String getCompititor_brand() {
        return compititor_brand;
    }

    public void setCompititor_brand(String compititor_brand) {
        this.compititor_brand = compititor_brand;
    }

    public String getCompititor_ItemName() {
        return Compititor_ItemName;
    }

    public void setCompititor_ItemName(String Compititor_ItemName) {
        this.Compititor_ItemName = Compititor_ItemName;
    }

    public String getCOMPITITOR_Price() {
        return COMPITITOR_Price;
    }

    public void setCOMPITITOR_Price(String COMPITITOR_Price) {
        this.COMPITITOR_Price = COMPITITOR_Price;
    }

    public String getCompititor_Promotion() {
        return Compititor_Promotion;
    }

    public void setCompititor_Promotion(String Compititor_Promotion) {
        this.Compititor_Promotion = Compititor_Promotion;
    }

    public String getCompititor_Notes() {
        return Compititor_Notes;
    }

    public void setCompititor_Notes(String Compititor_Notes) {
        this.Compititor_Notes = Compititor_Notes;
    }
    public String getCompititor_Image1() {
        return Compititor_Image1;
    }

    public void setCompititor_Image1(String Compititor_Image1) {
        this.Compititor_Image1 = Compititor_Image1;
    }

    public String getCompititor_Image2() {
        return Compititor_Image2;
    }

    public void setCompititor_Image2(String Compititor_Image2) {
        this.Compititor_Image2 = Compititor_Image2;
    }

    public String getCompititor_Image3() {
        return Compititor_Image3;
    }

    public void setCompititor_Image3(String Compititor_Image3) {
        this.Compititor_Image3 =Compititor_Image3;
    }

    public String getCompititor_Image4() {
        return Compititor_Image4;
    }

    public void setCompititor_Image4(String Compititor_Image4) {
        this.Compititor_Image4 = Compititor_Image4;
    }

}