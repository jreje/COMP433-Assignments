package com.example.amst3;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.api.services.vision.v1.Vision;

public class VisionService {
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private final Vision vision;
    public VisionService() {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(new VisionRequestInitializer("API_KEY"));
        vision = builder.build();
    }

    public Image bitmapToImage(Bitmap bitmap) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bout);
        Image myimage = new Image();
        myimage.encodeContent(bout.toByteArray());
        return myimage;
    }

    public void buildVision() {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(new VisionRequestInitializer("API_KEY"));
        Vision vision = builder.build();
    }

    public String annotateImage(Image image) throws IOException {
        //2. PREPARE AnnotateImageRequest
        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
        annotateImageRequest.setImage(image);
        Feature f = new Feature();
        f.setType("LABEL_DETECTION");
        f.setMaxResults(3);
        List<Feature> lf = new ArrayList<Feature>();
        lf.add(f);

        //4. CALL Vision.Images.Annotate
        annotateImageRequest.setFeatures(lf);
        BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
        List<AnnotateImageRequest> list = new ArrayList<AnnotateImageRequest>();
        list.add(annotateImageRequest);
        batchAnnotateImagesRequest.setRequests(list);
        Vision.Images.Annotate task = vision.images().annotate(batchAnnotateImagesRequest);
        BatchAnnotateImagesResponse response = task.execute();
        Log.v("MYTAG", response.toPrettyString());

        // Return Labels
        StringBuilder labelText = new StringBuilder();

        if (response.getResponses() != null && !response.getResponses().isEmpty()) {
            List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
            if (labels != null) {
                for (var label: labels) {
                    labelText.append(label.getDescription()).append(", ");
                }
            } else {
                labelText.append("No labels found");
            }
        }

        Log.v("MYTAG", labelText.toString());
        return labelText.toString();
    }
}
