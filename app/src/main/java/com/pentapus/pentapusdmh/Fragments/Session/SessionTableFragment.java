package com.pentapus.pentapusdmh.Fragments.Session;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.transition.ChangeBounds;
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
import com.pentapus.pentapusdmh.FabTransition;
import com.pentapus.pentapusdmh.Fragments.PC.PcTableFragment;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.Fragments.Campaign.CampaignTableFragment;
import com.pentapus.pentapusdmh.Fragments.Encounter.EncounterTableFragment;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;


public class SessionTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterNavigationCallback {


    private static final String MODE = "modeUpdate";
    private static final String SESSION_ID = "sessionId";
    private static final String SESSION_NAME = "sessionName";


    FloatingActionButton fab;
    private int campaignId;
    private SessionAdapter mSessionAdapter;
    private RecyclerView mSessionRecyclerView;
    private ActionMode mActionMode;


    public SessionTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SessionTableFragment.
     */
    public static SessionTableFragment newInstance() {
        return new SessionTableFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionAdapter = new SessionAdapter(getContext(), this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setFabVisibility(true);
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(SharedPrefsHelper.loadCampaignName(getContext()) + " Sessions");
        final View tableView = inflater.inflate(R.layout.fragment_session_table, container, false);
        Slide slide = (Slide) TransitionInflater.from(getContext()).inflateTransition(R.transition.slide);
        getActivity().getWindow().setExitTransition(slide);
        if (campaignId <= 0) {
            new AlertDialog.Builder(getContext()).setTitle("No Campaign Found")
                    .setCancelable(false)
                    .setMessage("Create a campaign first.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CampaignTableFragment ftable = new CampaignTableFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.FrameTop, ftable, "FT_CAMPAIGN")
                                    .addToBackStack("FT_CAMPAIGN")
                                    .commit();
                        }
                    })
                    .show();
        } else {
            //displayListView(tableView);

            mSessionRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewSession);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mSessionRecyclerView.setLayoutManager(linearLayoutManager);
            mSessionRecyclerView.setHasFixedSize(true);
            mSessionRecyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity()));
            mSessionRecyclerView.setAdapter(mSessionAdapter);

            // Inflate the layout for this fragment


            setUpItemTouchHelper();
            setUpAnimationDecoratorHelper();

        }
        return tableView;
    }

    public void onFabClick(){
        Bundle bundle = new Bundle();
        bundle.putBoolean(MODE, false);
        addSession(bundle);
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
                SessionAdapter testAdapter = (SessionAdapter) recyclerView.getAdapter();
                if (testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int swipedAdapterPosition = viewHolder.getAdapterPosition();
                SessionAdapter adapter = (SessionAdapter) mSessionRecyclerView.getAdapter();
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
        itemTouchHelper.attachToRecyclerView(mSessionRecyclerView);
    }


    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mSessionRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

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

    private void addSession(Bundle bundle) {
        Fragment fragment;
        fragment = new SessionEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_SESSION")
                .addToBackStack("FE_SESSION")
                .commit();
    }

    private void editSession(Bundle bundle) {
        Fragment fragment;
        fragment = new SessionEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_SESSION")
                .addToBackStack("FE_SESSION")
                .commit();
    }


    private void loadEncounters(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterTableFragment();
        fragment.setArguments(bundle);
        fragment.setEnterTransition(new Slide(Gravity.RIGHT));
        setExitTransition(new Slide(Gravity.LEFT));
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FT_ENCOUNTER")
                .addToBackStack("FT_ENCOUNTER")
                .commit();
    }

    public int getCampaignId() {
        return campaignId;
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
                if (DbContentProvider.SESSION.equals(getContext().getContentResolver().getType(pasteUri))) {
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
        String[] selectionArgs = new String[]{String.valueOf(campaignId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_SESSION, projection, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSessionAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSessionAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(int position) {
        Cursor cursor = mSessionAdapter.getCursor();
        cursor.moveToPosition(position);
        int sessionId =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
        String sessionName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));

        Bundle bundle = new Bundle();
        bundle.putInt(SESSION_ID, sessionId);
        bundle.putString(SESSION_NAME, sessionName);
        loadEncounters(bundle);
    }

    @Override
    public void onItemLongCLick(final int position) {
        if (mActionMode != null) {
            return;
        }
        mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //((SessionAdapter) mSessionRecyclerView.getAdapter()).toggleSelection(position);
                mSessionRecyclerView.getAdapter().notifyItemChanged(position);
                String title = "Selected: " + String.valueOf(position);
                mode.setTitle(title);
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                ((MainActivity)getActivity()).setFabVisibility(false);
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
                        Cursor cursor = mSessionAdapter.getCursor();
                        cursor.moveToPosition(position);
                        int sessionId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + sessionId);
                        getContext().getContentResolver().delete(uri, null, null);
                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard.hasPrimaryClip()) {
                            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
                            Uri pasteUri = itemPaste.getUri();
                            if (pasteUri == null) {
                                pasteUri = Uri.parse(String.valueOf(itemPaste.getText()));
                            }
                            if (pasteUri != null) {

                                if (pasteUri.equals(uri)) {
                                    Uri newUri = Uri.parse("");
                                    ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", newUri);
                                    clipboard.setPrimaryClip(clip);
                                    getActivity().invalidateOptionsMenu();
                                }
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.edit:
                        cursor = mSessionAdapter.getCursor();
                        cursor.moveToPosition(position);
                        sessionId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MODE, true);
                        bundle.putInt(SESSION_ID, sessionId);
                        editSession(bundle);
                        mode.finish();
                        mSessionRecyclerView.getAdapter().notifyItemChanged(position);
                        return true;
                    case R.id.copy:
                        cursor = mSessionAdapter.getCursor();
                        cursor.moveToPosition(position);
                        sessionId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                        uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + sessionId);
                        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", uri);
                        clipboard.setPrimaryClip(clip);
                        getActivity().invalidateOptionsMenu();
                        mode.finish();
                        mSessionRecyclerView.getAdapter().notifyItemChanged(position);
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                SessionAdapter.setSelectedPos(-1);
                mSessionRecyclerView.getAdapter().notifyItemChanged(position);
                ((MainActivity)getActivity()).setFabVisibility(true);
                mActionMode = null;
            }
        });
        //TODO: set title according to selection
    }

    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }

    public FloatingActionButton getFab(){
        return fab;
    }

    public void loadNPCTable(Bundle bundle){
        Fragment fragment = null;
        Class fragmentClass = PcTableFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragment.setEnterTransition(new Slide(Gravity.TOP));
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.FrameTop);
        fragmentManager.beginTransaction()
                .add(R.id.FrameTop, fragment, "FT_PC")
                .addToBackStack("NAV_F")
                .commit();
    }
}
