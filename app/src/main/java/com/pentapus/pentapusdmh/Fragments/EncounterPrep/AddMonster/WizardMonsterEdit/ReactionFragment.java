package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;

/**
 * Created by Koni on 11.11.2016.
 */

public class ReactionFragment extends Fragment {
    private static final String ARG_KEY = "reactions";
    private int MSG_SHOW_DIALOG = 1000, MSG_FINISH_DIALOG = 1001;

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private ReactionPage mPage;
    private ListView reactionList;
    private FloatingActionButton fabReactions;
    private Spanned reaction;
    private ArrayList<Spanned> listItems = new ArrayList<Spanned>();
    private ArrayAdapter<Spanned> adapter;


    public static ReactionFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        ReactionFragment fragment = new ReactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ReactionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (ReactionPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_reactions, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        reactionList = (ListView) rootView.findViewById(R.id.list);

        for(int i=0; i<5; i++){
            if((mPage.getData().getString(getItemNameKey(i)) != null && !mPage.getData().getString(getItemNameKey(i)).isEmpty()) || mPage.getData().getString(getItemDescKey(i)) != null && !mPage.getData().getString(getItemDescKey(i)).isEmpty()){
                Spanned trait = Html.fromHtml("<b>" + mPage.getData().getString(getItemNameKey(i)) + ".</b>" + mPage.getData().getString(getItemDescKey(i)));
                listItems.add(trait);
            }
        }

        adapter = new ArrayAdapter<Spanned>(getContext(), android.R.layout.simple_list_item_1, listItems);
        reactionList.setAdapter(adapter);
        reactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onFabClick(1, mPage.getData().getString(getItemNameKey(i)), mPage.getData().getString(getItemDescKey(i)), i);
            }
        });


        fabReactions = (FloatingActionButton) rootView.findViewById(R.id.fabTraits);
        fabReactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick(0, null, null, listItems.size());
            }
        });
        if(listItems.size() >= 5){
            fabReactions.setVisibility(View.GONE);
        }else{
            fabReactions.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
    }

    public void onFabClick(int mode, String name, String description, int traitNumber) {
        DialogFragment newFragment = AddReactionDialogFragment.newInstance(mode, name, description, traitNumber, "Reaction");
        newFragment.setTargetFragment(this, MSG_FINISH_DIALOG);
        newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDREACTION_DIALOG");
    }

    public void onDialogResult(int requestCode, int mode, String name, String description, int traitNumber) {
        reaction = Html.fromHtml("<b>" + name + ". </b> " + description);
        switch (mode) {
            case 0:  //add
                if (!name.isEmpty() || !description.isEmpty()) {
                    listItems.add(reaction);
                    adapter.notifyDataSetChanged();
                    mPage.getData().putString(getItemNameKey(traitNumber),
                            name);
                    mPage.getData().putString(getItemDescKey(traitNumber),
                            description);
                    mPage.notifyDataChanged();
                }
                if(listItems.size() >= 5){
                    fabReactions.setVisibility(View.GONE);
                }
                break;
            case 1: //update
                if (!name.isEmpty() || !description.isEmpty()) {

                    listItems.set(traitNumber, reaction);
                    adapter.notifyDataSetChanged();
                    mPage.getData().putString(getItemNameKey(traitNumber),
                            name);
                    mPage.getData().putString(getItemDescKey(traitNumber),
                            description);
                    mPage.notifyDataChanged();
                }else{
                    listItems.remove(traitNumber);
                    adapter.notifyDataSetChanged();
                    mPage.getData().remove(getItemNameKey(traitNumber));
                    mPage.getData().remove(getItemDescKey(traitNumber));
                    mPage.notifyDataChanged();
                    if(listItems.size() < 5){
                        fabReactions.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 2:
                listItems.remove(traitNumber);
                adapter.notifyDataSetChanged();
                mPage.getData().remove(getItemNameKey(traitNumber));
                mPage.getData().remove(getItemDescKey(traitNumber));
                mPage.notifyDataChanged();
                if(listItems.size() < 5){
                    fabReactions.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    public String getItemNameKey(int position) {
        switch (position) {
            case 0:
                return ReactionPage.R1NAME_DATA_KEY;
            case 1:
                return ReactionPage.R2NAME_DATA_KEY;
            case 2:
                return ReactionPage.R3NAME_DATA_KEY;
            case 3:
                return ReactionPage.R4NAME_DATA_KEY;
            case 4:
                return ReactionPage.R5NAME_DATA_KEY;
            default:
                return null;
        }
    }

    public String getItemDescKey(int position) {
        switch (position) {
            case 0:
                return ReactionPage.R1DESC_DATA_KEY;
            case 1:
                return ReactionPage.R2DESC_DATA_KEY;
            case 2:
                return ReactionPage.R3DESC_DATA_KEY;
            case 3:
                return ReactionPage.R4DESC_DATA_KEY;
            case 4:
                return ReactionPage.R5DESC_DATA_KEY;
            default:
                return null;
        }
    }
}
