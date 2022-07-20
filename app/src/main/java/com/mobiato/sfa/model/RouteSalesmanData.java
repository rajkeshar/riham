package com.mobiato.sfa.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RouteSalesmanData implements Serializable {

    @SerializedName("salesman_id")
    @Expose
    public String salesman_id;
    @SerializedName("salesmanname")
    @Expose
    public String salesmanname;
    @SerializedName("salesmancode")
    @Expose
    public String salesmancode;


    public RouteSalesmanData() {
    }

    public String getSalesman_id() {
        return salesman_id;
    }

    public void setSalesman_id(String salesman_id) {
        this.salesman_id = salesman_id;
    }

    public String getSalesmanname() {
        return salesmanname;
    }

    public void setSalesmanname(String salesmanname) {
        this.salesmanname = salesmanname;
    }

    public String getSalesmancode() {
        return salesmancode;
    }

    public void setSalesmancode(String salesmancode) {
        this.salesmancode = salesmancode;
    }
}