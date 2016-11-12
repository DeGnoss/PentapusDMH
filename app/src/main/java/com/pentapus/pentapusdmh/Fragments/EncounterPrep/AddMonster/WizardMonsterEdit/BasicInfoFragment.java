package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DataBaseHandler;
import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.model.CustomerInfoPage;
import com.wizardpager.wizard.ui.CustomerInfoFragment;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

/**
 * Created by Koni on 11.11.2016.
 */

public class BasicInfoFragment extends Fragment {
    private static final String ARG_KEY = "basicinfo";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private BasicInfoPage mPage;
    private TextView mNameView;
    private TextView mTypeView;
    private TextView mAlignmentView;
    private TextView mSpeedView;

    private ImageButton bChooseImage;
    private Uri myFile;
    private static int RESULT_CHOOSE_IMG = 2;

    public static BasicInfoFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        BasicInfoFragment fragment = new BasicInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BasicInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (BasicInfoPage) mCallbacks.onGetPage(mKey);

        mPage.getData().putString(BasicInfoPage.IMAGEURI_DATA_KEY, "android.resource://com.pentapus.pentapusdmh/drawable/avatar_knight");
        mPage.notifyDataChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_basic_info, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mNameView = ((TextView) rootView.findViewById(R.id.tvMonsterName));
        mNameView.setText(mPage.getData().getString(BasicInfoPage.NAME_DATA_KEY));

        mTypeView = ((TextView) rootView.findViewById(R.id.tvMonsterType));
        mTypeView.setText(mPage.getData().getString(BasicInfoPage.TYPE_DATA_KEY));

        mAlignmentView = ((TextView) rootView.findViewById(R.id.tvMonsterAlignment));
        mAlignmentView.setText(mPage.getData().getString(BasicInfoPage.ALIGNMENT_DATA_KEY));

        mSpeedView = ((TextView) rootView.findViewById(R.id.tvMonsterSpeed));
        mSpeedView.setText(mPage.getData().getString(BasicInfoPage.SPEED_DATA_KEY));

        bChooseImage = (ImageButton) rootView.findViewById(R.id.bChooseImage);
        bChooseImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showViewPager();
            }
        });
        if(mPage.getData().getString(BasicInfoPage.IMAGEURI_DATA_KEY) != null){
            bChooseImage.setImageURI(Uri.parse(mPage.getData().getString(BasicInfoPage.IMAGEURI_DATA_KEY)));
        }


        return rootView;
    }

    public void showViewPager() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        ImageViewPagerDialogFragment newFragment = new ImageViewPagerDialogFragment();
        newFragment.setTargetFragment(this, RESULT_CHOOSE_IMG);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.replace(R.id.ContainerFrame, newFragment, "F_IMAGE_PAGER")
                .addToBackStack("F_IMAGE_PAGER").commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (PageFragmentCallbacks) getParentFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(BasicInfoPage.NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mTypeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(BasicInfoPage.TYPE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mAlignmentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(BasicInfoPage.ALIGNMENT_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mSpeedView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(BasicInfoPage.SPEED_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mNameView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_CHOOSE_IMG && resultCode == Activity.RESULT_OK){
            if(data != null) {
                String value = data.getStringExtra("imageUri");
                if(value != null) {
                    Uri uri = Uri.parse(value);
                    myFile = uri;
                    Log.v("MonsterEdit", "Data passed from Child fragment = " + uri);
                    bChooseImage.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            bChooseImage.setImageURI(myFile);
                        }
                    });
                }
            }
        }
        mPage.getData().putString(BasicInfoPage.IMAGEURI_DATA_KEY, myFile.toString());
        mPage.notifyDataChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
