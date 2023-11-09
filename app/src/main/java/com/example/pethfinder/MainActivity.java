package com.example.pethfinder;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.BoardDB;
import dto.Board;

public class MainActivity extends AppCompatActivity {
    private BoardDB boardDb;
    private List<Board> boardList;
    private BoardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button mAddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardDb = BoardDB.getInstance(this);
        boardList = new ArrayList<>();
        mAdapter = new BoardAdapter(this, boardList);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mAddBtn = findViewById(R.id.writingBtn);
        Runnable r = () -> {
            try {
                boardList = boardDb.boardDao().getAll();
                mAdapter = new BoardAdapter(MainActivity.this, boardList);
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


