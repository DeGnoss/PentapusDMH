package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;

/**
 * Created by Koni on 11.11.2016.
 */

public class ActionFragment extends Fragment {
    private static final String ARG_KEY = "actions";
    private int MSG_FINISH_DIALOG = 1001;


    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private ActionPage mPage;
    private ListView actionList;
    private FloatingActionButton fabActions;
    private Spanned action;
    private ArrayList<Spanned> listItems = new ArrayList<Spanned>();
    private ArrayAdapter<Spanned> adapter;
    private EditText tvMultiAttack;

    public static ActionFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        ActionFragment fragment = new ActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ActionFragment() {
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

        tvMultiAttack = (EditText) rootView.findViewById(R.id.tvMultiattack);
        if(mPage.getData().getString(ActionPage.MULTIATTACK_DATA_KEY) != null && !mPage.getData().getString(ActionPage.MULTIATTACK_DATA_KEY).isEmpty()){
            tvMultiAttack.setText(mPage.getData().getString(ActionPage.MULTIATTACK_DATA_KEY));
        }

        actionList = (ListView) rootView.findViewById(R.id.list);

        for (int i = 0; i < 5; i++) {
            ActionDummy actionDummy = getActionDummy(i);
            if((actionDummy.getName() != null && !actionDummy.getName().isEmpty()) || (actionDummy.getDesc() != null && !actionDummy.getDesc().isEmpty())){
                action = Html.fromHtml("<b>" + actionDummy.getName() + ". </b> " + actionDummy.getDesc());
                listItems.add(action);
            }
        }

        adapter = new ArrayAdapter<Spanned>(getContext(), android.R.layout.simple_list_item_1, listItems);
        actionList.setAdapter(adapter);
        actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActionDummy action = getActionDummy(i);
                onFabClick(1, action.getName(), action.getDesc(), action.getAtkmod(), action.getDmg1(), action.getDmg1Type(), action.getDmg2(), action.getDmg2Type(), action.isAutoroll(), action.isAdditional(), i);
            }
        });

        Spanned trait = Html.fromHtml("<b>" + mPage.getData().getString(TraitsPage.T1NAME_DATA_KEY) + ".</b>" + mPage.getData().getString(TraitsPage.T1DESC_DATA_KEY));

        fabActions = (FloatingActionButton) rootView.findViewById(R.id.fabActions);
        fabActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick(0, null, null, null, null, null, null, null, false, false, listItems.size());
            }
        });
        if (listItems.size() >= 5) {
            fabActions.setVisibility(View.GONE);
        } else {
            fabActions.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public void onFabClick(int mode, String name, String description, String atkmod, String dmg1, String dmg1type, String dmg2, String dmg2type, boolean autoroll, boolean additional, int traitNumber) {
        DialogFragment newFragment = AddActionDialogFragment.newInstance(mode, name, description, atkmod, dmg1, dmg1type, dmg2, dmg2type, autoroll, additional, traitNumber);
        newFragment.setTargetFragment(this, MSG_FINISH_DIALOG);
        newFragment.show(getActivity().getSupportFragmentManager(), "F_ADDACTION_DIALOG");
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

        tvMultiAttack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(ActionPage.MULTIATTACK_DATA_KEY,
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


    public void onDialogResult(int requestCode, int mode, String name, String description, String atkmod, String dmg1, String dmg1type, String dmg2, String dmg2type, boolean autoroll, boolean additional, int actionNumber) {
        action = Html.fromHtml("<b>" + name + ". </b> " + description);
        switch (mode) {
            case 0:  //add
                if (!name.isEmpty() || !description.isEmpty()) {
                    listItems.add(action);
                    adapter.notifyDataSetChanged();
                    ActionDummy actionDummy = new ActionDummy(name, description, atkmod, dmg1, dmg1type, dmg2, dmg2type, autoroll, additional);
                    saveAction(actionDummy, actionNumber);
                }
                if(listItems.size() >= 5){
                    fabActions.setVisibility(View.GONE);
                }
                break;
            case 1: //update
                if (!name.isEmpty() || !description.isEmpty()) {
                    listItems.set(actionNumber, action);
                    adapter.notifyDataSetChanged();
                    ActionDummy actionDummy = new ActionDummy(name, description, atkmod, dmg1, dmg1type, dmg2, dmg2type, autoroll, additional);
                    saveAction(actionDummy, actionNumber);
                }else{
                    listItems.remove(actionNumber);
                    adapter.notifyDataSetChanged();
                    removeAction(actionNumber);
                    if(listItems.size() < 5){
                        fabActions.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case 2:
                listItems.remove(actionNumber);
                adapter.notifyDataSetChanged();
                removeAction(actionNumber);
                if(listItems.size() < 5){
                    fabActions.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }




    private ActionDummy getActionDummy(int position) {
        switch (position) {
            case 0:
                return new ActionDummy(mPage.getData().getString(ActionPage.A1NAME_DATA_KEY), mPage.getData().getString(ActionPage.A1DESC_DATA_KEY), mPage.getData().getString(ActionPage.A1MOD_DATA_KEY), mPage.getData().getString(ActionPage.A1ROLL1_DATA_KEY), mPage.getData().getString(ActionPage.A1TYPE1_DATA_KEY), mPage.getData().getString(ActionPage.A1ROLL2_DATA_KEY), mPage.getData().getString(ActionPage.A1TYPE2_DATA_KEY), mPage.getData().getBoolean(ActionPage.A1AUTO_DATA_KEY), mPage.getData().getBoolean(ActionPage.A1ADD_DATA_KEY));
            case 1:
                return new ActionDummy(mPage.getData().getString(ActionPage.A2NAME_DATA_KEY), mPage.getData().getString(ActionPage.A2DESC_DATA_KEY), mPage.getData().getString(ActionPage.A2MOD_DATA_KEY), mPage.getData().getString(ActionPage.A2ROLL1_DATA_KEY), mPage.getData().getString(ActionPage.A2TYPE1_DATA_KEY), mPage.getData().getString(ActionPage.A2ROLL2_DATA_KEY), mPage.getData().getString(ActionPage.A2TYPE2_DATA_KEY), mPage.getData().getBoolean(ActionPage.A2AUTO_DATA_KEY), mPage.getData().getBoolean(ActionPage.A2ADD_DATA_KEY));
            case 2:
                return new ActionDummy(mPage.getData().getString(ActionPage.A3NAME_DATA_KEY), mPage.getData().getString(ActionPage.A3DESC_DATA_KEY), mPage.getData().getString(ActionPage.A3MOD_DATA_KEY), mPage.getData().getString(ActionPage.A3ROLL1_DATA_KEY), mPage.getData().getString(ActionPage.A3TYPE1_DATA_KEY), mPage.getData().getString(ActionPage.A3ROLL2_DATA_KEY), mPage.getData().getString(ActionPage.A3TYPE2_DATA_KEY), mPage.getData().getBoolean(ActionPage.A3AUTO_DATA_KEY), mPage.getData().getBoolean(ActionPage.A3ADD_DATA_KEY));
            case 3:
                return new ActionDummy(mPage.getData().getString(ActionPage.A4NAME_DATA_KEY), mPage.getData().getString(ActionPage.A4DESC_DATA_KEY), mPage.getData().getString(ActionPage.A4MOD_DATA_KEY), mPage.getData().getString(ActionPage.A4ROLL1_DATA_KEY), mPage.getData().getString(ActionPage.A4TYPE1_DATA_KEY), mPage.getData().getString(ActionPage.A4ROLL2_DATA_KEY), mPage.getData().getString(ActionPage.A4TYPE2_DATA_KEY), mPage.getData().getBoolean(ActionPage.A4AUTO_DATA_KEY), mPage.getData().getBoolean(ActionPage.A4ADD_DATA_KEY));
            case 4:
                return new ActionDummy(mPage.getData().getString(ActionPage.A5NAME_DATA_KEY), mPage.getData().getString(ActionPage.A5DESC_DATA_KEY), mPage.getData().getString(ActionPage.A5MOD_DATA_KEY), mPage.getData().getString(ActionPage.A5ROLL1_DATA_KEY), mPage.getData().getString(ActionPage.A5TYPE1_DATA_KEY), mPage.getData().getString(ActionPage.A5ROLL2_DATA_KEY), mPage.getData().getString(ActionPage.A5TYPE2_DATA_KEY), mPage.getData().getBoolean(ActionPage.A5AUTO_DATA_KEY), mPage.getData().getBoolean(ActionPage.A5ADD_DATA_KEY));
            default:
                return null;
        }
    }

    private void saveAction(ActionDummy action, int position) {
        switch (position) {
            case 0:
                mPage.getData().putString(ActionPage.A1NAME_DATA_KEY, action.getName());
                mPage.getData().putString(ActionPage.A1DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(ActionPage.A1MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(ActionPage.A1ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(ActionPage.A1TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(ActionPage.A1ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(ActionPage.A1TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(ActionPage.A1AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(ActionPage.A1ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 1:
                mPage.getData().putString(ActionPage.A2NAME_DATA_KEY, action.getName());
                mPage.getData().putString(ActionPage.A2DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(ActionPage.A2MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(ActionPage.A2ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(ActionPage.A2TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(ActionPage.A2ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(ActionPage.A2TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(ActionPage.A2AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(ActionPage.A2ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 2:
                mPage.getData().putString(ActionPage.A3NAME_DATA_KEY, action.getName());
                mPage.getData().putString(ActionPage.A3DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(ActionPage.A3MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(ActionPage.A3ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(ActionPage.A3TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(ActionPage.A3ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(ActionPage.A3TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(ActionPage.A3AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(ActionPage.A3ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 3:
                mPage.getData().putString(ActionPage.A4NAME_DATA_KEY, action.getName());
                mPage.getData().putString(ActionPage.A4DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(ActionPage.A4MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(ActionPage.A4ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(ActionPage.A4TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(ActionPage.A4ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(ActionPage.A4TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(ActionPage.A4AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(ActionPage.A4ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 4:
                mPage.getData().putString(ActionPage.A5NAME_DATA_KEY, action.getName());
                mPage.getData().putString(ActionPage.A5DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(ActionPage.A5MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(ActionPage.A5ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(ActionPage.A5TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(ActionPage.A5ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(ActionPage.A5TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(ActionPage.A5AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(ActionPage.A5ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }


    private void removeAction(int position){
        switch(position){
            case 0:
                mPage.getData().remove(ActionPage.A1NAME_DATA_KEY);
                mPage.getData().remove(ActionPage.A1DESC_DATA_KEY);
                mPage.getData().remove(ActionPage.A1MOD_DATA_KEY);
                mPage.getData().remove(ActionPage.A1ROLL1_DATA_KEY);
                mPage.getData().remove(ActionPage.A1TYPE1_DATA_KEY);
                mPage.getData().remove(ActionPage.A1ROLL2_DATA_KEY);
                mPage.getData().remove(ActionPage.A1TYPE2_DATA_KEY);
                mPage.getData().remove(ActionPage.A1AUTO_DATA_KEY);
                mPage.getData().remove(ActionPage.A1ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 1:
                mPage.getData().remove(ActionPage.A2NAME_DATA_KEY);
                mPage.getData().remove(ActionPage.A2DESC_DATA_KEY);
                mPage.getData().remove(ActionPage.A2MOD_DATA_KEY);
                mPage.getData().remove(ActionPage.A2ROLL1_DATA_KEY);
                mPage.getData().remove(ActionPage.A2TYPE1_DATA_KEY);
                mPage.getData().remove(ActionPage.A2ROLL2_DATA_KEY);
                mPage.getData().remove(ActionPage.A2TYPE2_DATA_KEY);
                mPage.getData().remove(ActionPage.A2AUTO_DATA_KEY);
                mPage.getData().remove(ActionPage.A2ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 2:
                mPage.getData().remove(ActionPage.A3NAME_DATA_KEY);
                mPage.getData().remove(ActionPage.A3DESC_DATA_KEY);
                mPage.getData().remove(ActionPage.A3MOD_DATA_KEY);
                mPage.getData().remove(ActionPage.A3ROLL1_DATA_KEY);
                mPage.getData().remove(ActionPage.A3TYPE1_DATA_KEY);
                mPage.getData().remove(ActionPage.A3ROLL2_DATA_KEY);
                mPage.getData().remove(ActionPage.A3TYPE2_DATA_KEY);
                mPage.getData().remove(ActionPage.A3AUTO_DATA_KEY);
                mPage.getData().remove(ActionPage.A3ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 3:
                mPage.getData().remove(ActionPage.A4NAME_DATA_KEY);
                mPage.getData().remove(ActionPage.A4DESC_DATA_KEY);
                mPage.getData().remove(ActionPage.A4MOD_DATA_KEY);
                mPage.getData().remove(ActionPage.A4ROLL1_DATA_KEY);
                mPage.getData().remove(ActionPage.A4TYPE1_DATA_KEY);
                mPage.getData().remove(ActionPage.A4ROLL2_DATA_KEY);
                mPage.getData().remove(ActionPage.A4TYPE2_DATA_KEY);
                mPage.getData().remove(ActionPage.A4AUTO_DATA_KEY);
                mPage.getData().remove(ActionPage.A4ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 4:
                mPage.getData().remove(ActionPage.A5NAME_DATA_KEY);
                mPage.getData().remove(ActionPage.A5DESC_DATA_KEY);
                mPage.getData().remove(ActionPage.A5MOD_DATA_KEY);
                mPage.getData().remove(ActionPage.A5ROLL1_DATA_KEY);
                mPage.getData().remove(ActionPage.A5TYPE1_DATA_KEY);
                mPage.getData().remove(ActionPage.A5ROLL2_DATA_KEY);
                mPage.getData().remove(ActionPage.A5TYPE2_DATA_KEY);
                mPage.getData().remove(ActionPage.A5AUTO_DATA_KEY);
                mPage.getData().remove(ActionPage.A5ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }
}
