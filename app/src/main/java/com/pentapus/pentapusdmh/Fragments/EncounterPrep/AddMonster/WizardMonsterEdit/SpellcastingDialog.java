package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.Spells.SpellViewPagerDialogFragment;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class SpellcastingDialog extends DialogFragment implements AdapterView.OnItemSelectedListener{

    EditText tvSpellMod, tvSpellDC, tvSlot1, tvSlot2, tvSlot3, tvSlot4, tvSlot5, tvSlot6, tvSlot7, tvSlot8, tvSlot9;
    TextView labelSlot1, labelSlot2, labelSlot3, labelSlot4, labelSlot5, labelSlot6, labelSlot7, labelSlot8, labelSlot9;
    Spinner spSpellcastingAbility, spSpellcastingClass, spSpellCasterLevel;
    Fragment targetFragment;
    String sclevel, scability, scmod, scdc, scclass;
    int[] slots = new int[9];

    public SpellcastingDialog() {
    }

    //mode: 0 = add, 1 = update
    public static SpellcastingDialog newInstance(String scability, String sclevel, String scmod, String scdc, String scclass, int[] slots) {
        SpellcastingDialog f = new SpellcastingDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("scability", scability);
        args.putString("sclevel", sclevel);
        args.putString("scmod", scmod);
        args.putString("scdc", scdc);
        args.putString("scclass", scclass);
        args.putIntArray(TraitsPage.SCSLOTS_DATA_KEY, slots);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scability = getArguments().getString("scability");
            sclevel = getArguments().getString("sclevel");
            scmod = getArguments().getString("scmod");
            scdc = getArguments().getString("scdc");
            scclass = getArguments().getString("scclass");
            slots = getArguments().getIntArray(TraitsPage.SCSLOTS_DATA_KEY);
        }
        if(slots == null){
            slots = new int[9];
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
        builder.setTitle("Spellcasting");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_spellcasting, null);
        spSpellcastingAbility = ((Spinner) view.findViewById(R.id.spSpellcastingAbility));
        tvSpellMod = (EditText) view.findViewById(R.id.tvSpellMod);
        if(scmod != null && !scmod.isEmpty()){
            tvSpellMod.setText(scmod);
        }
        tvSpellDC = (EditText) view.findViewById(R.id.tvSpellDC);
        if(scdc != null && !scdc.isEmpty()){
            tvSpellDC.setText(scdc);
        }
        spSpellcastingClass = ((Spinner) view.findViewById(R.id.spSpellcastingClass));

        spSpellCasterLevel = ((Spinner) view.findViewById(R.id.spSpellCasterLevel));
        ArrayAdapter<CharSequence> adapterSpellCasterLevel = ArrayAdapter.createFromResource(getContext(),
                R.array.spellcaster_level, android.R.layout.simple_spinner_item);
        adapterSpellCasterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpellCasterLevel.setAdapter(adapterSpellCasterLevel);
        spSpellCasterLevel.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterAbility = ArrayAdapter.createFromResource(getContext(),
                R.array.spellcasting_ability, android.R.layout.simple_spinner_item);
        adapterAbility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpellcastingAbility.setAdapter(adapterAbility);
        spSpellcastingAbility.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapterClass = ArrayAdapter.createFromResource(getContext(),
                R.array.spellcasting_class, android.R.layout.simple_spinner_item);
        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpellcastingClass.setAdapter(adapterClass);
        spSpellcastingClass.setOnItemSelectedListener(this);

        if(scability != null && !scability.isEmpty()){
            spSpellcastingAbility.setSelection(adapterAbility.getPosition(scability));
        }else{
            scability = spSpellcastingAbility.getItemAtPosition(0).toString();
        }
        if(sclevel != null && !sclevel.isEmpty()){
            spSpellCasterLevel.setSelection(adapterSpellCasterLevel.getPosition(sclevel));
        }else{
            sclevel = spSpellCasterLevel.getItemAtPosition(0).toString();
        }
        if(scclass != null && !scclass.isEmpty()){
            spSpellcastingClass.setSelection(adapterClass.getPosition(scclass));
        }else{
            scclass = spSpellcastingClass.getItemAtPosition(0).toString();
        }

        tvSlot1 = (EditText) view.findViewById(R.id.tvSlots1);
        if(slots != null && slots[0] > 0){
            tvSlot1.setText(String.valueOf(slots[0]));
        }
        tvSlot2 = (EditText) view.findViewById(R.id.tvSlots2);
        if(slots != null && slots[1] > 0){
            tvSlot2.setText(String.valueOf(slots[1]));
        }
        tvSlot3 = (EditText) view.findViewById(R.id.tvSlots3);
        if(slots != null && slots[2] > 0){
            tvSlot3.setText(String.valueOf(slots[2]));
        }
        tvSlot4 = (EditText) view.findViewById(R.id.tvSlots4);
        if(slots != null && slots[3] > 0){
            tvSlot4.setText(String.valueOf(slots[3]));
        }
        tvSlot5 = (EditText) view.findViewById(R.id.tvSlots5);
        if(slots != null && slots[4] > 0){
            tvSlot5.setText(String.valueOf(slots[4]));
        }
        tvSlot6 = (EditText) view.findViewById(R.id.tvSlots6);
        if(slots != null && slots[5] > 0){
            tvSlot6.setText(String.valueOf(slots[5]));
        }
        tvSlot7 = (EditText) view.findViewById(R.id.tvSlots7);
        if(slots != null && slots[6] > 0){
            tvSlot7.setText(String.valueOf(slots[6]));
        }
        tvSlot8 = (EditText) view.findViewById(R.id.tvSlots8);
        if(slots != null && slots[7] > 0){
            tvSlot8.setText(String.valueOf(slots[7]));
        }
        tvSlot9 = (EditText) view.findViewById(R.id.tvSlots9);
        if(slots != null && slots[8] > 0){
            tvSlot9.setText(String.valueOf(slots[8]));
        }

        labelSlot2 = (TextView) view.findViewById(R.id.labelSlots2);
        labelSlot3 = (TextView) view.findViewById(R.id.labelSlots3);
        labelSlot4 = (TextView) view.findViewById(R.id.labelSlots4);
        labelSlot5 = (TextView) view.findViewById(R.id.labelSlots5);
        labelSlot6 = (TextView) view.findViewById(R.id.labelSlots6);
        labelSlot7 = (TextView) view.findViewById(R.id.labelSlots7);
        labelSlot8 = (TextView) view.findViewById(R.id.labelSlots8);
        labelSlot9 = (TextView) view.findViewById(R.id.labelSlots9);



        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                if(!tvSpellMod.getText().toString().isEmpty()){
                    results.putString(TraitsPage.SCMOD_DATA_KEY, tvSpellMod.getText().toString());
                }
                if(!tvSpellDC.getText().toString().isEmpty()){
                    results.putString(TraitsPage.SCDC_DATA_KEY, tvSpellDC.getText().toString());
                }
                results.putString(TraitsPage.SCABILITY_DATA_KEY, scability);
                results.putString(TraitsPage.SClEVEL_DATA_KEY, sclevel);
                results.putString(TraitsPage.SCCLASS_DATA_KEY, scclass);
                if(!tvSlot1.getText().toString().isEmpty()){
                    slots[0] = Integer.valueOf(tvSlot1.getText().toString());
                }
                if(!tvSlot2.getText().toString().isEmpty()){
                    slots[1] = Integer.valueOf(tvSlot2.getText().toString());
                }
                if(!tvSlot3.getText().toString().isEmpty()){
                    slots[2] = Integer.valueOf(tvSlot3.getText().toString());
                }
                if(!tvSlot4.getText().toString().isEmpty()){
                    slots[3] = Integer.valueOf(tvSlot4.getText().toString());
                }
                if(!tvSlot5.getText().toString().isEmpty()){
                    slots[4] = Integer.valueOf(tvSlot5.getText().toString());
                }
                if(!tvSlot6.getText().toString().isEmpty()){
                    slots[5] = Integer.valueOf(tvSlot6.getText().toString());
                }
                if(!tvSlot7.getText().toString().isEmpty()){
                    slots[6] = Integer.valueOf(tvSlot7.getText().toString());
                }
                if(!tvSlot8.getText().toString().isEmpty()){
                    slots[7] = Integer.valueOf(tvSlot8.getText().toString());
                }
                if(!tvSlot9.getText().toString().isEmpty()){
                    slots[8] = Integer.valueOf(tvSlot9.getText().toString());
                }
                results.putIntArray(TraitsPage.SCSLOTS_DATA_KEY, slots);

                sendResult(-1, results);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                results.putIntArray(TraitsPage.SCSLOTS_DATA_KEY, null);
                results.putString(TraitsPage.SCABILITY_DATA_KEY, "");
                results.putString(TraitsPage.SClEVEL_DATA_KEY, "");
                results.putString(TraitsPage.SCCLASS_DATA_KEY, "");
                results.putString(TraitsPage.SCMOD_DATA_KEY, "");
                results.putString(TraitsPage.SCMOD_DATA_KEY, "");
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
            case R.id.spSpellCasterLevel:
                sclevel = adapterView.getItemAtPosition(i).toString();
                setSlotVisibility(Integer.valueOf(adapterView.getItemAtPosition(i).toString()));
                break;
            case R.id.spSpellcastingAbility:
                scability = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.spSpellcastingClass:
                scclass = adapterView.getItemAtPosition(i).toString();
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setSlotVisibility(int level){
        labelSlot2.setVisibility(View.GONE);
        tvSlot2.setVisibility(View.GONE);
        labelSlot3.setVisibility(View.GONE);
        tvSlot3.setVisibility(View.GONE);
        labelSlot4.setVisibility(View.GONE);
        tvSlot4.setVisibility(View.GONE);
        labelSlot5.setVisibility(View.GONE);
        tvSlot5.setVisibility(View.GONE);
        labelSlot6.setVisibility(View.GONE);
        tvSlot6.setVisibility(View.GONE);
        labelSlot7.setVisibility(View.GONE);
        tvSlot7.setVisibility(View.GONE);
        labelSlot8.setVisibility(View.GONE);
        tvSlot8.setVisibility(View.GONE);
        labelSlot9.setVisibility(View.GONE);
        tvSlot9.setVisibility(View.GONE);
        if(level >= 3){
            labelSlot2.setVisibility(View.VISIBLE);
            tvSlot2.setVisibility(View.VISIBLE);
        }
        if(level>=5){
            labelSlot3.setVisibility(View.VISIBLE);
            tvSlot3.setVisibility(View.VISIBLE);
        }
        if(level>=7){
            labelSlot4.setVisibility(View.VISIBLE);
            tvSlot4.setVisibility(View.VISIBLE);
        }
        if(level>=9){
            labelSlot5.setVisibility(View.VISIBLE);
            tvSlot5.setVisibility(View.VISIBLE);
        }
        if(level>=11){
            labelSlot6.setVisibility(View.VISIBLE);
            tvSlot6.setVisibility(View.VISIBLE);
        }
        if(level>=13){
            labelSlot7.setVisibility(View.VISIBLE);
            tvSlot7.setVisibility(View.VISIBLE);
        }
        if(level>=15){
            labelSlot8.setVisibility(View.VISIBLE);
            tvSlot8.setVisibility(View.VISIBLE);
        }
        if(level>=17){
            labelSlot9.setVisibility(View.VISIBLE);
            tvSlot9.setVisibility(View.VISIBLE);
        }
    }
}