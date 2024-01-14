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

import android.annotation.SuppressLint;
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
import android.text.TextUtils;
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

public class MainActivity8 extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    private TextView taskTextView;
    private EditText answerEditText;
    private TextView scoreTextView;
    private Handler handler;
    private Runnable taskGeneratorRunnable;
    private int score;

    private int correctAnswer = 0;


    private int operand1 = 0;
    private int operand2 = 0;
    private long timeRemainingMillis;
    private static final long TASK_TIME_MILLIS = 60000; // 60 szekundum
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
    private int sound1;
    private int currentLevel = 1;


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        //Hangeffektusok betoltese
        sound1 = soundPool.load(this, R.raw.clicksound, 1);

        /*----------------------------------------------------------------------------------------------*/
        fetchDataFromServer();

        taskTextView = findViewById(R.id.tasktext);
        answerEditText = findViewById(R.id.answer);
        scoreTextView = findViewById(R.id.score);
        pauseButton = findViewById(R.id.floatingActionButton);
        timerTextView = findViewById(R.id.timer);

        timeRemainingMillis = TASK_TIME_MILLIS;
        startTaskCountdown();

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        handler = new Handler();
        score = 0;
        generateTaskAndCheckAnswer();


        answerEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (currentLevel == 1) {
                    if (charSequence.length() == (String.valueOf(operand1 + operand2)).length()) {
                        generateTaskAndCheckAnswer();
                    }
                }
                if (currentLevel == 2) {
                    if (charSequence.length() == (String.valueOf(operand1 - operand2)).length()) {
                        generateTaskAndCheckAnswer();
                    }
                }

                if (currentLevel == 3) {
                    if (charSequence.length() == (String.valueOf(operand1 * operand2)).length()) {
                        generateTaskAndCheckAnswer();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        /*----------------------------------------------------------------------------------------------*/

        //Pause gomb deklaralasa
        pauseButton = (ImageButton) findViewById(R.id.floatingActionButton);

        //Jelenitse meg a Dialog-ot, amellyel a MainActivity-re (fomenure) mehetunk at
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        long secondsRemaining = timeRemainingMillis / 1000;
        timerTextView.setText("Remaining Time: " + secondsRemaining + "s");
    }

    private void handleTaskEnd() {
        if (taskCountDownTimer != null) {
            taskCountDownTimer.cancel();
            taskCountDownTimer = null;
        }


        Intent intent = new Intent(MainActivity8.this, LevelFailed.class);
        startActivity(intent);
        finish(); //Activity befejezese
    }

    /*----------------------------------------------------------------------------------------------*/
    public void skipTask(View view) {
        playSound();

        if (remainingSkips > 0) {
            remainingSkips--;
            generateTaskAndCheckAnswer();
        } else {
            Toast.makeText(this, "Skip limit reached!", Toast.LENGTH_SHORT).show();
        }
    }

    /*----------------------------------------------------------------------------------------------*/
    private void generateTaskAndCheckAnswer() {

        Random random = new Random();
        //Log.d("answerEdit", "level: " + currentLevel);
        //Log.d("answerEdit", "completedTasks: " + completedTasks);

        switch (currentLevel) {
            case 1:
            default:
                generateAdditionTask(random);
                break;
            case 2:
                generateSubtractionTask(random);
                break;
            case 3:
                generateMultiplicationTask(random);
                break;
        }
    }

    private void generateAdditionTask(Random random) {
        correctAnswer = operand1 + operand2;

        operand1 = random.nextInt(10) + 1;
        operand2 = random.nextInt(10) + 1;
        taskTextView.setText(operand1 + " + " + operand2);

        String userAnswerString = answerEditText.getText().toString().trim();
        answerEditText.setText("");

        if (!TextUtils.isEmpty(userAnswerString)) {
            int userAnswer;

            try {
                userAnswer = Integer.parseInt(userAnswerString);
            } catch (NumberFormatException exception) {
                System.err.println("Input is not a valid integer");
                return;
            }
            //Log.d("answerEdit", "userInt: " + userAnswer);
            handleAnswer(userAnswer, correctAnswer);
            correctAnswer = 0;
        }


    }

    private void generateSubtractionTask(Random random) {
        correctAnswer = operand1 - operand2;
        operand1 = random.nextInt(10) + 1;
        operand2 = random.nextInt(10) + 1;

        if (operand1 < operand2) {
            int temp = operand1;
            operand1 = operand2;
            operand2 = temp;
        }

        taskTextView.setText(operand1 + " - " + operand2);
        String userAnswerString = answerEditText.getText().toString().trim();
        answerEditText.setText("");

        if (!TextUtils.isEmpty(userAnswerString)) {
            int userAnswer;

            try {
                userAnswer = Integer.parseInt(userAnswerString);
            } catch (NumberFormatException exception) {
                System.err.println("Input is not a valid integer");
                return;
            }
            handleAnswer(userAnswer, correctAnswer);
            correctAnswer = 0;
        }
    }

    private void generateMultiplicationTask(Random random) {
        // Helyes-e a valasz
        correctAnswer = operand1 * operand2;

        operand1 = random.nextInt(10) + 1;
        operand2 = random.nextInt(10) + 1;
        taskTextView.setText(operand1 + " * " + operand2);


        String userAnswerString = answerEditText.getText().toString().trim();
        answerEditText.setText("");
//Log.d("answerEdit", "userString: " + userAnswerString);
//Log.d("answerEdit", "correctAnswerAddition: " + correctAnswer);
        //scoreTextView.setText("Score: " + score);
        if (!TextUtils.isEmpty(userAnswerString)) {
            int userAnswer;

            try {
                userAnswer = Integer.parseInt(userAnswerString);
            } catch (NumberFormatException exception) {
                System.err.println("Input is not a valid integer");
                return;
            }
            //Log.d("answerEdit", "userInt: " + userAnswer);
            handleAnswer(userAnswer, correctAnswer);
            //correctAnswer = 0;
        }
    }


    private void generateSubtractionTask2(Random random) {
        operand1 = random.nextInt(10) + 1;
        operand2 = random.nextInt(10) + 1;

        // Hogy ne legyen negativ szam
        if (operand1 < operand2) {
            int temp = operand1;
            operand1 = operand2;
            operand2 = temp;
        }

        taskTextView.setText(operand1 + " - " + operand2);
        // Valasz ellenorzese
        int correctAnswerSubtraction = operand1 - operand2;
        Log.d("correctAnswerSubtraction", "value:" + correctAnswerSubtraction);
        Toast.makeText(MainActivity8.this, "correctAnswerSubtraction:" + correctAnswerSubtraction, Toast.LENGTH_SHORT).show();

        //Valasz
        handleCorrectAnswer(correctAnswerSubtraction);
    }


    private void handleAnswer(int userAnswer, int correctAnswer) {
        // Helyes / helytelen valasz
        if (userAnswer == correctAnswer) {
            score += 10;
        } else {
            score -= 5;
        }
//correctAnswer = 0;
        completedTasks++; // Elvegzett feladatok

        // Score kimutatasa
        scoreTextView.setText("Score: " + score);

        //Szintek szama
        //if (completedTasks >= 8) {
        if (completedTasks % 4 == 0) {
            startNextLevel();
        }
        //generateTaskAndCheckAnswer();
//        } else {
//            generateTaskAndCheckAnswer();
//        }

        generateTaskAndCheckAnswer();
    }

    private void handleCorrectAnswer(int correctAnswer) {
        String userAnswerString = answerEditText.getText().toString().trim();
        timerTextView.setText("Score: " + userAnswerString);

        if (!userAnswerString.isEmpty()) {
            int userAnswer;

            try {
                userAnswer = Integer.parseInt(userAnswerString);
            } catch (NumberFormatException exception) {
                System.err.println("Input is not a valid integer");
                return;
            }

            //answerEditText.setText("");
            handleAnswer(userAnswer, correctAnswer);
        }
    }


    private void startNextLevel() {
        //Kovetkezo szint
        currentLevel++;
        completedTasks = 0;

        if (currentLevel > 3) {
            currentLevel = 1;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(taskGeneratorRunnable);
        //Allitsa le a hangeffektust akkor, amikor kilepunk a jatekszintbol
        soundPool.release();
        soundPool = null;
        //Allitsa le az idozitot, amikor kilepunk a jatekszintbol
        if (taskCountDownTimer != null) {
            taskCountDownTimer.cancel();
        }
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
                        /* Toast.makeText(MainActivity8.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();*/
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    /*----------------------------------------------------------------------------------------------*/

    //Jelenitse meg a Dialog-ot
    public void openDialog() {
        ExampleDialog dialog = new ExampleDialog();
        dialog.show(getSupportFragmentManager(), "example dialog");

    }

    //Ha Yes-t nyomunk a Dialog-ban, akkor dobjon at minket a fomenure
    @Override
    public void onYesClicked() {
        Intent pauseintent = new Intent(this, MainActivity.class);
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
        //Allitsa le az idozitot, amikor kilepunk a jatekszintbol
        if (taskCountDownTimer != null) {
            taskCountDownTimer.cancel();
        }

    }

    /*----------------------------------------------------------------------------------------------*/

    public void playSound() {
        soundPool.play(sound1, 1, 1, 0, 0, 1);
    }

    /*----------------------------------------------------------------------------------------------*/


}