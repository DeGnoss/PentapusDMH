package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
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

/**
 * Created by Koni on 11.11.2016.
 */

public class SkillsFragment extends Fragment {
    private static final String ARG_KEY = "basicinfo";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private SkillsPage mPage;
    private TextView tvAcrobatics, tvAnimalHandling, tvArcana, tvAthletics, tvDeception, tvHistory, tvInsight, tvIntimidation, tvInvestigation, tvMedicine, tvNature, tvPerception, tvPerformance, tvPersuasion, tvReligion, tvSleightOfHand, tvStealth, tvSurvival;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_skills, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        tvAcrobatics = ((TextView) rootView.findViewById(R.id.tvAcrobatics));
        tvAcrobatics.setText(mPage.getData().getString(SkillsPage.ACROBATICS_DATA_KEY));

        tvAnimalHandling = ((TextView) rootView.findViewById(R.id.tvAnimalHandling));
        tvAnimalHandling.setText(mPage.getData().getString(SkillsPage.ANIMALHANDLING_DATA_KEY));

        tvArcana = ((TextView) rootView.findViewById(R.id.tvArcana));
        tvArcana.setText(mPage.getData().getString(SkillsPage.ARCANA_DATA_KEY));

        tvAthletics = ((TextView) rootView.findViewById(R.id.tvAthletics));
        tvAthletics.setText(mPage.getData().getString(SkillsPage.ATHLETICS_DATA_KEY));

        tvDeception = ((TextView) rootView.findViewById(R.id.tvDeception));
        tvDeception.setText(mPage.getData().getString(SkillsPage.DECEPTION_DATA_KEY));

        tvHistory = ((TextView) rootView.findViewById(R.id.tvHistory));
        tvHistory.setText(mPage.getData().getString(SkillsPage.HISTORY_DATA_KEY));

        tvInsight = ((TextView) rootView.findViewById(R.id.tvInsight));
        tvInsight.setText(mPage.getData().getString(SkillsPage.INSIGHT_DATA_KEY));

        tvIntimidation = ((TextView) rootView.findViewById(R.id.tvIntimidation));
        tvIntimidation.setText(mPage.getData().getString(SkillsPage.INTIMIDATION_DATA_KEY));

        tvInvestigation = ((TextView) rootView.findViewById(R.id.tvInvestigation));
        tvInvestigation.setText(mPage.getData().getString(SkillsPage.INVESTIGATION_DATA_KEY));

        tvMedicine = ((TextView) rootView.findViewById(R.id.tvMedicine));
        tvMedicine.setText(mPage.getData().getString(SkillsPage.MEDICINE_DATA_KEY));

        tvNature = ((TextView) rootView.findViewById(R.id.tvNature));
        tvNature.setText(mPage.getData().getString(SkillsPage.NATURE_DATA_KEY));

        tvPerception = ((TextView) rootView.findViewById(R.id.tvPerception));
        tvPerception.setText(mPage.getData().getString(SkillsPage.PERCEPTION_DATA_KEY));

        tvPerformance = ((TextView) rootView.findViewById(R.id.tvPerformance));
        tvPerformance.setText(mPage.getData().getString(SkillsPage.PERFORMANCE_DATA_KEY));

        tvPersuasion = ((TextView) rootView.findViewById(R.id.tvPersuasion));
        tvPersuasion.setText(mPage.getData().getString(SkillsPage.PERSUASION_DATA_KEY));

        tvReligion = ((TextView) rootView.findViewById(R.id.tvReligion));
        tvReligion.setText(mPage.getData().getString(SkillsPage.RELIGION_DATA_KEY));

        tvSleightOfHand = ((TextView) rootView.findViewById(R.id.tvSleightOfHand));
        tvSleightOfHand.setText(mPage.getData().getString(SkillsPage.SLEIGHTOFHAND_DATA_KEY));

        tvStealth = ((TextView) rootView.findViewById(R.id.tvStealth));
        tvStealth.setText(mPage.getData().getString(SkillsPage.STEALTH_DATA_KEY));

        tvSurvival = ((TextView) rootView.findViewById(R.id.tvSurvival));
        tvSurvival.setText(mPage.getData().getString(SkillsPage.SURVIVAL_DATA_KEY));

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvAcrobatics.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.ACROBATICS_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvAnimalHandling.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.ANIMALHANDLING_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvArcana.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.ARCANA_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvAthletics.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.ATHLETICS_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvDeception.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.DECEPTION_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvHistory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.HISTORY_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvInsight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.INSIGHT_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvIntimidation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.INTIMIDATION_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvInvestigation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.INVESTIGATION_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvMedicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.MEDICINE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvNature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.NATURE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvPerception.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.PERCEPTION_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvPerformance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.PERFORMANCE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvPersuasion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.PERSUASION_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvReligion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.RELIGION_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvSleightOfHand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.SLEIGHTOFHAND_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvStealth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.STEALTH_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvSurvival.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SkillsPage.SURVIVAL_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (tvAcrobatics != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
}
