package com.pentapus.pentapusdmh.Fragments.EncounterPrep;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.R;

import java.io.File;

/**
 * Created by Koni on 02.04.2016.
 */
public class PreFilledImageGridFragment extends Fragment implements AdapterNavigationCallback, ImageViewPagerDialogFragment.UpdateableFragment {
    private PreFilledImageGridAdapter imageGridAdapter;
    private GridLayoutManager gridLayoutManager;
    private static int selectedPos = -1;
    private int id;
    private ActionMode mActionMode;
    private RecyclerView mRecyclerView;

    @Override
    public void update(File[] updateData) {
        imageGridAdapter.setImageUris(updateData);
        imageGridAdapter.notifyDataSetChanged();
    }


    public static PreFilledImageGridFragment newInstance(int id) {
        PreFilledImageGridFragment fragment = new PreFilledImageGridFragment();
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
        //TODO get selectedPos
        //selectedPos = ((NPCEditFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER"));
    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.my_icons_grid, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.gridViewMyIcons);
        gridLayoutManager = new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        imageGridAdapter = new PreFilledImageGridAdapter(getContext(), this);
        imageGridAdapter.setSelectedPos(selectedPos);
        mRecyclerView.setAdapter(imageGridAdapter);
        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }

    private void onClick(int position) {
        imageGridAdapter.statusClicked(position);
        ImageGridAdapter.setSelectedUri(Uri.parse(imageGridAdapter.getImageUris()[position].toString()));
        selectedPos = position;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
    }

    @Override
    public void onItemClick(int position) {
        onClick(position);
    }

    @Override
    public void onItemLongCLick(final int position) {
        ImageGridAdapter.setHighlightedPos(position);
        int oldPos = ImageGridAdapter.getSelectedPos();
        imageGridAdapter.setSelectedPos(position);
        imageGridAdapter.notifyItemChanged(oldPos);
        imageGridAdapter.notifyItemChanged(position);
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
                        File fileDelete = ((ImageGridAdapter)mRecyclerView.getAdapter()).getImageUris()[position];
                        fileDelete.delete();
                        ((ImageGridAdapter)mRecyclerView.getAdapter()).updateUris();
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
                ImageGridAdapter.setHighlightedPos(-1);
                imageGridAdapter.setSelectedPos(-1);
                imageGridAdapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    public void onMenuRefresh() {

    }

    public static int getSelectedPos() {
        return selectedPos;
    }
}