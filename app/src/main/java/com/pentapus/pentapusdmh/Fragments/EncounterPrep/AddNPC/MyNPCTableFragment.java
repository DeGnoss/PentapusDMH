package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;


import android.content.AsyncQueryHandler;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.NotifyChange;
import com.pentapus.pentapusdmh.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class MyNPCTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {


    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";

    private int sessionId;
    private String sessionName;
    private RecyclerView myNPCRecyclerView;
    private ActionMode mActionMode;
    private MyNPCAdapter myNPCAdapter;
    private static int campaignId;

    public MyNPCTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MyNPCTableFragment newInstance() {
        MyNPCTableFragment fragment = new MyNPCTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        myNPCAdapter = new MyNPCAdapter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_monster_table, container, false);
        // insert a record

        myNPCRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myNPCRecyclerView.setLayoutManager(linearLayoutManager);
        myNPCRecyclerView.setHasFixedSize(true);
        myNPCRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        myNPCRecyclerView.setAdapter(myNPCAdapter);


        //setUpItemTouchHelper();
       // setUpAnimationDecoratorHelper();

        // Inflate the layout for this fragment
        return tableView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);

        } else {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

/*
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;


            private void init() {
                background = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorAccent));
                xMark = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getContext().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }


            //Drag & drop
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                MyNPCAdapter testAdapter = (MyNPCAdapter) recyclerView.getAdapter();
                if (testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int swipedAdapterPosition = viewHolder.getAdapterPosition();
                MyNPCAdapter adapter = (MyNPCAdapter) myNPCRecyclerView.getAdapter();
                adapter.pendingRemoval(swipedAdapterPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(myNPCRecyclerView);
    }


    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    /*
    private void setUpAnimationDecoratorHelper() {
        myNPCRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorAccent));
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }
    */


    public int getSessionId() {
        return sessionId;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.campaign_settings).setVisible(true);

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if (pasteUri == null) {
                pasteUri = Uri.parse(pasteString);
            }
            if (pasteUri != null) {
                if (DbContentProvider.NPC.equals(getContext().getContentResolver().getType(pasteUri))) {
                    menu.findItem(R.id.menu_paste).setVisible(true);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] selectionArgs = new String[]{String.valueOf(campaignId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_NPC, DataBaseHandler.PROJECTION_NPC_TEMPLATE, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myNPCAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myNPCAdapter.swapCursor(null);
    }


    @Override
    public void onItemClick(int position) {
        myNPCAdapter.statusClicked(position);
        EventBus.getDefault().post(new NotifyChange());
    }


    @Override
    public void onItemLongCLick(final int position) {
        NPCViewPagerDialogFragment.setSelectedType(0);
        NPCViewPagerDialogFragment.setHighlightedPos(position);
        int oldPos = NPCViewPagerDialogFragment.getSelectedPos();
        NPCViewPagerDialogFragment.setSelectedPos(position);
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                myNPCRecyclerView.getAdapter().notifyItemChanged(position);
                String title = "Selected: " + String.valueOf(position);
                mode.setTitle(title);
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        //TODO: ADD DIALOG TO MAKE SURE THE DELETE IS INTENTIONAL
                        Cursor cursor = myNPCAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
                        getContext().getContentResolver().delete(uri, null, null);
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard.hasPrimaryClip()) {
                            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
                            Uri pasteUri = itemPaste.getUri();
                            if (pasteUri == null) {
                                pasteUri = Uri.parse(String.valueOf(itemPaste.getText()));
                            }
                            if (pasteUri.equals(uri)) {
                                Uri newUri = Uri.parse("");
                                ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", newUri);
                                clipboard.setPrimaryClip(clip);
                                getActivity().invalidateOptionsMenu();
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.edit:
                        cursor = myNPCAdapter.getCursor();
                        cursor.moveToPosition(position);
                        npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MODE, true);
                        bundle.putInt(NPC_ID, npcId);
                        editNPC(bundle);
                        mode.finish();
                        return true;
                    case R.id.copy:
                        cursor = myNPCAdapter.getCursor();
                        cursor.moveToPosition(position);
                        npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
                        pasteNPC(uri);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                NPCViewPagerDialogFragment.setHighlightedPos(-1);
                NPCViewPagerDialogFragment.setSelectedPos(-1);
                myNPCRecyclerView.getAdapter().notifyItemChanged(position);
                mActionMode = null;
            }
        });
    }

    private void editNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new MyNPCEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment, "FE_MYNPC")
                .addToBackStack("FE_MYNPC")
                .commit();
    }


    private void pasteNPC(Uri pasteUri) {
        final AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContext().getContentResolver()) {

            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                if (cursor == null) {
                    // Some providers return null if an error occurs whereas others throw an exception
                } else if (cursor.getCount() < 1) {
                    // No matches found
                } else {
                    while (cursor.moveToNext()) {
                        ContentValues values = new ContentValues();
                        values.put(DataBaseHandler.KEY_NAME, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME)));
                        values.put(DataBaseHandler.KEY_INFO, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO)));
                        values.put(DataBaseHandler.KEY_INITIATIVEBONUS, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS)));
                        values.put(DataBaseHandler.KEY_MAXHP, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP)));
                        values.put(DataBaseHandler.KEY_AC, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC)));
                        values.put(DataBaseHandler.KEY_STRENGTH, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH)));
                        values.put(DataBaseHandler.KEY_DEXTERITY, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY)));
                        values.put(DataBaseHandler.KEY_CONSTITUTION, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION)));
                        values.put(DataBaseHandler.KEY_INTELLIGENCE, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE)));
                        values.put(DataBaseHandler.KEY_WISDOM, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM)));
                        values.put(DataBaseHandler.KEY_CHARISMA, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA)));
                        values.put(DataBaseHandler.KEY_ICON, cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
                        values.put(DataBaseHandler.KEY_TYPE, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_TYPE)));
                        values.put(DataBaseHandler.KEY_BELONGSTO, cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_BELONGSTO)));
                        startInsert(1, null, DbContentProvider.CONTENT_URI_NPC, values);
                    }
                    cursor.close();
                }
            }
        };

        queryHandler.startQuery(
                1, null,
                pasteUri,
                DataBaseHandler.PROJECTION_NPC_TEMPLATE,
                null,
                null,
                null
        );
    }

    @Subscribe
    public void onMessageEvent(NotifyChange event){
        myNPCAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }
}
