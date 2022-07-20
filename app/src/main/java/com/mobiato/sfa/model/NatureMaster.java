package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NatureMaster implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("ticket_no")
    @Expose
    private String ticket_no;
    
    @SerializedName("chiller_serial_number")
    @Expose
    private String serial_number;
    
    @SerializedName("asset_number")
    @Expose
    private String asset_number;

    @SerializedName("model_number")
    @Expose
    private String model_number;

    @SerializedName("branding")
    @Expose
    private String branding;

    @SerializedName("outlet_name")
    @Expose
    private String outlet_name;

    @SerializedName("nature_of_call")
    @Expose
    private String nature_of_call;

    @SerializedName("owner_name")
    @Expose
    private String owner_name;

    @SerializedName("contact_no1")
    @Expose
    private String customerphone;

    @SerializedName("contact_no2")
    @Expose
    private String customer_phone2;

    @SerializedName("town")
    @Expose
    private String town;

    @SerializedName("road_street")
    @Expose
    private String road_street;

    @SerializedName("landmark")
    @Expose
    private String landmark;

    @SerializedName("created_date")
    @Expose
    private String date;

    @SerializedName("district")
    @Expose
    private String district;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicket_no() {
        return ticket_no;
    }

    public void setTicket_no(String ticket_no) {
        this.ticket_no = ticket_no;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getAsset_number() {
        return asset_number;
    }

    public void setAsset_number(String asset_number) {
        this.asset_number = asset_number;
    }

    public String getModel_number() {
        return model_number;
    }

    public void setModel_number(String model_number) {
        this.model_number = model_number;
    }

    public String getBranding() {
        return branding;
    }

    public void setBranding(String branding) {
        this.branding = branding;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getNature_of_call() {
        return nature_of_call;
    }

    public void setNature_of_call(String nature_of_call) {
        this.nature_of_call = nature_of_call;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public String getCustomer_phone2() {
        return customer_phone2;
    }

    public void setCustomer_phone2(String customer_phone2) {
        this.customer_phone2 = customer_phone2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getRoad_street() {
        return road_street;
    }

    public void setRoad_street(String road_street) {
        this.road_street = road_street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}