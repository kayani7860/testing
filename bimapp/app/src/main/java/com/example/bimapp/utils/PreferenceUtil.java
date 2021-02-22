package com.example.bimapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bimapp.interfaces.Constant;

import static android.content.Context.MODE_PRIVATE;


public class PreferenceUtil implements Constant {


    private static final String PREFERENCE_NAME = "send_signal_preference";

    private static PreferenceUtil instance;
    private SharedPreferences sPref;

    private PreferenceUtil(Context context) {
        sPref = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtil(context);
        }
        return instance;
    }


    public boolean isLanguageSelected() {
        return sPref.getBoolean(IS_LANGUAGE, false);
    }

    public void setLanguageSelected(boolean value) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(IS_LANGUAGE, value);
        editor.apply();
    }


    public String getLanguage() {
        return sPref.getString(LANGUAGE, "en");
    }

    public void setLanguage(String value) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(LANGUAGE, value);
        editor.apply();
    }








}
