package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FOCItems implements Serializable {

    @SerializedName("promotion_id")
    @Expose
    public String promotionId;
    @SerializedName("priority")
    @Expose
    public String priority;
    @SerializedName("customers_id")
    @Expose
    public String customerId;
    @SerializedName("MinQuantityItemID")
    @Expose
    public String itemId;
    @SerializedName("MinQuantityUOM")
    @Expose
    public String itemUOM;
    @SerializedName("MinQuantityQty")
    @Expose
    public String itemQTY;
    @SerializedName("MaxQuantityQty")
    @Expose
    public String maxitemQTY;
    @SerializedName("FreeGoodItemID")
    @Expose
    public String focItemId;
    @SerializedName("FreeGoodsUOM")
    @Expose
    public String focItemUOM;
    @SerializedName("FreeGoodQty")
    @Expose
    public String focItemQTY;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemUOM() {
        return itemUOM;
    }

    public void setItemUOM(String itemUOM) {
        this.itemUOM = itemUOM;
    }

    public String getItemQTY() {
        return itemQTY;
    }

    public void setItemQTY(String itemQTY) {
        this.itemQTY = itemQTY;
    }

    public String getMaxItemQTY() {
        return maxitemQTY;
    }

    public void setMaxItemQTY(String maxitemQTY) {
        this.maxitemQTY = maxitemQTY;
    }

    public String getFocItemId() {
        return focItemId;
    }

    public void setFocItemId(String focItemId) {
        this.focItemId = focItemId;
    }

    public String getFocItemUOM() {
        return focItemUOM;
    }

    public void setFocItemUOM(String focItemUOM) {
        this.focItemUOM = focItemUOM;
    }

    public String getFocItemQTY() {
        return focItemQTY;
    }

    public void setFocItemQTY(String focItemQTY) {
        this.focItemQTY = focItemQTY;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}
