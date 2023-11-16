package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import database.BoardDB;
import dto.Board;

public class ViewActivity extends AppCompatActivity {
    private int position;
    private int boardId;
    private TextView viewUserName;
    private TextView viewTitle;
    private TextView viewDate;
    private TextView viewText;

    private Button backBtn;
    private Button updateBtn;
    private Button deleteBtn;

    private BoardDB boardDB;
    private List<Board> boardList;
    private BoardAdapter boardAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_board);
        boardDB = BoardDB.getInstance(this);
        boardList = boardDB.boardDao().getAll();
//        Log.e("Alter", boardList.toString());
        boardAdapter = new BoardAdapter(this, boardList);

        viewTitle = findViewById(R.id.editTitle);
        viewUserName = findViewById(R.id.editUserName);
        viewDate = findViewById(R.id.viewDate);
        viewText = findViewById(R.id.editText);
        backBtn = findViewById(R.id.backBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        Intent intent = getIntent();

        viewUserName.setText(intent.getStringExtra("userName"));
        viewTitle.setText(intent.getStringExtra("title"));
        viewText.setText(intent.getStringExtra("text"));
        viewDate.setText(intent.getStringExtra("date"));
        boardId = Integer.parseInt(intent.getStringExtra("id"));
        position = intent.getIntExtra("position",0);

        backBtn.setOnClickListener(e -> {
            Intent i = new Intent(ViewActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        updateBtn.setOnClickListener(e -> {
            Intent i = new Intent(ViewActivity.this, EditActivity.class);
            startActivity(i);
            finish();
        });


        deleteBtn.setOnClickListener(e -> {
            boardList.remove(position);
            Log.e("position", String.valueOf(position));
            boardDB.boardDao().delete(new Board(boardId));
            boardAdapter.notifyDataSetChanged();
            boardAdapter.notifyItemRangeChanged(position, boardList.size());
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            Log.e("Delete Complete", String.valueOf(boardList.size()));
            finish();
        });

    }
}
