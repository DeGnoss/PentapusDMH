package com.pentapus.pentapusdmh.Fragments.EncounterPrep;


import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pentapus.pentapusdmh.AdapterCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.RippleForegroundListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewSubAdapter;


public class CursorRecyclerViewAdapter extends RecyclerViewSubAdapter<CursorRecyclerViewAdapter.CharacterViewHolder> implements AdapterCallback {

    private Context mContext;
    private static int selectedType = -1;
    public static int selectedPos = -1;

    private Cursor mCursor;

    private boolean mDataValid;

    private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_enc_prep);

    private AdapterCallback mAdapterCallback;

    private List<String> itemsPendingRemoval;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>();
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;


    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;


    public CursorRecyclerViewAdapter(Context context, Cursor cursor, AdapterCallback callback) {
        this.mAdapterCallback = callback;
        itemsPendingRemoval = new ArrayList<>();
        mContext = context;
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        setHasStableIds(true);
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
    public void onBindViewHolder(final CharacterViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        final SimpleItemCard simpleItemCard = SimpleItemCard.fromCursor(mCursor);
        viewHolder.vName.setText(simpleItemCard.getName());
        viewHolder.vInfo.setText(simpleItemCard.getInfo());
        viewHolder.type = simpleItemCard.getType();
        if (viewHolder.type == 1) {
            viewHolder.vIndicatorLine.setBackgroundColor(Color.parseColor("#F44336"));
        } else if (viewHolder.type == 2) {
            viewHolder.vIndicatorLine.setBackgroundColor(Color.parseColor("#3F51B5"));
        }
        viewHolder.ivIcon.setImageURI(simpleItemCard.getIconUri());
        viewHolder.itemView.setActivated(selectedType == simpleItemCard.getType() && selectedPos == position);
        viewHolder.clicker.setOnTouchListener(rippleForegroundListener);
        viewHolder.identifier = simpleItemCard.type + ":" + simpleItemCard.id;


        if (itemsPendingRemoval.contains(viewHolder.identifier)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            viewHolder.clicker.setVisibility(View.GONE);
            viewHolder.vInfoDeleted.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(viewHolder.identifier);
                    pendingRunnables.remove(viewHolder.identifier);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(viewHolder.identifier);
                    // this will rebind the row in "normal" state
                    notifyDataSetChanged();
                    //TODO: MAKE IT WORK WITH NOTIFYITEMCHANGED
                    notifyItemChanged(position);
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.clicker.setVisibility(View.VISIBLE);
            viewHolder.vInfoDeleted.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }
    }


    public void pendingRemoval(final int position, int notifyPosition) {
        mCursor.moveToPosition(position);
        final String identifier = mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)) + ":" + mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));

        if (!itemsPendingRemoval.contains(identifier)) {
            itemsPendingRemoval.add(identifier);
            // this will redraw row in "undo" state
            notifyDataSetChanged();
            //TODO: MAKE IT WORK WITH NOTIFYITEMCHANGED
            //notifyItemChanged(notifyPosition);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(position, identifier);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(identifier, pendingRemovalRunnable);
        }
    }


    public boolean isPendingRemoval(int position) {
        mCursor.moveToPosition(position);
        return itemsPendingRemoval.contains(mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)) + ":" + mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID)));
    }


    public void remove(int position, String identifier) {
        if (itemsPendingRemoval.contains(identifier)) {
            itemsPendingRemoval.remove(identifier);
        }
        mCursor.moveToPosition(position);
        int characterId = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        int characterType = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
        switch (characterType) {
            case 1:
                Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + characterId);
                notifyItemRemoved(position);
                mContext.getContentResolver().delete(uri, null, null);
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard.hasPrimaryClip()) {
                    ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
                    Uri pasteUri = itemPaste.getUri();
                    if (pasteUri == null) {
                        pasteUri = Uri.parse(String.valueOf(itemPaste.getText()));
                    }
                    if (pasteUri != null) {

                        if (pasteUri.equals(uri)) {
                            Uri newUri = Uri.parse("");
                            ClipData clip = ClipData.newUri(mContext.getContentResolver(), "URI", newUri);
                            clipboard.setPrimaryClip(clip);
                            mAdapterCallback.onMenuRefresh();
                        }
                    }
                }
                break;
            case 2:
                /*uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + characterId);
                notifyItemRemoved(position);
                mContext.getContentResolver().delete(uri, null, null);*/
                break;
            default:
                break;
        }
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
        mAdapterCallback.onItemClick(position, positionType);
    }

    @Override
    public void onItemLongCLick(int position, int positionType) {
        if (selectedType == positionType && selectedPos == position) {
            selectedPos = -1;
            selectedType = -1;
        } else {
            selectedPos = position;
            selectedType = positionType;
        }
        notifyDataSetChanged();
        mAdapterCallback.onItemLongCLick(position, positionType);
    }

    @Override
    public void onMenuRefresh() {
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

    public static class CharacterViewHolder extends RecyclerViewSubAdapter.ViewHolder {
        public View view;
        protected TextView vName;
        protected TextView vInfo;
        protected CardView cardViewTracker;
        protected View vIndicatorLine;
        protected ImageView ivIcon;
        public int type;
        private RelativeLayout clicker;
        private AdapterCallback mAdapterCallback;
        private Button undoButton;
        private TextView vInfoDeleted;
        private String identifier;


        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_enc_prep);


        public CharacterViewHolder(View v, AdapterCallback adapterCallback) {
            super(v);
            this.mAdapterCallback = adapterCallback;
            vIndicatorLine = (View) v.findViewById(R.id.indicator_line_enc_prep);
            vName = (TextView) v.findViewById(R.id.name_enc_prep);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_enc_prep);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon_enc_prep);
            vInfo = (TextView) v.findViewById(R.id.info_enc_prep);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_enc_prep);
            undoButton = (Button) v.findViewById(R.id.undo_enc_prep);
            vInfoDeleted = (TextView) v.findViewById(R.id.deleted_enc_prep);

            clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("Viewholder ", "longclicked");
                    mAdapterCallback.onItemLongCLick(getAdapterPosition(), type);
                    return true;
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