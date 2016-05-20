package com.pentapus.pentapusdmh.Fragments.Spells;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.HelperClasses.RippleForegroundListener;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Koni on 14/4/16.
 */
public class PHBSpellAdapter extends RecyclerViewCursorAdapter<PHBSpellAdapter.MySpellViewHolder> implements AdapterNavigationCallback {

    public static int selectedPos = -1;


    private AdapterNavigationCallback mAdapterCallback;
    List<String> itemsPendingRemoval;
    Context mContext;


    /**
     * Constructor.
     *
     * @param context The Context the Adapter is displayed in.
     */
    public PHBSpellAdapter(Context context, AdapterNavigationCallback callback) {
        super(context);
        this.mContext = context;
        this.mAdapterCallback = callback;
        setHasStableIds(true);
        setupCursorAdapter(null, 0, R.layout.card_spell, false);
        FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return null;
            }
        };
    }

    /**
     * Returns the ViewHolder to use for this adapter.
     */
    @Override
    public MySpellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySpellViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent), mAdapterCallback);
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
    public void onBindViewHolder(MySpellViewHolder holder, int position) {
        // Move cursor to this position
        mCursorAdapter.getCursor().moveToPosition(position);

        // Set the ViewHolder
        setViewHolder(holder);

        // Bind this view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }


    public static void setSelectedPos(int selectedPos) {
        PHBSpellAdapter.selectedPos = selectedPos;
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


    public class MySpellViewHolder extends RecyclerViewCursorViewHolder {
        public View view;
        protected TextView vName;
        protected TextView vLevel, vSchool;
        protected CardView cardViewTracker;
        protected ImageView ivIcon;
        public int type;
        private RelativeLayout clicker;
        private AdapterNavigationCallback mAdapterCallback;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_spell);


        public MySpellViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);
            this.mAdapterCallback = adapterCallback;
            vName = (TextView) v.findViewById(R.id.name_spell);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_spell);
            vLevel = (TextView) v.findViewById(R.id.level_spell);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker_spell);
            vSchool = (TextView) v.findViewById(R.id.school_spell);

            //clicker.setOnTouchListener(rippleForegroundListener);

            clicker.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PHBSpellAdapter.selectedPos = getAdapterPosition();
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

            clicker.setOnTouchListener(rippleForegroundListener);
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            vLevel.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_LEVEL)));
            vSchool.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_SCHOOL)));
            itemView.setActivated(getAdapterPosition() == PHBSpellAdapter.selectedPos);
        }


    }


}
