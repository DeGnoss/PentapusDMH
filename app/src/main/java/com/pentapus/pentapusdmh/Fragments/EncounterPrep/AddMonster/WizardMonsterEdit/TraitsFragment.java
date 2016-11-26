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
    private int traitCounter;

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
        Spanned trait = Html.fromHtml("<b>" + mPage.getData().getString(TraitsPage.T1NAME_DATA_KEY) + ".</b>" + mPage.getData().getString(TraitsPage.T1DESC_DATA_KEY));
        tvTrait1Name.setText(trait);
        if (mPage.getData().getString(TraitsPage.T1NAME_DATA_KEY) == null || (mPage.getData().getString(TraitsPage.T1NAME_DATA_KEY).isEmpty())) {
            tvTrait1Name.setVisibility(View.GONE);
        }
        tvTrait1Name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onFabClick(1, mPage.getData().getString(TraitsPage.T1NAME_DATA_KEY), mPage.getData().getString(TraitsPage.T1DESC_DATA_KEY), 1);
            }
        });

        tvTrait2Name = ((TextView) rootView.findViewById(R.id.tvTrait2Name));
        trait = Html.fromHtml("<b>" + mPage.getData().getString(TraitsPage.T2NAME_DATA_KEY) + ".</b>" + mPage.getData().getString(TraitsPage.T2DESC_DATA_KEY));
        tvTrait2Name.setText(trait);
        if (mPage.getData().getString(TraitsPage.T2NAME_DATA_KEY) == null || (mPage.getData().getString(TraitsPage.T2NAME_DATA_KEY).isEmpty())) {
            tvTrait2Name.setVisibility(View.GONE);
        }
        tvTrait2Name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onFabClick(1, mPage.getData().getString(TraitsPage.T2NAME_DATA_KEY), mPage.getData().getString(TraitsPage.T2DESC_DATA_KEY), 2);
            }
        });

        tvTrait3Name = ((TextView) rootView.findViewById(R.id.tvTrait3Name));
        trait = Html.fromHtml("<b>" + mPage.getData().getString(TraitsPage.T3NAME_DATA_KEY) + ".</b>" + mPage.getData().getString(TraitsPage.T3DESC_DATA_KEY));
        tvTrait3Name.setText(trait);
        if (mPage.getData().getString(TraitsPage.T3NAME_DATA_KEY) == null || (mPage.getData().getString(TraitsPage.T3NAME_DATA_KEY).isEmpty())) {
            tvTrait3Name.setVisibility(View.GONE);
        }
        tvTrait3Name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onFabClick(1, mPage.getData().getString(TraitsPage.T3NAME_DATA_KEY), mPage.getData().getString(TraitsPage.T3DESC_DATA_KEY), 3);
            }
        });

        tvTrait4Name = ((TextView) rootView.findViewById(R.id.tvTrait4Name));
        trait = Html.fromHtml("<b>" + mPage.getData().getString(TraitsPage.T4NAME_DATA_KEY) + ".</b>" + mPage.getData().getString(TraitsPage.T4DESC_DATA_KEY));
        tvTrait4Name.setText(trait);
        if (mPage.getData().getString(TraitsPage.T4NAME_DATA_KEY) == null || (mPage.getData().getString(TraitsPage.T4NAME_DATA_KEY).isEmpty())) {
            tvTrait4Name.setVisibility(View.GONE);
        }
        tvTrait4Name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onFabClick(1, mPage.getData().getString(TraitsPage.T4NAME_DATA_KEY), mPage.getData().getString(TraitsPage.T4DESC_DATA_KEY), 4);
            }
        });

        tvTrait5Name = ((TextView) rootView.findViewById(R.id.tvTrait5Name));
        trait = Html.fromHtml("<b>" + mPage.getData().getString(TraitsPage.T5NAME_DATA_KEY) + ".</b>" + mPage.getData().getString(TraitsPage.T5DESC_DATA_KEY));
        tvTrait5Name.setText(trait);
        if (mPage.getData().getString(TraitsPage.T5NAME_DATA_KEY) == null || (mPage.getData().getString(TraitsPage.T5NAME_DATA_KEY).isEmpty())) {
            tvTrait5Name.setVisibility(View.GONE);
        }
        tvTrait5Name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onFabClick(1, mPage.getData().getString(TraitsPage.T5NAME_DATA_KEY), mPage.getData().getString(TraitsPage.T5DESC_DATA_KEY), 5);
            }
        });

        fabTraits = (FloatingActionButton) rootView.findViewById(R.id.fabTraits);
        fabTraits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick(0, null, null, 0);
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

    public void onFabClick(int mode, String name, String description, int traitNumber) {
        DialogFragment newFragment = AddTraitDialogFragment.newInstance(mode, name, description, traitNumber);
        newFragment.setTargetFragment(this, MSG_FINISH_DIALOG);
        newFragment.setTargetFragment(this, MSG_FINISH_DIALOG);
        newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDTRAIT_DIALOG");
    }

    public void onDialogResult(int requestCode, int mode, String name, String description, int traitNumber) {
        Spanned trait = Html.fromHtml("<b>" + name + ". </b> " + description);
        if (mode == 0) {
            switch (traitCounter) {
                case 0:
                    tvTrait1Name.setText(trait);
                    tvTrait1Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T1NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T1DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 1:
                    tvTrait2Name.setText(trait);
                    tvTrait2Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T2NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T2DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 2:
                    tvTrait3Name.setText(trait);
                    tvTrait3Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T3NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T3DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 3:
                    tvTrait4Name.setText(trait);
                    tvTrait4Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T4NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T4DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 4:
                    tvTrait5Name.setText(trait);
                    tvTrait5Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T5NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T5DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                default:
                    break;
            }
        } else if (mode == 1) {
            switch (traitNumber) {
                case 1:
                    tvTrait1Name.setText(trait);
                    tvTrait1Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T1NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T1DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 2:
                    tvTrait2Name.setText(trait);
                    tvTrait2Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T2NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T2DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 3:
                    tvTrait3Name.setText(trait);
                    tvTrait3Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T3NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T3DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 4:
                    tvTrait4Name.setText(trait);
                    tvTrait4Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T4NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T4DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                case 5:
                    tvTrait5Name.setText(trait);
                    tvTrait5Name.setVisibility(View.VISIBLE);
                    traitCounter++;
                    mPage.getData().putString(TraitsPage.T5NAME_DATA_KEY,
                            name);
                    mPage.getData().putString(TraitsPage.T5DESC_DATA_KEY,
                            description);
                    mPage.notifyDataChanged();
                    break;
                default:
                    break;
            }
        }

        /*l2.setVisibility(View.VISIBLE);
        tvTrait1Description.setText(description);
        tvTrait1Description.setVisibility(View.VISIBLE);*/
    }
}
