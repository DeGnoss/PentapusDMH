package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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

import java.util.List;

/**
 * Created by Koni on 14/4/16.
 */
public class MonsterManualAdapter extends RecyclerViewCursorAdapter<MonsterManualAdapter.MyMonsterViewHolder> implements AdapterNavigationCallback {

    public int selectedPos;
    private AdapterNavigationCallback mAdapterCallback;
    List<String> itemsPendingRemoval;
    Context mContext;
    boolean isNavMode;


    /**
     * Constructor.
     *
     * @param context The Context the Adapter is displayed in.
     */
    public MonsterManualAdapter(Context context, AdapterNavigationCallback callback, boolean isNavMode) {
        super(context);
        this.mContext = context;
        this.mAdapterCallback = callback;
        this.isNavMode = isNavMode;
        setHasStableIds(true);
        setupCursorAdapter(null, 0, R.layout.card_npc_add, false);
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
        mCursorAdapter.getCursor().moveToPosition(position);
        setViewHolder(holder);
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public void onItemClick(int position) {
        mAdapterCallback.onItemClick(position);
    }

    public void onItemAdd(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        MonsterViewPagerDialogFragment.addMonsterToList(Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + npcId));
    }

    public void onItemRemove(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        MonsterViewPagerDialogFragment.removeMonsterFromList(Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + npcId));
    }

    @Override
    public void onItemLongCLick(int position) {
        mAdapterCallback.onItemLongCLick(position);
    }

    public void setSelectedPos(int position) {
        this.selectedPos = position;
        notifyItemChanged(position);
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    @Override
    public void onMenuRefresh() {
        mAdapterCallback.onMenuRefresh();
    }

    public Cursor getCursor() {
        return mCursorAdapter.getCursor();
    }


    public class MyMonsterViewHolder extends RecyclerViewCursorViewHolder {
        public View view;
        protected TextView vName;
        protected CardView cardViewTracker;
        protected View vIndicatorLine;
        protected ImageView ivIcon;
        public int type;
        private RelativeLayout clicker;
        private AdapterNavigationCallback mAdapterCallback;
        private ImageButton buttonPlus, buttonMinus;
        private TextView tvNumber;
        private int numbers = 0;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_monster);


        public MyMonsterViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);
            this.mAdapterCallback = adapterCallback;
            vIndicatorLine = v.findViewById(R.id.indicator_line_monster);
            vName = (TextView) v.findViewById(R.id.name_monster);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_monster);
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon_monster);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_monster);
            buttonMinus = (ImageButton) v.findViewById(R.id.buttonMinus);
            buttonPlus = (ImageButton) v.findViewById(R.id.buttonPlus);
            tvNumber = (TextView) v.findViewById(R.id.tvNumber);
            if (isNavMode) {
                buttonMinus.setVisibility(View.GONE);
                buttonPlus.setVisibility(View.GONE);
                tvNumber.setVisibility(View.GONE);
            } else {
                tvNumber.setText(String.valueOf(numbers));

                buttonMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numbers--;
                        if (numbers < 0)
                            numbers = 0;
                        tvNumber.setText(String.valueOf(numbers));
                        onItemRemove(getAdapterPosition());
                        onMenuRefresh();
                    }
                });

                buttonPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numbers++;
                        if (numbers > 99)
                            numbers = 99;
                        tvNumber.setText(String.valueOf(numbers));
                        onItemAdd(getAdapterPosition());
                        onMenuRefresh();
                    }
                });
            }

            clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
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
            String uriTemp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON));
            if(uriTemp != null && !uriTemp.isEmpty()){
                ivIcon.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON))));
            }else{
                ivIcon.setImageURI(Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight"));
            }
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
        }


    }


}
