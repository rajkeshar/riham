package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesVo {

    @SerializedName("trip_id")
    @Expose
    private String tripId;
    @SerializedName("salesman_id")
    @Expose
    private String salesmanId;
    @SerializedName("salesman_name_en")
    @Expose
    private String salesmanNameEn;
    @SerializedName("salesman_name_ar")
    @Expose
    private String salesmanNameAr;
    @SerializedName("salesman_dis_channel")
    @Expose
    private String salesmanDisChannel;
    @SerializedName("salesman_org")
    @Expose
    private String salesmanOrg;
    @SerializedName("salesman_division")
    @Expose
    private String salesmanDivision;
    @SerializedName("salesman_route")
    @Expose
    private String salesmanRoute;
    @SerializedName("salesman_vehicle_no")
    @Expose
    private String salesmanVehicleNo;
    @SerializedName("supervisor_id")
    @Expose
    private String supervisorId;
    @SerializedName("INV_LAST")
    @Expose
    private String iNVLAST;
    @SerializedName("ORD_LAST")
    @Expose
    private String oRDLAST;
    @SerializedName("LOAD_LAST")
    @Expose
    private String lOADLAST;
    @SerializedName("COLLECTION_LAST")
    @Expose
    private String cOLLECTIONLAST;
    @SerializedName("CUSTOMER_LAST")
    @Expose
    private String cUSTOMERLAST;
    @SerializedName("PAYMENT_LAST")
    @Expose
    private String pAYMENTLAST;
    @SerializedName("LOAD_REQUEST_LAST")
    @Expose
    private String lOADREQUESTLAST;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getSalesmanNameEn() {
        return salesmanNameEn;
    }

    public void setSalesmanNameEn(String salesmanNameEn) {
        this.salesmanNameEn = salesmanNameEn;
    }

    public String getSalesmanNameAr() {
        return salesmanNameAr;
    }

    public void setSalesmanNameAr(String salesmanNameAr) {
        this.salesmanNameAr = salesmanNameAr;
    }

    public String getSalesmanDisChannel() {
        return salesmanDisChannel;
    }

    public void setSalesmanDisChannel(String salesmanDisChannel) {
        this.salesmanDisChannel = salesmanDisChannel;
    }

    public String getSalesmanOrg() {
        return salesmanOrg;
    }

    public void setSalesmanOrg(String salesmanOrg) {
        this.salesmanOrg = salesmanOrg;
    }

    public String getSalesmanDivision() {
        return salesmanDivision;
    }

    public void setSalesmanDivision(String salesmanDivision) {
        this.salesmanDivision = salesmanDivision;
    }

    public String getSalesmanRoute() {
        return salesmanRoute;
    }

    public void setSalesmanRoute(String salesmanRoute) {
        this.salesmanRoute = salesmanRoute;
    }

    public String getSalesmanVehicleNo() {
        return salesmanVehicleNo;
    }

    public void setSalesmanVehicleNo(String salesmanVehicleNo) {
        this.salesmanVehicleNo = salesmanVehicleNo;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getINVLAST() {
        return iNVLAST;
    }

    public void setINVLAST(String iNVLAST) {
        this.iNVLAST = iNVLAST;
    }

    public String getORDLAST() {
        return oRDLAST;
    }

    public void setORDLAST(String oRDLAST) {
        this.oRDLAST = oRDLAST;
    }

    public String getLOADLAST() {
        return lOADLAST;
    }

    public void setLOADLAST(String lOADLAST) {
        this.lOADLAST = lOADLAST;
    }

    public String getCOLLECTIONLAST() {
        return cOLLECTIONLAST;
    }

    public void setCOLLECTIONLAST(String cOLLECTIONLAST) {
        this.cOLLECTIONLAST = cOLLECTIONLAST;
    }

    public String getCUSTOMERLAST() {
        return cUSTOMERLAST;
    }

    public void setCUSTOMERLAST(String cUSTOMERLAST) {
        this.cUSTOMERLAST = cUSTOMERLAST;
    }

    public String getPAYMENTLAST() {
        return pAYMENTLAST;
    }

    public void setPAYMENTLAST(String pAYMENTLAST) {
        this.pAYMENTLAST = pAYMENTLAST;
    }

    public String getLOADREQUESTLAST() {
        return lOADREQUESTLAST;
    }

    public void setLOADREQUESTLAST(String lOADREQUESTLAST) {
        this.lOADREQUESTLAST = lOADREQUESTLAST;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private SalesVo() {
    }
}
