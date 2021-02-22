package com.example.bimapp.screens.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.bimapp.R;
import com.example.bimapp.screens.home.HomePage;

import android.widget.TextView;

public class InfoPage extends AppCompatActivity {
    ImageView imageView;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_info_page);
        getSupportActionBar().hide();

        String htmlAsString = getString(R.string.htmlform);      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView

        // set the html content on a TextView
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(htmlAsSpanned);

        imageView=(ImageView) findViewById(R.id.backInfo);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InfoPage.this, HomePage.class);
                startActivity(intent);
            }
        });




    }
}