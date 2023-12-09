package com.example.pethfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AppSplashActivity extends AppCompatActivity {

    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this){
                        wait(5000);
                    }
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(AppSplashActivity.this, MainActivity.class);
                    i.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}