package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Freeze implements Serializable {

    @SerializedName("route_id")
    @Expose
    private String route_id;

    @SerializedName("salesman_id")
    @Expose
    private String salesman_id;

    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @SerializedName("have_fridge")
    @Expose
    private String have_fridge;

    @SerializedName("image1")
    @Expose
    private String image1;

    @SerializedName("image2")
    @Expose
    private String image2;

    @SerializedName("image3")
    @Expose
    private String image3;

    @SerializedName("image4")
    @Expose
    private String image4;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("comments")
    @Expose
    private String comments;

    @SerializedName("complaint_type")
    @Expose
    private String complaint_type;
//    @SerializedName("customer_Category")
//    @Expose
//    private String customerCategory;

    @SerializedName("fridge_scan_img")
    @Expose
    private String fridge_scan_img;

    public String getFridge_scan_img() {
        return fridge_scan_img;
    }

    public void setFridge_scan_img(String fridge_scan_img) {
        this.fridge_scan_img = fridge_scan_img;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    @SerializedName("serial_no")
    @Expose
    private String serial_no;

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getSalesman_id() {
        return salesman_id;
    }

    public void setSalesman_id(String salesman_id) {
        this.salesman_id = salesman_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getHave_fridge() {
        return have_fridge;
    }

    public void setHave_fridge(String have_fridge) {
        this.have_fridge = have_fridge;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComplaint_type() {
        return complaint_type;
    }

    public void setComplaint_type(String complaint_type) {
        this.complaint_type = complaint_type;
    }
}