package com.mobiato.sfa.model;

import java.io.Serializable;

public class PromotionData implements Serializable {

    public String customerId;
    public String custName;
    public String itemId;
    public String itemName;
    public String itemUOM;
    public String itemQTY;
    public String itemMaxQTY;
    public String focItemId;
    public String focItemName;
    public String focItemUOM;
    public String focItemQTY;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getItemMaxQTY() {
        return itemMaxQTY;
    }

    public void setItemMaxQTY(String itemMaxQTY) {
        this.itemMaxQTY = itemMaxQTY;
    }

    public String getFocItemId() {
        return focItemId;
    }

    public void setFocItemId(String focItemId) {
        this.focItemId = focItemId;
    }

    public String getFocItemName() {
        return focItemName;
    }

    public void setFocItemName(String focItemName) {
        this.focItemName = focItemName;
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
}
