package com.example.literaryclubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);

                if (preferences.contains("isUser")) {
                    startActivity(new Intent(getApplicationContext(),Bottom.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), Sign.class));
                    finish();
                }

            }
        }, 1000);
    }
}
