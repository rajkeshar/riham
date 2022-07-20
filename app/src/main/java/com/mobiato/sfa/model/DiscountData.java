package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DiscountData implements Serializable {

    @SerializedName("discount_id")
    @Expose
    private String discountId;
    @SerializedName("discount_key")
    @Expose
    private String discount_key;
    @SerializedName("discount_desc")
    @Expose
    private String discount_desc;
    @SerializedName("discount_priority")
    @Expose
    private String discount_priority;
    @SerializedName("discount_type")
    @Expose
    private String discount_type;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("material_category")
    @Expose
    private String material_category;
    @SerializedName("include_customer")
    @Expose
    private String include_customer;
    @SerializedName("exclude_customer")
    @Expose
    private String exclude_customer;
    @SerializedName("discount_slab")
    @Expose
    private List<ItemSlab> discountSlab;
    @SerializedName("discount")
    @Expose
    private List<DiscountSlab> itemSlab;
    @SerializedName("customer")
    @Expose
    private List<DiscountCustomer> discCustomer;

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getDiscount_key() {
        return discount_key;
    }

    public void setDiscount_key(String discount_key) {
        this.discount_key = discount_key;
    }

    public String getDiscount_desc() {
        return discount_desc;
    }

    public void setDiscount_desc(String discount_desc) {
        this.discount_desc = discount_desc;
    }

    public String getDiscount_priority() {
        return discount_priority;
    }

    public void setDiscount_priority(String discount_priority) {
        this.discount_priority = discount_priority;
    }

    public List<DiscountSlab> getItemSlab() {
        return itemSlab;
    }

    public void setItemSlab(List<DiscountSlab> itemSlab) {
        this.itemSlab = itemSlab;
    }

    public List<DiscountCustomer> getDiscCustomer() {
        return discCustomer;
    }

    public void setDiscCustomer(List<DiscountCustomer> discCustomer) {
        this.discCustomer = discCustomer;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getInclude_customer() {
        return include_customer;
    }

    public void setInclude_customer(String include_customer) {
        this.include_customer = include_customer;
    }

    public String getExclude_customer() {
        return exclude_customer;
    }

    public void setExclude_customer(String exclude_customer) {
        this.exclude_customer = exclude_customer;
    }

    public List<ItemSlab> getDiscountSlab() {
        return discountSlab;
    }

    public void setDiscountSlab(List<ItemSlab> discountSlab) {
        this.discountSlab = discountSlab;
    }

    public String getMaterial_category() {
        return material_category;
    }

    public void setMaterial_category(String material_category) {
        this.material_category = material_category;
    }
}
