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
import com.wizardpager.wizard.model.Page;

import java.util.List;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddTraitDialogFragment extends DialogFragment {

    Button positiveButton;
    EditText tvTrait1Name, tvTrait1Description;
    int mode = 0, traitNumber;
    String name, description, title;


    public AddTraitDialogFragment(){
    }

    //mode: 0 = add, 1 = update
    public static AddTraitDialogFragment newInstance(int mode, String name, String description, int traitNumber, String title) {
        AddTraitDialogFragment f = new AddTraitDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putInt("traitNumber", traitNumber);
        args.putString("title", title);
        if(name != null){
            args.putString("monstername", name);
        }
        if(description != null){
            args.putString("description", description);
        }
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mode = getArguments().getInt("mode");
            name = getArguments().getString("monstername");
            description = getArguments().getString("description");
            traitNumber = getArguments().getInt("traitNumber");
            title = getArguments().getString("title");
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
                        dialog.dismiss();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        return true; // pretend we've processed it
                    }
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }



    private void sendResult(int mode, Bundle results) {
        ((TraitsFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), mode, results);
    }


    public Dialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title + " " + (traitNumber+1));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_createtrait, null);
        tvTrait1Name = (EditText) view.findViewById(R.id.tvTrait1Name);
        tvTrait1Description = (EditText) view.findViewById(R.id.tvTrait1Description);
        if(mode == 1){
            tvTrait1Name.setText(name);
            tvTrait1Description.setText(description);
        }
        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = tvTrait1Name.getText().toString();
                description = tvTrait1Description.getText().toString();
                Bundle results = new Bundle();
                results.putString("monstername", name);
                results.putString("description", description);
                results.putInt("traitNumber", traitNumber);
                sendResult(mode, results);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        builder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                results.putString("monstername", "");
                results.putString("description", "");
                results.putInt("traitNumber", traitNumber);
                sendResult(2, results);
                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        AlertDialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }




}