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
public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>{
    private List<CharacterInfoCard> characterList = new ArrayList<>();

    public CharacterAdapter(List<CharacterInfoCard> characterList) {
        this.characterList = characterList;
    }

    public CharacterAdapter() {
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder characterViewHolder, int position) {
        CharacterInfoCard ci = characterList.get(position);
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

    public void addListItem(CharacterInfoCard characterInfoCard){
        characterList.add(characterInfoCard);
        characterList = sortList(characterList);
    }

    private List<CharacterInfoCard> sortList(List<CharacterInfoCard> list){
        Collections.sort(list, new Comparator<CharacterInfoCard>() {
            public int compare(CharacterInfoCard lhs, CharacterInfoCard rhs) {
                if(Integer.parseInt(lhs.initiative) >= Integer.parseInt(rhs.initiative)){
                    return -1;
                } else{
                    return 1;
                }
            }
        });
        return list;
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
