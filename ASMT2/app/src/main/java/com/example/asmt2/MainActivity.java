package com.example.asmt2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    File drawingDir; // Directory that holds all image files
    File[] drawingFiles; // array of image files
    ImageView drawing1View;
    ImageView drawing2View;
    ImageView drawing3View;
    Bitmap drawing1Bitmap;

    private SensorManager sm;
    private Sensor shakeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Make Directory of pictures
        drawingDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Access the photo files within the directory
        drawing1View = findViewById(R.id.drawing1);
        drawing2View = findViewById(R.id.drawing2);
        drawing3View = findViewById(R.id.drawing3);
        drawingDir = getFilesDir();
        drawingFiles = drawingDir.listFiles();

        updateGallery();

        // Sensor Stuff
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // check shakeSensor exists
        if (shakeSensor == null) {
            Log.e("sensor", "accelerometer not available");
        } else {
            sm.registerListener(this, shakeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Load the saved images
    private void updateGallery() {
        // Set every underlying image to the most recent file. The first image is always "profileinstalled"
        try {
            if (drawingFiles.length >= 2) {
                // decode file into bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(drawingFiles[drawingFiles.length - 1].getAbsolutePath());
                // Set image to bitmap
                drawing1View.setImageBitmap(bitmap);
            }
            if (drawingFiles.length >= 3) {
                Bitmap bitmap = BitmapFactory.decodeFile(drawingFiles[drawingFiles.length - 2].getAbsolutePath());
                drawing2View.setImageBitmap(bitmap);
            }
            if (drawingFiles.length >= 4) {
                Bitmap bitmap = BitmapFactory.decodeFile(drawingFiles[drawingFiles.length - 3].getAbsolutePath());
                drawing3View.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(View view) {
        // Get the custom view
        DrawingArea customView = findViewById(R.id.drawingArea);
        // Use the object's clear method
        customView.clear();
    }

    public void saveDrawing(View view) {
        DrawingArea customView = findViewById(R.id.drawingArea);
        drawing1Bitmap = customView.getBitmap();
        try {
            // Create file
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.US).format(new Date());
            File f = new File(getFilesDir().getAbsolutePath(), "IMG_" + timeStamp + ".png");

            // Open file stream so we can write our image to the file we created
            FileOutputStream fos = new FileOutputStream(f);
            // compress the bitmap drawing in order to write the file
            drawing1Bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            // Update files
            drawingFiles = drawingDir.listFiles();
            updateGallery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        customView.clear();
    }

    // === MARK: Sensor ===
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i("sensor", Arrays.toString(sensorEvent.values));

        DrawingArea drawingArea = findViewById(R.id.drawingArea);
        // Get Acceleration Data
        float x_accl = sensorEvent.values[0];
        float y_accl = sensorEvent.values[1];
        float z_accl = sensorEvent.values[2];

        // x and z are usually close to 1,
        // but acceleration is usually closer to 9 because of gravity
        // if we exceed these bounds, then there's most likely a shake
        if (x_accl>2 ||
                x_accl < -2 ||
                y_accl > 12 ||
                y_accl < -12 ||
                z_accl > 2 ||
                z_accl < -2
        ) {
            drawingArea.shake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        // Stop recording sensor data
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        // Resume recording sensor data
        super.onResume();
        sm.registerListener(this, shakeSensor, 1000000);
    }
}