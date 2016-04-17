package com.pentapus.pentapusdmh.Fragments.EncounterPrep;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pentapus.pentapusdmh.R;

import java.io.File;

public class ViewPagerMyImageGridFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private File imageUris[];
    private ImageAdapter imageAdapter;
    private ActionMode mActionMode;
    private GridView mGridView;
    private int id;

    // A static dataset to back the GridView adapter

    // Empty constructor as per Fragment docs
    public ViewPagerMyImageGridFragment() {
    }

    public static ViewPagerMyImageGridFragment newInstance(int id) {
        ViewPagerMyImageGridFragment fragment = new ViewPagerMyImageGridFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());

        // path to /data/data/yourapp/app_data/iconDir
        File directory = cw.getDir("myIconDir", Context.MODE_PRIVATE);
        imageUris = directory.listFiles();
        imageAdapter = new ImageAdapter(getContext(), imageUris);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_imagegrid, container, false);
        mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(imageAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //set selected
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                String title = "Selected: " + String.valueOf(position);
                mode.setTitle(title);
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_imagegrid, menu);
                //fab.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        File fileDelete = imageUris[position];
                        fileDelete.delete();
                        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
                        File directory = cw.getDir("myIconDir", Context.MODE_PRIVATE);
                        imageUris = null;
                        imageUris = directory.listFiles();
                        ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUris);
                        mGridView.setAdapter(imageAdapter);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //fab.setVisibility(View.VISIBLE);
                mActionMode = null;
            }
        });
        return true;
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
                    .fitCenter()
                    .into((ImageView) convertView);

            return convertView;
        }
    }


    public void updateView(){
        imageUris = null;
        ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
        File directory = cw.getDir("myIconDir", Context.MODE_PRIVATE);
        imageUris = directory.listFiles();
        ImageAdapter imageAdapter = new ImageAdapter(getContext(), imageUris);
        mGridView.setAdapter(imageAdapter);
    }
}