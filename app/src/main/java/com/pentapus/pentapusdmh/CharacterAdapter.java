package com.pentapus.pentapusdmh;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Koni on 14/4/16.
 */
public class CharacterAdapter extends RecyclerViewCursorAdapter<CharacterAdapter.CharacterViewHolder> implements AdapterCallback{

    public static int selectedPos = -1;
    public static int selectedType = -1;


    private AdapterCallback mAdapterCallback;
    List<String> itemsPendingRemoval;
    private Handler handler = new Handler(); // handler for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>();
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;


    /**
     * Constructor.
     * @param context The Context the Adapter is displayed in.
     */
    public CharacterAdapter(Context context, AdapterCallback callback) {
        super(context);
        this.mAdapterCallback = callback;
        itemsPendingRemoval = new ArrayList<>();
        setHasStableIds(true);
        setupCursorAdapter(null, 0, R.layout.card_encounter_prep, false);
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     */
    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CharacterViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent), mAdapterCallback);
    }


    @Override
    public long getItemId(int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        return mCursorAdapter.getCursor().getInt(mCursorAdapter.getCursor().getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
    }

    /**
     * Moves the Cursor of the CursorAdapter to the appropriate position and binds the view for
     * that item.
     */
    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);

        // Set the ViewHolder
        setViewHolder(holder);

        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }


    public void pendingRemoval(final int position) {
        Cursor mCursor = mCursorAdapter.getCursor();
        mCursor.moveToPosition(position);
        final String identifier = mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)) + ":" + mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));

        if (!itemsPendingRemoval.contains(identifier)) {
            itemsPendingRemoval.add(identifier);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
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
        Cursor mCursor = mCursorAdapter.getCursor();
        mCursor.moveToPosition(position);
        return itemsPendingRemoval.contains(String.valueOf(mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID))));
    }


    public void remove(int position, String identifier) {
        Cursor mCursor = mCursorAdapter.getCursor();

        if (itemsPendingRemoval.contains(identifier)){
            itemsPendingRemoval.remove(identifier);
        }
        int characterId = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        int characterType = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
        switch(characterType){
            case 1:
                Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + characterId);
                notifyItemRemoved(position);
                mContext.getContentResolver().delete(uri, null, null);
                break;
            case 2:
                uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + characterId);
                notifyItemRemoved(position);
                mContext.getContentResolver().delete(uri, null, null);
                break;
        }

    }

    public static void setSelectedPos(int selectedPos) {
        CharacterAdapter.selectedPos = selectedPos;
    }
/*
    @Override
    public void onItemClick(int position) {
        mAdapterCallback.onItemClick(position);
    }

    @Override
    public void onItemLongCLick(int position) {
        mAdapterCallback.onItemLongCLick(position);
    }
*/
    public Cursor getCursor(){
        return mCursorAdapter.getCursor();
    }

    @Override
    public void onItemClick(int position, int positionType) {

    }

    @Override
    public void onItemLongCLick(int position, int positionType) {

    }


    public class CharacterViewHolder extends RecyclerViewCursorViewHolder {
        public View view;
        protected TextView vName, vInfo, vInfoDeleted;
        protected CardView cardViewTracker;
        protected View vIndicatorLine;
        protected ImageView ivIcon;
        public int type;
        private RelativeLayout clicker;
        private AdapterCallback mAdapterCallback;
        private Button undoButton;
        private String identifier;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_enc_prep);


        public CharacterViewHolder(View v, AdapterCallback adapterCallback) {
            super(v);

            this.mAdapterCallback = adapterCallback;
            vIndicatorLine = (View) v.findViewById(R.id.indicator_line_enc_prep);
            vInfo = (TextView) v.findViewById(R.id.info_enc_prep);
            vName = (TextView) v.findViewById(R.id.name_enc_prep);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_enc_prep);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_enc_prep);
            undoButton = (Button) v.findViewById(R.id.undo_enc_prep);
            vInfoDeleted = (TextView) v.findViewById(R.id.deleted_enc_prep);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon_enc_prep);

            clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("ViewHolder", "longclick");
                    selectedPos = getAdapterPosition();
                    mAdapterCallback.onItemLongCLick(getAdapterPosition(), type);
                    return true;
                }
            });

            clicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onItemClick(getAdapterPosition(), type);
                }
            });
        }

        @Override
        public void bindCursor(Cursor cursor) {
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            vInfo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            type = (cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
            identifier = type + ":" + cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            itemView.setActivated(type == selectedType && getAdapterPosition() == selectedPos);
            switch(type){
                case 1:
                    vIndicatorLine.setBackgroundColor(Color.parseColor("#F44336"));
                    break;
                case 2:
                    vIndicatorLine.setBackgroundColor(Color.parseColor("#3F51B5"));
                    break;
            }
            Uri iconUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));

            ivIcon.setImageURI(iconUri);


            if (itemsPendingRemoval.contains(String.valueOf(identifier))) {
                // we need to show the "undo" state of the row
                itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                vName.setVisibility(View.GONE);
                vInfo.setVisibility(View.GONE);
                vInfoDeleted.setVisibility(View.VISIBLE);
                undoButton.setVisibility(View.VISIBLE);
                undoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // user wants to undo the removal, let's cancel the pending task
                        Runnable pendingRemovalRunnable = pendingRunnables.get(String.valueOf(identifier));
                        pendingRunnables.remove(String.valueOf(identifier));
                        if (pendingRemovalRunnable != null)
                            handler.removeCallbacks(pendingRemovalRunnable);
                        itemsPendingRemoval.remove(String.valueOf(identifier));
                        // this will rebind the row in "normal" state
                        notifyItemChanged(getAdapterPosition());
                    }
                });
            } else {
                // we need to show the "normal" state
                itemView.setBackgroundColor(Color.WHITE);
                vName.setVisibility(View.VISIBLE);
                vInfo.setVisibility(View.VISIBLE);
                // viewHolder.titleTextView.setText(item);
                vInfoDeleted.setVisibility(View.GONE);
                undoButton.setVisibility(View.GONE);
                undoButton.setOnClickListener(null);
            }
        }



    }


}
