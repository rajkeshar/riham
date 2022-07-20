package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LoadRequest implements Parcelable {

    public String cust_no;
    public String orderNo;
    public String grossAmt;
    public String vatAmt;
    public String preVatAmt;
    public String exciseAmt;
    public String agentexciseAmt;
    public String directexciseAmt;
    public String netTotal;
    public String totalAmt;
    public String orderDate;
    public String deliveryDate;
    public String orderComment;
    public String routeId;
    public String salesmanId;

    public LoadRequest() {
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

    public String getAgentExcise() {
        return agentexciseAmt;
    }

    public void setAgentExcise(String agentexciseAmt) {
        this.agentexciseAmt = agentexciseAmt;
    }

    public String getDirectsellexcise() {
        return directexciseAmt;
    }

    public void setDirectsellexcise(String directexciseAmt) {
        this.directexciseAmt = directexciseAmt;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    protected LoadRequest(Parcel in) {
        cust_no = in.readString();
        orderNo = in.readString();
        grossAmt = in.readString();
        vatAmt = in.readString();
        preVatAmt = in.readString();
        exciseAmt = in.readString();
        netTotal = in.readString();
        totalAmt = in.readString();
        orderDate = in.readString();
        deliveryDate = in.readString();
        orderComment = in.readString();
        routeId = in.readString();
        salesmanId = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cust_no);
        dest.writeString(orderNo);
        dest.writeString(grossAmt);
        dest.writeString(vatAmt);
        dest.writeString(preVatAmt);
        dest.writeString(exciseAmt);
        dest.writeString(netTotal);
        dest.writeString(totalAmt);
        dest.writeString(orderDate);
        dest.writeString(deliveryDate);
        dest.writeString(orderComment);
        dest.writeString(routeId);
        dest.writeString(salesmanId);
    }

    @SuppressWarnings("unused")
    public static final Creator<LoadRequest> CREATOR = new Creator<LoadRequest>() {
        @Override
        public LoadRequest createFromParcel(Parcel in) {
            return new LoadRequest(in);
        }

        @Override
        public LoadRequest[] newArray(int size) {
            return new LoadRequest[size];
        }
    };
}
