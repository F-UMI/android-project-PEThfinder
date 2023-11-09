package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import database.BoardDB;

public class ViewActivity extends AppCompatActivity {
    private TextView viewId;
    private TextView viewUserName;
    private TextView viewTitle;
    private TextView viewDate;
    private TextView viewText;

    private Button backBtn;
    private Button updateBtn;
    private Button deleteBtn;

    private BoardDB boardDB;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_board);
        viewId = findViewById(R.id.viewId);
        TextView viewTitle = findViewById(R.id.viewTitle);
        backBtn = findViewById(R.id.backBtn);
        deleteBtn = findViewById(R.id.deleteBtn);


        backBtn.setOnClickListener(e ->{
            Intent i = new Intent(ViewActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
        deleteBtn.setOnClickListener(e -> {
            boardDB.boardDao().deleteAll();
        });

        Intent intent = getIntent();
        viewId.setText(intent.getStringExtra("id"));
        viewTitle.setText(intent.getStringExtra("title"));

    }

}
