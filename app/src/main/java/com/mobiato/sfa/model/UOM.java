package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UOM implements Serializable {

    @SerializedName("uom_id")
    @Expose
    private String uomId;
    @SerializedName("name")
    @Expose
    private String uomName;
    private String uomType;
    private String uomQty;
    private String uomGross;
    private String uomPrice;
    private String uomVat;
    private String uomNet;
    private String uomPrevat;
    private String uomExcise;
    private String uomtotal;
    private String uomExp;
    private String uomReason;
    private String uomreasonCode;
    private String uomReturnnType;

    public String getUomReturnnType() {
        return uomReturnnType;
    }

    public void setUomReturnnType(String uomReturnnType) {
        this.uomReturnnType = uomReturnnType;
    }

    public String getUomGross() {
        return uomGross;
    }

    public void setUomGross(String uomGross) {
        this.uomGross = uomGross;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getUomType() {
        return uomType;
    }

    public void setUomType(String uomType) {
        this.uomType = uomType;
    }

    public String getUomQty() {
        return uomQty;
    }

    public void setUomQty(String uomQty) {
        this.uomQty = uomQty;
    }

    public String getUomPrice() {
        return uomPrice;
    }

    public void setUomPrice(String uomPrice) {
        this.uomPrice = uomPrice;
    }

    public String getUomVat() {
        return uomVat;
    }

    public void setUomVat(String uomVat) {
        this.uomVat = uomVat;
    }

    public String getUomNet() {
        return uomNet;
    }

    public void setUomNet(String uomNet) {
        this.uomNet = uomNet;
    }

    public String getUomPrevat() {
        return uomPrevat;
    }

    public void setUomPrevat(String uomPrevat) {
        this.uomPrevat = uomPrevat;
    }

    public String getUomExcise() {
        return uomExcise;
    }

    public void setUomExcise(String uomExcise) {
        this.uomExcise = uomExcise;
    }

    public String getUomtotal() {
        return uomtotal;
    }

    public void setUomtotal(String uomtotal) {
        this.uomtotal = uomtotal;
    }

    public String getUomExp() {
        return uomExp;
    }

    public void setUomExp(String uomExp) {
        this.uomExp = uomExp;
    }

    public String getUomReason() {
        return uomReason;
    }

    public void setUomReason(String uomReason) {
        this.uomReason = uomReason;
    }

    public String getUomreasonCode() {
        return uomreasonCode;
    }

    public void setUomreasonCode(String uomreasonCode) {
        this.uomreasonCode = uomreasonCode;
    }
}