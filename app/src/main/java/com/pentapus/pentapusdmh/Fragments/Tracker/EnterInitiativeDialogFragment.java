package com.pentapus.pentapusdmh.Fragments.Tracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class EnterInitiativeDialogFragment extends DialogFragment {

    private int enteredInitiative, currentId;
    Button positiveButton;
    String name;
    private int listId, initiativeMod;


    public EnterInitiativeDialogFragment(){
    }

    public static EnterInitiativeDialogFragment newInstance(String name, int initiativeMod, int listId) {
        EnterInitiativeDialogFragment f = new EnterInitiativeDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putInt("initiativeMod", initiativeMod);
        args.putInt("listId", listId);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            //unchecked cast warning, just have to be sure the serializable object is a HashMap<Integer, TrackerInfoCard>
            name = getArguments().getString("name");
            initiativeMod = getArguments().getInt("initiativeMod");
            listId = getArguments().getInt("listId");
        }
        setCancelable(false);
    }



    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            positiveButton = (Button)d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
        }

    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return buildDialog(name, initiativeMod, listId);
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,
                                 android.view.KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    //This is the filter
                    if (event.getAction()!=KeyEvent.ACTION_DOWN){
                        Log.d("BackButton", "Down");
                        return true;
                    }
                    else
                    {
                        Log.d("BackButton", "Up");

                        showDismissDialog();
                        return true; // pretend we've processed it
                    }
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }



    private void sendResult(int listId, int initiative) {
        Log.d("Send result:", String.valueOf(listId));
        ((TrackerFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), listId, initiative);
    }

    public void showDismissDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("Exit Tracker")
                .setMessage("Are you sure you want to exit the Tracker? The encounter will be reset.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dismissAllDialogs(getActivity().getSupportFragmentManager());
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.ic_warning_black_24dp)
                .show();
    }


    public Dialog buildDialog(String name, int initiativeMod, final int listId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter initiative roll for " + name);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_initiative, null);
        final EditText etRoll = (EditText) view.findViewById(R.id.initiativeRoll);
        TextView tvMod = (TextView) view.findViewById(R.id.initiativeMod);
        tvMod.setText(String.valueOf(initiativeMod));
        builder.setView(view);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etRoll.getText() == null) {
                }
                enteredInitiative = Integer.valueOf(etRoll.getText().toString());
                Log.d("Initiative input:", String.valueOf(enteredInitiative));
                sendResult(listId, enteredInitiative);
            }
        });
        builder.setNeutralButton("Roll", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                enteredInitiative = DiceHelper.d20();
                Log.d("Initiative roll:", String.valueOf(enteredInitiative));
                sendResult(listId, enteredInitiative);
            }
        });
        AlertDialog dialog = builder.create();

        etRoll.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int value = Integer.valueOf(s.toString());
                    if (value > 0 && value <= 20) {
                        positiveButton.setEnabled(true);
                    } else {
                        positiveButton.setEnabled(false);
                    }
                } else {
                    positiveButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // Create the AlertDialog object and return it
        return dialog;
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