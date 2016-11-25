package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

/**
 * Created by Koni on 11.11.2016.
 */

public class ActionCreationFragment extends Fragment{
    private static final String ARG_KEY = "actions";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private ActionPage mPage;
    private TextView tvAction1Name, tvAction1Desc, tvAction1Mod, tvDmg1Roll1, tvDmg1Roll2;
    private Spinner spDmg1Type1, spDmg1Type2;
    private CheckBox cbAction1Auto, cbAction1Add;

    public static ActionCreationFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        ActionCreationFragment fragment = new ActionCreationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ActionCreationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (ActionPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_actions, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        tvAction1Name = ((TextView) rootView.findViewById(R.id.tvAction1Name));
        tvAction1Name.setText(mPage.getData().getString(ActionPage.A1NAME_DATA_KEY));

        tvAction1Desc = ((TextView) rootView.findViewById(R.id.tvAction1Description));
        tvAction1Desc.setText(mPage.getData().getString(ActionPage.A1DESC_DATA_KEY));

        tvAction1Mod = ((TextView) rootView.findViewById(R.id.tvActionMod1));
        tvAction1Mod.setText(mPage.getData().getString(ActionPage.A1MOD_DATA_KEY));

        tvDmg1Roll1 = ((TextView) rootView.findViewById(R.id.tvA1D1));
        tvDmg1Roll1.setText(mPage.getData().getString(ActionPage.A1ROLL1_DATA_KEY));

        spDmg1Type1 = (Spinner) rootView.findViewById(R.id.spinner_a1d1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.damagetypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDmg1Type1.setAdapter(adapter);

        tvDmg1Roll2 = ((TextView) rootView.findViewById(R.id.tvA1D2));
        tvDmg1Roll2.setText(mPage.getData().getString(ActionPage.A1ROLL2_DATA_KEY));

        spDmg1Type2 = (Spinner) rootView.findViewById(R.id.spinner_a1d2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.damagetypes_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDmg1Type2.setAdapter(adapter2);

        cbAction1Auto = ((CheckBox) rootView.findViewById(R.id.cbAuto1));
        cbAction1Auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                if (checked){
                    mPage.getData().putString(ActionPage.A1AUTO_DATA_KEY, "1");
                    mPage.notifyDataChanged();
                }else{
                    mPage.getData().putString(ActionPage.A1AUTO_DATA_KEY, "0");
                    mPage.notifyDataChanged();
                }
            }
        });

        cbAction1Add = ((CheckBox) rootView.findViewById(R.id.cbAdd1));
        cbAction1Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                if (checked){
                    mPage.getData().putString(ActionPage.A1ADD_DATA_KEY, "1");
                    mPage.notifyDataChanged();
                }else{
                    mPage.getData().putString(ActionPage.A1ADD_DATA_KEY, "0");
                    mPage.notifyDataChanged();
                }
            }
        });



        return rootView;
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch(parent.getId()){
            case R.id.spinner_a1d1:
                mPage.getData().putString(ActionPage.A1TYPE1_DATA_KEY, parent.getItemAtPosition(pos).toString());
                break;
            case R.id.spinner_a1d2:
                mPage.getData().putString(ActionPage.A1TYPE2_DATA_KEY, parent.getItemAtPosition(pos).toString());
                break;
        }
        mPage.notifyDataChanged();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        switch(parent.getId()){
            case R.id.spinner_a1d1:
                mPage.getData().putString(ActionPage.A1TYPE1_DATA_KEY, null);
                break;
            case R.id.spinner_a1d2:
                mPage.getData().putString(ActionPage.A1TYPE2_DATA_KEY, null);
                break;
        }
        mPage.notifyDataChanged();
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


        tvAction1Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(ActionPage.A1NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvAction1Desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(ActionPage.A1DESC_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvAction1Mod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(ActionPage.A1MOD_DATA_KEY,
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
        if (tvAction1Name != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
}
