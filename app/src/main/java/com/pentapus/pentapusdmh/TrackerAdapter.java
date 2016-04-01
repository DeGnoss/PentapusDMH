package com.pentapus.pentapusdmh;

import android.app.Dialog;
import android.content.Context;
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
        if(Integer.parseInt(characterList.get(id).maxHp) !=0){
            characterList.get(id).maxHp = String.valueOf(Integer.parseInt(characterList.get(id).maxHp) + hpDiff);
            notifyItemChanged(id);
        }
    }




    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vInitiative;
        protected TextView vAc;
        protected TextView vHp;


        public CharacterViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.name);
            vInitiative = (TextView)  v.findViewById(R.id.initiative);
            vAc = (TextView) v.findViewById(R.id.acV);
            vHp = (TextView) v.findViewById(R.id.hpV);
        }
    }

}
