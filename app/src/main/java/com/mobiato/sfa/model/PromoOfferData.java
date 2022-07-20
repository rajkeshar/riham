package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PromoOfferData implements Parcelable {

    public String promotionId;
    public String promotionName;
    public String promoPriority;
    public String isApply;
    public String offerQty;
    public String offerUom;
    public String assignUom;
    public ArrayList<Item> orderItem;
    public ArrayList<Item> offerItem;

    public PromoOfferData() {
    }

    protected PromoOfferData(Parcel in) {
        promotionId = in.readString();
        promotionName = in.readString();
        promoPriority = in.readString();
        isApply = in.readString();
        offerQty = in.readString();
        offerUom = in.readString();
        assignUom = in.readString();
        orderItem = in.readArrayList(null);
        offerItem = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(promotionId);
        dest.writeString(promotionName);
        dest.writeString(promoPriority);
        dest.writeString(isApply);
        dest.writeString(offerQty);
        dest.writeString(offerUom);
        dest.writeString(assignUom);
        dest.writeList(orderItem);
        dest.writeList(offerItem);
    }

    @SuppressWarnings("unused")
    public static final Creator<PromoOfferData> CREATOR = new Creator<PromoOfferData>() {
        @Override
        public PromoOfferData createFromParcel(Parcel in) {
            return new PromoOfferData(in);
        }

        @Override
        public PromoOfferData[] newArray(int size) {
            return new PromoOfferData[size];
        }
    };
}
