package com.example.pethfinder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.BoardDB;
import dto.BoardDto;

public class MainActivity extends AppCompatActivity {
    private BoardDB boardDb;
    private List<BoardDto> boardDtoList;
    private BoardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardDb = BoardDB.getInstance(this);
        boardDtoList = new ArrayList<>();
        mAdapter = new BoardAdapter(this, boardDtoList);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAddBtn = findViewById(R.id.writingBtn);
        Runnable r = () -> {
            try {
                boardDtoList = boardDb.boardDao().getAll();
                mAdapter = new BoardAdapter(MainActivity.this, boardDtoList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mRecyclerView.setHasFixedSize(true);
            } catch (Exception e) {
                Log.d("tag", "Error - " + e);
            }
        };
        Thread thread = new Thread(r);
        thread.start();

        mAddBtn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        BoardDB.destroyInstance();
        boardDb = null;
        super.onDestroy();
    }
}


