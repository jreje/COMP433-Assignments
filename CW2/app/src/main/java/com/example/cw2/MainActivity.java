package com.example.cw2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    public void reset(View view) {
        MyDrawingArea customView = findViewById(R.id.customView);
        customView.reset();
    }

    public void saveDrawing(View view) {
        MyDrawingArea customView = findViewById(R.id.customView);
        Bitmap b = customView.getBitmap(); //we wrote this function inside custom view
        try {
            File f = new File(getFilesDir().getAbsolutePath() + "/mysketch.png");
            FileOutputStream fos = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}