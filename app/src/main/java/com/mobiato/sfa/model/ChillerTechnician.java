package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChillerTechnician implements Serializable {


    @SerializedName("fridge_id")
    @Expose
    private String fridge_id;

    @SerializedName("ir_id")
    @Expose
    private String ir_id;

    public String getIr_id() {
        return ir_id;
    }

    public void setIr_id(String ir_id) {
        this.ir_id = ir_id;
    }

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

    @SerializedName("acquisition")
    @Expose
    private String acquisition;

    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;

    @SerializedName("fridge_type")
    @Expose
    private String fridge_type;

    @SerializedName("branding")
    @Expose
    private String branding;

    public String getBranding() {
        return branding;
    }

    public void setBranding(String branding) {
        this.branding = branding;
    }

    public String getFridge_id() {
        return fridge_id;
    }

    String AgreementID;

    public String getAgreementID() {
        return AgreementID;
    }

    public void setAgreementID(String agreementID) {
        AgreementID = agreementID;
    }

    public void setFridge_id(String fridge_id) {
        this.fridge_id = fridge_id;
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

    public String getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(String acquisition) {
        this.acquisition = acquisition;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFridge_type() {
        return fridge_type;
    }

    public void setFridge_type(String fridge_type) {
        this.fridge_type = fridge_type;
    }
}