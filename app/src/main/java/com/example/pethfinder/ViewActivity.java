package com.example.pethfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.List;

import database.BoardDB;
import dto.BoardDto;

public class ViewActivity extends AppCompatActivity {
    private int position;
    private int boardId;
    private TextView viewUserName;
    private TextView viewTitle;
    private TextView viewDate;
    private TextView viewText;

    private ImageView imageView;
    private Button backBtn;
    private Button updateBtn;
    private Button deleteBtn;

    private BoardDB boardDB;
    private List<BoardDto> boardDtoList;
    private BoardAdapter boardAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_board);
        boardDB = BoardDB.getInstance(this);
        boardDtoList = boardDB.boardDao().getAll();
        boardAdapter = new BoardAdapter(this, boardDtoList);

        viewTitle = findViewById(R.id.viewTitle);
        viewUserName = findViewById(R.id.viewUserName);
        viewDate = findViewById(R.id.viewDate);
        viewText = findViewById(R.id.viewText);
        imageView = findViewById(R.id.imageView);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        Intent intent = getIntent();

        viewUserName.setText(intent.getStringExtra("userName"));
        viewTitle.setText(intent.getStringExtra("title"));
        viewText.setText(intent.getStringExtra("text"));
        viewDate.setText(intent.getStringExtra("date"));
        Picasso.with(this).load("file:///" + intent.getStringExtra("imagePath")).into(imageView);
        Log.e("Path", intent.getStringExtra("imagePath"));
        boardId = (int) intent.getLongExtra("id", 0);
        position = intent.getIntExtra("position", 0);

        updateBtn.setOnClickListener(e -> {
            showPasswordInputDialog("update");
        });

        deleteBtn.setOnClickListener(e -> {
            showPasswordInputDialog("delete");
        });
        Log.e("viewID", boardId + "");
    }

    private void showPasswordInputDialog(String query) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.password_input_dialog, null);
        builder.setView(dialogView);

        final EditText passwordInput = dialogView.findViewById(R.id.passwordInput);

        builder.setPositiveButton("Update", (dialog, which) -> {
            // Check if the entered password is correct (you should replace "your_password" with the actual correct password)
            String enteredPassword = passwordInput.getText().toString();
            if (enteredPassword.equals(boardDtoList.get(position).getPassword())) {
                if (query.equals("update")) {
                    boardUpdate();
                } else {
                    boardDelete();
                }
            } else {
                showToast("잘못된 비밀번호");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // User canceled the dialog
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void boardUpdate() {
        Intent updateIntent = new Intent(ViewActivity.this, EditActivity.class);
        updateIntent.putExtra("id", boardDtoList.get(position).getId());
        updateIntent.putExtra("title", boardDtoList.get(position).getTitle());
        updateIntent.putExtra("userName", boardDtoList.get(position).getUserName());
        updateIntent.putExtra("text", boardDtoList.get(position).getText());
        updateIntent.putExtra("date", boardDtoList.get(position).getDate());
        updateIntent.putExtra("position", position);
        updateIntent.putExtra("imagePath", boardDtoList.get(position).getImagePath());
        Log.e("position", String.valueOf(position));
        startActivity(updateIntent);
        showToast("비밀번호 인증 완료");
        finish();
    }

    private void boardDelete() {
        boardDtoList.remove(position);
        Log.e("position", String.valueOf(position));
        boardDB.boardDao().delete(new BoardDto(boardId));
        boardAdapter.notifyDataSetChanged();
        boardAdapter.notifyItemRangeChanged(position, boardDtoList.size());
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        Log.e("Delete Complete", String.valueOf(boardDtoList.size()));
        showToast("삭제 완료");
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
