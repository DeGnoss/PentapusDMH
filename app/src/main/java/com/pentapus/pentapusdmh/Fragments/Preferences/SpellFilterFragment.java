package com.pentapus.pentapusdmh.Fragments.Preferences;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;

import java.util.List;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class SpellFilterFragment extends DialogFragment {

    Button positiveButton;
    private boolean phb=true, ee=true, scag=true;

    public SpellFilterFragment(){
    }

    public static SpellFilterFragment newInstance() {
        SpellFilterFragment f = new SpellFilterFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }
        phb = SharedPrefsHelper.loadPHBFilter(getContext());
        ee = SharedPrefsHelper.loadEEFilter(getContext());
        scag = SharedPrefsHelper.loadSCAGFilter(getContext());
        setCancelable(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return buildDialog();
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode ==  KeyEvent.KEYCODE_BACK))
                {
                    //This is the filter
                    if (event.getAction()!=KeyEvent.ACTION_DOWN){
                        Log.d("BackButton", "Down");
                        return true;
                    }
                    else
                    {
                        Log.d("BackButton", "Up");
                        getDialog().dismiss();
                        return true; // pretend we've processed it
                    }
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }


    public Dialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Spell Sources");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_spellfilter, null);
        CheckBox phbCheckBox = (CheckBox) view.findViewById(R.id.checkPHB);
        if(SharedPrefsHelper.loadPHBFilter(getContext()))
            phbCheckBox.setChecked(true);
        phbCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    phb = true;
                }else{
                    phb = false;
                }
            }
        });

        CheckBox eeCheckBox = (CheckBox) view.findViewById(R.id.checkEE);
        if(SharedPrefsHelper.loadEEFilter(getContext()))
            eeCheckBox.setChecked(true);
        eeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ee = true;
                }else{
                    ee = false;
                }
            }
        });

        CheckBox scagCheckBox = (CheckBox) view.findViewById(R.id.checkSCAG);
        if(SharedPrefsHelper.loadSCAGFilter(getContext()))
            scagCheckBox.setChecked(true);
        scagCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    scag = true;
                }else{
                    scag = false;
                }
            }
        });
        builder.setView(view);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefsHelper.savePHBFilter(getContext(), phb);
                SharedPrefsHelper.saveEEFilter(getContext(), ee);
                SharedPrefsHelper.saveSCAGFilter(getContext(), scag);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }


    public static void dismissAllDialogs(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();

        if (fragments == null)
            return;

        for (Fragment fragment : fragments) {
            if (fragment instanceof DialogFragment) {
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismissAllowingStateLoss();
            }

            FragmentManager childFragmentManager = fragment.getChildFragmentManager();
            if (childFragmentManager != null)
                dismissAllDialogs(childFragmentManager);
        }
    }

}