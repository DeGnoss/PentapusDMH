package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koni on 11.11.2016.
 */

public class SkillsFragment extends Fragment {
    private static final String ARG_KEY = "skillfragment";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private SkillsPage mPage;
    private TextView tvSkills, labelSkills, tvDmgVul, labelDmgVul, tvDmgRes, labelDmgRes, tvDmgIm, labelDmgIm, tvConIm, labelConIm;
    private String dmgVul, dmgRes, dmgIm, conIm;
    private int acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival;
    private static final int MSG_SHOW_DIALOG = 1000, MSG_SKILL_DIALOG = 1001, MSG_DMGVUL_DIALOG = 1002, MSG_DMGRES_DIALOG = 1003, MSG_DMGIM_DIALOG = 1004, MSG_CONIM_DIALOG = 1005;
    List<String> listDmgVul;
    SkillsFragment fragment;


    private ImageButton bChooseImage;
    private Uri myFile;
    private static int RESULT_CHOOSE_IMG = 2;

    public static SkillsFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        SkillsFragment fragment = new SkillsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SkillsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (SkillsPage) mCallbacks.onGetPage(mKey);
        fragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_skills, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());
        acrobatics = mPage.getData().getInt(SkillsPage.ACROBATICS_DATA_KEY);
        animalhandling = mPage.getData().getInt(SkillsPage.ANIMALHANDLING_DATA_KEY);
        arcana = mPage.getData().getInt(SkillsPage.ARCANA_DATA_KEY);
        athletics = mPage.getData().getInt(SkillsPage.ATHLETICS_DATA_KEY);
        deception = mPage.getData().getInt(SkillsPage.DECEPTION_DATA_KEY);
        history = mPage.getData().getInt(SkillsPage.HISTORY_DATA_KEY);
        insight = mPage.getData().getInt(SkillsPage.INSIGHT_DATA_KEY);
        intimidation = mPage.getData().getInt(SkillsPage.INTIMIDATION_DATA_KEY);
        investigation = mPage.getData().getInt(SkillsPage.INVESTIGATION_DATA_KEY);
        medicine = mPage.getData().getInt(SkillsPage.MEDICINE_DATA_KEY);
        nature = mPage.getData().getInt(SkillsPage.NATURE_DATA_KEY);
        perception = mPage.getData().getInt(SkillsPage.PERCEPTION_DATA_KEY);
        performance = mPage.getData().getInt(SkillsPage.PERFORMANCE_DATA_KEY);
        persuasion = mPage.getData().getInt(SkillsPage.PERSUASION_DATA_KEY);
        religion = mPage.getData().getInt(SkillsPage.RELIGION_DATA_KEY);
        sleightofhand = mPage.getData().getInt(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
        stealth = mPage.getData().getInt(SkillsPage.STEALTH_DATA_KEY);
        survival = mPage.getData().getInt(SkillsPage.SURVIVAL_DATA_KEY);

        dmgVul = mPage.getData().getString(SkillsPage.DMGVUL_DATA_KEY);
        dmgRes = mPage.getData().getString(SkillsPage.DMGRES_DATA_KEY);
        dmgIm = mPage.getData().getString(SkillsPage.DMGIM_DATA_KEY);
        conIm = mPage.getData().getString(SkillsPage.CONIM_DATA_KEY);


        String skills = buildSkillString(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
        tvSkills = ((TextView) rootView.findViewById(R.id.tvSkills));
        if (!skills.isEmpty()) {
            tvSkills.setText(skills);
        }
        tvSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSkillDialogFragment.newInstance(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
                newFragment.setTargetFragment(fragment, MSG_SKILL_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SKILL_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSKILL_DIALOG");
            }
        });

        labelSkills = ((TextView) rootView.findViewById(R.id.labelSkills));
        labelSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSkillDialogFragment.newInstance(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival);
                newFragment.setTargetFragment(fragment, MSG_SKILL_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SKILL_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSKILL_DIALOG");
            }
        });

        tvDmgVul = ((TextView) rootView.findViewById(R.id.tvDmgVul));
        if(dmgVul != null && !dmgVul.isEmpty()){
            tvDmgVul.setText(dmgVul.toLowerCase());
        }
        tvDmgVul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgVulDialogFragment.newInstance(dmgVul);
                newFragment.setTargetFragment(fragment, MSG_DMGVUL_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGVUL_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGVUL_DIALOG");
            }
        });


        labelDmgVul = ((TextView) rootView.findViewById(R.id.labelDmgVul));
        labelDmgVul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgVulDialogFragment.newInstance(dmgVul);
                newFragment.setTargetFragment(fragment, MSG_DMGVUL_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGVUL_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGVUL_DIALOG");
            }
        });

        tvDmgRes = ((TextView) rootView.findViewById(R.id.tvDmgRes));
        if(dmgRes != null && !dmgRes.isEmpty()) {
            tvDmgRes.setText(dmgRes.toLowerCase());
        }
        tvDmgRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgRes);
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });


        labelDmgRes = ((TextView) rootView.findViewById(R.id.labelDmgRes));
        labelDmgRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgRes);
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });

        tvDmgIm = ((TextView) rootView.findViewById(R.id.tvDmgIm));
        if(dmgIm != null && !dmgIm.isEmpty()) {
            tvDmgIm.setText(dmgIm.toLowerCase());
        }
        tvDmgIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgIm);
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });


        labelDmgIm = ((TextView) rootView.findViewById(R.id.labelDmgIm));
        labelDmgIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgIm);
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });

        tvConIm = ((TextView) rootView.findViewById(R.id.tvConIm));
        if(conIm != null && !conIm.isEmpty()) {
            tvConIm.setText(conIm.toLowerCase());
        }
        tvConIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddConImDialogFragment.newInstance(conIm);
                newFragment.setTargetFragment(fragment, MSG_CONIM_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_CONIM_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDCONIM_DIALOG");
            }
        });


        labelConIm = ((TextView) rootView.findViewById(R.id.labelConIm));
        labelConIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddConImDialogFragment.newInstance(conIm);
                newFragment.setTargetFragment(fragment, MSG_CONIM_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_CONIM_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDCONIM_DIALOG");
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (tvSkills != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public void onDialogResult(int requestCode, Bundle results) {
        switch (requestCode) {
            case MSG_SKILL_DIALOG:
                acrobatics = results.getInt("acrobatics");
                animalhandling = results.getInt("animalHandling");
                arcana = results.getInt("arcana");
                athletics = results.getInt("athletics");
                deception = results.getInt("deception");
                history = results.getInt("history");
                insight = results.getInt("insight");
                intimidation = results.getInt("intimidation");
                investigation = results.getInt("investigation");
                medicine = results.getInt("medicine");
                nature = results.getInt("nature");
                perception = results.getInt("perception");
                performance = results.getInt("performance");
                persuasion = results.getInt("persuasion");
                religion = results.getInt("religion");
                sleightofhand = results.getInt("sleightofHand");
                stealth = results.getInt("stealth");
                survival = results.getInt("survival");

                tvSkills.setText(buildSkillString(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival));


                mPage.getData().putInt(SkillsPage.ACROBATICS_DATA_KEY,
                        acrobatics);
                mPage.getData().putInt(SkillsPage.ANIMALHANDLING_DATA_KEY,
                        animalhandling);
                mPage.getData().putInt(SkillsPage.ARCANA_DATA_KEY,
                        arcana);
                mPage.getData().putInt(SkillsPage.ATHLETICS_DATA_KEY,
                        athletics);
                mPage.getData().putInt(SkillsPage.DECEPTION_DATA_KEY,
                        deception);
                mPage.getData().putInt(SkillsPage.HISTORY_DATA_KEY,
                        history);
                mPage.getData().putInt(SkillsPage.INSIGHT_DATA_KEY,
                        insight);
                mPage.getData().putInt(SkillsPage.INTIMIDATION_DATA_KEY,
                        intimidation);
                mPage.getData().putInt(SkillsPage.INVESTIGATION_DATA_KEY,
                        investigation);
                mPage.getData().putInt(SkillsPage.MEDICINE_DATA_KEY,
                        medicine);
                mPage.getData().putInt(SkillsPage.NATURE_DATA_KEY,
                        nature);
                mPage.getData().putInt(SkillsPage.PERCEPTION_DATA_KEY,
                        perception);
                mPage.getData().putInt(SkillsPage.PERFORMANCE_DATA_KEY,
                        performance);
                mPage.getData().putInt(SkillsPage.PERSUASION_DATA_KEY,
                        persuasion);
                mPage.getData().putInt(SkillsPage.RELIGION_DATA_KEY,
                        religion);
                mPage.getData().putInt(SkillsPage.SLEIGHTOFHAND_DATA_KEY,
                        sleightofhand);
                mPage.getData().putInt(SkillsPage.STEALTH_DATA_KEY,
                        stealth);
                mPage.getData().putInt(SkillsPage.SURVIVAL_DATA_KEY,
                        survival);
                break;
            case MSG_DMGVUL_DIALOG:
                dmgVul = null;
                listDmgVul = results.getStringArrayList("dmgVul");
                if (listDmgVul != null) {
                    for (int i = 0; i < listDmgVul.size(); i++) {
                        if (dmgVul == null) {
                            dmgVul = listDmgVul.get(i);
                        } else {
                            dmgVul = dmgVul + ", " + listDmgVul.get(i);
                        }
                    }
                }
                if(dmgVul != null){
                    tvDmgVul.setText(dmgVul.toLowerCase());
                    mPage.getData().putString(SkillsPage.DMGVUL_DATA_KEY,
                            dmgVul);
                }else{
                    tvDmgVul.setText("None (click to add)");
                }
                break;
            case MSG_DMGRES_DIALOG:
                dmgRes = null;
                listDmgVul = results.getStringArrayList("dmgRes");
                if (listDmgVul != null) {
                    for (int i = 0; i < listDmgVul.size(); i++) {
                        if (dmgRes == null) {
                            dmgRes = listDmgVul.get(i);
                        } else {
                            if(listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons") || listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons that aren't silvered")){
                                dmgRes = dmgRes + "; " + listDmgVul.get(i);
                            }else{
                                dmgRes = dmgRes + ", " + listDmgVul.get(i);
                            }
                        }
                    }
                }
                if(dmgRes != null){
                    tvDmgRes.setText(dmgRes.toLowerCase());
                    mPage.getData().putString(SkillsPage.DMGRES_DATA_KEY,
                            dmgRes);
                }else{
                    tvDmgRes.setText("None (click to add)");
                }
                break;
            case MSG_DMGIM_DIALOG:
                dmgIm = null;
                listDmgVul = results.getStringArrayList("dmgRes");
                if (listDmgVul != null) {
                    for (int i = 0; i < listDmgVul.size(); i++) {
                        if (dmgIm == null) {
                            dmgIm = listDmgVul.get(i);
                        } else {
                            if(listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons") || listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons that aren't silvered")){
                                dmgIm = dmgIm + "; " + listDmgVul.get(i);
                            }else{
                                dmgIm = dmgIm + ", " + listDmgVul.get(i);
                            }
                        }
                    }
                }
                if(dmgIm != null){
                    tvDmgIm.setText(dmgIm.toLowerCase());
                    mPage.getData().putString(SkillsPage.DMGIM_DATA_KEY,
                            dmgIm);
                }else{
                    tvDmgIm.setText("None (click to add)");
                }
                break;
            case MSG_CONIM_DIALOG:
                conIm = null;
                listDmgVul = results.getStringArrayList("conIm");
                if (listDmgVul != null) {
                    for (int i = 0; i < listDmgVul.size(); i++) {
                        if (conIm == null) {
                            conIm = listDmgVul.get(i);
                        } else {
                            conIm = conIm + ", " + listDmgVul.get(i);
                        }
                    }
                }
                if(conIm != null){
                    tvConIm.setText(conIm.toLowerCase());
                    mPage.getData().putString(SkillsPage.CONIM_DATA_KEY,
                            conIm);
                }else{
                    tvConIm.setText("None (click to add)");
                }
                break;
            default:
                break;
        }

    }

    private String buildSkillString(int acrobatics, int animalHandling, int arcana, int athletics, int deception, int history, int insight, int intimidation, int investigation, int medicine, int nature, int perception, int performance, int persuasion, int religion, int sleightofHand, int stealth, int survival) {
        String skills = "";
        if (acrobatics != 0) {
            skills = "Acrobatics ";
            if (acrobatics > 0) {
                skills = skills + "+" + acrobatics;
            } else if (acrobatics < 0) {
                skills = skills + acrobatics;
            }
        }
        if (animalHandling != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Animal Handling ";
            } else {
                skills = "Animal Handling ";
            }
            if (animalHandling > 0) {
                skills = skills + "+" + animalHandling;
            } else if (animalHandling < 0) {
                skills = skills + animalHandling;
            }
        }
        if (arcana != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Arcana ";
            } else {
                skills = "Arcana ";
            }
            if (arcana > 0) {
                skills = skills + "+" + arcana;
            } else if (arcana < 0) {
                skills = skills + arcana;
            }
        }
        if (athletics != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Athletics ";
            } else {
                skills = "Athletics ";
            }
            if (athletics > 0) {
                skills = skills + "+" + athletics;
            } else if (athletics < 0) {
                skills = skills + athletics;
            }
        }
        if (deception != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Deception ";
            } else {
                skills = "Deception ";
            }
            if (deception > 0) {
                skills = skills + "+" + deception;
            } else if (deception < 0) {
                skills = skills + deception;
            }
        }
        if (history != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", History ";
            } else {
                skills = "History ";
            }
            if (history > 0) {
                skills = skills + "+" + history;
            } else if (history < 0) {
                skills = skills + history;
            }
        }
        if (insight != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Insight ";
            } else {
                skills = "Insight ";
            }
            if (insight > 0) {
                skills = skills + "+" + insight;
            } else if (insight < 0) {
                skills = skills + insight;
            }
        }
        if (intimidation != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Intimidation ";
            } else {
                skills = "Intimidation ";
            }
            if (intimidation > 0) {
                skills = skills + "+" + intimidation;
            } else if (intimidation < 0) {
                skills = skills + intimidation;
            }
        }
        if (investigation != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Investigation ";
            } else {
                skills = "Investigation ";
            }
            if (investigation > 0) {
                skills = skills + "+" + investigation;
            } else if (investigation < 0) {
                skills = skills + investigation;
            }
        }
        if (medicine != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Medicine ";
            } else {
                skills = "Medicine ";
            }
            if (medicine > 0) {
                skills = skills + "+" + medicine;
            } else if (medicine < 0) {
                skills = skills + medicine;
            }
        }
        if (nature != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Nature ";
            } else {
                skills = "Nature ";
            }
            if (nature > 0) {
                skills = skills + "+" + nature;
            } else if (nature < 0) {
                skills = skills + nature;
            }
        }
        if (perception != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Perception ";
            } else {
                skills = "Perception ";
            }
            if (perception > 0) {
                skills = skills + "+" + perception;
            } else if (perception < 0) {
                skills = skills + perception;
            }
        }
        if (performance != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Performance ";
            } else {
                skills = "Performance ";
            }
            if (performance > 0) {
                skills = skills + "+" + performance;
            } else if (performance < 0) {
                skills = skills + performance;
            }
        }
        if (persuasion != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Persuasion ";
            } else {
                skills = "Persuasion ";
            }
            if (persuasion > 0) {
                skills = skills + "+" + persuasion;
            } else if (persuasion < 0) {
                skills = skills + persuasion;
            }
        }
        if (religion != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Religion ";
            } else {
                skills = "Religion ";
            }
            if (religion > 0) {
                skills = skills + "+" + religion;
            } else if (religion < 0) {
                skills = skills + religion;
            }
        }
        if (sleightofHand != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Sleight of Hand ";
            } else {
                skills = "Sleight of Hand ";
            }
            if (sleightofHand > 0) {
                skills = skills + "+" + sleightofHand;
            } else if (sleightofHand < 0) {
                skills = skills + sleightofHand;
            }
        }
        if (stealth != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Stealth ";
            } else {
                skills = "Stealth ";
            }
            if (stealth > 0) {
                skills = skills + "+" + stealth;
            } else if (stealth < 0) {
                skills = skills + stealth;
            }
        }
        if (survival != 0) {
            if (!skills.isEmpty()) {
                skills = skills + ", Survival ";
            } else {
                skills = "Survival ";
            }
            if (survival > 0) {
                skills = skills + "+" + survival;
            } else if (survival < 0) {
                skills = skills + survival;
            }
        }
        return skills;
    }
}
