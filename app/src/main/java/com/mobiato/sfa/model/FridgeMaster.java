package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FridgeMaster implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("fridge_code")
    @Expose
    private String fridge_code;
    
    @SerializedName("serial_number")
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

    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @SerializedName("customername")
    @Expose
    private String customername;

    @SerializedName("ccode")
    @Expose
    private String ccode;

    @SerializedName("owner_name")
    @Expose
    private String owner_name;

    @SerializedName("customerphone")
    @Expose
    private String customerphone;

    @SerializedName("customer_phone2")
    @Expose
    private String customer_phone2;

    @SerializedName("district")
    @Expose
    private String district;


    @SerializedName("street")
    @Expose
    private String street;

    @SerializedName("road_street")
    @Expose
    private String road_street;

    @SerializedName("landmark")
    @Expose
    private String landmark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFridge_code() {
        return fridge_code;
    }

    public void setFridge_code(String fridge_code) {
        this.fridge_code = fridge_code;
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

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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
}