package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Item implements Serializable {

    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_code")
    @Expose
    private String itemCode;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description2")
    @Expose
    private String description2;
    @SerializedName("baseuom")
    @Expose
    private String baseUOM;
    @SerializedName("alternateuom")
    @Expose
    private String altrUOM;
    @SerializedName("upc")
    @Expose
    private String upc;
    @SerializedName("uom_price")
    @Expose
    private String UOMPrice;
    @SerializedName("alternet_umo_price")
    @Expose
    private String alternateUOMPrice;
    @SerializedName("pack_size")
    @Expose
    private String packSize;
    @SerializedName("item_category")
    @Expose
    private String category;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("shelf_life")
    @Expose
    private String shelfLife;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("uom")
    @Expose
    private String uom;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("baseuomvol")
    @Expose
    private String baseVolume;
    @SerializedName("alternateuomvol")
    @Expose
    private String altVolume;
    @SerializedName("agent_excise")
    @Expose
    private String agent_excise;

    @SerializedName("direct_sell_excise")
    @Expose
    private String direct_sell_excise;

    private String actQty;
    private String saleQty;
    private String baseUOMQty;
    private String baseUOMPrice;
    private String alterUOMQty;
    private String alterUOMPrice;
    private String baseUOMName;
    private String alterUOMName;
    private String saleAltQty;
    private String saleBaseQty;
    private String saleAltPrice;
    private String saleBasePrice;
    private String altDiscPrice;
    private String baseDiscPrice;
    private String salesInvNo;
    private String actualAltQty;
    private String actuakBaseQty;
    @SerializedName("pre_vat")
    @Expose
    private String preVatAmt;
    @SerializedName("vat")
    @Expose
    private String vatAmt;
    @SerializedName("excies")
    @Expose
    private String exciseAmt;
    @SerializedName("net_total")
    @Expose
    private String netAmt;
    @SerializedName("item_total")
    @Expose
    private String totalAmt;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmt;
    private String discountPer;
    private String discountAlPer;
    private String discountAlAmt;
    private String discountId;
    @SerializedName("reason")
    @Expose
    private String reasonCode;
    private String returnType;
    private String isCheck;
    private String isFOCItem;
    private String focItemId;
    private String isFreeItem;
    private String isFreeApply;
    public Item focItem;
    private String inventoryId;
    private String currentDate;
    private String uomName;
    private String expiryItem;
    @SerializedName("product_type")
    @Expose
    private String productType;
    private String itemUOMType;
    private String perentItemId;
    @SerializedName("itemvalue")
    @Expose
    private String itemValue;
    private String QualitQty;
    @SerializedName("uomType")
    @Expose
    private String uomType;

    private ArrayList<UOM> arrSelectUOM;

    public ArrayList<UOM> getArrSelectUOM() {
        return arrSelectUOM;
    }

    public void setArrSelectUOM(ArrayList<UOM> arrSelectUOM) {
        this.arrSelectUOM = arrSelectUOM;
    }

    public String getQualitQty() {
        return QualitQty;
    }

    public void setQualitQty(String qualitQty) {
        QualitQty = qualitQty;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getBaseUOM() {
        return baseUOM;
    }

    public void setBaseUOM(String baseUOM) {
        this.baseUOM = baseUOM;
    }

    public String getAltrUOM() {
        return altrUOM;
    }

    public void setAltrUOM(String altrUOM) {
        this.altrUOM = altrUOM;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getUOMPrice() {
        return UOMPrice;
    }

    public void setUOMPrice(String UOMPrice) {
        this.UOMPrice = UOMPrice;
    }

    public String getAlternateUOMPrice() {
        return alternateUOMPrice;
    }

    public void setAlternateUOMPrice(String alternateUOMPrice) {
        this.alternateUOMPrice = alternateUOMPrice;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActQty() {
        return actQty;
    }

    public void setActQty(String actQty) {
        this.actQty = actQty;
    }

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }

    public String getBaseUOMQty() {
        return baseUOMQty;
    }

    public void setBaseUOMQty(String baseUOMQty) {
        this.baseUOMQty = baseUOMQty;
    }

    public String getBaseUOMPrice() {
        return baseUOMPrice;
    }

    public void setBaseUOMPrice(String baseUOMPrice) {
        this.baseUOMPrice = baseUOMPrice;
    }

    public String getAlterUOMQty() {
        return alterUOMQty;
    }

    public void setAlterUOMQty(String alterUOMQty) {
        this.alterUOMQty = alterUOMQty;
    }

    public String getAlterUOMPrice() {
        return alterUOMPrice;
    }

    public void setAlterUOMPrice(String alterUOMPrice) {
        this.alterUOMPrice = alterUOMPrice;
    }

    public String getBaseUOMName() {
        return baseUOMName;
    }

    public void setBaseUOMName(String baseUOMName) {
        this.baseUOMName = baseUOMName;
    }

    public String getAlterUOMName() {
        return alterUOMName;
    }

    public void setAlterUOMName(String alterUOMName) {
        this.alterUOMName = alterUOMName;
    }

    public String getSaleAltQty() {
        return saleAltQty;
    }

    public void setSaleAltQty(String saleAltQty) {
        this.saleAltQty = saleAltQty;
    }

    public String getSaleBaseQty() {
        return saleBaseQty;
    }

    public void setSaleBaseQty(String saleBaseQty) {
        this.saleBaseQty = saleBaseQty;
    }

    public String getSaleAltPrice() {
        return saleAltPrice;
    }

    public void setSaleAltPrice(String saleAltPrice) {
        this.saleAltPrice = saleAltPrice;
    }

    public String getSaleBasePrice() {
        return saleBasePrice;
    }

    public void setSaleBasePrice(String saleBasePrice) {
        this.saleBasePrice = saleBasePrice;
    }

    public String getSalesInvNo() {
        return salesInvNo;
    }

    public void setSalesInvNo(String salesInvNo) {
        this.salesInvNo = salesInvNo;
    }

    public String getPreVatAmt() {
        return preVatAmt;
    }

    public void setPreVatAmt(String preVatAmt) {
        this.preVatAmt = preVatAmt;
    }

    public String getVatAmt() {
        return vatAmt;
    }

    public void setVatAmt(String vatAmt) {
        this.vatAmt = vatAmt;
    }

    public String getExciseAmt() {
        return exciseAmt;
    }

    public void setExciseAmt(String exciseAmt) {
        this.exciseAmt = exciseAmt;
    }

    public String getAgentExcise() {
        return agent_excise;
    }

    public void setAgentExcise(String agent_excise) {
        this.agent_excise = agent_excise;
    }

    public String getDirectsellexcise() {
        return direct_sell_excise;
    }

    public void setDirectsellexcise(String direct_sell_excise) {
        this.direct_sell_excise = direct_sell_excise;
    }


    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(String baseVolume) {
        this.baseVolume = baseVolume;
    }

    public String getAltVolume() {
        return altVolume;
    }

    public void setAltVolume(String altVolume) {
        this.altVolume = altVolume;
    }

    public String getIsFOCItem() {
        return isFOCItem;
    }

    public void setIsFOCItem(String isFOCItem) {
        this.isFOCItem = isFOCItem;
    }

    public String getFocItemId() {
        return focItemId;
    }

    public void setFocItemId(String focItemId) {
        this.focItemId = focItemId;
    }

    public String getIsFreeItem() {
        return isFreeItem;
    }

    public void setIsFreeItem(String isFreeItem) {
        this.isFreeItem = isFreeItem;
    }

    public Item getFocItem() {
        return focItem;
    }

    public void setFocItem(Item focItem) {
        this.focItem = focItem;
    }

    public String getAltDiscPrice() {
        return altDiscPrice;
    }

    public void setAltDiscPrice(String altDiscPrice) {
        this.altDiscPrice = altDiscPrice;
    }

    public String getBaseDiscPrice() {
        return baseDiscPrice;
    }

    public void setBaseDiscPrice(String baseDiscPrice) {
        this.baseDiscPrice = baseDiscPrice;
    }

    public String getActualAltQty() {
        return actualAltQty;
    }

    public void setActualAltQty(String actualAltQty) {
        this.actualAltQty = actualAltQty;
    }

    public String getActuakBaseQty() {
        return actuakBaseQty;
    }

    public void setActuakBaseQty(String actuakBaseQty) {
        this.actuakBaseQty = actuakBaseQty;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getDiscountAlPer() {
        return discountAlPer;
    }

    public void setDiscountAlPer(String discountAlPer) {
        this.discountAlPer = discountAlPer;
    }

    public String getDiscountAlAmt() {
        return discountAlAmt;
    }

    public void setDiscountAlAmt(String discountAlAmt) {
        this.discountAlAmt = discountAlAmt;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getExpiryItem() {
        return expiryItem;
    }

    public void setExpiryItem(String expiryItem) {
        this.expiryItem = expiryItem;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getItemUOMType() {
        return itemUOMType;
    }

    public void setItemUOMType(String itemUOMType) {
        this.itemUOMType = itemUOMType;
    }

    public String getPerentItemId() {
        return perentItemId;
    }

    public void setPerentItemId(String perentItemId) {
        this.perentItemId = perentItemId;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getIsFreeApply() {
        return isFreeApply;
    }

    public void setIsFreeApply(String isFreeApply) {
        this.isFreeApply = isFreeApply;
    }

    public String getUomType() {
        return uomType;
    }

    public void setUomType(String uomType) {
        this.uomType = uomType;
    }
}