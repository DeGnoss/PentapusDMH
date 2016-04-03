package com.pentapus.pentapusdmh;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Koni on 30/3/16.
 */
public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.CharacterViewHolder>{
    private List<TrackerInfoCard> characterList = new ArrayList<>();

    public TrackerAdapter(List<TrackerInfoCard> characterList) {
        this.characterList = characterList;
    }

    public TrackerAdapter() {
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder characterViewHolder, int position) {
        TrackerInfoCard ci = characterList.get(position);
        characterViewHolder.vName.setText(ci.name);
        characterViewHolder.vInitiative.setText(ci.initiative);
        characterViewHolder.vAc.setText(ci.ac);
        characterViewHolder.vHp.setText(ci.maxHp);
        if(!characterList.get(position).dead) {
            if (characterList.get(position).type.equals("npc")) {
                characterViewHolder.cardViewTracker.setCardBackgroundColor(Color.parseColor("#55D32F2F"));
            } else if (characterList.get(position).type.equals("pc")) {
                characterViewHolder.cardViewTracker.setCardBackgroundColor(Color.parseColor("#551976D2"));
            }
        }else{
            characterViewHolder.cardViewTracker.setCardBackgroundColor(Color.parseColor("#99000000"));
        }

       /* if (position == 0) {
            characterViewHolder.cardViewTracker.setCardBackgroundColor(Color.parseColor("#551976D2"));
        } */
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_tracker, viewGroup, false);

        return new CharacterViewHolder(itemView);
    }

    public void addListItem(TrackerInfoCard trackerInfoCard){
        characterList.add(trackerInfoCard);
        characterList = sortList(characterList);
    }

    public void moveToBottom(){
        characterList.add(characterList.remove(0));
        if(characterList.get(0).dead && characterList.get(0).type != "pc"){
            moveToBottom();
        }
    }

    private List<TrackerInfoCard> sortList(List<TrackerInfoCard> list){
        Collections.sort(list, new Comparator<TrackerInfoCard>() {
            public int compare(TrackerInfoCard lhs, TrackerInfoCard rhs) {
                if (Integer.parseInt(lhs.initiative) > Integer.parseInt(rhs.initiative)) {
                    return -1;
                } else if (Integer.parseInt(lhs.initiative) == Integer.parseInt(rhs.initiative)) {
                    return reCompare(lhs, rhs);
                } else {
                    return 1;
                }
            }
        });
        return list;
    }

    private int reCompare(TrackerInfoCard lhs, TrackerInfoCard rhs) {
        if((Integer.parseInt(lhs.initiativeMod)+DiceHelper.d20()) > (Integer.parseInt(rhs.initiativeMod)+DiceHelper.d20())){
            return -1;
        } else if((Integer.parseInt(lhs.initiativeMod)+DiceHelper.d20()) == (Integer.parseInt(rhs.initiativeMod)+DiceHelper.d20())){
            return reCompare(lhs, rhs);
        }
        else{
            return 1;
        }
    }

    public void setHp(int id, int hpDiff){
        if(!(characterList.get(id).type).equals("pc")){
            characterList.get(id).maxHp = String.valueOf(Integer.parseInt(characterList.get(id).maxHp) - hpDiff);
            if((Integer.parseInt(characterList.get(id).maxHp)) == 0){
                characterList.get(id).dead = true;
            } else if((Integer.parseInt(characterList.get(id).maxHp)) < 0){
                characterList.get(id).dead = true;
                characterList.get(id).maxHp = "0";
            }
            notifyItemChanged(id);
        }
    }

    public void setSelected(int pos) {
        try {
            if (characterList.size() > 1) {
                characterList.get(pos).setSelected(false);
            }
            characterList.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vInitiative;
        protected TextView vAc;
        protected TextView vHp;
        protected  CardView cardViewTracker;



        public CharacterViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.name);
            vInitiative = (TextView)  v.findViewById(R.id.initiative);
            vAc = (TextView) v.findViewById(R.id.acV);
            vHp = (TextView) v.findViewById(R.id.hpV);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_tracker);

        }

        public void setEnable(){

        }
    }

}
