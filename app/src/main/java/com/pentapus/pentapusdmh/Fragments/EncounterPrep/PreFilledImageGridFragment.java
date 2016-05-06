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
import com.pentapus.pentapusdmh.BaseFragment;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;

import java.io.File;

/**
 * Created by Koni on 02.04.2016.
 */
public class PreFilledImageGridFragment extends BaseFragment implements AdapterNavigationCallback, ImageViewPagerDialogFragment.UpdateableFragment {
    private PreFilledImageGridAdapter imageGridAdapter;
    private GridLayoutManager gridLayoutManager;
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
        //selectedPos = ((MonsterEditFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER"));
    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setFabVisibility(false);
        View view = inflater.inflate(R.layout.my_icons_grid, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.gridViewMyIcons);
        gridLayoutManager = new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        imageGridAdapter = new PreFilledImageGridAdapter(getContext(), this);
        mRecyclerView.setAdapter(imageGridAdapter);
        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }

    private void onClick(int position) {
        imageGridAdapter.statusClicked(position);
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
    }

    @Override
    public void onMenuRefresh() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            imageGridAdapter.notifyDataSetChanged();
        }
    }
}