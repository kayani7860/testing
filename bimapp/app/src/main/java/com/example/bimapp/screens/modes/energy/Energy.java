package com.example.bimapp.screens.modes.energy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
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
import com.dropbox.core.DbxException;
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
import com.thekhaeng.pushdownanim.PushDownAnim;

import static android.os.Environment.DIRECTORY_MUSIC;

import java.io.File;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Energy extends AppCompatActivity {

    ImageView imageView, play_pauseImage, energyDownload1,energyDownload2,energyDownload3, EnergyPlay1, EnergyPlay2, EnergyPlay3;
    TextView current_time, total_duration,remaining_duration, EnergyTime1, EnergyTime2, EnergyTime3,EnergyText;
    SeekBar energy_seek_bar;
    MediaPlayer media_Player, mediaPlayer2;
    Handler handler = new Handler();
    String audioUrl = "";
    Switch aSwitch;
    boolean istoogleon = true;
    //static int MediaIndex=5;
    String three_min, five_min, eight_min;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.energy);
        getSupportActionBar().hide();

        setPermission();
        inIt();
        setListener();
        DownloadedFilesCheck();

        EnergyText = (TextView) findViewById(R.id.textViewenergy);

        if(Bimapp.db.getLanguage().equals("sv")){
            EnergyText.setText(R.string.Energisv);
        }


    }
    private void DownloadedFilesCheck(){
        if(Bimapp.db.getLanguage().equals("en")) {
            checkThreeMin(DropBoxManager.getInstance().isFileExist("ENERGY 3 MINTal & Ljud.m4a") &&
                                      DropBoxManager.getInstance().isFileExist("ENERGY 3 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("sv")) {
            checkThreeMin(DropBoxManager.getInstance().isFileExist("ENERGI 3 MINTal & Ljud.m4a") &&
                                       DropBoxManager.getInstance().isFileExist("ENERGI 3 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("en")) {
            checkFiveMin(DropBoxManager.getInstance().isFileExist("ENERGY 5 MINTal & Ljud.m4a") &&
                                      DropBoxManager.getInstance().isFileExist("ENERGY 5 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("sv")) {
            checkFiveMin(DropBoxManager.getInstance().isFileExist("ENERGI 5 MINTal & Ljud.m4a") &&
                                      DropBoxManager.getInstance().isFileExist("ENERGI 5 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("en")) {
            checkEightMin(DropBoxManager.getInstance().isFileExist("ENERGY 8 MINTal & Ljud.m4a") &&
                                       DropBoxManager.getInstance().isFileExist("ENERGY 8 MIN TAL.m4a"));
        }
        if(Bimapp.db.getLanguage().equals("sv")) {
            checkEightMin(DropBoxManager.getInstance().isFileExist("ENERGI 8 MINTal & Ljud.m4a") &&
                                       DropBoxManager.getInstance().isFileExist("ENERGI 8 MIN TAL.m4a"));
        }
    }

    private void checkThreeMin(boolean isDownloaded)
    {
        if(isDownloaded){
            energyDownload1.setImageResource(R.drawable.okicon);
        }
        else{
            energyDownload1.setImageResource(R.drawable.iosdownload);
        }
    }
    private void checkFiveMin(boolean isDownloaded)
    {
        if(isDownloaded){
            energyDownload2.setImageResource(R.drawable.okicon);
        }
        else{
            energyDownload2.setImageResource(R.drawable.iosdownload);
        }

    }
    private void checkEightMin(boolean isDownloaded)
    {
        if(isDownloaded){
            energyDownload3.setImageResource(R.drawable.okicon);
        }
        else{
            energyDownload3.setImageResource(R.drawable.iosdownload);
        }

    }


    public void downloadServerFiles(int index, String FolderPath, final int isFrom) {
       // final Animation animBlink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        DropBoxManager.getInstance().connect();
        DropBoxManager.getInstance().startDownload(index,FolderPath,new IDownloadCallback() {
            @Override
            public void onComplete() {
                Log.e("onComplete", "true");
              ;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(getApplicationContext(), "Your files successfully downloaded!", Toast.LENGTH_SHORT).show();
                            if(isFrom==1){
                                energyDownload1.clearAnimation();
                                energyDownload1.setImageResource(R.drawable.okicon);


                            }
                            if(isFrom==2){
                                energyDownload2.clearAnimation();
                                energyDownload2.setImageResource(R.drawable.okicon);
                            }
                            if(isFrom==3){
                                energyDownload3.clearAnimation();
                                energyDownload3.setImageResource(R.drawable.okicon);
                            }


                        }
                    });
            }

        });


    }
    private void setPermission(){
        Dexter.withContext(this).withPermissions(
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
    private void inIt()
    {
        imageView = (ImageView) findViewById(R.id.backEnergy);
        play_pauseImage = findViewById(R.id.play_pauseImage);

        energyDownload1 = findViewById(R.id.energydownload1);

        energyDownload2 = findViewById(R.id.energydownload2);

        energyDownload3 = findViewById(R.id.energydownload3);

        current_time = findViewById(R.id.current_time);
        total_duration = findViewById(R.id.total_duration);
        energy_seek_bar = findViewById(R.id.energy_seek_bar);
        //Energy play view
        EnergyPlay1 = findViewById(R.id.energyplay1);
        EnergyPlay2 = findViewById(R.id.energyplay2);
        EnergyPlay3 = findViewById(R.id.energyplay3);
        //Energy Time View
        EnergyTime1 = findViewById(R.id.energytime1);
        EnergyTime2 = findViewById(R.id.energytime2);
        EnergyTime3 = findViewById(R.id.energytime3);
        media_Player = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();
        aSwitch = findViewById(R.id.switchE);

    }
    @SuppressLint("ClickableViewAccessibility")
    private void setListener(){
        final Animation animFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        final Animation animBlink = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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




        energy_seek_bar.setMax(100);
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


        energy_seek_bar.setOnTouchListener(new View.OnTouchListener() {
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(Energy.this, HomePage.class);
                startActivity(intent);
                media_Player.stop();
                mediaPlayer2.stop();

            }
        });


        EnergyPlay1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                EnergyPlay1.startAnimation(animFade);
                EnergyPlay2.clearAnimation();
                EnergyPlay3.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("ENERGY 3 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGY 3 MIN TAL.m4a")) {
                        prepareMediaPlayer("ENERGY 3 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("ENERGY 3 MIN TAL.m4a");
                        EnergyTime1.setTypeface(null,Typeface.BOLD);
                        EnergyTime2.setTextAppearance(R.style.TimeTextEnergy);
                        EnergyTime3.setTextAppearance(R.style.TimeTextEnergy);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    if(DropBoxManager.getInstance().isFileExist("ENERGI 3 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGI 3 MIN TAL.m4a")) {
                        prepareMediaPlayer("ENERGI 3 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("ENERGI 3 MIN TAL.m4a");
                        EnergyTime1.setTypeface(null,Typeface.BOLD);
                        EnergyTime2.setTextAppearance(R.style.TimeTextEnergy);
                        EnergyTime3.setTextAppearance(R.style.TimeTextEnergy);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }
            }
        });
        EnergyPlay2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                EnergyPlay2.startAnimation(animFade);
                EnergyPlay1.clearAnimation();
                EnergyPlay3.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("ENERGY 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGY 5 MIN TAL.m4a"))
                    {
                        prepareMediaPlayer("ENERGY 5 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("ENERGY 5 MIN TAL.m4a");
                        EnergyTime2.setTypeface(null,Typeface.BOLD);
                        EnergyTime1.setTextAppearance(R.style.TimeTextEnergy);
                        EnergyTime3.setTextAppearance(R.style.TimeTextEnergy);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    if(DropBoxManager.getInstance().isFileExist("ENERGI 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGI 5 MIN TAL.m4a"))
                    {
                        prepareMediaPlayer("ENERGI 5 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("ENERGI 5 MIN TAL.m4a");
                        EnergyTime2.setTypeface(null,Typeface.BOLD);
                        EnergyTime1.setTextAppearance(R.style.TimeTextEnergy);
                        EnergyTime3.setTextAppearance(R.style.TimeTextEnergy);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Download Files First!",Toast.LENGTH_SHORT);
                    }

                }

            }
        });




        EnergyPlay3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                EnergyPlay3.startAnimation(animFade);
                EnergyPlay2.clearAnimation();
                EnergyPlay1.clearAnimation();
                if(Bimapp.db.getLanguage().equals("en")) {
                    if (DropBoxManager.getInstance().isFileExist("ENERGY 8 MINTal & Ljud.m4a") &&
                            DropBoxManager.getInstance().isFileExist("ENERGY 8 MIN TAL.m4a")) {
                        prepareMediaPlayer("ENERGY 8 MINTal & Ljud.m4a");
                        prepareMediaPlayer2("ENERGY 8 MIN TAL.m4a");
                        EnergyTime3.setTypeface(null, Typeface.BOLD);
                        EnergyTime1.setTextAppearance(R.style.TimeTextEnergy);
                        EnergyTime2.setTextAppearance(R.style.TimeTextEnergy);
                        play_pauseImage.setImageResource(R.drawable.ic_play);
                        play_pauseImage.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Download Files First!", Toast.LENGTH_SHORT);
                    }
                }
                else{if (DropBoxManager.getInstance().isFileExist("ENERGI 8 MINTal & Ljud.m4a") &&
                        DropBoxManager.getInstance().isFileExist("ENERGI 8 MIN TAL.m4a")) {
                    prepareMediaPlayer("ENERGI 8 MINTal & Ljud.m4a");
                    prepareMediaPlayer2("ENERGI 8 MIN TAL.m4a");
                    EnergyTime3.setTypeface(null, Typeface.BOLD);
                    EnergyTime1.setTextAppearance(R.style.TimeTextEnergy);
                    EnergyTime2.setTextAppearance(R.style.TimeTextEnergy);
                    play_pauseImage.setImageResource(R.drawable.ic_play);
                    play_pauseImage.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Download Files First!", Toast.LENGTH_SHORT);
                }

                }
            }
        });

        energyDownload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("ENERGY 3 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGY 3 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        energyDownload1.startAnimation(animBlink);
                        downloadServerFiles(4, "Tal & Ljud m4a",1);
                        downloadServerFiles(7, "Tal M4a",1);
                       // energyDownload1.setImageResource(R.drawable.okicon);
                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("ENERGI 3 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGI 3 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        energyDownload1.startAnimation(animBlink);
                        downloadServerFiles(0, "Tal & Ljud m4a",1);
                        downloadServerFiles(4, "Tal M4a",1);
                        //energyDownload1.setImageResource(R.drawable.okicon);
                    }

                }
            }

        });

        energyDownload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("ENERGY 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGY 5 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        downloadServerFiles(1, "Tal & Ljud m4a",2);
                        downloadServerFiles(13, "Tal M4a",2);
                        energyDownload2.startAnimation(animBlink);
                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("ENERGI 5 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGI 5 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        energyDownload2.startAnimation(animBlink);
                        downloadServerFiles(17, "Tal & Ljud m4a",2);
                        downloadServerFiles(15, "Tal M4a",2);
                    }

                }
            }
        });

        energyDownload3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bimapp.db.getLanguage().equals("en")){
                    if(DropBoxManager.getInstance().isFileExist("ENERGY 8 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGY 8 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        energyDownload3.startAnimation(animBlink);
                        downloadServerFiles(2, "Tal & Ljud m4a",3);
                        downloadServerFiles(1, "Tal M4a",3);

                    }
                }
                else {
                    if(DropBoxManager.getInstance().isFileExist("ENERGI 8 MINTal & Ljud.m4a")&&
                            DropBoxManager.getInstance().isFileExist("ENERGI 8 MIN TAL.m4a"))
                    {
                        System.out.println("Files Already Downloaded!!");
                    }
                    else {
                        energyDownload3.startAnimation(animBlink);
                        downloadServerFiles(14, "Tal & Ljud m4a",3);
                        downloadServerFiles(14, "Tal M4a",3);
                    }

                }
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
            energy_seek_bar.setProgress((int) (((float) media_Player.getCurrentPosition() / media_Player.getDuration()) * 100));
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

    private void loadAudio(){


        String completePath = DropBoxManager.getInstance().getFolderPath()+"/"+"ENERGY 3 MIN TAL.m4a";

        File file = new File(completePath);

        Uri myUri1 = Uri.fromFile(file);
        mediaPlayer2 = new MediaPlayer();
        mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer2.setDataSource(getApplicationContext(), myUri1);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer2.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mediaPlayer2.start();
    }

}

