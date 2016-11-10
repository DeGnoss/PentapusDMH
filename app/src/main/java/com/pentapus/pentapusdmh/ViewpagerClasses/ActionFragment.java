package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerFragment;
import com.pentapus.pentapusdmh.Fragments.Tracker.TrackerInfoCard;
import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.R;

/**
 * Created by Koni on 02.04.2016.
 */
public class ActionFragment extends Fragment {
    private static final String ARG_PAGE = "ARG_PAGE";

    private TextView tvMultiAttack, tvAction1, tvAction2, tvAction3, tvAction4;
    private CardView cardMultiattack, cardAction1, cardAction2, cardAction3, cardAction4;
    private Spanned multiAttack, action1, action2, action3, action4;
    private TrackerInfoCard selectedCharacter;

    private int id;
    private int[] abilities;

    public static ActionFragment newInstance(int id) {
        ActionFragment fragment = new ActionFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        //abilities = ((TrackerFragment)getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().getAbilityMods(id);
        selectedCharacter = ((TrackerFragment) getParentFragment().getFragmentManager().findFragmentByTag("F_TRACKER")).getChars().getItem(id);
    }



    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.viewpager_actions, container, false);

        tvMultiAttack = (TextView) view.findViewById(R.id.tvMultiattack);
        tvAction1 = (TextView) view.findViewById(R.id.tvAction1);
        tvAction2 = (TextView) view.findViewById(R.id.tvAction2);
        tvAction3 = (TextView) view.findViewById(R.id.tvAction3);
        tvAction4 = (TextView) view.findViewById(R.id.tvAction4);
        cardMultiattack = (CardView) view.findViewById(R.id.cardMultiattack);
        cardAction1 = (CardView) view.findViewById(R.id.cardAction1);
        cardAction2 = (CardView) view.findViewById(R.id.cardAction2);
        cardAction3 = (CardView) view.findViewById(R.id.cardAction3);
        cardAction4 = (CardView) view.findViewById(R.id.cardAction4);


        if(selectedCharacter.getMultiattack() != null && !selectedCharacter.getMultiattack().isEmpty()){
            multiAttack = Html.fromHtml("<b>Multiattack. </b>" + selectedCharacter.getMultiattack());
            tvMultiAttack.setText(multiAttack);
        }else{
            cardMultiattack.setVisibility(View.GONE);
        }

        if(selectedCharacter.getAtk1name() != null && !selectedCharacter.getAtk1name().isEmpty()){
            action1 = Html.fromHtml("<b>" + selectedCharacter.getAtk1name() + "</b>" + ". " + selectedCharacter.getAtk1desc());
            tvAction1.setText(action1);
        }else{
            cardAction1.setVisibility(View.GONE);
        }

        if(selectedCharacter.getAtk2name() != null && !selectedCharacter.getAtk2name().isEmpty()){
            action2 = Html.fromHtml("<b>" + selectedCharacter.getAtk2name() + "</b>" + ". " + selectedCharacter.getAtk2desc());
            tvAction2.setText(action2);
        }else{
            cardAction2.setVisibility(View.GONE);
        }

        if(selectedCharacter.getAtk3name() != null && !selectedCharacter.getAtk3name().isEmpty()){
            action3 = Html.fromHtml("<b>" + selectedCharacter.getAtk3name() + "</b>" + ". " + selectedCharacter.getAtk3desc());
            tvAction3.setText(action3);
        }else{
            cardAction3.setVisibility(View.GONE);
        }

        if(selectedCharacter.getAtk4name() != null && !selectedCharacter.getAtk4name().isEmpty()){
            action4 = Html.fromHtml("<b>" + selectedCharacter.getAtk4name() + "</b>" + ". " + selectedCharacter.getAtk4desc());
            tvAction4.setText(action4);
        }else{
            cardAction4.setVisibility(View.GONE);
        }






        // Inflate the layout to use as dialog or embedded fragment
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
    }


}