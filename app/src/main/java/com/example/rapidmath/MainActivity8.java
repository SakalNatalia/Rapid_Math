package com.example.rapidmath;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.CountDownTimer;
import java.util.Random;

public class MainActivity8 extends AppCompatActivity implements ExampleDialog.ExampleDialogListener{
    private TextView taskTextView;
    private EditText answerEditText;
    private TextView scoreTextView;
    private Handler handler;
    private Runnable taskGeneratorRunnable;
    private int score;
    private int operand1;
    private int operand2;
    private long timeRemainingMillis; // Remaining time for each task in milliseconds
    private static final long TASK_TIME_MILLIS = 60000; // Initial time for each task in milliseconds (60 seconds)
    private TextView timerTextView;
    private CountDownTimer taskCountDownTimer;
    private int remainingSkips = 2;
    private int completedTasks = 0;

    private ImageButton pauseButton;
    private String feladat;

    //Zene hozzaadasa a jatekhoz
    MediaPlayer mediaPlayer;

    //Hangeffektusok beillesztese a jatekba
    private SoundPool soundPool;
    private int sound1, sound2;
    //private int sound1, sound2, sound3, sound4, sound5, sound6;
    private static final String PREF_CURRENT_LEVEL = "current_level";
    private int currentLevel=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        //Zene beillesztese
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.levelmusictest1);
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
        fetchDataFromServer();

        taskTextView = findViewById(R.id.tasktext);
        answerEditText = findViewById(R.id.answer);
        scoreTextView = findViewById(R.id.score);
        pauseButton = findViewById(R.id.floatingActionButton);
        timerTextView = findViewById(R.id.timer);

        // Initialize the timer
        timeRemainingMillis = TASK_TIME_MILLIS;
        startTaskCountdown();

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        // Initialize the Handler
        handler = new Handler();
        score = 0;
        generateTask();

        // Create a Runnable to generate the task every 60 seconds

        answerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed in this case
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Check the answer only when the length of entered text equals the length of the correct answer
                if (charSequence.length() == (String.valueOf(operand1 + operand2)).length()) {
                    checkAnswer();
                    generateTask(); // This generates a new task immediately
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed in this case
            }
        });

        /*----------------------------------------------------------------------------------------------*/

        //Pause gomb deklaralasa
        pauseButton = (ImageButton) findViewById(R.id.floatingActionButton);

        //Jelenitse meg a dialog-ot, amellyel a MainActivity-re (fomenure) mehetunk at
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NEM HASZNALT -> openPauseActivity();
                //Dialog megjelenitese
                openDialog();
                playSound();
            }
        });
    }

    /*----------------------------------------------------------------------------------------------*/

    private void startTaskCountdown() {
        taskCountDownTimer = new CountDownTimer(timeRemainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMillis = millisUntilFinished;
                updateTimerUI();
            }
            @Override
            public void onFinish() {
                handleTaskEnd();
            }
        }.start();
    }
    /*----------------------------------------------------------------------------------------------*/
    private void updateTimerUI() {
        // Update the TextView to display the remaining time
        long secondsRemaining = timeRemainingMillis / 1000;
        timerTextView.setText("Remaining Time: " + secondsRemaining + "s");
    }

    private void handleTaskEnd() {
        // Check if taskCountDownTimer is not null before canceling
        if (taskCountDownTimer != null) {
            taskCountDownTimer.cancel();
            taskCountDownTimer = null; // Set to null to indicate the timer is stopped
        }


        Intent intent = new Intent(MainActivity8.this, LevelFailed.class);
        startActivity(intent);
        finish(); // Optional: Finish the current activity to prevent going back to it
    }
    /*----------------------------------------------------------------------------------------------*/
    public void skipTask(View view) {
        playSound();
        // Check if there are remaining skips
        if (remainingSkips > 0) {
            // Decrement the remaining skips
            remainingSkips--;

            // Generate a new task immediately
            generateTask();
        } else {
            // Optionally, show a message or perform some action when the skip limit is reached
            // For example, you can display a Toast message
            Toast.makeText(this, "Skip limit reached", Toast.LENGTH_SHORT).show();
        }
    }
    /*----------------------------------------------------------------------------------------------*/
    private void generateTask() {
        // Implement your generic task generation logic here
        // For example, you can generate a task with two numbers between 1 and 10
        Random random = new Random();
        operand1 = random.nextInt(10) + 1;
        operand2 = random.nextInt(10) + 1;

        // Display the task in the TextView
        String task = operand1 + " + " + operand2;
        taskTextView.setText(task);
    }


    private void checkAnswer() {
        // Check if the user's answer is correct
        String userAnswerString = answerEditText.getText().toString().trim();

        if (!userAnswerString.isEmpty()) {
            int userAnswer;

            try {
                userAnswer = Integer.parseInt(userAnswerString);
            } catch (NumberFormatException exception) {
                // Handle the case where the input is not a valid integer
                System.err.println("Input is not a valid integer");
                return;
            }

            // Calculate the correct answer based on the current level (in this case, level 1)
            int correctAnswer = operand1 + operand2;

            // Check the answer and update the score
            if (userAnswer == correctAnswer) {
                score += 10;
            } else {
                score -= 5;
            }

            completedTasks++; // Increment completed tasks regardless of the correctness of the answer

            // Display the updated score
            scoreTextView.setText("Score: " + score);

            // Clear the answer EditText
            answerEditText.setText("");

            // Check if the completed task limit is reached
          /*  if (completedTasks >= 8) {
                startNextLevel();
            } else {
                // Generate a new task immediately if the completed task limit is not reached
                generateTask();
            }*/
        }
    }





    // Don't forget to stop the handler when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(taskGeneratorRunnable);
        //Allitsa le a hangeffektust akkor, amikor kilepunk a jatekszintbol
        soundPool.release();
        soundPool = null;
    }


    /*----------------------------------------------------------------------------------------------*/
    public void fetchDataFromServer() {
        String url = "https://a5f2-46-40-10-216.ngrok-free.app/agilis_web/database.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject row = response.getJSONObject(i);

                                feladat = row.getString("feladat");
                                Log.d("TAG", feladat);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        /* Toast.makeText(MainActivity8.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();*/
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    /*----------------------------------------------------------------------------------------------*/

    /*//NEM HASZNALT!
    public void openPauseActivity(){
        Intent pauseintent=new Intent(this,Pause.class);
        startActivity(pauseintent);
    }*/

    //Jelenitse meg a dialog-ot
    public void openDialog(){
        ExampleDialog dialog = new ExampleDialog();
        dialog.show(getSupportFragmentManager(), "example dialog");

    }

    //Ha Yes-t nyomunk a Dialog-ban, akkor dobjon at minket a fomenure
    @Override
    public void onYesClicked(){
        Intent pauseintent=new Intent(this,MainActivity.class);
        startActivity(pauseintent);
        playSound();

    }

    /*----------------------------------------------------------------------------------------------*/

    //Zene leallitasa amikor kilepunk a MainActivity8-bol (Jatekszintbol)
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

    /*----------------------------------------------------------------------------------------------*/


}