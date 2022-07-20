package com.mobiato.sfa;

import android.app.Application;

public class App extends Application {

    public static String flag = "false";
    public static String strCustomerId = "customerId";
    public static int countNoti = 0;

    public static final String PM_ServiceLast = "pm_service_last";
    public static final String BD_ServiceLast = "bd_service_last";
    public static final String BD_Done_ServiceLast = "bd_done_service_last";
    public static final String BD_Pending_ServiceLast = "bd_pending_service_last";
    public static final String CA_ServiceLast = "ca_service_last";
    public static final String SA_ServiceLast = "SA_service_last";

    public static int counttech_PM_service = 1;
    public static int counttech_BD_MAIN_service = 1;
    public static int counttech_BD_service = 1;
    public static int counttech_BD_Pending_Service = 1;
    public static int counttech_SA_service = 1;
    public static int counttech_CA_service = 1;
    public static final String countNoti_tech = "0";

    //PRODUCTION URL
    public static final String ENVIRONMENT = "Production";
    public static final String BASE_URL = "https://osa.harissint.com/";

//    public static final String ENVIRONMENT = "Development";
//    public static final String BASE_URL = "https://osa-pre.harissint.com/";

    /*public static final String ENVIRONMENT = "Training";
    public static final String BASE_URL = "https://osa-training.harissint.com/";*/

    // public static final String BASE_URL = "http://bizvay.com/";
    // public static final String BASE_URL = "https://osa.harissint.com/pre-prod/";

    public static final String COMMON_URL = "API/master.php";
    public static final String COMMON_POST_URL = "API/transction.php";
    public static final String COMMON_MERCHENT_URL = "API/merchandising.php";
    public static final String COMMON_MERCHENT_POST_URL = "API/merchandising_post.php";

    // DEVELOPMENT URL
    // public static final String BASE_URL = "https://osa-dev.harissint.com/";
    // public static final String COMMON_URL = "/pre-prod/API/master.php";
    // public static final String COMMON_POST_URL = "/pre-prod/API/transction.php";
    // public static final String COMMON_MERCHENT_URL = "/pre-prod/API/merchandising.php";
    // public static final String COMMON_MERCHENT_POST_URL = "/pre-prod/API/merchandising_post.php";

    public static String Accuracy = "0";
    public static String Latitude = "";
    public static String Longitude = "";

    public static final String LOGOUT = "login_and_logout";
    public static final String LOGIN = "login";
    public static final String UPDATE_PRICE = "is_price_update";
    public static final String CUSTOMER = "customerlist";
    public static final String CUSTOMER_TECHNICIAN = "get_iro";
    public static final String CHILLER_APPROVAL_TECHNICIAN = "get_installation_fridge";
    public static final String CHILLER_NOTIFICATION_TECHNICIAN = "approval_notification";
    public static final String FRIDGE_MASTER = "fridge_master_data";
    public static final String NATURE_OF_CAL_MASTER = "nature_of_call";
    public static final String CHILLER_SCHEDULE = "IR_Status";
    public static final String CHILLER_CLOSE = "IR_close_status";
    public static final String CHILLER_REJECT = "reject_chiller";
    public static final String CUSTOMER_OTC = "otc_customerlist";
    public static final String CUSTOMER_DEPORT = "depot_customerlist";
    public static final String ALL_DEPOT = "alldepot_data";
    public static final String ITEM = "itemlist";
    public static final String UOM = "uomlist";
    public static final String LOADLIST = "loadlist";
    public static final String FREE_GOODS = "discount_free_goods";
    public static final String CUSTOMER_FREE_GOODS_NEW = "customer_promotion_new";
    public static final String FREE_GOODS_NEW = "route_promotion_new";
    public static final String AGENT_FREE_GOODS = "promotion_aget";
    public static final String AGENT_FREE_GOODS_NEW = "agent_promotion_new";
    public static final String OUTSTANDING_INVOICE = "outstanding_invoice";
    public static final String LOAD_CONFIRM = "load_conform";
    public static final String POST_INVOICE = "invoice_post";
    public static final String POST_ORDER = "order_post";
    public static final String POST_COLLECTION = "collection";
    public static final String POST_RETURN = "return";
    public static final String RETURN_UPDATE = "update_return";
    public static final String GET_DELIVERY = "delivery_list";
    public static final String GET_MERCHANT_DELIVERY = "merchandizer_delivery";
    public static final String PRICING_LIST = "pricingplan_list";
    public static final String PRICING_ROUTE_LIST = "pricingplan_route";
    public static final String POST_LOAD_REQUEST = "load_request";
    public static final String POST_SALESMAN_LOAD_REQUEST = "load_post";
    public static final String POST_UNLOAD = "unload_request";
    public static final String POST_CUSTOMERVISIT = "customer_visit_status";
    public static final String POST_CUSTOMERVISIT_INVOICE = "customer_add_location";

    public static final String ITEM_DISCOUNT = "discount_item";
    public static final String ROUTE_ITEM_DISCOUNT = "discount_route_item";
    public static final String CUSTOMER_ITEM_DISCOUNT = "discount_customer_item";
    public static final String CUSTOMER_CATEGORY_DISCOUNT = "category_discount_customer_item";
    public static final String ROUTE_CATEGORY_DISCOUNT = "category_discount_route_item";
    public static final String CUSTOMER_CATEGORY = "customer_category";
    public static final String CUSTOMER_CHANNEL = "outlet_channel";
    public static final String CUSTOMER_SUB_CATEGORY = "outlet_sub_category";
    public static final String CUSTOMER_TYPE = "customer_type";
    public static final String DELIVERY_DELETE = "delivery_delete";
    public static final String ADD_CUSTOMER = "add_custmer";
    public static final String ADD_TEMPCUSTOMER = "add_custmer_temp";
    public static final String PLANOGRAM = "planogram";
    public static final String ASSETS_LIST = "asset_item";
    public static final String ASSETS_SURVEY_LIST = "asset_survey";
    public static final String SURVEY_TOOLS = "survey_tool";
    public static final String SENSOR_SURVEY = "sensory_survey";
    public static final String CONSUMER_SURVEY = "consumer_survey";
    public static final String INVENTORY = "inventory";
    public static final String PROMOTIONAL_LIST = "promotional_list";
    public static final String DISTRIBUTION_ITEM = "distribution_item";
    public static final String PLANOGRAM_POST = "planogram_post";
    public static final String COMPLAINT_FEEDBACK = "complaint_feedback";
    public static final String CAMPAIGN_POST = "campaign_post";
    public static final String ASSETS = "asset_post";
    public static final String COMPITITOR_POST = "competitor_post";
    public static final String DAILY_ATTENDANCE = "daily_attendance";
    public static final String CHILLER_POST = "fridge_request";
    public static final String ADD_FRIDGE_POST = "add_fridge";
    public static final String FRIDGE_POST = "fridge_post";
    public static final String CHILLER_REQUEST_POST = "chiller_request";
    public static final String CHILLER_ADD_REQUEST_POST = "chiller_add_request";
    public static final String CHILLER_TRACKING = "chiller_tracking";
    public static final String SERVICE_VISIT_POST = "sevice_visit_post";
    public static final String ADD_CHILLER = "fridge_approval";
    public static final String AGREEMENT_POST = "fridge_agreement";
    public static final String CHILLERTRANSFER_POST = "fridge_transfer";
    public static final String UPDATE_CUST_POST = "update_custmer_temp";
    public static final String Expiry_ITEM = "expiry_item";
    public static final String DAMAGED_ITEM = "damaged_item";
    public static final String SURVEY_POST = "Survey_post";
    public static final String ASSETS_SURVEY_POST = "assets_survey_post";
    public static final String SENSURY_POST = "answer_sensury_post";
    public static final String INVENTORY_EXPIRY_POST = "inventory_post";
    public static final String DISTRIBUTION_STOCK = "distribution_stock";
    public static final String CONSUMER_POST = "consumer_post";
    public static final String DISTRIBUTION_IMAGE = "distribution_image";
    public static final String POST_Expiry_Item = "item_expiry";
    public static final String POST_Damaged_Item = "item_der";
    public static final String POST_ANSWER = "answer_post";
    public static final String POST_INVENTORY = "inventry_post";
    public static final String POST_SALE = "salesman_commission";
    public static final String POST_SALEMAN_COMMISION = "salesman_commission_itemwise";
    public static final String RETURN_LIST = "return_list";
    public static boolean isVisitInsert = false;
    public static final String PROMOTIONAL = "promotional_post";
    public static final String NOTIFICATION_DETAIL = "notification_order_details";
    public static final String GET_AGENT_LIST = "getagentlist";
    public static final String GET_DEPOT_ROUTE = "alldepot_route";
    public static final String GET_DEPOT_SALESMAN = "alldepot_salesman";
    public static final String CHANGE_PASSWORD = "change_password";
    public static final String SERVICE_VISIT_TRACK_POST = "service_visit_track_post";

    public static final String INVOICE_COMPLETE = "C";
    public static final String INVOICE_INCOMPLETE = "I";
    //Open items indicator
    public static final String ADD_INDICATOR = "S";

    //Static keys to store in shared preference due to global usage during posting
    public static final String LOGGID = "logg_id";
    public static final String SALESMAN = "salesman_data";
    public static final String SALESMANID = "salesman_id";
    public static final String SALESMANCONTACT = "salesman_contect";
    public static final String SALESMANINO = "salesman_no";
    public static final String SALESMANNAME = "salesman_name";
    public static final String ISLOGIN = "isLogin";
    public static final String TOKEN = "token";
    public static final String IS_DATA_SYNCING = "isDataSync";
    public static final String IS_LOAD_VERIFY = "isLoad_verify";
    public static final String IS_ATTENDANCE_IN = "isAttendance_in";
    public static final String IS_ATTENDANCE_OUT = "isAttendance_out";
    public static final String IS_ENDDAY = "isEndDay";
    public static final String IS_BADCAPTURE = "isBadReturn";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String ROUTEID = "routeId";
    public static final String EXCHANGE_AMONUNT = "0";
    public static final String INVOICE_CODE = "invoice_code";
    public static final String SCANSERIALNUMBER = "scanno";
    public static final String ASSOCIATED_ROUTEID = "asso_routeId";
    public static final String DEPOTID = "depotId";
    public static final String DEPOTNAME = "depotName";
    public static final String DEPOTTIN = "depotTIN";
    public static final String DEPOTVILLAGE = "depotVillage";
    public static final String DEPOTCITY = "depotCity";
    public static final String DEPOTSTREET = "depotStreet";
    public static final String DEPOTPHONE = "depotPhone";
    public static final String AGENTID = "agentId";
    public static final String CUSTOMERFILE = "customer_file";
    public static final String ITEMFILE = "item_file";
    public static final String PRICEFILE = "price_file";
    public static final String PROJECT_NAME = "project_name";
    public static final String ROLE = "role";
    public static final int REQUEST_BARCODE = 2;
    public static final String INVOICE_LAST = "Invoice_LastNo";
    public static final String ORDER_LAST = "Order_LastNo";
    public static final String COLLECTION_LAST = "Collection_LastNo";
    public static final String RETURN_LAST = "Retrun_LastNo";
    public static final String LOAD_LAST = "Load_Request_LastNo";
    public static final String UNLOAD_LAST = "Unload_LastNo";
    public static final String CUSTOMER_LAST = "Customer_LastNo";
    public static final String EXCHANGE_LAST = "Exchange_LastNO";
    public static final String SALE_TARGET = "sale_target";
    public static final String ACHIVE_TARGET = "achive_target";
    public static final String IS_SALE_POSTED = "isSalePost";
    public static final String OLD_PWD = "old_password";
    public static final String NEW_PWD = "new_password";
    public static final String PROJECT_ID = "projectId";
    public static int COUNT = 2;

    //Printing
    public static final String REQUEST = "REQUEST";
    public static final String LOAD_SUMMARY_REQUEST = "LoadSummaryRequest";
    public static final String VAN_STOCK = "vanstock";
    public static final String SALES_INVOICE = "Invoice";
    public static final String LOAD = "Load";
    public static final String SALESMAN_LOAD = "salesman_Load";
    public static final String ORDER = "order";
    public static final String COLLECTION = "collection";
    public static final String RETURN = "return";
    public static final String RETURN_REQUEST = "return_request";
    public static final String LOAD_REQUEST = "load_request";
    public static final String UNLOAD_REQUEST = "unload_request";
    public static final String VISIT_REQUEST = "visit_request";
    public static final String SALES_REQUEST = "sales_request";
    public static final String INVOICE_LOCATION = "invoice_location";
    public static final String SALES_SUMMARY = "sales_summary";
    public static final String DEPOSITE_SUMMARY = "deposite_summary";
    public static final String EXCHANGE = "exchange";
    public static final String DELIVERY_REQUEST = "delivery_delete";
    public static final String CUSTOMER_REQUEST = "add_customer";
    public static final String CUSTOMER_UPDATE_REQUEST = "update_customer";
    public static final String CUSTOMER_DEPT_UPDATE_REQUEST = "dept_update_customer";
    public static final String END_INVENTORY = "end_inventory";
    public static final String SALES_POST = "sale_post_request";

    //Transaction Types
    public static final String LoadConfirmation_TR = "LCON";

    //Connectivity Messages
    public static final String WIFI_CONNECTED = "Wifi Enabled";
    public static final String MOBILE_DATA_CONNECTED = "Mobile Data Enabled";
    public static final String NO_CONNECTION = "Not connected to Internet";

    //Collection Type
    public static final String COLLECTION_INVOICE = "INVOICE";
    public static final String COLLECTION_RETURN = "RETURN";
    public static final String COLLECTION_INVOICE_CASH = "INVOICE_CASH";
    public static final String COLLECTION_DELIVERY = "DELIVERY";

    //is API Sync
    public static boolean isInvoiceSync = false;
    public static boolean isSalesmanLoadSync = false;
    public static boolean isLoadSync = false;
    public static boolean isOrderSync = false;
    public static boolean isCollectionSync = false;
    public static boolean isReturnSync = false;
    public static boolean isLoadRequestSync = false;
    public static boolean isUnloadRequestSync = false;
    public static boolean isVisitRequestSync = false;
    public static boolean isDeleteRequestSync = false;
    public static boolean isAddRequestSync = false;
    public static boolean isUpdateRequestSync = false;
    public static boolean isDeptUpdateRequestSync = false;
    public static boolean isStockSync = false;
    public static boolean isComplaintSync = false;
    public static boolean isExpiryItemSync = false;
    public static boolean isSurveyPost = false;
    public static boolean isDamagedSync = false;
    public static boolean isPlanogramSync = false;
    public static boolean isCompititorSync = false;
    public static boolean isCampaignSync = false;
    public static boolean isPromotionalSync = false;
    public static boolean isChillerSync = false;
    public static boolean isFRIDGESync = false;
    public static boolean isChillerRequestSync = false;
    public static boolean isChillerTrackSync = false;
    public static boolean isServiceVisitPostSync = false;
    public static boolean isChillerAddRequestSync = false;
    public static boolean isInventoryPost = false;
    public static boolean isAssetsSync = false;
    public static boolean isSensoryPost = false;
    public static boolean isConsumerPost = false;
    public static boolean isAssetsPost = false;
    public static boolean isDisImageSync = false;
    public static boolean isVisitSync = false;
    public static boolean isSalePostRequestSync = false;
    public static boolean isReturnRequestSync = false;

}
