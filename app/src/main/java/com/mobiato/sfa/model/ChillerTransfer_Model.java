package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChillerTransfer_Model implements Serializable {

    @SerializedName("depot_id")
    @Expose
    private String depot_id;

    @SerializedName("salesman_id")
    @Expose
    private String salesman_id;

    @SerializedName("route_id")
    @Expose
    private String route_id;

    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @SerializedName("contact_number")
    @Expose
    private String contact_number;

    public String getSign__customer_file() {
        return sign__customer_file;
    }

    public void setSign__customer_file(String sign__customer_file) {
        this.sign__customer_file = sign__customer_file;
    }

    @SerializedName("sign__customer_file")
    @Expose
    private String sign__customer_file;

  /*  public String getPostal_address() {
        return postal_address;
    }

    public void setPostal_address(String postal_address) {
        this.postal_address = postal_address;
    }*/

/*    @SerializedName("postal_address")
    @Expose
    private String postal_address;*/

    @SerializedName("landmark")
    @Expose
    private String landmark;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("outlet_type")
    @Expose
    private String outlet_type;

    @SerializedName("specify_if_other_type")
    @Expose
    private String specify_if_other_type;

    @SerializedName("existing_coolers")
    @Expose
    private String existing_coolers;

    @SerializedName("stock_share_with_competitor")
    @Expose
    private String stock_share_with_competitor;

    @SerializedName("outlet_weekly_sale_volume")
    @Expose
    private String outlet_weekly_sale_volume;

    @SerializedName("outlet_weekly_sales")
    @Expose
    private String outlet_weekly_sales;

    @SerializedName("asset_number")
    @Expose
    private String asset_number;

    @SerializedName("serial_number")
    @Expose
    private String serial_number;

    @SerializedName("type_of_the_machine")
    @Expose
    private String type_of_the_machine;

    public String getAsset_number() {
        return asset_number;
    }

    public void setAsset_number(String asset_number) {
        this.asset_number = asset_number;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getType_of_the_machine() {
        return type_of_the_machine;
    }

    public void setType_of_the_machine(String type_of_the_machine) {
        this.type_of_the_machine = type_of_the_machine;
    }

    public String getReason_for_withdrawal() {
        return reason_for_withdrawal;
    }

    public void setReason_for_withdrawal(String reason_for_withdrawal) {
        this.reason_for_withdrawal = reason_for_withdrawal;
    }

    public String getHistory_of_the_outlet() {
        return history_of_the_outlet;
    }

    public void setHistory_of_the_outlet(String history_of_the_outlet) {
        this.history_of_the_outlet = history_of_the_outlet;
    }

    @SerializedName("reason_for_withdrawal")
    @Expose
    private String reason_for_withdrawal;

    @SerializedName("history_of_the_outlet")
    @Expose
    private String history_of_the_outlet;

    @SerializedName("national_id_file")
    @Expose
    private String national_id_file;

    @SerializedName("national_id")
    @Expose
    private String national_id;

    @SerializedName("password_photo")
    @Expose
    private String password_photo;

    @SerializedName("outlet_stamp")
    @Expose
    private String outlet_stamp;

    @SerializedName("lc_letter")
    @Expose
    private String lc_letter;

    public String getOutlet_stamp() {
        return outlet_stamp;
    }

    public void setOutlet_stamp(String outlet_stamp) {
        this.outlet_stamp = outlet_stamp;
    }

    public String getLc_letter() {
        return lc_letter;
    }

    public void setLc_letter(String lc_letter) {
        this.lc_letter = lc_letter;
    }

    public String getTrading_licence() {
        return trading_licence;
    }

    public void setTrading_licence(String trading_licence) {
        this.trading_licence = trading_licence;
    }

    @SerializedName("trading_licence")
    @Expose
    private String trading_licence;

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getPassword_photo() {
        return password_photo;
    }

    public void setPassword_photo(String password_photo) {
        this.password_photo = password_photo;
    }

    public String getOutlet_address_proof() {
        return outlet_address_proof;
    }

    public void setOutlet_address_proof(String outlet_address_proof) {
        this.outlet_address_proof = outlet_address_proof;
    }

    @SerializedName("outlet_address_proof")
    @Expose
    private String outlet_address_proof;

    public String getLc_letter_file() {
        return lc_letter_file;
    }

    public void setLc_letter_file(String lc_letter_file) {
        this.lc_letter_file = lc_letter_file;
    }

    public String getOutlet_stamp_file() {
        return outlet_stamp_file;
    }

    public void setOutlet_stamp_file(String outlet_stamp_file) {
        this.outlet_stamp_file = outlet_stamp_file;
    }

    @SerializedName("lc_letter_file")
    @Expose
    private String lc_letter_file;

    @SerializedName("outlet_stamp_file")
    @Expose
    private String outlet_stamp_file;

    public String getTrading_licence_file() {
        return trading_licence_file;
    }

    public void setTrading_licence_file(String trading_licence_file) {
        this.trading_licence_file = trading_licence_file;
    }

    @SerializedName("password_photo_file")
    @Expose
    private String password_photo_file;


    public String getPassword_photo_file() {
        return password_photo_file;
    }

    public void setPassword_photo_file(String password_photo_file) {
        this.password_photo_file = password_photo_file;
    }

    public String getOutlet_address_proof_file() {
        return outlet_address_proof_file;
    }

    public void setOutlet_address_proof_file(String outlet_address_proof_file) {
        this.outlet_address_proof_file = outlet_address_proof_file;
    }

    @SerializedName("outlet_address_proof_file")
    @Expose
    private String outlet_address_proof_file;

    @SerializedName("trading_licence_file")
    @Expose
    private String trading_licence_file;

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    @SerializedName("owner_name")
    @Expose
    private String owner_name;

    @SerializedName("national_id1_file")
    @Expose
    private String national_id1_file;

    @SerializedName("password_photo1_file")
    @Expose
    private String password_photo1_file;

    @SerializedName("outlet_address_proof1_file")
    @Expose
    private String outlet_address_proof1_file;

    @SerializedName("trading_licence1_file")
    @Expose
    private String trading_licence1_file;

    @SerializedName("lc_letter1_file")
    @Expose
    private String lc_letter1_file;

    @SerializedName("outlet_stamp1_file")
    @Expose
    private String outlet_stamp1_file;

    public String getFridge_scan_img() {
        return fridge_scan_img;
    }

    public void setFridge_scan_img(String fridge_scan_img) {
        this.fridge_scan_img = fridge_scan_img;
    }

    @SerializedName("fridge_scan_img")
    @Expose
    private String fridge_scan_img;

    public String getOutlet_stamp1_file() {
        return outlet_stamp1_file;
    }

    public void setOutlet_stamp1_file(String outlet_stamp1_file) {
        this.outlet_stamp1_file = outlet_stamp1_file;
    }

    public String getNational_id1_file() {
        return national_id1_file;
    }

    public void setNational_id1_file(String national_id1_file) {
        this.national_id1_file = national_id1_file;
    }

    public String getPassword_photo1_file() {
        return password_photo1_file;
    }

    public void setPassword_photo1_file(String password_photo1_file) {
        this.password_photo1_file = password_photo1_file;
    }

    public String getOutlet_address_proof1_file() {
        return outlet_address_proof1_file;
    }

    public void setOutlet_address_proof1_file(String outlet_address_proof1_file) {
        this.outlet_address_proof1_file = outlet_address_proof1_file;
    }

    public String getTrading_licence1_file() {
        return trading_licence1_file;
    }

    public void setTrading_licence1_file(String trading_licence1_file) {
        this.trading_licence1_file = trading_licence1_file;
    }

    public String getLc_letter1_file() {
        return lc_letter1_file;
    }

    public void setLc_letter1_file(String lc_letter1_file) {
        this.lc_letter1_file = lc_letter1_file;
    }

    public String getNational_id_file() {
        return national_id_file;
    }

    public void setNational_id_file(String national_id_file) {
        this.national_id_file = national_id_file;
    }

    public String getdepot_id() {
        return depot_id;
    }

    public void setdepot_id(String depot_id) {
        this.depot_id = depot_id;
    }

    public String getSalesman_id() {
        return salesman_id;
    }

    public void setSalesman_id(String salesman_id) {
        this.salesman_id = salesman_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getOutlet_type() {
        return outlet_type;
    }

    public void setOutlet_type(String outlet_type) {
        this.outlet_type = outlet_type;
    }

    public String getSpecify_if_other_type() {
        return specify_if_other_type;
    }

    public void setSpecify_if_other_type(String specify_if_other_type) {
        this.specify_if_other_type = specify_if_other_type;
    }

    public String getExisting_coolers() {
        return existing_coolers;
    }

    public void setExisting_coolers(String existing_coolers) {
        this.existing_coolers = existing_coolers;
    }

    public String getStock_share_with_competitor() {
        return stock_share_with_competitor;
    }

    public void setStock_share_with_competitor(String stock_share_with_competitor) {
        this.stock_share_with_competitor = stock_share_with_competitor;
    }

    public String getOutlet_weekly_sale_volume() {
        return outlet_weekly_sale_volume;
    }

    public void setOutlet_weekly_sale_volume(String outlet_weekly_sale_volume) {
        this.outlet_weekly_sale_volume = outlet_weekly_sale_volume;
    }

    public String getOutlet_weekly_sales() {
        return outlet_weekly_sales;
    }

    public void setOutlet_weekly_sales(String outlet_weekly_sales) {
        this.outlet_weekly_sales = outlet_weekly_sales;
    }

}