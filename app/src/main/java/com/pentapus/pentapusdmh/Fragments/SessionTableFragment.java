package com.pentapus.pentapusdmh.Fragments;


import android.app.AlertDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pentapus.pentapusdmh.DataBaseHandler;
import com.pentapus.pentapusdmh.DbContentProvider;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.SharedPrefsHelper;


public class SessionTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    final CharSequence[] items = {"Edit", "Delete"};
    FloatingActionButton fab;
    private SimpleCursorAdapter dataAdapterSessions;
    private static int campaignId;

    public SessionTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SessionTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SessionTableFragment newInstance(String param1, String param2) {
        SessionTableFragment fragment = new SessionTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate", ("campaignId = " + campaignId));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_session_table, container, false);
        campaignId = SharedPrefsHelper.loadCampaign(getContext());
        Log.d("oncreateview", ("campaignId = " + campaignId));
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
            Log.d("displaylistview after", ("campaignId = " + campaignId));
            // Inflate the layout for this fragment
            fab = (FloatingActionButton) tableView.findViewById(R.id.fabSession);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mode", "add");
                    bundle.putString("campaignId", String.valueOf(campaignId));
                    addSession(bundle);
                }
            });
        }
        return tableView;
    }

    private void displayListView(View view) {
        Log.d("displaylistview start", ("campaignId = " + campaignId));
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

        //dataAdapterSessions.changeCursor(null);
        //dataAdapterSessions.notifyDataSetChanged();
        final ListView listView = (ListView) view.findViewById(R.id.listViewSessions);
        listView.setAdapter(dataAdapterSessions);
        //Ensures a loader is initialized and active.
        if(getLoaderManager().getLoader(0) == null){
            getLoaderManager().initLoader(0, null, this);
        } else{
            getLoaderManager().restartLoader(0, null, this);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String sessionId =
                        cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));

                Bundle bundle = new Bundle();
                bundle.putString("sessionId", sessionId);
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
                                    String sessionId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putString("mode", "update");
                                    bundle.putString("sessionId", sessionId);
                                    bundle.putString("campaignId", String.valueOf(campaignId));
                                    editSession(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_SESSION + "/" + id);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    dialog.dismiss();
                                } else {
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
        Log.d("Loader", ("campaignId = " + campaignId));
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };
        String[] selectionArgs = new String[]{String.valueOf(campaignId)};
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_SESSION, projection, selection, selectionArgs, null);
        Log.d("Loader", selectionArgs[0]);
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
