package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UnloadSummary implements Parcelable {

    private String itemCode;
    private String itemId;
    private String itemName;
    private String baseUOM;
    private String alterUOM;
    private String baseLoadQty;
    private String alterLoadQTy;
    private String baseSaleQty;
    private String alterSaleQty;
    private String baseUnloadQty;
    private String alterUnloadQty;
    private String baseActQty;
    private String alterActQty;
    private String baseBadQty;
    private String alterBadQty;
    private String price;


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public String getBaseUOM() {
        return baseUOM;
    }

    public void setBaseUOM(String baseUOM) {
        this.baseUOM = baseUOM;
    }

    public String getAlterUOM() {
        return alterUOM;
    }

    public void setAlterUOM(String alterUOM) {
        this.alterUOM = alterUOM;
    }

    public String getBaseLoadQty() {
        return baseLoadQty;
    }

    public void setBaseLoadQty(String baseLoadQty) {
        this.baseLoadQty = baseLoadQty;
    }

    public String getAlterLoadQTy() {
        return alterLoadQTy;
    }

    public void setAlterLoadQTy(String alterLoadQTy) {
        this.alterLoadQTy = alterLoadQTy;
    }

    public String getBaseSaleQty() {
        return baseSaleQty;
    }

    public void setBaseSaleQty(String baseSaleQty) {
        this.baseSaleQty = baseSaleQty;
    }

    public String getAlterSaleQty() {
        return alterSaleQty;
    }

    public void setAlterSaleQty(String alterSaleQty) {
        this.alterSaleQty = alterSaleQty;
    }

    public String getBaseUnloadQty() {
        return baseUnloadQty;
    }

    public void setBaseUnloadQty(String baseUnloadQty) {
        this.baseUnloadQty = baseUnloadQty;
    }

    public String getAlterUnloadQty() {
        return alterUnloadQty;
    }

    public void setAlterUnloadQty(String alterUnloadQty) {
        this.alterUnloadQty = alterUnloadQty;
    }

    public String getBaseBadQty() {
        return baseBadQty;
    }

    public void setBaseBadQty(String baseBadQty) {
        this.baseBadQty = baseBadQty;
    }

    public String getAlterBadQty() {
        return alterBadQty;
    }

    public void setAlterBadQty(String alterBadQty) {
        this.alterBadQty = alterBadQty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBaseActQty() {
        return baseActQty;
    }

    public void setBaseActQty(String baseActQty) {
        this.baseActQty = baseActQty;
    }

    public String getAlterActQty() {
        return alterActQty;
    }

    public void setAlterActQty(String alterActQty) {
        this.alterActQty = alterActQty;
    }

    public static final Creator<UnloadSummary> CREATOR = new Creator<UnloadSummary>() {
        @Override
        public UnloadSummary createFromParcel(Parcel source) {
            UnloadSummary mSummary = new UnloadSummary();
            mSummary.itemId = source.readString();
            mSummary.itemCode = source.readString();
            mSummary.itemName = source.readString();
            mSummary.baseUOM = source.readString();
            mSummary.alterUOM = source.readString();
            mSummary.baseLoadQty = source.readString();
            mSummary.alterLoadQTy = source.readString();
            mSummary.baseSaleQty = source.readString();
            mSummary.alterSaleQty = source.readString();
            mSummary.baseUnloadQty = source.readString();
            mSummary.alterUnloadQty = source.readString();
            mSummary.baseBadQty = source.readString();
            mSummary.alterBadQty = source.readString();
            mSummary.price = source.readString();
            mSummary.baseActQty = source.readString();
            mSummary.alterActQty = source.readString();

            return mSummary;
        }

        @Override
        public UnloadSummary[] newArray(int size) {
            return new UnloadSummary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(itemId);
        parcel.writeString(itemCode);
        parcel.writeString(itemName);
        parcel.writeString(baseUOM);
        parcel.writeString(alterUOM);
        parcel.writeString(baseLoadQty);
        parcel.writeString(alterLoadQTy);
        parcel.writeString(baseSaleQty);
        parcel.writeString(alterSaleQty);
        parcel.writeString(baseUnloadQty);
        parcel.writeString(alterUnloadQty);
        parcel.writeString(baseBadQty);
        parcel.writeString(alterBadQty);
        parcel.writeString(price);
        parcel.writeString(alterActQty);
        parcel.writeString(baseActQty);
    }
}
