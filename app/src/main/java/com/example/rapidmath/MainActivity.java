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

public class MainActivity extends AppCompatActivity {

    private ImageButton playbutton;
    private ImageButton optionsbutton;
    private ImageButton helpbutton;
    private ImageButton highscorebutton;
    private ImageButton loginbutton;

    //Zene hozzaadasa a jatekhoz
    MediaPlayer mediaPlayer;

    //Hangeffektusok beillesztese a jatekba
    private SoundPool soundPool;
    private int sound1, sound2;
    //private int sound1, sound2, sound3, sound4, sound5, sound6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*----------------------------------------------------------------------------------------------*/
        //Zene beillesztese
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.menumusictest1);
        //Zene elinditasa
        mediaPlayer.start();
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
            //soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        //Hangeffektusok betoltese
        sound1 = soundPool.load(this, R.raw.clicksound, 1);
        //sound2 = soundPool.load(this, R.raw.sound1, 1);
        //sound3 = soundPool.load(this, R.raw.sound3, 1);
        //sound4 = soundPool.load(this, R.raw.sound4, 1);
        //sound5 = soundPool.load(this, R.raw.sound5, 1);
        //sound6 = soundPool.load(this, R.raw.sound6, 1);


        /*----------------------------------------------------------------------------------------------*/


        playbutton= (ImageButton) findViewById(R.id.play);
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hangeffektus lejatszasa
                playSound();
                openActivity8();
            }
        });

        highscorebutton= (ImageButton) findViewById(R.id.highscore);
        highscorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hangeffektus lejatszasa
                playSound();
                openActivity5();
            }
        });

        helpbutton= (ImageButton) findViewById(R.id.help);
        helpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hangeffektus lejatszasa
                playSound();
                openActivity7();
            }
        });

        optionsbutton= (ImageButton) findViewById(R.id.options);
        optionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hangeffektus lejatszasa
                playSound();
                openActivity6();
            }
        });

    }
    public void openActivity8(){
        Intent playintent=new Intent(this,MainActivity8.class);
        startActivity(playintent);
    }

    public void openActivity3(){
        Intent loginintent=new Intent(this,MainActivity3.class);
        startActivity(loginintent);
    }

    public void openActivity5(){
        Intent highscoreintent=new Intent(this,MainActivity5.class);
        startActivity(highscoreintent);
    }

    public void openActivity7(){
        Intent helpintent=new Intent(this,MainActivity7.class);
        startActivity(helpintent);
    }
    public void openActivity6(){
        Intent optionsintent=new Intent(this,MainActivity6.class);
        startActivity(optionsintent);
    }

    /*----------------------------------------------------------------------------------------------*/

    //Zene leallitasa amikor kilepunk a MainActivity-bol (Main Menu-bol)
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    /*public void playMusic(View v){
        if (mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(this, R.raw.testsong);
        }

        mediaPlayer.start();
    }*/

    /*----------------------------------------------------------------------------------------------*/

    //Hangeffektus elinditasa
    /*public void playSound(View v){
        int id = v.getId();
        if (id == R.id.play) {
            soundPool.play(sound1, 1, 1, 0, 0, 1);
        } else if (id == R.id.options) {
            soundPool.play(sound2, 1, 1, 0, 0, 1);
        }
    }*/

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