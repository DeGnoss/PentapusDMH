package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

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
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.R;

import java.util.List;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddTraitDialogFragment extends DialogFragment {

    Button positiveButton;
    String name, description;
    EditText tvTrait1Name, tvTrait1Description;


    public AddTraitDialogFragment(){
    }

    public static AddTraitDialogFragment newInstance() {
        AddTraitDialogFragment f = new AddTraitDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
        }
        setCancelable(true);
    }



    @Override
    public void onStart() {
        super.onStart();
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
                        return true;
                    }
                    else
                    {
                        getActivity().getSupportFragmentManager().popBackStack();
                        return true; // pretend we've processed it
                    }
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }



    private void sendResult(String name, String description) {
        ((TraitsFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), name, description);
    }


    public Dialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("New Trait");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_createtrait, null);
        tvTrait1Name = (EditText) view.findViewById(R.id.tvTrait1Name);
        tvTrait1Description = (EditText) view.findViewById(R.id.tvTrait1Description);
        builder.setView(view);


        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = tvTrait1Name.getText().toString();
                description = tvTrait1Description.getText().toString();
                sendResult(name, description);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        AlertDialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }




}