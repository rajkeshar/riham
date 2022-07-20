package com.mobiato.sfa.localdb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mobiato.sfa.App;
import com.mobiato.sfa.model.ASSETS_MODEL;
import com.mobiato.sfa.model.AddChiller;
import com.mobiato.sfa.model.Category;
import com.mobiato.sfa.model.ChannellType;
import com.mobiato.sfa.model.ChillerTechnician;
import com.mobiato.sfa.model.Chiller_Model;
import com.mobiato.sfa.model.Collection;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.Compaign;
import com.mobiato.sfa.model.Compititor;
import com.mobiato.sfa.model.Complain;
import com.mobiato.sfa.model.Customer;
import com.mobiato.sfa.model.CustomerTechnician;
import com.mobiato.sfa.model.CustomerType;
import com.mobiato.sfa.model.Delivery;
import com.mobiato.sfa.model.DepotData;
import com.mobiato.sfa.model.Discount;
import com.mobiato.sfa.model.DiscountCustomer;
import com.mobiato.sfa.model.DiscountData;
import com.mobiato.sfa.model.DiscountHeader;
import com.mobiato.sfa.model.DiscountMaster;
import com.mobiato.sfa.model.DiscountSlab;
import com.mobiato.sfa.model.Distribution;
import com.mobiato.sfa.model.FOCItems;
import com.mobiato.sfa.model.Freeze;
import com.mobiato.sfa.model.FridgeMaster;
import com.mobiato.sfa.model.Inventory;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.ItemSlab;
import com.mobiato.sfa.model.Load;
import com.mobiato.sfa.model.LoadRequest;
import com.mobiato.sfa.model.NatureMaster;
import com.mobiato.sfa.model.Notification;
import com.mobiato.sfa.model.Order;
import com.mobiato.sfa.model.Payment;
import com.mobiato.sfa.model.PlanoImages;
import com.mobiato.sfa.model.Planogram;
import com.mobiato.sfa.model.PlanogramList;
import com.mobiato.sfa.model.Pricing;
import com.mobiato.sfa.model.PromoItems;
import com.mobiato.sfa.model.PromoOfferData;
import com.mobiato.sfa.model.PromoSlab;
import com.mobiato.sfa.model.Promotion;
import com.mobiato.sfa.model.PromotionData;
import com.mobiato.sfa.model.Promotion_Item;
import com.mobiato.sfa.model.RecentCustomer;
import com.mobiato.sfa.model.Return;
import com.mobiato.sfa.model.ReturnOrder;
import com.mobiato.sfa.model.SalesInvoice;
import com.mobiato.sfa.model.SalesSummary;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.model.ServiceVisitPost;
import com.mobiato.sfa.model.Stock_Questions;
import com.mobiato.sfa.model.SubCategoryType;
import com.mobiato.sfa.model.Survey_Tools;
import com.mobiato.sfa.model.Transaction;
import com.mobiato.sfa.model.UOM;
import com.mobiato.sfa.utils.Constant;
import com.mobiato.sfa.utils.DecimalUtils;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.TimeAgo;
import com.mobiato.sfa.utils.UtilApp;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DBManager extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_SALES_MAN = "salesman";
    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_CUSTOMER_TECHNICIAN = "customer_technician";
    public static final String TABLE_CUSTOMER_CHIllER_TECHNICIAN = "customer_chiller_technician";
    public static final String TABLE_CHILLER_TECHNICIAN = "chiller_technician";
    public static final String TABLE_CHILLER_TECHNICIAN_CHECK = "chiller_technician_check";
    public static final String TABLE_CUSTOMER_OTC = "customer_otc";
    public static final String TABLE_ITEM = "item";
    public static final String TABLE_UOM = "uom";
    public static final String TABLE_LOAD_HEADER = "load_header";
    public static final String TABLE_LOAD_ITEMS = "load_items";
    public static final String TABLE_VAN_STOCK_ITEMS = "vanstock_items";
    public static final String TABLE_DELAY_PRINT = "delay_print";
    public static final String TABLE_CHILLER_REQUEST = "chiller_request";
    public static final String TABLE_CHILLER_TRACKING = "chiller_tracking";
    public static final String TABLE_TRANSACTION = "transactions";
    public static final String TABLE_INVOICE_HEADER = "invoice_header";
    public static final String TABLE_INVOICE_ITEMS = "invoice_items";
    public static final String TABLE_COLLECTION = "collection";
    public static final String TABLE_RETURN_HEADER = "return_header";
    public static final String TABLE_RETURN_ITEMS = "return_items";
    public static final String TABLE_RETURN_REQUEST_HEADER = "return__request_header";
    public static final String TABLE_RETURN_REQUEST_ITEMS = "return_request_items";
    public static final String TABLE_ORDER_HEADER = "order_header";
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String TABLE_LOAD_REQUEST_HEADER = "load_request_header";
    public static final String TABLE_LOAD_REQUEST_ITEMS = "load_request_items";
    public static final String TABLE_SALESMAN_LOAD_REQUEST_HEADER = "salesman_load_header";
    public static final String TABLE_SALESMAN_LOAD_REQUEST_ITEMS = "salesman_load_items";
    public static final String TABLE_UNLOAD_VARIANCE = "unload_variance";
    public static final String TABLE_DELIVERY_HEADER = "delivery_header";
    public static final String TABLE_DELIVERY_ITEMS = "delivery_items";
    public static final String TABLE_DELIVERY_ACCEPT_HEADER = "delivery_accept_header";
    public static final String TABLE_DELIVERY_ACCEPT_ITEMS = "delivery_accept_items";
    public static final String TABLE_AGENT_PRICING = "agent_pricing";
    public static final String TABLE_CUSTOMER_PRICING = "customer_pricing";
    public static final String TABLE_RECENT_CUSTOMER = "recent_customer";
    public static final String TABLE_DEPOT_CUSTOMER = "depot_customer";
    public static final String TABLE_PAYMENT = "payment";
    public static final String TABLE_CUSTOMER_VISIT = "customer_visit";
    public static final String TABLE_CUSTOMER_VISIT_SALES = "customer_visit_sales";
    public static final String TABLE_ITEM_DISCOUNT = "item_discount";
    public static final String ITEM_AGENT_EXCISE = "agent_excise";
    public static final String ITEM_DIRECT_EXCISE = "direct_sell_excise";
    public static final String TABLE_ROUTE_ITEM_DISCOUNT = "route_item_discount";
    public static final String TABLE_CUSTOMER_DISCOUNT = "customer_discount";
    public static final String TABLE_CATEGORY_DISCOUNT = "category_discount";
    public static final String TABLE_ROUTE_CATEGORY_DISCOUNT = "route_category_discount";
    public static final String TABLE_SALE_SUMMARY = "today_sales_summary";
    public static final String TABLE_FREE_GOODS = "free_goods";
    public static final String TABLE_AGENT_FREE_GOODS = "agent_free_goods";
    public static final String TABLE_COMPITITOR = "compititor";
    public static final String TABLE_COMPLAIN = "complain";
    public static final String TABLE_INVENTORY = "Inventory";
    public static final String TABLE_CAMPAIGN = "Campiagn";
    public static final String TABLE_PLANOGRAM = "Planogram";
    public static final String TABLE_CUSTOMER_ASSETS = "customer_asset";
    public static final String TABLE_ASSETS_QUESTIONS = "asset_Questions";
    public static final String TABLE_SURVEY_QUESTIONS = "Survey_Questions";
    public static final String TABLE_SENSOR_QUESTIONS = "Sensor_Questions";
    public static final String TABLE_CONSUMER_QUESTIONS = "Consumer_Questions";
    public static final String TABLE_CUSTOMER_INVENTORY = "customer_inventory";
    public static final String TABLE_PROMOTION_ITEM = "Promotion_item";
    public static final String TABLE_DISTRIBUTION = "distribution";
    public static final String TABLE_CONSUMER_POST_HEADER = "consumer_post_header";
    public static final String TABLE_CONSUMER_POST = "consumer_post";
    public static final String TABLE_ASSETS_POST = "assets_post";
    public static final String TABLE_ASSETS_POST_HEADER = "assets_post_header";
    public static final String TABLE_SURVEY_POST_HEADER = "survey_post_header";
    public static final String TABLE_SURVEY_POST = "survey_post";
    public static final String TABLE_SENSOR_HEADER = "Sensor_Header";
    public static final String TABLE_SENSOR_POST = "sensor_post";
    public static final String TABLE_CUSTOMER_INVENTORY_HEADER = "customer_inventory_header";
    public static final String TABLE_CUSTOMER_INVENTORY_ITEMS = "customer_inventory_items";
    public static final String TABLE_DISTRIBUTION_IMAGE = "distribution_image";
    public static final String TABLE_EXPIRY_ITEM = "expiry_item";
    public static final String TABLE_EXPIRY_HEADER = "expiry_header";
    public static final String TABLE_DAMAGED_ITEM = "damaged_item";
    public static final String TABLE_DAMAGED_HEADER = "damaged_header";
    public static final String TABLE_CUSTOMER_TYPE = "customer_type";
    public static final String TABLE_CUSTOMER_CATEGORY = "customer_category";
    public static final String TABLE_CUSTOMER_CHANNEL = "customer_channel";
    public static final String TABLE_CUSTOMER_SUB_CATEGORY = "customer_sub_category";
    public static final String TABLE_PUSH_NOTIFICATION = "push_notification";
    public static final String TABLE_PROMOTION = "promotion";
    public static final String TABLE_DEPOT = "depot";
    public static final String TABLE_CHILLER_ADD = "chiller_add";
    public static final String TABLE_ADD_CHILLER = "add_chiller";
    public static final String TABLE_ADD_FRIDGE = "add_fridge";
    public static final String TABLE_SERVICE_VISIT = "service_visit";
    public static final String TABLE_SERVICE_VISIT_POST = "service_visit_post";
    public static final String TABLE_FRIDGE_MASTER = "fridge_master";
    public static final String TABLE_NATURE_OF_CALL = "nature_of_call";
    public static final String TABLE_DISCOUNT_MAIN_HEADER = "discount_main_header";
    public static final String TABLE_DISCOUNT_SLAB = "discount_slab";
    public static final String TABLE_DISCOUNT_CUSTOMER_HEADER = "discount_customer_header";
    public static final String TABLE_DISCOUNT_CUSTOMER_EXCLUDE = "discount_customer_exclude";
    public static final String TABLE_DISCOUNT_MAIN_CATEGORY = "discount_main_category";
    public static final String TABLE_DISCOUNT_SITEM = "discount_item";
    public static final String TABLE_ITEM_PRIORITY = "item_priority";
    public static final String TABLE_ORDER_ITEM = "promo_order_item";
    public static final String TABLE_OFFER_ITEM = "promo_offer_item";
    public static final String TABLE_PROMO_SLAB = "promo_slab";
    public static final String TABLE_PROMO_CUSTOMER_EXCLUDE = "promo_customer_exclude";
    public static final String TABLE_PROMO = "promo_header";
    public static final String TABLE_AGENT_ORDER_ITEM = "agent_promo_order_item";
    public static final String TABLE_AGENT_OFFER_ITEM = "agent_promo_offer_item";
    public static final String TABLE_AGENT_PROMO_SLAB = "agent_promo_slab";
    public static final String TABLE_AGENT_PROMO = "agent_promo_header";
    public static final String TABLE_AGENT_PRICE_COUNT = "agent_price_count";
    public static final String TABLE_CUSTOMER_ORDER_ITEM = "cust_promo_order_item";
    public static final String TABLE_CUSTOMER_OFFER_ITEM = "cust_promo_offer_item";
    public static final String TABLE_CUSTOMER_PROMO_SLAB = "cust_promo_slab";
    public static final String TABLE_CUSTOMER_PROMO = "cust_promo_header";
    public static final String TABLE_PROMO_CUSTOMER = "promo_customer";
    public static final String TABLE_DEPOT_CUSTOMER_COUNT = "depot_cust_count";

    //Salesman columns
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_DISCOUNT_KEY = "discount_key";
    public static final String ST_ID = "s_id";
    public static final String UNIQUE_ID = "u_id";
    public static final String SALESMAN_ID = "salesman_id";
    public static final String SALESMAN_CODE = "sman_code";
    public static final String SALESMAN_NAME_EN = "sname_en";
    public static final String SALESMAN_NAME_AR = "sname_ar";
    public static final String SALESMAN_DIS_CHANNEL = "s_dis_channel";
    public static final String SALESMAN_ORG = "s_org";
    public static final String SALESMAN_DIVISION = "s_division";
    public static final String SALESMAN_ROUTE = "s_route";
    public static final String SALESMAN_ROUTE_NAME = "s_route_name";
    public static final String SALESMAN_VEHICLE_NO = "vehicle_no";
    public static final String SALESMAN_LOGIN_STATUS = "login_status";
    public static final String SALESMAN_REGION = "region";
    public static final String SALESMAN_SUB_REGION = "sub_region";
    public static final String SALESMAN_DEPOT = "depot";
    public static final String SALESMAN_TYPE = "type";
    public static final String SALESMAN_CONTACTNO = "contactNo";
    public static final String SALESMAN_DOJOIN = "date_of_join";
    public static final String SALESMAN_USERNAME = "username";
    public static final String SALESMAN_PASSWORD = "password";
    public static final String SALESMAN_TEAM_LEAD = "team_lead";
    public static final String SALESMAN_AGENT = "agent";
    public static final String SUPERVISOR_ID = "supervisor_id";
    public static final String SUPERVISOR_NAME = "supervisor_name";
    public static final String SALESMAN_ROLE = "s_role";
    public static final String DEPOT_BARCODE = "depot_barcode";
    public static final String DEPOT_LATITUDE = "depot_latitude";
    public static final String DEPOT_LONGITUDE = "depot_longitude";
    public static final String THRESHOLD_RADIOUS = "threshold_radious";
    public static final String SALESMAN_DISCOUNT_FLAG = "discount_flag";
    public static final String SALESMAN_ENFORCE_SEQ = "enforce_seq";
    public static final String SUMMARY_TYPE = "sales_type";
    public static final String SALE_TIME = "sales_time";
    public static final String COUNT_ID = "no_record";

    //cHILLER TECHNICIAN
    public static final String FRIDGE_ID = "fridge_id";
    public static final String FRIDGE_CODE = "fridge_code";
    public static final String IR_ID = "ir_id";
    public static final String FRIDGE_SERIALNUMBER = "serial_number";
    public static final String FRIDGE_ASSETNUMBER = "asset_number";
    public static final String FRIDGE_MODELNUMBER = "model_number";
    public static final String FRIDGE_ASQUISITION = "acquisition";
    public static final String FRIDGE_MANUFACTURAR = "manufacturer";
    public static final String FRIDGE_TYPE = "fridge_type";

    public static final String CTS_STATUS = "cts_status";
    public static final String CTS_COMMENT = "cts_comment";
    public static final String OTHER_COMMENT = "other_comment";
    public static final String COOLER_IMAGE1 = "cooler_image1";
    public static final String COOLER_IMAGE2 = "cooler_image2";
    public static final String QUALITY_RATE = "quality_rate";
    public static final String TECH_RATING = "tech_rating";
    public static final String PENDING_SPARE = "pending_spare";
    public static final String SPARE_DETAIL = "spare_detail";
    public static final String PENDING_REASON = "pending_reason";
    public static final String TEMPRATURE = "temprature";
    public static final String AMPS = "amps";
    public static final String CURRENT_VOLT = "ccurrent_volt";
    public static final String WORK_STATUS = "work_status";
    public static final String CONTACT_PERSON = "contact_person";
    public static final String CONTACT_NUMBER2 = "contact_number2";
    public static final String CONTACT_NUMBER = "contact_number";
    public static final String POSTAL_ADDRESS = "postal_address";
    public static final String LANDMARK = "landmark";
    public static final String DISTRICT = "district";
    public static final String LOCATION = "location";
    public static final String OUTLET_TYPE = "outlet_type";
    public static final String OTHER_TYPE = "other_type";
    public static final String EXIST_COOLER = "exist_cooler";
    public static final String STOCK_COMPATITER = "stock_copatiter";
    public static final String WEEKLY_SALE_VOLUME = "weekly_sale_volumn";
    public static final String WEEKLY_SALES = "weekly_sale";
    public static final String SIZE_REQUEST = "size_request";
    public static final String SAFTY_GRILL = "safty_grill";
    public static final String DISPLAY_LOCATION = "display_location";
    public static final String NATIONAL_ID = "national_id";
    public static final String PASSWORD_PHOTO = "passpwor_photo";
    public static final String ADDRESS_PROOF = "adress_proof";
    public static final String STAMP = "stamp";
    public static final String LC_LETTER = "lc_lettter";
    public static final String TREDING_LICENCE = "treding_licence";
    public static final String NATIONAL_ID_FRONT = "national_front";
    public static final String NATIONAL_ID_BACK = "national_back";
    public static final String PASSPORT_ID_FRONT = "passport_front";
    public static final String PASSPORT_ID_BACK = "passport_back";
    public static final String ADDRESS_PROOF_FRONT = "address_front";
    public static final String ADDRESS_PROOF_BACK = "address_back";
    public static final String TRADING_FRONT = "trading_front";
    public static final String TRADING_BACK = "trading_back";
    public static final String LC_FRONT = "lc_front";
    public static final String LC_BACK = "lc_back";
    public static final String STAMP_FRONT = "stamp_front";
    public static final String STAMP_BACK = "stamp_back";


    //Customer columns
    public static final String CUSTOMER_ID = "cus_id";
    public static final String CUSTOMER_ID1 = "customer_id";
    public static final String CUSTOMER_CODE = "cust_code";
    public static final String CUSTOMER_NAME = "cust_name";
    public static final String CUSTOMER_NAME2 = "cust_name2";
    public static final String BARCODE = "barCode";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CUSTOMER_PHONE_OTHER = "phone_other";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_TYPE = "cust_type";
    public static final String CUSTOMER_REGION = "region";
    public static final String CUSTOMER_SUBREGION = "sub_region";
    public static final String CUSTOMER_DIVISON = "cust_division";
    public static final String CUSTOMER_CHANNEL = "cust_channel";
    public static final String CUSTOMER_ORG = "cust_org";
    public static final String CUSTOMER_ADDRESS = "cust_address";
    public static final String CRF_ID = "crf_id";
    public static final String CC_CODE = "cc_code";

    public static final String CUSTOMER_LOCATION = "cust_location";
    public static final String CUSTOMER_LANDMARK = "cust_landmark";

    public static final String CUSTOMER_OUTLET = "cust_outlet";
    public static final String CUSTOMER_AGENT_NAME = "cust_agent_name";

    public static final String CUSTOMER_PAYMENT_TYPE = "payment_type";
    public static final String CUSTOMER_PAYMENT_TERM = "payment_term";
    public static final String CUSTOMER_BALANCE = "balance";
    public static final String CUSTOMER_CREDIT_LIMIT = "credit_limit";
    public static final String CUSTOMER_RADIUS = "thresold_radius";
    public static final String MON_SEQ = "mon_seq";
    public static final String TUE_SEQ = "tue_seq";
    public static final String WED_SEQ = "wed_seq";
    public static final String THU_SEQ = "thu_seq";
    public static final String FRI_SEQ = "fri_seq";
    public static final String SAT_SEQ = "sat_seq";
    public static final String SUN_SEQ = "sun_seq";
    public static final String IS_FREEZE_ASSIGN = "is_fridge_assign";
    public static final String FRIDGER_CODE = "fridger_code";
    public static final String ADDRESS_TWO = "address_two";
    public static final String CUST_STATE = "cust_state";
    public static final String CUST_CITY = "cust_city";
    public static final String CUST_ZIP = "cust_zip";
    public static final String IS_NEW = "is_new";
    public static final String CUST_SALE = "cust_sale";
    public static final String CUST_COLL = "cust_coll";
    public static final String CUST_ORDER = "cust_order";
    public static final String CUST_RETURN = "cust_return";
    public static final String LANGUAGE = "language";
    public static final String FRIDGE = "fridge";
    public static final String TIN_NO = "tin_no";
    public static final String CUSTOMER_VISIABLE = "customer_visiblity";
    public static final String CUSTOMER_TYPEID = "customer_type_id";
    public static final String CUSTOMER_CATEGORYID = "customer_category_id";
    public static final String CUSTOMER_CHANNEL_ID = "customer_channel_id";
    public static final String CUSTOMER_SUB_CATEGORYID = "customer_sub_category_id";
    public static final String KEY_TIME_STAMP = "timeStamp";
    public static final String KEY_TEMP_CUST = "temp_cust";

    //Item columns
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_CODE = "item_code";
    public static final String ITEM_DESCRIPTION = "description";
    public static final String ITEM_DESCRIPTION2 = "description2";
    public static final String ITEM_BASEUOM = "base_uom";
    public static final String ITEM_ALRT_UOM = "alter_uom";
    public static final String ITEM_UPC = "item_upc";
    public static final String ITEM_UOM_PRICE = "uom_price";
    public static final String ITEM_ALRT_UOM_PRCE = "alter_uom_price";
    public static final String ITEM_PACK_SIZE = "pack_size";
    public static final String ITEM_CATEGORY = "category";
    public static final String ITEM_SHELF_LIFE = "shelf_life";
    public static final String ITEM_BASE_VOLUME = "base_volume";
    public static final String ITEM_ALTER_VOLUME = "alter_volume";
    public static final String ITEM_BRAND = "barnd";
    public static final String ITEM_WEIGHT = "weight";
    public static final String ITEM_IMAGE = "image";
    public static final String ITEM_PRE_VAT = "item_per_vat";
    public static final String ITEM_VAT_VAL = "item_vat_val";
    public static final String ITEM_NET_VAL = "item_net_val";
    public static final String ITEM_EXCISE_VAL = "item_excise_val";
    public static final String ITEM_UOM_TYPE = "item_uom_type";
    public static final String ROUTE_ID = "route_id";
    public static final String ROUTE_NAME = "route_name";
    public static final String TYPE = "type";
    public static final String CATEGORY_ID = "category_id";
    public static final String DISCOUNT_TYPE = "discount_type";
    public static final String DISCOUNT_MAIN_TYPE = "discount_main_type";
    public static final String DISCOUNT = "discount";
    public static final String DISCOUNT_AMT = "discount_amt";
    public static final String DISCOUNT_QTY = "discount_qty";
    public static final String ACTUAL_ALTER_QTY = "actual_alqty";
    public static final String ACTUAL_BASE_QTY = "actual_bsqty";
    public static final String ALRT_PRCE = "alter_price";
    public static final String BASE_PRCE = "base_price";
    public static final String IS_EXCHANGE = "is_exchange";
    public static final String QTY_MIN = "qty_min";
    public static final String QTY_MAX = "qty_max";

    //UOM columns
    public static final String UOM_ID = "uom_id";
    public static final String UOM_NAME = "uom_name";

    //LOAD HEADER columns
    public static final String LOAD_ID = "load_id";
    public static final String SUB_LOAD_ID = "sub_load_id";
    public static final String LOAD_DATE = "load_date";
    public static final String LOAD_IS_VERIFIED = "is_verify";
    public static final String DATE_TIME = "date_time";

    //LOAD ITEMS columns
    public static final String ITEM_UOM = "item_uom";
    public static final String ITEM_QTY = "item_qty";
    public static final String ITEM_MAXQTY = "item_maxqty";
    public static final String ITEM_PRICE = "item_price";
    public static final String IS_FREE_ITEM = "is_free_item";
    public static final String BASE_UOM_NAME = "base_uom_name";
    public static final String ALTER_UOM_NAME = "alter_uom_name";
    public static final String ITEM_BASE_UOM_QTY = "base_uom_qty";
    public static final String ITEM_ALTER_UOM_QTY = "alter_uom_qty";
    public static final String ITEM_BASE_PRICE = "base_uom_price";
    public static final String ITEM_ALTER_PRICE = "alter_uom_price";
    public static final String IS_POSTED = "is_posted";

    //VANSTOCK ITEMS columns
    public static final String ACTUAL_QTY = "actual_qty";
    public static final String RESEVERD_QTY = "reserved_qty";
    public static final String REMAINING_QTY = "remaining_qty";
    public static final String IS_VERIFY = "is_verify";

    //Delay Print
    public static final String KEY_DATA = "data";
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_DOC_TYPE = "documentType";

    //Transaction table columns
    public static final String TR_TYPE = "tr_type";
    public static final String TR_DATE = "tr_date_time";
    public static final String TR_CUSTOMER_NUM = "tr_customer_num";
    public static final String TR_CUSTOMER_NAME = "tr_customer_name";
    public static final String TR_SALESMAN_ID = "tr_salesman_id";
    public static final String TR_INVOICE_ID = "tr_invoice_id";
    public static final String TR_ORDER_ID = "tr_order_id";
    public static final String TR_COLLECTION_ID = "tr_collection_id";
    public static final String TR_PYAMENT_ID = "tr_pyament_id";
    public static final String TR_IS_POSTED = "tr_is_posted";
    public static final String TR_MESSAGE = "tr_error_message";


    //SALES INVOICE HEADER COLUMNS
    public static final String SVH_CODE = "sv_code";
    public static final String SVH_INVOICE_TYPE = "sv_type";
    public static final String SVH_EXCHANGE_NO = "sv_exchange_no";
    public static final String SVH_INVOICE_TYPE_CODE = "sv_type_code";
    public static final String SVH_CUST_CODE = "sv_cust_code";
    public static final String SVH_INVOICE_DATE = "sv_invoice_date";
    public static final String SVH_DELVERY_DATE = "sv_delvery_date";
    public static final String SVH_DELVERY_NO = "sv_delvery_no";
    public static final String SVH_CUST_NAME = "sv_cust_name";
    public static final String SVH_TOT_AMT_SALES = "sv_tot_amt_sales";
    public static final String SVH_PRE_VAT = "sv_per_vat";
    public static final String SVH_VAT_VAL = "sv_vat_val";
    public static final String SVH_NET_VAL = "sv_net_val";
    public static final String SVH_EXCISE_VAL = "sv_excise_val";
    public static final String SVH_DISCOUNT_VAL = "sv_discount_val";
    public static final String SVH_GROSS_VAL = "sv_gross_val";
    public static final String SVH_EXCHANGE_AMT = "sv_exchange_amt";
    public static final String SVH_GR_NO = "sv_gr_no";
    public static final String SVH_BR_NO = "sv_br_no";
    public static final String SVH_ALT_QTY = "sv_alt_qty";
    public static final String SVH_BASE_QTY = "sv_base_qty";
    public static final String SVH_PURCHASER_NAME = "sv_purchase_name";
    public static final String SVH_PURCHASER_NO = "sv_purchase_no";

    //Collection
    public static final String KEY_INVOICE_NO = "invoiceNo";
    public static final String KEY_DUE_DATE = "dueDate";
    public static final String KEY_INVOICE_DATE = "invoiceDate";
    public static final String KEY_INVOICE_AMOUNT = "invoiceAmount";
    public static final String KEY_AMOUNT_CLEARED = "amountCleared";
    public static final String KEY_AMOUNT_PAY = "amountPay";
    public static final String KEY_CASH_AMOUNT = "cashAmount";
    public static final String KEY_CHEQUE_AMOUNT = "chequeAmount";
    public static final String KEY_CASH_AMOUNTPRE = "cashAmountPRE";
    public static final String KEY_CHEQUE_AMOUNTPRE = "chequeAmountPRE";
    public static final String KEY_CHEQUE_AMOUNT_INDIVIDUAL = "chequeAmountIndividual";
    public static final String KEY_CHEQUE_NUMBER = "chequeNumber";
    public static final String KEY_CHEQUE_DATE = "chequeDate";
    public static final String KEY_CHEQUE_BANK_NAME = "bankName";
    public static final String KEY_CHEQUE_BANK_CODE = "bankCode";
    public static final String KEY_COLLECTION_TYPE = "collectionType";
    public static final String KEY_IS_INVOICE_COMPLETE = "isInvoiceComplete";
    public static final String KEY_SAP_INVOICE_NO = "sapInvoiceNo";
    public static final String KEY_INVOICE_DAYS = "invoiceDays";
    public static final String KEY_INDICATOR = "indicator";
    public static final String KEY_IS_OUTSTANDING = "is_outstanding";


    public static final String COLL_NO = "coll_no";
    public static final String DUE_DATE = "due_date";
    public static final String INVOICE_DATE = "invoice_date";
    public static final String INVOICE_AMT = "invoice_amt";
    public static final String AMT_CLEARED = "amt_cleared";
    public static final String AMT_PAY = "amt_pay";
    public static final String CASH_AMT = "cash_amt";
    public static final String CHEQUE_AMT = "cheque_amt";
    public static final String CHEQUE_NO = "cheque_no";
    public static final String CHEQUE_DATE = "cheque_date";
    public static final String BANK_NAME = "bank_name";
    public static final String COLL_TYPE = "coll_type";
    public static final String COLL_PAYABLE = "is_payable";
    public static final String COLL_ISCOLLECTED = "is_collected";
    public static final String DATA_MARK_FOR_POST = "mark_to_post";
    public static final String ERROR_MSG_POST = "error_message";

    public static final String DISCOUNT_ID = "discount_id";
    public static final String DELIVERY_ID = "delivery_id";
    public static final String DELIVERY_ORDER_NO = "delivery_order_no";
    public static final String ORDER_NO = "order_no";
    public static final String ORDER_DATE = "order_date";
    public static final String DELIVERY_DATE = "delivery_date";
    public static final String ORDER_AMOUNT = "order_amount";
    public static final String ORDER_COMMENT = "order_comment";
    public static final String RETURN_TYPE = "return_type";
    public static final String REASON_TYPE = "reason_type";
    public static final String UNLOAD_TYPE = "unload_type";
    public static final String REASON = "reason";
    public static final String IS_DELETE = "is_delete";
    public static final String PRICING_PLAN_ID = "pricing_plan_id";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String CAPTURE_LATITUDE = "capture_latitude";
    public static final String CAPTURE_LONGITUDE = "capture_longitude";
    public static final String VISIT_STATUS = "visit_satatus";
    public static final String IS_RETURN_LIST = "is_returnList";
    public static final String IS_PARTIAL = "is_partial";

    // PAYMENT Table columns
    public static final String PAYMENT_INVOICE_ID = "invoice_id";
    public static final String PAYMENT_COLLECTION_ID = "collection_id";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String PAYMENT_DATE = "payment_date";
    public static final String PAYMENT_CHEQUE_NO = "cheque_no";
    public static final String PAYMENT_BANK_NAME = "bank_name";
    public static final String PAYMENT_AMOUNT = "payment_amount";

    // Free Goods Table columns
    public static final String FOC_ITEM_ID = "foc_itemId";
    public static final String FOC_ITEM_UOM = "foc_itemUOM";
    public static final String FOC_ITEM_QTY = "foc_itemQty";

    //Compititor columns
    public static final String COMPITITORID = "compititor_id";
    public static final String COMPITITOR_COMPANY_NAME = "compititor_company_name";
    public static final String COMPITITOR_BRAND = "compititor_brand";
    public static final String COMPITITOR_ITEM_NAME = "compititor_item_name";
    public static final String COMPITITOR_PRICE = "compititor_price";
    public static final String COMPITITOR_PROMOTION = "compititor_promotion";
    public static final String COMPITITOR_NOTES = "compititor_notes";
    public static final String COMPITITOR_Image1 = "compititor_image1";
    public static final String COMPITITOR_Image2 = "compititor_image2";
    public static final String COMPITITOR_Image3 = "compititor_image3";
    public static final String COMPITITOR_Image4 = "compititor_image4";

    //Complain columns
    public static final String COMPLAINID = "complain_id";
    public static final String COMPLAIN_FEEDBACK = "complain_feedback";
    public static final String COMPLAIN_BRAND = "complain_brand";
    public static final String COMPLAIN_FEEBACK_NOTES = "complain_feedback_notes";
    public static final String COMPLAIN_Image1 = "complain_image1";
    public static final String COMPLAIN_Image2 = "complain_image2";
    public static final String COMPLAIN_Image3 = "complain_image3";
    public static final String COMPLAIN_Image4 = "complain_image4";

    //Cam[aign capture
    public static final String COMMENT = "comment";
    public static final String Compaign_Image1 = "Compaign_image1";
    public static final String Compaign_Image2 = "Compaign_image2";
    public static final String Compaign_Image3 = "Compaign_image3";
    public static final String Compaign_Image4 = "Compaign_image4";
    public static final String COMPAIGN_ID = "Compaign_id";

    //Planogram list
    public static final String PLANOGRAM_ID = "planogram_id";
    public static final String PLANOGRAM_NAME = "planogram_name";
    public static final String VALID_DATE_FROM = "valid_date_from";
    public static final String VALID_DATE_TO = "valid_date_to";
    public static final String IMAGE1 = "image1";
    public static final String IMAGE2 = "image2";
    public static final String IMAGE3 = "image3";
    public static final String IMAGE4 = "image4";
    public static final String FRONT_IMAGE = "front_image";
    public static final String BACK_IMAGE = "back_image";
    public static final String DISPUTE_IMAGE1 = "disptute_image1";
    public static final String DISPUTE_IMAGE2 = "disptute_image2";

    //DIStribution columns
    public static final String ASSIGN_ITEM_ID = "assign_item_id";
    public static final String CUST_ID = "cust_id";
    public static final String DISTRIBUTION_TOOL_ID = "distribution_tool_id";
    public static final String DISTRIBUTION_TOOL_NAME = "distribution_tool_name";
    public static final String DISTRIBUTION_TOOL_FROM = "distribution_tool_valid_from";
    public static final String DISTRIBUTION_TOOL_TO = "distribution_tool_valid_to";
    public static final String DISTRIBUTION_TOOL_HEIGHT = "distribution_tool_height";
    public static final String DISTRIBUTION_TOOL_WIDTH = "distribution_tool_width";
    public static final String DISTRIBUTION_TOOL_LENGTH = "distribution_tool_lenght";
    public static final String DISTRIBUTION_TOOL_CAPACITY = "distribution_tool_capacity";
    public static final String ALTERNATIVE_CODE = "alternatecode";
    public static final String FILL_QTY = "fill_qty";
    public static final String UOM = "uom";
    public static final String QTY = "qty";

    //ASSETS list
    public static final String ASSETS_ID = "assets_id";
    public static final String ASSETS_IMAGE1 = "assets_image1";
    public static final String ASSETS_IMAGE2 = "assets_image2";
    public static final String ASSETS_IMAGE3 = "assets_image3";
    public static final String ASSETS_IMAGE4 = "assets_image4";
    public static final String ASSETS_FEEDBACK = "assets_feedback";

    //Survey HEADER columns
    public static final String SURVEY_ID = "survey_id";
    public static final String ASSET_ID = "asset_id";
    public static final String ASSET_CODE = "asset_code";
    public static final String ASSET_NAME = "asset_name";
    public static final String ASSET_TYPE = "asset_type";
    public static final String UNIQUESURVEY_ID = "unique_survey_id";

    //Survey Questions columns
    public static final String QUESTION_ID = "question_id";
    public static final String QUESTION = "question";
    public static final String QUESTION_TYPE = "question_type";
    public static final String SELECT_BASE_QUESTION = "select_base_question";
    public static final String ANSWER_TEXT = "answer_text";
    public static final String DISTRIBUTION_TYPE = "distribution_type";

    public static final String INVENTORY_ID = "inventory_id";
    public static final String INVENTORYNO = "inventory_no";
    public static final String IS_CURRENTDATE = "is_current_date";
    public static final String EXPIRY_DATE = "expiry_date";
    public static final String PC = "pc";

    //Promotional Accountablity columns
    public static final String PROMOTIONID = "promotion_id";
    public static final String PROMOTION_CUSTOMERNAME = "promotion_customer_name";
    public static final String PROMOTION_CUSTOMERPHONE = "promotion_customer_phone";
    public static final String PROMOTION_AMOUNT = "promotion_amount";
    public static final String PROMOTION_HUMPER = "promotion_humper";
    public static final String PROMOTION_ITEMNAME = "promotion_item_name";
    public static final String PROMOTION_ITEMID = "promotion_item_id";
    public static final String PROMOTIONAL_ID = "promotional_id";
    public static final String ASSIGN_UOM_ID = "assign_uom";
    public static final String QUALIFY_UOM_ID = "qualify_uom";
    public static final String INVOICE_NO = "invoice_no";
    public static final String INVOICE_IMAGE = "invoice_image";
    public static final String AMOUNT = "amount";
    public static final String FROM_DATE = "from_date";
    public static final String TO_DATE = "to_date";
    public static final String CATEGORY = "category";
    public static final String OWNER_NAME = "owner_name";
    public static final String SIGNATURE = "signature";
    public static final String SALESMAN_SIGNATURE = "salesman_signature";
    public static final String SERIAL_NO = "serial_no";
    public static final String CHILLER_IMAGE = "chiller_image";

    // Damaged oitem list
    public static final String DAMAGED_ITEM = "damage_item";
    public static final String EXPIRED_ITEM = "expired_item";
    public static final String SALEABLE_ITEM = "saleable_item";

    // Customer category Table columns
    public static final String CATEGORY_TYPE_ID = "category_id";
    public static final String CATEGORY_CODE = "category_code";
    public static final String CATEGORY_NAME = "category_name";

    // Depot Table columns
    public static final String DEPOT_ID = "depot_id";
    public static final String DEPOT_NAME = "depot_name";
    public static final String AGENT_ID = "agent_id";
    public static final String AGENT_NAME = "agent_name";
    public static final String CREDITDAY = "creditday";


    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String IS_READ = "is_read";
    public static final String PARENT_ITEM_ID = "parent_item_id";
    public static final String IS_FREE_GOOD = "is_free";
    public static final String CUSTOMER_CHILLER_REQUEST = "customer_chiller_request";
    public static final String FRIDGE_BRANDING = "branding";

    public static final String SALESMAN_NAME = "salesman_name";
    public static final String SALESMAN_CONTACT = "salesman_contact";

    public static final String SERVICE_TYPE = "service_type";
    public static final String TIMEIN = "time_in";
    public static final String TIME_OUT = "time_out";
    public static final String MODEL_NO = "model_no";
    public static final String SV_SERIAL_NO = "serial_no";
    public static final String ASSETS_NO = "assets_no";
    public static final String SV_BRANDING = "branding";
    public static final String SV_NATURE = "sav_nature";
    public static final String TICKET_NO = "ticket_no";
    public static final String SV_COMPLAIN_TYPE = "compain_type";
    public static final String SV_COMMENT = "comment";
    public static final String SV_DISPUTE = "any_dispute";
    public static final String IS_WORKING_ANY = "working_id";
    public static final String WORKING_IMAGE = "working_image";
    public static final String CLEANLESS_ID = "cleanless_id";
    public static final String CLENLESS_IMAGE = "clenless_image";
    public static final String COIL_ID = "coil_id";
    public static final String COIL_IMAGE = "coil_image";
    public static final String GASKET_ID = "gasket_id";
    public static final String GASKET_IMAGE = "gasket_image";
    public static final String BRANDING_ID = "branding_id";
    public static final String BRANDING_IMAGE = "branding_image";
    public static final String LIGHT_ID = "light_id";
    public static final String LIGHT_IMAGE = "light_image";
    public static final String VENTILATION_ID = "ventilation_id";
    public static final String VENTILATION_IMAGE = "ventilation_image";
    public static final String LEVELING_ID = "leveling_id";
    public static final String LEVELING_IMAGE = "leveling_image";
    public static final String STOCK_ID = "stock_id";
    public static final String STOCK_IMAGE = "stock_image";
    public static final String OUTLET_NAME = "outlet_name";
    public static final String TOWN_VILLAGE = "town_village";

    // Database Information
    public static final String DB_NAME = "Riham.db";


    // Creating table query
    private static final String CREATE_SALESMAN = "create table if not EXISTS " + TABLE_SALES_MAN + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SALESMAN_ID + " TEXT, " + SALESMAN_CODE + " TEXT, " + SALESMAN_NAME_EN + " TEXT," +
            "" + SALESMAN_NAME_AR + " TEXT," + SALESMAN_DIS_CHANNEL + " TEXT," +
            "" + SALESMAN_ORG + " TEXT," + SALESMAN_DIVISION + " TEXT," +
            "" + SALESMAN_ROUTE + " TEXT," + SALESMAN_VEHICLE_NO + " TEXT," +
            "" + SALESMAN_ROUTE_NAME + " TEXT," +
            "" + SALESMAN_USERNAME + " TEXT," +
            "" + SALESMAN_PASSWORD + " TEXT," +
            "" + SALESMAN_REGION + " TEXT," +
            "" + SALESMAN_SUB_REGION + " TEXT," +
            "" + SALESMAN_DEPOT + " TEXT," +
            "" + SALESMAN_TYPE + " TEXT," +
            "" + SALESMAN_CONTACTNO + " TEXT," +
            "" + SALESMAN_TEAM_LEAD + " TEXT," +
            "" + SALESMAN_AGENT + " TEXT," +
            "" + SUPERVISOR_ID + " TEXT," +
            "" + SUPERVISOR_NAME + " TEXT," +
            "" + SALESMAN_ROLE + " TEXT," +
            "" + DEPOT_BARCODE + " TEXT," +
            "" + DEPOT_LATITUDE + " TEXT," +
            "" + DEPOT_LONGITUDE + " TEXT," +
            "" + SALESMAN_DISCOUNT_FLAG + " TEXT," +
            "" + THRESHOLD_RADIOUS + " TEXT," +
            "" + SALESMAN_ENFORCE_SEQ + " TEXT," +
            "" + SALESMAN_DOJOIN + " TEXT," +
            "" + SALESMAN_LOGIN_STATUS + " TEXT);";

    // Creating table query
    private static final String CREATE_FRIDGE_MASTER = "create table if not EXISTS " + TABLE_FRIDGE_MASTER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + FRIDGE_ID + " TEXT, " + FRIDGE_CODE + " TEXT, " + SERIAL_NO + " TEXT," +
            "" + ASSETS_NO + " TEXT," + MODEL_NO + " TEXT," +
            "" + BRANDING_ID + " TEXT," + CUSTOMER_ID + " TEXT," +
            "" + OWNER_NAME + " TEXT," + OUTLET_NAME + " TEXT," +
            "" + LANDMARK + " TEXT," +
            "" + LOCATION + " TEXT," +
            "" + TOWN_VILLAGE + " TEXT," +
            "" + CONTACT_NUMBER + " TEXT," +
            "" + CUSTOMER_CODE + " TEXT," +
            "" + IS_POSTED + " TEXT);";


    // Creating table query
    private static final String CREATE_SERVICE_VISIT = "create table if not EXISTS " + TABLE_SERVICE_VISIT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SALESMAN_ID + " TEXT, " + SERVICE_TYPE + " TEXT, " + TICKET_NO + " TEXT," +
            "" + TIMEIN + " TEXT," + TIME_OUT + " TEXT," +
            "" + LATITUDE + " TEXT," + LONGITUDE + " TEXT," +
            "" + OWNER_NAME + " TEXT," + OUTLET_NAME + " TEXT," +
            "" + LANDMARK + " TEXT," +
            "" + LOCATION + " TEXT," +
            "" + TOWN_VILLAGE + " TEXT," +
            "" + CONTACT_NUMBER + " TEXT," +
            "" + MODEL_NO + " TEXT," +
            "" + SV_SERIAL_NO + " TEXT," +
            "" + ASSETS_NO + " TEXT," +
            "" + SV_BRANDING + " TEXT," +
            "" + SV_COMPLAIN_TYPE + " TEXT," +
            "" + SV_COMMENT + " TEXT," +
            "" + SV_DISPUTE + " TEXT," +
            "" + IS_WORKING_ANY + " TEXT," +
            "" + WORKING_IMAGE + " TEXT," +
            "" + CLEANLESS_ID + " TEXT," +
            "" + CLENLESS_IMAGE + " TEXT," +
            "" + COIL_ID + " TEXT," +
            "" + COIL_IMAGE + " TEXT," +
            "" + LIGHT_ID + " TEXT," +
            "" + LIGHT_IMAGE + " TEXT," +
            "" + GASKET_ID + " TEXT," +
            "" + GASKET_IMAGE + " TEXT," +
            "" + BRANDING_ID + " TEXT," +
            "" + BRANDING_IMAGE + " TEXT," +
            "" + VENTILATION_ID + " TEXT," +
            "" + VENTILATION_IMAGE + " TEXT," +
            "" + LEVELING_ID + " TEXT," +
            "" + LEVELING_IMAGE + " TEXT," +
            "" + STOCK_ID + " TEXT," +
            "" + STOCK_IMAGE + " TEXT," +
            "" + DATA_MARK_FOR_POST + " TEXT," +
            "" + IS_POSTED + " TEXT);";

    // Creating table query
    private static final String CREATE_SERVICE_VISIT_POST = "create table if not EXISTS " + TABLE_SERVICE_VISIT_POST + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SALESMAN_ID + " TEXT, " + SERVICE_TYPE + " TEXT, " + TICKET_NO + " TEXT," +
            "" + TIMEIN + " TEXT," + TIME_OUT + " TEXT," +
            "" + LATITUDE + " TEXT," + LONGITUDE + " TEXT," +
            "" + OWNER_NAME + " TEXT," + OUTLET_NAME + " TEXT," +
            "" + LANDMARK + " TEXT," +
            "" + LOCATION + " TEXT," +
            "" + TOWN_VILLAGE + " TEXT," +
            "" + DISTRICT + " TEXT," +
            "" + CONTACT_NUMBER + " TEXT," +
            "" + CONTACT_NUMBER2 + " TEXT," +
            "" + CONTACT_PERSON + " TEXT," +
            "" + IMAGE1 + " TEXT," +
            "" + DISPUTE_IMAGE1 + " TEXT," +
            "" + DISPUTE_IMAGE2 + " TEXT," +
            "" + WORK_STATUS + " TEXT," +
            "" + PENDING_REASON + " TEXT," +
            "" + PENDING_SPARE + " TEXT," +
            "" + SPARE_DETAIL + " TEXT," +
            "" + TECH_RATING + " TEXT," +
            "" + QUALITY_RATE + " TEXT," +
            "" + CURRENT_VOLT + " TEXT," +
            "" + AMPS + " TEXT," +
            "" + CTS_STATUS + " TEXT," +
            "" + CTS_COMMENT + " TEXT," +
            "" + OTHER_COMMENT + " TEXT," +
            "" + COOLER_IMAGE1 + " TEXT," +
            "" + COOLER_IMAGE2 + " TEXT," +
            "" + TEMPRATURE + " TEXT," +
            "" + MODEL_NO + " TEXT," +
            "" + SV_SERIAL_NO + " TEXT," +
            "" + SV_NATURE + " TEXT," +
            "" + ASSETS_NO + " TEXT," +
            "" + SV_BRANDING + " TEXT," +
            "" + SV_COMPLAIN_TYPE + " TEXT," +
            "" + SV_COMMENT + " TEXT," +
            "" + SV_DISPUTE + " TEXT," +
            "" + IS_WORKING_ANY + " TEXT," +
            "" + WORKING_IMAGE + " TEXT," +
            "" + CLEANLESS_ID + " TEXT," +
            "" + CLENLESS_IMAGE + " TEXT," +
            "" + COIL_ID + " TEXT," +
            "" + COIL_IMAGE + " TEXT," +
            "" + LIGHT_ID + " TEXT," +
            "" + LIGHT_IMAGE + " TEXT," +
            "" + GASKET_ID + " TEXT," +
            "" + GASKET_IMAGE + " TEXT," +
            "" + BRANDING_ID + " TEXT," +
            "" + BRANDING_IMAGE + " TEXT," +
            "" + VENTILATION_ID + " TEXT," +
            "" + VENTILATION_IMAGE + " TEXT," +
            "" + LEVELING_ID + " TEXT," +
            "" + LEVELING_IMAGE + " TEXT," +
            "" + STOCK_ID + " TEXT," +
            "" + STOCK_IMAGE + " TEXT," +
            "" + SIGNATURE + " TEXT," +
            "" + DATA_MARK_FOR_POST + " TEXT," +
            "" + IS_POSTED + " TEXT);";

    // Creating table query
    private static final String CREATE_NATURE_OF_CALL = "create table if not EXISTS " + TABLE_NATURE_OF_CALL + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + KEY_ORDER_ID + " TEXT," +
            "" + TICKET_NO + " TEXT," +
            "" + OWNER_NAME + " TEXT," + OUTLET_NAME + " TEXT," +
            "" + LANDMARK + " TEXT," +
            "" + LOCATION + " TEXT," +
            "" + TOWN_VILLAGE + " TEXT," +
            "" + CONTACT_NUMBER + " TEXT," +
            "" + CONTACT_NUMBER2 + " TEXT," +
            "" + CONTACT_PERSON + " TEXT," +
            "" + DISTRICT + " TEXT," +
            "" + MODEL_NO + " TEXT," +
            "" + SV_SERIAL_NO + " TEXT," +
            "" + ASSETS_NO + " TEXT," +
            "" + SV_BRANDING + " TEXT," +
            "" + SV_NATURE + " TEXT," +
            "" + DATE_TIME + " TEXT," +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CUSTOMER = "create table if not EXISTS " + TABLE_CUSTOMER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT," +
            "" + CUSTOMER_CODE + " TEXT NOT NULL, "
            + CUSTOMER_NAME + " TEXT NOT NULL," +
            "" + CUSTOMER_NAME2 + " TEXT," + CUSTOMER_PHONE + " TEXT," +
            "" + CUSTOMER_PHONE_OTHER + " TEXT," +
            "" + CUSTOMER_EMAIL + " TEXT," + CUSTOMER_TYPE + " TEXT," +
            "" + CUSTOMER_REGION + " TEXT," + CUSTOMER_SUBREGION + " TEXT," +
            "" + BARCODE + " TEXT," + LATITUDE + " TEXT," +
            "" + LONGITUDE + " TEXT," + CUSTOMER_DIVISON + " TEXT," +
            "" + CUSTOMER_CHANNEL + " TEXT," + CUSTOMER_ORG + " TEXT," +
            "" + CUSTOMER_ADDRESS + " TEXT," + CUSTOMER_PAYMENT_TYPE + " TEXT," +
            "" + CUSTOMER_PAYMENT_TERM + " TEXT," + CUSTOMER_BALANCE + " TEXT," +
            "" + CUSTOMER_CREDIT_LIMIT + " TEXT," +
            "" + CUSTOMER_RADIUS + " TEXT," +
            "" + ROUTE_ID + " TEXT," +
            "" + CUST_SALE + " TEXT," +
            "" + CUST_COLL + " TEXT," +
            "" + CUST_ORDER + " TEXT," +
            "" + CUST_RETURN + " TEXT," +
            "" + MON_SEQ + " TEXT," +
            "" + TUE_SEQ + " TEXT," +
            "" + WED_SEQ + " TEXT," +
            "" + THU_SEQ + " TEXT," +
            "" + FRI_SEQ + " TEXT," +
            "" + SAT_SEQ + " TEXT," +
            "" + SUN_SEQ + " TEXT," +
            "" + IS_FREEZE_ASSIGN + " TEXT," +
            "" + FRIDGER_CODE + " TEXT," +
            "" + ADDRESS_TWO + " TEXT," +
            "" + CUST_STATE + " TEXT," +
            "" + CUST_CITY + " TEXT," +
            "" + CUST_ZIP + " TEXT," +
            "" + LANGUAGE + " TEXT," +
            "" + FRIDGE + " TEXT," +
            "" + TIN_NO + " TEXT," +
            "" + IS_NEW + " TEXT," +
            "" + CUSTOMER_VISIABLE + " TEXT," +
            "" + CUSTOMER_TYPEID + " TEXT," +
            "" + CUSTOMER_CATEGORYID + " TEXT," +
            "" + CUSTOMER_CHANNEL_ID + " TEXT," +
            "" + CUSTOMER_SUB_CATEGORYID + " TEXT," +
            "" + SALESMAN_ID + " TEXT," +
            "" + KEY_TEMP_CUST + " TEXT," +
            "" + DATA_MARK_FOR_POST + " TEXT," +
            "" + IS_POSTED + " TEXT);";


    //riddhi

    private static final String CREATE_CUSTOMER_TECHNICIAN = "create table if not EXISTS " + TABLE_CUSTOMER_TECHNICIAN + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT," +
            "" + CRF_ID + " TEXT," +
            "" + CC_CODE + " TEXT, " + CUSTOMER_NAME + " TEXT NOT NULL," +
            CUSTOMER_PHONE + " TEXT," +
            "" + OWNER_NAME + " TEXT," +
            "" + SIGNATURE + " TEXT," +
            "" + CUSTOMER_ADDRESS + " TEXT," +
            "" + CUSTOMER_LOCATION + " TEXT," +
            "" + CUSTOMER_LANDMARK + " TEXT," +
            "" + CUSTOMER_OUTLET + " TEXT," +
            "" + CUSTOMER_AGENT_NAME + " TEXT," +
            "" + CUSTOMER_CHILLER_REQUEST + " TEXT," +
            "" + SALESMAN_NAME + " TEXT," +
            "" + SALESMAN_CONTACT + " TEXT," +
            "" + IS_POSTED + " TEXT);";
    private static final String CREATE_CUSTOMER_CHILLER_TECHNICIAN = "create table if not EXISTS " + TABLE_CUSTOMER_CHIllER_TECHNICIAN + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT," +
            "" + CRF_ID + " TEXT," +
            "" + CC_CODE + " TEXT, " + CUSTOMER_NAME + " TEXT NOT NULL," +
            CUSTOMER_PHONE + " TEXT," +
            "" + OWNER_NAME + " TEXT," +
            "" + SIGNATURE + " TEXT," +
            "" + CUSTOMER_ADDRESS + " TEXT," +
            "" + CUSTOMER_LOCATION + " TEXT," +
            "" + CUSTOMER_LANDMARK + " TEXT," +
            "" + CUSTOMER_OUTLET + " TEXT," +
            "" + CUSTOMER_AGENT_NAME + " TEXT," +
            "" + CUSTOMER_CHILLER_REQUEST + " TEXT," +
            "" + SALESMAN_NAME + " TEXT," +
            "" + SALESMAN_CONTACT + " TEXT," +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CHILLER_ADD = "create table if not EXISTS " + TABLE_ADD_CHILLER + "(" + ST_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT, " + UNIQUE_ID + " TEXT, " +
            "" + FRIDGE_ASSETNUMBER + " TEXT, " +
            "" + IMAGE1 + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_FRIDGE_ADD = "create table if not EXISTS " + TABLE_ADD_FRIDGE + "(" + ST_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT, " + UNIQUE_ID + " TEXT, " + TYPE + " TEXT, " + SIGNATURE + " TEXT, " +
            "" + LATITUDE + " TEXT, " + LONGITUDE + " TEXT, " + IS_FREEZE_ASSIGN + " TEXT, " + COMMENT + " TEXT, " +
            "" + IMAGE1 + " TEXT, " + IMAGE2 + " TEXT, " + IMAGE3 + " TEXT, " + IMAGE4 + " TEXT, " + FRIDGE_ASSETNUMBER + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CHILLER_TRACK_ADD = "create table if not EXISTS " + TABLE_CHILLER_TRACKING + "(" + ST_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT, " + UNIQUE_ID + " TEXT, " + TYPE + " TEXT, " + ROUTE_ID + " TEXT, " +
            "" + LATITUDE + " TEXT, " + LONGITUDE + " TEXT, " + IS_FREEZE_ASSIGN + " TEXT, " + COMMENT + " TEXT, " +
            "" + IMAGE1 + " TEXT, " + IMAGE2 + " TEXT, " + IMAGE3 + " TEXT, " + IMAGE4 + " TEXT, " + FRIDGE_ASSETNUMBER + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CHILLER_REQUEST = "create table if not EXISTS " + TABLE_CHILLER_REQUEST + "(" + ST_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + UNIQUE_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " + OWNER_NAME + " TEXT, " + CONTACT_NUMBER + " TEXT, " + POSTAL_ADDRESS + " TEXT, " +
            "" + LANDMARK + " TEXT, " + LOCATION + " TEXT, " + OUTLET_TYPE + " TEXT, " + OTHER_TYPE + " TEXT, " +
            "" + DISPLAY_LOCATION + " TEXT, " + EXIST_COOLER + " TEXT, " + STOCK_COMPATITER + " TEXT, " + WEEKLY_SALE_VOLUME + " TEXT, " +
            "" + WEEKLY_SALES + " TEXT, " + SIZE_REQUEST + " TEXT, " + SAFTY_GRILL + " TEXT, " + NATIONAL_ID + " TEXT, " +
            "" + PASSWORD_PHOTO + " TEXT, " + ADDRESS_PROOF + " TEXT, " + STAMP + " TEXT, " + LC_LETTER + " TEXT," +
            "" + TREDING_LICENCE + " TEXT, " + NATIONAL_ID_FRONT + " TEXT, " + NATIONAL_ID_BACK + " TEXT, " + PASSPORT_ID_FRONT + " TEXT, " +
            "" + PASSPORT_ID_BACK + " TEXT, " + LC_FRONT + " TEXT, " + LC_BACK + " TEXT, " + STAMP_FRONT + " TEXT, " +
            "" + STAMP_BACK + " TEXT, " + ADDRESS_PROOF_FRONT + " TEXT, " + ADDRESS_PROOF_BACK + " TEXT, " + TRADING_FRONT + " TEXT, " + TRADING_BACK + " TEXT, " +
            "" + SIGNATURE + " TEXT, " + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CHILLER_ADD_REQUEST = "create table if not EXISTS " + TABLE_CHILLER_ADD + "(" + ST_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + UNIQUE_ID + " TEXT, " + SERIAL_NO + " TEXT, " + CHILLER_IMAGE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " + OWNER_NAME + " TEXT, " + CONTACT_NUMBER + " TEXT, " + POSTAL_ADDRESS + " TEXT, " +
            "" + LANDMARK + " TEXT, " + LOCATION + " TEXT, " + OUTLET_TYPE + " TEXT, " + OTHER_TYPE + " TEXT, " +
            "" + DISPLAY_LOCATION + " TEXT, " + EXIST_COOLER + " TEXT, " + STOCK_COMPATITER + " TEXT, " + WEEKLY_SALE_VOLUME + " TEXT, " +
            "" + WEEKLY_SALES + " TEXT, " + SIZE_REQUEST + " TEXT, " + SAFTY_GRILL + " TEXT, " + NATIONAL_ID + " TEXT, " +
            "" + PASSWORD_PHOTO + " TEXT, " + ADDRESS_PROOF + " TEXT, " + STAMP + " TEXT, " + LC_LETTER + " TEXT," +
            "" + TREDING_LICENCE + " TEXT, " + NATIONAL_ID_FRONT + " TEXT, " + NATIONAL_ID_BACK + " TEXT, " + PASSPORT_ID_FRONT + " TEXT, " +
            "" + PASSPORT_ID_BACK + " TEXT, " + LC_FRONT + " TEXT, " + LC_BACK + " TEXT, " + STAMP_FRONT + " TEXT, " +
            "" + STAMP_BACK + " TEXT, " + ADDRESS_PROOF_FRONT + " TEXT, " + ADDRESS_PROOF_BACK + " TEXT, " + TRADING_FRONT + " TEXT, " + TRADING_BACK + " TEXT, " +
            "" + SIGNATURE + " TEXT, " + SALESMAN_SIGNATURE + " TEXT, " + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    //Chiller
    private static final String CREATE_CHILLER_TECHNICIAN = "create table if not EXISTS " + TABLE_CHILLER_TECHNICIAN + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + FRIDGE_ID + " TEXT," +
            "" + FRIDGE_CODE + " TEXT," +
            "" + IR_ID + " TEXT," +
            "" + FRIDGE_SERIALNUMBER + " TEXT, " + FRIDGE_ASSETNUMBER + " TEXT NOT NULL," +
            FRIDGE_MODELNUMBER + " TEXT," +
            "" + FRIDGE_ASQUISITION + " TEXT," +
            "" + FRIDGE_MANUFACTURAR + " TEXT," +
            "" + FRIDGE_TYPE + " TEXT," +
            "" + FRIDGE_BRANDING + " TEXT," +
            "" + IS_POSTED + " TEXT);";


    private static final String CREATE_CHILLER_TECHNICIAN_CHECK = "create table if not EXISTS " + TABLE_CHILLER_TECHNICIAN_CHECK + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + IR_ID + " TEXT," +
            "" + FRIDGE_ID + " TEXT," +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_DEPOT_CUSTOMER = "create table if not EXISTS " + TABLE_DEPOT_CUSTOMER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT," +
            "" + CUSTOMER_CODE + " TEXT NOT NULL, " + CUSTOMER_NAME + " TEXT NOT NULL," +
            "" + CUSTOMER_NAME2 + " TEXT," + CUSTOMER_PHONE + " TEXT," +
            "" + CUSTOMER_PHONE_OTHER + " TEXT," +
            "" + CUSTOMER_EMAIL + " TEXT," + CUSTOMER_TYPE + " TEXT," +
            "" + CUSTOMER_REGION + " TEXT," + CUSTOMER_SUBREGION + " TEXT," +
            "" + BARCODE + " TEXT," + LATITUDE + " TEXT," +
            "" + LONGITUDE + " TEXT," + CUSTOMER_DIVISON + " TEXT," +
            "" + CUSTOMER_CHANNEL + " TEXT," + CUSTOMER_ORG + " TEXT," +
            "" + CUSTOMER_ADDRESS + " TEXT," + CUSTOMER_PAYMENT_TYPE + " TEXT," +
            "" + CUSTOMER_PAYMENT_TERM + " TEXT," + CUSTOMER_BALANCE + " TEXT," +
            "" + CUSTOMER_CREDIT_LIMIT + " TEXT," +
            "" + CUSTOMER_RADIUS + " TEXT," +
            "" + ROUTE_ID + " TEXT," +
            "" + CUST_SALE + " TEXT," +
            "" + CUST_COLL + " TEXT," +
            "" + CUST_ORDER + " TEXT," +
            "" + CUST_RETURN + " TEXT," +
            "" + MON_SEQ + " TEXT," +
            "" + TUE_SEQ + " TEXT," +
            "" + WED_SEQ + " TEXT," +
            "" + THU_SEQ + " TEXT," +
            "" + FRI_SEQ + " TEXT," +
            "" + SAT_SEQ + " TEXT," +
            "" + SUN_SEQ + " TEXT," +
            "" + IS_FREEZE_ASSIGN + " TEXT," +
            "" + FRIDGER_CODE + " TEXT," +
            "" + ADDRESS_TWO + " TEXT," +
            "" + CUST_STATE + " TEXT," +
            "" + CUST_CITY + " TEXT," +
            "" + CUST_ZIP + " TEXT," +
            "" + LANGUAGE + " TEXT," +
            "" + FRIDGE + " TEXT," +
            "" + TIN_NO + " TEXT," +
            "" + IS_NEW + " TEXT," +
            "" + CUSTOMER_VISIABLE + " TEXT," +
            "" + CUSTOMER_TYPEID + " TEXT," +
            "" + CUSTOMER_CATEGORYID + " TEXT," +
            "" + CUSTOMER_CHANNEL_ID + " TEXT," +
            "" + CUSTOMER_SUB_CATEGORYID + " TEXT," +
            "" + SALESMAN_ID + " TEXT," +
            "" + KEY_TEMP_CUST + " TEXT," +
            "" + DATA_MARK_FOR_POST + " TEXT," +
            "" + IS_POSTED + " TEXT);";

   /* private static final String CREATE_DEPOT_CUSTOMER = "create table if not EXISTS " + TABLE_DEPOT_CUSTOMER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT," +
            "" + CUSTOMER_CODE + " TEXT NOT NULL, " + CUSTOMER_NAME + " TEXT NOT NULL," +
            "" + CUSTOMER_NAME2 + " TEXT," + CUSTOMER_PHONE + " TEXT," +
            "" + CUSTOMER_EMAIL + " TEXT," + CUSTOMER_TYPE + " TEXT," +
            "" + CUSTOMER_REGION + " TEXT," + CUSTOMER_SUBREGION + " TEXT," +
            "" + BARCODE + " TEXT," + LATITUDE + " TEXT," +
            "" + LONGITUDE + " TEXT," + CUSTOMER_DIVISON + " TEXT," +
            "" + CUSTOMER_CHANNEL + " TEXT," + CUSTOMER_ORG + " TEXT," +
            "" + CUSTOMER_ADDRESS + " TEXT," + CUSTOMER_PAYMENT_TYPE + " TEXT," +
            "" + CUSTOMER_PAYMENT_TERM + " TEXT," + CUSTOMER_BALANCE + " TEXT," +
            "" + CUSTOMER_CREDIT_LIMIT + " TEXT," +
            "" + CUSTOMER_RADIUS + " TEXT," +
            "" + MON_SEQ + " TEXT," +
            "" + TUE_SEQ + " TEXT," +
            "" + WED_SEQ + " TEXT," +
            "" + THU_SEQ + " TEXT," +
            "" + FRI_SEQ + " TEXT," +
            "" + SAT_SEQ + " TEXT," +
            "" + SUN_SEQ + " TEXT," +
            "" + SALESMAN_ID + " TEXT);";*/

    private static final String CREATE_RECENT_CUSTOMER = "create table if not EXISTS " + TABLE_RECENT_CUSTOMER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_ID + " TEXT NOT NULL, "
            + CUSTOMER_NAME + " TEXT NOT NULL," +
            DATE_TIME + " TEXT );";

    private static final String CREATE_ITEM = "create table if not EXISTS " + TABLE_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT NOT NULL, " + ITEM_CODE + " TEXT NOT NULL," +
            "" + ITEM_NAME + " TEXT," + ITEM_DESCRIPTION + " TEXT," +
            "" + ITEM_DESCRIPTION2 + " TEXT," + ITEM_BASEUOM + " TEXT," +
            "" + ITEM_ALRT_UOM + " TEXT," + ITEM_UPC + " TEXT," +
            "" + ITEM_UOM_PRICE + " TEXT," + ITEM_ALRT_UOM_PRCE + " TEXT," +
            "" + ITEM_PACK_SIZE + " TEXT," + ITEM_CATEGORY + " TEXT," +
            "" + ITEM_SHELF_LIFE + " TEXT," + ITEM_BRAND + " TEXT," +
            "" + ITEM_WEIGHT + " TEXT," +
            "" + ITEM_BASE_VOLUME + " TEXT," +
            "" + ITEM_ALTER_VOLUME + " TEXT," +
            "" + ITEM_AGENT_EXCISE + " TEXT," +
            "" + ITEM_DIRECT_EXCISE + " TEXT," +
            "" + ITEM_IMAGE + " TEXT);";

    private static final String CREATE_UOM = "create table if not EXISTS " + TABLE_UOM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + UOM_ID + " TEXT NOT NULL, " +
            "" + UOM_NAME + " TEXT);";

    private static final String CREATE_LOAD_HEADER = "create table if not EXISTS " + TABLE_LOAD_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + LOAD_ID + " TEXT, " +
            "" + SUB_LOAD_ID + " TEXT, " +
            "" + LOAD_DATE + " TEXT, " +
            "" + IMAGE1 + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + LOAD_IS_VERIFIED + " TEXT);";

    private static final String CREATE_LOAD_ITEMS = "create table if not EXISTS " + TABLE_LOAD_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + LOAD_ID + " TEXT, " +
            "" + SUB_LOAD_ID + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + BASE_UOM_NAME + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ALTER_UOM_NAME + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_QTY + " TEXT, " +
            "" + ITEM_PRICE + " TEXT, " +
            "" + IS_POSTED + " TEXT, " +
            "" + LOAD_IS_VERIFIED + " TEXT);";

    private static final String CREATE_VAN_STOCK_ITEMS = "create table if not EXISTS " + TABLE_VAN_STOCK_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + LOAD_ID + " TEXT, " +
            "" + SUB_LOAD_ID + " TEXT, " +
            "" + LOAD_DATE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ACTUAL_BASE_QTY + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ACTUAL_ALTER_QTY + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + IS_POSTED + " TEXT, " +
            "" + IS_DELETE + " TEXT, " +
            "" + IS_VERIFY + " TEXT);";

    private static final String CREATE_DELAY_PRINT = "create table if not EXISTS " + TABLE_DELAY_PRINT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_CODE + " TEXT, " +
            "" + KEY_ORDER_ID + " TEXT, " +
            "" + KEY_DOC_TYPE + " TEXT, " +
            "" + KEY_DATA + " TEXT);";

    private static final String CREATE_TRANSACTION = "create table if not EXISTS " + TABLE_TRANSACTION + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TR_TYPE + " TEXT NOT NULL, " + TR_DATE + " TEXT NOT NULL," +
            TR_CUSTOMER_NUM + " TEXT, " + "" + TR_CUSTOMER_NAME + " TEXT, " +
            TR_SALESMAN_ID + " TEXT, " + "" + TR_INVOICE_ID + " TEXT, " + "" + TR_MESSAGE + " TEXT, " +
            TR_ORDER_ID + " TEXT, " + "" + TR_COLLECTION_ID + " TEXT, " + "" + KEY_DATA + " TEXT, " +
            "" + TR_PYAMENT_ID + " TEXT," + "" + TR_IS_POSTED + " TEXT);";


    private static final String CREATE_INVOICE_HEADER = "create table if not EXISTS " + TABLE_INVOICE_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SVH_CODE + " TEXT, " +
            "" + SVH_INVOICE_TYPE + " TEXT, " +
            "" + SVH_EXCHANGE_NO + " TEXT, " +
            "" + SVH_INVOICE_TYPE_CODE + " TEXT, " +
            "" + SVH_INVOICE_DATE + " TEXT, " +
            "" + SVH_DELVERY_DATE + " TEXT, " +
            "" + SVH_DELVERY_NO + " TEXT, " +
            "" + SVH_CUST_CODE + " TEXT, " +
            "" + SVH_CUST_NAME + " TEXT, " +
            "" + SVH_TOT_AMT_SALES + " TEXT, " +
            "" + SVH_PRE_VAT + " TEXT, " +
            "" + SVH_VAT_VAL + " TEXT, " +
            "" + SVH_GROSS_VAL + " TEXT, " +
            "" + SVH_EXCISE_VAL + " TEXT, " +
            "" + SVH_DISCOUNT_VAL + " TEXT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + ITEM_QTY + " TEXT, " +
            "" + SVH_EXCHANGE_AMT + " TEXT, " +
            "" + SVH_GR_NO + " TEXT, " +
            "" + SVH_BR_NO + " TEXT, " +
            "" + SVH_ALT_QTY + " TEXT, " +
            "" + SVH_BASE_QTY + " TEXT, " +
            "" + SALE_TIME + " TEXT, " +
            "" + SVH_NET_VAL + " TEXT, " +
            "" + SVH_PURCHASER_NAME + " TEXT, " +
            "" + SVH_PURCHASER_NO + " TEXT, " +
            "" + LATITUDE + " TEXT, " +
            "" + LONGITUDE + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_SALES_SUMMARY_HEADER = "create table if not EXISTS " + TABLE_SALE_SUMMARY + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SVH_CODE + " TEXT, " +
            "" + SUMMARY_TYPE + " TEXT, " +
            "" + SVH_INVOICE_DATE + " TEXT, " +
            "" + SVH_CUST_CODE + " TEXT, " +
            "" + SVH_CUST_NAME + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + SVH_TOT_AMT_SALES + " TEXT);";

    private static final String CREATE_INVOICE_ITEMS = "create table if not EXISTS " + TABLE_INVOICE_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SVH_CODE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_PRICE + " TEXT, " +
            "" + ITEM_QTY + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + BASE_PRCE + " TEXT, " +
            "" + ALRT_PRCE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + CUSTOMER_CODE + " TEXT, " +
            "" + IS_FREE_ITEM + " TEXT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + IS_EXCHANGE + " TEXT, " +
            "" + DISCOUNT + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT);";

    private static final String CREATE_COLLECTION_HEADER = "create table if not EXISTS " + TABLE_COLLECTION + "("
            + UNIQUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TIME_STAMP + " TEXT,"
            + KEY_COLLECTION_TYPE + " TEXT,"
            + CUSTOMER_TYPE + " TEXT,"
            + CUSTOMER_ID + " TEXT,"
            + KEY_INVOICE_NO + " TEXT,"
            + KEY_ORDER_ID + " TEXT,"
            + KEY_INVOICE_AMOUNT + " TEXT,"
            + KEY_DUE_DATE + " TEXT,"
            + KEY_INVOICE_DATE + " TEXT,"
            + KEY_AMOUNT_CLEARED + " TEXT,"
            + KEY_AMOUNT_PAY + " TEXT,"
            + KEY_CASH_AMOUNT + " TEXT,"
            + KEY_CHEQUE_AMOUNT + " TEXT,"
            + KEY_CASH_AMOUNTPRE + " TEXT,"
            + KEY_CHEQUE_AMOUNTPRE + " TEXT,"
            + KEY_CHEQUE_AMOUNT_INDIVIDUAL + " TEXT,"
            + KEY_CHEQUE_NUMBER + " TEXT,"
            + KEY_CHEQUE_DATE + " TEXT,"
            + KEY_CHEQUE_BANK_CODE + " TEXT,"
            + KEY_CHEQUE_BANK_NAME + " TEXT,"
            + KEY_INDICATOR + " TEXT,"
            + KEY_IS_OUTSTANDING + " TEXT,"
            + DATA_MARK_FOR_POST + " TEXT,"
            + IS_POSTED + " TEXT,"
            + KEY_IS_INVOICE_COMPLETE + " TEXT " + ")";

    /*private static final String CREATE_COLLECTION_HEADER = "create table " + TABLE_COLLECTION + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SVH_CODE + " TEXT, " +
            "" + COLL_NO + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + CUSTOMER_TYPE + " TEXT, " +
            "" + COLL_TYPE + " TEXT, " +
            "" + INVOICE_DATE + " TEXT, " +
            "" + DUE_DATE + " TEXT, " +
            "" + INVOICE_AMT + " TEXT, " +
            "" + AMT_CLEARED + " TEXT, " +
            "" + AMT_PAY + " TEXT, " +
            "" + CASH_AMT + " TEXT, " +
            "" + CHEQUE_AMT + " TEXT, " +
            "" + CHEQUE_DATE + " TEXT, " +
            "" + CHEQUE_NO + " TEXT, " +
            "" + BANK_NAME + " TEXT, " +
            "" + COLL_PAYABLE + " TEXT, " +
            "" + IS_POSTED + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + COLL_ISCOLLECTED + " TEXT);";*/

    private static final String CREATE_RETURN_HEADER = "create table if not EXISTS " + TABLE_RETURN_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + KEY_ORDER_ID + " TEXT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + ORDER_AMOUNT + " TEXT, " +
            "" + RETURN_TYPE + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT, " +
            "" + SVH_EXCHANGE_NO + " TEXT, " +
            "" + IS_RETURN_LIST + " TEXT, " +
            "" + IS_PARTIAL + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_RETURN_ITEMS = "create table if not EXISTS " + TABLE_RETURN_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + RETURN_TYPE + " TEXT, " +
            "" + REASON_TYPE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + BASE_PRCE + " TEXT, " +
            "" + ALRT_PRCE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + EXPIRY_DATE + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT);";

    private static final String CREATE_RETURN_REQUEST_HEADER = "create table if not EXISTS " + TABLE_RETURN_REQUEST_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + ORDER_AMOUNT + " TEXT, " +
            "" + RETURN_TYPE + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT, " +
            "" + SVH_EXCHANGE_NO + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_RETURN_REQUEST_ITEMS = "create table if not EXISTS " + TABLE_RETURN_REQUEST_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + RETURN_TYPE + " TEXT, " +
            "" + REASON_TYPE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + BASE_PRCE + " TEXT, " +
            "" + ALRT_PRCE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT);";

    private static final String CREATE_ORDER_HEADER = "create table if not EXISTS " + TABLE_ORDER_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + DELIVERY_DATE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + ORDER_AMOUNT + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT, " +
            "" + ORDER_COMMENT + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_ORDER_ITEMS = "create table if not EXISTS " + TABLE_ORDER_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + DELIVERY_DATE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + BASE_PRCE + " TEXT, " +
            "" + ALRT_PRCE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + PARENT_ITEM_ID + " TEXT, " +
            "" + IS_FREE_ITEM + " TEXT, " +
            "" + DISCOUNT + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT);";

    private static final String CREATE_LOAD_REQUEST_HEADER = "create table if not EXISTS " + TABLE_LOAD_REQUEST_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + DELIVERY_DATE + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + AGENT_ID + " TEXT, " +
            "" + ORDER_AMOUNT + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT, " +
            "" + ORDER_COMMENT + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_SALESMAN_LOAD_REQUEST_HEADER = "create table if not EXISTS " + TABLE_SALESMAN_LOAD_REQUEST_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + ROUTE_ID + " TEXT, " +
            "" + SALESMAN_NAME + " TEXT, " +
            "" + ROUTE_NAME + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_SALESMAN_LOAD_REQUEST_ITEMS = "create table if not EXISTS " + TABLE_SALESMAN_LOAD_REQUEST_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + BASE_PRCE + " TEXT, " +
            "" + ALRT_PRCE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT);";

    private static final String CREATE_LOAD_REQUEST_ITEMS = "create table if not EXISTS " + TABLE_LOAD_REQUEST_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + BASE_PRCE + " TEXT, " +
            "" + ALRT_PRCE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + PARENT_ITEM_ID + " TEXT, " +
            "" + IS_FREE_ITEM + " TEXT, " +
            "" + DISCOUNT + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT);";

    private static final String CREATE_UNLOAD_VARIANCE = "create table if not EXISTS " + TABLE_UNLOAD_VARIANCE + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + REASON_TYPE + " TEXT, " +
            "" + UNLOAD_TYPE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";


    private static final String CREATE_DELIVERY_HEADER = "create table if not EXISTS " + TABLE_DELIVERY_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DELIVERY_ID + " TEXT, " +
            "" + DELIVERY_ORDER_NO + " TEXT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + ORDER_AMOUNT + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + REASON + " TEXT, " +
            "" + IS_DELETE + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_DELIVERY_ITEMS = "create table if not EXISTS " + TABLE_DELIVERY_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DELIVERY_ID + " TEXT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + ORDER_DATE + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + REASON + " TEXT, " +
            "" + IS_DELETE + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT);";

    private static final String CREATE_DELIVERY_ACCEPT_HEADER = "create table if not EXISTS " + TABLE_DELIVERY_ACCEPT_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DELIVERY_ID + " TEXT, " +
            "" + DELIVERY_ORDER_NO + " TEXT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + ORDER_AMOUNT + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + REASON + " TEXT, " +
            "" + IS_DELETE + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_DELIVERY_ACCEPT_ITEMS = "create table if not EXISTS " + TABLE_DELIVERY_ACCEPT_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DELIVERY_ID + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CODE + " TEXT, " +
            "" + ITEM_NAME + " TEXT, " +
            "" + ITEM_UOM_TYPE + " TEXT, " +
            "" + ITEM_BASEUOM + " TEXT, " +
            "" + ITEM_BASE_UOM_QTY + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + ITEM_ALRT_UOM + " TEXT, " +
            "" + ITEM_ALTER_UOM_QTY + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + REASON + " TEXT, " +
            "" + IS_DELETE + " TEXT, " +
            "" + ITEM_PRE_VAT + " TEXT, " +
            "" + ITEM_VAT_VAL + " TEXT, " +
            "" + ITEM_EXCISE_VAL + " TEXT, " +
            "" + ITEM_NET_VAL + " TEXT);";


    private static final String CREATE_CUSTOMER_PRICING = "create table if not EXISTS " + TABLE_CUSTOMER_PRICING + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + PRICING_PLAN_ID + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT);";

    private static final String CREATE_AGENT_PRICING = "create table if not EXISTS " + TABLE_AGENT_PRICING + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PRICING_PLAN_ID + " TEXT, " +
            "" + ROUTE_ID + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_BASE_PRICE + " TEXT, " +
            "" + ITEM_ALRT_UOM_PRCE + " TEXT);";

    private static final String CREATE_TABLE_PAYMENT = "create table if not EXISTS " + TABLE_PAYMENT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PAYMENT_INVOICE_ID + " TEXT NOT NULL, " +
            PAYMENT_COLLECTION_ID + " TEXT NOT NULL, "
            + PAYMENT_TYPE + " TEXT NOT NULL, " +
            PAYMENT_DATE + " TEXT NOT NULL, " + ""
            + PAYMENT_CHEQUE_NO + " TEXT NOT NULL, " +
            PAYMENT_BANK_NAME + " TEXT NOT NULL, " +
            PAYMENT_AMOUNT + " TEXT NOT NULL, " +
            CUSTOMER_ID + " TEXT NOT NULL);";

    private static final String CREATE_CUSTOMER_VISIT = "create table if not EXISTS " + TABLE_CUSTOMER_VISIT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + START_TIME + " TEXT, " +
            "" + END_TIME + " TEXT, " +
            "" + LATITUDE + " TEXT, " +
            "" + LONGITUDE + " TEXT, " +
            "" + CAPTURE_LATITUDE + " TEXT, " +
            "" + CAPTURE_LONGITUDE + " TEXT, " +
            "" + VISIT_STATUS + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CUSTOMER_VISIT_SALES = "create table if not EXISTS " + TABLE_CUSTOMER_VISIT_SALES + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID1 + " TEXT, " +
            "" + LATITUDE + " TEXT, " +
            "" + LONGITUDE + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";


    private static final String CREATE_DISCOUNT_MAIN_HEADER = "create table if not EXISTS " + TABLE_DISCOUNT_MAIN_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + KEY_DATA + " TEXT, " +
            "" + DISCOUNT_TYPE + " TEXT, " +
            "" + DISCOUNT_MAIN_TYPE + " TEXT, " +
            "" + DISCOUNT + " TEXT, " +
            "" + KEY_DISCOUNT_KEY + " TEXT);";

    private static final String CREATE_DISCOUNT_CUSTOMER_HEADER = "create table if not EXISTS " + TABLE_DISCOUNT_CUSTOMER_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT);";

    private static final String CREATE_DISCOUNT_CUSTOMER_EXCLUDE = "create table if not EXISTS " + TABLE_DISCOUNT_CUSTOMER_EXCLUDE + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT);";

    private static final String CREATE_DISCOUNT_MAIN_CATEGORY = "create table if not EXISTS " + TABLE_DISCOUNT_MAIN_CATEGORY + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + CATEGORY_ID + " TEXT);";


    private static final String CREATE_DISCOUNT_SITEM = "create table if not EXISTS " + TABLE_DISCOUNT_SITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + DISCOUNT_MAIN_TYPE + " TEXT, " +
            "" + ITEM_ID + " TEXT);";

    private static final String CREATE_DISCOUNT_SLAB = "create table if not EXISTS " + TABLE_DISCOUNT_SLAB + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + QTY_MIN + " TEXT, " +
            "" + QTY_MAX + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT);";


    private static final String CREATE_ITEM_PRIORITY = "create table if not EXISTS " + TABLE_ITEM_PRIORITY + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " + KEY_DISCOUNT_KEY + " TEXT, " +
            "" + KEY_DOC_TYPE + " TEXT, " + DISCOUNT_ID + " TEXT, " +
            "" + KEY_PRIORITY + " TEXT);";

    private static final String CREATE_PROMO_ORDER_ITEM = "create table if not EXISTS " + TABLE_ORDER_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " + PROMOTIONAL_ID + " TEXT, " +
            "" + UOM_ID + " TEXT);";

    private static final String CREATE_CUSTOMER_PROMO_ORDER_ITEM = "create table if not EXISTS " + TABLE_CUSTOMER_ORDER_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " + PROMOTIONAL_ID + " TEXT, " +
            "" + UOM_ID + " TEXT);";

    private static final String CREATE_AGENT_PROMO_ORDER_ITEM = "create table if not EXISTS " + TABLE_AGENT_ORDER_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " + PROMOTIONAL_ID + " TEXT, " +
            "" + UOM_ID + " TEXT);";

    private static final String CREATE_PROMO_OFFER_ITEM = "create table if not EXISTS " + TABLE_OFFER_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " + PROMOTIONAL_ID + " TEXT, " +
            "" + UOM_ID + " TEXT);";

    private static final String CREATE_CUSTOMER_PROMO_OFFER_ITEM = "create table if not EXISTS " + TABLE_CUSTOMER_OFFER_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " + PROMOTIONAL_ID + " TEXT, " +
            "" + UOM_ID + " TEXT);";

    private static final String CREATE_AGENT_PROMO_OFFER_ITEM = "create table if not EXISTS " + TABLE_AGENT_OFFER_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " + PROMOTIONAL_ID + " TEXT, " +
            "" + UOM_ID + " TEXT);";

    private static final String CREATE_PROMO_SLAB = "create table if not EXISTS " + TABLE_PROMO_SLAB + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + QTY_MIN + " TEXT, " + QTY_MAX + " TEXT, " +
            "" + QTY + " TEXT);";

    private static final String CREATE_CUSTOMER_PROMO_SLAB = "create table if not EXISTS " + TABLE_CUSTOMER_PROMO_SLAB + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + QTY_MIN + " TEXT, " + QTY_MAX + " TEXT, " +
            "" + QTY + " TEXT);";

    private static final String CREATE_PROMO_CUSTOMER_EXCLUDE = "create table if not EXISTS " + TABLE_PROMO_CUSTOMER_EXCLUDE + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + CUST_ID + " TEXT);";

    private static final String CREATE_PROMO_CUSTOMER = "create table if not EXISTS " + TABLE_PROMO_CUSTOMER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + CUST_ID + " TEXT);";

    private static final String CREATE_AGENT_PROMO_SLAB = "create table if not EXISTS " + TABLE_AGENT_PROMO_SLAB + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + QTY_MIN + " TEXT, " + QTY_MAX + " TEXT, " +
            "" + QTY + " TEXT);";

    private static final String CREATE_AGENT_PRICE_COUNT = "create table if not EXISTS " + TABLE_AGENT_PRICE_COUNT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ROUTE_ID + " TEXT, " +
            "" + COUNT_ID + " TEXT);";

    private static final String CREATE_DEPOT_CUSTOMER_COUNT = "create table if not EXISTS " + TABLE_DEPOT_CUSTOMER_COUNT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DEPOT_ID + " TEXT, " +
            "" + COUNT_ID + " TEXT);";

    private static final String CREATE_PROMO_HEADER = "create table if not EXISTS " + TABLE_PROMO + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + KEY_DATA + " TEXT, " + ASSIGN_UOM_ID + " TEXT, " + QUALIFY_UOM_ID + " TEXT, " +
            "" + KEY_PRIORITY + " TEXT);";

    private static final String CREATE_CUSTOMER_PROMO_HEADER = "create table if not EXISTS " + TABLE_CUSTOMER_PROMO + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + KEY_DATA + " TEXT, " + ASSIGN_UOM_ID + " TEXT, " + QUALIFY_UOM_ID + " TEXT, " +
            "" + KEY_PRIORITY + " TEXT);";

    private static final String CREATE_AGENT_PROMO_HEADER = "create table if not EXISTS " + TABLE_AGENT_PROMO + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT, " +
            "" + KEY_DATA + " TEXT, " + ASSIGN_UOM_ID + " TEXT, " + QUALIFY_UOM_ID + " TEXT, " +
            "" + KEY_PRIORITY + " TEXT);";

    private static final String CREATE_ITEMS_DISCOUNT = "create table if not EXISTS " + TABLE_ITEM_DISCOUNT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + UOM_ID + " TEXT, " +
            "" + TYPE + " TEXT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + DISCOUNT_TYPE + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + DISCOUNT_QTY + " TEXT, " +
            "" + DISCOUNT + " TEXT);";

    private static final String CREATE_ROUTE_ITEMS_DISCOUNT = "create table if not EXISTS " + TABLE_ROUTE_ITEM_DISCOUNT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + TYPE + " TEXT, " +
            "" + UOM_ID + " TEXT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + DISCOUNT_TYPE + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + DISCOUNT_QTY + " TEXT, " +
            "" + DISCOUNT + " TEXT);";

    private static final String CREATE_CUSTOMER_DISCOUNT = "create table if not EXISTS " + TABLE_CUSTOMER_DISCOUNT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + UOM_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + TYPE + " TEXT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + DISCOUNT_TYPE + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + DISCOUNT_QTY + " TEXT, " +
            "" + DISCOUNT + " TEXT);";

    private static final String CREATE_CATEGORY_DISCOUNT = "create table if not EXISTS " + TABLE_CATEGORY_DISCOUNT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CATEGORY_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + TYPE + " TEXT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + DISCOUNT_TYPE + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + DISCOUNT_QTY + " TEXT, " +
            "" + DISCOUNT + " TEXT);";

    private static final String CREATE_ROUTE_CATEGORY_DISCOUNT = "create table if not EXISTS " + TABLE_ROUTE_CATEGORY_DISCOUNT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CATEGORY_ID + " TEXT, " +
            "" + TYPE + " TEXT, " +
            "" + DISCOUNT_ID + " TEXT, " +
            "" + DISCOUNT_TYPE + " TEXT, " +
            "" + DISCOUNT_AMT + " TEXT, " +
            "" + DISCOUNT_QTY + " TEXT, " +
            "" + DISCOUNT + " TEXT);";

    private static final String CREATE_FREE_GOODS = "create table if not EXISTS " + TABLE_FREE_GOODS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_UOM + " TEXT, " +
            "" + ITEM_QTY + " TEXT, " +
            "" + ITEM_MAXQTY + " TEXT, " +
            "" + FOC_ITEM_ID + " TEXT, " +
            "" + FOC_ITEM_UOM + " TEXT, " +
            "" + FOC_ITEM_QTY + " TEXT, " +
            "" + KEY_PRIORITY + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT);";

    private static final String CREATE_AGENNT_FREE_GOODS = "create table if not EXISTS " + TABLE_AGENT_FREE_GOODS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_UOM + " TEXT, " +
            "" + ITEM_QTY + " TEXT, " +
            "" + ITEM_MAXQTY + " TEXT, " +
            "" + FOC_ITEM_ID + " TEXT, " +
            "" + FOC_ITEM_UOM + " TEXT, " +
            "" + FOC_ITEM_QTY + " TEXT, " +
            "" + KEY_PRIORITY + " TEXT);";


    private static final String CREATE_COMPITITOR = "create table if not EXISTS " + TABLE_COMPITITOR
            + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + COMPITITORID + " TEXT NOT NULL, " + COMPITITOR_COMPANY_NAME + " TEXT NOT NULL," +
            "" + COMPITITOR_ITEM_NAME + " TEXT," + COMPITITOR_BRAND + " TEXT NOT NULL," +
            "" + COMPITITOR_PRICE + " TEXT," + COMPITITOR_PROMOTION + " TEXT," + COMPITITOR_NOTES + " TEXT," +
            "" + COMPITITOR_Image1 + " TEXT," +
            "" + COMPITITOR_Image2 + " TEXT," +
            "" + COMPITITOR_Image3 + " TEXT," +
            "" + COMPITITOR_Image4 + " TEXT," +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_COMPLAIN = "create table if not EXISTS " + TABLE_COMPLAIN
            + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + COMPLAINID + " TEXT NOT NULL, " + COMPLAIN_FEEDBACK + " TEXT NOT NULL," +
            "" + COMPLAIN_FEEBACK_NOTES + " TEXT NOT NULL," +
            "" + ITEM_ID + " TEXT," +
            "" + COMPLAIN_Image1 + " TEXT," +
            "" + COMPLAIN_Image2 + " TEXT," +
            "" + COMPLAIN_Image3 + " TEXT," +
            "" + COMPLAIN_Image4 + " TEXT," +
            "" + COMPLAIN_BRAND + " TEXT," +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CAMPAIGN = "create table if not EXISTS " + TABLE_CAMPAIGN + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + Compaign_Image1 + " TEXT, " +
            "" + Compaign_Image2 + " TEXT, " +
            "" + Compaign_Image3 + " TEXT, " +
            "" + Compaign_Image4 + " TEXT, " +
            "" + COMMENT + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + COMPAIGN_ID + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_PLANOGRAM = "create table if not EXISTS " + TABLE_PLANOGRAM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PLANOGRAM_ID + " TEXT, " +
            "" + DISTRIBUTION_TOOL_ID + " TEXT, " +
            "" + DISTRIBUTION_TOOL_NAME + " TEXT, " +
            "" + FRONT_IMAGE + " TEXT, " +
            "" + BACK_IMAGE + " TEXT, " +
            "" + COMMENT + " TEXT, " +
            "" + PLANOGRAM_NAME + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + IMAGE1 + " TEXT, " +
            "" + IMAGE2 + " TEXT, " +
            "" + IMAGE3 + " TEXT, " +
            "" + IMAGE4 + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_ASSETS_CUSTOMER = "create table if not EXISTS " + TABLE_CUSTOMER_ASSETS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ASSET_ID + " TEXT, " +
            "" + ASSET_CODE + " TEXT, " +
            "" + ASSET_NAME + " TEXT, " +
            "" + ASSET_TYPE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + LATITUDE + " TEXT, " +
            "" + LONGITUDE + " TEXT, " +
            "" + BARCODE + " TEXT, " +
            "" + IMAGE1 + " TEXT, " +
            "" + ASSETS_IMAGE1 + " TEXT, " +
            "" + ASSETS_IMAGE2 + " TEXT, " +
            "" + ASSETS_IMAGE3 + " TEXT, " +
            "" + ASSETS_IMAGE4 + " TEXT, " +
            "" + ASSETS_FEEDBACK + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_ASSETS_QUESTIONS = "create table if not EXISTS " + TABLE_ASSETS_QUESTIONS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + QUESTION_TYPE + " TEXT, " +
            "" + QUESTION + " TEXT, " +
            "" + SELECT_BASE_QUESTION + " TEXT);";

    private static final String CREATE_SURVEY_QUESTIONS = "create table if not EXISTS " + TABLE_SURVEY_QUESTIONS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + DISTRIBUTION_TYPE + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + QUESTION_TYPE + " TEXT, " +
            "" + QUESTION + " TEXT, " +
            "" + SELECT_BASE_QUESTION + " TEXT);";

    private static final String CREATE_SENSOR_QUESTIONS = "create table if not EXISTS " + TABLE_SENSOR_QUESTIONS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + QUESTION_TYPE + " TEXT, " +
            "" + QUESTION + " TEXT, " +
            "" + SELECT_BASE_QUESTION + " TEXT);";

    private static final String CREATE_CONSUMER_QUESTIONS = "create table if not EXISTS " + TABLE_CONSUMER_QUESTIONS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + CUST_ID + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + QUESTION_TYPE + " TEXT, " +
            "" + QUESTION + " TEXT, " +
            "" + SELECT_BASE_QUESTION + " TEXT);";

    private static final String CREATE_CUSTOMER_INVENTORY = "create table if not EXISTS " + TABLE_CUSTOMER_INVENTORY + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + INVENTORY_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_QTY + " TEXT, " +
            "" + ITEM_UOM + " TEXT, " +
            "" + EXPIRY_DATE + " TEXT, " +
            "" + IS_CURRENTDATE + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_PROMOTION_ITEM = "create table if not EXISTS " + TABLE_PROMOTION_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONAL_ID + " TEXT NOT NULL, " + ITEM_ID + " TEXT NOT NULL," +
            "" + ITEM_NAME + " TEXT," + AMOUNT + " TEXT," +
            "" + FROM_DATE + " TEXT," +
            "" + TO_DATE + " TEXT);";

    private static final String CREATE_DISTRIBUTION = "create table if not EXISTS " + TABLE_DISTRIBUTION + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + ASSIGN_ITEM_ID + " TEXT NOT NULL, " + CUST_ID + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_ID + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_NAME + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_FROM + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_TO + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_HEIGHT + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_WIDTH + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_LENGTH + " TEXT NOT NULL," +
            "" + DISTRIBUTION_TOOL_CAPACITY + " TEXT NOT NULL," +
            "" + ALTERNATIVE_CODE + " TEXT NOT NULL," +
            "" + UOM + " TEXT NOT NULL," +
            "" + QTY + " TEXT NOT NULL," +
            "" + ITEM_ID + " TEXT NOT NULL," +
            "" + ITEM_NAME + " TEXT NOT NULL," +
            "" + ITEM_CATEGORY + " TEXT NOT NULL," +
            "" + FILL_QTY + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";


    private static final String CREATE_CONSUMER_POST = "create table if not EXISTS " + TABLE_CONSUMER_POST + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + CUST_ID + " TEXT, " +
            "" + ANSWER_TEXT + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CONSUMER_POST_HEADER = "create table if not EXISTS " + TABLE_CONSUMER_POST_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + CUST_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_ASSETS_POST = "create table if not EXISTS " + TABLE_ASSETS_POST + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + DISTRIBUTION_TYPE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + ANSWER_TEXT + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";
    private static final String CREATE_ASSETS_POST_HEADER = "create table if not EXISTS " + TABLE_ASSETS_POST_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + CUST_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_SURVEY_POST = "create table if not EXISTS " + TABLE_SURVEY_POST + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + DISTRIBUTION_TYPE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + ANSWER_TEXT + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";
    private static final String CREATE_SURVEY_POST_HEADER = "create table if not EXISTS " + TABLE_SURVEY_POST_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + DISTRIBUTION_TYPE + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_SENSOR_HEADER = "create table if not EXISTS " + TABLE_SENSOR_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + CUSTOMER_NAME + " TEXT, " +
            "" + CUSTOMER_EMAIL + " TEXT, " +
            "" + CUSTOMER_ADDRESS + " TEXT, " +
            "" + CUSTOMER_PHONE + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_SENSOR_POST = "create table if not EXISTS " + TABLE_SENSOR_POST + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + SURVEY_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + QUESTION_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + CUSTOMER_NAME + " TEXT, " +
            "" + CUSTOMER_EMAIL + " TEXT, " +
            "" + CUSTOMER_PHONE + " TEXT, " +
            "" + ANSWER_TEXT + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CUSTOMER_INVENTORY_HEADR = "create table if not EXISTS " + TABLE_CUSTOMER_INVENTORY_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + INVENTORY_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + INVENTORYNO + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CUSTOMER_INVENTORY_ITEMS = "create table if not EXISTS " + TABLE_CUSTOMER_INVENTORY_ITEMS + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + INVENTORY_ID + " TEXT NOT NULL, " + ITEM_ID + " TEXT NOT NULL," +
            "" + ITEM_CATEGORY + " TEXT," +
            "" + ITEM_UOM + " TEXT," +
            "" + CUSTOMER_ID + " TEXT);";

    private static final String CREATE_DISTR_IMAGE = "create table if not EXISTS " + TABLE_DISTRIBUTION_IMAGE + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + DISTRIBUTION_TOOL_ID + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + IMAGE1 + " TEXT, " +
            "" + IMAGE2 + " TEXT, " +
            "" + IMAGE3 + " TEXT, " +
            "" + IMAGE4 + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_EXPIRY_ITEM = "create table if not EXISTS " + TABLE_EXPIRY_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISTRIBUTION_TOOL_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + UOM + " TEXT, " +
            "" + EXPIRY_DATE + " TEXT, " +
            "" + PC + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_EXPIRY_HEADER = "create table if not EXISTS " + TABLE_EXPIRY_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + DISTRIBUTION_TOOL_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_DAMAGED_HEADER = "create table if not EXISTS " + TABLE_DAMAGED_HEADER + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + DISTRIBUTION_TOOL_ID + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_DAMAGED_ITEM = "create table if not EXISTS " + TABLE_DAMAGED_ITEM + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DISTRIBUTION_TOOL_ID + " TEXT, " +
            "" + ITEM_ID + " TEXT, " +
            "" + UNIQUESURVEY_ID + " TEXT, " +
            "" + ITEM_CATEGORY + " TEXT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + SALESMAN_ID + " TEXT, " +
            "" + DAMAGED_ITEM + " TEXT, " +
            "" + EXPIRED_ITEM + " TEXT, " +
            "" + SALEABLE_ITEM + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_CUSTOMER_CATEGORY = "create table if not EXISTS " + TABLE_CUSTOMER_CATEGORY + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CATEGORY_TYPE_ID + " TEXT, " +
            "" + CATEGORY_CODE + " TEXT, " +
            "" + CUSTOMER_CHANNEL_ID + " TEXT, " +
            "" + CATEGORY_NAME + " TEXT);";

    private static final String CREATE_CUSTOMER_SUB_CATEGORY = "create table if not EXISTS " + TABLE_CUSTOMER_SUB_CATEGORY + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CATEGORY_TYPE_ID + " TEXT, " +
            "" + CATEGORY_CODE + " TEXT, " +
            "" + CATEGORY_NAME + " TEXT);";

    private static final String CREATE_CUSTOMER_CHANNEL = "create table if not EXISTS " + TABLE_CUSTOMER_CHANNEL + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CATEGORY_TYPE_ID + " TEXT, " +
            "" + CATEGORY_CODE + " TEXT, " +
            "" + CATEGORY_NAME + " TEXT);";

    private static final String CREATE_CUSTOMER_TYPE = "create table if not EXISTS " + TABLE_CUSTOMER_TYPE + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CATEGORY_TYPE_ID + " TEXT, " +
            "" + CATEGORY_CODE + " TEXT, " +
            "" + CATEGORY_NAME + " TEXT);";

    private static final String CREATE_PUSH_NOTIFICATION = "create table if not EXISTS " + TABLE_PUSH_NOTIFICATION + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + CUSTOMER_ID + " TEXT, " +
            "" + ORDER_NO + " TEXT, " +
            "" + TYPE + " TEXT, " +
            "" + TITLE + " TEXT, " +
            "" + MESSAGE + " TEXT, " +
            "" + DATE_TIME + " TEXT, " +
            "" + IS_READ + " TEXT);";

    private static final String CREATE_PROMOTION = "create table if not EXISTS " + TABLE_PROMOTION + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + PROMOTIONID + " TEXT, " +
            "" + PROMOTION_CUSTOMERNAME + " TEXT, " +
            "" + PROMOTION_CUSTOMERPHONE + " TEXT, " +
            "" + PROMOTION_AMOUNT + " TEXT, " +
            "" + PROMOTION_ITEMNAME + " TEXT, " +
            "" + PROMOTION_ITEMID + " TEXT, " +
            "" + INVOICE_NO + " TEXT, " +
            "" + INVOICE_IMAGE + " TEXT, " +
            "" + DATA_MARK_FOR_POST + " TEXT, " +
            "" + IS_POSTED + " TEXT);";

    private static final String CREATE_DEPOT = "create table if not EXISTS " + TABLE_DEPOT + "(" + UNIQUE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + DEPOT_ID + " TEXT, " +
            "" + DEPOT_NAME + " TEXT, " +
            "" + AGENT_ID + " TEXT, " +
            "" + AGENT_NAME + " TEXT, " +
            "" + CREDITDAY + " TEXT);";


    public DBManager(Context context) {
        super(context, DB_NAME, null, 36);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SALESMAN);
        db.execSQL(CREATE_CUSTOMER);
        db.execSQL(CREATE_ITEM);
        db.execSQL(CREATE_UOM);
        db.execSQL(CREATE_LOAD_HEADER);
        db.execSQL(CREATE_LOAD_ITEMS);
        db.execSQL(CREATE_VAN_STOCK_ITEMS);
        db.execSQL(CREATE_DELAY_PRINT);
        db.execSQL(CREATE_TRANSACTION);
        db.execSQL(CREATE_INVOICE_HEADER);
        db.execSQL(CREATE_CHILLER_ADD);
        db.execSQL(CREATE_FRIDGE_ADD);
        db.execSQL(CREATE_CHILLER_TRACK_ADD);
        db.execSQL(CREATE_INVOICE_ITEMS);
        db.execSQL(CREATE_COLLECTION_HEADER);
        db.execSQL(CREATE_RETURN_HEADER);
        db.execSQL(CREATE_RETURN_ITEMS);
        db.execSQL(CREATE_ORDER_HEADER);
        db.execSQL(CREATE_ORDER_ITEMS);
        db.execSQL(CREATE_LOAD_REQUEST_HEADER);
        db.execSQL(CREATE_LOAD_REQUEST_ITEMS);
        db.execSQL(CREATE_SALESMAN_LOAD_REQUEST_HEADER);
        db.execSQL(CREATE_SALESMAN_LOAD_REQUEST_ITEMS);
        db.execSQL(CREATE_UNLOAD_VARIANCE);
        db.execSQL(CREATE_DELIVERY_HEADER);
        db.execSQL(CREATE_DELIVERY_ITEMS);
        db.execSQL(CREATE_DELIVERY_ACCEPT_HEADER);
        db.execSQL(CREATE_DELIVERY_ACCEPT_ITEMS);
        db.execSQL(CREATE_CUSTOMER_PRICING);
        db.execSQL(CREATE_AGENT_PRICING);
        db.execSQL(CREATE_RECENT_CUSTOMER);
        db.execSQL(CREATE_DEPOT_CUSTOMER);
        db.execSQL(CREATE_CUSTOMER_TECHNICIAN);
        db.execSQL(CREATE_CUSTOMER_CHILLER_TECHNICIAN);
        db.execSQL(CREATE_CHILLER_REQUEST);
        db.execSQL(CREATE_CHILLER_ADD_REQUEST);
        db.execSQL(CREATE_CHILLER_TECHNICIAN);
        db.execSQL(CREATE_CHILLER_TECHNICIAN_CHECK);
        db.execSQL(CREATE_TABLE_PAYMENT);
        db.execSQL(CREATE_CUSTOMER_VISIT);
        db.execSQL(CREATE_CUSTOMER_VISIT_SALES);
        db.execSQL(CREATE_ITEMS_DISCOUNT);
        db.execSQL(CREATE_ROUTE_ITEMS_DISCOUNT);
        db.execSQL(CREATE_CUSTOMER_DISCOUNT);
        db.execSQL(CREATE_SALES_SUMMARY_HEADER);
        db.execSQL(CREATE_FREE_GOODS);
        db.execSQL(CREATE_COMPITITOR);
        db.execSQL(CREATE_COMPLAIN);
        db.execSQL(CREATE_CAMPAIGN);
        db.execSQL(CREATE_PLANOGRAM);
        db.execSQL(CREATE_ASSETS_CUSTOMER);
        db.execSQL(CREATE_ASSETS_QUESTIONS);
        db.execSQL(CREATE_SURVEY_QUESTIONS);
        db.execSQL(CREATE_SENSOR_QUESTIONS);
        db.execSQL(CREATE_CONSUMER_QUESTIONS);
        db.execSQL(CREATE_CUSTOMER_INVENTORY);
        db.execSQL(CREATE_PROMOTION_ITEM);
        db.execSQL(CREATE_DISTRIBUTION);
        db.execSQL(CREATE_CONSUMER_POST);
        db.execSQL(CREATE_CONSUMER_POST_HEADER);
        db.execSQL(CREATE_ASSETS_POST);
        db.execSQL(CREATE_ASSETS_POST_HEADER);
        db.execSQL(CREATE_SURVEY_POST);
        db.execSQL(CREATE_SURVEY_POST_HEADER);
        db.execSQL(CREATE_SENSOR_HEADER);
        db.execSQL(CREATE_SENSOR_POST);
        db.execSQL(CREATE_CUSTOMER_INVENTORY_HEADR);
        db.execSQL(CREATE_DISTR_IMAGE);
        db.execSQL(CREATE_EXPIRY_ITEM);
        db.execSQL(CREATE_EXPIRY_HEADER);
        db.execSQL(CREATE_DAMAGED_HEADER);
        db.execSQL(CREATE_DAMAGED_ITEM);
        db.execSQL(CREATE_CATEGORY_DISCOUNT);
        db.execSQL(CREATE_RETURN_REQUEST_HEADER);
        db.execSQL(CREATE_RETURN_REQUEST_ITEMS);
        db.execSQL(CREATE_CUSTOMER_CATEGORY);
        db.execSQL(CREATE_CUSTOMER_SUB_CATEGORY);
        db.execSQL(CREATE_CUSTOMER_CHANNEL);
        db.execSQL(CREATE_CUSTOMER_TYPE);
        db.execSQL(CREATE_ROUTE_CATEGORY_DISCOUNT);
        db.execSQL(CREATE_PUSH_NOTIFICATION);
        db.execSQL(CREATE_PROMOTION);
        db.execSQL(CREATE_DEPOT);
        db.execSQL(CREATE_CUSTOMER_INVENTORY_ITEMS);
        db.execSQL(CREATE_SERVICE_VISIT);
        db.execSQL(CREATE_FRIDGE_MASTER);
        db.execSQL(CREATE_SERVICE_VISIT_POST);
        db.execSQL(CREATE_NATURE_OF_CALL);

        db.execSQL(CREATE_ITEM_PRIORITY);
        db.execSQL(CREATE_AGENNT_FREE_GOODS);
        db.execSQL(CREATE_PROMO_HEADER);
        db.execSQL(CREATE_PROMO_CUSTOMER_EXCLUDE);
        db.execSQL(CREATE_PROMO_ORDER_ITEM);
        db.execSQL(CREATE_PROMO_OFFER_ITEM);
        db.execSQL(CREATE_PROMO_SLAB);
        db.execSQL(CREATE_AGENT_PROMO_HEADER);
        db.execSQL(CREATE_AGENT_PROMO_ORDER_ITEM);
        db.execSQL(CREATE_AGENT_PROMO_OFFER_ITEM);
        db.execSQL(CREATE_AGENT_PROMO_SLAB);
        db.execSQL(CREATE_AGENT_PRICE_COUNT);
        db.execSQL(CREATE_CUSTOMER_PROMO_HEADER);
        db.execSQL(CREATE_PROMO_CUSTOMER);
        db.execSQL(CREATE_CUSTOMER_PROMO_ORDER_ITEM);
        db.execSQL(CREATE_CUSTOMER_PROMO_OFFER_ITEM);
        db.execSQL(CREATE_CUSTOMER_PROMO_SLAB);
        db.execSQL(CREATE_DEPOT_CUSTOMER_COUNT);
        db.execSQL(CREATE_DISCOUNT_MAIN_HEADER);
        db.execSQL(CREATE_DISCOUNT_SITEM);
        db.execSQL(CREATE_DISCOUNT_SLAB);
        db.execSQL(CREATE_DISCOUNT_CUSTOMER_HEADER);
        db.execSQL(CREATE_DISCOUNT_CUSTOMER_EXCLUDE);
        db.execSQL(CREATE_DISCOUNT_MAIN_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
//            db.execSQL("ALTER TABLE item_discount ADD COLUMN discount_id TEXT");
//            db.execSQL("ALTER TABLE route_item_discount ADD COLUMN discount_id TEXT");
//            db.execSQL("ALTER TABLE customer_discount ADD COLUMN discount_id TEXT");
//            db.execSQL("ALTER TABLE category_discount ADD COLUMN discount_id TEXT");
//            db.execSQL("ALTER TABLE route_category_discount ADD COLUMN discount_id TEXT");
            // db.execSQL("ALTER TABLE customer ADD COLUMN temp_cust TEXT");

            if (!isFieldExist(db, "customer", "customer_channel_id")) {
                db.execSQL("ALTER TABLE customer ADD COLUMN customer_channel_id TEXT");
            }

            if (!isFieldExist(db, "customer", "customer_sub_category_id")) {
                db.execSQL("ALTER TABLE customer ADD COLUMN customer_sub_category_id TEXT");
            }

            if (!isFieldExist(db, "depot_customer", "customer_channel_id")) {
                db.execSQL("ALTER TABLE depot_customer ADD COLUMN customer_channel_id TEXT");
            }

            if (!isFieldExist(db, "depot_customer", "customer_sub_category_id")) {
                db.execSQL("ALTER TABLE depot_customer ADD COLUMN customer_sub_category_id TEXT");
            }

            if (!isFieldExist(db, "customer_category", "customer_channel_id")) {
                db.execSQL("ALTER TABLE customer_category ADD COLUMN customer_channel_id TEXT");
            }

            if (!isFieldExist(db, "service_visit_post", "sav_nature")) {
                db.execSQL("ALTER TABLE service_visit_post ADD COLUMN sav_nature TEXT");
            }

            if (!isFieldExist(db, "service_visit_post", "cts_status")) {
                db.execSQL("ALTER TABLE service_visit_post ADD COLUMN cts_status TEXT");
            }

            if (!isFieldExist(db, "service_visit_post", "cts_comment")) {
                db.execSQL("ALTER TABLE service_visit_post ADD COLUMN cts_comment TEXT");
            }

            if (!isFieldExist(db, "service_visit_post", "other_comment")) {
                db.execSQL("ALTER TABLE service_visit_post ADD COLUMN other_comment TEXT");
            }

            if (!isFieldExist(db, "service_visit_post", "cooler_image1")) {
                db.execSQL("ALTER TABLE service_visit_post ADD COLUMN cooler_image1 TEXT");
            }

            if (!isFieldExist(db, "service_visit_post", "cooler_image2")) {
                db.execSQL("ALTER TABLE service_visit_post ADD COLUMN cooler_image2 TEXT");
            }

            if (!isFieldExist(db, "service_visit_post", "district")) {
                db.execSQL("ALTER TABLE service_visit_post ADD COLUMN district TEXT");
            }

            if (!isFieldExist(db, "invoice_header", "sv_discount_val")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN sv_discount_val TEXT");
            }
            if (!isFieldExist(db, "invoice_header", "discount_id")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN discount_id TEXT");
            }

            if (!isFieldExist(db, "invoice_header", "sv_gross_val")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN sv_gross_val TEXT");
            }

            if (!isFieldExist(db, "invoice_header", "promotional_id")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN promotional_id TEXT");
            }

            if (!isFieldExist(db, "invoice_header", "sv_purchase_name")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN sv_purchase_name TEXT");
            }
            if (!isFieldExist(db, "invoice_header", "sv_purchase_no")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN sv_purchase_no TEXT");
            }

            if (!isFieldExist(db, "invoice_items", "promotional_id")) {
                db.execSQL("ALTER TABLE invoice_items ADD COLUMN promotional_id TEXT");
            }

            if (!isFieldExist(db, "free_goods", "priority")) {
                db.execSQL("ALTER TABLE free_goods ADD COLUMN priority TEXT");
            }

            if (!isFieldExist(db, "return_items", "expiry_date")) {
                db.execSQL("ALTER TABLE return_items ADD COLUMN expiry_date TEXT");
            }

            if (!isFieldExist(db, "invoice_header", "latitude")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN latitude TEXT");
            }

            if (!isFieldExist(db, "invoice_header", "longitude")) {
                db.execSQL("ALTER TABLE invoice_header ADD COLUMN longitude TEXT");
            }
            onCreate(db);
        }
    }


    public Boolean isFieldExist(SQLiteDatabase db, String mTableName, String fieldName) {
        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + mTableName + " LIMIT 1", null);
            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex != -1) {
                isExist = true;
            }
        } catch (java.lang.Exception e) {
        } finally {
            try {
                res.close();
            } catch (java.lang.Exception e1) {
            }
        }
        return isExist;
    }

    //========================================== INSERT PART START ========================================================

    //INSERT SALESMAN HEADER ONLY SINGLE
    public void insertSalesman(Salesman mSalesman) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (checkUserExist(mSalesman.getSalesmanId())) {
            updateSalesman(mSalesman);
        } else {
            ContentValues contentValue = new ContentValues();

            contentValue.put(SALESMAN_ID, mSalesman.getSalesmanId());
            contentValue.put(SALESMAN_CODE, mSalesman.getSalesmanCode());
            contentValue.put(SALESMAN_NAME_EN, mSalesman.getSalesmanName());
            contentValue.put(SALESMAN_NAME_AR, mSalesman.getSalesmanName2());
            contentValue.put(SALESMAN_DIS_CHANNEL, mSalesman.getDist_channel());
            contentValue.put(SALESMAN_ORG, mSalesman.getOrganisation());
            contentValue.put(SALESMAN_DIVISION, mSalesman.getDivision());
            contentValue.put(SALESMAN_ROUTE, mSalesman.getRoute());
            contentValue.put(SALESMAN_VEHICLE_NO, mSalesman.getVehicle_no());
            contentValue.put(SALESMAN_CONTACTNO, mSalesman.getContactNo());
            contentValue.put(SALESMAN_REGION, mSalesman.getRegion());
            contentValue.put(SALESMAN_SUB_REGION, mSalesman.getSubRegion());
            contentValue.put(SALESMAN_TYPE, mSalesman.getType());
            contentValue.put(SALESMAN_DEPOT, mSalesman.getDepot());
            contentValue.put(DEPOT_BARCODE, mSalesman.getDepotBarcode());
            contentValue.put(DEPOT_LATITUDE, mSalesman.getDepotLatitude());
            contentValue.put(DEPOT_LONGITUDE, mSalesman.getDepotLongitude());
            contentValue.put(SALESMAN_USERNAME, mSalesman.getUsername());
            contentValue.put(SALESMAN_PASSWORD, mSalesman.getPassword());
            contentValue.put(SALESMAN_TEAM_LEAD, mSalesman.getTeamLead());
            contentValue.put(SALESMAN_AGENT, mSalesman.getAgent());
            contentValue.put(SUPERVISOR_ID, mSalesman.getSupervisorId());
            contentValue.put(SUPERVISOR_NAME, mSalesman.getSupervisorName());
            contentValue.put(THRESHOLD_RADIOUS, mSalesman.getThreshold_ORG());
            contentValue.put(SALESMAN_ROLE, mSalesman.getRole());
            contentValue.put(SALESMAN_DISCOUNT_FLAG, mSalesman.getDiscountFlag());
            contentValue.put(SALESMAN_DOJOIN, mSalesman.getDateOfJoin());
            contentValue.put(SALESMAN_ENFORCE_SEQ, mSalesman.getEnforceSeq());
            contentValue.put(SALESMAN_LOGIN_STATUS, "1");
            contentValue.put(SALESMAN_ROUTE_NAME, mSalesman.getRouteName());

            db.insert(TABLE_SALES_MAN, null, contentValue);
        }

    }

    public void insertRecentCustomer(RecentCustomer recentCustomer) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();

        contentValue.put(CUSTOMER_ID, recentCustomer.getCustomer_id());
        contentValue.put(CUSTOMER_NAME, recentCustomer.getCustomer_name());
        contentValue.put(DATE_TIME, recentCustomer.getDate_time());

        if (!CheckIfRecentCustomerAlreadyExist(recentCustomer.getCustomer_id())) {
            db.insert(TABLE_RECENT_CUSTOMER, null, contentValue);
        }

    }

    public boolean checkUserExist(String salesmanId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean userExist = false;
        Cursor c = db.query(TABLE_SALES_MAN, null, SALESMAN_ID + " = ?", new String[]{salesmanId}, null, null, null);
        if (c == null) return userExist;

        while (c.moveToNext()) {
            userExist = true;
        }
        c.close();

        return userExist;
    }

    public boolean CheckIfRecentCustomerAlreadyExist(String cust_num) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean userExist = false;
        Cursor c = db.query(TABLE_RECENT_CUSTOMER, null, CUSTOMER_ID + " = ?", new String[]{cust_num}, null, null, null);
        if (c == null) return userExist;

        while (c.moveToNext()) {
            userExist = true;
        }
        c.close();

        return userExist;
    }

    public boolean CheckIfDepotAlreadyExist(String cust_num) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean userExist = false;
        Cursor c = db.query(TABLE_DEPOT_CUSTOMER, null, CUSTOMER_ID + " = ?", new String[]{cust_num}, null, null, null);
        if (c == null) return userExist;

        while (c.moveToNext()) {
            userExist = true;
        }
        c.close();

        return userExist;
    }


    public void updateSalesman(Salesman mSalesman) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(SALESMAN_ID, mSalesman.getSalesmanId());
        contentValue.put(SALESMAN_CODE, mSalesman.getSalesmanCode());
        contentValue.put(SALESMAN_NAME_EN, mSalesman.getSalesmanName());
        contentValue.put(SALESMAN_NAME_AR, mSalesman.getSalesmanName2());
        contentValue.put(SALESMAN_DIS_CHANNEL, mSalesman.getDist_channel());
        contentValue.put(SALESMAN_ORG, mSalesman.getOrganisation());
        contentValue.put(SALESMAN_DIVISION, mSalesman.getDivision());
        contentValue.put(SALESMAN_ROUTE, mSalesman.getRoute());
        contentValue.put(SALESMAN_VEHICLE_NO, mSalesman.getVehicle_no());
        contentValue.put(SALESMAN_CONTACTNO, mSalesman.getContactNo());
        contentValue.put(SALESMAN_REGION, mSalesman.getRegion());
        contentValue.put(SALESMAN_SUB_REGION, mSalesman.getSubRegion());
        contentValue.put(SALESMAN_TYPE, mSalesman.getType());
        contentValue.put(SALESMAN_DEPOT, mSalesman.getDepot());
        contentValue.put(DEPOT_BARCODE, mSalesman.getDepotBarcode());
        contentValue.put(DEPOT_LATITUDE, mSalesman.getDepotLatitude());
        contentValue.put(DEPOT_LONGITUDE, mSalesman.getDepotLongitude());
        contentValue.put(SALESMAN_USERNAME, mSalesman.getUsername());
        contentValue.put(SALESMAN_PASSWORD, mSalesman.getPassword());
        contentValue.put(SALESMAN_TEAM_LEAD, mSalesman.getTeamLead());
        contentValue.put(SALESMAN_AGENT, mSalesman.getAgent());
        contentValue.put(SUPERVISOR_ID, mSalesman.getSupervisorId());
        contentValue.put(SUPERVISOR_NAME, mSalesman.getSupervisorName());
        contentValue.put(THRESHOLD_RADIOUS, mSalesman.getThreshold_ORG());
        contentValue.put(SALESMAN_ROLE, mSalesman.getRole());
        contentValue.put(SALESMAN_DISCOUNT_FLAG, mSalesman.getDiscountFlag());
        contentValue.put(SALESMAN_DOJOIN, mSalesman.getDateOfJoin());
        contentValue.put(SALESMAN_ENFORCE_SEQ, mSalesman.getEnforceSeq());
        contentValue.put(SALESMAN_LOGIN_STATUS, "1");
        contentValue.put(SALESMAN_ROUTE_NAME, mSalesman.getRouteName());


        db.update(TABLE_SALES_MAN, contentValue, SALESMAN_ID + " = ? ", new String[]{mSalesman.getSalesmanId()});

    }

    //INSERT CUSTOMER HEADER
    public void insertCustomer(ArrayList<Customer> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {
            Customer mCustomer = arrData.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(CUSTOMER_ID, mCustomer.getCustomerId());
            contentValue.put(CUSTOMER_CODE, mCustomer.getCustomerCode());
            contentValue.put(CUSTOMER_NAME, mCustomer.getCustomerName());
            contentValue.put(CUSTOMER_NAME2, mCustomer.getCustomerName2());
            contentValue.put(CUSTOMER_DIVISON, mCustomer.getCustDivison());
            contentValue.put(CUSTOMER_CHANNEL, mCustomer.getCustChannel());
            contentValue.put(CUSTOMER_ORG, mCustomer.getCustOrg());
            contentValue.put(CUSTOMER_PHONE, mCustomer.getCustPhone());
            contentValue.put(CUSTOMER_PHONE_OTHER, mCustomer.getCustomerphone1());
            contentValue.put(CUSTOMER_EMAIL, mCustomer.getCustEmail());
            contentValue.put(CUSTOMER_TYPE, mCustomer.getCustType());
            contentValue.put(CUSTOMER_REGION, mCustomer.getCustRegion());
            contentValue.put(CUSTOMER_SUBREGION, mCustomer.getCustSubRegion());
            contentValue.put(CUSTOMER_ADDRESS, mCustomer.getAddress());
            contentValue.put(BARCODE, mCustomer.getBarcode());
            contentValue.put(LATITUDE, mCustomer.getLatitude());
            contentValue.put(LONGITUDE, mCustomer.getLongitude());
            contentValue.put(CUSTOMER_PAYMENT_TYPE, "");
            contentValue.put(CUSTOMER_PAYMENT_TERM, mCustomer.getPaymentTerm());
            if (!mCustomer.getBalance().equalsIgnoreCase("")) {
                double balnce = Double.parseDouble(mCustomer.getBalance());
                contentValue.put(CUSTOMER_BALANCE, DecimalUtils.round(balnce, 2));
            } else {
                contentValue.put(CUSTOMER_BALANCE, mCustomer.getBalance());
            }
            contentValue.put(CUSTOMER_CREDIT_LIMIT, mCustomer.getCreditLimit());
            contentValue.put(CUSTOMER_RADIUS, mCustomer.getRadius());
            contentValue.put(MON_SEQ, mCustomer.getMonSqu().equals("") ? "0" : mCustomer.getMonSqu());
            contentValue.put(TUE_SEQ, mCustomer.getTueSqu().equals("") ? "0" : mCustomer.getTueSqu());
            contentValue.put(WED_SEQ, mCustomer.getWedSqu().equals("") ? "0" : mCustomer.getWedSqu());
            contentValue.put(THU_SEQ, mCustomer.getThuSqu().equals("") ? "0" : mCustomer.getThuSqu());
            contentValue.put(FRI_SEQ, mCustomer.getFriSqu().equals("") ? "0" : mCustomer.getFriSqu());
            contentValue.put(SAT_SEQ, mCustomer.getSatSqu().equals("") ? "0" : mCustomer.getSatSqu());
            contentValue.put(SUN_SEQ, mCustomer.getSunSqu().equals("") ? "0" : mCustomer.getSunSqu());
            // riddhi
            contentValue.put(IS_FREEZE_ASSIGN, mCustomer.getIs_fridge_assign());
            contentValue.put(FRIDGER_CODE, mCustomer.getFridger_code());
            contentValue.put(ADDRESS_TWO, mCustomer.getAddress2());
            contentValue.put(CUST_STATE, mCustomer.getCustomerstate());
            contentValue.put(CUST_CITY, mCustomer.getCustomercity());
            contentValue.put(CUST_ZIP, mCustomer.getCustomerzip());
            contentValue.put(CUST_SALE, "0");
            contentValue.put(CUST_COLL, "0");
            contentValue.put(CUST_ORDER, "0");
            contentValue.put(CUST_RETURN, "0");
            contentValue.put(IS_NEW, "0");
            contentValue.put(DATA_MARK_FOR_POST, "Y");
            contentValue.put(IS_POSTED, "1");
            contentValue.put(CUSTOMER_VISIABLE, "0");
            contentValue.put(SALESMAN_ID, mCustomer.getSalesmanId());
            contentValue.put(CUSTOMER_TYPEID, mCustomer.getCustomerType());
            contentValue.put(CUSTOMER_CATEGORYID, mCustomer.getCustomerCategory());
            if (mCustomer.getCustomerchannel() != null) {
                contentValue.put(CUSTOMER_CHANNEL_ID, mCustomer.getCustomerchannel());
            } else {
                contentValue.put(CUSTOMER_CHANNEL_ID, "0");
            }

            if (mCustomer.getCustomerSubCategory() != null) {
                contentValue.put(CUSTOMER_SUB_CATEGORYID, mCustomer.getCustomerSubCategory());
            } else {
                contentValue.put(CUSTOMER_SUB_CATEGORYID, "0");
            }
            contentValue.put(ROUTE_ID, mCustomer.getRouteId());
            contentValue.put(KEY_TEMP_CUST, "0");

            if (!checkIsCustomerExist(mCustomer.getCustomerId())) {
                db.insert(TABLE_CUSTOMER, null, contentValue);
            } else {
                db.update(TABLE_CUSTOMER,
                        contentValue, CUSTOMER_ID + "=?", new String[]{mCustomer.getCustomerId()});
            }
        }
    }

    //INSERT CUSTOMER TECHNICIAN HEADER
    public void insertCustomerTachnician(ArrayList<CustomerTechnician> arrDatatech) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrDatatech.size(); i++) {
            CustomerTechnician mCustomer_tech = arrDatatech.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(CUSTOMER_ID, mCustomer_tech.getCustomer_id());
            contentValue.put(CRF_ID, mCustomer_tech.getCrf_id());
            contentValue.put(CC_CODE, mCustomer_tech.getCcode());
            contentValue.put(CUSTOMER_NAME, mCustomer_tech.getCustomername());
            contentValue.put(CUSTOMER_PHONE, mCustomer_tech.getContact_number());
            contentValue.put(OWNER_NAME, mCustomer_tech.getOwner_name());
            contentValue.put(SIGNATURE, mCustomer_tech.getSign__customer_file());
            contentValue.put(CUSTOMER_ADDRESS, mCustomer_tech.getPostal_address());
            contentValue.put(CUSTOMER_LOCATION, mCustomer_tech.getLocation());
            contentValue.put(CUSTOMER_LANDMARK, mCustomer_tech.getLandmark());
            contentValue.put(CUSTOMER_OUTLET, mCustomer_tech.getOutlet_type());
            contentValue.put(CUSTOMER_AGENT_NAME, mCustomer_tech.getAgent_name());
            contentValue.put(CUSTOMER_CHILLER_REQUEST, mCustomer_tech.getChiller_size_requested());
            contentValue.put(SALESMAN_NAME, mCustomer_tech.getSalesman_name());
            contentValue.put(SALESMAN_CONTACT, mCustomer_tech.getSalesman_contact());
            contentValue.put(IS_POSTED, "1");
            db.insert(TABLE_CUSTOMER_TECHNICIAN, null, contentValue);

         /*   if (!checkIsCustomerTechnicianExist(mCustomer_tech.getCustomer_id())) {
                db.insert(TABLE_CUSTOMER_TECHNICIAN, null, contentValue);
            } else {
                db.update(TABLE_CUSTOMER_TECHNICIAN,
                        contentValue, CUSTOMER_ID + "=?", new String[]{mCustomer_tech.getCustomer_id()});
            }*/
        }
    }

    //GET
    public ArrayList<CustomerTechnician> getCutomerTechnicianList() {
        ArrayList<CustomerTechnician> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_TECHNICIAN;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            try {
                if (cursor.moveToFirst()) {
                    do {

                        CustomerTechnician item = new CustomerTechnician();
                        item.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCrf_id(cursor.getString(cursor.getColumnIndex(CRF_ID)));
                        item.setCcode(cursor.getString(cursor.getColumnIndex(CC_CODE)));
                        item.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setContact_number(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                        item.setSign__customer_file(cursor.getString(cursor.getColumnIndex(SIGNATURE)));
                        item.setPostal_address(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setLocation(cursor.getString(cursor.getColumnIndex(CUSTOMER_LOCATION)));
                        item.setLandmark(cursor.getString(cursor.getColumnIndex(CUSTOMER_LANDMARK)));
                        item.setOutlet_type(cursor.getString(cursor.getColumnIndex(CUSTOMER_OUTLET)));
                        item.setAgent_name(cursor.getString(cursor.getColumnIndex(CUSTOMER_AGENT_NAME)));
                        item.setChiller_size_requested(cursor.getString(cursor.getColumnIndex(CUSTOMER_CHILLER_REQUEST)));
                        item.setSalesman_name(cursor.getString(cursor.getColumnIndex(SALESMAN_NAME)));
                        item.setSalesman_contact(cursor.getString(cursor.getColumnIndex(SALESMAN_CONTACT)));

                        //item.setIsReturn(cursor.getString(cursor.getColumnIndex(IS_NEW)));

                        arrData.add(item);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Chiller customer
    public void insertChillerCustomerTachnician(ArrayList<CustomerTechnician> arrDatatech) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrDatatech.size(); i++) {
            CustomerTechnician mCustomer_tech = arrDatatech.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(CUSTOMER_ID, mCustomer_tech.getCustomer_id());
            contentValue.put(CRF_ID, mCustomer_tech.getCrf_id());
            contentValue.put(CC_CODE, mCustomer_tech.getCcode());
            contentValue.put(CUSTOMER_NAME, mCustomer_tech.getCustomername());
            contentValue.put(CUSTOMER_PHONE, mCustomer_tech.getContact_number());
            contentValue.put(OWNER_NAME, mCustomer_tech.getOwner_name());
            contentValue.put(SIGNATURE, mCustomer_tech.getSign__customer_file());
            contentValue.put(CUSTOMER_ADDRESS, mCustomer_tech.getPostal_address());
            contentValue.put(CUSTOMER_LOCATION, mCustomer_tech.getLocation());
            contentValue.put(CUSTOMER_LANDMARK, mCustomer_tech.getLandmark());
            contentValue.put(CUSTOMER_OUTLET, mCustomer_tech.getOutlet_type());
            contentValue.put(CUSTOMER_AGENT_NAME, mCustomer_tech.getAgent_name());
            contentValue.put(CUSTOMER_CHILLER_REQUEST, mCustomer_tech.getChiller_size_requested());
            contentValue.put(SALESMAN_NAME, mCustomer_tech.getSalesman_name());
            contentValue.put(SALESMAN_CONTACT, mCustomer_tech.getSalesman_contact());

            contentValue.put(IS_POSTED, "1");

            db.insert(TABLE_CUSTOMER_CHIllER_TECHNICIAN, null, contentValue);
          /*  if (!checkIsCustomerchillTechnicianExist(mCustomer_tech.getCustomer_id())) {
                db.insert(TABLE_CUSTOMER_CHIllER_TECHNICIAN, null, contentValue);
            } else {
                db.update(TABLE_CUSTOMER_CHIllER_TECHNICIAN,
                        contentValue, CUSTOMER_ID + "=?", new String[]{mCustomer_tech.getCustomer_id()});
            }*/
        }
    }

    public boolean deleteChillerCustomer(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TABLE_CUSTOMER_CHIllER_TECHNICIAN, CRF_ID + "=?", new String[]{id}) > 0;
    }

    public void deleteChillerCustomerAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CUSTOMER_CHIllER_TECHNICIAN);
        db.execSQL("delete from " + TABLE_CUSTOMER_TECHNICIAN);
        db.execSQL("delete from " + TABLE_CHILLER_TECHNICIAN);
    }


    //Get Chiller
    public ArrayList<CustomerTechnician> getChillerCutomerTechnicianList() {
        ArrayList<CustomerTechnician> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CHIllER_TECHNICIAN;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            try {
                if (cursor.moveToFirst()) {
                    do {

                        CustomerTechnician item = new CustomerTechnician();
                        item.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCrf_id(cursor.getString(cursor.getColumnIndex(CRF_ID)));
                        item.setCcode(cursor.getString(cursor.getColumnIndex(CC_CODE)));
                        item.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setContact_number(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                        item.setSign__customer_file(cursor.getString(cursor.getColumnIndex(SIGNATURE)));
                        item.setPostal_address(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setLocation(cursor.getString(cursor.getColumnIndex(CUSTOMER_LOCATION)));
                        item.setLandmark(cursor.getString(cursor.getColumnIndex(CUSTOMER_LANDMARK)));
                        item.setOutlet_type(cursor.getString(cursor.getColumnIndex(CUSTOMER_OUTLET)));
                        item.setAgent_name(cursor.getString(cursor.getColumnIndex(CUSTOMER_AGENT_NAME)));
                        item.setChiller_size_requested(cursor.getString(cursor.getColumnIndex(CUSTOMER_CHILLER_REQUEST)));
                        item.setSalesman_name(cursor.getString(cursor.getColumnIndex(SALESMAN_NAME)));
                        item.setSalesman_contact(cursor.getString(cursor.getColumnIndex(SALESMAN_CONTACT)));

                        //item.setIsReturn(cursor.getString(cursor.getColumnIndex(IS_NEW)));

                        arrData.add(item);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<CustomerTechnician> getChillerCutomerTechnicianListR(String id) {
        ArrayList<CustomerTechnician> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CHIllER_TECHNICIAN + " Where " + CUSTOMER_ID + " = " + id;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            try {
                if (cursor.moveToFirst()) {
                    do {

                        CustomerTechnician item = new CustomerTechnician();
                        item.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCrf_id(cursor.getString(cursor.getColumnIndex(CRF_ID)));
                        item.setCcode(cursor.getString(cursor.getColumnIndex(CC_CODE)));
                        item.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setContact_number(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                        item.setSign__customer_file(cursor.getString(cursor.getColumnIndex(SIGNATURE)));
                        item.setPostal_address(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setLocation(cursor.getString(cursor.getColumnIndex(CUSTOMER_LOCATION)));
                        item.setLandmark(cursor.getString(cursor.getColumnIndex(CUSTOMER_LANDMARK)));
                        item.setOutlet_type(cursor.getString(cursor.getColumnIndex(CUSTOMER_OUTLET)));
                        item.setAgent_name(cursor.getString(cursor.getColumnIndex(CUSTOMER_AGENT_NAME)));
                        item.setChiller_size_requested(cursor.getString(cursor.getColumnIndex(CUSTOMER_CHILLER_REQUEST)));
                        item.setSalesman_name(cursor.getString(cursor.getColumnIndex(SALESMAN_NAME)));
                        item.setSalesman_contact(cursor.getString(cursor.getColumnIndex(SALESMAN_CONTACT)));

                        //item.setIsReturn(cursor.getString(cursor.getColumnIndex(IS_NEW)));

                        arrData.add(item);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Add Fridge
    public void insertChillerAdd(AddChiller mCustomer_tech) {
        SQLiteDatabase db = this.getWritableDatabase();
        //for (int i = 0; i < arrDatatech.size(); i++) {
        // AddChiller mCustomer_tech = arrDatatech.get(i);
        ContentValues contentValue = new ContentValues();
        contentValue.put(CUSTOMER_ID, mCustomer_tech.getCustomer_id());
        contentValue.put(FRIDGE_ASSETNUMBER, mCustomer_tech.getAsset_no());
        contentValue.put(IS_POSTED, "1");
        db.insert(TABLE_ADD_CHILLER, null, contentValue);
        // }
    }

    public ArrayList<AddChiller> getChillerAddList() {
        ArrayList<AddChiller> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ADD_CHILLER;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        AddChiller item = new AddChiller();
                        item.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setAsset_no(cursor.getString(cursor.getColumnIndex(FRIDGE_ASSETNUMBER)));
                        arrData.add(item);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<FridgeMaster> getFridgeData() {
        ArrayList<FridgeMaster> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FRIDGE_MASTER;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        FridgeMaster item = new FridgeMaster();
                        item.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setModel_number(cursor.getString(cursor.getColumnIndex(MODEL_NO)));
                        item.setSerial_number(cursor.getString(cursor.getColumnIndex(SERIAL_NO)));
                        item.setAsset_number(cursor.getString(cursor.getColumnIndex(ASSETS_NO)));
                        item.setBranding(cursor.getString(cursor.getColumnIndex(BRANDING_ID)));

                        item.setCcode(cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                        item.setCustomername(cursor.getString(cursor.getColumnIndex(OUTLET_NAME)));
                        item.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                        item.setLandmark(cursor.getString(cursor.getColumnIndex(LANDMARK)));
                        item.setDistrict(cursor.getString(cursor.getColumnIndex(LOCATION)));
                        item.setRoad_street(cursor.getString(cursor.getColumnIndex(TOWN_VILLAGE)));
                        item.setCustomerphone(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                        arrData.add(item);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<NatureMaster> getNatureData() {
        ArrayList<NatureMaster> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NATURE_OF_CALL;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        NatureMaster item = new NatureMaster();
                        item.setId(cursor.getString(cursor.getColumnIndex(KEY_ORDER_ID)));
                        item.setModel_number(cursor.getString(cursor.getColumnIndex(MODEL_NO)));
                        item.setSerial_number(cursor.getString(cursor.getColumnIndex(SV_SERIAL_NO)));
                        item.setAsset_number(cursor.getString(cursor.getColumnIndex(ASSETS_NO)));
                        item.setBranding(cursor.getString(cursor.getColumnIndex(SV_BRANDING)));
                        item.setNature_of_call(cursor.getString(cursor.getColumnIndex(SV_NATURE)));
                        item.setTicket_no(cursor.getString(cursor.getColumnIndex(TICKET_NO)));

                        item.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                        item.setLandmark(cursor.getString(cursor.getColumnIndex(LANDMARK)));
                        item.setOutlet_name(cursor.getString(cursor.getColumnIndex(OUTLET_NAME)));
                        item.setTown(cursor.getString(cursor.getColumnIndex(TOWN_VILLAGE)));
                        item.setRoad_street(cursor.getString(cursor.getColumnIndex(LOCATION)));
                        item.setCustomerphone(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                        item.setCustomer_phone2(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER2)));

                        if (cursor.getString(cursor.getColumnIndex(DATE_TIME)) != null) {
                            item.setDate(cursor.getString(cursor.getColumnIndex(DATE_TIME)));
                        } else {
                            item.setDate("");
                        }

                        if (cursor.getString(cursor.getColumnIndex(DISTRICT)) != null) {
                            item.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
                        } else {
                            item.setDistrict("");
                        }

                        if (cursor.getString(cursor.getColumnIndex(IS_POSTED)).equals("0")) {
                            arrData.add(item);
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<NatureMaster> getAllNatureData() {
        ArrayList<NatureMaster> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NATURE_OF_CALL;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        NatureMaster item = new NatureMaster();
                        item.setId(cursor.getString(cursor.getColumnIndex(KEY_ORDER_ID)));
                        item.setModel_number(cursor.getString(cursor.getColumnIndex(MODEL_NO)));
                        item.setSerial_number(cursor.getString(cursor.getColumnIndex(SV_SERIAL_NO)));
                        item.setAsset_number(cursor.getString(cursor.getColumnIndex(ASSETS_NO)));
                        item.setBranding(cursor.getString(cursor.getColumnIndex(SV_BRANDING)));
                        item.setNature_of_call(cursor.getString(cursor.getColumnIndex(SV_NATURE)));
                        item.setTicket_no(cursor.getString(cursor.getColumnIndex(TICKET_NO)));

                        item.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                        item.setLandmark(cursor.getString(cursor.getColumnIndex(LANDMARK)));
                        item.setOutlet_name(cursor.getString(cursor.getColumnIndex(OUTLET_NAME)));
                        item.setTown(cursor.getString(cursor.getColumnIndex(TOWN_VILLAGE)));
                        item.setRoad_street(cursor.getString(cursor.getColumnIndex(LOCATION)));
                        item.setCustomerphone(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                        item.setCustomer_phone2(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER2)));

                        if (cursor.getString(cursor.getColumnIndex(DATE_TIME)) != null) {
                            item.setDate(cursor.getString(cursor.getColumnIndex(DATE_TIME)));
                        } else {
                            item.setDate("");
                        }

                        if (cursor.getString(cursor.getColumnIndex(DISTRICT)) != null) {
                            item.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
                        } else {
                            item.setDistrict("");
                        }

                        if (cursor.getString(cursor.getColumnIndex(IS_POSTED)).equals("0")) {
                            arrData.add(item);
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //CHILLER TECHNICIAN
    //INSERT CHILLER TECHNICIAN HEADER
    public void insertChillerTachnician(ArrayList<ChillerTechnician> arrDatatech) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrDatatech.size(); i++) {
            ChillerTechnician mCustomer_tech = arrDatatech.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(FRIDGE_ID, mCustomer_tech.getFridge_id());
            contentValue.put(FRIDGE_CODE, mCustomer_tech.getFridge_code());
            contentValue.put(IR_ID, mCustomer_tech.getIr_id());
            contentValue.put(FRIDGE_SERIALNUMBER, mCustomer_tech.getSerial_number());
            contentValue.put(FRIDGE_ASSETNUMBER, mCustomer_tech.getAsset_number());
            contentValue.put(FRIDGE_MODELNUMBER, mCustomer_tech.getModel_number());
            contentValue.put(FRIDGE_ASQUISITION, mCustomer_tech.getAcquisition());
            contentValue.put(FRIDGE_MANUFACTURAR, mCustomer_tech.getManufacturer());
            contentValue.put(FRIDGE_TYPE, mCustomer_tech.getFridge_type());
            contentValue.put(FRIDGE_BRANDING, mCustomer_tech.getBranding());
            contentValue.put(IS_POSTED, "1");
            db.insert(TABLE_CHILLER_TECHNICIAN, null, contentValue);
        }
    }

    public void insertFridgeMaster(ArrayList<FridgeMaster> arrDatatech) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrDatatech.size(); i++) {
            FridgeMaster mCustomer_tech = arrDatatech.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(FRIDGE_ID, mCustomer_tech.getId());
            contentValue.put(FRIDGE_CODE, mCustomer_tech.getFridge_code());
            contentValue.put(SERIAL_NO, mCustomer_tech.getSerial_number());
            contentValue.put(ASSETS_NO, mCustomer_tech.getAsset_number());
            contentValue.put(MODEL_NO, mCustomer_tech.getModel_number());
            contentValue.put(BRANDING_ID, mCustomer_tech.getBranding());

            if (mCustomer_tech.getCustomer_id() != null) {
                contentValue.put(CUSTOMER_ID, mCustomer_tech.getCustomer_id());
            } else {
                contentValue.put(CUSTOMER_ID, "");
            }

            if (mCustomer_tech.getCcode() != null) {
                contentValue.put(CUSTOMER_CODE, mCustomer_tech.getCcode());
            } else {
                contentValue.put(CUSTOMER_CODE, "");
            }

            if (mCustomer_tech.getOwner_name() != null) {
                contentValue.put(OWNER_NAME, mCustomer_tech.getOwner_name());
            } else {
                contentValue.put(OWNER_NAME, "");
            }

            if (mCustomer_tech.getCustomername() != null) {
                contentValue.put(OUTLET_NAME, mCustomer_tech.getCustomername());
            } else {
                contentValue.put(OUTLET_NAME, "");
            }

            if (mCustomer_tech.getLandmark() != null) {
                contentValue.put(LANDMARK, mCustomer_tech.getLandmark());
            } else {
                contentValue.put(LANDMARK, "");
            }

            if (mCustomer_tech.getDistrict() != null) {
                contentValue.put(LOCATION, mCustomer_tech.getDistrict());
            } else {
                contentValue.put(LOCATION, "");
            }

            if (mCustomer_tech.getRoad_street() != null) {
                contentValue.put(TOWN_VILLAGE, mCustomer_tech.getRoad_street());
            } else {
                contentValue.put(TOWN_VILLAGE, "");
            }
            if (mCustomer_tech.getCustomerphone() != null) {
                contentValue.put(CONTACT_NUMBER, mCustomer_tech.getCustomerphone());
            } else {
                contentValue.put(CONTACT_NUMBER, "");
            }
            contentValue.put(IS_POSTED, "1");
            db.insert(TABLE_FRIDGE_MASTER, null, contentValue);
        }
    }

    public void insertNatureMaster(ArrayList<NatureMaster> arrDatatech) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrDatatech.size(); i++) {

            NatureMaster mCustomer_tech = arrDatatech.get(i);

            if (!checkIsNatureExist(mCustomer_tech.getTicket_no())) {

                ContentValues contentValue = new ContentValues();
                contentValue.put(TICKET_NO, mCustomer_tech.getTicket_no());
                contentValue.put(KEY_ORDER_ID, mCustomer_tech.getId());
                contentValue.put(SV_NATURE, mCustomer_tech.getNature_of_call());
                contentValue.put(SV_SERIAL_NO, mCustomer_tech.getSerial_number());
                contentValue.put(ASSETS_NO, mCustomer_tech.getAsset_number());
                contentValue.put(MODEL_NO, mCustomer_tech.getModel_number());
                contentValue.put(SV_BRANDING, mCustomer_tech.getBranding());

                if (mCustomer_tech.getOwner_name() != null) {
                    contentValue.put(OWNER_NAME, mCustomer_tech.getOwner_name());
                } else {
                    contentValue.put(OWNER_NAME, "");
                }

                if (mCustomer_tech.getOutlet_name() != null) {
                    contentValue.put(OUTLET_NAME, mCustomer_tech.getOutlet_name());
                } else {
                    contentValue.put(OUTLET_NAME, "");
                }

                if (mCustomer_tech.getLandmark() != null) {
                    contentValue.put(LANDMARK, mCustomer_tech.getLandmark());
                } else {
                    contentValue.put(LANDMARK, "");
                }

                if (mCustomer_tech.getRoad_street() != null) {
                    contentValue.put(LOCATION, mCustomer_tech.getRoad_street());
                } else {
                    contentValue.put(LOCATION, "");
                }

                if (mCustomer_tech.getTown() != null) {
                    contentValue.put(TOWN_VILLAGE, mCustomer_tech.getTown());
                } else {
                    contentValue.put(TOWN_VILLAGE, "");
                }
                if (mCustomer_tech.getCustomerphone() != null) {
                    contentValue.put(CONTACT_NUMBER, mCustomer_tech.getCustomerphone());
                } else {
                    contentValue.put(CONTACT_NUMBER, "");
                }

                if (mCustomer_tech.getCustomer_phone2() != null) {
                    contentValue.put(CONTACT_NUMBER2, mCustomer_tech.getCustomer_phone2());
                } else {
                    contentValue.put(CONTACT_NUMBER2, "");
                }
                if (mCustomer_tech.getDate() != null) {
                    contentValue.put(DATE_TIME, mCustomer_tech.getDate());
                } else {
                    contentValue.put(DATE_TIME, "");
                }

                if (mCustomer_tech.getDistrict() != null) {
                    contentValue.put(DISTRICT, mCustomer_tech.getDistrict());
                } else {
                    contentValue.put(DISTRICT, "");
                }
                contentValue.put(IS_POSTED, "0");
                db.insert(TABLE_NATURE_OF_CALL, null, contentValue);
            }

        }
    }

    //GET
    public ArrayList<ChillerTechnician> getChillerTechnicianList() {
        ArrayList<ChillerTechnician> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CHILLER_TECHNICIAN;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        ChillerTechnician item = new ChillerTechnician();
                        item.setFridge_id(cursor.getString(cursor.getColumnIndex(FRIDGE_ID)));
                        item.setFridge_code(cursor.getString(cursor.getColumnIndex(FRIDGE_CODE)));
                        item.setIr_id(cursor.getString(cursor.getColumnIndex(IR_ID)));
                        item.setSerial_number(cursor.getString(cursor.getColumnIndex(FRIDGE_SERIALNUMBER)));
                        item.setAsset_number(cursor.getString(cursor.getColumnIndex(FRIDGE_ASSETNUMBER)));
                        item.setModel_number(cursor.getString(cursor.getColumnIndex(FRIDGE_MODELNUMBER)));
                        item.setAcquisition(cursor.getString(cursor.getColumnIndex(FRIDGE_ASQUISITION)));
                        item.setManufacturer(cursor.getString(cursor.getColumnIndex(FRIDGE_MANUFACTURAR)));
                        item.setFridge_type(cursor.getString(cursor.getColumnIndex(FRIDGE_TYPE)));
                        item.setBranding(cursor.getString(cursor.getColumnIndex(FRIDGE_BRANDING)));

                        arrData.add(item);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //INSERT CHILLER TECHNICIAN CHECK
    public void insertChillerTachniciancheck(String arrDatatech, String fridge_id, String agreementid) {
        SQLiteDatabase db = this.getWritableDatabase();
        //for (int i = 0; i < arrDatatech.(); i++) {
        // ChillerTechnician mCustomer_tech = arrDatatech;

        ContentValues contentValue = new ContentValues();
        contentValue.put(IR_ID, arrDatatech);
        contentValue.put(FRIDGE_ID, fridge_id);
        contentValue.put(IS_POSTED, agreementid);
        db.insert(TABLE_CHILLER_TECHNICIAN_CHECK, null, contentValue);

    }

    public ArrayList<ChillerTechnician> getChillerTechnicianListcheck() {
        ArrayList<ChillerTechnician> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CHILLER_TECHNICIAN_CHECK;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        ChillerTechnician item = new ChillerTechnician();
                        item.setIr_id(cursor.getString(cursor.getColumnIndex(IR_ID)));
                        item.setFridge_id(cursor.getString(cursor.getColumnIndex(FRIDGE_ID)));
                        item.setAgreementID(cursor.getString(cursor.getColumnIndex(IS_POSTED)));
                        arrData.add(item);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //INSERT OTC
    public void insertDept(ArrayList<Customer> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Customer mCustomer = arrData.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(CUSTOMER_ID, mCustomer.getCustomerId());
            contentValue.put(CUSTOMER_CODE, mCustomer.getCustomerCode());
            contentValue.put(CUSTOMER_NAME, mCustomer.getCustomerName());
            contentValue.put(CUSTOMER_NAME2, mCustomer.getCustomerName2());
            contentValue.put(CUSTOMER_DIVISON, mCustomer.getCustDivison());
            contentValue.put(CUSTOMER_CHANNEL, mCustomer.getCustChannel());
            contentValue.put(CUSTOMER_ORG, mCustomer.getCustOrg());
            contentValue.put(CUSTOMER_PHONE, mCustomer.getCustPhone());
            contentValue.put(CUSTOMER_PHONE_OTHER, mCustomer.getCustomerphone1());
            contentValue.put(CUSTOMER_EMAIL, mCustomer.getCustEmail());
            contentValue.put(CUSTOMER_TYPE, mCustomer.getCustType());
            contentValue.put(CUSTOMER_REGION, mCustomer.getCustRegion());
            contentValue.put(CUSTOMER_SUBREGION, mCustomer.getCustSubRegion());
            contentValue.put(CUSTOMER_ADDRESS, mCustomer.getAddress());
            contentValue.put(BARCODE, mCustomer.getBarcode());
            contentValue.put(LATITUDE, mCustomer.getLatitude());
            contentValue.put(LONGITUDE, mCustomer.getLongitude());
            contentValue.put(CUSTOMER_PAYMENT_TYPE, "");
            contentValue.put(CUSTOMER_PAYMENT_TERM, mCustomer.getPaymentTerm());
            if (!mCustomer.getBalance().equalsIgnoreCase("")) {
                double balnce = Double.parseDouble(mCustomer.getBalance());
                contentValue.put(CUSTOMER_BALANCE, DecimalUtils.round(balnce, 2));
            } else {
                contentValue.put(CUSTOMER_BALANCE, mCustomer.getBalance());
            }
            contentValue.put(CUSTOMER_CREDIT_LIMIT, mCustomer.getCreditLimit());
            contentValue.put(CUSTOMER_RADIUS, mCustomer.getRadius());
            contentValue.put(MON_SEQ, mCustomer.getMonSqu().equals("") ? "0" : mCustomer.getMonSqu());
            contentValue.put(TUE_SEQ, mCustomer.getTueSqu().equals("") ? "0" : mCustomer.getTueSqu());
            contentValue.put(WED_SEQ, mCustomer.getWedSqu().equals("") ? "0" : mCustomer.getWedSqu());
            contentValue.put(THU_SEQ, mCustomer.getThuSqu().equals("") ? "0" : mCustomer.getThuSqu());
            contentValue.put(FRI_SEQ, mCustomer.getFriSqu().equals("") ? "0" : mCustomer.getFriSqu());
            contentValue.put(SAT_SEQ, mCustomer.getSatSqu().equals("") ? "0" : mCustomer.getSatSqu());
            contentValue.put(SUN_SEQ, mCustomer.getSunSqu().equals("") ? "0" : mCustomer.getSunSqu());
            contentValue.put(IS_FREEZE_ASSIGN, mCustomer.getIs_fridge_assign());
            contentValue.put(FRIDGER_CODE, mCustomer.getFridger_code());
            contentValue.put(ADDRESS_TWO, mCustomer.getAddress2());
            contentValue.put(CUST_STATE, mCustomer.getCustomerstate());
            contentValue.put(CUST_CITY, mCustomer.getCustomercity());
            contentValue.put(CUST_ZIP, mCustomer.getCustomerzip());
            contentValue.put(CUST_SALE, "0");
            contentValue.put(CUST_COLL, "0");
            contentValue.put(CUST_ORDER, "0");
            contentValue.put(CUST_RETURN, "0");
            contentValue.put(IS_NEW, "0");
            contentValue.put(DATA_MARK_FOR_POST, "Y");
            contentValue.put(IS_POSTED, "1");
            contentValue.put(CUSTOMER_VISIABLE, "0");
            contentValue.put(SALESMAN_ID, mCustomer.getSalesmanId());
            contentValue.put(CUSTOMER_TYPEID, mCustomer.getCustomerType());
            contentValue.put(CUSTOMER_CATEGORYID, mCustomer.getCustomerCategory());
            contentValue.put(CUSTOMER_CHANNEL_ID, mCustomer.getCustomerchannel());
            contentValue.put(CUSTOMER_SUB_CATEGORYID, mCustomer.getCustomerSubCategory());
            contentValue.put(ROUTE_ID, mCustomer.getRouteId());
            contentValue.put(KEY_TEMP_CUST, "0");

           /* Customer mCustomer = arrData.get(i);


            ContentValues contentValue = new ContentValues();
            contentValue.put(CUSTOMER_ID, mCustomer.getCustomerId());
            contentValue.put(CUSTOMER_CODE, mCustomer.getCustomerCode());
            contentValue.put(CUSTOMER_NAME, mCustomer.getCustomerName());
          //  contentValue.put(CUSTOMER_NAME2, mCustomer.getCustomerName2());
            contentValue.put(CUSTOMER_PHONE, mCustomer.getCustPhone());
            contentValue.put(CUSTOMER_EMAIL, mCustomer.getCustEmail());
            contentValue.put(CUSTOMER_TYPE, mCustomer.getCustType());
            contentValue.put(CUSTOMER_REGION, mCustomer.getCustRegion());
            contentValue.put(CUSTOMER_SUBREGION, mCustomer.getCustSubRegion());
            contentValue.put(BARCODE, mCustomer.getBarcode());
            contentValue.put(LATITUDE, mCustomer.getLatitude());
            contentValue.put(LONGITUDE, mCustomer.getLongitude());
            contentValue.put(CUSTOMER_DIVISON, mCustomer.getCustDivison());
           // contentValue.put(CUSTOMER_CHANNEL, mCustomer.getCustChannel());
           *//* contentValue.put(CUSTOMER_ORG, mCustomer.getCustOrg());
            contentValue.put(CUSTOMER_ADDRESS, mCustomer.getAddress());
            contentValue.put(CUSTOMER_PAYMENT_TYPE, "");


            contentValue.put(CUSTOMER_PAYMENT_TERM, mCustomer.getPaymentTerm());
            if (!mCustomer.getBalance().equalsIgnoreCase("")) {
                double balnce = Double.parseDouble(mCustomer.getBalance());
                contentValue.put(CUSTOMER_BALANCE, DecimalUtils.round(balnce, 2));
            } else {
                contentValue.put(CUSTOMER_BALANCE, mCustomer.getBalance());
            }
            contentValue.put(CUSTOMER_CREDIT_LIMIT, mCustomer.getCreditLimit());
            contentValue.put(CUSTOMER_RADIUS, mCustomer.getRadius());*//*
             *//* contentValue.put(MON_SEQ, mCustomer.getMonSqu().equals("") ? "0" : mCustomer.getMonSqu());
            contentValue.put(TUE_SEQ, mCustomer.getTueSqu().equals("") ? "0" : mCustomer.getTueSqu());
            contentValue.put(WED_SEQ, mCustomer.getWedSqu().equals("") ? "0" : mCustomer.getWedSqu());
            contentValue.put(THU_SEQ, mCustomer.getThuSqu().equals("") ? "0" : mCustomer.getThuSqu());
            contentValue.put(FRI_SEQ, mCustomer.getFriSqu().equals("") ? "0" : mCustomer.getFriSqu());
            contentValue.put(SAT_SEQ, mCustomer.getSatSqu().equals("") ? "0" : mCustomer.getSatSqu());
            contentValue.put(SUN_SEQ, mCustomer.getSunSqu().equals("") ? "0" : mCustomer.getSunSqu());*//*
            contentValue.put(SALESMAN_ID, mCustomer.getSalesmanId());
            contentValue.put(CUSTOMER_TYPEID, mCustomer.getCustomerType());
            contentValue.put(CUSTOMER_CATEGORYID, mCustomer.getCustomerCategory());*/

            if (!CheckIfDepotAlreadyExist(mCustomer.getCustomerId())) {
                db.insert(TABLE_DEPOT_CUSTOMER, null, contentValue);
            } else {
                db.update(TABLE_DEPOT_CUSTOMER,
                        contentValue, CUSTOMER_ID + "=?", new String[]{mCustomer.getCustomerId()});
            }
        }
    }

    //INSERT CUSTOMER HEADER
    public void addCustomer(Customer mCustomer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(CUSTOMER_ID, mCustomer.getCustomerId());
        contentValue.put(CUSTOMER_CODE, mCustomer.getCustomerCode());
        contentValue.put(CUSTOMER_NAME, mCustomer.getCustomerName());
        contentValue.put(CUSTOMER_NAME2, mCustomer.getCustomerName2());
        contentValue.put(CUSTOMER_DIVISON, mCustomer.getCustDivison());
        contentValue.put(CUSTOMER_CHANNEL, mCustomer.getCustChannel());
        contentValue.put(CUSTOMER_ORG, mCustomer.getCustOrg());
        contentValue.put(CUSTOMER_PHONE, mCustomer.getCustPhone());
        contentValue.put(CUSTOMER_PHONE_OTHER, mCustomer.getCustomerphone1());
        contentValue.put(CUSTOMER_EMAIL, mCustomer.getCustEmail());
        contentValue.put(CUSTOMER_TYPE, mCustomer.getCustType());
        contentValue.put(CUSTOMER_REGION, mCustomer.getCustRegion());
        contentValue.put(CUSTOMER_SUBREGION, mCustomer.getCustSubRegion());
        contentValue.put(CUSTOMER_ADDRESS, mCustomer.getAddress());
        contentValue.put(BARCODE, mCustomer.getBarcode());
        contentValue.put(LATITUDE, mCustomer.getLatitude());
        contentValue.put(LONGITUDE, mCustomer.getLongitude());
        contentValue.put(CUSTOMER_PAYMENT_TYPE, "");
        contentValue.put(CUSTOMER_PAYMENT_TERM, mCustomer.getPaymentTerm());
        contentValue.put(CUSTOMER_BALANCE, mCustomer.getBalance());
        contentValue.put(CUSTOMER_CREDIT_LIMIT, mCustomer.getCreditLimit());
        contentValue.put(CUSTOMER_RADIUS, mCustomer.getRadius());
        contentValue.put(MON_SEQ, mCustomer.getMonSqu());
        contentValue.put(TUE_SEQ, mCustomer.getTueSqu());
        contentValue.put(WED_SEQ, mCustomer.getWedSqu());
        contentValue.put(THU_SEQ, mCustomer.getThuSqu());
        contentValue.put(FRI_SEQ, mCustomer.getFriSqu());
        contentValue.put(SAT_SEQ, mCustomer.getSatSqu());
        contentValue.put(SUN_SEQ, mCustomer.getSunSqu());
        contentValue.put(IS_FREEZE_ASSIGN, mCustomer.getIs_fridge_assign());
        contentValue.put(FRIDGER_CODE, mCustomer.getFridger_code());
        contentValue.put(ADDRESS_TWO, mCustomer.getAddress2());
        contentValue.put(CUST_STATE, mCustomer.getCustomerstate());
        contentValue.put(CUST_CITY, mCustomer.getCustomercity());
        contentValue.put(CUST_ZIP, mCustomer.getCustomerzip());
        contentValue.put(CUST_SALE, "0");
        contentValue.put(CUST_COLL, "0");
        contentValue.put(CUST_ORDER, "0");
        contentValue.put(CUST_RETURN, "0");
        contentValue.put(ROUTE_ID, mCustomer.getRouteId());
        contentValue.put(LANGUAGE, mCustomer.getLanguage());
        contentValue.put(FRIDGE, mCustomer.getFridge());
        contentValue.put(TIN_NO, mCustomer.getTinNo());
        contentValue.put(IS_NEW, "1");
        contentValue.put(CUSTOMER_TYPEID, mCustomer.getCustomerType());
        contentValue.put(CUSTOMER_CATEGORYID, mCustomer.getCustomerCategory());
        contentValue.put(CUSTOMER_CHANNEL_ID, mCustomer.getCustomerchannel());
        contentValue.put(CUSTOMER_SUB_CATEGORYID, mCustomer.getCustomerSubCategory());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");
        contentValue.put(KEY_TEMP_CUST, "1");

        db.insert(TABLE_CUSTOMER, null, contentValue);
    }


    public void updateCustomer(String customerId, Customer mCustomer) {

        ContentValues contentValue = new ContentValues();
        //contentValue.put(CUSTOMER_ID, mCustomer.getCustomerId());
        contentValue.put(CUSTOMER_CODE, mCustomer.getCustomerCode());
        contentValue.put(CUSTOMER_NAME, mCustomer.getCustomerName());
        contentValue.put(CUSTOMER_NAME2, mCustomer.getCustomerName2());
        contentValue.put(CUSTOMER_DIVISON, mCustomer.getCustDivison());
        contentValue.put(CUSTOMER_CHANNEL, mCustomer.getCustChannel());
        contentValue.put(CUSTOMER_ORG, mCustomer.getCustOrg());
        contentValue.put(CUSTOMER_PHONE, mCustomer.getCustPhone());
        contentValue.put(CUSTOMER_PHONE_OTHER, mCustomer.getCustomerphone1());
        contentValue.put(CUSTOMER_EMAIL, mCustomer.getCustEmail());
        contentValue.put(CUSTOMER_TYPE, mCustomer.getCustType());
        contentValue.put(CUSTOMER_REGION, mCustomer.getCustRegion());
        contentValue.put(CUSTOMER_SUBREGION, mCustomer.getCustSubRegion());
        contentValue.put(CUSTOMER_ADDRESS, mCustomer.getAddress());
        contentValue.put(BARCODE, mCustomer.getBarcode());
        contentValue.put(LATITUDE, mCustomer.getLatitude());
        contentValue.put(LONGITUDE, mCustomer.getLongitude());
        contentValue.put(CUSTOMER_PAYMENT_TYPE, "");
        contentValue.put(CUSTOMER_PAYMENT_TERM, mCustomer.getPaymentTerm());
        contentValue.put(CUSTOMER_BALANCE, mCustomer.getBalance());
        contentValue.put(CUSTOMER_CREDIT_LIMIT, mCustomer.getCreditLimit());
        contentValue.put(CUSTOMER_RADIUS, mCustomer.getRadius());
        contentValue.put(MON_SEQ, mCustomer.getMonSqu());
        contentValue.put(TUE_SEQ, mCustomer.getTueSqu());
        contentValue.put(WED_SEQ, mCustomer.getWedSqu());
        contentValue.put(THU_SEQ, mCustomer.getThuSqu());
        contentValue.put(FRI_SEQ, mCustomer.getFriSqu());
        contentValue.put(SAT_SEQ, mCustomer.getSatSqu());
        contentValue.put(SUN_SEQ, mCustomer.getSunSqu());
        contentValue.put(IS_FREEZE_ASSIGN, mCustomer.getIs_fridge_assign());
        contentValue.put(FRIDGER_CODE, mCustomer.getFridger_code());
        contentValue.put(ADDRESS_TWO, mCustomer.getAddress2());
        contentValue.put(CUST_STATE, mCustomer.getCustomerstate());
        contentValue.put(CUST_CITY, mCustomer.getCustomercity());
        contentValue.put(CUST_ZIP, mCustomer.getCustomerzip());
        contentValue.put(CUST_SALE, "0");
        contentValue.put(CUST_COLL, "0");
        contentValue.put(CUST_ORDER, "0");
        contentValue.put(CUST_RETURN, "0");
        contentValue.put(ROUTE_ID, mCustomer.getRouteId());
        contentValue.put(LANGUAGE, mCustomer.getLanguage());
        contentValue.put(FRIDGE, mCustomer.getFridge());
        contentValue.put(TIN_NO, mCustomer.getTinNo());
        contentValue.put(IS_NEW, "0");
        contentValue.put(CUSTOMER_TYPEID, mCustomer.getCustomerType());
        contentValue.put(CUSTOMER_CATEGORYID, mCustomer.getCustomerCategory());
        contentValue.put(CUSTOMER_CHANNEL_ID, mCustomer.getCustomerchannel());
        contentValue.put(CUSTOMER_SUB_CATEGORYID, mCustomer.getCustomerSubCategory());
        contentValue.put(DATA_MARK_FOR_POST, "U");
        contentValue.put(IS_POSTED, "0");
        contentValue.put(KEY_TEMP_CUST, "1");

        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("check id--> " + customerId);

        db.update(TABLE_CUSTOMER,
                contentValue, CUSTOMER_ID + " = ? ", new String[]{customerId});
        // return i;
    }

    public void updateDepotCustomer(String customerId, Customer mCustomer) {

        ContentValues contentValue = new ContentValues();
        //contentValue.put(CUSTOMER_ID, mCustomer.getCustomerId());
        contentValue.put(CUSTOMER_CODE, mCustomer.getCustomerCode());
        contentValue.put(CUSTOMER_NAME, mCustomer.getCustomerName());
        contentValue.put(CUSTOMER_NAME2, mCustomer.getCustomerName2());
        contentValue.put(CUSTOMER_DIVISON, mCustomer.getCustDivison());
        contentValue.put(CUSTOMER_CHANNEL, mCustomer.getCustChannel());
        contentValue.put(CUSTOMER_ORG, mCustomer.getCustOrg());
        contentValue.put(CUSTOMER_PHONE, mCustomer.getCustPhone());
        contentValue.put(CUSTOMER_PHONE_OTHER, mCustomer.getCustomerphone1());
        contentValue.put(CUSTOMER_EMAIL, mCustomer.getCustEmail());
        contentValue.put(CUSTOMER_TYPE, mCustomer.getCustType());
        contentValue.put(CUSTOMER_REGION, mCustomer.getCustRegion());
        contentValue.put(CUSTOMER_SUBREGION, mCustomer.getCustSubRegion());
        contentValue.put(CUSTOMER_ADDRESS, mCustomer.getAddress());
        contentValue.put(BARCODE, mCustomer.getBarcode());
        contentValue.put(LATITUDE, mCustomer.getLatitude());
        contentValue.put(LONGITUDE, mCustomer.getLongitude());
        contentValue.put(CUSTOMER_PAYMENT_TYPE, "");
        contentValue.put(CUSTOMER_PAYMENT_TERM, mCustomer.getPaymentTerm());
        contentValue.put(CUSTOMER_BALANCE, mCustomer.getBalance());
        contentValue.put(CUSTOMER_CREDIT_LIMIT, mCustomer.getCreditLimit());
        contentValue.put(CUSTOMER_RADIUS, mCustomer.getRadius());
        contentValue.put(MON_SEQ, mCustomer.getMonSqu());
        contentValue.put(TUE_SEQ, mCustomer.getTueSqu());
        contentValue.put(WED_SEQ, mCustomer.getWedSqu());
        contentValue.put(THU_SEQ, mCustomer.getThuSqu());
        contentValue.put(FRI_SEQ, mCustomer.getFriSqu());
        contentValue.put(SAT_SEQ, mCustomer.getSatSqu());
        contentValue.put(SUN_SEQ, mCustomer.getSunSqu());
        contentValue.put(IS_FREEZE_ASSIGN, mCustomer.getIs_fridge_assign());
        contentValue.put(FRIDGER_CODE, mCustomer.getFridger_code());
        contentValue.put(ADDRESS_TWO, mCustomer.getAddress2());
        contentValue.put(CUST_STATE, mCustomer.getCustomerstate());
        contentValue.put(CUST_CITY, mCustomer.getCustomercity());
        contentValue.put(CUST_ZIP, mCustomer.getCustomerzip());
        contentValue.put(CUST_SALE, "0");
        contentValue.put(CUST_COLL, "0");
        contentValue.put(CUST_ORDER, "0");
        contentValue.put(CUST_RETURN, "0");
        contentValue.put(ROUTE_ID, mCustomer.getRouteId());
        contentValue.put(LANGUAGE, mCustomer.getLanguage());
        contentValue.put(FRIDGE, mCustomer.getFridge());
        contentValue.put(TIN_NO, mCustomer.getTinNo());
        contentValue.put(IS_NEW, "0");
        contentValue.put(CUSTOMER_TYPEID, mCustomer.getCustomerType());
        contentValue.put(CUSTOMER_CATEGORYID, mCustomer.getCustomerCategory());
        contentValue.put(CUSTOMER_CHANNEL_ID, mCustomer.getCustomerchannel());
        contentValue.put(CUSTOMER_SUB_CATEGORYID, mCustomer.getCustomerSubCategory());
        contentValue.put(DATA_MARK_FOR_POST, "U");
        contentValue.put(IS_POSTED, "0");
        contentValue.put(KEY_TEMP_CUST, "1");

        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("check id--> " + customerId);

        db.update(TABLE_DEPOT_CUSTOMER,
                contentValue, CUSTOMER_ID + " = ? ", new String[]{customerId});
        // return i;
    }

    //INSERT VISITED CUSTOMER HEADER
    public void insertVisitedCustomer(String custId, String startTime, String latitude, String longitude, String captLatitude, String capLongitude, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(CUSTOMER_ID, custId);
        contentValue.put(START_TIME, startTime);
        contentValue.put(END_TIME, "");
        contentValue.put(LATITUDE, latitude);
        contentValue.put(LONGITUDE, longitude);
        contentValue.put(CAPTURE_LATITUDE, captLatitude);
        contentValue.put(CAPTURE_LONGITUDE, capLongitude);
        contentValue.put(VISIT_STATUS, status);
        contentValue.put(DATA_MARK_FOR_POST, "N");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_CUSTOMER_VISIT, null, contentValue);

    }

    //riddhib
    //INSERT VISITED CUSTOMER HEADER SALES
    public void insertVisitedCustomersales(String custId, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(CUSTOMER_ID1, custId);
        contentValue.put(LATITUDE, latitude);
        contentValue.put(LONGITUDE, longitude);
        contentValue.put(DATA_MARK_FOR_POST, "N");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_CUSTOMER_VISIT_SALES, null, contentValue);

    }

    //GET Post Visit Request Items change riddhibhavik
    public HashMap<String, String> getPostVisitItemsINVOICE() {

        HashMap<String, String> mData = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_VISIT + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {

                    mData.put("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID1)));
                    mData.put("latitude", cursor.getString(cursor.getColumnIndex(LATITUDE)));
                    mData.put("longitude", cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mData;
    }

    //Update Customer Visit
    public int updateCustomerVisit(String endTime, String customerId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(END_TIME, endTime);

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CUSTOMER_VISIT,
                contentValues, CUSTOMER_ID + " = ? ", new String[]{customerId});
        return i;
    }

    //INSERT Item HEADER ONLY SINGLE
    public void insertItem(ArrayList<Item> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {
            Item mItem = arrData.get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(ITEM_ID, mItem.getItemId());
            contentValue.put(ITEM_CODE, mItem.getItemCode());
            contentValue.put(ITEM_NAME, mItem.getItemName());
            contentValue.put(ITEM_DESCRIPTION, mItem.getDescription());
            contentValue.put(ITEM_DESCRIPTION2, mItem.getDescription2());
            contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
            contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
            contentValue.put(ITEM_UPC, mItem.getUpc());
            contentValue.put(ITEM_UOM_PRICE, mItem.getUOMPrice());
            contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getAlternateUOMPrice());
            contentValue.put(ITEM_PACK_SIZE, mItem.getPackSize());
            contentValue.put(ITEM_CATEGORY, mItem.getCategory());
            contentValue.put(ITEM_SHELF_LIFE, mItem.getShelfLife());
            contentValue.put(ITEM_BRAND, mItem.getBrand());
            contentValue.put(ITEM_WEIGHT, mItem.getWeight());
            contentValue.put(ITEM_AGENT_EXCISE, mItem.getAgentExcise());
            contentValue.put(ITEM_DIRECT_EXCISE, mItem.getDirectsellexcise());
            contentValue.put(ITEM_IMAGE, mItem.getImage());
            contentValue.put(ITEM_BASE_VOLUME, mItem.getBaseVolume());
            contentValue.put(ITEM_ALTER_VOLUME, mItem.getAltVolume());

            if (!checkIsItemExist(mItem.getItemId())) {
                db.insert(TABLE_ITEM, null, contentValue);
            } else {
                db.update(TABLE_ITEM,
                        contentValue, ITEM_ID + "=?", new String[]{mItem.getItemId()});
            }

        }

    }

    //INSERT UOM HEADER ONLY SINGLE
    public void insertUOM(ArrayList<UOM> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            UOM mItem = arrData.get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(UOM_ID, mItem.getUomId());
            contentValue.put(UOM_NAME, mItem.getUomName());

            db.insert(TABLE_UOM, null, contentValue);
        }

    }

    //INSERT LOAD HEADER ONLY SINGLE
    public void insertLoad(ArrayList<Load> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Load mLoad = arrData.get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(LOAD_ID, mLoad.getLoad_no());
            contentValue.put(SUB_LOAD_ID, mLoad.getSub_loadId());
            contentValue.put(LOAD_DATE, mLoad.getDel_date());
            contentValue.put(IMAGE1, "");
            contentValue.put(DATA_MARK_FOR_POST, "N");
            contentValue.put(LOAD_IS_VERIFIED, "0");

            if (!checkIsLoadExist(mLoad.getLoad_no())) {
                db.insert(TABLE_LOAD_HEADER, null, contentValue);
            }

            for (int j = 0; j < mLoad.getLoadItems().size(); j++) {

                Item mItems = mLoad.getLoadItems().get(j);
                ContentValues contentValueItem = new ContentValues();
                contentValueItem.put(LOAD_ID, mLoad.getLoad_no());
                contentValueItem.put(SUB_LOAD_ID, mLoad.getSub_loadId());
                contentValueItem.put(ITEM_ID, mItems.getItemId());
                contentValueItem.put(ITEM_CODE, mItems.getItemCode());
                contentValueItem.put(ITEM_NAME, mItems.getItemName());

                if (mItems.getUomType().equals("1")) {
                    contentValueItem.put(BASE_UOM_NAME, getUOM(mItems.getUom()));
                    contentValueItem.put(ITEM_BASEUOM, mItems.getUom());
                    contentValueItem.put(ITEM_BASE_UOM_QTY, mItems.getQty());
                    contentValueItem.put(ITEM_BASE_PRICE, mItems.getPrice());
                    contentValueItem.put(ITEM_ALTER_UOM_QTY, "0");
                    contentValueItem.put(ITEM_ALRT_UOM_PRCE, "0");
                } else {
                    contentValueItem.put(ITEM_ALRT_UOM, mItems.getUom());
                    contentValueItem.put(ALTER_UOM_NAME, getUOM(mItems.getUom()));
                    contentValueItem.put(ITEM_ALTER_UOM_QTY, mItems.getQty());
                    contentValueItem.put(ITEM_ALRT_UOM_PRCE, mItems.getPrice());
                    contentValueItem.put(ITEM_BASE_UOM_QTY, "0");
                    contentValueItem.put(ITEM_BASE_PRICE, "0");
                }

                contentValueItem.put(IS_POSTED, "N");
                contentValueItem.put(LOAD_IS_VERIFIED, "0");

                if (!checkIsLoadItemExist(mLoad.getSub_loadId(), mItems.getItemId())) {
                    db.insert(TABLE_LOAD_ITEMS, null, contentValueItem);
                }
            }
        }
    }

    public Item getLoadItemExist(String load_no, String itemId) {
        Item mItem = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_ITEMS + " WHERE " +
                    SUB_LOAD_ID + "=?" + " AND "
                    + ITEM_ID + "=?", new String[]{load_no, itemId});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        mItem.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mItem;
    }


    public int getTotalLoadQty() {
        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_ITEMS, null);
            try {
                if (cursor.moveToFirst()) {
                    do {

                        int itemQty = 0;
                        int bsQty = 0, alQty = 0;

                        if (cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)) != null) {
                            bsQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        }

                        if (cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)) != null) {
                            alQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }

                        if (bsQty > 0 && alQty > 0) {
                            int upc = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                            itemQty = bsQty + (alQty * upc);
                        } else {
                            if (bsQty > 0) {
                                itemQty = bsQty;
                            } else {
                                itemQty = alQty;
                            }
                        }

                        qty += itemQty;
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;
    }

    //Update Load Items
    public int updateLoadLoadItems(ContentValues contentValueItem, String sub_lod_id, String itemId) {

        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.update(TABLE_LOAD_ITEMS,
                contentValueItem, SUB_LOAD_ID + " = ? AND " +
                        ITEM_ID + " = ?", new String[]{sub_lod_id, itemId});
        return i;
    }

    //Update Load Verify
    public int updateLoadVerified(String sub_load_no, String sign) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUB_LOAD_ID, sub_load_no);
        contentValues.put(IMAGE1, sign);
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(LOAD_IS_VERIFIED, "1");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_LOAD_HEADER,
                contentValues, SUB_LOAD_ID + " = ? ", new String[]{sub_load_no});
        return i;
    }

    //Update Load Post
    public int updateLoadPost(String sub_load_no, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUB_LOAD_ID, sub_load_no);
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(LOAD_IS_VERIFIED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_LOAD_HEADER,
                contentValues, SUB_LOAD_ID + " = ? ", new String[]{sub_load_no});
        return i;
    }

    //Update Invoice Post
    public int updateInvoicePost(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_INVOICE_HEADER,
                contentValues, SVH_CODE + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Invoice Post
    public int markForInvoicePost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_INVOICE_HEADER,
                contentValues, SVH_CODE + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Invoice Post
    public int markForLoadPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_LOAD_HEADER,
                contentValues, SUB_LOAD_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Invoice Post
    public int markForExchangeInvoicePost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_INVOICE_HEADER,
                contentValues, SVH_EXCHANGE_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Invoice Post
    public int markForExchangeReturnPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_RETURN_HEADER,
                contentValues, SVH_EXCHANGE_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForCustomerPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CUSTOMER,
                contentValues, CUSTOMER_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForChillerRequestPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CHILLER_REQUEST,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForCOmplaintPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_COMPLAIN,
                contentValues, COMPLAINID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForCompaignPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CAMPAIGN,
                contentValues, COMPAIGN_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForPlanoPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CAMPAIGN,
                contentValues, COMPAIGN_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForChillerAddPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CHILLER_ADD,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForChillerTrackPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CHILLER_TRACKING,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForServiceVisitPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_SERVICE_VISIT_POST,
                contentValues, TICKET_NO + "=?", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForFridgePost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_ADD_FRIDGE,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForCustomerUpdatePost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "U");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CUSTOMER,
                contentValues, CUSTOMER_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Customer Post
    public int markForDepotCustomerUpdatePost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "U");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_DEPOT_CUSTOMER,
                contentValues, CUSTOMER_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Order Post
    public int updateOrderPost(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_ORDER_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Order Post
    public int markForOrderPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_ORDER_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Return Post
    public int updateReturnPost(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_RETURN_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }


    //Update Invoice Post
    public int markForReturnPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_RETURN_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Return Post
    public int updateReturnRequestPost(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_RETURN_REQUEST_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Load Request Post
    public int updateLoadRequestPost(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_LOAD_REQUEST_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Load Request Post
    public int updateSalesmanLoadRequestPost(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_SALESMAN_LOAD_REQUEST_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Load Request Post
    public int markForLoadRequestPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_LOAD_REQUEST_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Load Request Post
    public int markForSalesmanLoadRequestPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_SALESMAN_LOAD_REQUEST_HEADER,
                contentValues, ORDER_NO + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Collection Post
    public int updateCollectionPost(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_COLLECTION,
                contentValues, KEY_ORDER_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //Update Collection Post
    public int markForCollectionPost(String orderNo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATA_MARK_FOR_POST, "M");
        contentValues.put(IS_POSTED, "0");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_COLLECTION,
                contentValues, KEY_ORDER_ID + " = ? ", new String[]{orderNo});
        return i;
    }

    //INSERT Vanstock
    public void insertVanStockItem(ArrayList<Item> arrData, Load mLoad) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            Item mItem = arrData.get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(LOAD_ID, mLoad.getLoad_no());
            contentValue.put(SUB_LOAD_ID, mLoad.getSub_loadId());
            contentValue.put(LOAD_DATE, mLoad.getDel_date());
            contentValue.put(ITEM_ID, mItem.getItemId());
            contentValue.put(ITEM_CODE, mItem.getItemCode());
            contentValue.put(ITEM_NAME, mItem.getItemName());
            contentValue.put(ITEM_CATEGORY, getItemCategory(mItem.getItemId()));
            contentValue.put(IS_POSTED, "N");
            contentValue.put(IS_VERIFY, "0");
            contentValue.put(IS_DELETE, "0");

            if (checkItemExitsInVanStock(mItem.getItemId())) {
                int existBaseQty, existAltQty, totBaseQty, totAlrtQty, existAclBaseQty, exitActAlQty, totalActBsQty, totalActAlQty;

                //Base Qty
                existBaseQty = Integer.parseInt(getVanStockExistItemQty(mItem.getItemId(), "Base"));
                int addBaseQty = Integer.parseInt(mItem.getBaseUOMQty());
                totBaseQty = existBaseQty + addBaseQty;
                existAclBaseQty = Integer.parseInt(getVanStockExistActualItemQty(mItem.getItemId(), "Base"));
                totalActBsQty = existAclBaseQty + addBaseQty;

                //Alter Qty
                existAltQty = Integer.parseInt(getVanStockExistItemQty(mItem.getItemId(), "Alter"));
                int addAlrtQty = Integer.parseInt(mItem.getAlterUOMQty());
                totAlrtQty = existAltQty + addAlrtQty;
                exitActAlQty = Integer.parseInt(getVanStockExistActualItemQty(mItem.getItemId(), "Alter"));
                totalActAlQty = exitActAlQty + addAlrtQty;

                updateVanStockItemQty(mItem.getItemId(), String.valueOf(totBaseQty), String.valueOf(totAlrtQty),
                        "" + totalActBsQty, "" + totalActAlQty);
            } else {

                contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
                contentValue.put(ACTUAL_BASE_QTY, mItem.getBaseUOMQty());
                contentValue.put(ITEM_BASE_UOM_QTY, mItem.getBaseUOMQty());
                contentValue.put(ITEM_BASE_PRICE, mItem.getBaseUOMPrice());
                contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
                contentValue.put(ACTUAL_ALTER_QTY, mItem.getAlterUOMQty());
                contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getAlterUOMQty());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getAlterUOMPrice());
                contentValue.put(IS_DELETE, "0");

                db.insert(TABLE_VAN_STOCK_ITEMS, null, contentValue);
            }

        }

    }

    //INSERT Vanstock
    public void insertReturnVanStockItem(Item mItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        if (checkItemExitsInVanStock(mItem.getItemId())) {
            int existBaseQty, existAltQty, totBaseQty, totAlrtQty, existAclBaseQty, totalActBsQty, existAclAlQty, totalActAlQty;
            //Base Qty
            existBaseQty = Integer.parseInt(getVanStockExistItemQty(mItem.getItemId(), "Base"));
            int addBaseQty = Integer.parseInt(mItem.getBaseUOMQty());
            totBaseQty = existBaseQty + addBaseQty;
            existAclBaseQty = Integer.parseInt(getVanStockExistActualItemQty(mItem.getItemId(), "Base"));
            totalActBsQty = existAclBaseQty + addBaseQty;

            //Alter Qty
            existAltQty = Integer.parseInt(getVanStockExistItemQty(mItem.getItemId(), "Alter"));
            int addAlrtQty = Integer.parseInt(mItem.getAlterUOMQty());
            totAlrtQty = existAltQty + addAlrtQty;
            existAclAlQty = Integer.parseInt(getVanStockExistActualItemQty(mItem.getItemId(), "Alter"));
            totalActAlQty = existAclAlQty + addAlrtQty;

            updateVanStockItemQty(mItem.getItemId(), String.valueOf(totBaseQty), String.valueOf(totAlrtQty), ""
                    + totalActBsQty, "" + totalActAlQty);
        } else {

            contentValue.put(LOAD_ID, "");
            contentValue.put(SUB_LOAD_ID, "");
            contentValue.put(LOAD_DATE, "");
            contentValue.put(ITEM_ID, mItem.getItemId());
            contentValue.put(ITEM_CODE, mItem.getItemCode());
            contentValue.put(ITEM_NAME, mItem.getItemName());
            contentValue.put(ITEM_CATEGORY, getItemCategory(mItem.getItemId()));
            contentValue.put(IS_POSTED, "N");
            contentValue.put(IS_VERIFY, "0");

            contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
            contentValue.put(ACTUAL_BASE_QTY, mItem.getBaseUOMQty());
            contentValue.put(ITEM_BASE_UOM_QTY, mItem.getBaseUOMQty());
            contentValue.put(ITEM_BASE_PRICE, mItem.getBaseUOMPrice());
            contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
            contentValue.put(ACTUAL_ALTER_QTY, mItem.getAlterUOMQty());
            contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getAlterUOMQty());
            contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getAlterUOMPrice());
            contentValue.put(IS_DELETE, "0");

            db.insert(TABLE_VAN_STOCK_ITEMS, null, contentValue);
        }

    }

    //INSERT UNLOAD Variance Item
    public void insertUnLoadVariance(Item mItems) {
        SQLiteDatabase db = this.getWritableDatabase();


        if (Integer.parseInt(mItems.getSaleBaseQty()) > 0 && Integer.parseInt(mItems.getSaleAltQty()) > 0) {
            //add Base QTY
            insertBaseUnloadItem(mItems);

            //add Alter QTY
            insertAlterUnloadItem(mItems);
        } else {
            ContentValues contentValueItem = new ContentValues();
            contentValueItem.put(ORDER_NO, "");
            contentValueItem.put(REASON_TYPE, mItems.getReasonCode());
            contentValueItem.put(UNLOAD_TYPE, UtilApp.getReasonName(mItems.getReasonCode()));
            contentValueItem.put(ITEM_ID, mItems.getItemId());
            contentValueItem.put(ITEM_CODE, mItems.getItemCode());
            contentValueItem.put(ITEM_NAME, mItems.getItemName());
            contentValueItem.put(ITEM_BASEUOM, mItems.getBaseUOM());
            contentValueItem.put(ITEM_BASE_UOM_QTY, mItems.getSaleBaseQty());
            contentValueItem.put(ITEM_BASE_PRICE, mItems.getBaseUOMPrice());
            contentValueItem.put(ITEM_ALRT_UOM, mItems.getAltrUOM());
            contentValueItem.put(ITEM_ALTER_UOM_QTY, mItems.getSaleAltQty());
            contentValueItem.put(ITEM_ALRT_UOM_PRCE, mItems.getAlterUOMPrice());
            contentValueItem.put(ITEM_CATEGORY, mItems.getCategory());
            contentValueItem.put(DATA_MARK_FOR_POST, "M");
            contentValueItem.put(IS_POSTED, "0");

            if (Integer.parseInt(mItems.getSaleBaseQty()) > 0) {
                contentValueItem.put(ITEM_UOM_TYPE, "Base");
            } else {
                contentValueItem.put(ITEM_UOM_TYPE, "Alter");
            }

            db.insert(TABLE_UNLOAD_VARIANCE, null, contentValueItem);

        }

    }

    public void insertBaseUnloadItem(Item mItems) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueItem = new ContentValues();

        contentValueItem.put(ORDER_NO, "");
        contentValueItem.put(REASON_TYPE, mItems.getReasonCode());
        contentValueItem.put(UNLOAD_TYPE, UtilApp.getReasonName(mItems.getReasonCode()));
        contentValueItem.put(ITEM_ID, mItems.getItemId());
        contentValueItem.put(ITEM_CODE, mItems.getItemCode());
        contentValueItem.put(ITEM_NAME, mItems.getItemName());
        contentValueItem.put(ITEM_BASEUOM, mItems.getBaseUOM());
        contentValueItem.put(ITEM_BASE_UOM_QTY, mItems.getSaleBaseQty());
        contentValueItem.put(ITEM_BASE_PRICE, mItems.getBaseUOMPrice());
        contentValueItem.put(ITEM_ALRT_UOM, mItems.getAltrUOM());
        contentValueItem.put(ITEM_ALTER_UOM_QTY, "0");
        contentValueItem.put(ITEM_ALRT_UOM_PRCE, "0");
        contentValueItem.put(ITEM_CATEGORY, mItems.getCategory());
        contentValueItem.put(DATA_MARK_FOR_POST, "M");
        contentValueItem.put(IS_POSTED, "0");

        contentValueItem.put(ITEM_UOM_TYPE, "Base");


        db.insert(TABLE_UNLOAD_VARIANCE, null, contentValueItem);
    }

    public void insertAlterUnloadItem(Item mItems) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueItem = new ContentValues();

        contentValueItem.put(ORDER_NO, "");
        contentValueItem.put(REASON_TYPE, mItems.getReasonCode());
        contentValueItem.put(UNLOAD_TYPE, UtilApp.getReasonName(mItems.getReasonCode()));
        contentValueItem.put(ITEM_ID, mItems.getItemId());
        contentValueItem.put(ITEM_CODE, mItems.getItemCode());
        contentValueItem.put(ITEM_NAME, mItems.getItemName());
        contentValueItem.put(ITEM_BASEUOM, mItems.getBaseUOM());
        contentValueItem.put(ITEM_BASE_UOM_QTY, "0");
        contentValueItem.put(ITEM_BASE_PRICE, "0");
        contentValueItem.put(ITEM_ALRT_UOM, mItems.getAltrUOM());
        contentValueItem.put(ITEM_ALTER_UOM_QTY, mItems.getSaleAltQty());
        contentValueItem.put(ITEM_ALRT_UOM_PRCE, mItems.getAlterUOMPrice());
        contentValueItem.put(ITEM_CATEGORY, mItems.getCategory());
        contentValueItem.put(DATA_MARK_FOR_POST, "M");
        contentValueItem.put(IS_POSTED, "0");

        contentValueItem.put(ITEM_UOM_TYPE, "Alter");


        db.insert(TABLE_UNLOAD_VARIANCE, null, contentValueItem);
    }


    //INSERT Delay Print
    public void insertDelayPrint(HashMap<String, String> arrData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(CUSTOMER_CODE, arrData.get(CUSTOMER_CODE));
        contentValue.put(KEY_ORDER_ID, arrData.get(KEY_ORDER_ID));
        contentValue.put(KEY_DOC_TYPE, arrData.get(KEY_DOC_TYPE));
        contentValue.put(KEY_DATA, arrData.get(KEY_DATA));

        db.insert(TABLE_DELAY_PRINT, null, contentValue);

    }

    //INSERT SALES INVOICE
    public void insertSalesInvoice(SalesInvoice salesInvoice) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(SVH_CODE, salesInvoice.inv_no);
        contentValue.put(SVH_INVOICE_TYPE, salesInvoice.inv_type);
        contentValue.put(SVH_INVOICE_TYPE_CODE, salesInvoice.inv_type_code);
        contentValue.put(SVH_CUST_CODE, salesInvoice.cust_code);
        contentValue.put(SVH_INVOICE_DATE, salesInvoice.inv_date);
        contentValue.put(SVH_DELVERY_DATE, salesInvoice.del_date);
        contentValue.put(SVH_CUST_NAME, salesInvoice.cust_name);
        contentValue.put(SVH_GROSS_VAL, salesInvoice.grossAmt);
        contentValue.put(SVH_TOT_AMT_SALES, salesInvoice.tot_amnt_sales);
        contentValue.put(SVH_PRE_VAT, salesInvoice.pre_VatAmt);
        contentValue.put(SVH_VAT_VAL, salesInvoice.vatAmt);
        contentValue.put(SVH_EXCISE_VAL, salesInvoice.exciseAmt);
        contentValue.put(SVH_DISCOUNT_VAL, salesInvoice.discountAmt);
        contentValue.put(DISCOUNT_ID, salesInvoice.discountId);
        contentValue.put(PROMOTIONAL_ID, salesInvoice.promotionId);
        contentValue.put(SVH_NET_VAL, salesInvoice.netAmt);
        contentValue.put(ITEM_QTY, salesInvoice.total_qty);
        contentValue.put(SVH_EXCHANGE_AMT, salesInvoice.exchangeAmt);
        contentValue.put(SVH_GR_NO, salesInvoice.grNo);
        contentValue.put(SVH_BR_NO, salesInvoice.brNo);
        contentValue.put(SVH_DELVERY_NO, salesInvoice.delivery_no);
        contentValue.put(SALE_TIME, salesInvoice.saletime);
        contentValue.put(SVH_EXCHANGE_NO, salesInvoice.exchangeNo);
        contentValue.put(SVH_ALT_QTY, salesInvoice.altQty);
        contentValue.put(SVH_BASE_QTY, salesInvoice.baseQty);
        contentValue.put(LATITUDE, salesInvoice.latitude);
        contentValue.put(LONGITUDE, salesInvoice.longitute);
        contentValue.put(SVH_PURCHASER_NO, salesInvoice.purchaseNo);
        contentValue.put(SVH_PURCHASER_NAME, salesInvoice.purchaseName);
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_INVOICE_HEADER, null, contentValue);
    }

    //INSERT SALES Summary
    public void insertSalesSummary(SalesSummary mSummary) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(SVH_CODE, mSummary.getTransactionNo());
        contentValue.put(SUMMARY_TYPE, mSummary.getTransactionType());
        contentValue.put(SVH_CUST_CODE, mSummary.getCustomerNo());
        contentValue.put(SVH_CUST_NAME, mSummary.getCustomerName());
        contentValue.put(SVH_TOT_AMT_SALES, mSummary.getTotalSales());
        contentValue.put(DISCOUNT_AMT, mSummary.getDiscounts());

        db.insert(TABLE_SALE_SUMMARY, null, contentValue);
    }

    //INSERT SALES INVOICEs Items
    public void insertSalesInvoiceItems(Item mItem, String invNo, String custCode, String isExchange) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(SVH_CODE, invNo);
        contentValue.put(ITEM_ID, mItem.getItemId());
        contentValue.put(ITEM_CODE, mItem.getItemCode());
        contentValue.put(ITEM_NAME, mItem.getItemName());
        contentValue.put(ITEM_PRICE, mItem.getPrice());

        if (Integer.parseInt(mItem.getSaleBaseQty()) > 0 && Integer.parseInt(mItem.getSaleAltQty()) > 0) {

            //add Base QTY
            insertBaseItem(invNo, custCode, mItem, isExchange);

            //add Alter QTY
            insertAlterUOMItem(invNo, custCode, mItem, isExchange);

        } else {

            contentValue.put(IS_EXCHANGE, isExchange);
            contentValue.put(ITEM_QTY, mItem.getSaleQty());
            contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
            contentValue.put(ITEM_BASE_UOM_QTY, mItem.getSaleBaseQty());
            contentValue.put(ITEM_BASE_PRICE, mItem.getSaleBasePrice());
            contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
            contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getSaleAltQty());
            contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getSaleAltPrice());
            contentValue.put(ITEM_CATEGORY, mItem.getCategory());
            contentValue.put(CUSTOMER_CODE, custCode);
            contentValue.put(ITEM_PRE_VAT, mItem.getPreVatAmt());
            contentValue.put(ITEM_VAT_VAL, mItem.getVatAmt());
            contentValue.put(ITEM_EXCISE_VAL, mItem.getExciseAmt());
            contentValue.put(ITEM_NET_VAL, mItem.getNetAmt());
            contentValue.put(IS_FREE_ITEM, mItem.getIsFreeItem());
            contentValue.put(BASE_PRCE, mItem.getUOMPrice());
            contentValue.put(ALRT_PRCE, mItem.getAlterUOMPrice());
            if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
                contentValue.put(DISCOUNT, mItem.getDiscountPer());
                contentValue.put(DISCOUNT_AMT, mItem.getDiscountAmt());
            } else {
                contentValue.put(DISCOUNT, mItem.getDiscountAlPer());
                contentValue.put(DISCOUNT_AMT, mItem.getDiscountAlAmt());
            }

            if (mItem.getPerentItemId() != null) {
                contentValue.put(PROMOTIONAL_ID, mItem.getPerentItemId());
            } else {
                contentValue.put(PROMOTIONAL_ID, "");
            }

            db.insert(TABLE_INVOICE_ITEMS, null, contentValue);
        }

    }

    public void insertBaseItem(String invNo, String custCode, Item mItem, String isExchange) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(SVH_CODE, invNo);
        contentValue.put(ITEM_ID, mItem.getItemId());
        contentValue.put(ITEM_CODE, mItem.getItemCode());
        contentValue.put(ITEM_NAME, mItem.getItemName());
        contentValue.put(ITEM_PRICE, mItem.getSaleBasePrice());

        contentValue.put(IS_EXCHANGE, isExchange);
        contentValue.put(ITEM_QTY, mItem.getSaleBaseQty());
        contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
        contentValue.put(ITEM_BASE_UOM_QTY, mItem.getSaleBaseQty());
        contentValue.put(ITEM_BASE_PRICE, mItem.getSaleBasePrice());
        contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
        contentValue.put(ITEM_ALTER_UOM_QTY, "0");
        contentValue.put(ITEM_ALRT_UOM_PRCE, "0");
        contentValue.put(BASE_PRCE, mItem.getUOMPrice());
        contentValue.put(ALRT_PRCE, "0");
        contentValue.put(ITEM_CATEGORY, mItem.getCategory());
        contentValue.put(CUSTOMER_CODE, custCode);
        contentValue.put(IS_FREE_ITEM, mItem.getIsFreeItem());
        contentValue.put(DISCOUNT, mItem.getDiscount());
        contentValue.put(DISCOUNT_AMT, mItem.getDiscountAmt());

        double itemVatAmt = UtilApp.getVat(Double.parseDouble(mItem.getSaleBasePrice()));
        double itemPreVatAmt = Double.parseDouble(mItem.getSaleBasePrice()) - itemVatAmt;

        double itemExcise = 0;
        double itemNet = itemPreVatAmt - itemExcise;

        contentValue.put(ITEM_PRE_VAT, "" + Math.round(itemPreVatAmt));
        contentValue.put(ITEM_VAT_VAL, "" + Math.round(itemVatAmt));
        contentValue.put(ITEM_EXCISE_VAL, "" + Math.round(itemExcise));
        contentValue.put(ITEM_NET_VAL, "" + Math.round(itemNet));
        contentValue.put(DISCOUNT, mItem.getDiscountPer());
        contentValue.put(DISCOUNT_AMT, mItem.getDiscountAmt());

        if (mItem.getPerentItemId() != null) {
            contentValue.put(PROMOTIONAL_ID, mItem.getPerentItemId());
        } else {
            contentValue.put(PROMOTIONAL_ID, "");
        }

        db.insert(TABLE_INVOICE_ITEMS, null, contentValue);

    }

    public void insertAlterUOMItem(String invNo, String custCode, Item mItem, String isExchange) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(SVH_CODE, invNo);
        contentValue.put(ITEM_ID, mItem.getItemId());
        contentValue.put(ITEM_CODE, mItem.getItemCode());
        contentValue.put(ITEM_NAME, mItem.getItemName());
        contentValue.put(ITEM_PRICE, mItem.getSaleAltPrice());

        contentValue.put(IS_EXCHANGE, isExchange);
        contentValue.put(ITEM_QTY, mItem.getSaleAltQty());
        contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
        contentValue.put(ITEM_BASE_UOM_QTY, "0");
        contentValue.put(ITEM_BASE_PRICE, "0");
        contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
        contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getSaleAltQty());
        contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getSaleAltPrice());
        contentValue.put(ITEM_CATEGORY, mItem.getCategory());
        contentValue.put(CUSTOMER_CODE, custCode);
        contentValue.put(IS_FREE_ITEM, mItem.getIsFreeItem());
        contentValue.put(BASE_PRCE, "0");
        contentValue.put(ALRT_PRCE, mItem.getAlterUOMPrice());
        contentValue.put(DISCOUNT, mItem.getDiscount());
        contentValue.put(DISCOUNT_AMT, mItem.getDiscountAmt());

        double itemVatAmt = UtilApp.getVat(Double.parseDouble(mItem.getSaleAltPrice()));
        double itemPreVatAmt = Double.parseDouble(mItem.getSaleAltPrice()) - itemVatAmt;

        double itemExcise = 0;
        double itemNet = itemPreVatAmt - itemExcise;

        contentValue.put(ITEM_PRE_VAT, "" + Math.round(itemPreVatAmt));
        contentValue.put(ITEM_VAT_VAL, "" + Math.round(itemVatAmt));
        contentValue.put(ITEM_EXCISE_VAL, "" + Math.round(itemExcise));
        contentValue.put(ITEM_NET_VAL, "" + Math.round(itemNet));
        contentValue.put(DISCOUNT, mItem.getDiscountAlPer());
        contentValue.put(DISCOUNT_AMT, mItem.getDiscountAlAmt());

        if (mItem.getPerentItemId() != null) {
            contentValue.put(PROMOTIONAL_ID, mItem.getPerentItemId());
        } else {
            contentValue.put(PROMOTIONAL_ID, "");
        }

        db.insert(TABLE_INVOICE_ITEMS, null, contentValue);
    }

    //Update Returns Items
    public void updateReturnItems(String orderNo, String orderDate, String orderAmt, String customerCode, String returnType,
                                  String salesmanId, String vatAmt, String preVatAmt, String excise, String netAmt,
                                  ArrayList<Item> mItem, String exchangeNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHead = new ContentValues();


        contentValueHead.put(ORDER_DATE, orderDate);
        contentValueHead.put(ORDER_AMOUNT, orderAmt);
        contentValueHead.put(CUSTOMER_ID, customerCode);
        contentValueHead.put(RETURN_TYPE, returnType);
        contentValueHead.put(SALESMAN_ID, salesmanId);
        contentValueHead.put(ITEM_VAT_VAL, vatAmt);
        contentValueHead.put(ITEM_PRE_VAT, preVatAmt);
        contentValueHead.put(ITEM_EXCISE_VAL, excise);
        contentValueHead.put(ITEM_NET_VAL, netAmt);
        contentValueHead.put(SVH_EXCHANGE_NO, exchangeNo);
        contentValueHead.put(IS_RETURN_LIST, "1");
        contentValueHead.put(IS_PARTIAL, "1");
        contentValueHead.put(DATA_MARK_FOR_POST, "M");
        contentValueHead.put(IS_POSTED, "0");

        db.update(TABLE_RETURN_HEADER, contentValueHead, ORDER_NO + " = ? ", new String[]{orderNo});

        for (int i = 0; i < mItem.size(); i++) {
            ContentValues contentValue = new ContentValues();

            if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0 && Integer.parseInt(mItem.get(i).getSaleAltQty()) > 0) {

                //Insert Base UOM Item
                insertORDBaseItem(orderNo, orderDate, mItem.get(i), "Return", returnType, "");

                //Insert Alter UOM Item
                insertORDAlterItem(orderNo, orderDate, mItem.get(i), "Return", returnType, "");

                if (returnType.equalsIgnoreCase("Good")) {
                    Item rlItem = new Item();
                    rlItem.setItemId(mItem.get(i).getItemId());
                    rlItem.setItemCode(mItem.get(i).getItemCode());
                    rlItem.setItemName(mItem.get(i).getItemName());
                    rlItem.setBaseUOM(mItem.get(i).getBaseUOM());
                    rlItem.setBaseUOMQty(mItem.get(i).getSaleBaseQty());
                    rlItem.setAltrUOM(mItem.get(i).getAltrUOM());
                    rlItem.setAlterUOMQty(mItem.get(i).getSaleAltQty());
                    rlItem.setBaseUOMPrice(mItem.get(i).getBaseUOMPrice());
                    rlItem.setAlterUOMPrice(mItem.get(i).getAlterUOMPrice());
                    insertReturnVanStockItem(rlItem);
                }
            } else {

                contentValue.put(ORDER_NO, orderNo);
                contentValue.put(ORDER_DATE, orderDate);
                contentValue.put(RETURN_TYPE, returnType);
                contentValue.put(REASON_TYPE, mItem.get(i).getReasonCode());
                contentValue.put(ITEM_ID, mItem.get(i).getItemId());
                contentValue.put(ITEM_CODE, mItem.get(i).getItemCode());
                contentValue.put(ITEM_NAME, mItem.get(i).getItemName());
                contentValue.put(ITEM_BASEUOM, mItem.get(i).getBaseUOM());
                contentValue.put(ITEM_BASE_UOM_QTY, mItem.get(i).getSaleBaseQty());
                contentValue.put(ITEM_BASE_PRICE, mItem.get(i).getSaleBasePrice());
                contentValue.put(ITEM_ALRT_UOM, mItem.get(i).getAltrUOM());
                contentValue.put(ITEM_ALTER_UOM_QTY, mItem.get(i).getSaleAltQty());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.get(i).getSaleAltPrice());
                contentValue.put(ITEM_CATEGORY, mItem.get(i).getCategory());
                contentValue.put(ITEM_PRE_VAT, mItem.get(i).getPreVatAmt());
                contentValue.put(ITEM_VAT_VAL, mItem.get(i).getVatAmt());
                contentValue.put(ITEM_EXCISE_VAL, mItem.get(i).getExciseAmt());
                contentValue.put(ITEM_NET_VAL, mItem.get(i).getNetAmt());
                contentValue.put(ALRT_PRCE, mItem.get(i).getAlterUOMPrice());
                contentValue.put(BASE_PRCE, mItem.get(i).getUOMPrice());

                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(ITEM_UOM_TYPE, "Base");
                } else {
                    contentValue.put(ITEM_UOM_TYPE, "Alter");
                }

                if (returnType.equalsIgnoreCase("Good")) {
                    Item rlItem = new Item();
                    rlItem.setItemId(mItem.get(i).getItemId());
                    rlItem.setItemCode(mItem.get(i).getItemCode());
                    rlItem.setItemName(mItem.get(i).getItemName());
                    rlItem.setBaseUOM(mItem.get(i).getBaseUOM());
                    rlItem.setAltrUOM(mItem.get(i).getAltrUOM());

                    if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                        rlItem.setBaseUOMQty(mItem.get(i).getSaleBaseQty());
                        rlItem.setAlterUOMQty("0");
                    } else {
                        rlItem.setAlterUOMQty(mItem.get(i).getSaleAltQty());
                        rlItem.setBaseUOMQty("0");
                    }
                    rlItem.setBaseUOMPrice(mItem.get(i).getBaseUOMPrice());
                    rlItem.setAlterUOMPrice(mItem.get(i).getAlterUOMPrice());
                    insertReturnVanStockItem(rlItem);
                }

                db.insert(TABLE_RETURN_ITEMS, null, contentValue);
            }

        }

    }

    //INSERT Returns Items
    public void insertReturnItems(String orderNo, String orderDate, String orderAmt, String customerCode, String returnType,
                                  String salesmanId, String vatAmt, String preVatAmt, String excise, String netAmt,
                                  ArrayList<Item> mItem, String exchangeNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHead = new ContentValues();


        contentValueHead.put(KEY_ORDER_ID, orderNo);
        contentValueHead.put(ORDER_NO, orderNo);
        contentValueHead.put(ORDER_DATE, orderDate);
        contentValueHead.put(ORDER_AMOUNT, orderAmt);
        contentValueHead.put(CUSTOMER_ID, customerCode);
        contentValueHead.put(RETURN_TYPE, returnType);
        contentValueHead.put(SALESMAN_ID, salesmanId);
        contentValueHead.put(ITEM_VAT_VAL, vatAmt);
        contentValueHead.put(ITEM_PRE_VAT, preVatAmt);
        contentValueHead.put(ITEM_EXCISE_VAL, excise);
        contentValueHead.put(ITEM_NET_VAL, netAmt);
        contentValueHead.put(SVH_EXCHANGE_NO, exchangeNo);
        contentValueHead.put(IS_RETURN_LIST, "0");
        contentValueHead.put(IS_PARTIAL, "0");
        contentValueHead.put(DATA_MARK_FOR_POST, "M");
        contentValueHead.put(IS_POSTED, "0");

        db.insert(TABLE_RETURN_HEADER, null, contentValueHead);


        for (int i = 0; i < mItem.size(); i++) {
            ContentValues contentValue = new ContentValues();

            if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0 && Integer.parseInt(mItem.get(i).getSaleAltQty()) > 0) {

                //Insert Base UOM Item
                insertORDBaseItem(orderNo, orderDate, mItem.get(i), "Return", returnType, "");

                //Insert Alter UOM Item
                insertORDAlterItem(orderNo, orderDate, mItem.get(i), "Return", returnType, "");

                if (returnType.equalsIgnoreCase("Good")) {
                    Item rlItem = new Item();
                    rlItem.setItemId(mItem.get(i).getItemId());
                    rlItem.setItemCode(mItem.get(i).getItemCode());
                    rlItem.setItemName(mItem.get(i).getItemName());
                    rlItem.setBaseUOM(mItem.get(i).getBaseUOM());
                    rlItem.setBaseUOMQty(mItem.get(i).getSaleBaseQty());
                    rlItem.setAltrUOM(mItem.get(i).getAltrUOM());
                    rlItem.setAlterUOMQty(mItem.get(i).getSaleAltQty());
                    rlItem.setBaseUOMPrice(mItem.get(i).getBaseUOMPrice());
                    rlItem.setAlterUOMPrice(mItem.get(i).getAlterUOMPrice());
                    insertReturnVanStockItem(rlItem);
                }
            } else {

                contentValue.put(ORDER_NO, orderNo);
                contentValue.put(ORDER_DATE, orderDate);
                contentValue.put(RETURN_TYPE, returnType);
                contentValue.put(REASON_TYPE, mItem.get(i).getReasonCode());
                contentValue.put(ITEM_ID, mItem.get(i).getItemId());
                contentValue.put(ITEM_CODE, mItem.get(i).getItemCode());
                contentValue.put(ITEM_NAME, mItem.get(i).getItemName());
                contentValue.put(ITEM_BASEUOM, mItem.get(i).getBaseUOM());
                contentValue.put(ITEM_BASE_UOM_QTY, mItem.get(i).getSaleBaseQty());
                contentValue.put(ITEM_BASE_PRICE, mItem.get(i).getSaleBasePrice());
                contentValue.put(ITEM_ALRT_UOM, mItem.get(i).getAltrUOM());
                contentValue.put(ITEM_ALTER_UOM_QTY, mItem.get(i).getSaleAltQty());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.get(i).getSaleAltPrice());
                contentValue.put(ITEM_CATEGORY, mItem.get(i).getCategory());
                contentValue.put(ITEM_PRE_VAT, mItem.get(i).getPreVatAmt());
                contentValue.put(ITEM_VAT_VAL, mItem.get(i).getVatAmt());
                contentValue.put(ITEM_EXCISE_VAL, mItem.get(i).getExciseAmt());
                contentValue.put(ITEM_NET_VAL, mItem.get(i).getNetAmt());
                contentValue.put(ALRT_PRCE, mItem.get(i).getAlterUOMPrice());
                contentValue.put(BASE_PRCE, mItem.get(i).getUOMPrice());
                contentValue.put(EXPIRY_DATE, mItem.get(i).getAgentExcise());

                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(ITEM_UOM_TYPE, "Base");
                } else {
                    contentValue.put(ITEM_UOM_TYPE, "Alter");
                }

                if (returnType.equalsIgnoreCase("Good")) {
                    Item rlItem = new Item();
                    rlItem.setItemId(mItem.get(i).getItemId());
                    rlItem.setItemCode(mItem.get(i).getItemCode());
                    rlItem.setItemName(mItem.get(i).getItemName());
                    rlItem.setBaseUOM(mItem.get(i).getBaseUOM());
                    rlItem.setAltrUOM(mItem.get(i).getAltrUOM());

                    if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                        rlItem.setBaseUOMQty(mItem.get(i).getSaleBaseQty());
                        rlItem.setAlterUOMQty("0");
                    } else {
                        rlItem.setAlterUOMQty(mItem.get(i).getSaleAltQty());
                        rlItem.setBaseUOMQty("0");
                    }
                    rlItem.setBaseUOMPrice(mItem.get(i).getBaseUOMPrice());
                    rlItem.setAlterUOMPrice(mItem.get(i).getAlterUOMPrice());
                    insertReturnVanStockItem(rlItem);
                }

                db.insert(TABLE_RETURN_ITEMS, null, contentValue);
            }

        }

    }

    //INSERT Returns Items
    public void insertORReturnItems(String orderNo, String orderDate, String orderAmt, String customerCode, String returnType,
                                    String salesmanId, String vatAmt, String preVatAmt, String excise, String netAmt,
                                    ArrayList<Item> mItem, String exchangeNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHead = new ContentValues();

        contentValueHead.put(KEY_ORDER_ID, orderNo);
        contentValueHead.put(ORDER_NO, orderNo);
        contentValueHead.put(ORDER_DATE, orderDate);
        contentValueHead.put(ORDER_AMOUNT, orderAmt);
        contentValueHead.put(CUSTOMER_ID, customerCode);
        contentValueHead.put(RETURN_TYPE, returnType);
        contentValueHead.put(SALESMAN_ID, salesmanId);
        contentValueHead.put(ITEM_VAT_VAL, vatAmt);
        contentValueHead.put(ITEM_PRE_VAT, preVatAmt);
        contentValueHead.put(ITEM_EXCISE_VAL, excise);
        contentValueHead.put(ITEM_NET_VAL, netAmt);
        contentValueHead.put(SVH_EXCHANGE_NO, exchangeNo);
        contentValueHead.put(IS_RETURN_LIST, "0");
        contentValueHead.put(IS_PARTIAL, "0");
        contentValueHead.put(DATA_MARK_FOR_POST, "M");
        contentValueHead.put(IS_POSTED, "0");

        db.insert(TABLE_RETURN_HEADER, null, contentValueHead);


        for (int i = 0; i < mItem.size(); i++) {

            ArrayList<UOM> arrUOM = mItem.get(i).getArrSelectUOM();

            for (int j = 0; j < arrUOM.size(); j++) {

                ContentValues contentValue = new ContentValues();
                contentValue.put(ORDER_NO, orderNo);
                contentValue.put(ORDER_DATE, orderDate);
                contentValue.put(RETURN_TYPE, returnType);
                contentValue.put(REASON_TYPE, arrUOM.get(j).getUomreasonCode());
                contentValue.put(ITEM_ID, mItem.get(i).getItemId());
                contentValue.put(ITEM_CODE, mItem.get(i).getItemCode());
                contentValue.put(ITEM_NAME, mItem.get(i).getItemName());
                contentValue.put(EXPIRY_DATE, arrUOM.get(j).getUomExp());
                contentValue.put(ITEM_UOM_TYPE, arrUOM.get(j).getUomType());
                contentValue.put(ITEM_CATEGORY, mItem.get(i).getCategory());
                contentValue.put(ITEM_PRE_VAT, arrUOM.get(j).getUomPrevat());
                contentValue.put(ITEM_VAT_VAL, arrUOM.get(j).getUomVat());
                contentValue.put(ITEM_EXCISE_VAL, arrUOM.get(j).getUomExcise());
                contentValue.put(ITEM_NET_VAL, arrUOM.get(j).getUomNet());

                if (arrUOM.get(j).getUomType().equals("Base")) {
                    contentValue.put(ITEM_BASEUOM, arrUOM.get(j).getUomId());
                    contentValue.put(ITEM_BASE_UOM_QTY, arrUOM.get(j).getUomQty());
                    contentValue.put(ITEM_BASE_PRICE, arrUOM.get(j).getUomGross());
                    contentValue.put(ITEM_ALRT_UOM, "");
                    contentValue.put(ITEM_ALTER_UOM_QTY, "0");
                    contentValue.put(ITEM_ALRT_UOM_PRCE, "0");
                    contentValue.put(ALRT_PRCE, "0");
                    contentValue.put(BASE_PRCE, arrUOM.get(j).getUomPrice());
                } else {
                    contentValue.put(ITEM_ALRT_UOM, arrUOM.get(j).getUomId());
                    contentValue.put(ITEM_ALTER_UOM_QTY, arrUOM.get(j).getUomQty());
                    contentValue.put(ITEM_ALRT_UOM_PRCE, arrUOM.get(j).getUomGross());
                    contentValue.put(ITEM_BASEUOM, "");
                    contentValue.put(ITEM_BASE_UOM_QTY, "0");
                    contentValue.put(ITEM_BASE_PRICE, "0");
                    contentValue.put(BASE_PRCE, "0");
                    contentValue.put(ALRT_PRCE, arrUOM.get(j).getUomPrice());
                }

                db.insert(TABLE_RETURN_ITEMS, null, contentValue);
            }

            if (returnType.equalsIgnoreCase("Good")) {
                Item rlItem = new Item();
                rlItem.setItemId(mItem.get(i).getItemId());
                rlItem.setItemCode(mItem.get(i).getItemCode());
                rlItem.setItemName(mItem.get(i).getItemName());
                rlItem.setBaseUOM(mItem.get(i).getBaseUOM());
                rlItem.setBaseUOMQty(mItem.get(i).getSaleBaseQty());
                rlItem.setAltrUOM(mItem.get(i).getAltrUOM());
                rlItem.setAlterUOMQty(mItem.get(i).getSaleAltQty());
                rlItem.setBaseUOMPrice(mItem.get(i).getBaseUOMPrice());
                rlItem.setAlterUOMPrice(mItem.get(i).getAlterUOMPrice());
                insertReturnVanStockItem(rlItem);
            }

        }

    }

    //INSERT Returns Items
    public void insertReturnRequestItems(String orderNo, String orderDate, String orderAmt, String customerCode, String returnType,
                                         String salesmanId, String vatAmt, String preVatAmt, String excise, String netAmt,
                                         ArrayList<Item> mItem, String exchangeNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHead = new ContentValues();

        contentValueHead.put(ORDER_NO, orderNo);
        contentValueHead.put(ORDER_DATE, orderDate);
        contentValueHead.put(ORDER_AMOUNT, orderAmt);
        contentValueHead.put(CUSTOMER_ID, customerCode);
        contentValueHead.put(RETURN_TYPE, returnType);
        contentValueHead.put(SALESMAN_ID, salesmanId);
        contentValueHead.put(ITEM_VAT_VAL, vatAmt);
        contentValueHead.put(ITEM_PRE_VAT, preVatAmt);
        contentValueHead.put(ITEM_EXCISE_VAL, excise);
        contentValueHead.put(ITEM_NET_VAL, netAmt);
        contentValueHead.put(SVH_EXCHANGE_NO, exchangeNo);
        contentValueHead.put(DATA_MARK_FOR_POST, "M");
        contentValueHead.put(IS_POSTED, "0");

        db.insert(TABLE_RETURN_REQUEST_HEADER, null, contentValueHead);


        for (int i = 0; i < mItem.size(); i++) {
            ContentValues contentValue = new ContentValues();

            if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0 && Integer.parseInt(mItem.get(i).getSaleAltQty()) > 0) {

                //Insert Base UOM Item
                insertORDBaseItem(orderNo, orderDate, mItem.get(i), "Return Request", returnType, "");

                //Insert Alter UOM Item
                insertORDAlterItem(orderNo, orderDate, mItem.get(i), "Return Request", returnType, "");

//                if (returnType.equalsIgnoreCase("Good")) {
//                    Item rlItem = new Item();
//                    rlItem.setItemId(mItem.get(i).getItemId());
//                    rlItem.setItemCode(mItem.get(i).getItemCode());
//                    rlItem.setItemName(mItem.get(i).getItemName());
//                    rlItem.setBaseUOM(mItem.get(i).getBaseUOM());
//                    rlItem.setBaseUOMQty(mItem.get(i).getSaleBaseQty());
//                    rlItem.setAltrUOM(mItem.get(i).getAltrUOM());
//                    rlItem.setAlterUOMQty(mItem.get(i).getSaleAltQty());
//                    rlItem.setBaseUOMPrice(mItem.get(i).getBaseUOMPrice());
//                    rlItem.setAlterUOMPrice(mItem.get(i).getAlterUOMPrice());
//                    insertReturnVanStockItem(rlItem);
//                }
            } else {

                contentValue.put(ORDER_NO, orderNo);
                contentValue.put(ORDER_DATE, orderDate);
                contentValue.put(RETURN_TYPE, returnType);
                contentValue.put(REASON_TYPE, mItem.get(i).getReasonCode());
                contentValue.put(ITEM_ID, mItem.get(i).getItemId());
                contentValue.put(ITEM_CODE, mItem.get(i).getItemCode());
                contentValue.put(ITEM_NAME, mItem.get(i).getItemName());
                contentValue.put(ITEM_BASEUOM, mItem.get(i).getBaseUOM());
                contentValue.put(ITEM_BASE_UOM_QTY, mItem.get(i).getSaleBaseQty());
                contentValue.put(ITEM_BASE_PRICE, mItem.get(i).getSaleBasePrice());
                contentValue.put(ITEM_ALRT_UOM, mItem.get(i).getAltrUOM());
                contentValue.put(ITEM_ALTER_UOM_QTY, mItem.get(i).getSaleAltQty());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.get(i).getSaleAltPrice());
                contentValue.put(ITEM_CATEGORY, mItem.get(i).getCategory());
                contentValue.put(ITEM_PRE_VAT, mItem.get(i).getPreVatAmt());
                contentValue.put(ITEM_VAT_VAL, mItem.get(i).getVatAmt());
                contentValue.put(ITEM_EXCISE_VAL, mItem.get(i).getExciseAmt());
                contentValue.put(ITEM_NET_VAL, mItem.get(i).getNetAmt());
                contentValue.put(ALRT_PRCE, mItem.get(i).getAlterUOMPrice());
                contentValue.put(BASE_PRCE, mItem.get(i).getUOMPrice());

                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(ITEM_UOM_TYPE, "Base");
                } else {
                    contentValue.put(ITEM_UOM_TYPE, "Alter");
                }

                /*if (returnType.equalsIgnoreCase("Good")) {
                    Item rlItem = new Item();
                    rlItem.setItemId(mItem.get(i).getItemId());
                    rlItem.setItemCode(mItem.get(i).getItemCode());
                    rlItem.setItemName(mItem.get(i).getItemName());
                    rlItem.setBaseUOM(mItem.get(i).getBaseUOM());
                    rlItem.setAltrUOM(mItem.get(i).getAltrUOM());

                    if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                        rlItem.setBaseUOMQty(mItem.get(i).getSaleBaseQty());
                        rlItem.setAlterUOMQty("0");
                    } else {
                        rlItem.setAlterUOMQty(mItem.get(i).getSaleAltQty());
                        rlItem.setBaseUOMQty("0");
                    }
                    rlItem.setBaseUOMPrice(mItem.get(i).getBaseUOMPrice());
                    rlItem.setAlterUOMPrice(mItem.get(i).getAlterUOMPrice());
                    insertReturnVanStockItem(rlItem);
                }*/

                db.insert(TABLE_RETURN_REQUEST_ITEMS, null, contentValue);
            }

        }

    }

    //INSERT ORDER Items
    public void insertSalesmanLoad(String orderNo, String orderDate, String routeId, String salesmanId,
                                   ArrayList<Item> mItem, String salesmanName, String routeName) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < mItem.size(); i++) {
            ContentValues contentValue = new ContentValues();

            if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0 && Integer.parseInt(mItem.get(i).getSaleAltQty()) > 0) {

                //Insert Base UOM Item
                insertLoadORDBaseItem(orderNo, mItem.get(i));

                //Insert Alter UOM Item
                insertLoadORDAlterItem(orderNo, mItem.get(i));
            } else {

                contentValue.put(ORDER_NO, orderNo);
                contentValue.put(ITEM_ID, mItem.get(i).getItemId());
                contentValue.put(ITEM_CODE, mItem.get(i).getItemCode());
                contentValue.put(ITEM_NAME, mItem.get(i).getItemName());
                contentValue.put(ITEM_BASEUOM, mItem.get(i).getBaseUOM());
                contentValue.put(ITEM_BASE_UOM_QTY, mItem.get(i).getSaleBaseQty());
                contentValue.put(ITEM_BASE_PRICE, mItem.get(i).getSaleBasePrice());
                contentValue.put(ITEM_ALRT_UOM, mItem.get(i).getAltrUOM());
                contentValue.put(ITEM_ALTER_UOM_QTY, mItem.get(i).getSaleAltQty());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.get(i).getSaleAltPrice());
                contentValue.put(ALRT_PRCE, mItem.get(i).getAlterUOMPrice());
                contentValue.put(BASE_PRCE, mItem.get(i).getUOMPrice());

                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(ITEM_UOM_TYPE, "Base");
                } else {
                    contentValue.put(ITEM_UOM_TYPE, "Alter");
                }

                db.insert(TABLE_SALESMAN_LOAD_REQUEST_ITEMS, null, contentValue);


            }
        }

        ContentValues contentValueHead = new ContentValues();

        contentValueHead.put(ORDER_NO, orderNo);
        contentValueHead.put(ORDER_DATE, orderDate);
        contentValueHead.put(SALESMAN_ID, salesmanId);
        contentValueHead.put(ROUTE_ID, routeId);
        contentValueHead.put(ROUTE_NAME, routeName);
        contentValueHead.put(SALESMAN_NAME, salesmanName);
        contentValueHead.put(DATA_MARK_FOR_POST, "M");
        contentValueHead.put(IS_POSTED, "0");

        db.insert(TABLE_SALESMAN_LOAD_REQUEST_HEADER, null, contentValueHead);
    }

    //INSERT ORDER Items
    public void insertOrderItems(String orderNo, String orderDate, String deliveryDate, String orderAmt, String customerCode,
                                 String salesmanId, String vatAmt, String preVatAmt, String excise, String netAmt,
                                 ArrayList<Item> mItem, ArrayList<Item> arrFOCItem, String orderComment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHead = new ContentValues();


        contentValueHead.put(ORDER_NO, orderNo);
        contentValueHead.put(ORDER_DATE, orderDate);
        contentValueHead.put(DELIVERY_DATE, deliveryDate);
        contentValueHead.put(ORDER_AMOUNT, orderAmt);
        contentValueHead.put(CUSTOMER_ID, customerCode);
        contentValueHead.put(SALESMAN_ID, salesmanId);
        contentValueHead.put(ITEM_VAT_VAL, vatAmt);
        contentValueHead.put(ITEM_PRE_VAT, preVatAmt);
        contentValueHead.put(ITEM_EXCISE_VAL, excise);
        contentValueHead.put(ITEM_NET_VAL, netAmt);
        contentValueHead.put(ORDER_COMMENT, orderComment);
        contentValueHead.put(DATA_MARK_FOR_POST, "M");
        contentValueHead.put(IS_POSTED, "0");

        db.insert(TABLE_ORDER_HEADER, null, contentValueHead);


        for (int i = 0; i < mItem.size(); i++) {
            ContentValues contentValue = new ContentValues();

            if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0 && Integer.parseInt(mItem.get(i).getSaleAltQty()) > 0) {

                //Insert Base UOM Item
                insertORDBaseItem(orderNo, orderDate, mItem.get(i), "Order", "", deliveryDate);

                //Insert Alter UOM Item
                insertORDAlterItem(orderNo, orderDate, mItem.get(i), "Order", "", deliveryDate);
            } else {

                contentValue.put(ORDER_NO, orderNo);
                contentValue.put(ORDER_DATE, orderDate);
                contentValue.put(DELIVERY_DATE, deliveryDate);
                contentValue.put(ITEM_ID, mItem.get(i).getItemId());
                contentValue.put(ITEM_CODE, mItem.get(i).getItemCode());
                contentValue.put(ITEM_NAME, mItem.get(i).getItemName());
                contentValue.put(ITEM_BASEUOM, mItem.get(i).getBaseUOM());
                contentValue.put(ITEM_BASE_UOM_QTY, mItem.get(i).getSaleBaseQty());
                contentValue.put(ITEM_BASE_PRICE, mItem.get(i).getSaleBasePrice());
                contentValue.put(ITEM_ALRT_UOM, mItem.get(i).getAltrUOM());
                contentValue.put(ITEM_ALTER_UOM_QTY, mItem.get(i).getSaleAltQty());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.get(i).getSaleAltPrice());
                contentValue.put(ITEM_CATEGORY, mItem.get(i).getCategory());
                contentValue.put(ITEM_PRE_VAT, mItem.get(i).getPreVatAmt());
                contentValue.put(ITEM_VAT_VAL, mItem.get(i).getVatAmt());
                contentValue.put(ITEM_EXCISE_VAL, mItem.get(i).getExciseAmt());
                contentValue.put(ITEM_NET_VAL, mItem.get(i).getNetAmt());
                contentValue.put(IS_FREE_ITEM, mItem.get(i).getIsFreeItem());
                contentValue.put(ALRT_PRCE, mItem.get(i).getAlterUOMPrice());
                contentValue.put(BASE_PRCE, mItem.get(i).getUOMPrice());
                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(DISCOUNT, mItem.get(i).getDiscountPer());
                    contentValue.put(DISCOUNT_AMT, mItem.get(i).getDiscountAmt());
                } else {
                    contentValue.put(DISCOUNT, mItem.get(i).getDiscountAlPer());
                    contentValue.put(DISCOUNT_AMT, mItem.get(i).getDiscountAlAmt());
                }

                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(ITEM_UOM_TYPE, "Base");
                } else {
                    contentValue.put(ITEM_UOM_TYPE, "Alter");
                }

                db.insert(TABLE_ORDER_ITEMS, null, contentValue);
            }

            if (arrFOCItem.size() > 0) {
                boolean isCOntain = false;
                int position = 0;
                for (int j = 0; j < arrFOCItem.size(); j++) {
                    if (arrFOCItem.get(j).getItemId().equals(mItem.get(i).getItemId())) {
                        isCOntain = true;
                        position = j;
                        if (Integer.parseInt(arrFOCItem.get(j).getSaleBaseQty()) > 0) {
                            //Insert Base UOM Item
                            insertORDBaseItem(orderNo, orderDate, arrFOCItem.get(i), "Order", "", deliveryDate);

                        } else if (Integer.parseInt(arrFOCItem.get(j).getSaleAltQty()) > 0) {
                            //Insert Alter UOM Item
                            insertORDAlterItem(orderNo, orderDate, arrFOCItem.get(i), "Order", "", deliveryDate);
                        }

                        break;
                    }
                }

                if (isCOntain) {
                    arrFOCItem.remove(position);
                }
            }

        }

        for (int i = 0; i < arrFOCItem.size(); i++) {
            if (Integer.parseInt(arrFOCItem.get(i).getSaleBaseQty()) > 0) {
                //Insert Base UOM Item
                insertORDBaseItem(orderNo, orderDate, arrFOCItem.get(i), "Order", "", deliveryDate);

            } else if (Integer.parseInt(arrFOCItem.get(i).getSaleAltQty()) > 0) {
                //Insert Alter UOM Item
                insertORDAlterItem(orderNo, orderDate, arrFOCItem.get(i), "Order", "", deliveryDate);
            }

        }

    }

    public void insertORDBaseItem(String orderNo, String orderDate, Item mItem, String type, String returnType, String deliveryDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(ORDER_NO, orderNo);
        contentValue.put(ORDER_DATE, orderDate);
        contentValue.put(ITEM_ID, mItem.getItemId());
        contentValue.put(ITEM_CODE, mItem.getItemCode());
        contentValue.put(ITEM_NAME, mItem.getItemName());
        contentValue.put(ITEM_UOM_TYPE, "Base");
        contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
        contentValue.put(ITEM_BASE_UOM_QTY, mItem.getSaleBaseQty());
        contentValue.put(ITEM_BASE_PRICE, mItem.getSaleBasePrice());
        contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
        contentValue.put(ITEM_ALTER_UOM_QTY, "0");
        contentValue.put(ITEM_ALRT_UOM_PRCE, "0");
        contentValue.put(ALRT_PRCE, "0");
        contentValue.put(BASE_PRCE, mItem.getUOMPrice());
        contentValue.put(ITEM_CATEGORY, mItem.getCategory());

        double itemVatAmt = UtilApp.getVat(Double.parseDouble(mItem.getSaleBasePrice()));
        double itemPreVatAmt = Double.parseDouble(mItem.getSaleBasePrice()) - itemVatAmt;

        double itemExcise = 0;
        if (type.equalsIgnoreCase("Load")) {
            if (Double.parseDouble(mItem.getSaleBasePrice()) > 0) {
                String itemCat = mItem.getCategory();
                String itemBasVolume = getItemBaseVolume(mItem.getItemId());
                if (itemCat.equalsIgnoreCase("1")) {
                    itemExcise = 0;
                } else {
                    double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(itemBasVolume), itemCat);
                    double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), itemCat);

                    if (exciseFirst > exciseSecond) {
                        itemExcise = exciseFirst;
                    } else {
                        itemExcise = exciseSecond;
                    }
                }
            }

        }

        double itemNet = itemPreVatAmt - itemExcise;

        contentValue.put(ITEM_PRE_VAT, "" + Math.round(itemPreVatAmt));
        contentValue.put(ITEM_VAT_VAL, "" + Math.round(itemVatAmt));
        contentValue.put(ITEM_EXCISE_VAL, "" + Math.round(itemExcise));
        contentValue.put(ITEM_NET_VAL, "" + Math.round(itemNet));


        if (type.equalsIgnoreCase("Order")) {
            contentValue.put(DISCOUNT, mItem.getDiscountPer());
            contentValue.put(DISCOUNT_AMT, mItem.getDiscountAmt());
            contentValue.put(DELIVERY_DATE, deliveryDate);
            contentValue.put(IS_FREE_ITEM, mItem.getIsFreeItem());
            contentValue.put(PARENT_ITEM_ID, mItem.getPerentItemId());
            db.insert(TABLE_ORDER_ITEMS, null, contentValue);
        } else if (type.equalsIgnoreCase("Return")) {
            contentValue.put(REASON_TYPE, mItem.getReasonCode());
            contentValue.put(RETURN_TYPE, returnType);
            contentValue.put(EXPIRY_DATE, mItem.getAgentExcise());
            db.insert(TABLE_RETURN_ITEMS, null, contentValue);
        } else if (type.equalsIgnoreCase("Return Request")) {
            contentValue.put(REASON_TYPE, mItem.getReasonCode());
            contentValue.put(RETURN_TYPE, returnType);
            db.insert(TABLE_RETURN_REQUEST_ITEMS, null, contentValue);
        } else if (type.equalsIgnoreCase("Load")) {
            contentValue.put(DISCOUNT, mItem.getDiscountPer());
            contentValue.put(DISCOUNT_AMT, mItem.getDiscountAmt());
            contentValue.put(IS_FREE_ITEM, mItem.getIsFreeItem());
            contentValue.put(PARENT_ITEM_ID, mItem.getPerentItemId());
            db.insert(TABLE_LOAD_REQUEST_ITEMS, null, contentValue);
        }

    }

    public void insertLoadORDBaseItem(String orderNo, Item mItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(ORDER_NO, orderNo);
        contentValue.put(ITEM_ID, mItem.getItemId());
        contentValue.put(ITEM_CODE, mItem.getItemCode());
        contentValue.put(ITEM_NAME, mItem.getItemName());
        contentValue.put(ITEM_UOM_TYPE, "Base");
        contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
        contentValue.put(ITEM_BASE_UOM_QTY, mItem.getSaleBaseQty());
        contentValue.put(ITEM_BASE_PRICE, mItem.getSaleBasePrice());
        contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
        contentValue.put(ITEM_ALTER_UOM_QTY, "0");
        contentValue.put(ITEM_ALRT_UOM_PRCE, "0");
        contentValue.put(ALRT_PRCE, "0");
        contentValue.put(BASE_PRCE, mItem.getUOMPrice());

        db.insert(TABLE_SALESMAN_LOAD_REQUEST_ITEMS, null, contentValue);

    }

    public void insertLoadORDAlterItem(String orderNo, Item mItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(ORDER_NO, orderNo);
        contentValue.put(ITEM_ID, mItem.getItemId());
        contentValue.put(ITEM_CODE, mItem.getItemCode());
        contentValue.put(ITEM_NAME, mItem.getItemName());
        contentValue.put(ITEM_UOM_TYPE, "Alter");
        contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
        contentValue.put(ITEM_BASE_UOM_QTY, "0");
        contentValue.put(ITEM_BASE_PRICE, "0");
        contentValue.put(ALRT_PRCE, mItem.getAlterUOMPrice());
        contentValue.put(BASE_PRCE, "0");
        contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
        contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getSaleAltQty());
        contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getSaleAltPrice());


        db.insert(TABLE_SALESMAN_LOAD_REQUEST_ITEMS, null, contentValue);

    }

    public void insertORDAlterItem(String orderNo, String orderDate, Item mItem, String type, String returnType, String deliveryDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(ORDER_NO, orderNo);
        contentValue.put(ORDER_DATE, orderDate);
        contentValue.put(ITEM_ID, mItem.getItemId());
        contentValue.put(ITEM_CODE, mItem.getItemCode());
        contentValue.put(ITEM_NAME, mItem.getItemName());
        contentValue.put(ITEM_UOM_TYPE, "Alter");
        contentValue.put(ITEM_BASEUOM, mItem.getBaseUOM());
        contentValue.put(ITEM_BASE_UOM_QTY, "0");
        contentValue.put(ITEM_BASE_PRICE, "0");
        contentValue.put(ALRT_PRCE, mItem.getAlterUOMPrice());
        contentValue.put(BASE_PRCE, "0");
        contentValue.put(ITEM_ALRT_UOM, mItem.getAltrUOM());
        contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getSaleAltQty());
        contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getSaleAltPrice());
        contentValue.put(ITEM_CATEGORY, mItem.getCategory());

        double itemVatAmt = UtilApp.getVat(Double.parseDouble(mItem.getSaleAltPrice()));
        double itemPreVatAmt = Double.parseDouble(mItem.getSaleAltPrice()) - itemVatAmt;

        double itemExcise = 0;
        if (type.equalsIgnoreCase("Load")) {
            if (Integer.parseInt(mItem.getSaleAltPrice()) > 0) {
                String itemCat = mItem.getCategory();
                String itemAltVolume = getItemAlterVolume(mItem.getItemId());
                if (itemCat.equalsIgnoreCase("1")) {
                    itemExcise = 0;
                } else {
                    double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(itemAltVolume), itemCat);
                    double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), itemCat);

                    if (exciseFirst > exciseSecond) {
                        itemExcise = exciseFirst;
                    } else {
                        itemExcise = exciseSecond;
                    }
                }
            }
        }
        double itemNet = itemPreVatAmt - itemExcise;

        contentValue.put(ITEM_PRE_VAT, "" + Math.round(itemPreVatAmt));
        contentValue.put(ITEM_VAT_VAL, "" + Math.round(itemVatAmt));
        contentValue.put(ITEM_EXCISE_VAL, "" + Math.round(itemExcise));
        contentValue.put(ITEM_NET_VAL, "" + Math.round(itemNet));


        if (type.equalsIgnoreCase("Order")) {
            contentValue.put(DISCOUNT, mItem.getDiscountAlPer());
            contentValue.put(DISCOUNT_AMT, mItem.getDiscountAlAmt());
            contentValue.put(DELIVERY_DATE, deliveryDate);
            contentValue.put(IS_FREE_ITEM, mItem.getIsFreeItem());
            contentValue.put(PARENT_ITEM_ID, mItem.getPerentItemId());
            db.insert(TABLE_ORDER_ITEMS, null, contentValue);
        } else if (type.equalsIgnoreCase("Return")) {
            contentValue.put(REASON_TYPE, mItem.getReasonCode());
            contentValue.put(RETURN_TYPE, returnType);
            contentValue.put(EXPIRY_DATE, mItem.getAgentExcise());
            db.insert(TABLE_RETURN_ITEMS, null, contentValue);
        } else if (type.equalsIgnoreCase("Return Request")) {
            contentValue.put(REASON_TYPE, mItem.getReasonCode());
            contentValue.put(RETURN_TYPE, returnType);
            db.insert(TABLE_RETURN_REQUEST_ITEMS, null, contentValue);
        } else if (type.equalsIgnoreCase("Load")) {
            contentValue.put(DISCOUNT, mItem.getDiscountAlPer());
            contentValue.put(DISCOUNT_AMT, mItem.getDiscountAlAmt());
            contentValue.put(IS_FREE_ITEM, mItem.getIsFreeItem());
            contentValue.put(PARENT_ITEM_ID, mItem.getPerentItemId());
            db.insert(TABLE_LOAD_REQUEST_ITEMS, null, contentValue);
        }

    }

    //INSERT Delivery Items
    public void insertDeliveryItems(ArrayList<Delivery> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValueHead = new ContentValues();

            Delivery mDelivery = arrData.get(i);

            contentValueHead.put(DELIVERY_ID, mDelivery.getOrderId());
            contentValueHead.put(ORDER_NO, mDelivery.getOrderNo());
            contentValueHead.put(DELIVERY_ORDER_NO, mDelivery.getDeliveryOrderNo());
            contentValueHead.put(ORDER_DATE, mDelivery.getOrderDate());
            contentValueHead.put(ORDER_AMOUNT, mDelivery.getTotalAmt());
            contentValueHead.put(CUSTOMER_ID, mDelivery.getCustomerId());
            contentValueHead.put(SALESMAN_ID, Settings.getString(App.SALESMANID));
            contentValueHead.put(REASON, "");
            contentValueHead.put(IS_DELETE, "0");
            contentValueHead.put(DATA_MARK_FOR_POST, "N");
            contentValueHead.put(IS_POSTED, "0");

            if (!checkIsDeliveryExist(mDelivery.getOrderId())) {
                db.insert(TABLE_DELIVERY_HEADER, null, contentValueHead);

                for (int j = 0; j < mDelivery.getDeliveryItems().size(); j++) {

                    Item mItem = mDelivery.getDeliveryItems().get(j);

                    ContentValues contentValue = new ContentValues();

                    contentValue.put(DELIVERY_ID, mDelivery.getOrderId());
                    contentValue.put(ORDER_NO, mDelivery.getOrderNo());
                    contentValue.put(ORDER_DATE, mDelivery.getOrderDate());
                    contentValue.put(ITEM_ID, mItem.getItemId());
                    contentValue.put(ITEM_CODE, getItemCode(mItem.getItemId()));
                    contentValue.put(ITEM_NAME, getItemName(mItem.getItemId()));

                    if (checkIsBaseUOM(mItem.getUom(), mItem.getItemId())) {
                        contentValue.put(ITEM_BASEUOM, mItem.getUom());
                        contentValue.put(ITEM_BASE_UOM_QTY, mItem.getQty());
                        contentValue.put(ITEM_BASE_PRICE, mItem.getTotalAmt());
                        contentValue.put(ITEM_ALRT_UOM, getItemUOM(mItem.getItemId(), "Alter"));
                        contentValue.put(ITEM_ALTER_UOM_QTY, "0");
                        contentValue.put(ITEM_ALRT_UOM_PRCE, "0");
                        contentValue.put(ITEM_UOM_TYPE, "Base");
                    } else if (checkIsAlterUOM(mItem.getUom(), mItem.getItemId())) {
                        contentValue.put(ITEM_BASEUOM, getItemUOM(mItem.getItemId(), "Base"));
                        contentValue.put(ITEM_BASE_UOM_QTY, "0");
                        contentValue.put(ITEM_BASE_PRICE, "0");
                        contentValue.put(ITEM_ALRT_UOM, mItem.getUom());
                        contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getQty());
                        contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getTotalAmt());
                        contentValue.put(ITEM_UOM_TYPE, "Alter");
                    }
                    contentValue.put(ITEM_CATEGORY, getItemCategory(mItem.getItemId()));
                    contentValue.put(ITEM_PRE_VAT, mItem.getPreVatAmt());
                    contentValue.put(ITEM_VAT_VAL, mItem.getVatAmt());
                    contentValue.put(ITEM_EXCISE_VAL, mItem.getExciseAmt());
                    contentValue.put(ITEM_NET_VAL, mItem.getNetAmt());
                    contentValue.put(IS_DELETE, "0");

                    db.insert(TABLE_DELIVERY_ITEMS, null, contentValue);
                }
            }

        }
    }

    //INSERT Delivery Items
    public void insertDeliveryAcceptItems(ArrayList<Delivery> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValueHead = new ContentValues();

            Delivery mDelivery = arrData.get(i);

            contentValueHead.put(DELIVERY_ID, mDelivery.getOrderId());
            contentValueHead.put(ORDER_NO, mDelivery.getOrderNo());
            contentValueHead.put(DELIVERY_ORDER_NO, mDelivery.getDeliveryOrderNo());
            contentValueHead.put(ORDER_AMOUNT, mDelivery.getTotalAmt());
            contentValueHead.put(CUSTOMER_ID, mDelivery.getCustomerId());
            contentValueHead.put(SALESMAN_ID, Settings.getString(App.SALESMANID));
            contentValueHead.put(REASON, "");
            contentValueHead.put(IS_DELETE, "0");
            contentValueHead.put(DATA_MARK_FOR_POST, "N");
            contentValueHead.put(IS_POSTED, "0");

            db.insert(TABLE_DELIVERY_ACCEPT_HEADER, null, contentValueHead);

            for (int j = 0; j < mDelivery.getDeliveryItems().size(); j++) {

                Item mItem = mDelivery.getDeliveryItems().get(j);

                ContentValues contentValue = new ContentValues();

                contentValue.put(DELIVERY_ID, mDelivery.getOrderId());
                contentValue.put(ITEM_ID, mItem.getItemId());
                contentValue.put(ITEM_CODE, getItemCode(mItem.getItemId()));
                contentValue.put(ITEM_NAME, getItemName(mItem.getItemId()));

                if (checkIsBaseUOM(mItem.getUom(), mItem.getItemId())) {
                    contentValue.put(ITEM_BASEUOM, mItem.getUom());
                    contentValue.put(ITEM_BASE_UOM_QTY, mItem.getQty());
                    contentValue.put(ITEM_BASE_PRICE, mItem.getTotalAmt());
                    contentValue.put(ITEM_ALRT_UOM, getItemUOM(mItem.getItemId(), "Alter"));
                    contentValue.put(ITEM_ALTER_UOM_QTY, "0");
                    contentValue.put(ITEM_ALRT_UOM_PRCE, "0");
                    contentValue.put(ITEM_UOM_TYPE, "Base");
                } else if (checkIsAlterUOM(mItem.getUom(), mItem.getItemId())) {
                    contentValue.put(ITEM_BASEUOM, getItemUOM(mItem.getItemId(), "Base"));
                    contentValue.put(ITEM_BASE_UOM_QTY, "0");
                    contentValue.put(ITEM_BASE_PRICE, "0");
                    contentValue.put(ITEM_ALRT_UOM, mItem.getUom());
                    contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getQty());
                    contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getTotalAmt());
                    contentValue.put(ITEM_UOM_TYPE, "Alter");
                }
                contentValue.put(ITEM_CATEGORY, getItemCategory(mItem.getItemId()));
                contentValue.put(ITEM_PRE_VAT, mItem.getPreVatAmt());
                contentValue.put(ITEM_VAT_VAL, mItem.getVatAmt());
                contentValue.put(ITEM_EXCISE_VAL, mItem.getExciseAmt());
                contentValue.put(ITEM_NET_VAL, mItem.getNetAmt());
                contentValue.put(IS_DELETE, "0");

                db.insert(TABLE_DELIVERY_ACCEPT_ITEMS, null, contentValue);
            }

        }
    }

    //INSERT Return Items
    public void insertReturnListItems(ArrayList<Return> arrData) {

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValueHead = new ContentValues();

            Return mDelivery = arrData.get(i);

            contentValueHead.put(KEY_ORDER_ID, mDelivery.getOrderId());
            contentValueHead.put(ORDER_NO, mDelivery.getOrderNo());
            contentValueHead.put(ORDER_DATE, mDelivery.getOrderDate());
            contentValueHead.put(ORDER_AMOUNT, mDelivery.getTotalAmt());
            contentValueHead.put(CUSTOMER_ID, mDelivery.getCustomerId());
            contentValueHead.put(RETURN_TYPE, mDelivery.getDeliveryItems().get(0).getProductType().equalsIgnoreCase("1") ? "Good" : "Bad");
            contentValueHead.put(SALESMAN_ID, Settings.getString(App.SALESMANID));
            contentValueHead.put(ITEM_VAT_VAL, mDelivery.getVatAmt());
            contentValueHead.put(ITEM_PRE_VAT, mDelivery.getPreVatAmt());
            contentValueHead.put(ITEM_EXCISE_VAL, mDelivery.getExcise());
            contentValueHead.put(ITEM_NET_VAL, mDelivery.getNetTotal());
            contentValueHead.put(SVH_EXCHANGE_NO, "");
            contentValueHead.put(IS_RETURN_LIST, "1");
            contentValueHead.put(IS_PARTIAL, "0");
            contentValueHead.put(DATA_MARK_FOR_POST, "N");
            contentValueHead.put(IS_POSTED, "0");

            if (!checkIsReturnExist(mDelivery.getOrderId())) {
                db.insert(TABLE_RETURN_HEADER, null, contentValueHead);

                for (int j = 0; j < mDelivery.getDeliveryItems().size(); j++) {

                    Item mItem = mDelivery.getDeliveryItems().get(j);

                    ContentValues contentValue = new ContentValues();

                    contentValue.put(ORDER_NO, mDelivery.getOrderNo());
                    contentValue.put(ORDER_DATE, mDelivery.getOrderDate());
                    contentValue.put(RETURN_TYPE, mItem.getProductType().equalsIgnoreCase("1") ? "Good" : "Bad");
                    contentValue.put(REASON_TYPE, getReasonType(mItem.getReasonCode()));
                    contentValue.put(ITEM_ID, mItem.getItemId());
                    contentValue.put(ITEM_CODE, getItemCode(mItem.getItemId()));
                    contentValue.put(ITEM_NAME, getItemName(mItem.getItemId()));

                    /*double itemBasePrice = 0, itemAlterPrice = 0;
                    if (isPricingItems(mDelivery.getCustomerId(), mItem.getItemId())) {
                        Item pItem = getCustPricingItems(mDelivery.getCustomerId(), mItem.getItemId());
                        itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                        itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                    } else if (isAgentPricingItems(mItem.getItemId(), Settings.getString(App.ROUTEID))) {
                        Item pItem = getAgentPricingItems(mItem.getItemId());
                        itemBasePrice = Double.parseDouble(pItem.getBaseUOMPrice());
                        itemAlterPrice = Double.parseDouble(pItem.getAlterUOMPrice());
                    } else {
                        itemBasePrice = getItemPrice(mItem.getItemId());
                        itemAlterPrice = getItemAlterPrice(mItem.getItemId());
                    }*/

                    if (checkIsBaseUOM(mItem.getUom(), mItem.getItemId())) {
                        contentValue.put(ITEM_BASEUOM, mItem.getUom());
                        contentValue.put(ITEM_BASE_UOM_QTY, mItem.getQty());
                        contentValue.put(ITEM_BASE_PRICE, mItem.getTotalAmt());
                        contentValue.put(ITEM_ALRT_UOM, getItemUOM(mItem.getItemId(), "Alter"));
                        contentValue.put(ITEM_ALTER_UOM_QTY, "0");
                        contentValue.put(ITEM_ALRT_UOM_PRCE, "0");
                        contentValue.put(ITEM_UOM_TYPE, "Base");
                        contentValue.put(ALRT_PRCE, "0");
                        contentValue.put(BASE_PRCE, mItem.getItemValue());
                    } else if (checkIsAlterUOM(mItem.getUom(), mItem.getItemId())) {
                        contentValue.put(ITEM_BASEUOM, getItemUOM(mItem.getItemId(), "Base"));
                        contentValue.put(ITEM_BASE_UOM_QTY, "0");
                        contentValue.put(ITEM_BASE_PRICE, "0");
                        contentValue.put(ITEM_ALRT_UOM, mItem.getUom());
                        contentValue.put(ITEM_ALTER_UOM_QTY, mItem.getQty());
                        contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getTotalAmt());
                        contentValue.put(ITEM_UOM_TYPE, "Alter");
                        contentValue.put(ALRT_PRCE, mItem.getItemValue());
                        contentValue.put(BASE_PRCE, "0");
                    }
                    contentValue.put(ITEM_CATEGORY, getItemCategory(mItem.getItemId()));
                    contentValue.put(ITEM_PRE_VAT, mItem.getPreVatAmt());
                    contentValue.put(ITEM_VAT_VAL, mItem.getVatAmt());
                    contentValue.put(ITEM_EXCISE_VAL, mItem.getExciseAmt());
                    contentValue.put(ITEM_NET_VAL, mItem.getNetAmt());

                    db.insert(TABLE_RETURN_ITEMS, null, contentValue);
                }
            }
        }
    }

    private String getReasonType(String name) {
        String code = "0";

        switch (name) {
            case "Damage":
                code = "2";
                break;
            case "Expired":
                code = "3";
                break;
            case "Short Expiry":
                code = "6";
                break;
            case "Packing Issue":
                code = "7";
                break;
            case "Product Replacement":
                code = "8";
                break;
            case "Non moving product":
                code = "9";
                break;
            case "0":
                code = "0";
                break;
        }
        return code;

    }

    //INSERT Push Notification
    public void insertPushNotification(Notification mData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(CUSTOMER_ID, mData.getId());
        contentValue.put(ORDER_NO, mData.getOrderId());
        contentValue.put(TYPE, mData.getType());
        contentValue.put(TITLE, mData.getTitle());
        contentValue.put(MESSAGE, mData.getMessage());
        contentValue.put(DATE_TIME, UtilApp.getCurrentDateBG());
        contentValue.put(IS_READ, "0");

        db.insert(TABLE_PUSH_NOTIFICATION, null, contentValue);
    }

    public void insertAgentPricingCount(ArrayList<Pricing> arrData, String routeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        for (int i = 0; i < arrData.size(); i++) {

            Pricing mPricing = arrData.get(i);
            contentValue.put(ROUTE_ID, routeId);
            contentValue.put(COUNT_ID, mPricing.totalRecords);

            db.insert(TABLE_AGENT_PRICE_COUNT, null, contentValue);
        }

    }

    public void insertDepotCustomerCount(String count, String routeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();


        contentValue.put(DEPOT_ID, routeId);
        contentValue.put(COUNT_ID, count);

        db.insert(TABLE_DEPOT_CUSTOMER_COUNT, null, contentValue);

    }

    //INSERT Pricing Items
    public void insertAgentPricingItems(ArrayList<Pricing> arrData, String routeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            Pricing mPricing = arrData.get(i);

            for (int j = 0; j < mPricing.getItemList().size(); j++) {

                Item mItem = mPricing.getItemList().get(j);

                ContentValues contentValue = new ContentValues();

                contentValue.put(PRICING_PLAN_ID, mPricing.getPlanId());
                contentValue.put(ROUTE_ID, routeId);
                contentValue.put(ITEM_ID, mItem.getItemId());
                contentValue.put(ITEM_BASE_PRICE, mItem.getUOMPrice());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getAlternateUOMPrice());

                db.insert(TABLE_AGENT_PRICING, null, contentValue);
            }
        }
    }

    //INSERT Pricing Items
    public void insertPricingItems(ArrayList<Pricing> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            Pricing mPricing = arrData.get(i);

            for (int j = 0; j < mPricing.getItemList().size(); j++) {

                Item mItem = mPricing.getItemList().get(j);

                ContentValues contentValue = new ContentValues();

                contentValue.put(PRICING_PLAN_ID, mPricing.getPlanId());
                contentValue.put(CUSTOMER_ID, mPricing.getCustomerId());
                contentValue.put(ITEM_ID, mItem.getItemId());
                contentValue.put(ITEM_BASE_PRICE, mItem.getUOMPrice());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.getAlternateUOMPrice());

                db.insert(TABLE_CUSTOMER_PRICING, null, contentValue);
            }
        }
    }

    //INSERT Free Goods Items
    public void insertNewFreeGoods(ArrayList<PromoItems> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            PromoItems mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(KEY_PRIORITY, "");
            contentValue.put(PROMOTIONAL_ID, mItem.getPromotionId());
            contentValue.put(KEY_DATA, mItem.getPromotional_name());
            contentValue.put(ASSIGN_UOM_ID, mItem.getAssignment_uom());
            contentValue.put(QUALIFY_UOM_ID, mItem.getQualification_uom());
            db.insert(TABLE_PROMO, null, contentValue);

            if (mItem.getExcluded_customer() != null) {
                if (!mItem.getExcluded_customer().isEmpty()) {
                    String[] spSplits = mItem.getExcluded_customer().split(",");
                    for (int k = 0; k < spSplits.length; k++) {
                        ContentValues contentValueSB = new ContentValues();
                        contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                        contentValueSB.put(CUST_ID, spSplits[k]);
                        db.insert(TABLE_PROMO_CUSTOMER_EXCLUDE, null, contentValueSB);
                    }
                }
            }

            if (!mItem.getAssignment_item_id().isEmpty()) {
                String[] spSplits = mItem.getAssignment_item_id().split(",");
                for (int k = 0; k < spSplits.length; k++) {
                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(ITEM_ID, spSplits[k]);
                    contentValueSB.put(UOM_ID, mItem.getAssignment_uom());
                    db.insert(TABLE_ORDER_ITEM, null, contentValueSB);
                }
            }

            if (!mItem.getQualification_item_id().isEmpty()) {
                String[] spSplits = mItem.getQualification_item_id().split(",");
                for (int k = 0; k < spSplits.length; k++) {
                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(ITEM_ID, spSplits[k]);
                    contentValueSB.put(UOM_ID, mItem.getQualification_uom());
                    db.insert(TABLE_OFFER_ITEM, null, contentValueSB);
                }
            }

            if (mItem.getPromo_slab().size() > 0) {
                for (int j = 0; j < mItem.getPromo_slab().size(); j++) {
                    PromoSlab mSlab = mItem.getPromo_slab().get(j);

                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(QTY_MIN, mSlab.getLower_qty());
                    contentValueSB.put(QTY_MAX, mSlab.getUpper_qty());
                    contentValueSB.put(QTY, mSlab.getFree_qty());
                    db.insert(TABLE_PROMO_SLAB, null, contentValueSB);
                }
            }
        }
    }

    //INSERT Free Goods Items
    public void insertNewCustomerFreeGoods(ArrayList<PromoItems> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            PromoItems mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(KEY_PRIORITY, "");
            contentValue.put(PROMOTIONAL_ID, mItem.getPromotionId());
            contentValue.put(KEY_DATA, mItem.getPromotional_name());
            contentValue.put(ASSIGN_UOM_ID, mItem.getAssignment_uom());
            contentValue.put(QUALIFY_UOM_ID, mItem.getQualification_uom());
            db.insert(TABLE_CUSTOMER_PROMO, null, contentValue);

            if (mItem.getIncluded_customer() != null) {
                if (!mItem.getIncluded_customer().isEmpty()) {
                    String[] spSplits = mItem.getIncluded_customer().split(",");
                    for (int k = 0; k < spSplits.length; k++) {
                        ContentValues contentValueSB = new ContentValues();
                        contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                        contentValueSB.put(CUST_ID, spSplits[k]);
                        db.insert(TABLE_PROMO_CUSTOMER, null, contentValueSB);
                    }
                }
            }

            if (!mItem.getAssignment_item_id().isEmpty()) {
                String[] spSplits = mItem.getAssignment_item_id().split(",");
                for (int k = 0; k < spSplits.length; k++) {
                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(ITEM_ID, spSplits[k]);
                    contentValueSB.put(UOM_ID, mItem.getAssignment_uom());
                    db.insert(TABLE_CUSTOMER_ORDER_ITEM, null, contentValueSB);
                }
            }

            if (!mItem.getQualification_item_id().isEmpty()) {
                String[] spSplits = mItem.getQualification_item_id().split(",");
                for (int k = 0; k < spSplits.length; k++) {
                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(ITEM_ID, spSplits[k]);
                    contentValueSB.put(UOM_ID, mItem.getQualification_uom());
                    db.insert(TABLE_CUSTOMER_OFFER_ITEM, null, contentValueSB);
                }
            }

            if (mItem.getPromo_slab().size() > 0) {
                for (int j = 0; j < mItem.getPromo_slab().size(); j++) {
                    PromoSlab mSlab = mItem.getPromo_slab().get(j);

                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(QTY_MIN, mSlab.getLower_qty());
                    contentValueSB.put(QTY_MAX, mSlab.getUpper_qty());
                    contentValueSB.put(QTY, mSlab.getFree_qty());
                    db.insert(TABLE_CUSTOMER_PROMO_SLAB, null, contentValueSB);
                }
            }
        }
    }

    //INSERT Free Goods Items
    public void insertNewAgentFreeGoods(ArrayList<PromoItems> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            PromoItems mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();
            contentValue.put(KEY_PRIORITY, "");
            contentValue.put(PROMOTIONAL_ID, mItem.getPromotionId());
            contentValue.put(KEY_DATA, mItem.getPromotional_name());
            contentValue.put(ASSIGN_UOM_ID, mItem.getAssignment_uom());
            contentValue.put(QUALIFY_UOM_ID, mItem.getQualification_uom());
            db.insert(TABLE_AGENT_PROMO, null, contentValue);

            if (!mItem.getAssignment_item_id().isEmpty()) {
                String[] spSplits = mItem.getAssignment_item_id().split(",");
                for (int k = 0; k < spSplits.length; k++) {
                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(ITEM_ID, spSplits[k]);
                    contentValueSB.put(UOM_ID, mItem.getAssignment_uom());
                    db.insert(TABLE_AGENT_ORDER_ITEM, null, contentValueSB);
                }
            }

            if (!mItem.getQualification_item_id().isEmpty()) {
                String[] spSplits = mItem.getQualification_item_id().split(",");
                for (int k = 0; k < spSplits.length; k++) {
                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(ITEM_ID, spSplits[k]);
                    contentValueSB.put(UOM_ID, mItem.getQualification_uom());
                    db.insert(TABLE_AGENT_OFFER_ITEM, null, contentValueSB);
                }
            }

            if (mItem.getPromo_slab().size() > 0) {
                for (int j = 0; j < mItem.getPromo_slab().size(); j++) {
                    PromoSlab mSlab = mItem.getPromo_slab().get(j);

                    ContentValues contentValueSB = new ContentValues();
                    contentValueSB.put(PROMOTIONAL_ID, mItem.getPromotionId());
                    contentValueSB.put(QTY_MIN, mSlab.getLower_qty());
                    contentValueSB.put(QTY_MAX, mSlab.getUpper_qty());
                    contentValueSB.put(QTY, mSlab.getFree_qty());
                    db.insert(TABLE_AGENT_PROMO_SLAB, null, contentValueSB);
                }
            }
        }
    }

    //INSERT Free Goods Items
    public void insertFreeGoods(ArrayList<FOCItems> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            FOCItems mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(KEY_PRIORITY, mItem.getPriority());
            contentValue.put(CUSTOMER_ID, "");
            contentValue.put(ITEM_ID, mItem.getItemId());
            contentValue.put(ITEM_UOM, mItem.getItemUOM());
            //Maxchange
            contentValue.put(ITEM_QTY, mItem.getItemQTY());
            contentValue.put(ITEM_MAXQTY, mItem.getMaxItemQTY());
            contentValue.put(FOC_ITEM_ID, mItem.getFocItemId());
            contentValue.put(FOC_ITEM_UOM, mItem.getFocItemUOM());
            contentValue.put(FOC_ITEM_QTY, mItem.getFocItemQTY());

            db.insert(TABLE_FREE_GOODS, null, contentValue);

            ContentValues contentPriority = new ContentValues();
            contentPriority.put(KEY_PRIORITY, mItem.getPriority());
            contentPriority.put(KEY_DOC_TYPE, "Promo");
            contentPriority.put(KEY_DISCOUNT_KEY, "0");
            contentPriority.put(DISCOUNT_ID, mItem.getPromotionId());
            contentPriority.put(ITEM_ID, mItem.getItemId());
            db.insert(TABLE_ITEM_PRIORITY, null, contentPriority);

        }
    }

    //INSERT Free Goods Items
    public void insertAgeentFreeGoods(ArrayList<FOCItems> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            FOCItems mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(KEY_PRIORITY, mItem.getPriority());
            contentValue.put(ITEM_ID, mItem.getItemId());
            contentValue.put(ITEM_UOM, mItem.getItemUOM());
            //Maxchange
            contentValue.put(ITEM_QTY, mItem.getItemQTY());
            contentValue.put(ITEM_MAXQTY, mItem.getMaxItemQTY());
            contentValue.put(FOC_ITEM_ID, mItem.getFocItemId());
            contentValue.put(FOC_ITEM_UOM, mItem.getFocItemUOM());
            contentValue.put(FOC_ITEM_QTY, mItem.getFocItemQTY());

            db.insert(TABLE_AGENT_FREE_GOODS, null, contentValue);

        }
    }

    //INSERT Discount Items
    public void insertItemDiscount(ArrayList<DiscountData> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            DiscountData mDisData = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(DISCOUNT_ID, mDisData.getDiscountId());
            contentValue.put(KEY_DATA, mDisData.getDiscount_desc());
            contentValue.put(KEY_DISCOUNT_KEY, mDisData.getDiscount_key());
            contentValue.put(DISCOUNT_TYPE, mDisData.getDiscount_type());
            contentValue.put(DISCOUNT_MAIN_TYPE, "3");
            contentValue.put(DISCOUNT, mDisData.getType());

            db.insert(TABLE_DISCOUNT_MAIN_HEADER, null, contentValue);

            if (!mDisData.getItemId().isEmpty()) {
                String[] mDisItem = mDisData.getItemId().split(",");
                for (int j = 0; j < mDisItem.length; j++) {
                    ContentValues contentItemValue = new ContentValues();

                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(ITEM_ID, mDisItem[j]);
                    contentItemValue.put(DISCOUNT_MAIN_TYPE, "3");
                    db.insert(TABLE_DISCOUNT_SITEM, null, contentItemValue);
                }
            }

            if (mDisData.getDiscountSlab().size() > 0) {
                for (int k = 0; k < mDisData.getDiscountSlab().size(); k++) {

                    ItemSlab mSlab = mDisData.getDiscountSlab().get(k);

                    ContentValues contentItemValue = new ContentValues();
                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(QTY_MIN, mSlab.getQty_min());
                    contentItemValue.put(QTY_MAX, mSlab.getQty_max());
                    contentItemValue.put(DISCOUNT_AMT, mSlab.getDiscount_amt());
                    db.insert(TABLE_DISCOUNT_SLAB, null, contentItemValue);
                }
            }

        }
    }

    public void insertCustomerItemDiscount(ArrayList<DiscountData> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            DiscountData mDisData = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(DISCOUNT_ID, mDisData.getDiscountId());
            contentValue.put(KEY_DATA, mDisData.getDiscount_desc());
            contentValue.put(KEY_DISCOUNT_KEY, mDisData.getDiscount_key());
            contentValue.put(DISCOUNT_TYPE, mDisData.getDiscount_type());
            contentValue.put(DISCOUNT_MAIN_TYPE, "1");
            contentValue.put(DISCOUNT, mDisData.getType());

            db.insert(TABLE_DISCOUNT_MAIN_HEADER, null, contentValue);

            if (!mDisData.getInclude_customer().isEmpty()) {
                String[] mDisCus = mDisData.getInclude_customer().split(",");
                for (int j = 0; j < mDisCus.length; j++) {
                    ContentValues contentItemValue = new ContentValues();

                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(CUSTOMER_ID, mDisCus[j]);
                    db.insert(TABLE_DISCOUNT_CUSTOMER_HEADER, null, contentItemValue);
                }
            }

            if (!mDisData.getItemId().isEmpty()) {
                String[] mDisItem = mDisData.getItemId().split(",");
                for (int j = 0; j < mDisItem.length; j++) {
                    ContentValues contentItemValue = new ContentValues();

                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(ITEM_ID, mDisItem[j]);
                    contentItemValue.put(DISCOUNT_MAIN_TYPE, "1");
                    db.insert(TABLE_DISCOUNT_SITEM, null, contentItemValue);
                }
            }

            if (mDisData.getDiscountSlab().size() > 0) {
                for (int k = 0; k < mDisData.getDiscountSlab().size(); k++) {

                    ItemSlab mSlab = mDisData.getDiscountSlab().get(k);

                    ContentValues contentItemValue = new ContentValues();
                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(QTY_MIN, mSlab.getQty_min());
                    contentItemValue.put(QTY_MAX, mSlab.getQty_max());
                    contentItemValue.put(DISCOUNT_AMT, mSlab.getDiscount_amt());
                    db.insert(TABLE_DISCOUNT_SLAB, null, contentItemValue);
                }
            }

        }
    }

    //INSERT Category Discount
    public void insertCategoryDiscount(ArrayList<Discount> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            Discount mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(CATEGORY_ID, mItem.getCategoryId());
            contentValue.put(CUSTOMER_ID, mItem.getCustomerId());
            contentValue.put(TYPE, mItem.getType());
            contentValue.put(DISCOUNT_ID, mItem.getDiscountId());
            contentValue.put(DISCOUNT_TYPE, mItem.getDiscountType());
            contentValue.put(DISCOUNT, mItem.getDiscount());
            contentValue.put(DISCOUNT_AMT, mItem.getSales());
            contentValue.put(DISCOUNT_QTY, mItem.getQty());

            db.insert(TABLE_CATEGORY_DISCOUNT, null, contentValue);
        }
    }

    //INSERT Category Discount
    public void insertRouteCategoryDiscount(ArrayList<DiscountData> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            DiscountData mDisData = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(DISCOUNT_ID, mDisData.getDiscountId());
            contentValue.put(KEY_DATA, mDisData.getDiscount_desc());
            contentValue.put(KEY_DISCOUNT_KEY, mDisData.getDiscount_key());
            contentValue.put(DISCOUNT_TYPE, mDisData.getDiscount_type());
            contentValue.put(DISCOUNT_MAIN_TYPE, "2");
            contentValue.put(DISCOUNT, mDisData.getType());

            db.insert(TABLE_DISCOUNT_MAIN_HEADER, null, contentValue);

            if (!mDisData.getExclude_customer().isEmpty()) {
                String[] mDisCus = mDisData.getExclude_customer().split(",");
                for (int j = 0; j < mDisCus.length; j++) {
                    ContentValues contentItemValue = new ContentValues();

                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(CUSTOMER_ID, mDisCus[j]);
                    db.insert(TABLE_DISCOUNT_CUSTOMER_EXCLUDE, null, contentItemValue);
                }
            }

            if (!mDisData.getMaterial_category().isEmpty()) {
                String[] mDisCus = mDisData.getMaterial_category().split(",");
                for (int j = 0; j < mDisCus.length; j++) {
                    ContentValues contentItemValue = new ContentValues();

                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(CATEGORY_ID, mDisCus[j]);
                    db.insert(TABLE_DISCOUNT_MAIN_CATEGORY, null, contentItemValue);
                }
            }

            if (!mDisData.getItemId().isEmpty()) {
                String[] mDisItem = mDisData.getItemId().split(",");
                for (int j = 0; j < mDisItem.length; j++) {
                    ContentValues contentItemValue = new ContentValues();

                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(ITEM_ID, mDisItem[j]);
                    contentItemValue.put(DISCOUNT_MAIN_TYPE, "2");
                    db.insert(TABLE_DISCOUNT_SITEM, null, contentItemValue);
                }
            }

            if (mDisData.getDiscountSlab().size() > 0) {
                for (int k = 0; k < mDisData.getDiscountSlab().size(); k++) {

                    ItemSlab mSlab = mDisData.getDiscountSlab().get(k);

                    ContentValues contentItemValue = new ContentValues();
                    contentItemValue.put(DISCOUNT_ID, mDisData.getDiscountId());
                    contentItemValue.put(QTY_MIN, mSlab.getQty_min());
                    contentItemValue.put(QTY_MAX, mSlab.getQty_max());
                    contentItemValue.put(DISCOUNT_AMT, mSlab.getDiscount_amt());
                    db.insert(TABLE_DISCOUNT_SLAB, null, contentItemValue);
                }
            }

        }
    }

    //INSERT Customer Type
    public void insertCustomerType(ArrayList<CustomerType> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            CustomerType mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(CATEGORY_TYPE_ID, mItem.getCategoryId());
            contentValue.put(CATEGORY_CODE, mItem.getCategoryCode());
            contentValue.put(CATEGORY_NAME, mItem.getCategoryName());

            db.insert(TABLE_CUSTOMER_TYPE, null, contentValue);
        }
    }

    //INSERT Customer Type
    public void insertCustomerChannel(ArrayList<ChannellType> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            ChannellType mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(CATEGORY_TYPE_ID, mItem.getId());
            contentValue.put(CATEGORY_CODE, mItem.getOutlet_channel_code());
            contentValue.put(CATEGORY_NAME, mItem.getOutlet_channel());

            db.insert(TABLE_CUSTOMER_CHANNEL, null, contentValue);
        }
    }

    //INSERT Customer Type
    public void insertCustomerCategory(ArrayList<CustomerType> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            CustomerType mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(CATEGORY_TYPE_ID, mItem.getCategoryId());
            contentValue.put(CATEGORY_CODE, mItem.getCategoryCode());
            contentValue.put(CATEGORY_NAME, mItem.getCategoryName());
            contentValue.put(CUSTOMER_CHANNEL_ID, mItem.getChannelId());

            db.insert(TABLE_CUSTOMER_CATEGORY, null, contentValue);
        }
    }

    //INSERT Customer Type
    public void insertCustomerSubCategory(ArrayList<SubCategoryType> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {

            SubCategoryType mItem = arrData.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(CATEGORY_TYPE_ID, mItem.getId());
            contentValue.put(CATEGORY_CODE, mItem.getCategory_code());
            contentValue.put(CATEGORY_NAME, mItem.getName());

            db.insert(TABLE_CUSTOMER_SUB_CATEGORY, null, contentValue);
        }
    }


    //get Customer Pricing Items
    public boolean isPricingItems(String custId, String itemId) {
        boolean isContain = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_PRICING + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + ITEM_ID + "=?", new String[]{custId, itemId});
            if (cursor.getCount() > 0) {
                isContain = true;
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isContain;
    }

    //get Agent Pricing Items
    public boolean isAgentPricingItems(String itemId, String routeId) {
        boolean isContain = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_PRICING + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + ROUTE_ID + "=?", new String[]{itemId, routeId});
            if (cursor.getCount() > 0) {
                isContain = true;
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isContain;
    }

    //get Customer Pricing Items
    public Item getCustPricingItems(String custId, String itemId) {
        Item mItem = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_PRICING + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + ITEM_ID + "=?", new String[]{custId, itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        if (cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)) != null) {
                            mItem.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        } else {
                            mItem.setAlterUOMPrice("0");
                        }

                        if (cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)) != null) {
                            mItem.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        } else {
                            mItem.setBaseUOMPrice("0");
                        }

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mItem;
    }

    //get Agent Pricing Items
    public Item getAgentPricingItems(String itemId) {
        Item mItem = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_PRICING + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        if (cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)) != null) {
                            mItem.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        } else {
                            mItem.setAlterUOMPrice("0");
                        }

                        if (cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)) != null) {
                            mItem.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        } else {
                            mItem.setBaseUOMPrice("0");
                        }

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mItem;
    }

    //get Customer Pricing Items
    public double getTimeslotSales(String minTime, String maxTime) {
        double totalSale = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER + " WHERE " +
                    SALE_TIME + ">?" + " AND "
                    + SALE_TIME + "<?", new String[]{minTime, maxTime});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String netSale = cursor.getString(cursor.getColumnIndex(SVH_TOT_AMT_SALES));
                        totalSale += Double.parseDouble(netSale);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalSale;
    }

    //Is customer Item Discount
    public boolean isCustomerDiscount(String custId, String itemId) {
        boolean isAvailable = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_DISCOUNT + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + ITEM_ID + "=?", new String[]{custId, itemId});
            if (cursor.getCount() > 0) {
                isAvailable = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAvailable;
    }

    //Is Category  Discount
    public boolean isCustomerSpecificPromo(String custId) {
        boolean isOther = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PROMO_CUSTOMER + " WHERE " +
                    CUST_ID + "=?", new String[]{custId});

            if (cursor.getCount() > 0) {
                isOther = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOther;
    }

    //Is Category  Discount
    public boolean isCustomerRoutePromoExclude(String custId) {
        boolean isOther = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PROMO_CUSTOMER_EXCLUDE + " WHERE " +
                    CUST_ID + "=?", new String[]{custId});

            if (cursor.getCount() > 0) {
                isOther = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOther;
    }

    //Get Route Category Discount
    public boolean isDiscountItemExist(String itemId, String discountId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_SITEM + " WHERE " +
                    ITEM_ID + "=? AND " + DISCOUNT_ID + "=?", new String[]{itemId, discountId});
            if (cursor.getCount() > 0) {
                isPromo = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public boolean isItemDiscount(String itemId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_SITEM + " WHERE " +
                    ITEM_ID + "=? AND " + DISCOUNT_MAIN_TYPE + "=?", new String[]{itemId, "3"});
            if (cursor.getCount() > 0) {
                isPromo = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public boolean isCustomerDiscount(String customerId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_CUSTOMER_HEADER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            if (cursor.getCount() > 0) {
                isPromo = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public boolean isExcludeCustomerDiscount(String customerId, String discountId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_CUSTOMER_EXCLUDE + " WHERE " +
                    CUSTOMER_ID + "=? AND " + DISCOUNT_ID + "=?", new String[]{customerId, discountId});
            if (cursor.getCount() > 0) {
                isPromo = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public boolean isCategoryDiscount() {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_MAIN_HEADER + " WHERE " +
                    DISCOUNT_MAIN_TYPE + "=?", new String[]{"2"});
            if (cursor.getCount() > 0) {
                isPromo = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public String getItemDiscountId(String itemId) {

        String discountId = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_SITEM + " WHERE " +
                    ITEM_ID + "=? AND " + DISCOUNT_MAIN_TYPE + "=?", new String[]{itemId, "3"});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        discountId = cursor.getString(cursor.getColumnIndex(DISCOUNT_ID));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return discountId;
    }

    //Get Route Category Discount
    public String getCustomerDiscountId(String itemId) {

        String discountId = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_CUSTOMER_HEADER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        discountId = cursor.getString(cursor.getColumnIndex(DISCOUNT_ID));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return discountId;
    }

    //Get Route Category Discount
    public ArrayList<String> getDiscountCategory(String itemId) {

        ArrayList<String> arrCat = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_MAIN_CATEGORY + " WHERE " +
                    DISCOUNT_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String catId = cursor.getString(cursor.getColumnIndex(CATEGORY_ID));
                        arrCat.add(catId);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrCat;
    }

    //Get Route Category Discount
    public ArrayList<DiscountHeader> getDiscountHeader(String itemId) {

        ArrayList<DiscountHeader> arrCat = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_MAIN_HEADER + " WHERE " +
                    DISCOUNT_MAIN_TYPE + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        DiscountHeader mHeader = new DiscountHeader();
                        mHeader.setName(cursor.getString(cursor.getColumnIndex(KEY_DATA)));
                        mHeader.setPriority(cursor.getString(cursor.getColumnIndex(KEY_DISCOUNT_KEY)));
                        arrCat.add(mHeader);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrCat;
    }

    //Get Route Category Discount
    public String getCategoryDiscountId() {

        String discountId = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_MAIN_HEADER + " WHERE " +
                    DISCOUNT_MAIN_TYPE + "=?", new String[]{"2"});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        discountId = cursor.getString(cursor.getColumnIndex(DISCOUNT_ID));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return discountId;
    }

    //Get Route Category Discount
    public Discount getDiscountDetail(String discountId) {

        Discount mDiscount = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_MAIN_HEADER + " WHERE " +
                    DISCOUNT_ID + "=?", new String[]{discountId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        mDiscount = new Discount();
                        mDiscount.setDiscountId(cursor.getString(cursor.getColumnIndex(DISCOUNT_ID)));
                        mDiscount.setType(cursor.getString(cursor.getColumnIndex(DISCOUNT)));
                        mDiscount.setDiscountType(cursor.getString(cursor.getColumnIndex(DISCOUNT_TYPE)));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mDiscount;
    }

    //Get Route Category Discount
    public boolean isSlabDiscountValue(double price, String discountId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_SLAB + " WHERE " +
                    DISCOUNT_ID + "=?", new String[]{discountId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (price >= Double.parseDouble(qtyMin) && price <= Double.parseDouble(qtyMaxx)) {
                            isPromo = true;
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public boolean isSlabDiscountQty(int price, String discountId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_SLAB + " WHERE " +
                    DISCOUNT_ID + "=?", new String[]{discountId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (price >= Integer.parseInt(qtyMin) && price <= Integer.parseInt(qtyMaxx)) {
                            isPromo = true;
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }


    //Get Route Category Discount
    public String getSlabDiscountValue(double price, String discountId) {

        String mValue = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_SLAB + " WHERE " +
                    DISCOUNT_ID + "=?", new String[]{discountId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (price >= Double.parseDouble(qtyMin) && price <= Double.parseDouble(qtyMaxx)) {
                            mValue = cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT));
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mValue;
    }

    //Get Route Category Discount
    public String getSlabDiscountQty(int price, String discountId) {

        String mValue = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISCOUNT_SLAB + " WHERE " +
                    DISCOUNT_ID + "=?", new String[]{discountId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (price >= Integer.parseInt(qtyMin) && price <= Integer.parseInt(qtyMaxx)) {
                            mValue = cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT));
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mValue;
    }

    //Get Route Category Discount
    public boolean isSlabPromo(String qty, String itemId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PROMO_SLAB + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (Integer.parseInt(qty) >= Integer.parseInt(qtyMin) && Integer.parseInt(qty) <= Integer.parseInt(qtyMaxx)) {
                            isPromo = true;
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public boolean isSlabCustomerPromo(String qty, String itemId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_PROMO_SLAB + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (Integer.parseInt(qty) >= Integer.parseInt(qtyMin) && Integer.parseInt(qty) <= Integer.parseInt(qtyMaxx)) {
                            isPromo = true;
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public boolean isSlabAgentPromo(String qty, String itemId) {

        boolean isPromo = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_PROMO_SLAB + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (Integer.parseInt(qty) >= Integer.parseInt(qtyMin) && Integer.parseInt(qty) <= Integer.parseInt(qtyMaxx)) {
                            isPromo = true;
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPromo;
    }

    //Get Route Category Discount
    public String getSlabPromoOfferQty(String qty, String itemId) {

        String offerQTy = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PROMO_SLAB + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (Integer.parseInt(qty) >= Integer.parseInt(qtyMin) && Integer.parseInt(qty) <= Integer.parseInt(qtyMaxx)) {
                            offerQTy = cursor.getString(cursor.getColumnIndex(QTY));
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return offerQTy;
    }

    //Get Route Category Discount
    public String getSlabCustomerPromoOfferQty(String qty, String itemId) {

        String offerQTy = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_PROMO_SLAB + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (Integer.parseInt(qty) >= Integer.parseInt(qtyMin) && Integer.parseInt(qty) <= Integer.parseInt(qtyMaxx)) {
                            offerQTy = cursor.getString(cursor.getColumnIndex(QTY));
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return offerQTy;
    }

    //Get Route Category Discount
    public String getSlabAgentPromoOfferQty(String qty, String itemId) {

        String offerQTy = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_PROMO_SLAB + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        String qtyMin = cursor.getString(cursor.getColumnIndex(QTY_MIN));
                        String qtyMaxx = cursor.getString(cursor.getColumnIndex(QTY_MAX));

                        if (Integer.parseInt(qty) >= Integer.parseInt(qtyMin) && Integer.parseInt(qty) <= Integer.parseInt(qtyMaxx)) {
                            offerQTy = cursor.getString(cursor.getColumnIndex(QTY));
                        }

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return offerQTy;
    }

    //get Category Sale
    public double getCategorySales(String catId) {
        double totalSale = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    ITEM_CATEGORY + "=?", new String[]{catId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String netSale = cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL));
                        String vatAmt = cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL));
                        totalSale += (Double.parseDouble(netSale) + Double.parseDouble(vatAmt));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalSale;
    }

    //get Free Goods available
    public boolean isFreeGoods(String itemId, String customerId, String uom, String qty) {

        boolean isFree = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            System.out.println("QTY-->" + customerId);
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_FREE_GOODS + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + ITEM_UOM + "=?", new String[]{itemId, uom});
           /* Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_FREE_GOODS + " WHERE " +
                    ITEM_ID + "=?" , new String[]{itemId});*/
            System.out.println("QTYp-->" + cursor.getCount());
            if (cursor.getCount() > 0) {
                for (int i = -1; i < cursor.getCount(); i++) {
                    if (cursor.moveToPosition(i)) {
                        int iqty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        System.out.println("QTYpo-->" + iqty + " qty--> " + qty);
                        if (Integer.parseInt(qty) >= iqty) {
                            isFree = true;
                            // mItem.setQty(cursor.getString(cursor.getColumnIndex(FOC_ITEM_QTY)));
                        }
                    }
                }

            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isFree;
    }

    //get Free Goods available
    public boolean isAgentFreeGoods(String itemId, String customerId, String uom, String qty) {

        boolean isFree = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            System.out.println("QTY-->" + customerId);
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_FREE_GOODS + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + ITEM_UOM + "=?", new String[]{itemId, uom});
           /* Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_FREE_GOODS + " WHERE " +
                    ITEM_ID + "=?" , new String[]{itemId});*/
            System.out.println("QTYp-->" + cursor.getCount());
            if (cursor.getCount() > 0) {
                for (int i = -1; i < cursor.getCount(); i++) {
                    if (cursor.moveToPosition(i)) {
                        int iqty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        System.out.println("QTYpo-->" + iqty + " qty--> " + qty);
                        if (Integer.parseInt(qty) >= iqty) {
                            isFree = true;
                            // mItem.setQty(cursor.getString(cursor.getColumnIndex(FOC_ITEM_QTY)));
                        }
                    }
                }

            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isFree;
    }


    //get Free Goods available
    public Item getFreeGoodsItem(String itemId, String customerId, String uom, String qty) {

        Item mItem = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_FREE_GOODS + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + ITEM_UOM + "=?", new String[]{itemId, uom});
            /*if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    mItem.setItemId(cursor.getString(cursor.getColumnIndex(FOC_ITEM_ID)));
                    mItem.setQty(cursor.getString(cursor.getColumnIndex(FOC_ITEM_QTY)));
                    mItem.setUom(cursor.getString(cursor.getColumnIndex(FOC_ITEM_UOM)));
                    mItem.setQualitQty(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                }
            }*/

            if (cursor.getCount() > 0) {
                for (int i = -1; i < cursor.getCount(); i++) {
                    if (cursor.moveToPosition(i)) {
                        int iqty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        System.out.println("QTYpo1-->" + iqty + " qty--> " + qty);
                        if (Integer.parseInt(qty) >= iqty) {
                            mItem.setItemId(cursor.getString(cursor.getColumnIndex(FOC_ITEM_ID)));
                            mItem.setQty(cursor.getString(cursor.getColumnIndex(FOC_ITEM_QTY)));
                            mItem.setUom(cursor.getString(cursor.getColumnIndex(FOC_ITEM_UOM)));
                            mItem.setQualitQty(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        }
                    }
                }

            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mItem;
    }

    //get Free Goods available
    public Item getAgentFreeGoodsItem(String itemId, String customerId, String uom, String qty) {

        Item mItem = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_FREE_GOODS + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + ITEM_UOM + "=?", new String[]{itemId, uom});
            /*if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    mItem.setItemId(cursor.getString(cursor.getColumnIndex(FOC_ITEM_ID)));
                    mItem.setQty(cursor.getString(cursor.getColumnIndex(FOC_ITEM_QTY)));
                    mItem.setUom(cursor.getString(cursor.getColumnIndex(FOC_ITEM_UOM)));
                    mItem.setQualitQty(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                }
            }*/

            if (cursor.getCount() > 0) {
                for (int i = -1; i < cursor.getCount(); i++) {
                    if (cursor.moveToPosition(i)) {
                        int iqty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        System.out.println("QTYpo1-->" + iqty + " qty--> " + qty);
                        if (Integer.parseInt(qty) >= iqty) {
                            mItem.setItemId(cursor.getString(cursor.getColumnIndex(FOC_ITEM_ID)));
                            mItem.setQty(cursor.getString(cursor.getColumnIndex(FOC_ITEM_QTY)));
                            mItem.setUom(cursor.getString(cursor.getColumnIndex(FOC_ITEM_UOM)));
                            mItem.setQualitQty(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        }
                    }
                }

            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mItem;
    }

    //get Free Goods available
    public boolean isFreeGoodAvailable(String itemId, String type, String saleQty) {

        boolean isContain = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {

                    int qty = 0;
                    if (type.equalsIgnoreCase("Base")) {
                        qty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                    } else {
                        qty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                    }

                    if (qty >= Integer.parseInt(saleQty)) {
                        isContain = true;
                    }
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isContain;
    }

    //get Free Goods available
    public boolean isItemAvailable(String itemId, String saleQty, String saleAQty) {

        boolean isContain = false;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {

                    int qBty = 0, aQty = 0;
                    qBty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                    aQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));

                    if (qBty >= Integer.parseInt(saleQty) && aQty >= Integer.parseInt(saleAQty)) {
                        isContain = true;
                    }
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isContain;
    }


    //get Customer Pricing Items
    public String getItemBase(String itemId) {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        basPrice = cursor.getString(cursor.getColumnIndex(ITEM_UOM_PRICE));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get Salesman Region
    public String getSalesmanRegion(String salesmanId) {

        String region = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SALES_MAN + " WHERE " +
                    SALESMAN_ID + "=?", new String[]{salesmanId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        region = cursor.getString(cursor.getColumnIndex(SALESMAN_REGION));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return region;
    }

    //get Agent Credit Day
    public String getAgentCreditDay(String agentId) {

        String region = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT + " WHERE " +
                    AGENT_ID + "=?", new String[]{agentId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        region = cursor.getString(cursor.getColumnIndex(CREDITDAY));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return region;
    }


    //get Customer Pricing Items
    public String getItemAlter(String itemId) {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        basPrice = cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //INSERT Load Request Items
    public void insertLoadRequestItems(String orderNo, String orderDate, String deliveryDate, String orderAmt,
                                       String salesmanId, String vatAmt, String preVatAmt, String excise, String netAmt,
                                       ArrayList<Item> mItem, String agentId, ArrayList<Item> arrFOCItem, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHead = new ContentValues();

        contentValueHead.put(ORDER_NO, orderNo);
        contentValueHead.put(ORDER_DATE, orderDate);
        contentValueHead.put(DELIVERY_DATE, deliveryDate);
        contentValueHead.put(ORDER_AMOUNT, orderAmt);
        contentValueHead.put(SALESMAN_ID, salesmanId);
        contentValueHead.put(ITEM_VAT_VAL, vatAmt);
        contentValueHead.put(ITEM_PRE_VAT, preVatAmt);
        contentValueHead.put(ITEM_EXCISE_VAL, excise);
        contentValueHead.put(ITEM_NET_VAL, netAmt);
        contentValueHead.put(AGENT_ID, agentId);
        contentValueHead.put(ORDER_COMMENT, comment);
        contentValueHead.put(DATA_MARK_FOR_POST, "M");
        contentValueHead.put(IS_POSTED, "0");

        db.insert(TABLE_LOAD_REQUEST_HEADER, null, contentValueHead);


        for (int i = 0; i < mItem.size(); i++) {
            ContentValues contentValue = new ContentValues();

            if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0 && Integer.parseInt(mItem.get(i).getSaleAltQty()) > 0) {

                //Insert Base UOM Item
                insertORDBaseItem(orderNo, orderDate, mItem.get(i), "Load", "", "");

                //Insert Alter UOM Item
                insertORDAlterItem(orderNo, orderDate, mItem.get(i), "Load", "", "");
            } else {

                contentValue.put(ORDER_NO, orderNo);
                contentValue.put(ORDER_DATE, orderDate);
                contentValue.put(ITEM_ID, mItem.get(i).getItemId());
                contentValue.put(ITEM_CODE, mItem.get(i).getItemCode());
                contentValue.put(ITEM_NAME, mItem.get(i).getItemName());
                contentValue.put(ITEM_BASEUOM, mItem.get(i).getBaseUOM());
                contentValue.put(ITEM_BASE_UOM_QTY, mItem.get(i).getSaleBaseQty());
                contentValue.put(ITEM_BASE_PRICE, mItem.get(i).getSaleBasePrice());
                contentValue.put(ITEM_ALRT_UOM, mItem.get(i).getAltrUOM());
                contentValue.put(ITEM_ALTER_UOM_QTY, mItem.get(i).getSaleAltQty());
                contentValue.put(ITEM_ALRT_UOM_PRCE, mItem.get(i).getSaleAltPrice());
                contentValue.put(ALRT_PRCE, mItem.get(i).getAlterUOMPrice());
                contentValue.put(BASE_PRCE, mItem.get(i).getUOMPrice());
                contentValue.put(ITEM_CATEGORY, mItem.get(i).getCategory());
                contentValue.put(ITEM_PRE_VAT, mItem.get(i).getPreVatAmt());
                contentValue.put(ITEM_VAT_VAL, mItem.get(i).getVatAmt());
                contentValue.put(ITEM_EXCISE_VAL, mItem.get(i).getExciseAmt());
                contentValue.put(ITEM_NET_VAL, mItem.get(i).getNetAmt());
                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(DISCOUNT, mItem.get(i).getDiscountPer());
                    contentValue.put(DISCOUNT_AMT, mItem.get(i).getDiscountAmt());
                } else {
                    contentValue.put(DISCOUNT, mItem.get(i).getDiscountAlPer());
                    contentValue.put(DISCOUNT_AMT, mItem.get(i).getDiscountAlAmt());
                }

                if (Integer.parseInt(mItem.get(i).getSaleBaseQty()) > 0) {
                    contentValue.put(ITEM_UOM_TYPE, "Base");
                } else {
                    contentValue.put(ITEM_UOM_TYPE, "Alter");
                }

                db.insert(TABLE_LOAD_REQUEST_ITEMS, null, contentValue);


            }

            if (arrFOCItem.size() > 0) {
                boolean isCOntain = false;
                int position = 0;
                for (int j = 0; j < arrFOCItem.size(); j++) {
                    if (arrFOCItem.get(j).getItemId().equals(mItem.get(i).getItemId())) {
                        isCOntain = true;
                        position = j;
                        if (Integer.parseInt(arrFOCItem.get(j).getSaleBaseQty()) > 0) {
                            //Insert Base UOM Item
                            insertORDBaseItem(orderNo, orderDate, arrFOCItem.get(j), "Load", "", "");

                        } else if (Integer.parseInt(arrFOCItem.get(j).getSaleAltQty()) > 0) {
                            //Insert Alter UOM Item
                            insertORDAlterItem(orderNo, orderDate, arrFOCItem.get(j), "Load", "", "");
                        }

                        break;
                    }
                }

                if (isCOntain) {
                    arrFOCItem.remove(position);
                }
            }
        }

        for (int j = 0; j < arrFOCItem.size(); j++) {
            if (Integer.parseInt(arrFOCItem.get(j).getSaleBaseQty()) > 0) {
                //Insert Base UOM Item
                insertORDBaseItem(orderNo, orderDate, arrFOCItem.get(j), "Load", "", "");

            } else if (Integer.parseInt(arrFOCItem.get(j).getSaleAltQty()) > 0) {
                //Insert Alter UOM Item
                insertORDAlterItem(orderNo, orderDate, arrFOCItem.get(j), "Load", "", "");
            }
        }

    }

    //INSERT OUTSTANDING COLLECTION
    public void insertOutstandingCollection(ArrayList<CollectionData> arrCollection) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrCollection.size(); i++) {

            CollectionData mColl = arrCollection.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(KEY_ORDER_ID, mColl.getOrderId());
            contentValue.put(CUSTOMER_ID, mColl.getCustomerNo());
            contentValue.put(CUSTOMER_TYPE, getCustomerType(mColl.getCustomerNo()));
            contentValue.put(KEY_COLLECTION_TYPE, "");
            contentValue.put(KEY_INVOICE_NO, mColl.getInvoiceNo());
            contentValue.put(KEY_INVOICE_DATE, mColl.getInvoiceDate());
            contentValue.put(KEY_INVOICE_AMOUNT, mColl.getInvoiceAmount());
            contentValue.put(KEY_DUE_DATE, mColl.getInvoiceDueDate());
            contentValue.put(KEY_INDICATOR, App.ADD_INDICATOR);
            contentValue.put(KEY_AMOUNT_CLEARED, mColl.getAmountCleared());
            contentValue.put(KEY_AMOUNT_PAY, "0");
            contentValue.put(KEY_CHEQUE_AMOUNT, "0");
            contentValue.put(KEY_CHEQUE_AMOUNTPRE, "0");
            contentValue.put(KEY_CHEQUE_AMOUNT_INDIVIDUAL, "0");
            contentValue.put(KEY_CHEQUE_NUMBER, "0000");
            contentValue.put(KEY_CHEQUE_DATE, "0000");
            contentValue.put(KEY_CHEQUE_BANK_CODE, "0000");
            contentValue.put(KEY_CHEQUE_BANK_NAME, "0000");
            contentValue.put(KEY_CASH_AMOUNT, "0");
            contentValue.put(KEY_CASH_AMOUNTPRE, "0");
            contentValue.put(KEY_IS_INVOICE_COMPLETE, App.INVOICE_INCOMPLETE);
            contentValue.put(DATA_MARK_FOR_POST, "N");
            contentValue.put(IS_POSTED, "0");
            contentValue.put(KEY_IS_OUTSTANDING, "1");

            db.insert(TABLE_COLLECTION, null, contentValue);
        }
    }

    //INSERT DEPOT
    public void insertDepot(ArrayList<DepotData> arrDepot) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrDepot.size(); i++) {

            DepotData mDepot = arrDepot.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(DEPOT_ID, mDepot.getDepotId());
            contentValue.put(DEPOT_NAME, mDepot.getDepotName());
            contentValue.put(AGENT_ID, mDepot.getAgentId());
            contentValue.put(AGENT_NAME, mDepot.getAgentName());
            contentValue.put(CREDITDAY, mDepot.getCreditDay());

            db.insert(TABLE_DEPOT, null, contentValue);
        }
    }

    /*//INSERT OUTSTANDING COLLECTION
    public void insertOutstandingCollection(ArrayList<Collection> arrCollection) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrCollection.size(); i++) {

            Collection mColl = arrCollection.get(i);

            ContentValues contentValue = new ContentValues();

            contentValue.put(SVH_CODE, mColl.getColl_invoiceNo());
            contentValue.put(COLL_NO, mColl.getColl_Id());
            contentValue.put(CUSTOMER_ID, mColl.getColl_customerId());
            contentValue.put(CUSTOMER_TYPE, getCustomerType(mColl.getColl_customerId()));
            contentValue.put(COLL_TYPE, "");
            contentValue.put(DUE_DATE, mColl.getColl_dueDate());
            contentValue.put(INVOICE_DATE, mColl.getColl_invoiceDate());
            contentValue.put(INVOICE_AMT, mColl.getColl_amount());
            contentValue.put(AMT_PAY, "0");
            contentValue.put(CASH_AMT, "0");
            contentValue.put(CHEQUE_AMT, "0");
            contentValue.put(CHEQUE_NO, "");
            contentValue.put(CHEQUE_DATE, "");
            contentValue.put(BANK_NAME, "");
            contentValue.put(COLL_ISCOLLECTED, "0");
            contentValue.put(AMT_CLEARED, mColl.getColl_paidAmount());
            contentValue.put(COLL_PAYABLE, "0");
            contentValue.put(DATA_MARK_FOR_POST, "N");

            db.insert(TABLE_COLLECTION, null, contentValue);
        }
    }*/

    //INSERT COLLECTION
    public void insertCollection(String invNo, String coll_No, String cust_Code, String coll_Type, String dueDate, String invoiceDate,
                                 String invAmt, String amtPay, String cashAmt, String chequeAmt, String chequeNo, String chequeDate,
                                 String bankName, String amtClear, String isPayable, String markPost) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(SVH_CODE, invNo);
        contentValue.put(COLL_NO, coll_No);
        contentValue.put(CUSTOMER_ID, cust_Code);
        contentValue.put(CUSTOMER_TYPE, getCustomerType(cust_Code));
        contentValue.put(COLL_TYPE, coll_Type);
        contentValue.put(DUE_DATE, dueDate);
        contentValue.put(INVOICE_DATE, invoiceDate);
        contentValue.put(INVOICE_AMT, invAmt);
        contentValue.put(AMT_PAY, amtPay);
        contentValue.put(CASH_AMT, cashAmt);
        contentValue.put(CHEQUE_AMT, chequeAmt);
        contentValue.put(CHEQUE_NO, chequeNo);
        contentValue.put(CHEQUE_DATE, chequeDate);
        contentValue.put(BANK_NAME, bankName);
        contentValue.put(COLL_ISCOLLECTED, "0");
        contentValue.put(AMT_CLEARED, amtClear);
        contentValue.put(COLL_PAYABLE, isPayable);
        contentValue.put(DATA_MARK_FOR_POST, markPost);

        db.insert(TABLE_COLLECTION, null, contentValue);
    }

    public void addCollection(String invNo, String coll_No, String cust_Code, String dueDate, String invoiceDate,
                              String invAmt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(KEY_COLLECTION_TYPE, App.COLLECTION_INVOICE);
        contentValue.put(CUSTOMER_ID, cust_Code);
        contentValue.put(CUSTOMER_TYPE, getCustomerType(cust_Code));
        contentValue.put(KEY_INVOICE_NO, invNo);
        contentValue.put(KEY_ORDER_ID, coll_No);
        contentValue.put(KEY_INVOICE_AMOUNT, invAmt);
        contentValue.put(KEY_INVOICE_DATE, invoiceDate);
        contentValue.put(KEY_DUE_DATE, dueDate);
        contentValue.put(KEY_AMOUNT_CLEARED, "0");
        contentValue.put(KEY_CHEQUE_AMOUNT, "0");
        contentValue.put(KEY_AMOUNT_PAY, "0");
        contentValue.put(KEY_CHEQUE_AMOUNTPRE, "0");
        contentValue.put(KEY_CHEQUE_AMOUNT_INDIVIDUAL, "0");
        contentValue.put(KEY_CHEQUE_NUMBER, "0000");
        contentValue.put(KEY_CHEQUE_DATE, "0000");
        contentValue.put(KEY_CHEQUE_BANK_CODE, "0000");
        contentValue.put(KEY_CHEQUE_BANK_NAME, "0000");
        contentValue.put(KEY_CASH_AMOUNT, "0");
        contentValue.put(KEY_CASH_AMOUNTPRE, "0");
        contentValue.put(KEY_INDICATOR, App.ADD_INDICATOR);
        contentValue.put(KEY_IS_INVOICE_COMPLETE, App.INVOICE_INCOMPLETE);
        contentValue.put(DATA_MARK_FOR_POST, "N");
        contentValue.put(IS_POSTED, "0");
        contentValue.put(KEY_IS_OUTSTANDING, "0");

        db.insert(TABLE_COLLECTION, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(TR_TYPE, transaction.tr_type);
        contentValue.put(TR_DATE, transaction.tr_date_time);
        contentValue.put(TR_CUSTOMER_NUM, transaction.tr_customer_num);
        contentValue.put(TR_CUSTOMER_NAME, transaction.tr_customer_name);
        contentValue.put(TR_SALESMAN_ID, transaction.tr_salesman_id);
        contentValue.put(TR_INVOICE_ID, transaction.tr_invoice_id);
        contentValue.put(TR_ORDER_ID, transaction.tr_order_id);
        contentValue.put(TR_COLLECTION_ID, transaction.tr_collection_id);
        contentValue.put(TR_PYAMENT_ID, transaction.tr_pyament_id);
        contentValue.put(TR_IS_POSTED, transaction.tr_is_posted);
        contentValue.put(KEY_DATA, transaction.tr_printData);

        db.insert(TABLE_TRANSACTION, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertServiceVisitPost(ServiceVisitPost transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(SERVICE_TYPE, transaction.serviceType);
        contentValue.put(TICKET_NO, transaction.ticketNo);
        contentValue.put(TIMEIN, transaction.timeIn);
        contentValue.put(TIME_OUT, transaction.timeOut);
        contentValue.put(LATITUDE, transaction.latitude);
        contentValue.put(LONGITUDE, transaction.longitude);
        contentValue.put(MODEL_NO, transaction.modelNo);
        contentValue.put(SV_SERIAL_NO, transaction.serialNo);
        contentValue.put(ASSETS_NO, transaction.assetNo);
        contentValue.put(SV_BRANDING, transaction.brand);
        contentValue.put(SV_NATURE, transaction.natureOfCall);
        contentValue.put(OUTLET_NAME, transaction.outletName);
        contentValue.put(OWNER_NAME, transaction.ownerName);
        contentValue.put(LOCATION, transaction.location);
        contentValue.put(IMAGE1, transaction.serialImage);
        contentValue.put(LANDMARK, transaction.landmark);
        contentValue.put(DISTRICT, transaction.district);
        contentValue.put(TOWN_VILLAGE, transaction.townVillage);
        contentValue.put(CONTACT_NUMBER, transaction.contactNumber);
        contentValue.put(CONTACT_NUMBER2, transaction.contactNumber2);
        contentValue.put(CONTACT_PERSON, transaction.contactPerson);
        contentValue.put(IS_WORKING_ANY, transaction.workingId);
        contentValue.put(WORKING_IMAGE, transaction.conditionImage);
        contentValue.put(CLEANLESS_ID, transaction.cleanlessId);
        contentValue.put(CLENLESS_IMAGE, transaction.cleanlessImage);
        contentValue.put(COIL_ID, transaction.coilId);
        contentValue.put(COIL_IMAGE, transaction.coilImage);
        contentValue.put(GASKET_ID, transaction.gasketId);
        contentValue.put(GASKET_IMAGE, transaction.gasketImage);
        contentValue.put(LIGHT_ID, transaction.lightId);
        contentValue.put(LIGHT_IMAGE, transaction.lightImage);
        contentValue.put(BRANDING_ID, transaction.brandingId);
        contentValue.put(BRANDING_IMAGE, transaction.brandingImage);
        contentValue.put(VENTILATION_ID, transaction.ventilationId);
        contentValue.put(VENTILATION_IMAGE, transaction.ventilationImage);
        contentValue.put(LEVELING_ID, transaction.levelingId);
        contentValue.put(LEVELING_IMAGE, transaction.levelingImage);
        contentValue.put(STOCK_ID, transaction.stockPer);
        contentValue.put(STOCK_IMAGE, transaction.stockImage);
        contentValue.put(CURRENT_VOLT, transaction.currentVolt);
        contentValue.put(AMPS, transaction.amps);
        contentValue.put(TEMPRATURE, transaction.temprature);
        contentValue.put(WORK_STATUS, transaction.workstatus);
        contentValue.put(PENDING_REASON, transaction.pendingReason);
        contentValue.put(PENDING_SPARE, transaction.pendingSpare);
        contentValue.put(SV_COMPLAIN_TYPE, transaction.workDoneType);
        contentValue.put(SV_COMMENT, transaction.workDoneComment);
        contentValue.put(SPARE_DETAIL, transaction.workSpare);
        contentValue.put(TECH_RATING, transaction.techRating);
        contentValue.put(QUALITY_RATE, transaction.qualityRate);
        contentValue.put(SV_DISPUTE, transaction.anyDispute);
        contentValue.put(DISPUTE_IMAGE1, transaction.disputeImage1);
        contentValue.put(DISPUTE_IMAGE2, transaction.disputeImage2);
        contentValue.put(SIGNATURE, transaction.customerSignature);
        contentValue.put(CTS_STATUS, transaction.ctsStatus);
        contentValue.put(CTS_COMMENT, transaction.ctcComment);
        contentValue.put(OTHER_COMMENT, transaction.otherSpecific);
        contentValue.put(COOLER_IMAGE1, transaction.coolerImage1);
        contentValue.put(COOLER_IMAGE2, transaction.coolerImage2);
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_SERVICE_VISIT_POST, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertChiller(String uniqId, String custId, String assetNo, String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(UNIQUE_ID, uniqId);
        contentValue.put(CUSTOMER_ID, custId);
        contentValue.put(FRIDGE_ASSETNUMBER, assetNo);
        contentValue.put(IMAGE1, image);
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_ADD_CHILLER, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertFreedge(String uniqId, Freeze mFridge) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(UNIQUE_ID, uniqId);
        contentValue.put(CUSTOMER_ID, mFridge.getCustomer_id());
        contentValue.put(FRIDGE_ASSETNUMBER, mFridge.getSerial_no());
        contentValue.put(IS_FREEZE_ASSIGN, mFridge.getHave_fridge());
        contentValue.put(COMMENT, mFridge.getComments());
        contentValue.put(TYPE, mFridge.getComplaint_type());
        contentValue.put(LATITUDE, mFridge.getLatitude());
        contentValue.put(LONGITUDE, mFridge.getLongitude());
        contentValue.put(IMAGE1, mFridge.getImage1());
        contentValue.put(IMAGE2, mFridge.getImage2());
        contentValue.put(IMAGE3, mFridge.getImage3());
        contentValue.put(IMAGE4, mFridge.getImage4());
        contentValue.put(SIGNATURE, mFridge.getFridge_scan_img());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_ADD_FRIDGE, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertChillerTrack(String uniqId, Freeze mFridge) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(UNIQUE_ID, uniqId);
        contentValue.put(CUSTOMER_ID, mFridge.getCustomer_id());
        contentValue.put(FRIDGE_ASSETNUMBER, mFridge.getSerial_no());
        contentValue.put(IS_FREEZE_ASSIGN, mFridge.getHave_fridge());
        contentValue.put(COMMENT, mFridge.getComments());
        contentValue.put(TYPE, mFridge.getComplaint_type());
        contentValue.put(LATITUDE, mFridge.getLatitude());
        contentValue.put(LONGITUDE, mFridge.getLongitude());
        contentValue.put(IMAGE1, mFridge.getImage1());
        contentValue.put(IMAGE2, mFridge.getImage2());
        contentValue.put(IMAGE3, mFridge.getImage3());
        contentValue.put(IMAGE4, mFridge.getImage4());
        contentValue.put(ROUTE_ID, mFridge.getRoute_id());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_CHILLER_TRACKING, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertChillerRequest(Customer mCustomer, String uniqId, String number, String landmark, Chiller_Model mPromotion) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(UNIQUE_ID, uniqId);
        contentValue.put(CUSTOMER_ID, mPromotion.getCustomer_id());
        contentValue.put(OWNER_NAME, mPromotion.getOwner_name());
        contentValue.put(CONTACT_NUMBER, number);
        contentValue.put(POSTAL_ADDRESS, mCustomer.getAddress());
        contentValue.put(LANDMARK, landmark);
        contentValue.put(LOCATION, mPromotion.getLocation());
        contentValue.put(OUTLET_TYPE, mPromotion.getOutlet_type());
        contentValue.put(OTHER_TYPE, mPromotion.getSpecify_if_other_type());
        contentValue.put(DISPLAY_LOCATION, mPromotion.getDisplay_location());
        contentValue.put(EXIST_COOLER, mPromotion.getExisting_coolers());
        contentValue.put(STOCK_COMPATITER, mPromotion.getStock_share_with_competitor());
        contentValue.put(WEEKLY_SALE_VOLUME, mPromotion.getOutlet_weekly_sale_volume());
        contentValue.put(WEEKLY_SALES, mPromotion.getOutlet_weekly_sales());
        contentValue.put(SIZE_REQUEST, mPromotion.getChiller_size_requested());
        contentValue.put(SAFTY_GRILL, mPromotion.getChiller_safty_grill());
        contentValue.put(NATIONAL_ID, mPromotion.getNational_id());
        contentValue.put(PASSWORD_PHOTO, mPromotion.getPassword_photo());
        contentValue.put(ADDRESS_PROOF, mPromotion.getOutlet_address_proof());
        contentValue.put(STAMP, mPromotion.getOutlet_stamp());
        contentValue.put(LC_LETTER, mPromotion.getLc_letter());
        contentValue.put(TREDING_LICENCE, mPromotion.getTrading_licence());
        contentValue.put(NATIONAL_ID_FRONT, mPromotion.getNational_id_file());
        contentValue.put(NATIONAL_ID_BACK, mPromotion.getNational_id1_file());
        contentValue.put(LC_FRONT, mPromotion.getLc_letter_file());
        contentValue.put(LC_BACK, mPromotion.getLc_letter1_file());
        contentValue.put(STAMP_FRONT, mPromotion.getOutlet_stamp_file());
        contentValue.put(STAMP_BACK, mPromotion.getOutlet_stamp1_file());
        contentValue.put(ADDRESS_PROOF_FRONT, mPromotion.getOutlet_address_proof_file());
        contentValue.put(ADDRESS_PROOF_BACK, mPromotion.getOutlet_address_proof1_file());
        contentValue.put(TRADING_FRONT, mPromotion.getTrading_licence_file());
        contentValue.put(TRADING_BACK, mPromotion.getTrading_licence1_file());
        contentValue.put(PASSPORT_ID_FRONT, mPromotion.getPassword_photo_file());
        contentValue.put(PASSPORT_ID_BACK, mPromotion.getPassword_photo1_file());
        contentValue.put(SIGNATURE, mPromotion.getSign__customer_file());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_CHILLER_REQUEST, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertChillerAddRequest(Customer mCustomer, String uniqId, String number, String landmark, Chiller_Model mPromotion) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(UNIQUE_ID, uniqId);
        contentValue.put(SERIAL_NO, mPromotion.getSerialNo());
        contentValue.put(CUSTOMER_ID, mPromotion.getCustomer_id());
        contentValue.put(OWNER_NAME, mPromotion.getOwner_name());
        contentValue.put(CONTACT_NUMBER, number);
        contentValue.put(POSTAL_ADDRESS, mCustomer.getAddress());
        contentValue.put(LANDMARK, landmark);
        contentValue.put(LOCATION, mPromotion.getLocation());
        contentValue.put(OUTLET_TYPE, mPromotion.getOutlet_type());
        contentValue.put(OTHER_TYPE, mPromotion.getSpecify_if_other_type());
        contentValue.put(DISPLAY_LOCATION, mPromotion.getDisplay_location());
        contentValue.put(EXIST_COOLER, mPromotion.getExisting_coolers());
        contentValue.put(STOCK_COMPATITER, mPromotion.getStock_share_with_competitor());
        contentValue.put(WEEKLY_SALE_VOLUME, mPromotion.getOutlet_weekly_sale_volume());
        contentValue.put(WEEKLY_SALES, mPromotion.getOutlet_weekly_sales());
        contentValue.put(SIZE_REQUEST, mPromotion.getChiller_size_requested());
        contentValue.put(SAFTY_GRILL, mPromotion.getChiller_safty_grill());
        contentValue.put(NATIONAL_ID, mPromotion.getNational_id());
        contentValue.put(PASSWORD_PHOTO, mPromotion.getPassword_photo());
        contentValue.put(ADDRESS_PROOF, mPromotion.getOutlet_address_proof());
        contentValue.put(STAMP, mPromotion.getOutlet_stamp());
        contentValue.put(LC_LETTER, mPromotion.getLc_letter());
        contentValue.put(TREDING_LICENCE, mPromotion.getTrading_licence());
        contentValue.put(NATIONAL_ID_FRONT, mPromotion.getNational_id_file());
        contentValue.put(NATIONAL_ID_BACK, mPromotion.getNational_id1_file());
        contentValue.put(LC_FRONT, mPromotion.getLc_letter_file());
        contentValue.put(LC_BACK, mPromotion.getLc_letter1_file());
        contentValue.put(STAMP_FRONT, mPromotion.getOutlet_stamp_file());
        contentValue.put(STAMP_BACK, mPromotion.getOutlet_stamp1_file());
        contentValue.put(ADDRESS_PROOF_FRONT, mPromotion.getOutlet_address_proof_file());
        contentValue.put(ADDRESS_PROOF_BACK, mPromotion.getOutlet_address_proof1_file());
        contentValue.put(TRADING_FRONT, mPromotion.getTrading_licence_file());
        contentValue.put(TRADING_BACK, mPromotion.getTrading_licence1_file());
        contentValue.put(PASSPORT_ID_FRONT, mPromotion.getPassword_photo_file());
        contentValue.put(PASSPORT_ID_BACK, mPromotion.getPassword_photo1_file());
        contentValue.put(SIGNATURE, mPromotion.getSign__customer_file());
        contentValue.put(SALESMAN_SIGNATURE, mPromotion.getSalesmanSignature());
        contentValue.put(CHILLER_IMAGE, mPromotion.getChillerImage());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_CHILLER_ADD, null, contentValue);
    }

    //INSERT TRANSACTION FOR CUSTOMER TIMELINE
    public void insertTransaction1(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(TR_TYPE, transaction.tr_type);
        contentValue.put(TR_DATE, transaction.tr_date_time);
        contentValue.put(TR_CUSTOMER_NUM, transaction.tr_customer_num);
        contentValue.put(TR_CUSTOMER_NAME, transaction.tr_customer_name);
        contentValue.put(TR_SALESMAN_ID, transaction.tr_salesman_id);
        contentValue.put(TR_INVOICE_ID, transaction.tr_invoice_id);
        contentValue.put(TR_ORDER_ID, transaction.tr_order_id);
        contentValue.put(TR_COLLECTION_ID, transaction.tr_collection_id);
        contentValue.put(TR_PYAMENT_ID, transaction.tr_pyament_id);
        contentValue.put(TR_IS_POSTED, transaction.tr_is_posted);
        // contentValue.put(KEY_DATA, transaction.tr_printData);

        db.insert(TABLE_TRANSACTION, null, contentValue);
    }

    //Update Customer Transaction
    public void updateNatureOfTicket(String ticketNo) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_POSTED, "1");

        db.update(TABLE_NATURE_OF_CALL, contentValues,
                TICKET_NO + " =?", new String[]{ticketNo});
    }

    //Update Customer Transaction
    public void updateCustomerTransaction(String custId, String type) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        if (type.equals("sale")) {
            contentValues.put(CUST_SALE, "1");
        } else if (type.equals("order")) {
            contentValues.put(CUST_ORDER, "1");
        } else if (type.equals("collection")) {
            contentValues.put(CUST_COLL, "1");
        } else if (type.equals("return")) {
            contentValues.put(CUST_RETURN, "1");
        }

        db.update(TABLE_CUSTOMER, contentValues,
                CUSTOMER_ID + " =?", new String[]{custId});
    }

    //INSERT Payment FOR CUSTOMER TIMELINE
    public void insertPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(PAYMENT_INVOICE_ID, payment.getInvoice_id());
        contentValue.put(PAYMENT_COLLECTION_ID, payment.getCollection_id());
        contentValue.put(PAYMENT_TYPE, payment.getPayment_type());
        contentValue.put(PAYMENT_DATE, payment.getPayment_date());
        contentValue.put(PAYMENT_CHEQUE_NO, payment.getCheque_no());
        contentValue.put(PAYMENT_BANK_NAME, payment.getBank_name());
        contentValue.put(PAYMENT_AMOUNT, payment.getPayment_amount());
        contentValue.put(CUSTOMER_ID, payment.getCust_id());

        db.insert(TABLE_PAYMENT, null, contentValue);
    }

    //Update TRANSACTION
    public int updateLoadTransaction(String subloadId, String type, String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        if (type.equals("2")) {
            contentValue.put(TR_IS_POSTED, "Fail");
            contentValue.put(TR_MESSAGE, message);
        } else {
            contentValue.put(TR_IS_POSTED, "Yes");
        }

        int i = db.update(TABLE_TRANSACTION, contentValue,
                TR_INVOICE_ID + " = ?", new String[]{subloadId});

        return i;
    }

    //Update TRANSACTION Invoice
    public int updateInvoiceTransaction(String invoiceNo, String type, String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        if (type.equals("2")) {
            contentValue.put(TR_IS_POSTED, "Fail");
            contentValue.put(TR_MESSAGE, message);
        } else {
            contentValue.put(TR_IS_POSTED, "Yes");
        }

        int i = db.update(TABLE_TRANSACTION, contentValue,
                TR_INVOICE_ID + " = ?" + " AND "
                        + TR_TYPE + "=?", new String[]{invoiceNo, Constant.TRANSACTION_TYPES.TT_SALES_CREATED});

        return i;
    }

    //Update Unload Invoice
    public int updateUnloadData() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DATA_MARK_FOR_POST, "Y");
        contentValue.put(IS_POSTED, "1");

        int i = db.update(TABLE_UNLOAD_VARIANCE, contentValue,
                null, null);

        return i;
    }

    //Update Customer Visit Status
    public int updateVisitStatus(String custId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DATA_MARK_FOR_POST, "Y");
        contentValue.put(IS_POSTED, "1");

        int i = db.update(TABLE_CUSTOMER_VISIT, contentValue,
                CUSTOMER_ID + " = ?", new String[]{custId});

        return i;
    }

    public int updateInvoiceLocationStatus(String custId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DATA_MARK_FOR_POST, "Y");
        contentValue.put(IS_POSTED, "1");

        int i = db.update(TABLE_CUSTOMER_VISIT_SALES, contentValue,
                CUSTOMER_ID + " = ?", new String[]{custId});

        return i;
    }

    //Update Customer Add Status
    public int updateCustomerAddStatus(String custId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        if (status.equalsIgnoreCase("1")) {
            contentValue.put(DATA_MARK_FOR_POST, "Y");
            contentValue.put(IS_POSTED, "1");
        } else {
            contentValue.put(DATA_MARK_FOR_POST, "E");
            contentValue.put(IS_POSTED, "0");
        }

        int i = db.update(TABLE_CUSTOMER, contentValue,
                CUSTOMER_ID + " = ?", new String[]{custId});

        return i;
    }

    //Update Customer Add Status
    public int updateDeptCustomerAddStatus(String custId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        if (status.equalsIgnoreCase("1")) {
            contentValue.put(DATA_MARK_FOR_POST, "Y");
            contentValue.put(IS_POSTED, "1");
        } else {
            contentValue.put(DATA_MARK_FOR_POST, "E");
            contentValue.put(IS_POSTED, "0");
        }

        int i = db.update(TABLE_DEPOT_CUSTOMER, contentValue,
                CUSTOMER_ID + " = ?", new String[]{custId});

        return i;
    }

    //Update Customer Visit Status
    public int updateDeliveryDeleteStatus(String custId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DATA_MARK_FOR_POST, "Y");
        contentValue.put(IS_POSTED, "1");

        int i = db.update(TABLE_DELIVERY_HEADER, contentValue,
                DELIVERY_ID + " = ?", new String[]{custId});

        return i;
    }

    //Update Delivery Delete
    public int deliveryDeleteStatus(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_DELETE, "1");
        contentValue.put(IS_POSTED, "0");

        int i = db.update(TABLE_DELIVERY_HEADER, contentValue,
                DELIVERY_ID + " = ?", new String[]{orderId});

        return i;
    }

    //Update Delivery Delete
    public int deliveryStatus(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DATA_MARK_FOR_POST, "Y");
        contentValue.put(IS_DELETE, "2");
        contentValue.put(IS_POSTED, "1");

        int i = db.update(TABLE_DELIVERY_HEADER, contentValue,
                DELIVERY_ID + " = ?", new String[]{orderId});

        return i;
    }

    //Update Delivery Items Delete
    public int deliveryItemStatus(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(IS_DELETE, "2");

        int i = db.update(TABLE_DELIVERY_ITEMS, contentValue,
                DELIVERY_ID + " = ?", new String[]{orderId});

        return i;
    }

    //Update Unload Invoice
    public int updateUnloadTransaction(String invoiceNo, String type, String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        if (type.equals("2")) {
            contentValue.put(TR_IS_POSTED, "Fail");
            contentValue.put(TR_MESSAGE, message);
        } else {
            contentValue.put(TR_IS_POSTED, "Yes");
        }

        int i = db.update(TABLE_TRANSACTION, contentValue,
                TR_ORDER_ID + " = ?", new String[]{invoiceNo});

        return i;
    }

    //Update Customer Invoice
    public int updateCustomerTransaction(String invoiceNo, String type, String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        if (type.equals("2")) {
            contentValue.put(TR_IS_POSTED, "Fail");
            contentValue.put(TR_MESSAGE, message);
        } else {
            contentValue.put(TR_IS_POSTED, "Yes");
        }

        int i = db.update(TABLE_TRANSACTION, contentValue,
                TR_ORDER_ID + " = ?", new String[]{invoiceNo});

        return i;
    }


    //Update TRANSACTION Order
    public int updateOrderTransaction(String orderNo, String type, String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        if (type.equals("2")) {
            contentValue.put(TR_IS_POSTED, "Fail");
            contentValue.put(TR_MESSAGE, message);
        } else {
            contentValue.put(TR_IS_POSTED, "Yes");
        }

        int i = db.update(TABLE_TRANSACTION, contentValue,
                TR_ORDER_ID + " = ?", new String[]{orderNo});

        return i;
    }

    //Update COLLECTION Order
    public int updateCollectionTransaction(String collectionNo, String type, String message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        if (type.equalsIgnoreCase("2")) {
            contentValue.put(TR_IS_POSTED, "Fail");
            contentValue.put(TR_MESSAGE, message);
        } else {
            contentValue.put(TR_IS_POSTED, "Yes");
        }

        int i = db.update(TABLE_TRANSACTION, contentValue,
                TR_COLLECTION_ID + " = ?", new String[]{collectionNo});

        return i;
    }


    public int updateVanStockItemQty(String item_id, String item_qty, String item_altQty, String actualBsQty, String actualAlQty) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_BASE_UOM_QTY, item_qty);
        contentValues.put(ITEM_ALTER_UOM_QTY, item_altQty);
        contentValues.put(ACTUAL_BASE_QTY, actualBsQty);
        contentValues.put(ACTUAL_ALTER_QTY, actualAlQty);

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_VAN_STOCK_ITEMS, contentValues,
                ITEM_ID + " = ?", new String[]{item_id});

        return i;
    }

    public int updateVanStockQty(String item_id, String item_qty, String item_altQty, String type) {
        ContentValues contentValues = new ContentValues();

        if (type.equalsIgnoreCase("Base")) {
            contentValues.put(ITEM_BASE_UOM_QTY, item_qty);
        } else if (type.equalsIgnoreCase("Alter")) {
            contentValues.put(ITEM_ALTER_UOM_QTY, item_altQty);
        } else if (type.equalsIgnoreCase("Both")) {
            contentValues.put(ITEM_BASE_UOM_QTY, item_qty);
            contentValues.put(ITEM_ALTER_UOM_QTY, item_altQty);
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_VAN_STOCK_ITEMS, contentValues,
                ITEM_ID + " = ?", new String[]{item_id});

        return i;
    }

    public int updateVanStockDeleteQty(String item_id, String item_qty, String item_altQty, String type) {
        ContentValues contentValues = new ContentValues();

        if (type.equalsIgnoreCase("Base")) {
            contentValues.put(ITEM_BASE_UOM_QTY, item_qty);
        } else if (type.equalsIgnoreCase("Alter")) {
            contentValues.put(ITEM_ALTER_UOM_QTY, item_altQty);
        } else if (type.equalsIgnoreCase("Both")) {
            contentValues.put(ITEM_BASE_UOM_QTY, item_qty);
            contentValues.put(ITEM_ALTER_UOM_QTY, item_altQty);
        }
        contentValues.put(IS_DELETE, "1");

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_VAN_STOCK_ITEMS, contentValues,
                ITEM_ID + " = ?", new String[]{item_id});

        return i;
    }

    //========================================== GET PART START ========================================================
    public boolean checkSalesmanExits(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean userExist = false;

        Cursor c = db.rawQuery("SELECT * FROM " +
                TABLE_SALES_MAN + " WHERE " +
                SALESMAN_USERNAME + "=?" + " AND "
                + SALESMAN_PASSWORD + "=?", new String[]{username, password});
        if (c == null) return userExist;

        while (c.moveToNext()) {
            userExist = true;
        }
        c.close();

        return userExist;
    }

    public boolean checkItemExitsInVanStock(String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean itemExist = false;

        Cursor c = db.rawQuery("SELECT * FROM " +
                TABLE_VAN_STOCK_ITEMS + " WHERE " +
                ITEM_ID + "=?", new String[]{itemId});
        if (c == null) return itemExist;

        while (c.moveToNext()) {
            itemExist = true;
        }
        c.close();

        return itemExist;
    }

    public boolean checkExchangeExist(String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean itemExist = false;

        Cursor c = db.rawQuery("SELECT * FROM " +
                TABLE_INVOICE_HEADER + " WHERE " +
                SVH_EXCHANGE_NO + "=?", new String[]{itemId});
        if (c == null) return itemExist;

        while (c.moveToNext()) {
            itemExist = true;
        }
        c.close();

        return itemExist;
    }

    public boolean checkIfLoadExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean itemExist = false;

        Cursor c = db.rawQuery("SELECT * FROM " +
                TABLE_LOAD_HEADER + " WHERE " +
                LOAD_IS_VERIFIED + "=?", new String[]{"0"});
        if (c == null) return itemExist;

        while (c.moveToNext()) {
            itemExist = true;
        }
        c.close();

        return itemExist;
    }

    public boolean checkIfAgentPriceExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean itemExist = false;

        Cursor c = db.rawQuery("SELECT * FROM " +
                TABLE_AGENT_PRICING, null);
        if (c == null) return itemExist;

        while (c.moveToNext()) {
            itemExist = true;
        }
        c.close();

        return itemExist;
    }

    public boolean checkIfCustomerExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean itemExist = false;

        Cursor c = db.rawQuery("SELECT * FROM " +
                TABLE_CUSTOMER, null);
        if (c == null) return itemExist;

        while (c.moveToNext()) {
            itemExist = true;
        }
        c.close();

        return itemExist;
    }

    public boolean checkIfDepotCustomerExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean itemExist = false;

        Cursor c = db.rawQuery("SELECT * FROM " +
                TABLE_DEPOT_CUSTOMER, null);
        if (c == null) return itemExist;

        while (c.moveToNext()) {
            itemExist = true;
        }
        c.close();

        return itemExist;
    }

    public double getCusAvailableBal(String custId) {
        double inAMt = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        //String selectQuery = "SELECT * FROM " + TABLE_COLLECTION + " WHERE u_id IN (SELECT MAX(u_id) FROM " + TABLE_COLLECTION + " WHERE " + CUSTOMER_ID + " = '" + custId + "'" + " GROUP BY sv_code)";

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{custId});
            try {
                if (cursor.moveToFirst()) {
                    do {

//                        double due_amt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(INVOICE_AMT))) -
//                                Double.parseDouble(cursor.getString(cursor.getColumnIndex(AMT_CLEARED)));
                        inAMt += Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_INVOICE_AMOUNT)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inAMt;
    }

    //GET Post Invoice Count
    public double getTotalAmtSale() {

        double invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        invCount += Double.parseDouble(cursor.getString(cursor.getColumnIndex(SVH_TOT_AMT_SALES)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public ArrayList<Item> getSaleItem() {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            try {
                Cursor cursor = db.rawQuery("SELECT " + ITEM_ID + ", SUM(" + ITEM_BASE_UOM_QTY + ") as Total, " + " SUM(" + ITEM_ALTER_UOM_QTY + ") as CaseTotal FROM " +
                        TABLE_INVOICE_ITEMS + " WHERE " +
                        IS_EXCHANGE + "=?" + " GROUP BY " + ITEM_ID, new String[]{"0"});
                if (cursor.moveToFirst()) {
                    do {
                        int PCQty = 0;
                        int caseQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CaseTotal")));
                        int PCICount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Total")));
                        int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));

                        if (PCICount > 0) {
                            int ALCase = (int) (PCICount / UPC);
                            caseQty += ALCase;

                            int toalPC = ALCase * UPC;
                            int remain = PCICount - toalPC;
                            PCQty = remain;
                        }

                        Item mItem = new Item();
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.setAlterUOMQty("" + caseQty);
                        mItem.setBaseUOMQty("" + PCQty);
                        arrData.add(mItem);
                    } while (cursor.moveToNext());

                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrData;
    }

    //GET Total Category Sale
    public int getTotalCategorySale(String catId) {

        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT " + ITEM_ID + ", SUM(" + ITEM_BASE_UOM_QTY + ") as Total, " + " SUM(" + ITEM_ALTER_UOM_QTY + ") as CaseTotal FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    ITEM_CATEGORY + "=?" + " AND "
                    + IS_EXCHANGE + "=?" + " GROUP BY " + ITEM_ID, new String[]{catId, "0"});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex("CaseTotal")));
                        int PCICount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Total")));
                        int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));

                        if (PCICount > 0) {
                            int ALCase = (int) (PCICount / UPC);
                            qty += ALCase;

//                            int toalPC = ALCase * UPC;
//                            int remain = PCICount - toalPC;
//                            PCQty += remain;
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;
    }

    //GET Invoice Count
    public int getTotalInvoice() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER, null);
            try {
                invCount = cursor.getCount();
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Invoice Count
    public int getTotalQtySale() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        invCount += Integer.parseInt(cursor.getString(cursor.getColumnIndex(SVH_ALT_QTY)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Category Qty Count
    public int getTotalCategoryQtySale(String catId) {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    ITEM_CATEGORY + "=?", new String[]{catId});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        invCount += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Category Qty Count
    public HashMap<String, String> getTotalCategoryPCSQtySale(String catId) {

        HashMap<String, String> mItem = new HashMap<>();
        int caseQty = 0, PCQty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT " + ITEM_ID + ", SUM(" + ITEM_BASE_UOM_QTY + ") as Total, " + " SUM(" + ITEM_ALTER_UOM_QTY + ") as CaseTotal FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    ITEM_CATEGORY + "=?" + " GROUP BY " + ITEM_ID, new String[]{catId});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        caseQty += Integer.parseInt(cursor.getString(cursor.getColumnIndex("CaseTotal")));
                        int PCICount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Total")));
                        int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));

                        if (PCICount > 0) {
                            int ALCase = (int) (PCICount / UPC);
                            caseQty += ALCase;

                            int toalPC = ALCase * UPC;
                            int remain = PCICount - toalPC;
                            PCQty += remain;
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        mItem.put("Case", "" + caseQty);
        mItem.put("PCs", "" + PCQty);

        return mItem;
    }

    //GET Category Qty Count
    public HashMap<String, String> getTotalCategoryQtySale() {

        HashMap<String, String> mItem = new HashMap<>();
        int caseQty = 0, PCQty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT " + ITEM_ID + ", SUM(" + ITEM_BASE_UOM_QTY + ") as Total, " + " SUM(" + ITEM_ALTER_UOM_QTY + ") as CaseTotal FROM " +
                    TABLE_INVOICE_ITEMS + " GROUP BY " + ITEM_ID, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        caseQty += Integer.parseInt(cursor.getString(cursor.getColumnIndex("CaseTotal")));
                        int PCICount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Total")));
                        int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));

                        if (PCICount > 0) {
                            int ALCase = (int) (PCICount / UPC);
                            caseQty += ALCase;

                            int toalPC = ALCase * UPC;
                            int remain = PCICount - toalPC;
                            PCQty += remain;
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        mItem.put("Case", "" + caseQty);
        mItem.put("PCs", "" + PCQty);

        return mItem;
    }

    //GET Category Qty Count
    public int getTotalCategoryCaseSale(String catId) {

        int caseQty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT " + ITEM_ID + ", SUM(" + ITEM_BASE_UOM_QTY + ") as Total, " + " SUM(" + ITEM_ALTER_UOM_QTY + ") as CaseTotal FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    ITEM_CATEGORY + "=?" + " GROUP BY " + ITEM_ID, new String[]{catId});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        caseQty += Integer.parseInt(cursor.getString(cursor.getColumnIndex("CaseTotal")));
                        int PCICount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Total")));
                        int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));

                        if (PCICount > 0) {
                            int ALCase = (int) (PCICount / UPC);
                            caseQty += ALCase;
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return caseQty;
    }


    //GET Post Invoice Count
    public int getPostInvoiceCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Load Count
    public int getLoadCount() {

        int loadCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            loadCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadCount;
    }

    //GET Load Count
    public int getSalesmanLoadCount() {

        int loadCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SALESMAN_LOAD_REQUEST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            loadCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadCount;
    }

    //GET Post Order Count
    public int getPostOrderCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ORDER_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Order Count
    public boolean isSyncCustomer(String custId, String seq) {

        boolean isAvailable = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + seq + "=?", new String[]{custId, "1"});
            if (cursor.getCount() > 0) {
                isAvailable = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAvailable;
    }

    //GET Post isSyncTransaction
    public boolean isSyncCompleteTransaction() {

        boolean isAvailable = true;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_TRANSACTION + " WHERE " +
                    TR_IS_POSTED + "!=?", new String[]{"Yes"});
            if (cursor.getCount() > 0) {
                isAvailable = false;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAvailable;
    }

    //GET Post Collection Count
    public int getPostCollectionCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Invoice Posted
    //riddhi
    public boolean getInvoicePosted(String invoiceNo) {

        boolean isPosted = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_TRANSACTION + " WHERE " +
                    TR_INVOICE_ID + "=?" + " AND "
                    + TR_IS_POSTED + "=?", new String[]{invoiceNo, "Yes"});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                isPosted = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPosted;
    }

    public boolean clearInvoicePosted(String invoiceNo) {

        boolean isPosted = false;

        SQLiteDatabase db = this.getReadableDatabase();


        try {
            // db.execSQL("delete from " + TABLE_TRANSACTION);
            Cursor cursor = db.rawQuery("delete from " +
                    TABLE_TRANSACTION + " WHERE " +
                    TR_INVOICE_ID + "=?" + " AND "
                    + TR_IS_POSTED + "=?", new String[]{invoiceNo, "Yes"});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                isPosted = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPosted;
    }

    public boolean getInvoiceExchandePosted(String invoiceNo) {

        boolean isPosted = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER + " WHERE " +
                    SVH_EXCHANGE_NO + "=?" + " AND "
                    + DATA_MARK_FOR_POST + "=?", new String[]{invoiceNo, "Y"});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                isPosted = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPosted;
    }

    public boolean getReturnExchandePosted(String invoiceNo) {

        boolean isPosted = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_HEADER + " WHERE " +
                    SVH_EXCHANGE_NO + "=?" + " AND "
                    + DATA_MARK_FOR_POST + "=?", new String[]{invoiceNo, "Y"});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                isPosted = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPosted;
    }

    //GET Customer Posted
    public boolean getCustomerPosted(String customerId) {

        boolean isPosted = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + DATA_MARK_FOR_POST + "=?", new String[]{customerId, "Y"});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                isPosted = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPosted;
    }

    //GET Customer Posted
    public boolean getDepotCustomerPosted(String customerId) {

        boolean isPosted = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + DATA_MARK_FOR_POST + "=?", new String[]{customerId, "Y"});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                isPosted = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isPosted;
    }

    //GET Post Return Count
    public int getPostReturnCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Return Count
    public int getPostReturnRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_REQUEST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Load Request Count
    public int getPostLoadRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_REQUEST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Customer Visit Request Count
    public int getPostVisitRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_VISIT + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CUSTOMER_VISIT_SALES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //GET Post Customer Visit Request Count
    public int getPostVisitRequestsalesCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_VISIT_SALES + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Delete Delivery
    public int getPostDeleteRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + IS_DELETE + "=?", new String[]{"M", "1"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Customer request
    public int getPostCustomerRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Customer request
    public int getPostCustomerUpdateRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"U"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post Customer request
    public int getPostDeptCustomerUpdateRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"U"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Post UnLoad Request Count
    public int getPostUnLoadRequestCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UNLOAD_VARIANCE + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }


    //GET Post Invoice Items
    public JsonArray getPostInvoiceItem(String invNo) {

        JsonArray arrItem = new JsonArray();
        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    SVH_CODE + "=?", new String[]{invNo});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();

                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) > 0) {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(BASE_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                            if (Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT))) > 0) {
                                double discountAmt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                                mItem.addProperty("discount_amount", "" + discountAmt);
                            } else {
                                mItem.addProperty("discount_amount", "0");
                            }

                        } else {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(ALRT_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                            if (Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT))) > 0) {
                                double discountAmt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                                mItem.addProperty("discount_amount", "" + discountAmt);
                            } else {
                                mItem.addProperty("discount_amount", "0");
                            }
                        }
                        mItem.addProperty("vat", cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL)));
                        mItem.addProperty("pre_vat", cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT)));
                        mItem.addProperty("net_total", cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL)));
                        mItem.addProperty("excies", cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL)));
                        mItem.addProperty("discount", cursor.getString(cursor.getColumnIndex(DISCOUNT)));

                        if (cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)) != null) {
                            mItem.addProperty("promotion_id", cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        } else {
                            mItem.addProperty("promotion_id", "");
                        }
                        arrItem.add(mItem);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;

    }

    //GET Post Order Items
    public JsonArray getPostOrderItems(String orderNo) {

        JsonArray arrItem = new JsonArray();
        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ORDER_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();

                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) > 0) {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(BASE_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                            if (Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT))) > 0) {
                                double discountAmt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(BASE_PRCE))) -
                                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                                mItem.addProperty("discount_amount", "" + discountAmt);
                            } else {
                                mItem.addProperty("discount_amount", "0");
                            }

                        } else {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(ALRT_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                            if (Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT))) > 0) {
                                double discountAmt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ALRT_PRCE))) -
                                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                                mItem.addProperty("discount_amount", "" + discountAmt);
                            } else {
                                mItem.addProperty("discount_amount", "0");
                            }
                        }


                        mItem.addProperty("is_free", cursor.getString(cursor.getColumnIndex(IS_FREE_ITEM)));
                        mItem.addProperty("vat", cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL)));
                        mItem.addProperty("pre_vat", cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT)));
                        mItem.addProperty("net_total", cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL)));
                        mItem.addProperty("excies", cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL)));
                        mItem.addProperty("discount", cursor.getString(cursor.getColumnIndex(DISCOUNT)));
                        //mItem.addProperty("discount_amount", cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                        mItem.addProperty("parent_item", cursor.getString(cursor.getColumnIndex(PARENT_ITEM_ID)));
                        mItem.addProperty("is_free", cursor.getString(cursor.getColumnIndex(IS_FREE_ITEM)));


                        arrItem.add(mItem);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;

    }

    //GET Post Return Items
    public JsonArray getPostReturnItems(String orderNo) {

        JsonArray arrItem = new JsonArray();
        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();

                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) > 0) {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(BASE_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        } else {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(ALRT_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        }

                        if (cursor.getString(cursor.getColumnIndex(RETURN_TYPE)).equalsIgnoreCase("Bad")) {
                            mItem.addProperty("product_type", "2");
                        } else {
                            mItem.addProperty("product_type", "1");
                        }
                        mItem.addProperty("reason", getReasonCode(cursor.getString(cursor.getColumnIndex(REASON_TYPE))));
                        mItem.addProperty("vat", cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL)));
                        mItem.addProperty("pre_vat", cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT)));
                        mItem.addProperty("net_total", cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL)));
                        mItem.addProperty("excies", cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL)));
                        mItem.addProperty("discount", "0");
                        mItem.addProperty("discount_amount", "0");
                        if (cursor.getString(cursor.getColumnIndex(EXPIRY_DATE)) != null) {
                            mItem.addProperty("expiry_date", cursor.getString(cursor.getColumnIndex(EXPIRY_DATE)));
                        } else {
                            mItem.addProperty("expiry_date", "");
                        }

                        arrItem.add(mItem);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;

    }

    //GET Post Return Items
    public JsonArray getPostReturnRequestItems(String orderNo) {

        JsonArray arrItem = new JsonArray();
        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_REQUEST_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();

                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) > 0) {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(BASE_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        } else {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(ALRT_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        }

                        if (cursor.getString(cursor.getColumnIndex(RETURN_TYPE)).equalsIgnoreCase("Bad")) {
                            mItem.addProperty("product_type", "2");
                        } else {
                            mItem.addProperty("product_type", "1");
                        }
                        mItem.addProperty("reason", getReasonCode(cursor.getString(cursor.getColumnIndex(REASON_TYPE))));
                        mItem.addProperty("vat", cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL)));
                        mItem.addProperty("pre_vat", cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT)));
                        mItem.addProperty("net_total", cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL)));
                        mItem.addProperty("excies", cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL)));
                        mItem.addProperty("discount", "0");
                        mItem.addProperty("discount_amount", "0");


                        arrItem.add(mItem);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;

    }

    private String getReasonCode(String name) {
        String code = "0";

        switch (name) {
            case "2":
                code = "Damage";
                break;
            case "3":
                code = "Expired";
                break;
            case "6":
                code = "Short Expiry";
                break;
            case "7":
                code = "Packing Issue";
                break;
            case "8":
                code = "Product Replacement";
                break;
            case "9":
                code = "Non moving product";
                break;
        }
        return code;

    }

    public JsonArray getPostSalesmanLoadRequestItems(String orderNo) {

        JsonArray arrItem = new JsonArray();
        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SALESMAN_LOAD_REQUEST_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();

                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) > 0) {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            mItem.addProperty("qty", cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));

                        } else {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            mItem.addProperty("qty", cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }

                        arrItem.add(mItem);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;

    }

    //GET Post Load Request Items
    public JsonArray getPostLoadRequestItems(String orderNo) {

        JsonArray arrItem = new JsonArray();
        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_REQUEST_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();

                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) > 0) {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(BASE_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                            if (Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT))) > 0) {
                                double discountAmt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(BASE_PRCE))) -
                                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                                mItem.addProperty("discount_amount", "" + discountAmt);
                            } else {
                                mItem.addProperty("discount_amount", "0");
                            }

                        } else {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            mItem.addProperty("itemvalue", cursor.getString(cursor.getColumnIndex(ALRT_PRCE)));
                            mItem.addProperty("item_total", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                            if (Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT))) > 0) {
                                double discountAmt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ALRT_PRCE))) -
                                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                                mItem.addProperty("discount_amount", "" + discountAmt);
                            } else {
                                mItem.addProperty("discount_amount", "0");
                            }
                        }


                        mItem.addProperty("vat", cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL)));
                        mItem.addProperty("pre_vat", cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT)));
                        mItem.addProperty("net_total", cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL)));
                        mItem.addProperty("excies", cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL)));
                        mItem.addProperty("discount", cursor.getString(cursor.getColumnIndex(DISCOUNT)));
                        mItem.addProperty("parent_item", cursor.getString(cursor.getColumnIndex(PARENT_ITEM_ID)));
                        mItem.addProperty("is_free", cursor.getString(cursor.getColumnIndex(IS_FREE_ITEM)));

                        arrItem.add(mItem);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;

    }

    //GET Post UnLoad Request Items
    public JsonArray getPostUnLoadItems() {

        JsonArray arrItem = new JsonArray();
        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UNLOAD_VARIANCE, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();

                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));

                        if (cursor.getString(cursor.getColumnIndex(REASON_TYPE)).equalsIgnoreCase("5")) {
                            mItem.addProperty("unloadtype", "return");
                        } else {
                            mItem.addProperty("unloadtype", "goods");
                        }
                        mItem.addProperty("itemvalue", "");

                        int qty = 0;
                        if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            qty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        } else {
                            mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            mItem.addProperty("quantity", cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            qty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }

                        if (cursor.getString(cursor.getColumnIndex(REASON_TYPE)).equalsIgnoreCase("5")
                                || cursor.getString(cursor.getColumnIndex(REASON_TYPE)).equalsIgnoreCase("0")) {
                            if (qty > 0)
                                arrItem.add(mItem);
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItem;

    }

    //GET Post Visit Request Items
    public HashMap<String, String> getPostVisitItems() {

        HashMap<String, String> mData = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_VISIT + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {

                    mData.put("customerid", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    mData.put("start_time", cursor.getString(cursor.getColumnIndex(START_TIME)));
                    mData.put("end_time", cursor.getString(cursor.getColumnIndex(END_TIME)));
                    mData.put("latitude", cursor.getString(cursor.getColumnIndex(LATITUDE)));
                    mData.put("longitude", cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                    mData.put("captured_latitude", cursor.getString(cursor.getColumnIndex(CAPTURE_LATITUDE)));
                    mData.put("captured_longitude", cursor.getString(cursor.getColumnIndex(CAPTURE_LONGITUDE)));
                    mData.put("status", cursor.getString(cursor.getColumnIndex(VISIT_STATUS)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mData;
    }

    //GET Post Add Customer Request
    public JsonObject getPostAddCustomer() {

        JsonObject jObj = new JsonObject();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {

                    if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
                        jObj.addProperty("method", App.ADD_TEMPCUSTOMER);
                    } else {
                        jObj.addProperty("method", App.ADD_CUSTOMER);
                    }
                    jObj.addProperty("cust_code", cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                    jObj.addProperty("cctype", cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                    jObj.addProperty("route_id", Settings.getString(App.ROUTEID));
                    jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                    jObj.addProperty("customername", cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                    jObj.addProperty("customeraddress1", cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                    jObj.addProperty("customerphone", cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                    jObj.addProperty("customerphone1", cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                    jObj.addProperty("cust_name2", cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME2)));
                    jObj.addProperty("customeraddress2", cursor.getString(cursor.getColumnIndex(ADDRESS_TWO)));
                    jObj.addProperty("customerstate", cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                    jObj.addProperty("street", cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                    jObj.addProperty("customercity", cursor.getString(cursor.getColumnIndex(CUST_CITY)));
                    jObj.addProperty("customerzip", cursor.getString(cursor.getColumnIndex(CUST_ZIP)));
                    jObj.addProperty("longitude", cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                    jObj.addProperty("latitude", cursor.getString(cursor.getColumnIndex(LATITUDE)));
                    jObj.addProperty("threshold_radius", cursor.getString(cursor.getColumnIndex(CUSTOMER_RADIUS)));
                    jObj.addProperty("region_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_REGION)));
                    jObj.addProperty("sub_region_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_SUBREGION)));
                    jObj.addProperty("vat_no", "");
                    jObj.addProperty("trn_no", cursor.getString(cursor.getColumnIndex(TIN_NO)));
                    jObj.addProperty("payment_type", cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE)));
                    jObj.addProperty("category", cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                    if (cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)) != null) {
                        jObj.addProperty("outlet_channel", cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)));
                    } else {
                        jObj.addProperty("outlet_channel", "");
                    }
                    if (cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)) != null) {
                        jObj.addProperty("outlet_sub_category", cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)));
                    } else {
                        jObj.addProperty("outlet_sub_category", "");
                    }
                    jObj.addProperty("language", cursor.getString(cursor.getColumnIndex(LANGUAGE)));
                    jObj.addProperty("fridge", cursor.getString(cursor.getColumnIndex(FRIDGE)));
                    jObj.addProperty("invoice_code", Settings.getString(App.INVOICE_CODE));
                    jObj.addProperty("sunday", cursor.getString(cursor.getColumnIndex(SUN_SEQ)));
                    jObj.addProperty("monday", cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                    jObj.addProperty("tuesday", cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                    jObj.addProperty("wednesday", cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                    jObj.addProperty("thursday", cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                    jObj.addProperty("friday", cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                    jObj.addProperty("saturday", cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //GET Post Update Customer Request
    public JsonObject getPostUpdateCustomer() {

        JsonObject jObj = new JsonObject();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"U"});
            try {
                if (cursor.moveToFirst()) {

                    jObj.addProperty("method", App.UPDATE_CUST_POST);
                    jObj.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    jObj.addProperty("cust_code", cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                    jObj.addProperty("cctype", cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                    jObj.addProperty("route_id", cursor.getString(cursor.getColumnIndex(ROUTE_ID)));
                    jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                    jObj.addProperty("customername", cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                    jObj.addProperty("customeraddress1", cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                    jObj.addProperty("customerphone", cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                    jObj.addProperty("customerphone1", cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                    jObj.addProperty("cust_name2", cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME2)));
                    jObj.addProperty("customeraddress2", cursor.getString(cursor.getColumnIndex(ADDRESS_TWO)));
                    jObj.addProperty("street", cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                    jObj.addProperty("customerstate", cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                    jObj.addProperty("customercity", cursor.getString(cursor.getColumnIndex(CUST_CITY)));
                    jObj.addProperty("customerzip", cursor.getString(cursor.getColumnIndex(CUST_ZIP)));
                    jObj.addProperty("longitude", cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                    jObj.addProperty("latitude", cursor.getString(cursor.getColumnIndex(LATITUDE)));
                    jObj.addProperty("region_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_REGION)));
                    jObj.addProperty("sub_region_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_SUBREGION)));
                    jObj.addProperty("trn_no", cursor.getString(cursor.getColumnIndex(TIN_NO)));
                    jObj.addProperty("category", cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                    if (cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)) != null) {
                        jObj.addProperty("outlet_channel", cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)));
                    } else {
                        jObj.addProperty("outlet_channel", "");
                    }
                    if (cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)) != null) {
                        jObj.addProperty("outlet_sub_category", cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)));
                    } else {
                        jObj.addProperty("outlet_sub_category", "");
                    }
                    jObj.addProperty("language", cursor.getString(cursor.getColumnIndex(LANGUAGE)));
                    jObj.addProperty("fridge", cursor.getString(cursor.getColumnIndex(FRIDGE)));
                    jObj.addProperty("invoice_code", Settings.getString(App.INVOICE_CODE));
                    jObj.addProperty("sunday", cursor.getString(cursor.getColumnIndex(SUN_SEQ)));
                    jObj.addProperty("monday", cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                    jObj.addProperty("tuesday", cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                    jObj.addProperty("wednesday", cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                    jObj.addProperty("thursday", cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                    jObj.addProperty("friday", cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                    jObj.addProperty("saturday", cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //GET Post Update Customer Request
    public JsonObject getPostDeptUpdateCustomer() {

        JsonObject jObj = new JsonObject();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"U"});
            try {
                if (cursor.moveToFirst()) {

                    jObj.addProperty("method", App.UPDATE_CUST_POST);
                    jObj.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    jObj.addProperty("cust_code", cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                    jObj.addProperty("cctype", cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                    jObj.addProperty("route_id", cursor.getString(cursor.getColumnIndex(ROUTE_ID)));
                    jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                    jObj.addProperty("customername", cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                    jObj.addProperty("customeraddress1", cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                    jObj.addProperty("customerphone", cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                    jObj.addProperty("customerphone1", cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                    jObj.addProperty("cust_name2", cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME2)));
                    jObj.addProperty("customeraddress2", cursor.getString(cursor.getColumnIndex(ADDRESS_TWO)));
                    jObj.addProperty("street", cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                    jObj.addProperty("customerstate", cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                    jObj.addProperty("customercity", cursor.getString(cursor.getColumnIndex(CUST_CITY)));
                    jObj.addProperty("customerzip", cursor.getString(cursor.getColumnIndex(CUST_ZIP)));
                    jObj.addProperty("longitude", cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                    jObj.addProperty("latitude", cursor.getString(cursor.getColumnIndex(LATITUDE)));
                    jObj.addProperty("region_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_REGION)));
                    jObj.addProperty("sub_region_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_SUBREGION)));
                    jObj.addProperty("trn_no", cursor.getString(cursor.getColumnIndex(TIN_NO)));
                    jObj.addProperty("category", cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                    if (cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)) != null) {
                        jObj.addProperty("outlet_channel", cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)));
                    } else {
                        jObj.addProperty("outlet_channel", "");
                    }
                    if (cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)) != null) {
                        jObj.addProperty("outlet_sub_category", cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)));
                    } else {
                        jObj.addProperty("outlet_sub_category", "");
                    }
                    jObj.addProperty("language", cursor.getString(cursor.getColumnIndex(LANGUAGE)));
                    jObj.addProperty("fridge", cursor.getString(cursor.getColumnIndex(FRIDGE)));
                    jObj.addProperty("invoice_code", Settings.getString(App.INVOICE_CODE));
                    jObj.addProperty("sunday", cursor.getString(cursor.getColumnIndex(SUN_SEQ)));
                    jObj.addProperty("monday", cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                    jObj.addProperty("tuesday", cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                    jObj.addProperty("wednesday", cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                    jObj.addProperty("thursday", cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                    jObj.addProperty("friday", cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                    jObj.addProperty("saturday", cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //GET Delete Request
    public String getPostDeleteItems() {

        String deliveryId = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    deliveryId = cursor.getString(cursor.getColumnIndex(DELIVERY_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deliveryId;
    }


    //GET Invoice
    public ArrayList<HashMap<String, String>> getPostInvoiceNo() {

        ArrayList<HashMap<String, String>> arrInvoice = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> mData = new HashMap<>();
                        mData.put("invoiceNo", cursor.getString(cursor.getColumnIndex(SVH_CODE)));
                        mData.put("customerid", cursor.getString(cursor.getColumnIndex(SVH_CUST_CODE)));
                        arrInvoice.add(mData);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrInvoice;
    }

    //GET Order Detail
    public Order getPostOrderDetail() {

        Order mOrder = new Order();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ORDER_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    mOrder.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                    mOrder.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                    mOrder.deliveyDate = cursor.getString(cursor.getColumnIndex(DELIVERY_DATE));
                    mOrder.cust_id = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
                    mOrder.orderAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                    mOrder.vatAmt = cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL));
                    mOrder.preVatAmt = cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT));
                    mOrder.exciseAmt = cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL));
                    mOrder.netAmt = cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL));
                    mOrder.salesmanId = cursor.getString(cursor.getColumnIndex(SALESMAN_ID));
                    mOrder.orderComment = cursor.getString(cursor.getColumnIndex(ORDER_COMMENT));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mOrder;
    }

    //GET Collection Detail
    public ArrayList<CollectionData> getCollectionPostDetail() {

        ArrayList<CollectionData> mCollData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    do {
                        CollectionData mColl = new CollectionData();
                        mColl.setOrderId(cursor.getString(cursor.getColumnIndex(KEY_ORDER_ID)));
                        mColl.setCustomerNo(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        mColl.setInvoiceNo(cursor.getString(cursor.getColumnIndex(KEY_INVOICE_NO)));
                        mColl.setIsOutStand(cursor.getString(cursor.getColumnIndex(KEY_IS_OUTSTANDING)));

                        mCollData.add(mColl);
                    } while (cursor.moveToNext());

                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mCollData;
    }

    //GET Collection Detail
    public ArrayList<CollectionData> getCollectionDetail(String collNo) {

        ArrayList<CollectionData> arrCollection = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    KEY_ORDER_ID + "=?", new String[]{collNo});

            try {
                if (cursor.moveToFirst()) {
                    do {

                        CollectionData mCollData = new CollectionData();

                        mCollData.setOrderId(cursor.getString(cursor.getColumnIndex(KEY_ORDER_ID)));
                        mCollData.setInvoiceNo(cursor.getString(cursor.getColumnIndex(KEY_INVOICE_NO)));
                        mCollData.setInvoiceAmount(cursor.getString(cursor.getColumnIndex(KEY_INVOICE_AMOUNT)));
                        mCollData.setCustomerNo(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        mCollData.setAmountPay(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT_PAY)));
                        mCollData.setCashAmt(cursor.getString(cursor.getColumnIndex(KEY_CASH_AMOUNT)));
                        mCollData.setChequeAmt(cursor.getString(cursor.getColumnIndex(KEY_CHEQUE_AMOUNT)));
                        mCollData.setChequeNo(cursor.getString(cursor.getColumnIndex(KEY_CHEQUE_NUMBER)));
                        mCollData.setChequeDat(cursor.getString(cursor.getColumnIndex(KEY_CHEQUE_DATE)));
                        mCollData.setBankCode(cursor.getString(cursor.getColumnIndex(KEY_CHEQUE_BANK_CODE)));
                        mCollData.setBankName(cursor.getString(cursor.getColumnIndex(KEY_CHEQUE_BANK_NAME)));
                        mCollData.setCollType(cursor.getString(cursor.getColumnIndex(KEY_COLLECTION_TYPE)));

                        arrCollection.add(mCollData);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrCollection;
    }


    //GET Collection Detail
    public Collection getPostCollectionDetail() {

        Collection mOrder = new Collection();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    mOrder.coll_Id = cursor.getString(cursor.getColumnIndex(COLL_NO));
                    mOrder.coll_invoiceNo = cursor.getString(cursor.getColumnIndex(SVH_CODE));
                    mOrder.coll_invoiceDate = cursor.getString(cursor.getColumnIndex(INVOICE_DATE));
                    mOrder.coll_customerId = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
                    mOrder.coll_type = cursor.getString(cursor.getColumnIndex(COLL_TYPE));
                    if (cursor.getString(cursor.getColumnIndex(COLL_TYPE)).equalsIgnoreCase("Cash")) {
                        mOrder.cash_amt = cursor.getString(cursor.getColumnIndex(CASH_AMT));
                    } else {
                        mOrder.cheque_amt = cursor.getString(cursor.getColumnIndex(CHEQUE_AMT));
                        mOrder.cheque_no = cursor.getString(cursor.getColumnIndex(CHEQUE_NO));
                        mOrder.cheque_date = cursor.getString(cursor.getColumnIndex(CHEQUE_DATE));
                        mOrder.coll_bank = cursor.getString(cursor.getColumnIndex(BANK_NAME));
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mOrder;
    }

    //GET Return Detail
    public ReturnOrder getPostReturnDetail() {

        ReturnOrder mReturn = new ReturnOrder();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    mReturn.orderId = cursor.getString(cursor.getColumnIndex(KEY_ORDER_ID));
                    mReturn.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                    mReturn.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                    mReturn.cust_no = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
                    mReturn.grossAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                    mReturn.vatAmt = cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL));
                    mReturn.preVatAmt = cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT));
                    mReturn.exciseAmt = cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL));
                    mReturn.netTotal = cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL));
                    mReturn.totalAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                    mReturn.exchangeNo = cursor.getString(cursor.getColumnIndex(SVH_EXCHANGE_NO));
                    mReturn.isReturnRequest = cursor.getString(cursor.getColumnIndex(IS_RETURN_LIST));
                    mReturn.salesmanId = cursor.getString(cursor.getColumnIndex(SALESMAN_ID));

                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mReturn;
    }

    //GET Return Detail
    public ReturnOrder getPostReturnRequestDetail() {

        ReturnOrder mReturn = new ReturnOrder();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_REQUEST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    mReturn.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                    mReturn.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                    mReturn.cust_no = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
                    mReturn.grossAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                    mReturn.vatAmt = cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL));
                    mReturn.preVatAmt = cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT));
                    mReturn.exciseAmt = cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL));
                    mReturn.netTotal = cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL));
                    mReturn.totalAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                    mReturn.exchangeNo = cursor.getString(cursor.getColumnIndex(SVH_EXCHANGE_NO));
                    mReturn.salesmanId = cursor.getString(cursor.getColumnIndex(SALESMAN_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mReturn;
    }

    //GET Load Request Detail
    public LoadRequest getPostSalesmanLoadRequestDetail() {

        LoadRequest mLoad = new LoadRequest();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SALESMAN_LOAD_REQUEST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    mLoad.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                    mLoad.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                    mLoad.routeId = cursor.getString(cursor.getColumnIndex(ROUTE_ID));
                    mLoad.salesmanId = cursor.getString(cursor.getColumnIndex(SALESMAN_ID));

                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLoad;
    }

    //GET Load Request Detail
    public LoadRequest getPostLoadRequestDetail() {

        LoadRequest mLoad = new LoadRequest();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_REQUEST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    mLoad.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                    mLoad.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                    mLoad.deliveryDate = cursor.getString(cursor.getColumnIndex(DELIVERY_DATE));
                    mLoad.grossAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                    mLoad.vatAmt = cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL));
                    mLoad.preVatAmt = cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT));
                    mLoad.exciseAmt = cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL));
                    mLoad.netTotal = cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL));
                    mLoad.totalAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                    mLoad.cust_no = cursor.getString(cursor.getColumnIndex(AGENT_ID));
                    mLoad.orderComment = cursor.getString(cursor.getColumnIndex(ORDER_COMMENT));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLoad;
    }


    //GET Load Detail
    public String getConfirmLoadNo() {

        String loadNo = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    loadNo = cursor.getString(cursor.getColumnIndex(SUB_LOAD_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadNo;
    }

    //GET Load Detail
    public String getConfirmLoadImage(String loadID) {

        String loadNo = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_HEADER + " WHERE " +
                    SUB_LOAD_ID + "=?", new String[]{loadID});
            try {
                if (cursor.moveToFirst()) {
                    loadNo = cursor.getString(cursor.getColumnIndex(IMAGE1));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadNo;
    }

    //GET Invoice Detail
    public HashMap<String, String> getPostInvoiceDetail(String invNo) {

        HashMap<String, String> mData = new HashMap<>();


        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_HEADER + " WHERE " +
                    SVH_CODE + "=?", new String[]{invNo});

            try {
                if (cursor.moveToFirst()) {
                    do {

                        mData.put("customerid", cursor.getString(cursor.getColumnIndex(SVH_CUST_CODE)));
                        mData.put("payment_type", getCustomerType(cursor.getString(cursor.getColumnIndex(SVH_CUST_CODE))));
                        if (getCustomerType(cursor.getString(cursor.getColumnIndex(SVH_CUST_CODE))).equalsIgnoreCase("1")) {
                            mData.put("payment_term", "0");
                            mData.put("due_date", "");
                        } else {
                            mData.put("payment_term", getPaymentTerm(cursor.getString(cursor.getColumnIndex(SVH_CUST_CODE))));
                            mData.put("due_date", UtilApp.getTCDueDate(getPaymentTerm(cursor.getString(cursor.getColumnIndex(SVH_CUST_CODE)))));
                        }
                        mData.put("invoiceNo", cursor.getString(cursor.getColumnIndex(SVH_CODE)));
                        mData.put("invoice_date", cursor.getString(cursor.getColumnIndex(SVH_INVOICE_DATE)));
                        mData.put("invoice_time", cursor.getString(cursor.getColumnIndex(SALE_TIME)));
                        mData.put("price_list", "1");
                        mData.put("net_total", cursor.getString(cursor.getColumnIndex(SVH_NET_VAL)));
                        mData.put("vat", cursor.getString(cursor.getColumnIndex(SVH_VAT_VAL)));
                        mData.put("gross_total", cursor.getString(cursor.getColumnIndex(SVH_GROSS_VAL)));
                        mData.put("pre_vat", cursor.getString(cursor.getColumnIndex(SVH_PRE_VAT)));
                        mData.put("excise", cursor.getString(cursor.getColumnIndex(SVH_EXCISE_VAL)));
                        mData.put("discount", "0");
                        mData.put("total_amount", cursor.getString(cursor.getColumnIndex(SVH_TOT_AMT_SALES)));
                        mData.put("delivery_id", cursor.getString(cursor.getColumnIndex(SVH_DELVERY_NO)));
                        mData.put("exchangeNo", cursor.getString(cursor.getColumnIndex(SVH_EXCHANGE_NO)));

                        if (cursor.getString(cursor.getColumnIndex(SVH_DISCOUNT_VAL)) != null) {
                            mData.put("discount", cursor.getString(cursor.getColumnIndex(SVH_DISCOUNT_VAL)));
                        } else {
                            mData.put("discount", "0");
                        }

                        if (cursor.getString(cursor.getColumnIndex(DISCOUNT_ID)) != null) {
                            mData.put("discount_id", cursor.getString(cursor.getColumnIndex(DISCOUNT_ID)));
                        } else {
                            mData.put("discount_id", "");
                        }

                        if (cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)) != null) {
                            mData.put("promotion_id", cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        } else {
                            mData.put("promotion_id", "");
                        }

                        if (cursor.getString(cursor.getColumnIndex(LATITUDE)) != null) {
                            mData.put("latitude", cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        } else {
                            mData.put("latitude", "");
                        }

                        if (cursor.getString(cursor.getColumnIndex(LONGITUDE)) != null) {
                            mData.put("longitude", cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        } else {
                            mData.put("longitude", "");
                        }

                        if (cursor.getString(cursor.getColumnIndex(SVH_PURCHASER_NO)) != null) {
                            mData.put("purchaser_contact", cursor.getString(cursor.getColumnIndex(SVH_PURCHASER_NO)));
                        } else {
                            mData.put("purchaser_contact", "");
                        }

                        if (cursor.getString(cursor.getColumnIndex(SVH_PURCHASER_NAME)) != null) {
                            mData.put("purchaser_name", cursor.getString(cursor.getColumnIndex(SVH_PURCHASER_NAME)));
                        } else {
                            mData.put("purchaser_name", "");
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mData;
    }

    //GET QTY
    public String getVanStockExistItemQty(String itemId, String type) {

        String qty = "0";

        SQLiteDatabase db = this.getReadableDatabase();


        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0) {
                if (type.equalsIgnoreCase("Base")) {
                    qty = cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY));
                } else {
                    qty = cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY));
                }

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;
    }

    //GET QTY
    public String getVanStockExistActualItemQty(String itemId, String type) {

        String qty = "0";

        SQLiteDatabase db = this.getReadableDatabase();


        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0) {
                if (type.equalsIgnoreCase("Base")) {
                    qty = cursor.getString(cursor.getColumnIndex(ACTUAL_BASE_QTY));
                } else {
                    qty = cursor.getString(cursor.getColumnIndex(ACTUAL_ALTER_QTY));
                }

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qty;
    }

    //GET Item Category
    public String getItemCategory(String itemId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_CATEGORY + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    //GET Item Category
    public String getItemBaseVolume(String itemId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_BASE_VOLUME + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(ITEM_BASE_VOLUME));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    public String getItemAGENTVolume(String itemId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_AGENT_EXCISE + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(ITEM_AGENT_EXCISE));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    public String getItemAlterVolume(String itemId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_ALTER_VOLUME + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(ITEM_ALTER_VOLUME));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    //GET Item Code
    public String getItemCode(String itemId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_CODE + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(ITEM_CODE));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    //GET Item Name
    public String getItemName(String itemId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_NAME + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(ITEM_NAME));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    //GET Item Name
    public String getRoutePriceCount(String routeId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + COUNT_ID + " FROM " + TABLE_AGENT_PRICE_COUNT +
                " WHERE " + ROUTE_ID + " = " + routeId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(COUNT_ID));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    //GET Item Name
    public String getDepotCustomerCount(String routeId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + COUNT_ID + " FROM " + TABLE_DEPOT_CUSTOMER_COUNT +
                " WHERE " + DEPOT_ID + " = " + routeId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(DEPOT_ID));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    //GET Item Base Price
    public double getItemBasePrice(String itemId) {

        double price = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_UOM_PRICE + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_UOM_PRICE)));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return price;
    }

    //GET Item Base Price
    public double getItemAltPrice(String itemId) {

        double price = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + ITEM_ALRT_UOM_PRCE + " FROM " + TABLE_ITEM +
                " WHERE " + ITEM_ID + " = " + itemId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return price;
    }

    //GET Invoice Amt
    public String getInvoiceAmount(String invId) {

        String category = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        selectQuery = "SELECT " + INVOICE_AMT + " FROM " + TABLE_INVOICE_HEADER +
                " WHERE " + SVH_CODE + " = " + invId;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            int count = cursor.getCount();
            if (count > 0)
                category = cursor.getString(cursor.getColumnIndex(INVOICE_AMT));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    public ArrayList<Customer> getCutomerList() {
        ArrayList<Customer> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Customer item = new Customer();
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerCode(cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setCustPhone(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setCustomerphone1(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                        item.setAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setCustType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)).equalsIgnoreCase("")) {
                            item.setPaymentTerm("0");
                        } else {
                            item.setPaymentTerm(cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)));
                        }

                        item.setCreditLimit(cursor.getString(cursor.getColumnIndex(CUSTOMER_CREDIT_LIMIT)));
                        item.setBalance(cursor.getString(cursor.getColumnIndex(CUSTOMER_BALANCE)));
                        item.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        item.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        item.setRadius(cursor.getString(cursor.getColumnIndex(CUSTOMER_RADIUS)));
                        item.setIsSale(cursor.getString(cursor.getColumnIndex(CUST_SALE)));
                        item.setIsColl(cursor.getString(cursor.getColumnIndex(CUST_COLL)));
                        item.setIsOrder(cursor.getString(cursor.getColumnIndex(CUST_ORDER)));
                        item.setIsReturn(cursor.getString(cursor.getColumnIndex(CUST_RETURN)));
                        item.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                        item.setVisibility(cursor.getString(cursor.getColumnIndex(CUSTOMER_VISIABLE)));
                        item.setCustomerType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                        item.setCustomerCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));

                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)) != null) {
                            item.setCustChannel(cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)));
                        } else {
                            item.setCustChannel("");
                        }

                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)) != null) {
                            item.setCustomerSubCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)));
                        } else {
                            item.setCustomerSubCategory("");
                        }

                        item.setSalesmanId(cursor.getString(cursor.getColumnIndex(SALESMAN_ID)));
                        item.setIsNew(cursor.getString(cursor.getColumnIndex(IS_NEW)));
                        item.setRouteId(cursor.getString(cursor.getColumnIndex(ROUTE_ID)));
                        item.setIs_fridge_assign(cursor.getString(cursor.getColumnIndex(IS_FREEZE_ASSIGN)));
                        item.setFridger_code(cursor.getString(cursor.getColumnIndex(FRIDGER_CODE)));
                        item.setAddress2(cursor.getString((cursor.getColumnIndex(ADDRESS_TWO))));
                        item.setCustomerstate(cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                        item.setCustomercity(cursor.getString(cursor.getColumnIndex(CUST_CITY)));
                        item.setCustomerzip(cursor.getString(cursor.getColumnIndex(CUST_ZIP)));

                        item.setMonSqu(cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                        item.setTueSqu(cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                        item.setWedSqu(cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                        item.setThuSqu(cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                        item.setFriSqu(cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                        item.setSatSqu(cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                        item.setSunSqu(cursor.getString(cursor.getColumnIndex(SUN_SEQ)));

                        if (cursor.getString(cursor.getColumnIndex(KEY_TEMP_CUST)) != null) {
                            if (cursor.getString(cursor.getColumnIndex(KEY_TEMP_CUST)).equals("0")) {
                                if (item.getCustomerCategory().equals("21")) {
                                    arrData.add(0, item);
                                } else {
                                    arrData.add(item);
                                }
                            }
                        } else {
                            if (item.getCustomerCategory().equals("21")) {
                                arrData.add(0, item);
                            } else {
                                arrData.add(item);
                            }
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<Customer> getCutomerDepotList() {
        ArrayList<Customer> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DEPOT_CUSTOMER;
        String dept = Settings.getString(App.DEPOTID);

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Customer item = new Customer();
                       /* item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerCode(cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setCustPhone(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setCustType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)).equalsIgnoreCase("")) {
                            item.setPaymentTerm("0");
                        } else {
                            item.setPaymentTerm(cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)));
                        }

                        item.setCreditLimit(cursor.getString(cursor.getColumnIndex(CUSTOMER_CREDIT_LIMIT)));
                        item.setBalance(cursor.getString(cursor.getColumnIndex(CUSTOMER_BALANCE)));
                        item.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        item.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        item.setRadius(cursor.getString(cursor.getColumnIndex(CUSTOMER_RADIUS)));
                        item.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                        item.setCustomerType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                        item.setCustomerCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                        item.setSalesmanId(cursor.getString(cursor.getColumnIndex(SALESMAN_ID)));
                        item.setRouteId(cursor.getString(cursor.getColumnIndex(ROUTE_ID)));*/

                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerCode(cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setCustPhone(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setCustomerphone1(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                        item.setAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setCustType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)).equalsIgnoreCase("")) {
                            item.setPaymentTerm("0");
                        } else {
                            item.setPaymentTerm(cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)));
                        }

                        item.setCreditLimit(cursor.getString(cursor.getColumnIndex(CUSTOMER_CREDIT_LIMIT)));
                        item.setBalance(cursor.getString(cursor.getColumnIndex(CUSTOMER_BALANCE)));
                        item.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        item.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        item.setRadius(cursor.getString(cursor.getColumnIndex(CUSTOMER_RADIUS)));
                        item.setIsSale(cursor.getString(cursor.getColumnIndex(CUST_SALE)));
                        item.setIsColl(cursor.getString(cursor.getColumnIndex(CUST_COLL)));
                        item.setIsOrder(cursor.getString(cursor.getColumnIndex(CUST_ORDER)));
                        item.setIsReturn(cursor.getString(cursor.getColumnIndex(CUST_RETURN)));
                        item.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                        item.setVisibility(cursor.getString(cursor.getColumnIndex(CUSTOMER_VISIABLE)));
                        item.setCustomerType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                        item.setCustomerCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)) != null) {
                            item.setCustChannel(cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)));
                        } else {
                            item.setCustChannel("");
                        }

                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)) != null) {
                            item.setCustomerSubCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)));
                        } else {
                            item.setCustomerSubCategory("");
                        }
                        item.setSalesmanId(cursor.getString(cursor.getColumnIndex(SALESMAN_ID)));
                        item.setIsNew(cursor.getString(cursor.getColumnIndex(IS_NEW)));
                        item.setRouteId(Settings.getString(App.ROUTEID));
                        item.setIs_fridge_assign(cursor.getString(cursor.getColumnIndex(IS_FREEZE_ASSIGN)));
                        item.setFridger_code(cursor.getString(cursor.getColumnIndex(FRIDGER_CODE)));
                        item.setMonSqu(cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                        item.setTueSqu(cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                        item.setWedSqu(cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                        item.setThuSqu(cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                        item.setFriSqu(cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                        item.setSatSqu(cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                        item.setSunSqu(cursor.getString(cursor.getColumnIndex(SUN_SEQ)));

                        arrData.add(item);
                        /*if (item.getCustomerType().equals("33") || item.getCustomerType().equals("6") || item.getCustomerType().equals("7")) {
                            arrData.add(item);
                        }*/
                       /* if (item.getCustomerType().equals("2")) {
                            arrData.add(item);
                        }*/

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getCustomerTypeList() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_TYPE;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }

            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getCustomerTypeIdList() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_TYPE;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE_ID));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }


    public String[] getCustomerCategoryList() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CATEGORY;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getChannelCategoryList(String channelId) {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_CATEGORY + " WHERE " +
                    CUSTOMER_CHANNEL_ID + "=?", new String[]{channelId});

            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getCustomerChannelList() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CHANNEL;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getCustomerSubCategoryList() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_SUB_CATEGORY;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<Category> getCustCategoryList() {
        ArrayList<Category> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CATEGORY;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    arrData = new ArrayList<>();
                    if (cursor.moveToFirst()) {
                        do {
                            Category mCat = new Category();
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            mCat.name = name;
                            arrData.add(mCat);
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<SubCategoryType> getCustSubCategoryList() {
        ArrayList<SubCategoryType> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_SUB_CATEGORY;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    arrData = new ArrayList<>();
                    if (cursor.moveToFirst()) {
                        do {
                            SubCategoryType mCat = new SubCategoryType();
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            mCat.name = name;
                            arrData.add(mCat);
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<ChannellType> getCustChannelList() {
        ArrayList<ChannellType> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CHANNEL;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    arrData = new ArrayList<>();
                    if (cursor.moveToFirst()) {
                        do {
                            ChannellType mCat = new ChannellType();
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME));
                            mCat.outlet_channel = name;
                            arrData.add(mCat);
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getChannelCategoryId(String channelId) {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_CATEGORY + " WHERE " +
                    CUSTOMER_CHANNEL_ID + "=?", new String[]{channelId});
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE_ID));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getCustomerCategoryId() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CATEGORY;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE_ID));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getCustomerChannelId() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_CHANNEL;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE_ID));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public String[] getCustomerSubCategoryId() {
        String[] arrData = new String[0];

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER_SUB_CATEGORY;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.getCount() > 0) {
                    int i = 0;
                    arrData = new String[cursor.getCount()];
                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex(CATEGORY_TYPE_ID));
                            arrData[i] = name;
                            i++;
                        } while (cursor.moveToNext());
                    }
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }


    //Insert customer visit
    public void UpdateCustomerVisiblity(String customerId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(CUSTOMER_VISIABLE, "1");

        db.update(TABLE_CUSTOMER,
                contentValueItem, CUSTOMER_ID + " = ?", new String[]{customerId});
    }

    public int getTotalCustomerList() {
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                count = cursor.getCount();
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return count;
    }

    public int getTotalVisibleCustomer() {
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_VISIABLE + "=?", new String[]{"1"});
            // looping through all rows and adding to list
            try {
                count = cursor.getCount();
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return count;
    }


    public Customer getCustomerDetail(String customerId) {
        Customer item = new Customer();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId}, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerCode(cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setCustPhone(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setCustomerphone1(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                        item.setAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setCustType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)).equalsIgnoreCase("")) {
                            item.setPaymentTerm("0");
                        } else {
                            item.setPaymentTerm(cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)));
                        }
                        item.setCreditLimit(cursor.getString(cursor.getColumnIndex(CUSTOMER_CREDIT_LIMIT)));
                        item.setBalance(cursor.getString(cursor.getColumnIndex(CUSTOMER_BALANCE)));
                        item.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        item.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        item.setRadius(cursor.getString(cursor.getColumnIndex(CUSTOMER_RADIUS)));
                        item.setIsSale(cursor.getString(cursor.getColumnIndex(CUST_SALE)));
                        item.setIsColl(cursor.getString(cursor.getColumnIndex(CUST_COLL)));
                        item.setIsOrder(cursor.getString(cursor.getColumnIndex(CUST_ORDER)));
                        item.setIsReturn(cursor.getString(cursor.getColumnIndex(CUST_RETURN)));
                        item.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                        item.setVisibility(cursor.getString(cursor.getColumnIndex(CUSTOMER_VISIABLE)));
                        item.setCustomerType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                        item.setCustomerCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                        item.setSalesmanId(cursor.getString(cursor.getColumnIndex(SALESMAN_ID)));
                        item.setIsNew(cursor.getString(cursor.getColumnIndex(IS_NEW)));
                        item.setRouteId(cursor.getString(cursor.getColumnIndex(ROUTE_ID)));
                        item.setIs_fridge_assign(cursor.getString(cursor.getColumnIndex(IS_FREEZE_ASSIGN)));
                        item.setFridger_code(cursor.getString(cursor.getColumnIndex(FRIDGER_CODE)));
                        item.setAddress2(cursor.getString((cursor.getColumnIndex(ADDRESS_TWO))));
                        item.setCustomerstate(cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                        item.setCustomercity(cursor.getString(cursor.getColumnIndex(CUST_CITY)));
                        item.setCustomerzip(cursor.getString(cursor.getColumnIndex(CUST_ZIP)));
                        item.setMonSqu(cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                        item.setTueSqu(cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                        item.setWedSqu(cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                        item.setThuSqu(cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                        item.setFriSqu(cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                        item.setSatSqu(cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                        item.setSunSqu(cursor.getString(cursor.getColumnIndex(SUN_SEQ)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }


    public ArrayList<Customer> getRecentList() {
        ArrayList<Customer> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_RECENT_CUSTOMER;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Customer item = new Customer();
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public int getCutomerCount() {
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                count = cursor.getCount();
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return count;
    }

    public int getVisitedCustomer() {
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_RECENT_CUSTOMER;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                count = cursor.getCount();
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return count;
    }

    public ArrayList<Customer> getSeqCustomerList(String seq) {
        ArrayList<Customer> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    seq + "=?", new String[]{"1"});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Customer item = new Customer();
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerCode(cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setCustPhone(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setCustomerphone1(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                        item.setAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setCustType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)).equalsIgnoreCase("")) {
                            item.setPaymentTerm("0");
                        } else {
                            item.setPaymentTerm(cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)));
                        }

                        item.setCreditLimit(cursor.getString(cursor.getColumnIndex(CUSTOMER_CREDIT_LIMIT)));
                        item.setBalance(cursor.getString(cursor.getColumnIndex(CUSTOMER_BALANCE)));
                        item.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        item.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        item.setRadius(cursor.getString(cursor.getColumnIndex(CUSTOMER_RADIUS)));
                        item.setIsSale(cursor.getString(cursor.getColumnIndex(CUST_SALE)));
                        item.setIsColl(cursor.getString(cursor.getColumnIndex(CUST_COLL)));
                        item.setIsOrder(cursor.getString(cursor.getColumnIndex(CUST_ORDER)));
                        item.setIsReturn(cursor.getString(cursor.getColumnIndex(CUST_RETURN)));
                        item.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                        item.setVisibility(cursor.getString(cursor.getColumnIndex(CUSTOMER_VISIABLE)));
                        item.setCustomerType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                        item.setCustomerCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)) != null) {
                            item.setCustChannel(cursor.getString(cursor.getColumnIndex(CUSTOMER_CHANNEL_ID)));
                        } else {
                            item.setCustChannel("");
                        }

                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)) != null) {
                            item.setCustomerSubCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_SUB_CATEGORYID)));
                        } else {
                            item.setCustomerSubCategory("");
                        }
                        item.setSalesmanId(cursor.getString(cursor.getColumnIndex(SALESMAN_ID)));
                        item.setIsNew(cursor.getString(cursor.getColumnIndex(IS_NEW)));
                        item.setRouteId(cursor.getString(cursor.getColumnIndex(ROUTE_ID)));
                        item.setIs_fridge_assign(cursor.getString(cursor.getColumnIndex(IS_FREEZE_ASSIGN)));
                        item.setFridger_code(cursor.getString(cursor.getColumnIndex(FRIDGER_CODE)));
                        item.setAddress2(cursor.getString((cursor.getColumnIndex(ADDRESS_TWO))));
                        item.setCustomerstate(cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                        item.setCustomercity(cursor.getString(cursor.getColumnIndex(CUST_CITY)));
                        item.setCustomerzip(cursor.getString(cursor.getColumnIndex(CUST_ZIP)));
                        item.setMonSqu(cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                        item.setTueSqu(cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                        item.setWedSqu(cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                        item.setThuSqu(cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                        item.setFriSqu(cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                        item.setSatSqu(cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                        item.setSunSqu(cursor.getString(cursor.getColumnIndex(SUN_SEQ)));

                        if (cursor.getString(cursor.getColumnIndex(KEY_TEMP_CUST)) != null) {
                            if (cursor.getString(cursor.getColumnIndex(KEY_TEMP_CUST)).equals("0")) {
                                if (item.getCustomerCategory().equals("21")) {
                                    arrData.add(0, item);
                                } else {
                                    arrData.add(item);
                                }
                            }
                        } else {
                            if (item.getCustomerCategory().equals("21")) {
                                arrData.add(0, item);
                            } else {
                                arrData.add(item);
                            }

                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public int getSeqCustomerCount(String seq) {
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    seq + "=?" + " AND "
                    + CUSTOMER_VISIABLE + "=?", new String[]{"1"});
            // looping through all rows and adding to list
            try {
                count = cursor.getCount();
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return count;
    }

    public ArrayList<PromoOfferData> getAllCustomerPromotion() {
        ArrayList<PromoOfferData> arrPayment = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_PROMO, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        PromoOfferData payment = new PromoOfferData();
                        //only one column
                        payment.promotionId = cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID));
                        payment.promotionName = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                        payment.offerUom = cursor.getString(cursor.getColumnIndex(QUALIFY_UOM_ID));
                        payment.assignUom = cursor.getString(cursor.getColumnIndex(ASSIGN_UOM_ID));
                        payment.offerItem = getCustomerPromoOfferItem(cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        payment.orderItem = getCustomerPromoOrderItem(cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        arrPayment.add(payment);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrPayment;
    }

    public ArrayList<PromoOfferData> getAllPromotion() {
        ArrayList<PromoOfferData> arrPayment = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PROMO, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        PromoOfferData payment = new PromoOfferData();
                        //only one column
                        payment.promotionId = cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID));
                        payment.promotionName = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                        payment.offerUom = cursor.getString(cursor.getColumnIndex(QUALIFY_UOM_ID));
                        payment.assignUom = cursor.getString(cursor.getColumnIndex(ASSIGN_UOM_ID));
                        payment.offerItem = getPromoOfferrItem(cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        payment.orderItem = getPromoOrderItem(cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        arrPayment.add(payment);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrPayment;
    }


    public ArrayList<PromoOfferData> getAllAgentPromotion() {
        ArrayList<PromoOfferData> arrPayment = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_PROMO, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        PromoOfferData payment = new PromoOfferData();
                        //only one column
                        payment.promotionId = cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID));
                        payment.promotionName = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                        payment.offerUom = cursor.getString(cursor.getColumnIndex(QUALIFY_UOM_ID));
                        payment.assignUom = cursor.getString(cursor.getColumnIndex(ASSIGN_UOM_ID));
                        payment.offerItem = getAgentPromoOfferrItem(cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        payment.orderItem = getAgentPromoOrderItem(cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        arrPayment.add(payment);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrPayment;
    }

    public ArrayList<Item> getPromoOrderItem(String itemId) {
        ArrayList<Item> arrItem = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ORDER_ITEM + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        Item mItem = new Item();
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrItem;
    }

    public ArrayList<Item> getAgentPromoOrderItem(String itemId) {
        ArrayList<Item> arrItem = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_ORDER_ITEM + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        Item mItem = new Item();
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrItem;
    }

    public ArrayList<Item> getCustomerPromoOrderItem(String itemId) {
        ArrayList<Item> arrItem = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_ORDER_ITEM + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        Item mItem = new Item();
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrItem;
    }

    public ArrayList<Item> getPromoOfferrItem(String itemId) {
        ArrayList<Item> arrItem = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_OFFER_ITEM + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        Item mItem = new Item();
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.setItemName(getItemName(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrItem;
    }

    public ArrayList<Item> getCustomerPromoOfferItem(String itemId) {
        ArrayList<Item> arrItem = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_OFFER_ITEM + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        Item mItem = new Item();
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.setItemName(getItemName(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrItem;
    }

    public ArrayList<Item> getAgentPromoOfferrItem(String itemId) {
        ArrayList<Item> arrItem = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_OFFER_ITEM + " WHERE " +
                    PROMOTIONAL_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        Item mItem = new Item();
                        mItem.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.setItemName(getItemName(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrItem;
    }

    public ArrayList<Payment> getAllPaymentToday() {
        ArrayList<Payment> arrPayment = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PAYMENT + " WHERE " +
                    PAYMENT_DATE + "=?", new String[]{UtilApp.getCurrentDate()});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Payment payment = new Payment();
                        //only one column
                        payment.invoice_id = cursor.getString(cursor.getColumnIndex(PAYMENT_INVOICE_ID));
                        payment.collection_id = cursor.getString(cursor.getColumnIndex(PAYMENT_COLLECTION_ID));
                        payment.payment_type = cursor.getString(cursor.getColumnIndex(PAYMENT_TYPE));
                        payment.payment_date = cursor.getString(cursor.getColumnIndex(PAYMENT_DATE));
                        payment.cheque_no = cursor.getString(cursor.getColumnIndex(PAYMENT_CHEQUE_NO));
                        payment.bank_name = cursor.getString(cursor.getColumnIndex(PAYMENT_BANK_NAME));
                        payment.payment_amount = cursor.getString(cursor.getColumnIndex(PAYMENT_AMOUNT));
                        payment.cust_id = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));

                        arrPayment.add(payment);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrPayment;
    }


    public Salesman getSalesmanDetail(String salesmanID) {
        Salesman mSalesman = new Salesman();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SALES_MAN + " WHERE " +
                    SALESMAN_ID + "=?", new String[]{salesmanID});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        mSalesman.setSalesmanId(cursor.getString(cursor.getColumnIndex(SALESMAN_ID)));
                        mSalesman.setSalesmanCode(cursor.getString(cursor.getColumnIndex(SALESMAN_CODE)));
                        mSalesman.setSalesmanName(cursor.getString(cursor.getColumnIndex(SALESMAN_NAME_EN)));
                        mSalesman.setRoute(cursor.getString(cursor.getColumnIndex(SALESMAN_ROUTE)));
                        mSalesman.setRole(cursor.getString(cursor.getColumnIndex(SALESMAN_ROLE)));
                        mSalesman.setDist_channel(cursor.getString(cursor.getColumnIndex(SALESMAN_DIS_CHANNEL)));
                        mSalesman.setVehicle_no(cursor.getString(cursor.getColumnIndex(SALESMAN_VEHICLE_NO)));
                        mSalesman.setRouteName(cursor.getString(cursor.getColumnIndex(SALESMAN_ROUTE_NAME)));
                        mSalesman.setRegion(cursor.getString(cursor.getColumnIndex(SALESMAN_REGION)));
                        mSalesman.setSubRegion(cursor.getString(cursor.getColumnIndex(SALESMAN_SUB_REGION)));

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSalesman;
    }

    public String getUOM(String uomId) {
        String strUOM = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UOM + " WHERE " +
                    UOM_ID + "=?", new String[]{uomId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        strUOM = cursor.getString(cursor.getColumnIndex(UOM_NAME));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUOM;
    }

    public String getItemUOM(String itemId, String type) {
        String strUOM = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        if (type.equalsIgnoreCase("Base")) {
                            strUOM = cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM));
                        } else {
                            strUOM = cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM));
                        }

                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUOM;
    }

    //Get Item UPC
    public String getItemUPC(String itemId) {
        String strUOM = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        strUOM = cursor.getString(cursor.getColumnIndex(ITEM_UPC));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUOM;
    }

    //Get Item UPC
    public String getItemAgentExcies(String itemId) {
        String strUOM = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        strUOM = cursor.getString(cursor.getColumnIndex(ITEM_AGENT_EXCISE));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUOM;
    }

    //Get Item UPC
    public String getItemDirectExcies(String itemId) {
        String strUOM = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        strUOM = cursor.getString(cursor.getColumnIndex(ITEM_DIRECT_EXCISE));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUOM;
    }

    public ArrayList<Item> getLoadItemList(String subLoadId) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_ITEMS + " WHERE " +
                    SUB_LOAD_ID + "=?", new String[]{subLoadId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));

                        if (cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)) != null) {
                            item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                            item.setBaseUOMName(cursor.getString(cursor.getColumnIndex(BASE_UOM_NAME)));
                            item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            item.setBaseUOMPrice(getItemBase(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        } else {
                            item.setBaseUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Base"));
                            item.setBaseUOMName(getUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Base")));
                            item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            item.setBaseUOMPrice(getItemBase(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        }

                        if (cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)) != null) {
                            item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                            item.setAlterUOMName(cursor.getString(cursor.getColumnIndex(ALTER_UOM_NAME)));
                            item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            item.setAlterUOMPrice(getItemAlter(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        } else {
                            item.setAltrUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Alter"));
                            item.setAlterUOMName(getUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Alter")));
                            item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            item.setAlterUOMPrice(getItemAlter(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        }

                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public int getLoadItemQty(String itemId, String type) {

        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        if (type.equalsIgnoreCase("Base")) {
                            if (cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)) != null) {
                                qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                            } else {
                                qty += 0;
                            }

                        } else {
                            if (cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)) != null) {
                                qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            } else {
                                qty += 0;
                            }
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return qty;
    }

    public int getSaleItemQty(String itemId, String type) {

        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        if (type.equalsIgnoreCase("Base")) {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        } else {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return qty;
    }

    public double getSaleItemPrice(String itemId, String type) {

        double qty = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_INVOICE_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        if (type.equalsIgnoreCase("Base")) {
                            if (cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)) != null) {
                                if (!cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)).equals("")) {
                                    qty += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                                }
                            }
                        } else {
                            if (cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)) != null) {
                                if (!cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)).equals("")) {
                                    qty += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                                }
                            }
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return qty;
    }

    public int getBadItemQty(String itemId, String type) {
        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_ITEMS + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + RETURN_TYPE + "=?", new String[]{itemId, "Bad"});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        if (type.equalsIgnoreCase("Base")) {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        } else {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return qty;
    }

    public int getUnloadItemQty(String itemId, String type) {
        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UNLOAD_VARIANCE + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + REASON_TYPE + "=?", new String[]{itemId, "0"});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        if (type.equalsIgnoreCase("Base")) {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        } else {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return qty;
    }

    //Get Variance Items
    public ArrayList<Item> getVarianceItem(String reasonCode) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UNLOAD_VARIANCE + " WHERE " +
                    REASON_TYPE + "=?", new String[]{reasonCode});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            int position = 0;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                    position = i;
                                    isContain = true;
                                    break;
                                }
                            }

                            if (isContain) {
                                Item containItem = arrData.get(position);
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    containItem.setBaseUOMQty(item.getBaseUOMQty());
                                    containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                    containItem.setBaseUOM(item.getBaseUOM());
                                    containItem.setBaseUOMName(item.getBaseUOMName());
                                } else {
                                    containItem.setAlterUOMQty(item.getAlterUOMQty());
                                    containItem.setAlterUOMPrice(item.getAlterUOMPrice());
                                    containItem.setAltrUOM(item.getAltrUOM());
                                    containItem.setAlterUOMName(item.getAlterUOMName());
                                }
                                arrData.set(position, containItem);
                            } else {
                                arrData.add(item);
                            }
                        } else {
                            arrData.add(item);
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get Variance Bad Items
    public int getBadVarianceItem(String itemId, String type) {
        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UNLOAD_VARIANCE + " WHERE " +
                    REASON_TYPE + "=?" + " AND "
                    + ITEM_ID + "=?", new String[]{"5", itemId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();

                        if (type.equalsIgnoreCase("Base")) {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        } else {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return qty;
    }


    public ArrayList<Load> getAllLoad() {
        ArrayList<Load> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_LOAD_HEADER;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Load mLoad = new Load();
                        mLoad.setLoad_no(cursor.getString(cursor.getColumnIndex(LOAD_ID)));
                        mLoad.setSub_loadId(cursor.getString(cursor.getColumnIndex(SUB_LOAD_ID)));
                        mLoad.setDel_date(cursor.getString(cursor.getColumnIndex(LOAD_DATE)));
                        mLoad.setIs_uploaded(cursor.getString(cursor.getColumnIndex(DATA_MARK_FOR_POST)));
                        mLoad.setIs_verified(cursor.getString(cursor.getColumnIndex(LOAD_IS_VERIFIED)));

                        arrData.add(mLoad);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }

    public String getLoadAmount(String subLoadId) {
        String amt = "0";
        int bsQty = 0, alQty = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_ITEMS + " WHERE " +
                    SUB_LOAD_ID + "=?", new String[]{subLoadId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        double price = 0;
                        if (cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)) != null) {
                            alQty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                            //double alterPrice = (Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY))) * Double.parseDouble(getItemAlter(cursor.getString(cursor.getColumnIndex(ITEM_ID)))));
                            //double basePrice = 0;
                            if (cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)) != null) {
                                bsQty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                                //basePrice = (Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) * Double.parseDouble(getItemBase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))));
                            }
                            //price = alterPrice + basePrice;
                        } else {
                            if (cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)) != null) {
                                bsQty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                                // price = (Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY))) * Double.parseDouble(getItemBase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))));
                            }
                        }
                        // amt = amt + price;
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        amt = alQty + "/" + bsQty;
        return amt;
    }

    //get Item Price
    public double getItemPrice(String itemId) {
        double price = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_UOM_PRICE)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return price;
    }

    //get Item Price
    public double getItemAlterPrice(String itemId) {
        double price = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return price;
    }

    //get CustomerName
    public String getCustomerName(String customerId) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //get CustomerName
    public String getResolutionStatus(String ticketNo) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SERVICE_VISIT_POST + " WHERE " +
                    TICKET_NO + "=?", new String[]{ticketNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(SV_COMPLAIN_TYPE));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //get CustomerName
    public String getDepotCustomerName(String customerId) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //get Customer Code
    public String getCustomerCode(String customerId) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //get Customer Code
    public String getCustomerRoute(String customerId) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(ROUTE_ID));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //get Customer Code
    public String getDepotCustomerCode(String customerId) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //get Customer Code
    public String getDepotCustomerRoute(String customerId) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(ROUTE_ID));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //get Customer Pricing Items
    public String getCustomerType(String customerId) {

        String basPrice = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        basPrice = cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    /*//get Customer Name
    public String getCustomerName(String customerId) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }*/

    //get PaymentTerm
    public String getPaymentTerm(String customerId) {
        String name = "1";

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return name;
    }

    //Get All Transaction Data
    public ArrayList<Transaction> getAllTransactions() {

        ArrayList<Transaction> list = new ArrayList<Transaction>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Transaction transaction = new Transaction();
                        //only one column
                        transaction.tr_type = cursor.getString(cursor.getColumnIndex(TR_TYPE));
                        transaction.tr_date_time = cursor.getString(cursor.getColumnIndex(TR_DATE));
                        transaction.tr_salesman_id = cursor.getString(cursor.getColumnIndex(TR_SALESMAN_ID));
                        transaction.tr_customer_name = cursor.getString(cursor.getColumnIndex(TR_CUSTOMER_NAME));
                        transaction.tr_customer_num = cursor.getString(cursor.getColumnIndex(TR_CUSTOMER_NUM));
                        transaction.tr_collection_id = cursor.getString(cursor.getColumnIndex(TR_COLLECTION_ID));
                        transaction.tr_order_id = cursor.getString(cursor.getColumnIndex(TR_ORDER_ID));
                        transaction.tr_invoice_id = cursor.getString(cursor.getColumnIndex(TR_INVOICE_ID));
                        transaction.tr_pyament_id = cursor.getString(cursor.getColumnIndex(TR_PYAMENT_ID));
                        transaction.tr_is_posted = cursor.getString(cursor.getColumnIndex(TR_IS_POSTED));
                        transaction.tr_printData = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                        transaction.tr_message = cursor.getString(cursor.getColumnIndex(TR_MESSAGE));
                        transaction.tr_isCheck = "false";

                        //you could add additional columns here..
                        list.add(transaction);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    //Get All Transaction Data
    public ArrayList<Transaction> getCusTransactions(String custId) {

        ArrayList<Transaction> list = new ArrayList<Transaction>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION +
                " WHERE " + TR_CUSTOMER_NUM + " =?";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{custId});
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Transaction transaction = new Transaction();
                        //only one column
                        transaction.tr_type = cursor.getString(cursor.getColumnIndex(TR_TYPE));
                        transaction.tr_date_time = cursor.getString(cursor.getColumnIndex(TR_DATE));
                        transaction.tr_salesman_id = cursor.getString(cursor.getColumnIndex(TR_SALESMAN_ID));
                        transaction.tr_customer_name = cursor.getString(cursor.getColumnIndex(TR_CUSTOMER_NAME));
                        transaction.tr_customer_num = cursor.getString(cursor.getColumnIndex(TR_CUSTOMER_NUM));
                        transaction.tr_collection_id = cursor.getString(cursor.getColumnIndex(TR_COLLECTION_ID));
                        transaction.tr_order_id = cursor.getString(cursor.getColumnIndex(TR_ORDER_ID));
                        transaction.tr_invoice_id = cursor.getString(cursor.getColumnIndex(TR_INVOICE_ID));
                        transaction.tr_pyament_id = cursor.getString(cursor.getColumnIndex(TR_PYAMENT_ID));
                        transaction.tr_is_posted = cursor.getString(cursor.getColumnIndex(TR_IS_POSTED));
                        transaction.tr_printData = cursor.getString(cursor.getColumnIndex(KEY_DATA));
                        transaction.tr_message = cursor.getString(cursor.getColumnIndex(TR_MESSAGE));

                        //you could add additional columns here..
                        list.add(transaction);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }


    //Get Return Type Order
    public String getReturnType(String orderNo) {

        String returnType = "";

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETURN_HEADER +
                " WHERE " + ORDER_NO + " =?";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{orderNo});
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        returnType = cursor.getString(cursor.getColumnIndex(RETURN_TYPE));
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return returnType;
    }


    //Get All Return Order
    public ArrayList<ReturnOrder> getAllReturn(String customerCode) {

        ArrayList<ReturnOrder> list = new ArrayList<ReturnOrder>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETURN_HEADER +
                " WHERE " + CUSTOMER_ID + " =?";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{customerCode});
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        ReturnOrder order = new ReturnOrder();

                        order.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                        order.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                        order.returnType = cursor.getString(cursor.getColumnIndex(RETURN_TYPE));
                        order.isReturnRequest = cursor.getString(cursor.getColumnIndex(IS_RETURN_LIST));
                        order.isPosted = cursor.getString(cursor.getColumnIndex(DATA_MARK_FOR_POST));

                        //you could add additional columns here..
                        list.add(order);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    //Get All Return Order
    public ArrayList<ReturnOrder> getAllReturnMR(String customerCode) {

        ArrayList<ReturnOrder> list = new ArrayList<ReturnOrder>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETURN_REQUEST_HEADER +
                " WHERE " + CUSTOMER_ID + " =?";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{customerCode});
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        ReturnOrder order = new ReturnOrder();

                        order.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                        order.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                        order.returnType = cursor.getString(cursor.getColumnIndex(RETURN_TYPE));

                        //you could add additional columns here..
                        list.add(order);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    //Get All Return Order
    public ArrayList<ReturnOrder> getCreditReturn() {

        ArrayList<ReturnOrder> list = new ArrayList<ReturnOrder>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RETURN_HEADER;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        ReturnOrder order = new ReturnOrder();

                        order.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                        order.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                        order.returnType = cursor.getString(cursor.getColumnIndex(RETURN_TYPE));
                        order.totalAmt = cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT));
                        order.cust_no = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));

                        if (cursor.getString(cursor.getColumnIndex(SVH_EXCHANGE_NO)).equalsIgnoreCase("")) {
                            //you could add additional columns here..
                            list.add(order);
                        }

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    //Get All Order
    public ArrayList<Order> getAllOrder(String customerCode) {

        ArrayList<Order> list = new ArrayList<Order>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORDER_HEADER +
                " WHERE " + CUSTOMER_ID + " =?";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{customerCode});
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        Order order = new Order();

                        order.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                        order.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                        order.deliveyDate = cursor.getString(cursor.getColumnIndex(DELIVERY_DATE));

                        //you could add additional columns here..
                        list.add(order);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    //Get All Load Request
    public ArrayList<Order> getAllLoadRequest(String salesmanId) {

        ArrayList<Order> list = new ArrayList<Order>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOAD_REQUEST_HEADER +
                " WHERE " + SALESMAN_ID + " =?";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{salesmanId});
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        Order order = new Order();

                        order.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                        order.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                        order.deliveyDate = cursor.getString(cursor.getColumnIndex(DELIVERY_DATE));

                        //you could add additional columns here..
                        list.add(order);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    //Get All Load Request
    public ArrayList<Order> getAllSalesmanLoadRequest() {

        ArrayList<Order> list = new ArrayList<Order>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SALESMAN_LOAD_REQUEST_HEADER;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        Order order = new Order();

                        order.orderNo = cursor.getString(cursor.getColumnIndex(ORDER_NO));
                        order.orderDate = cursor.getString(cursor.getColumnIndex(ORDER_DATE));
                        order.salesmanId = cursor.getString(cursor.getColumnIndex(SALESMAN_NAME));
                        order.deliveyDate = cursor.getString(cursor.getColumnIndex(ROUTE_NAME));

                        //you could add additional columns here..
                        list.add(order);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    public ArrayList<Item> getReturnItems(String orderNo) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setSaleBaseQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setSaleBasePrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setSaleAltQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleAltPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setNetAmt(cursor.getString(cursor.getColumnIndex(ITEM_NET_VAL)));
                        item.setVatAmt(cursor.getString(cursor.getColumnIndex(ITEM_VAT_VAL)));
                        item.setPreVatAmt(cursor.getString(cursor.getColumnIndex(ITEM_PRE_VAT)));
                        item.setExciseAmt(cursor.getString(cursor.getColumnIndex(ITEM_EXCISE_VAL)));
                        item.setReasonCode(cursor.getString(cursor.getColumnIndex(REASON_TYPE)));
                        item.setUOMPrice(cursor.getString(cursor.getColumnIndex(BASE_PRCE)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ALRT_PRCE)));

                        double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE))) +
                                Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setPrice("" + price);

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            int position = 0;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                    position = i;
                                    isContain = true;
                                    break;
                                }
                            }

                            if (isContain) {
                                Item containItem = arrData.get(position);
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    containItem.setBaseUOMQty(item.getBaseUOMQty());
                                    containItem.setSaleBaseQty(item.getBaseUOMQty());
                                    //containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                    containItem.setSaleBasePrice(item.getBaseUOMPrice());
                                    containItem.setBaseUOM(item.getBaseUOM());
                                    containItem.setBaseUOMName(item.getBaseUOMName());

                                    double priceitem = Double.parseDouble(item.getSaleBasePrice()) +
                                            Double.parseDouble(containItem.getSaleAltPrice());
                                    containItem.setPrice("" + priceitem);

                                    double vatAmt = Double.parseDouble(item.getVatAmt()) +
                                            Double.parseDouble(containItem.getVatAmt());
                                    containItem.setVatAmt("" + vatAmt);

                                    double preVatAmt = Double.parseDouble(item.getPreVatAmt()) +
                                            Double.parseDouble(containItem.getPreVatAmt());
                                    containItem.setPreVatAmt("" + preVatAmt);

                                    double exciseAmt = Double.parseDouble(item.getExciseAmt()) +
                                            Double.parseDouble(containItem.getExciseAmt());
                                    containItem.setExciseAmt("" + exciseAmt);

                                    double netAmt = Double.parseDouble(item.getNetAmt()) +
                                            Double.parseDouble(containItem.getNetAmt());
                                    containItem.setNetAmt("" + netAmt);

                                } else {
                                    containItem.setAlterUOMQty(item.getAlterUOMQty());
                                    containItem.setSaleAltQty(item.getAlterUOMQty());
                                    containItem.setSaleAltPrice(item.getSaleAltPrice());
                                    containItem.setAltrUOM(item.getAltrUOM());
                                    containItem.setAlterUOMName(item.getAlterUOMName());

                                    double priceitem = Double.parseDouble(containItem.getSaleBasePrice()) +
                                            Double.parseDouble(item.getSaleAltPrice());
                                    containItem.setPrice("" + priceitem);

                                    double vatAmt = Double.parseDouble(item.getVatAmt()) +
                                            Double.parseDouble(containItem.getVatAmt());
                                    containItem.setVatAmt("" + vatAmt);

                                    double preVatAmt = Double.parseDouble(item.getPreVatAmt()) +
                                            Double.parseDouble(containItem.getPreVatAmt());
                                    containItem.setPreVatAmt("" + preVatAmt);

                                    double exciseAmt = Double.parseDouble(item.getExciseAmt()) +
                                            Double.parseDouble(containItem.getExciseAmt());
                                    containItem.setExciseAmt("" + exciseAmt);

                                    double netAmt = Double.parseDouble(item.getNetAmt()) +
                                            Double.parseDouble(containItem.getNetAmt());
                                    containItem.setNetAmt("" + netAmt);
                                }

                                arrData.set(position, containItem);
                            } else {
                                arrData.add(item);
                            }
                        } else {
                            arrData.add(item);
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<Item> getReturnRequestItems(String orderNo) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_REQUEST_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            int position = 0;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                    position = i;
                                    isContain = true;
                                    break;
                                }
                            }

                            if (isContain) {
                                Item containItem = arrData.get(position);
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    containItem.setBaseUOMQty(item.getBaseUOMQty());
                                    containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                    containItem.setBaseUOM(item.getBaseUOM());
                                    containItem.setBaseUOMName(item.getBaseUOMName());
                                } else {
                                    containItem.setAlterUOMQty(item.getAlterUOMQty());
                                    containItem.setAlterUOMPrice(item.getAlterUOMPrice());
                                    containItem.setAltrUOM(item.getAltrUOM());
                                    containItem.setAlterUOMName(item.getAlterUOMName());
                                }
                                arrData.set(position, containItem);
                            } else {
                                arrData.add(item);
                            }
                        } else {
                            arrData.add(item);
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public double getReturnTotalAmt(String orderNo) {
        double amt = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_HEADER + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        amt += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return amt;
    }

    public double getReturnRequestTotalAmt(String orderNo) {
        double amt = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_REQUEST_HEADER + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        amt += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return amt;
    }


    public double getOrderTotalAmt(String orderNo) {
        double amt = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ORDER_HEADER + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        amt += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return amt;
    }

    public double getLoadTotalAmt(String orderNo) {
        double amt = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_REQUEST_HEADER + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        amt += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ORDER_AMOUNT)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return amt;
    }


    public ArrayList<Item> getBadReturnItems(String type) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_RETURN_ITEMS + " WHERE " +
                    RETURN_TYPE + "=?", new String[]{type});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleBaseQty("0");
                        item.setSaleAltQty("0");
                        item.setReasonCode("0");
                        item.setIsCheck("0");

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            int position = 0;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                    position = i;
                                    isContain = true;
                                    break;
                                }
                            }

                            if (isContain) {
                                Item containItem = arrData.get(position);
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    int bsQty = Integer.parseInt(containItem.getBaseUOMQty()) + Integer.parseInt(item.getBaseUOMQty());
                                    containItem.setBaseUOMQty("" + bsQty);
                                } else {
                                    int asQty = Integer.parseInt(containItem.getAlterUOMQty()) + Integer.parseInt(item.getAlterUOMQty());
                                    containItem.setAlterUOMQty("" + asQty);
                                }
                                arrData.set(position, containItem);
                            } else {
                                arrData.add(item);
                            }
                        } else {
                            arrData.add(item);
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }


    public ArrayList<Item> getOrderItems(String orderNo) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ORDER_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleAltPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleBasePrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));

                        if (cursor.getString(cursor.getColumnIndex(IS_FREE_ITEM)) != null) {
                            if (cursor.getString(cursor.getColumnIndex(IS_FREE_ITEM)).equals("1")) {
                                if (arrData.size() > 0) {
                                    boolean isContain = false;
                                    int position = 0;
                                    for (int i = 0; i < arrData.size(); i++) {
                                        if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                            position = i;
                                            isContain = true;
                                            break;
                                        }
                                    }

                                    if (isContain) {
                                        Item containItem = arrData.get(position);
                                        if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                            containItem.setBaseUOMQty(item.getBaseUOMQty());
                                            containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                            containItem.setBaseUOM(item.getBaseUOM());
                                            containItem.setBaseUOMName(item.getBaseUOMName());
                                            containItem.setSaleBasePrice(item.getSaleBasePrice());
                                        } else {
                                            containItem.setAlterUOMQty(item.getAlterUOMQty());
                                            containItem.setAlterUOMPrice(item.getAlterUOMPrice());
                                            containItem.setAltrUOM(item.getAltrUOM());
                                            containItem.setAlterUOMName(item.getAlterUOMName());
                                            containItem.setSaleAltPrice(item.getSaleAltPrice());
                                        }
                                        arrData.set(position, containItem);
                                    } else {
                                        arrData.add(item);
                                    }
                                } else {
                                    arrData.add(item);
                                }
                            } else {
                                arrData.add(item);
                            }
                        } else {
                            if (arrData.size() > 0) {
                                boolean isContain = false;
                                int position = 0;
                                for (int i = 0; i < arrData.size(); i++) {
                                    if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                        position = i;
                                        isContain = true;
                                        break;
                                    }
                                }

                                if (isContain) {
                                    Item containItem = arrData.get(position);
                                    if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                        containItem.setBaseUOMQty(item.getBaseUOMQty());
                                        containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                        containItem.setBaseUOM(item.getBaseUOM());
                                        containItem.setBaseUOMName(item.getBaseUOMName());
                                        containItem.setSaleBasePrice(item.getSaleBasePrice());
                                    } else {
                                        containItem.setAlterUOMQty(item.getAlterUOMQty());
                                        containItem.setAlterUOMPrice(item.getAlterUOMPrice());
                                        containItem.setAltrUOM(item.getAltrUOM());
                                        containItem.setAlterUOMName(item.getAlterUOMName());
                                        containItem.setSaleAltPrice(item.getSaleAltPrice());
                                    }
                                    arrData.set(position, containItem);
                                } else {
                                    arrData.add(item);
                                }
                            } else {
                                arrData.add(item);
                            }
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<Delivery> getAllDelivery(String customerId) {
        ArrayList<Delivery> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_HEADER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Delivery mDelivery = new Delivery();
                        mDelivery.setOrderId(cursor.getString(cursor.getColumnIndex(DELIVERY_ID)));
                        mDelivery.setOrderNo(cursor.getString(cursor.getColumnIndex(ORDER_NO)));
                        mDelivery.setOrderDate(cursor.getString(cursor.getColumnIndex(ORDER_DATE)));
                        mDelivery.setIsDelete(cursor.getString(cursor.getColumnIndex(IS_DELETE)));

                        arrData.add(mDelivery);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }

    public ArrayList<Delivery> getAllDeliveryList() {
        ArrayList<Delivery> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_ACCEPT_HEADER, null);

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Delivery mDelivery = new Delivery();
                        mDelivery.setOrderId(cursor.getString(cursor.getColumnIndex(DELIVERY_ID)));
                        mDelivery.setOrderNo(cursor.getString(cursor.getColumnIndex(ORDER_NO)));
                        mDelivery.setIsDelete(cursor.getString(cursor.getColumnIndex(IS_DELETE)));
                        mDelivery.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));

                        arrData.add(mDelivery);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }

    //Id DEport
    public Customer getDepotCustomerDetail(String customerId) {
        Customer item = new Customer();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId}, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerCode(cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setCustPhone(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setCustomerphone1(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE_OTHER)));
                        item.setAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        item.setCustType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPE)));
                        if (cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)).equalsIgnoreCase("")) {
                            item.setPaymentTerm("0");
                        } else {
                            item.setPaymentTerm(cursor.getString(cursor.getColumnIndex(CUSTOMER_PAYMENT_TERM)));
                        }
                        item.setCreditLimit(cursor.getString(cursor.getColumnIndex(CUSTOMER_CREDIT_LIMIT)));
                        item.setBalance(cursor.getString(cursor.getColumnIndex(CUSTOMER_BALANCE)));
                        item.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        item.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        item.setRadius(cursor.getString(cursor.getColumnIndex(CUSTOMER_RADIUS)));
                        item.setIsSale(cursor.getString(cursor.getColumnIndex(CUST_SALE)));
                        item.setIsColl(cursor.getString(cursor.getColumnIndex(CUST_COLL)));
                        item.setIsOrder(cursor.getString(cursor.getColumnIndex(CUST_ORDER)));
                        item.setIsReturn(cursor.getString(cursor.getColumnIndex(CUST_RETURN)));
                        item.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                        item.setVisibility(cursor.getString(cursor.getColumnIndex(CUSTOMER_VISIABLE)));
                        item.setCustomerType(cursor.getString(cursor.getColumnIndex(CUSTOMER_TYPEID)));
                        item.setCustomerCategory(cursor.getString(cursor.getColumnIndex(CUSTOMER_CATEGORYID)));
                        item.setSalesmanId(cursor.getString(cursor.getColumnIndex(SALESMAN_ID)));
                        item.setIsNew(cursor.getString(cursor.getColumnIndex(IS_NEW)));
                        item.setRouteId(cursor.getString(cursor.getColumnIndex(ROUTE_ID)));
                        item.setIs_fridge_assign(cursor.getString(cursor.getColumnIndex(IS_FREEZE_ASSIGN)));
                        item.setFridger_code(cursor.getString(cursor.getColumnIndex(FRIDGER_CODE)));
                        item.setAddress2(cursor.getString((cursor.getColumnIndex(ADDRESS_TWO))));
                        item.setCustomerstate(cursor.getString(cursor.getColumnIndex(CUST_STATE)));
                        item.setCustomercity(cursor.getString(cursor.getColumnIndex(CUST_CITY)));
                        item.setCustomerzip(cursor.getString(cursor.getColumnIndex(CUST_ZIP)));
                        item.setMonSqu(cursor.getString(cursor.getColumnIndex(MON_SEQ)));
                        item.setTueSqu(cursor.getString(cursor.getColumnIndex(TUE_SEQ)));
                        item.setWedSqu(cursor.getString(cursor.getColumnIndex(WED_SEQ)));
                        item.setThuSqu(cursor.getString(cursor.getColumnIndex(THU_SEQ)));
                        item.setFriSqu(cursor.getString(cursor.getColumnIndex(FRI_SEQ)));
                        item.setSatSqu(cursor.getString(cursor.getColumnIndex(SAT_SEQ)));
                        item.setSunSqu(cursor.getString(cursor.getColumnIndex(SUN_SEQ)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    public ArrayList<DepotData> getAllDepot() {
        ArrayList<DepotData> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT, null);

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        DepotData mDepot = new DepotData();
                        mDepot.setDepotId(cursor.getString(cursor.getColumnIndex(DEPOT_ID)));
                        mDepot.setDepotName(cursor.getString(cursor.getColumnIndex(DEPOT_NAME)));
                        mDepot.setAgentId(cursor.getString(cursor.getColumnIndex(AGENT_ID)));
                        mDepot.setAgentName(cursor.getString(cursor.getColumnIndex(AGENT_NAME)));

                        arrData.add(mDepot);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }


    public ArrayList<Item> getDeliveryItems(String orderNo) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setSaleBaseQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setSaleBasePrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setSaleAltQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setSaleAltPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            int position = 0;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                    position = i;
                                    isContain = true;
                                    break;
                                }
                            }

                            if (isContain) {
                                Item containItem = arrData.get(position);
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    containItem.setSaleBaseQty(item.getSaleBaseQty());
                                    containItem.setSaleBasePrice(item.getSaleBasePrice());
                                    containItem.setBaseUOMQty(item.getSaleBaseQty());
                                    containItem.setBaseUOMPrice(item.getSaleBasePrice());
                                    containItem.setBaseUOM(item.getBaseUOM());
                                    containItem.setBaseUOMName(item.getBaseUOMName());
                                    int selQty = Integer.parseInt(containItem.getSaleQty()) + Integer.parseInt(item.getSaleBaseQty());
                                    double price = Double.parseDouble(containItem.getPrice()) + Double.parseDouble(item.getSaleBasePrice());
                                    containItem.setSaleQty("" + selQty);
                                    containItem.setPrice("" + price);
                                } else {
                                    containItem.setSaleAltQty(item.getSaleAltQty());
                                    containItem.setSaleAltPrice(item.getSaleAltPrice());
                                    containItem.setAlterUOMQty(item.getSaleAltQty());
                                    containItem.setAlterUOMPrice(item.getSaleAltPrice());
                                    containItem.setAltrUOM(item.getAltrUOM());
                                    containItem.setAlterUOMName(item.getAlterUOMName());
                                    int UPC = Integer.parseInt(getItemUPC(item.getItemId()));
                                    int altrQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY))) * UPC;
                                    int selQty = Integer.parseInt(containItem.getSaleQty()) + altrQty;
                                    double price = Double.parseDouble(containItem.getPrice()) + Double.parseDouble(item.getSaleAltPrice());
                                    containItem.setSaleQty("" + selQty);
                                    containItem.setPrice("" + price);
                                }
                                arrData.set(position, containItem);
                            } else {
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                                    item.setSaleQty("" + selQty);
                                    item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                                } else {
                                    int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                                    int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY))) * UPC;
                                    item.setSaleQty("" + selQty);
                                    item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                                }
                                arrData.add(item);
                            }
                        } else {
                            if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                                item.setSaleQty("" + selQty);
                                item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                            } else {
                                int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                                int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY))) * UPC;
                                item.setSaleQty("" + selQty);
                                item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                            }
                            arrData.add(item);
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<Item> getDeliveryAcceptItems(String orderNo) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_ACCEPT_ITEMS + " WHERE " +
                    DELIVERY_ID + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setSaleBaseQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setSaleBasePrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setSaleAltQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setSaleAltPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            int position = 0;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                    position = i;
                                    isContain = true;
                                    break;
                                }
                            }

                            if (isContain) {
                                Item containItem = arrData.get(position);
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    containItem.setSaleBaseQty(item.getSaleBaseQty());
                                    containItem.setSaleBasePrice(item.getSaleBasePrice());
                                    containItem.setBaseUOMQty(item.getSaleBaseQty());
                                    containItem.setBaseUOMPrice(item.getSaleBasePrice());
                                    containItem.setBaseUOM(item.getBaseUOM());
                                    containItem.setBaseUOMName(item.getBaseUOMName());
                                    int selQty = Integer.parseInt(containItem.getSaleQty()) + Integer.parseInt(item.getSaleBaseQty());
                                    double price = Double.parseDouble(containItem.getPrice()) + Double.parseDouble(item.getSaleBasePrice());
                                    containItem.setSaleQty("" + selQty);
                                    containItem.setPrice("" + price);
                                } else {
                                    containItem.setSaleAltQty(item.getSaleAltQty());
                                    containItem.setSaleAltPrice(item.getSaleAltPrice());
                                    containItem.setAlterUOMQty(item.getSaleAltQty());
                                    containItem.setAlterUOMPrice(item.getSaleAltPrice());
                                    containItem.setAltrUOM(item.getAltrUOM());
                                    containItem.setAlterUOMName(item.getAlterUOMName());
                                    int UPC = Integer.parseInt(getItemUPC(item.getItemId()));
                                    int altrQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY))) * UPC;
                                    int selQty = Integer.parseInt(containItem.getSaleQty()) + altrQty;
                                    double price = Double.parseDouble(containItem.getPrice()) + Double.parseDouble(item.getSaleAltPrice());
                                    containItem.setSaleQty("" + selQty);
                                    containItem.setPrice("" + price);
                                }
                                arrData.set(position, containItem);
                            } else {
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                                    item.setSaleQty("" + selQty);
                                    item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                                } else {
                                    int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                                    int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY))) * UPC;
                                    item.setSaleQty("" + selQty);
                                    item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                                }
                                arrData.add(item);
                            }
                        } else {
                            if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                                item.setSaleQty("" + selQty);
                                item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                            } else {
                                int UPC = Integer.parseInt(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                                int selQty = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY))) * UPC;
                                item.setSaleQty("" + selQty);
                                item.setPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                            }
                            arrData.add(item);
                        }
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public double getDeliveryTotalAmt(String orderNo) {
        double amt = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                            amt += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        } else {
                            amt += Double.parseDouble(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return amt;
    }


    public ArrayList<Item> getSalesmanLoadRequestItems(String orderNo) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SALESMAN_LOAD_REQUEST_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleAltPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleBasePrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            int position = 0;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                    position = i;
                                    isContain = true;
                                    break;
                                }
                            }

                            if (isContain) {
                                Item containItem = arrData.get(position);
                                if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                    containItem.setBaseUOMQty(item.getBaseUOMQty());
                                    containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                    containItem.setBaseUOM(item.getBaseUOM());
                                    containItem.setBaseUOMName(item.getBaseUOMName());
                                    containItem.setSaleBasePrice(item.getSaleBasePrice());
                                } else {
                                    containItem.setAlterUOMQty(item.getAlterUOMQty());
                                    containItem.setAlterUOMPrice(item.getAlterUOMPrice());
                                    containItem.setAltrUOM(item.getAltrUOM());
                                    containItem.setAlterUOMName(item.getAlterUOMName());
                                    containItem.setSaleAltPrice(item.getSaleAltPrice());
                                }
                                arrData.set(position, containItem);
                            } else {
                                arrData.add(item);
                            }
                        } else {
                            arrData.add(item);
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get Load Request Items
    public ArrayList<Item> getLoadRequestItems(String orderNo) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_LOAD_REQUEST_ITEMS + " WHERE " +
                    ORDER_NO + "=?", new String[]{orderNo});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleAltPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM_PRCE)));
                        item.setSaleBasePrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));

                        if (cursor.getString(cursor.getColumnIndex(IS_FREE_ITEM)) != null) {
                            if (cursor.getString(cursor.getColumnIndex(IS_FREE_ITEM)).equals("1")) {
                                arrData.add(item);
                            } else {

                                if (arrData.size() > 0) {
                                    boolean isContain = false;
                                    int position = 0;
                                    for (int i = 0; i < arrData.size(); i++) {
                                        if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                            position = i;
                                            isContain = true;
                                            break;
                                        }
                                    }

                                    if (isContain) {
                                        Item containItem = arrData.get(position);
                                        if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                            containItem.setBaseUOMQty(item.getBaseUOMQty());
                                            containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                            containItem.setBaseUOM(item.getBaseUOM());
                                            containItem.setBaseUOMName(item.getBaseUOMName());
                                            containItem.setSaleBasePrice(item.getSaleBasePrice());
                                        } else {
                                            containItem.setAlterUOMQty(item.getAlterUOMQty());
                                            containItem.setAlterUOMPrice(item.getAlterUOMPrice());
                                            containItem.setAltrUOM(item.getAltrUOM());
                                            containItem.setAlterUOMName(item.getAlterUOMName());
                                            containItem.setSaleAltPrice(item.getSaleAltPrice());
                                        }
                                        arrData.set(position, containItem);
                                    } else {
                                        arrData.add(item);
                                    }
                                } else {
                                    arrData.add(item);
                                }
                            }
                        } else {
                            if (arrData.size() > 0) {
                                boolean isContain = false;
                                int position = 0;
                                for (int i = 0; i < arrData.size(); i++) {
                                    if (arrData.get(i).getItemId().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(ITEM_ID)))) {
                                        position = i;
                                        isContain = true;
                                        break;
                                    }
                                }

                                if (isContain) {
                                    Item containItem = arrData.get(position);
                                    if (cursor.getString(cursor.getColumnIndex(ITEM_UOM_TYPE)).equalsIgnoreCase("Base")) {
                                        containItem.setBaseUOMQty(item.getBaseUOMQty());
                                        containItem.setBaseUOMPrice(item.getBaseUOMPrice());
                                        containItem.setBaseUOM(item.getBaseUOM());
                                        containItem.setBaseUOMName(item.getBaseUOMName());
                                        containItem.setSaleBasePrice(item.getSaleBasePrice());
                                    } else {
                                        containItem.setAlterUOMQty(item.getAlterUOMQty());
                                        containItem.setAlterUOMPrice(item.getAlterUOMPrice());
                                        containItem.setAltrUOM(item.getAltrUOM());
                                        containItem.setAlterUOMName(item.getAlterUOMName());
                                        containItem.setSaleAltPrice(item.getSaleAltPrice());
                                    }
                                    arrData.set(position, containItem);
                                } else {
                                    arrData.add(item);
                                }
                            } else {
                                arrData.add(item);
                            }
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }


    //get All Transaction from Customer
    public ArrayList<Transaction> getAllTransactionsForCustomer(String customerCode) {
        ArrayList<Transaction> list = new ArrayList<Transaction>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION +
                " WHERE " + TR_CUSTOMER_NUM + " =?";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{customerCode});

            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Transaction transaction = new Transaction();
                        //only one column
                        transaction.tr_type = cursor.getString(cursor.getColumnIndex(TR_TYPE));
                        transaction.tr_date_time = cursor.getString(cursor.getColumnIndex(TR_DATE));
                        transaction.tr_salesman_id = cursor.getString(cursor.getColumnIndex(TR_SALESMAN_ID));
                        transaction.tr_customer_name = cursor.getString(cursor.getColumnIndex(TR_CUSTOMER_NAME));
                        transaction.tr_customer_num = cursor.getString(cursor.getColumnIndex(TR_CUSTOMER_NUM));
                        transaction.tr_collection_id = cursor.getString(cursor.getColumnIndex(TR_COLLECTION_ID));
                        transaction.tr_order_id = cursor.getString(cursor.getColumnIndex(TR_ORDER_ID));
                        transaction.tr_invoice_id = cursor.getString(cursor.getColumnIndex(TR_INVOICE_ID));
                        transaction.tr_pyament_id = cursor.getString(cursor.getColumnIndex(TR_PYAMENT_ID));
                        transaction.tr_is_posted = cursor.getString(cursor.getColumnIndex(TR_IS_POSTED));
                        transaction.tr_printData = cursor.getString(cursor.getColumnIndex(KEY_DATA));

                        //you could add additional columns here..
                        list.add(transaction);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Push Notification
    public ArrayList<Notification> getAllNotification() {
        ArrayList<Notification> list = new ArrayList<Notification>();


        String selectQuery = "SELECT * FROM " + TABLE_PUSH_NOTIFICATION + " ORDER BY " + UNIQUE_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery(selectQuery, null);

            try {
                if (cursorInvo.moveToFirst()) {
                    do {

                        Notification mNotification = new Notification();
                        mNotification.setId(cursorInvo.getString(cursorInvo.getColumnIndex(CUSTOMER_ID)));
                        mNotification.setOrderId(cursorInvo.getString(cursorInvo.getColumnIndex(ORDER_NO)));
                        mNotification.setType(cursorInvo.getString(cursorInvo.getColumnIndex(TYPE)));
                        mNotification.setTitle(cursorInvo.getString(cursorInvo.getColumnIndex(TITLE)));
                        mNotification.setMessage(cursorInvo.getString(cursorInvo.getColumnIndex(MESSAGE)));
                        Date mDate = UtilApp.getDateTimeStamp(cursorInvo.getString(cursorInvo.getColumnIndex(DATE_TIME)));
                        String time = TimeAgo.getTimeAgo(mDate.getTime());
                        mNotification.setDateTime(time);
                        mNotification.setIsRead(cursorInvo.getString(cursorInvo.getColumnIndex(IS_READ)));

                        list.add(mNotification);

                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    public ArrayList<CollectionData> getCollectionCustomer(String customerId) {
        ArrayList<CollectionData> list = new ArrayList<CollectionData>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor c = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});
            try {

                // looping through all rows and adding to list
                if (c.moveToFirst()) {

                    do {
                        int indicator = 1;
                        String invoiceAmount = "";

                        CollectionData collection = new CollectionData();
                        collection.setInvoiceNo(c.getString(c.getColumnIndex(KEY_INVOICE_NO)));
                        collection.setOrderId(c.getString(c.getColumnIndex(KEY_ORDER_ID)));
                        collection.setInvoiceDate(c.getString(c.getColumnIndex(KEY_INVOICE_DATE)));
                        collection.setIndicator(c.getString(c.getColumnIndex(KEY_INDICATOR)));
                        indicator = c.getString(c.getColumnIndex(KEY_INDICATOR)).equals(App.ADD_INDICATOR) ? indicator : indicator * -1;
                        invoiceAmount = c.getString(c.getColumnIndex(KEY_INVOICE_AMOUNT)).equals("") ? "0" : c.getString(c.getColumnIndex(KEY_INVOICE_AMOUNT));
                        collection.setTempAmountDue(String.valueOf(Double.parseDouble(invoiceAmount) * indicator));
                        collection.setAmountCleared(c.getString(c.getColumnIndex(KEY_AMOUNT_CLEARED)).equals("") ? "0" : c.getString(c.getColumnIndex(KEY_AMOUNT_CLEARED)));
                        collection.setInvoiceDueDate(c.getString(c.getColumnIndex(KEY_INDICATOR)));
                        collection.setInvoiceSplite(false);
                        collection.setSpliteChecked(false);
                        collection.setAmountEnter("0");
                        list.add(collection);
                    } while (c.moveToNext());
                }
            } finally {
                try {
                    c.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Customer Collection
    public ArrayList<CollectionData> getCustomerCollection(String customerCode) {
        ArrayList<CollectionData> list = new ArrayList<CollectionData>();


        //String selectQuery = "SELECT * FROM " + TABLE_COLLECTION + " WHERE u_id IN (SELECT MAX(u_id) FROM " + TABLE_COLLECTION + " WHERE " + CUSTOMER_ID + " = '" + customerCode + "'" + " GROUP BY sv_code)";


        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerCode});

            try {

                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    do {

                        int indicator = 1;
                        String invoiceAmount = "";
                        CollectionData collection = new CollectionData();

                        collection.setCustomerNo(cursorInvo.getString(cursorInvo.getColumnIndex(CUSTOMER_ID)));
                        collection.setInvoiceNo(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_NO)));
                        collection.setOrderId(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_ORDER_ID)));
                        collection.setInvoiceAmount(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT)));
                        collection.setIndicator(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INDICATOR)));
                        indicator = cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INDICATOR)).equals(App.ADD_INDICATOR) ? indicator : indicator * -1;
                        invoiceAmount = cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT)).equals("") ? "0" : cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT));
                        collection.setTempAmountDue(String.valueOf(Double.parseDouble(invoiceAmount) * indicator));
                        collection.setInvoiceDueDate(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_DUE_DATE)));
                        collection.setInvoiceDate(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_DATE)));
                        collection.setAmountPay(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_PAY)));
                        collection.setAmountCleared(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)).equals("") ? "0" : cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)));
                        collection.setIsInvoiceComplete(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_IS_INVOICE_COMPLETE)));
                        collection.setInvoiceSplite(false);
                        collection.setSpliteChecked(false);
                        collection.setAmountEnter("0");
                        collection.setIsOutStand(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_IS_OUTSTANDING)));

                        //you could add additional columns here..
                        list.add(collection);

                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Customer Collection
    public ArrayList<CollectionData> getAllCollection(String customerCode) {

        ArrayList<CollectionData> list = new ArrayList<CollectionData>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerCode});

            try {

                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    do {
                        int indicator = 1;
                        String invoiceAmount = "";
                        CollectionData collection = new CollectionData();

                        collection.setCustomerNo(cursorInvo.getString(cursorInvo.getColumnIndex(CUSTOMER_ID)));
                        collection.setInvoiceNo(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_NO)));
                        collection.setOrderId(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_ORDER_ID)));
                        collection.setInvoiceAmount(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT)));
                        collection.setIndicator(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INDICATOR)));
                        indicator = cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INDICATOR)).equals(App.ADD_INDICATOR) ? indicator : indicator * -1;
                        invoiceAmount = cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT)).equals("") ? "0" : cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT));
                        collection.setTempAmountDue(String.valueOf(Double.parseDouble(invoiceAmount) * indicator));
                        collection.setInvoiceDueDate(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_DUE_DATE)));
                        collection.setInvoiceDate(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_DATE)));
                        collection.setAmountCleared(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)).equals("") ? "0" : cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)));
                        collection.setIsInvoiceComplete(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_IS_INVOICE_COMPLETE)));
                        collection.setInvoiceSplite(false);
                        collection.setSpliteChecked(false);
                        collection.setAmountEnter("0");
                        collection.setIsOutStand(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_IS_OUTSTANDING)));

//                        double due_amt = Double.parseDouble(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT))) -
//                                Double.parseDouble(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)));
//                        collection.setAmountPending("" + due_amt);

                        //you could add additional columns here..
                        list.add(collection);

                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Discount Item
    public ArrayList<DiscountMaster> getAllItemDiscount() {

        ArrayList<DiscountMaster> list = new ArrayList<DiscountMaster>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM_DISCOUNT, null);

            try {
                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    do {

                        DiscountMaster mData = new DiscountMaster();
                        mData.setItemId(cursorInvo.getString(cursorInvo.getColumnIndex(ITEM_ID)));
                        mData.setItemCode(getItemCode(cursorInvo.getString(cursorInvo.getColumnIndex(ITEM_ID))));
                        mData.setItemName(getItemName(cursorInvo.getString(cursorInvo.getColumnIndex(ITEM_ID))));
                        mData.setUOM(getUOM(cursorInvo.getString(cursorInvo.getColumnIndex(UOM_ID))));
                        String type = cursorInvo.getString(cursorInvo.getColumnIndex(TYPE));
                        if (type.equalsIgnoreCase("1")) {
                            mData.setType("Percent");
                        } else {
                            mData.setType("Fixed");
                        }

                        String discType = cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_TYPE));
                        if (discType.equalsIgnoreCase("1")) {
                            mData.setDiscountType("Sales");
                        } else {
                            mData.setDiscountType("Qty");
                        }
                        mData.setDiscountValue(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT)));
                        mData.setDiscountQty(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_QTY)));
                        mData.setDiscountSale(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_AMT)));
                        list.add(mData);

                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }


    //get Discount Item
    public ArrayList<DiscountMaster> getAllRouteItemDiscount() {

        ArrayList<DiscountMaster> list = new ArrayList<DiscountMaster>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_ROUTE_ITEM_DISCOUNT, null);

            try {
                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    do {

                        DiscountMaster mData = new DiscountMaster();
                        mData.setItemId(cursorInvo.getString(cursorInvo.getColumnIndex(ITEM_ID)));
                        mData.setItemCode(getItemCode(cursorInvo.getString(cursorInvo.getColumnIndex(ITEM_ID))));
                        mData.setItemName(getItemName(cursorInvo.getString(cursorInvo.getColumnIndex(ITEM_ID))));
                        mData.setUOM(getUOM(cursorInvo.getString(cursorInvo.getColumnIndex(UOM_ID))));
                        String type = cursorInvo.getString(cursorInvo.getColumnIndex(TYPE));
                        if (type.equalsIgnoreCase("1")) {
                            mData.setType("Percent");
                        } else {
                            mData.setType("Fixed");
                        }

                        String discType = cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_TYPE));
                        if (discType.equalsIgnoreCase("1")) {
                            mData.setDiscountType("Sales");
                        } else {
                            mData.setDiscountType("Qty");
                        }
                        mData.setDiscountValue(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT)));
                        mData.setDiscountQty(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_QTY)));
                        mData.setDiscountSale(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_AMT)));
                        list.add(mData);

                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Category Discount
    public ArrayList<DiscountMaster> getAllCategoryDiscount() {

        ArrayList<DiscountMaster> list = new ArrayList<DiscountMaster>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_ROUTE_CATEGORY_DISCOUNT, null);

            try {
                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    do {

                        DiscountMaster mData = new DiscountMaster();

                        //Get Discount Category
                        String categoryId = cursorInvo.getString(cursorInvo.getColumnIndex(CATEGORY_ID));
                        String[] spCat = categoryId.split(",");
                        String catName = "";
                        for (int k = 0; k < spCat.length; k++) {
                            String name = "";
                            if (spCat[k].equals("1")) {
                                name = "Biscuit";
                            } else if (spCat[k].equals("2")) {
                                name = "CSD";
                            } else if (spCat[k].equals("3")) {
                                name = "Water";
                            } else if (spCat[k].equals("4")) {
                                name = "Juice";
                            }

                            if (catName.equals("")) {
                                catName = name;
                            } else {
                                catName = catName + "," + name;
                            }
                        }

                        mData.setCategory(catName);
                        String type = cursorInvo.getString(cursorInvo.getColumnIndex(TYPE));
                        if (type.equalsIgnoreCase("1")) {
                            mData.setType("Percent");
                        } else {
                            mData.setType("Fixed");
                        }

                        String discType = cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_TYPE));
                        if (discType.equalsIgnoreCase("1")) {
                            mData.setDiscountType("Sales");
                        } else {
                            mData.setDiscountType("Qty");
                        }
                        mData.setDiscountValue(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT)));
                        mData.setDiscountQty(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_QTY)));
                        mData.setDiscountSale(cursorInvo.getString(cursorInvo.getColumnIndex(DISCOUNT_AMT)));
                        list.add(mData);

                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Customer Collection
    public CollectionData getCollectionDetail(String customerCode, String invoiceNo) {

        CollectionData collection = null;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + KEY_INVOICE_NO + "=?", new String[]{customerCode, invoiceNo});

            try {

                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    collection = new CollectionData();

                    collection.setOrderId(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_ORDER_ID)));
                    collection.setAmountCleared(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)));
                    collection.setCashAmt(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CASH_AMOUNT)));
                    collection.setChequeAmt(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_AMOUNT)));
                    collection.setChequeAmtIndividule(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_AMOUNT_INDIVIDUAL)));
                    collection.setChequeDat(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_DATE)));
                    collection.setChequeNo(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_NUMBER)));
                    collection.setBankCode(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_BANK_CODE)));
                    collection.setBankName(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_BANK_NAME)));
                    collection.setIsOutStand(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_IS_OUTSTANDING)));
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return collection;
    }

    public void updateCollection(CollectionData mColl, String invNo, String custId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(KEY_ORDER_ID, mColl.getOrderId());
        contentValue.put(CUSTOMER_ID, mColl.getCustomerNo());
        contentValue.put(CUSTOMER_TYPE, getCustomerType(mColl.getCustomerNo()));
        contentValue.put(KEY_COLLECTION_TYPE, mColl.getCollType());
        contentValue.put(KEY_INVOICE_NO, mColl.getInvoiceNo());
        contentValue.put(KEY_INVOICE_AMOUNT, mColl.getInvoiceAmount());
        contentValue.put(KEY_INDICATOR, App.ADD_INDICATOR);
        contentValue.put(KEY_AMOUNT_PAY, mColl.getAmountPay());
        contentValue.put(KEY_AMOUNT_CLEARED, mColl.getAmountCleared());
        contentValue.put(KEY_CHEQUE_AMOUNT, mColl.getChequeAmt());
        contentValue.put(KEY_CHEQUE_AMOUNT_INDIVIDUAL, mColl.getChequeAmtIndividule());
        contentValue.put(KEY_CHEQUE_AMOUNTPRE, mColl.getChequeAmtPre());
        contentValue.put(KEY_CHEQUE_NUMBER, mColl.getChequeNo());
        contentValue.put(KEY_CHEQUE_DATE, mColl.getChequeDat());
        contentValue.put(KEY_CHEQUE_BANK_CODE, mColl.getBankCode());
        contentValue.put(KEY_CHEQUE_BANK_NAME, mColl.getBankName());
        contentValue.put(KEY_CASH_AMOUNT, mColl.getCashAmt());
        contentValue.put(KEY_CASH_AMOUNTPRE, mColl.getCashAmtPre());
        contentValue.put(KEY_IS_INVOICE_COMPLETE, mColl.getIsInvoiceComplete());
        contentValue.put(KEY_IS_OUTSTANDING, mColl.getIsOutStand());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.update(TABLE_COLLECTION, contentValue, KEY_INVOICE_NO + " = ? AND " +
                CUSTOMER_ID + " = ?", new String[]{invNo, custId});

    }

    //get Customer Collection
    public ArrayList<CollectionData> getCashCollection() {
        ArrayList<CollectionData> list = new ArrayList<CollectionData>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION, null);

            try {

                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    do {

                        CollectionData collection = new CollectionData();
                        int indicator = 1;
                        String invoiceAmount = "";
                        collection.setCustomerNo(cursorInvo.getString(cursorInvo.getColumnIndex(CUSTOMER_ID)));
                        collection.setInvoiceNo(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_NO)));
                        collection.setOrderId(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_ORDER_ID)));
                        collection.setInvoiceAmount(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT)));
                        collection.setIndicator(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INDICATOR)));
                        indicator = cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INDICATOR)).equals(App.ADD_INDICATOR) ? indicator : indicator * -1;
                        invoiceAmount = cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT)).equals("") ? "0" : cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_AMOUNT));
                        collection.setTempAmountDue(String.valueOf(Double.parseDouble(invoiceAmount) * indicator));
                        collection.setInvoiceDueDate(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_DUE_DATE)));
                        collection.setInvoiceDate(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_INVOICE_DATE)));
                        collection.setAmountCleared(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)).equals("") ? "0" : cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_CLEARED)));
                        collection.setIsInvoiceComplete(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_IS_INVOICE_COMPLETE)));
                        collection.setCashAmt(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CASH_AMOUNTPRE)));
                        collection.setChequeAmt(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_AMOUNTPRE)));
                        collection.setChequeDat(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_DATE)));
                        collection.setChequeNo(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_CHEQUE_NUMBER)));
                        collection.setInvoiceSplite(false);
                        collection.setSpliteChecked(false);
                        collection.setAmountEnter("0");

                        //you could add additional columns here..
                        if (!cursorInvo.getString(cursorInvo.getColumnIndex(KEY_COLLECTION_TYPE)).equals("")) {
                            list.add(collection);
                        }
                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Sales Summary
    public ArrayList<SalesSummary> getSalesSummary() {
        ArrayList<SalesSummary> list = new ArrayList<SalesSummary>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SALE_SUMMARY, null);

            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {

                        SalesSummary mSummary = new SalesSummary();
                        //only one column
                        mSummary.setTransactionNo(cursor.getString(cursor.getColumnIndex(SVH_CODE)));
                        mSummary.setTransactionType(cursor.getString(cursor.getColumnIndex(SUMMARY_TYPE)));
                        mSummary.setCustomerNo(cursor.getString(cursor.getColumnIndex(SVH_CUST_CODE)));
                        mSummary.setCustomerName(cursor.getString(cursor.getColumnIndex(SVH_CUST_NAME)));
                        mSummary.setDiscounts(cursor.getString(cursor.getColumnIndex(DISCOUNT_AMT)));
                        mSummary.setTotalSales(cursor.getString(cursor.getColumnIndex(SVH_TOT_AMT_SALES)));

                        //you could add additional columns here..
                        list.add(mSummary);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return list;
    }

    //get Total Collection
    public double getTotalCollection() {

        double amtCollect = 0;

        //String selectQuery = "SELECT * FROM " + TABLE_COLLECTION + " WHERE u_id IN (SELECT MAX(u_id) FROM " + TABLE_COLLECTION + " GROUP BY sv_code)";


        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursorInvo = db.rawQuery("SELECT * FROM " +
                    TABLE_COLLECTION, null);

            try {

                // looping through all rows and adding to list
                if (cursorInvo.moveToFirst()) {
                    do {
                        double amt = Double.parseDouble(cursorInvo.getString(cursorInvo.getColumnIndex(KEY_AMOUNT_PAY)));
                        amtCollect += amt;

                    } while (cursorInvo.moveToNext());
                }
            } finally {
                try {
                    cursorInvo.close();
                } catch (Exception ignore) {
                }
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return amtCollect;
    }


    //Get Vanstock Items
    public ArrayList<Item> getVanStockItemList(String catId) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    ITEM_CATEGORY + "=?", new String[]{catId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_PRICE)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setSaleQty("0");
                        item.setSaleAltQty("0");
                        item.setSaleBaseQty("0");
                        item.setPrice("");
                        item.setDiscountAlAmt("0");
                        item.setDiscountAmt("0");
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get Vanstock Items
    public Item getItemVanStock(String itemId) {

        Item item = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {

                    item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                    item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));

                } else {
                    item.setBaseUOMQty("0");
                    item.setAlterUOMQty("0");
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    //Get Items from Category
    public ArrayList<Item> getInventoryItemListByCategory(String catId, String custId) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_INVENTORY_ITEMS + " WHERE " +
                    ITEM_CATEGORY + "=?" + " AND "
                    + CUSTOMER_ID + "=?", new String[]{catId, custId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(getItemCode(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setItemName(getItemName(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Base"));
                        item.setBaseUOMName(getUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Base")));
                        item.setBaseUOMQty("0");
                        item.setAltrUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Alter"));
                        item.setAlterUOMName(getUOM(getItemUOM(cursor.getString(cursor.getColumnIndex(ITEM_ID)), "Alter")));
                        item.setAlterUOMQty("0");
                        item.setAlterUOMPrice(getItemAlter(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setBaseUOMPrice(getItemBase(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setUpc(getItemUPC(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setAgentExcise(getItemAgentExcies(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setDirectsellexcise(getItemDirectExcies(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setSaleQty("0");
                        item.setSaleAltQty("0");
                        item.setSaleBaseQty("0");
                        item.setDiscountAmt("0");
                        item.setDiscountAlAmt("0");
                        item.setQty("0");
                        item.setPrice("0");
                        item.setIsFOCItem("");
                        item.setPerentItemId("");
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }


    //Get Items from Category
    public ArrayList<Item> getItemListByCategory(String catId) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_CATEGORY + "=?", new String[]{catId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty("0");
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty("0");
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_PRICE)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_UOM_PRICE)));
                        item.setUpc(cursor.getString(cursor.getColumnIndex(ITEM_UPC)));
                        item.setAgentExcise(cursor.getString(cursor.getColumnIndex(ITEM_AGENT_EXCISE)));
                        item.setDirectsellexcise(cursor.getString(cursor.getColumnIndex(ITEM_DIRECT_EXCISE)));
                        item.setSaleQty("0");
                        item.setSaleAltQty("0");
                        item.setSaleBaseQty("0");
                        item.setDiscountAmt("0");
                        item.setDiscountAlAmt("0");
                        item.setQty("0");
                        item.setPrice("0");
                        item.setIsFOCItem("");
                        item.setPerentItemId("");
                        ArrayList<UOM> arrSeleectUom = new ArrayList<>();
                        item.setArrSelectUOM(arrSeleectUom);
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get Vanstock Item
    public Item getVanStockItem(String itemId) {
        Item item = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    ITEM_ID + "=?", new String[]{itemId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setAlterUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_PRICE)));
                        item.setBaseUOMPrice(cursor.getString(cursor.getColumnIndex(ITEM_BASE_PRICE)));
                        item.setSaleQty("0");
                        item.setSaleAltQty("0");
                        item.setSaleBaseQty("0");
                        item.setPrice("");
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    //Get Delivery Item
    public int getDeliveryStockItem(String itemId, String type) {
        int qty = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_ITEMS + " WHERE " +
                    ITEM_ID + "=?" + " AND "
                    + IS_DELETE + "=?", new String[]{itemId, "0"});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        if (type.equalsIgnoreCase("Base")) {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        } else {
                            qty += Integer.parseInt(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        }

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return qty;
    }


    //Get  Items
    public ArrayList<Item> getAllItemList() {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM, null);

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setUpc(cursor.getString(cursor.getColumnIndex(ITEM_UPC)));
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get  Items
    public ArrayList<PromotionData> getAllPromotionList() {
        ArrayList<PromotionData> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_FREE_GOODS, null);

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        PromotionData item = new PromotionData();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemName(getItemName(cursor.getString(cursor.getColumnIndex(ITEM_ID))));
                        item.setItemUOM(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_UOM))));
                        item.setItemQTY(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        item.setItemMaxQTY(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        item.setFocItemId(cursor.getString(cursor.getColumnIndex(FOC_ITEM_ID)));
                        item.setFocItemName(getItemName(cursor.getString(cursor.getColumnIndex(FOC_ITEM_ID))));
                        item.setFocItemUOM(getUOM(cursor.getString(cursor.getColumnIndex(FOC_ITEM_UOM))));
                        item.setFocItemQTY(cursor.getString(cursor.getColumnIndex(FOC_ITEM_QTY)));
                        String cusId = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
                        if (cusId != null) {
                            if (cusId.equals(Settings.getString(App.AGENTID))) {
                                item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                                item.setCustName(Settings.getString(App.DEPOTNAME));
                            } else {
                                item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                                item.setCustName(getCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID))));
                            }
                        } else {
                            item.setCustomerId("");
                            item.setCustName("");
                        }
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get Vanstock Items
    public ArrayList<Item> getAllVanStockItemList() {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    IS_DELETE + "=?", new String[]{"0"});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setSaleAltQty("0");
                        item.setSaleBaseQty("0");
                        item.setActuakBaseQty(cursor.getString(cursor.getColumnIndex(ACTUAL_BASE_QTY)));
                        item.setActualAltQty(cursor.getString(cursor.getColumnIndex(ACTUAL_ALTER_QTY)));
                        item.setIsCheck("0");
                        item.setReasonCode("0");
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get Vanstock Items
    public ArrayList<Item> getDeleteVanStockItemList() {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_VAN_STOCK_ITEMS + " WHERE " +
                    IS_DELETE + "=?", new String[]{"1"});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setBaseUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM))));
                        item.setBaseUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_BASE_UOM_QTY)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setAlterUOMName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM))));
                        item.setAlterUOMQty(cursor.getString(cursor.getColumnIndex(ITEM_ALTER_UOM_QTY)));
                        item.setSaleAltQty("0");
                        item.setSaleBaseQty("0");
                        item.setActuakBaseQty(cursor.getString(cursor.getColumnIndex(ACTUAL_BASE_QTY)));
                        item.setActualAltQty(cursor.getString(cursor.getColumnIndex(ACTUAL_ALTER_QTY)));
                        item.setIsCheck("0");
                        item.setReasonCode("0");
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get IsNew Cust
    public String isNewCustomer(String customerId) {
        String isNew = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{customerId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        isNew = cursor.getString(cursor.getColumnIndex(IS_NEW));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return isNew;
    }

    //Get Delivery Order
    public String getDeliveryOrder(String deliveryId) {
        String orderNo = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DELIVERY_HEADER + " WHERE " +
                    DELIVERY_ID + "=?", new String[]{deliveryId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        orderNo = cursor.getString(cursor.getColumnIndex(DELIVERY_ORDER_NO));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return orderNo;
    }


    public ArrayList<Compititor> getCompititorList() {
        ArrayList<Compititor> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPITITOR;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Compititor item = new Compititor();
                        item.setCompititorId(cursor.getString(cursor.getColumnIndex(COMPITITORID)));
                        item.setCompititorCompanyName(cursor.getString(cursor.getColumnIndex(COMPITITOR_COMPANY_NAME)));
                        item.setCompititor_brand(cursor.getString(cursor.getColumnIndex(COMPITITOR_BRAND)));
                        item.setCompititor_ItemName(cursor.getString(cursor.getColumnIndex(COMPITITOR_ITEM_NAME)));
                        item.setCOMPITITOR_Price(cursor.getString(cursor.getColumnIndex(COMPITITOR_PRICE)));
                        item.setCompititor_Promotion(cursor.getString(cursor.getColumnIndex(COMPITITOR_PROMOTION)));
                        item.setCompititor_Notes(cursor.getString(cursor.getColumnIndex(COMPITITOR_NOTES)));
                        item.setCompititor_Image1(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image1)));
                        item.setCompititor_Image2(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image2)));
                        item.setCompititor_Image3(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image3)));
                        item.setCompititor_Image4(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image4)));

                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //get compititor list
    public Compititor getCompititorListByID(String ID) {
        Compititor item = new Compititor();
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COMPITITOR + " WHERE " +
                    COMPITITORID + "=?", new String[]{ID});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        item.setCompititorId(cursor.getString(cursor.getColumnIndex(COMPITITORID)));
                        item.setCompititorCompanyName(cursor.getString(cursor.getColumnIndex(COMPITITOR_COMPANY_NAME)));
                        item.setCompititor_brand(cursor.getString(cursor.getColumnIndex(COMPITITOR_BRAND)));
                        item.setCompititor_ItemName(cursor.getString(cursor.getColumnIndex(COMPITITOR_ITEM_NAME)));
                        item.setCOMPITITOR_Price(cursor.getString(cursor.getColumnIndex(COMPITITOR_PRICE)));
                        item.setCompititor_Promotion(cursor.getString(cursor.getColumnIndex(COMPITITOR_PROMOTION)));
                        item.setCompititor_Notes(cursor.getString(cursor.getColumnIndex(COMPITITOR_NOTES)));
                        item.setCompititor_Image1(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image1)));
                        item.setCompititor_Image2(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image2)));
                        item.setCompititor_Image3(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image3)));
                        item.setCompititor_Image4(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image4)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    //INSERT Compititor Accountablity
    public void insertCOMPITITOR(Compititor mSalesman) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(COMPITITORID, mSalesman.getCompititorId());
        contentValue.put(COMPITITOR_COMPANY_NAME, mSalesman.getCompititorCompanyName());
        contentValue.put(COMPITITOR_BRAND, mSalesman.getCompititor_brand());
        contentValue.put(COMPITITOR_ITEM_NAME, mSalesman.getCompititor_ItemName());
        contentValue.put(COMPITITOR_PRICE, mSalesman.getCOMPITITOR_Price());
        contentValue.put(COMPITITOR_PROMOTION, mSalesman.getCompititor_Promotion());
        contentValue.put(COMPITITOR_NOTES, mSalesman.getCompititor_Notes());
        contentValue.put(COMPITITOR_Image1, mSalesman.getCompititor_Image1());
        contentValue.put(COMPITITOR_Image2, mSalesman.getCompititor_Image2());
        contentValue.put(COMPITITOR_Image3, mSalesman.getCompititor_Image3());
        contentValue.put(COMPITITOR_Image4, mSalesman.getCompititor_Image4());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_COMPITITOR, null, contentValue);

    }

    //get complain list
    public ArrayList<Complain> getAllComplainList() {
        ArrayList<Complain> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPLAIN;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Complain item = new Complain();
                        item.setComplainId(cursor.getString(cursor.getColumnIndex(COMPLAINID)));
                        item.setComplain_Feedback(cursor.getString(cursor.getColumnIndex(COMPLAIN_FEEDBACK)));
                        item.setComplain_brand(cursor.getString(cursor.getColumnIndex(COMPLAIN_BRAND)));
                        item.setComplanin_Note(cursor.getString(cursor.getColumnIndex(COMPLAIN_FEEBACK_NOTES)));
                        item.setComplain_Image1(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image1)));
                        item.setComplain_Image2(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image2)));
                        item.setComplain_Image3(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image3)));
                        item.setComplain_Image4(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image4)));

                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //get complain list
    public Complain getComplainListByID(String ID) {
        Complain item = new Complain();
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COMPLAIN + " WHERE " +
                    COMPLAINID + "=?", new String[]{ID});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        item.setComplainId(cursor.getString(cursor.getColumnIndex(COMPLAINID)));
                        item.setComplain_Feedback(cursor.getString(cursor.getColumnIndex(COMPLAIN_FEEDBACK)));
                        item.setComplain_brand(cursor.getString(cursor.getColumnIndex(COMPLAIN_BRAND)));
                        item.setComplanin_Note(cursor.getString(cursor.getColumnIndex(COMPLAIN_FEEBACK_NOTES)));
                        item.setComplain_Image1(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image1)));
                        item.setComplain_Image2(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image2)));
                        item.setComplain_Image3(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image3)));
                        item.setComplain_Image4(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image4)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    public ArrayList<Item> getSubItemList(String ID) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM;

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_CATEGORY + "=?", new String[]{ID});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //INSERT Complain Accountablity
    public void insertCOMPLAIN(Complain mComlain) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(ITEM_ID, mComlain.getItemId());
        contentValue.put(COMPLAINID, mComlain.getComplainId());
        contentValue.put(COMPLAIN_FEEDBACK, mComlain.getComplain_Feedback());
        contentValue.put(COMPLAIN_BRAND, mComlain.getComplain_brand());
        contentValue.put(COMPLAIN_FEEBACK_NOTES, mComlain.getComplanin_Note());
        contentValue.put(COMPLAIN_Image1, mComlain.getComplain_Image1());
        contentValue.put(COMPLAIN_Image2, mComlain.getComplain_Image2());
        contentValue.put(COMPLAIN_Image3, mComlain.getComplain_Image3());
        contentValue.put(COMPLAIN_Image4, mComlain.getComplain_Image4());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_COMPLAIN, null, contentValue);


    }

    public ArrayList<Compaign> getCompaignList() {
        ArrayList<Compaign> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CAMPAIGN;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Compaign item = new Compaign();
                        item.setCompaignId(cursor.getString(cursor.getColumnIndex(COMPAIGN_ID)));
                        item.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                        item.setCompaign_Image1(cursor.getString(cursor.getColumnIndex(Compaign_Image1)));
                        item.setCompaign_Image2(cursor.getString(cursor.getColumnIndex(Compaign_Image2)));
                        item.setCompaign_Image3(cursor.getString(cursor.getColumnIndex(Compaign_Image3)));
                        item.setCompaign_Image4(cursor.getString(cursor.getColumnIndex(Compaign_Image4)));

                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //get Campaign list
    public Compaign getCampaignListByID(String ID) {
        Compaign item = new Compaign();
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CAMPAIGN + " WHERE " +
                    COMPAIGN_ID + "=?", new String[]{ID});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        item.setCompaignId(cursor.getString(cursor.getColumnIndex(COMPAIGN_ID)));
                        item.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                        item.setCompaign_Image1(cursor.getString(cursor.getColumnIndex(Compaign_Image1)));
                        item.setCompaign_Image2(cursor.getString(cursor.getColumnIndex(Compaign_Image2)));
                        item.setCompaign_Image3(cursor.getString(cursor.getColumnIndex(Compaign_Image3)));
                        item.setCompaign_Image4(cursor.getString(cursor.getColumnIndex(Compaign_Image4)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    //INSERT Compaign Accountablity
    public void insertCOMPAIGN(Compaign mSalesman) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(COMPAIGN_ID, mSalesman.getCompaignId());
        contentValue.put(COMMENT, mSalesman.getComment());
        contentValue.put(CUSTOMER_ID, mSalesman.getCustomerId());
        contentValue.put(Compaign_Image1, mSalesman.getCompaign_Image1());
        contentValue.put(Compaign_Image2, mSalesman.getCompaign_Image2());
        contentValue.put(Compaign_Image3, mSalesman.getCompaign_Image3());
        contentValue.put(Compaign_Image4, mSalesman.getCompaign_Image4());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_CAMPAIGN, null, contentValue);


    }

    //INSERT Planogram HEADER and items
    public void insertPlanogram(ArrayList<Planogram> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Planogram mPlanogram = arrData.get(i);

            for (int j = 0; j < mPlanogram.getPlanogram().size(); j++) {

                PlanoImages mPlanogramImages = mPlanogram.getPlanogram().get(j);
                ContentValues contentValueItem = new ContentValues();
                contentValueItem.put(PLANOGRAM_ID, mPlanogram.getPlanogramId());
                contentValueItem.put(CUSTOMER_ID, mPlanogram.getCustomerId());
                contentValueItem.put(DISTRIBUTION_TOOL_ID, mPlanogramImages.getDistributionToolId());
                contentValueItem.put(DISTRIBUTION_TOOL_NAME, mPlanogramImages.getDISTRIBUTION_TOOL_NAME());
                contentValueItem.put(IMAGE1, mPlanogramImages.getImage1());
                contentValueItem.put(IMAGE2, mPlanogramImages.getImage2());
                contentValueItem.put(IMAGE3, mPlanogramImages.getImage3());
                contentValueItem.put(IMAGE4, mPlanogramImages.getImage4());
                contentValueItem.put(FRONT_IMAGE, "");
                contentValueItem.put(BACK_IMAGE, "");
                contentValueItem.put(COMMENT, "");
                contentValueItem.put(PLANOGRAM_NAME, mPlanogram.getPlanogramName());
                contentValueItem.put(DATA_MARK_FOR_POST, "N");
                contentValueItem.put(IS_POSTED, "0");
                db.insert(TABLE_PLANOGRAM, null, contentValueItem);
            }
        }

    }

    //INSERT Assets Survey HEADER ONLY SINGLE
    public void insertCustomerAssets(ArrayList<ASSETS_MODEL> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            ASSETS_MODEL mSurvey = arrData.get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(ASSET_CODE, mSurvey.getAssetscode());
            contentValue.put(ASSET_NAME, mSurvey.getAssetsName());
            contentValue.put(ASSET_TYPE, mSurvey.getAssetsType());
            contentValue.put(ASSET_ID, mSurvey.getAssetsId());
            contentValue.put(CUSTOMER_ID, mSurvey.getCustomerId());
            contentValue.put(LATITUDE, mSurvey.getLatitude());
            contentValue.put(LONGITUDE, mSurvey.getLongitude());
            contentValue.put(BARCODE, mSurvey.getBarcode());
            contentValue.put(IMAGE1, mSurvey.getAssetsImage());
            contentValue.put(ASSETS_IMAGE1, "");
            contentValue.put(ASSETS_IMAGE2, "");
            contentValue.put(ASSETS_IMAGE3, "");
            contentValue.put(ASSETS_IMAGE4, "");
            contentValue.put(ASSETS_FEEDBACK, "");
            contentValue.put(DATA_MARK_FOR_POST, "N");
            contentValue.put(IS_POSTED, "0");
            db.insert(TABLE_CUSTOMER_ASSETS, null, contentValue);

        }

    }

    //INSERT Assets Survey HEADER ONLY SINGLE
    public void insertAssetsSurvey(ArrayList<Survey_Tools> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Survey_Tools mSurvey = arrData.get(i);

            for (int j = 0; j < mSurvey.getQuestions().size(); j++) {

                Stock_Questions mQuestions = mSurvey.getQuestions().get(j);
                ContentValues contentValueItem = new ContentValues();
                contentValueItem.put(SURVEY_ID, mSurvey.getSurvey_Id());
                contentValueItem.put(QUESTION_ID, mQuestions.getQuestionId());
                contentValueItem.put(QUESTION, mQuestions.getQuestion());
                contentValueItem.put(QUESTION_TYPE, mQuestions.getQuestion_type());
                contentValueItem.put(SELECT_BASE_QUESTION, mQuestions.getQuestion_type_based());
                db.insert(TABLE_ASSETS_QUESTIONS, null, contentValueItem);
            }
        }

    }

    //INSERT Survey HEADER ONLY SINGLE
    public void insertSurvey(ArrayList<Survey_Tools> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Survey_Tools mSurvey = arrData.get(i);

            for (int j = 0; j < mSurvey.getQuestions().size(); j++) {

                Stock_Questions mQuestions = mSurvey.getQuestions().get(j);
                ContentValues contentValueItem = new ContentValues();
                contentValueItem.put(SURVEY_ID, mSurvey.getSurvey_Id());
                contentValueItem.put(DISTRIBUTION_TYPE, mSurvey.getDistribution_Type());
                contentValueItem.put(QUESTION_ID, mQuestions.getQuestionId());
                contentValueItem.put(QUESTION, mQuestions.getQuestion());
                contentValueItem.put(QUESTION_TYPE, mQuestions.getQuestion_type());
                contentValueItem.put(SELECT_BASE_QUESTION, mQuestions.getQuestion_type_based());
                db.insert(TABLE_SURVEY_QUESTIONS, null, contentValueItem);

            }
        }

    }

    //INSERT Sensor Survey HEADER ONLY SINGLE
    public void insertSensorSurvey(ArrayList<Survey_Tools> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Survey_Tools mSurvey = arrData.get(i);

            for (int j = 0; j < mSurvey.getQuestions().size(); j++) {

                Stock_Questions mQuestions = mSurvey.getQuestions().get(j);
                ContentValues contentValueItem = new ContentValues();
                contentValueItem.put(SURVEY_ID, mSurvey.getSurvey_Id());
                contentValueItem.put(QUESTION_ID, mQuestions.getQuestionId());
                contentValueItem.put(QUESTION, mQuestions.getQuestion());
                contentValueItem.put(QUESTION_TYPE, mQuestions.getQuestion_type());
                contentValueItem.put(SELECT_BASE_QUESTION, mQuestions.getQuestion_type_based());
                db.insert(TABLE_SENSOR_QUESTIONS, null, contentValueItem);

            }
        }

    }

    //INSERT Consumer Survey HEADER ONLY SINGLE
    public void insertConsumerSurvey(ArrayList<Survey_Tools> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Survey_Tools mSurvey = arrData.get(i);

            for (int j = 0; j < mSurvey.getQuestions().size(); j++) {

                Stock_Questions mQuestions = mSurvey.getQuestions().get(j);
                ContentValues contentValueItem = new ContentValues();
                contentValueItem.put(SURVEY_ID, mSurvey.getSurvey_Id());
                contentValueItem.put(CUST_ID, mSurvey.getCustomerID());
                contentValueItem.put(QUESTION_ID, mQuestions.getQuestionId());
                contentValueItem.put(QUESTION, mQuestions.getQuestion());
                contentValueItem.put(QUESTION_TYPE, mQuestions.getQuestion_type());
                contentValueItem.put(SELECT_BASE_QUESTION, mQuestions.getQuestion_type_based());
                db.insert(TABLE_CONSUMER_QUESTIONS, null, contentValueItem);
            }
        }
    }

    //INSERT Inventory
    public void insertInventory(ArrayList<Inventory> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Inventory mInventory = arrData.get(i);

            ContentValues contentValueItem = new ContentValues();
            contentValueItem.put(INVENTORY_ID, mInventory.getInventoryId());
            contentValueItem.put(INVENTORYNO, "");
            contentValueItem.put(DATA_MARK_FOR_POST, "N");
            contentValueItem.put(CUSTOMER_ID, mInventory.getCustomerId());
            contentValueItem.put(IS_POSTED, "0");

            db.insert(TABLE_CUSTOMER_INVENTORY_HEADER, null, contentValueItem);

            for (int j = 0; j < mInventory.getArrItem().size(); j++) {

                Item mItem = mInventory.getArrItem().get(j);
                ContentValues contentValueIN = new ContentValues();
                contentValueIN.put(INVENTORY_ID, mInventory.getInventoryId());
                contentValueIN.put(CUSTOMER_ID, mInventory.getCustomerId());
                contentValueIN.put(ITEM_ID, mItem.getItemId());
                contentValueIN.put(ITEM_UOM, mItem.getUom());
                contentValueIN.put(ITEM_CATEGORY, getItemCategory(mItem.getItemId()));

                db.insert(TABLE_CUSTOMER_INVENTORY_ITEMS, null, contentValueIN);
            }
        }
    }

    //INSERT Expiry Item list
    public void insertPromotionItemList(ArrayList<Promotion_Item> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValue = new ContentValues();

            contentValue.put(PROMOTIONAL_ID, arrData.get(i).getPromotionId());
            contentValue.put(ITEM_ID, arrData.get(i).getItem_Id());
            contentValue.put(ITEM_NAME, arrData.get(i).getItem_Name());
            contentValue.put(AMOUNT, arrData.get(i).getAmount());
            contentValue.put(FROM_DATE, arrData.get(i).getFrom_Date());
            contentValue.put(TO_DATE, arrData.get(i).getTo_Date());
            db.insert(TABLE_PROMOTION_ITEM, null, contentValue);
        }


    }

    //INSERT Distribution HEADER ONLY SINGLE
    public void insertDISTRIBUTION(ArrayList<Distribution> arrData) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Distribution mItem = arrData.get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(ASSIGN_ITEM_ID, mItem.getAssignitemId());
            contentValue.put(DISTRIBUTION_TOOL_ID, mItem.getDistribution_Tool_Id());
            contentValue.put(DISTRIBUTION_TOOL_NAME, mItem.getDistribution_Tool_Name());
            contentValue.put(DISTRIBUTION_TOOL_FROM, mItem.getDistribution_Tool_From());
            contentValue.put(DISTRIBUTION_TOOL_TO, mItem.getDistribution_Tool_To());
            contentValue.put(DISTRIBUTION_TOOL_HEIGHT, mItem.getDistribution_Tool_Height());
            contentValue.put(DISTRIBUTION_TOOL_WIDTH, mItem.getDistribution_Tool_Width());
            contentValue.put(DISTRIBUTION_TOOL_LENGTH, mItem.getDistribution_Tool_Length());
            contentValue.put(DISTRIBUTION_TOOL_CAPACITY, mItem.getDistribution_Tool_Capacity());
            contentValue.put(ALTERNATIVE_CODE, mItem.getAlternate_Code());
            contentValue.put(UOM, mItem.getUOM());
            contentValue.put(QTY, mItem.getQTY());
            contentValue.put(ITEM_ID, mItem.getItem_Id());
            contentValue.put(ITEM_NAME, mItem.getItem_Name());
            contentValue.put(ITEM_CATEGORY, mItem.getItem_Category());
            contentValue.put(CUST_ID, mItem.getCustomerId());
            contentValue.put(FILL_QTY, "");
            contentValue.put(DATA_MARK_FOR_POST, "N");
            contentValue.put(IS_POSTED, "0");

            db.insert(TABLE_DISTRIBUTION, null, contentValue);
        }

    }

    public ASSETS_MODEL getAssetsModel(String assetsId) {
        ASSETS_MODEL mSalesman = new ASSETS_MODEL();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_ASSETS + " WHERE " +
                    ASSET_ID + "=?", new String[]{assetsId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        mSalesman.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        mSalesman.setAssetsName(cursor.getString(cursor.getColumnIndex(ASSET_NAME)));
                        mSalesman.setAssetsType(cursor.getString(cursor.getColumnIndex(ASSET_TYPE)));
                        mSalesman.setAssetsImage(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                        mSalesman.setAssetsImage1(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE1)));
                        mSalesman.setAssetsImage2(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE2)));
                        mSalesman.setAssetsImage3(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE3)));
                        mSalesman.setAssetsImage4(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE4)));
                        mSalesman.setAssetsFeedback(cursor.getString(cursor.getColumnIndex(ASSETS_FEEDBACK)));

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSalesman;
    }

    //Get Items from Category
    public ArrayList<PlanogramList> getPlanogramList(String Id) {
        ArrayList<PlanogramList> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PLANOGRAM + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{Id});
            System.out.println(cursor.getCount());

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        PlanogramList item = new PlanogramList();
                        item.setPlanogramName(cursor.getString(cursor.getColumnIndex(PLANOGRAM_NAME)));
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setPlanogramId(cursor.getString(cursor.getColumnIndex(PLANOGRAM_ID)));
                        item.setBack_image(cursor.getString(cursor.getColumnIndex(BACK_IMAGE)));
                        item.setFront_image(cursor.getString(cursor.getColumnIndex(FRONT_IMAGE)));
                        item.setImage1(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                        item.setImage2(cursor.getString(cursor.getColumnIndex(IMAGE2)));
                        item.setImage3(cursor.getString(cursor.getColumnIndex(IMAGE3)));
                        item.setImage4(cursor.getString(cursor.getColumnIndex(IMAGE4)));
                        item.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                        item.setDistribution_Tool_Name(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_NAME)));
                        item.setDistribution_tool_id(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get Vanstock Item
    public PlanogramList getPlanogramItem(String itemId) {
        PlanogramList item = new PlanogramList();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PLANOGRAM + " WHERE " +
                    DISTRIBUTION_TOOL_ID + "=?", new String[]{itemId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        item.setPlanogramId(cursor.getString(cursor.getColumnIndex(PLANOGRAM_ID)));
                        item.setDistribution_tool_id(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        item.setFront_image(cursor.getString(cursor.getColumnIndex(FRONT_IMAGE)));
                        item.setBack_image(cursor.getString(cursor.getColumnIndex(BACK_IMAGE)));
                        item.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                        item.setImage1(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                        item.setImage2(cursor.getString(cursor.getColumnIndex(IMAGE2)));
                        item.setImage3(cursor.getString(cursor.getColumnIndex(IMAGE3)));
                        item.setImage4(cursor.getString(cursor.getColumnIndex(IMAGE4)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    public void updatePlanogram(String id, PlanogramList mplanogram) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(FRONT_IMAGE, mplanogram.getFront_image());
        contentValue.put(BACK_IMAGE, mplanogram.getBack_image());
        contentValue.put(COMMENT, mplanogram.getComment());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.update(TABLE_PLANOGRAM, contentValue, PLANOGRAM_ID + " = ? AND " +
                DISTRIBUTION_TOOL_ID + " = ? ", new String[]{mplanogram.getPlanogramId(), id});

    }

    //Get Items from Category
    public ArrayList<ASSETS_MODEL> getAssetsList(String Id) {
        ArrayList<ASSETS_MODEL> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_ASSETS + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{Id});
            System.out.println(cursor.getCount());

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        ASSETS_MODEL item = new ASSETS_MODEL();
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setAssetsId(cursor.getString(cursor.getColumnIndex(ASSET_ID)));
                        item.setAssetscode(cursor.getString(cursor.getColumnIndex(ASSET_CODE)));
                        item.setAssetsType(cursor.getString(cursor.getColumnIndex(ASSET_TYPE)));
                        item.setAssetsName(cursor.getString(cursor.getColumnIndex(ASSET_NAME)));
                        item.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                        item.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                        item.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODE)));
                        item.setAssetsImage(cursor.getString(cursor.getColumnIndex(IMAGE1)));

                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Update Assets Items
    public int updateAssets(ASSETS_MODEL mAssets, String custId, String assetsId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(ASSETS_IMAGE1, mAssets.getAssetsImage1());
        contentValueItem.put(ASSETS_IMAGE2, mAssets.getAssetsImage2());
        contentValueItem.put(ASSETS_IMAGE3, mAssets.getAssetsImage3());
        contentValueItem.put(ASSETS_IMAGE4, mAssets.getAssetsImage4());
        contentValueItem.put(ASSETS_FEEDBACK, mAssets.getAssetsFeedback());
        contentValueItem.put(DATA_MARK_FOR_POST, "M");
        contentValueItem.put(IS_POSTED, "0");

        int i = db.update(TABLE_CUSTOMER_ASSETS,
                contentValueItem, CUSTOMER_ID + " = ? AND " +
                        ASSET_ID + " = ?", new String[]{custId, assetsId});
        return i;
    }

    //get All Questions
    public ArrayList<Stock_Questions> getAllSensorQuestions() {
        ArrayList<Stock_Questions> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SENSOR_QUESTIONS;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Stock_Questions mLoad = new Stock_Questions();
                        mLoad.setQuestionId(cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mLoad.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION)));
                        mLoad.setQuestion_type(cursor.getString(cursor.getColumnIndex(QUESTION_TYPE)));
                        mLoad.setServeyId(cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        mLoad.setQuestion_type_based(cursor.getString(cursor.getColumnIndex(SELECT_BASE_QUESTION)));

                        arrData.add(mLoad);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }

    //get All Questions
    public ArrayList<Stock_Questions> getAllConsumerQuestions(String custID) {
        ArrayList<Stock_Questions> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CONSUMER_QUESTIONS + " WHERE " +
                    CUST_ID + "=?", new String[]{custID});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Stock_Questions mLoad = new Stock_Questions();
                        mLoad.setQuestionId(cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mLoad.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION)));
                        mLoad.setQuestion_type(cursor.getString(cursor.getColumnIndex(QUESTION_TYPE)));
                        mLoad.setServeyId(cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        mLoad.setQuestion_type_based(cursor.getString(cursor.getColumnIndex(SELECT_BASE_QUESTION)));

                        arrData.add(mLoad);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }

    //get All Questions
    public ArrayList<Stock_Questions> getAllAssetsQuestions() {
        ArrayList<Stock_Questions> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSETS_QUESTIONS;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Stock_Questions mLoad = new Stock_Questions();
                        mLoad.setQuestionId(cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mLoad.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION)));
                        mLoad.setQuestion_type(cursor.getString(cursor.getColumnIndex(QUESTION_TYPE)));
                        mLoad.setServeyId(cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        ;
                        mLoad.setQuestion_type_based(cursor.getString(cursor.getColumnIndex(SELECT_BASE_QUESTION)));

                        arrData.add(mLoad);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }

    //get All Questions
    public ArrayList<Stock_Questions> getAllQuestions(String id) {
        ArrayList<Stock_Questions> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SURVEY_QUESTIONS + " WHERE " +
                    DISTRIBUTION_TYPE + "=?", new String[]{id});
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Stock_Questions mLoad = new Stock_Questions();
                        mLoad.setQuestionId(cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mLoad.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION)));
                        mLoad.setQuestion_type(cursor.getString(cursor.getColumnIndex(QUESTION_TYPE)));
                        mLoad.setServeyId(cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        mLoad.setQuestionId(cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mLoad.setQuestion_type_based(cursor.getString(cursor.getColumnIndex(SELECT_BASE_QUESTION)));
                        mLoad.setDistributionType(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TYPE)));

                        arrData.add(mLoad);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return arrData;
    }

    public boolean isConsumerSurvey(String customerId) {
        boolean isDone = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CONSUMER_POST_HEADER + " WHERE " +
                    CUST_ID + "=?", new String[]{customerId});

            if (cursor.getCount() > 0) {
                isDone = true;
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return isDone;
    }

    public boolean isAssetsSurvey(String customerId) {
        boolean isDone = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ASSETS_POST_HEADER + " WHERE " +
                    CUST_ID + "=?", new String[]{customerId});

            if (cursor.getCount() > 0) {
                isDone = true;
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return isDone;
    }

    public boolean isToolSurvey(String customerId, String toolId) {
        boolean isDone = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SURVEY_POST_HEADER + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + DISTRIBUTION_TYPE + "=?", new String[]{customerId, toolId});

            if (cursor.getCount() > 0) {
                isDone = true;
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return isDone;
    }


    //INSERT Sensor Survey HEADER ONLY SINGLE
    public void insertSensorSurveyHeader(String uniqueID, String CustomerID, String SurveyID, String CustomerEmail, String CusomerName, String CustomerPhone, String CustomerAddress) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(UNIQUESURVEY_ID, uniqueID);
        contentValue.put(SURVEY_ID, SurveyID);
        contentValue.put(CUSTOMER_ID, CustomerID);
        contentValue.put(CUSTOMER_NAME, CusomerName);
        contentValue.put(CUSTOMER_EMAIL, CustomerEmail);
        contentValue.put(CUSTOMER_PHONE, CustomerPhone);
        contentValue.put(CUSTOMER_ADDRESS, CustomerAddress);
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");
        db.insert(TABLE_SENSOR_HEADER, null, contentValue);

    }

    //INSERT Sensor Survey Post
    public void insertSensoryPost(ArrayList<Stock_Questions> arrData, String uniqNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValue = new ContentValues();

            contentValue.put(UNIQUESURVEY_ID, uniqNo);
            contentValue.put(SURVEY_ID, arrData.get(i).getServeyId());
            contentValue.put(QUESTION_ID, arrData.get(i).getQuestionId());
            contentValue.put(CUSTOMER_ID, arrData.get(i).getCustomerId());
            contentValue.put(CUSTOMER_NAME, arrData.get(i).getCustomerName());
            contentValue.put(CUSTOMER_EMAIL, arrData.get(i).getCustomerEmail());
            contentValue.put(CUSTOMER_PHONE, arrData.get(i).getCustomerPhone());
            contentValue.put(ANSWER_TEXT, arrData.get(i).getAnswer());
            contentValue.put(DATA_MARK_FOR_POST, "M");
            contentValue.put(IS_POSTED, "0");
            db.insert(TABLE_SENSOR_POST, null, contentValue);
        }

    }

    //INSERT Consumer Survey Post
    public void insertConsumerPost(ArrayList<Stock_Questions> arrData, String uniQNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHER = new ContentValues();
        contentValueHER.put(SURVEY_ID, arrData.get(0).getServeyId());
        contentValueHER.put(UNIQUESURVEY_ID, uniQNo);
        contentValueHER.put(CUST_ID, arrData.get(0).getCustomerId());
        contentValueHER.put(DATA_MARK_FOR_POST, "M");
        contentValueHER.put(IS_POSTED, "0");
        db.insert(TABLE_CONSUMER_POST_HEADER, null, contentValueHER);

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValue = new ContentValues();

            contentValue.put(UNIQUESURVEY_ID, uniQNo);
            contentValue.put(SURVEY_ID, arrData.get(i).getServeyId());
            contentValue.put(QUESTION_ID, arrData.get(i).getQuestionId());
            contentValue.put(CUST_ID, arrData.get(i).getCustomerId());
            contentValue.put(ANSWER_TEXT, arrData.get(i).getAnswer());
            contentValue.put(DATA_MARK_FOR_POST, "M");
            contentValue.put(IS_POSTED, "0");
            db.insert(TABLE_CONSUMER_POST, null, contentValue);
        }

    }

    //INSERT Assets Survey Post
    public void insertAssetsSurveyPost(ArrayList<Stock_Questions> arrData, String uniQNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHER = new ContentValues();
        contentValueHER.put(SURVEY_ID, arrData.get(0).getServeyId());
        contentValueHER.put(UNIQUESURVEY_ID, uniQNo);
        contentValueHER.put(CUST_ID, arrData.get(0).getCustomerId());
        contentValueHER.put(DATA_MARK_FOR_POST, "M");
        contentValueHER.put(IS_POSTED, "0");
        db.insert(TABLE_ASSETS_POST_HEADER, null, contentValueHER);


        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValue = new ContentValues();

            contentValue.put(SURVEY_ID, arrData.get(i).getServeyId());
            contentValue.put(UNIQUESURVEY_ID, uniQNo);
            contentValue.put(DISTRIBUTION_TYPE, arrData.get(i).getQuestion_type_based());
            contentValue.put(QUESTION_ID, arrData.get(i).getQuestionId());
            contentValue.put(CUSTOMER_ID, arrData.get(i).getCustomerId());
            contentValue.put(ANSWER_TEXT, arrData.get(i).getAnswer());
            contentValue.put(DISTRIBUTION_TYPE, arrData.get(i).getDistributionType());
            contentValue.put(DATA_MARK_FOR_POST, "M");
            contentValue.put(IS_POSTED, "0");
            db.insert(TABLE_ASSETS_POST, null, contentValue);
        }
    }

    //INSERT Survey Post
    public void insertSurveyPost(ArrayList<Stock_Questions> arrData, String uniQNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHER = new ContentValues();
        contentValueHER.put(DISTRIBUTION_TYPE, arrData.get(0).getDistributionType());
        contentValueHER.put(UNIQUESURVEY_ID, uniQNo);
        contentValueHER.put(CUSTOMER_ID, arrData.get(0).getCustomerId());
        contentValueHER.put(DATA_MARK_FOR_POST, "M");
        contentValueHER.put(IS_POSTED, "0");
        db.insert(TABLE_SURVEY_POST_HEADER, null, contentValueHER);


        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValue = new ContentValues();

            contentValue.put(SURVEY_ID, arrData.get(i).getServeyId());
            contentValue.put(UNIQUESURVEY_ID, uniQNo);
            contentValue.put(DISTRIBUTION_TYPE, arrData.get(i).getQuestion_type_based());
            contentValue.put(QUESTION_ID, arrData.get(i).getQuestionId());
            contentValue.put(CUSTOMER_ID, arrData.get(i).getCustomerId());
            contentValue.put(ANSWER_TEXT, arrData.get(i).getAnswer());
            contentValue.put(DISTRIBUTION_TYPE, arrData.get(i).getDistributionType());
            contentValue.put(DATA_MARK_FOR_POST, "M");
            contentValue.put(IS_POSTED, "0");
            db.insert(TABLE_SURVEY_POST, null, contentValue);
        }
    }

    //Get  DistSensor  item list
    public ArrayList<Stock_Questions> getAllSensoryCustomerList() {
        ArrayList<Stock_Questions> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SENSOR_HEADER, null);

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Stock_Questions item = new Stock_Questions();
                        item.setServeyId(cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        item.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        item.setCustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                        item.setCustomerEmail(cursor.getString(cursor.getColumnIndex(CUSTOMER_EMAIL)));
                        item.setCustomerPhone(cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                        item.setCustomerAddress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //get details from Customer inventory
    public String getCustomerInventoryId(String Id) {
        String inventoryId = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_INVENTORY_HEADER + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{Id});
            System.out.println(cursor.getCount());

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {

                        inventoryId = cursor.getString(cursor.getColumnIndex(INVENTORY_ID));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return inventoryId;
    }


    //get details from Customer inventory
    public ArrayList<Item> getCustomerInventory(String Id) {
        ArrayList<Item> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_INVENTORY + " WHERE " +
                    CUSTOMER_ID + "=?", new String[]{Id});
            System.out.println(cursor.getCount());

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Item item = new Item();
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setInventoryId(cursor.getString(cursor.getColumnIndex(INVENTORY_ID)));
                        Item mDetail = getItemDetail(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(mDetail.getItemCode());
                        item.setCurrentDate(cursor.getString(cursor.getColumnIndex(IS_CURRENTDATE)));
                        item.setItemName(mDetail.getItemName());
                        item.setUom(cursor.getString(cursor.getColumnIndex(ITEM_UOM)));
                        item.setUomName(getUOM(cursor.getString(cursor.getColumnIndex(ITEM_UOM))));
                        item.setQty(cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        item.setExpiryItem(cursor.getString(cursor.getColumnIndex(EXPIRY_DATE)));
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public Item getItemDetail(String id) {
        Item item = new Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM + " WHERE " +
                    ITEM_ID + "=?", new String[]{id});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        item.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setItemCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                        item.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setCategory(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        item.setBaseUOM(cursor.getString(cursor.getColumnIndex(ITEM_BASEUOM)));
                        item.setAltrUOM(cursor.getString(cursor.getColumnIndex(ITEM_ALRT_UOM)));
                        item.setUpc(cursor.getString(cursor.getColumnIndex(ITEM_UPC)));
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return item;
    }

    //Insert Inventory
    public void insertInventoryHeader(String invId, String customerId, String invNo) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(INVENTORYNO, invNo);
        contentValueItem.put(DATA_MARK_FOR_POST, "M");
        contentValueItem.put(IS_POSTED, "0");

        db.update(TABLE_CUSTOMER_INVENTORY_HEADER,
                contentValueItem, CUSTOMER_ID + " = ? AND " +
                        INVENTORY_ID + " = ?", new String[]{customerId, invId});

        //db.insert(TABLE_CUSTOMER_INVENTORY_HEADER, null, contentValueItem);
    }

    //Update Inventory
    public void updateInventoryExpiry(ArrayList<Item> arrData, String customerId, String CurrentDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Item mItem = arrData.get(i);

            ContentValues contentValueItem = new ContentValues();
            contentValueItem.put(INVENTORY_ID, mItem.getInventoryId());
            contentValueItem.put(CUSTOMER_ID, customerId);
            contentValueItem.put(ITEM_QTY, mItem.getQty());
            contentValueItem.put(EXPIRY_DATE, mItem.getExpiryItem());
            contentValueItem.put(DATA_MARK_FOR_POST, "M");
            contentValueItem.put(IS_CURRENTDATE, CurrentDate);
            contentValueItem.put(IS_POSTED, "0");
            contentValueItem.put(ITEM_ID, mItem.getItemId());
            contentValueItem.put(ITEM_UOM, mItem.getAltrUOM());

//            db.update(TABLE_CUSTOMER_INVENTORY,
//                    contentValueItem, CUSTOMER_ID + " = ? AND " +
//                            INVENTORY_ID + " = ? AND " +
//                            ITEM_ID + " = ?", new String[]{customerId, mItem.getInventoryId(), mItem.getItemId()});

            db.insert(TABLE_CUSTOMER_INVENTORY, null, contentValueItem);

        }
    }

    //Get  Distribution tools list
    public ArrayList<Distribution> getCustomerToolsList(String customerId) {
        ArrayList<Distribution> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION + " WHERE " +
                    CUST_ID + "=?", new String[]{customerId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Distribution item = new Distribution();
                        item.setItem_Id(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setDistribution_Tool_Name(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_NAME)));
                        item.setDistribution_Tool_Id(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        item.setItem_Category(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));

                        if (arrData.size() > 0) {
                            boolean isContain = false;
                            for (int i = 0; i < arrData.size(); i++) {
                                if (arrData.get(i).getDistribution_Tool_Id().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)))) {
                                    isContain = true;
                                    break;
                                }
                            }
                            if (!isContain) {
                                arrData.add(item);
                            }
                        } else {
                            arrData.add(item);
                        }


                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    //Get  Distribution tools list
    public ArrayList<Distribution> getrToolsList(String customerId) {
        ArrayList<Distribution> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION + " WHERE " +
                    CUST_ID + "=?", new String[]{customerId});

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Distribution item = new Distribution();
                        item.setItem_Id(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setDistribution_Tool_Name(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_NAME)));
                        item.setDistribution_Tool_Id(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        item.setItem_Category(cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY)));
                        arrData.add(item);


                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public ArrayList<Distribution> getDistributionListByName(String Id, String cat, String custId) {
        ArrayList<Distribution> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION + " WHERE " +
                    DISTRIBUTION_TOOL_ID + "=?" + " AND "
                    + ITEM_CATEGORY + "=?" + " AND "
                    + CUST_ID + "=?", new String[]{Id, cat, custId});
            System.out.println(cursor.getCount());

            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Distribution item = new Distribution();
                        item.setDistribution_Tool_Name(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_NAME)));
                        item.setDistribution_Tool_Id(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        item.setItem_Name(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        item.setItem_Id(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        item.setQTY(cursor.getString(cursor.getColumnIndex(QTY)));
                        item.setUOM(getUOM(cursor.getString(cursor.getColumnIndex(UOM))));
                        item.setAlternate_Code(cursor.getString(cursor.getColumnIndex(ALTERNATIVE_CODE)));
                        item.setFillQty(cursor.getString(cursor.getColumnIndex(FILL_QTY)));
                        item.setGoodSaleQty("");
                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public boolean isDistributionImage(String customerId, String toolId, String catId) {
        boolean isDone = false;

        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION_IMAGE + " WHERE " +
                    CUSTOMER_ID + "=?" + " AND "
                    + DISTRIBUTION_TOOL_ID + "=?" + " AND "
                    + ITEM_CATEGORY + "=?", new String[]{customerId, toolId, catId});

            if (cursor.getCount() > 0) {
                isDone = true;
            }
        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return isDone;
    }

    //INSERT Damaged Item list
    public void insertDistributionItem(ArrayList<String> arrData, String toolId, String customerId, String catId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DISTRIBUTION_TOOL_ID, toolId);
        contentValue.put(CUSTOMER_ID, customerId);
        contentValue.put(ITEM_CATEGORY, catId);

        if (arrData.size() == 4) {
            contentValue.put(IMAGE1, arrData.get(0));
            contentValue.put(IMAGE2, arrData.get(1));
            contentValue.put(IMAGE3, arrData.get(2));
            contentValue.put(IMAGE4, arrData.get(3));
        } else if (arrData.size() == 3) {
            contentValue.put(IMAGE1, arrData.get(0));
            contentValue.put(IMAGE2, arrData.get(1));
            contentValue.put(IMAGE3, arrData.get(2));
            contentValue.put(IMAGE4, "");
        } else if (arrData.size() == 2) {
            contentValue.put(IMAGE1, arrData.get(0));
            contentValue.put(IMAGE2, arrData.get(1));
            contentValue.put(IMAGE3, "");
            contentValue.put(IMAGE4, "");
        } else if (arrData.size() == 1) {
            contentValue.put(IMAGE1, arrData.get(0));
            contentValue.put(IMAGE2, "");
            contentValue.put(IMAGE3, "");
            contentValue.put(IMAGE4, "");
        }
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");
        db.insert(TABLE_DISTRIBUTION_IMAGE, null, contentValue);
    }

    //INSERT Expiry Item list
    public void insertExpiryItemList(ArrayList<Distribution> arrData, String uniqNo, String custId, String catId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHeader = new ContentValues();

        contentValueHeader.put(UNIQUESURVEY_ID, uniqNo);
        contentValueHeader.put(DISTRIBUTION_TOOL_ID, arrData.get(0).getDistribution_Tool_Id());
        contentValueHeader.put(ITEM_CATEGORY, catId);
        contentValueHeader.put(CUSTOMER_ID, custId);
        contentValueHeader.put(DATA_MARK_FOR_POST, "M");
        contentValueHeader.put(IS_POSTED, "0");
        db.insert(TABLE_EXPIRY_HEADER, null, contentValueHeader);

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValue = new ContentValues();

            contentValue.put(UNIQUESURVEY_ID, uniqNo);
            contentValue.put(CUSTOMER_ID, custId);
            contentValue.put(ITEM_CATEGORY, catId);
            contentValue.put(DISTRIBUTION_TOOL_ID, arrData.get(i).getDistribution_Tool_Id());
            contentValue.put(ITEM_ID, arrData.get(i).getItem_Id());
            contentValue.put(SALESMAN_ID, arrData.get(i).getSalesmanId());
            contentValue.put(UOM, getUOMId(arrData.get(i).getUOM()));
            contentValue.put(PC, arrData.get(i).getPC());
            contentValue.put(EXPIRY_DATE, arrData.get(i).getExpiryDate());
            contentValue.put(DATA_MARK_FOR_POST, "M");
            contentValue.put(IS_POSTED, "0");
            db.insert(TABLE_EXPIRY_ITEM, null, contentValue);
        }

    }

    //INSERT Damaged Item list
    public void insertDamagedItemList(ArrayList<Distribution> arrData, String cmpNo, String custId, String catId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHeader = new ContentValues();

        contentValueHeader.put(DISTRIBUTION_TOOL_ID, arrData.get(0).getDistribution_Tool_Id());
        contentValueHeader.put(UNIQUESURVEY_ID, cmpNo);
        contentValueHeader.put(CUSTOMER_ID, custId);
        contentValueHeader.put(ITEM_CATEGORY, catId);
        contentValueHeader.put(DATA_MARK_FOR_POST, "M");
        contentValueHeader.put(IS_POSTED, "0");
        db.insert(TABLE_DAMAGED_HEADER, null, contentValueHeader);

        for (int i = 0; i < arrData.size(); i++) {
            ContentValues contentValue = new ContentValues();

            contentValue.put(DISTRIBUTION_TOOL_ID, arrData.get(i).getDistribution_Tool_Id());
            contentValue.put(UNIQUESURVEY_ID, cmpNo);
            contentValue.put(CUSTOMER_ID, custId);
            contentValue.put(ITEM_CATEGORY, catId);
            contentValue.put(ITEM_ID, arrData.get(i).getItem_Id());
            contentValue.put(SALESMAN_ID, arrData.get(i).getSalesmanId());
            contentValue.put(DAMAGED_ITEM, arrData.get(i).getUOM());
            contentValue.put(EXPIRED_ITEM, arrData.get(i).getPC());
            contentValue.put(SALEABLE_ITEM, arrData.get(i).getExpiryDate());
            contentValue.put(DATA_MARK_FOR_POST, "M");
            contentValue.put(IS_POSTED, "0");
            db.insert(TABLE_DAMAGED_ITEM, null, contentValue);
        }

    }

    //INSERT Distribution HEADER ONLY SINGLE
    public void updateDISTRIBUTION(ArrayList<Distribution> arrData, String customerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrData.size(); i++) {

            Distribution mItem = arrData.get(i);
            if (!mItem.getFillQty().equalsIgnoreCase("")) {
                ContentValues contentValueItem = new ContentValues();
                String toolId = mItem.getDistribution_Tool_Id();
                String itemId = mItem.getItem_Id();
                contentValueItem.put(FILL_QTY, mItem.getFillQty());
                contentValueItem.put(DATA_MARK_FOR_POST, "M");
                contentValueItem.put(IS_POSTED, "0");
                db.update(TABLE_DISTRIBUTION,
                        contentValueItem, DISTRIBUTION_TOOL_ID + " = ? AND " +
                                ITEM_ID + " = ? AND " +
                                CUST_ID + " = ?", new String[]{toolId, itemId, customerID});

            }
        }
    }

    public String getUOMId(String uomId) {
        String strUOM = "";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UOM + " WHERE " +
                    UOM_NAME + "=?", new String[]{uomId});
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        strUOM = cursor.getString(cursor.getColumnIndex(UOM_ID));
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUOM;
    }

    public int getPostPlanogramCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PLANOGRAM + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostComplaintCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COMPLAIN + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostCampaignCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CAMPAIGN + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostAssetsCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_ASSETS + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }


    public int getPostCompititortCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COMPITITOR + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostExpiryCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_EXPIRY_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostDamagedCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DAMAGED_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostSurveyCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SURVEY_POST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostAssetsSurveyCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ASSETS_POST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostSensoryCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SENSOR_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostInventoryCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_INVENTORY_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostConsumerCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CONSUMER_POST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostDisImageCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION_IMAGE + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostDisStockCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getPostPromotionCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PROMOTION + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getChillerPostCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ADD_CHILLER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getFridgePostCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ADD_FRIDGE + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getChillerRequestPostCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CHILLER_REQUEST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getChillerAddRequestPostCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CHILLER_ADD + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getChillerTrackPostCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CHILLER_TRACKING + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    public int getServiceVisitPostCount() {

        int invCount = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SERVICE_VISIT_POST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            cursor.moveToFirst();
            invCount = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invCount;
    }

    //GET Planogram Detail
    public PlanogramList getPostPlanogramDetail() {

        PlanogramList mPlanogram = new PlanogramList();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PLANOGRAM + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    mPlanogram.setPlanogramId(cursor.getString(cursor.getColumnIndex(PLANOGRAM_ID)));
                    mPlanogram.setDistribution_tool_id(cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                    mPlanogram.setBack_image(cursor.getString(cursor.getColumnIndex(BACK_IMAGE)));
                    mPlanogram.setFront_image(cursor.getString(cursor.getColumnIndex(FRONT_IMAGE)));
                    mPlanogram.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                    mPlanogram.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mPlanogram;
    }

    //Update Planogram
    public void updatePlanogramPosted(String PlanogramID, String tooldId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_PLANOGRAM,
                contentValueItem, PLANOGRAM_ID + " = ? AND " +
                        DISTRIBUTION_TOOL_ID + " = ? ", new String[]{PlanogramID, tooldId});
    }

    //Update COmplaint Transaction
    public int updateTransaction(String orderNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(TR_IS_POSTED, "Yes");

        int i = db.update(TABLE_TRANSACTION, contentValue,
                TR_ORDER_ID + " = ?", new String[]{orderNo});

        return i;
    }

    public Complain getComplaintPost() {
        Complain mComplain = new Complain();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COMPLAIN + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {
                    do {

                        mComplain.setComplain_Feedback(cursor.getString(cursor.getColumnIndex(COMPLAIN_FEEDBACK)));
                        mComplain.setComplanin_Note(cursor.getString(cursor.getColumnIndex(COMPLAIN_FEEBACK_NOTES)));
                        mComplain.setComplain_brand(cursor.getString(cursor.getColumnIndex(COMPLAIN_BRAND)));
                        mComplain.setComplain_Image1(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image1)));
                        mComplain.setComplain_Image2(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image2)));
                        mComplain.setComplain_Image3(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image3)));
                        mComplain.setComplain_Image4(cursor.getString(cursor.getColumnIndex(COMPLAIN_Image4)));
                        mComplain.setComplainId(cursor.getString(cursor.getColumnIndex(COMPLAINID)));
                        mComplain.setItemId(cursor.getString(cursor.getColumnIndex(ITEM_ID)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mComplain;
    }

    //Update Complain
    public void updateComplainPosted(String ComplainID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_COMPLAIN,
                contentValueItem, COMPLAINID + " = ?", new String[]{ComplainID});
    }

    public Compititor getCompititorPost() {
        Compititor mComplain = new Compititor();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_COMPITITOR + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {
                    do {
                        ;
                        mComplain.setCompititorCompanyName(cursor.getString(cursor.getColumnIndex(COMPITITOR_COMPANY_NAME)));
                        mComplain.setCompititor_brand(cursor.getString(cursor.getColumnIndex(COMPITITOR_BRAND)));
                        mComplain.setCOMPITITOR_Price(cursor.getString(cursor.getColumnIndex(COMPITITOR_PRICE)));
                        mComplain.setCompititor_Promotion(cursor.getString(cursor.getColumnIndex(COMPITITOR_PROMOTION)));
                        mComplain.setCompititor_Notes(cursor.getString(cursor.getColumnIndex(COMPITITOR_NOTES)));
                        mComplain.setCompititor_ItemName(cursor.getString(cursor.getColumnIndex(COMPITITOR_ITEM_NAME)));
                        mComplain.setCompititor_Image1(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image1)));
                        mComplain.setCompititor_Image2(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image2)));
                        mComplain.setCompititor_Image3(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image3)));
                        mComplain.setCompititor_Image4(cursor.getString(cursor.getColumnIndex(COMPITITOR_Image4)));
                        mComplain.setCompititorId(cursor.getString(cursor.getColumnIndex(COMPITITORID)));
//                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mComplain;
    }

    //Update Compititor
    public void updateCampititorPosted(String CampititorId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_COMPITITOR,
                contentValueItem, COMPITITORID + " = ?", new String[]{CampititorId});
    }

    public Compaign getCampaignPost() {
        Compaign mComplain = new Compaign();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CAMPAIGN + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {
                    do {

                        mComplain.setCompaignId(cursor.getString(cursor.getColumnIndex(COMPAIGN_ID)));
                        mComplain.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        mComplain.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                        mComplain.setCompaign_Image1(cursor.getString(cursor.getColumnIndex(Compaign_Image1)));
                        mComplain.setCompaign_Image2(cursor.getString(cursor.getColumnIndex(Compaign_Image2)));
                        mComplain.setCompaign_Image3(cursor.getString(cursor.getColumnIndex(Compaign_Image3)));
                        mComplain.setCompaign_Image4(cursor.getString(cursor.getColumnIndex(Compaign_Image4)));
//                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mComplain;
    }

    public Promotion getPromotionalPost() {
        Promotion mComplain = new Promotion();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_PROMOTION + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {
                    do {

                        mComplain.setPromotioncustomerName(cursor.getString(cursor.getColumnIndex(PROMOTION_CUSTOMERNAME)));
                        mComplain.setPromotioncustPhone(cursor.getString(cursor.getColumnIndex(PROMOTION_CUSTOMERPHONE)));
                        mComplain.setAmount(cursor.getString(cursor.getColumnIndex(PROMOTION_AMOUNT)));
                        mComplain.setPromotionId(cursor.getString(cursor.getColumnIndex(PROMOTIONID)));
                        mComplain.setPromotionItemId(cursor.getString(cursor.getColumnIndex(PROMOTION_ITEMID)));
                        mComplain.setInvoiceNo(cursor.getString(cursor.getColumnIndex(INVOICE_NO)));
                        mComplain.setInvoiceImage(cursor.getString(cursor.getColumnIndex(INVOICE_IMAGE)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mComplain;
    }

    public Promotion getChillerPost() {
        Promotion mComplain = new Promotion();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ADD_CHILLER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {

                    mComplain.setPromotioncustomerName(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    mComplain.setPromotioncustPhone(cursor.getString(cursor.getColumnIndex(FRIDGE_ASSETNUMBER)));
                    mComplain.setPromotionId(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));
                    mComplain.setInvoiceImage(cursor.getString(cursor.getColumnIndex(IMAGE1)));

                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mComplain;
    }

    public Freeze getFridgePost() {
        Freeze mComplain = new Freeze();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ADD_FRIDGE + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {

                    mComplain.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    mComplain.setSerial_no(cursor.getString(cursor.getColumnIndex(FRIDGE_ASSETNUMBER)));
                    mComplain.setSalesman_id(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));
                    mComplain.setImage1(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                    mComplain.setImage2(cursor.getString(cursor.getColumnIndex(IMAGE2)));
                    mComplain.setImage3(cursor.getString(cursor.getColumnIndex(IMAGE3)));
                    mComplain.setImage4(cursor.getString(cursor.getColumnIndex(IMAGE4)));
                    mComplain.setComments(cursor.getString(cursor.getColumnIndex(COMMENT)));
                    mComplain.setComplaint_type(cursor.getString(cursor.getColumnIndex(TYPE)));
                    mComplain.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                    mComplain.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                    mComplain.setHave_fridge(cursor.getString(cursor.getColumnIndex(IS_FREEZE_ASSIGN)));
                    mComplain.setFridge_scan_img(cursor.getString(cursor.getColumnIndex(SIGNATURE)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mComplain;
    }

    public Freeze getChilerTrackPost() {
        Freeze mComplain = new Freeze();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CHILLER_TRACKING + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {

                    mComplain.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    mComplain.setSerial_no(cursor.getString(cursor.getColumnIndex(FRIDGE_ASSETNUMBER)));
                    mComplain.setSalesman_id(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));
                    mComplain.setImage1(cursor.getString(cursor.getColumnIndex(IMAGE1)));
                    mComplain.setImage2(cursor.getString(cursor.getColumnIndex(IMAGE2)));
                    mComplain.setImage3(cursor.getString(cursor.getColumnIndex(IMAGE3)));
                    mComplain.setImage4(cursor.getString(cursor.getColumnIndex(IMAGE4)));
                    mComplain.setComments(cursor.getString(cursor.getColumnIndex(COMMENT)));
                    mComplain.setComplaint_type(cursor.getString(cursor.getColumnIndex(TYPE)));
                    mComplain.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                    mComplain.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));
                    mComplain.setHave_fridge(cursor.getString(cursor.getColumnIndex(IS_FREEZE_ASSIGN)));
                    mComplain.setFridge_scan_img("");
                    mComplain.setRoute_id(cursor.getString(cursor.getColumnIndex(ROUTE_ID)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mComplain;
    }

    public ServiceVisitPost getServiceVisitPost() {
        ServiceVisitPost mComplain = new ServiceVisitPost();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SERVICE_VISIT_POST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {
                    mComplain.serviceType = cursor.getString(cursor.getColumnIndex(SERVICE_TYPE));
                    mComplain.ticketNo = cursor.getString(cursor.getColumnIndex(TICKET_NO));
                    mComplain.timeIn = cursor.getString(cursor.getColumnIndex(TIMEIN));
                    mComplain.timeOut = cursor.getString(cursor.getColumnIndex(TIME_OUT));

                    if (cursor.getString(cursor.getColumnIndex(LATITUDE)) != null) {
                        mComplain.latitude = cursor.getString(cursor.getColumnIndex(LATITUDE));
                    } else {
                        mComplain.latitude = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(LONGITUDE)) != null) {
                        mComplain.longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE));
                    } else {
                        mComplain.longitude = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(SV_SERIAL_NO)) != null) {
                        mComplain.serialNo = cursor.getString(cursor.getColumnIndex(SV_SERIAL_NO));
                    } else {
                        mComplain.serialNo = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(MODEL_NO)) != null) {
                        mComplain.modelNo = cursor.getString(cursor.getColumnIndex(MODEL_NO));
                    } else {
                        mComplain.modelNo = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(ASSETS_NO)) != null) {
                        mComplain.assetNo = cursor.getString(cursor.getColumnIndex(ASSETS_NO));
                    } else {
                        mComplain.assetNo = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(SV_BRANDING)) != null) {
                        mComplain.brand = cursor.getString(cursor.getColumnIndex(SV_BRANDING));
                    } else {
                        mComplain.brand = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(OUTLET_NAME)) != null) {
                        mComplain.outletName = cursor.getString(cursor.getColumnIndex(OUTLET_NAME));
                    } else {
                        mComplain.outletName = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(OWNER_NAME)) != null) {
                        mComplain.ownerName = cursor.getString(cursor.getColumnIndex(OWNER_NAME));
                    } else {
                        mComplain.ownerName = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(LOCATION)) != null) {
                        mComplain.location = cursor.getString(cursor.getColumnIndex(LOCATION));
                    } else {
                        mComplain.location = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(LANDMARK)) != null) {
                        mComplain.landmark = cursor.getString(cursor.getColumnIndex(LANDMARK));
                    } else {
                        mComplain.landmark = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(TOWN_VILLAGE)) != null) {
                        mComplain.townVillage = cursor.getString(cursor.getColumnIndex(TOWN_VILLAGE));
                    } else {
                        mComplain.townVillage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)) != null) {
                        mComplain.contactNumber = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER));
                    } else {
                        mComplain.contactNumber = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER2)) != null) {
                        mComplain.contactNumber2 = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER2));
                    } else {
                        mComplain.contactNumber2 = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(CONTACT_PERSON)) != null) {
                        mComplain.contactPerson = cursor.getString(cursor.getColumnIndex(CONTACT_PERSON));
                    } else {
                        mComplain.contactPerson = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(SV_NATURE)) != null) {
                        mComplain.natureOfCall = cursor.getString(cursor.getColumnIndex(SV_NATURE));
                    } else {
                        mComplain.natureOfCall = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(WORK_STATUS)) != null) {
                        mComplain.workstatus = cursor.getString(cursor.getColumnIndex(WORK_STATUS));
                    } else {
                        mComplain.workstatus = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(PENDING_SPARE)) != null) {
                        mComplain.pendingSpare = cursor.getString(cursor.getColumnIndex(PENDING_SPARE));
                    } else {
                        mComplain.pendingSpare = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(PENDING_REASON)) != null) {
                        mComplain.pendingReason = cursor.getString(cursor.getColumnIndex(PENDING_REASON));
                    } else {
                        mComplain.pendingReason = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(SV_COMMENT)) != null) {
                        mComplain.workDoneComment = cursor.getString(cursor.getColumnIndex(SV_COMMENT));
                    } else {
                        mComplain.workDoneComment = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(SV_COMPLAIN_TYPE)) != null) {
                        mComplain.workDoneType = cursor.getString(cursor.getColumnIndex(SV_COMPLAIN_TYPE));
                    } else {
                        mComplain.workDoneType = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(SV_DISPUTE)) != null) {
                        mComplain.anyDispute = cursor.getString(cursor.getColumnIndex(SV_DISPUTE));
                    } else {
                        mComplain.anyDispute = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(DISPUTE_IMAGE1)) != null) {
                        mComplain.disputeImage1 = cursor.getString(cursor.getColumnIndex(DISPUTE_IMAGE1));
                    } else {
                        mComplain.disputeImage1 = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(DISPUTE_IMAGE2)) != null) {
                        mComplain.disputeImage2 = cursor.getString(cursor.getColumnIndex(DISPUTE_IMAGE2));
                    } else {
                        mComplain.disputeImage2 = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(CURRENT_VOLT)) != null) {
                        mComplain.currentVolt = cursor.getString(cursor.getColumnIndex(CURRENT_VOLT));
                    } else {
                        mComplain.currentVolt = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(AMPS)) != null) {
                        mComplain.amps = cursor.getString(cursor.getColumnIndex(AMPS));
                    } else {
                        mComplain.amps = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(TEMPRATURE)) != null) {
                        mComplain.temprature = cursor.getString(cursor.getColumnIndex(TEMPRATURE));
                    } else {
                        mComplain.temprature = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(SPARE_DETAIL)) != null) {
                        mComplain.workSpare = cursor.getString(cursor.getColumnIndex(SPARE_DETAIL));
                    } else {
                        mComplain.workSpare = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(TECH_RATING)) != null) {
                        mComplain.techRating = cursor.getString(cursor.getColumnIndex(TECH_RATING));
                    } else {
                        mComplain.techRating = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(QUALITY_RATE)) != null) {
                        mComplain.qualityRate = cursor.getString(cursor.getColumnIndex(QUALITY_RATE));
                    } else {
                        mComplain.qualityRate = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(IMAGE1)) != null) {
                        mComplain.serialImage = cursor.getString(cursor.getColumnIndex(IMAGE1));
                    } else {
                        mComplain.serialImage = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(IS_WORKING_ANY)).equals("Yes")) {
                        mComplain.workingId = "1";
                    } else {
                        mComplain.workingId = "0";
                    }

                    if (cursor.getString(cursor.getColumnIndex(WORKING_IMAGE)) != null) {
                        mComplain.conditionImage = cursor.getString(cursor.getColumnIndex(WORKING_IMAGE));
                    } else {
                        mComplain.conditionImage = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(CLEANLESS_ID)).equals("Yes")) {
                        mComplain.cleanlessId = "1";
                    } else {
                        mComplain.cleanlessId = "0";
                    }
                    if (cursor.getString(cursor.getColumnIndex(CLENLESS_IMAGE)) != null) {
                        mComplain.cleanlessImage = cursor.getString(cursor.getColumnIndex(CLENLESS_IMAGE));
                    } else {
                        mComplain.cleanlessImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(COIL_ID)).equals("Yes")) {
                        mComplain.coilId = "1";
                    } else {
                        mComplain.coilId = "0";
                    }
                    if (cursor.getString(cursor.getColumnIndex(COIL_IMAGE)) != null) {
                        mComplain.coilImage = cursor.getString(cursor.getColumnIndex(COIL_IMAGE));
                    } else {
                        mComplain.coilImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(GASKET_ID)).equals("Yes")) {
                        mComplain.gasketId = "1";
                    } else {
                        mComplain.gasketId = "0";
                    }
                    if (cursor.getString(cursor.getColumnIndex(GASKET_IMAGE)) != null) {
                        mComplain.gasketImage = cursor.getString(cursor.getColumnIndex(GASKET_IMAGE));
                    } else {
                        mComplain.gasketImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(BRANDING_ID)).equals("Yes")) {
                        mComplain.brandingId = "1";
                    } else {
                        mComplain.brandingId = "0";
                    }
                    if (cursor.getString(cursor.getColumnIndex(BRANDING_IMAGE)) != null) {
                        mComplain.brandingImage = cursor.getString(cursor.getColumnIndex(BRANDING_IMAGE));
                    } else {
                        mComplain.brandingImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(LIGHT_ID)).equals("Yes")) {
                        mComplain.lightId = "1";
                    } else {
                        mComplain.lightId = "0";
                    }
                    if (cursor.getString(cursor.getColumnIndex(LIGHT_IMAGE)) != null) {
                        mComplain.lightImage = cursor.getString(cursor.getColumnIndex(LIGHT_IMAGE));
                    } else {
                        mComplain.lightImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(VENTILATION_ID)).equals("Yes")) {
                        mComplain.ventilationId = "1";
                    } else {
                        mComplain.ventilationId = "0";
                    }
                    if (cursor.getString(cursor.getColumnIndex(VENTILATION_IMAGE)) != null) {
                        mComplain.ventilationImage = cursor.getString(cursor.getColumnIndex(VENTILATION_IMAGE));
                    } else {
                        mComplain.ventilationImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(LEVELING_ID)).equals("Yes")) {
                        mComplain.levelingId = "1";
                    } else {
                        mComplain.levelingId = "0";
                    }
                    if (cursor.getString(cursor.getColumnIndex(LEVELING_IMAGE)) != null) {
                        mComplain.levelingImage = cursor.getString(cursor.getColumnIndex(LEVELING_IMAGE));
                    } else {
                        mComplain.levelingImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(STOCK_ID)) != null) {
                        mComplain.stockPer = cursor.getString(cursor.getColumnIndex(STOCK_ID));
                    } else {
                        mComplain.stockPer = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(STOCK_IMAGE)) != null) {
                        mComplain.stockImage = cursor.getString(cursor.getColumnIndex(STOCK_IMAGE));
                    } else {
                        mComplain.stockImage = "";
                    }
                    if (cursor.getString(cursor.getColumnIndex(SIGNATURE)) != null) {
                        mComplain.customerSignature = cursor.getString(cursor.getColumnIndex(SIGNATURE));
                    } else {
                        mComplain.customerSignature = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(CTS_STATUS)) != null) {
                        mComplain.ctsStatus = cursor.getString(cursor.getColumnIndex(CTS_STATUS));
                    } else {
                        mComplain.ctsStatus = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(COOLER_IMAGE1)) != null) {
                        mComplain.coolerImage1 = cursor.getString(cursor.getColumnIndex(COOLER_IMAGE1));
                    } else {
                        mComplain.coolerImage1 = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(COOLER_IMAGE2)) != null) {
                        mComplain.coolerImage2 = cursor.getString(cursor.getColumnIndex(COOLER_IMAGE2));
                    } else {
                        mComplain.coolerImage2 = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(DISTRICT)) != null) {
                        mComplain.district = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    } else {
                        mComplain.district = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(CTS_COMMENT)) != null) {
                        mComplain.ctcComment = cursor.getString(cursor.getColumnIndex(CTS_COMMENT));
                    } else {
                        mComplain.ctcComment = "";
                    }

                    if (cursor.getString(cursor.getColumnIndex(OTHER_COMMENT)) != null) {
                        mComplain.otherSpecific = cursor.getString(cursor.getColumnIndex(OTHER_COMMENT));
                    } else {
                        mComplain.otherSpecific = "";
                    }

                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mComplain;
    }

    public Chiller_Model getChillerRequestPost() {
        Chiller_Model mPromotion = new Chiller_Model();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CHILLER_REQUEST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {

                    mPromotion.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    mPromotion.setdepot_id(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));
                    mPromotion.setContact_number(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                    mPromotion.setPostal_address(cursor.getString(cursor.getColumnIndex(POSTAL_ADDRESS)));
                    mPromotion.setLandmark(cursor.getString(cursor.getColumnIndex(LANDMARK)));
                    mPromotion.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
                    mPromotion.setOutlet_type(cursor.getString(cursor.getColumnIndex(OUTLET_TYPE)));
                    mPromotion.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                    mPromotion.setSpecify_if_other_type(cursor.getString(cursor.getColumnIndex(OTHER_TYPE)));
                    mPromotion.setExisting_coolers(cursor.getString(cursor.getColumnIndex(EXIST_COOLER)));
                    mPromotion.setStock_share_with_competitor(cursor.getString(cursor.getColumnIndex(STOCK_COMPATITER)));
                    mPromotion.setOutlet_weekly_sale_volume(cursor.getString(cursor.getColumnIndex(WEEKLY_SALE_VOLUME)));
                    mPromotion.setOutlet_weekly_sales(cursor.getString(cursor.getColumnIndex(WEEKLY_SALES)));
                    mPromotion.setChiller_size_requested(cursor.getString(cursor.getColumnIndex(SIZE_REQUEST)));
                    mPromotion.setChiller_safty_grill(cursor.getString(cursor.getColumnIndex(SAFTY_GRILL)));
                    mPromotion.setDisplay_location(cursor.getString(cursor.getColumnIndex(DISPLAY_LOCATION)));

                    mPromotion.setNational_id(cursor.getString(cursor.getColumnIndex(NATIONAL_ID)));
                    mPromotion.setPassword_photo(cursor.getString(cursor.getColumnIndex(PASSWORD_PHOTO)));
                    mPromotion.setOutlet_address_proof(cursor.getString(cursor.getColumnIndex(ADDRESS_PROOF)));
                    mPromotion.setLc_letter(cursor.getString(cursor.getColumnIndex(LC_LETTER)));
                    mPromotion.setTrading_licence(cursor.getString(cursor.getColumnIndex(TREDING_LICENCE)));
                    mPromotion.setOutlet_stamp(cursor.getString(cursor.getColumnIndex(STAMP)));
                    mPromotion.setSign__customer_file(cursor.getString(cursor.getColumnIndex(SIGNATURE)));

                    mPromotion.setNational_id_file(cursor.getString(cursor.getColumnIndex(NATIONAL_ID_FRONT)));
                    mPromotion.setNational_id1_file(cursor.getString(cursor.getColumnIndex(NATIONAL_ID_BACK)));
                    mPromotion.setOutlet_stamp_file(cursor.getString(cursor.getColumnIndex(STAMP_FRONT)));
                    mPromotion.setOutlet_stamp1_file(cursor.getString(cursor.getColumnIndex(STAMP_BACK)));
                    mPromotion.setTrading_licence_file(cursor.getString(cursor.getColumnIndex(TRADING_FRONT)));
                    mPromotion.setTrading_licence1_file(cursor.getString(cursor.getColumnIndex(TRADING_BACK)));
                    mPromotion.setOutlet_address_proof_file(cursor.getString(cursor.getColumnIndex(ADDRESS_PROOF_FRONT)));
                    mPromotion.setOutlet_address_proof1_file(cursor.getString(cursor.getColumnIndex(ADDRESS_PROOF_BACK)));
                    mPromotion.setLc_letter_file(cursor.getString(cursor.getColumnIndex(LC_FRONT)));
                    mPromotion.setLc_letter1_file(cursor.getString(cursor.getColumnIndex(LC_BACK)));
                    mPromotion.setPassword_photo_file(cursor.getString(cursor.getColumnIndex(PASSPORT_ID_FRONT)));
                    mPromotion.setPassword_photo1_file(cursor.getString(cursor.getColumnIndex(PASSPORT_ID_BACK)));


                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mPromotion;
    }

    public Chiller_Model getAddChillerRequestPost() {
        Chiller_Model mPromotion = new Chiller_Model();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CHILLER_ADD + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {

                    mPromotion.setCustomer_id(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    mPromotion.setdepot_id(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));
                    mPromotion.setSerialNo(cursor.getString(cursor.getColumnIndex(SERIAL_NO)));
                    mPromotion.setContact_number(cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER)));
                    mPromotion.setPostal_address(cursor.getString(cursor.getColumnIndex(POSTAL_ADDRESS)));
                    mPromotion.setLandmark(cursor.getString(cursor.getColumnIndex(LANDMARK)));
                    mPromotion.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
                    mPromotion.setOutlet_type(cursor.getString(cursor.getColumnIndex(OUTLET_TYPE)));
                    mPromotion.setOwner_name(cursor.getString(cursor.getColumnIndex(OWNER_NAME)));
                    mPromotion.setSpecify_if_other_type(cursor.getString(cursor.getColumnIndex(OTHER_TYPE)));
                    mPromotion.setExisting_coolers(cursor.getString(cursor.getColumnIndex(EXIST_COOLER)));
                    mPromotion.setStock_share_with_competitor(cursor.getString(cursor.getColumnIndex(STOCK_COMPATITER)));
                    mPromotion.setOutlet_weekly_sale_volume(cursor.getString(cursor.getColumnIndex(WEEKLY_SALE_VOLUME)));
                    mPromotion.setOutlet_weekly_sales(cursor.getString(cursor.getColumnIndex(WEEKLY_SALES)));
                    mPromotion.setChiller_size_requested(cursor.getString(cursor.getColumnIndex(SIZE_REQUEST)));
                    mPromotion.setChiller_safty_grill(cursor.getString(cursor.getColumnIndex(SAFTY_GRILL)));
                    mPromotion.setDisplay_location(cursor.getString(cursor.getColumnIndex(DISPLAY_LOCATION)));

                    mPromotion.setNational_id(cursor.getString(cursor.getColumnIndex(NATIONAL_ID)));
                    mPromotion.setPassword_photo(cursor.getString(cursor.getColumnIndex(PASSWORD_PHOTO)));
                    mPromotion.setOutlet_address_proof(cursor.getString(cursor.getColumnIndex(ADDRESS_PROOF)));
                    mPromotion.setLc_letter(cursor.getString(cursor.getColumnIndex(LC_LETTER)));
                    mPromotion.setTrading_licence(cursor.getString(cursor.getColumnIndex(TREDING_LICENCE)));
                    mPromotion.setOutlet_stamp(cursor.getString(cursor.getColumnIndex(STAMP)));
                    mPromotion.setSign__customer_file(cursor.getString(cursor.getColumnIndex(SIGNATURE)));
                    mPromotion.setSalesmanSignature(cursor.getString(cursor.getColumnIndex(SALESMAN_SIGNATURE)));

                    mPromotion.setNational_id_file(cursor.getString(cursor.getColumnIndex(NATIONAL_ID_FRONT)));
                    mPromotion.setNational_id1_file(cursor.getString(cursor.getColumnIndex(NATIONAL_ID_BACK)));
                    mPromotion.setOutlet_stamp_file(cursor.getString(cursor.getColumnIndex(STAMP_FRONT)));
                    mPromotion.setOutlet_stamp1_file(cursor.getString(cursor.getColumnIndex(STAMP_BACK)));
                    mPromotion.setTrading_licence_file(cursor.getString(cursor.getColumnIndex(TRADING_FRONT)));
                    mPromotion.setTrading_licence1_file(cursor.getString(cursor.getColumnIndex(TRADING_BACK)));
                    mPromotion.setOutlet_address_proof_file(cursor.getString(cursor.getColumnIndex(ADDRESS_PROOF_FRONT)));
                    mPromotion.setOutlet_address_proof1_file(cursor.getString(cursor.getColumnIndex(ADDRESS_PROOF_BACK)));
                    mPromotion.setLc_letter_file(cursor.getString(cursor.getColumnIndex(LC_FRONT)));
                    mPromotion.setLc_letter1_file(cursor.getString(cursor.getColumnIndex(LC_BACK)));
                    mPromotion.setPassword_photo_file(cursor.getString(cursor.getColumnIndex(PASSPORT_ID_FRONT)));
                    mPromotion.setPassword_photo1_file(cursor.getString(cursor.getColumnIndex(PASSPORT_ID_BACK)));
                    mPromotion.setChillerImage(cursor.getString(cursor.getColumnIndex(CHILLER_IMAGE)));


                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mPromotion;
    }

    //Update Campaign
    public void updateCampaignPosted(String CampaignId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_CAMPAIGN,
                contentValueItem, COMPAIGN_ID + " = ?", new String[]{CampaignId});
    }

    //Update Promotional
    public void updatePromotionalPosted(String CampaignId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_PROMOTION,
                contentValueItem, PROMOTIONID + " = ?", new String[]{CampaignId});
    }

    //Update Promotional
    public int updateChillerPosted(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_ADD_CHILLER,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;

    }

    //Update Promotional
    public int updateFridggePosted(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_ADD_FRIDGE,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;

    }

    //Update Promotional
    public int updateChillerRequestPosted(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CHILLER_REQUEST,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;

    }

    //Update Promotional
    public int updateChillerTrackPosted(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CHILLER_TRACKING,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;

    }

    //Update Promotional
    public int updateServiceVisitPosted(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_SERVICE_VISIT_POST,
                contentValues, TICKET_NO + " = ? ", new String[]{orderNo});
        return i;

    }

    //Update Promotional
    public int updateAddChillerRequestPosted(String orderNo, String status) {
        ContentValues contentValues = new ContentValues();
        if (status.equalsIgnoreCase("1")) {
            contentValues.put(DATA_MARK_FOR_POST, "Y");
            contentValues.put(IS_POSTED, "1");
        } else {
            contentValues.put(DATA_MARK_FOR_POST, "E");
            contentValues.put(IS_POSTED, "0");
        }

        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.update(TABLE_CHILLER_ADD,
                contentValues, UNIQUE_ID + " = ? ", new String[]{orderNo});
        return i;

    }

    //GET Assets Detail
    public ASSETS_MODEL getPostAssetsDetail() {

        ASSETS_MODEL mOrder = new ASSETS_MODEL();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_ASSETS + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    mOrder.setAssetsId(cursor.getString(cursor.getColumnIndex(ASSET_ID)));
                    mOrder.setAssetsFeedback(cursor.getString(cursor.getColumnIndex(ASSETS_FEEDBACK)));
                    mOrder.setAssetsImage1(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE1)));
                    mOrder.setAssetsImage2(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE2)));
                    mOrder.setAssetsImage3(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE3)));
                    mOrder.setAssetsImage4(cursor.getString(cursor.getColumnIndex(ASSETS_IMAGE4)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mOrder;
    }

    //Update Compititor
    public void updateAssetsPosted(String CampititorId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_CUSTOMER_ASSETS,
                contentValueItem, ASSET_ID + " = ?", new String[]{CampititorId});
    }

    public String getExpiry() {
        String surveyId = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_EXPIRY_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    surveyId = cursor.getString(cursor.getColumnIndex(UNIQUESURVEY_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return surveyId;
    }

    public JsonArray getExpiryAnswer(String id) {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_EXPIRY_ITEM + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + UNIQUESURVEY_ID + "=?", new String[]{"M", id});

            try {

                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("distribution_tool_id", cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                        mItem.addProperty("uom", cursor.getString(cursor.getColumnIndex(UOM)));
                        mItem.addProperty("pc", cursor.getString(cursor.getColumnIndex(PC)));
                        mItem.addProperty("expriydate", cursor.getString(cursor.getColumnIndex(EXPIRY_DATE)));
                        mItem.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }

    //Update Expiry List
    public void updateExpiryListPosted(String uniqId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHeader = new ContentValues();
        contentValueHeader.put(DATA_MARK_FOR_POST, "Y");
        contentValueHeader.put(IS_POSTED, "1");
        db.update(TABLE_EXPIRY_HEADER,
                contentValueHeader, UNIQUESURVEY_ID + " = ?", new String[]{uniqId});

        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");
        db.update(TABLE_EXPIRY_ITEM,
                contentValueItem, UNIQUESURVEY_ID + " = ?", new String[]{uniqId});
    }

    public String getDamage() {
        String surveyId = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DAMAGED_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    surveyId = cursor.getString(cursor.getColumnIndex(UNIQUESURVEY_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return surveyId;
    }

    public JsonArray getDamagedAnswer(String id) {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DAMAGED_ITEM + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + UNIQUESURVEY_ID + "=?", new String[]{"M", id});

            try {

                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("distribution_tool_id", cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                        mItem.addProperty("damage_item", cursor.getString(cursor.getColumnIndex(DAMAGED_ITEM)));
                        mItem.addProperty("expired_item", cursor.getString(cursor.getColumnIndex(EXPIRED_ITEM)));
                        mItem.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        mItem.addProperty("saleable_item", cursor.getString(cursor.getColumnIndex(SALEABLE_ITEM)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }

    //Update Damaged List
    public void updateDamagedListPosted(String uniqId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValueHeader = new ContentValues();
        contentValueHeader.put(DATA_MARK_FOR_POST, "Y");
        contentValueHeader.put(IS_POSTED, "1");
        db.update(TABLE_DAMAGED_HEADER,
                contentValueHeader, UNIQUESURVEY_ID + " = ?", new String[]{uniqId});

        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");
        db.update(TABLE_DAMAGED_ITEM,
                contentValueItem,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});
    }

    public String getToolSurvey() {
        String surveyId = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SURVEY_POST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    surveyId = cursor.getString(cursor.getColumnIndex(UNIQUESURVEY_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return surveyId;
    }

    public JsonArray getSurveyAnswer(String uniqId) {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SURVEY_POST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + UNIQUESURVEY_ID + "=?", new String[]{"M", uniqId});

            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("survey_id", cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        mItem.addProperty("question_id", cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mItem.addProperty("answer", cursor.getString(cursor.getColumnIndex(ANSWER_TEXT)));
                        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                        mItem.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }


    //Update Survey
    public void updateSurveyPosted(JsonObject jObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        String uniqId = jObj.get("answer_identifer").getAsString();
        ContentValues contentValueHeader = new ContentValues();
        contentValueHeader.put(DATA_MARK_FOR_POST, "Y");
        contentValueHeader.put(IS_POSTED, "1");

        db.update(TABLE_SURVEY_POST_HEADER,
                contentValueHeader,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});

        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_SURVEY_POST,
                contentValueItem,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});

    }

    public String getAssetsSurvey() {
        String surveyId = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ASSETS_POST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    surveyId = cursor.getString(cursor.getColumnIndex(UNIQUESURVEY_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return surveyId;
    }

    public JsonArray getAssetsSurveyAnswer(String uniqId) {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ASSETS_POST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + UNIQUESURVEY_ID + "=?", new String[]{"M", uniqId});

            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("survey_id", cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        mItem.addProperty("question_id", cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mItem.addProperty("answer", cursor.getString(cursor.getColumnIndex(ANSWER_TEXT)));
                        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                        mItem.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }

    //Update Assets survey
    public void updateAssetsSurveyPosted(JsonObject jObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        String uniqId = jObj.get("answer_identifer").getAsString();
        ContentValues contentValueHeader = new ContentValues();
        contentValueHeader.put(DATA_MARK_FOR_POST, "Y");
        contentValueHeader.put(IS_POSTED, "1");

        db.update(TABLE_ASSETS_POST_HEADER,
                contentValueHeader,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});

        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_ASSETS_POST,
                contentValueItem,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});
    }

    public HashMap<String, String> getSensorySurvey() {
        HashMap<String, String> mItem = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SENSOR_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    mItem.put("uniqueid", cursor.getString(cursor.getColumnIndex(UNIQUESURVEY_ID)));
                    mItem.put("surveyId", cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                    mItem.put("customer_name", cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
                    mItem.put("customer_email", cursor.getString(cursor.getColumnIndex(CUSTOMER_EMAIL)));
                    mItem.put("customer_phone", cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE)));
                    mItem.put("customer_address", cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mItem;
    }

    public JsonArray getSensoryAnswer(String uniqId) {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_SENSOR_POST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + UNIQUESURVEY_ID + "=?", new String[]{"M", uniqId});
            try {

                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("survey_id", cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        mItem.addProperty("question_id", cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mItem.addProperty("answer", cursor.getString(cursor.getColumnIndex(ANSWER_TEXT)));
                        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                        mItem.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }

    //Update Survey
    public void updateSensoryPosted(JsonObject jObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        String uniqId = jObj.get("answer_identifer").getAsString();
        ContentValues contentValueHeader = new ContentValues();
        contentValueHeader.put(DATA_MARK_FOR_POST, "Y");
        contentValueHeader.put(IS_POSTED, "1");

        db.update(TABLE_SENSOR_HEADER,
                contentValueHeader,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});


        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_SENSOR_POST,
                contentValueItem,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});
    }


    public String getConsumerSurvey() {
        String surveyId = "";

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CONSUMER_POST_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {
                if (cursor.moveToFirst()) {
                    surveyId = cursor.getString(cursor.getColumnIndex(UNIQUESURVEY_ID));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return surveyId;
    }


    public JsonArray getConsumerSurveyAnswer(String surveyID) {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CONSUMER_POST + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + UNIQUESURVEY_ID + "=?", new String[]{"M", surveyID});

            try {
                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("survey_id", cursor.getString(cursor.getColumnIndex(SURVEY_ID)));
                        mItem.addProperty("question_id", cursor.getString(cursor.getColumnIndex(QUESTION_ID)));
                        mItem.addProperty("answer", cursor.getString(cursor.getColumnIndex(ANSWER_TEXT)));
                        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                        mItem.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUST_ID)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }


    //Update Consumer Survey
    public void updateConsumerSurveyPosted(JsonObject jObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        String uniqId = jObj.get("answer_identifer").getAsString();
        ContentValues contentValueHeader = new ContentValues();
        contentValueHeader.put(DATA_MARK_FOR_POST, "Y");
        contentValueHeader.put(IS_POSTED, "1");

        db.update(TABLE_CONSUMER_POST_HEADER,
                contentValueHeader,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});


        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_CONSUMER_POST,
                contentValueItem,
                UNIQUESURVEY_ID + " = ?", new String[]{uniqId});
    }

    //GET Sensor Survey Detail
    public HashMap<String, String> getPostInventoryDetail() {

        HashMap<String, String> mOrder = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_INVENTORY_HEADER + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});
            try {
                if (cursor.moveToFirst()) {
                    mOrder.put("inventoryId", cursor.getString(cursor.getColumnIndex(INVENTORY_ID)));
                    mOrder.put("cusId", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                    mOrder.put("invNo", cursor.getString(cursor.getColumnIndex(INVENTORYNO)));
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mOrder;
    }


    public JsonArray getInventoryExpiry(String custId) {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_INVENTORY + " WHERE " +
                    DATA_MARK_FOR_POST + "=?" + " AND "
                    + CUSTOMER_ID + "=?", new String[]{"M", custId});

            try {

                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("inventory_id", cursor.getString(cursor.getColumnIndex(INVENTORY_ID)));
                        mItem.addProperty("customerid", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.addProperty("uom_id", cursor.getString(cursor.getColumnIndex(ITEM_UOM)));
                        mItem.addProperty("uom_name", getUOM(cursor.getString(cursor.getColumnIndex(ITEM_UOM))));
                        mItem.addProperty("qty", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                        mItem.addProperty("expiry", cursor.getString(cursor.getColumnIndex(EXPIRY_DATE)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }

    //Update Inventory
    public void updateInventoryPosted(JsonObject jObj, String invNo) {
        SQLiteDatabase db = this.getWritableDatabase();

        JsonArray jArra = jObj.getAsJsonArray("answer_array");

        ContentValues contentValueHeader = new ContentValues();
        contentValueHeader.put(DATA_MARK_FOR_POST, "Y");
        contentValueHeader.put(IS_POSTED, "1");

        db.update(TABLE_CUSTOMER_INVENTORY_HEADER,
                contentValueHeader,
                INVENTORYNO + " = ?", new String[]{invNo});


        for (int i = 0; i < jArra.size(); i++) {

            JsonObject jsonObject = jArra.get(i).getAsJsonObject();

            String customerId = jsonObject.get("customerid").getAsString();
            String inventoryId = jsonObject.get("inventory_id").getAsString();

            ContentValues contentValueItem = new ContentValues();
            contentValueItem.put(DATA_MARK_FOR_POST, "Y");
            contentValueItem.put(IS_POSTED, "1");

            db.update(TABLE_CUSTOMER_INVENTORY,
                    contentValueItem, CUSTOMER_ID + " = ? AND " +
                            INVENTORY_ID + " = ?", new String[]{customerId, inventoryId});
        }
    }


    public HashMap<String, String> getDistImagePost() {
        HashMap<String, String> mComplain = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION_IMAGE + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {
                    do {

                        mComplain.put("tool_id", cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        mComplain.put("customerId", cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
                        mComplain.put("image1", cursor.getString(cursor.getColumnIndex(IMAGE1)));
                        mComplain.put("image2", cursor.getString(cursor.getColumnIndex(IMAGE2)));
                        mComplain.put("image3", cursor.getString(cursor.getColumnIndex(IMAGE3)));
                        mComplain.put("image4", cursor.getString(cursor.getColumnIndex(IMAGE4)));

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return mComplain;
    }


    //Update Campaign
    public void updateDistrImagePosted(String customerId, String CampaignId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValueItem = new ContentValues();
        contentValueItem.put(DATA_MARK_FOR_POST, "Y");
        contentValueItem.put(IS_POSTED, "1");

        db.update(TABLE_DISTRIBUTION_IMAGE,
                contentValueItem, CUSTOMER_ID + " = ? AND " +
                        DISTRIBUTION_TOOL_ID + " = ? ", new String[]{customerId, CampaignId});
    }


    public JsonArray getStockDistri() {
        JsonArray arrItem = new JsonArray();

        JsonObject mItem;

        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DISTRIBUTION + " WHERE " +
                    DATA_MARK_FOR_POST + "=?", new String[]{"M"});

            try {

                if (cursor.moveToFirst()) {
                    do {
                        mItem = new JsonObject();
                        mItem.addProperty("tool_id", cursor.getString(cursor.getColumnIndex(DISTRIBUTION_TOOL_ID)));
                        mItem.addProperty("item_id", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                        mItem.addProperty("customer_id", cursor.getString(cursor.getColumnIndex(CUST_ID)));
                        mItem.addProperty("stock", cursor.getString(cursor.getColumnIndex(FILL_QTY)));
                        arrItem.add(mItem);

                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return arrItem;
    }

    //Update Survey
    public void updateDistriStockPosted(JsonObject jObj) {
        SQLiteDatabase db = this.getWritableDatabase();

        JsonArray jArra = jObj.getAsJsonArray("items_array");

        for (int i = 0; i < jArra.size(); i++) {

            JsonObject jsonObject = jArra.get(i).getAsJsonObject();

            String inventoryId = jsonObject.get("tool_id").getAsString();
            String customerId = jsonObject.get("customer_id").getAsString();
            String itemId = jsonObject.get("item_id").getAsString();

            ContentValues contentValueItem = new ContentValues();
            contentValueItem.put(DATA_MARK_FOR_POST, "Y");
            contentValueItem.put(IS_POSTED, "1");

            db.update(TABLE_DISTRIBUTION,
                    contentValueItem,
                    CUST_ID + " = ? AND " +
                            DISTRIBUTION_TOOL_ID + " = ? AND " +
                            ITEM_ID + " = ? ", new String[]{customerId, inventoryId, itemId});
        }
    }

    public Promotion_Item getPromotionalItemDetail(String stAmt) {
        Promotion_Item mSalesman = new Promotion_Item();

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String selectQuery = "SELECT * FROM " + TABLE_PROMOTION_ITEM + " WHERE " +
                    AMOUNT + " >= " + stAmt + " ORDER BY " + AMOUNT + " DESC LIMIT 1";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.getCount();
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        mSalesman.setItem_Id(cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                        mSalesman.setPromotionId(cursor.getString(cursor.getColumnIndex(PROMOTIONAL_ID)));
                        mSalesman.setItem_Name(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                        mSalesman.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));
                        mSalesman.setFrom_Date(cursor.getString(cursor.getColumnIndex(FROM_DATE)));
                        mSalesman.setTo_Date(cursor.getString(cursor.getColumnIndex(TO_DATE)));

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSalesman;
    }

    //INSERT PRomotion Accountablity
    public void insertPromotion(Promotion mSalesman) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();

        contentValue.put(PROMOTIONID, mSalesman.getPromotionId());
        contentValue.put(PROMOTION_CUSTOMERNAME, mSalesman.getPromotioncustomerName());
        contentValue.put(PROMOTION_CUSTOMERPHONE, mSalesman.getPromotioncustPhone());
        contentValue.put(PROMOTION_AMOUNT, mSalesman.getAmount());
        contentValue.put(PROMOTION_ITEMID, mSalesman.getPromotionItemId());
        contentValue.put(PROMOTION_ITEMNAME, mSalesman.getPromotionItemName());
        contentValue.put(INVOICE_NO, mSalesman.getInvoiceNo());
        contentValue.put(INVOICE_IMAGE, mSalesman.getInvoiceImage());
        contentValue.put(DATA_MARK_FOR_POST, "M");
        contentValue.put(IS_POSTED, "0");

        db.insert(TABLE_PROMOTION, null, contentValue);


    }

    public ArrayList<Promotion> getPromotionList() {
        ArrayList<Promotion> arrData = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PROMOTION;

        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Promotion item = new Promotion();
                        item.setPromotionId(cursor.getString(cursor.getColumnIndex(PROMOTIONID)));
                        item.setPromotioncustomerName(cursor.getString(cursor.getColumnIndex(PROMOTION_CUSTOMERNAME)));
                        item.setPromotioncustPhone(cursor.getString(cursor.getColumnIndex(PROMOTION_CUSTOMERPHONE)));
                        item.setPromotionItemId(cursor.getString(cursor.getColumnIndex(PROMOTION_ITEMID)));
                        item.setPromotionItemName(cursor.getString(cursor.getColumnIndex(PROMOTION_ITEMNAME)));
                        item.setAmount(cursor.getString(cursor.getColumnIndex(PROMOTION_AMOUNT)));
                        item.setInvoiceNo(cursor.getString(cursor.getColumnIndex(INVOICE_NO)));
                        item.setInvoiceImage(cursor.getString(cursor.getColumnIndex(INVOICE_IMAGE)));

                        arrData.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                try {
                    db.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }
        return arrData;
    }

    public boolean checkIsCustomerExist(String customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_CUSTOMER + " WHERE " +
                CUSTOMER_ID + "=?", new String[]{customerId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsCustomerTechnicianExist(String customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_CUSTOMER_TECHNICIAN + " WHERE " +
                CUSTOMER_ID + "=?", new String[]{customerId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsCustomerchillTechnicianExist(String customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_CUSTOMER_CHIllER_TECHNICIAN + " WHERE " +
                CUSTOMER_ID + "=?", new String[]{customerId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public boolean checkIsChillerTechnicianExist(String fridge_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_CHILLER_TECHNICIAN + " WHERE " +
                FRIDGE_ID + "=?", new String[]{fridge_id});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsItemExist(String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_ITEM + " WHERE " +
                ITEM_ID + "=?", new String[]{itemId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsLoadExist(String load_no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_LOAD_HEADER + " WHERE " +
                LOAD_ID + "=?", new String[]{load_no});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsDeliveryExist(String delivery_no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_DELIVERY_HEADER + " WHERE " +
                DELIVERY_ID + "=?", new String[]{delivery_no});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsReturnExist(String return_no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_RETURN_HEADER + " WHERE " +
                KEY_ORDER_ID + "=?", new String[]{return_no});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsLoadItemExist(String load_no, String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_LOAD_ITEMS + " WHERE " +
                SUB_LOAD_ID + "=?" + " AND "
                + ITEM_ID + "=?", new String[]{load_no, itemId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsNatureExist(String ticketNo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_NATURE_OF_CALL + " WHERE " +
                TICKET_NO + "=?", new String[]{ticketNo});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsBaseUOM(String uom, String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_ITEM + " WHERE " +
                ITEM_BASEUOM + "=?" + " AND "
                + ITEM_ID + "=?", new String[]{uom, itemId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsAlterUOM(String uom, String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                TABLE_ITEM + " WHERE " +
                ITEM_ALRT_UOM + "=?" + " AND "
                + ITEM_ID + "=?", new String[]{uom, itemId});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //get Material Count
    public String getMaterialCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_ITEM, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get Customer Count
    public String getCustomerCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get Customer Count
    public String getDepotCustomerCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }


    //get Customer Count
    public String getAgentCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get Customer Count
    public String getCustomerCategoryCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_CATEGORY, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get UOM Count
    public String getUOMCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_UOM, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get Agent Price Count
    public String getAgentPriceCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_AGENT_PRICING, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get Agent Price Count
    public String getDepotCusCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_DEPOT_CUSTOMER, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }

    //get Customer Price Count
    public String getCustomerPriceCount() {

        String basPrice = "0";

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    TABLE_CUSTOMER_PRICING, null);
            basPrice = String.valueOf(cursor.getCount());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return basPrice;
    }


    public boolean deleteReturnItem(String strOrder) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RETURN_ITEMS, ORDER_NO + " = '" + strOrder + "'", null) > 0;
    }

    public boolean deleteDeliveryItem(String strOrder, String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DELIVERY_ITEMS,
                DELIVERY_ID + " =?" + " AND "
                        + ITEM_ID + "=?", new String[]{strOrder, itemId}) > 0;
    }

    //riddhit
    public boolean deleteDepot(String depot) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CUSTOMER,
                CUSTOMER_TYPE + " =?", new String[]{depot}) > 0;
    }

    //riddhit
    public boolean deleteItemPriority(String depot) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEM_PRIORITY,
                KEY_DOC_TYPE + " =?", new String[]{depot}) > 0;
    }

    public void deleteRow(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CUSTOMER + " WHERE " + CUSTOMER_TYPE + "='" + value + "'");
        db.close();
    }

    public void deleteTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tableName);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from " + TABLE_CUSTOMER);
//        db.execSQL("delete from " + TABLE_ITEM);
//        db.execSQL("delete from " + TABLE_UOM);
        //db.execSQL("delete from " + TABLE_CUSTOMER_CATEGORY);
        //  db.execSQL("delete from " + TABLE_CUSTOMER_TYPE);
        //db.execSQL("delete from " + TABLE_DEPOT);
        //db.execSQL("delete from " + TABLE_CUSTOMER_PRICING);

        db.execSQL("delete from " + TABLE_LOAD_HEADER);
        db.execSQL("delete from " + TABLE_LOAD_ITEMS);
        db.execSQL("delete from " + TABLE_VAN_STOCK_ITEMS);
        db.execSQL("delete from " + TABLE_DELAY_PRINT);
        db.execSQL("delete from " + TABLE_TRANSACTION);
        db.execSQL("delete from " + TABLE_INVOICE_HEADER);
        db.execSQL("delete from " + TABLE_INVOICE_ITEMS);
        db.execSQL("delete from " + TABLE_COLLECTION);
        db.execSQL("delete from " + TABLE_RETURN_HEADER);
        db.execSQL("delete from " + TABLE_RETURN_ITEMS);
        db.execSQL("delete from " + TABLE_ORDER_HEADER);
        db.execSQL("delete from " + TABLE_ORDER_ITEMS);
        db.execSQL("delete from " + TABLE_LOAD_REQUEST_HEADER);
        db.execSQL("delete from " + TABLE_LOAD_REQUEST_ITEMS);
        db.execSQL("delete from " + TABLE_SALESMAN_LOAD_REQUEST_HEADER);
        db.execSQL("delete from " + TABLE_SALESMAN_LOAD_REQUEST_ITEMS);
        db.execSQL("delete from " + TABLE_UNLOAD_VARIANCE);
        db.execSQL("delete from " + TABLE_DELIVERY_HEADER);
        db.execSQL("delete from " + TABLE_DELIVERY_ITEMS);
        db.execSQL("delete from " + TABLE_RECENT_CUSTOMER);
        db.execSQL("delete from " + TABLE_PAYMENT);
        db.execSQL("delete from " + TABLE_CUSTOMER_VISIT);

        db.execSQL("delete from " + TABLE_SALE_SUMMARY);
        db.execSQL("delete from " + TABLE_FREE_GOODS);
        db.execSQL("delete from " + TABLE_COMPITITOR);
        db.execSQL("delete from " + TABLE_COMPLAIN);
        db.execSQL("delete from " + TABLE_CAMPAIGN);
        db.execSQL("delete from " + TABLE_PLANOGRAM);
        db.execSQL("delete from " + TABLE_CUSTOMER_ASSETS);
        db.execSQL("delete from " + TABLE_ASSETS_QUESTIONS);
        db.execSQL("delete from " + TABLE_SURVEY_QUESTIONS);
        db.execSQL("delete from " + TABLE_SENSOR_QUESTIONS);
        db.execSQL("delete from " + TABLE_CONSUMER_QUESTIONS);
        db.execSQL("delete from " + TABLE_CUSTOMER_INVENTORY);
        db.execSQL("delete from " + TABLE_PROMOTION_ITEM);
        db.execSQL("delete from " + TABLE_DISTRIBUTION);
        db.execSQL("delete from " + TABLE_CONSUMER_POST);
        db.execSQL("delete from " + TABLE_CONSUMER_POST_HEADER);
        db.execSQL("delete from " + TABLE_ASSETS_POST);
        db.execSQL("delete from " + TABLE_ASSETS_POST_HEADER);
        db.execSQL("delete from " + TABLE_SURVEY_POST);
        db.execSQL("delete from " + TABLE_SURVEY_POST_HEADER);
        db.execSQL("delete from " + TABLE_SENSOR_HEADER);
        db.execSQL("delete from " + TABLE_SENSOR_POST);
        db.execSQL("delete from " + TABLE_CUSTOMER_INVENTORY_HEADER);
        db.execSQL("delete from " + TABLE_DISTRIBUTION_IMAGE);
        db.execSQL("delete from " + TABLE_EXPIRY_ITEM);
        db.execSQL("delete from " + TABLE_EXPIRY_HEADER);
        db.execSQL("delete from " + TABLE_DAMAGED_HEADER);
        db.execSQL("delete from " + TABLE_DAMAGED_ITEM);
        db.execSQL("delete from " + TABLE_CATEGORY_DISCOUNT);
        db.execSQL("delete from " + TABLE_RETURN_REQUEST_HEADER);
        db.execSQL("delete from " + TABLE_RETURN_REQUEST_ITEMS);
        db.execSQL("delete from " + TABLE_ROUTE_CATEGORY_DISCOUNT);
        db.execSQL("delete from " + TABLE_PUSH_NOTIFICATION);
        db.execSQL("delete from " + TABLE_PROMOTION);
        db.execSQL("delete from " + TABLE_CUSTOMER_TECHNICIAN);
        db.execSQL("delete from " + TABLE_CUSTOMER_TECHNICIAN);
        db.execSQL("delete from " + TABLE_CUSTOMER_CHIllER_TECHNICIAN);
        db.execSQL("delete from " + TABLE_CHILLER_TECHNICIAN);
        db.execSQL("delete from " + TABLE_CHILLER_TECHNICIAN_CHECK);
        db.execSQL("delete from " + TABLE_ADD_CHILLER);
        db.execSQL("delete from " + TABLE_CHILLER_REQUEST);
        db.execSQL("delete from " + TABLE_ADD_FRIDGE);
        db.execSQL("delete from " + TABLE_CHILLER_ADD);
        db.execSQL("delete from " + TABLE_CHILLER_TRACKING);
        db.execSQL("delete from " + TABLE_SERVICE_VISIT);
        db.execSQL("delete from " + TABLE_FRIDGE_MASTER);
        db.execSQL("delete from " + TABLE_SERVICE_VISIT_POST);
        db.execSQL("delete from " + TABLE_NATURE_OF_CALL);
        db.execSQL("delete from " + TABLE_ITEM_PRIORITY);
        db.execSQL("delete from " + TABLE_AGENT_FREE_GOODS);
        db.execSQL("delete from " + TABLE_PROMO);
        db.execSQL("delete from " + TABLE_ORDER_ITEM);
        db.execSQL("delete from " + TABLE_OFFER_ITEM);
        db.execSQL("delete from " + TABLE_PROMO_SLAB);
        db.execSQL("delete from " + TABLE_PROMO_CUSTOMER_EXCLUDE);
        db.execSQL("delete from " + TABLE_AGENT_PROMO);
        db.execSQL("delete from " + TABLE_AGENT_ORDER_ITEM);
        db.execSQL("delete from " + TABLE_AGENT_OFFER_ITEM);
        db.execSQL("delete from " + TABLE_AGENT_PROMO_SLAB);
        db.execSQL("delete from " + TABLE_CUSTOMER_PROMO);
        db.execSQL("delete from " + TABLE_CUSTOMER_ORDER_ITEM);
        db.execSQL("delete from " + TABLE_CUSTOMER_OFFER_ITEM);
        db.execSQL("delete from " + TABLE_CUSTOMER_PROMO_SLAB);
        db.execSQL("delete from " + TABLE_PROMO_CUSTOMER);
    }


}