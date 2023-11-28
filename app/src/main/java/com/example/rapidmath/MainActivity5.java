package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity5 extends AppCompatActivity {
    private ImageButton xbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        xbutton= (ImageButton) findViewById(R.id.xbutton);
        xbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivityMain2();
            }
        });
    }
    public void openActivityMain2(){
        Intent backintent=new Intent(this,MainActivity.class);
        startActivity(backintent);
    }
}