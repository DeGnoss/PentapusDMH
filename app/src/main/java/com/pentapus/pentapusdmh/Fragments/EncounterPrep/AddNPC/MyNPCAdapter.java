package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
public class MyNPCAdapter extends RecyclerViewCursorAdapter<MyNPCAdapter.MyNPCViewHolder> implements AdapterNavigationCallback {
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
    public MyNPCAdapter(Context context, AdapterNavigationCallback callback) {
        super(context);
        this.mContext = context;
        this.mAdapterCallback = callback;
        itemsPendingRemoval = new ArrayList<>();
        setHasStableIds(true);
        setupCursorAdapter(null, 0, R.layout.card_npc_add, false);
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
      /*  int uniquePosition = -1;
        if (position >= 0) {
            uniquePosition = (int) getItemId(position);
        }
        int oldType = NPCViewPagerDialogFragment.getSelectedType();
        int oldPos = NPCViewPagerDialogFragment.getSelectedPosAdapter();
        if (NPCViewPagerDialogFragment.getSelectedType() == 0 && uniquePosition == NPCViewPagerDialogFragment.getSelectedPosUnique()) {
            NPCViewPagerDialogFragment.setSelectedType(-1);
            NPCViewPagerDialogFragment.setSelectedPos(-1, -1);
            NPCViewPagerDialogFragment.addNpcToList(null);
            notifyItemChanged(position);
        } else if (position == -1) {
            NPCViewPagerDialogFragment.setSelectedType(-1);
            NPCViewPagerDialogFragment.setSelectedPos(-1, -1);
            NPCViewPagerDialogFragment.addNpcToList(null);
            if (oldType == 0) {
                notifyItemChanged(oldPos);
            }
        } else if (NPCViewPagerDialogFragment.getSelectedType() == 0) {
            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            NPCViewPagerDialogFragment.setSelectedPos(uniquePosition, position);
            NPCViewPagerDialogFragment.addNpcToList(Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId));
            if (oldType == 0) {
                notifyItemChanged(oldPos);
            }
            notifyItemChanged(position);
        } else {
            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            NPCViewPagerDialogFragment.setSelectedType(0);
            NPCViewPagerDialogFragment.setSelectedPos(uniquePosition, position);
            NPCViewPagerDialogFragment.addNpcToList(Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId));
            notifyItemChanged(position);
        }*/
    }

    @Override
    public void onItemClick(int position) {
        mAdapterCallback.onItemClick(position);
    }

    public void onItemAdd(int position){
        int uniquePosition = -1;
        if (position >= 0) {
            uniquePosition = (int) getItemId(position);
        }
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        NPCViewPagerDialogFragment.addNpcToList(Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId));
    }

    public void onItemRemove(int position){
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        NPCViewPagerDialogFragment.removeNpcFromList(Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId));
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
        private ImageButton buttonPlus, buttonMinus;
        private TextView tvNumber;
        private String identifier;
        private int numbers = 0;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_monster);


        public MyNPCViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);
            this.mAdapterCallback = adapterCallback;
            vIndicatorLine = v.findViewById(R.id.indicator_line_monster);
            vName = (TextView) v.findViewById(R.id.name_monster);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_monster);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon_monster);
            vInfo = (TextView) v.findViewById(R.id.info_monster);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_monster);
            buttonMinus = (ImageButton) v.findViewById(R.id.buttonMinus);
            buttonPlus = (ImageButton) v.findViewById(R.id.buttonPlus);
            tvNumber = (TextView) v.findViewById(R.id.tvNumber);
            tvNumber.setText(String.valueOf(numbers));


            //TODO: add touchlistener to detect when button is held down and start counting up/down faster

            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numbers--;
                    if(numbers<0)
                        numbers=0;
                    tvNumber.setText(String.valueOf(numbers));
                    onItemRemove(getAdapterPosition());
                }
            });

            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numbers++;
                    if(numbers>99)
                        numbers=99;
                    tvNumber.setText(String.valueOf(numbers));
                    onItemAdd(getAdapterPosition());
                }
            });

            clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    NPCViewPagerDialogFragment.setSelectedPos((int) getItemId(), getAdapterPosition());
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
            itemView.setActivated(getItemId() == NPCViewPagerDialogFragment.getSelectedPosUnique());
            identifier = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        }


    }


}
