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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class SketchTagger extends AppCompatActivity {
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
    TextView drawing1Text;
    TextView drawing2Text;
    TextView drawing3Text;
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
        drawing1View = findViewById(R.id.drawing1);
        drawing2View = findViewById(R.id.drawing2);
        drawing3View = findViewById(R.id.drawing3);
        drawing1Text = findViewById(R.id.drawing1Text);
        drawing2Text = findViewById(R.id.drawing2Text);
        drawing3Text = findViewById(R.id.drawing3Text);

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

        updateGallery();
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

    private void updateGallery() {
        if (mydb == null) return;
        Cursor cursor = mydb.rawQuery("SELECT * FROM DRAWINGS ORDER BY ID DESC LIMIT 3", null);

        if (cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            drawing1View.setImageBitmap(bitmap);
            String tags = cursor.getString(1);
            String time = cursor.getString(2);
            String text = getString(R.string.drawing_info, tags, time);
            drawing1Text.setText(text);
        }
        if (cursor.moveToNext()) {
            byte[] imageBytes = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            drawing2View.setImageBitmap(bitmap);
            String tags = cursor.getString(1);
            String time = cursor.getString(2);
            String text = getString(R.string.drawing_info, tags, time);
            drawing2Text.setText(text);
        }
        if (cursor.moveToNext()) {
            byte[] imageBytes = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            drawing3View.setImageBitmap(bitmap);
            String tags = cursor.getString(1);
            String time = cursor.getString(2);
            String text = getString(R.string.drawing_info, tags, time);
            drawing3Text.setText(text);
        }
        cursor.close();
    }

    public void find(View view) {
        String input = query.getText().toString();
        Cursor cursor = mydb.rawQuery("SELECT * FROM DRAWINGS ORDER BY ID DESC LIMIT 3", null);
        if (!input.isEmpty()) {
            cursor.close();
            cursor = mydb.rawQuery("SELECT * FROM DRAWINGS WHERE TAG LIKE '%" + input + "%' ORDER BY ID DESC LIMIT 3", null);
        }

        ImageView[] imageViews = {drawing1View, drawing2View, drawing3View};
        TextView[] textViews = {drawing1Text, drawing2Text, drawing3Text};

        // Clear previous images/text
        for (int i = 0; i < 3; i++) {
            imageViews[i].setImageBitmap(null);
            textViews[i].setText("unavailable");
        }

        if (cursor.moveToFirst()) {
            byte[] imageBytes = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            drawing1View.setImageBitmap(bitmap);
            String tags = cursor.getString(1);
            String time = cursor.getString(2);
            String text = getString(R.string.drawing_info, tags, time);
            drawing1Text.setText(text);
        }
        if (cursor.moveToNext()) {
            byte[] imageBytes = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            drawing2View.setImageBitmap(bitmap);
            String tags = cursor.getString(1);
            String time = cursor.getString(2);
            String text = getString(R.string.drawing_info, tags, time);
            drawing2Text.setText(text);
        }
        if (cursor.moveToNext()) {
            byte[] imageBytes = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            drawing3View.setImageBitmap(bitmap);
            String tags = cursor.getString(1);
            String time = cursor.getString(2);
            String text = getString(R.string.drawing_info, tags, time);
            drawing3Text.setText(text);
        }

        cursor.close();
    }
    public void annotateImage(View view) {
        
    }
}

