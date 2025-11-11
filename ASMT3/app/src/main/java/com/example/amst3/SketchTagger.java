package com.example.amst3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.api.services.vision.v1.model.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SketchTagger extends AppCompatActivity {
    // ListView
    ListView l;
    // DrawingArea variables
    View drawingAreaView;
    DrawingArea drawingArea;

    // File Variables
    File drawingsDirectory;
    File[] drawingFilesList;
    // Gallery
    ArrayList<GalleryItem> gallery = new ArrayList<>();
    // Database
    SQLiteDatabase mydb;
    // EditText
    EditText tagsInput;
    EditText query;
    // ID
    int currId;
    // Vision
    VisionService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sketch_tagger);
        // Service
        service = new VisionService();

        // Drawing
        drawingAreaView = findViewById(R.id.drawingArea);
        drawingArea = findViewById(R.id.drawingArea);
        // Gallery
        l = findViewById(R.id.galleryListView);
        GalleryAdapter arr = new GalleryAdapter(this, gallery);
        l.setAdapter(arr);

        drawingsDirectory = getFilesDir();
        drawingFilesList = drawingsDirectory.listFiles();

        // EditText
        tagsInput = findViewById(R.id.tagsInput);
        query = findViewById(R.id.findInput);

        // Database
        createDatabase();

        // Update currId
        Cursor cursor = mydb.rawQuery("SELECT MAX(ID) FROM DRAWINGS", null);
        if (cursor.moveToFirst()) {
            currId = cursor.getInt(0) + 1;
        }
        cursor.close();
        cursor = mydb.rawQuery("SELECT * FROM DRAWINGS ORDER BY ID DESC", null);
        updateGallery(cursor);
        cursor.close();
    }

    void createDatabase() {
        mydb = this.openOrCreateDatabase("mydb", Context.MODE_PRIVATE, null);

        mydb.execSQL("CREATE TABLE IF NOT EXISTS DRAWINGS (ID INT PRIMARY KEY, TAG TEXT, TIME TEXT, IMAGE BLOB)");
    }

    public void clear(View view) {
        drawingArea.clear();
    }

    public void save(View view) {
        String timeStamp = new SimpleDateFormat("MMM dd, yyyy - hha", Locale.US).format(new Date());
        String tags = tagsInput.getText().toString();
        if (tags.isEmpty()) {
            Toast.makeText(this, "Enter a tag", Toast.LENGTH_SHORT).show();
            return;
        }
        drawingArea = findViewById(R.id.drawingArea);
        Bitmap drawingBitmap = drawingArea.getBitmap();
        byte[] imageBytes = bitmapToBytes(drawingBitmap);

        ContentValues cv = new ContentValues();
        cv.put("ID", currId);
        cv.put("TAG", tags);
        cv.put("TIME", timeStamp);
        cv.put("IMAGE", imageBytes);

        mydb.insert("DRAWINGS", null, cv);
        currId += 1;
    }

    private byte[] bitmapToBytes(Bitmap bitmap) {
        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void updateGallery(Cursor cursor) {
        if (mydb == null) return;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            byte[] imageBytes = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ImageView image = new ImageView(this);
            image.setImageBitmap(bitmap);

            int id = cursor.getInt(0);
            String tags = cursor.getString(1);
            String time = cursor.getString(2);
            String text = getString(R.string.drawing_info, tags, time);
            TextView textView = new TextView(this);
            textView.setText(text);

            GalleryItem galleryItem = new GalleryItem(id, bitmap, text);

            gallery.add(galleryItem);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void find(View view) {
        String input = query.getText().toString();
        Cursor cursor = mydb.rawQuery("SELECT * FROM DRAWINGS ORDER BY ID DESC", null);
        if (!input.isEmpty()) {
            cursor.close();
            cursor = mydb.rawQuery("SELECT * FROM DRAWINGS WHERE TAG LIKE '%" + input + "%' ORDER BY ID DESC", null);
        }
        cursor.close();
    }
    public void annotateImage(View view) throws IOException {
        drawingArea = findViewById(R.id.drawingArea);
        Bitmap drawingBitmap = drawingArea.getBitmap();
        Image image = service.bitmapToImage(drawingBitmap);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = service.annotateImage(image);
                    runOnUiThread(() -> tagsInput.setText(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

