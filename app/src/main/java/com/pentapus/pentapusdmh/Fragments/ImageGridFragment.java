package com.pentapus.pentapusdmh.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pentapus.pentapusdmh.R;

import java.io.File;
import java.lang.ref.WeakReference;

public class ImageGridFragment extends Fragment implements AdapterView.OnItemClickListener {

    private File imageUris[];
    private ImageAdapter imageAdapter;
    // A static dataset to back the GridView adapter

    // Empty constructor as per Fragment docs
    public ImageGridFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
        // path to /data/data/yourapp/app_data/iconDir
        File directory = cw.getDir("iconDir", Context.MODE_PRIVATE);
        imageUris = directory.listFiles();
        imageAdapter = new ImageAdapter(getContext(), imageUris);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_imagegrid, container, false);
        final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(imageAdapter);
        mGridView.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Log.d("ImageGridFragment", String.valueOf(imageUris[position]));
        Intent intent = new Intent();
        intent.putExtra("imageUri", String.valueOf(imageUris[position]));
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getFragmentManager().popBackStack();
    }

    public class ImageAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private File[] imageUrls;

        public ImageAdapter(Context context, File[] imageUrls) {
            super(context, R.layout.imageview_grid, imageUrls);

            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.imageview_grid, parent, false);
            }

            Glide
                    .with(context)
                    .load(imageUrls[position])
                    .into((ImageView) convertView);

            return convertView;
        }
    }
}