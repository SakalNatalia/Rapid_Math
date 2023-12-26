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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity8 extends AppCompatActivity {

    //tesztkent a PauseGomb megnyomasakor dobjon at a Pause Activity-re
    private ImageButton pauseButton;
    private String feladat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
        fetchDataFromServer();

        //pause gomb deklaralasa
        pauseButton = (ImageButton) findViewById(R.id.floatingActionButton);

        //dobjon at a Pause Activity-re openPauseActivity-vel
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPauseActivity();
            }
        });
    }
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
                        Toast.makeText(MainActivity8.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    //fuggveny, hogy atdobjon a Pause Activity-re
    public void openPauseActivity(){
        Intent pauseintent=new Intent(this,Pause.class);
        startActivity(pauseintent);
    }



}