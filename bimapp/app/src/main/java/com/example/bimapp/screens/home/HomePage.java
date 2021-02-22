package com.example.bimapp.screens.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bimapp.Bimapp;
import com.example.bimapp.screens.modes.energy.Energy;
import com.example.bimapp.screens.info.InfoPage;
import com.example.bimapp.R;
import com.example.bimapp.screens.modes.relaxation.Relaxation;
import com.example.bimapp.screens.modes.sleep.Sleep;

public class HomePage extends AppCompatActivity {

    TextView textview,energy,relaxation,sleep;
    ImageView imageView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_page);
        getSupportActionBar().hide();

        energy = (TextView) findViewById(R.id.textenergy);
        energy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Energy.class);
                startActivity(intent);
            }
        });

        relaxation = (TextView) findViewById(R.id.textrelax);
        relaxation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Relaxation.class);
                startActivity(intent);
            }
        });

        sleep = (TextView) findViewById(R.id.textsleep);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, Sleep.class);
                startActivity(intent);
            }
        });

        imageView = (ImageView) findViewById(R.id.infoIconhomepage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, InfoPage.class);
                startActivity(intent);
            }
        });

        if(Bimapp.db.getLanguage().equals("sv"))
        {
          energy.setText(R.string.Energisv);
          relaxation.setText(R.string.Relaxsv);
          sleep.setText(R.string.Sleepsv);
        }



    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
