package com.mobiato.sfa.utils;

public class Constant {

    public static String AppNameSuper = "Benchmark";

    public static int TotalAPICOUNT = 12;
    public static String APICOUNT = TotalAPICOUNT + "/" + TotalAPICOUNT;


    public static class SHRED_PR {

        public static final String SHARE_PREF = AppNameSuper + "_preferences";

        public static final String SALESMANID = "salesman_id";
        public static final String ISINTRO = "isIntro";
    }


    public static class TRANSACTION_TYPES {
        public static final String TT_LOAD_CONF = "tt_load_conf";
        public static final String TT_STOCK_CAP = "tt_stock_cap";
        public static final String TT_SALES_CREATED = "tt_sales_created";
        public static final String TT_ADD_CUSTOEMR_CREATED = "tt_cust_created";
        public static final String TT_ADD_CUSTOEMR_UPDATE = "tt_cust_updated";
        public static final String TT_ADD_DEPT_CUSTOEMR_UPDATE = "tt_deptt_cust_updated";
        public static final String TT_CREDIT_COLLECTION = "tt_credit_collection";
        public static final String TT_CASH_COLLECTION = "tt_cash_collection";
        public static final String TT_CHILER_REQUEST = "tt_chiller_request";
        public static final String TT_CHILER_TRANSFER = "tt_chiller_transfer";
        public static final String TT_CHILER_ADD = "tt_chiller_add";
        public static final String TT_CHILER_TRACK= "tt_chiller_track";
        public static final String TT_FRIDGE_ADD = "tt_fridge_add";
        public static final String TT_PAYMENT_BY_CASH = "tt_payment_by_cash";
        public static final String TT_PAYMENT_BY_CHEQUE = "tt_payment_by_cheque";
        public static final String TT_CUST_CREATED = "tt_cust_created";
        public static final String TT_UNLOAD = "tt_unload";
        public static final String TT_SALESMAN_LOAD_CREATED = "tt_salesman_load_created";
        public static final String TT_OREDER_CREATED = "tt_order_created";
        public static final String TT_RETURN_CREATED = "tt_return_created";
        public static final String TT_END_DAY = "tt_end_day";
        public static final String TT_LOAD_CREATE = "tt_load_create";
        public static final String TT_SAP_LOAD_CREATE = "tt_SAP_load_create";
        public static final String TT_EXCHANGE_CREATED = "tt_exchange_created";
        public static final String TT_COMPLAINT_FEEDBACK = "tt_complaint";
        public static final String TT_COMPAIGN_FEEDBACK = "tt_compaign";
        public static final String TT_PLANOGRAM = "tt_planogram";
        public static final String TT_COMPATITOR = "tt_compatitor";
        public static final String TT_PRAMOTION = "tt_pramotion";
        public static final String TT_INVENTORY = "tt_inventory";
        public static final String TT_ASSETS = "tt_assets";
        public static final String TT_CONS_SURVEY = "tt_consumer_servey";
        public static final String TT_SENSURY_SURVEY = "tt_sensury_servey";
        public static final String TT_ASSETS_SURVEY = "tt_assets_servey";
        public static final String TT_DISTRIBUTION_SURVEY = "tt_distribution_servey";
        public static final String TT_EXPIRYITEM = "tt_expiryItem";
        public static final String TT_DERITEM = "tt_derItem";
        public static final String TT_SERVICE_VISIT= "tt_service_visit";
    }

}
