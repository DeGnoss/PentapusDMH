package com.pentapus.pentapusdmh;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.pentapus.pentapusdmh.Fragments.TrackerFragment;

/**
 * Created by Koni on 02.04.2016.
 */
public class FullScreenDialog extends DialogFragment{

    private int id;
    private boolean[] statuses = new boolean[15];
    private boolean[] oldStatuses = new boolean[15];
    private boolean statusesChanged;



    public interface DialogFragmentListener {
        public void onDialogFragmentDone(int id, int hpDiff, boolean[] statuses);
    }

    DialogFragmentListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (DialogFragmentListener) getTargetFragment();
    }


    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.dialog_modify, container, false);


        if (this.getArguments() != null) {
            id = getArguments().getInt("id");
            oldStatuses = getArguments().getBooleanArray("statuses");
        }

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker1);
        np.setMinValue(0);
        np.setMaxValue(999);
        np.setValue(0);
        np.setWrapSelectorWheel(false);

        Button b1 = (Button) view.findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusesChanged){
                    mListener.onDialogFragmentDone(id, np.getValue(), statuses);
                }else{
                    mListener.onDialogFragmentDone(id, np.getValue(), oldStatuses);
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button bStatus = (Button) view.findViewById(R.id.buttonStatus);
        bStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusDialogFragment newFragment = new StatusDialogFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                newFragment.setTargetFragment(FullScreenDialog.this, 0);
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
        });
        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view


    }


    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = (View) inflater.inflate(R.layout.dialog_modify, null);
        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(view);
        return dialog;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if(data != null) {
               // blinded = data.getBooleanExtra("blinded", false);
                statuses = data.getBooleanArrayExtra("statuses");
                statusesChanged = data.getBooleanExtra("statusesChanged", false);
            }
        }
    }
}