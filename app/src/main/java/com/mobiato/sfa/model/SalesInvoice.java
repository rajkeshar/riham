package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;


public class SalesInvoice implements Parcelable {

    public String inv_no;
    public String inv_type;
    public String inv_type_code;
    public String delivery_no;
    public String cust_code;
    public String cust_name;
    public String inv_date;
    public String del_date;
    public String total_qty;
    public String grossAmt;
    public String tot_amnt_sales;
    public String pre_VatAmt;
    public String vatAmt;
    public String exciseAmt;
    public String discountAmt;
    public String discountId;
    public String netAmt;
    public String exchangeAmt;
    public String is_uploaded;
    public String brNo;
    public String grNo;
    public String saletime;
    public String exchangeNo;
    public String altQty;
    public String baseQty;
    public String promotionId;
    public String latitude;
    public String longitute;
    public String purchaseName;
    public String purchaseNo;

    public SalesInvoice() {

    }


    protected SalesInvoice(Parcel in) {
        purchaseName = in.readString();
        purchaseNo = in.readString();
        inv_no = in.readString();
        inv_type = in.readString();
        exchangeNo = in.readString();
        inv_type_code = in.readString();
        delivery_no = in.readString();
        cust_code = in.readString();
        inv_date = in.readString();
        cust_name = in.readString();
        tot_amnt_sales = in.readString();
        del_date = in.readString();
        total_qty = in.readString();
        grossAmt = in.readString();
        pre_VatAmt = in.readString();
        exciseAmt = in.readString();
        vatAmt = in.readString();
        discountAmt = in.readString();
        discountId = in.readString();
        netAmt = in.readString();
        is_uploaded = in.readString();
        exchangeAmt = in.readString();
        brNo = in.readString();
        grNo = in.readString();
        saletime = in.readString();
        altQty = in.readString();
        baseQty = in.readString();
        promotionId = in.readString();
        latitude = in.readString();
        longitute = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inv_no);
        dest.writeString(inv_type);
        dest.writeString(exchangeNo);
        dest.writeString(inv_type_code);
        dest.writeString(delivery_no);
        dest.writeString(cust_code);
        dest.writeString(cust_name);
        dest.writeString(inv_date);
        dest.writeString(del_date);
        dest.writeString(total_qty);
        dest.writeString(tot_amnt_sales);
        dest.writeString(grossAmt);
        dest.writeString(pre_VatAmt);
        dest.writeString(exciseAmt);
        dest.writeString(vatAmt);
        dest.writeString(discountAmt);
        dest.writeString(discountId);
        dest.writeString(netAmt);
        dest.writeString(exchangeAmt);
        dest.writeString(is_uploaded);
        dest.writeString(grNo);
        dest.writeString(brNo);
        dest.writeString(saletime);
        dest.writeString(altQty);
        dest.writeString(baseQty);
        dest.writeString(promotionId);
        dest.writeString(latitude);
        dest.writeString(longitute);
        dest.writeString(purchaseName);
        dest.writeString(purchaseNo);
    }

    @SuppressWarnings("unused")
    public static final Creator<SalesInvoice> CREATOR = new Creator<SalesInvoice>() {
        @Override
        public SalesInvoice createFromParcel(Parcel in) {
            return new SalesInvoice(in);
        }

        @Override
        public SalesInvoice[] newArray(int size) {
            return new SalesInvoice[size];
        }
    };
}