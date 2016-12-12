package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.Spells.SpellViewPagerDialogFragment;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Koni on 11.11.2016.
 */

public class TraitsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_KEY = "traits";
    private static final int MSG_FINISH_DIALOG = 1000, MSG_SPELLCASTING_DIALOG = 1001, MSG_CHOOSESPELLS_DIALOG = 1001, MSG_INNATESPELLCASTING_DIALOG = 1003;

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private TraitsPage mPage;
    private ListView traitsList;
    private FloatingActionButton fabTraits;
    private TextView labelSpellcasting, tvSpellcasting, tvSpellsKnown, labelInnateSpellcasting, tvInnateSpellcasting, tvInnateSpellsKnown;
    private Spanned trait;
    String scDescription, innateDescription;
    private ArrayList<Spanned> listItems = new ArrayList<Spanned>();
    private ArrayAdapter<Spanned> adapter;
    Fragment targetFragment;
    private List<Spell> spellList = new ArrayList<>();
    ArrayList<String> selectionList = new ArrayList<>();
    HashMap<Integer, Integer> innateSelectionList = new HashMap<>();
    private boolean isInnate = false;
    private int[] slots = new int[9];
    String monstername;


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
        targetFragment = this;
        monstername = ((MonsterEditWizardFragment) getParentFragment()).getName();
        mPage.getData().putString(TraitsPage.NAME_DATA_KEY, monstername);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_traits, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        labelSpellcasting = (TextView) rootView.findViewById(R.id.labelSpellcasting);
        tvSpellcasting = (TextView) rootView.findViewById(R.id.tvSpellcasting);
        tvSpellsKnown = (TextView) rootView.findViewById(R.id.tvSpellsKnown);
        tvSpellsKnown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpellList(1, MSG_CHOOSESPELLS_DIALOG, selectionList);
            }
        });

        if (mPage.getData().getString(TraitsPage.SCDESCRIPTION_DATA_KEY) != null && !mPage.getData().getString(TraitsPage.SCDESCRIPTION_DATA_KEY).isEmpty()) {
            scDescription = mPage.getData().getString(TraitsPage.SCDESCRIPTION_DATA_KEY);
            tvSpellcasting.setText(scDescription);
            tvSpellsKnown.setVisibility(View.VISIBLE);
        }
        if (mPage.getData().getStringArrayList(TraitsPage.SCSPELLSKNOWN_DATA_KEY) != null) {
            selectionList = mPage.getData().getStringArrayList(TraitsPage.SCSPELLSKNOWN_DATA_KEY);
            slots = mPage.getData().getIntArray(TraitsPage.SCSLOTS_DATA_KEY);
            tvSpellsKnown.setText(mPage.getData().getString(TraitsPage.SCSPELLSKNOWNSTRING_DATA_KEY));
            isInnate = false;
        }

        labelSpellcasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = SpellcastingDialog.newInstance(mPage.getData().getString(TraitsPage.SCABILITY_DATA_KEY), mPage.getData().getString(TraitsPage.SClEVEL_DATA_KEY), mPage.getData().getString(TraitsPage.SCMOD_DATA_KEY), mPage.getData().getString(TraitsPage.SCDC_DATA_KEY), mPage.getData().getString(TraitsPage.SCCLASS_DATA_KEY), mPage.getData().getIntArray(TraitsPage.SCSLOTS_DATA_KEY));
                newFragment.setTargetFragment(targetFragment, MSG_SPELLCASTING_DIALOG);
                newFragment.setTargetFragment(targetFragment, MSG_SPELLCASTING_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_SPELLCASTING_DIALOG");
            }
        });

        tvSpellcasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = SpellcastingDialog.newInstance(mPage.getData().getString(TraitsPage.SCABILITY_DATA_KEY), mPage.getData().getString(TraitsPage.SClEVEL_DATA_KEY), mPage.getData().getString(TraitsPage.SCMOD_DATA_KEY), mPage.getData().getString(TraitsPage.SCDC_DATA_KEY), mPage.getData().getString(TraitsPage.SCCLASS_DATA_KEY), mPage.getData().getIntArray(TraitsPage.SCSLOTS_DATA_KEY));
                newFragment.setTargetFragment(targetFragment, MSG_SPELLCASTING_DIALOG);
                newFragment.setTargetFragment(targetFragment, MSG_SPELLCASTING_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_SPELLCASTING_DIALOG");
            }
        });

        traitsList = (ListView) rootView.findViewById(R.id.list);

        for (int i = 0; i < 5; i++) {
            if ((mPage.getData().getString(getItemNameKey(i)) != null && !mPage.getData().getString(getItemNameKey(i)).isEmpty()) || mPage.getData().getString(getItemDescKey(i)) != null && !mPage.getData().getString(getItemDescKey(i)).isEmpty()) {
                Spanned trait = Html.fromHtml("<b>" + mPage.getData().getString(getItemNameKey(i)) + ".</b>" + mPage.getData().getString(getItemDescKey(i)));
                listItems.add(trait);
            }
        }

        adapter = new ArrayAdapter<Spanned>(getContext(), R.layout.custom_list_item, listItems);
        traitsList.setAdapter(adapter);
        traitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onFabClick(1, mPage.getData().getString(getItemNameKey(i)), mPage.getData().getString(getItemDescKey(i)), i);
            }
        });
        getListViewSize(traitsList);

        fabTraits = (FloatingActionButton) rootView.findViewById(R.id.fabTraits);
        fabTraits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick(0, null, null, listItems.size());
            }
        });
        if (listItems.size() >= 5) {
            fabTraits.setVisibility(View.GONE);
        } else {
            fabTraits.setVisibility(View.VISIBLE);
        }

        labelInnateSpellcasting = (TextView) rootView.findViewById(R.id.labelInnateSpellCasting);
        tvInnateSpellcasting = (TextView) rootView.findViewById(R.id.tvInnateSpellcasting);
        tvInnateSpellsKnown = (TextView) rootView.findViewById(R.id.tvInnateSpellsKnown);
        tvInnateSpellsKnown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInnateSpellList(2, MSG_INNATESPELLCASTING_DIALOG, innateSelectionList);
            }
        });

        if (mPage.getData().getString(TraitsPage.INNATEDESCRIPTION_DATA_KEY) != null && !mPage.getData().getString(TraitsPage.INNATEDESCRIPTION_DATA_KEY).isEmpty()) {
            innateDescription = mPage.getData().getString(TraitsPage.INNATEDESCRIPTION_DATA_KEY);
            tvInnateSpellcasting.setText(innateDescription);
            tvInnateSpellsKnown.setVisibility(View.VISIBLE);
        }
        if (mPage.getData().getSerializable(TraitsPage.INNATESPELLSKNOWN_DATA_KEY) != null) {
            innateSelectionList = (HashMap<Integer, Integer>) mPage.getData().getSerializable(TraitsPage.INNATESPELLSKNOWN_DATA_KEY);
            tvInnateSpellsKnown.setText(mPage.getData().getString(TraitsPage.INNATESPELLSKNOWNSTRING_DATA_KEY));
        }

        labelInnateSpellcasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = InnateSpellcastingDialog.newInstance(mPage.getData().getString(TraitsPage.INNATEABILITY_DATA_KEY), mPage.getData().getString(TraitsPage.INNATEMOD_DATA_KEY), mPage.getData().getString(TraitsPage.INNATEDC_DATA_KEY));
                newFragment.setTargetFragment(targetFragment, MSG_INNATESPELLCASTING_DIALOG);
                newFragment.setTargetFragment(targetFragment, MSG_INNATESPELLCASTING_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_INNATESPELLCASTING_DIALOG");
            }
        });

        tvInnateSpellcasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = InnateSpellcastingDialog.newInstance(mPage.getData().getString(TraitsPage.INNATEABILITY_DATA_KEY), mPage.getData().getString(TraitsPage.INNATEMOD_DATA_KEY), mPage.getData().getString(TraitsPage.INNATEDC_DATA_KEY));
                newFragment.setTargetFragment(targetFragment, MSG_INNATESPELLCASTING_DIALOG);
                newFragment.setTargetFragment(targetFragment, MSG_INNATESPELLCASTING_DIALOG);
                newFragment.show(getActivity().getSupportFragmentManager(), "F_INNATESPELLCASTING_DIALOG");
            }
        });

        return rootView;
    }

    public void showInnateSpellList(int mode, int message, HashMap<Integer, Integer> selectionList) {
        Fragment fragment = null;
        Class fragmentClass;
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentClass = SpellViewPagerDialogFragment.class;
        bundle.putBoolean("navMode", true);
        bundle.putInt("mode", mode);
        bundle.putSerializable("selectedspells", selectionList);
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setTargetFragment(targetFragment, message);
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
             /*   if (isNavMode()) {
                    fragmentManager.popBackStack();
                }*/
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ContainerFrame, fragment, "F_SPELL_PAGER")
                .addToBackStack("NAV_F")
                .commit();
    }

    public void showSpellList(int mode, int message, ArrayList<String> selectionList) {
        Fragment fragment = null;
        Class fragmentClass;
        Bundle bundle = new Bundle();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentClass = SpellViewPagerDialogFragment.class;
        bundle.putBoolean("navMode", true);
        bundle.putInt("mode", mode);
        if (mode == 1) {
            if (mPage.getData().getString(TraitsPage.SClEVEL_DATA_KEY) != null && !mPage.getData().getString(TraitsPage.SClEVEL_DATA_KEY).isEmpty()) {
                bundle.putString("level", mPage.getData().getString(TraitsPage.SClEVEL_DATA_KEY));
            }
            if (mPage.getData().getString(TraitsPage.SCCLASS_DATA_KEY) != null && !mPage.getData().getString(TraitsPage.SCCLASS_DATA_KEY).isEmpty()) {
                bundle.putString(TraitsPage.SCCLASS_DATA_KEY, mPage.getData().getString(TraitsPage.SCCLASS_DATA_KEY));
            }
        }
        bundle.putStringArrayList("selectionList", selectionList);
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setTargetFragment(targetFragment, message);
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.ContainerFrame, fragment, "F_SPELL_PAGER")
                .addToBackStack("NAV_F")
                .commit();
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
    }

    public void onFabClick(int mode, String name, String description, int traitNumber) {
        DialogFragment newFragment = AddTraitDialogFragment.newInstance(mode, name, description, traitNumber, "Trait");
        newFragment.setTargetFragment(this, MSG_FINISH_DIALOG);
        newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDTRAIT_DIALOG");
    }

    public void onDialogResult(int requestCode, int mode, Bundle results) {
        String name, description;
        int traitNumber;
        switch (mode) {
            case 0:  //add
                name = results.getString("monstername");
                description = results.getString("description");
                traitNumber = results.getInt("traitNumber");
                trait = Html.fromHtml("<b>" + name + ". </b> " + description);
                if (!name.isEmpty() || !description.isEmpty()) {
                    listItems.add(trait);
                    adapter.notifyDataSetChanged();
                    mPage.getData().putString(getItemNameKey(traitNumber),
                            name);
                    mPage.getData().putString(getItemDescKey(traitNumber),
                            description);
                    mPage.notifyDataChanged();
                }
                if (listItems.size() >= 5) {
                    fabTraits.setVisibility(View.GONE);
                }
                getListViewSize(traitsList);
                break;
            case 1: //update
                name = results.getString("monstername");
                description = results.getString("description");
                traitNumber = results.getInt("traitNumber");
                trait = Html.fromHtml("<b>" + name + ". </b> " + description);
                if (!name.isEmpty() || !description.isEmpty()) {

                    listItems.set(traitNumber, trait);
                    adapter.notifyDataSetChanged();
                    mPage.getData().putString(getItemNameKey(traitNumber),
                            name);
                    mPage.getData().putString(getItemDescKey(traitNumber),
                            description);
                    mPage.notifyDataChanged();
                } else {
                    traitNumber = results.getInt("traitNumber");
                    listItems.remove(traitNumber);
                    adapter.notifyDataSetChanged();
                    mPage.getData().remove(getItemNameKey(traitNumber));
                    mPage.getData().remove(getItemDescKey(traitNumber));
                    mPage.notifyDataChanged();
                    if (listItems.size() < 5) {
                        fabTraits.setVisibility(View.VISIBLE);
                    }
                }
                getListViewSize(traitsList);
                break;
            case 2: // remove
                traitNumber = results.getInt("traitNumber");
                listItems.remove(traitNumber);
                adapter.notifyDataSetChanged();
                mPage.getData().remove(getItemNameKey(traitNumber));
                mPage.getData().remove(getItemDescKey(traitNumber));
                mPage.notifyDataChanged();
                if (listItems.size() < 5) {
                    fabTraits.setVisibility(View.VISIBLE);
                }
                getListViewSize(traitsList);
                break;
            case -1: // spellcasting info
                String scdescription = "";
                switch (requestCode) {
                    case MSG_SPELLCASTING_DIALOG:
                        mPage.getData().putBoolean(TraitsPage.SC_DATA_KEY, true);
                        //mPage.getData().putString(TraitsPage.SCDESCRIPTION_DATA_KEY, results.getString(TraitsPage.SCDESCRIPTION_DATA_KEY));
                        mPage.getData().putString(TraitsPage.SClEVEL_DATA_KEY, results.getString(TraitsPage.SClEVEL_DATA_KEY));
                        mPage.getData().putString(TraitsPage.SCABILITY_DATA_KEY, results.getString(TraitsPage.SCABILITY_DATA_KEY));
                        mPage.getData().putString(TraitsPage.SCDC_DATA_KEY, results.getString(TraitsPage.SCDC_DATA_KEY));
                        mPage.getData().putString(TraitsPage.SCMOD_DATA_KEY, results.getString(TraitsPage.SCMOD_DATA_KEY));
                        mPage.getData().putString(TraitsPage.SCCLASS_DATA_KEY, results.getString(TraitsPage.SCCLASS_DATA_KEY));
                        mPage.getData().putIntArray(TraitsPage.SCSLOTS_DATA_KEY, results.getIntArray(TraitsPage.SCSLOTS_DATA_KEY));
                        scdescription = "The " + mPage.getData().getString(TraitsPage.NAME_DATA_KEY) + " is a ";

                        if (results.getString(TraitsPage.SClEVEL_DATA_KEY).equals("1")) {
                            scdescription = scdescription + results.getString(TraitsPage.SClEVEL_DATA_KEY) + "st";
                        } else if (results.getString(TraitsPage.SClEVEL_DATA_KEY).equals("2")) {
                            scdescription = scdescription + results.getString(TraitsPage.SClEVEL_DATA_KEY) + "nd";
                        } else if (results.getString(TraitsPage.SClEVEL_DATA_KEY).equals("3")) {
                            scdescription = scdescription + results.getString(TraitsPage.SClEVEL_DATA_KEY) + "rd";
                        } else {
                            scdescription = scdescription + results.getString(TraitsPage.SClEVEL_DATA_KEY) + "th";
                        }
                        scdescription = scdescription + " level spellcaster. Its spellcasting ability is " + results.getString(TraitsPage.SCABILITY_DATA_KEY);
                        if (results.getString(TraitsPage.SCDC_DATA_KEY) != null && !results.getString(TraitsPage.SCDC_DATA_KEY).isEmpty()) {
                            scdescription = scdescription + " (spell save DC " + results.getString(TraitsPage.SCDC_DATA_KEY);
                            if (results.getString(TraitsPage.SCMOD_DATA_KEY) != null && !results.getString(TraitsPage.SCMOD_DATA_KEY).isEmpty()) {
                                if (Integer.valueOf(results.getString(TraitsPage.SCMOD_DATA_KEY)) >= 0) {
                                    scdescription = scdescription + ", +" + results.getString(TraitsPage.SCMOD_DATA_KEY) + " to hit with spell attacks).";
                                } else {
                                    scdescription = scdescription + ", " + results.getString(TraitsPage.SCMOD_DATA_KEY) + " to hit with spell attacks).";
                                }
                            } else {
                                scdescription = scdescription + ").";
                            }
                        } else {
                            if (results.getString(TraitsPage.SCMOD_DATA_KEY) != null && !results.getString(TraitsPage.SCMOD_DATA_KEY).isEmpty()) {
                                if (Integer.valueOf(results.getString(TraitsPage.SCMOD_DATA_KEY)) >= 0) {
                                    scdescription = scdescription + " (+" + results.getString(TraitsPage.SCMOD_DATA_KEY) + " to hit with spell attacks).";
                                } else {
                                    scdescription = scdescription + "(" + results.getString(TraitsPage.SCMOD_DATA_KEY) + " to hit with spell attacks).";
                                }
                            } else {
                                scdescription = scdescription + ".";
                            }
                        }
                        if (results.getString(TraitsPage.SCCLASS_DATA_KEY).toLowerCase().equals("all")) {
                            scdescription = scdescription + " It has the following spells prepared:";
                        } else {
                            scdescription = scdescription + " It has the following " + results.getString(TraitsPage.SCCLASS_DATA_KEY) + " spells prepared:";
                        }
                        mPage.getData().putString(TraitsPage.SCDESCRIPTION_DATA_KEY, scdescription);
                        tvSpellcasting.setText(scdescription);
                        tvSpellsKnown.setVisibility(View.VISIBLE);
                        break;
                    case MSG_INNATESPELLCASTING_DIALOG:
                        mPage.getData().putBoolean(TraitsPage.INNATE_DATA_KEY, true);
                        mPage.getData().putString(TraitsPage.INNATEABILITY_DATA_KEY, results.getString(TraitsPage.INNATEABILITY_DATA_KEY));
                        mPage.getData().putString(TraitsPage.INNATEMOD_DATA_KEY, results.getString(TraitsPage.INNATEMOD_DATA_KEY));
                        mPage.getData().putString(TraitsPage.INNATEDC_DATA_KEY, results.getString(TraitsPage.INNATEDC_DATA_KEY));
                        mPage.getData().putString(TraitsPage.INNATEABILITY_DATA_KEY, results.getString(TraitsPage.INNATEABILITY_DATA_KEY));
                        scdescription = "The " + mPage.getData().getString(TraitsPage.NAME_DATA_KEY) + "'s innate spellcasting ability is " + results.getString(TraitsPage.INNATEABILITY_DATA_KEY);

                        if (results.getString(TraitsPage.INNATEDC_DATA_KEY) != null && !results.getString(TraitsPage.INNATEDC_DATA_KEY).isEmpty()) {
                            scdescription = scdescription + " (spell save DC " + results.getString(TraitsPage.INNATEDC_DATA_KEY);
                            if (results.getString(TraitsPage.INNATEMOD_DATA_KEY) != null && !results.getString(TraitsPage.INNATEMOD_DATA_KEY).isEmpty()) {
                                if (Integer.valueOf(results.getString(TraitsPage.INNATEMOD_DATA_KEY)) >= 0) {
                                    scdescription = scdescription + ", +" + results.getString(TraitsPage.INNATEMOD_DATA_KEY) + " to hit with spell attacks).";
                                } else {
                                    scdescription = scdescription + ", " + results.getString(TraitsPage.INNATEMOD_DATA_KEY) + " to hit with spell attacks).";
                                }
                            } else {
                                scdescription = scdescription + ").";
                            }
                        } else {
                            if (results.getString(TraitsPage.INNATEMOD_DATA_KEY) != null && !results.getString(TraitsPage.INNATEMOD_DATA_KEY).isEmpty()) {
                                if (Integer.valueOf(results.getString(TraitsPage.INNATEMOD_DATA_KEY)) >= 0) {
                                    scdescription = scdescription + " (+" + results.getString(TraitsPage.INNATEMOD_DATA_KEY) + " to hit with spell attacks).";
                                } else {
                                    scdescription = scdescription + "(" + results.getString(TraitsPage.INNATEMOD_DATA_KEY) + " to hit with spell attacks).";
                                }
                            } else {
                                scdescription = scdescription + ".";
                            }
                        }
                        scdescription = scdescription + " It can innately cast the following spells, requiring no material components:";
                        mPage.getData().putString(TraitsPage.INNATEDESCRIPTION_DATA_KEY, scdescription);
                        tvInnateSpellcasting.setText(scdescription);
                        tvInnateSpellsKnown.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }


                break;
            case -2: //spells chosen
                switch (requestCode) {
                    case MSG_SPELLCASTING_DIALOG:
                        isInnate = false;
                        if (getLoaderManager().getLoader(0) == null) {
                            getLoaderManager().initLoader(0, results, this);
                        } else {
                            getLoaderManager().restartLoader(0, results, this);
                        }
                        break;
                    case MSG_INNATESPELLCASTING_DIALOG:
                        isInnate = true;
                        if (getLoaderManager().getLoader(0) == null) {
                            getLoaderManager().initLoader(0, results, this);
                        } else {
                            getLoaderManager().restartLoader(0, results, this);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case -3:
                switch (requestCode) {
                    case MSG_SPELLCASTING_DIALOG:
                        mPage.getData().putBoolean(TraitsPage.SC_DATA_KEY, false);
                        mPage.getData().remove(TraitsPage.SClEVEL_DATA_KEY);
                        mPage.getData().remove(TraitsPage.SCABILITY_DATA_KEY);
                        mPage.getData().remove(TraitsPage.SCDC_DATA_KEY);
                        mPage.getData().remove(TraitsPage.SCMOD_DATA_KEY);
                        mPage.getData().remove(TraitsPage.SCCLASS_DATA_KEY);
                        mPage.getData().remove(TraitsPage.SCSLOTS_DATA_KEY);
                        mPage.getData().remove(TraitsPage.SCDESCRIPTION_DATA_KEY);
                        tvSpellcasting.setText("");
                        tvSpellsKnown.setVisibility(View.GONE);
                        break;
                    case MSG_INNATESPELLCASTING_DIALOG:
                        mPage.getData().putBoolean(TraitsPage.INNATE_DATA_KEY, false);
                        mPage.getData().remove(TraitsPage.INNATEABILITY_DATA_KEY);
                        mPage.getData().remove(TraitsPage.INNATEMOD_DATA_KEY);
                        mPage.getData().remove(TraitsPage.INNATEDC_DATA_KEY);
                        mPage.getData().remove(TraitsPage.INNATEABILITY_DATA_KEY);
                        mPage.getData().remove(TraitsPage.INNATEDESCRIPTION_DATA_KEY);
                        tvInnateSpellcasting.setText("");
                        tvInnateSpellsKnown.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    public String getItemNameKey(int position) {
        switch (position) {
            case 0:
                return TraitsPage.T1NAME_DATA_KEY;
            case 1:
                return TraitsPage.T2NAME_DATA_KEY;
            case 2:
                return TraitsPage.T3NAME_DATA_KEY;
            case 3:
                return TraitsPage.T4NAME_DATA_KEY;
            case 4:
                return TraitsPage.T5NAME_DATA_KEY;
            default:
                return null;
        }
    }

    public String getItemDescKey(int position) {
        switch (position) {
            case 0:
                return TraitsPage.T1DESC_DATA_KEY;
            case 1:
                return TraitsPage.T2DESC_DATA_KEY;
            case 2:
                return TraitsPage.T3DESC_DATA_KEY;
            case 3:
                return TraitsPage.T4DESC_DATA_KEY;
            case 4:
                return TraitsPage.T5DESC_DATA_KEY;
            default:
                return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            getLoaderManager().restartLoader(0, null, this);
        }*/
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //ArrayList<String> selectedSpells = results.getStringArrayList("selectedspells");
        //Object[] tempArray = selectedSpells.toArray();
        //String[] selectionArgs = Arrays.copyOf(tempArray, tempArray.length, String[].class);
        String selection = "";
        ArrayList<String> tempList = new ArrayList<>();
        HashMap<Integer, Integer> tempMap = new HashMap<>();
        String[] selectionArgs = new String[1];
        String orderBy = "";
        if (args != null) {
            if (isInnate) {
                innateSelectionList = (HashMap<Integer, Integer>) args.getSerializable("spellcounter");
                tempMap = innateSelectionList;
                if (tempMap != null && tempMap.size() > 0) {
                    Object[] tempArray = tempMap.keySet().toArray();
                    selectionArgs = new String[tempArray.length];
                    for (int j = 0; j < tempArray.length; j++) {
                        selectionArgs[j] = String.valueOf(tempArray[j]);
                    }
                    for (int i = 0; i < selectionArgs.length; i++) {
                        if (!selection.isEmpty()) {
                            selection = selection + " OR ";
                            selection = selection + DataBaseHandler.KEY_ROWID + " = ?";
                        } else {
                            selection = "(" + DataBaseHandler.KEY_ROWID + " = ?";
                        }
                    }
                    if (!selection.isEmpty()) {
                        selection = selection + ")";
                    }
                } else {
                    //selectionArgs = new String[]{"%" + "PHB" + "%", "%" + "EE" + "%", "%" + "PHB" + "%"};
                    selectionArgs = new String[1];
                    selectionArgs[0] = "";
                    selection = "(" + DataBaseHandler.KEY_ROWID + " = ?)";
                }
            } else {
                selectionList = args.getStringArrayList("selectedspells");
                tempList = selectionList;
                if (tempList.size() > 0) {
                    selectionArgs = new String[tempList.size()];
                    tempList.toArray(selectionArgs);

                    for (int i = 0; i < selectionArgs.length; i++) {
                        if (!selection.isEmpty()) {
                            selection = selection + " OR ";
                            selection = selection + DataBaseHandler.KEY_ROWID + " = ?";
                        } else {
                            selection = "(" + DataBaseHandler.KEY_ROWID + " = ?";
                        }
                    }
                    if (!selection.isEmpty()) {
                        selection = selection + ")";
                    }
                } else {
                    //selectionArgs = new String[]{"%" + "PHB" + "%", "%" + "EE" + "%", "%" + "PHB" + "%"};
                    selectionArgs = new String[1];
                    selectionArgs[0] = "";
                    selection = "(" + DataBaseHandler.KEY_ROWID + " = ?)";
                }
                orderBy = DataBaseHandler.KEY_LEVEL + " ASC";
            }
        }
        //List<String> selectionList = new ArrayList<>();

        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_SPELL, DataBaseHandler.PROJECTION_SPELL, selection, selectionArgs, orderBy);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        updateSpells(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void updateSpells(Cursor data) {
        spellList.clear();
        while (data.moveToNext()) {
            spellList.add(new Spell(data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID)), data.getString(data.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)), data.getInt(data.getColumnIndexOrThrow(DataBaseHandler.KEY_LEVEL))));
        }
        if (isInnate) {
            setInnateSpellText();
        } else {
            setSpellText();
        }
        data.close();
    }


    private class Spell {
        private int id, level;
        private String name;

        Spell(int id, String name, int level) {
            this.id = id;
            this.name = name;
            this.level = level;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }
    }


    private void setInnateSpellText() {
        Spanned spellText = null;
        ArrayList<String> atwill = new ArrayList<>();
        ArrayList<String> once = new ArrayList<>();
        ArrayList<String> twice = new ArrayList<>();
        ArrayList<String> three = new ArrayList<>();
        int a = 0, b = 0, c = 0, d = 0;
        boolean bonce = false, btwice = false, bthree = false, batwill = false;
        for (int i = 0; i < spellList.size(); i++) {
            if (innateSelectionList.get(spellList.get(i).getId()) == 1) {
                once.add(a, spellList.get(i).getName());
                a++;
            }
            if (innateSelectionList.get(spellList.get(i).getId()) == 2) {
                twice.add(b, spellList.get(i).getName());
                b++;
            }
            if (innateSelectionList.get(spellList.get(i).getId()) == 3) {
                three.add(c, spellList.get(i).getName());
                c++;
            }
            if (innateSelectionList.get(spellList.get(i).getId()) >= 4) {
                atwill.add(d, spellList.get(i).getName());
                d++;
            }
        }
        for (int i = 0; i < atwill.size(); i++) {
            if (!batwill) {
                spellText = Html.fromHtml("At will: <i>" + atwill.get(i) + "</i>");
                batwill = true;
            } else {
                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + atwill.get(i) + "</i>"));
            }
        }

        for (int i = 0; i < three.size(); i++) {
            if (spellText != null && !spellText.toString().isEmpty()) {
                if (!bthree) {
                    if (three.size() > 1) {
                        spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>3/day each: <i>" + three.get(i) + "</i>"));
                    } else {
                        spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>3/day: <i>" + three.get(i) + "</i>"));
                    }
                    bthree = true;
                } else {
                    spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + three.get(i) + "</i>"));
                }
            } else {
                if (!bthree) {
                    if (three.size() > 1) {
                        spellText = Html.fromHtml("3/day each: <i>" + three.get(i) + "</i>");
                    } else {
                        spellText = Html.fromHtml("3/day: <i>" + three.get(i) + "</i>");
                    }
                    bthree = true;
                } else {
                    spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + three.get(i) + "</i>"));
                }
            }
        }

        for (int i = 0; i < twice.size(); i++) {
            if (spellText != null && !spellText.toString().isEmpty()) {
                if (!btwice) {
                    if (twice.size() > 1) {
                        spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>2/day each: <i>" + twice.get(i) + "</i>"));
                    } else {
                        spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>2/day: <i>" + twice.get(i) + "</i>"));
                    }
                    btwice = true;
                } else {
                    spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + twice.get(i) + "</i>"));
                }
            } else {
                if (!btwice) {
                    if (twice.size() > 1) {
                        spellText = Html.fromHtml("2/day each: <i>" + twice.get(i) + "</i>");
                    } else {
                        spellText = Html.fromHtml("2/day: <i>" + twice.get(i) + "</i>");
                    }
                    btwice = true;
                } else {
                    spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + twice.get(i) + "</i>"));
                }
            }
        }

        for (int i = 0; i < once.size(); i++) {
            if (spellText != null && !spellText.toString().isEmpty()) {
                if (!bonce) {
                    if (once.size() > 1) {
                        spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>1/day each: <i>" + once.get(i) + "</i>"));
                    } else {
                        spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>1/day: <i>" + once.get(i) + "</i>"));
                    }
                    bonce = true;
                } else {
                    spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + once.get(i) + "</i>"));
                }
            } else {
                if (!bonce) {
                    if (once.size() > 1) {
                        spellText = Html.fromHtml("1/day each: <i>" + once.get(i) + "</i>");
                    } else {
                        spellText = Html.fromHtml("1/day: <i>" + once.get(i) + "</i>");
                    }
                    bonce = true;
                } else {
                    spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + once.get(i) + "</i>"));
                }
            }
        }


        if (spellText != null) {
            mPage.getData().putString(TraitsPage.INNATESPELLSKNOWNSTRING_DATA_KEY, spellText.toString());
            mPage.getData().putSerializable(TraitsPage.INNATESPELLSKNOWN_DATA_KEY, innateSelectionList);
        }
        tvInnateSpellsKnown.setText(spellText);
    }


    private void setSpellText() {
        Spanned spellText = null;
        boolean cantrip = false, first = false, second = false, third = false, fourth = false, fifth = false, sixth = false, seventh = false, eighth = false, ninth = false;
        slots = mPage.getData().getIntArray(TraitsPage.SCSLOTS_DATA_KEY);
        for (int i = 0; i < spellList.size(); i++) {
            switch (spellList.get(i).getLevel()) {
                case 0:
                    if (!cantrip) {
                        spellText = Html.fromHtml("Cantrips (at will): <i>" + spellList.get(i).getName() + "</i>");
                        cantrip = true;
                    } else {
                        spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                    }
                    break;
                case 1:
                    if (spellText == null) {
                        if (slots[0] != 1) {
                            spellText = Html.fromHtml("1st level (" + slots[0] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("1st level (" + slots[0] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!first) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[0] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("1st level (" + slots[0] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("1st level (" + slots[0] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            first = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 2:
                    if (spellText == null) {
                        if (slots[1] != 1) {
                            spellText = Html.fromHtml("2nd level (" + slots[1] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("2nd level (" + slots[1] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!second) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[1] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("2nd level (" + slots[1] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("2nd level (" + slots[1] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            second = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 3:
                    if (spellText == null) {
                        if (slots[2] != 1) {
                            spellText = Html.fromHtml("3rd level (" + slots[2] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("3rd level (" + slots[2] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!third) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[2] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("3rd level (" + slots[2] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("3rd level (" + slots[2] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            third = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 4:
                    if (spellText == null) {
                        if (slots[3] != 1) {
                            spellText = Html.fromHtml("4th level (" + slots[3] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("4th level (" + slots[3] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!fourth) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[3] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("4th level (" + slots[3] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("4th level (" + slots[3] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            fourth = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 5:
                    if (spellText == null) {
                        if (slots[4] != 1) {
                            spellText = Html.fromHtml("5th level (" + slots[4] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("5th level (" + slots[4] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!fifth) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[4] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("5th level (" + slots[4] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("5th level (" + slots[4] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));

                            }
                            fifth = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 6:
                    if (spellText == null) {
                        if (slots[5] != 1) {
                            spellText = Html.fromHtml("6th level (" + slots[5] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("6th level (" + slots[5] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!sixth) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[5] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("6th level (" + slots[5] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("6th level (" + slots[5] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            sixth = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 7:
                    if (spellText == null) {
                        if (slots[6] != 1) {
                            spellText = Html.fromHtml("7th level (" + slots[6] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("7th level (" + slots[6] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!seventh) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[6] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("7th level (" + slots[6] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("7th level (" + slots[6] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            seventh = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 8:
                    if (spellText == null) {
                        if (slots[7] != 1) {
                            spellText = Html.fromHtml("8th level (" + slots[7] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("8th level (" + slots[7] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!eighth) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[7] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("8th level (" + slots[7] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("8th level (" + slots[7] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            eighth = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                case 9:
                    if (spellText == null) {
                        if (slots[8] != 1) {
                            spellText = Html.fromHtml("9th level (" + slots[8] + " Slots): <i>" + spellList.get(i).getName() + "</i>");
                        } else {
                            spellText = Html.fromHtml("9th level (" + slots[8] + " Slot): <i>" + spellList.get(i).getName() + "</i>");
                        }
                    } else {
                        if (!ninth) {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<br>"));
                            if (slots[8] != 1) {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("9th level (" + slots[8] + " Slots): <i>" + spellList.get(i).getName() + "</i>"));
                            } else {
                                spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("9th level (" + slots[8] + " Slot): <i>" + spellList.get(i).getName() + "</i>"));
                            }
                            fourth = true;
                        } else {
                            spellText = (Spanned) TextUtils.concat(spellText, Html.fromHtml("<i>, " + spellList.get(i).getName() + "</i>"));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        if (spellText != null) {
            mPage.getData().putString(TraitsPage.SCSPELLSKNOWNSTRING_DATA_KEY, spellText.toString());
            mPage.getData().putStringArrayList(TraitsPage.SCSPELLSKNOWN_DATA_KEY, selectionList);
        }
        tvSpellsKnown.setText(spellText);
    }

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
    }

}
