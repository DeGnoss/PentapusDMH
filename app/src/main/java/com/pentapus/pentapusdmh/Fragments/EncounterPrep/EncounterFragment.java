package com.pentapus.pentapusdmh.Fragments.EncounterPrep;


import android.app.Dialog;
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
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.pentapus.pentapusdmh.AdapterCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.MonsterViewPagerDialogFragment;
import com.pentapus.pentapusdmh.HelperClasses.DividerItemDecoration;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class EncounterFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterCallback {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ENCOUNTER_ID = "encounterId";
    private static final String ENCOUNTER_NAME = "encounterName";

    private static final String MODE = "modeUpdate";
    private static final String CHARACTER_ID = "characterId";
    private static final String MONSTER_ID = "monsterId";
    private static final String NPC_ID = "npcId";

/*
    @BindView(R.id.loc_item_detail_floatmenu_btn_npc) FloatingActionButton btnNpc;
    @BindView(R.id.loc_item_detail_floatmenu_btn_monster) FloatingActionButton btnMonster;
    @BindView(R.id.loc_item_detail_floatmenu_btn_close) FloatingActionButton btnClose;
    @BindView(R.id.fabEncounter) FloatingActionButton fab;*/

    private ActionMode mActionMode;

    private int encounterId;
    private String encounterName;

    private Dialog dialog;


    private CursorRecyclerViewAdapter dataAdapterNPC, dataAdapterPC;
    private static int campaignId;
    RecyclerViewMergeAdapter<CursorRecyclerViewAdapter> mergeAdapter;
    private RecyclerView mRecyclerView;


    public EncounterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param encounterName Parameter 1.
     * @param encounterId   Parameter 2.
     * @return A new instance of fragment SessionTableFragment.
     */
    public static EncounterFragment newInstance(String encounterName, int encounterId) {
        EncounterFragment fragment = new EncounterFragment();
        Bundle args = new Bundle();
        args.putString(ENCOUNTER_NAME, encounterName);
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        if (getArguments() != null) {
            encounterId = getArguments().getInt("encounterId");
            encounterName = getArguments().getString("encounterName");
        }
        setHasOptionsMenu(true);
        dataAdapterNPC = new CursorRecyclerViewAdapter(getContext(), null, this);
        dataAdapterPC = new CursorRecyclerViewAdapter(getContext(), null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_encounter, container, false);
        //displayListView(tableView);
        FloatingActionButton fab = (FloatingActionButton) tableView.findViewById(R.id.fabEncounter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFloatingMenu();
            }
        });

        //getLoaderManager().initLoader(0, null, this);
        //getLoaderManager().initLoader(1, null, this);
        mRecyclerView = (RecyclerView) tableView.findViewById(R.id.recyclerViewEncounter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));

        mergeAdapter = new RecyclerViewMergeAdapter<>();
        mergeAdapter.addAdapter(0, dataAdapterNPC);
        mergeAdapter.addAdapter(1, dataAdapterPC);

        mRecyclerView.setAdapter(mergeAdapter);


        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        return tableView;

    }


    //@OnClick(R.id.fabEncounter)
    public void showFloatingMenu() {

        dialog = new Dialog(getContext());
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.floating_menu);
        // set the background partial transparent
        ColorDrawable c = new ColorDrawable(Color.BLACK);
        c.setAlpha(180);
        dialog.getWindow().setBackgroundDrawable(c);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        param.width = WindowManager.LayoutParams.MATCH_PARENT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(true);



        View closeButton = (View) dialog.findViewById(R.id.loc_item_detail_floatmenu_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss the dialog
                dialog.dismiss();
            }
        });



        View npcButton = (View) dialog.findViewById(R.id.loc_item_detail_floatmenu_npc);
        npcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNpc(v);
                dialog.dismiss();
            }
        });


        View addMonsterButton = (View) dialog.findViewById(R.id.loc_item_detail_floatmenu_monster);
        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViewPager();
                //onAddMonster(v);
                dialog.dismiss();
            }
        });

        // it show the dialog box
        dialog.show();
    }


   // @OnClick(R.id.loc_item_detail_floatmenu_npc)
    public void onAddNpc(View v) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(MODE, false);
        bundle.putInt(ENCOUNTER_ID, encounterId);
        addNPC(bundle);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(encounterName + " Preparation");
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            getLoaderManager().restartLoader(0, null, this);
        }
        if (getLoaderManager().getLoader(1) == null) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            getLoaderManager().restartLoader(1, null, this);
        }
        if (getLoaderManager().getLoader(2) == null) {
            getLoaderManager().initLoader(2, null, this);
        } else {
            getLoaderManager().restartLoader(2, null, this);
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
                CursorRecyclerViewAdapter.CharacterViewHolder characterViewHolder = (CursorRecyclerViewAdapter.CharacterViewHolder) viewHolder;
                if (characterViewHolder.type == 0 || characterViewHolder.type == 1) {
                    int position = characterViewHolder.getSubAdapterPosition();
                    CursorRecyclerViewAdapter testAdapter = (CursorRecyclerViewAdapter) ((RecyclerViewMergeAdapter) recyclerView.getAdapter()).getSubAdapter(0);
                    if (testAdapter.isPendingRemoval(position)) {
                        return 0;
                    }
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else {
                    return 0;
                }

            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                CursorRecyclerViewAdapter.CharacterViewHolder characterViewHolder = (CursorRecyclerViewAdapter.CharacterViewHolder) viewHolder;
                if (characterViewHolder.type == 0 || characterViewHolder.type == 1) {
                    int swipedAdapterPosition = characterViewHolder.getSubAdapterPosition();
                    CursorRecyclerViewAdapter adapter = (CursorRecyclerViewAdapter) ((RecyclerViewMergeAdapter) mRecyclerView.getAdapter()).getSubAdapter(0);
                    int notifyPosition = characterViewHolder.getLayoutPosition();
                    adapter.pendingRemoval(swipedAdapterPosition, notifyPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                CursorRecyclerViewAdapter.CharacterViewHolder characterViewHolder = (CursorRecyclerViewAdapter.CharacterViewHolder) viewHolder;

                View itemView = characterViewHolder.itemView;

                if (characterViewHolder.getSubAdapterPosition() == -1) {
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
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

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

    public void showViewPager() {
        Bundle bundle = new Bundle();
        bundle.putInt(ENCOUNTER_ID, encounterId);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        MonsterViewPagerDialogFragment newFragment = new MonsterViewPagerDialogFragment();
        //newFragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag("FE_NPC"), RESULT_CHOOSE_IMG);
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.replace(android.R.id.content, newFragment, "F_MONSTER_PAGER")
                .addToBackStack("F_MONSTER_PAGER").commit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            // Set title
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(encounterName + " Preparation");

        }
    }

    private void addNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new NPCEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_NPC")
                .addToBackStack("FE_NPC")
                .commit();
    }

    private void editNpc(Bundle bundle) {
        Fragment fragment;
        fragment = new NPCEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_NPC")
                .addToBackStack("FE_NPC")
                .commit();
    }

    private void editMonster(Bundle bundle) {
        Fragment fragment;
        fragment = new MonsterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_MONSTER")
                .addToBackStack("FE_MONSTER")
                .commit();
    }

    public int getEncounterId() {
        return encounterId;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.play_mode).setVisible(true);

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData.Item itemPaste = clipboard.getPrimaryClip().getItemAt(0);
            Uri pasteUri = itemPaste.getUri();
            String pasteString = String.valueOf(itemPaste.getText());
            if (pasteUri == null) {
                pasteUri = Uri.parse(pasteString);
            }
            if (pasteUri != null) {
                if (DbContentProvider.ENCOUNTERPREP.equals(getContext().getContentResolver().getType(pasteUri))) {
                    menu.findItem(R.id.menu_paste).setVisible(true);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //FIXME: copy into db and load or only load?

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_TYPE,
                DataBaseHandler.KEY_ICON
        };
        switch (id) {
            case 0:
                String[] selectionArgs = new String[]{String.valueOf(encounterId)};
                String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
                return new CursorLoader(this.getContext(),
                        DbContentProvider.CONTENT_URI_ENCOUNTERPREP, projection, selection, selectionArgs, null);
            case 1:
                selectionArgs = new String[]{String.valueOf(campaignId)};
                selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
                return new CursorLoader(this.getContext(),
                        DbContentProvider.CONTENT_URI_PC, projection, selection, selectionArgs, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                dataAdapterNPC.swapCursor(data);
                break;
            case 1:
                dataAdapterPC.swapCursor(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case 0:
                dataAdapterNPC.swapCursor(null);
                break;
            case 1:
                dataAdapterPC.swapCursor(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position, int positionType) {
        Log.d("EncounterFragment ", "itemClicked");

    }

    @Override
    public void onItemLongCLick(final int position, final int positionType) {
        Log.d("EncounterFragment ", "itemLongClicked");
        if (positionType == 0 || positionType == 1) {
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    //mRecyclerView.getAdapter().notifyItemChanged(position);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
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

                            switch(positionType){
                                case 0:
                                    Cursor cursor = dataAdapterNPC.getCursor();
                                    cursor.moveToPosition(position);
                                    int characterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    cursor.close();
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTERPREP + "/" + characterId);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    mergeAdapter.getSubAdapter(0).notifyItemRemoved(position);
                                  /*  if (clipboard.hasPrimaryClip()) {
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
                                    }*/
                                    break;
                                case 1:
                                    cursor = dataAdapterNPC.getCursor();
                                    cursor.moveToPosition(position);
                                    characterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    cursor.close();
                                    uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTERPREP + "/" + characterId);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    mergeAdapter.getSubAdapter(0).notifyItemRemoved(position);
                                 /*   clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
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
                                    }*/
                                    break;
                                default:
                                    break;
                            }
                            mode.finish();
                            return true;
                        case R.id.edit:
                            switch(positionType){
                                case 0:
                                    Cursor cursor = dataAdapterNPC.getCursor();
                                    cursor.moveToPosition(position);
                                    int characterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    cursor.close();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt(MONSTER_ID, characterId);
                                    bundle.putInt(ENCOUNTER_ID, encounterId);
                                    editMonster(bundle);
                                    break;
                                case 1:
                                    cursor = dataAdapterNPC.getCursor();
                                    cursor.moveToPosition(position);
                                    characterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    cursor.close();
                                    bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt(NPC_ID, characterId);
                                    bundle.putInt(ENCOUNTER_ID, encounterId);
                                    editNpc(bundle);
                                    break;
                                case 2:
                                    break;
                            }
                            mode.finish();
                            return true;
                        case R.id.copy:
                            if (positionType == 0) {
                                Cursor cursor = dataAdapterNPC.getCursor();
                                cursor.moveToPosition(position);
                                int characterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTERPREP + "/" + characterId);
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", uri);
                                clipboard.setPrimaryClip(clip);
                                getActivity().invalidateOptionsMenu();
                            }else if (positionType == 1) {
                                Cursor cursor = dataAdapterNPC.getCursor();
                                cursor.moveToPosition(position);
                                int characterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTERPREP + "/" + characterId);
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newUri(getContext().getContentResolver(), "URI", uri);
                                clipboard.setPrimaryClip(clip);
                                getActivity().invalidateOptionsMenu();
                            }
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    CursorRecyclerViewAdapter.selectedPos = -1;
                    //TODO: make it work with notifyItemChanged()
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    int test = 0;
                }
            });
        } else if(positionType == 2) {
            Uri myFile;
            String myName ="";
            Cursor cursor = dataAdapterPC.getCursor();
            cursor.moveToPosition(position);
            int characterId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + characterId);
            cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_PC, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int disabled = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DISABLED));
                myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
                String myInitiative = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS));
                String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
                String myMaxHp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
                String myAc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
                myFile = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
                ContentValues values = new ContentValues();
                values.put(DataBaseHandler.KEY_NAME, myName);
                values.put(DataBaseHandler.KEY_INFO, myInfo);
                values.put(DataBaseHandler.KEY_INITIATIVEBONUS, myInitiative);
                values.put(DataBaseHandler.KEY_MAXHP, myMaxHp);
                values.put(DataBaseHandler.KEY_AC, myAc);
                values.put(DataBaseHandler.KEY_TYPE, DataBaseHandler.TYPE_PC);
                values.put(DataBaseHandler.KEY_ICON, String.valueOf(myFile));
                values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);

                if(disabled == 0){
                    values.put(DataBaseHandler.KEY_DISABLED, 1);
                    Toast.makeText(getContext(), myName + " disabled." , Toast.LENGTH_SHORT).show();
                }else{
                    values.put(DataBaseHandler.KEY_DISABLED, 0);
                    Toast.makeText(getContext(), myName + " enabled." , Toast.LENGTH_SHORT).show();
                }
                uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + characterId);
                getContext().getContentResolver().update(uri, values, null, null);
            }
            assert cursor != null;
            cursor.close();
            CursorRecyclerViewAdapter.selectedPos = -1;
            mergeAdapter.notifyDataSetChanged();

        } else{
            Toast.makeText(getContext(), "Function not yet implemented.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMenuRefresh() {
        getActivity().invalidateOptionsMenu();
    }
}
