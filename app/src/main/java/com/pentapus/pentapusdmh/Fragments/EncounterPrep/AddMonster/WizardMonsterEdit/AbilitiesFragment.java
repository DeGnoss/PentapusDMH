package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koni on 11.11.2016.
 */

public class AbilitiesFragment extends Fragment {
    private static final String ARG_KEY = "abilities";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private AbilitiesPage mPage;
    private TextView mStrView, mDexView, mConView, mIntView, mWisView, mChaView;
    private TextView mHpView, mHpDiceView, mAcView;
    private CustomAutoCompleteTextView mAcTypeView;
    ArrayAdapter<String> mSuggestionAdapter;
    String[] item;
    private TextView mStStrView, mStDexView, mStConView, mStIntView, mStWisView, mStChaView;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_abilities, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

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



        mHpView = ((TextView) rootView.findViewById(R.id.tvHP));
        mHpView.setText(mPage.getData().getString(AbilitiesPage.HP_DATA_KEY));

        mHpDiceView = ((TextView) rootView.findViewById(R.id.tvHitDice));
        mHpDiceView.setText(mPage.getData().getString(AbilitiesPage.HITDICE_DATA_KEY));

        mAcView = ((TextView) rootView.findViewById(R.id.tvAC));
        mAcView.setText(mPage.getData().getString(AbilitiesPage.AC_DATA_KEY));

        mAcTypeView = ((CustomAutoCompleteTextView) rootView.findViewById(R.id.tvACType));
        mAcTypeView.setText(mPage.getData().getString(AbilitiesPage.ACTYPE_DATA_KEY));
        mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
        mAcTypeView.setAdapter(mSuggestionAdapter);



        mStStrView = ((TextView) rootView.findViewById(R.id.tvStStr));
        mStStrView.setText(mPage.getData().getString(AbilitiesPage.STSTR_DATA_KEY));

        mStDexView = ((TextView) rootView.findViewById(R.id.tvStDex));
        mStDexView.setText(mPage.getData().getString(AbilitiesPage.STDEX_DATA_KEY));

        mStConView = ((TextView) rootView.findViewById(R.id.tvStCon));
        mStConView.setText(mPage.getData().getString(AbilitiesPage.STCON_DATA_KEY));

        mStIntView = ((TextView) rootView.findViewById(R.id.tvStInt));
        mStIntView.setText(mPage.getData().getString(AbilitiesPage.STINT_DATA_KEY));

        mStWisView = ((TextView) rootView.findViewById(R.id.tvStWis));
        mStWisView.setText(mPage.getData().getString(AbilitiesPage.STWIS_DATA_KEY));

        mStChaView = ((TextView) rootView.findViewById(R.id.tvStCha));
        mStChaView.setText(mPage.getData().getString(AbilitiesPage.STCHA_DATA_KEY));

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
                mPage.notifyDataChanged();
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


        mHpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.HP_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mHpDiceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.HITDICE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mAcView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.AC_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mAcTypeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // query the database based on the user input
                item = getItemsFromDb(charSequence.toString(), "actype");

                // update the adapater
                mSuggestionAdapter.notifyDataSetChanged();
                mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item);
                mAcTypeView.setAdapter(mSuggestionAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.ACTYPE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });



        mStStrView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.STSTR_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mStDexView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.STDEX_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mStConView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.STCON_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mStIntView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.STINT_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mStWisView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.STWIS_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mStChaView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AbilitiesPage.STCHA_DATA_KEY,
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
}
