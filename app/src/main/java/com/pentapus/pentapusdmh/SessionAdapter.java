package com.pentapus.pentapusdmh;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class SessionAdapter extends RecyclerViewCursorAdapter<SessionAdapter.SessionViewHolder> implements AdapterNavigationCallback{

    public static int selectedPos = -1;


    private AdapterNavigationCallback mAdapterCallback;
    List<String> itemsPendingRemoval;
    private Handler handler = new Handler(); // handler for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>();
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;


    /**
     * Constructor.
     * @param context The Context the Adapter is displayed in.
     */
    public SessionAdapter(Context context, AdapterNavigationCallback callback) {
        super(context);
        this.mAdapterCallback = callback;
        itemsPendingRemoval = new ArrayList<>();

        setupCursorAdapter(null, 0, R.layout.card_session, false);
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     */
    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SessionViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent), mAdapterCallback);
    }

    /**
     * Moves the Cursor of the CursorAdapter to the appropriate position and binds the view for
     * that item.
     */
    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
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

        Log.d("pendingRemoval ", String.valueOf(position));
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
        int sessionId = mCursor.getInt(mCursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + sessionId);
        mContext.getContentResolver().delete(uri, null, null);
        notifyItemRemoved(position);
    }

    public static int getSelectedPos() {
      //  return selectedPos;
        return 0;
    }

    public static void setSelectedPos(int selectedPos) {
      //  SessionAdapter.selectedPos = selectedPos;
    }

    @Override
    public void onItemClick(int position) {
        mAdapterCallback.onItemClick(position);
    }

    @Override
    public void onItemLongCLick(int position) {
        //notifyItemChanged(selectedPos);
      //  mAdapterCallback.onItemLongCLick(position);
    }

    public Cursor getCursor(){
        return mCursorAdapter.getCursor();
    }


    public class SessionViewHolder extends RecyclerViewCursorViewHolder {
        public View view;
        protected TextView vName, vInfo, vInfoDeleted;
        protected CardView cardViewTracker;
        private RelativeLayout clicker;
        private AdapterNavigationCallback mAdapterCallback;
        private Button undoButton;
        private Cursor mCursor;
        private String identifier;
        private int longClickSelected;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_session);


        public SessionViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);

            this.mAdapterCallback = adapterCallback;
            vName = (TextView) v.findViewById(R.id.nameEncounter);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_session);
            vInfo = (TextView) v.findViewById(R.id.info);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker);
            undoButton = (Button) v.findViewById(R.id.undo_button);
            vInfoDeleted = (TextView) v.findViewById(R.id.info_deleted);

            clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("ViewHolder", "longclick");
                    //selectedPos = getAdapterPosition();
                    //mAdapterCallback.onItemLongCLick(getAdapterPosition());
                    return true;
                }
            });

            clicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mAdapterCallback.onItemClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void bindCursor(Cursor cursor) {
            this.mCursor = cursor;
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            vInfo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            identifier = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            //itemView.setActivated(getAdapterPosition() == selectedPos);

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
