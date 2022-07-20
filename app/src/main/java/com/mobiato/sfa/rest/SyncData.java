package com.mobiato.sfa.rest;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.mobiato.sfa.App;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.ASSETS_MODEL;
import com.mobiato.sfa.model.Chiller_Model;
import com.mobiato.sfa.model.CollectionData;
import com.mobiato.sfa.model.Compaign;
import com.mobiato.sfa.model.Compititor;
import com.mobiato.sfa.model.Complain;
import com.mobiato.sfa.model.Freeze;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.LoadRequest;
import com.mobiato.sfa.model.Order;
import com.mobiato.sfa.model.PlanogramList;
import com.mobiato.sfa.model.Promotion;
import com.mobiato.sfa.model.ReturnOrder;
import com.mobiato.sfa.model.ServiceVisitPost;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;


public class SyncData extends IntentService {


    public static String TAG = "SyncData";
    DBManager db;
    Activity activity;
    public static final String BROADCAST_ACTION = "com.riham.UPDATE";
    String CustomerId, PromotionId, AssetsId;

    private static SyncData instance = null;
    Intent intent;

    public static synchronized SyncData getInstance() {

        if (instance == null) {
            instance = new SyncData();
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "com.mobiato.sfa";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Riham", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("App is running in background").build();
            startForeground(App.COUNT, notification);
            App.COUNT++;


        }
    }

    public SyncData() {
        super(TAG);
        instance = this;
        intent = new Intent(BROADCAST_ACTION);
        db = new DBManager(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("I am here", "IntentService" + Settings.getString(App.IS_DATA_SYNCING));

        if (Settings.getString(App.IS_DATA_SYNCING) != null) {
            if (!Boolean.parseBoolean(Settings.getString(App.IS_DATA_SYNCING))) {
                syncData();
            }
        }

    }

    public void syncData() {

        //Post Add Customer
        if (getSyncCount(App.CUSTOMER_REQUEST) > 0) {
            if (!App.isAddRequestSync) {
                App.isAddRequestSync = true;
                JsonObject jObj = getCustomerRequestForPost();
                callAddCustomerAPI(jObj);
            }
        }

        //Post Add Customer
        if (getSyncCount(App.CUSTOMER_UPDATE_REQUEST) > 0) {
            if (!App.isUpdateRequestSync) {
                App.isUpdateRequestSync = true;
                JsonObject jObj = getCustomerUpdateForPost();
                callUpdateCustomerAPI(jObj);
            }
        }

        //Post Add Customer
        if (getSyncCount(App.CUSTOMER_DEPT_UPDATE_REQUEST) > 0) {
            if (!App.isDeptUpdateRequestSync) {
                App.isDeptUpdateRequestSync = true;
                JsonObject jObj = getDeptCustomerUpdateForPost();
                callUpdateDeptCustomerAPI(jObj);
            }
        }

        //Post Invoice
        if (getSyncCount(App.SALES_INVOICE) > 0) {
            if (!App.isInvoiceSync) {
                App.isInvoiceSync = true;
                ArrayList<HashMap<String, String>> arrInvoices = db.getPostInvoiceNo();
                String strInvNo = "";
                boolean isInvoice = false;
                for (int i = 0; i < arrInvoices.size(); i++) {
                    strInvNo = arrInvoices.get(i).get("invoiceNo");
                    isInvoice = true;
                    break;
                }

                if (isInvoice) {
                    JsonObject jObj = getInvoiceForPost(strInvNo);
                    String invNo = jObj.get("invoiceNo").getAsString();
                    String exchangeNo = jObj.get("exchangeNo").getAsString();
                    callInvoiceAPI(jObj, invNo, exchangeNo);
                } else {
                    App.isInvoiceSync = false;
                }

            }
        }

        //Post Collection
        if (getSyncCount(App.COLLECTION) > 0) {
            if (!App.isCollectionSync) {
                App.isCollectionSync = true;
                ArrayList<CollectionData> arrColl = db.getCollectionPostDetail();
                CollectionData mCollection = new CollectionData();
                boolean isCollect = false;
                for (int i = 0; i < arrColl.size(); i++) {
                    if (arrColl.get(i).getIsOutStand().equalsIgnoreCase("0")) {
                        if (db.getInvoicePosted(arrColl.get(i).getInvoiceNo())) {
                            mCollection = arrColl.get(i);
                            isCollect = true;
                            break;
                        }
                    } else {
                        mCollection = arrColl.get(i);
                        isCollect = true;
                        break;
                    }
                }

                if (isCollect) {
                    JsonObject jObj = getCollectionForPost(mCollection);
                    String collNo = jObj.get("collection_number").getAsString();
                    callCollectionAPI(jObj, collNo);
                } else {
                    App.isCollectionSync = false;
                }
            }
        }

        //Load Confirm
        if (getSyncCount(App.LOAD) > 0) {
            if (!App.isLoadSync) {
                App.isLoadSync = true;
                String subLoadId = db.getConfirmLoadNo();
                callLoadConfirmAPI(subLoadId);
            }
        }

        if (getSyncCount(App.SALESMAN_LOAD) > 0) {
            if (!App.isSalesmanLoadSync) {
                App.isSalesmanLoadSync = true;
                JsonObject jObj = getSalesmanLoadRequestForPost();
                String invNo = jObj.get("load_no").getAsString();
                callSalesmanLoadRequestAPI(jObj, invNo);
            }
        }

        //Post Order
        if (getSyncCount(App.ORDER) > 0) {
            if (!App.isOrderSync) {
                App.isOrderSync = true;
                JsonObject jObj = getOrderForPost();
                String invNo = jObj.get("OrderNo").getAsString();
                callOrderAPI(jObj, invNo);
            }
        }

        //Post Return
        if (getSyncCount(App.RETURN) > 0) {
            if (!App.isReturnSync) {
                App.isReturnSync = true;
                JsonObject jObj = getReturnForPost();
                String invNo = jObj.get("returnNo").getAsString();
                callReturnAPI(jObj, invNo);
            }
        }

        //Post Return Request
        if (getSyncCount(App.RETURN_REQUEST) > 0) {
            if (!App.isReturnRequestSync) {
                App.isReturnRequestSync = true;
                JsonObject jObj = getReturnRequestForPost();
                String invNo = jObj.get("returnNo").getAsString();
                callReturnRequestAPI(jObj, invNo);
            }
        }

        //Post Load Request
        if (getSyncCount(App.LOAD_REQUEST) > 0) {
            if (!App.isLoadRequestSync) {
                App.isLoadRequestSync = true;
                JsonObject jObj = getLoadRequestForPost();
                String invNo = jObj.get("loadrequestNo").getAsString();
                callLoadRequestAPI(jObj, invNo);
            }
        }

        //Post UnLoad Request
        if (getSyncCount(App.UNLOAD_REQUEST) > 0) {
            if (!App.isUnloadRequestSync) {
                App.isUnloadRequestSync = true;
                JsonObject jObj = getUnLoadRequestForPost();
                String invNo = jObj.get("unloadNo").getAsString();
                callUnloadAPI(jObj, invNo);
            }
        }

        //Post Sale Post Request
        if (getSyncCount(App.SALES_POST) > 0) {
            if (!App.isSalePostRequestSync) {
                App.isSalePostRequestSync = true;
                JsonObject jObj = getSaleRequestForPost();
                callSaleAmountAPI(jObj);
            }
        }


        //Post Visit Request
        if (getSyncCount(App.VISIT_REQUEST) > 0) {
            if (!App.isVisitRequestSync) {
                App.isVisitRequestSync = true;
                JsonObject jObj = getVisitRequestForPost();
                callVisitAPI(jObj);
            }
        }

        //Location Invoice 23/01
        if (getSyncCount(App.VISIT_REQUEST) > 0) {
            if (!App.isVisitRequestSync) {
                App.isVisitRequestSync = true;
                JsonObject jObj = getVisitRequestForPostNew();
                callVisitInvoiceLocationAPI(jObj);
            }
        }

        //Post Delivery Delete
        if (getSyncCount(App.DELIVERY_REQUEST) > 0) {
            if (!App.isDeleteRequestSync) {
                App.isDeleteRequestSync = true;
                String deliveryId = getDeleteForPost();
                callDeleteAPI(deliveryId);
            }
        }

        //Post Planogram
        if (getSyncCount(App.PLANOGRAM_POST) > 0) {
            if (!App.isPlanogramSync) {
                App.isPlanogramSync = true;
                callPlanogramAPI();
            }

        }

        // Complaint post
        if (getSyncCount(App.COMPLAINT_FEEDBACK) > 0) {
            if (!App.isComplaintSync) {
                App.isComplaintSync = true;
                callComplaintAPI();
            }
        }


        // Compititor post
        if (getSyncCount(App.COMPITITOR_POST) > 0) {
            if (!App.isCompititorSync) {
                App.isCompititorSync = true;
                callCompititorAPI();
            }
        }

        // Campaign post
        if (getSyncCount(App.CAMPAIGN_POST) > 0) {
            if (!App.isCampaignSync) {
                App.isCampaignSync = true;
                callCampaignAPI();
            }
        }

        //Post Assets
        if (getSyncCount(App.ASSETS) > 0) {
            if (!App.isAssetsSync) {
                App.isAssetsSync = true;
                callAssetsAPI();
            }
        }

        //Post Expiry item list
        if (getSyncCount(App.Expiry_ITEM) > 0) {
            if (!App.isExpiryItemSync) {
                App.isExpiryItemSync = true;
                System.out.println("Demo Item Load");
                String uniqId = db.getExpiry();
                JsonObject jObj = getExpiryItemListPost(uniqId);
                callExpiryItemAPI(jObj, uniqId);
            }
        }

        //Post Damaged item list
        if (getSyncCount(App.DAMAGED_ITEM) > 0) {
            if (!App.isDamagedSync) {
                App.isDamagedSync = true;
                System.out.println("Demo Item Load");
                String uniqId = db.getDamage();
                JsonObject jObj = getDamagedItemListPost(uniqId);
                callDamagedItemAPI(jObj, uniqId);
            }
        }

        //Post Survey
        if (getSyncCount(App.SURVEY_POST) > 0) {
            if (!App.isSurveyPost) {
                App.isSurveyPost = true;
                JsonObject jObj = getSurveyForPost();
                callSurveyPostAPI(jObj);
            }
        }

        //Post Assets Survey
        if (getSyncCount(App.ASSETS_SURVEY_POST) > 0) {
            if (!App.isAssetsPost) {
                App.isAssetsPost = true;
                JsonObject jObj = getAssetsSurveyForPost();
                callAssetsSurveyPostAPI(jObj);
            }
        }

        //Post Sensory Survey
        if (getSyncCount(App.SENSURY_POST) > 0) {
            if (!App.isSensoryPost) {
                App.isSensoryPost = true;
                JsonObject jObj = getSensoryForPost();
                callSensoryPostAPI(jObj);
            }
        }

        //Post Consumer Survey Post
        if (getSyncCount(App.CONSUMER_POST) > 0) {
            if (!App.isConsumerPost) {
                App.isConsumerPost = true;
                JsonObject jObj = getConsumerSurveyForPost();
                callConsumerSurveyPostAPI(jObj);
            }

        }

        //Post Inventory
        if (getSyncCount(App.INVENTORY_EXPIRY_POST) > 0) {
            if (!App.isInventoryPost) {
                App.isInventoryPost = true;
                HashMap<String, String> mItem = db.getPostInventoryDetail();
                JsonObject jObj = getInventoryItemPost(mItem.get("cusId"));
                callPostInventoryAPI(jObj, mItem.get("invNo"));
            }
        }

        //Post Distr Image
        if (getSyncCount(App.DISTRIBUTION_IMAGE) > 0) {
            if (!App.isDisImageSync) {
                App.isDisImageSync = true;
                callDistrIMageAPI();
            }
        }

        //Post Distr STock
        if (getSyncCount(App.DISTRIBUTION_STOCK) > 0) {
            if (!App.isStockSync) {
                App.isStockSync = true;
                JsonObject jObj = getStockItemListPost();
                callDistributionPostAPI(jObj);
            }
        }

        //Post Promotional
        if (getSyncCount(App.PROMOTIONAL) > 0) {
            if (!App.isPromotionalSync) {
                App.isPromotionalSync = true;
                callPromotionAPI();
            }
        }

        //Post Promotional
        if (getSyncCount(App.CHILLER_POST) > 0) {
            if (!App.isChillerSync) {
                App.isChillerSync = true;
                callCHillerAddAPI();
            }
        }

        //Post Promotional
        if (getSyncCount(App.FRIDGE_POST) > 0) {
            if (!App.isFRIDGESync) {
                App.isFRIDGESync = true;
                callFridgeAddAPI();
            }
        }

        //Post Promotional
        if (getSyncCount(App.CHILLER_ADD_REQUEST_POST) > 0) {
            if (!App.isChillerAddRequestSync) {
                App.isChillerAddRequestSync = true;
                callCHillerAddRequestAPI();
            }
        }

        //Post Promotional
        if (getSyncCount(App.CHILLER_REQUEST_POST) > 0) {
            if (!App.isChillerRequestSync) {
                App.isChillerRequestSync = true;
                callCHillerRequestAPI();
            }
        }

        //Post Promotional
        if (getSyncCount(App.CHILLER_TRACKING) > 0) {
            if (!App.isChillerTrackSync) {
                App.isChillerTrackSync = true;
                callCHillerTrackAPI();
            }
        }

        //Post Promotional
        if (getSyncCount(App.SERVICE_VISIT_TRACK_POST) > 0) {
            if (!App.isServiceVisitPostSync) {
                App.isServiceVisitPostSync = true;
                callServiceVisitPostAPI();
            }
        }

    }

    public int getSyncCount(String function) {
        int syncCount = 0;
        try {

            switch (function) {
                case App.SALES_INVOICE: {
                    int count = db.getPostInvoiceCount();
                    syncCount = count;
                    break;
                }
                case App.LOAD: {
                    int count = db.getLoadCount();
                    syncCount = count;
                    break;
                }
                case App.SALESMAN_LOAD: {
                    int count = db.getSalesmanLoadCount();
                    syncCount = count;
                    break;
                }
                case App.ORDER: {
                    int count = db.getPostOrderCount();
                    syncCount = count;
                    break;
                }
                case App.COLLECTION: {
                    int count = db.getPostCollectionCount();
                    syncCount = count;
                    break;
                }
                case App.RETURN: {
                    int count = db.getPostReturnCount();
                    syncCount = count;
                    break;
                }
                case App.RETURN_REQUEST: {
                    int count = db.getPostReturnRequestCount();
                    syncCount = count;
                    break;
                }
                case App.LOAD_REQUEST: {
                    int count = db.getPostLoadRequestCount();
                    syncCount = count;
                    break;
                }
                case App.VISIT_REQUEST: {
                    int count = db.getPostVisitRequestCount();
                    syncCount = count;
                    break;
                }
                case App.INVOICE_LOCATION: {
                    int count = db.getPostVisitRequestsalesCount();
                    syncCount = count;
                    break;
                }
                case App.UNLOAD_REQUEST: {
                    if (Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
                        int count = db.getPostUnLoadRequestCount();
                        syncCount = count;
                    }
                    break;
                }
                case App.SALES_POST: {
                    if (Settings.getString(App.IS_ENDDAY).equalsIgnoreCase("1")) {
                        if (Settings.getString(App.IS_SALE_POSTED).equalsIgnoreCase("0")) {
                            int count = 1;
                            syncCount = count;
                        }
                    }
                    break;
                }
                case App.DELIVERY_REQUEST: {
                    int count = db.getPostDeleteRequestCount();
                    syncCount = count;
                    break;
                }
                case App.CUSTOMER_REQUEST: {
                    int count = db.getPostCustomerRequestCount();
                    syncCount = count;
                    break;
                }
                case App.CUSTOMER_UPDATE_REQUEST: {
                    int count = db.getPostCustomerUpdateRequestCount();
                    syncCount = count;
                    break;
                }
                case App.CUSTOMER_DEPT_UPDATE_REQUEST: {
                    int count = db.getPostDeptCustomerUpdateRequestCount();
                    syncCount = count;
                    break;
                }
                case App.PLANOGRAM_POST: {
                    int count = db.getPostPlanogramCount();
                    syncCount = count;
                    break;
                }
                case App.COMPLAINT_FEEDBACK: {
                    int count = db.getPostComplaintCount();
                    syncCount = count;
                    break;
                }
                case App.CAMPAIGN_POST: {
                    int count = db.getPostCampaignCount();
                    syncCount = count;
                    break;
                }
                case App.ASSETS: {
                    int count = db.getPostAssetsCount();
                    syncCount = count;
                    break;
                }
                case App.COMPITITOR_POST: {
                    int count = db.getPostCompititortCount();
                    syncCount = count;
                    break;
                }
                case App.Expiry_ITEM: {
                    int count = db.getPostExpiryCount();
                    syncCount = count;
                    break;
                }
                case App.DAMAGED_ITEM: {
                    int count = db.getPostDamagedCount();
                    syncCount = count;
                    break;
                }
                case App.SURVEY_POST: {
                    int count = db.getPostSurveyCount();
                    syncCount = count;
                    break;
                }
                case App.ASSETS_SURVEY_POST: {
                    int count = db.getPostAssetsSurveyCount();
                    syncCount = count;
                    break;
                }
                case App.SENSURY_POST: {
                    int count = db.getPostSensoryCount();
                    syncCount = count;
                    break;
                }
                case App.INVENTORY_EXPIRY_POST: {
                    int count = db.getPostInventoryCount();
                    syncCount = count;
                    break;
                }
                case App.CONSUMER_POST: {
                    int count = db.getPostConsumerCount();
                    syncCount = count;
                    break;
                }
                case App.DISTRIBUTION_IMAGE: {
                    int count = db.getPostDisImageCount();
                    syncCount = count;
                    break;
                }
                case App.DISTRIBUTION_STOCK: {
                    int count = db.getPostDisStockCount();
                    syncCount = count;
                    break;
                }
                case App.PROMOTIONAL: {
                    int count = db.getPostPromotionCount();
                    syncCount = count;
                    break;
                }
                case App.CHILLER_POST: {
                    int count = db.getChillerPostCount();
                    syncCount = count;
                    break;
                }
                case App.FRIDGE_POST: {
                    int count = db.getFridgePostCount();
                    syncCount = count;
                    break;
                }
                case App.CHILLER_REQUEST_POST: {
                    int count = db.getChillerRequestPostCount();
                    syncCount = count;
                    break;
                }
                case App.CHILLER_ADD_REQUEST_POST: {
                    int count = db.getChillerAddRequestPostCount();
                    syncCount = count;
                    break;
                }
                case App.CHILLER_TRACKING: {
                    int count = db.getChillerTrackPostCount();
                    syncCount = count;
                    break;
                }
                case App.SERVICE_VISIT_TRACK_POST: {
                    int count = db.getServiceVisitPostCount();
                    syncCount = count;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return syncCount;
    }

    //Get Expiry  item list Posting
    public JsonObject getExpiryItemListPost(String uniqId) {

        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getExpiryAnswer(uniqId);
        try {
            jObj.addProperty("method", App.POST_Expiry_Item);
            jObj.add("item_expiry_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Damage  item list Posting
    public JsonObject getDamagedItemListPost(String uniqId) {

        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getDamagedAnswer(uniqId);
        try {
            jObj.addProperty("method", App.POST_Damaged_Item);
            jObj.add("item_der_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    public JsonObject getSurveyForPost() {

        JsonObject jObj = new JsonObject();

        String uniqId = db.getToolSurvey();

        JsonArray jsonArray = db.getSurveyAnswer(uniqId);

        try {
            jObj.addProperty("method", App.POST_ANSWER);
            jObj.addProperty("answer_identifer", uniqId);
            jObj.add("answer_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    public JsonObject getAssetsSurveyForPost() {

        JsonObject jObj = new JsonObject();

        String uniqId = db.getAssetsSurvey();

        JsonArray jsonArray = db.getAssetsSurveyAnswer(uniqId);
        try {
            jObj.addProperty("method", App.POST_ANSWER);
            jObj.addProperty("answer_identifer", uniqId);
            jObj.add("answer_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    // senspry post
    public JsonObject getSensoryForPost() {
        JsonObject jObj = new JsonObject();

        HashMap<String, String> uniqId = db.getSensorySurvey();

        JsonArray jsonArray = db.getSensoryAnswer(uniqId.get("uniqueid"));
        try {
            jObj.addProperty("method", App.SENSURY_POST);
            jObj.addProperty("answer_identifer", uniqId.get("uniqueid"));
            jObj.addProperty("customer_name", uniqId.get("customer_name"));
            jObj.addProperty("customer_email", uniqId.get("customer_email"));
            jObj.addProperty("customer_phone", uniqId.get("customer_phone"));
//            jObj.addProperty("customer_address", uniqId.get("customer_address"));
            jObj.add("answer_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    public JsonObject getConsumerSurveyForPost() {
        JsonObject jObj = new JsonObject();

        String uniqId = db.getConsumerSurvey();
        JsonArray jsonArray = db.getConsumerSurveyAnswer(uniqId);

        try {
            jObj.addProperty("method", App.POST_ANSWER);
            jObj.addProperty("answer_identifer", uniqId);
            jObj.add("answer_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    //Get Inventory  item list Posting
    public JsonObject getInventoryItemPost(String cutId) {

        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getInventoryExpiry(cutId);
        try {
            jObj.addProperty("method", App.POST_INVENTORY);
            jObj.add("answer_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Distribution Stock list Posting
    public JsonObject getStockItemListPost() {

        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getStockDistri();
        try {
            jObj.addProperty("method", App.DISTRIBUTION_STOCK);
            jObj.add("items_array", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }


    //Get Invoice Posting
    public JsonObject getInvoiceForPost(String invNo) {

        HashMap<String, String> mItem = db.getPostInvoiceDetail(invNo);
        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getPostInvoiceItem(invNo);
        try {
            jObj.addProperty("method", App.POST_INVOICE);
            jObj.addProperty("currency", "UGX");

            String routeId = "";
            if (db.checkIsCustomerExist(mItem.get("customerid"))) {
                routeId = db.getCustomerRoute(mItem.get("customerid"));
            } else {
                routeId = db.getDepotCustomerRoute(mItem.get("customerid"));
            }

            if (routeId.isEmpty()) {
                routeId = Settings.getString(App.ROUTEID);
            }
            jObj.addProperty("tripid", routeId);
            jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("customerid", mItem.get("customerid"));
            jObj.addProperty("invoiceNo", mItem.get("invoiceNo"));
            jObj.addProperty("invoice_date", mItem.get("invoice_date"));
            jObj.addProperty("invoice_time", mItem.get("invoice_time"));
            jObj.addProperty("project_id", Settings.getString(App.PROJECT_ID));
            jObj.addProperty("invoice_type", UtilApp.orderType(Settings.getString(App.ROLE)));
            if (mItem.get("delivery_id") != null) {
                jObj.addProperty("delivery_id", mItem.get("delivery_id"));
                if (!mItem.get("delivery_id").equalsIgnoreCase("")) {
                    jObj.addProperty("orderNo", db.getDeliveryOrder(mItem.get("delivery_id")));
                } else {
                    jObj.addProperty("orderNo", "");
                }
            } else {
                jObj.addProperty("delivery_id", "");
                jObj.addProperty("orderNo", "");
            }

            jObj.addProperty("due_date", mItem.get("due_date"));
            jObj.addProperty("payment_type", mItem.get("payment_type"));
            jObj.addProperty("payment_term", mItem.get("payment_term"));
            jObj.addProperty("price_list", mItem.get("price_list"));
            jObj.addProperty("gross_total", mItem.get("gross_total"));
            jObj.addProperty("excise", mItem.get("excise"));
            jObj.addProperty("pre_vat", mItem.get("pre_vat"));
            jObj.addProperty("net_total", mItem.get("net_total"));
            jObj.addProperty("vat", mItem.get("vat"));
            jObj.addProperty("discount", mItem.get("discount"));
            jObj.addProperty("total_amount", mItem.get("total_amount"));
            jObj.addProperty("exchangeNo", mItem.get("exchangeNo"));
            jObj.addProperty("discount", mItem.get("discount"));
            jObj.addProperty("discount_id", mItem.get("discount_id"));
            jObj.addProperty("promotion_id", mItem.get("promotion_id"));
            jObj.addProperty("latitude", mItem.get("latitude"));
            jObj.addProperty("longitude", mItem.get("longitude"));
            jObj.addProperty("purchaser_name", mItem.get("purchaser_name"));
            jObj.addProperty("purchaser_contact", mItem.get("purchaser_contact"));
            jObj.addProperty("is_cust_new", db.isNewCustomer(mItem.get("customerid")));
            jObj.add("itemlist", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Order Post Data
    public JsonObject getOrderForPost() {
        Order mOrder = db.getPostOrderDetail();

        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getPostOrderItems(mOrder.getOrderNo());
        System.out.println("post-->");
        try {
            jObj.addProperty("method", App.POST_ORDER);
            jObj.addProperty("currency", "UGX");
            jObj.addProperty("tripid", Settings.getString(App.ROUTEID));
            jObj.addProperty("customerid", mOrder.getCust_id());
            jObj.addProperty("OrderNo", mOrder.getOrderNo());
            jObj.addProperty("order_date", mOrder.getOrderDate());
            jObj.addProperty("order_type", UtilApp.orderType(Settings.getString(App.ROLE)));
            jObj.addProperty("delivery_date", mOrder.getDeliveyDate());
            jObj.addProperty("payment_term", "");
            jObj.addProperty("price_list", "1");
            jObj.addProperty("gross_total", mOrder.getOrderAmt());
            jObj.addProperty("excise", mOrder.getExciseAmt());
            jObj.addProperty("vat", mOrder.getVatAmt());
            jObj.addProperty("pre_vat", mOrder.getPreVatAmt());
            jObj.addProperty("net_total", mOrder.getNetAmt());
            jObj.addProperty("discount", "0");
            jObj.addProperty("total_amount", mOrder.getOrderAmt());
            jObj.addProperty("order_comment", mOrder.getOrderComment());
            jObj.addProperty("user_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("is_cust_new", db.isNewCustomer(mOrder.getCust_id()));
            if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Merchandising")) {
                jObj.addProperty("is_agent", "3");
                if (mOrder.getSalesmanId().equalsIgnoreCase(Settings.getString(App.SALESMANID))) {
                    jObj.addProperty("salesman_id", "0");
                } else {
                    jObj.addProperty("salesman_id", mOrder.getSalesmanId());
                }
            } else {
                jObj.addProperty("is_agent", "2");
                jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            }
            jObj.add("itemlist", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Collection Post Data
    public JsonObject getCollectionForPost(CollectionData mCollection) {


        ArrayList<CollectionData> arrCollection = db.getCollectionDetail(mCollection.getOrderId());

        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = new JsonArray();

        String invNo = "";
        try {

            double totAMt = 0;
            for (int i = 0; i < arrCollection.size(); i++) {
                CollectionData mColl = arrCollection.get(i);

                JsonObject jColl = new JsonObject();
                invNo = mColl.getInvoiceNo();
                jColl.addProperty("invoice_no", mColl.getInvoiceNo());
                if (mColl.getCollType().equalsIgnoreCase("Cash")) {
                    jColl.addProperty("payment_type", "1");
                    jColl.addProperty("invoice_amount", mColl.getInvoiceAmount());
                    jColl.addProperty("amount", mColl.getAmountPay());
                    totAMt = totAMt + Double.parseDouble(mColl.getAmountPay());
                } else {
                    totAMt = totAMt + Double.parseDouble(mColl.getAmountPay());
                    jColl.addProperty("payment_type", "2");
                    jColl.addProperty("invoice_amount", mColl.getInvoiceAmount());
                    jColl.addProperty("amount", mColl.getAmountPay());
                    jColl.addProperty("cheque_number", mColl.getChequeNo());
                    jColl.addProperty("cheque_date", mColl.getChequeDat());
                    jColl.addProperty("bank_name", mColl.getBankName());
                }
                jsonArray.add(jColl);
            }

            jObj.addProperty("method", App.POST_COLLECTION);
            jObj.addProperty("collection_identifer", UtilApp.getCollectionUnique());
            jObj.addProperty("collection_number", mCollection.getOrderId());
            jObj.addProperty("customer_id", mCollection.getCustomerNo());
            jObj.addProperty("customer_payment_type", db.getCustomerType(mCollection.getCustomerNo()));
            jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("trip_id", Settings.getString(App.ROUTEID));
            jObj.addProperty("total_amount", totAMt);
            jObj.addProperty("is_cust_new", db.isNewCustomer(mCollection.getCustomerNo()));
            jObj.addProperty("invNo", invNo);
            jObj.add("collectionlist", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }


    //Get Return Post Data
    public JsonObject getReturnForPost() {

        ReturnOrder mReturn = db.getPostReturnDetail();
        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getPostReturnItems(mReturn.getOrderNo());

        if (mReturn.getIsReturnRequest().equalsIgnoreCase("1")) {
            try {
                jObj.addProperty("method", App.RETURN_UPDATE);
                jObj.addProperty("partial_return", "1");
                jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                jObj.addProperty("return_id", mReturn.getOrderId());
                jObj.addProperty("returnNo", mReturn.getOrderNo());
                jObj.addProperty("gross_total", mReturn.getGrossAmt());
                jObj.addProperty("excise", mReturn.getExciseAmt());
                jObj.addProperty("vat", mReturn.getVatAmt());
                jObj.addProperty("pre_vat", mReturn.getPreVatAmt());
                jObj.addProperty("net_total", mReturn.getNetTotal());
                jObj.addProperty("discount", "0");
                jObj.addProperty("total_amount", mReturn.getTotalAmt());
                jObj.add("itemlist", jsonArray);
            } catch (JsonIOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                jObj.addProperty("method", App.POST_RETURN);
                jObj.addProperty("currency", "UGX");
                jObj.addProperty("tripid", Settings.getString(App.ROUTEID));
//                if (mReturn.getSalesmanId().equalsIgnoreCase(Settings.getString(App.SALESMANID))) {
//                    jObj.addProperty("salesman_id", "0");
//                } else {
                jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
                //}
                jObj.addProperty("customerid", mReturn.getCust_no());
                jObj.addProperty("returnNo", mReturn.getOrderNo());
                jObj.addProperty("order_date", mReturn.getOrderDate());
                jObj.addProperty("gross_total", mReturn.getGrossAmt());
                jObj.addProperty("excise", mReturn.getExciseAmt());
                jObj.addProperty("vat", mReturn.getVatAmt());
                jObj.addProperty("pre_vat", mReturn.getPreVatAmt());
                jObj.addProperty("net_total", mReturn.getNetTotal());
                jObj.addProperty("discount", "0");
                jObj.addProperty("total_amount", mReturn.getTotalAmt());
                jObj.addProperty("exchangeNo", mReturn.getExchangeNo());
                jObj.addProperty("is_cust_new", db.isNewCustomer(mReturn.getCust_no()));
                jObj.addProperty("recived_date", UtilApp.getCurrentDate());
                jObj.addProperty("user_id", Settings.getString(App.SALESMANID));
                jObj.addProperty("is_agent", "2");
                jObj.add("itemlist", jsonArray);
            } catch (JsonIOException e) {
                e.printStackTrace();
            }
        }

        return jObj;
    }

    //Get Return Post Data
    public JsonObject getReturnRequestForPost() {

        ReturnOrder mReturn = db.getPostReturnRequestDetail();
        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getPostReturnRequestItems(mReturn.getOrderNo());

        try {
            jObj.addProperty("method", App.POST_RETURN);
            jObj.addProperty("currency", "UGX");
            jObj.addProperty("tripid", Settings.getString(App.ROUTEID));
            if (mReturn.getSalesmanId().equalsIgnoreCase(Settings.getString(App.SALESMANID))) {
                jObj.addProperty("salesman_id", "0");
            } else {
                jObj.addProperty("salesman_id", mReturn.getSalesmanId());
            }

            jObj.addProperty("customerid", mReturn.getCust_no());
            jObj.addProperty("returnNo", mReturn.getOrderNo());
            jObj.addProperty("order_date", UtilApp.getCurrentDate());
            jObj.addProperty("gross_total", mReturn.getGrossAmt());
            jObj.addProperty("excise", mReturn.getExciseAmt());
            jObj.addProperty("vat", mReturn.getVatAmt());
            jObj.addProperty("pre_vat", mReturn.getPreVatAmt());
            jObj.addProperty("net_total", mReturn.getNetTotal());
            jObj.addProperty("discount", "0");
            jObj.addProperty("total_amount", mReturn.getTotalAmt());
            jObj.addProperty("exchangeNo", mReturn.getExchangeNo());
            jObj.addProperty("is_cust_new", db.isNewCustomer(mReturn.getCust_no()));
            jObj.addProperty("recived_date", mReturn.getOrderDate());
            jObj.addProperty("user_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("is_agent", "3");
            jObj.add("itemlist", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Load Request Post Data
    public JsonObject getLoadRequestForPost() {

        LoadRequest mLoad = db.getPostLoadRequestDetail();
        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getPostLoadRequestItems(mLoad.getOrderNo());
        System.out.println("po09" + jsonArray.toString());

        try {
            jObj.addProperty("method", App.POST_LOAD_REQUEST);
            jObj.addProperty("currency", "UGX");
            jObj.addProperty("tripid", Settings.getString(App.ROUTEID));
            jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("region", db.getSalesmanRegion(Settings.getString(App.SALESMANID)));
            jObj.addProperty("customerid", mLoad.getCust_no());
            jObj.addProperty("loadrequestNo", mLoad.getOrderNo());
            jObj.addProperty("order_date", mLoad.getOrderDate());
            jObj.addProperty("order_type", "2");
            jObj.addProperty("creditday", db.getAgentCreditDay(mLoad.getCust_no()));
            //jObj.addProperty("order_type", UtilApp.orderType(Settings.getString(App.ROLE)));
            jObj.addProperty("delivery_date", mLoad.getDeliveryDate());
            jObj.addProperty("gross_total", mLoad.getGrossAmt());
            jObj.addProperty("excise", mLoad.getExciseAmt());
            jObj.addProperty("vat", mLoad.getVatAmt());
            jObj.addProperty("pre_vat", mLoad.getPreVatAmt());
            jObj.addProperty("net_total", mLoad.getNetTotal());
            jObj.addProperty("discount", "0");
            jObj.addProperty("total_amount", mLoad.getTotalAmt());
            jObj.addProperty("order_comment", mLoad.getOrderComment());
            jObj.add("itemlist", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }


        return jObj;
    }

    //Get Load Request Post Data
    public JsonObject getSalesmanLoadRequestForPost() {

        LoadRequest mLoad = db.getPostSalesmanLoadRequestDetail();
        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getPostSalesmanLoadRequestItems(mLoad.getOrderNo());
        System.out.println("po09" + jsonArray.toString());

        try {
            jObj.addProperty("method", App.POST_SALESMAN_LOAD_REQUEST);
            jObj.addProperty("route_id", mLoad.routeId);
            jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("load_salesman_id", mLoad.salesmanId);
            jObj.addProperty("load_date", mLoad.orderDate);
            jObj.addProperty("load_no", mLoad.orderNo);
            jObj.add("itemlist", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Load Request Post Data
    public JsonObject getUnLoadRequestForPost() {

        JsonObject jObj = new JsonObject();

        JsonArray jsonArray = db.getPostUnLoadItems();

        try {
            jObj.addProperty("method", App.POST_UNLOAD);
            jObj.addProperty("tripid", Settings.getString(App.ROUTEID));
            jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("customerid", Settings.getString(App.AGENTID));
            jObj.addProperty("unloadNo", Settings.getString(App.UNLOAD_LAST));
            jObj.add("itemlist", jsonArray);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Sale Commision Request Post
    public JsonObject getSaleRequestForPost() {

        JsonObject jObj = new JsonObject();

        JsonArray arrItemList = new JsonArray();
        ArrayList<Item> arrItem = db.getSaleItem();
        for (int i = 0; i < arrItem.size(); i++) {
            JsonObject mItem = new JsonObject();
            mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            mItem.addProperty("item_id", arrItem.get(i).getItemId());
            mItem.addProperty("quantity", arrItem.get(i).getAlterUOMQty());
            arrItemList.add(mItem);
        }

        JsonArray arrCategory = new JsonArray();
        JsonObject mItem;
        mItem = new JsonObject();
        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
        mItem.addProperty("category_id", "2");
        mItem.addProperty("quantity", db.getTotalCategorySale("2"));
        arrCategory.add(mItem);

        mItem = new JsonObject();
        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
        mItem.addProperty("category_id", "1");
        mItem.addProperty("quantity", db.getTotalCategorySale("1"));
        arrCategory.add(mItem);

        mItem = new JsonObject();
        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
        mItem.addProperty("category_id", "3");
        mItem.addProperty("quantity", db.getTotalCategorySale("3"));
        arrCategory.add(mItem);

        mItem = new JsonObject();
        mItem.addProperty("salesman_id", Settings.getString(App.SALESMANID));
        mItem.addProperty("category_id", "4");
        mItem.addProperty("quantity", db.getTotalCategorySale("4"));
        arrCategory.add(mItem);

        //double totalSale = db.getTotalAmtSale();

        try {
            jObj.addProperty("method", App.POST_SALEMAN_COMMISION);
            jObj.add("itemlist", arrItemList);
            jObj.add("categorylist", arrCategory);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Visit Request Post Data
    public JsonObject getVisitRequestForPost() {

        JsonObject jObj = new JsonObject();

        HashMap<String, String> mData = db.getPostVisitItems();

        try {
            jObj.addProperty("method", App.POST_CUSTOMERVISIT);
            jObj.addProperty("tripid", Settings.getString(App.ROUTEID));
            jObj.addProperty("salesman_id", Settings.getString(App.SALESMANID));
            jObj.addProperty("customerid", mData.get("customerid"));
            jObj.addProperty("start_time", mData.get("start_time"));
            jObj.addProperty("end_time", mData.get("end_time"));
            jObj.addProperty("latitude", mData.get("latitude"));
            jObj.addProperty("longitude", mData.get("longitude"));
            jObj.addProperty("captured_latitude", mData.get("captured_latitude"));
            jObj.addProperty("captured_longitude", mData.get("captured_longitude"));
            jObj.addProperty("status", mData.get("status"));
            boolean isplanned = db.isSyncCustomer(mData.get("customerid"), UtilApp.getCurrentDay());
            if (isplanned) {
                jObj.addProperty("unpland", "0");
            } else {
                jObj.addProperty("unpland", "1");
            }

        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Customer invoice location
    public JsonObject getVisitRequestForPostNew() {

        JsonObject jObj = new JsonObject();

        HashMap<String, String> mData = db.getPostVisitItems();

        try {
            jObj.addProperty("method", App.POST_CUSTOMERVISIT_INVOICE);
            jObj.addProperty("customer_id", mData.get("customer_id"));
            jObj.addProperty("latitude", mData.get("latitude"));
            jObj.addProperty("longitude", mData.get("longitude"));
           /* boolean isplanned = db.isSyncCustomer(mData.get("customerid"), UtilApp.getCurrentDay());
            if (isplanned) {
                jObj.addProperty("unpland", "0");
            } else {
                jObj.addProperty("unpland", "1");
            }*/

        } catch (JsonIOException e) {
            e.printStackTrace();
        }

        return jObj;
    }

    //Get Customer Request Post Data
    public JsonObject getCustomerRequestForPost() {

        JsonObject jObj = new JsonObject();

        jObj = db.getPostAddCustomer();

        return jObj;
    }

    //Get Customer Request Post Data
    public JsonObject getCustomerUpdateForPost() {

        JsonObject jObj = new JsonObject();

        jObj = db.getPostUpdateCustomer();

        return jObj;
    }

    //Get Customer Request Post Data
    public JsonObject getDeptCustomerUpdateForPost() {

        JsonObject jObj = new JsonObject();

        jObj = db.getPostDeptUpdateCustomer();

        return jObj;
    }

    //Get Delete Request Post Data
    public String getDeleteForPost() {

        String deliveryID = "";

        deliveryID = db.getPostDeleteItems();

        return deliveryID;
    }


    //Call Invoice API
    private void callInvoiceAPI(JsonObject jObj, String invNo, String exchangeNo) {

        UtilApp.logData(SyncData.this, "Invoice Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Invoice Response:", response.toString());
                UtilApp.logData(SyncData.this, "Invoice Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Invoice Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateInvoiceTransaction(response.body().get("invoice_number").getAsString(), "1", "");
                            db.updateInvoicePost(response.body().get("invoice_number").getAsString(), "1");
                            App.isInvoiceSync = false;

                            if (!exchangeNo.equalsIgnoreCase("")) {
                                db.updateOrderTransaction(exchangeNo, "1", "");
                            }
                        } else {
                            // Fail to Post
                            db.updateInvoicePost(invNo, "2");
                            db.updateInvoiceTransaction(response.body().get("invoice_number").getAsString(), "2",
                                    response.body().get("MESSAGE").getAsString());
                            App.isInvoiceSync = false;
                            if (!exchangeNo.equalsIgnoreCase("")) {
                                db.updateOrderTransaction(exchangeNo, "2", response.body().get("MESSAGE").getAsString());
                            }
                        }
                    } else {
                        App.isInvoiceSync = false;
                    }
                } else {
                    App.isInvoiceSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isInvoiceSync = false;
                db.updateInvoicePost(invNo, "2");
                db.updateInvoiceTransaction(invNo, "2", "API Fail");
                if (!exchangeNo.equalsIgnoreCase("")) {
                    db.updateOrderTransaction(exchangeNo, "2", "API Fail");
                }
                Log.e("Invoice Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Invoice Fail: " + error.toString());
            }
        });
    }

    //Call Load Confirm API
    private void callLoadConfirmAPI(String subLoadId) {

        UtilApp.logData(SyncData.this, "Load Confirm Request: " + subLoadId);

        String image = db.getConfirmLoadImage(subLoadId);

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.LOAD_CONFIRM);
        RequestBody loadId = RequestBody.create(MediaType.parse("text/plain"), subLoadId);

        List<MultipartBody.Part> parts = new ArrayList<>();

        // add dynamic amount
        if (!image.equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePartSign("signature_img", image));
        }

        final Call<JsonObject> labelResponse = ApiClient.getService().postLoadConfirm(method, loadId, parts);

        //final Call<JsonObject> labelResponse = ApiClient.getService().loadConfirm(App.LOAD_CONFIRM, subLoadId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Load Confirm Response", response.toString());
                UtilApp.logData(SyncData.this, "Load Confirm Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        UtilApp.logData(SyncData.this, "Load Confirm Body Response: " + response.body().toString());
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateLoadPost(subLoadId, "1");
                            db.updateLoadTransaction(subLoadId, "1", "");
                            App.isLoadSync = false;
                        } else {
                            db.updateLoadTransaction(subLoadId, "2", response.body().get("MESSAGE").getAsString());
                            App.isLoadSync = false;
                            db.updateLoadPost(subLoadId, "2");
                        }
                        sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    db.updateLoadTransaction(subLoadId, "2", "response blank");
                    App.isLoadSync = false;
                    db.updateLoadPost(subLoadId, "2");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isLoadSync = false;
                Log.e("Load Confirm Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Load Confirm fail: " + error.getMessage());
                db.updateLoadTransaction(subLoadId, "2", "API Fail");
                db.updateLoadPost(subLoadId, "2");
                callLoadConfirmAPI(subLoadId);
            }
        });
    }

    //Call Order API
    private void callOrderAPI(JsonObject jObj, String invNo) {

        UtilApp.logData(SyncData.this, "Order Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Order Response:", response.toString());
                UtilApp.logData(SyncData.this, "Order Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Order Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateOrderTransaction(response.body().get("order_number").getAsString(), "1", "");
                            db.updateOrderPost(response.body().get("order_number").getAsString(), "1");
                            App.isOrderSync = false;
                        } else {
                            // Fail to Post
                            db.updateOrderPost(invNo, "2");
                            db.updateOrderTransaction(invNo, "2", response.body().get("MESSAGE").getAsString());
                            App.isOrderSync = false;
                        }
                    }
                } else {
                    App.isOrderSync = false;
                    db.updateOrderTransaction(invNo, "2", "Resonse Blank");
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isOrderSync = false;
                UtilApp.logData(SyncData.this, "Order Fail: " + error.toString());
                Log.e("Order Fail", error.getMessage());
                db.updateOrderPost(invNo, "2");
                db.updateOrderTransaction(invNo, "2", "API Fail");
            }
        });


    }

    //Call Collection API
    private void callCollectionAPI(JsonObject jObj, String invNo) {

        UtilApp.logData(SyncData.this, "Collection Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Collection Response:", response.toString());
                UtilApp.logData(SyncData.this, "Collection Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Collection Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateCollectionTransaction(response.body().get("collection_number").getAsString(), "1", "");
                            db.updateCollectionPost(response.body().get("collection_number").getAsString(), "1");
                            App.isCollectionSync = false;
                        } else {
                            // Fail to Post
                            db.updateCollectionPost(invNo, "2");
                            App.isCollectionSync = false;
                            db.updateCollectionTransaction(invNo, "2", response.body().get("MESSAGE").getAsString());
                        }
                    }
                } else {
                    App.isCollectionSync = false;
                    db.updateCollectionTransaction(invNo, "2", "Blank Response");
                }
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isCollectionSync = false;
                db.updateCollectionPost(invNo, "2");
                db.updateCollectionTransaction(invNo, "2", "API Fail");
                Log.e("Collection Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Collection Fail: " + error.getMessage());
            }
        });

    }

    //Call Return API
    private void callReturnAPI(JsonObject jObj, String invNo) {

        UtilApp.logData(SyncData.this, "Return Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Return Response:", response.toString());
                UtilApp.logData(SyncData.this, "Return Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Return Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateOrderTransaction(invNo, "1", "");
                            db.updateReturnPost(invNo, "1");
                            App.isReturnSync = false;
                        } else {
                            // Fail to Post
                            App.isReturnSync = false;
                            db.updateReturnPost(invNo, "2");
                            db.updateOrderTransaction(invNo, "2", response.body().get("MESSAGE").getAsString());
                        }
                    }
                } else {
                    App.isReturnSync = false;
                    db.updateReturnPost(invNo, "2");
                    db.updateOrderTransaction(invNo, "2", "Blank Response");
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isReturnSync = false;
                Log.e("Return Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Return Fail: " + jObj.toString());
                db.updateReturnPost(invNo, "2");
                db.updateOrderTransaction(invNo, "2", "API Fail");
            }
        });
    }

    //Call Return API
    private void callReturnRequestAPI(JsonObject jObj, String invNo) {

        UtilApp.logData(SyncData.this, "Return Request Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Return Response:", response.toString());
                UtilApp.logData(SyncData.this, "Return Request Response: " + response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {
                        UtilApp.logData(SyncData.this, "Return Request Body Response: " + response.body().toString());
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateOrderTransaction(response.body().get("return_number").getAsString(), "1", "");
                            db.updateReturnRequestPost(response.body().get("return_number").getAsString(), "1");
                            App.isReturnRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isReturnRequestSync = false;
                            db.updateReturnRequestPost(invNo, "2");
                            db.updateOrderTransaction(invNo, "2", response.body().get("MESSAGE").getAsString());
                        }
                    }
                } else {
                    App.isReturnRequestSync = false;
                    db.updateReturnRequestPost(invNo, "2");
                    db.updateOrderTransaction(invNo, "2", "Blank Response");
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isReturnRequestSync = false;
                Log.e("Return Fail", error.getMessage());
                db.updateReturnRequestPost(invNo, "2");
                UtilApp.logData(SyncData.this, "Return Request Fail: " + error.getMessage());
                db.updateOrderTransaction(invNo, "2", "API Fail");
            }
        });
    }

    //Call Load Request API
    private void callLoadRequestAPI(JsonObject jObj, String invNo) {

        UtilApp.logData(SyncData.this, "Load Request Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Load Request Response:", response.toString());
                UtilApp.logData(SyncData.this, "Load Request Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Load Request Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateOrderTransaction(response.body().get("load_request_number").getAsString(), "1", "");
                            db.updateLoadRequestPost(response.body().get("load_request_number").getAsString(), "1");
                            App.isLoadRequestSync = false;
                        } else {
                            // Fail to Post
                            db.updateLoadRequestPost(invNo, "2");
                            db.updateOrderTransaction(invNo, "2", response.body().get("MESSAGE").getAsString());
                            App.isLoadRequestSync = false;
                        }

                    }
                } else {
                    db.updateLoadRequestPost(invNo, "2");
                    db.updateOrderTransaction(invNo, "2", "Blank Response");
                    App.isLoadRequestSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isLoadRequestSync = false;
                Log.e("Load Request Fail", error.getMessage());
                db.updateLoadRequestPost(invNo, "2");
                UtilApp.logData(SyncData.this, "Load Request Fail: " + error.getMessage());
                db.updateOrderTransaction(invNo, "2", "API Fail");
            }
        });
    }

    //Call Load Request API
    private void callSalesmanLoadRequestAPI(JsonObject jObj, String invNo) {

        UtilApp.logData(SyncData.this, "SalesmanLoad Request Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("SalesmanLoadRequest Response:", response.toString());
                UtilApp.logData(SyncData.this, "SalesmanLoad Request Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "SalesmanLoad Request Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateOrderTransaction(invNo, "1", "");
                            db.updateSalesmanLoadRequestPost(invNo, "1");
                            App.isSalesmanLoadSync = false;
                        } else {
                            // Fail to Post
                            db.updateSalesmanLoadRequestPost(invNo, "2");
                            db.updateOrderTransaction(invNo, "2", response.body().get("MESSAGE").getAsString());
                            App.isSalesmanLoadSync = false;
                        }

                    }
                } else {
                    db.updateSalesmanLoadRequestPost(invNo, "2");
                    db.updateOrderTransaction(invNo, "2", "Blank Response");
                    App.isSalesmanLoadSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isSalesmanLoadSync = false;
                Log.e("SalesmanLoad Request Fail", error.getMessage());
                db.updateSalesmanLoadRequestPost(invNo, "2");
                UtilApp.logData(SyncData.this, "SalesmanLoad Request Fail: " + error.getMessage());
                db.updateOrderTransaction(invNo, "2", "API Fail");

                sendBroadcast(intent);
            }
        });
    }

    //Call Unload API
    private void callUnloadAPI(JsonObject jObj, String invNo) {

        UtilApp.logData(SyncData.this, "Unload Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Unload Response:", response.toString());
                UtilApp.logData(SyncData.this, "Unload Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Unload Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateUnloadTransaction(response.body().get("unload_no").getAsString(), "1", "");
                            db.updateUnloadData();
                            App.isUnloadRequestSync = false;
                        } else {
                            // Fail to Post
                            db.updateUnloadTransaction(invNo, "2", response.body().get("MESSAGE").getAsString());
                            App.isUnloadRequestSync = false;
                        }
                    } else {
                        db.updateUnloadTransaction(invNo, "2", "Blank Response");
                        App.isUnloadRequestSync = false;
                    }
                } else {
                    db.updateUnloadTransaction(invNo, "2", "Blank Response");
                    App.isUnloadRequestSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isUnloadRequestSync = false;
                Log.e("Unload Fail", error.getMessage());
                db.updateUnloadTransaction(invNo, "2", "API Fail");
                UtilApp.logData(SyncData.this, "Unload Fail: " + error.getMessage());
                callUnloadAPI(jObj, invNo);
            }
        });
    }

    //Call Sales Amount API
    private void callSaleAmountAPI(JsonObject jObj) {

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Commission Response:", response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Commision Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            Settings.setString(App.IS_SALE_POSTED, "1");
                            App.isSalePostRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isSalePostRequestSync = false;
                        }
                    }
                } else {
                    App.isSalePostRequestSync = false;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isSalePostRequestSync = false;
                Log.e("Commission Post Fail", error.getMessage());
            }
        });
    }


    //Call Visit API
    private void callVisitAPI(JsonObject jObj) {

        UtilApp.logData(SyncData.this, "Customer Visit Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Visit Response:", response.toString());
                UtilApp.logData(SyncData.this, "Customer Visit Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Customer Visit Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateVisitStatus(response.body().get("customerid").getAsString());
                            App.isVisitRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isVisitRequestSync = false;
                        }
                    }
                } else {
                    App.isVisitRequestSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isVisitRequestSync = false;
                Log.e("Visit Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Customer Visit Fail: " + error.getMessage());
            }
        });
    }

    //Call invoice location API
    private void callVisitInvoiceLocationAPI(JsonObject jObj) {

        UtilApp.logData(SyncData.this, "Customer invoice location Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("invoice location Response:", response.toString());
                UtilApp.logData(SyncData.this, "Customer invoice location Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Customer Invoice Location Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateInvoiceLocationStatus(response.body().get("customerid").getAsString());
                            App.isVisitRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isVisitRequestSync = false;
                        }
                    }
                } else {
                    App.isVisitRequestSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isVisitRequestSync = false;
                Log.e("Visit Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Customer Visit Fail: " + error.getMessage());
            }
        });
    }

    //Call Delete API
    private void callDeleteAPI(String deliveryId) {

        UtilApp.logData(SyncData.this, "Delivery Delete Request: " + deliveryId);

        final Call<JsonObject> labelResponse = ApiClient.getService().deleteDelivery(App.DELIVERY_DELETE, deliveryId);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Delete Response:", response.toString());
                UtilApp.logData(SyncData.this, "Delivery Delete Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Delivery Delete Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateDeliveryDeleteStatus(deliveryId);
                            App.isDeleteRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isDeleteRequestSync = false;
                        }
                    }
                } else {
                    App.isDeleteRequestSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isDeleteRequestSync = false;
                Log.e("Delete Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Delivery delete Fail: " + error.getMessage());
            }
        });
    }

    //Call Add Customer API
    private void callAddCustomerAPI(JsonObject jObj) {

        UtilApp.logData(SyncData.this, "Add Customer Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postInvoice(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Add Cus Response:", response.toString());
                UtilApp.logData(SyncData.this, "Add Customer Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Add Customer Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "1", "");
                            db.updateCustomerAddStatus(jObj.get("cust_code").getAsString(), "1");
                            App.isAddRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isAddRequestSync = false;
                            db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", response.body().get("MESSAGE").getAsString());
                            db.updateCustomerAddStatus(jObj.get("cust_code").getAsString(), "2");
                        }
                    }
                } else {
                    App.isAddRequestSync = false;
                    db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", "Blank Response");
                    db.updateCustomerAddStatus(jObj.get("cust_code").getAsString(), "2");
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isAddRequestSync = false;
                Log.e("Add Cust Fail", error.getMessage());
                db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", "API Fail");
                db.updateCustomerAddStatus(jObj.get("cust_code").getAsString(), "2");
                UtilApp.logData(SyncData.this, "Add Customer Fail: " + error.getMessage());

                sendBroadcast(intent);
            }
        });
    }

    //Call Add Customer API
    private void callUpdateCustomerAPI(JsonObject jObj) {

        UtilApp.logData(SyncData.this, "Add Customer Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().getUpdateCustomer(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Update Cus Response:", response.toString());
                UtilApp.logData(SyncData.this, "Update Customer Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Update Customer Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "1", "");
                            db.updateCustomerAddStatus(jObj.get("customer_id").getAsString(), "1");
                            App.isUpdateRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isUpdateRequestSync = false;
                            db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", response.body().get("MESSAGE").getAsString());
                            db.updateCustomerAddStatus(jObj.get("customer_id").getAsString(), "2");
                        }
                    }
                } else {
                    App.isUpdateRequestSync = false;
                    db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", "Blank Response");
                    db.updateCustomerAddStatus(jObj.get("customer_id").getAsString(), "2");
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isUpdateRequestSync = false;
                Log.e("Update Cust Fail", error.getMessage());
                db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", "API Fail");
                db.updateCustomerAddStatus(jObj.get("customer_id").getAsString(), "2");
                UtilApp.logData(SyncData.this, "Update Customer Fail: " + error.getMessage());

                sendBroadcast(intent);
            }
        });
    }

    //Call Add Customer API
    private void callUpdateDeptCustomerAPI(JsonObject jObj) {

        UtilApp.logData(SyncData.this, "Add Customer Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().getUpdateCustomer(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Update Cus Response:", response.toString());
                UtilApp.logData(SyncData.this, "Update Customer Response: " + response.toString());
                if (response.body() != null) {
                    UtilApp.logData(SyncData.this, "Update Customer Body Response: " + response.body().toString());
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "1", "");
                            db.updateDeptCustomerAddStatus(jObj.get("customer_id").getAsString(), "1");
                            App.isDeptUpdateRequestSync = false;
                        } else {
                            // Fail to Post
                            App.isDeptUpdateRequestSync = false;
                            db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", response.body().get("MESSAGE").getAsString());
                            db.updateDeptCustomerAddStatus(jObj.get("customer_id").getAsString(), "2");
                        }
                    }
                } else {
                    App.isDeptUpdateRequestSync = false;
                    db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", "Blank Response");
                    db.updateDeptCustomerAddStatus(jObj.get("customer_id").getAsString(), "2");
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isDeptUpdateRequestSync = false;
                Log.e("Update Cust Fail", error.getMessage());
                db.updateCustomerTransaction(jObj.get("cust_code").getAsString(), "2", "API Fail");
                db.updateDeptCustomerAddStatus(jObj.get("customer_id").getAsString(), "2");
                UtilApp.logData(SyncData.this, "Update Customer Fail: " + error.getMessage());

                sendBroadcast(intent);
            }
        });
    }

    //Call Planogram API
    private void callPlanogramAPI() {

        PlanogramList mPlanogram = db.getPostPlanogramDetail();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.PLANOGRAM_POST);
        RequestBody customerId = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCustomerId());
        RequestBody routeId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody distributionId = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getDistribution_tool_id());
        RequestBody planogramId = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getPlanogramId());
        RequestBody discription = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getComment());

        List<MultipartBody.Part> parts = new ArrayList<>();

        if (!mPlanogram.getBack_image().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("before_img", mPlanogram.getBack_image()));
        }

        if (!mPlanogram.getFront_image().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("after_img", mPlanogram.getFront_image()));
        }

        UtilApp.logData(SyncData.this, "Planogram Request: " + mPlanogram.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postPlanogramData(method, routeId, salesmanId, customerId,
                distributionId, planogramId, discription, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Palnogram Response:", response.toString());
                UtilApp.logData(SyncData.this, "Planogram Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                Log.e("Status", jsonObject.getString("STATUS"));
                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                    App.isPlanogramSync = true;
                                    Log.e("Planogram Success", jsonObject.getString("STATUS"));
                                    db.updatePlanogramPosted(mPlanogram.getPlanogramId(), mPlanogram.getDistribution_tool_id());
                                    String strPlanoId = "PLN" + mPlanogram.getPlanogramId() + mPlanogram.getDistribution_tool_id();
                                    db.updateTransaction(strPlanoId);
                                } else {
                                    // Fail to Post
                                    App.isPlanogramSync = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        App.isPlanogramSync = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Planogram fail:", t.toString());
                UtilApp.logData(SyncData.this, "Planogram Fail: " + t.getMessage());
                App.isPlanogramSync = true;
            }
        });

    }

    //Call Complaint API
    private void callComplaintAPI() {


        Complain mPlanogram = db.getComplaintPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.COMPLAINT_FEEDBACK);
        RequestBody routeId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getComplain_Feedback());
        RequestBody Description = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getComplanin_Note());
        RequestBody Category = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getComplain_brand());
        RequestBody item_id = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getItemId());

        List<MultipartBody.Part> parts = new ArrayList<>();

        // add dynamic amount
        if (!mPlanogram.getComplain_Image1().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image1", mPlanogram.getComplain_Image1()));
        }

        if (!mPlanogram.getComplain_Image2().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image2", mPlanogram.getComplain_Image2()));
        }

        if (!mPlanogram.getComplain_Image3().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image3", mPlanogram.getComplain_Image3()));
        }

        if (!mPlanogram.getComplain_Image4().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image4", mPlanogram.getComplain_Image4()));
        }

        UtilApp.logData(SyncData.this, "Complain Request: " + mPlanogram.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postComplainData(method, routeId, salesmanId, title,
                Description, Category, item_id, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Complain Response:", response.toString());
                UtilApp.logData(SyncData.this, "Complain Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                Log.e("Complain Success", jsonObject.getString("STATUS"));
                                db.updateComplainPosted(mPlanogram.getComplainId());
                                db.updateTransaction(mPlanogram.getComplainId());
                                App.isComplaintSync = false;
                            } else {
                                // Fail to Post
                                App.isComplaintSync = false;
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Complain fail:", t.toString());
                UtilApp.logData(SyncData.this, "Complain Fail: " + t.getMessage());
                App.isComplaintSync = false;
            }
        });

    }

    //Call Compititoer API
    private void callCompititorAPI() {


        Compititor mPlanogram = db.getCompititorPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.COMPITITOR_POST);
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody companyName = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCompititorCompanyName());
        RequestBody Brand = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCompititor_brand());
        RequestBody ItemName = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCompititor_ItemName());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCOMPITITOR_Price());
        RequestBody Note = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCompititor_Notes());
        RequestBody Promotion = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCompititor_Promotion());

        List<MultipartBody.Part> parts = new ArrayList<>();

//        // add dynamic amount
        if (!mPlanogram.getCompititor_Image1().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image1", mPlanogram.getCompititor_Image1()));
        }

        if (!mPlanogram.getCompititor_Image2().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image2", mPlanogram.getCompititor_Image2()));
        }

        if (!mPlanogram.getCompititor_Image3().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image3", mPlanogram.getCompititor_Image3()));
        }

        if (!mPlanogram.getCompititor_Image4().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image4", mPlanogram.getCompititor_Image4()));
        }

        UtilApp.logData(SyncData.this, "Compatitor Feedback Request: " + mPlanogram.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postCompatitorData(method, salesmanId, companyName,
                Brand, ItemName, price, Note, Promotion, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Compititor Response:", response.toString());
                UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                Log.e("Status", jsonObject.getString("STATUS"));
                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                    App.isCompititorSync = false;
                                    Log.e("Compititor Success", jsonObject.getString("STATUS"));
                                    db.updateCampititorPosted(mPlanogram.getCompititorId());
                                    db.updateTransaction(mPlanogram.getCompititorId());
                                } else {
                                    // Fail to Post
                                    App.isCompititorSync = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        App.isCompititorSync = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Compititor fail:", t.toString());
                UtilApp.logData(SyncData.this, "Compititoer Fail: " + t.getMessage());
                App.isCompititorSync = false;
            }
        });
    }

    //Call Compititoer API
    private void callCampaignAPI() {


        Compaign mPlanogram = db.getCampaignPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CAMPAIGN_POST);
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody CampaignID = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCompaignId());
        RequestBody CustomerID = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getCustomerId());
        RequestBody Feedback = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getComment());
        List<MultipartBody.Part> parts = new ArrayList<>();

        // add dynamic amount
        if (!mPlanogram.getCompaign_Image1().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image1", mPlanogram.getCompaign_Image1()));
        }

        if (!mPlanogram.getCompaign_Image2().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image2", mPlanogram.getCompaign_Image2()));
        }

        if (!mPlanogram.getCompaign_Image3().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image3", mPlanogram.getCompaign_Image3()));
        }

        if (!mPlanogram.getCompaign_Image4().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image4", mPlanogram.getCompaign_Image4()));
        }

        UtilApp.logData(SyncData.this, "Campaign Request: " + mPlanogram.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postCampaignData(method, salesmanId, CampaignID,
                CustomerID, Feedback, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Campaign Response:", response.toString());
                UtilApp.logData(SyncData.this, "Campaign Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                    App.isCampaignSync = false;
                                    db.updateCampaignPosted(mPlanogram.getCompaignId());
                                    db.updateTransaction(mPlanogram.getCompaignId());
                                    Log.e("Campaign Success", jsonObject.getString("STATUS"));
                                } else {
                                    // Fail to Post
                                    App.isCampaignSync = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        App.isCampaignSync = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Campaign fail:", t.toString());
                UtilApp.logData(SyncData.this, "Campaign Fail: " + t.getMessage());
                App.isCampaignSync = false;
            }
        });

    }

    //Call Promotional API
    private void callPromotionAPI() {


        Promotion mPlanogram = db.getPromotionalPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.PROMOTIONAL);
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody custName = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getPromotioncustomerName());
        RequestBody custPhone = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getPromotioncustPhone());
        RequestBody invoiceNo = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getInvoiceNo());
        RequestBody amount = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getAmount());
        RequestBody itemId = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getPromotionItemId());

        List<MultipartBody.Part> parts = new ArrayList<>();

        // add dynamic amount
        if (!mPlanogram.getInvoiceImage().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image1", mPlanogram.getInvoiceImage()));
        }

        UtilApp.logData(SyncData.this, "Pramotion Request: " + mPlanogram.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postPromotionalData(method, salesmanId, custName,
                custPhone, amount, itemId, invoiceNo, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Promotional Response:", response.toString());
                UtilApp.logData(SyncData.this, "Promotional Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                    App.isPromotionalSync = false;
                                    db.updatePromotionalPosted(mPlanogram.getPromotionId());
                                    db.updateTransaction(mPlanogram.getPromotionId());
                                    Log.e("Promotional Success", jsonObject.getString("STATUS"));
                                } else {
                                    // Fail to Post
                                    App.isPromotionalSync = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        App.isPromotionalSync = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Promotional fail:", t.toString());
                App.isPromotionalSync = false;
                UtilApp.logData(SyncData.this, "Promotional Fail: " + t.getMessage());
            }
        });

    }

    //Call Promotional API
    private void callCHillerAddAPI() {

        Promotion mPlanogram = db.getChillerPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.ADD_CHILLER);
        RequestBody depot_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.DEPOTID));
        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getPromotioncustomerName());
        RequestBody asset_no = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getPromotioncustPhone());
        List<MultipartBody.Part> parts = new ArrayList<>();

        try {
            if (!mPlanogram.getInvoiceImage().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPlanogram.getInvoiceImage()));
            }
        } catch (Exception e) {
            e.toString();
        }

        UtilApp.logData(SyncData.this, "Chiller Request: " + mPlanogram.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postAddFridgeData(method, depot_id, salesman_id,
                customer_id, asset_no, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Chiller Response:", response.toString());
                UtilApp.logData(SyncData.this, "Chiller Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                App.isChillerSync = false;
                                db.updateChillerPosted(mPlanogram.getPromotionId(), "1");
                                db.updateOrderTransaction(mPlanogram.getPromotionId(), "1", "");
                                Log.e("Chiller Success", jsonObject.getString("STATUS"));
                            } else {
                                // Fail to Post
                                App.isChillerSync = false;
                                db.updateOrderTransaction(mPlanogram.getPromotionId(), "2", response.body().get("MESSAGE").getAsString());
                                db.updateChillerPosted(mPlanogram.getPromotionId(), "2");
                            }
                        } else {
                            // Fail to Post
                            App.isChillerSync = false;
                            db.updateOrderTransaction(mPlanogram.getPromotionId(), "2", "API fail");
                            db.updateChillerPosted(mPlanogram.getPromotionId(), "2");
                        }
                    } catch (Exception e) {
                        App.isChillerSync = false;
                        db.updateOrderTransaction(mPlanogram.getPromotionId(), "2", "API fail");
                        db.updateChillerPosted(mPlanogram.getPromotionId(), "2");
                    }

                    sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Chiller fail:", t.toString());
                App.isChillerSync = false;
                UtilApp.logData(SyncData.this, "Chiller Fail: " + t.getMessage());
                db.updateOrderTransaction(mPlanogram.getPromotionId(), "2", "API fail");
                db.updateChillerPosted(mPlanogram.getPromotionId(), "2");

                sendBroadcast(intent);
            }
        });

    }

    //Call Promotional API
    private void callFridgeAddAPI() {

        Freeze mPromotion = db.getFridgePost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
        RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getCustomer_id());
        RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getHave_fridge());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLatitude());
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLongitude());
        RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComments());
        RequestBody Complaint_type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComplaint_type());
        RequestBody serial_no = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSerial_no());

        List<MultipartBody.Part> parts = new ArrayList<>();

//        // add dynamic amount
        try {
            if (!mPromotion.getImage1().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
            }

            if (!mPromotion.getImage2().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
            }

            if (!mPromotion.getImage3().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
            }

            if (!mPromotion.getImage4().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
            }
        } catch (Exception e) {
            e.toString();
        }
        try {
            if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
            }
        } catch (Exception e) {
            e.toString();
        }

        UtilApp.logData(this, "Compatitor Feedback Request: " + mPromotion.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postFridgeData(method, route_id, salesman_id,
                customer_id, have_fridge, latitude, longitude, comments, Complaint_type, serial_no, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Chiller Response:", response.toString());
                UtilApp.logData(SyncData.this, "Chiller Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                App.isFRIDGESync = false;
                                db.updateFridggePosted(mPromotion.getSalesman_id(), "1");
                                db.updateOrderTransaction(mPromotion.getSalesman_id(), "1", "");
                                Log.e("Chiller Success", jsonObject.getString("STATUS"));
                            } else {
                                // Fail to Post
                                App.isFRIDGESync = false;
                                db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", response.body().get("MESSAGE").getAsString());
                                db.updateFridggePosted(mPromotion.getSalesman_id(), "2");
                            }
                        } else {
                            // Fail to Post
                            App.isFRIDGESync = false;
                            db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", "API fail");
                            db.updateFridggePosted(mPromotion.getSalesman_id(), "2");
                        }
                    } catch (Exception e) {
                        App.isFRIDGESync = false;
                        db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", "API fail");
                        db.updateFridggePosted(mPromotion.getSalesman_id(), "2");
                    }
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Chiller fail:", t.toString());
                UtilApp.logData(SyncData.this, "Chiller Fail: " + t.getMessage());
                App.isFRIDGESync = false;
                db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", "API fail");
                db.updateFridggePosted(mPromotion.getSalesman_id(), "2");
                sendBroadcast(intent);
            }
        });

    }

    //Call Promotional API
    private void callCHillerRequestAPI() {

        Chiller_Model mPromotion = db.getChillerRequestPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_POST);
        RequestBody chiller_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getdepot_id());
        RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getCustomer_id());
        RequestBody depot_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.DEPOTID));
        RequestBody contact_number = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getContact_number());
        RequestBody cust_address = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getPostal_address());
        RequestBody cust_address2 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLandmark());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLocation());
        RequestBody isMerchandiser;
        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
            isMerchandiser = RequestBody.create(MediaType.parse("text/plain"), "0");
        } else {
            isMerchandiser = RequestBody.create(MediaType.parse("text/plain"), "1");
        }

        RequestBody outlettype = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_type());
        RequestBody ownername = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOwner_name());
        RequestBody specify_if_other_type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSpecify_if_other_type());
        RequestBody existing_cooler = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getExisting_coolers());
        RequestBody stock = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getStock_share_with_competitor());


        //lc_letter,trading_licence,
        RequestBody currentsale = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_weekly_sale_volume());
        RequestBody expectedsale = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_weekly_sales());
        RequestBody chillersize = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getChiller_size_requested());
        RequestBody grill = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getChiller_safty_grill());
        RequestBody display_location = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getDisplay_location());

        RequestBody nation_id1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getNational_id());
        RequestBody password_photo1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getPassword_photo());
        RequestBody outlet_address_proof1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_address_proof());
        RequestBody outlet_stamp1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_stamp());
        RequestBody lc_letter1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLc_letter());
        RequestBody trading_licence1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getTrading_licence());

        MultipartBody.Part body1 = null;
        try {
            File file_bh = new File(mPromotion.getSign__customer_file());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
            body1 = MultipartBody.Part.createFormData("sign__customer_file", file_bh.getName(), reqFile);
        } catch (Exception e) {

        }

        List<MultipartBody.Part> parts = new ArrayList<>();

//        // add dynamic amount
        try {
            if (!mPromotion.getNational_id_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("national_id_file", mPromotion.getNational_id_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getNational_id1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("national_id1_file", mPromotion.getNational_id1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getPassword_photo_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("password_photo_file", mPromotion.getPassword_photo_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getPassword_photo1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("password_photo1_file", mPromotion.getPassword_photo1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getOutlet_address_proof_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_address_proof_file", mPromotion.getOutlet_address_proof_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getOutlet_address_proof1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_address_proof1_file", mPromotion.getOutlet_address_proof1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getTrading_licence_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("trading_licence_file", mPromotion.getTrading_licence_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getTrading_licence1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("trading_licence1_file", mPromotion.getTrading_licence1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getLc_letter_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("lc_letter_file", mPromotion.getLc_letter_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getLc_letter1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("lc_letter1_file", mPromotion.getLc_letter1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getOutlet_stamp_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_stamp_file", mPromotion.getOutlet_stamp_file()));
            }
        } catch (Exception e) {
            e.toString();
        }

        try {
            if (!mPromotion.getOutlet_stamp1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_stamp1_file", mPromotion.getOutlet_stamp1_file()));
            }
        } catch (Exception e) {
            e.toString();
        }


        UtilApp.logData(this, "Compatitor Feedback Request: " + mPromotion.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postChillerData(method, chiller_id, depot_id, salesman_id,
                route_id, customer_id, ownername, contact_number, cust_address, cust_address2,
                location, outlettype, specify_if_other_type, existing_cooler, stock, currentsale,
                expectedsale, chillersize, grill, display_location, nation_id1, password_photo1,
                outlet_address_proof1, outlet_stamp1, lc_letter1, trading_licence1, isMerchandiser, parts, body1);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Chiller Response:", response.toString());
                UtilApp.logData(SyncData.this, "Chiller Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                App.isChillerRequestSync = false;
                                db.updateChillerRequestPosted(mPromotion.getdepot_id(), "1");
                                db.updateOrderTransaction(mPromotion.getdepot_id(), "1", "");
                                Log.e("Chiller Success", jsonObject.getString("STATUS"));
                            } else {
                                // Fail to Post
                                App.isChillerRequestSync = false;
                                db.updateOrderTransaction(mPromotion.getdepot_id(), "2", response.body().get("MESSAGE").getAsString());
                                db.updateChillerRequestPosted(mPromotion.getdepot_id(), "2");
                            }
                        } else {
                            // Fail to Post
                            App.isChillerRequestSync = false;
                            db.updateOrderTransaction(mPromotion.getdepot_id(), "2", "API fail");
                            db.updateChillerRequestPosted(mPromotion.getdepot_id(), "2");
                        }
                    } catch (Exception e) {
                        App.isChillerRequestSync = false;
                        db.updateOrderTransaction(mPromotion.getdepot_id(), "2", "API fail");
                        db.updateChillerRequestPosted(mPromotion.getdepot_id(), "2");
                    }
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Chiller fail:", t.toString());
                App.isChillerRequestSync = false;
                UtilApp.logData(SyncData.this, "Chiller Fail: " + t.getMessage());
                db.updateOrderTransaction(mPromotion.getdepot_id(), "2", "API fail");
                db.updateChillerRequestPosted(mPromotion.getdepot_id(), "2");

                sendBroadcast(intent);
            }
        });

    }

    //Call Promotional API
    private void callServiceVisitPostAPI() {

        ServiceVisitPost mPromotion = db.getServiceVisitPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.SERVICE_VISIT_POST);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.serviceType);
        RequestBody tickeNo = RequestBody.create(MediaType.parse("text/plain"), mPromotion.ticketNo);
        RequestBody timeIn = RequestBody.create(MediaType.parse("text/plain"), mPromotion.timeIn);
        RequestBody timeOutM = RequestBody.create(MediaType.parse("text/plain"), mPromotion.timeOut);
        RequestBody mlatitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.latitude);
        RequestBody mlongitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.longitude);
        RequestBody modelName = RequestBody.create(MediaType.parse("text/plain"), mPromotion.modelNo);
        RequestBody assetNo = RequestBody.create(MediaType.parse("text/plain"), mPromotion.assetNo);
        RequestBody serialNo = RequestBody.create(MediaType.parse("text/plain"), mPromotion.serialNo);
        RequestBody brandingM = RequestBody.create(MediaType.parse("text/plain"), mPromotion.brand);
        RequestBody moutletName = RequestBody.create(MediaType.parse("text/plain"), mPromotion.outletName);
        RequestBody mOwnerName = RequestBody.create(MediaType.parse("text/plain"), mPromotion.ownerName);
        RequestBody mlandMark = RequestBody.create(MediaType.parse("text/plain"), mPromotion.landmark);
        RequestBody mTown = RequestBody.create(MediaType.parse("text/plain"), mPromotion.townVillage);
        RequestBody mRoadStreet = RequestBody.create(MediaType.parse("text/plain"), mPromotion.location);
        RequestBody mContact = RequestBody.create(MediaType.parse("text/plain"), mPromotion.contactNumber);
        RequestBody mContact2 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.contactNumber2);
        RequestBody mContactPerson = RequestBody.create(MediaType.parse("text/plain"), mPromotion.contactPerson);
        RequestBody mCTSStatus = RequestBody.create(MediaType.parse("text/plain"), mPromotion.ctsStatus);

        RequestBody mDistrict = RequestBody.create(MediaType.parse("text/plain"), mPromotion.district);
        RequestBody mTechRate = RequestBody.create(MediaType.parse("text/plain"), mPromotion.techRating);
        RequestBody mQalityRate = RequestBody.create(MediaType.parse("text/plain"), mPromotion.qualityRate);
        RequestBody techniquse = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));


        MultipartBody.Part bodyScan = null;
        if (!mPromotion.serialImage.isEmpty()) {
            try {
                String image3Name = mPromotion.serialImage.substring(mPromotion.serialImage.lastIndexOf("/") + 1);
                bodyScan = UtilApp.prepareFilePart("scan_image", image3Name);
            } catch (Exception e) {

            }
        }

        RequestBody isMachine_work = RequestBody.create(MediaType.parse("text/plain"), mPromotion.workingId);
        MultipartBody.Part bodyMAchine = null;

        if (!mPromotion.conditionImage.isEmpty()) {
            try {
                String image1Name = mPromotion.conditionImage.substring(mPromotion.conditionImage.lastIndexOf("/") + 1);
                bodyMAchine = UtilApp.prepareFilePart("is_machine_in_working_img", image1Name);
            } catch (Exception e) {

            }
        }


        RequestBody cleanlesAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.cleanlessId);

        MultipartBody.Part bodyCleanless = null;
        if (!mPromotion.cleanlessImage.isEmpty()) {
            try {
                String image2Name = mPromotion.cleanlessImage.substring(mPromotion.cleanlessImage.lastIndexOf("/") + 1);
                bodyCleanless = UtilApp.prepareFilePart("cleanliness_img", image2Name);
            } catch (Exception e) {

            }
        }

        RequestBody coilAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.coilId);
        MultipartBody.Part bodyCoil = null;
        if (!mPromotion.coilImage.isEmpty()) {
            try {
                String image3Name = mPromotion.coilImage.substring(mPromotion.coilImage.lastIndexOf("/") + 1);
                bodyCoil = UtilApp.prepareFilePart("condensor_coil_cleand_img", image3Name);
            } catch (Exception e) {

            }
        }


        RequestBody stocKAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.stockPer);
        MultipartBody.Part bodyStock = null;
        if (!mPromotion.stockImage.isEmpty()) {
            try {
                String image4Name = mPromotion.stockImage.substring(mPromotion.stockImage.lastIndexOf("/") + 1);
                bodyStock = UtilApp.prepareFilePart("stock_availability_in_img", image4Name);
            } catch (Exception e) {

            }
        }


        RequestBody gasKetAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.gasketId);
        MultipartBody.Part bodyGasket = null;
        if (!mPromotion.gasketImage.isEmpty()) {
            try {
                String image5Name = mPromotion.gasketImage.substring(mPromotion.gasketImage.lastIndexOf("/") + 1);
                bodyGasket = UtilApp.prepareFilePart("gaskets_img", image5Name);
            } catch (Exception e) {

            }
        }


        RequestBody brandAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.brandingId);
        MultipartBody.Part bodybrand = null;
        if (!mPromotion.brandingImage.isEmpty()) {
            try {
                String image5Name = mPromotion.brandingImage.substring(mPromotion.brandingImage.lastIndexOf("/") + 1);
                bodybrand = UtilApp.prepareFilePart("branding_no_img", image5Name);
            } catch (Exception e) {

            }
        }

        RequestBody lightAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.lightId);
        MultipartBody.Part bodyLight = null;
        if (!mPromotion.lightImage.isEmpty()) {
            try {
                String image5Name = mPromotion.lightImage.substring(mPromotion.lightImage.lastIndexOf("/") + 1);
                bodyLight = UtilApp.prepareFilePart("light_working_img", image5Name);
            } catch (Exception e) {

            }
        }

        RequestBody ventiAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.ventilationId);
        MultipartBody.Part bodyVenty = null;
        if (!mPromotion.ventilationImage.isEmpty()) {
            try {
                String image5Name = mPromotion.ventilationImage.substring(mPromotion.ventilationImage.lastIndexOf("/") + 1);
                bodyVenty = UtilApp.prepareFilePart("propper_ventilation_available_img", image5Name);
            } catch (Exception e) {

            }
        }


        RequestBody levelingAny = RequestBody.create(MediaType.parse("text/plain"), mPromotion.levelingId);
        MultipartBody.Part bodyLeveling = null;
        if (!mPromotion.levelingImage.isEmpty()) {
            try {
                String image5Name = mPromotion.levelingImage.substring(mPromotion.levelingImage.lastIndexOf("/") + 1);
                bodyLeveling = UtilApp.prepareFilePart("leveling_positioning_img", image5Name);
            } catch (Exception e) {

            }
        }

        MultipartBody.Part bodyCoolerImage1 = null;
        if (!mPromotion.coolerImage1.isEmpty()) {
            try {
                String image5Name = mPromotion.coolerImage1.substring(mPromotion.coolerImage1.lastIndexOf("/") + 1);
                bodyCoolerImage1 = UtilApp.prepareFilePart("cooler_image", image5Name);
            } catch (Exception e) {

            }
        }

        MultipartBody.Part bodyCoolerImage2 = null;
        if (!mPromotion.coolerImage2.isEmpty()) {
            try {
                String image5Name = mPromotion.coolerImage2.substring(mPromotion.coolerImage2.lastIndexOf("/") + 1);
                bodyCoolerImage2 = UtilApp.prepareFilePart("cooler_image2", image5Name);
            } catch (Exception e) {

            }
        }


        MultipartBody.Part bodyCustSign = null;
        try {
            if (!mPromotion.customerSignature.isEmpty()) {
                try {
                    File file_bh = new File(mPromotion.customerSignature);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
                    bodyCustSign = MultipartBody.Part.createFormData("customer_signature", file_bh.getName(), reqFile);
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }

        MultipartBody.Part typeDteail1 = null;
        MultipartBody.Part typeDteail2 = null;
        if (!mPromotion.disputeImage1.isEmpty()) {
            String image6Name = mPromotion.disputeImage1.substring(mPromotion.disputeImage1.lastIndexOf("/") + 1);
            try {
                typeDteail1 = UtilApp.prepareFilePart("type_details_photo1", image6Name);
            } catch (Exception e) {

            }
        }

        if (!mPromotion.disputeImage2.isEmpty()) {
            String image6Name = mPromotion.disputeImage2.substring(mPromotion.disputeImage2.lastIndexOf("/") + 1);
            try {
                typeDteail2 = UtilApp.prepareFilePart("type_details_photo2", image6Name);
            } catch (Exception e) {

            }
        }

        RequestBody complainType = RequestBody.create(MediaType.parse("text/plain"), mPromotion.workDoneType);
        RequestBody mComment = RequestBody.create(MediaType.parse("text/plain"), mPromotion.workDoneComment);
        RequestBody anyDispute = RequestBody.create(MediaType.parse("text/plain"), mPromotion.anyDispute);
        RequestBody mWorkStatus = RequestBody.create(MediaType.parse("text/plain"), mPromotion.workstatus);
        RequestBody mPendingReason = RequestBody.create(MediaType.parse("text/plain"), mPromotion.pendingReason);
        RequestBody mPendingSpare = RequestBody.create(MediaType.parse("text/plain"), mPromotion.pendingSpare);

        RequestBody mSpareDetail = RequestBody.create(MediaType.parse("text/plain"), mPromotion.workSpare);
        RequestBody mCurrentVolt = RequestBody.create(MediaType.parse("text/plain"), mPromotion.currentVolt);
        RequestBody mAMPS = RequestBody.create(MediaType.parse("text/plain"), mPromotion.amps);
        RequestBody mTemprature = RequestBody.create(MediaType.parse("text/plain"), mPromotion.temprature);
        String natureId = "";
        if (mPromotion.natureOfCall != null) {
            natureId = mPromotion.natureOfCall;
        }
        RequestBody mNatureId = RequestBody.create(MediaType.parse("text/plain"), natureId);
        RequestBody mCTCComment = RequestBody.create(MediaType.parse("text/plain"), mPromotion.ctcComment);
        RequestBody mOtherComment = RequestBody.create(MediaType.parse("text/plain"), mPromotion.otherSpecific);
//
        final Call<JsonObject> labelResponse = ApiClient.getService().postServiceVisitData(method, type, tickeNo, timeIn, timeOutM, modelName,
                assetNo, serialNo, brandingM, moutletName, mOwnerName, mlandMark, mRoadStreet, mTown, mContact, mlongitude, mlatitude, techniquse, isMachine_work,
                cleanlesAny, coilAny, gasKetAny, lightAny, brandAny, ventiAny, levelingAny, stocKAny, complainType, mComment, anyDispute,
                mContact2, mContactPerson, mTechRate, mQalityRate, mCurrentVolt, mAMPS, mTemprature, mWorkStatus, mPendingReason, mPendingSpare, mSpareDetail, mNatureId, mCTSStatus,
                mDistrict, mCTCComment, mOtherComment, bodyMAchine, bodyCleanless, bodyCoil, bodyGasket, bodybrand, bodyLight, bodyVenty, bodyLeveling, bodyStock, bodyCustSign, typeDteail1, typeDteail2, bodyScan,
                bodyCoolerImage1, bodyCoolerImage2);


        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Freeze Response:", response.toString());
                //  UtilApp.logData(SyncData.this, "Compitior Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                App.isServiceVisitPostSync = false;
                                db.updateServiceVisitPosted(mPromotion.ticketNo, "1");
                                db.updateOrderTransaction(mPromotion.ticketNo, "1", "");
                                Log.e("Chiller Success", jsonObject.getString("STATUS"));
                            } else {
                                // Fail to Post
                                App.isServiceVisitPostSync = false;
                                db.updateOrderTransaction(mPromotion.ticketNo, "2", response.body().get("MESSAGE").getAsString());
                                db.updateServiceVisitPosted(mPromotion.ticketNo, "2");
                            }
                        } else {
                            // Fail to Post
                            App.isServiceVisitPostSync = false;
                            db.updateOrderTransaction(mPromotion.ticketNo, "2", "API fail");
                            db.updateServiceVisitPosted(mPromotion.ticketNo, "2");
                        }
                    } catch (Exception e) {
                        App.isServiceVisitPostSync = false;
                        db.updateOrderTransaction(mPromotion.ticketNo, "2", "API fail");
                        db.updateServiceVisitPosted(mPromotion.ticketNo, "2");
                    }
                }

                App.isServiceVisitPostSync = false;
                sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Service fail:", t.toString());
                App.isServiceVisitPostSync = false;
                UtilApp.logData(SyncData.this, "Service Fail: " + t.getMessage());
                db.updateOrderTransaction(mPromotion.ticketNo, "2", "API fail");
                db.updateServiceVisitPosted(mPromotion.ticketNo, "2");

                sendBroadcast(intent);

            }
        });
    }

    //Call Promotional API
    private void callCHillerTrackAPI() {

        Freeze mPromotion = db.getChilerTrackPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.CHILLER_TRACKING);
        RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getRoute_id());
        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getCustomer_id());
        RequestBody have_fridge = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getHave_fridge());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLatitude());
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLongitude());
        RequestBody comments = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComments());
        RequestBody Complaint_type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getComplaint_type());
        RequestBody serial_no = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSerial_no());

        List<MultipartBody.Part> parts = new ArrayList<>();

//        // add dynamic amount
        try {
            if (!mPromotion.getImage1().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image1", mPromotion.getImage1()));
            }

            if (!mPromotion.getImage2().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image2", mPromotion.getImage2()));
            }

            if (!mPromotion.getImage3().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image3", mPromotion.getImage3()));
            }

            if (!mPromotion.getImage4().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("image4", mPromotion.getImage4()));
            }
        } catch (Exception e) {
            e.toString();
        }
        try {
            if (!mPromotion.getFridge_scan_img().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getFridge_scan_img()));
            }
        } catch (Exception e) {
            e.toString();
        }

        UtilApp.logData(this, "Compatitor Feedback Request: " + mPromotion.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postFridgeData(method, route_id, salesman_id,
                customer_id, have_fridge, latitude, longitude, comments, Complaint_type, serial_no, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Chiller Response:", response.toString());
                UtilApp.logData(SyncData.this, "Chiller Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                App.isChillerTrackSync = false;
                                db.updateChillerTrackPosted(mPromotion.getSalesman_id(), "1");
                                db.updateOrderTransaction(mPromotion.getSalesman_id(), "1", "");
                                Log.e("Chiller Success", jsonObject.getString("STATUS"));
                            } else {
                                // Fail to Post
                                App.isChillerTrackSync = false;
                                db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", response.body().get("MESSAGE").getAsString());
                                db.updateChillerTrackPosted(mPromotion.getSalesman_id(), "2");
                            }
                        } else {
                            // Fail to Post
                            App.isChillerTrackSync = false;
                            db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", "API fail");
                            db.updateChillerTrackPosted(mPromotion.getSalesman_id(), "2");
                        }
                    } catch (Exception e) {
                        App.isChillerTrackSync = false;
                        db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", "API fail");
                        db.updateChillerTrackPosted(mPromotion.getSalesman_id(), "2");
                    }
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Chiller fail:", t.toString());
                App.isChillerTrackSync = false;
                UtilApp.logData(SyncData.this, "Chiller Fail: " + t.getMessage());
                db.updateOrderTransaction(mPromotion.getSalesman_id(), "2", "API fail");
                db.updateChillerTrackPosted(mPromotion.getSalesman_id(), "2");

                sendBroadcast(intent);
            }
        });

    }

    //Call Promotional API
    private void callCHillerAddRequestAPI() {

        Chiller_Model mPromotion = db.getAddChillerRequestPost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.ADD_FRIDGE_POST);
        RequestBody chiller_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getdepot_id());
        RequestBody route_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.ROUTEID));
        RequestBody salesman_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getCustomer_id());
        RequestBody depot_id = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.DEPOTID));
        RequestBody serialNo = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSerialNo());
        RequestBody contact_number = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getContact_number());
        RequestBody cust_address = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getPostal_address());
        RequestBody cust_address2 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLandmark());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLocation());
        RequestBody isMerchandiser;
        if (UtilApp.salesmanType(Settings.getString(App.ROLE)).equalsIgnoreCase("Salesman")) {
            isMerchandiser = RequestBody.create(MediaType.parse("text/plain"), "0");
        } else {
            isMerchandiser = RequestBody.create(MediaType.parse("text/plain"), "1");
        }
        RequestBody outlettype = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_type());
        RequestBody ownername = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOwner_name());
        RequestBody specify_if_other_type = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getSpecify_if_other_type());
        RequestBody existing_cooler = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getExisting_coolers());
        RequestBody stock = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getStock_share_with_competitor());


        //lc_letter,trading_licence,
        RequestBody currentsale = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_weekly_sale_volume());
        RequestBody expectedsale = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_weekly_sales());
        RequestBody chillersize = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getChiller_size_requested());
        RequestBody grill = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getChiller_safty_grill());
        RequestBody display_location = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getDisplay_location());

        RequestBody nation_id1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getNational_id());
        RequestBody password_photo1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getPassword_photo());
        RequestBody outlet_address_proof1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_address_proof());
        RequestBody outlet_stamp1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getOutlet_stamp());
        RequestBody lc_letter1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getLc_letter());
        RequestBody trading_licence1 = RequestBody.create(MediaType.parse("text/plain"), mPromotion.getTrading_licence());

        MultipartBody.Part body1 = null;
        try {
            File file_bh = new File(mPromotion.getSign__customer_file());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
            body1 = MultipartBody.Part.createFormData("sign__customer_file", file_bh.getName(), reqFile);
        } catch (Exception e) {

        }

        MultipartBody.Part body2 = null;
        try {
            File file_bh = new File(mPromotion.getSalesmanSignature());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file_bh);
            body2 = MultipartBody.Part.createFormData("sign_salesman_file", file_bh.getName(), reqFile);
        } catch (Exception e) {

        }

        List<MultipartBody.Part> parts = new ArrayList<>();

//        // add dynamic amount
        try {
            if (!mPromotion.getChillerImage().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("fridge_scan_img", mPromotion.getChillerImage()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getNational_id_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("national_id_file", mPromotion.getNational_id_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getNational_id1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("national_id1_file", mPromotion.getNational_id1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getPassword_photo_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("password_photo_file", mPromotion.getPassword_photo_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getPassword_photo1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("password_photo1_file", mPromotion.getPassword_photo1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getOutlet_address_proof_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_address_proof_file", mPromotion.getOutlet_address_proof_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getOutlet_address_proof1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_address_proof1_file", mPromotion.getOutlet_address_proof1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getTrading_licence_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("trading_licence_file", mPromotion.getTrading_licence_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getTrading_licence1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("trading_licence1_file", mPromotion.getTrading_licence1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getLc_letter_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("lc_letter_file", mPromotion.getLc_letter_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getLc_letter1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("lc_letter1_file", mPromotion.getLc_letter1_file()));
            }
        } catch (Exception e) {
        }

        try {
            if (!mPromotion.getOutlet_stamp_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_stamp_file", mPromotion.getOutlet_stamp_file()));
            }
        } catch (Exception e) {
            e.toString();
        }

        try {
            if (!mPromotion.getOutlet_stamp1_file().equalsIgnoreCase("")) {
                parts.add(UtilApp.prepareFilePart("outlet_stamp1_file", mPromotion.getOutlet_stamp1_file()));
            }
        } catch (Exception e) {
            e.toString();
        }


        UtilApp.logData(this, "Compatitor Feedback Request: " + mPromotion.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postAddChillerData(method, chiller_id, depot_id, salesman_id,
                route_id, customer_id, ownername, serialNo, contact_number, cust_address, cust_address2,
                location, outlettype, specify_if_other_type, existing_cooler, stock, currentsale,
                expectedsale, chillersize, grill, display_location, nation_id1, password_photo1,
                outlet_address_proof1, outlet_stamp1, lc_letter1, trading_licence1, isMerchandiser, parts, body1, body2);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Chiller Response:", response.toString());
                UtilApp.logData(SyncData.this, "Chiller Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                App.isChillerAddRequestSync = false;
                                db.updateAddChillerRequestPosted(mPromotion.getdepot_id(), "1");
                                db.updateOrderTransaction(mPromotion.getdepot_id(), "1", "");
                                Log.e("Chiller Success", jsonObject.getString("STATUS"));
                            } else {
                                // Fail to Post
                                App.isChillerAddRequestSync = false;
                                db.updateOrderTransaction(mPromotion.getdepot_id(), "2", response.body().get("MESSAGE").getAsString());
                                db.updateAddChillerRequestPosted(mPromotion.getdepot_id(), "2");
                            }
                        } else {
                            // Fail to Post
                            App.isChillerAddRequestSync = false;
                            db.updateOrderTransaction(mPromotion.getdepot_id(), "2", "API fail");
                            db.updateAddChillerRequestPosted(mPromotion.getdepot_id(), "2");
                        }
                    } catch (Exception e) {
                        App.isChillerAddRequestSync = false;
                        db.updateOrderTransaction(mPromotion.getdepot_id(), "2", "API fail");
                        db.updateAddChillerRequestPosted(mPromotion.getdepot_id(), "2");
                    }
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Chiller fail:", t.toString());
                App.isChillerAddRequestSync = false;
                UtilApp.logData(SyncData.this, "Chiller Fail: " + t.getMessage());
                db.updateOrderTransaction(mPromotion.getdepot_id(), "2", "API fail");
                db.updateAddChillerRequestPosted(mPromotion.getdepot_id(), "2");

                sendBroadcast(intent);
            }
        });

    }

    //Call Assets API
    private void callAssetsAPI() {


        ASSETS_MODEL mPlanogram = db.getPostAssetsDetail();
        AssetsId = mPlanogram.getAssetsId();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.ASSETS);
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody AssetId = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.getAssetsId());


        List<MultipartBody.Part> parts = new ArrayList<>();

        if (!mPlanogram.getAssetsImage1().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image1", mPlanogram.getAssetsImage1()));
        }

        if (!mPlanogram.getAssetsImage2().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image2", mPlanogram.getAssetsImage2()));
        }

        if (!mPlanogram.getAssetsImage3().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image3", mPlanogram.getAssetsImage3()));
        }

        if (!mPlanogram.getAssetsImage4().equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image4", mPlanogram.getAssetsImage4()));
        }

        UtilApp.logData(SyncData.this, "Assets Request: " + mPlanogram.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postAssetsData(method, salesmanId, AssetId,
                parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Assets Response:", response.toString());
                UtilApp.logData(SyncData.this, "Assets Response: " + response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                    Log.e("Assets Success", jsonObject.getString("STATUS"));
                                    db.updateAssetsPosted(AssetsId);
                                    String assts = "AST" + AssetsId;
                                    db.updateTransaction(assts);
                                    App.isAssetsSync = false;
                                } else {
                                    // Fail to Post
                                    App.isAssetsSync = false;
                                }
                            }
                        }
                    } catch (Exception e) {
                        App.isAssetsSync = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Assets fail:", t.toString());
                UtilApp.logData(SyncData.this, "Assets Fail: " + t.getMessage());
                App.isAssetsSync = false;
            }
        });

    }

    //Call Expiry API
    private void callExpiryItemAPI(JsonObject jObj, String uniqId) {
        UtilApp.logData(SyncData.this, "Expiry Item Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Expiry Response:", response.toString());
                UtilApp.logData(SyncData.this, "Expiry Response: " + response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {

                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateExpiryListPosted(uniqId);
                            db.updateTransaction(uniqId);
                            System.out.println("Demo Item Load Done");
                            App.isExpiryItemSync = false;
                        } else {
                            App.isExpiryItemSync = false;
                            System.out.println("Demo Item Load Done");
                        }
                    }
                } else {
                    App.isExpiryItemSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isExpiryItemSync = false;
                Log.e("Expiry Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Expiry Fail: " + error.getMessage());
            }
        });

    }

    //Call Damaged API
    private void callDamagedItemAPI(JsonObject jObj, String uniqId) {

        UtilApp.logData(SyncData.this, "Damaged Tool Request: " + jObj.toString());

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Damaged Response:", response.toString());
                UtilApp.logData(SyncData.this, "Damaged Response: " + response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {

                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateDamagedListPosted(uniqId);
                            db.updateTransaction(uniqId);
                            System.out.println("Demo Item Load Done");
                            App.isDamagedSync = false;
                        } else {
                            App.isDamagedSync = false;
                            System.out.println("Demo Item Load Done");
                        }
                    }
                } else {
                    App.isDamagedSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isDamagedSync = false;
                Log.e("Damaged Fail", error.getMessage());
                UtilApp.logData(SyncData.this, "Damaged Fail: " + error.getMessage());
            }
        });
    }

    private void callSurveyPostAPI(JsonObject jObj) {

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Survey Response:", response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {

                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateSurveyPosted(jObj);
                            String uniqId = jObj.get("answer_identifer").getAsString();
                            db.updateTransaction(uniqId);
                            App.isSurveyPost = false;
                        } else {
                            App.isSurveyPost = false;
                        }
                    }
                } else {
                    App.isSurveyPost = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isSurveyPost = false;
                Log.e("Survey Fail", error.getMessage());
            }
        });
    }

    private void callAssetsSurveyPostAPI(JsonObject jObj) {

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Assets Response:", response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {

                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateAssetsSurveyPosted(jObj);

                            String uniqId = jObj.get("answer_identifer").getAsString();
                            db.updateTransaction(uniqId);

                            App.isAssetsPost = false;
                        } else {
                            App.isAssetsPost = false;
                        }
                    }
                } else {
                    App.isAssetsPost = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isAssetsPost = false;
                Log.e("Assets Fail", error.getMessage());
            }
        });

    }

    private void callSensoryPostAPI(JsonObject jObj) {

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Sensory Response:", response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {

                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateSensoryPosted(jObj);
                            String uniqId = jObj.get("answer_identifer").getAsString();
                            db.updateTransaction(uniqId);
                            App.isSensoryPost = false;
                        } else {
                            App.isSensoryPost = false;
                        }
                    }
                } else {
                    App.isSensoryPost = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isSensoryPost = false;
                Log.e("Sensory Fail", error.getMessage());
            }
        });
    }

    private void callConsumerSurveyPostAPI(JsonObject jObj) {

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Consumer Response:", response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {

                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            db.updateConsumerSurveyPosted(jObj);

                            String uniqId = jObj.get("answer_identifer").getAsString();
                            db.updateTransaction(uniqId);
                            App.isConsumerPost = false;
                        } else {
                            App.isConsumerPost = false;
                        }
                    }
                } else {
                    App.isConsumerPost = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isConsumerPost = false;
                Log.e("Consumer Fail", error.getMessage());
            }
        });

    }

    //Post Inventory Expiry
    private void callPostInventoryAPI(JsonObject jObj, String invNo) {

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Inventory Response:", response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {
                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            Log.e("Inventory Success", response.body().get("STATUS").getAsString());
                            db.updateInventoryPosted(jObj, invNo);
                            db.updateTransaction(invNo);
                            App.isInventoryPost = false;
                        } else {
                            // Fail to Post
                            App.isInventoryPost = false;
                        }
                    }
                } else {
                    App.isInventoryPost = false;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                App.isInventoryPost = false;
                Log.e("Inventory fail:", t.toString());
            }
        });
    }

    //Call DistriImage API
    private void callDistrIMageAPI() {


        HashMap<String, String> mPlanogram = db.getDistImagePost();

        RequestBody method = RequestBody.create(MediaType.parse("text/plain"), App.DISTRIBUTION_IMAGE);
        RequestBody salesmanId = RequestBody.create(MediaType.parse("text/plain"), Settings.getString(App.SALESMANID));
        RequestBody CampaignID = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.get("tool_id"));
        RequestBody CustomerID = RequestBody.create(MediaType.parse("text/plain"), mPlanogram.get("customerId"));
        List<MultipartBody.Part> parts = new ArrayList<>();

        // add dynamic amount
        if (!mPlanogram.get("image1").equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image1", mPlanogram.get("image1")));
        }

        if (!mPlanogram.get("image2").equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image2", mPlanogram.get("image2")));
        }

        if (!mPlanogram.get("image3").equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image3", mPlanogram.get("image3")));
        }

        if (!mPlanogram.get("image4").equalsIgnoreCase("")) {
            parts.add(UtilApp.prepareFilePart("image4", mPlanogram.get("image4")));
        }

        final Call<JsonObject> labelResponse = ApiClient.getService().postDistImageData(method, salesmanId, CampaignID,
                CustomerID, parts);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Dist Image Response:", response.toString());
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.has("STATUS")) {
                            if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                if (jsonObject.getString("STATUS").equalsIgnoreCase("1")) {
                                    db.updateDistrImagePosted(mPlanogram.get("customerId"), mPlanogram.get("tool_id"));
                                    Log.e("Dist Image Success", jsonObject.getString("STATUS"));
                                    App.isDisImageSync = false;
                                } else {
                                    // Fail to Post
                                    App.isDisImageSync = false;
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Dist Image fail:", t.toString());
                App.isDisImageSync = false;
            }
        });

    }

    private void callDistributionPostAPI(JsonObject jObj) {

        final Call<JsonObject> labelResponse = ApiClient.getService().postMerchandising(jObj);

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Distri Stock Response:", response.toString());
                if (response.body() != null) {
                    if (response.body().has("STATUS")) {

                        if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                            App.isStockSync = false;
                            db.updateDistriStockPosted(jObj);
                        } else {
                            App.isStockSync = false;
                        }
                    }
                } else {
                    App.isStockSync = false;
                }

                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                App.isStockSync = false;
                Log.e("Distri Stock Fail", error.getMessage());
            }
        });
    }

}
