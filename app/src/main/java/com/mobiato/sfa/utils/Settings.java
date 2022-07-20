package com.mobiato.sfa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mobiato.sfa.model.Salesman;


public class Settings {
    private static final String PREFS_NAME = "RihamPrefs";
    private static SharedPreferences sharedPreferences;

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(Settings.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getInstance() {
        return sharedPreferences;
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, null);
    }


    public static int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clearPreferenceStore() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }

    public static void setSalesmanData(String key, Salesman mSalesman) {
        SharedPreferences.Editor editor = getEditor();
        Gson gson = new Gson();
        String json = gson.toJson(mSalesman);
        editor.putString(key, json);
        editor.commit();
    }

    public static Salesman getSalesmanData(String key) {
        Salesman mSalesman;
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        mSalesman = gson.fromJson(json, Salesman.class);
        return mSalesman;
    }
}
