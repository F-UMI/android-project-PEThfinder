package com.example.pethfinder;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import database.BoardDB;
import dto.BoardDto;

public class BoardViewActivity extends AppCompatActivity {
    private int position;
    private int boardId;
    private byte[] imagePath;
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
        setContentView(R.layout.activity_info_board);
        boardDB = BoardDB.getInstance(this);
        boardDtoList = boardDB.boardDao().getAll();
        boardAdapter = new BoardAdapter(this, boardDtoList);


        viewTitle = findViewById(R.id.editBoardTitle);
        viewUserName = findViewById(R.id.editBoardUserName);
        viewDate = findViewById(R.id.editBoardPassword);
        viewText = findViewById(R.id.editBoardText);
        imageView = findViewById(R.id.editBoardImage);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        Intent intent = getIntent();

        viewUserName.setText(intent.getStringExtra("userName"));
        viewTitle.setText(intent.getStringExtra("title"));
        viewText.setText(intent.getStringExtra("text"));
        viewDate.setText(intent.getStringExtra("date"));
        boardId = (int) intent.getLongExtra("id", 0);
        position = intent.getIntExtra("position", 0);
        imagePath = intent.getByteArrayExtra("imagePath");


        if (imagePath != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(imagePath, 0, imagePath.length));
        } else {
            imageView.setVisibility(ImageView.GONE);
        }

        Bitmap specificPhotoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.transparency);

        // Get the Bitmap from the ImageView
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap imageViewBitmap = imageView.getDrawingCache();

        if (bitmapEquals(imageViewBitmap, specificPhotoBitmap)) {
            imageView.setVisibility(ImageView.GONE);
        }


        imageView.setOnClickListener(v -> showPopupImage());


        updateBtn.setOnClickListener(e -> {
            showPasswordInputDialog("update");
        });

        deleteBtn.setOnClickListener(e -> {
            showPasswordInputDialog("delete");
        });
        Log.e("viewID", boardId + "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
                startActivity(intent);

                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPasswordInputDialog(String query) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("비밀번호를 입력하세요");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.password_input_dialog, null);
        builder.setView(dialogView);

        final EditText passwordInput = dialogView.findViewById(R.id.passwordInput);

        builder.setPositiveButton("인증", (dialog, which) -> {
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
        builder.setNegativeButton("취소", (dialog, which) -> {

            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void boardUpdate() {
        Intent updateIntent = new Intent(BoardViewActivity.this, BoardEditActivity.class);
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
        Intent i = new Intent(this, DeleteActivity.class);
        startActivity(i);
        Log.e("Delete Complete", String.valueOf(boardDtoList.size()));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showPopupImage() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_image);

        // 다이얼로그 레이아웃에서 ImageView 찾기
        ImageView fullScreenImageView = dialog.findViewById(R.id.fullScreenImageView);

        // ImageView에 확대된 이미지 설정
        if (imagePath != null) {
            fullScreenImageView.setImageBitmap(BitmapFactory.decodeByteArray(imagePath, 0, imagePath.length));
        }

        // ImageView를 클릭하면 다이얼로그를 닫기 위한 onClickListener 설정
        fullScreenImageView.setOnClickListener(v -> dialog.dismiss());

        // 다이얼로그 크기 설정 및 표시
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        dialog.show();
    }


    private boolean bitmapEquals(Bitmap bitmap1, Bitmap bitmap2) {
        if (bitmap1 == null || bitmap2 == null) {
            return false;
        }

        if (bitmap1.getWidth() != bitmap2.getWidth() || bitmap1.getHeight() != bitmap2.getHeight()) {
            return false;
        }

        for (int x = 0; x < bitmap1.getWidth(); x++) {
            for (int y = 0; y < bitmap1.getHeight(); y++) {
                if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}

