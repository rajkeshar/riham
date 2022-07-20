package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ASSETS_MODEL implements Serializable {

    @SerializedName("asset_id")
    @Expose
    private String assetsId;
    @SerializedName("asset_code")
    @Expose
    private String assetscode;
    @SerializedName("asset_name")
    @Expose
    private String assetsName;
    @SerializedName("asset_type")
    @Expose
    private String assetsType;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("image")
    @Expose
    private String assetsImage;

    @SerializedName("assets_feedback")
    @Expose
    private String assetsFeedback;

    @SerializedName("assets_image1")
    @Expose
    private String assetsImage1;
    @SerializedName("assets_image2")
    @Expose
    private String assetsImage2;
    @SerializedName("assets_image3")
    @Expose
    private String assetsImage3;
    @SerializedName("assets_image4")
    @Expose
    private String assetsImage4;


    public String getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(String assetsId) {
        this.assetsId = assetsId;
    }

    public String getAssetsFeedback() {
        return assetsFeedback;
    }

    public void setAssetsFeedback(String assetsFeedback) {
        this.assetsFeedback = assetsFeedback;
    }

    public String getAssetsImage1() {
        return assetsImage1;
    }

    public void setAssetsImage1(String assetsImage1) {
        this.assetsImage1 = assetsImage1;
    }

    public String getAssetsImage2() {
        return assetsImage2;
    }

    public void setAssetsImage2(String assetsImage2) {
        this.assetsImage2 = assetsImage2;
    }

    public String getAssetsImage3() {
        return assetsImage3;
    }

    public void setAssetsImage3(String assetsImage3) {
        this.assetsImage3 = assetsImage3;
    }

    public String getAssetsImage4() {
        return assetsImage4;
    }

    public void setAssetsImage4(String assetsImage4) {
        this.assetsImage4 = assetsImage4;
    }

    public String getAssetscode() {
        return assetscode;
    }

    public void setAssetscode(String assetscode) {
        this.assetscode = assetscode;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public String getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAssetsImage() {
        return assetsImage;
    }

    public void setAssetsImage(String assetsImage) {
        this.assetsImage = assetsImage;
    }
}