package com.pentapus.pentapusdmh.Fragments.Encounter;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.EncounterFragment;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.RippleForegroundListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Koni on 14/4/16.
 */
public class EncounterAdapter extends RecyclerViewCursorAdapter<EncounterAdapter.EncounterViewHolder> implements AdapterNavigationCallback {

    public static int selectedPos = -1;


    private AdapterNavigationCallback mAdapterCallback;
    List<String> itemsPendingRemoval;
    private Handler handler = new Handler(); // handler for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>();
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;
    Context mContext;


    /**
     * Constructor.
     *
     * @param context The Context the Adapter is displayed in.
     */
    public EncounterAdapter(Context context, AdapterNavigationCallback callback) {
        super(context);
        this.mContext = context;
        this.mAdapterCallback = callback;
        itemsPendingRemoval = new ArrayList<>();
        setHasStableIds(true);
        setupCursorAdapter(null, 0, R.layout.card_encounter, false);
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     */
    @Override
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EncounterViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent), mAdapterCallback);
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
    public void onBindViewHolder(EncounterViewHolder holder, int position) {
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
        final String identifier = mCursor.getString(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));

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
        mCursor.moveToPosition(position);
        if (itemsPendingRemoval.contains(identifier)) {
            itemsPendingRemoval.remove(identifier);
        }
        int encounterId = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + encounterId);
        if (position == 0) {
            notifyItemChanged(position);
        } else {
            notifyItemRemoved(position);
        }
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
    }


    public static void setSelectedPos(int selectedPos) {
        EncounterAdapter.selectedPos = selectedPos;
    }

    @Override
    public void onItemClick(int position) {
        mAdapterCallback.onItemClick(position);
    }

    @Override
    public void onItemLongCLick(int position) {
        mAdapterCallback.onItemLongCLick(position);
    }

    @Override
    public void onMenuRefresh() {
    }

    public Cursor getCursor() {
        return mCursorAdapter.getCursor();
    }


    public class EncounterViewHolder extends RecyclerViewCursorViewHolder {
        public View view;
        protected TextView vName, vInfo, vInfoDeleted;
        protected CardView cardViewTracker;
        private RelativeLayout clicker;
        private AdapterNavigationCallback mAdapterCallback;
        private Button undoButton;
        private String identifier;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_encounter);


        public EncounterViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);

            this.mAdapterCallback = adapterCallback;
            vName = (TextView) v.findViewById(R.id.name_encounter);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_encounter);
            vInfo = (TextView) v.findViewById(R.id.info_encounter);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_encounter);
            undoButton = (Button) v.findViewById(R.id.undo_encounter);
            vInfoDeleted = (TextView) v.findViewById(R.id.deleted_encounter);

            clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    EncounterAdapter.selectedPos = getAdapterPosition();
                    int test = getAdapterPosition();
                    mAdapterCallback.onItemLongCLick(getAdapterPosition());
                    return true;
                }
            });

            clicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onItemClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void bindCursor(Cursor cursor) {
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            vInfo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            identifier = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            itemView.setActivated(getAdapterPosition() == EncounterAdapter.selectedPos);

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
