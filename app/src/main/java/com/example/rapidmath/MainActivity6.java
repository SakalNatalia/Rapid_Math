package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity6 extends AppCompatActivity {
    private ImageButton backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        backbutton= (ImageButton) findViewById(R.id.backoptions);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivityMain3();
            }
        });

    }
    public void openActivityMain3(){
        Intent backintent=new Intent(this,MainActivity.class);
        startActivity(backintent);
    }

}