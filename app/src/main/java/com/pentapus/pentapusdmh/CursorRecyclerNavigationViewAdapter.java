package com.pentapus.pentapusdmh;


import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewSubAdapter;


public class CursorRecyclerNavigationViewAdapter extends RecyclerViewSubAdapter<CursorRecyclerNavigationViewAdapter.EncounterViewHolder> implements AdapterNavigationCallback {

    private Context mContext;
    private static int selectedType = -1;
    public static int selectedPos = -1;

    private Cursor mCursor;

    private boolean mDataValid;

    private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_navigation);

    private AdapterNavigationCallback mAdapterCallback;

    List<String> itemsPendingRemoval;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>();
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;


    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;


    public CursorRecyclerNavigationViewAdapter(Context context, Cursor cursor, AdapterNavigationCallback callback) {
        this.mAdapterCallback = callback;
        itemsPendingRemoval = new ArrayList<>();
        setHasStableIds(true);
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
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_navigation, parent, false);

        return new EncounterViewHolder(itemView, this);
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
        if (mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        } else {
            return 0;
        }

    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }


    @Override
    public void onBindViewHolder(final EncounterViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        final SimpleNavigationItemCard simpleItemCard = SimpleNavigationItemCard.fromCursor(mCursor);
        viewHolder.vName.setText(simpleItemCard.getName());
        viewHolder.vInfo.setText(simpleItemCard.getInfo());
        viewHolder.itemView.setSelected(selectedPos == position);
       // viewHolder.clicker.setOnTouchListener(rippleForegroundListener);


        if (itemsPendingRemoval.contains(String.valueOf(simpleItemCard.getRowId()))) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            viewHolder.vName.setVisibility(View.GONE);
            viewHolder.vInfo.setVisibility(View.GONE);
            viewHolder.vInfoDeleted.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(String.valueOf(simpleItemCard.getRowId()));
                    pendingRunnables.remove(String.valueOf(simpleItemCard.getRowId()));
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(String.valueOf(simpleItemCard.getRowId()));
                    // this will rebind the row in "normal" state
                    notifyItemChanged(position);
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.vName.setVisibility(View.VISIBLE);
            viewHolder.vInfo.setVisibility(View.VISIBLE);
            // viewHolder.titleTextView.setText(item);
            viewHolder.vInfoDeleted.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }
    }


    public void pendingRemoval(final int position) {
        mCursor.moveToPosition(position);
        Log.d("pendingRemoval ", String.valueOf(position));
        final SimpleNavigationItemCard simpleItemCard = SimpleNavigationItemCard.fromCursor(mCursor);
        if (!itemsPendingRemoval.contains(String.valueOf(simpleItemCard.getRowId()))) {
            itemsPendingRemoval.add(String.valueOf(simpleItemCard.getRowId()));
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(position, String.valueOf(simpleItemCard.getRowId()));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(String.valueOf(simpleItemCard.getRowId()), pendingRemovalRunnable);
        }
    }


    public void remove(int position, String identifier) {
        if (itemsPendingRemoval.contains(identifier)){
            itemsPendingRemoval.remove(identifier);
        }
        int encounterId = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + encounterId);
        mContext.getContentResolver().delete(uri, null, null);
        notifyItemRemoved(position);
    }


    public boolean isPendingRemoval(int position) {
        mCursor.moveToPosition(position);
        SimpleNavigationItemCard simpleItemCard = SimpleNavigationItemCard.fromCursor(mCursor);
        return itemsPendingRemoval.contains(String.valueOf(simpleItemCard.getRowId()));
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
    public void onItemClick(int position) {
        Log.d("Adapter ", "clicked");
        mAdapterCallback.onItemClick(position);
    }

    @Override
    public void onItemLongCLick(int position) {
        notifyItemChanged(selectedPos);
        if (selectedPos == position) {
            selectedPos = -1;
            selectedType = -1;
        }else{
            selectedPos = position;
        }
        notifyItemChanged(position);
        Log.d("Adapter ", "longclicked");
        mAdapterCallback.onItemLongCLick(position);
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

    public static class EncounterViewHolder extends RecyclerViewSubAdapter.ViewHolder {
        public View view;
        protected TextView vName, vInfo, vInfoDeleted;
        protected CardView cardViewTracker;
        private RelativeLayout clicker;
        private AdapterNavigationCallback mAdapterCallback;
        private Button undoButton;
        private int id;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_navigation);


        public EncounterViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);
            this.mAdapterCallback = adapterCallback;
            vName = (TextView) v.findViewById(R.id.nameEncounter);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_navigation);
            vInfo = (TextView) v.findViewById(R.id.info);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker);
            undoButton = (Button) v.findViewById(R.id.undo_button);
            vInfoDeleted = (TextView) v.findViewById(R.id.info_deleted);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("Viewholder ", "longclicked");
                    mAdapterCallback.onItemLongCLick(getAdapterPosition());
                    return true;
                }
            });

            clicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onItemClick(getAdapterPosition());
                    Log.d("Viewholder ", "clicked");
                }
            });


        }

    }
}