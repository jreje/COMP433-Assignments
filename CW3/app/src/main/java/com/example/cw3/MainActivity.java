package com.example.cw3;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

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
    }

    public void clear(View view) {
        drawingArea.clear();
    }

    public void save(View view) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}