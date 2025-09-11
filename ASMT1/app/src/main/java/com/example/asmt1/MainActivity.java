package com.example.asmt1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    File imageDir; // Directory that holds all image files
    File[] imageFiles; // array of image files
    ImageView mainImageView; // Largest image (n)
    ImageView image1View; // leftmost image (n-1)
    ImageView image2View; // n-2
    ImageView image3View; // n-3
    int mainIndex; // Index of the main index, starts at the end of the files array (recent most pic)
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
        // Make Directory of pictures
        imageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Access the photo files within the directory
        imageFiles = imageDir.listFiles();
        // Main index is the last file because it was most recent to be added (latest pic)
        mainIndex = imageFiles.length - 1;

        // Image Views
        mainImageView = findViewById(R.id.mainImageView);
        image1View = findViewById(R.id.image1);
        image2View = findViewById(R.id.image2);
        image3View = findViewById(R.id.image3);

        updateGallery();
    }

    public void updateGallery() {
        // Check that files does exist and there is at least one item
        if (imageFiles == null || imageFiles.length < 1) return;

        // Main image
        if (imageIndexIsInDirectory(mainIndex)) {
            // Set main image to image at index n
            setImage(Images.MAIN_IMAGE, mainIndex);
            System.out.println("mainImage: " + imageFiles[mainIndex].getName());
        }

        // Image 1
        if (imageIndexIsInDirectory(mainIndex - 1)) {
            // Set image 1 to image at index n-1
            setImage(Images.IMAGE_1, mainIndex - 1);
            System.out.println("image_1: " + imageFiles[mainIndex - 1].getName());
        } else {
            // If there is none, we have to make sure no image appears
            image1View.setImageDrawable(null);
        }

        // Image 2
        if (imageIndexIsInDirectory(mainIndex - 2)) {
            setImage(Images.IMAGE_2, mainIndex - 2);
            System.out.println("image_2: " + imageFiles[mainIndex - 2].getName());
        } else {
            image2View.setImageDrawable(null);
        }


        // Image 3
        if (imageIndexIsInDirectory(mainIndex - 3)) {
            setImage(Images.IMAGE_3, mainIndex - 3);
            System.out.println("image_3: " + imageFiles[mainIndex].getName());
        } else {
            image3View.setImageDrawable(null);
        }

    }

    public void startCamera(View view) {
        // Start intent to switch to the image capturing activity (on the camera) which is indicated by MediaStore.ACTION_IMAGE_CAPTURE
        Intent cam_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Track time to name and create the photoFile within the imageDir file
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.US).format(new Date());
        File photoFile = new File(imageDir, "IMG_" + timeStamp + ".png");
        // Save this path so the OnActivityResult() can update the displayed image

        // Use the FileProvider to convert the file into a *content* URI and send it to OnActivityResult
        // We don't use a *file* URI because that causes a File Uri Exposed Exception
        Uri photoUri = FileProvider.getUriForFile(this, "com.example.asmt1.file_provider", photoFile);
        cam_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(cam_intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Reload the image files from directory
            imageFiles = imageDir.listFiles();
            // After taking a photo, we want to always update the main image to the most recent photo, or the last item
            mainIndex = imageFiles.length - 1;

            // Update all images
            updateGallery();
        }
    }

    public boolean imageIndexIsInDirectory(int imageIndex) {
        return (imageIndex >= 0 && imageFiles[imageIndex] != null);
    }

    public void setImage(Images image, int toImageIndex) {
        // Obtain the image file at index toImageIndex - this is the file that we're switching to
        // Bitmap factory decodes this file into a bitmap image
        // Set our image to the bitmap
        Bitmap toImage = BitmapFactory.decodeFile(imageFiles[toImageIndex].getAbsolutePath());
        switch(image) {
            case MAIN_IMAGE:
                mainImageView.setImageBitmap(toImage);
                break;
            case IMAGE_1:
                image1View.setImageBitmap(toImage);
                break;
            case IMAGE_2:
                image2View.setImageBitmap(toImage);
                break;
            case IMAGE_3:
                image3View.setImageBitmap(toImage);
                break;
            default:

        }
    }

    public void makeImage1Main(View view) {
        Log.v("MYTAG", "Image Clicked");
        if (imageIndexIsInDirectory(mainIndex - 1)) {
            //setImage(Images.MAIN_IMAGE, 1);
            mainIndex = mainIndex - 1;
            updateGallery();
        }
    }

    public void makeImage2Main(View view) {
        Log.v("MYTAG", "Image Clicked");
        if (imageIndexIsInDirectory(mainIndex - 2)) {
            //setImage(Images.MAIN_IMAGE, 2);
            mainIndex = mainIndex - 2;
            updateGallery();
        }
    }

    public void makeImage3Main(View view) {
        Log.v("MYTAG", "Image Clicked");
        if (imageIndexIsInDirectory(mainIndex - 3)) {
            setImage(Images.MAIN_IMAGE, 3);
            mainIndex = mainIndex - 3;
            //updateGallery();
        }
    }

    public enum Images{
        MAIN_IMAGE, IMAGE_1, IMAGE_2, IMAGE_3;
    }
}