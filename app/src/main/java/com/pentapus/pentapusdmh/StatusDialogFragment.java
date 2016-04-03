package com.pentapus.pentapusdmh;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * Created by Koni on 02.04.2016.
 */
public class StatusDialogFragment extends DialogFragment {
    private StatusAdapter statusAdapter;
    private GridLayoutManager gridLayoutManager;
    private int id;
    private boolean blinded;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.dialog_status, container, false);

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
            //statusAdapter.setStatusBlinded(getArguments().getBoolean("blinded"));
            statusAdapter.setStatuses(getArguments().getBooleanArray("statuses"));
        }
        mRecyclerView.setAdapter(statusAdapter);


        Button b1 = (Button) view.findViewById(R.id.bDone);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.putExtra("blinded", statusAdapter.getStatusBlinded());
                intent.putExtra("statuses", statusAdapter.getStatuses());
                intent.putExtra("statusesChanged", true);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                //mListener.onStatusEffectSelected(statusAdapter.getStatusBlinded(), statusAdapter.getStatusCharmed(), statusAdapter.getStatusDeafened());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //TrackerInfoCard current = chars.getList().get(position);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
    }




    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = (View)inflater.inflate(R.layout.dialog_status, null);
        //initialize the numberpicker

        //initialize the button



        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(view);
        //Button b2 = (Button) d.findViewById(R.id.button2);
        //d.show();

        return dialog;
    }
}