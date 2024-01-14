package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity3 extends AppCompatActivity {
    private ImageButton backxbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        backxbutton = (ImageButton) findViewById(R.id.backx);
        backxbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivityMain();
            }
        });

    }

    public void openActivityMain() {
        Intent backintent = new Intent(this, MainActivity.class);
        startActivity(backintent);
    }
}