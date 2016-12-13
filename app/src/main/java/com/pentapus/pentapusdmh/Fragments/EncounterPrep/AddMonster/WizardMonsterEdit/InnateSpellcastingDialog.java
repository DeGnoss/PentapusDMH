package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.pentapus.pentapusdmh.R;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class InnateSpellcastingDialog extends DialogFragment implements AdapterView.OnItemSelectedListener{

    EditText tvSpellMod, tvSpellDC;
    Spinner spSpellcastingAbility;
    Fragment targetFragment;
    String innateability, innatemod, innatedc;
    CheckedTextView ctvPsionics;
    boolean isPsionics;

    public InnateSpellcastingDialog() {
    }

    //mode: 0 = add, 1 = update
    public static InnateSpellcastingDialog newInstance(String scability, String scmod, String scdc, boolean psionics) {
        InnateSpellcastingDialog f = new InnateSpellcastingDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("innateability", scability);
        args.putString("innatemod", scmod);
        args.putString("innatedc", scdc);
        args.putBoolean("psionics", psionics);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            innateability = getArguments().getString("innateability");
            innatemod = getArguments().getString("innatemod");
            innatedc = getArguments().getString("innatedc");
            isPsionics = getArguments().getBoolean("psionics");
        }
        targetFragment = this;
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


    private void sendResult(int mode, Bundle results) {
        ((TraitsFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), mode, results);
    }


    public Dialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Innate spellcasting");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_innatespellcasting, null);
        spSpellcastingAbility = ((Spinner) view.findViewById(R.id.spSpellcastingAbility));
        tvSpellMod = (EditText) view.findViewById(R.id.tvSpellMod);
        if(innatemod != null && !innatemod.isEmpty()){
            tvSpellMod.setText(innatemod);
        }
        tvSpellDC = (EditText) view.findViewById(R.id.tvSpellDC);
        if(innatedc != null && !innatedc.isEmpty()){
            tvSpellDC.setText(innatedc);
        }

        ArrayAdapter<CharSequence> adapterAbility = ArrayAdapter.createFromResource(getContext(),
                R.array.spellcasting_ability, android.R.layout.simple_spinner_item);
        adapterAbility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpellcastingAbility.setAdapter(adapterAbility);
        spSpellcastingAbility.setOnItemSelectedListener(this);

        if(innateability != null && !innateability.isEmpty()){
            spSpellcastingAbility.setSelection(adapterAbility.getPosition(innateability));
        }else{
            innateability = spSpellcastingAbility.getItemAtPosition(0).toString();
        }

        ctvPsionics = (CheckedTextView) view.findViewById(R.id.ctvPsionics);
        ctvPsionics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ctvPsionics.isChecked()){
                    isPsionics = false;
                    ctvPsionics.setChecked(false);
                }else{
                    ctvPsionics.setChecked(true);
                    isPsionics = true;
                }
            }
        });

        if(isPsionics){
            ctvPsionics.setChecked(true);
        }else{
            ctvPsionics.setChecked(false);
        }

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                if(!tvSpellMod.getText().toString().isEmpty()){
                    results.putString(TraitsPage.INNATEMOD_DATA_KEY, tvSpellMod.getText().toString());
                }
                if(!tvSpellDC.getText().toString().isEmpty()){
                    results.putString(TraitsPage.INNATEDC_DATA_KEY, tvSpellDC.getText().toString());
                }
                results.putString(TraitsPage.INNATEABILITY_DATA_KEY, innateability);
                results.putBoolean(TraitsPage.INNATEPSIONICS_DATA_KEY, isPsionics);
                results.putBoolean(TraitsPage.INNATE_DATA_KEY, true);
                sendResult(-1, results);
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
                results.putIntArray(TraitsPage.INNATEMOD_DATA_KEY, null);
                results.putString(TraitsPage.INNATEDC_DATA_KEY, "");
                results.putString(TraitsPage.INNATEABILITY_DATA_KEY, "");
                results.putBoolean(TraitsPage.INNATEPSIONICS_DATA_KEY, false);
                results.putBoolean(TraitsPage.INNATE_DATA_KEY, false);
                sendResult(-3, results);
                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        AlertDialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.spSpellcastingAbility:
                innateability = adapterView.getItemAtPosition(i).toString();
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}