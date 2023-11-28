package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity7 extends AppCompatActivity {
    private ImageButton backhelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        backhelp= (ImageButton) findViewById(R.id.backhelp);
        backhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivityMain4();
            }
        });
    }
    public void openActivityMain4(){
        Intent backintent=new Intent(this,MainActivity.class);
        startActivity(backintent);
    }
}