package com.example.bimapp.screens.modes.relaxation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.downloader.PRDownloader;
import com.example.bimapp.Bimapp;
import com.example.bimapp.R;
import com.example.bimapp.interfaces.IDownloadCallback;
import com.example.bimapp.managers.DropBoxManager;
import com.example.bimapp.screens.home.HomePage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Relaxation extends AppCompatActivity {

    ImageView imageView, play_pauseImage, RelaxDownload1, RelaxDownload2, RelaxDownload3, RelaxPlay1, RelaxPlay2, RelaxPlay3;
    TextView current_time, total_duration, RelaxTime1, RelaxTime2, RelaxTime3,RelaxText;
    SeekBar Relax_seek_bar;
    MediaPlayer media_Player, mediaPlayer2;
    Handler handler = new Handler();
    String audioUrl = "";
    Switch bSwitch;
    boolean istoogleon = true;

    String three_min, five_min, eight_min;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.relaxation);
        getSupportActionBar().hide();

        setPermission();
        inIt();
        setListener();
        DownloadedFilesCheck();

        RelaxText = (TextView) findViewById(R.id.textViewsRelax);
        if(Bimapp.db.getLanguage().equals("sv")){
            RelaxText.setText(R.string.Relaxsv);
        }

    }
    private void DownloadedFilesCheck(){
        if(Bimapp.db.getLanguage().equals("en")) {
            checkFiveMin(DropBoxManager.getInstance().isFileExist("RELAX 5 MINTal & Ljud.m4a") &&
                                       DropBoxManager.getInstance().isFileExist("RELAX 5 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("sv")) {
            checkFiveMin(DropBoxManager.getInstance().isFileExist("AVSLAPP 5 MINTal & Ljud.m4a") &&
                                       DropBoxManager.getInstance().isFileExist("AVSLAPP 5 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("en")) {
            checkEightMin(DropBoxManager.getInstance().isFileExist("RELAX 8 MINTal & Ljud.m4a") &&
                                      DropBoxManager.getInstance().isFileExist("RELAX 8 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("sv")) {
            checkEightMin(DropBoxManager.getInstance().isFileExist("AVSLAPP 8 MINTal & Ljud.m4a") &&
                                      DropBoxManager.getInstance().isFileExist("AVSLAPP 8 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("en")) {
            checkTwelveMin(DropBoxManager.getInstance().isFileExist("RELAX 12 MINTal & Ljud.m4a") &&
                                       DropBoxManager.getInstance().isFileExist("RELAX 12 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("sv")) {
            checkTwelveMin(DropBoxManager.getInstance().isFileExist("AVSLAPP 12 MINTal & Ljud.m4a") &&
                                       DropBoxManager.getInstance().isFileExist("AVSLAPP 12 MIN TAL.m4a"));
        }
    }

    private void checkFiveMin(boolean isDownloaded)
    {
        if(isDownloaded){
            RelaxDownload1.setImageResource(R.drawable.okicon);
        }
        else{
            RelaxDownload1.setImageResource(R.drawable.iosdownload);
        }
    }
    private void checkEightMin(boolean isDownloaded)
    {
        if(isDownloaded){
            RelaxDownload2.setImageResource(R.drawable.okicon);
        }
        else{
            RelaxDownload2.setImageResource(R.drawable.iosdownload);
        }

    }
    private void checkTwelveMin(boolean isDownloaded)
    {
        if(isDownloaded){
            RelaxDownload3.setImageResource(R.drawable.okicon);
        }
        else{
            RelaxDownload3.setImageResource(R.drawable.iosdownload);
        }

    }
    private void inIt(){
        imageView = (ImageView) findViewById(R.id.backrelax);
        play_pauseImage = findViewById(R.id.play_pauseImage);
        RelaxDownload1 = findViewById(R.id.relaxdownload1);
        RelaxDownload2 = findViewById(R.id.relaxdownload2);
        RelaxDownload3 = findViewById(R.id.relaxdownload3);
        current_time = findViewById(R.id.current_time);
        total_duration = findViewById(R.id.total_duration);
        Relax_seek_bar = findViewById(R.id.relax_seek_bar);
        //Energy play view
        RelaxPlay1 = findViewById(R.id.relaxplay1);
        RelaxPlay2 = findViewById(R.id.relaxplay2);
        RelaxPlay3 = findViewById(R.id.relaxplay3);
        //Energy Time View
        RelaxTime1 = findViewById(R.id.relaxtime1);
        RelaxTime2 = findViewById(R.id.relaxtime2);
        RelaxTime3 = findViewById(R.id.relaxtime3);
        media_Player = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();
        bSwitch = findViewById(R.id.switchR);


    }
    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){
        final Animation animFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        final Animation animBlink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);

        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                istoogleon = isChecked;
                if (istoogleon) {
                    media_Player.setVolume(1, 1);
                    mediaPlayer2.setVolume(0, 0);
                } else {
                    mediaPlayer2.setVolume(1, 1);
                    media_Player.setVolume(0, 0);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Relaxation.this, HomePage.class);
                startActivity(intent);
                media_Player.stop();
                mediaPlayer2.stop();

            }
        });
        RelaxPlay1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                RelaxPlay1.startAnimation(animFade);
                RelaxPlay2.clearAnimation();
                RelaxPlay3.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("RELAX 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("RELAX 5 MIN TAL.m4a")) {
                        prepareMediaPlayer("RELAX 5 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("RELAX 5 MIN TAL.m4a");
                        RelaxTime1.setTypeface(null,Typeface.BOLD);
                        RelaxTime2.setTextAppearance(R.style.TimeTextRelax);
                        RelaxTime3.setTextAppearance(R.style.TimeTextRelax);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    if(DropBoxManager.getInstance().isFileExist("AVSLAPP 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("AVSLAPP 5 MIN TAL.m4a")) {
                        prepareMediaPlayer("AVSLAPP 5 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("AVSLAPP 5 MIN TAL.m4a");
                        //RelaxTime1.setTypeface(null,Typeface.BOLD);
                        RelaxTime1.setTypeface(null,Typeface.BOLD);
                        RelaxTime2.setTextAppearance(R.style.TimeTextRelax);
                        RelaxTime3.setTextAppearance(R.style.TimeTextRelax);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }
            }
        });
        RelaxPlay2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                RelaxPlay2.startAnimation(animFade);
                RelaxPlay1.clearAnimation();
                RelaxPlay3.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){

                    if(DropBoxManager.getInstance().isFileExist("RELAX 8 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("RELAX 8 MIN TAL.m4a")) {
                        prepareMediaPlayer("RELAX 8 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("RELAX 8 MIN TAL.m4a");
                        RelaxTime2.setTypeface(null,Typeface.BOLD);
                        RelaxTime1.setTextAppearance(R.style.TimeTextRelax);
                        RelaxTime3.setTextAppearance(R.style.TimeTextRelax);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    if(DropBoxManager.getInstance().isFileExist("AVSLAPP 8 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("AVSLAPP 8 MIN TAL.m4a")) {
                        prepareMediaPlayer("AVSLAPP 8 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("AVSLAPP 8 MIN TAL.m4a");
                        RelaxTime2.setTypeface(null,Typeface.BOLD);
                        RelaxTime1.setTextAppearance(R.style.TimeTextRelax);
                        RelaxTime3.setTextAppearance(R.style.TimeTextRelax);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }
            }
        });
        RelaxPlay3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                RelaxPlay3.startAnimation(animFade);
                RelaxPlay2.clearAnimation();
                RelaxPlay1.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("RELAX 12 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("RELAX 12 MIN TAL.m4a")) {
                        prepareMediaPlayer("RELAX 12 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("RELAX 12 MIN TAL.m4a");
                        RelaxTime3.setTypeface(null,Typeface.BOLD);
                        RelaxTime2.setTextAppearance(R.style.TimeTextRelax);
                        RelaxTime1.setTextAppearance(R.style.TimeTextRelax);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    if(DropBoxManager.getInstance().isFileExist("AVSLAPP 12 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("AVSLAPP 12 MIN TAL.m4a")) {
                        prepareMediaPlayer("AVSLAPP 12 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("AVSLAPP 12 MIN TAL.m4a");
                        RelaxTime3.setTypeface(null,Typeface.BOLD);
                        RelaxTime2.setTextAppearance(R.style.TimeTextRelax);
                        RelaxTime1.setTextAppearance(R.style.TimeTextRelax);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }
            }
        });
        RelaxDownload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("RELAX 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("RELAX 5 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        RelaxDownload1.startAnimation(animBlink);
                        downloadServerFiles(16, "Tal & Ljud m4a",1);
                        downloadServerFiles(2, "Tal M4a",1);

                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("AVSLAPP 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("AVSLAPP 5 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        RelaxDownload1.startAnimation(animBlink);
                        downloadServerFiles(3, "Tal & Ljud m4a",1);
                        downloadServerFiles(0, "Tal M4a",1);

                    }

                }
            }
        });
        RelaxDownload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("RELAX 8 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("RELAX 8 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        RelaxDownload2.startAnimation(animBlink);
                        downloadServerFiles(10, "Tal & Ljud m4a",2);
                        downloadServerFiles(9, "Tal M4a",2);

                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("AVSLAPP 8 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("AVSLAPP 8 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        RelaxDownload2.startAnimation(animBlink);
                        downloadServerFiles(13, "Tal & Ljud m4a",2);
                        downloadServerFiles(6, "Tal M4a",2);

                    }

                }
            }
        });
        RelaxDownload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("RELAX 12 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("RELAX 12 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        RelaxDownload3.startAnimation(animBlink);
                        downloadServerFiles(7, "Tal & Ljud m4a",3);
                        downloadServerFiles(3, "Tal M4a",3);

                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("AVSLAPP 12 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("AVSLAPP 12 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        RelaxDownload3.startAnimation(animBlink);
                        downloadServerFiles(12, "Tal & Ljud m4a",3);
                        downloadServerFiles(8, "Tal M4a",3);

                    }

                }
            }
        });

        Relax_seek_bar.setMax(100);
        play_pauseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media_Player.isPlaying()&&mediaPlayer2.isPlaying()) {
                    handler.removeCallbacks(updater);
                    media_Player.pause();
                    mediaPlayer2.pause();
                    play_pauseImage.setImageResource(R.drawable.ic_play);
                } else {

                    media_Player.start();
                    mediaPlayer2.start();
                    play_pauseImage.setImageResource(R.drawable.ic_pause);
                    updateSeekbar();
                }


                if (istoogleon) {
                    media_Player.setVolume(1, 1);
                    mediaPlayer2.setVolume(0, 0);
                } else {
                    mediaPlayer2.setVolume(1, 1);
                    media_Player.setVolume(0, 0);

                }

            }
        });


        Relax_seek_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SeekBar seekBar = (SeekBar) view;
                int play_position = (media_Player.getDuration() / 100) * seekBar.getProgress();
                int play_position2=(mediaPlayer2.getDuration() / 100) * seekBar.getProgress();
                media_Player.seekTo(play_position);
                mediaPlayer2.seekTo(play_position2);
                current_time.setText(milliSecondsToTimer(media_Player.getCurrentPosition()));
                return false;

            }
        });
    }
    private void setPermission(){
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();

            }

        }).check();

    }

    public void downloadServerFiles(int index, String FolderPath, final int isFrom) {
        DropBoxManager.getInstance().connect();

        DropBoxManager.getInstance().startDownload(index,FolderPath,new IDownloadCallback() {
            @Override
            public void onComplete() {
                Log.e("onComplete", "true");
                ;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Your files successfully downloaded!", Toast.LENGTH_SHORT).show();
                        if(isFrom==1){
                            RelaxDownload1.clearAnimation();
                            RelaxDownload1.setImageResource(R.drawable.okicon);
                        }
                        if(isFrom==2){
                            RelaxDownload2.clearAnimation();
                            RelaxDownload2.setImageResource(R.drawable.okicon);
                        }
                        if(isFrom==3){
                            RelaxDownload3.clearAnimation();
                            RelaxDownload3.setImageResource(R.drawable.okicon);
                        }

                    }
                });
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void  prepareMediaPlayer(String path) {
        String completePath = DropBoxManager.getInstance().getFolderPath()+"/"+path;
        File file = new File(completePath);
        Uri myUri1 = Uri.fromFile(file);

        handler.removeCallbacks(updater);
        media_Player.pause();
        media_Player.stop();



        try {

            media_Player.reset();
            media_Player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            media_Player.setDataSource(getApplicationContext(), myUri1);
            media_Player.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        total_duration.setText(milliSecondsToTimer(media_Player.getDuration()));

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void prepareMediaPlayer2(String MusicFileName) {

        String completePath = DropBoxManager.getInstance().getFolderPath()+"/"+MusicFileName;
        File file = new File(completePath);
        Uri myUri1 = Uri.fromFile(file);

        handler.removeCallbacks(updater);
        mediaPlayer2.pause();
        mediaPlayer2.stop();

        try {


            mediaPlayer2.reset();
            mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer2.setDataSource(getApplicationContext(), myUri1);
            mediaPlayer2.prepare();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        total_duration.setText(milliSecondsToTimer(mediaPlayer2.getDuration()));

    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
            long currentDuration = media_Player.getCurrentPosition();
            current_time.setText(milliSecondsToTimer(currentDuration));
            total_duration.setText(milliSecondsToTimer(mediaPlayer2.getDuration()-mediaPlayer2.getCurrentPosition()));
        }
    };


    private void updateSeekbar() {
        if (media_Player.isPlaying()) {
            Relax_seek_bar.setProgress((int) (((float) media_Player.getCurrentPosition() / media_Player.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }


    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        //Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there


        // Pre appending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        media_Player.stop();
    }

    }

