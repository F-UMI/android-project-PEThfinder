package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

import dao.BoardDao;
import database.BoardDB;
import dto.Board;

public class ViewActivity extends AppCompatActivity {
    private int position;
    private TextView viewId;
    private TextView viewUserName;
    private TextView viewTitle;
    private TextView viewDate;
    private TextView viewText;

    private Button backBtn;
    private Button updateBtn;
    private Button deleteBtn;

    private BoardDB boardDB;
    private List<Board> boardList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_board);
        boardDB = Room.databaseBuilder(this, BoardDB.class, "board").allowMainThreadQueries().build();
        boardList = boardDB.boardDao().getAll();

        viewTitle = findViewById(R.id.viewTitle);
        viewUserName = findViewById(R.id.viewUserName);
        viewDate = findViewById(R.id.viewDate);
        viewText = findViewById(R.id.viewText);
        backBtn = findViewById(R.id.backBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        Intent intent = getIntent();

        viewUserName.setText(intent.getStringExtra("userName"));
        viewTitle.setText(intent.getStringExtra("title"));
        viewText.setText(intent.getStringExtra("text"));
        viewDate.setText(intent.getStringExtra("date"));
        position = intent.getIntExtra("position", 0);

        backBtn.setOnClickListener(e ->{
            Intent i = new Intent(ViewActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        deleteBtn.setOnClickListener(e -> {
/*            boardDB.boardDao().delete(boardList.get(position));
            boardList.remove(position);
            notifyItemRangeChanged(position, items.size());*/
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

            finish();
        });


    }

}
