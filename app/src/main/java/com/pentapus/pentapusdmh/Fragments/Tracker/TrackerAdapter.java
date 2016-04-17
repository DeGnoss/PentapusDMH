package com.pentapus.pentapusdmh.Fragments.Tracker;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.HelperClasses.DiceHelper;
import com.pentapus.pentapusdmh.HelperClasses.Utils;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Koni on 30/3/16.
 */
public class TrackerAdapter extends RecyclerView.Adapter<TrackerAdapter.CharacterViewHolder> {
    private List<TrackerInfoCard> characterList = new ArrayList<>();
    private Context context;
    private int layoutCounter = 0;
    private GridLayout.LayoutParams layoutParams;
    private boolean firstTime;
    CustomRecyclerLayoutManager llm;

    public TrackerAdapter(Context context, List<TrackerInfoCard> characterList) {
        this.characterList = characterList;
        this.context = context;
        layoutCounter = 0;
    }

    public TrackerAdapter(Context context) {
        this.llm = llm;
        this.context = context;
        layoutCounter = 0;
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    @Override
    public void onBindViewHolder(final CharacterViewHolder characterViewHolder, int position) {
        layoutCounter = 0;
        if (characterList.get(position).hp == 0) {
            characterList.get(position).dead = true;
        } else if (characterList.get(position).hp < 0) {
            characterList.get(position).dead = true;
            characterList.get(position).hp = 0;
        } else {
            characterList.get(position).dead = false;
        }
        TrackerInfoCard ci = characterList.get(position);
        characterViewHolder.vName.setText(ci.name);
        characterViewHolder.vInitiative.setText(ci.initiative);
        characterViewHolder.vAc.setText(ci.ac);
        characterViewHolder.vHp.setText(String.valueOf(ci.hp));
        characterViewHolder.ivIcon.setImageURI(ci.iconUri);
        if (characterList.get(position).type.equals("npc")) {
            characterViewHolder.vIndicatorLine.setBackgroundColor(Color.parseColor("#F44336"));
        } else if (characterList.get(position).type.equals("pc")) {
            characterViewHolder.vIndicatorLine.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        if (characterList.get(position).dead) {
            characterViewHolder.cardViewTracker.setBackgroundColor(Color.parseColor("#99AA361C"));
        } else {
            characterViewHolder.cardViewTracker.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if (characterList.get(position).isBlinded()) {
            characterViewHolder.vImageView1.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView1.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView1.setVisibility(View.GONE);
        }
        if (characterList.get(position).isCharmed()) {
            characterViewHolder.vImageView2.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView2.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView2.setVisibility(View.GONE);
        }
        if (characterList.get(position).isDeafened()) {
            characterViewHolder.vImageView3.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView3.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView3.setVisibility(View.GONE);
        }
        if (characterList.get(position).isFrightened()) {
            characterViewHolder.vImageView4.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView4.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView4.setVisibility(View.GONE);
        }
        if (characterList.get(position).isGrappled()) {
            characterViewHolder.vImageView5.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView5.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView5.setVisibility(View.GONE);
        }
        if (characterList.get(position).isIncapacitated()) {
            characterViewHolder.vImageView6.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView6.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView6.setVisibility(View.GONE);
        }
        if (characterList.get(position).isInvisible()) {
            characterViewHolder.vImageView7.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView7.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView7.setVisibility(View.GONE);
        }
        if (characterList.get(position).isParalyzed()) {
            characterViewHolder.vImageView8.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView8.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView8.setVisibility(View.GONE);
        }
        if (characterList.get(position).isPetrified()) {
            characterViewHolder.vImageView9.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView9.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView9.setVisibility(View.GONE);
        }
        if (characterList.get(position).isPoisoned()) {
            characterViewHolder.vImageView10.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView10.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView10.setVisibility(View.GONE);
        }
        if (characterList.get(position).isProne()) {
            characterViewHolder.vImageView11.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView11.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView11.setVisibility(View.GONE);
        }
        if (characterList.get(position).isRestrained()) {
            characterViewHolder.vImageView12.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView12.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView12.setVisibility(View.GONE);
        }
        if (characterList.get(position).isStunned()) {
            characterViewHolder.vImageView13.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView13.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView13.setVisibility(View.GONE);
        }
        if (characterList.get(position).isUnconscious()) {
            characterViewHolder.vImageView14.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView14.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView14.setVisibility(View.GONE);
        }
        if (characterList.get(position).isCustom()) {
            characterViewHolder.vImageView15.setVisibility(View.VISIBLE);
            characterViewHolder.vImageView15.setLayoutParams(getLayoutParams());
        } else {
            characterViewHolder.vImageView15.setVisibility(View.GONE);
        }
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_tracker, viewGroup, false);

        return new CharacterViewHolder(itemView);
    }

    public void addListItem(TrackerInfoCard trackerInfoCard) {
        characterList.add(trackerInfoCard);
        characterList = sortList(characterList);
    }

    public void moveToBottom() {
        characterList.add(characterList.remove(0));
        if (characterList.get(0).dead && characterList.get(0).type.equals("npc")) {
            moveToBottom();
        }
    }

    private List<TrackerInfoCard> sortList(List<TrackerInfoCard> list) {
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
        if ((Integer.parseInt(lhs.initiativeMod) + DiceHelper.d20()) > (Integer.parseInt(rhs.initiativeMod) + DiceHelper.d20())) {
            return -1;
        } else if ((Integer.parseInt(lhs.initiativeMod) + DiceHelper.d20()) == (Integer.parseInt(rhs.initiativeMod) + DiceHelper.d20())) {
            return reCompare(lhs, rhs);
        } else {
            return 1;
        }
    }

    public void setHp(int id, int hpDiff) {
        characterList.get(id).hp = characterList.get(id).hp - hpDiff;
        if (characterList.get(id).hp == 0) {
            characterList.get(id).dead = true;
        } else if (characterList.get(id).hp < 0) {
            characterList.get(id).dead = true;
            characterList.get(id).hp = 0;
        }
        notifyItemChanged(id);
    }

    public void setStatuses(int id, boolean[] statuses) {
        characterList.get(id).setStatuses(statuses);
        notifyItemChanged(id);
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

    public int getHp(int pos) {
        if (characterList.get(pos).hp != 0) {
            return characterList.get(pos).getHp();
        } else {
            return 0;
        }
    }

    public int[] getAbilities(int pos) {
        int[] abilities = new int[6];
        abilities[0] = characterList.get(pos).getStrength();
        abilities[1] = characterList.get(pos).getDexterity();
        abilities[2] = characterList.get(pos).getConstitution();
        abilities[3] = characterList.get(pos).getIntelligence();
        abilities[4] = characterList.get(pos).getWisdom();
        abilities[5] = characterList.get(pos).getCharisma();
        return abilities;
    }

    public boolean[] getStatuses(int pos) {
        return characterList.get(pos).getStatuses();
    }

    public void setItem(int pos, TrackerInfoCard trackerInfoCard) {
        characterList.set(pos, trackerInfoCard);
    }

    public TrackerInfoCard getItem(int pos) {
        return characterList.get(pos);
    }

    public GridLayout.LayoutParams getLayoutParams() {
        GridLayout.LayoutParams gridLayoutParams = new GridLayout.LayoutParams();
        //gridLayoutParams.height = 50;

        gridLayoutParams.height = (int) context.getResources().getDimension(R.dimen.image_status_trackerfragment);
        gridLayoutParams.width = (int) context.getResources().getDimension(R.dimen.image_status_trackerfragment);

        //gridLayoutParams.width = 50;
        if (layoutCounter < 5) {
            gridLayoutParams.columnSpec = GridLayout.spec(layoutCounter);
            gridLayoutParams.rowSpec = GridLayout.spec(0);
        } else if (layoutCounter < 10) {
            gridLayoutParams.columnSpec = GridLayout.spec(9 - layoutCounter);
            gridLayoutParams.rowSpec = GridLayout.spec(1);
        } else {
            gridLayoutParams.columnSpec = GridLayout.spec(14 - layoutCounter);
            gridLayoutParams.rowSpec = GridLayout.spec(2);
        }
        layoutCounter++;
        return gridLayoutParams;
    }


    public static class CharacterViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vInitiative;
        protected TextView vAc;
        protected TextView vHp;
        protected CardView cardViewTracker;
        protected GridLayout vGridStatus;
        protected View vIndicatorLine;
        protected ImageView ivIcon;
        protected ImageView vImageView1, vImageView2, vImageView3, vImageView4, vImageView5, vImageView6, vImageView7, vImageView8, vImageView9, vImageView10, vImageView11, vImageView12, vImageView13, vImageView14, vImageView15;
        public View view;


        public CharacterViewHolder(View v) {
            super(v);
            view = v;
            vIndicatorLine = (View) v.findViewById(R.id.indicator_line);
            vName = (TextView) v.findViewById(R.id.name);
            vInitiative = (TextView) v.findViewById(R.id.initiative);
            vAc = (TextView) v.findViewById(R.id.acV);
            vHp = (TextView) v.findViewById(R.id.hpV);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_tracker);
            vGridStatus = (GridLayout) v.findViewById(R.id.gridStatus);
            vImageView1 = (ImageView) v.findViewById(R.id.imageView1);
            vImageView2 = (ImageView) v.findViewById(R.id.imageView2);
            vImageView3 = (ImageView) v.findViewById(R.id.imageView3);
            vImageView4 = (ImageView) v.findViewById(R.id.imageView4);
            vImageView5 = (ImageView) v.findViewById(R.id.imageView5);
            vImageView6 = (ImageView) v.findViewById(R.id.imageView6);
            vImageView7 = (ImageView) v.findViewById(R.id.imageView7);
            vImageView8 = (ImageView) v.findViewById(R.id.imageView8);
            vImageView9 = (ImageView) v.findViewById(R.id.imageView9);
            vImageView10 = (ImageView) v.findViewById(R.id.imageView10);
            vImageView11 = (ImageView) v.findViewById(R.id.imageView11);
            vImageView12 = (ImageView) v.findViewById(R.id.imageView12);
            vImageView13 = (ImageView) v.findViewById(R.id.imageView13);
            vImageView14 = (ImageView) v.findViewById(R.id.imageView14);
            vImageView15 = (ImageView) v.findViewById(R.id.imageView15);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
        }
    }

}
