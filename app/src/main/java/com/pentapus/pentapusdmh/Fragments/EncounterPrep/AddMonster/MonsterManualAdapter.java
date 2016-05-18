package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.RippleForegroundListener;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Koni on 14/4/16.
 */
public class MonsterManualAdapter extends RecyclerViewCursorAdapter<MonsterManualAdapter.MyMonsterViewHolder> implements AdapterNavigationCallback {

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
    public MonsterManualAdapter(Context context, AdapterNavigationCallback callback) {
        super(context);
        this.mContext = context;
        this.mAdapterCallback = callback;
        itemsPendingRemoval = new ArrayList<>();
        setHasStableIds(true);
        setupCursorAdapter(null, 0, R.layout.card_monster, false);
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     */
    @Override
    public MyMonsterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMonsterViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent), mAdapterCallback);
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
    public void onBindViewHolder(MyMonsterViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);

        // Set the ViewHolder
        setViewHolder(holder);

        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }


    public static void setSelectedPos(int selectedPos) {
        MonsterManualAdapter.selectedPos = selectedPos;
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


    public class MyMonsterViewHolder extends RecyclerViewCursorViewHolder {
        public View view;
        protected TextView vName;
        protected TextView vInfo;
        protected CardView cardViewTracker;
        protected View vIndicatorLine;
        protected ImageView ivIcon;
        public int type;
        private RelativeLayout clicker;
        private AdapterNavigationCallback mAdapterCallback;
        private Button undoButton;
        private TextView vInfoDeleted;
        private String identifier;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_monster);


        public MyMonsterViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);
            this.mAdapterCallback = adapterCallback;
            vIndicatorLine = (View) v.findViewById(R.id.indicator_line_monster);
            vName = (TextView) v.findViewById(R.id.name_monster);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_monster);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon_monster);
            vInfo = (TextView) v.findViewById(R.id.info_monster);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_monster);

            clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MonsterManualAdapter.selectedPos = getAdapterPosition();
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

            type = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE));
            vIndicatorLine.setBackgroundColor(Color.parseColor("#F44336"));
            ivIcon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON))));
            //ivIcon.setImageURI(Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight"));
            clicker.setOnTouchListener(rippleForegroundListener);
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            vInfo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            itemView.setActivated(getAdapterPosition() == MonsterManualAdapter.selectedPos);
            identifier = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));


            /*
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
            } */
        }


    }


}
