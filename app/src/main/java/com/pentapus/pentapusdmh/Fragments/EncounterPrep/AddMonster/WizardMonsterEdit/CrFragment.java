package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.Fragments.Spells.SpellViewPagerDialogFragment;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Koni on 11.11.2016.
 */

public class CrFragment extends Fragment{
    private static final String ARG_KEY = "cr";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private CrPage mPage;
    private EditText tvCR, tvXP;
    CrFragment targetFragment;

    private String cr, xp;
    private boolean isSpellCaster;

    private static final int MSG_SPELL_DIALOG = 1001;


    public static CrFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        CrFragment fragment = new CrFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (CrPage) mCallbacks.onGetPage(mKey);
        targetFragment = this;

        mPage.notifyDataChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_cr, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        tvCR = (EditText) rootView.findViewById(R.id.tvCR);
        if(mPage.getData().getString(CrPage.CR_DATA_KEY) != null && !mPage.getData().getString(CrPage.CR_DATA_KEY).isEmpty()){
            tvCR.setText(mPage.getData().getString(CrPage.CR_DATA_KEY));
        }
        tvXP = (EditText) rootView.findViewById(R.id.tvXP);

        if(mPage.getData().getString(CrPage.XP_DATA_KEY) != null && !mPage.getData().getString(CrPage.XP_DATA_KEY).isEmpty()) {
            tvXP.setText(mPage.getData().getString(CrPage.XP_DATA_KEY));
        }

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

        tvCR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(CrPage.CR_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvXP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(CrPage.XP_DATA_KEY,
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
    }
}
