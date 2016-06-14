package com.pentapus.pentapusdmh.Fragments.Encounter;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
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
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.EncounterFragment;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;


public class EncounterTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SESSION_NAME = "sessionName";
    private static final String SESSION_ID = "sessionId";

    private static final String ENCOUNTER_NAME = "encounterName";
    private static final String ENCOUNTER_ID = "encounterId";

    private static final String MODE = "modeUpdate";


    private int sessionId;
    private String sessionName;

    private RecyclerView mEncounterRecyclerView;
    private ActionMode mActionMode;

    private EncounterAdapter mEncounterAdapter;
    private FloatingActionButton fab;

    public EncounterTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sessionName Parameter 1.
     * @param sessionId   Parameter 2.
     * @return A new instance of fragment SessionTableFragment.
     */
    public static EncounterTableFragment newInstance(String sessionName, int sessionId) {
        EncounterTableFragment fragment = new EncounterTableFragment();
        Bundle args = new Bundle();
        args.putString(SESSION_NAME, sessionName);
        args.putInt(SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (this.getArguments() != null) {
            sessionId = getArguments().getInt(SESSION_ID);
            sessionName = getArguments().getString(SESSION_NAME);
        }
        mEncounterAdapter = new EncounterAdapter(getContext(), this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_encounter_table, container, false);


        mEncounterRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEncounterRecyclerView.setLayoutManager(linearLayoutManager);
        mEncounterRecyclerView.setHasFixedSize(true);
        mEncounterRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));
        mEncounterRecyclerView.setAdapter(mEncounterAdapter);


        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        // Inflate the layout for this fragment
        return tableView;
    }

    public void onFabClick() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(MODE, false);
        bundle.putInt(SESSION_ID, sessionId);
        addEncounter(bundle);
    }




    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(sessionName + " Encounters");
        ((MainActivity)getActivity()).setFabVisibility(true);
        ((MainActivity)getActivity()).setFabIcon(true);
        ((MainActivity)getActivity()).enableNavigationDrawer();
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);

        } else {
            getLoaderManager().restartLoader(0, null, this);
        }
    }


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
                EncounterAdapter testAdapter = (EncounterAdapter) recyclerView.getAdapter();
                if (testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int swipedAdapterPosition = viewHolder.getAdapterPosition();
                EncounterAdapter adapter = (EncounterAdapter) mEncounterRecyclerView.getAdapter();
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
        itemTouchHelper.attachToRecyclerView(mEncounterRecyclerView);
    }


    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mEncounterRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

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


    private void addEncounter(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerFrame, fragment, "FE_ENCOUNTER")
                .addToBackStack("FE_ENCOUNTER")
                .commit();
    }

    private void editEncounter(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContainerFrame, fragment, "FE_ENCOUNTER")
                .addToBackStack("FE_ENCOUNTER")
                .commit();
    }

    private void loadNPC(Bundle bundle, int encounterId, String encounterName) {
        SharedPrefsHelper.saveEncounter(getContext(), encounterId, encounterName);
        Fragment fragment;
        fragment = new EncounterFragment();
        fragment.setArguments(bundle);
        //fragment.setEnterTransition(new Slide(Gravity.START));
        //setExitTransition(new Slide(Gravity.START));
        //fragment.setReturnTransition(new Slide(Gravity.END));
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        transaction.replace(R.id.ContainerFrame, fragment, "F_ENCOUNTER")
                .addToBackStack("F_ENCOUNTER")
                .commit();
    }

    public int getSessionId() {
        return sessionId;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if (pasteUri == null) {
                pasteUri = Uri.parse(pasteString);
            }
            if (pasteUri != null) {
                if (DbContentProvider.ENCOUNTER.equals(getContext().getContentResolver().getType(pasteUri))) {
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
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };
        String[] selectionArgs = new String[]{String.valueOf(sessionId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        return new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_ENCOUNTER, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mEncounterAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEncounterAdapter.swapCursor(null);
    }


    @Override
    public void onItemClick(int position) {
        if(mActionMode!= null){
            mActionMode.finish();
        }
        Cursor cursor = mEncounterAdapter.getCursor();
        cursor.moveToPosition(position);
        int encounterId =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        String encounterName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));

        Bundle bundle = new Bundle();
        bundle.putInt(ENCOUNTER_ID, encounterId);
        bundle.putString(ENCOUNTER_NAME, encounterName);
        loadNPC(bundle, encounterId, encounterName);

    }

    @Override
    public void onItemLongCLick(final int position) {
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mEncounterRecyclerView.getAdapter().notifyItemChanged(position);
                String title = "Selected: " + String.valueOf(position);
                mode.setTitle(title);
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
//                fab.setVisibility(View.GONE);
                ((MainActivity) getActivity()).setFabVisibility(false);
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
                        Cursor cursor = mEncounterAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int npcId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + npcId);
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
                        cursor = mEncounterAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int encounterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MODE, true);
                        bundle.putInt(ENCOUNTER_ID, encounterId);
                        bundle.putInt(SESSION_ID, sessionId);
                        editEncounter(bundle);
                        mode.finish();
                        return true;
                    case R.id.copy:
                        cursor = mEncounterAdapter.getCursor();
                        cursor.moveToPosition(position);
                        encounterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + encounterId);
                        clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", uri);
                        clipboard.setPrimaryClip(clip);
                        getActivity().invalidateOptionsMenu();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                EncounterAdapter.setSelectedPos(-1);
                mEncounterRecyclerView.getAdapter().notifyItemChanged(position);
                //    fab.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).setFabVisibility(true);
                mActionMode = null;
            }
        });
    }

    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }
}
