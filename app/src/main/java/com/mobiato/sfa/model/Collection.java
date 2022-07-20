package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Collection implements Serializable {

    @SerializedName("outstanding_id")
    @Expose
    public String coll_Id;
    @SerializedName("invoice_no")
    @Expose
    public String coll_invoiceNo;
    @SerializedName("invoice_date")
    @Expose
    public String coll_invoiceDate;
    @SerializedName("customerid")
    @Expose
    public String coll_customerId;
    @SerializedName("invoice_amount")
    @Expose
    public String coll_amount;
    @SerializedName("due_date")
    @Expose
    public String coll_dueDate;
    @SerializedName("paid_amount")
    @Expose
    public String coll_paidAmount;
    @SerializedName("pending_amount")
    @Expose
    public String coll_dueAmount;
    public String coll_type;
    public String coll_is_payable;
    public String is_uploaded;
    public String cheque_no;
    public String cash_amt;
    public String cheque_amt;
    public String cheque_date;
    public String coll_bank;
    public String customerType;

    public Collection() {
    }

    public String getColl_Id() {
        return coll_Id;
    }

    public void setColl_Id(String coll_Id) {
        this.coll_Id = coll_Id;
    }

    public String getColl_invoiceNo() {
        return coll_invoiceNo;
    }

    public void setColl_invoiceNo(String coll_invoiceNo) {
        this.coll_invoiceNo = coll_invoiceNo;
    }

    public String getColl_invoiceDate() {
        return coll_invoiceDate;
    }

    public void setColl_invoiceDate(String coll_invoiceDate) {
        this.coll_invoiceDate = coll_invoiceDate;
    }

    public String getColl_customerId() {
        return coll_customerId;
    }

    public void setColl_customerId(String coll_customerId) {
        this.coll_customerId = coll_customerId;
    }

    public String getColl_amount() {
        return coll_amount;
    }

    public void setColl_amount(String coll_amount) {
        this.coll_amount = coll_amount;
    }

    public String getColl_dueDate() {
        return coll_dueDate;
    }

    public void setColl_dueDate(String coll_dueDate) {
        this.coll_dueDate = coll_dueDate;
    }

    public String getColl_paidAmount() {
        return coll_paidAmount;
    }

    public void setColl_paidAmount(String coll_paidAmount) {
        this.coll_paidAmount = coll_paidAmount;
    }

    public String getColl_dueAmount() {
        return coll_dueAmount;
    }

    public void setColl_dueAmount(String coll_dueAmount) {
        this.coll_dueAmount = coll_dueAmount;
    }

    public String getColl_type() {
        return coll_type;
    }

    public void setColl_type(String coll_type) {
        this.coll_type = coll_type;
    }

    public String getColl_is_payable() {
        return coll_is_payable;
    }

    public void setColl_is_payable(String coll_is_payable) {
        this.coll_is_payable = coll_is_payable;
    }

    public String getIs_uploaded() {
        return is_uploaded;
    }

    public void setIs_uploaded(String is_uploaded) {
        this.is_uploaded = is_uploaded;
    }

    public String getCheque_no() {
        return cheque_no;
    }

    public void setCheque_no(String cheque_no) {
        this.cheque_no = cheque_no;
    }

    public String getCash_amt() {
        return cash_amt;
    }

    public void setCash_amt(String cash_amt) {
        this.cash_amt = cash_amt;
    }

    public String getCheque_amt() {
        return cheque_amt;
    }

    public void setCheque_amt(String cheque_amt) {
        this.cheque_amt = cheque_amt;
    }

    public String getCheque_date() {
        return cheque_date;
    }

    public void setCheque_date(String cheque_date) {
        this.cheque_date = cheque_date;
    }

    public String getColl_bank() {
        return coll_bank;
    }

    public void setColl_bank(String coll_bank) {
        this.coll_bank = coll_bank;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}