package com.mobiato.sfa.model;

import java.io.Serializable;

/**
 * Created by SUGNESH on 1/04/2019.
 */

public class Profile implements Serializable, Cloneable {

    private String name, mobile, email;
    private int imgRes;


    public String getName() {
        return name;
    }

    public Profile setName(String name) {
        this.name = name;
        return this;
    }

    public int getImgRes() {
        return imgRes;
    }

    public Profile setImgRes(int imgRes) {
        this.imgRes = imgRes;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
