package com.example.pethfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import database.BoardDB;
import dto.BoardDto;

public class BoardAddActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private BoardDB boardDb;
    private String date;

    private EditText addTitle;
    private EditText addUserName;

    private EditText addPassword;
    private EditText addText;
    private ImageView addImage;
    private Button btnGetImage;

    private Spinner class_Spinner;

    private byte[] imagePath;

    private String[] classification;

    private String selectedClassification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_board);
        boardDb = BoardDB.getInstance(this);
        date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
        addTitle = findViewById(R.id.addTitle);
        addUserName = findViewById(R.id.addUserName);
        addPassword = findViewById(R.id.addPassword);
        addText = findViewById(R.id.addText);
        addImage = findViewById(R.id.addImage);
        btnGetImage = findViewById(R.id.btnGetImage);
        class_Spinner = findViewById(R.id.edit_class_spinner);
        classification = new String[]{"정보", "리뷰"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_list, classification);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        class_Spinner.setAdapter(adapter);

        // 스피너에서 선택 했을 경우 이벤트 처리
        class_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClassification = class_Spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
            newBoardDto.setClassification(selectedClassification);
            boardDb.boardDao().insert(newBoardDto);
        };

        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(v -> {
            Thread addThread = new Thread(addRunnable);
            addThread.start();
            Intent i = new Intent(this, LoadingCompletedActivity.class);
//            i.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");
            startActivity(i);
            finish();
        });
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
            Bitmap imageBitmap = convertUriToBitmap(selectedImageUri);
            displaySelectedImage(imageBitmap);
            setImagePath(imageBitmap);
        }
    }

    private Bitmap convertUriToBitmap(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void displaySelectedImage(Bitmap imageBitmap) {
        addImage.setImageBitmap(imageBitmap);
    }

    private void setImagePath(Bitmap imageBitmap) {
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        this.imagePath = stream.toByteArray();
    }
}



