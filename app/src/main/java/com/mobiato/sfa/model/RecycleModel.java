package com.mobiato.sfa.model;

import java.util.ArrayList;

/**
 * Created by Himanshu on 4/10/2017.
 */


public class RecycleModel {

    public static final int DATE = 1;
    public static final int PROGRESS_VIEW = 2;


    public int type;
    //    public int data;
    public ArrayList<Feed> users;

    public RecycleModel(int type, ArrayList<Feed> users) {
        this.type = type;
//        this.data = data;
        this.users = users;
    }
}