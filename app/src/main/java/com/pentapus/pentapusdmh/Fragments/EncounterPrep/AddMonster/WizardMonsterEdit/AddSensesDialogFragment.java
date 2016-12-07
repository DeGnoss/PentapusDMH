package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddSensesDialogFragment extends DialogFragment {

    Button positiveButton;
    EditText tvDarkvision, tvBlindsight, tvTremorsense, tvTruesight, tvPassivePerception, tvOther;
    String senses, other;
    int darkvision, blindsight, tremorsense, truesight, passivePerception;


    public AddSensesDialogFragment() {
    }

    //mode: 0 = add, 1 = update
    public static AddSensesDialogFragment newInstance(String senses) {
        AddSensesDialogFragment f = new AddSensesDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("senses", senses);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            senses = getArguments().getString("senses");
        }
        if(senses!= null){
            ArrayList<String> selectedItems = splitString(senses);
            if (selectedItems != null || selectedItems.size() != 0) {

                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i).contains("darkvision")) {
                        darkvision = getOnlyNumerics(selectedItems.get(i));
                    }else if(selectedItems.get(i).contains("blindsight")){
                        blindsight = getOnlyNumerics(selectedItems.get(i));
                    }else if(selectedItems.get(i).contains("tremorsense")){
                        tremorsense = getOnlyNumerics(selectedItems.get(i));
                    }else if(selectedItems.get(i).contains("truesight")){
                        truesight = getOnlyNumerics(selectedItems.get(i));
                    }else if(selectedItems.get(i).contains("passive Perception")){
                        passivePerception = getOnlyNumerics(selectedItems.get(i));
                    }else{
                        other = selectedItems.get(i);
                    }
                }
            }
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

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return true;
                    } else {
                        dialog.dismiss();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }


    private void sendResult(Bundle results) {
        ((SkillsFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), results);
    }


    public Dialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Senses");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_senses, null);
        tvDarkvision = (EditText) view.findViewById(R.id.tvDarkvision);
        tvBlindsight = (EditText) view.findViewById(R.id.tvBlindsight);
        tvTremorsense = (EditText) view.findViewById(R.id.tvTremorsense);
        tvTruesight = (EditText) view.findViewById(R.id.tvTruesight);
        tvPassivePerception = (EditText) view.findViewById(R.id.tvPassivePerception);
        tvOther = (EditText) view.findViewById(R.id.tvOther);

        tvDarkvision.setText(String.valueOf(darkvision));
        tvBlindsight.setText(String.valueOf(blindsight));
        tvTremorsense.setText(String.valueOf(tremorsense));
        tvTruesight.setText(String.valueOf(truesight));
        tvPassivePerception.setText(String.valueOf(passivePerception));
        if(other != null){
            tvOther.setText(other);
        }

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                if(!tvDarkvision.getText().toString().isEmpty()){
                    darkvision = Integer.parseInt(tvDarkvision.getText().toString());
                }else{
                    darkvision = 0;
                }
                if(!tvBlindsight.getText().toString().isEmpty()){
                    blindsight = Integer.parseInt(tvBlindsight.getText().toString());
                }else{
                    blindsight = 0;
                }
                if(!tvTremorsense.getText().toString().isEmpty()){
                    tremorsense = Integer.parseInt(tvTremorsense.getText().toString());
                }else{
                    tremorsense = 0;
                }
                if(!tvTruesight.getText().toString().isEmpty()){
                    truesight = Integer.parseInt(tvTruesight.getText().toString());
                }else{
                    truesight = 0;
                }
                if(!tvPassivePerception.getText().toString().isEmpty()){
                    passivePerception = Integer.parseInt(tvPassivePerception.getText().toString());
                }else{
                    passivePerception = 0;
                }
                results.putString("senses", buildSensesString(darkvision, blindsight, tremorsense, truesight, passivePerception, tvOther.getText().toString()));
                sendResult(results);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        AlertDialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }

    private ArrayList<String> splitString(String senses) {
        ArrayList<String> sensesList = new ArrayList<>();
        StringTokenizer tokens = new StringTokenizer(senses, ",");
        while (tokens.hasMoreTokens()) {
            sensesList.add(tokens.nextToken().trim());
        }
        return sensesList;
    }

    public static int getOnlyNumerics(String str) {
        StringBuffer strBuff = new StringBuffer();
        char c;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return Integer.valueOf(strBuff.toString());
    }

    public String buildSensesString(int darkvision, int blindsight, int tremorsense, int truesight, int passivePerception, String other){
        String senses = null;
        if(darkvision != 0){
            senses = "darkvision " + darkvision + " ft.";
        }
        if(blindsight != 0){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", blindsight " + blindsight + " ft.";
            }else{
                senses = "blindsight " + blindsight + " ft.";
            }
        }
        if(tremorsense != 0){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", tremorsense " + tremorsense + " ft.";
            }else{
                senses = "tremorsense " + tremorsense + " ft.";
            }
        }
        if(truesight != 0){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", truesight " + truesight + " ft.";
            }else{
                senses = "truesight " + truesight + " ft.";
            }
        }
        if(passivePerception != 0){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", passive Perception " + passivePerception;
            }else{
                senses = "passive Perception " + passivePerception;
            }
        }
        if(other != null && !other.isEmpty()){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", " + other;
            }else{
                senses = other;
            }
        }
        return senses;
    }


}