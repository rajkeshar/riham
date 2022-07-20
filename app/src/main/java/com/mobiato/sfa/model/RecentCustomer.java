package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RecentCustomer implements Parcelable {

    String customer_id;
    String customer_name;
    String date_time;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customer_id);
        dest.writeString(this.customer_name);
        dest.writeString(this.date_time);
    }

    public RecentCustomer() {
    }

    protected RecentCustomer(Parcel in) {
        this.customer_id = in.readString();
        this.customer_name = in.readString();
        this.date_time = in.readString();
    }

    public static final Creator<RecentCustomer> CREATOR = new Creator<RecentCustomer>() {
        @Override
        public RecentCustomer createFromParcel(Parcel source) {
            return new RecentCustomer(source);
        }

        @Override
        public RecentCustomer[] newArray(int size) {
            return new RecentCustomer[size];
        }
    };
}
