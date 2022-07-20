package com.mobiato.sfa.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mobiato.sfa.App;
import com.mobiato.sfa.activity.AddSalesmanLoadActivity;
import com.mobiato.sfa.activity.DataPostingActivity;
import com.mobiato.sfa.activity.ExchangeActivity;
import com.mobiato.sfa.activity.FreshUnloadActivity;
import com.mobiato.sfa.activity.LoadVerifyActivity;
import com.mobiato.sfa.activity.MultiCollectionActivity;
import com.mobiato.sfa.activity.OrderRequestActivity;
import com.mobiato.sfa.activity.PaymentDetailActivity;
import com.mobiato.sfa.activity.ReturnUpdateActivity;
import com.mobiato.sfa.activity.SalesActivity;
import com.mobiato.sfa.activity.TransactionActivity;
import com.mobiato.sfa.merchandising.ReturnRequestActivity;
import com.mobiato.sfa.model.DevicesData;
import com.mobiato.sfa.utils.Settings;
import com.mobiato.sfa.utils.StringUtilities;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;
import com.printer.sdk.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class PrintLog {

    private BluetoothAdapter mBtAdapter;
    private ArrayList<DevicesData> arrData = new ArrayList<>();
    Context context;
    private Activity activity;
    private JSONObject status;
    private String callbackId;
    private ProgressDialog progressDialog;
    private int retryCount;
    private String sMacAddr;
    String devicename;
    public static PrinterInstance myPrinter;
    private JSONArray jArr;
    private static BluetoothDevice mDevice;
    public static boolean isConnected = false;
    private int count;

    public PrintLog(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void execute(String request, JSONArray jsonArray) {
        this.jArr = jsonArray;
        this.arrData = new ArrayList();

        print();

    }

    public void print() {
        new asyncgetDevices().execute(new Void[0]);
    }

    private class asyncgetDevices extends AsyncTask<Void, Set<BluetoothDevice>, Set<BluetoothDevice>> {
        private asyncgetDevices() {
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected Set<BluetoothDevice> doInBackground(Void... params) {
            PrintLog.this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            PrintLog.this.mBtAdapter.enable();
            if (PrintLog.this.mBtAdapter.isEnabled()) {
                return PrintLog.this.mBtAdapter.getBondedDevices();
            }
            cancel(true);

            new asyncgetDevices().execute(new Void[0]);
            return null;
        }

        protected void onPostExecute(Set<BluetoothDevice> pairedDevices) {
            super.onPostExecute(pairedDevices);
            try {
                if (pairedDevices == null) {
                    return;
                }
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        Log.e("devices", device.getName() + StringUtilities.LF + device.getAddress());
                        System.out.println("devices" + device.getName() + StringUtilities.LF + device.getAddress());
                        DevicesData d1 = new DevicesData();
                        d1.setAddress(device.getAddress());
                        d1.setName(device.getName());
                        PrintLog.this.arrData.add(d1);
                    }
                    PrintLog.this.showDialog(PrintLog.this.arrData);
                    return;
                }
                Toast.makeText(context, "No Devices Found!", Toast.LENGTH_SHORT).show();
                System.out.println("No devices");
                Log.e("devices", "No devices");

                try {
                    PrintLog.this.status.put("status", false);
                    PrintLog.this.status.put("isconnected", -7);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PrintLog.this.sendUpdate(PrintLog.this.status, true);

                if (context instanceof LoadVerifyActivity) {
                    ((LoadVerifyActivity) context).callbackFunction();
                }

                if (context instanceof DataPostingActivity) {
                    ((DataPostingActivity) context).callback("");
                }

                if (context instanceof TransactionActivity) {
                    ((TransactionActivity) context).callback("");
                }

                if (context instanceof SalesActivity) {
                    ((SalesActivity) context).callback();
                }
                if (context instanceof PaymentDetailActivity) {
                    ((PaymentDetailActivity) context).callback();
                }
                if (context instanceof OrderRequestActivity) {
                    ((OrderRequestActivity) context).callback();
                }
                if (context instanceof FreshUnloadActivity) {
                    ((FreshUnloadActivity) context).callback();
                }

                if (context instanceof ReturnUpdateActivity) {
                    ((ReturnUpdateActivity) context).callback();
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (context instanceof LoadVerifyActivity) {
                    ((LoadVerifyActivity) context).callbackFunction();
                }

                if (context instanceof DataPostingActivity) {
                    ((DataPostingActivity) context).callback("");
                }
                if (context instanceof TransactionActivity) {
                    ((TransactionActivity) context).callback("");
                }

                if (context instanceof SalesActivity) {
                    ((SalesActivity) context).callback();
                }
                if (context instanceof PaymentDetailActivity) {
                    ((PaymentDetailActivity) context).callback();
                }
                if (context instanceof OrderRequestActivity) {
                    ((OrderRequestActivity) context).callback();
                }
                if (context instanceof FreshUnloadActivity) {
                    ((FreshUnloadActivity) context).callback();
                }

                if (context instanceof ReturnUpdateActivity) {
                    ((ReturnUpdateActivity) context).callback();
                }
                // Crashlytics.logException(e);
            }
        }
    }

    private void sendUpdate(JSONObject obj, boolean keepCallback) {
        if (this.callbackId != null) {
            Log.e("End of plugin", "true");
            // success(new PluginResult(Status.OK, obj), this.callbackId);
        }
    }

    public void showDialog(ArrayList<DevicesData> arrData) {
        String[] arrDevices = new String[arrData.size()];
        for (int i = 0; i < arrData.size(); i++) {
            arrDevices[i] = new StringBuilder(String.valueOf(((DevicesData) arrData.get(i)).getName())).append(StringUtilities.LF).append(((DevicesData) arrData.get(i)).getAddress()).toString();
        }
        activity.runOnUiThread(new C04694(arrDevices, arrData));
    }

    class C04694 implements Runnable {
        private final /* synthetic */ ArrayList val$arrData;
        private final /* synthetic */ String[] val$arrDevices;

        /* renamed from: com.phonegap.sfa.DotmatHelper.4.1 */
        class C04671 implements DialogInterface.OnClickListener {
            private final /* synthetic */ ArrayList val$arrData;

            C04671(ArrayList arrayList) {
                this.val$arrData = arrayList;
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    PrintLog.this.retryCount = 0;
                    PrintLog.this.sMacAddr = ((DevicesData) this.val$arrData.get(which)).getAddress();
                    if (!PrintLog.this.sMacAddr.contains(":") && PrintLog.this.sMacAddr.length() == 12) {
                        char[] cAddr = new char[17];
                        int j = 0;
                        for (int i = 0; i < 12; i += 2) {
                            PrintLog.this.sMacAddr.getChars(i, i + 2, cAddr, j);
                            j += 2;
                            if (j < 17) {
                                int j2 = j + 1;
                                cAddr[j] = ':';
                                j = j2;
                            }

                        }
                        PrintLog.this.sMacAddr = new String(cAddr);
                    }
                    connect2BlueToothdevice(PrintLog.this.sMacAddr);
//                    PrintLog.this.showProgressDialog();
//                    PrintLog.this.doConnectionTest(PrintLog.this.sMacAddr);
                } catch (Exception e) {
                    if (context instanceof LoadVerifyActivity) {
                        ((LoadVerifyActivity) context).callbackFunction();
                    }
                    if (context instanceof DataPostingActivity) {
                        ((DataPostingActivity) context).callback("");
                    }
                    if (context instanceof TransactionActivity) {
                        ((TransactionActivity) context).callback("");
                    }

                    if (context instanceof SalesActivity) {
                        ((SalesActivity) context).callback();
                    }
                    if (context instanceof PaymentDetailActivity) {
                        ((PaymentDetailActivity) context).callback();
                    }
                    if (context instanceof OrderRequestActivity) {
                        ((OrderRequestActivity) context).callback();
                    }
                    if (context instanceof FreshUnloadActivity) {
                        ((FreshUnloadActivity) context).callback();
                    }

                    if (context instanceof ReturnUpdateActivity) {
                        ((ReturnUpdateActivity) context).callback();
                    }
                    e.printStackTrace();
                    // Crashlytics.logException(e);
                }
                Toast.makeText(context, " you have selected " + ((DevicesData) this.val$arrData.get(which)).getName(), Toast.LENGTH_SHORT).show();
            }
        }

        /* renamed from: com.phonegap.sfa.DotmatHelper.4.2 */
        class C04682 implements DialogInterface.OnClickListener {
            C04682() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    //   PrinterHelper.this.status.put("status", false);
                    //   PrinterHelper.this.status.put("isconnected", -1);

                    if (context instanceof LoadVerifyActivity) {
                        ((LoadVerifyActivity) context).callbackFunction();
                    }

                    if (context instanceof DataPostingActivity) {
                        ((DataPostingActivity) context).callback("");
                    }
                    if (context instanceof TransactionActivity) {
                        ((TransactionActivity) context).callback("");
                    }

                    if (context instanceof SalesActivity) {
                        ((SalesActivity) context).callback();
                    }
                    if (context instanceof PaymentDetailActivity) {
                        ((PaymentDetailActivity) context).callback();
                    }
                    if (context instanceof OrderRequestActivity) {
                        ((OrderRequestActivity) context).callback();
                    }
                    if (context instanceof FreshUnloadActivity) {
                        ((FreshUnloadActivity) context).callback();
                    }
                    if (context instanceof ReturnUpdateActivity) {
                        ((ReturnUpdateActivity) context).callback();
                    }

                    if (context instanceof AddSalesmanLoadActivity) {
                        ((AddSalesmanLoadActivity) context).callback();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PrintLog.this.sendUpdate(PrintLog.this.status, true);
            }
        }

        C04694(String[] strArr, ArrayList arrayList) {
            this.val$arrDevices = strArr;
            this.val$arrData = arrayList;
        }

        public void run() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Choose Device To Pair");
            dialog.setItems(this.val$arrDevices, new C04671(this.val$arrData));
            dialog.setPositiveButton("Cancel", new C04682());
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private void connect2BlueToothdevice(String devicesAddress) {
        showProgressDialog();
        mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(devicesAddress);
        devicename = mDevice.getName();
        myPrinter = PrinterInstance.getPrinterInstance(mDevice, mHandler);
        if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {// 未绑定
            // IntentFilter boundFilter = new IntentFilter();
            // boundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            // mContext.registerReceiver(boundDeviceReceiver, boundFilter);
            PairOrConnect(true);
        } else {
            PairOrConnect(false);
        }
    }

    private void PairOrConnect(boolean pair) {
        if (pair) {
            IntentFilter boundFilter = new IntentFilter();
            boundFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            context.registerReceiver(boundDeviceReceiver, boundFilter);
            boolean success = false;
            try {
                // // 自动设置pin值
                // Method autoBondMethod =
                // BluetoothDevice.class.getMethod("setPin", new Class[] {
                // byte[].class });
                // boolean result = (Boolean) autoBondMethod.invoke(mDevice, new
                // Object[] { "1234".getBytes() });
                // Log.i(TAG, "setPin is success? : " + result);

                // 开始配对 这段代码打开输入配对密码的对话框
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                success = (Boolean) createBondMethod.invoke(mDevice);
                // // 取消用户输入
                // Method cancelInputMethod =
                // BluetoothDevice.class.getMethod("cancelPairingUserInput");
                // boolean cancleResult = (Boolean)
                // cancelInputMethod.invoke(mDevice);
                // Log.i(TAG, "cancle is success? : " + cancleResult);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Log.i("sdfds", "createBond is success? : " + success);
        } else {
            new connectThread().start();
        }
    }

    private BroadcastReceiver boundDeviceReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!mDevice.equals(device)) {
                    return;
                }
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.i("", "bounding......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.i("", "bound success");
                        // if bound success, auto init BluetoothPrinter. open
                        // connect.
                        context.unregisterReceiver(boundDeviceReceiver);
                        PrintLog.this.showProgressDialog();
                        // 配对完成开始连接
                        if (myPrinter != null) {
                            new connectThread().start();
                        }
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.i("", "执行顺序----4");
                        context.unregisterReceiver(boundDeviceReceiver);
                        Log.i("", "bound cancel");
                        break;
                    default:
                        break;
                }

            }
        }
    };

    private class connectThread extends Thread {
        @Override
        public void run() {
            if (myPrinter != null) {
                isConnected = myPrinter.openConnection();
                PrinterConstants.paperWidth = 724;

                PrintLog.this.dismissProgress();

                try {
                    PrintLog.this.printReports(PrintLog.this.sMacAddr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                PrintLog.this.dismissProgress();
            }
        }
    }

    @SuppressLint({"NewApi"})
    void printReports(String address) throws JSONException {
        Log.e("Print Report", "" + this.jArr.toString());
        Log.e("Array Length", "" + this.jArr.length());

        try {
            for (int j = 0; j < (this.jArr.length() > 1 ? 1 : this.jArr.length()); j++) {
                JSONArray jInner = this.jArr.getJSONArray(j);
                for (int i = 0; i < jInner.length(); i++) {
                    JSONObject jDict = jInner.getJSONObject(i);
                    String request = jDict.getString(App.REQUEST);
                    JSONObject jsnData = jDict.getJSONObject("mainArr");
                    this.count = 0;

                    if (request.equals(App.LOAD_SUMMARY_REQUEST)) {
                        parseLoadSummaryResponse(jsnData, address);
                    }


                    if (request.equals(App.VAN_STOCK)) {
                        printVanStockReport(jsnData, address);
                    }

                    if (request.equals(App.SALES_INVOICE)) {
                        parseSalesInvoiceResponse(jsnData, address);
                    }

                    if (request.equals(App.COLLECTION)) {
                        parseCollectionResponse(jsnData, address);
                    }

                    if (request.equals(App.ORDER)) {
                        parseOrderRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.LOAD)) {
                        parseLoadRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.SALESMAN_LOAD)) {
                        parseSalesmanLoadRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.RETURN)) {
                        parseReturnResponse(jsnData, address);
                    }

                    if (request.equals(App.RETURN_REQUEST)) {
                        parseReturnRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.DEPOSITE_SUMMARY)) {
                        printDepositReport(jsnData, address);
                    }

                    if (request.equals(App.SALES_SUMMARY)) {
                        printSalesSummaryReport(jsnData, address);
                    }

                    if (request.equals(App.EXCHANGE)) {
                        parseExchangeRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.END_INVENTORY)) {
                        printEndInventoryReport(jsnData, address);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            for (int j = 0; j < this.jArr.length(); j++) {
                JSONArray jInner = this.jArr.getJSONArray(j);
                JSONArray jInnerSub = jInner.getJSONArray(0);
                for (int i = 0; i < jInnerSub.length(); i++) {
                    JSONObject jDict = jInnerSub.getJSONObject(i);
                    String request = jDict.getString(App.REQUEST);
                    JSONObject jsnData = jDict.getJSONObject("mainArr");
                    this.count = 0;

                    if (request.equals(App.LOAD_SUMMARY_REQUEST)) {
                        parseLoadSummaryResponse(jsnData, address);
                    }

                    if (request.equals(App.VAN_STOCK)) {
                        printVanStockReport(jsnData, address);
                    }

                    if (request.equals(App.SALES_INVOICE)) {
                        parseSalesInvoiceResponse(jsnData, address);
                    }

                    if (request.equals(App.SALESMAN_LOAD)) {
                        parseSalesmanLoadRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.COLLECTION)) {
                        parseCollectionResponse(jsnData, address);
                    }

                    if (request.equals(App.ORDER)) {
                        parseOrderRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.LOAD)) {
                        parseLoadRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.RETURN)) {
                        parseReturnResponse(jsnData, address);
                    }

                    if (request.equals(App.RETURN_REQUEST)) {
                        parseReturnRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.DEPOSITE_SUMMARY)) {
                        printDepositReport(jsnData, address);
                    }

                    if (request.equals(App.SALES_SUMMARY)) {
                        printSalesSummaryReport(jsnData, address);
                    }

                    if (request.equals(App.EXCHANGE)) {
                        parseExchangeRequestResponse(jsnData, address);
                    }

                    if (request.equals(App.END_INVENTORY)) {
                        printEndInventoryReport(jsnData, address);
                    }

                }
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        myPrinter.closeConnection();
        isConnected = false;

        if (context instanceof DataPostingActivity) {
            ((DataPostingActivity) context).callback("");
        }

        if (context instanceof TransactionActivity) {
            ((TransactionActivity) context).callback("");
        }

        if (context instanceof ReturnUpdateActivity) {
            ((ReturnUpdateActivity) context).callback();
        }

    }

    private Handler mHandler = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    //Toast.makeText(context, "Connected...", Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.FAILED:
                    // Toast.makeText(context, "Connection failed!", Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.CLOSED:
                    // Toast.makeText(context, "Connection close", Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    // Toast.makeText(context, "No device", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        }

    };

    private void showProgressDialog() {
        try {
            this.progressDialog = ProgressDialog.show(context, "Please Wait", "Connecting to printer..", false);
        } catch (Exception e) {
            e.printStackTrace();
            // Crashlytics.logException(e);
        }
    }

    private void dismissProgress() {
        try {
            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Crashlytics.logException(e);
        }

    }

    //Load Summary Print
    void parseLoadSummaryResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("LOADING SHEET\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            printHeader(myPrinter);
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            StringBuffer sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - "
                    + Settings.getString(App.SALESMANNAME) + "        Date:" + object.getString("DOC DATE") + " " +
                    object.getString("TIME") + "\n\n");
            sb.append("Load Sheet No: " + object.getString("Load Number") + " \n");
            myPrinter.printText(sb.toString());

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableLoadSheet(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("---------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
            sb = new StringBuffer();
            if (jTOBject.has("BaseQty")) {
                sb.append("   Total Qty : " + jTOBject.getString("BaseQty") + "/" + jTOBject.getString("AlterQty") + "     \n");
            }
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            Bitmap bitmap1 = getBitmap(object.getString("signature"));
            myPrinter.printImage(bitmap1, PrinterConstants.PAlign.START, 0, 128);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("    --------------------              ---------------------\n");
            sb.append("     Salesman Signature                  Depot Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof LoadVerifyActivity) {
                ((LoadVerifyActivity) context).callbackFunction();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTableLoadSheet(PrinterInstance mPrinter, JSONArray jData) {

        //String column = "ItemCode;Description;BaseUOM;BaseQty;AlterUOM;AlterQty";
        String column = "Sr.No;ItemCode;Description;UOM;Qty";
        Table table = null;
        if (PrinterConstants.paperWidth == 724) {
            table = new Table(column, ";", new int[]{8, 13, 22, 12, 11});
        }
        table.setColumnAlignRight(false);

        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {

                JSONArray jArr = jData.getJSONArray(i);
                String srNo = String.valueOf(i + 1);
                String itemcode = jArr.getString(0);
                String name = jArr.getString(1);

                String uom = jArr.getString(3) + "/" + jArr.getString(5);
                String qty = jArr.getString(2) + "/" + jArr.getString(4);

                String column2 = "" + srNo + ";" + itemcode + ";" + name + ";" + uom + ";" + qty;

                Table table2 = null;
                table2 = new Table(column2, ";", new int[]{8, 13, 22, 12, 11});
                table.setColumnAlignRight(false);

                mPrinter.printTable(table2);

            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    //Invoice Summary Print
    void parseSalesInvoiceResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);

            //Print Header
            printInvoiceHeaderTime(myPrinter, "Invoice", object.getString("invoicenumber"), object.getString("DOC DATE"),
                    object.getString("TIME"));

            StringBuffer sb;
            String customerType = "Cash Sale";
            if (object.getString("customertype").equals("1")) {
                customerType = "(Cash Sale)";
            } else if (object.getString("customertype").equals("2")) {
                customerType = "(Credit Sale)";
            } else if (object.getString("customertype").equals("3")) {
                customerType = "(Credit Sale)";
            }

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("Tax Invoice\n");
            sb.append(customerType);
            myPrinter.printText(sb.toString());
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);

            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("Issued to: " + object.getString("CUSTOMERID") + "              Sold By: " + Settings.getString(App.SALESMANINO) + " - "
                    + Settings.getString(App.SALESMANNAME) + "\n");
            sb.append(object.getString("CUSTOMER") + "\n");
            sb.append(object.getString("ADDRESS") + "\n");
            sb.append("Customer TIN:" + object.getString("TRN") + "\n");
            myPrinter.printText(sb.toString());

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableInvoice(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);

            printTableTotalInvoice(myPrinter, jTOBject);

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append(" Invoice Value is Inclusive of 18% VAT\n\n");
            myPrinter.printText(sb.toString());
            myPrinter.setFont(0, 0, 0, 1, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("This is a system generated invoice and doesn't require \nany signature\n");
            myPrinter.printText(sb.toString());
            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("Thank you for purchasing Riham products\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof SalesActivity) {
                ((SalesActivity) context).callback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTableInvoice(PrinterInstance mPrinter, JSONArray jData) {
        String column = "S/N;Description;Qty;UOM;Price;Total";
        Table table = null;
        table = new Table(column, ";", new int[]{6, 22, 6, 7, 12, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String srNo = String.valueOf((i + 1));
                String name = jArr.getString(1);
                String qty = jArr.getString(3);
                String UOM = jArr.getString(2);
                String price = jArr.getString(4);
                String Total = jArr.getString(6);

                String columnRow = "" + srNo + ";" + name + ";" + qty + ";" + UOM + ";" + price
                        + ";" + Total;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{6, 22, 6, 7, 12, 12});
                tableRow.setColumnAlignRight(false);

                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableTotalInvoice(PrinterInstance mPrinter, JSONObject jTOBject) {
        String subTotal = "", titleDiscount = "Discount:", titleTotal = "Total (UGX)",
                netAmt = "0", vatAmt = "0", titleVat = "", invoiceTotal = "", discount = "";

        String column;
        Table table = null;
        try {
            if (jTOBject.has("Sub Total")) {
                subTotal = jTOBject.getString("Sub Total");
            }

            if (jTOBject.has("VAT")) {
                vatAmt = jTOBject.getString("VAT");
            }

            if (jTOBject.has("Discount")) {
                discount = jTOBject.getString("Discount");
            }

            if (jTOBject.has("NET")) {
                netAmt = jTOBject.getString("NET");
            }

            if (jTOBject.has("Invoice Total")) {
                invoiceTotal = jTOBject.getString("Invoice Total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        titleVat = "VAT: " + vatAmt + "  NET: " + netAmt;

        String name1 = " ", titleSub = "Sub Total (UGX)";
        column = name1 + ";" + titleSub + ";" + subTotal;
        table = new Table(column, ";", new int[]{34, 20, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        column = " " + ";" + titleDiscount + ";" + discount;
        table = new Table(column, ";", new int[]{34, 20, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        column = titleVat + ";" + titleTotal + ";" + invoiceTotal;
        table = new Table(column, ";", new int[]{34, 20, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);
    }

    //Collection Print
    void parseCollectionResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("PAYMENT RECEIPT\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            printInvoiceHeader(myPrinter, "Payment", object.getString("DOCUMENT NO"), object.getString("DOC DATE"));

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            StringBuffer sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - " + object.getString("SALESMAN") + "\n");
            sb.append("Customer: " + object.getString("CUSTOMERID") + " - " + object.getString("CUSTOMER") + "\n\n");
            myPrinter.printText(sb.toString());
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableCollection(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONObject jTotal = object.getJSONObject("TOTAL");
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
            sb = new StringBuffer();
            if (jTotal.has("Amount Paid")) {
                sb.append("   Total : " + jTotal.getString("Amount Paid") + "   \n");
            }
            myPrinter.printText(sb.toString());
//            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
//            sb = new StringBuffer();
//            if (jTotal.has("Remain Amt")) {
//                sb.append("   Balance : " + jTotal.getString("Remain Amt") + "   \n\n");
//            }
//            myPrinter.printText(sb.toString());

            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("PAYMENT DETAIL\n");
            myPrinter.setFont(0, 0, 0, 0, 0);

            switch (Integer.parseInt(object.getString("PaymentType"))) {

                case 0:
                    if (object.has("Cash")) {
                        JSONObject jCash = object.getJSONObject("Cash");
                        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                        myPrinter.printText("Cash : " + jCash.getString("Amount"));
                    } else {
                        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                        myPrinter.printText("Cash : 0");
                    }
                    break;
                case 1:
                    if (object.has("Cheque")) {
                        JSONArray jCheque = object.getJSONArray("Cheque");
                        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                        myPrinter.printText("Cheque\n\n");
                        printTableChequeCollection(myPrinter, jCheque);

                        sb = new StringBuffer();
                        sb.append("================================================================\n");
                        myPrinter.printText(sb.toString());

                    } else {
                        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                        myPrinter.printText("Cheque");
                    }
                    break;
            }

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 1, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("This is a system generated receipt and doesn't require \nany signature\n");
            myPrinter.printText(sb.toString());
           /* myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("Thank you for purchasing Riham products\n");
            myPrinter.printText(sb.toString());*/
            /*myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("     --------------------               --------------------\n");
            sb.append("      Salesman Signature                 Customer Signature\n");
            myPrinter.printText(sb.toString());
*/
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof PaymentDetailActivity) {
                ((PaymentDetailActivity) context).callback();
            }

            if (context instanceof MultiCollectionActivity) {
                ((MultiCollectionActivity) context).callback();
            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableCollection(PrinterInstance mPrinter, JSONArray jData) {

        String column = "Invoice#;Due Date;Inv Date;Due Amt;Amt Paid";
        Table table = null;
        table = new Table(column, ";", new int[]{14, 14, 13, 12, 4});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);

                String invoice = " " + jArr.getString(0);
                String dueDate = jArr.getString(1);
                String invocieDate = jArr.getString(2);
                String amtDue = jArr.getString(3);
                String balance = jArr.getString(4);
                String amtPaid = jArr.getString(5);


                String columnRow = invoice + ";" + dueDate + ";" + invocieDate + ";" + amtDue
                        + ";" + amtPaid;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{14, 14, 13, 12, 4});
                tableRow.setColumnAlignRight(false);
                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        // mPrinter.printTable(table);
    }

    public static void printTableChequeCollection(PrinterInstance mPrinter, JSONArray jData) {

        String column = "Cheque Date#;Cheque No;Bank;Amount";
        Table table = null;
        table = new Table(column, ";", new int[]{19, 19, 19, 11});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONObject jArr = jData.getJSONObject(i);

                String chqDate = jArr.getString("Cheque Date");
                String chqNo = jArr.getString("Cheque No");
                String bank = jArr.getString("Bank");
                String amount = jArr.getString("Amount");

                //table.addRow(chqDate + ";" + chqNo + ";" + bank + ";" + amount);
                String columnRow = chqDate + ";" + chqNo + ";" + bank + ";" + amount;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{19, 19, 19, 11});
                tableRow.setColumnAlignRight(false);
                mPrinter.printTable(tableRow);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    //Van Stock Print
    void printVanStockReport(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("VAN STOCK\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            printHeader(myPrinter);

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            StringBuffer sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - "
                    + Settings.getString(App.SALESMANNAME) + "        Date:" + object.getString("DOC DATE") + " " +
                    object.getString("TIME") + "\n\n");
            myPrinter.printText(sb.toString());

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableVanStock(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("---------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
            sb = new StringBuffer();
            if (jTOBject.has("LOADED QTY")) {
                sb.append("   LOADED QTY : " + jTOBject.getString("LOADED QTY") + "   \n");
            }
            if (jTOBject.has("SALE QTY")) {
                sb.append("   SALE QTY : " + jTOBject.getString("SALE QTY") + "   \n");
            }
            if (jTOBject.has("TRUCK STOCK")) {
                sb.append("   TRUCK STOCK : " + jTOBject.getString("TRUCK STOCK") + "   \n");
            }
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("    --------------------              ---------------------\n");
            sb.append("     Salesman Signature                  Depot Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTableVanStock(PrinterInstance mPrinter, JSONArray jData) {

        String column = "S/N;Description;LoadQty;SaleQty;Truck Stock";
        Table table = null;
        table = new Table(column, ";", new int[]{6, 27, 10, 10, 13});
        table.setColumnAlignRight(false);

        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {

                JSONArray jArr = jData.getJSONArray(i);
                String srNo = String.valueOf((i + 1));
                String name = jArr.getString(1);
                String dQty = jArr.getString(2);
                String saleQty = jArr.getString(3);
                String stock = jArr.getString(4);

                String column2 = srNo + ";" + name + ";" + dQty + ";" + saleQty + ";" + stock;

                Table table2 = null;
                table2 = new Table(column2, ";", new int[]{6, 27, 10, 10, 13});
                table2.setColumnAlignRight(false);

                mPrinter.printTable(table2);

            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    //Order Summary Print
    void parseOrderRequestResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);

            if (object.getString("isHyperMarket").equalsIgnoreCase("true")) {
                printInvoiceHeaderHariss(myPrinter, "Order", object.getString("invoicenumber"), object.getString("deliveryDate"));
            } else {
                printInvoiceHeader(myPrinter, "Order", object.getString("invoicenumber"), object.getString("deliveryDate"));
            }

            StringBuffer sb = new StringBuffer();
            sb.append("\n");
            myPrinter.printText(sb.toString());
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("ORDER RECEIPT\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);

            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - " + object.getString("SALESMAN") + "\n");
            sb.append("Customer: " + object.getString("CUSTOMERID") + " - " + object.getString("CUSTOMER") + "\n\n");
            myPrinter.printText(sb.toString());

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableOrder(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);
            printTableTotalOrder(myPrinter, jTOBject, object.getString("isHyperMarket"));

            sb = new StringBuffer();
            sb.append("=================================================================\n");
            myPrinter.printText(sb.toString());


            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            if (object.getString("isHyperMarket").equalsIgnoreCase("true")) {
                Bitmap bitmap1 = getBitmap(object.getString("signature"));
//                if (bitmap1.getWidth() > PrinterConstants.paperWidth) {
//                    bitmap1 = Utils.zoomImage(bitmap1, PrinterConstants.paperWidth);
//                }
                myPrinter.printImage(bitmap1, PrinterConstants.PAlign.CENTER, 0, 128);
            }
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("--------------------\n");
            sb.append(" Customer Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof OrderRequestActivity) {
                ((OrderRequestActivity) context).callback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTableOrder(PrinterInstance mPrinter, JSONArray jData) {
        String column = "S/N;Description;Qty;UOM;Price;Total";
        Table table = null;
        table = new Table(column, ";", new int[]{6, 26, 5, 6, 10, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String srNo = String.valueOf((i + 1));
                String name = jArr.getString(1);
                String qty = jArr.getString(3);
                String UOM = jArr.getString(2);
                String price = jArr.getString(4);
                String Total = jArr.getString(6);

                String columnRow = "" + srNo + ";" + name + ";" + qty + ";" + UOM + ";" + price
                        + ";" + Total;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{6, 26, 5, 6, 10, 1});
                tableRow.setColumnAlignRight(false);

                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableTotalOrder(PrinterInstance mPrinter, JSONObject jTOBject, String isHyperMarket) {
        String subTotal = "",
                netAmt = "0", vatAmt = "0", invoiceTotal = "", discount = "-", excise = "0";

        String column;
        Table table = null;
        try {
            if (jTOBject.has("Sub Total")) {
                subTotal = jTOBject.getString("Sub Total");
            }

            if (jTOBject.has("VAT")) {
                vatAmt = jTOBject.getString("VAT");
            }

            if (jTOBject.has("NET")) {
                netAmt = jTOBject.getString("NET");
            }

            if (jTOBject.has("Total Amount")) {
                invoiceTotal = jTOBject.getString("Total Amount");
            }

            if (jTOBject.has("EXCISE")) {
                excise = jTOBject.getString("EXCISE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        column = " ;" + "Gross Amount (UGX);" + subTotal;
        table = new Table(column, ";", new int[]{32, 20, 14});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        column = " ;" + "Discount:  0%;" + discount;
        table = new Table(column, ";", new int[]{32, 20, 14});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        column = " ;" + "VAT (UGX);" + vatAmt;
        table = new Table(column, ";", new int[]{32, 20, 14});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        if (isHyperMarket.equalsIgnoreCase("true")) {
            column = " ;" + "Excise (UGX);" + excise;
            table = new Table(column, ";", new int[]{32, 20, 14});
            table.setColumnAlignRight(false);
            mPrinter.printTable(table);
        }

        column = " ;" + "NET (UGX);" + netAmt;
        table = new Table(column, ";", new int[]{32, 20, 14});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        column = " ;" + "Total Amount (UGX);" + invoiceTotal;
        table = new Table(column, ";", new int[]{32, 20, 14});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

    }

    //Load Summary Print
    void parseLoadRequestResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("LOAD REQUEST\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);

            printHeader(myPrinter);

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            myPrinter.printText("Load No: " + object.getString("invoicenumber") + "             Date:" + object.getString("DOC DATE") + " " +
                    object.getString("TIME") + " \n\n");

            StringBuffer sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableLoadRequest(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);

            if (jTOBject.has("VAT")) {
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
                myPrinter.printText("VAT: " + jTOBject.getString("VAT") + " UGX \n");
            }

            if (jTOBject.has("Total Amount")) {
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
                myPrinter.printText("Total Amount: " + jTOBject.getString("Total Amount") + " UGX \n");
            }

            sb = new StringBuffer();
            sb.append("=================================================================\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("     --------------------               --------------------\n");
            sb.append("      Salesman Signature                   Depot Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof OrderRequestActivity) {
                ((OrderRequestActivity) context).callback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Load Summary Print
    void parseSalesmanLoadRequestResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("SALESMAN LOAD\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);

            printHeader(myPrinter);

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            myPrinter.printText("Load No: " + object.getString("invoicenumber") + "             Date:" + object.getString("deliveryDate") + " \n");
            myPrinter.printText("Salesman: " + object.getString("SALESMAN_NAME") + " \n");
            myPrinter.printText("Route: " + object.getString("ROUTENAME") + " \n");

            StringBuffer sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableSalesmanLoadRequest(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("=================================================================\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("     --------------------               --------------------\n");
            sb.append("      Salesman Signature                   Depot Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof AddSalesmanLoadActivity) {
                ((AddSalesmanLoadActivity) context).callback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTableLoadRequest(PrinterInstance mPrinter, JSONArray jData) {
        String column = "ItemCode;Description;UOM;Qty;Price;Total";
        Table table = null;
        table = new Table(column, ";", new int[]{11, 20, 7, 7, 10, 10});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String itemcode = " " + jArr.getString(0);
                String name = jArr.getString(1);
                String qty = jArr.getString(3);
                String UOM = jArr.getString(2);
                String price = jArr.getString(4);
                String Total = jArr.getString(5);

                String columnRow = "" + itemcode + ";" + name + ";" + UOM + ";" + qty + ";" + price
                        + ";" + Total;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{11, 20, 7, 7, 10, 10});
                tableRow.setColumnAlignRight(false);

                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableSalesmanLoadRequest(PrinterInstance mPrinter, JSONArray jData) {
        String column = "ItemCode;Description;UOM;Qty";
        Table table = null;
        table = new Table(column, ";", new int[]{11, 40, 7, 7});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String itemcode = " " + jArr.getString(0);
                String name = jArr.getString(1);
                String qty = jArr.getString(3);
                String UOM = jArr.getString(2);

                String columnRow = "" + itemcode + ";" + name + ";" + UOM + ";" + qty;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{11, 40, 7, 7});
                tableRow.setColumnAlignRight(false);

                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    //Return Summary Print
    void parseReturnResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);

            printInvoiceHeader(myPrinter, "Credit", object.getString("invoicenumber"), object.getString("DOC DATE"));

            StringBuffer sb = new StringBuffer();
            sb.append("\n");
            myPrinter.printText(sb.toString());
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("CREDIT NOTE\n");
            myPrinter.setFont(0, 0, 0, 0, 0);

            if (object.getString("RETURN TYPE").equalsIgnoreCase("Bad")) {
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                myPrinter.printText("(BAD RETURN)\n");
            } else {
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                myPrinter.printText("(GOOD RETURN)\n");
            }

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - " + object.getString("SALESMAN") + "\n");
            sb.append("Customer: " + object.getString("CUSTOMERID") + " - " + object.getString("CUSTOMER") + "\n\n");
            myPrinter.printText(sb.toString());

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableReturn(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);
            printTableTotalReturn(myPrinter, jTOBject);

            sb = new StringBuffer();
            sb.append("=================================================================\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 1, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("This is a system generated document and doesn't require \nany signature\n");
            myPrinter.printText(sb.toString());
            /*myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("--------------------\n");
            sb.append(" Customer Signature\n");
            myPrinter.printText(sb.toString());*/

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof OrderRequestActivity) {
                ((OrderRequestActivity) context).callback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Return Request Summary Print
    void parseReturnRequestResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);

            printInvoiceHeader(myPrinter, "Credit", object.getString("invoicenumber"), object.getString("DOC DATE"));

            StringBuffer sb = new StringBuffer();
            sb.append("\n");
            myPrinter.printText(sb.toString());
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("RETURN REQUEST\n");
            myPrinter.setFont(0, 0, 0, 0, 0);

            if (object.getString("RETURN TYPE").equalsIgnoreCase("Bad")) {
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                myPrinter.printText("(BAD RETURN)\n");
            } else {
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                myPrinter.printText("(GOOD RETURN)\n");
            }

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - " + object.getString("SALESMAN") + "\n");
            sb.append("Customer: " + object.getString("CUSTOMERID") + " - " + object.getString("CUSTOMER") + "\n\n");
            myPrinter.printText(sb.toString());

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableReturn(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);
            printTableTotalReturn(myPrinter, jTOBject);

            sb = new StringBuffer();
            sb.append("=================================================================\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 1, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("This is a system generated document and doesn't require \nany signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof ReturnRequestActivity) {
                ((ReturnRequestActivity) context).callback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void printTableReturn(PrinterInstance mPrinter, JSONArray jData) {
        String column = "ItemCode;Description;UOM;Qty;Price;Total";
        Table table = null;
        table = new Table(column, ";", new int[]{11, 15, 11, 8, 10, 10});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String itemcode = " " + jArr.getString(0);
                String name = jArr.getString(1);
                String qty = jArr.getString(3);
                String UOM = jArr.getString(2);
                String price = jArr.getString(4);
                String Total = jArr.getString(5);

                String columnRow = "" + itemcode + ";" + name + ";" + UOM + ";" + qty + ";" + price
                        + ";" + Total;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{11, 15, 11, 8, 10, 10});
                tableRow.setColumnAlignRight(false);

                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableTotalReturn(PrinterInstance mPrinter, JSONObject jTOBject) {
        String vatAmt = "0", invoiceTotal = "";

        String column;
        Table table = null;
        try {
            if (jTOBject.has("VAT")) {
                vatAmt = jTOBject.getString("VAT");
            }

            if (jTOBject.has("Total Amount")) {
                invoiceTotal = jTOBject.getString("Total Amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        column = " ;" + "VAT (UGX);" + vatAmt;
        table = new Table(column, ";", new int[]{36, 20, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        column = " ;" + "Total Amount (UGX);" + invoiceTotal;
        table = new Table(column, ";", new int[]{36, 20, 10});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

    }

    //Deposit Summary Print
    void printDepositReport(JSONObject object, String args) {

        try {
            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("DEPOSIT SUMMARY\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);

            printHeader(myPrinter);

            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            StringBuffer sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - " + Settings.getString(App.SALESMANNAME) + "        Date:" + object.getString("DOC DATE") + " " +
                    object.getString("TIME") + "\n\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (int j = 0; j < jData.length(); j++) {
                JSONObject mainJson = jData.getJSONObject(j);
                JSONArray jInnerData = mainJson.getJSONArray("DATA");
                printTableDepositSummary(myPrinter, jInnerData);
            }

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

//            JSONArray jTotal = object.getJSONArray("TOTAL");
//            if (jTotal.length() > 0) {
//                printTableDepositCash(myPrinter, jTotal);
//            }

            String totalAmt = object.getString("TOTAL DEPOSIT AMOUNT");
            String totalCashAmt = object.getString("TOTAL CASH AMOUNT");
            String totalChequeAmt = object.getString("TOTAL CHEQUE AMOUNT");
            String totalCreditAmt = object.getString("TOTAL CREDIT AMOUNT");

            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
            sb = new StringBuffer();
            sb.append("  \nTOTAL CASH AMOUNT : " + totalCashAmt + "  \n");
            sb.append("  TOTAL CHEQUE AMOUNT : " + totalChequeAmt + "  \n");
            sb.append("  TOTAL DEPOSIT AMOUNT : " + totalAmt + "  \n\n");
            myPrinter.printText(sb.toString());

            JSONArray jData1 = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (int j = 0; j < jData1.length(); j++) {
                JSONObject mainJson = jData1.getJSONObject(j);
                JSONArray jInnerData = mainJson.getJSONArray("DATACredit");
                if (jInnerData.length() > 0) {

                    myPrinter.setFont(0, 1, 0, 0, 0);
                    myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                    myPrinter.printText("CREDIT NOTE\n\n");
                    myPrinter.setFont(0, 0, 0, 0, 0);
                    printTableDepositSummary(myPrinter, jInnerData);

                    sb = new StringBuffer();
                    sb.append("----------------------------------------------------------------\n");
                    myPrinter.printText(sb.toString());

//                    JSONArray jTotalCredit = object.getJSONArray("TOTALCredit");
//                    if (jTotalCredit.length() > 0) {
//                        printTableDepositCredit(myPrinter, jTotalCredit);
//                    }

                }
            }

            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
            sb = new StringBuffer();
            sb.append("  \nTOTAL CREDIT AMOUNT : " + "-" + totalCreditAmt + "  \n\n");
            myPrinter.printText(sb.toString());

            String totalAmtCredit = object.getString("GRAND TOTAL");
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
            sb = new StringBuffer();
            sb.append("  \nGRAND TOTAL : " + totalAmtCredit + " ");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("     --------------------               --------------------\n");
            sb.append("      Accounts Signature                 Salesman Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof DataPostingActivity) {
                ((DataPostingActivity) context).callback("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTableDepositSummary(PrinterInstance mPrinter, JSONArray jData) {

        String column = "TR. No;Cust. No.;Customer;Cheq. Date;Cheque Amt;Cash Amt";
        Table table = null;
        table = new Table(column, ";", new int[]{11, 11, 13, 11, 11, 11});
        table.setColumnAlignRight(false);

        mPrinter.printTable(table);
        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);

                String trNo = " " + jArr.getString(1);
                String custNo = jArr.getString(4);
                String customer = jArr.getString(5);
                String chqDate = jArr.getString(6);
                String chqAmt = jArr.getString(7);
                String cashAmt = jArr.getString(8);

                String columnRow1 = trNo + ";" + custNo + ";" + customer + ";" + chqDate + ";" + chqAmt + ";" + cashAmt;
                Table tableRow = null;
                tableRow = new Table(columnRow1, ";", new int[]{11, 11, 13, 11, 11, 11});
                tableRow.setColumnAlignRight(false);
                mPrinter.printTable(tableRow);

            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    //Sales Summary Print
    void printSalesSummaryReport(JSONObject object, String args) {

        try {
            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("SALES SUMMARY\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);

            printHeader(myPrinter);

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            StringBuffer sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - " + Settings.getString(App.SALESMANNAME) + "        Date:" + object.getString("DOC DATE") + " " +
                    object.getString("TIME") + "\n\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);

            JSONArray cashData = object.getJSONArray("data");
            if (cashData.length() > 0) {
                myPrinter.setFont(0, 1, 0, 0, 0);
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                myPrinter.printText("CASH INVOICE\n\n");
                myPrinter.setFont(0, 0, 0, 0, 0);

                //Print Table Data
                printTableSalesSummary(myPrinter, cashData);

                sb = new StringBuffer();
                sb.append("----------------------------------------------------------------\n");
                myPrinter.printText(sb.toString());

                if (jTOBject.has("Total_Case")) {
                    myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
                    sb = new StringBuffer();
                    sb.append("  \nTotal Cash Sales : " + jTOBject.getString("Total_Case") + "  \n");
                    sb.append("  Total Cash Collection : " + jTOBject.getString("Total_CasePaid") + "  \n\n");
                    myPrinter.printText(sb.toString());
                }
            }

            JSONArray creditData = object.getJSONArray("tcData");
            if (creditData.length() > 0) {
                myPrinter.setFont(0, 1, 0, 0, 0);
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                myPrinter.printText("CREDIT INVOICE\n\n");
                myPrinter.setFont(0, 0, 0, 0, 0);

                //Print Table Data
                printTableSalesSummary(myPrinter, creditData);

                sb = new StringBuffer();
                sb.append("----------------------------------------------------------------\n");
                myPrinter.printText(sb.toString());

                if (jTOBject.has("Total_Credit")) {
                    myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
                    sb = new StringBuffer();
                    sb.append("  \nTotal Credit Sales : " + jTOBject.getString("Total_Credit") + "  \n");
                    sb.append("  Total Credit Collection : " + jTOBject.getString("Total_CreditPaid") + "  \n\n");
                    myPrinter.printText(sb.toString());
                }

            }

            JSONArray creditNoteData = object.getJSONArray("creditData");
            if (creditNoteData.length() > 0) {
                myPrinter.setFont(0, 1, 0, 0, 0);
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
                myPrinter.printText("CREDIT NOTE\n\n");
                myPrinter.setFont(0, 0, 0, 0, 0);

                //Print Table Data
                printTableSalesSummaryNote(myPrinter, creditNoteData);

                sb = new StringBuffer();
                sb.append("----------------------------------------------------------------\n");
                myPrinter.printText(sb.toString());

                if (jTOBject.has("Total_CreditReturn")) {
                    myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_RIGHT);
                    sb = new StringBuffer();
                    sb.append("  \nTotal Credit : " + jTOBject.getString("Total_CreditReturn") + "  \n\n");
                    myPrinter.printText(sb.toString());
                }
            }

            if (jTOBject.has("Total_Sales")) {
                myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
                sb = new StringBuffer();
                sb.append("  Total Sales : " + jTOBject.getString("Total_Sales") + "      Total Amount Collected : " +
                        jTOBject.getString("Total_SalesPaid"));
                myPrinter.printText(sb.toString());
            }

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("     --------------------               --------------------\n");
            sb.append("     Supervisor Signature                Salesman Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableSalesSummary(PrinterInstance mPrinter, JSONArray jData) {

        String column = "Inv. No;Cust. No.;Customer;Net Sale;Disc.;Amt.Paid";
        Table table = null;
        table = new Table(column, ";", new int[]{12, 12, 15, 10, 6, 10});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);

                String trNo = jArr.getString(1);
                String custNo = jArr.getString(2);
                String customer = jArr.getString(3);
                String netSale = jArr.getString(4);
                String discount = jArr.getString(5);
                String amtPaid = jArr.getString(6);

                String columnRow = " " + trNo + ";" + custNo + ";" + customer + ";" + netSale + ";" + discount + ";" + amtPaid;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{12, 12, 15, 10, 6, 10});
                tableRow.setColumnAlignRight(true);
                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    public static void printTableSalesSummaryNote(PrinterInstance mPrinter, JSONArray jData) {

        String column = "Inv. No;Cust. No.;Customer;Net Sale;Disc.;Amt.Paid";
        Table table = null;
        table = new Table(column, ";", new int[]{12, 12, 15, 10, 6, 10});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);

                String trNo = jArr.getString(1);
                String custNo = jArr.getString(2);
                String customer = jArr.getString(3);
                String netSale = jArr.getString(4);
                String amtPaid = jArr.getString(6);
                String discount = "00";

                String columnRow = " " + trNo + ";" + custNo + ";" + customer + ";" + netSale + ";" + discount + ";" + amtPaid;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{12, 12, 15, 10, 6, 10});
                tableRow.setColumnAlignRight(true);
                mPrinter.printTable(tableRow);

            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    //Exchange Summary Print
    void parseExchangeRequestResponse(JSONObject object, String args) {

        try {

            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("EXCHANGE RECEIPT\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            printInvoiceHeader(myPrinter, "Exchange", object.getString("exchangeNo"), object.getString("DOC DATE"));

            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            StringBuffer sb = new StringBuffer();
            sb.append("\n\nIssued to:                     Sold By: " + Settings.getString(App.SALESMANINO) + " - "
                    + Settings.getString(App.SALESMANNAME) + "\n");
            sb.append(object.getString("CUSTOMER") + "\n");
            sb.append(object.getString("ADDRESS") + "\n");
            sb.append("Customer TIN:" + object.getString("TRN") + "\n");
            myPrinter.printText(sb.toString());

            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("Received\n");
            myPrinter.setFont(0, 0, 0, 0, 0);

            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jExchData = object.getJSONArray(JsonRpcUtil.PARAM_CREDIT_DATA);
            printTableCredit(myPrinter, jExchData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);
            printTableTotalCredit(myPrinter, jTOBject);

            sb = new StringBuffer();
            sb.append("=================================================================\n");
            myPrinter.printText(sb.toString());

            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("Issued\n");
            myPrinter.setFont(0, 0, 0, 0, 0);

            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableCredit(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            printTableTotalSale(myPrinter, jTOBject);

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append(" Invoice Value is Inclusive of 18% VAT\n\n");
            myPrinter.printText(sb.toString());
            myPrinter.setFont(0, 0, 0, 1, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("This is a system generated invoice and doesn't require \nany signature\n");
            myPrinter.printText(sb.toString());
            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            sb = new StringBuffer();
            sb.append("Thank you for purchasing Riham products\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof ExchangeActivity) {
                ((ExchangeActivity) context).callback();
            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableCredit(PrinterInstance mPrinter, JSONArray jData) {
        String column = "S/N;Description;Qty;UOM;Price;Total";
        Table table = null;
        table = new Table(column, ";", new int[]{6, 22, 6, 7, 12, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());

        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String srNo = String.valueOf((i + 1));
                String name = jArr.getString(1);
                String qty = jArr.getString(3);
                String UOM = jArr.getString(2);
                String price = jArr.getString(4);
                String Total = jArr.getString(6);

                String columnRow = "" + srNo + ";" + name + ";" + qty + ";" + UOM + ";" + price
                        + ";" + Total;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{6, 22, 6, 7, 12, 12});
                tableRow.setColumnAlignRight(false);

                mPrinter.printTable(tableRow);

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void printTableTotalCredit(PrinterInstance mPrinter, JSONObject jTOBject) {
        String subTotal = "",
                netAmt = "0", vatAmt = "0", invoiceTotal = "", discount = "-";

        String column;
        Table table = null;
        try {

            if (jTOBject.has("Total Amount Credit")) {
                invoiceTotal = jTOBject.getString("Total Amount Credit");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        column = " ;" + "Total Amount (UGX);" + invoiceTotal;
        table = new Table(column, ";", new int[]{34, 20, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

    }

    public static void printTableTotalSale(PrinterInstance mPrinter, JSONObject jTOBject) {
        String invoiceTotal = "";

        String column;
        Table table = null;
        try {

            if (jTOBject.has("Invoice Total")) {
                invoiceTotal = jTOBject.getString("Invoice Total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        column = " ;" + "Total Amount (UGX);" + invoiceTotal;
        table = new Table(column, ";", new int[]{34, 20, 12});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

    }


    public void printHeader(PrinterInstance mPrinter) {
        mPrinter.setFont(0, 0, 0, 1, 0);
        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        sb.append(Settings.getString(App.DEPOTNAME) + "\n");
        mPrinter.printText(sb.toString());
        mPrinter.setFont(0, 0, 0, 0, 0);
        sb = new StringBuffer();
        sb.append(Settings.getString(App.DEPOTSTREET) + "\n");
        if (!Settings.getString(App.DEPOTVILLAGE).equalsIgnoreCase("")) {
            sb.append(Settings.getString(App.DEPOTVILLAGE) + ", " + Settings.getString(App.DEPOTCITY) + ", Uganda\n");
        } else {
            sb.append(Settings.getString(App.DEPOTCITY) + ", Uganda\n");
        }
        sb.append("Tel:" + Settings.getString(App.DEPOTPHONE) + "\n");
        sb.append("TIN No: " + Settings.getString(App.DEPOTTIN));
        mPrinter.printText(sb.toString());
    }

    public void printHeaderUnload(PrinterInstance mPrinter, String routeName) {
        mPrinter.setFont(0, 0, 0, 1, 0);
        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        sb.append(Settings.getString(App.DEPOTNAME) + "\n");
        mPrinter.printText(sb.toString());
        mPrinter.setFont(0, 0, 0, 0, 0);
//        sb.append("Route: " + routeName + "\n");
//        mPrinter.printText(sb.toString());
    }

    public void printHeaderHariss(PrinterInstance mPrinter) {
        mPrinter.setFont(0, 0, 0, 1, 0);
        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        sb.append("Hariss International Limited" + "\n");
        mPrinter.printText(sb.toString());
        mPrinter.setFont(0, 0, 0, 0, 0);
        sb = new StringBuffer();
        sb.append("Plot 32, 33 Bombo Rd," + "\n");
        sb.append("Kawempe, Kampala, Uganda\n");
        sb.append("Tel: 0204000100" + "\n");
        sb.append("TIN No: 1000032087");
        mPrinter.printText(sb.toString());
    }

    public void printInvoiceHeaderHariss(PrinterInstance myPrinter, String type, String strInvoice, String strDate) {

        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        sb.append("Hariss International Limited" + "\n");
        myPrinter.printText(sb.toString());
        myPrinter.setFont(0, 0, 0, 0, 0);
        sb = new StringBuffer();
        sb.append("Plot 32, 33 Bombo Rd," + "\n");
        myPrinter.printText(sb.toString());
        String address2 = "";
        address2 = "Kawempe, Kampala, Uganda";

        String invoiceNo, date;
        if (type.equalsIgnoreCase("Invoice")) {
            invoiceNo = "Invoice No: " + strInvoice;
            date = "Date:       " + strDate;
        } else if (type.equalsIgnoreCase("Payment")) {
            invoiceNo = "Receipt No: " + strInvoice;
            date = "Date:       " + strDate;
        } else if (type.equalsIgnoreCase("Order")) {
            invoiceNo = "Order No: " + strInvoice;
            date = "Date:     " + strDate;
        } else if (type.equalsIgnoreCase("Credit")) {
            invoiceNo = "Invoice No: " + strInvoice;
            date = "Date:       " + strDate;
        } else {
            invoiceNo = "Exchange No: " + strInvoice;
            date = "Date:        " + strDate;
        }

        String column = address2 + ";" + invoiceNo;
        Table table = null;
        table = new Table(column, ";", new int[]{36, 30});
        table.setColumnAlignRight(false);
        myPrinter.printTable(table);

        String telNp = "Tel:0204000100";
        column = telNp + ";" + date;
        table = new Table(column, ";", new int[]{36, 30});
        table.setColumnAlignRight(false);
        myPrinter.printTable(table);

        sb = new StringBuffer();
        sb.append("TIN No: 1000032087");
        myPrinter.printText(sb.toString());

    }

    public void printInvoiceHeader(PrinterInstance myPrinter, String type, String strInvoice, String strDate) {

        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        sb.append(Settings.getString(App.DEPOTNAME) + "\n");
        myPrinter.printText(sb.toString());
        myPrinter.setFont(0, 0, 0, 0, 0);
        sb = new StringBuffer();
        sb.append(Settings.getString(App.DEPOTSTREET) + "\n");
        myPrinter.printText(sb.toString());
        String address2 = "";
        if (!Settings.getString(App.DEPOTVILLAGE).equalsIgnoreCase("")) {
            address2 = Settings.getString(App.DEPOTVILLAGE) + ", " + Settings.getString(App.DEPOTCITY) + ", Uganda";
        } else {
            address2 = Settings.getString(App.DEPOTCITY) + ", Uganda";
        }
        String invoiceNo, date;
        if (type.equalsIgnoreCase("Invoice")) {
            invoiceNo = "Invoice No: " + strInvoice;
            date = "Date:       " + strDate;
        } else if (type.equalsIgnoreCase("Payment")) {
            invoiceNo = "Receipt No: " + strInvoice;
            date = "Date:       " + strDate;
        } else if (type.equalsIgnoreCase("Order")) {
            invoiceNo = "Order No: " + strInvoice;
            date = "Date:     " + strDate;
        } else if (type.equalsIgnoreCase("Credit")) {
            invoiceNo = "Invoice No: " + strInvoice;
            date = "Date:       " + strDate;
        } else {
            invoiceNo = "Exchange No: " + strInvoice;
            date = "Date:        " + strDate;
        }

        String column = address2 + ";" + invoiceNo;
        Table table = null;
        table = new Table(column, ";", new int[]{36, 30});
        table.setColumnAlignRight(false);
        myPrinter.printTable(table);

        String telNp = "Tel:" + Settings.getString(App.DEPOTPHONE);
        column = telNp + ";" + date;
        table = new Table(column, ";", new int[]{36, 30});
        table.setColumnAlignRight(false);
        myPrinter.printTable(table);

        sb = new StringBuffer();
        sb.append("TIN No: " + Settings.getString(App.DEPOTTIN));
        myPrinter.printText(sb.toString());

    }

    public void printInvoiceHeaderTime(PrinterInstance myPrinter, String type, String strInvoice, String strDate, String time) {

        myPrinter.setFont(0, 0, 0, 1, 0);
        myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        sb.append(Settings.getString(App.DEPOTNAME) + "\n");
        myPrinter.printText(sb.toString());
        myPrinter.setFont(0, 0, 0, 0, 0);
        sb = new StringBuffer();
        sb.append(Settings.getString(App.DEPOTSTREET) + "\n");
        myPrinter.printText(sb.toString());
        String address2 = "";
        if (!Settings.getString(App.DEPOTVILLAGE).equalsIgnoreCase("")) {
            address2 = Settings.getString(App.DEPOTVILLAGE) + ", " + Settings.getString(App.DEPOTCITY) + ", Uganda";
        } else {
            address2 = Settings.getString(App.DEPOTCITY) + ", Uganda";
        }
        String invoiceNo, date, invTime = "";
        if (type.equalsIgnoreCase("Invoice")) {
            invoiceNo = "Invoice No: " + strInvoice;
            date = "Date:       " + strDate;
            invTime = "Invoice Time: " + time;
        } else if (type.equalsIgnoreCase("Payment")) {
            invoiceNo = "Receipt No: " + strInvoice;
            date = "Date:       " + strDate;
        } else if (type.equalsIgnoreCase("Order")) {
            invoiceNo = "Order No: " + strInvoice;
            date = "Date:     " + strDate;
        } else if (type.equalsIgnoreCase("Credit")) {
            invoiceNo = "Invoice No: " + strInvoice;
            date = "Date:       " + strDate;
        } else {
            invoiceNo = "Exchange No: " + strInvoice;
            date = "Date:        " + strDate;
        }

        String column = address2 + ";" + invoiceNo;
        Table table = null;
        table = new Table(column, ";", new int[]{36, 30});
        table.setColumnAlignRight(false);
        myPrinter.printTable(table);

        String telNp = "Tel:" + Settings.getString(App.DEPOTPHONE);
        column = telNp + ";" + date;
        table = new Table(column, ";", new int[]{36, 30});
        table.setColumnAlignRight(false);
        myPrinter.printTable(table);

        String tin = "TIN No: " + Settings.getString(App.DEPOTTIN);
        column = tin + ";" + invTime;
        table = new Table(column, ";", new int[]{36, 30});
        table.setColumnAlignRight(false);
        myPrinter.printTable(table);

    }

    //End Inventory Print
    void printEndInventoryReport(JSONObject object, String args) {

        try {
            int i = 1;
            myPrinter.initPrinter();
            myPrinter.setLeftMargin(4);
            myPrinter.setFont(0, 1, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
            myPrinter.printText("ROUTE SETTELMENT SHEET\n");
            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);

            printHeaderUnload(myPrinter, object.getString("ROUTENAME"));

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            StringBuffer sb = new StringBuffer();
            sb.append("Salesman: " + Settings.getString(App.SALESMANINO) + " - " + Settings.getString(App.SALESMANNAME) + "        Date:" + object.getString("DOC DATE") + " " +
                    object.getString("TIME") + "\n\n");
            myPrinter.printText(sb.toString());

            sb = new StringBuffer();
            sb.append("================================================================\n");
            myPrinter.printText(sb.toString());

            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            printTableUnload(myPrinter, jData); // Print Data in Table Body

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);
            myPrinter.setFont(0, 0, 0, 0, 0);
            myPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
            sb = new StringBuffer();
            sb.append("     --------------------               --------------------\n");
            sb.append("      Salesman Signature                   Depot Signature\n");
            myPrinter.printText(sb.toString());

            myPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, i++);

            if (context instanceof FreshUnloadActivity) {
                ((FreshUnloadActivity) context).callback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTableUnload(PrinterInstance mPrinter, JSONArray jData) {
        String column = "S/N;Description;Load;Sold;Unload;Bad;Amount";
        Table table = null;
        table = new Table(column, ";", new int[]{4, 24, 7, 7, 7, 7, 7});
        table.setColumnAlignRight(false);
        mPrinter.printTable(table);

        StringBuffer sb = new StringBuffer();
        sb.append("================================================================\n");
        mPrinter.printText(sb.toString());
        String totalLoad = "0", totalSold = "0", totalUnload = "0", totalBad = "0";
        int LBQty = 0, LAQty = 0, SBQty = 0, SAQty = 0, UBQty = 0, UAQty = 0, BBQty = 0, BAQty = 0;
        int mtotal = 0;
        try {
            for (int i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String srNo = String.valueOf((i + 1));
                String name = jArr.getString(1);
                String LQty = jArr.getString(3);
                String UOM = jArr.getString(2);
                String saleQty = jArr.getString(4);
                String ULQty = jArr.getString(5);
                String badQty = jArr.getString(6);
                String price = jArr.getString(15);
                mtotal += Integer.parseInt(price);

                LBQty += Integer.parseInt(jArr.getString(7));
                LAQty += Integer.parseInt(jArr.getString(8));

                SBQty += Integer.parseInt(jArr.getString(9));
                SAQty += Integer.parseInt(jArr.getString(10));

                UBQty += Integer.parseInt(jArr.getString(11));
                UAQty += Integer.parseInt(jArr.getString(12));

                BBQty += Integer.parseInt(jArr.getString(13));
                BAQty += Integer.parseInt(jArr.getString(14));


                String columnRow = "" + srNo + ";" + name + " " + ";" + LQty + ";" + saleQty
                        + ";" + ULQty + ";" + badQty + ";" + price;
                Table tableRow = null;
                tableRow = new Table(columnRow, ";", new int[]{4, 24, 7, 7, 7, 7, 7});
                tableRow.setColumnAlignRight(false);

                mPrinter.printTable(tableRow);

            }

            sb = new StringBuffer();
            sb.append("----------------------------------------------------------------\n");
            mPrinter.printText(sb.toString());

            totalLoad = LAQty + "/" + LBQty;
            totalSold = SAQty + "/" + SBQty;
            totalUnload = UAQty + "/" + UBQty;
            totalBad = BAQty + "/" + BBQty;

            String columnTT = "Total :" + ";" + totalLoad + ";" + totalSold + ";" + totalUnload + ";" + totalBad + ";" + mtotal;
            Table tableTT = null;
            tableTT = new Table(columnTT, ";", new int[]{28, 7, 7, 7, 7, 7});
            tableTT.setColumnAlignRight(false);
            myPrinter.printTable(tableTT);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap = null;
            File f = new File(path);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 100, false);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
