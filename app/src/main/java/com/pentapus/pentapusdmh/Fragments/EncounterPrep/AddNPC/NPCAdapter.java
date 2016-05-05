package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;

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
public class NPCAdapter extends RecyclerViewCursorAdapter<NPCAdapter.MyNPCViewHolder> implements AdapterNavigationCallback {
    private AdapterNavigationCallback mAdapterCallback;
    Context mContext;

    /**
     * Constructor.
     *
     * @param context The Context the Adapter is displayed in.
     */
    public NPCAdapter(Context context, AdapterNavigationCallback callback) {
        super(context);
        this.mContext = context;
        this.mAdapterCallback = callback;
        setHasStableIds(true);
        setupCursorAdapter(null, 0, R.layout.card_monster, false);
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     */
    @Override
    public MyNPCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyNPCViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent), mAdapterCallback);
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
    public void onBindViewHolder(MyNPCViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);

        // Set the ViewHolder
        setViewHolder(holder);

        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }


    public void statusClicked(int position) {
        int oldPos = NPCViewPagerDialogFragment.getSelectedPos();
        if (NPCViewPagerDialogFragment.getSelectedType() == 0 && position == NPCViewPagerDialogFragment.getSelectedPos()) {
            NPCViewPagerDialogFragment.setSelectedType(-1);
            NPCViewPagerDialogFragment.setSelectedPos(-1);
            NPCViewPagerDialogFragment.setNPCUri(null);
            notifyItemChanged(position);
        } else if (NPCViewPagerDialogFragment.getSelectedType() == 0) {
            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            NPCViewPagerDialogFragment.setSelectedPos(position);
            NPCViewPagerDialogFragment.setNPCUri(Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId));
            notifyItemChanged(oldPos);
            notifyItemChanged(position);
        } else {
            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            NPCViewPagerDialogFragment.setSelectedType(0);
            NPCViewPagerDialogFragment.setSelectedPos(position);
            NPCViewPagerDialogFragment.setNPCUri(Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId));
            notifyItemChanged(position);
        }
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


    public class MyNPCViewHolder extends RecyclerViewCursorViewHolder {
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


        public MyNPCViewHolder(View v, AdapterNavigationCallback adapterCallback) {
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
                    NPCViewPagerDialogFragment.setSelectedPos(getAdapterPosition());
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
            vIndicatorLine.setBackgroundColor(Color.parseColor("#4caf50"));
            ivIcon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON))));
            clicker.setOnTouchListener(rippleForegroundListener);
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            vInfo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
            itemView.setActivated(NPCViewPagerDialogFragment.getSelectedType() == 0 && getAdapterPosition() == NPCViewPagerDialogFragment.getSelectedPos());
            identifier = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        }
    }
}
