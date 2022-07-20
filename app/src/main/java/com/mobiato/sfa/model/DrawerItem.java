package com.mobiato.sfa.model;

/**
 * Created by HTISPL on 24-Dec-16.
 */

public class DrawerItem {

    private String name;
    private int imgRes;
    private DrawerEnum drawerEnum;

    public DrawerItem() {
    }

    public DrawerItem(String name, int image, DrawerEnum drawerEnum) {
        this.name = name;
        this.imgRes = image;
        this.drawerEnum = drawerEnum;
    }

    public String getName() {
        return name;
    }

    public DrawerItem setName(String name) {
        this.name = name;
        return this;
    }

    public int getImgRes() {
        return imgRes;
    }

    public DrawerItem setImgRes(int imgRes) {
        this.imgRes = imgRes;
        return this;
    }

    public DrawerEnum getDrawerEnum() {
        return drawerEnum;
    }

    public DrawerItem setDrawerEnum(DrawerEnum drawerEnum) {
        this.drawerEnum = drawerEnum;
        return this;
    }
}