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
    String acrobatics, animalHandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofHand, stealth, survival;

    public AddSkillDialogFragment() {
    }

    //mode: 0 = add, 1 = update
    public static AddSkillDialogFragment newInstance(String acrobatics, String animalHandling, String arcana, String athletics, String deception, String history, String insight, String intimidation, String investigation, String medicine, String nature, String perception, String performance, String persuasion, String religion, String sleightofHand, String stealth, String survival) {
        AddSkillDialogFragment f = new AddSkillDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(SkillsPage.ACROBATICS_DATA_KEY, acrobatics);
        args.putString(SkillsPage.ANIMALHANDLING_DATA_KEY, animalHandling);
        args.putString(SkillsPage.ARCANA_DATA_KEY, arcana);
        args.putString(SkillsPage.ATHLETICS_DATA_KEY, athletics);
        args.putString(SkillsPage.DECEPTION_DATA_KEY, deception);
        args.putString(SkillsPage.HISTORY_DATA_KEY, history);
        args.putString(SkillsPage.INSIGHT_DATA_KEY, insight);
        args.putString(SkillsPage.INTIMIDATION_DATA_KEY, intimidation);
        args.putString(SkillsPage.INVESTIGATION_DATA_KEY, investigation);
        args.putString(SkillsPage.MEDICINE_DATA_KEY, medicine);
        args.putString(SkillsPage.NATURE_DATA_KEY, nature);
        args.putString(SkillsPage.PERCEPTION_DATA_KEY, perception);
        args.putString(SkillsPage.PERFORMANCE_DATA_KEY, performance);
        args.putString(SkillsPage.PERSUASION_DATA_KEY, persuasion);
        args.putString(SkillsPage.RELIGION_DATA_KEY, religion);
        args.putString(SkillsPage.SLEIGHTOFHAND_DATA_KEY, sleightofHand);
        args.putString(SkillsPage.STEALTH_DATA_KEY, stealth);
        args.putString(SkillsPage.SURVIVAL_DATA_KEY, survival);
        f.setArguments(args);
        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            acrobatics = getArguments().getString(SkillsPage.ACROBATICS_DATA_KEY);
            animalHandling = getArguments().getString(SkillsPage.ANIMALHANDLING_DATA_KEY);
            arcana = getArguments().getString(SkillsPage.ARCANA_DATA_KEY);
            athletics = getArguments().getString(SkillsPage.ATHLETICS_DATA_KEY);
            deception = getArguments().getString(SkillsPage.DECEPTION_DATA_KEY);
            history = getArguments().getString(SkillsPage.HISTORY_DATA_KEY);
            insight = getArguments().getString(SkillsPage.INSIGHT_DATA_KEY);
            intimidation = getArguments().getString(SkillsPage.INTIMIDATION_DATA_KEY);
            investigation = getArguments().getString(SkillsPage.INVESTIGATION_DATA_KEY);
            medicine = getArguments().getString(SkillsPage.MEDICINE_DATA_KEY);
            nature = getArguments().getString(SkillsPage.NATURE_DATA_KEY);
            perception = getArguments().getString(SkillsPage.PERCEPTION_DATA_KEY);
            performance = getArguments().getString(SkillsPage.PERFORMANCE_DATA_KEY);
            persuasion = getArguments().getString(SkillsPage.PERSUASION_DATA_KEY);
            religion = getArguments().getString(SkillsPage.RELIGION_DATA_KEY);
            sleightofHand = getArguments().getString(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
            stealth = getArguments().getString(SkillsPage.STEALTH_DATA_KEY);
            survival = getArguments().getString(SkillsPage.SURVIVAL_DATA_KEY);
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

        tvAcrobatics.setText(acrobatics);
        tvAnimalHandling.setText(animalHandling);
        tvArcana.setText(arcana);
        tvAthletics.setText(athletics);
        tvDeception.setText(deception);
        tvHistory.setText(history);
        tvInsight.setText(insight);
        tvIntimidation.setText(intimidation);
        tvInvestigation.setText(investigation);
        tvMedicine.setText(medicine);
        tvNature.setText(nature);
        tvPerception.setText(perception);
        tvPerformance.setText(performance);
        tvPersuasion.setText(persuasion);
        tvReligion.setText(religion);
        tvSleightofHand.setText(sleightofHand);
        tvStealth.setText(stealth);
        tvSurvival.setText(survival);
        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                if (!tvAcrobatics.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.ACROBATICS_DATA_KEY, tvAcrobatics.getText().toString());
                }
                if (!tvAnimalHandling.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.ANIMALHANDLING_DATA_KEY, tvAnimalHandling.getText().toString());
                }
                if (!tvArcana.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.ARCANA_DATA_KEY, tvArcana.getText().toString());
                }
                if (!tvAthletics.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.ATHLETICS_DATA_KEY, tvAthletics.getText().toString());
                }
                if (!tvDeception.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.DECEPTION_DATA_KEY, tvDeception.getText().toString());
                }
                if (!tvHistory.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.HISTORY_DATA_KEY, tvHistory.getText().toString());
                }
                if (!tvInsight.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.INSIGHT_DATA_KEY, tvInsight.getText().toString());
                }
                if (!tvIntimidation.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.INTIMIDATION_DATA_KEY, tvIntimidation.getText().toString());
                }
                if (!tvInvestigation.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.INVESTIGATION_DATA_KEY, tvInvestigation.getText().toString());
                }
                if (!tvMedicine.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.MEDICINE_DATA_KEY, tvMedicine.getText().toString());
                }
                if (!tvNature.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.NATURE_DATA_KEY, tvNature.getText().toString());
                }
                if (!tvPerception.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.PERCEPTION_DATA_KEY, tvPerception.getText().toString());
                }
                if (!tvPerformance.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.PERFORMANCE_DATA_KEY, tvPerformance.getText().toString());
                }
                if (!tvPersuasion.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.PERSUASION_DATA_KEY, tvPersuasion.getText().toString());
                }
                if (!tvReligion.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.RELIGION_DATA_KEY, tvReligion.getText().toString());
                }
                if (!tvSleightofHand.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.SLEIGHTOFHAND_DATA_KEY, tvSleightofHand.getText().toString());
                }
                if (!tvStealth.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.STEALTH_DATA_KEY, tvStealth.getText().toString());
                }
                if (!tvSurvival.getText().toString().isEmpty()) {
                    results.putString(SkillsPage.SURVIVAL_DATA_KEY, tvSurvival.getText().toString());
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