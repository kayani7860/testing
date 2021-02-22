package com.example.bimapp.screens.language;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bimapp.Bimapp;
import com.example.bimapp.R;
import com.example.bimapp.databinding.SelectLanguageBinding;
import com.example.bimapp.screens.home.HomePage;
import com.example.bimapp.utils.helper;


public class SelectLanguage extends AppCompatActivity {

    SelectLanguageBinding binding;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.select_language);
        getSupportActionBar().hide();

        binding.tvEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bimapp.db.setLanguageSelected(true);
                Bimapp.db.setLanguage("en");
                /*helper.setLanguage(SelectLanguage.this, "en");*/
                nextActivity();
            }
        });

        binding.tvSwedish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bimapp.db.setLanguageSelected(true);
                Bimapp.db.setLanguage("sv");
               /* helper.setLanguage(SelectLanguage.this, "sv");*/
                nextActivity();
            }
        });



    }

    void nextActivity(){
        Intent intent=new Intent(SelectLanguage.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}
