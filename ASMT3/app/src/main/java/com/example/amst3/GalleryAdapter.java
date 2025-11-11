package com.example.amst3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<GalleryItem> {
    public GalleryAdapter(@NonNull Context context, ArrayList<GalleryItem> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_item, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        GalleryItem currentGalleryItemPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        ImageView image = currentItemView.findViewById(R.id.image);
        assert currentGalleryItemPosition != null;
        image.setImageBitmap(currentGalleryItemPosition.getImageBitmap());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.text);
        textView1.setText(currentGalleryItemPosition.getText());

        // then return the recyclable view
        return currentItemView;
    }
}
