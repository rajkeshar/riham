package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PromoItems implements Serializable {

    @SerializedName("promotional_id")
    @Expose
    public String promotionId;
    @SerializedName("promotional_name")
    @Expose
    public String promotional_name;
    @SerializedName("excluded_customer_ids")
    @Expose
    public String excluded_customer;
    @SerializedName("included_customer_ids")
    @Expose
    public String included_customer;
    @SerializedName("assignment_uom")
    @Expose
    public String assignment_uom;
    @SerializedName("qualification_uom")
    @Expose
    public String qualification_uom;
    @SerializedName("assignment_item_id")
    @Expose
    public String assignment_item_id;
    @SerializedName("qualification_item_id")
    @Expose
    public String qualification_item_id;
    @SerializedName("promo_slab")
    @Expose
    public ArrayList<PromoSlab> promo_slab;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotional_name() {
        return promotional_name;
    }

    public void setPromotional_name(String promotional_name) {
        this.promotional_name = promotional_name;
    }

    public String getAssignment_uom() {
        return assignment_uom;
    }

    public void setAssignment_uom(String assignment_uom) {
        this.assignment_uom = assignment_uom;
    }

    public String getQualification_uom() {
        return qualification_uom;
    }

    public void setQualification_uom(String qualification_uom) {
        this.qualification_uom = qualification_uom;
    }

    public String getAssignment_item_id() {
        return assignment_item_id;
    }

    public void setAssignment_item_id(String assignment_item_id) {
        this.assignment_item_id = assignment_item_id;
    }

    public String getQualification_item_id() {
        return qualification_item_id;
    }

    public void setQualification_item_id(String qualification_item_id) {
        this.qualification_item_id = qualification_item_id;
    }

    public String getExcluded_customer() {
        return excluded_customer;
    }

    public void setExcluded_customer(String excluded_customer) {
        this.excluded_customer = excluded_customer;
    }

    public String getIncluded_customer() {
        return included_customer;
    }

    public void setIncluded_customer(String included_customer) {
        this.included_customer = included_customer;
    }

    public ArrayList<PromoSlab> getPromo_slab() {
        return promo_slab;
    }

    public void setPromo_slab(ArrayList<PromoSlab> promo_slab) {
        this.promo_slab = promo_slab;
    }
}
