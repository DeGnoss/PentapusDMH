package com.pentapus.pentapusdmh.Fragments.EncounterPrep;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.ImageGridFragment;
import com.pentapus.pentapusdmh.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class NPCEditFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String NPC_ID = "npcId";
    private static final String ENCOUNTER_ID = "encounterId";

    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_CHOOSE_IMG = 2;


    private Uri myFile;

    private boolean modeUpdate;
    private int npcId;
    private int encounterId;
    private int px;

    Button addchar_btn, bAddImage, bChooseImage;
    EditText name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar;
    ImageView ivAvatar;

    public NPCEditFragment() {
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
    public static NPCEditFragment newInstance(boolean modeUpdate, int npcId, int encounterId) {
        NPCEditFragment fragment = new NPCEditFragment();
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
            //check wheter entry gets updated or added
            if (modeUpdate) {
                npcId = getArguments().getInt(NPC_ID);
            }
        }
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_npc_edit, container, false);
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
        bAddImage = (Button) charEditView.findViewById(R.id.bAddImage);
        ivAvatar = (ImageView) charEditView.findViewById(R.id.ivAvatar);
        bChooseImage = (Button) charEditView.findViewById(R.id.bChooseImage);


        if (modeUpdate) {
            loadNPCInfo(name_tf, info_tf, init_tf, maxHp_tf, ac_tf, etStrength, etDex, etConst, etInt, etWis, etChar, npcId);
        }
        addchar_btn = (Button) charEditView.findViewById(R.id.bDone);
        addchar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButton(modeUpdate);
            }
        });

        bAddImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG); */
                Glide.clear(ivAvatar);
                Crop.pickImage(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("FE_NPC"));
            }
        });

        bChooseImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ImageGridFragment fragment = new ImageGridFragment();
                fragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag("FE_NPC"), RESULT_CHOOSE_IMG);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FrameTop, fragment, "F_IMAGEGRID")
                        .addToBackStack("F_IMAGEGRID")
                        .commit();
            }
        });



        // Inflate the layout for this fragment
        return charEditView;
    }

    private void loadNPCInfo(EditText name, EditText info, EditText init, EditText maxHp, EditText ac, EditText strength, EditText dex, EditText constit, EditText intelligence, EditText wis, EditText charisma, int id) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_INITIATIVEBONUS,
                DataBaseHandler.KEY_MAXHP,
                DataBaseHandler.KEY_AC,
                DataBaseHandler.KEY_STRENGTH,
                DataBaseHandler.KEY_DEXTERITY,
                DataBaseHandler.KEY_CONSTITUTION,
                DataBaseHandler.KEY_INTELLIGENCE,
                DataBaseHandler.KEY_WISDOM,
                DataBaseHandler.KEY_CHARISMA,
                DataBaseHandler.KEY_ICON};
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_NPC + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
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
            ivAvatar.setImageURI(myFile);

        }
    }

    public void doneButton(boolean modeUpdate) {
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
        if(myFile == null){
            myFile = Uri.parse("android.resource://com.pentapus.pentapusdmh/drawable/ninja");
        }
        values.put(DataBaseHandler.KEY_ICON, String.valueOf(myFile));
        values.put(DataBaseHandler.KEY_BELONGSTO, encounterId);
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
        getActivity().getSupportFragmentManager().popBackStack();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if(requestCode == RESULT_CHOOSE_IMG && resultCode == Activity.RESULT_OK){
            if(data != null) {
                String value = data.getStringExtra("imageUri");
                if(value != null) {
                    Uri uri = Uri.parse(value);
                    myFile = uri;
                    Log.v("NPCEdit", "Data passed from Child fragment = " + uri);
                    ivAvatar.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            ivAvatar.setImageURI(myFile);
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
                            Log.v("NPCEdit", "Data passed from Child fragment = " + uri);
                            ivAvatar.setImageURI(uri);
                            ivAvatar.invalidate();
                        }
                    });

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
