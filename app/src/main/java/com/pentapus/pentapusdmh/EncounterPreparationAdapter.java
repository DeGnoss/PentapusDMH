package com.pentapus.pentapusdmh;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewSubAdapter;

/**
 * Created by Koni on 30/3/16.
 */
public class EncounterPreparationAdapter extends RecyclerView.Adapter<EncounterPreparationAdapter.CharacterViewHolder>{
    private List<SimpleItemCard> characterList = new ArrayList<>();
    private Context context;
    private int layoutCounter = 0;
    private GridLayout.LayoutParams layoutParams;

    public EncounterPreparationAdapter(Context context, List<SimpleItemCard> characterList) {
        this.characterList = characterList;
        this.context = context;
        layoutCounter = 0;
    }

    public EncounterPreparationAdapter(Context context) {
        this.context = context;
        layoutCounter = 0;
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder characterViewHolder, int position) {
        layoutCounter = 0;

        SimpleItemCard ci = characterList.get(position);
        characterViewHolder.vName.setText(ci.name);
        characterViewHolder.vInfo.setText(ci.info);
        characterViewHolder.ivIcon.setImageURI(ci.iconUri);
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_encounter_prep, viewGroup, false);

        return new CharacterViewHolder(itemView);
    }

    public void addListItem(SimpleItemCard simpleItemCard){
        characterList.add(simpleItemCard);
        //characterList = sortList(characterList);
    }

    public SimpleItemCard getItem(int pos){
        return characterList.get(pos);
    }


    public static class CharacterViewHolder extends RecyclerView.ViewHolder{
        public View view;
        protected TextView vName;
        protected TextView vInfo;
        protected CardView cardViewTracker;
        protected View vIndicatorLine;
        protected ImageView ivIcon;



        public CharacterViewHolder(View v) {
            super(v);
            vIndicatorLine = (View) v.findViewById(R.id.indicator_line);
            vName =  (TextView) v.findViewById(R.id.name);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_tracker);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            vInfo = (TextView) v.findViewById(R.id.info);
            view = v.findViewById(R.id.view);
        }
    }

}
