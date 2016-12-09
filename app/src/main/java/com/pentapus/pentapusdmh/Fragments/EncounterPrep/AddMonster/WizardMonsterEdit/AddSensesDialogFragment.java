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
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddSensesDialogFragment extends DialogFragment {

    Button positiveButton;
    EditText tvDarkvision, tvBlindsight, tvTremorsense, tvTruesight, tvPassivePerception, tvOther;
    String senses, other, wisdom;
    String darkvision, blindsight, tremorsense, truesight;
    CheckedTextView ctvCalculateFromWisdom;
    String passivePerception;
    boolean isNotCalculatedFromWisdom;

    public AddSensesDialogFragment() {
    }

    //mode: 0 = add, 1 = update
    public static AddSensesDialogFragment newInstance(String senses, String wisdom, boolean isNotCalculatedFromWisdom) {
        AddSensesDialogFragment f = new AddSensesDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("senses", senses);
        args.putString("wisdom", wisdom);
        args.putBoolean("isNotCalculatedFromWisdom", isNotCalculatedFromWisdom);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            senses = getArguments().getString("senses");
            wisdom = getArguments().getString("wisdom");
            isNotCalculatedFromWisdom = getArguments().getBoolean("isNotCalculatedFromWisdom");
        }
        if(senses!= null){
            ArrayList<String> selectedItems = splitString(senses);
            if (selectedItems != null || selectedItems.size() != 0) {

                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i).contains("darkvision")) {
                        darkvision = String.valueOf(getOnlyNumerics(selectedItems.get(i)));
                    }else if(selectedItems.get(i).contains("blindsight")){
                        blindsight = String.valueOf(getOnlyNumerics(selectedItems.get(i)));
                    }else if(selectedItems.get(i).contains("tremorsense")){
                        tremorsense = String.valueOf(getOnlyNumerics(selectedItems.get(i)));
                    }else if(selectedItems.get(i).contains("truesight")){
                        truesight = String.valueOf(getOnlyNumerics(selectedItems.get(i)));
                    }else if(selectedItems.get(i).contains("passive Perception")){
                        passivePerception = String.valueOf(getOnlyNumerics(selectedItems.get(i)));
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
        ctvCalculateFromWisdom = (CheckedTextView) view.findViewById(R.id.ctvPassivePerception);
        ctvCalculateFromWisdom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ctvCalculateFromWisdom.isChecked()){
                    isNotCalculatedFromWisdom = true;
                    ctvCalculateFromWisdom.setChecked(false);
                    tvPassivePerception.setEnabled(true);
                    tvPassivePerception.setHint("e.g. 12");
                }else{
                    ctvCalculateFromWisdom.setChecked(true);
                    isNotCalculatedFromWisdom = false;
                    tvPassivePerception.setEnabled(false);
                    if(wisdom != null && !wisdom.isEmpty()){
                        passivePerception = String.valueOf(AbilityModifierCalculator.calculateMod(Integer.valueOf(wisdom)) + 10);
                        tvPassivePerception.setHint("e.g. 12");
                    }else{
                        passivePerception = "";
                        tvPassivePerception.setHint("");
                    }
                    tvPassivePerception.setText(passivePerception);
                }
            }
        });
        if(!isNotCalculatedFromWisdom){
            ctvCalculateFromWisdom.setChecked(true);
            tvPassivePerception.setEnabled(false);
            if(wisdom == null || wisdom.isEmpty()){
                tvPassivePerception.setHint("");
            }else{
                tvPassivePerception.setHint("e.g. 12");
            }
        }else{
            ctvCalculateFromWisdom.setChecked(false);
            tvPassivePerception.setEnabled(true);
            tvPassivePerception.setHint("e.g. 12");
        }

        tvDarkvision.setText(darkvision);
        tvBlindsight.setText(blindsight);
        tvTremorsense.setText(tremorsense);
        tvTruesight.setText(truesight);
        if(passivePerception != null && !passivePerception.isEmpty()){
            tvPassivePerception.setText(passivePerception);
        }else{
            if(!isNotCalculatedFromWisdom && wisdom != null && !wisdom.isEmpty()){
                int tempPassivePerception = AbilityModifierCalculator.calculateMod(Integer.valueOf(wisdom)) + 10;
                tvPassivePerception.setText(String.valueOf(tempPassivePerception));
            }
        }
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
                    darkvision = tvDarkvision.getText().toString();
                }else{
                    darkvision = "";
                }
                if(!tvBlindsight.getText().toString().isEmpty()){
                    blindsight = tvBlindsight.getText().toString();
                }else{
                    blindsight = "";
                }
                if(!tvTremorsense.getText().toString().isEmpty()){
                    tremorsense = tvTremorsense.getText().toString();
                }else{
                    tremorsense = "";
                }
                if(!tvTruesight.getText().toString().isEmpty()){
                    truesight = tvTruesight.getText().toString();
                }else{
                    truesight = "";
                }
                if(!tvPassivePerception.getText().toString().isEmpty()){
                    passivePerception = tvPassivePerception.getText().toString();
                }else{
                    passivePerception = "";
                }
                results.putString("senses", buildSensesString(darkvision, blindsight, tremorsense, truesight, passivePerception, tvOther.getText().toString()));
                results.putBoolean("isNotCalculatedFromWisdom", isNotCalculatedFromWisdom);
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

    public String buildSensesString(String darkvision, String blindsight, String tremorsense, String truesight, String passivePerception, String other){
        String senses = "";
        if(darkvision != null && !darkvision.isEmpty()){
            senses = "darkvision " + darkvision + " ft.";
        }
        if(blindsight != null && !blindsight.isEmpty()){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", blindsight " + blindsight + " ft.";
            }else{
                senses = "blindsight " + blindsight + " ft.";
            }
        }
        if(tremorsense != null && !tremorsense.isEmpty()){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", tremorsense " + tremorsense + " ft.";
            }else{
                senses = "tremorsense " + tremorsense + " ft.";
            }
        }
        if(truesight != null && !truesight.isEmpty()){
            if(senses != null && !senses.isEmpty()){
                senses = senses + ", truesight " + truesight + " ft.";
            }else{
                senses = "truesight " + truesight + " ft.";
            }
        }
        if(passivePerception != null && !passivePerception.isEmpty()){
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