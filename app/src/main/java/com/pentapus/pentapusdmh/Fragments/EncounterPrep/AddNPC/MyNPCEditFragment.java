package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddNPC;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.pentapus.pentapusdmh.AdapterNavigationCallback;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.MainActivity;
import com.pentapus.pentapusdmh.R;
import com.pentapus.pentapusdmh.Utils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class MyNPCEditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";
    private static final String ENCOUNTER_ID = "encounterId";

    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_CHOOSE_IMG = 2;


    private Uri myFile;

    private static int campaignId;
    private boolean modeUpdate;
    private int npcId;
    private int encounterId;
    private int px;

    ImageButton bChooseImage;
    EditText name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar;

    public MyNPCEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate  Parameter 1.
     * @param npcId       Parameter 2.
     * @param encounterId Parameter 3.
     * @return A new instance of fragment EncounterEditFragment.
     */
    public static MyNPCEditFragment newInstance(boolean modeUpdate, int npcId, int encounterId) {
        MyNPCEditFragment fragment = new MyNPCEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(NPC_ID, npcId);
        args.putInt(ENCOUNTER_ID, encounterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            encounterId = getArguments().getInt(ENCOUNTER_ID);
            campaignId = SharedPrefsHelper.loadCampaignId(getContext());
            //check whether entry gets updated or added
            if (modeUpdate) {
                npcId = getArguments().getInt(NPC_ID);
            }
        }
        px = (int) Math.ceil(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().invalidateOptionsMenu();
        final View charEditView = inflater.inflate(R.layout.fragment_mymonster_edit, container, false);
        charEditView.setBackgroundColor(Color.WHITE);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);
        init_tf = (EditText) charEditView.findViewById(R.id.etInit);
        maxHp_tf = (EditText) charEditView.findViewById(R.id.etHpMax);
        ac_tf = (EditText) charEditView.findViewById(R.id.etAc);
        etStrength = (EditText) charEditView.findViewById(R.id.etStrength);
        etDex = (EditText) charEditView.findViewById(R.id.etDex);
        etConst = (EditText) charEditView.findViewById(R.id.etConst);
        etInt = (EditText) charEditView.findViewById(R.id.etInt);
        etWis = (EditText) charEditView.findViewById(R.id.etWis);
        etChar = (EditText) charEditView.findViewById(R.id.etChar);
        bChooseImage = (ImageButton) charEditView.findViewById(R.id.bChooseImage);


        name_tf.setText("New Friend");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("New Friend");
        name_tf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (modeUpdate) {
            loadNPCInfo(name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar, npcId);
        }

        bChooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showViewPager();
                /*
                ViewPagerMyImageGridFragment fragment = new ViewPagerMyImageGridFragment();
                fragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag("FE_NPC"), RESULT_CHOOSE_IMG);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FrameTop, fragment, "F_IMAGEGRID")
                        .addToBackStack("F_IMAGEGRID")
                        .commit();*/
            }
        });


        bChooseImage.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Glide.clear(bChooseImage);
                Crop.pickImage(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("FE_MYNPC"));
                return true;
            }
        });


        // Inflate the layout for this fragment
        return charEditView;
    }

    public void onFabClick(){
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInfo = info_tf.getText().toString();
        String myInitiative = init_tf.getText().toString();
        String myMaxHp = maxHp_tf.getText().toString();
        String myAc = ac_tf.getText().toString();
        String myStrength = etStrength.getText().toString();
        String myDexterity = etDex.getText().toString();
        String myConstitution = etConst.getText().toString();
        String myIntelligence = etInt.getText().toString();
        String myWisdom = etWis.getText().toString();
        String myCharisma = etChar.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInfo);
        values.put(DataBaseHandler.KEY_INITIATIVEBONUS, myInitiative);
        values.put(DataBaseHandler.KEY_MAXHP, myMaxHp);
        values.put(DataBaseHandler.KEY_AC, myAc);
        values.put(DataBaseHandler.KEY_STRENGTH, myStrength);
        values.put(DataBaseHandler.KEY_DEXTERITY, myDexterity);
        values.put(DataBaseHandler.KEY_CONSTITUTION, myConstitution);
        values.put(DataBaseHandler.KEY_INTELLIGENCE, myIntelligence);
        values.put(DataBaseHandler.KEY_WISDOM, myWisdom);
        values.put(DataBaseHandler.KEY_CHARISMA, myCharisma);
        if (myFile == null) {
            myFile = Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight");
        }
        values.put(DataBaseHandler.KEY_ICON, String.valueOf(myFile));
        values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);
        values.put(DataBaseHandler.KEY_TYPE, DataBaseHandler.TYPE_NPC);

        // insert a record
        if (!modeUpdate) {
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_NPC, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + npcId);
            getContext().getContentResolver().update(uri, values, null, null);
        }

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getActivity().getSupportFragmentManager().popBackStack();
    }


    public void showViewPager() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ImageViewPagerDialogFragment newFragment = new ImageViewPagerDialogFragment();
        newFragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag("FE_MYNPC"), RESULT_CHOOSE_IMG);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.replace(R.id.ContainerFrame, newFragment, "F_IMAGE_PAGER")
                .addToBackStack(null).commit();
    }

    private void loadNPCInfo(EditText name, EditText info, EditText init, EditText maxHp, EditText ac, EditText strength, EditText dex, EditText constit, EditText intelligence, EditText wis, EditText charisma, int id) {

        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, DataBaseHandler.PROJECTION_NPC_TEMPLATE, null, null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            String myInitiative = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS));
            String myMaxHp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
            String myAc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            String myStrength = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_STRENGTH));
            String myDexterity = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_DEXTERITY));
            String myConstitution = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CONSTITUTION));
            String myIntelligence = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INTELLIGENCE));
            String myWisdom = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_WISDOM));
            String myCharisma = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_CHARISMA));
            myFile = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));

            name.setText(myName, TextView.BufferType.EDITABLE);
            info.setText(myInfo, TextView.BufferType.EDITABLE);
            init.setText(myInitiative, TextView.BufferType.EDITABLE);
            maxHp.setText(myMaxHp, TextView.BufferType.EDITABLE);
            ac.setText(myAc, TextView.BufferType.EDITABLE);
            strength.setText(myStrength, TextView.BufferType.EDITABLE);
            dex.setText(myDexterity, TextView.BufferType.EDITABLE);
            constit.setText(myConstitution, TextView.BufferType.EDITABLE);
            intelligence.setText(myIntelligence, TextView.BufferType.EDITABLE);
            wis.setText(myWisdom, TextView.BufferType.EDITABLE);
            charisma.setText(myCharisma, TextView.BufferType.EDITABLE);
            bChooseImage.setImageURI(myFile);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(myName);

        }
        assert cursor != null;
        cursor.close();
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
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity)getActivity()).setFabVisibility(true);
        ((MainActivity)getActivity()).setFabIcon(false);
        ((MainActivity)getActivity()).disableNavigationDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CHOOSE_IMG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String value = data.getStringExtra("imageUri");
                if (value != null) {
                    Uri uri = Uri.parse(value);
                    myFile = uri;
                    Log.v("NPCEdit", "Data passed from Child fragment = " + uri);
                    bChooseImage.post(new Runnable() {
                        @Override
                        public void run() {
                            bChooseImage.setImageURI(myFile);
                        }
                    });
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getContext().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("FE_NPC"));
    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Glide.with(getContext())
                    .load(Crop.getOutput(result))
                    .asBitmap()
                    .override(px, px)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ContextWrapper cw = new ContextWrapper(getContext().getApplicationContext());
                            // path to /data/data/yourapp/app_data/iconDir
                            File directory = cw.getDir("iconDir", Context.MODE_PRIVATE);
                            // Create iconDir
                            UUID uuid = UUID.randomUUID();
                            String randomUUIDString = uuid.toString();
                            File mypath = new File(directory, randomUUIDString);

                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(mypath);
                                // Use the compress method on the BitMap object to write image to the OutputStream
                                resource.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //File directory = new getContext().getFileStreamPath("app_iconDir");
                            Uri uri = Uri.parse(mypath.getPath());
                            myFile = uri;
                            bChooseImage.setImageURI(uri);
                            bChooseImage.invalidate();
                        }
                    });

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
