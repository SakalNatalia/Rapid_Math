package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity7 extends AppCompatActivity {
    private ImageButton backhelp;

    private SoundPool soundPool;

    //Hangeffektusok beillesztese a jatekba
    private int sound1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        /*----------------------------------------------------------------------------------------------*/

        //Hangeffektusok beillesztese
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    //.setMaxStreams(6)
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }else{
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        //Hangeffektusok betoltese
        sound1 = soundPool.load(this, R.raw.clicksound, 1);


        /*----------------------------------------------------------------------------------------------*/


        backhelp= (ImageButton) findViewById(R.id.backhelp);
        backhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                openActivityMain4();
            }
        });
    }


    public void openActivityMain4(){
        Intent backintent=new Intent(this,MainActivity.class);
        startActivity(backintent);
    }

    /*----------------------------------------------------------------------------------------------*/
    public void playSound() {
        soundPool.play(sound1, 1, 1, 0, 0, 1);
    }

    //Hangeffektusok leallitasa
    @Override
    protected void onDestroy(){
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    /*----------------------------------------------------------------------------------------------*/
}