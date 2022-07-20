package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Himm on 3/13/2018.
 */

public class ReturnOrder implements Parcelable {

    public String cust_no;
    public String orderId;
    public String orderNo;
    public String orderDate;
    public String grossAmt;
    public String vatAmt;
    public String preVatAmt;
    public String exciseAmt;
    public String netTotal;
    public String totalAmt;
    public String returnType;
    public String exchangeNo;
    public String isReturnRequest;
    public String isPosted;
    public String salesmanId;


    public ReturnOrder() {
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
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

    public String getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(String grossAmt) {
        this.grossAmt = grossAmt;
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

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getExchangeNo() {
        return exchangeNo;
    }

    public void setExchangeNo(String exchangeNo) {
        this.exchangeNo = exchangeNo;
    }

    public String getIsReturnRequest() {
        return isReturnRequest;
    }

    public void setIsReturnRequest(String isReturnRequest) {
        this.isReturnRequest = isReturnRequest;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    protected ReturnOrder(Parcel in) {
        cust_no = in.readString();
        orderId = in.readString();
        orderNo = in.readString();
        orderDate = in.readString();
        grossAmt = in.readString();
        vatAmt = in.readString();
        preVatAmt = in.readString();
        exciseAmt = in.readString();
        netTotal = in.readString();
        totalAmt = in.readString();
        returnType = in.readString();
        exchangeNo = in.readString();
        isReturnRequest = in.readString();
        isPosted = in.readString();
        salesmanId = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cust_no);
        dest.writeString(orderId);
        dest.writeString(orderNo);
        dest.writeString(orderDate);
        dest.writeString(grossAmt);
        dest.writeString(vatAmt);
        dest.writeString(preVatAmt);
        dest.writeString(exciseAmt);
        dest.writeString(netTotal);
        dest.writeString(totalAmt);
        dest.writeString(returnType);
        dest.writeString(exchangeNo);
        dest.writeString(isReturnRequest);
        dest.writeString(isPosted);
        dest.writeString(salesmanId);
    }

    @SuppressWarnings("unused")
    public static final Creator<ReturnOrder> CREATOR = new Creator<ReturnOrder>() {
        @Override
        public ReturnOrder createFromParcel(Parcel in) {
            return new ReturnOrder(in);
        }

        @Override
        public ReturnOrder[] newArray(int size) {
            return new ReturnOrder[size];
        }
    };
}