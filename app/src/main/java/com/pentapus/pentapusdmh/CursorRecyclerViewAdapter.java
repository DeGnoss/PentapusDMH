package com.pentapus.pentapusdmh;

/*
 * Copyright (C) 2014 skyfish.jy@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pentapus.pentapusdmh.Fragments.EncounterFragment;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewSubAdapter;


/**
 * Created by skyfishjy on 10/31/14.
 */

public class CursorRecyclerViewAdapter extends RecyclerViewSubAdapter<CursorRecyclerViewAdapter.CharacterViewHolder> implements AdapterCallback {

    private Context mContext;
    private static int selectedType = -1;
    public static int selectedPos = -1;

    private Cursor mCursor;

    private boolean mDataValid;

    private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_tracker);

    private AdapterCallback mAdapterCallback;


    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;



    public CursorRecyclerViewAdapter(Context context, Cursor cursor, AdapterCallback callback) {
        this.mAdapterCallback = callback;
        mContext = context;
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_encounter_prep, parent, false);

        return new CharacterViewHolder(itemView, this);
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }


    @Override
    public void onBindViewHolder(CharacterViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        SimpleItemCard simpleItemCard = SimpleItemCard.fromCursor(mCursor);
        viewHolder.vName.setText(simpleItemCard.getName());
        viewHolder.vInfo.setText(simpleItemCard.getInfo());
        viewHolder.type = simpleItemCard.getType();
        if (viewHolder.type==1) {
            viewHolder.vIndicatorLine.setBackgroundColor(Color.parseColor("#F44336"));
        } else if (viewHolder.type==2) {
            viewHolder.vIndicatorLine.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        viewHolder.ivIcon.setImageURI(simpleItemCard.getIconUri());
        viewHolder.itemView.setSelected(selectedType == simpleItemCard.getType() && selectedPos == position);
        viewHolder.clicker.setOnTouchListener(rippleForegroundListener);


/*

        viewHolder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Viewholder ", "clicked");
            }
        });

        viewHolder.clicker.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                Log.d("ViewHolder ", "longclicked");
                return false;
            }
        }); */

      /*
        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int positionAdapter, int positionView, int positionType, boolean isLongClick) {
                itemClickListener.onItemClick(positionAdapter, positionType, isLongClick);
                if(isLongClick){
                    selectedPos = positionAdapter;
                    selectedType = positionType;
                    Log.d("view", String.valueOf(positionView));
                }else{
                    notifyDataSetChanged();
                    Log.d("adapter", String.valueOf(positionAdapter));
                    Log.d("type", String.valueOf(positionType));
                }
                notifyDataSetChanged();
            }
        }); */
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    @Override
    public void onItemClick(int position, int positionType) {
        Log.d("Adapter ", "clicked");
        mAdapterCallback.onItemClick(position, positionType);
    }

    @Override
    public void onItemLongCLick(int position, int positionType) {
        selectedPos = position;
        selectedType = positionType;
        notifyDataSetChanged();
        Log.d("Adapter ", "longclicked");
        mAdapterCallback.onItemLongCLick(position, positionType);
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

    public static class CharacterViewHolder extends RecyclerViewSubAdapter.ViewHolder{
        public View view;
        protected TextView vName;
        protected TextView vInfo;
        protected CardView cardViewTracker;
        protected View vIndicatorLine;
        protected ImageView ivIcon;
        private int type;
        private RelativeLayout clicker;
        private AdapterCallback mAdapterCallback;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_tracker);


        public CharacterViewHolder(View v, AdapterCallback adapterCallback) {
            super(v);
            this.mAdapterCallback = adapterCallback;
            vIndicatorLine = (View) v.findViewById(R.id.indicator_line);
            vName =  (TextView) v.findViewById(R.id.name);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_tracker);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            vInfo = (TextView) v.findViewById(R.id.info);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("Viewholder ", "longclicked");
                    mAdapterCallback.onItemLongCLick(getAdapterPosition(), type);
                    return false;
                }
            });

            clicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onItemClick(getAdapterPosition(), type);
                    Log.d("Viewholder ", "clicked");
                }
            });



        }

    }
}