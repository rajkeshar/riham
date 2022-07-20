package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CollectionData implements Serializable {

    @SerializedName("outstanding_id")
    @Expose
    public String orderId;
    @SerializedName("customerid")
    @Expose
    private String customerNo;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("invoice_amount")
    @Expose
    private String invoiceAmount;
    @SerializedName("due_date")
    @Expose
    private String invoiceDueDate;
    @SerializedName("invoice_date")
    @Expose
    private String invoiceDate;
    @SerializedName("paid_amount")
    @Expose
    private String amountCleared;
    @SerializedName("pending_amount")
    @Expose
    private String amountPending;
    private String amountEnter;
    private String tempAmountDue;
    private String isInvoiceComplete;
    private boolean isInvoiceSplite;
    private boolean isSpliteChecked;
    private String indicator;
    private String cashAmt;
    private String cashAmtPre;
    private String chequeAmt;
    private String chequeAmtPre;
    private String chequeAmtIndividule;
    private String chequeDat;
    private String chequeNo;
    private String bankCode;
    private String bankName;
    private String amountPay;
    private String collType;
    private String isOutStand;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(String invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getAmountCleared() {
        return amountCleared;
    }

    public void setAmountCleared(String amountCleared) {
        this.amountCleared = amountCleared;
    }

    public String getAmountEnter() {
        return amountEnter;
    }

    public void setAmountEnter(String amountEnter) {
        this.amountEnter = amountEnter;
    }

    public String getTempAmountDue() {
        return tempAmountDue;
    }

    public void setTempAmountDue(String tempAmountDue) {
        this.tempAmountDue = tempAmountDue;
    }

    public String getIsInvoiceComplete() {
        return isInvoiceComplete;
    }

    public void setIsInvoiceComplete(String isInvoiceComplete) {
        this.isInvoiceComplete = isInvoiceComplete;
    }

    public boolean isInvoiceSplite() {
        return isInvoiceSplite;
    }

    public void setInvoiceSplite(boolean invoiceSplite) {
        isInvoiceSplite = invoiceSplite;
    }

    public boolean isSpliteChecked() {
        return isSpliteChecked;
    }

    public void setSpliteChecked(boolean spliteChecked) {
        isSpliteChecked = spliteChecked;
    }

    public String getAmountPending() {
        return amountPending;
    }

    public void setAmountPending(String amountPending) {
        this.amountPending = amountPending;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(String cashAmt) {
        this.cashAmt = cashAmt;
    }

    public String getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(String chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public String getChequeAmtIndividule() {
        return chequeAmtIndividule;
    }

    public void setChequeAmtIndividule(String chequeAmtIndividule) {
        this.chequeAmtIndividule = chequeAmtIndividule;
    }

    public String getChequeDat() {
        return chequeDat;
    }

    public void setChequeDat(String chequeDat) {
        this.chequeDat = chequeDat;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCollType() {
        return collType;
    }

    public void setCollType(String collType) {
        this.collType = collType;
    }

    public String getAmountPay() {
        return amountPay;
    }

    public void setAmountPay(String amountPay) {
        this.amountPay = amountPay;
    }

    public String getCashAmtPre() {
        return cashAmtPre;
    }

    public void setCashAmtPre(String cashAmtPre) {
        this.cashAmtPre = cashAmtPre;
    }

    public String getChequeAmtPre() {
        return chequeAmtPre;
    }

    public void setChequeAmtPre(String chequeAmtPre) {
        this.chequeAmtPre = chequeAmtPre;
    }

    public String getIsOutStand() {
        return isOutStand;
    }

    public void setIsOutStand(String isOutStand) {
        this.isOutStand = isOutStand;
    }
}
