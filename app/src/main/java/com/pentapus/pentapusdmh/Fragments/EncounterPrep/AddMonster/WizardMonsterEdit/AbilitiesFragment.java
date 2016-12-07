package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.HelperClasses.AbilityModifierCalculator;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.HelperClasses.HitDiceCalculator;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Koni on 11.11.2016.
 */

public class AbilitiesFragment extends Fragment implements BasicInfoFragment.OnSizeChangedListener {
    private static final String ARG_KEY = "abilities";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private AbilitiesPage mPage;
    private TextView mStrView, mDexView, mConView, mIntView, mWisView, mChaView;
    private TextView tvAC, labelAC, tvHP, labelHP, tvSpeed, labelSpeed;
    private String ac1type, ac2type, ac, hp, speed, ac1, ac2, size;
    private int hitdice;
    private CustomAutoCompleteTextView mAcTypeView;
    ArrayAdapter<String> mSuggestionAdapter;
    String[] item;
    private static final int MSG_AC_DIALOG = 1001, MSG_HP_DIALOG = 1002, MSG_SPEED_DIALOG = 1003;
    AbilitiesFragment fragment;


    public static AbilitiesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        AbilitiesFragment fragment = new AbilitiesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AbilitiesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AbilitiesPage) mCallbacks.onGetPage(mKey);
        mPage.notifyDataChanged();
        fragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_abilities, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mPage.getData().putString(AbilitiesPage.STR_DATA_KEY, "10");
        mPage.getData().putString(AbilitiesPage.DEX_DATA_KEY, "10");
        mPage.getData().putString(AbilitiesPage.CON_DATA_KEY, "10");
        mPage.getData().putString(AbilitiesPage.INT_DATA_KEY, "10");
        mPage.getData().putString(AbilitiesPage.WIS_DATA_KEY, "10");
        mPage.getData().putString(AbilitiesPage.CHA_DATA_KEY, "10");


        mStrView = ((TextView) rootView.findViewById(R.id.tvStrength));
        mStrView.setText(mPage.getData().getString(AbilitiesPage.STR_DATA_KEY));

        mDexView = ((TextView) rootView.findViewById(R.id.tvDex));
        mDexView.setText(mPage.getData().getString(AbilitiesPage.DEX_DATA_KEY));

        mConView = ((TextView) rootView.findViewById(R.id.tvCon));
        mConView.setText(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY));

        mIntView = ((TextView) rootView.findViewById(R.id.tvInt));
        mIntView.setText(mPage.getData().getString(AbilitiesPage.INT_DATA_KEY));

        mWisView = ((TextView) rootView.findViewById(R.id.tvWis));
        mWisView.setText(mPage.getData().getString(AbilitiesPage.WIS_DATA_KEY));

        mChaView = ((TextView) rootView.findViewById(R.id.tvCha));
        mChaView.setText(mPage.getData().getString(AbilitiesPage.CHA_DATA_KEY));

        ac1 = mPage.getData().getString(AbilitiesPage.AC1_DATA_KEY);
        ac1type = mPage.getData().getString(AbilitiesPage.AC1TYPE_DATA_KEY);
        ac2 = mPage.getData().getString(AbilitiesPage.AC2_DATA_KEY);
        ac2type = mPage.getData().getString(AbilitiesPage.AC2TYPE_DATA_KEY);


        tvAC = ((TextView) rootView.findViewById(R.id.tvAC));
        if (ac1type == null || ac1type.isEmpty()) {
            if (ac1 != null && !ac1.isEmpty()) {
                ac = String.valueOf(ac1);
            }
        } else {
            ac = ac1 + " (" + ac1type + ") ";
        }
        if (ac2 != null && !ac2.isEmpty()) {
            if (ac2type != null && !ac2type.isEmpty()) {
                if (ac == null || ac.isEmpty()) {
                    ac = ac2 + " (" + ac2type + ")";
                } else {
                    ac = ac + ", " + ac2 + " (" + ac2type + ")";
                }
            } else {
                if (ac == null || ac.isEmpty()) {
                    ac = ac2;
                } else {
                    ac = ac + ", " + ac2;
                }
            }
        }
        if (ac != null && !ac.isEmpty()) {
            tvAC.setText(ac);
        }
        tvAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddACDialogFragment.newInstance(ac1, ac2, ac1type, ac2type);
                newFragment.setTargetFragment(fragment, MSG_AC_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_AC_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDAC_DIALOG");
            }
        });

        labelAC = ((TextView) rootView.findViewById(R.id.labelAC));
        labelAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddACDialogFragment.newInstance(ac1, ac2, ac1type, ac2type);
                newFragment.setTargetFragment(fragment, MSG_AC_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_AC_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDAC_DIALOG");
            }
        });


        labelHP = ((TextView) rootView.findViewById(R.id.labelHP));
        labelHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = AddHPDialogFragment.newInstance(hp, AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY))), size, mPage.getData().getInt(AbilitiesPage.HITDICE_DATA_KEY));
                newFragment.setTargetFragment(fragment, MSG_HP_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_HP_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDHP_DIALOG");
            }
        });

        tvHP = ((TextView) rootView.findViewById(R.id.tvHitPoints));
        tvHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddHPDialogFragment.newInstance(hp, AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY))), size, mPage.getData().getInt(AbilitiesPage.HITDICE_DATA_KEY));
                newFragment.setTargetFragment(fragment, MSG_HP_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_HP_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDHP_DIALOG");
            }
        });

        size = mPage.getData().getString(AbilitiesPage.SIZE_DATA_KEY);
        hp = mPage.getData().getString(AbilitiesPage.HP_DATA_KEY);
        hitdice = mPage.getData().getInt(AbilitiesPage.HITDICE_DATA_KEY);
        String hpString;
        if (hp != null && !hp.isEmpty() && !Objects.equals(hp, "0") && size != null) {
            hpString = hp + " (" + hitdice + HitDiceCalculator.calculateHdType(hitdice, size, AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY)))) + ")";
        } else {
            hpString = "";
        }
        tvHP.setText(hpString);


        labelSpeed = ((TextView) rootView.findViewById(R.id.labelSpeed));
        labelSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSpeedDialogFragment.newInstance(speed);
                newFragment.setTargetFragment(fragment, MSG_SPEED_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SPEED_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSPEED_DIALOG");
            }
        });

        tvSpeed = ((TextView) rootView.findViewById(R.id.tvSpeed));
        tvSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = AddSpeedDialogFragment.newInstance(speed);
                newFragment.setTargetFragment(fragment, MSG_SPEED_DIALOG);
                newFragment.setTargetFragment(fragment, MSG_SPEED_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDSPEED_DIALOG");
            }
        });

        speed = mPage.getData().getString(AbilitiesPage.SPEED_DATA_KEY);
        tvSpeed.setText(speed);


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


        mStrView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.STR_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mDexView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.DEX_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mConView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.CON_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                String hpString;
                if (editable != null && !editable.toString().isEmpty()) {
                    hp = String.valueOf(HitDiceCalculator.calculateAverageHp(hitdice, size, AbilityModifierCalculator.calculateMod(Integer.valueOf((editable != null) ? editable.toString() : null))));
                } else {
                    hp = "0";
                }
                mPage.getData().putString(AbilitiesPage.HP_DATA_KEY, hp);
                mPage.notifyDataChanged();
                String conmod = mPage.getData().getString(AbilitiesPage.CON_DATA_KEY);
                if (hp != null && !hp.isEmpty() && !Objects.equals(hp, "0")) {
                    if (conmod != null && !conmod.isEmpty()) {
                        hpString = hp + " (" + hitdice + HitDiceCalculator.calculateHdType(hitdice, size, AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY)))) + ")";
                    } else {
                        hpString = hp + " (" + hitdice + HitDiceCalculator.calculateHdType(hitdice, size, 0) + ")";
                    }
                } else {
                    if (conmod != null && !conmod.isEmpty()) {
                        hpString = "";
                    } else {
                        hpString = "Enter Constitution Score to calculate HP";
                    }
                }
                tvHP.setText(hpString);
            }
        });

        mIntView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.INT_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mWisView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.WIS_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mChaView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.CHA_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
    }


    public void onDialogResult(int requestCode, Bundle results) {
        switch (requestCode) {
            case MSG_AC_DIALOG:
                ac1 = results.getString(AbilitiesPage.AC1_DATA_KEY);
                ac1type = results.getString(AbilitiesPage.AC1TYPE_DATA_KEY);
                ac2 = results.getString(AbilitiesPage.AC2_DATA_KEY);
                ac2type = results.getString(AbilitiesPage.AC2TYPE_DATA_KEY);

                if (ac1type == null || ac1type.isEmpty()) {
                    ac = String.valueOf(ac1);
                } else {
                    ac = ac1 + " (" + ac1type + ") ";
                }
                if (ac2 != null && !ac2.isEmpty()) {
                    if (ac2type != null && !ac2type.isEmpty()) {
                        if (ac == null || ac.isEmpty()) {
                            ac = ac2 + " (" + ac2type + ")";
                        } else {
                            ac = ac + ", " + ac2 + " (" + ac2type + ")";
                        }
                    } else {
                        if (ac == null || ac.isEmpty()) {
                            ac = ac2;
                        } else {
                            ac = ac + ", " + ac2;
                        }
                    }

                }

                if (!ac.isEmpty()) {
                    tvAC.setText(ac);
                } else {
                    tvAC.setText("");
                }

                mPage.getData().putString(AbilitiesPage.AC1_DATA_KEY,
                        ac1);
                mPage.getData().putString(AbilitiesPage.AC1TYPE_DATA_KEY,
                        ac1type);
                mPage.getData().putString(AbilitiesPage.AC2_DATA_KEY,
                        ac2);
                mPage.getData().putString(AbilitiesPage.AC2TYPE_DATA_KEY,
                        ac2type);
                break;
            case MSG_HP_DIALOG:
                hp = results.getString(AbilitiesPage.HP_DATA_KEY);
                mPage.getData().putString(AbilitiesPage.HP_DATA_KEY,
                        hp);
                hitdice = results.getInt(AbilitiesPage.HITDICE_DATA_KEY);
                mPage.getData().putInt(AbilitiesPage.HITDICE_DATA_KEY,
                        hitdice);
                String hpString;
                if (hp != null && !hp.isEmpty() && !Objects.equals(hp, "0")) {
                    hpString = hp + " (" + hitdice + HitDiceCalculator.calculateHdType(hitdice, size, AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY)))) + ")";
                } else {
                    hpString = "";
                }
                tvHP.setText(hpString);
                break;
            case MSG_SPEED_DIALOG:
                speed = results.getString(AbilitiesPage.SPEED_DATA_KEY);
                mPage.getData().putString(AbilitiesPage.SPEED_DATA_KEY,
                        speed);
                tvSpeed.setText(speed);
                break;
            default:
                break;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mStrView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public String[] getItemsFromDb(String searchTerm, String column) {

        // add items on the array dynamically
        List<String> entries = filterData(searchTerm, column);
        int rowCount = entries.size();

        String[] item = new String[rowCount];
        //String[] item = new String[1];
        int x = 0;
        //item[x] = "Monstrosity";

        for (String record : entries) {

            item[x] = record;
            x++;
        }

        return item;
    }


    public List<String> filterData(String filterArgs, String column) {
        List<String> results = new ArrayList<String>();

        Uri uri = DbContentProvider.CONTENT_URI_MONSTER;
        String selection = "";
        String[] selectionArgs;
        List<String> selectionList = new ArrayList<>();

        String filter = "%" + filterArgs + "%";
        selectionList.add(filter);
        selectionArgs = new String[selectionList.size()];
        selectionList.toArray(selectionArgs);
        if (filterArgs != null) {
            selection = column + " LIKE ?";
        }

        //String filterFormatted = "%" + filterArgs + "%";
        //filters.putString("filter", filterFormatted);
        Cursor cursor = getContext().getContentResolver().query(uri, new String[]{"DISTINCT " + column}, selection, selectionArgs,
                null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(cursor.getColumnIndexOrThrow(column)));
            } while (cursor.moveToNext());
        }

        return results;
    }

    @Override
    public void onSizeChanged(String sizeFromListener) {
        size = sizeFromListener;
        mPage.getData().putString("size", size);
        String hpString;
        hp = String.valueOf(HitDiceCalculator.calculateAverageHp(hitdice, size, AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY)))));
        if (!hp.isEmpty() && !Objects.equals(hp, "0")) {
            hpString = hp + " (" + hitdice + HitDiceCalculator.calculateHdType(hitdice, size, AbilityModifierCalculator.calculateMod(Integer.valueOf(mPage.getData().getString(AbilitiesPage.CON_DATA_KEY)))) + ")";
        } else {
            hpString = "";
        }
        tvHP.setText(hpString);
    }
}
