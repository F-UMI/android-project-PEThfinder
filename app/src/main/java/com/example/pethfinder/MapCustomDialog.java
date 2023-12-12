package com.example.pethfinder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

/*
 * 가게명 : content[0]
 * X : content[11]
 * Y : content[12]
 * 가게 주소 : content[14]
 * 전화번호 : content[16]
 */

public class MapCustomDialog extends Dialog {
    private TextView textView;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private TextView storeTextView;
    private TextView addressTextView;
    private TextView phoneNumberTextView;
    private TextView weekdayOpenTimeTextView;
    private TextView weekendOpenTimeTextView;


    public MapCustomDialog(@NonNull Context context, String[] content, String marketId) {
        super(context);
        setContentView(R.layout.popup_board);

//        String storeName = content[0];
//        String address = content[14];
//        String phoneNumber = content[16];
//        String weekdayOpenTime = content[17];
//        String weekendOpenTime = content[18];
        String storeName = getContent(content, 0);
        String address = getContent(content, 14);
        String phoneNumber = getContent(content, 16);
        String weekdayOpenTime = getContent(content, 17);
        String weekendOpenTime = getContent(content, 18);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView_market);
        imageView1 = findViewById(R.id.imageView_market1);
        imageView2 = findViewById(R.id.imageView_market2);
        storeTextView = findViewById(R.id.textView_storeName);
        addressTextView = findViewById(R.id.textView_address);
        phoneNumberTextView = findViewById(R.id.textView_Number);
        weekdayOpenTimeTextView = findViewById(R.id.textView_WeekdayOpenTime);
        weekendOpenTimeTextView = findViewById(R.id.textView_WeekendOpenTime);

        imageView.setOnClickListener(view -> showFullScreenImageDialog(imageView));
        imageView1.setOnClickListener(view -> showFullScreenImageDialog(imageView1));
        imageView2.setOnClickListener(view -> showFullScreenImageDialog(imageView2));
        textView.setText(marketId);

        storeTextView.setText(storeName);
        addressTextView.setText( address);
        phoneNumberTextView.setText(phoneNumber);
        weekdayOpenTimeTextView.setText(weekdayOpenTime);
        weekendOpenTimeTextView.setText(weekendOpenTime);
        setImage(imageView, marketId, 1);
        setImage(imageView1, marketId, 2);
        setImage(imageView2, marketId, 3);
    }

    private String getContent(String[] content, int i) {
        String value = "";
        if (content[i].isEmpty()) {
            value = "정보가 없습니다.";
        } else value = content[i];
        return value;
    }

    private void setImage(ImageView imageView, String marketId, int i) {
        String imageName = marketId.toLowerCase() + "_image_" + i;
        int imageResourceId = getContext().getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());

        if (imageResourceId != 0) {
            imageView.setImageResource(imageResourceId);
        }
    }


    private void showFullScreenImageDialog(ImageView imageView) {
        Dialog dialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.popup_image);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ImageView fullScreenImageView = dialog.findViewById(R.id.fullScreenImageView);
        fullScreenImageView.setImageBitmap(bitmap);
        dialog.show();
    }
}