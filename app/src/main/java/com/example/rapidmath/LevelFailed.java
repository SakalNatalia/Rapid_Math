package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LevelFailed extends AppCompatActivity {

    private ImageButton backbutton;

    private ImageButton menubutton;

    //Zene hozzaadasa a jatekhoz
    MediaPlayer mediaPlayer;

    //Hangeffektusok beillesztese a jatekba
    private SoundPool soundPool;
    private int sound1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_failed);

        /*----------------------------------------------------------------------------------------------*/
        //Zene beillesztese
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.levelfailedmusictest1);
        //Zene ismetlese
        //mediaPlayer.setLooping(true);
        //Zene elinditasa
        mediaPlayer.start();
        /*----------------------------------------------------------------------------------------------*/

        //Hangeffektusok beillesztese
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    //.setMaxStreams(6)
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        //Hangeffektusok betoltese
        sound1 = soundPool.load(this, R.raw.clicksound, 1);

        /*----------------------------------------------------------------------------------------------*/


        backbutton = findViewById(R.id.tryagain);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hangeffektus lejatszasa
                playSound();
                openActivity8();
            }
        });

        menubutton = findViewById(R.id.SubmitYourScoreButton);
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hangeffektus lejatszasa
                playSound();
                openActivity1();
            }
        });

    }

    public void openActivity8() {
        Intent optionsintent = new Intent(this, MainActivity8.class);
        startActivity(optionsintent);
    }


    public void openActivity1() {
        Intent optionsintent2 = new Intent(this, MainActivity.class);
        startActivity(optionsintent2);
    }

    /*----------------------------------------------------------------------------------------------*/

    //Zene leallitasa amikor kilepunk
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    /*----------------------------------------------------------------------------------------------*/

    public void playSound() {
        soundPool.play(sound1, 1, 1, 0, 0, 1);
    }

    //Hangeffektusok leallitasa
    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    /*----------------------------------------------------------------------------------------------*/

}