package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddSpeedDialogFragment extends DialogFragment {

    Button positiveButton;
    EditText tvSpeed, tvSpeedFly, tvSpeedBurrow, tvSpeedClimb, tvSpeedSwim;
    TextView labelSpeed;
    CheckedTextView ctvHover;
    boolean isHover;
    String speedString, speed, speedFly, speedBurrow, speedClimb, speedSwim;


    public AddSpeedDialogFragment() {
    }

    //mode: 0 = add, 1 = update
    public static AddSpeedDialogFragment newInstance(String speedString) {
        AddSpeedDialogFragment f = new AddSpeedDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(AbilitiesPage.SPEED_DATA_KEY, speedString);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            speedString = getArguments().getString(AbilitiesPage.SPEED_DATA_KEY);
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
        ((AbilitiesFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), results);
    }


    public Dialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Speed");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_speed, null);
        if(speedString!= null && !speedString.isEmpty()){
            splitString(speedString);
        }
        tvSpeed = (EditText) view.findViewById(R.id.tvSpeed);
        if(speed != null && !speed.isEmpty()){
            tvSpeed.setText(speed);
        }
        tvSpeedBurrow = (EditText) view.findViewById(R.id.tvSpeedBurrow);
        if(speedBurrow != null && !speedBurrow.isEmpty()){
            tvSpeedBurrow.setText(speedBurrow);
        }
        tvSpeedFly = (EditText) view.findViewById(R.id.tvSpeedFly);
        if(speedFly != null && !speedFly.isEmpty()){
            tvSpeedFly.setText(speedFly);
        }
        tvSpeedClimb = (EditText) view.findViewById(R.id.tvSpeedClimb);
        if(speedClimb != null && !speedClimb.isEmpty()){
            tvSpeedClimb.setText(speedClimb);
        }
        tvSpeedSwim = (EditText) view.findViewById(R.id.tvSpeedSwim);
        if(speedSwim != null && !speedSwim.isEmpty()){
            tvSpeedSwim.setText(speedSwim);
        }
        labelSpeed = (TextView) view.findViewById(R.id.l4);
        ctvHover = (CheckedTextView) view.findViewById(R.id.ctvHover);
        ctvHover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ctvHover.isChecked()){
                    labelSpeed.setText("Fly");
                    isHover = false;
                    ctvHover.setChecked(false);
                }else{
                    ctvHover.setChecked(true);
                    isHover = true;
                    labelSpeed.setText("Fly (Hover)");
                }
            }
        });
        if(isHover){
            ctvHover.setChecked(true);
        }else{
            ctvHover.setChecked(false);
        }



        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                speedString="";
                    speed = tvSpeed.getText().toString();

                    speedBurrow = tvSpeedBurrow.getText().toString();

                    speedClimb = tvSpeedClimb.getText().toString();

                    speedFly = tvSpeedFly.getText().toString();

                    speedSwim = tvSpeedSwim.getText().toString();

                speedString = buildSpeedString(speed, speedBurrow, speedClimb, speedFly, speedSwim, isHover);


                results.putString(AbilitiesPage.SPEED_DATA_KEY, speedString);
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

    private String buildSpeedString(String speed, String speedBurrow, String speedClimb, String speedFly, String speedSwim, boolean isHover){
        String tempSpeed = "";
        if(speed != null && !speed.isEmpty()){
            tempSpeed = speed + " ft.";
        }
        if(speedBurrow != null && !speedBurrow.isEmpty()){
            if(!tempSpeed.isEmpty()){
                tempSpeed = tempSpeed + ", ";
            }
            tempSpeed = tempSpeed + "burrow " + speedBurrow + " ft.";
        }
        if(speedClimb != null && !speedClimb.isEmpty()){
            if(!tempSpeed.isEmpty()){
                tempSpeed = tempSpeed + ", ";
            }
            tempSpeed = tempSpeed + "climb " + speedClimb + " ft.";
        }
        if(speedFly != null && !speedFly.isEmpty()){
            if(!tempSpeed.isEmpty()){
                tempSpeed = tempSpeed + ", ";
            }
            if(isHover){
                tempSpeed = tempSpeed + "fly " + speedFly + " ft. (hover)";
            }else{
                tempSpeed = tempSpeed + "fly " + speedFly + " ft.";
            }
        }
        if(speedSwim != null && !speedSwim.isEmpty()){
            if(!tempSpeed.isEmpty()){
                tempSpeed = tempSpeed + ", ";
            }
            tempSpeed = tempSpeed + "swim " + speedSwim + " ft.";
        }
        return tempSpeed;
    }


    private void splitString(String speedString){
        ArrayList<String> speedSplit = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(speedString, ",");
        while(tokens.hasMoreTokens()){
            speedSplit.add(tokens.nextToken());
        }
        for(int i=0; i<speedSplit.size(); i++){
            if(speedSplit.get(i).contains("burrow")){
                speedBurrow = extractNumber(speedSplit.get(i));
            }else if(speedSplit.get(i).contains("climb")){
                speedClimb = extractNumber(speedSplit.get(i));
            }else if(speedSplit.get(i).contains("fly")){
                speedFly = extractNumber(speedSplit.get(i));
                if(speedSplit.get(i).contains("hover")){
                    isHover = true;
                }else{
                    isHover = false;
                }
            }else if(speedSplit.get(i).contains("swim")){
                speedSwim = extractNumber(speedSplit.get(i));
            }else{
                speed = extractNumber(speedSplit.get(i));
            }
        }
    }


    public static String extractNumber(final String str) {

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }

}