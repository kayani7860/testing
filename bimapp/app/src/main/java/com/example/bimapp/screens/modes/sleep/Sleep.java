package com.example.bimapp.screens.modes.sleep;

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
import com.dropbox.core.v2.teamlog.SmartSyncOptOutDetails;
import com.example.bimapp.Bimapp;
import com.example.bimapp.R;
import com.example.bimapp.interfaces.IDownloadCallback;
import com.example.bimapp.managers.DropBoxManager;
import com.example.bimapp.screens.home.HomePage;
import com.example.bimapp.screens.modes.relaxation.Relaxation;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Sleep extends AppCompatActivity {

    ImageView imageView, play_pauseImage, SleepDownload1,SleepDownload2,SleepDownload3, SleepPlay1, SleepPlay2, SleepPlay3;
    TextView current_time, total_duration, SleepTime1, SleepTime2, SleepTime3,SleepText;
    SeekBar Sleep_seek_bar;
    MediaPlayer media_Player, mediaPlayer2;
    Handler handler = new Handler();
    String audioUrl = "";
    Switch cSwitch;
    boolean istoogleon = true;

    String three_min, five_min, eight_min;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



   requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.sleep);
            getSupportActionBar().hide();

            setPermission();
            inIt();
            setListener();
            DownloadedFilesCheck();

            SleepText = (TextView) findViewById(R.id.textViewsleep);
        if(Bimapp.db.getLanguage().equals("sv")){
            SleepText.setText(R.string.Sleepsv);
        }

    }
    private void DownloadedFilesCheck() {
        if (Bimapp.db.getLanguage().equals("en")) {
            checkTenMin(DropBoxManager.getInstance().isFileExist("SLEEP 10 MINTal & Ljud.m4a") &&
                    DropBoxManager.getInstance().isFileExist("SLEEP 10 MIN TAL.m4a"));
        }
        if (Bimapp.db.getLanguage().equals("sv")) {
            checkTenMin(DropBoxManager.getInstance().isFileExist("SÖMN 10 MINTal & Ljud.m4a") &&
                    DropBoxManager.getInstance().isFileExist("SÖMN 10 MIN TAL.m4a"));
        }
        if (Bimapp.db.getLanguage().equals("en")) {
            checkTwentyMin(DropBoxManager.getInstance().isFileExist("SLEEP 20 MINTal & Ljud.m4a") &&
                    DropBoxManager.getInstance().isFileExist("SLEEP 20 MIN TAL.m4a"));
        }
        if (Bimapp.db.getLanguage().equals("sv")) {
            checkTwentyMin(DropBoxManager.getInstance().isFileExist("SÖMN 20 MINTal & Ljud.m4a") &&
                    DropBoxManager.getInstance().isFileExist("SÖMN 20 MIN TAL.m4a"));
        }
        if (Bimapp.db.getLanguage().equals("en")) {
            checkThirtyMin(DropBoxManager.getInstance().isFileExist("SLEEP 30 MINTal & Ljud.m4a") &&
                    DropBoxManager.getInstance().isFileExist("SLEEP 30 MIN TAL.m4a"));
        }
        if (Bimapp.db.getLanguage().equals("sv")) {
            checkThirtyMin(DropBoxManager.getInstance().isFileExist("SÖMN 30 MINTal & Ljud.m4a") &&
                    DropBoxManager.getInstance().isFileExist("SÖMN 30 MIN TAL.m4a"));
        }
    }
    private void checkTenMin(boolean isDownloaded)
    {
        if(isDownloaded){
            SleepDownload1.setImageResource(R.drawable.okicon);
        }
        else{
            SleepDownload1.setImageResource(R.drawable.iosdownload);
        }
    }
    private void checkTwentyMin(boolean isDownloaded)
    {
        if(isDownloaded){
            SleepDownload2.setImageResource(R.drawable.okicon);
        }
        else{
            SleepDownload2.setImageResource(R.drawable.iosdownload);
        }

    }
    private void checkThirtyMin(boolean isDownloaded)
    {
        if(isDownloaded){
            SleepDownload3.setImageResource(R.drawable.okicon);
        }
        else{
            SleepDownload3.setImageResource(R.drawable.iosdownload);
        }

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

    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){
        final Animation animFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        final Animation animBlink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);

        Sleep_seek_bar.setMax(100);
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



        Sleep_seek_bar.setOnTouchListener(new View.OnTouchListener() {
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
        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                Intent intent = new Intent(Sleep.this, HomePage.class);
                startActivity(intent);
                media_Player.stop();
                mediaPlayer2.stop();

            }
        });

        SleepPlay1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                SleepPlay1.startAnimation(animFade);
                SleepPlay2.clearAnimation();
                SleepPlay3.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){

                    if(DropBoxManager.getInstance().isFileExist("SLEEP 10 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SLEEP 10 MIN TAL.m4a")) {
                        prepareMediaPlayer("SLEEP 10 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("SLEEP 10 MIN TAL.m4a");
                        System.out.println("media players prepared");
                        SleepTime1.setTypeface(null,Typeface.BOLD);
                        SleepTime2.setTextAppearance(R.style.TimeTextSleep);
                        SleepTime3.setTextAppearance(R.style.TimeTextSleep);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{

                    if(DropBoxManager.getInstance().isFileExist("SÖMN 10 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SÖMN 10 MIN TAL.m4a")) {

                        prepareMediaPlayer("SÖMN 10 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("SÖMN 10 MIN TAL.m4a");

                        SleepTime1.setTypeface(null,Typeface.BOLD);
                        SleepTime2.setTextAppearance(R.style.TimeTextSleep);
                        SleepTime3.setTextAppearance(R.style.TimeTextSleep);

                        play_pauseImage.setImageResource(R.drawable.ic_play);

                        play_pauseImage.setVisibility(View.VISIBLE);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }
            }
        });
        SleepPlay2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                SleepPlay2.startAnimation(animFade);
                SleepPlay1.clearAnimation();
                SleepPlay3.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("SLEEP 20 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SLEEP 20 MIN TAL.m4a")) {
                        prepareMediaPlayer("SLEEP 20 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("SLEEP 20 MIN TAL.m4a");
                        SleepTime2.setTypeface(null,Typeface.BOLD);
                        SleepTime1.setTextAppearance(R.style.TimeTextSleep);
                        SleepTime3.setTextAppearance(R.style.TimeTextSleep);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    if(DropBoxManager.getInstance().isFileExist("SÖMN 20 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SÖMN 20 MIN TAL.m4a")) {
                        prepareMediaPlayer("SÖMN 20 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("SÖMN 20 MIN TAL.m4a");
                        SleepTime2.setTypeface(null,Typeface.BOLD);
                        SleepTime1.setTextAppearance(R.style.TimeTextSleep);
                        SleepTime3.setTextAppearance(R.style.TimeTextSleep);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }
            }
        });
        SleepPlay3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                SleepPlay3.startAnimation(animFade);
                SleepPlay2.clearAnimation();
                SleepPlay1.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){

                    if(DropBoxManager.getInstance().isFileExist("SLEEP 30 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SLEEP 30 MIN TAL.m4a")) {
                        prepareMediaPlayer("SLEEP 30 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("SLEEP 30 MIN TAL.m4a");
                        SleepTime3.setTypeface(null,Typeface.BOLD);
                        SleepTime1.setTextAppearance(R.style.TimeTextSleep);
                        SleepTime2.setTextAppearance(R.style.TimeTextSleep);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    if(DropBoxManager.getInstance().isFileExist("SÖMN 30 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SÖMN 30 MIN TAL.m4a")) {
                        prepareMediaPlayer("SÖMN 30 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("SÖMN 30 MIN TAL.m4a");
                        SleepTime3.setTypeface(null,Typeface.BOLD);
                        SleepTime1.setTextAppearance(R.style.TimeTextSleep);
                        SleepTime2.setTextAppearance(R.style.TimeTextSleep);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }
            }
        });

        SleepDownload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("SLEEP 10 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SLEEP 10 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        SleepDownload1.startAnimation(animBlink);
                        downloadServerFiles(11, "Tal & Ljud m4a",1);
                        downloadServerFiles(16, "Tal M4a",1);

                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("SÖMN 10 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SÖMN 10 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        SleepDownload1.startAnimation(animBlink);
                        downloadServerFiles(15, "Tal & Ljud m4a",1);
                        downloadServerFiles(17, "Tal M4a",1);

                    }

                }
            }
        });
        SleepDownload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    //Bimapp.getGenericContext().getApplicationContext().
                    if(DropBoxManager.getInstance().isFileExist("SLEEP 20 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SLEEP 20 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        SleepDownload2.startAnimation(animBlink);
                        downloadServerFiles(15, "Tal & Ljud m4a",2);
                        downloadServerFiles(10, "Tal M4a",2);

                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("SÖMN 20 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SÖMN 20 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        SleepDownload2.startAnimation(animBlink);
                        downloadServerFiles(9, "Tal & Ljud m4a",2);
                        downloadServerFiles(12, "Tal M4a",2);

                    }

                }
            }
        });
        SleepDownload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("SLEEP 30 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SLEEP 30 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        SleepDownload3.startAnimation(animBlink);
                        downloadServerFiles(6, "Tal & Ljud m4a",3);
                        downloadServerFiles(11, "Tal M4a",3);

                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("SÖMN 30 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("SÖMN 30 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        SleepDownload3.startAnimation(animBlink);
                        downloadServerFiles(8, "Tal & Ljud m4a",3);
                        downloadServerFiles(5, "Tal M4a",3);

                    }

                }
            }
        });
    }

    private void inIt(){

        imageView = (ImageView) findViewById(R.id.backSleep);
        play_pauseImage = findViewById(R.id.play_pauseImage);
        SleepDownload1 = findViewById(R.id.sleepdownload1);
        SleepDownload2 = findViewById(R.id.sleepdownload2);
        SleepDownload3 = findViewById(R.id.sleepdownload3);
        current_time = findViewById(R.id.current_time);
        total_duration = findViewById(R.id.total_duration);
        Sleep_seek_bar = findViewById(R.id.sleep_seek_bar);
        //Sleep play view
        SleepPlay1 = findViewById(R.id.SleepPlay1);
        SleepPlay2 = findViewById(R.id.SleepPlay2);
        SleepPlay3 = findViewById(R.id.SleepPlay3);
        //Sleep Time View
        SleepTime1 = findViewById(R.id.sleeptime1);
        SleepTime2 = findViewById(R.id.sleeptime2);
        SleepTime3 = findViewById(R.id.sleeptime3);
        media_Player = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();
        cSwitch = findViewById(R.id.switchS);
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
                            SleepDownload1.clearAnimation();
                            SleepDownload1.setImageResource(R.drawable.okicon);
                        }
                        if(isFrom==2){
                            SleepDownload2.clearAnimation();
                            SleepDownload2.setImageResource(R.drawable.okicon);
                        }
                        if(isFrom==3){
                            SleepDownload3.clearAnimation();
                            SleepDownload3.setImageResource(R.drawable.okicon);
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
            Sleep_seek_bar.setProgress((int) (((float) media_Player.getCurrentPosition() / media_Player.getDuration()) * 100));
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

