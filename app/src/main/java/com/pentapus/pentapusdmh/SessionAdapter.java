package com.pentapus.pentapusdmh;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;

/**
 * Created by Koni on 14/4/16.
 */
public class SessionAdapter extends RecyclerViewCursorAdapter<SessionAdapter.SessionViewHolder> implements AdapterNavigationCallback{

    /**
     * Column projection for the query to pull data from the database.
     */
    public static final String[] sessionColumns = new String[] {
            DataBaseHandler.TABLE_SESSION + "." + DataBaseHandler.KEY_ROWID,
            DataBaseHandler.KEY_NAME,
            DataBaseHandler.KEY_INFO
    };

    private AdapterNavigationCallback mAdapterCallback;


    /**
     * Constructor.
     * @param context The Context the Adapter is displayed in.
     */
    public SessionAdapter(Context context, AdapterNavigationCallback callback) {
        super(context);
        this.mAdapterCallback = callback;

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

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongCLick(int positione) {

    }


    public class SessionViewHolder extends RecyclerViewCursorViewHolder {
        public View view;
        protected TextView vName, vInfo, vInfoDeleted;
        protected CardView cardViewTracker;
        private RelativeLayout clicker;
        private AdapterNavigationCallback mAdapterCallback;
        private Button undoButton;

        private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.card_view_session);


        public SessionViewHolder(View v, AdapterNavigationCallback adapterCallback) {
            super(v);

            this.mAdapterCallback = adapterCallback;
            vName = (TextView) v.findViewById(R.id.nameEncounter);
            cardViewTracker = (CardView) v.findViewById(R.id.card_view_navigation);
            vInfo = (TextView) v.findViewById(R.id.info);
            clicker = (RelativeLayout) v.findViewById(R.id.clicker);
            undoButton = (Button) v.findViewById(R.id.undo_button);
            vInfoDeleted = (TextView) v.findViewById(R.id.info_deleted);

            clicker.setOnTouchListener(rippleForegroundListener);

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

        @Override
        public void bindCursor(Cursor cursor) {
            vName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
            vInfo.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
        }
    }


}
