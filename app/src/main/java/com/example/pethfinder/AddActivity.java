package com.example.pethfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import database.BoardDB;
import dto.BoardDto;

public class AddActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private BoardDB boardDb;
    private String date;

    private EditText addTitle;
    private EditText addUserName;

    private EditText addPassword;
    private EditText addText;
    private ImageView addImage;
    private Button btnGetImage;

    private String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        boardDb = BoardDB.getInstance(this);
        date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
        addTitle = findViewById(R.id.addTitle);
        addUserName = findViewById(R.id.addUserName);
        addPassword = findViewById(R.id.addPassword);
        addText = findViewById(R.id.addText);
        addImage = findViewById(R.id.addImage);

        btnGetImage = findViewById(R.id.btnGetImage);
        btnGetImage.setOnClickListener(v -> {
            openGallery();
        });


        final Runnable addRunnable = () -> {
            BoardDto newBoardDto = new BoardDto();
            newBoardDto.setTitle(addTitle.getText().toString());
            newBoardDto.setUserName(addUserName.getText().toString());
            newBoardDto.setPassword(addPassword.getText().toString());
            newBoardDto.setText(addText.getText().toString());
            newBoardDto.setDate(date);
            newBoardDto.setImagePath(imagePath);
            boardDb.boardDao().insert(newBoardDto);
        };

        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(v -> {
            Thread addThread = new Thread(addRunnable);
            addThread.start();
            Intent i = new Intent(AddActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        BoardDB.destroyInstance();
        super.onDestroy();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            displaySelectedImage(selectedImageUri);
            setImagePath(selectedImageUri);
        }
    }

    private void displaySelectedImage(Uri uri) {
        // Set the selected image to the ImageView
        addImage.setImageURI(uri);
    }

    private void setImagePath(Uri selectedImageUri) {
        this.imagePath = selectedImageUri.getPath();
        Log.e("PATH", imagePath);
    }
}



