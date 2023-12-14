package com.example.pethfinder;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.BoardDB;
import dto.BoardDto;

public class BoardFragment extends Fragment {
    private BoardDB boardDb;
    private List<BoardDto> boardList;
    private BoardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button mAddBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        boardDb = BoardDB.getInstance(requireContext());
        boardList = new ArrayList<>();
        mAdapter = new BoardAdapter(requireContext(), boardList);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mAddBtn = view.findViewById(R.id.writingBtn);

            Runnable r = () -> {
            try {
                boardList = boardDb.boardDao().getAll();
                mAdapter = new BoardAdapter(requireContext(), boardList);

                requireActivity().runOnUiThread(() -> {
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mRecyclerView.setHasFixedSize(true);
                });
            } catch (Exception e) {
                Log.d("tag", "Error - " + e);
            }
        };
        Thread thread = new Thread(r);
        thread.start();

        mAddBtn.setOnClickListener(v -> {
            if (isAdded()) {
                Intent i = new Intent(requireContext(), BoardAddActivity.class);
                startActivity(i);
                requireActivity().finish();
            }
        });



        return view;
    }

    public void setBoardList(List<BoardDto> newBoardList) {
        boardList.clear();
        boardList.addAll(newBoardList);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        BoardDB.destroyInstance();
        boardDb = null;
        super.onDestroy();
    }
}