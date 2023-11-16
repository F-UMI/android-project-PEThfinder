package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import database.BoardDB;
import dto.Board;

public class EditActivity extends AppCompatActivity {


    private BoardDB boardDB;
    private List<Board> boardList;
    private BoardAdapter boardAdapter;

    private EditText editBoardUserName;
    private EditText editBoardTitle;
    private EditText editText;
    private Button editBoardBtn;

    private int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_board);
        boardDB = BoardDB.getInstance(this);
        boardList = boardDB.boardDao().getAll();
//
        boardAdapter = new BoardAdapter(this, boardList);

        editBoardTitle = findViewById(R.id.editBoardTitle);
        editBoardUserName = findViewById(R.id.editBoardUserId);
        editText = findViewById(R.id.editBoardText);
        editBoardBtn = findViewById(R.id.editBoardBtn);

        Intent intent = getIntent();

        editBoardTitle.setText(intent.getStringExtra("title"));
        editBoardUserName.setText(intent.getStringExtra("userName"));
        editText.setText(intent.getStringExtra("text"));
        position = intent.getIntExtra("position", 0);

        editBoardBtn.setOnClickListener(e -> {
            String title = editBoardTitle.getText().toString();
            String userName = editBoardUserName.getText().toString();
            String text = editText.getText().toString();
            String date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
            Board editBoard = new Board(boardList.get(position).getId(), title, userName, text, date);
            boardDB.boardDao().update(editBoard);
            boardAdapter.notifyDataSetChanged();
            Intent i = new Intent(EditActivity.this, ViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(i);
            finish();
        });

    }


}
