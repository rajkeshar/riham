package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Distribution implements Serializable {

    @SerializedName("assign_item_id")
    @Expose
    private String AssignitemId;
    @SerializedName("cust_id")
    @Expose
    private String CustomerId;
    @SerializedName("distribution_tool_id")
    @Expose
    private String Distribution_Tool_Id;
    @SerializedName("distribution_tool_name")
    @Expose
    private String Distribution_Tool_Name;
    @SerializedName("distribution_tool_valid_from")
    @Expose
    private String Distribution_Tool_From;
    @SerializedName("distribution_tool_valid_to")
    @Expose
    private String Distribution_Tool_To;
    @SerializedName("distribution_tool_height")
    @Expose
    private String Distribution_Tool_Height;
    @SerializedName("distribution_tool_width")
    @Expose
    private String Distribution_Tool_Width;
    @SerializedName("distribution_tool_lenght")
    @Expose
    private String Distribution_Tool_Length;
    @SerializedName("distribution_tool_capacity")
    @Expose
    private String Distribution_Tool_Capacity;
    @SerializedName("item_id")
    @Expose
    private String Item_Id;
    @SerializedName("alternatecode")
    @Expose
    private String Alternate_Code;
    @SerializedName("item_name")
    @Expose
    private String Item_Name;
    @SerializedName("uom")
    @Expose
    private String UOM;
    @SerializedName("qty")
    @Expose
    private String QTY;
    @SerializedName("item_category")
    @Expose
    private String Item_Category;
    private String SalesmanId;
    private String PC;
    private String ExpiryDate;
    private String fillQty;
    private String goodSaleQty;


    public String getAssignitemId() {
        return AssignitemId;
    }

    public void setAssignitemId(String AssignitemId) {
        this.AssignitemId = AssignitemId;
    }

    public String getAlternate_Code() {
        return Alternate_Code;
    }

    public void setAlternate_Code(String Alternate_Code) {
        this.Alternate_Code = Alternate_Code;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getDistribution_Tool_Id() {
        return Distribution_Tool_Id;
    }

    public void setDistribution_Tool_Id(String Distribution_Tool_Id) {
        this.Distribution_Tool_Id = Distribution_Tool_Id;
    }

    public String getDistribution_Tool_Name() {
        return Distribution_Tool_Name;
    }

    public void setDistribution_Tool_Name(String Distribution_Tool_Name) {
        this.Distribution_Tool_Name = Distribution_Tool_Name;
    }

    public String getDistribution_Tool_From() {
        return Distribution_Tool_From;
    }

    public void setDistribution_Tool_From(String Distribution_Tool_From) {
        this.Distribution_Tool_From = Distribution_Tool_From;
    }

    public String getDistribution_Tool_To() {
        return Distribution_Tool_To;
    }

    public void setDistribution_Tool_To(String Distribution_Tool_To) {
        this.Distribution_Tool_To = Distribution_Tool_To;
    }

    public String getDistribution_Tool_Height() {
        return Distribution_Tool_Height;
    }

    public void setDistribution_Tool_Height(String Distribution_Tool_Height) {
        this.Distribution_Tool_Height = Distribution_Tool_Height;
    }

    public String getDistribution_Tool_Width() {
        return Distribution_Tool_Width;
    }

    public void setDistribution_Tool_Width(String Distribution_Tool_Width) {
        this.Distribution_Tool_Width = Distribution_Tool_Width;
    }

    public String getDistribution_Tool_Length() {
        return Distribution_Tool_Length;
    }

    public void setDistribution_Tool_Length(String Distribution_Tool_Length) {
        this.Distribution_Tool_Length = Distribution_Tool_Length;
    }

    public String getDistribution_Tool_Capacity() {
        return Distribution_Tool_Capacity;
    }

    public void setDistribution_Tool_Capacity(String Distribution_Tool_Capacity) {
        this.Distribution_Tool_Capacity = Distribution_Tool_Capacity;
    }

    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String Item_Id) {
        this.Item_Id = Item_Id;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String Item_Name) {
        this.Item_Name = Item_Name;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getItem_Category() {
        return Item_Category;
    }

    public void setItem_Category(String Item_Category) {
        this.Item_Category = Item_Category;
    }

    public String getSalesmanId() {
        return SalesmanId;
    }

    public void setSalesmanId(String SalesmanId) {
        this.SalesmanId = SalesmanId;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String ExpiryDate) {
        this.ExpiryDate = ExpiryDate;
    }

    public String getFillQty() {
        return fillQty;
    }

    public void setFillQty(String fillQty) {
        this.fillQty = fillQty;
    }

    public String getGoodSaleQty() {
        return goodSaleQty;
    }

    public void setGoodSaleQty(String goodSaleQty) {
        this.goodSaleQty = goodSaleQty;
    }
}