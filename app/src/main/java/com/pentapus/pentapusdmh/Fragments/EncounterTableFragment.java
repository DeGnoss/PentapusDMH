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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pentapus.pentapusdmh.DataBaseHandler;
import com.pentapus.pentapusdmh.DbContentProvider;
import com.pentapus.pentapusdmh.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EncounterTableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EncounterTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncounterTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private String sessionId;
    private FloatingActionButton fab;
    final CharSequence[] items = { "Edit", "Delete" };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SimpleCursorAdapter dataAdapterEncounters;

    public EncounterTableFragment() {
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
    public static EncounterTableFragment newInstance(String param1, String param2) {
        EncounterTableFragment fragment = new EncounterTableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_encounter_table, container, false);
        // insert a record
        if (this.getArguments() != null){
            sessionId = getArguments().getString("sessionId");
        }
        displayListView(tableView);
        fab = (FloatingActionButton) tableView.findViewById(R.id.fabEncounter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("mode", "add");
                bundle.putString("sessionId", sessionId);
                mListener.addEncounter(bundle);
            }
        });
        // Inflate the layout for this fragment
        return tableView;

    }

    private void displayListView(View view) {

        String[] columns = new String[] {
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };

        int[] to = new int[] {
                R.id.name,
                R.id.info,
        };

        dataAdapterEncounters = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.encounter_info,
                null,
                columns,
                to,
                0);

        final ListView listView = (ListView) view.findViewById(R.id.listViewEncounters);
        listView.setAdapter(dataAdapterEncounters);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String encounterId =
                        cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));

                Bundle bundle = new Bundle();
                bundle.putString("encounterId", encounterId);
                loadNPC(bundle);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                new AlertDialog.Builder(getContext()).setTitle("Student Record")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                    String encounterId = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putString("mode", "update");
                                    bundle.putString("encounterId", encounterId);
                                    bundle.putString("sessionId", sessionId);
                                    editEncounter(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_ENCOUNTER + "/" + id);
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

    private void editEncounter(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_ENCOUNTER")
                .addToBackStack("FE_ENCOUNTER")
                .commit();
    }

    private void loadNPC(Bundle bundle) {
        Fragment fragment;
        fragment = new EncounterFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "F_ENCOUNTER")
                .addToBackStack("F_ENCOUNTER")
                .commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (OnFragmentInteractionListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO
        };
        String[] selectionArgs = new String[]{ sessionId };
        String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_ENCOUNTER, projection, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapterEncounters.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapterEncounters.swapCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void addEncounter(Bundle bundle);
    }
}
