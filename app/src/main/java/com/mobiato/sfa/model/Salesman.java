package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Salesman implements Serializable {

    @SerializedName("salesman_id")
    @Expose
    private String salesmanId;
    @SerializedName("salesmancode")
    @Expose
    private String salesmanCode;
    @SerializedName("salesmanname")
    @Expose
    private String salesmanName;
    @SerializedName("salesmanname2")
    @Expose
    private String salesmanName2;
    @SerializedName("channel")
    @Expose
    private String dist_channel;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("organisation")
    @Expose
    private String organisation;
    @SerializedName("routeid")
    @Expose
    private String route;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("sub_region")
    @Expose
    private String subRegion;
    @SerializedName("routename")
    @Expose
    private String routeName;
    @SerializedName("vehiclenumber")
    @Expose
    private String vehicle_no;
    @SerializedName("contact")
    @Expose
    private String contactNo;
    @SerializedName("depot")
    @Expose
    private String depot;
    @SerializedName("salesman_type")
    @Expose
    private String type;
    @SerializedName("date_joining")
    @Expose
    private String dateOfJoin;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("team_lead")
    @Expose
    private String teamLead;
    @SerializedName("agent_id")
    @Expose
    private String agent;
    @SerializedName("supervisor_id")
    @Expose
    private String supervisorId;
    @SerializedName("supervisor_name")
    @Expose
    private String supervisorName;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("depot_barcode")
    @Expose
    private String depotBarcode;
    @SerializedName("lattitude")
    @Expose
    private String depotLatitude;
    @SerializedName("longitude")
    @Expose
    private String depotLongitude;
    @SerializedName("depot_threshold_radius")
    @Expose
    private String threshold_ORG;
    @SerializedName("manual_discount_flag")
    @Expose
    private String discountFlag;
    @SerializedName("enforce_sequence")
    @Expose
    private String enforceSeq;
    @SerializedName("Invoice_LastNo")
    @Expose
    private String invLast;
    @SerializedName("Order_LastNo")
    @Expose
    private String orderLast;
    @SerializedName("Collection_LastNo")
    @Expose
    private String collectionLast;
    @SerializedName("Retrun_LastNo")
    @Expose
    private String returnLast;
    @SerializedName("Load_Request_LastNo")
    @Expose
    private String loadLast;
    @SerializedName("Unload_LastNo")
    @Expose
    private String unLoadLast;
    @SerializedName("Customer_LastNo")
    @Expose
    private String customerLast;
    @SerializedName("Exchange_LastNo")
    @Expose
    private String exchangeLast;
    @SerializedName("depot_name")
    @Expose
    private String depotName;
    @SerializedName("depot_tin_no")
    @Expose
    private String depotTIN;
    @SerializedName("depot_city")
    @Expose
    private String depotCity;
    @SerializedName("depot_village")
    @Expose
    private String depotVillage;
    @SerializedName("depot_street")
    @Expose
    private String depotStreet;
    @SerializedName("agent_contactno")
    @Expose
    private String depotPhone;
    @SerializedName("sales_target")
    @Expose
    private String salesTarget;
    @SerializedName("achive_target")
    @Expose
    private String achiveTarget;
    @SerializedName("sale_target_csd")
    @Expose
    private String CSDTarget;
    @SerializedName("achive_target_csd")
    @Expose
    private String achiveCSDTarget;
    @SerializedName("sale_target_juice")
    @Expose
    private String JuiceTarget;
    @SerializedName("achive_target_juice")
    @Expose
    private String achiveJuiceTarget;
    @SerializedName("sale_target_water")
    @Expose
    private String WaterTarget;
    @SerializedName("achive_target_water")
    @Expose
    private String achiveWaterTarget;
    @SerializedName("sale_target_biscuit")
    @Expose
    private String BiscutsTarget;
    @SerializedName("achive_target_biscuit")
    @Expose
    private String achiveBiscutsTarget;
    @SerializedName("associated_salesman")
    @Expose
    private String assignRoute;

    @SerializedName("achive_target_hampar")
    @Expose
    private String achiveHamperTarget;

    @SerializedName("get_attendance")
    @Expose
    private String get_attendance;

    @SerializedName("get_customer_file")
    @Expose
    private String customer_file;

    @SerializedName("get_item_file")
    @Expose
    private String item_file;

    @SerializedName("get_pricingplan_file")
    @Expose
    private String price_file;

    @SerializedName("total_customers")
    @Expose
    private String total_customers;

    @SerializedName("total_pricingplan")
    @Expose
    private String total_pricingplan;

    @SerializedName("projectid")
    @Expose
    private String projectid;

    public String getAchiveConfectionaryTarget() {
        return achiveConfectionaryTarget;
    }

    public void setAchiveConfectionaryTarget(String achiveConfectionaryTarget) {
        this.achiveConfectionaryTarget = achiveConfectionaryTarget;
    }

    public String getHamperConfectionary() {
        return HamperConfectionary;
    }

    public void setHamperConfectionary(String hamperConfectionary) {
        HamperConfectionary = hamperConfectionary;
    }

    @SerializedName("sale_target_hampar")
    @Expose
    private String HamperTarget;

    @SerializedName("achive_target_confectionary")
    @Expose
    private String achiveConfectionaryTarget;

    @SerializedName("sale_target_confectionary")
    @Expose
    private String HamperConfectionary;

    @SerializedName("is_price_update")
    @Expose
    private String isPriceUpdate;

    @SerializedName("projectname")
    @Expose
    private String projectname;

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getSalesmanCode() {
        return salesmanCode;
    }

    public void setSalesmanCode(String salesmanCode) {
        this.salesmanCode = salesmanCode;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getSalesmanName2() {
        return salesmanName2;
    }

    public void setSalesmanName2(String salesmanName2) {
        this.salesmanName2 = salesmanName2;
    }

    public String getDist_channel() {
        return dist_channel;
    }

    public void setDist_channel(String dist_channel) {
        this.dist_channel = dist_channel;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(String dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(String teamLead) {
        this.teamLead = teamLead;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepotBarcode() {
        return depotBarcode;
    }

    public void setDepotBarcode(String depotBarcode) {
        this.depotBarcode = depotBarcode;
    }

    public String getDepotLatitude() {
        return depotLatitude;
    }

    public void setDepotLatitude(String depotLatitude) {
        this.depotLatitude = depotLatitude;
    }

    public String getDepotLongitude() {
        return depotLongitude;
    }

    public void setDepotLongitude(String depotLongitude) {
        this.depotLongitude = depotLongitude;
    }

    public String getThreshold_ORG() {
        return threshold_ORG;
    }

    public void setThreshold_ORG(String threshold_ORG) {
        this.threshold_ORG = threshold_ORG;
    }

    public String getDiscountFlag() {
        return discountFlag;
    }

    public void setDiscountFlag(String discountFlag) {
        this.discountFlag = discountFlag;
    }

    public String getEnforceSeq() {
        return enforceSeq;
    }

    public void setEnforceSeq(String enforceSeq) {
        this.enforceSeq = enforceSeq;
    }

    public String getInvLast() {
        return invLast;
    }

    public void setInvLast(String invLast) {
        this.invLast = invLast;
    }

    public String getOrderLast() {
        return orderLast;
    }

    public void setOrderLast(String orderLast) {
        this.orderLast = orderLast;
    }

    public String getCollectionLast() {
        return collectionLast;
    }

    public void setCollectionLast(String collectionLast) {
        this.collectionLast = collectionLast;
    }

    public String getReturnLast() {
        return returnLast;
    }

    public void setReturnLast(String returnLast) {
        this.returnLast = returnLast;
    }

    public String getLoadLast() {
        return loadLast;
    }

    public void setLoadLast(String loadLast) {
        this.loadLast = loadLast;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getUnLoadLast() {
        return unLoadLast;
    }

    public void setUnLoadLast(String unLoadLast) {
        this.unLoadLast = unLoadLast;
    }

    public String getCustomerLast() {
        return customerLast;
    }

    public void setCustomerLast(String customerLast) {
        this.customerLast = customerLast;
    }

    public String getExchangeLast() {
        return exchangeLast;
    }

    public void setExchangeLast(String exchangeLast) {
        this.exchangeLast = exchangeLast;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getDepotTIN() {
        return depotTIN;
    }

    public void setDepotTIN(String depotTIN) {
        this.depotTIN = depotTIN;
    }

    public String getDepotCity() {
        return depotCity;
    }

    public void setDepotCity(String depotCity) {
        this.depotCity = depotCity;
    }

    public String getDepotVillage() {
        return depotVillage;
    }

    public void setDepotVillage(String depotVillage) {
        this.depotVillage = depotVillage;
    }

    public String getDepotStreet() {
        return depotStreet;
    }

    public void setDepotStreet(String depotStreet) {
        this.depotStreet = depotStreet;
    }

    public String getDepotPhone() {
        return depotPhone;
    }

    public void setDepotPhone(String depotPhone) {
        this.depotPhone = depotPhone;
    }

    public String getSalesTarget() {
        return salesTarget;
    }

    public void setSalesTarget(String salesTarget) {
        this.salesTarget = salesTarget;
    }

    public String getAchiveTarget() {
        return achiveTarget;
    }

    public void setAchiveTarget(String achiveTarget) {
        this.achiveTarget = achiveTarget;
    }

    public String getCSDTarget() {
        return CSDTarget;
    }

    public void setCSDTarget(String CSDTarget) {
        this.CSDTarget = CSDTarget;
    }

    public String getJuiceTarget() {
        return JuiceTarget;
    }

    public void setJuiceTarget(String juiceTarget) {
        JuiceTarget = juiceTarget;
    }

    public String getWaterTarget() {
        return WaterTarget;
    }

    public void setWaterTarget(String waterTarget) {
        WaterTarget = waterTarget;
    }

    public String getBiscutsTarget() {
        return BiscutsTarget;
    }

    public void setBiscutsTarget(String biscutsTarget) {
        BiscutsTarget = biscutsTarget;
    }

    public String getHamperTarget() {
        return HamperTarget;
    }

    public void setHamperTarget(String HamperTarget) {
        HamperTarget = HamperTarget;
    }

    public String getAchiveCSDTarget() {
        return achiveCSDTarget;
    }

    public void setAchiveCSDTarget(String achiveCSDTarget) {
        this.achiveCSDTarget = achiveCSDTarget;
    }

    public String getAchiveJuiceTarget() {
        return achiveJuiceTarget;
    }

    public void setAchiveJuiceTarget(String achiveJuiceTarget) {
        this.achiveJuiceTarget = achiveJuiceTarget;
    }

    public String getAchiveWaterTarget() {
        return achiveWaterTarget;
    }

    public void setAchiveWaterTarget(String achiveWaterTarget) {
        this.achiveWaterTarget = achiveWaterTarget;
    }

    public String getAchiveHamperTarget() {
        return achiveHamperTarget;
    }

    public void setAchiveHamperTarget(String achiveHamperTarget) {
        this.achiveHamperTarget = achiveHamperTarget;
    }

    public String getAchiveBiscutsTarget() {
        return achiveBiscutsTarget;
    }

    public void setAchiveBiscutsTarget(String achiveBiscutsTarget) {
        this.achiveBiscutsTarget = achiveBiscutsTarget;
    }

    public String getAssignRoute() {
        return assignRoute;
    }

    public void setAssignRoute(String assignRoute) {
        this.assignRoute = assignRoute;
    }

    public String getIsPriceUpdate() {
        return isPriceUpdate;
    }

    public void setIsPriceUpdate(String isPriceUpdate) {
        this.isPriceUpdate = isPriceUpdate;
    }

    public String getGet_attendance() {
        return get_attendance;
    }

    public void setGet_attendance(String get_attendance) {
        this.get_attendance = get_attendance;
    }

    public String getCustomer_file() {
        return customer_file;
    }

    public void setCustomer_file(String customer_file) {
        this.customer_file = customer_file;
    }

    public String getItem_file() {
        return item_file;
    }

    public void setItem_file(String item_file) {
        this.item_file = item_file;
    }

    public String getPrice_file() {
        return price_file;
    }

    public void setPrice_file(String price_file) {
        this.price_file = price_file;
    }

    public String getTotal_customers() {
        return total_customers;
    }

    public void setTotal_customers(String total_customers) {
        this.total_customers = total_customers;
    }

    public String getTotal_pricingplan() {
        return total_pricingplan;
    }

    public void setTotal_pricingplan(String total_pricingplan) {
        this.total_pricingplan = total_pricingplan;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }
}
