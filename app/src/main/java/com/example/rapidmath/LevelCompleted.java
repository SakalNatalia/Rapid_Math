package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LevelCompleted extends AppCompatActivity {
    private ImageButton nextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_completed);

        nextbutton= findViewById(R.id.nextlevel);
        nextbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start MainActivity for the next level
                    Intent intent = new Intent(LevelCompleted.this, MainActivity8.class);
                    startActivity(intent);
                }
        });
    }

}