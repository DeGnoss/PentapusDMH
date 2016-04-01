package com.pentapus.pentapusdmh.Fragments;


import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.commonsware.cwac.merge.MergeAdapter;
import com.pentapus.pentapusdmh.RecyclerItemClickListener;
import com.pentapus.pentapusdmh.TrackerAdapter;
import com.pentapus.pentapusdmh.TrackerInfoCard;
import com.pentapus.pentapusdmh.DataBaseHandler;
import com.pentapus.pentapusdmh.DbContentProvider;
import com.pentapus.pentapusdmh.DiceHelper;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.SharedPrefsHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class TrackerFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TrackerAdapter chars;
    private static int campaignId, encounterId;
    private MergeAdapter mergeAdapter;
    private ArrayList<String> names;
    private ArrayList<Integer> initiative;

    final int minValue = -5;
    final int maxValue = 5;
    String[] nums = new String[10];




    public TrackerFragment() {
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
    public static TrackerFragment newInstance(String param1, String param2) {
        TrackerFragment fragment = new TrackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        names = new ArrayList<String>();
        initiative = new ArrayList<Integer>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View tableView = inflater.inflate(R.layout.fragment_tracker, container, false);
        final RecyclerView mRecyclerView = (RecyclerView) tableView.findViewById(R.id.listViewEncounter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        // insert a record
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(SharedPrefsHelper.loadEncounterName(getContext()));

        for(int i=0; i<nums.length; i++){
            nums[i] = Integer.toString(i-(nums.length/2));
        }

        campaignId = SharedPrefsHelper.loadCampaignId(getContext());
        encounterId = SharedPrefsHelper.loadEncounterId(getContext());

        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(1, null, this);
        chars = new TrackerAdapter();
        mRecyclerView.setAdapter(chars);
        //Log.d("Name:", names.get(0));

        Button next = (Button) tableView.findViewById(R.id.bNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chars.moveToBottom();
                chars.notifyDataSetChanged();
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //TrackerInfoCard current = chars.getList().get(position);
                        onClick(position);
                    }
                })
        );

        // Inflate the layout for this fragment
        return tableView;

    }


    public void onClick(int id){
        show(getContext(), id);
        //Log.d("Current Hp: ", String.valueOf(characterList.get(id).maxHp));
        //characterList.get(id).maxHp = String.valueOf(10);
    }

    public void show(Context context, final int id)
    {

        final Dialog d = new Dialog(context);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_modify);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);


        np.setDisplayedValues(nums);
       /* np.setMinValue(0);
        np.setMaxValue(maxValue - minValue);
        np.setValue(maxValue);
        np.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                return Integer.toString(index + minValue);
            }
        });

        np.setWrapSelectorWheel(false);
        Field f = null;
        try {
            f = NumberPicker.class.getDeclaredField("mInputText");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        EditText inputText = null;
        try {
            inputText = (EditText)f.get(np);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        inputText.setFilters(new InputFilter[0]);


        //hack to make the numberpicker work correctly
        try {
            Method method = np.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(np, true);
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
*/

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                chars.setHp(id, np.getValue()+minValue);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }




    @Override
    public void onPrepareOptionsMenu(Menu menu){
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
        if (id == 0) {
            String[] projection = {
                    DataBaseHandler.KEY_ROWID,
                    DataBaseHandler.KEY_NAME,
                    DataBaseHandler.KEY_INITIATIVEBONUS,
                    DataBaseHandler.KEY_AC,
                    DataBaseHandler.KEY_MAXHP
            };
            String[] selectionArgs = new String[]{String.valueOf(encounterId)};
            String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
            return new CursorLoader(this.getContext(),
                    DbContentProvider.CONTENT_URI_NPC, projection, selection, selectionArgs, null);
        } else {
            String[] projection = {
                    DataBaseHandler.KEY_ROWID,
                    DataBaseHandler.KEY_NAME,
                    DataBaseHandler.KEY_INITIATIVEBONUS,
            };
            String[] selectionArgs = new String[]{String.valueOf(campaignId)};
            String selection = DataBaseHandler.KEY_BELONGSTO + " = ?";
            return new CursorLoader(this.getContext(),
                    DbContentProvider.CONTENT_URI_PC, projection, selection, selectionArgs, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            //case NPC
            case 0:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    int initiative = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    int ac = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_AC));
                    int maxHp = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_MAXHP));
                    initiative = initiative+ DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    ci.ac = String.valueOf(ac);
                    ci.maxHp = String.valueOf(maxHp);
                    chars.addListItem(ci);
                    //names.add(data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME)));
                    //initiative.add(data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS)));

                }
                break;
            //case PC
            case 1:
                while (data.moveToNext()) {
                    String names = data.getString(data.getColumnIndex(DataBaseHandler.KEY_NAME));
                    int initiative = data.getInt(data.getColumnIndex(DataBaseHandler.KEY_INITIATIVEBONUS));
                    int initiativeMod = initiative;
                    initiative = initiative + DiceHelper.d20();
                    TrackerInfoCard ci = new TrackerInfoCard();
                    ci.name = names;
                    ci.initiative = String.valueOf(initiative);
                    ci.initiativeMod = String.valueOf(initiativeMod);
                    chars.addListItem(ci);
                }
                break;
            default:
                break;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chars.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case 0:
                //dataAdapterNPC.swapCursor(null);
                break;
            case 1:
                //dataAdapterPC.swapCursor(null);
                break;
            default:
                break;
        }
    }
}
