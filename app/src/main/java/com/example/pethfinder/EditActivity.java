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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class EditActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    byte[] imagePath = new byte[0];
    private BoardDB boardDB;
    private List<BoardDto> boardDtoList;
    private BoardAdapter boardAdapter;

    private EditText editBoardUserName;
    private EditText editBoardTitle;

    private EditText editBoardPassword;
    private EditText editBoardText;
    private ImageView editBoardImage;
    private Button imageEditBtn;
    private Button updateBoardBtn;


    private int position;

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
        editBoardImage.setImageBitmap(byteArrayToBitmap(boardDtoList.get(position).getImagePath()));

        imageEditBtn.setOnClickListener(e -> {
            openGallery();
        });

        updateBoardBtn.setOnClickListener(e -> {
            new Thread(() -> {
                String title = editBoardTitle.getText().toString();
                String userName = editBoardUserName.getText().toString();
                String text = editBoardText.getText().toString();
                String password = editBoardPassword.getText().toString();
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

}
