package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LevelFailed extends AppCompatActivity {

    private ImageButton backbutton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_failed);

        backbutton= findViewById(R.id.tryagain);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity8();
            }
        });

    }

    public void openActivity8(){
        Intent optionsintent=new Intent(this,MainActivity8.class);
        startActivity(optionsintent);
    }
}