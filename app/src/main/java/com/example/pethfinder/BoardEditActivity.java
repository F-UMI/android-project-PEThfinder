package com.example.pethfinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import java.util.List;

import database.BoardDB;
import dto.BoardDto;

public class BoardEditActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    byte[] imagePath;
    private BoardDB boardDB;
    private List<BoardDto> boardDtoList;
    private BoardAdapter boardAdapter;

    private BoardDto editBoardDto;

    private EditText editBoardUserName;
    private EditText editBoardTitle;

    private EditText editBoardPassword;
    private EditText editBoardText;
    private ImageView editBoardImage;
    private Button imageEditBtn;
    private Button updateBoardBtn;
    private int position;
    private Spinner class_Spinner;
    private String[] classification;
    private String selectedClassification;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_board);
        boardDB = BoardDB.getInstance(this);
        boardDtoList = boardDB.boardDao().getAll();
//
        boardAdapter = new BoardAdapter(this, boardDtoList);

        editBoardTitle = findViewById(R.id.editBoardTitle);
        editBoardUserName = findViewById(R.id.editBoardUserName);
        editBoardPassword = findViewById(R.id.editBoardPassword);
        editBoardText = findViewById(R.id.editBoardText);
        editBoardImage = findViewById(R.id.editBoardImage);

        imageEditBtn = findViewById(R.id.imageEditBtn);
        updateBoardBtn = findViewById(R.id.updateBoardBtn);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        editBoardTitle.setText(intent.getStringExtra("title"));
        editBoardUserName.setText(intent.getStringExtra("userName"));
        editBoardPassword.setText(intent.getStringExtra("password"));
        editBoardText.setText(intent.getStringExtra("text"));
        if (boardDtoList.get(position).getImagePath() != null) {
            editBoardImage.setImageBitmap(byteArrayToBitmap(intent.getByteArrayExtra("imagePath")));
        } else {
            editBoardImage.setVisibility(ImageView.GONE);
        }

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

        imageEditBtn.setOnClickListener(e -> {
            openGallery();
        });

        Bitmap specificPhotoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.transparency);

        // Get the Bitmap from the ImageView
        editBoardImage.setDrawingCacheEnabled(true);
        editBoardImage.buildDrawingCache();
        Bitmap imageViewBitmap = editBoardImage.getDrawingCache();

        updateBoardBtn.setOnClickListener(e -> {
            new Thread(() -> {
                String title = editBoardTitle.getText().toString();
                String userName = editBoardUserName.getText().toString();
                String text = editBoardText.getText().toString();
                String password = editBoardPassword.getText().toString();
                String date = String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")));
                if (bitmapEquals(imageViewBitmap, specificPhotoBitmap)) {
                    editBoardDto = new BoardDto(boardDtoList.get(position).getId(), password, title, userName, text, date, intent.getByteArrayExtra("imagePath"), selectedClassification);
                } else {
                    editBoardDto = new BoardDto(boardDtoList.get(position).getId(), password, title, userName, text, date, imagePath, selectedClassification);
                }
                Log.e("editID", boardDtoList.get(position).getId().toString());
                boardDB.boardDao().update(editBoardDto);
                boardAdapter.notifyDataSetChanged();
            }).start();

            Intent i = new Intent(this, LoadingCompletedActivity.class);
            /*            i.putExtra("FRAGMENT_TO_LOAD", "BoardFragment");*/
            startActivity(i);
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
        // Convert the URI to a bitmap (you may want to use a library like Glide or Picasso)
        // For simplicity, you can use BitmapFactory
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void displaySelectedImage(Bitmap imageBitmap) {
        // Set the selected image to the ImageView
        editBoardImage.setImageBitmap(imageBitmap);
        editBoardImage.setVisibility(ImageView.VISIBLE);
    }

    private void setImagePath(Bitmap imageBitmap) {
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        this.imagePath = stream.toByteArray();
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
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

