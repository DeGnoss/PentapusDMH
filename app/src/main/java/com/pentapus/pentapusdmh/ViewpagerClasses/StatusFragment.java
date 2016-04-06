package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pentapus.pentapusdmh.Fragments.TrackerFragment;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.RecyclerItemClickListener;
import com.pentapus.pentapusdmh.TrackerActivityListener;
import com.pentapus.pentapusdmh.TrackerInfoCard;

/**
 * Created by Koni on 02.04.2016.
 */
public class StatusFragment extends Fragment {
    private StatusAdapter statusAdapter;
    private static final String ARG_PAGE = "ARG_PAGE";
    private GridLayoutManager gridLayoutManager;
    private int mPage;
    private boolean[] statuses;
    TrackerActivityListener mListener;


    public static StatusFragment newInstance(int page) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
        statuses = ((TrackerFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getmSaveTrackerInfoCard().getStatuses();
    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.viewpager_status, container, false);

        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.listViewEncounter);
        gridLayoutManager = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        statusAdapter = new StatusAdapter();
        if (this.getArguments() != null) {
            statusAdapter.setStatuses(statuses);
        }
        mRecyclerView.setAdapter(statusAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        onClick(position);
                    }
                })
        );
        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }

    private void onClick(int position) {
        Log.d("Status clicked", String.valueOf(position));
        statusAdapter.statusClicked(position);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mListener = (TrackerActivityListener) context;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
    }

    public void saveChanges(){
        mListener.getTrackerInfoCard().setStatuses(statuses);
        Log.d("Test", "test");
    }
}