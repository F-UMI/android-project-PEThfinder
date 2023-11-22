package com.example.pethfinder;

import android.content.Intent;
import android.media.effect.Effect;
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

    private EditText addTitle;
    private EditText addUserName;

    private EditText addPassword;
    private EditText addText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        boardDb = BoardDB.getInstance(this);
        date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
        addTitle = findViewById(R.id.addTitle);
        addUserName = findViewById(R.id.addUserName);
        addPassword = findViewById(R.id.addPassword);
        addText = findViewById(R.id.addText);
        final Runnable addRunnable = () -> {
            Board newBoard = new Board();
            newBoard.setTitle(addTitle.getText().toString());
            newBoard.setUserName(addUserName.getText().toString());
            newBoard.setPassword(addPassword.getText().toString());
            newBoard.setText(addText.getText().toString());
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



