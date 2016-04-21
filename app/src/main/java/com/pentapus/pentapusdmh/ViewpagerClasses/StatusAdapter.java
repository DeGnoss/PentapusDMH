package com.pentapus.pentapusdmh.ViewpagerClasses;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Koni on 03.04.2016.
 */
public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private final List<StatusEffect> statusList = Arrays.asList(new StatusEffect("Blinded", R.drawable.status_blinded), new StatusEffect("Charmed", R.drawable.status_charmed), new StatusEffect("Deafened", R.drawable.status_deafened), new StatusEffect("Frightened", R.drawable.status_frightened), new StatusEffect("Grappled", R.drawable.status_grappled), new StatusEffect("Incapacitated", R.drawable.status_incapacitated), new StatusEffect("Invisible", R.drawable.status_invisible),
            new StatusEffect("Paralyzed", R.drawable.status_paralyzed), new StatusEffect("Petrified", R.drawable.status_petrified), new StatusEffect("Poisoned", R.drawable.status_poisoned), new StatusEffect("Prone", R.drawable.status_prone), new StatusEffect("Restrained", R.drawable.status_restrained), new StatusEffect("Stunned", R.drawable.status_stunned), new StatusEffect("Unconscious", R.drawable.status_unconscious), new StatusEffect("Custom", R.drawable.status_custom));
    private final String[] statusNames = {"Blinded", "Charmed", "Deafened", "Frightened", "Grappled", "Incapacitated", "Invisible", "Paralyzed", "Petrified", "Poisoned", "Prone", "Restrained", "Stunned", "Unconscious", "Custom"};
    private final Integer[] statusImageId = {R.drawable.status_blinded, R.drawable.status_charmed, R.drawable.status_deafened, R.drawable.status_frightened, R.drawable.status_grappled, R.drawable.status_incapacitated, R.drawable.status_invisible, R.drawable.status_paralyzed, R.drawable.status_petrified, R.drawable.status_poisoned, R.drawable.status_prone, R.drawable.status_restrained, R.drawable.status_stunned, R.drawable.status_unconscious, R.drawable.status_custom};
    private final boolean[] statuses = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};


    @Override
    public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.status_layout, parent, false);

        return new StatusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatusAdapter.StatusViewHolder holder, int position) {
        StatusEffect ci = statusList.get(position);
        holder.vStatusName.setText(ci.name);
        holder.vStatusImage.setImageResource(ci.imageId);
        holder.vCardView.setCardBackgroundColor(statusList.get(position).isSelected() ? Color.parseColor("#77DCE775") : Color.TRANSPARENT);

    }

    @Override
    public int getItemCount() {
            return statusList.size();
    }

    public void statusClicked(int position) {
        statusList.get(position).toggleSelected();
        notifyItemChanged(position);
    }

    public void setStatuses(boolean[] selected){
        for(int i=0; i<selected.length; i++){
            statusList.get(i).setSelected(selected[i]);
        }
    }

    public boolean[] getStatuses(){
        boolean[] statuses = new boolean[15];
        for(int i=0;i<statuses.length; i++){
            statuses[i] = statusList.get(i).isSelected();
        }
        return statuses;
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        protected TextView vStatusName;
        protected ImageView vStatusImage;
        protected CardView vCardView;


        public StatusViewHolder(View v) {
            super(v);
            vStatusName =  (TextView) v.findViewById(R.id.statusTextView);
            vStatusImage = (ImageView)  v.findViewById(R.id.statusImageView);
            vCardView = (CardView) v.findViewById(R.id.status_card);
        }
    }
}
