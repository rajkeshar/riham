package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Promotion implements Serializable {

    @SerializedName("promotion_id")
    @Expose
    private String promotionId;

    @SerializedName("promotion_cust_name")
    @Expose
    private String PromotioncustomerName;

    @SerializedName("promotion_cust_phone")
    @Expose
    private String PromotioncustPhone;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("humper")
    @Expose
    private String humper;
    @SerializedName("category")
    @Expose
    private String Category;
    @SerializedName("promotion_item_name")
    @Expose
    private String PromotionItemName;
    @SerializedName("promotion_item_id")
    @Expose
    private String PromotionItemId;
    private String invoiceNo;
    private String invoiceImage;

//    @SerializedName("customer_Category")
//    @Expose
//    private String customerCategory;

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHumper() {
        return humper;
    }

    public void setHumper(String humper) {
        this.humper = humper;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotioncustomerName() {
        return PromotioncustomerName;
    }

    public void setPromotioncustomerName(String PromotioncustomerName) {
        this.PromotioncustomerName = PromotioncustomerName;
    }

    public String getPromotioncustPhone() {
        return PromotioncustPhone;
    }

    public void setPromotioncustPhone(String PromotioncustPhone) {
        this.PromotioncustPhone = PromotioncustPhone;
    }

    public String getPromotionItemId() {
        return PromotionItemId;
    }

    public void setPromotionItemId(String PromotionItemId) {
        this.PromotionItemId = PromotionItemId;
    }

    public String getPromotionItemName() {
        return PromotionItemName;
    }

    public void setPromotionItemName(String PromotionItemName) {
        this.PromotionItemName = PromotionItemName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceImage() {
        return invoiceImage;
    }

    public void setInvoiceImage(String invoiceImage) {
        this.invoiceImage = invoiceImage;
    }
}