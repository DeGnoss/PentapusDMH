package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Koni on 11.11.2016.
 */

public class SkillsFragment extends Fragment implements AbilitiesFragment.OnWisdomChangedListener {
    private static final String ARG_KEY = "skillfragment";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private SkillsPage mPage;
    private TextView tvSkills, labelSkills, tvDmgVul, labelDmgVul, tvDmgRes, labelDmgRes, tvDmgIm, labelDmgIm, tvConIm, labelConIm, tvSavingThrows, labelSavingThrows, tvSenses, labelSenses, tvLanguages, labelLanguages;
    private String dmgVul, dmgRes, dmgIm, conIm;
    private String acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival;
    private String stStr, stDex, stCon, stInt, stWis, stCha;
    private String darkvision, blindsight, tremorsense, truesight, passivePerception, other;
    private String senses, languages;
    private boolean isNotCalculatedFromWisdom;
    private static final int MSG_SHOW_DIALOG = 1000, MSG_SKILL_DIALOG = 1001, MSG_DMGVUL_DIALOG = 1002, MSG_DMGRES_DIALOG = 1003, MSG_DMGIM_DIALOG = 1004, MSG_CONIM_DIALOG = 1005, MSG_SAVINGTHROW_DIALOG = 1006, MSG_SENSES_DIALOG = 1007, MSG_LANGUAGES_DIALOG = 1008;
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
        acrobatics = mPage.getData().getString(SkillsPage.ACROBATICS_DATA_KEY);
        animalhandling = mPage.getData().getString(SkillsPage.ANIMALHANDLING_DATA_KEY);
        arcana = mPage.getData().getString(SkillsPage.ARCANA_DATA_KEY);
        athletics = mPage.getData().getString(SkillsPage.ATHLETICS_DATA_KEY);
        deception = mPage.getData().getString(SkillsPage.DECEPTION_DATA_KEY);
        history = mPage.getData().getString(SkillsPage.HISTORY_DATA_KEY);
        insight = mPage.getData().getString(SkillsPage.INSIGHT_DATA_KEY);
        intimidation = mPage.getData().getString(SkillsPage.INTIMIDATION_DATA_KEY);
        investigation = mPage.getData().getString(SkillsPage.INVESTIGATION_DATA_KEY);
        medicine = mPage.getData().getString(SkillsPage.MEDICINE_DATA_KEY);
        nature = mPage.getData().getString(SkillsPage.NATURE_DATA_KEY);
        perception = mPage.getData().getString(SkillsPage.PERCEPTION_DATA_KEY);
        performance = mPage.getData().getString(SkillsPage.PERFORMANCE_DATA_KEY);
        persuasion = mPage.getData().getString(SkillsPage.PERSUASION_DATA_KEY);
        religion = mPage.getData().getString(SkillsPage.RELIGION_DATA_KEY);
        sleightofhand = mPage.getData().getString(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
        stealth = mPage.getData().getString(SkillsPage.STEALTH_DATA_KEY);
        survival = mPage.getData().getString(SkillsPage.SURVIVAL_DATA_KEY);

        dmgVul = mPage.getData().getString(SkillsPage.DMGVUL_DATA_KEY);
        dmgRes = mPage.getData().getString(SkillsPage.DMGRES_DATA_KEY);
        dmgIm = mPage.getData().getString(SkillsPage.DMGIM_DATA_KEY);
        conIm = mPage.getData().getString(SkillsPage.CONIM_DATA_KEY);

        stStr = mPage.getData().getString(SkillsPage.STSTR_DATA_KEY);
        stDex = mPage.getData().getString(SkillsPage.STDEX_DATA_KEY);
        stCon = mPage.getData().getString(SkillsPage.STCON_DATA_KEY);
        stInt = mPage.getData().getString(SkillsPage.STINT_DATA_KEY);
        stWis = mPage.getData().getString(SkillsPage.STWIS_DATA_KEY);
        stCha = mPage.getData().getString(SkillsPage.STCHA_DATA_KEY);

        senses = mPage.getData().getString(SkillsPage.SENSES_DATA_KEY);
        isNotCalculatedFromWisdom = mPage.getData().getBoolean(SkillsPage.ISCALCULATEDFROMWISDOM_DATA_KEY);
        languages = mPage.getData().getString(SkillsPage.LANGUAGES_DATA_KEY);


        tvSavingThrows = ((TextView) rootView.findViewById(R.id.tvSavingThrows));
        String savingThrows = buildSavingThrowString(stStr, stDex, stCon, stInt, stWis, stCha);
        if (!savingThrows.isEmpty()) {
            tvSavingThrows.setText(savingThrows);
        }
        tvSavingThrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSavingThrowDialogFragment.newInstance(stStr, stDex, stCon, stInt, stWis, stCha);
                newFragment.setTargetFragment(fragment, MSG_SAVINGTHROW_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SAVINGTHROW_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSAVINGTHROW_DIALOG");
            }
        });

        labelSavingThrows = ((TextView) rootView.findViewById(R.id.labelSavingThrows));
        labelSavingThrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSavingThrowDialogFragment.newInstance(stStr, stDex, stCon, stInt, stWis, stCha);
                newFragment.setTargetFragment(fragment, MSG_SAVINGTHROW_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SAVINGTHROW_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSAVINGTHROW_DIALOG");
            }
        });


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
        if (dmgVul != null && !dmgVul.isEmpty()) {
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
        if (dmgRes != null && !dmgRes.isEmpty()) {
            tvDmgRes.setText(dmgRes.toLowerCase());
        }
        tvDmgRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgRes, "Damage Resistance");
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });


        labelDmgRes = ((TextView) rootView.findViewById(R.id.labelDmgRes));
        labelDmgRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgRes, "Damage Resistance");
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGRES_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });

        tvDmgIm = ((TextView) rootView.findViewById(R.id.tvDmgIm));
        if (dmgIm != null && !dmgIm.isEmpty()) {
            tvDmgIm.setText(dmgIm.toLowerCase());
        }
        tvDmgIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgIm, "Damage Immunities");
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });


        labelDmgIm = ((TextView) rootView.findViewById(R.id.labelDmgIm));
        labelDmgIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddDmgResDialogFragment.newInstance(dmgIm, "Damage Immunities");
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_DMGIM_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDDMGRES_DIALOG");
            }
        });

        tvConIm = ((TextView) rootView.findViewById(R.id.tvConIm));
        if (conIm != null && !conIm.isEmpty()) {
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


        tvSenses = ((TextView) rootView.findViewById(R.id.tvSenses));
        if (!isNotCalculatedFromWisdom) {
            if (senses != null && !senses.isEmpty()) {
                if (!senses.contains("passive Perception")) {
                    if (mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY) != null && !mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY).isEmpty()) {
                        passivePerception = String.valueOf(10 + AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY))));
                        senses = senses + ", passive Perception " + passivePerception;
                    }
                }
            } else {
                if (mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY) != null && !mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY).isEmpty()) {
                    passivePerception = String.valueOf(10 + AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY))));
                    senses = "passive Perception " + passivePerception;
                }
            }
        } else {
            if (senses != null) {
                tvSenses.setText(senses);
            } else {
                tvSenses.setText("");
            }
        }
        tvSenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSensesDialogFragment.newInstance(senses, mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY), isNotCalculatedFromWisdom);
                newFragment.setTargetFragment(fragment, MSG_SENSES_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SENSES_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSENSES_DIALOG");
            }
        });


        labelSenses = ((TextView) rootView.findViewById(R.id.labelSenses));
        labelSenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSensesDialogFragment.newInstance(senses, mPage.getData().getString(SkillsPage.WISDOM_DATA_KEY), isNotCalculatedFromWisdom);
                newFragment.setTargetFragment(fragment, MSG_SENSES_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SENSES_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSENSES_DIALOG");
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
        if (mPage != null && senses != null) {
            mPage.getData().putString(SkillsPage.SENSES_DATA_KEY, senses);
            mPage.getData().putBoolean(SkillsPage.ISCALCULATEDFROMWISDOM_DATA_KEY, isNotCalculatedFromWisdom);
        }

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
                acrobatics = results.getString(SkillsPage.ACROBATICS_DATA_KEY);
                animalhandling = results.getString(SkillsPage.ANIMALHANDLING_DATA_KEY);
                arcana = results.getString(SkillsPage.ARCANA_DATA_KEY);
                athletics = results.getString(SkillsPage.ATHLETICS_DATA_KEY);
                deception = results.getString(SkillsPage.DECEPTION_DATA_KEY);
                history = results.getString(SkillsPage.HISTORY_DATA_KEY);
                insight = results.getString(SkillsPage.INSIGHT_DATA_KEY);
                intimidation = results.getString(SkillsPage.INTIMIDATION_DATA_KEY);
                investigation = results.getString(SkillsPage.INVESTIGATION_DATA_KEY);
                medicine = results.getString(SkillsPage.MEDICINE_DATA_KEY);
                nature = results.getString(SkillsPage.NATURE_DATA_KEY);
                perception = results.getString(SkillsPage.PERCEPTION_DATA_KEY);
                performance = results.getString(SkillsPage.PERFORMANCE_DATA_KEY);
                persuasion = results.getString(SkillsPage.PERSUASION_DATA_KEY);
                religion = results.getString(SkillsPage.RELIGION_DATA_KEY);
                sleightofhand = results.getString(SkillsPage.SLEIGHTOFHAND_DATA_KEY);
                stealth = results.getString(SkillsPage.STEALTH_DATA_KEY);
                survival = results.getString(SkillsPage.SURVIVAL_DATA_KEY);

                tvSkills.setText(buildSkillString(acrobatics, animalhandling, arcana, athletics, deception, history, insight, intimidation, investigation, medicine, nature, perception, performance, persuasion, religion, sleightofhand, stealth, survival));


                mPage.getData().putString(SkillsPage.ACROBATICS_DATA_KEY,
                        acrobatics);
                mPage.getData().putString(SkillsPage.ANIMALHANDLING_DATA_KEY,
                        animalhandling);
                mPage.getData().putString(SkillsPage.ARCANA_DATA_KEY,
                        arcana);
                mPage.getData().putString(SkillsPage.ATHLETICS_DATA_KEY,
                        athletics);
                mPage.getData().putString(SkillsPage.DECEPTION_DATA_KEY,
                        deception);
                mPage.getData().putString(SkillsPage.HISTORY_DATA_KEY,
                        history);
                mPage.getData().putString(SkillsPage.INSIGHT_DATA_KEY,
                        insight);
                mPage.getData().putString(SkillsPage.INTIMIDATION_DATA_KEY,
                        intimidation);
                mPage.getData().putString(SkillsPage.INVESTIGATION_DATA_KEY,
                        investigation);
                mPage.getData().putString(SkillsPage.MEDICINE_DATA_KEY,
                        medicine);
                mPage.getData().putString(SkillsPage.NATURE_DATA_KEY,
                        nature);
                mPage.getData().putString(SkillsPage.PERCEPTION_DATA_KEY,
                        perception);
                mPage.getData().putString(SkillsPage.PERFORMANCE_DATA_KEY,
                        performance);
                mPage.getData().putString(SkillsPage.PERSUASION_DATA_KEY,
                        persuasion);
                mPage.getData().putString(SkillsPage.RELIGION_DATA_KEY,
                        religion);
                mPage.getData().putString(SkillsPage.SLEIGHTOFHAND_DATA_KEY,
                        sleightofhand);
                mPage.getData().putString(SkillsPage.STEALTH_DATA_KEY,
                        stealth);
                mPage.getData().putString(SkillsPage.SURVIVAL_DATA_KEY,
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
                if (dmgVul != null) {
                    tvDmgVul.setText(dmgVul.toLowerCase());
                    mPage.getData().putString(SkillsPage.DMGVUL_DATA_KEY,
                            dmgVul);
                } else {
                    tvDmgVul.setText("");
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
                            if (listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons") || listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons that aren't silvered")) {
                                dmgRes = dmgRes + "; " + listDmgVul.get(i);
                            } else {
                                dmgRes = dmgRes + ", " + listDmgVul.get(i);
                            }
                        }
                    }
                }
                if (dmgRes != null) {
                    tvDmgRes.setText(dmgRes.toLowerCase());
                    mPage.getData().putString(SkillsPage.DMGRES_DATA_KEY,
                            dmgRes);
                } else {
                    tvDmgRes.setText("");
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
                            if (listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons") || listDmgVul.get(i).equals("Bludgeoning, piercing and slashing from nonmagical weapons that aren't silvered")) {
                                dmgIm = dmgIm + "; " + listDmgVul.get(i);
                            } else {
                                dmgIm = dmgIm + ", " + listDmgVul.get(i);
                            }
                        }
                    }
                }
                if (dmgIm != null) {
                    tvDmgIm.setText(dmgIm.toLowerCase());
                    mPage.getData().putString(SkillsPage.DMGIM_DATA_KEY,
                            dmgIm);
                } else {
                    tvDmgIm.setText("");
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
                if (conIm != null) {
                    tvConIm.setText(conIm.toLowerCase());
                    mPage.getData().putString(SkillsPage.CONIM_DATA_KEY,
                            conIm);
                } else {
                    tvConIm.setText("");
                }
                break;
            case MSG_SAVINGTHROW_DIALOG:
                stStr = results.getString(SkillsPage.STSTR_DATA_KEY);
                stDex = results.getString(SkillsPage.STDEX_DATA_KEY);
                stCon = results.getString(SkillsPage.STCON_DATA_KEY);
                stInt = results.getString(SkillsPage.STINT_DATA_KEY);
                stWis = results.getString(SkillsPage.STWIS_DATA_KEY);
                stCha = results.getString(SkillsPage.STCHA_DATA_KEY);

                tvSavingThrows.setText(buildSavingThrowString(stStr, stDex, stCon, stInt, stWis, stCha));

                mPage.getData().putString(SkillsPage.STSTR_DATA_KEY,
                        stStr);
                mPage.getData().putString(SkillsPage.STDEX_DATA_KEY,
                        stDex);
                mPage.getData().putString(SkillsPage.STCON_DATA_KEY,
                        stCon);
                mPage.getData().putString(SkillsPage.STINT_DATA_KEY,
                        stInt);
                mPage.getData().putString(SkillsPage.STWIS_DATA_KEY,
                        stWis);
                mPage.getData().putString(SkillsPage.STCHA_DATA_KEY,
                        stCha);
                break;
            case MSG_SENSES_DIALOG:
                senses = results.getString("senses");
                isNotCalculatedFromWisdom = results.getBoolean("isNotCalculatedFromWisdom");
                tvSenses.setText(senses);
                mPage.getData().putBoolean(SkillsPage.ISCALCULATEDFROMWISDOM_DATA_KEY, isNotCalculatedFromWisdom);
                mPage.getData().putString(SkillsPage.SENSES_DATA_KEY, senses);
            default:
                break;
        }

    }

    private String buildSkillString(String acrobatics, String animalHandling, String arcana, String athletics, String deception, String history, String insight, String intimidation, String investigation, String medicine, String nature, String perception, String performance, String persuasion, String religion, String sleightofHand, String stealth, String survival) {
        String skills = "";
        if (acrobatics != null && !acrobatics.isEmpty()) {
            if (Integer.valueOf(acrobatics) >= 0) {
                skills = "Acrobatics +" + acrobatics;
            } else {
                skills = "Acrobatics " + acrobatics;
            }
        }
        if (animalHandling != null && !animalHandling.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Animal Handling ";
            } else {
                skills = "Animal Handling ";
            }
            if (Integer.valueOf(animalHandling) >= 0) {
                skills = skills + "+" + animalHandling;
            } else {
                skills = skills + animalHandling;
            }
        }
        if (arcana != null && !arcana.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Arcana ";
            } else {
                skills = "Arcana ";
            }
            if (Integer.valueOf(arcana) >= 0) {
                skills = skills + "+" + arcana;
            } else {
                skills = skills + arcana;
            }
        }
        if (athletics != null && !athletics.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Athletics ";
            } else {
                skills = "Athletics ";
            }
            if (Integer.valueOf(athletics) >= 0) {
                skills = skills + "+" + athletics;
            } else {
                skills = skills + athletics;
            }
        }
        if (deception != null && !deception.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Deception ";
            } else {
                skills = "Deception ";
            }
            if (Integer.valueOf(deception) >= 0) {
                skills = skills + "+" + deception;
            } else {
                skills = skills + deception;
            }
        }
        if (history != null && !history.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", History ";
            } else {
                skills = "History ";
            }
            if (Integer.valueOf(history) >= 0) {
                skills = skills + "+" + history;
            } else {
                skills = skills + history;
            }
        }
        if (insight != null && !insight.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Insight ";
            } else {
                skills = "Insight ";
            }
            if (Integer.valueOf(insight) >= 0) {
                skills = skills + "+" + insight;
            } else {
                skills = skills + insight;
            }
        }
        if (intimidation != null && !intimidation.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Intimidation ";
            } else {
                skills = "Intimidation ";
            }
            if (Integer.valueOf(intimidation) >= 0) {
                skills = skills + "+" + intimidation;
            } else {
                skills = skills + intimidation;
            }
        }
        if (investigation != null && !investigation.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Investigation ";
            } else {
                skills = "Investigation ";
            }
            if (Integer.valueOf(investigation) >= 0) {
                skills = skills + "+" + investigation;
            } else {
                skills = skills + investigation;
            }
        }
        if (medicine != null && !medicine.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Medicine ";
            } else {
                skills = "Medicine ";
            }
            if (Integer.valueOf(medicine) >= 0) {
                skills = skills + "+" + medicine;
            } else {
                skills = skills + medicine;
            }
        }
        if (nature != null && !nature.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Nature ";
            } else {
                skills = "Nature ";
            }
            if (Integer.valueOf(nature) >= 0) {
                skills = skills + "+" + nature;
            } else {
                skills = skills + nature;
            }
        }
        if (perception != null && !perception.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Perception ";
            } else {
                skills = "Perception ";
            }
            if (Integer.valueOf(perception) >= 0) {
                skills = skills + "+" + perception;
            } else {
                skills = skills + perception;
            }
        }
        if (performance != null && !performance.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Performance ";
            } else {
                skills = "Performance ";
            }
            if (Integer.valueOf(performance) >= 0) {
                skills = skills + "+" + performance;
            } else {
                skills = skills + performance;
            }
        }
        if (persuasion != null && !persuasion.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Persuasion ";
            } else {
                skills = "Persuasion ";
            }
            if (Integer.valueOf(persuasion) >= 0) {
                skills = skills + "+" + persuasion;
            } else {
                skills = skills + persuasion;
            }
        }
        if (religion != null && !religion.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Religion ";
            } else {
                skills = "Religion ";
            }
            if (Integer.valueOf(religion) >= 0) {
                skills = skills + "+" + religion;
            } else {
                skills = skills + religion;
            }
        }
        if (sleightofHand != null && !sleightofHand.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Sleight of Hand ";
            } else {
                skills = "Sleight of Hand ";
            }
            if (Integer.valueOf(sleightofHand) >= 0) {
                skills = skills + "+" + sleightofHand;
            } else {
                skills = skills + sleightofHand;
            }
        }
        if (stealth != null && !stealth.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Stealth ";
            } else {
                skills = "Stealth ";
            }
            if (Integer.valueOf(stealth) >= 0) {
                skills = skills + "+" + stealth;
            } else {
                skills = skills + stealth;
            }
        }
        if (survival != null && !survival.isEmpty()) {
            if (!skills.isEmpty()) {
                skills = skills + ", Survival ";
            } else {
                skills = "Survival ";
            }
            if (Integer.valueOf(survival) >= 0) {
                skills = skills + "+" + survival;
            } else {
                skills = skills + survival;
            }
        }
        return skills;
    }

    private String buildSavingThrowString(String stStr, String stDex, String stCon, String stInt, String stWis, String stCha) {
        String savingThrows = "";
        if (stStr != null && !stStr.isEmpty()) {
            savingThrows = "Str ";
            if (Integer.valueOf(stStr) >= 0) {
                savingThrows = savingThrows + "+" + stStr;
            } else {
                savingThrows = savingThrows + stStr;
            }
        }
        if (stDex != null && !stDex.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Dex ";
            } else {
                savingThrows = "Dex ";
            }
            if (Integer.valueOf(stDex) >= 0) {
                savingThrows = savingThrows + "+" + stDex;
            } else {
                savingThrows = savingThrows + stDex;
            }
        }
        if (stCon != null && !stCon.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Con ";
            } else {
                savingThrows = "Con ";
            }
            if (Integer.valueOf(stCon) >= 0) {
                savingThrows = savingThrows + "+" + stCon;
            } else {
                savingThrows = savingThrows + stCon;
            }
        }
        if (stInt != null && !stInt.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Int ";
            } else {
                savingThrows = "Int ";
            }
            if (Integer.valueOf(stInt) >= 0) {
                savingThrows = savingThrows + "+" + stInt;
            } else {
                savingThrows = savingThrows + stInt;
            }
        }
        if (stWis != null && !stWis.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ",  Wis ";
            } else {
                savingThrows = "Wis ";
            }
            if (Integer.valueOf(stWis) >= 0) {
                savingThrows = savingThrows + "+" + stWis;
            } else {
                savingThrows = savingThrows + stWis;
            }
        }
        if (stCha != null && !stCha.isEmpty()) {
            if (!savingThrows.isEmpty()) {
                savingThrows = savingThrows + ", Cha ";
            } else {
                savingThrows = "Cha ";
            }
            if (Integer.valueOf(stCha) >= 0) {
                savingThrows = savingThrows + "+" + stCha;
            } else {
                savingThrows = savingThrows + stCha;
            }
        }
        return savingThrows;
    }

    @Override
    public void onWisdomChanged(String wisdom) {
        if (mPage != null) {
            mPage.getData().putString(SkillsPage.WISDOM_DATA_KEY, wisdom);
        }
        if (!isNotCalculatedFromWisdom && wisdom !=null && !wisdom.isEmpty()) {
            if (senses != null && !senses.isEmpty()) {
                if (!senses.contains("passive Perception")) {
                    passivePerception = String.valueOf(10 + AbilityModifierCalculator.calculateMod(Integer.valueOf(wisdom)));
                    senses = senses + ", passive Perception " + passivePerception;
                    if (mPage != null) {
                        mPage.getData().putString(SkillsPage.SENSES_DATA_KEY, senses);
                    }
                    tvSenses.setText(senses);
                } else {
                    ArrayList<String> selectedItems = splitString(senses);
                    if (selectedItems != null || selectedItems.size() != 0) {
                        senses = "";
                        for (int i = 0; i < selectedItems.size(); i++) {
                            if (!selectedItems.get(i).contains("passive Perception")) {
                                senses = senses + selectedItems.get(i) + ", ";
                            }
                        }
                        senses = senses + "passive Perception " + String.valueOf(10 + AbilityModifierCalculator.calculateMod(Integer.valueOf(wisdom)));
                        mPage.getData().putString(SkillsPage.SENSES_DATA_KEY, senses);
                        tvSenses.setText(senses);
                    }
                }
            } else {
                passivePerception = String.valueOf(10 + AbilityModifierCalculator.calculateMod(Integer.valueOf(wisdom)));
                if (tvSenses != null) {
                    senses = "passive Perception " + passivePerception;
                    tvSenses.setText(senses);
                }
                if (mPage != null) {
                    mPage.getData().putString(SkillsPage.SENSES_DATA_KEY, senses);
                }
            }
        }else if(!isNotCalculatedFromWisdom){
            senses = "";
            tvSenses.setText(senses);
            if (mPage != null) {
                mPage.getData().putString(SkillsPage.SENSES_DATA_KEY, senses);
            }
        }
    }


    private ArrayList<String> splitString(String senses) {
        ArrayList<String> sensesList = new ArrayList<>();
        StringTokenizer tokens = new StringTokenizer(senses, ",");
        while (tokens.hasMoreTokens()) {
            sensesList.add(tokens.nextToken().trim());
        }
        return sensesList;
    }
}
