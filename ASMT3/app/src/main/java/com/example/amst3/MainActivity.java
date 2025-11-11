package com.example.amst3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    public void startPhotoTaggerIntent(View view) {
        Intent photoTaggerIntent = new Intent(MainActivity.this, PhotoTagger.class);
        MainActivity.this.startActivity(photoTaggerIntent);
    }

    public void startSketchTaggerIntent(View view) {
        Intent sketchTaggerIntent = new Intent(MainActivity.this, SketchTagger.class);
        startActivity(sketchTaggerIntent);
    }

    public void clear(View view) {
    }

    public void save(View view) {
    }

    public void find(View view) {
    }


}