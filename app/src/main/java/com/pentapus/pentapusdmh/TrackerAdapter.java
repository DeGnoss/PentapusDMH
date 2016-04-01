package com.pentapus.pentapusdmh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Koni on 30/3/16.
 */
public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.CharacterViewHolder>{
    private List<TrackerInfoCard> characterList = new ArrayList<>();
    private static List<TrackerInfoCard> initiativeList = new ArrayList<>();


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

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vInitiative;

        public CharacterViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.name);
            vInitiative = (TextView)  v.findViewById(R.id.initiative);
        }
    }

}
