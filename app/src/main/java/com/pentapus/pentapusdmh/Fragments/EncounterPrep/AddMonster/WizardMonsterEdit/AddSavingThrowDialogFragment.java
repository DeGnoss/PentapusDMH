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

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddSavingThrowDialogFragment extends DialogFragment {

    Button positiveButton;
    EditText tvStr, tvDex, tvCon, tvInt, tvWis, tvCha;
    String stStr, stDex, stCon, stInt, stWis, stCha;


    public AddSavingThrowDialogFragment(){
    }

    //mode: 0 = add, 1 = update
    public static AddSavingThrowDialogFragment newInstance(String stStr, String stDex, String stCon, String stInt, String stWis, String stCha) {
        AddSavingThrowDialogFragment f = new AddSavingThrowDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(SkillsPage.STSTR_DATA_KEY, stStr);
        args.putString(SkillsPage.STDEX_DATA_KEY, stDex);
        args.putString(SkillsPage.STCON_DATA_KEY, stCon);
        args.putString(SkillsPage.STINT_DATA_KEY, stInt);
        args.putString(SkillsPage.STWIS_DATA_KEY, stWis);
        args.putString(SkillsPage.STCHA_DATA_KEY, stCha);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            stStr = getArguments().getString(SkillsPage.STSTR_DATA_KEY);
            stDex = getArguments().getString(SkillsPage.STDEX_DATA_KEY);
            stCon = getArguments().getString(SkillsPage.STCON_DATA_KEY);
            stInt = getArguments().getString(SkillsPage.STINT_DATA_KEY);
            stWis = getArguments().getString(SkillsPage.STWIS_DATA_KEY);
            stCha = getArguments().getString(SkillsPage.STCHA_DATA_KEY);
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



    private void sendResult(Bundle results){
        ((SkillsFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), results);
    }


    public Dialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Saving Throw");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_savingthrows, null);
        tvStr = (EditText) view.findViewById(R.id.tvStr);
        tvDex = (EditText) view.findViewById(R.id.tvDex);
        tvCon = (EditText) view.findViewById(R.id.tvCon);
        tvInt = (EditText) view.findViewById(R.id.tvInt);
        tvWis = (EditText) view.findViewById(R.id.tvWis);
        tvCha = (EditText) view.findViewById(R.id.tvCha);


        tvStr.setText(stStr);
        tvDex.setText(stDex);
        tvCon.setText(stCon);
        tvInt.setText(stInt);
        tvWis.setText(stWis);
        tvCha.setText(stCha);

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                if(!tvStr.getText().toString().isEmpty()){
                    results.putString(SkillsPage.STSTR_DATA_KEY, tvStr.getText().toString());
                }
                if(!tvDex.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.STDEX_DATA_KEY, tvDex.getText().toString());
                }
                if(!tvCon.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.STCON_DATA_KEY, tvCon.getText().toString());
                }
                if(!tvInt.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.STINT_DATA_KEY, tvInt.getText().toString());
                }
                if(!tvWis.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.STWIS_DATA_KEY, tvWis.getText().toString());
                }
                if(!tvCha.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.STCHA_DATA_KEY, tvCha.getText().toString());
                }

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




}