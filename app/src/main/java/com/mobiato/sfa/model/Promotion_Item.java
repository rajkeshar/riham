package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Promotion_Item implements Serializable {

    @SerializedName("promotional_id")
    @Expose
    private String promotionId;

    @SerializedName("item_id")
    @Expose
    private String Item_Id;

    @SerializedName("item_name")
    @Expose
    private String Item_Name;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("from_date")
    @Expose
    private String From_Date;
    @SerializedName("to_date")
    @Expose
    private String To_Date;


    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String Item_Id) {
        this.Item_Id = Item_Id;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFrom_Date() {
        return From_Date;
    }

    public void setFrom_Date(String From_Date) {
        this.From_Date = From_Date;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String Item_Name) {
        this.Item_Name = Item_Name;
    }

    public String getTo_Date() {
        return To_Date;
    }

    public void setTo_Date(String To_Date) {
        this.To_Date = To_Date;
    }



}