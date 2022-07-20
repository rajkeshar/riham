package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Complain implements Serializable {

    @SerializedName("Complain_id")
    @Expose
    private String complainId;

    @SerializedName("complain_Feedbak")
    @Expose
    private String complain_Feedback;

    @SerializedName("complain_brand")
    @Expose
    private String complain_brand;
    @SerializedName("complain_Note")
    @Expose
    private String Complanin_Note;

    @SerializedName("complain_Image1")
    @Expose
    private String complain_Image1;
    @SerializedName("complain_Image2")
    @Expose
    private String complain_Image2;
    @SerializedName("complain_Image3")
    @Expose
    private String complain_Image3;
    @SerializedName("complain_Image4")
    @Expose
    private String complain_Image4;
    private String itemId;
//    @SerializedName("customer_Category")
//    @Expose
//    private String customerCategory;

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getComplain_brand() {
        return complain_brand;
    }

    public void setComplain_brand(String complain_brand) {
        this.complain_brand = complain_brand;
    }

    public String getComplain_Feedback() {
        return complain_Feedback;
    }

    public void setComplain_Feedback(String complain_Feedback) {
        this.complain_Feedback = complain_Feedback;
    }

    public String getComplanin_Note() {
        return Complanin_Note;
    }

    public void setComplanin_Note(String Complanin_Note) {
        this.Complanin_Note = Complanin_Note;
    }


    public String getComplain_Image1() {
        return complain_Image1;
    }

    public void setComplain_Image1(String complain_Image1) {
        this.complain_Image1 = complain_Image1;
    }

    public String getComplain_Image2() {
        return complain_Image2;
    }

    public void setComplain_Image2(String complain_Image2) {
        this.complain_Image2 = complain_Image2;
    }

    public String getComplain_Image3() {
        return complain_Image3;
    }

    public void setComplain_Image3(String complain_Image3) {
        this.complain_Image3 = complain_Image3;
    }

    public String getComplain_Image4() {
        return complain_Image4;
    }

    public void setComplain_Image4(String complain_Image4) {
        this.complain_Image4 = complain_Image4;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}