package com.example.bimapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class helper {

    private static SharedPreferences sharedPreferences;


    public static void setLanguage(Context context, String language) {

        sharedPreferences = context.getSharedPreferences("app", MODE_PRIVATE);

            if (!language.isEmpty()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lan", language);
                editor.apply();
            }

         /*
        Locale locale = new Locale(language);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());*/
    }

    public String getlan(Context context) {
        sharedPreferences = context.getSharedPreferences("app", MODE_PRIVATE);
        return sharedPreferences.getString("lan", "");
    }


}
