package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.pentapus.pentapusdmh.R;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddLegendaryActionDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{

    Button positiveButton;
    private TextView tvName, tvDesc, tvAtkMod, tvDmg1, tvDmg2;
    private Spinner spDmg1Type, spDmg2Type;
    private CheckBox cbAction1Auto, cbAction1Add;
    int mode = 0, actionNumber;
    String name, description, atkmod, dmg1, dmg1type, dmg2, dmg2type;
    boolean autoroll, additional;


    public AddLegendaryActionDialogFragment(){
    }

    //mode: 0 = add, 1 = update
    public static AddLegendaryActionDialogFragment newInstance(int mode, String name, String description, String atkmod, String dmg1, String dmg1type, String dmg2, String dmg2type, boolean autoroll, boolean additional, int actionNumber) {
        AddLegendaryActionDialogFragment f = new AddLegendaryActionDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putInt("actionNumber", actionNumber);
        args.putString("monstername", name);
        args.putString("description", description);
        args.putString("atkmod", atkmod);
        args.putString("dmg1", dmg1);
        args.putString("dmg1type", dmg1type);
        args.putString("dmg2", dmg2);
        args.putString("dmg2type", dmg2type);
        args.putBoolean("autoroll", autoroll);
        args.putBoolean("additional", additional);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mode = getArguments().getInt("mode");
            actionNumber = getArguments().getInt("actionNumber");
            name = getArguments().getString("monstername");
            description = getArguments().getString("description");
            atkmod = getArguments().getString("atkmod");
            dmg1 = getArguments().getString("dmg1");
            dmg1type = getArguments().getString("dmg1type");
            dmg2 = getArguments().getString("dmg2");
            dmg2type = getArguments().getString("dmg2type");
            autoroll = getArguments().getBoolean("autoroll");
            additional = getArguments().getBoolean("additional");
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



    private void sendResult(int mode, String name, String description, String atkmod, String dmg1, String dmg1type, String dmg2, String dmg2type, boolean autoroll, boolean additional, int traitNumber) {
        ((LegendaryActionFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), mode, name, description, atkmod, dmg1, dmg1type, dmg2, dmg2type, autoroll, additional, traitNumber);
    }


    public Dialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Legendary Action " + (actionNumber+1));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_action, null);
        tvName = ((TextView) view.findViewById(R.id.tvAction1Name));
        if(name!=null){
            tvName.setText(name);
        }

        tvDesc = ((TextView) view.findViewById(R.id.tvAction1Description));
        if(description!=null){
            tvDesc.setText(description);
        }

        tvAtkMod = ((TextView) view.findViewById(R.id.tvActionMod1));
        if(atkmod!=null){
            tvAtkMod.setText(atkmod);
        }

        tvDmg1 = ((TextView) view.findViewById(R.id.tvA1D1));
        if(dmg1!=null){
            tvDmg1.setText(dmg1);
        }

        spDmg1Type = (Spinner) view.findViewById(R.id.spinner_a1d1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.damagetypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDmg1Type.setAdapter(adapter);
        spDmg1Type.setOnItemSelectedListener(this);

        if(dmg1type != null && !dmg1type.isEmpty()){
            spDmg1Type.setSelection(adapter.getPosition(dmg1type));
        }else{
            dmg1type = spDmg1Type.getItemAtPosition(0).toString();
        }

        tvDmg2 = ((TextView) view.findViewById(R.id.tvA1D2));
        if(dmg2!=null){
            tvDmg2.setText(dmg2);
        }

        spDmg2Type = (Spinner) view.findViewById(R.id.spinner_a1d2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.damagetypes_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDmg2Type.setAdapter(adapter2);
        spDmg2Type.setOnItemSelectedListener(this);

        if(dmg2type != null && !dmg2type.isEmpty()){
            spDmg2Type.setSelection(adapter.getPosition(dmg2type));
        }else{
            dmg2type = spDmg2Type.getItemAtPosition(0).toString();
        }

        cbAction1Auto = ((CheckBox) view.findViewById(R.id.cbAuto1));
        cbAction1Auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                if (checked){
                    autoroll = true;
                }else{
                    autoroll = false;
                }
            }
        });

        cbAction1Add = ((CheckBox) view.findViewById(R.id.cbAdd1));
        cbAction1Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                if (checked){
                    additional = true;
                }else{
                    additional = false;
                }
            }
        });


        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = tvName.getText().toString();
                description = tvDesc.getText().toString();
                atkmod = tvAtkMod.getText().toString();
                dmg1 = tvDmg1.getText().toString();
                dmg2 = tvDmg2.getText().toString();
                sendResult(mode, name, description, atkmod, dmg1, dmg1type, dmg2, dmg2type, autoroll, additional, actionNumber);
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
                sendResult(2, "", "", "", "", "", "", "", false, false, actionNumber);
                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        AlertDialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch(parent.getId()){
            case R.id.spinner_a1d1:
                dmg1type = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.spinner_a1d2:
                dmg2type = parent.getItemAtPosition(pos).toString();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        switch(parent.getId()){
            case R.id.spinner_a1d1:
                dmg1type = "";
                break;
            case R.id.spinner_a1d2:
                dmg2type = "";
                break;
        }
    }




}