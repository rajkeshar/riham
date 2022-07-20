package com.mobiato.sfa.firebase;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.NotificationActivity;
import com.mobiato.sfa.activity.SplashActivity;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Delivery;
import com.mobiato.sfa.model.Notification;
import com.mobiato.sfa.model.Return;
import com.mobiato.sfa.rest.ApiClient;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.UtilApp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //    public static final String BROADCAST_ACTION = "com.ae.fayha.FCM";
    public static final String BROADCAST_ACTION = "com.riham.FCM";
    private static final String TAG = "MyFirebaseMsgService";
    Intent intentBr;
    JSONObject object;
    private DBManager db;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Displaying data in log
        // It is optional
        this.db = new DBManager(this);
        this.intentBr = new Intent(BROADCAST_ACTION);
        try {
            object = new JSONObject(remoteMessage.getData());
            Log.v(TAG, "Push notification: " + object.toString());
            sendNotification(object);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(JSONObject object) {

        Gson gson = new Gson();
        String json = object.toString();
        Notification mNotification = gson.fromJson(json, Notification.class);
        App.countNoti++;

        Intent intent = new Intent();
        if (isAppIsInBackground(this)) {
            intent = new Intent(this, SplashActivity.class);
        } else {
            intent = new Intent(this, NotificationActivity.class);
        }
System.out.println("P->"+mNotification.getType());
        String title = "";
        if (mNotification.getType().equalsIgnoreCase("return")) {
            title = mNotification.getTitle();
            //Call Return API
            getReturnList();
        } else if (mNotification.getType().equalsIgnoreCase("delivery")) {
            title = mNotification.getTitle();
            //Call Return API
            getDeliveryList();
        } else {
            title = mNotification.getOrderId() + " " + mNotification.getTitle();
        }

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "com.mobiato.sfa")
                .setContentTitle(title)
                .setContentText(mNotification.getMessage())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(App.COUNT, notificationBuilder.build());
        App.COUNT++;

        //Insert into DataBase
        db.insertPushNotification(mNotification);

    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    private void getReturnList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryList(App.RETURN_LIST, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Return List Response", response.body().toString());
                UtilApp.logData(MyFirebaseMessagingService.this, "Return List Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Return> arrReturn = new ArrayList<>();
                        arrReturn = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Return>>() {
                                }.getType());
                        db.insertReturnListItems(arrReturn);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Return List Fail", error.getMessage());
                UtilApp.logData(MyFirebaseMessagingService.this, "Return List Fail: " + error.getMessage());
            }
        });
    }

    private void getDeliveryList() {

        final Call<JsonObject> labelResponse = ApiClient.getService().getDeliveryList(App.GET_DELIVERY, Settings.getString(App.SALESMANID));

        labelResponse.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Log.e("Delivery Response", response.body().toString());
                UtilApp.logData(MyFirebaseMessagingService.this, "Delivery Response: " + response.body().toString());
                if (response.body() != null) {
                    if (response.body().get("STATUS").getAsString().equalsIgnoreCase("1")) {
                        ArrayList<Delivery> arrDelivery = new ArrayList<>();
                        arrDelivery = new Gson().fromJson(response.body().get("RESULT").getAsJsonArray().toString(),
                                new TypeToken<List<Delivery>>() {
                                }.getType());
                        db.insertDeliveryItems(arrDelivery);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable error) {
                Log.e("Delivery Fail", error.getMessage());
                UtilApp.logData(MyFirebaseMessagingService.this, "Delivery Fail: " + error.getMessage());
            }
        });
    }

}