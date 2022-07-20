package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerTechnician implements Serializable {


    @SerializedName("customer_id")
    @Expose
    private String customer_id;

    @SerializedName("crf_id")
    @Expose
    private String crf_id;

    @SerializedName("ccode")
    @Expose
    private String ccode;
    
    @SerializedName("customername")
    @Expose
    private String customername;
    
    @SerializedName("contact_number")
    @Expose
    private String contact_number;

    @SerializedName("postal_address")
    @Expose
    private String postal_address;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("landmark")
    @Expose
    private String landmark;
    
    @SerializedName("outlet_type")
    @Expose
    private String outlet_type;

    @SerializedName("agent_name")
    @Expose
    private String agent_name;

    private String isReturn;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("outlet_name")
    @Expose
    private String outlet_name;
    @SerializedName("existing_coolers")
    @Expose
    private String existing_coolers;
    @SerializedName("outlet_weekly_sale_volume")
    @Expose
    private String outlet_weekly_sale_volume;
    @SerializedName("display_location")
    @Expose
    private String display_location;
    @SerializedName("chiller_safty_grill")
    @Expose
    private String chiller_safty_grill;
    @SerializedName("agent")
    @Expose
    private String agent;

    @SerializedName("owner_name")
    @Expose
    private String owner_name;

    @SerializedName("salesman_name")
    @Expose
    private String salesman_name;

    public String getSalesman_name() {
        return salesman_name;
    }

    public void setSalesman_name(String salesman_name) {
        this.salesman_name = salesman_name;
    }

    public String getSalesman_contact() {
        return salesman_contact;
    }

    public void setSalesman_contact(String salesman_contact) {
        this.salesman_contact = salesman_contact;
    }

    @SerializedName("salesman_contact")
    @Expose
    private String salesman_contact;

    public String getSign__customer_file() {
        return sign__customer_file;
    }

    public void setSign__customer_file(String sign__customer_file) {
        this.sign__customer_file = sign__customer_file;
    }

    @SerializedName("sign__customer_file")
    @Expose
    private String sign__customer_file;

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    @SerializedName("manager_sales_marketing")
    @Expose
    private String manager_sales_marketing;
    @SerializedName("national_id")
    @Expose
    private String national_id;
    @SerializedName("outlet_stamp")
    @Expose
    private String outlet_stamp;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("hil")
    @Expose
    private String hil;
    @SerializedName("ir_reference_no")
    @Expose
    private String ir_reference_no;
    @SerializedName("installation_done_by")
    @Expose
    private String installation_done_by;
    @SerializedName("date_lnitial")
    @Expose
    private String date_lnitial;
    @SerializedName("date_lnitial2")
    @Expose
    private String date_lnitial2;
    @SerializedName("contract_attached")
    @Expose
    private String contract_attached;
    @SerializedName("machine_number")
    @Expose
    private String machine_number;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("asset_number")
    @Expose
    private String asset_number;
    @SerializedName("lc_letter")
    @Expose
    private String lc_letter;
    @SerializedName("trading_licence")
    @Expose
    private String trading_licence;

    @SerializedName("password_photo")
    @Expose
    private String password_photo;

    @SerializedName("outlet_address_proof")
    @Expose
    private String outlet_address_proof;

    @SerializedName("chiller_asset_care_manager")
    @Expose
    private String chiller_asset_care_manager;
    @SerializedName("national_id_file")
    @Expose
    private String national_id_file;
    @SerializedName("password_photo_file")
    @Expose
    private String password_photo_file;
    @SerializedName("outlet_address_proof_file")
    @Expose
    private String outlet_address_proof_file;
    @SerializedName("trading_licence_file")
    @Expose
    private String trading_licence_file;
    @SerializedName("lc_letter_file")
    @Expose
    private String lc_letter_file;
    @SerializedName("outlet_stamp_file")
    @Expose
    private String outlet_stamp_file;
    @SerializedName("sales_marketing_director")
    @Expose
    private String sales_marketing_director;
    @SerializedName("agent_id")
    @Expose
    private String agent_id;
    @SerializedName("area_manager")
    @Expose
    private String area_manager;
    @SerializedName("name_contact_of_the_customer")
    @Expose
    private String name_contact_of_the_customer;
    @SerializedName("chiller_size_requested")
    @Expose
    private String chiller_size_requested;
    @SerializedName("outlet_weekly_sales")
    @Expose
    private String outlet_weekly_sales;
    @SerializedName("stock_share_with_competitor")
    @Expose
    private String stock_share_with_competitor;
    @SerializedName("specify_if_other_type")
    @Expose
    private String specify_if_other_type;
    @SerializedName("customer_name")
    @Expose
    private String customer_name;
    @SerializedName("created_date")
    @Expose
    private String created_date;
    @SerializedName("sales_excutive")
    @Expose
    private String sales_excutive;
    @SerializedName("salesman_id")
    @Expose
    private String salesman_id;
    @SerializedName("route_id")
    @Expose
    private String route_id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("fridge_status")
    @Expose
    private String fridge_status;
    @SerializedName("remark")
    @Expose
    private String remark;


    // Getter Methods


    public String getId() {
        return id;
    }

    public String getOutlet_name() {
        return outlet_name;
    }


    public String getExisting_coolers() {
        return existing_coolers;
    }

    public String getOutlet_weekly_sale_volume() {
        return outlet_weekly_sale_volume;
    }

    public String getDisplay_location() {
        return display_location;
    }

    public String getChiller_safty_grill() {
        return chiller_safty_grill;
    }

    public String getAgent() {
        return agent;
    }

    public String getManager_sales_marketing() {
        return manager_sales_marketing;
    }

    public String getNational_id() {
        return national_id;
    }

    public String getOutlet_stamp() {
        return outlet_stamp;
    }

    public String getModel() {
        return model;
    }

    public String getHil() {
        return hil;
    }

    public String getIr_reference_no() {
        return ir_reference_no;
    }

    public String getInstallation_done_by() {
        return installation_done_by;
    }

    public String getDate_lnitial() {
        return date_lnitial;
    }

    public String getDate_lnitial2() {
        return date_lnitial2;
    }

    public String getContract_attached() {
        return contract_attached;
    }

    public String getMachine_number() {
        return machine_number;
    }

    public String getBrand() {
        return brand;
    }

    public String getAsset_number() {
        return asset_number;
    }

    public String getLc_letter() {
        return lc_letter;
    }

    public String getTrading_licence() {
        return trading_licence;
    }

    public String getPassword_photo() {
        return password_photo;
    }

    public String getOutlet_address_proof() {
        return outlet_address_proof;
    }

    public String getChiller_asset_care_manager() {
        return chiller_asset_care_manager;
    }

    public String getNational_id_file() {
        return national_id_file;
    }

    public String getPassword_photo_file() {
        return password_photo_file;
    }

    public String getOutlet_address_proof_file() {
        return outlet_address_proof_file;
    }

    public String getTrading_licence_file() {
        return trading_licence_file;
    }

    public String getLc_letter_file() {
        return lc_letter_file;
    }

    public String getOutlet_stamp_file() {
        return outlet_stamp_file;
    }

    public String getSales_marketing_director() {
        return sales_marketing_director;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public String getArea_manager() {
        return area_manager;
    }

    public String getName_contact_of_the_customer() {
        return name_contact_of_the_customer;
    }

    public String getChiller_size_requested() {
        return chiller_size_requested;
    }

    public String getOutlet_weekly_sales() {
        return outlet_weekly_sales;
    }

    public String getStock_share_with_competitor() {
        return stock_share_with_competitor;
    }

    public String getSpecify_if_other_type() {
        return specify_if_other_type;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getSales_excutive() {
        return sales_excutive;
    }

    public String getSalesman_id() {
        return salesman_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getStatus() {
        return status;
    }

    public String getFridge_status() {
        return fridge_status;
    }

    public String getRemark() {
        return remark;
    }



    // Setter Methods

    public void setId( String id ) {
        this.id = id;
    }

    public void setOutlet_name( String outlet_name ) {
        this.outlet_name = outlet_name;
    }


    public void setExisting_coolers( String existing_coolers ) {
        this.existing_coolers = existing_coolers;
    }

    public void setOutlet_weekly_sale_volume( String outlet_weekly_sale_volume ) {
        this.outlet_weekly_sale_volume = outlet_weekly_sale_volume;
    }

    public void setDisplay_location( String display_location ) {
        this.display_location = display_location;
    }

    public void setChiller_safty_grill( String chiller_safty_grill ) {
        this.chiller_safty_grill = chiller_safty_grill;
    }

    public void setAgent( String agent ) {
        this.agent = agent;
    }

    public void setManager_sales_marketing( String manager_sales_marketing ) {
        this.manager_sales_marketing = manager_sales_marketing;
    }

    public void setNational_id( String national_id ) {
        this.national_id = national_id;
    }

    public void setOutlet_stamp( String outlet_stamp ) {
        this.outlet_stamp = outlet_stamp;
    }

    public void setModel( String model ) {
        this.model = model;
    }

    public void setHil( String hil ) {
        this.hil = hil;
    }

    public void setIr_reference_no( String ir_reference_no ) {
        this.ir_reference_no = ir_reference_no;
    }

    public void setInstallation_done_by( String installation_done_by ) {
        this.installation_done_by = installation_done_by;
    }

    public void setDate_lnitial( String date_lnitial ) {
        this.date_lnitial = date_lnitial;
    }

    public void setDate_lnitial2( String date_lnitial2 ) {
        this.date_lnitial2 = date_lnitial2;
    }

    public void setContract_attached( String contract_attached ) {
        this.contract_attached = contract_attached;
    }

    public void setMachine_number( String machine_number ) {
        this.machine_number = machine_number;
    }

    public void setBrand( String brand ) {
        this.brand = brand;
    }

    public void setAsset_number( String asset_number ) {
        this.asset_number = asset_number;
    }

    public void setLc_letter( String lc_letter ) {
        this.lc_letter = lc_letter;
    }

    public void setTrading_licence( String trading_licence ) {
        this.trading_licence = trading_licence;
    }

    public void setPassword_photo( String password_photo ) {
        this.password_photo = password_photo;
    }

    public void setOutlet_address_proof( String outlet_address_proof ) {
        this.outlet_address_proof = outlet_address_proof;
    }

    public void setChiller_asset_care_manager( String chiller_asset_care_manager ) {
        this.chiller_asset_care_manager = chiller_asset_care_manager;
    }

    public void setNational_id_file( String national_id_file ) {
        this.national_id_file = national_id_file;
    }

    public void setPassword_photo_file( String password_photo_file ) {
        this.password_photo_file = password_photo_file;
    }

    public void setOutlet_address_proof_file( String outlet_address_proof_file ) {
        this.outlet_address_proof_file = outlet_address_proof_file;
    }

    public void setTrading_licence_file( String trading_licence_file ) {
        this.trading_licence_file = trading_licence_file;
    }

    public void setLc_letter_file( String lc_letter_file ) {
        this.lc_letter_file = lc_letter_file;
    }

    public void setOutlet_stamp_file( String outlet_stamp_file ) {
        this.outlet_stamp_file = outlet_stamp_file;
    }

    public void setSales_marketing_director( String sales_marketing_director ) {
        this.sales_marketing_director = sales_marketing_director;
    }

    public void setAgent_id( String agent_id ) {
        this.agent_id = agent_id;
    }

    public void setArea_manager( String area_manager ) {
        this.area_manager = area_manager;
    }

    public void setName_contact_of_the_customer( String name_contact_of_the_customer ) {
        this.name_contact_of_the_customer = name_contact_of_the_customer;
    }

    public void setChiller_size_requested( String chiller_size_requested ) {
        this.chiller_size_requested = chiller_size_requested;
    }

    public void setOutlet_weekly_sales( String outlet_weekly_sales ) {
        this.outlet_weekly_sales = outlet_weekly_sales;
    }

    public void setStock_share_with_competitor( String stock_share_with_competitor ) {
        this.stock_share_with_competitor = stock_share_with_competitor;
    }

    public void setSpecify_if_other_type( String specify_if_other_type ) {
        this.specify_if_other_type = specify_if_other_type;
    }


    public void setCustomer_name( String customer_name ) {
        this.customer_name = customer_name;
    }

    public void setCreated_date( String created_date ) {
        this.created_date = created_date;
    }

    public void setSales_excutive( String sales_excutive ) {
        this.sales_excutive = sales_excutive;
    }

    public void setSalesman_id( String salesman_id ) {
        this.salesman_id = salesman_id;
    }

    public void setRoute_id( String route_id ) {
        this.route_id = route_id;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public void setFridge_status( String fridge_status ) {
        this.fridge_status = fridge_status;
    }

    public void setRemark( String remark ) {
        this.remark = remark;
    }

//end

    public String getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }

    public String getCrf_id() {
        return crf_id;
    }

    public void setCrf_id(String crf_id) {
        this.crf_id = crf_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostal_address() {
        return postal_address;
    }

    public void setPostal_address(String postal_address) {
        this.postal_address = postal_address;
    }

    public String getOutlet_type() {
        return outlet_type;
    }

    public void setOutlet_type(String outlet_type) {
        this.outlet_type = outlet_type;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }
}