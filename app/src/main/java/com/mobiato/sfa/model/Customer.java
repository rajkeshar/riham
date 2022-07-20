package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Customer implements Serializable {

    @SerializedName("cust_id")
    @Expose
    private String customerId;
    @SerializedName("cust_code")
    @Expose
    private String customerCode;
    @SerializedName("cust_name")
    @Expose
    private String customerName;
    @SerializedName("cust_name2")
    @Expose
    private String customerName2;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("cust_phone")
    @Expose
    private String custPhone;
    @SerializedName("email")
    @Expose
    private String custEmail;
    @SerializedName("payment_type")
    @Expose
    private String custType;
    @SerializedName("region_id")
    @Expose
    private String custRegion;
    @SerializedName("sub_region_id")
    @Expose
    private String custSubRegion;
    @SerializedName("division")
    @Expose
    private String custDivison;
    @SerializedName("organisataion")
    @Expose
    private String custOrg;
    @SerializedName("dist_channel")
    @Expose
    private String custChannel;
    @SerializedName("payment_term")
    @Expose
    private String paymentTerm;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("creditlimit")
    @Expose
    private String creditLimit;
    @SerializedName("threshold_radius")
    @Expose
    private String radius;
    @SerializedName("address1")
    @Expose
    private String address;
    @SerializedName("monseq")
    @Expose
    private String monSqu;
    @SerializedName("tueseq")
    @Expose
    private String tueSqu;
    @SerializedName("wedseq")
    @Expose
    private String wedSqu;
    @SerializedName("thuseq")
    @Expose
    private String thuSqu;
    @SerializedName("friseq")
    @Expose
    private String friSqu;
    @SerializedName("satseq")
    @Expose
    private String satSqu;
    @SerializedName("sunseq")
    @Expose
    private String sunSqu;
    @SerializedName("cust_type")
    @Expose
    private String customerType;
    @SerializedName("category")
    @Expose
    private String customerCategory;
    @SerializedName("salesman_id")
    @Expose
    private String salesmanId;
    @SerializedName("routeid")
    @Expose
    private String routeId;
    @SerializedName("is_fridge_assign")
    @Expose
    private String is_fridge_assign;
    @SerializedName("fridger_code")
    @Expose
    private String fridger_code;

    @SerializedName("address2")
    @Expose
    private String address2;

    @SerializedName("customerstate")
    @Expose
    private String customerstate;

    @SerializedName("customercity")
    @Expose
    private String customercity;

    @SerializedName("customerzip")
    @Expose
    private String customerzip;

    @SerializedName("customerphone1")
    @Expose
    private String customerphone1;

    @SerializedName("outlet_channel")
    @Expose
    private String customerchannel;

    @SerializedName("outlet_sub_category")
    @Expose
    private String customerSubCategory;

    public String getCustomerphone1() {
        return customerphone1;
    }

    public void setCustomerphone1(String customerphone1) {
        this.customerphone1 = customerphone1;
    }


    private String isSale;
    private String isOrder;
    private String isColl;
    private String isReturn;
    private String language;
    private String fridge;
    private String tinNo;
    private String visibility;
    private String isNew;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName2() {
        return customerName2;
    }

    public void setCustomerName2(String customerName2) {
        this.customerName2 = customerName2;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustRegion() {
        return custRegion;
    }

    public void setCustRegion(String custRegion) {
        this.custRegion = custRegion;
    }

    public String getCustSubRegion() {
        return custSubRegion;
    }

    public void setCustSubRegion(String custSubRegion) {
        this.custSubRegion = custSubRegion;
    }

    public String getCustDivison() {
        return custDivison;
    }

    public void setCustDivison(String custDivison) {
        this.custDivison = custDivison;
    }

    public String getCustOrg() {
        return custOrg;
    }

    public void setCustOrg(String custOrg) {
        this.custOrg = custOrg;
    }

    public String getCustChannel() {
        return custChannel;
    }

    public void setCustChannel(String custChannel) {
        this.custChannel = custChannel;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMonSqu() {
        return monSqu;
    }

    public void setMonSqu(String monSqu) {
        this.monSqu = monSqu;
    }

    public String getTueSqu() {
        return tueSqu;
    }

    public void setTueSqu(String tueSqu) {
        this.tueSqu = tueSqu;
    }

    public String getWedSqu() {
        return wedSqu;
    }

    public void setWedSqu(String wedSqu) {
        this.wedSqu = wedSqu;
    }

    public String getThuSqu() {
        return thuSqu;
    }

    public void setThuSqu(String thuSqu) {
        this.thuSqu = thuSqu;
    }

    public String getFriSqu() {
        return friSqu;
    }

    public void setFriSqu(String friSqu) {
        this.friSqu = friSqu;
    }

    public String getSatSqu() {
        return satSqu;
    }

    public void setSatSqu(String satSqu) {
        this.satSqu = satSqu;
    }

    public String getSunSqu() {
        return sunSqu;
    }

    public void setSunSqu(String sunSqu) {
        this.sunSqu = sunSqu;
    }

    public String getIsSale() {
        return isSale;
    }

    public void setIsSale(String isSale) {
        this.isSale = isSale;
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }

    public String getIsColl() {
        return isColl;
    }

    public void setIsColl(String isColl) {
        this.isColl = isColl;
    }

    public String getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFridge() {
        return fridge;
    }

    public void setFridge(String fridge) {
        this.fridge = fridge;
    }

    public String getTinNo() {
        return tinNo;
    }

    public void setTinNo(String tinNo) {
        this.tinNo = tinNo;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getIs_fridge_assign() {
        return is_fridge_assign;
    }

    public void setIs_fridge_assign(String is_fridge_assign) {
        this.is_fridge_assign = is_fridge_assign;
    }

    public String getFridger_code() {
        return fridger_code;
    }

    public void setFridger_code(String fridger_code) {
        this.fridger_code = fridger_code;
    }


    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }


    public String getCustomerstate() {
        return customerstate;
    }

    public void setCustomerstate(String customerstate) {
        this.customerstate = customerstate;
    }


    public String getCustomercity() {
        return customercity;
    }

    public void setCustomercity(String customercity) {
        this.customercity = customercity;
    }


    public String getCustomerzip() {
        return customerzip;
    }

    public void setCustomerzip(String customerzip) {
        this.customerzip = customerzip;
    }

    public String getCustomerchannel() {
        return customerchannel;
    }

    public void setCustomerchannel(String customerchannel) {
        this.customerchannel = customerchannel;
    }

    public String getCustomerSubCategory() {
        return customerSubCategory;
    }

    public void setCustomerSubCategory(String customerSubCategory) {
        this.customerSubCategory = customerSubCategory;
    }
}