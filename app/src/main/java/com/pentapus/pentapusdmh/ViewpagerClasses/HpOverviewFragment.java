package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.pentapus.pentapusdmh.R;

/**
 * Created by Koni on 02.04.2016.
 */
public class HpOverviewFragment extends Fragment{

    private static final String ARG_PAGE = "ARG_PAGE";


    private int id;
    private boolean[] statuses = new boolean[15];
    private boolean[] oldStatuses = new boolean[15];
    private boolean statusesChanged;



    /*public interface DialogFragmentListener {
        public void onDialogFragmentDone(int id, int hpDiff, boolean[] statuses);
    }*/

    //DialogFragmentListener mListener;



    public static HpOverviewFragment newInstance(int page) {
        HpOverviewFragment fragment = new HpOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  mListener = (DialogFragmentListener) getTargetFragment();
    }


    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.viewpager_hpoverview, container, false);

/*
        if (this.getArguments() != null) {
            id = getArguments().getInt("id");
            oldStatuses = getArguments().getBooleanArray("statuses");
        }*/

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker1);
        np.setMinValue(0);
        np.setMaxValue(999);
        np.setValue(0);
        np.setWrapSelectorWheel(false);

        Button b1 = (Button) view.findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusesChanged) {
                   // mListener.onDialogFragmentDone(id, np.getValue(), statuses);
                } else {
                  //  mListener.onDialogFragmentDone(id, np.getValue(), oldStatuses);
                }
               // getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        /*
        Button bStatus = (Button) view.findViewById(R.id.buttonStatus);
        bStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusFragment newFragment = new StatusFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                newFragment.setTargetFragment(HpOverviewFragment.this, 0);
                Bundle bundle = new Bundle();
                bundle.putBooleanArray("statuses", oldStatuses);
                newFragment.setArguments(bundle);
                Fragment old = fm.findFragmentByTag("F_DIALOG_HP");
                if (old != null) {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(android.R.id.content, newFragment, "F_DIALOG_STATUS")
                            .addToBackStack(null).commit();
                }


            }
        });*/


        /*
        Button bSavingThrow = (Button) view.findViewById(R.id.buttonSavingThrow);
        bSavingThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavingThrowFragment newFragment = new SavingThrowFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                newFragment.setTargetFragment(HpOverviewFragment.this, 0);
                //Bundle bundle = new Bundle();
               // bundle.putBooleanArray("statuses", oldStatuses);
               // newFragment.setArguments(bundle);
                Fragment old = fm.findFragmentByTag("F_DIALOG_HP");
                if (old != null) {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(android.R.id.content, newFragment, "F_DIALOG_SAVINGTHROW")
                            .addToBackStack(null).commit();
                }


            }
        });*/
        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if(data != null) {
               // blinded = data.getBooleanExtra("blinded", false);
                statuses = data.getBooleanArrayExtra("statuses");
                statusesChanged = data.getBooleanExtra("statusesChanged", false);
            }
        }*/
    }
}