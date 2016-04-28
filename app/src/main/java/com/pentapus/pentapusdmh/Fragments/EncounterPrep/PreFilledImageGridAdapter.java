package com.pentapus.pentapusdmh.Fragments.EncounterPrep;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.R;

import java.io.File;

/**
 * Created by Koni on 03.04.2016.
 */
public class PreFilledImageGridAdapter extends RecyclerView.Adapter<PreFilledImageGridAdapter.ImageViewHolder> implements AdapterNavigationCallback{

    private File imageUris[];
    private Context mContext;
    private AdapterNavigationCallback mAdapterNavigationCallback;
    private File directory;


    public PreFilledImageGridAdapter(Context context, AdapterNavigationCallback adapterNavigationCallback) {
        mAdapterNavigationCallback = adapterNavigationCallback;
        mContext = context;
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        directory = cw.getDir("IconDir", Context.MODE_PRIVATE);
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
    public void onBindViewHolder(PreFilledImageGridAdapter.ImageViewHolder holder, int position) {
        Glide
                .with(mContext)
                .load(imageUris[position])
                .fitCenter()
                .into(holder.vStatusImage);

        holder.vCardView.setCardBackgroundColor(ImageViewPagerDialogFragment.getSelectedType() == 1 && position==ImageViewPagerDialogFragment.getSelectedPos() ? Color.parseColor("#aa000000") : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
            return imageUris.length;
    }

    public void statusClicked(int position) {
        int oldPos = ImageViewPagerDialogFragment.getSelectedPos();
        if(ImageViewPagerDialogFragment.getSelectedType() == 1 && position == ImageViewPagerDialogFragment.getSelectedPos()){
            ImageViewPagerDialogFragment.setSelectedType(-1);
            ImageViewPagerDialogFragment.setSelectedPos(-1);
            ImageViewPagerDialogFragment.setSelectedUri(null);
            notifyItemChanged(position);
        }else if(ImageViewPagerDialogFragment.getSelectedType() == 1){
            ImageViewPagerDialogFragment.setSelectedPos(position);
            ImageViewPagerDialogFragment.setSelectedUri(Uri.parse(imageUris[position].toString()));
            notifyItemChanged(oldPos);
            notifyItemChanged(position);
        }else{
            ImageViewPagerDialogFragment.setSelectedType(1);
            ImageViewPagerDialogFragment.setSelectedPos(position);
            ImageViewPagerDialogFragment.setSelectedUri(Uri.parse(imageUris[position].toString()));
            notifyItemChanged(position);
        }
    }



    public File[] getImageUris() {
        return imageUris;
    }

    public void setImageUris(File[] imageUris) {
        this.imageUris = imageUris;
    }

    @Override
    public void onItemClick(int position) {
        mAdapterNavigationCallback.onItemClick(position);
    }

    @Override
    public void onItemLongCLick(int position) {

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

            //clicker.setActivated(ImageViewPagerDialogFragment.getSelectedType() == 1 && getAdapterPosition() == ImageViewPagerDialogFragment.getHighlightedPos());


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
}
