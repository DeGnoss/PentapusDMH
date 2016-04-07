package com.pentapus.pentapusdmh.Fragments;


import android.app.AlertDialog;
import android.app.FragmentManager;
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

public class CampaignTableFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MODE = "modeUpdate";
    private static final String CAMPAIGN_ID = "campaignId";


    final CharSequence[] items = {"Edit", "Delete", "Player Characters"};
    FloatingActionButton fab;
    private SimpleCursorAdapter dataAdapterCampaigns;

    public CampaignTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SessionTableFragment.
     */
    public static CampaignTableFragment newInstance() {
        return new CampaignTableFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.campaignsFragmentTitle);
        final View tableView = inflater.inflate(R.layout.fragment_campaign_table, container, false);
        displayListView(tableView);
        // Inflate the layout for this fragment
        fab = (FloatingActionButton) tableView.findViewById(R.id.fabCampaign);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putBoolean(MODE, false);
                addCampaign(bundle);
            }
        });
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

        dataAdapterCampaigns = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.session_info,
                null,
                columns,
                to,
                0);

        final ListView listView = (ListView) view.findViewById(R.id.listViewCampaigns);
        listView.setAdapter(dataAdapterCampaigns);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                int campaignId =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                String campaignName =
                        cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
                loadCampaign(campaignId, campaignName);
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
                                    int campaignId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(MODE, true);
                                    bundle.putInt(CAMPAIGN_ID, campaignId);
                                    editCampaign(bundle);
                                    dialog.dismiss();
                                } else if (item == 1) {
                                    Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_CAMPAIGN + "/" + id);
                                    getContext().getContentResolver().delete(uri, null, null);
                                    dialog.dismiss();
                                } else if (item == 2) {
                                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                    int campaignId =
                                            cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ROWID));
                                    String campaignName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(CAMPAIGN_ID, campaignId);
                                    bundle.putString("campaignName", campaignName);
                                    loadPCs(bundle);
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        }).show();
                return true;
            }
        });


    }

    private void loadPCs(Bundle bundle) {
        Fragment fragment;
        fragment = new PcTableFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FT_PC")
                .addToBackStack("FT_PC")
                .commit();
    }

    private void editCampaign(Bundle bundle) {
        Fragment fragment;
        fragment = new CampaignEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_CAMPAIGN")
                .addToBackStack("FE_CAMPAIGN")
                .commit();
    }


    private void loadCampaign(int campaignId, String campaignName) {
        SharedPrefsHelper.saveCampaign(getContext(), campaignId, campaignName);
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        CursorLoader cursorLoader = new CursorLoader(this.getContext(),
                DbContentProvider.CONTENT_URI_CAMPAIGN, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapterCampaigns.swapCursor(data);

    }


    //TODO menu item invisible
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.campaign_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapterCampaigns.swapCursor(null);
    }


    public void addCampaign(Bundle bundle) {
        Fragment fragment;
        fragment = new CampaignEditFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameTop, fragment, "FE_CAMPAIGN")
                .addToBackStack("FE_CAMPAIGN")
                .commit();
    }
}
