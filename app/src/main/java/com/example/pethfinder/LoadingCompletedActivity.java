package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingCompletedActivity extends AppCompatActivity {

    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        timer = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this){
                        wait(1200);
                    }
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(LoadingCompletedActivity.this, MainActivity.class);
                    i.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}