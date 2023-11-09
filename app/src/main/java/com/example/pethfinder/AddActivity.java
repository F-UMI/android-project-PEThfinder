package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import database.BoardDB;
import dto.Board;

public class AddActivity extends AppCompatActivity {
    private BoardDB boardDb;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        boardDb = BoardDB.getInstance(this);
        date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
        EditText addTitle = (EditText) findViewById(R.id.addTitle);
        EditText addUserName = (EditText) findViewById(R.id.addUserName);
        final Runnable addRunnable = () -> {
            Board newBoard = new Board();
            newBoard.setTitle(addTitle.getText().toString());
            newBoard.setUserName(addUserName.getText().toString());
            newBoard.setDate(date);
            boardDb.boardDao().insert(newBoard);
        };

        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(v -> {
            Thread addThread = new Thread(addRunnable);
            addThread.start();
            Intent i = new Intent(AddActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        BoardDB.destroyInstance();
        super.onDestroy();
    }
}



