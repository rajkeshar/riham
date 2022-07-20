package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Himm on 3/13/2018.
 */

public class Order implements Parcelable {

    public String cust_id;
    public String orderNo;
    public String orderDate;
    public String deliveyDate;
    public String vatAmt;
    public String preVatAmt;
    public String exciseAmt;
    public String netAmt;
    public String orderAmt;
    public String salesmanId;
    public String orderComment;


    public Order() {
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getVatAmt() {
        return vatAmt;
    }

    public void setVatAmt(String vatAmt) {
        this.vatAmt = vatAmt;
    }

    public String getPreVatAmt() {
        return preVatAmt;
    }

    public void setPreVatAmt(String preVatAmt) {
        this.preVatAmt = preVatAmt;
    }

    public String getExciseAmt() {
        return exciseAmt;
    }

    public void setExciseAmt(String exciseAmt) {
        this.exciseAmt = exciseAmt;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getDeliveyDate() {
        return deliveyDate;
    }

    public void setDeliveyDate(String deliveyDate) {
        this.deliveyDate = deliveyDate;
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    protected Order(Parcel in) {
        cust_id = in.readString();
        orderNo = in.readString();
        orderDate = in.readString();
        orderAmt = in.readString();
        vatAmt = in.readString();
        preVatAmt = in.readString();
        exciseAmt = in.readString();
        netAmt = in.readString();
        deliveyDate = in.readString();
        salesmanId = in.readString();
        orderComment = in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cust_id);
        dest.writeString(orderNo);
        dest.writeString(orderDate);
        dest.writeString(orderAmt);
        dest.writeString(vatAmt);
        dest.writeString(preVatAmt);
        dest.writeString(exciseAmt);
        dest.writeString(netAmt);
        dest.writeString(deliveyDate);
        dest.writeString(salesmanId);
        dest.writeString(orderComment);

    }

    @SuppressWarnings("unused")
    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}