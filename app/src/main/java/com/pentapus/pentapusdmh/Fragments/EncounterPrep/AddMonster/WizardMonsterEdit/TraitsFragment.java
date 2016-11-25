package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.Tracker.EnterInitiativeDialogFragment;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

/**
 * Created by Koni on 11.11.2016.
 */

public class TraitsFragment extends Fragment {
    private static final String ARG_KEY = "traits";
    private int MSG_SHOW_DIALOG = 1000, MSG_FINISH_DIALOG = 1001;

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private TraitsPage mPage;
    private TextView l2, l3, l4, l5, l6, l7, l8, l9, l10;
    private TextView tvTrait1Name, tvTrait1Description, tvTrait2Name, tvTrait2Description, tvTrait3Name, tvTrait3Description, tvTrait4Name, tvTrait4Description, tvTrait5Name, tvTrait5Description;
    private FloatingActionButton fabTraits;

    public static TraitsFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        TraitsFragment fragment = new TraitsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TraitsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (TraitsPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_traits, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        tvTrait1Name = ((TextView) rootView.findViewById(R.id.tvTrait1Name));
        tvTrait1Name.setText(mPage.getData().getString(TraitsPage.T1NAME_DATA_KEY));
        //tvTrait1Name.setVisibility(View.GONE);

        l2 = ((TextView) rootView.findViewById(R.id.l2));
        l2.setVisibility(View.GONE);

        tvTrait1Description = ((TextView) rootView.findViewById(R.id.tvTrait1Description));
        tvTrait1Description.setText(mPage.getData().getString(TraitsPage.T1DESC_DATA_KEY));
        tvTrait1Description.setVisibility(View.GONE);

        l3 = ((TextView) rootView.findViewById(R.id.l3));
        l3.setVisibility(View.GONE);

        tvTrait2Name = ((TextView) rootView.findViewById(R.id.tvTrait2Name));
        tvTrait2Name.setText(mPage.getData().getString(TraitsPage.T2NAME_DATA_KEY));
        tvTrait2Name.setVisibility(View.GONE);

        l4 = ((TextView) rootView.findViewById(R.id.l4));
        l4.setVisibility(View.GONE);

        tvTrait2Description = ((TextView) rootView.findViewById(R.id.tvTrait2Description));
        tvTrait2Description.setText(mPage.getData().getString(TraitsPage.T2DESC_DATA_KEY));
        tvTrait2Description.setVisibility(View.GONE);

        l5 = ((TextView) rootView.findViewById(R.id.l5));
        l5.setVisibility(View.GONE);

        tvTrait3Name = ((TextView) rootView.findViewById(R.id.tvTrait3Name));
        tvTrait3Name.setText(mPage.getData().getString(TraitsPage.T3NAME_DATA_KEY));
        tvTrait3Name.setVisibility(View.GONE);

        l6 = ((TextView) rootView.findViewById(R.id.l6));
        l6.setVisibility(View.GONE);

        tvTrait3Description = ((TextView) rootView.findViewById(R.id.tvTrait3Description));
        tvTrait3Description.setText(mPage.getData().getString(TraitsPage.T3DESC_DATA_KEY));
        tvTrait3Description.setVisibility(View.GONE);

        l7 = ((TextView) rootView.findViewById(R.id.l7));
        l7.setVisibility(View.GONE);

        tvTrait4Name = ((TextView) rootView.findViewById(R.id.tvTrait4Name));
        tvTrait4Name.setText(mPage.getData().getString(TraitsPage.T4NAME_DATA_KEY));
        tvTrait4Name.setVisibility(View.GONE);

        l8 = ((TextView) rootView.findViewById(R.id.l8));
        l8.setVisibility(View.GONE);

        tvTrait4Description = ((TextView) rootView.findViewById(R.id.tvTrait4Description));
        tvTrait4Description.setText(mPage.getData().getString(TraitsPage.T4DESC_DATA_KEY));
        tvTrait4Description.setVisibility(View.GONE);

        l9 = ((TextView) rootView.findViewById(R.id.l9));
        l9.setVisibility(View.GONE);

        tvTrait5Name = ((TextView) rootView.findViewById(R.id.tvTrait5Name));
        tvTrait5Name.setText(mPage.getData().getString(TraitsPage.T5NAME_DATA_KEY));
        tvTrait5Name.setVisibility(View.GONE);

        l10 = ((TextView) rootView.findViewById(R.id.l10));
        l10.setVisibility(View.GONE);

        tvTrait5Description = ((TextView) rootView.findViewById(R.id.tvTrait5Description));
        tvTrait5Description.setText(mPage.getData().getString(TraitsPage.T5DESC_DATA_KEY));
        tvTrait5Description.setVisibility(View.GONE);

        fabTraits = (FloatingActionButton) rootView.findViewById(R.id.fabTraits);
        fabTraits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvTrait1Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T1NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvTrait1Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T1DESC_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvTrait2Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T2NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvTrait2Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T2DESC_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });


        tvTrait3Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T3NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvTrait3Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T3DESC_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });


        tvTrait4Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T4NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvTrait4Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T4DESC_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });


        tvTrait5Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T5NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        tvTrait5Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(TraitsPage.T5DESC_DATA_KEY,
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
        if (tvTrait1Name != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public void onFabClick(){
        DialogFragment newFragment = AddTraitDialogFragment.newInstance();
        newFragment.setTargetFragment(this, MSG_FINISH_DIALOG);
        newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDTRAIT_DIALOG");
    }

    public void onDialogResult(int requestCode, String name, String description) {
        Spanned trait1 = Html.fromHtml("<b>" + name + ". </b> " + description);
        tvTrait1Name.setText(trait1);
        //tvTrait1Name.setVisibility(View.VISIBLE);
        /*l2.setVisibility(View.VISIBLE);
        tvTrait1Description.setText(description);
        tvTrait1Description.setVisibility(View.VISIBLE);*/
    }
}
