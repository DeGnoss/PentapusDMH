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

public class LegendaryActionFragment extends Fragment {
    private static final String ARG_KEY = "legandaryactions";
    private int MSG_FINISH_DIALOG = 1001;


    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private LegendaryActionPage mPage;
    private ListView actionList;
    private FloatingActionButton fabActions;
    private Spanned action;
    private ArrayList<Spanned> listItems = new ArrayList<Spanned>();
    private ArrayAdapter<Spanned> adapter;
    private TextView tvTitleMultiattack;
    private EditText tvMultiAttack;

    public static LegendaryActionFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        LegendaryActionFragment fragment = new LegendaryActionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LegendaryActionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (LegendaryActionPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_actions, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        tvTitleMultiattack = (TextView) rootView.findViewById(R.id.tvTitleMultiattack);
        tvTitleMultiattack.setHint("Legendary Actions Multiattack");

        tvMultiAttack = (EditText) rootView.findViewById(R.id.tvMultiattack);
        if(mPage.getData().getString(LegendaryActionPage.LMULTIATTACK_DATA_KEY) != null && !mPage.getData().getString(LegendaryActionPage.LMULTIATTACK_DATA_KEY).isEmpty()){
            tvMultiAttack.setText(mPage.getData().getString(LegendaryActionPage.LMULTIATTACK_DATA_KEY));
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
        DialogFragment newFragment = AddLegendaryActionDialogFragment.newInstance(mode, name, description, atkmod, dmg1, dmg1type, dmg2, dmg2type, autoroll, additional, traitNumber);
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
                mPage.getData().putString(LegendaryActionPage.LMULTIATTACK_DATA_KEY,
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
                return new ActionDummy(mPage.getData().getString(LegendaryActionPage.LA1NAME_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA1DESC_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA1MOD_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA1ROLL1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA1TYPE1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA1ROLL2_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA1TYPE2_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA1AUTO_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA1ADD_DATA_KEY));
            case 1:
                return new ActionDummy(mPage.getData().getString(LegendaryActionPage.LA2NAME_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA2DESC_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA2MOD_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA2ROLL1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA2TYPE1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA2ROLL2_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA2TYPE2_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA2AUTO_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA2ADD_DATA_KEY));
            case 2:
                return new ActionDummy(mPage.getData().getString(LegendaryActionPage.LA3NAME_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA3DESC_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA3MOD_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA3ROLL1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA3TYPE1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA3ROLL2_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA3TYPE2_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA3AUTO_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA3ADD_DATA_KEY));
            case 3:
                return new ActionDummy(mPage.getData().getString(LegendaryActionPage.LA4NAME_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA4DESC_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA4MOD_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA4ROLL1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA4TYPE1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA4ROLL2_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA4TYPE2_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA4AUTO_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA4ADD_DATA_KEY));
            case 4:
                return new ActionDummy(mPage.getData().getString(LegendaryActionPage.LA5NAME_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA5DESC_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA5MOD_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA5ROLL1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA5TYPE1_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA5ROLL2_DATA_KEY), mPage.getData().getString(LegendaryActionPage.LA5TYPE2_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA5AUTO_DATA_KEY), mPage.getData().getBoolean(LegendaryActionPage.LA5ADD_DATA_KEY));
            default:
                return null;
        }
    }

    private void saveAction(ActionDummy action, int position) {
        switch (position) {
            case 0:
                mPage.getData().putString(LegendaryActionPage.LA1NAME_DATA_KEY, action.getName());
                mPage.getData().putString(LegendaryActionPage.LA1DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(LegendaryActionPage.LA1MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(LegendaryActionPage.LA1ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(LegendaryActionPage.LA1TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(LegendaryActionPage.LA1ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(LegendaryActionPage.LA1TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(LegendaryActionPage.LA1AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(LegendaryActionPage.LA1ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 1:
                mPage.getData().putString(LegendaryActionPage.LA2NAME_DATA_KEY, action.getName());
                mPage.getData().putString(LegendaryActionPage.LA2DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(LegendaryActionPage.LA2MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(LegendaryActionPage.LA2ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(LegendaryActionPage.LA2TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(LegendaryActionPage.LA2ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(LegendaryActionPage.LA2TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(LegendaryActionPage.LA2AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(LegendaryActionPage.LA2ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 2:
                mPage.getData().putString(LegendaryActionPage.LA3NAME_DATA_KEY, action.getName());
                mPage.getData().putString(LegendaryActionPage.LA3DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(LegendaryActionPage.LA3MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(LegendaryActionPage.LA3ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(LegendaryActionPage.LA3TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(LegendaryActionPage.LA3ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(LegendaryActionPage.LA3TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(LegendaryActionPage.LA3AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(LegendaryActionPage.LA3ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 3:
                mPage.getData().putString(LegendaryActionPage.LA4NAME_DATA_KEY, action.getName());
                mPage.getData().putString(LegendaryActionPage.LA4DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(LegendaryActionPage.LA4MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(LegendaryActionPage.LA4ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(LegendaryActionPage.LA4TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(LegendaryActionPage.LA4ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(LegendaryActionPage.LA4TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(LegendaryActionPage.LA4AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(LegendaryActionPage.LA4ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            case 4:
                mPage.getData().putString(LegendaryActionPage.LA5NAME_DATA_KEY, action.getName());
                mPage.getData().putString(LegendaryActionPage.LA5DESC_DATA_KEY, action.getDesc());
                mPage.getData().putString(LegendaryActionPage.LA5MOD_DATA_KEY, action.getAtkmod());
                mPage.getData().putString(LegendaryActionPage.LA5ROLL1_DATA_KEY, action.getDmg1());
                mPage.getData().putString(LegendaryActionPage.LA5TYPE1_DATA_KEY, action.getDmg1Type());
                mPage.getData().putString(LegendaryActionPage.LA5ROLL2_DATA_KEY, action.getDmg2());
                mPage.getData().putString(LegendaryActionPage.LA5TYPE2_DATA_KEY, action.getDmg2Type());
                mPage.getData().putBoolean(LegendaryActionPage.LA5AUTO_DATA_KEY, action.isAutoroll());
                mPage.getData().putBoolean(LegendaryActionPage.LA5ADD_DATA_KEY, action.isAdditional());
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }


    private void removeAction(int position){
        switch(position){
            case 0:
                mPage.getData().remove(LegendaryActionPage.LA1NAME_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1DESC_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1MOD_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1ROLL1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1TYPE1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1ROLL2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1TYPE2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1AUTO_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA1ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 1:
                mPage.getData().remove(LegendaryActionPage.LA2NAME_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2DESC_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2MOD_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2ROLL1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2TYPE1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2ROLL2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2TYPE2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2AUTO_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA2ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 2:
                mPage.getData().remove(LegendaryActionPage.LA3NAME_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3DESC_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3MOD_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3ROLL1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3TYPE1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3ROLL2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3TYPE2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3AUTO_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA3ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 3:
                mPage.getData().remove(LegendaryActionPage.LA4NAME_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4DESC_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4MOD_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4ROLL1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4TYPE1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4ROLL2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4TYPE2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4AUTO_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA4ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            case 4:
                mPage.getData().remove(LegendaryActionPage.LA5NAME_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5DESC_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5MOD_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5ROLL1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5TYPE1_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5ROLL2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5TYPE2_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5AUTO_DATA_KEY);
                mPage.getData().remove(LegendaryActionPage.LA5ADD_DATA_KEY);
                mPage.notifyDataChanged();
                break;
            default:
                break;
        }
    }
}