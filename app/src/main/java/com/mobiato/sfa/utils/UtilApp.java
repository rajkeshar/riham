package com.mobiato.sfa.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobiato.sfa.App;
import com.mobiato.sfa.R;
import com.mobiato.sfa.activity.OrderRequestActivity;
import com.mobiato.sfa.interfaces.OnSearchableDialog;
import com.mobiato.sfa.localdb.DBManager;
import com.mobiato.sfa.model.Item;
import com.mobiato.sfa.model.Salesman;
import com.mobiato.sfa.rest.BackgroundJobLower;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UtilApp {

    public static DecimalFormat df = new DecimalFormat("###.##");
    private static int kJobId = 0;
    private DBManager db;

    //Connectivity Messages
    public static final String WIFI_CONNECTED = "Wifi Enabled";
    public static final String MOBILE_DATA_CONNECTED = "Mobile Data Enabled";
    public static final String NO_CONNECTION = "Not connected to Internet";

    private static String[] suffix = new String[]{"", "K", "M", "B", "T"};
    private static int MAX_LENGTH = 4;

    //Getting lat long
    public static boolean ReadSharePrefrence(Context context, String key) {
        @SuppressWarnings("static-access")
        SharedPreferences read_data = null;
        try {

            read_data = context.getSharedPreferences(
                    Constant.SHRED_PR.SHARE_PREF, context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return read_data.getBoolean(key, false);
    }

    public static String formatDate(Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String formatTime(Date date, String format) {
        if (date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String getLanguageDirection(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("languageDirection", "ltr");
    }

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity me) {
        if (me.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) me.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(me.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String getString(InputStream in) {
        InputStreamReader is = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        try {
            String read = br.readLine();
            while (read != null) {
                sb.append(read);
                read = br.readLine();
            }
        } catch (Exception e) {
            Log.e("", "ERROR WHILE PARSING RESPONSE TO STRING :: " + e.getMessage());
        } finally {
            try {
                if (is != null) is.close();
                if (br != null) br.close();
            } catch (Exception e) {
            }
        }
        return sb.toString();
    }

    public static void WriteSharePrefrence(Context context, String key, boolean values) {
        @SuppressWarnings("static-access")
        SharedPreferences write_Data = context.getSharedPreferences(
                Constant.SHRED_PR.SHARE_PREF, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = write_Data.edit();
        editor.putBoolean(key, values);
        editor.commit();
        editor.apply();
    }


    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();

        // System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getTCDueDate(String paymentTerm) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, Integer.parseInt(paymentTerm));
        Date dd = c.getTime();
        //System.out.println("Current time => " + dd);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(dd);
        return formattedDate;
    }

    public static String getYesterdayDate() {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday
        Date c = c1.getTime();
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrentDateBG() {
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static Date getDateTimeStamp(String strDate) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getCurrentTime() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrent12Time() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrentTimeVisit() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrent24TimeVisit() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getStringDate(String strDate) {
        String dateTime = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (DateUtils.isToday(date.getTime())) {
            dateTime = "Today";
        } else if (isYesterday(date)) {
            dateTime = "Yesterday";
        } else {
            dateTime = strDate;
        }
        return dateTime;

    }

    public static boolean isYesterday(Date d) {
        return DateUtils.isToday(d.getTime() + DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void displayErrorDialog(Context mContext, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Error")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public static void displayAlert(Context mContext, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Alert")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public static void confirmationDialog(String message, Context mContext, final OnSearchableDialog onSearchableDialog) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onSearchableDialog != null)
                            onSearchableDialog.onItemSelected("yes");
                        dialog.dismiss();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onSearchableDialog != null)
                            onSearchableDialog.onItemSelected("no");
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public static void shopDialog(Context mContext, final OnSearchableDialog onSearchableDialog) {
        final Dialog alertDialog = new Dialog(mContext);
        alertDialog.setCancelable(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_shop_open_close);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.findViewById(R.id.rl_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null)
                    onSearchableDialog.onItemSelected("open");
                //your business logic
                alertDialog.dismiss();


            }
        });

        alertDialog.findViewById(R.id.rl_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null)
                    onSearchableDialog.onItemSelected("close");
                //your business logic
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    public static void dialogPrint(Context mContext, final OnSearchableDialog onSearchableDialog) {
        final Dialog alertDialog = new Dialog(mContext);
        alertDialog.setCancelable(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_print_donot_print);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView img_print = alertDialog.findViewById(R.id.img_pring);

        img_print.setColorFilter(ContextCompat.getColor(mContext, R.color.theme_color), android.graphics.PorterDuff.Mode.MULTIPLY);


        alertDialog.findViewById(R.id.rl_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null)
                    onSearchableDialog.onItemSelected("yes");
                alertDialog.dismiss();
            }
        });

        alertDialog.findViewById(R.id.rl_donot_print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                if (onSearchableDialog != null)
                    onSearchableDialog.onItemSelected("no");
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    public static String decodeString(String data) {
        if (data == null) return "";
        try {
            data = URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static double getVat(double price) {
        double vatVal = (price * 18) / 118;
        return DecimalUtils.round(vatVal, 2);
    }


    public static double getVat_Hamper(double price) {
        double vatVal = (price * 18) / (100 + 18);
        return DecimalUtils.round(vatVal, 2);
    }

    public static double getExciseFirstMethod(double uomVolume, String itemCat) {
        double amt = 0;
        amt = uomVolume * getExciseLitterValue(itemCat);
        return DecimalUtils.round(amt, 2);
    }

    public static double getExciseSecondMethod(double price, String itemCat) {
        double amt = 0;
        int percent = getExcisePercent(itemCat);
        double value = getExciseValueCat(itemCat);
        amt = ((price / value) * percent) / 100;
        return DecimalUtils.round(amt, 2);
    }

    public static double getExciseValueCat(String itemCat) {
        double value = 0;
        switch (Integer.parseInt(itemCat)) {
            case 2:
                value = 1.3216;
                break;
            case 3:
                value = 1.298;
                break;
            case 4:
                value = 1.3216;
                //value = 1.334;
                break;
            default:
                value = 1.3216;
                break;
        }
        return value;
    }

    private static int getExcisePercent(String itemCat) {
        int percent;
        System.out.println("p00-->" + itemCat);
        switch (Integer.parseInt(itemCat)) {
            case 2:
                percent = 12;
                break;
            case 3:
                percent = 10;
                break;
            case 4:
                percent = 12;
                break;
            default:
                percent = 12;
                break;
        }

        return percent;
    }

    private static int getExciseLitterValue(String itemCat) {
        int percent;
        switch (Integer.parseInt(itemCat)) {
            case 2:
                percent = 250;
                break;
            case 4:
                percent = 250;
                break;
            default:
                percent = 250;
                break;
        }

        return percent;
    }

    private boolean isBaseQty(Item mItem) {
        boolean isBase = false;

        if (Integer.parseInt(mItem.getBaseUOMQty()) > 0)
            return true;
        return isBase;
    }

    public static void createBackgroundJob(Context context) {

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ComponentName mServiceComponent = new ComponentName(context, BackgroundJob.class);
//            if (kJobId == 60) {
//                kJobId = 1;
//            }
//            JobInfo.Builder builder = null;
//            builder = new JobInfo.Builder(kJobId++, mServiceComponent);
//            builder.setMinimumLatency(1 * 1000); // wait at least
//            builder.setOverrideDeadline(6 * 1000); // maximum delay
//            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//            builder.setRequiresDeviceIdle(false); // device should be idle
//            builder.setRequiresCharging(false); // we don't care if the device is charging or not
//            JobScheduler jobScheduler = (JobScheduler) context.getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            if (builder != null)
//                jobScheduler.schedule(builder.build());
//        } else {
        long time = 1 * 1000;
        Intent intentAlarm = new Intent(context, BackgroundJobLower.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        // }

    }

    public static String getReasonName(String name) {
        String code;

        switch (name) {
            case "1":
                code = "ROT";
                break;
            case "2":
                code = "Truck Damage";
                break;
            case "3":
                code = "Van Expiry";
                break;
            case "4":
                code = "Theft/Variance";
                break;
            case "5":
                code = "Bad Return Variance";
                break;
            case "0":
                code = "End Inventory";
                break;
            default:
                code = "End Inventory";
                break;
        }
        return code;

    }

    public static String getCurrentDay() {
        String crDay = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                crDay = DBManager.SUN_SEQ;
                break;
            case Calendar.MONDAY:
                crDay = DBManager.MON_SEQ;
                break;
            case Calendar.TUESDAY:
                crDay = DBManager.TUE_SEQ;
                break;
            case Calendar.WEDNESDAY:
                crDay = DBManager.WED_SEQ;
                break;
            case Calendar.THURSDAY:
                crDay = DBManager.THU_SEQ;
                break;
            case Calendar.FRIDAY:
                crDay = DBManager.FRI_SEQ;
                break;
            case Calendar.SATURDAY:
                crDay = DBManager.SAT_SEQ;
                break;
        }

        return crDay;
    }

    public static String getLastIndex(String type) {
        String lastNo = "", padLenth, strPadd;
        int val = 0;
        String[] str;
        switch (type) {
            case "Invoice":
                str = Settings.getString(App.INVOICE_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastInvNo = Settings.getString(App.INVOICE_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastInvNo);
                val += 1;
                padLenth = "%0" + lastInvNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                break;
            case "Order":
                str = Settings.getString(App.ORDER_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastOrderNo = Settings.getString(App.ORDER_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastOrderNo);
                val += 1;
                padLenth = "%0" + lastOrderNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                break;
            case "Return":
                str = Settings.getString(App.RETURN_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastReturnNo = Settings.getString(App.RETURN_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastReturnNo);
                val += 1;
                padLenth = "%0" + lastReturnNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                break;
            case "Collection":
                str = Settings.getString(App.COLLECTION_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastCollNo = Settings.getString(App.COLLECTION_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastCollNo);
                val += 1;
                padLenth = "%0" + lastCollNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                break;
            case "Load":
                str = Settings.getString(App.LOAD_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastLoadNo = Settings.getString(App.LOAD_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastLoadNo);
                val += 1;
                padLenth = "%0" + lastLoadNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                break;
            case "UnLoad":
                str = Settings.getString(App.UNLOAD_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastUnLoadNo = Settings.getString(App.UNLOAD_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastUnLoadNo);
                System.out.println("Lst1-->" + val);
                val += 1;
                padLenth = "%0" + lastUnLoadNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                System.out.println("Lst-->" + lastNo);
                break;
            case "Customer":
                str = Settings.getString(App.CUSTOMER_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastCustomerNo = Settings.getString(App.CUSTOMER_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastCustomerNo);
                val += 1;
                padLenth = "%0" + lastCustomerNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                break;
            case "Exchange":
                str = Settings.getString(App.EXCHANGE_LAST).split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String lastExchangeNo = Settings.getString(App.EXCHANGE_LAST).replaceAll("[^0-9.]", "");
                val = Integer.parseInt(lastExchangeNo);
                val += 1;
                padLenth = "%0" + lastExchangeNo.length() + "d";
                strPadd = String.format(padLenth, val);
                lastNo = str[0] + strPadd;
                break;
        }
        return lastNo;
    }

    public static String orderType(String role) {
        String type = "";
        switch (role) {
            case "2":
                type = "3";
                break;
            case "3":
                type = "3";
                break;
            case "4":
                type = "1";
                break;
            default:
                break;
        }
        return type;
    }

    public static String getTimeStamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    public static String getCollectionUnique() {
        return "COL" + getTimeStamp();
    }

    public static String salesmanRole(String role) {
        String type = "";
        switch (role) {
            case "2":
                type = "Hariss salesman";
                break;
            case "3":
                type = "Depot Salesman";
                break;
            case "4":
                type = "Merchandiser";
                break;
            case "5":
                type = "Fridge Supervisor";
                break;
            case "6":
                type = "School Team";
                break;
            case "7":
                type = "School Team";
                break;
            case "8":
                type = "School Team";
                break;
            case "9":
                type = "School Team";
                break;
            case "10":
                type = "School Team";
                break;
            default:
                break;
        }
        return type;
    }

    public static String salesmanType(String role) {
        String type = "";
        switch (role) {
            case "2":
                type = "Salesman";
                break;
            case "3":
                type = "Salesman";
                break;
            case "4":
                type = "Merchandising";
                break;
            case "5":
                type = "Fridge Supervisor";
                break;
            case "6":
                type = "Salesman";
                break;
            case "7":
                type = "Salesman";
                break;
            case "8":
                type = "Salesman";
                break;
            case "9":
                type = "Salesman";
                break;
            case "10":
                type = "Salesman";
                break;
            default:
                break;
        }
        return type;
    }


    public static void logData(Context context, String data) {
        Logger logger = new Logger();
        if (context instanceof Activity) {
            logger.appendLog((Activity) context, data);
        } else {
            logger.appendLog(context, data);
        }
    }

    //Riddhi Vat Change Category
    public static double vatAmount(Item mItem, String itemCat) {
        double vatAmt = 0;

        if (Integer.parseInt(mItem.getSaleBaseQty()) > 0 && Integer.parseInt(mItem.getSaleAltQty()) > 0) {
            double basVat = getVat(Double.parseDouble(mItem.getSaleBasePrice()));
            double altVat = getVat(Double.parseDouble(mItem.getSaleAltPrice()));

            vatAmt = basVat + altVat;
        } else {
            if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
                vatAmt = getVat(Double.parseDouble(mItem.getSaleBasePrice()));
            } else {
                if (itemCat.equals("5")) {
                    vatAmt = getVat_Hamper(Double.parseDouble(mItem.getSaleAltPrice()));
                } else {
                    vatAmt = getVat(Double.parseDouble(mItem.getSaleAltPrice()));
                }
            }
        }

        return vatAmt;
    }

    //Riddhi Vat Change Category
    public static double vatUOMAmount(String mPrice, String itemCat) {
        double vatAmt = 0;

        if (itemCat.equals("5")) {
            vatAmt = getVat_Hamper(Double.parseDouble(mPrice));
        } else {
            vatAmt = getVat(Double.parseDouble(mPrice));
        }

        return vatAmt;
    }

    public static double getPreVatAmount(Item mItem, String itemCat) {
        double preVatAmt = 0;

        if (Integer.parseInt(mItem.getSaleBaseQty()) > 0 && Integer.parseInt(mItem.getSaleAltQty()) > 0) {
            double basVat = getVat(Double.parseDouble(mItem.getSaleBasePrice()));
            double altVat = getVat(Double.parseDouble(mItem.getSaleAltPrice()));

            preVatAmt = (Double.parseDouble(mItem.getSaleBasePrice()) - basVat) + (Double.parseDouble(mItem.getSaleAltPrice()) - altVat);
        } else {
            if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
                double vatAmt = getVat(Double.parseDouble(mItem.getSaleBasePrice()));
                preVatAmt = Double.parseDouble(mItem.getSaleBasePrice()) - vatAmt;
            } else {
                if (itemCat.equals("5")) {
                    double vatAmt = getVat_Hamper(Double.parseDouble(mItem.getSaleAltPrice()));
                    preVatAmt = Double.parseDouble(mItem.getSaleAltPrice()) - vatAmt;
                } else {
                    double vatAmt = getVat(Double.parseDouble(mItem.getSaleAltPrice()));
                    preVatAmt = Double.parseDouble(mItem.getSaleAltPrice()) - vatAmt;
                }
            }
        }

        return preVatAmt;
    }

  /*  public static double getExciseValue(Item mItem, String cat, String basVolume, String alterVolume) {
        double exciseAmt = 0;
//riddhi
        if (cat.equalsIgnoreCase("3")) {
            if (Integer.parseInt(mItem.getSaleBaseQty()) > 0 && Integer.parseInt(mItem.getSaleAltQty()) > 0) {

                double BaseExcise = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);
                double altExcise = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                exciseAmt = BaseExcise + altExcise;
            } else {
                if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
                    exciseAmt = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);
                } else {
                    exciseAmt = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);
                }
            }

        } else {
            if (Integer.parseInt(mItem.getSaleBaseQty()) > 0 && Integer.parseInt(mItem.getSaleAltQty()) > 0) {

                double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(basVolume), cat);
                double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);

                double baseExciseAmt = 0;
                if (exciseFirst > exciseSecond) {
                    baseExciseAmt = exciseFirst;
                } else {
                    baseExciseAmt = exciseSecond;
                }

                double exciseAlFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(alterVolume), cat);
                double exciseAlSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                double alExciseAmt = 0;
                if (exciseAlFirst > exciseAlSecond) {
                    alExciseAmt = exciseAlFirst;
                } else {
                    alExciseAmt = exciseAlSecond;
                }

                exciseAmt = baseExciseAmt + alExciseAmt;

            } else {
                if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
                    double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(basVolume), cat);
                    double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);

                    if (exciseFirst > exciseSecond) {
                        exciseAmt = exciseFirst;
                    } else {
                        exciseAmt = exciseSecond;
                    }
                } else {
                    if (cat.equals("5")) {
                        String mSalesman = UtilApp.salesmanRole(Settings.getString(App.ROLE));
                        //System.out.println("po-->"+mSalesman);
                        if (mSalesman.equals("Hariss salesman")) {
                            // double in = ((Double.parseDouble(mItem.getPreVatAmt()) * Double.parseDouble(mItem.getAgentExcise()) / (100*(Double.parseDouble(mItem.getAgentExcise())))));
                            double in = (((Double.parseDouble(mItem.getPreVatAmt()) * (Double.parseDouble(mItem.getAgentExcise()))) / ((100 + (Double.parseDouble(mItem.getAgentExcise()))))));
                            exciseAmt = in;
                        } else if (mSalesman.equals("Merchandiser")) {
                            double in = (((Double.parseDouble(mItem.getPreVatAmt()) * (Double.parseDouble(mItem.getDirectsellexcise()))) / ((100 + (Double.parseDouble(mItem.getDirectsellexcise()))))));
                            exciseAmt = in;
                        } else {
                            double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(alterVolume), cat);
                            double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                            if (exciseFirst > exciseSecond) {
                                exciseAmt = exciseFirst;
                            } else {
                                exciseAmt = exciseSecond;
                            }
                        }
                    } else {
                        double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(alterVolume), cat);
                        double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                        if (exciseFirst > exciseSecond) {
                            exciseAmt = exciseFirst;
                        } else {
                            exciseAmt = exciseSecond;
                        }
                    }
                }
            }
        }
        return exciseAmt;
    }*/

    public static double getExciseValue(Item mItem, String cat, String basVolume, String alterVolume) {
        double exciseAmt = 0;
//riddhi
        if (cat.equalsIgnoreCase("3")) {
            if (Integer.parseInt(mItem.getSaleBaseQty()) > 0 && Integer.parseInt(mItem.getSaleAltQty()) > 0) {

                double BaseExcise = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);
                double altExcise = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                exciseAmt = BaseExcise + altExcise;
            } else {
                if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
                    exciseAmt = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);
                } else {
                    exciseAmt = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);
                }
            }

        } else {
            if (Integer.parseInt(mItem.getSaleBaseQty()) > 0 && Integer.parseInt(mItem.getSaleAltQty()) > 0) {

                double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(basVolume), cat);
                double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);

                double baseExciseAmt = 0;
                if (exciseFirst > exciseSecond) {
                    baseExciseAmt = exciseFirst;
                } else {
                    baseExciseAmt = exciseSecond;
                }

                double exciseAlFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(alterVolume), cat);
                double exciseAlSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                double alExciseAmt = 0;
                if (exciseAlFirst > exciseAlSecond) {
                    alExciseAmt = exciseAlFirst;
                } else {
                    alExciseAmt = exciseAlSecond;
                }

                exciseAmt = baseExciseAmt + alExciseAmt;

            } else {
                if (Integer.parseInt(mItem.getSaleBaseQty()) > 0) {
                    double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(basVolume), cat);
                    double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleBasePrice()), cat);

                    if (exciseFirst > exciseSecond) {
                        exciseAmt = exciseFirst;
                    } else {
                        exciseAmt = exciseSecond;
                    }
                } else {
                    if (cat.equals("6")) {
                        String mSalesman = UtilApp.salesmanRole(Settings.getString(App.ROLE));
                        if (mSalesman.equals("Hariss salesman")) {
                            exciseAmt = 0;
                        }
                    } else if (cat.equals("5")) {
                        String mSalesman = UtilApp.salesmanRole(Settings.getString(App.ROLE));
                        //System.out.println("po-->"+mSalesman);
                        if (mSalesman.equals("Hariss salesman")) {
                            // double in = ((Double.parseDouble(mItem.getPreVatAmt()) * Double.parseDouble(mItem.getAgentExcise()) / (100*(Double.parseDouble(mItem.getAgentExcise())))));
                            double in = (((Double.parseDouble(mItem.getPreVatAmt()) * (Double.parseDouble(mItem.getAgentExcise()))) / ((100 + (Double.parseDouble(mItem.getAgentExcise()))))));
                            exciseAmt = in;
                        } else if (mSalesman.equals("Merchandiser")) {
                            double in = (((Double.parseDouble(mItem.getPreVatAmt()) * (Double.parseDouble(mItem.getDirectsellexcise()))) / ((100 + (Double.parseDouble(mItem.getDirectsellexcise()))))));
                            exciseAmt = in;
                        } else {
                            double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(alterVolume), cat);
                            double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                            if (exciseFirst > exciseSecond) {
                                exciseAmt = exciseFirst;
                            } else {
                                exciseAmt = exciseSecond;
                            }
                        }
                    } else {
                        double exciseFirst = UtilApp.getExciseFirstMethod(Double.parseDouble(alterVolume), cat);
                        double exciseSecond = UtilApp.getExciseSecondMethod(Double.parseDouble(mItem.getSaleAltPrice()), cat);

                        if (exciseFirst > exciseSecond) {
                            exciseAmt = exciseFirst;
                        } else {
                            exciseAmt = exciseSecond;
                        }
                    }
                }
            }
        }
        return exciseAmt;
    }

    public static float getRadius(Location currentLocation, Location depotLocation) {

        float distance = currentLocation.distanceTo(depotLocation);

        return distance;
    }

    public static String getNumberFormate(double amt) {
        return NumberFormat.getNumberInstance(Locale.US).format(amt);
    }

    public static String getNumberFormateInt(int amt) {
        return NumberFormat.getNumberInstance(Locale.US).format(amt);
    }

    public static String compressImage(Context mContext, String imageUri) {

        String filePath = getRealPathFromURI(mContext, imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {

        File storageDir = Environment.getExternalStoragePublicDirectory("Riham");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String s = "Riham_" + timeStamp + ".jpg";

        String uriSting = (storageDir.getAbsolutePath() + "/" + s);
        return uriSting;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private static String getRealPathFromURI(Context mContext, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static String getComplaintNo() {
        String complaint = "";
        complaint = "COM" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return complaint;
    }

    public static String getChillerNo() {
        String complaint = "";
        complaint = "CHL" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return complaint;
    }

    public static String getChillerRequestNo() {
        String complaint = "";
        complaint = "CHLR" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return complaint;
    }

    public static String getChilerTrackingNo() {
        String complaint = "";
        complaint = "CHLT" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return complaint;
    }

    public static String getAssetsNo() {
        String Assets = "";
        Assets = "AST" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return Assets;
    }

    public static String getCompititorNo() {
        String compititor = "";
        compititor = "CPT" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return compititor;
    }

    public static String getSalesmanLoadNo() {
        String compititor = "";
        compititor = "LR" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return compititor;
    }

    public static String getCampiganNo() {
        String complaint = "";
        complaint = "CAM" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return complaint;
    }

    public static String getPramotionNo() {
        String compititor = "";
        compititor = "PROM" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return compititor;
    }

    public static String getInventoryNo() {
        String compititor = "";
        compititor = "INV" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return compititor;
    }

    public static String getSurvNo() {
        String compititor = "";
        compititor = "SRV" + Settings.getString(App.SALESMANID) + getTimeStamp();
        return compititor;
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String imageName) {

        File storageDir = Environment.getExternalStoragePublicDirectory("Riham");

        // use the FileUtils to get the actual file by uri
        File file = new File(storageDir.getPath() + "/" + imageName);


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getMimeType(file.getAbsolutePath())),
                        file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePartSign(String partName, String imageName) {

        // use the FileUtils to get the actual file by uri
        File file = new File(imageName);


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getMimeType(file.getAbsolutePath())),
                        file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMBTPE".charAt(exp - 1));
    }

    public static int getSalePercent(double target, double sale) {
        int percent = 0;
        percent = (int) ((sale * 100) / target);
        return percent;
    }

    private static final float BITMAP_SCALE = 0.4f;
    private static final int BLUR_RADIUS = 8;

    public static Bitmap fastblur(Bitmap sentBitmap) {
        float scale = BITMAP_SCALE;
        int radius = BLUR_RADIUS;
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);

    }

    public static Location currentBestLocation;


    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides access to the Fused Location Provider API.
     */
    public static FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    public static SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    public static LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    public static LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    public static LocationCallback mLocationCallback;
    public static boolean canGetLocation = false;

    public static void initialiseLocationVariables(Context activity, final LocationUpdate locationUpdate) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentBestLocation = locationResult.getLastLocation();
                Log.d("Location", "onLocationResult accuracy=" + locationResult.getLastLocation().getAccuracy());
                Log.d("Location", "onLocationResult latitude=" + currentBestLocation.getLatitude());
                Log.d("Location", "onLocationResult longitude=" + currentBestLocation.getLongitude());

                App.Accuracy = currentBestLocation.getAccuracy() + "";
                App.Latitude = currentBestLocation.getLatitude() + "";
                App.Longitude = currentBestLocation.getLongitude() + "";

                canGetLocation = true;
                locationUpdate.onLocationDetect(true);

            }
        };


        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

    }

    public interface LocationUpdate {
        void onLocationDetect(boolean isDetect);
    }

    public static final int REQUEST_CHECK_SETTINGS = 12462;

    public static void startLocationUpdates(final Activity activity) {

        stopLocationUpdates();

        if (activity != null) {
            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            Log.i("Location", "All location settings are satisfied.");
                            //noinspection MissingPermission
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());
                            canGetLocation = true;

                        }
                    }).addOnFailureListener(activity, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    canGetLocation = false;
                                    int statusCode = ((ApiException) e).getStatusCode();
                                    switch (statusCode) {
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            Log.i("Location", "Location settings are not satisfied. Attempting to upgrade " +
                                                    "location settings ");
                                            try {
                                                // Show the dialog by calling startResolutionForResult(), and check the
                                                // result in onActivityResult().
                                                ResolvableApiException rae = (ResolvableApiException) e;
                                                rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                            } catch (Exception sie) {
                                                Log.i("Location", "PendingIntent unable to execute request.");
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                                    "fixed here. Fix in Settings.";
                                            Log.e("Location", errorMessage);
                                            Toast.makeText(activity, "Please turn on GPS!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    public static void stopLocationUpdates() {
        if (mFusedLocationClient != null && mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


}
