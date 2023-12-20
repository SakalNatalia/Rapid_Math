package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton playbutton;
    private ImageButton optionsbutton;
    private ImageButton helpbutton;
    private ImageButton highscorebutton;
    private ImageButton loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playbutton= (ImageButton) findViewById(R.id.play);
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity8();
            }
        });

        highscorebutton= (ImageButton) findViewById(R.id.highscore);
        highscorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5();
            }
        });

        helpbutton= (ImageButton) findViewById(R.id.help);
        helpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity7();
            }
        });

        optionsbutton= (ImageButton) findViewById(R.id.options);
        optionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity6();
            }
        });

        //loginbutton= (ImageButton) findViewById(R.id.login);
        //loginbutton.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //openActivity3();
            //}
        //});



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
}