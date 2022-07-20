package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Category implements Parcelable {

    public String name;
    public String catId;
    public String is_click;

    public Category() {
    }

    protected Category(Parcel in) {
        catId = in.readString();
        name = in.readString();
        is_click = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(catId);
        dest.writeString(name);
        dest.writeString(is_click);
    }

    @SuppressWarnings("unused")
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}

