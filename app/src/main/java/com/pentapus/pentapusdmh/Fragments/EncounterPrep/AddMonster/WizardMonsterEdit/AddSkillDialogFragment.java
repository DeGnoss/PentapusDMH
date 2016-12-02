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
public class AddSkillDialogFragment extends DialogFragment {

    Button positiveButton;
    EditText tvAcrobatics, tvAnimalHandling, tvArcana, tvAthletics, tvDeception, tvHistory, tvInsight, tvIntimidation, tvInvestigation, tvMedicine, tvNature, tvPerception, tvPerformance, tvPersuasion, tvReligion, tvSleightofHand, tvStealth, tvSurvival;
    int acrobatics, animalHandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofHand, stealth, survival;


    public AddSkillDialogFragment(){
    }

    //mode: 0 = add, 1 = update
    public static AddSkillDialogFragment newInstance(int acrobatics, int animalHandling, int arcana, int athletics, int deception, int history, int insight, int intimidation, int investigation, int medicine, int nature, int perception, int performance, int persuasion, int religion, int sleightofHand, int stealth, int survival) {
        AddSkillDialogFragment f = new AddSkillDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("acrobatics", acrobatics);
        args.putInt("animalHandling", animalHandling);
        args.putInt("arcana", arcana);
        args.putInt("athletics", athletics);
        args.putInt("deception", deception);
        args.putInt("history", history);
        args.putInt("insight", insight);
        args.putInt("intimidation", intimidation);
        args.putInt("investigation", investigation);
        args.putInt("medicine", medicine);
        args.putInt("nature", nature);
        args.putInt("perception", perception);
        args.putInt("performance", performance);
        args.putInt("persuasion", persuasion);
        args.putInt("religion", religion);
        args.putInt("sleightofHand", sleightofHand);
        args.putInt("stealth", stealth);
        args.putInt("survival", survival);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            acrobatics = getArguments().getInt("acrobatics");
            animalHandling = getArguments().getInt("animalHandling");
            arcana = getArguments().getInt("arcana");
            athletics = getArguments().getInt("athletics");
            deception = getArguments().getInt("deception");
            history = getArguments().getInt("history");
            insight = getArguments().getInt("insight");
            intimidation = getArguments().getInt("intimidation");
            investigation = getArguments().getInt("investigation");
            medicine = getArguments().getInt("medicine");
            nature = getArguments().getInt("nature");
            perception = getArguments().getInt("perception");
            performance = getArguments().getInt("performance");
            persuasion = getArguments().getInt("persuasion");
            religion = getArguments().getInt("religion");
            sleightofHand = getArguments().getInt("sleightofHand");
            stealth = getArguments().getInt("stealth");
            survival = getArguments().getInt("survival");
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
        builder.setTitle("Skills");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_skills, null);
        tvAcrobatics = (EditText) view.findViewById(R.id.tvAcrobatics);

        tvAnimalHandling = (EditText) view.findViewById(R.id.tvAnimalHandling);

        tvArcana = (EditText) view.findViewById(R.id.tvArcana);

        tvAthletics = (EditText) view.findViewById(R.id.tvAthletics);

        tvDeception = (EditText) view.findViewById(R.id.tvDeception);

        tvHistory = (EditText) view.findViewById(R.id.tvHistory);

        tvInsight = (EditText) view.findViewById(R.id.tvInsight);

        tvIntimidation = (EditText) view.findViewById(R.id.tvIntimidation);

        tvInvestigation = (EditText) view.findViewById(R.id.tvInvestigation);

        tvMedicine = (EditText) view.findViewById(R.id.tvMedicine);

        tvNature = (EditText) view.findViewById(R.id.tvNature);

        tvPerception = (EditText) view.findViewById(R.id.tvPerception);

        tvPerformance = (EditText) view.findViewById(R.id.tvPerformance);

        tvPersuasion = (EditText) view.findViewById(R.id.tvPersuasion);

        tvReligion = (EditText) view.findViewById(R.id.tvReligion);

        tvSleightofHand = (EditText) view.findViewById(R.id.tvSleightOfHand);

        tvStealth = (EditText) view.findViewById(R.id.tvStealth);

        tvSurvival = (EditText) view.findViewById(R.id.tvSurvival);

            tvAcrobatics.setText(String.valueOf(acrobatics));
            tvAnimalHandling.setText(String.valueOf(animalHandling));
            tvArcana.setText(String.valueOf(arcana));
            tvAthletics.setText(String.valueOf(athletics));
            tvDeception.setText(String.valueOf(deception));
            tvHistory.setText(String.valueOf(history));
            tvInsight.setText(String.valueOf(insight));
            tvIntimidation.setText(String.valueOf(intimidation));
            tvInvestigation.setText(String.valueOf(investigation));
            tvMedicine.setText(String.valueOf(medicine));
            tvNature.setText(String.valueOf(nature));
            tvPerception.setText(String.valueOf(perception));
            tvPerformance.setText(String.valueOf(performance));
            tvPersuasion.setText(String.valueOf(persuasion));
            tvReligion.setText(String.valueOf(religion));
            tvSleightofHand.setText(String.valueOf(sleightofHand));
            tvStealth.setText(String.valueOf(stealth));
            tvSurvival.setText(String.valueOf(survival));
        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                results.putInt("acrobatics", Integer.valueOf(tvAcrobatics.getText().toString()));
                results.putInt("animalHandling", Integer.valueOf(tvAnimalHandling.getText().toString()));
                results.putInt("arcana", Integer.valueOf(tvArcana.getText().toString()));
                results.putInt("athletics", Integer.valueOf(tvAthletics.getText().toString()));
                results.putInt("deception", Integer.valueOf(tvDeception.getText().toString()));
                results.putInt("history", Integer.valueOf(tvHistory.getText().toString()));
                results.putInt("insight", Integer.valueOf(tvInsight.getText().toString()));
                results.putInt("intimidation", Integer.valueOf(tvIntimidation.getText().toString()));
                results.putInt("investigation", Integer.valueOf(tvInvestigation.getText().toString()));
                results.putInt("medicine", Integer.valueOf(tvMedicine.getText().toString()));
                results.putInt("nature", Integer.valueOf(tvNature.getText().toString()));
                results.putInt("perception", Integer.valueOf(tvPerception.getText().toString()));
                results.putInt("performance", Integer.valueOf(tvPerformance.getText().toString()));
                results.putInt("persuasion", Integer.valueOf(tvPersuasion.getText().toString()));
                results.putInt("religion", Integer.valueOf(tvReligion.getText().toString()));
                results.putInt("sleightofHand", Integer.valueOf(tvSleightofHand.getText().toString()));
                results.putInt("stealth", Integer.valueOf(tvStealth.getText().toString()));
                results.putInt("survival", Integer.valueOf(tvSurvival.getText().toString()));



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