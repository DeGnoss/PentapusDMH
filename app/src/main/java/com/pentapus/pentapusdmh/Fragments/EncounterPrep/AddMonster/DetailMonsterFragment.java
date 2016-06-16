package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;


public class DetailMonsterFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NPC_ID = "npcId";
    private static final String NPC_NAME = "npcName";

    private int npcId;
    private String npcName;
    private Uri myFile;

    ImageView ivAvatar;
    TextView tvName, tvInfo, tvInit, tvMaxHp, tvAc, tvStrength, tvDex, tvConst, tvInt, tvWis, tvCha;

    public DetailMonsterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param npcId    Parameter 2.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static DetailMonsterFragment newInstance(int npcId) {
        DetailMonsterFragment fragment = new DetailMonsterFragment();
        Bundle args = new Bundle();
        args.putInt(NPC_ID, npcId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
                npcId = getArguments().getInt(NPC_ID);
                //npcName = getArguments().getString(NPC_NAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_monster_detail, container, false);
        //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) charEditView.getLayoutParams();
        //params.setMargins(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        charEditView.setBackgroundColor(Color.WHITE);
        tvName = (TextView) charEditView.findViewById(R.id.tvName);
        tvInfo = (TextView) charEditView.findViewById(R.id.tvInfo);
        tvInit = (TextView) charEditView.findViewById(R.id.tvInit);
        tvAc = (TextView) charEditView.findViewById(R.id.tvAc);
        tvMaxHp = (TextView) charEditView.findViewById(R.id.tvMaxHp);
        tvStrength = (TextView) charEditView.findViewById(R.id.tvStrength);
        tvDex = (TextView) charEditView.findViewById(R.id.tvDex);
        tvConst = (TextView) charEditView.findViewById(R.id.tvConst);
        tvInt = (TextView) charEditView.findViewById(R.id.tvInt);
        tvWis = (TextView) charEditView.findViewById(R.id.tvWis);
        tvCha = (TextView) charEditView.findViewById(R.id.tvChar);
        ivAvatar = (ImageView) charEditView.findViewById(R.id.ivAvatar);



        loadMonsterInfo(tvName, tvInfo, tvInit, tvAc, tvMaxHp, tvStrength, tvDex, tvConst, tvInt, tvWis, tvCha, ivAvatar, npcId);

        // Inflate the layout for this fragment
        return charEditView;
    }


    private void loadMonsterInfo(TextView name, TextView info, TextView init, TextView ac, TextView maxHp, TextView strength, TextView dex, TextView con, TextView intel, TextView wis, TextView cha, ImageView ivAvatar, int id) {

        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_MONSTER + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_MONSTER_TEMPLATE, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            String myInitiative = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS));
            String myMaxHp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
            String myAc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            String myStrength = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH)));
            String myDexterity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY)));
            String myConstitution = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION)));
            String myIntelligence = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE)));
            String myWisdom = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM)));
            String myCharisma = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA)));
            myFile = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myName);
            name.setText(myName);
            info.setText(myInfo);
            init.setText(myInitiative);
            ac.setText(myAc);
            maxHp.setText(myMaxHp);
            strength.setText(myStrength);
            dex.setText(myDexterity);
            con.setText(myConstitution);
            intel.setText(myIntelligence);
            wis.setText(myWisdom);
            cha.setText(myCharisma);
            ivAvatar.setImageURI(myFile);

            assert cursor != null;
            cursor.close();
        }
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
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().invalidateOptionsMenu();
        ((MainActivity)getActivity()).setFabVisibility(false);
        ((MainActivity)getActivity()).disableNavigationDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
