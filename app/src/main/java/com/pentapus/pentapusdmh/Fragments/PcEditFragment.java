package com.pentapus.pentapusdmh.Fragments;

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
import com.pentapus.pentapusdmh.HelperClasses.SharedPrefsHelper;
import com.pentapus.pentapusdmh.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class PcEditFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MODE = "modeUpdate";
    private static final String CAMPAIGN_ID = "campaignId";
    private static final String PC_ID = "pcId";

    private static int RESULT_CHOOSE_IMG = 2;

    private boolean modeUpdate;
    private int pcId;
    private int campaignId;
    private int px;

    private Uri myFile;

    Button addchar_btn, bAddImage, bChooseImage;
    EditText name_tf, info_tf, init_tf, maxHp_tf, ac_tf;
    ImageView ivAvatar;


    public PcEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modeUpdate Parameter 1.
     * @param pcId       Parameter 2.
     * @return A new instance of fragment SessionEditFragment.
     */
    public static PcEditFragment newInstance(boolean modeUpdate, int pcId, int campaignId) {
        PcEditFragment fragment = new PcEditFragment();
        Bundle args = new Bundle();
        args.putBoolean(MODE, modeUpdate);
        args.putInt(PC_ID, pcId);
        args.putInt(CAMPAIGN_ID, campaignId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            modeUpdate = getArguments().getBoolean(MODE);
            campaignId = getArguments().getInt(CAMPAIGN_ID);
            //check whether entry gets updated or added
            if (modeUpdate) {
                pcId = getArguments().getInt(PC_ID);
            }
        }
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View charEditView = inflater.inflate(R.layout.fragment_pc_edit, container, false);
        name_tf = (EditText) charEditView.findViewById(R.id.etName);
        info_tf = (EditText) charEditView.findViewById(R.id.etInfo);
        init_tf = (EditText) charEditView.findViewById(R.id.etInit);
        maxHp_tf = (EditText) charEditView.findViewById(R.id.etHpMax);
        ac_tf = (EditText) charEditView.findViewById(R.id.etAc);
        bAddImage = (Button) charEditView.findViewById(R.id.bAddImage);
        ivAvatar = (ImageView) charEditView.findViewById(R.id.ivAvatar);
        bChooseImage = (Button) charEditView.findViewById(R.id.bChooseImage);

        if (modeUpdate) {
            loadCharacterInfo(name_tf, info_tf, init_tf, maxHp_tf, ac_tf, pcId);
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
                Crop.pickImage(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("FE_PC"));
            }
        });

        bChooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageGridFragment fragment = new ImageGridFragment();
                fragment.setTargetFragment(getActivity().getSupportFragmentManager().findFragmentByTag("FE_PC"), RESULT_CHOOSE_IMG);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FrameTop, fragment, "F_IMAGEGRID")
                        .addToBackStack("F_IMAGEGRID")
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return charEditView;

    }

    private void loadCharacterInfo(EditText name, EditText info, EditText init, EditText maxHp, EditText ac, int id) {
        String[] projection = {
                DataBaseHandler.KEY_ROWID,
                DataBaseHandler.KEY_NAME,
                DataBaseHandler.KEY_INFO,
                DataBaseHandler.KEY_INITIATIVEBONUS,
                DataBaseHandler.KEY_MAXHP,
                DataBaseHandler.KEY_AC,
                DataBaseHandler.KEY_ICON
        };
        Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + id);
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_NAME));
            String myInitiative = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INITIATIVEBONUS));
            String myInfo = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_INFO));
            String myMaxHp = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_MAXHP));
            String myAc = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_AC));
            myFile = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHandler.KEY_ICON)));
            name.setText(myName, TextView.BufferType.EDITABLE);
            info.setText(myInfo, TextView.BufferType.EDITABLE);
            init.setText(myInitiative, TextView.BufferType.EDITABLE);
            maxHp.setText(myMaxHp, TextView.BufferType.EDITABLE);
            ac.setText(myAc, TextView.BufferType.EDITABLE);
            ivAvatar.setImageURI(myFile);
        }
    }

    public void doneButton(boolean mode) {
        // get values from the input text fields
        String myName = name_tf.getText().toString();
        String myInitiative = init_tf.getText().toString();
        String myInfo = info_tf.getText().toString();
        String myMaxHp = maxHp_tf.getText().toString();
        String myAc = ac_tf.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.KEY_NAME, myName);
        values.put(DataBaseHandler.KEY_INFO, myInfo);
        values.put(DataBaseHandler.KEY_INITIATIVEBONUS, myInitiative);
        values.put(DataBaseHandler.KEY_MAXHP, myMaxHp);
        values.put(DataBaseHandler.KEY_AC, myAc);
        values.put(DataBaseHandler.KEY_ICON, String.valueOf(myFile));
        values.put(DataBaseHandler.KEY_BELONGSTO, campaignId);

        // insert a record
        if (!mode) {
            getContext();
            getContext().getContentResolver().insert(DbContentProvider.CONTENT_URI_PC, values);
        }
        // update a record
        else {
            Uri uri = Uri.parse(DbContentProvider.CONTENT_URI_PC + "/" + pcId);
            getContext().getContentResolver().update(uri, values, null, null);
        }
        // Bundle bundle = new Bundle();
        //bundle.putString("type", "pc");
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
        Crop.of(source, destination).asSquare().start(getContext(), getActivity().getSupportFragmentManager().findFragmentByTag("FE_PC"));
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
                            ivAvatar.setImageURI(uri);
                            ivAvatar.invalidate();
                        }
                    });

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
