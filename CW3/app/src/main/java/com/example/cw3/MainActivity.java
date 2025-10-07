package com.example.cw3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // DrawingArea variables
    View drawingAreaView;
    DrawingArea drawingArea;

    // File Variables
    File drawingsDirectory;
    File[] drawingFilesList;
    // Gallery
    ImageView drawing1View;
    ImageView drawing2View;
    ImageView drawing3View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        drawingAreaView = findViewById(R.id.drawingArea);
        drawingArea = findViewById(R.id.drawingArea);
        drawing1View = findViewById(R.id.drawing1);
        drawing2View = findViewById(R.id.drawing2);
        drawing3View = findViewById(R.id.drawing3);
        drawingsDirectory = getFilesDir();
        drawingFilesList = drawingsDirectory.listFiles();
        updateGallery();
    }

    public void clear(View view) {
        drawingArea.clear();
    }

    public void save(View view) {
        drawingArea = findViewById(R.id.drawingArea);
        Bitmap drawingBitmap = drawingArea.getBitmap();
        try {
            // Create file
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.US).format(new Date());
            File f = new File(getFilesDir().getAbsolutePath(), "IMG_" + timeStamp + ".png");

            // Open file stream so we can write our image to the file we created
            FileOutputStream fos = new FileOutputStream(f);
            // compress the bitmap drawing in order to write the file
            drawingBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            // Update files
            drawingFilesList = drawingsDirectory.listFiles();
            updateGallery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Load the saved images
    private void updateGallery() {
        // Set every underlying image to the most recent file. The first image is always "profileinstalled"
        try {
            if (drawingFilesList.length >= 2) {
                // decode file into bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(drawingFilesList[drawingFilesList.length - 1].getAbsolutePath());
                // Set image to bitmap
                drawing1View.setImageBitmap(bitmap);
            }
            if (drawingFilesList.length >= 3) {
                Bitmap bitmap = BitmapFactory.decodeFile(drawingFilesList[drawingFilesList.length - 2].getAbsolutePath());
                drawing2View.setImageBitmap(bitmap);
            }
            if (drawingFilesList.length >= 4) {
                Bitmap bitmap = BitmapFactory.decodeFile(drawingFilesList[drawingFilesList.length - 3].getAbsolutePath());
                drawing3View.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}