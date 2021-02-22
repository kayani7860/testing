package com.example.bimapp;

import android.app.Application;
import android.content.Context;

import com.example.bimapp.utils.PreferenceUtil;


public class Bimapp extends Application {
    public static PreferenceUtil db;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        db = PreferenceUtil.getInstance(this);
        context=this;

    }

    public static  Context getGenericContext(){
        return context;
 }




}
