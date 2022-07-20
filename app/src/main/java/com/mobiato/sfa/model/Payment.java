package com.mobiato.sfa.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Payment implements Parcelable {

    public String payment_id;
    public String invoice_id;
    public String collection_id;
    public String payment_type;
    public String payment_date;
    public String cheque_no;
    public String bank_name;
    public String payment_amount;
    public String cust_id;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(String collection_id) {
        this.collection_id = collection_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getCheque_no() {
        return cheque_no;
    }

    public void setCheque_no(String cheque_no) {
        this.cheque_no = cheque_no;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payment_id);
        dest.writeString(this.invoice_id);
        dest.writeString(this.collection_id);
        dest.writeString(this.payment_type);
        dest.writeString(this.payment_date);
        dest.writeString(this.cheque_no);
        dest.writeString(this.bank_name);
        dest.writeString(this.payment_amount);
        dest.writeString(this.cust_id);
    }

    public Payment() {
    }

    protected Payment(Parcel in) {
        this.payment_id = in.readString();
        this.invoice_id = in.readString();
        this.collection_id = in.readString();
        this.payment_type = in.readString();
        this.payment_date = in.readString();
        this.cheque_no = in.readString();
        this.bank_name = in.readString();
        this.payment_amount = in.readString();
        this.cust_id = in.readString();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel source) {
            return new Payment(source);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };
}
