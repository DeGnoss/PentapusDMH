package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.pentapus.pentapusdmh.HelperClasses.HitDiceCalculator;
import com.pentapus.pentapusdmh.R;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddHPDialogFragment extends DialogFragment{

    Button positiveButton;
    EditText tvhp, tvHdNumber;
    TextView tvHdMod;
    CheckBox cbCalculate;
    String hp, size;
    int conmod, hitdice;
    String[] item;
    boolean dontCalculateAutomatically;


    public AddHPDialogFragment() {
    }

    //mode: 0 = add, 1 = update
    public static AddHPDialogFragment newInstance(String hp, int conmod, String size, int hitdice, boolean calculateAutomatically) {
        AddHPDialogFragment f = new AddHPDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("hp", hp);
        args.putInt("conmod", conmod);
        args.putString("size", size);
        args.putInt("hitdice", hitdice);
        args.putBoolean("dontCalculateAutomatically", calculateAutomatically);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hp = getArguments().getString("hp");
            conmod = getArguments().getInt("conmod");
            size = getArguments().getString("size");
            hitdice = getArguments().getInt("hitdice");
            dontCalculateAutomatically = getArguments().getBoolean("dontCalculateAutomatically");
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
        builder.setTitle("Hit Points");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_hp, null);
        tvhp = (EditText) view.findViewById(R.id.tvHP);
        //tvAC1Type = (EditText) view.findViewById(R.id.tvAC1Type);
        tvHdNumber = (EditText) view.findViewById(R.id.tvhdnumber);
        cbCalculate = (CheckBox) view.findViewById(R.id.ctvHP);
        cbCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbCalculate.isChecked()){
                    cbCalculate.setChecked(true);
                    dontCalculateAutomatically = false;
                    tvhp.setEnabled(false);
                    hp = String.valueOf(HitDiceCalculator.calculateAverageHp(hitdice, size, conmod));
                    tvhp.setText(hp);
                }else{
                    dontCalculateAutomatically = true;
                    cbCalculate.setChecked(false);
                    tvhp.setEnabled(true);
                    tvhp.setHint("e.g. 64");
                }
            }
        });
        if(!dontCalculateAutomatically){
            cbCalculate.setChecked(true);
            hp = String.valueOf(HitDiceCalculator.calculateAverageHp(hitdice, size, conmod));
            tvhp.setText(hp);
            tvhp.setEnabled(false);
        }else{
            cbCalculate.setChecked(false);
            tvhp.setEnabled(true);
        }
        if(hitdice != 0){
            tvHdNumber.setText(String.valueOf(hitdice));
        }
        tvHdMod = (TextView) view.findViewById(R.id.tvhdmod);
        tvHdMod.setText(HitDiceCalculator.calculateHdType(hitdice, size, conmod));

        if (hp != null && !hp.isEmpty() && !hp.equals("0")) {
            tvhp.setText(hp);
        }

        tvHdNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence!= null && !charSequence.toString().isEmpty()){
                    hitdice = Integer.valueOf(charSequence.toString());
                    //bCalculate.setEnabled(true);
                }else{
                    hitdice = 0;
                }
                tvHdMod.setText(HitDiceCalculator.calculateHdType(hitdice, size, conmod));
                if(!dontCalculateAutomatically){
                    hp = String.valueOf(HitDiceCalculator.calculateAverageHp(hitdice, size, conmod));
                    tvhp.setText(hp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                if (!tvhp.getText().toString().isEmpty()) {
                    hp = tvhp.getText().toString();
                } else {
                    hp = String.valueOf(HitDiceCalculator.calculateAverageHp(hitdice, size, conmod));
                }
                results.putString("hp", hp);
                results.putInt("hitdice", hitdice);
                results.putBoolean("dontCalculateAutomatically", dontCalculateAutomatically);
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
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Create the AlertDialog object and return it
        return dialog;
    }
}