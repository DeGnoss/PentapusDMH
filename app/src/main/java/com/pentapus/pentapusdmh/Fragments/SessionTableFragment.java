package com.pentapus.pentapusdmh.Fragments;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;


public class SessionTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final String MODE = "modeUpdate";
    private static final String SESSION_ID = "sessionId";
    private static final String SESSION_NAME = "sessionName";


    final CharSequence[] items = {"Edit", "Delete", "Copy"};
    FloatingActionButton fab;
    private SimpleCursorAdapter dataAdapterSessions;
    private int campaignId;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(SharedPrefsHelper.loadCampaignName(getContext()) + " Sessions");
        final View tableView = inflater.inflate(R.layout.fragment_session_table, container, false);
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
            displayListView(tableView);
            // Inflate the layout for this fragment
            fab = (FloatingActionButton) tableView.findViewById(R.id.fabSession);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(MODE, false);
                    addSession(bundle);
                }
            });
        }
        return tableView;
    }

    private void displayListView(View view) {
        String[] columns = new String[]{
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };

        int[] to = new int[]{
                R.id.name,
                R.id.info,
        };

        dataAdapterSessions = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.session_info,
                null,
                columns,
                to,
                0);

        final ListView listView = (ListView) view.findViewById(R.id.listViewSessions);
        listView.setAdapter(dataAdapterSessions);
        //Ensures a loader is initialized and active.
        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            getLoaderManager().restartLoader(0, null, this);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                int sessionId =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                String sessionName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));

                Bundle bundle = new Bundle();
                bundle.putInt(SESSION_ID, sessionId);
                bundle.putString(SESSION_NAME, sessionName);
                loadEncounters(bundle);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
                new AlertDialog.Builder(getContext()).setTitle(title)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                    int sessionId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt(SESSION_ID, sessionId);
                                    editSession(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + id);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    dialog.dismiss();
                                }else if (item == 2) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + id);
                                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                    clipboard.setPrimaryClip(ClipData.newUri(getContext().getContentResolver(), "SESSION", uri));
                                    dialog.dismiss();
                                }
                                else {
                                    dialog.dismiss();
                                }
                            }
                        }).show();
                return true;
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
        menu.findItem(R.id.play_mode).setVisible(false);
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
        dataAdapterSessions.swapCursor(data);
        dataAdapterSessions.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapterSessions.swapCursor(null);
    }
}
