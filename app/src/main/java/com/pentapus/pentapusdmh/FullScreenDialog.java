package com.pentapus.pentapusdmh;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
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
public class FullScreenDialog extends DialogFragment {

    private int id;
    public interface DialogFragmentListener {
        public void onDialogFragmentDone(int id, int hpDiff);
    }

    DialogFragmentListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (DialogFragmentListener) getTargetFragment();
    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.dialog_modify, null);

        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.dialog_modify, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        if (this.getArguments() != null) {
            id = Integer.parseInt(getArguments().getString("id"));
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
                mListener.onDialogFragmentDone(id, np.getValue());
                //TrackerFragment.onDialogButtonClick();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
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

        View view = (View)inflater.inflate(R.layout.dialog_modify, null);
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