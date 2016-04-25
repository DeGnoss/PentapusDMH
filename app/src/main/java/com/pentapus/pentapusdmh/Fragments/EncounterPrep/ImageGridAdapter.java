package com.pentapus.pentapusdmh.Fragments.EncounterPrep;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.ViewpagerClasses.StatusEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Koni on 03.04.2016.
 */
public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ImageViewHolder> implements AdapterNavigationCallback{
    private static int selectedPos =-1;
    private static int highlightedPos = -1;
    private File imageUris[];
    private Context mContext;
    private static Uri selectedUri;
    private AdapterNavigationCallback mAdapterNavigationCallback;
    private File directory;


    public ImageGridAdapter(Context context, AdapterNavigationCallback adapterNavigationCallback) {
        mAdapterNavigationCallback = adapterNavigationCallback;
        mContext = context;
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        directory = cw.getDir("myIconDir", Context.MODE_PRIVATE);
        imageUris = directory.listFiles();
    }



    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.my_icons_viewholder, parent, false);

        return new ImageViewHolder(itemView, mAdapterNavigationCallback);
    }

    @Override
    public void onBindViewHolder(ImageGridAdapter.ImageViewHolder holder, int position) {
        Glide
                .with(mContext)
                .load(imageUris[position])
                .fitCenter()
                .into((ImageView) holder.vStatusImage);

        holder.vCardView.setCardBackgroundColor(position==selectedPos ? Color.parseColor("#aa000000") : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
            return imageUris.length;
    }

    public void statusClicked(int position) {
        int oldPos = selectedPos;
        if(position == selectedPos){
            selectedPos = -1;
            notifyItemChanged(position);
        }else{
            selectedPos = position;
            notifyItemChanged(oldPos);
            notifyItemChanged(position);
        }
    }

    public static void setHighlightedPos(int highlightedPos) {
        ImageGridAdapter.selectedPos = highlightedPos;
    }



    public void setSelectedPos(int selectedPos) {
        ImageGridAdapter.selectedPos = selectedPos;
    }

    public static int getSelectedPos() {
        return selectedPos;
    }

    public static void setSelectedUri(Uri selectedUri) {
        ImageGridAdapter.selectedUri = selectedUri;
    }



    public File[] getImageUris() {
        return imageUris;
    }

    public void setImageUris(File[] imageUris) {
        this.imageUris = imageUris;
    }

    public void updateUris(){
        imageUris = directory.listFiles();
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        mAdapterNavigationCallback.onItemClick(position);
    }

    @Override
    public void onItemLongCLick(int position) {
        mAdapterNavigationCallback.onItemLongCLick(position);
    }

    @Override
    public void onMenuRefresh() {

    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vStatusImage;
        protected CardView vCardView;
        private AdapterNavigationCallback mAdapterNavigationCallback;
        private RelativeLayout clicker;

        public ImageViewHolder(View v, AdapterNavigationCallback adapterNavigationCallback) {
            super(v);
            mAdapterNavigationCallback = adapterNavigationCallback;
            vStatusImage = (ImageView)  v.findViewById(R.id.ivGridIcon);
            vCardView = (CardView) v.findViewById(R.id.cardGridIcon);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_imagegrid);

            clicker.setActivated(getAdapterPosition() == selectedPos);


            vCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterNavigationCallback.onItemClick(getAdapterPosition());
                }
            });

            vCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mAdapterNavigationCallback.onItemLongCLick(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public static Uri getSelectedUri() {
        return selectedUri;
    }
}
