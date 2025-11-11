package com.example.amst3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.services.vision.v1.model.Image;

public class GalleryItem {
    private int id;
    private Bitmap bitmap;
    String text;
    public GalleryItem(int id, Bitmap bitmap, String text) {
        this.id = id;
        this.bitmap = bitmap;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public Bitmap getImageBitmap() {
        return bitmap;
    }

    public String getText() {
        return text;
    }

}
