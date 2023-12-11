package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity {

    Thread timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        timer = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this){
                        wait(1500);
                    }
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(DeleteActivity.this, MainActivity.class);
                    i.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}