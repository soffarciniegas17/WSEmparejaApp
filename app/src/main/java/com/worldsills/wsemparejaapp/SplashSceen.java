package com.worldsills.wsemparejaapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashSceen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sceen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashSceen.this, Home.class);
                startActivity(intent);
            }
        }, 2000);
    }
}
