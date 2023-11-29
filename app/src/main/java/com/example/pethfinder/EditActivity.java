package com.example.pethfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import database.BoardDB;
import dto.BoardDto;

public class EditActivity extends AppCompatActivity {


    private BoardDB boardDB;
    private List<BoardDto> boardDtoList;
    private BoardAdapter boardAdapter;

    private EditText editBoardUserName;
    private EditText editBoardTitle;

    private EditText editPassword;
    private EditText editText;
    private ImageView editImageView;
    private Button editBoardBtn;

    private int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_board);
        boardDB = BoardDB.getInstance(this);
        boardDtoList = boardDB.boardDao().getAll();
//
        boardAdapter = new BoardAdapter(this, boardDtoList);

        editBoardTitle = findViewById(R.id.editBoardTitle);
        editBoardUserName = findViewById(R.id.editBoardUserId);
        editPassword = findViewById(R.id.editPassword);
        editText = findViewById(R.id.editBoardText);
        editBoardBtn = findViewById(R.id.editBoardBtn);
        editImageView = findViewById(R.id.editImageView);

        Intent intent = getIntent();

        editBoardTitle.setText(intent.getStringExtra("title"));
        editBoardUserName.setText(intent.getStringExtra("userName"));
        editPassword.setText(intent.getStringExtra("password"));
        editText.setText(intent.getStringExtra("text"));
        editImageView.setImageURI(Uri.parse(intent.getStringExtra("imagePath")));
        position = intent.getIntExtra("position", 0);


        editBoardBtn.setOnClickListener(e -> {
            new Thread(() -> {
                String title = editBoardTitle.getText().toString();
                String userName = editBoardUserName.getText().toString();
                String text = editText.getText().toString();
                String password = editPassword.getText().toString();
                byte[] imagePath = new byte[10];
                String date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
                BoardDto editBoardDto = new BoardDto(boardDtoList.get(position).getId(), password, title, userName, text, date, imagePath);
                Log.e("editID", boardDtoList.get(position).getId().toString());
                boardDB.boardDao().update(editBoardDto);
                boardAdapter.notifyDataSetChanged();
            }).start();

            Intent i = new Intent(this, BoardFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("id", boardDtoList.get(position).getId());
            i.putExtra("title", boardDtoList.get(position).getTitle());
            i.putExtra("userName", boardDtoList.get(position).getUserName());
            i.putExtra("text", boardDtoList.get(position).getText());
            i.putExtra("date", boardDtoList.get(position).getDate());
            i.putExtra("position", position);
            this.startActivity(i);
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
                startActivity(intent);

                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
