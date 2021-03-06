package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerInfoCard;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.RecyclerItemClickListener;

/**
 * Created by Koni on 02.04.2016.
 */
public class StatusFragment extends Fragment{
    private StatusAdapter statusAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean[] statuses;
    private int id;
    private TrackerInfoCard selectedCharacter;


    public static StatusFragment newInstance(int id) {
        StatusFragment fragment = new StatusFragment();
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
        selectedCharacter = ((TrackerFragment) getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().getItem(id);
        statuses = selectedCharacter.getStatuses();
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
                return true;
            }
        };
        mRecyclerView.setLayoutManager(gridLayoutManager);
        statusAdapter = new StatusAdapter();
        statusAdapter.setStatuses(statuses);
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
        statusAdapter.statusClicked(position);
        selectedCharacter.setStatuses(statusAdapter.getStatuses());
        ((TrackerFragment) getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().notifyDataSetChanged();
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

    public void saveChanges(){
        selectedCharacter.setStatuses(statusAdapter.getStatuses());
        //((TrackerFragment)mListener.getTrackerFragment()).getChars().setStatuses(id, statuses);
    }
}