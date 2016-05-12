package com.pentapus.pentapusdmh.Fragments.Tracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.R;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class EnterInitiativeDialogFragment extends DialogFragment {

    private int initiativeMod, enteredInitiative, listId;
    private String name;
    Button positiveButton;

    public interface EnterInitiativeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, int enteredInitiative);
        void onDialogNeutralClick(DialogFragment dialog, int enteredInitiative);
    }

    //EnterInitiativeDialogListener mListener;


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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            name = getArguments().getString("name");
            initiativeMod = getArguments().getInt("initiativeMod");
            listId = getArguments().getInt("listId");
        }
        setCancelable(false);
    }



   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EnterInitiativeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }*/



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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Initiative for " + name);
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
                if(etRoll.getText() == null){
                }
                enteredInitiative = Integer.valueOf(etRoll.getText().toString());
                Log.d("Initiative input:", String.valueOf(enteredInitiative));
                sendResult(listId, enteredInitiative);

                //mListener.onDialogPositiveClick(EnterInitiativeDialogFragment.this, enteredInitiative);
                /*
                trackerInfoCard.setInitiative(String.valueOf(enteredInitiative + Integer.valueOf(trackerInfoCard.getInitiativeMod())));
                characterList.add(trackerInfoCard);
                characterList = sortList(characterList);
                notifyDataSetChanged();*/
            }
        });
        builder.setNeutralButton("Roll", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enteredInitiative = DiceHelper.d20();
                Log.d("Initiative roll:", String.valueOf(enteredInitiative));
                sendResult(listId, enteredInitiative);

                //mListener.onDialogNeutralClick(EnterInitiativeDialogFragment.this, enteredInitiative);

                /*trackerInfoCard.setInitiative(String.valueOf(enteredInitiative + Integer.valueOf(trackerInfoCard.getInitiativeMod())));
                characterList.add(trackerInfoCard);
                characterList = sortList(characterList);
                notifyDataSetChanged();*/
            }
        });
        AlertDialog dialog = builder.create();

        etRoll.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    int value = Integer.valueOf(s.toString());
                    if(value >0 && value <=20){
                        positiveButton.setEnabled(true);
                    }else{
                        positiveButton.setEnabled(false);
                    }
                }else{
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




    private void sendResult(int listId, int initiative) {
        ((TrackerFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), listId, initiative);
    }

}