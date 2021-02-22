package com.example.bimapp.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bimapp.Bimapp;
import com.example.bimapp.R;
import com.example.bimapp.interfaces.IDownloadCallback;
import com.example.bimapp.managers.DropBoxManager;
import com.example.bimapp.screens.home.HomePage;
import com.example.bimapp.screens.language.SelectLanguage;
import com.example.bimapp.utils.helper;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread {
        public void run() {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           nextActivity();
          //downloadServerFiles();
        }
    }

    /*private void downloadServerFiles() {
        DropBoxManager.getInstance().connect();
        DropBoxManager.getInstance().startDownload(new IDownloadCallback() {
            @Override
            public void onComplete() {
                Log.e("onComplete","true");
                nextActivity();
            }
        });
    }*/

    void nextActivity() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (Bimapp.db.isLanguageSelected()) {
                    if (!Bimapp.db.getLanguage().equals("en")) {
                        helper.setLanguage(SplashScreen.this, Bimapp.db.getLanguage());
                    }

                    Intent intent = new Intent(SplashScreen.this, HomePage.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, SelectLanguage.class);
                    startActivity(intent);
                    SplashScreen.this.finish();
                }
            }
        });
    }
}
