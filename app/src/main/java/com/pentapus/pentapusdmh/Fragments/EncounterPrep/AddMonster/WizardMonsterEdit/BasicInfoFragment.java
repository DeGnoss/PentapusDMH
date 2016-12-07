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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.Fragments.EncounterPrep.ImageViewPagerDialogFragment;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.R;
import com.wizardpager.wizard.ui.PageFragmentCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Koni on 11.11.2016.
 */

public class BasicInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String ARG_KEY = "basicinfo";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private BasicInfoPage mPage;
    private TextView mNameView;
    private CustomAutoCompleteTextView mTypeView;
    private CustomAutoCompleteTextView mAlignmentView;
    private Spinner sizeSpinner;
    ArrayAdapter<String> mSuggestionAdapter;
    String[] item;
    private Bundle filters;
    OnSizeChangedListener mOnSizeChangedListener;

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

        mTypeView = ((CustomAutoCompleteTextView) rootView.findViewById(R.id.tvMonsterType));
        mTypeView.setText(mPage.getData().getString(BasicInfoPage.TYPE_DATA_KEY));
        mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
        mTypeView.setAdapter(mSuggestionAdapter);

        mAlignmentView = ((CustomAutoCompleteTextView) rootView.findViewById(R.id.tvMonsterAlignment));
        mAlignmentView.setText(mPage.getData().getString(BasicInfoPage.ALIGNMENT_DATA_KEY));
        mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
        mTypeView.setAdapter(mSuggestionAdapter);

        bChooseImage = (ImageButton) rootView.findViewById(R.id.bChooseImage);
        bChooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showViewPager();
            }
        });
        if (mPage.getData().getString(BasicInfoPage.IMAGEURI_DATA_KEY) != null) {
            bChooseImage.setImageURI(Uri.parse(mPage.getData().getString(BasicInfoPage.IMAGEURI_DATA_KEY)));
        }

        sizeSpinner = (Spinner) rootView.findViewById(R.id.size_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);
        sizeSpinner.setOnItemSelectedListener(this);
        if(mPage.getData().getString(BasicInfoPage.SIZE_DATA_KEY) != null && !mPage.getData().getString(BasicInfoPage.SIZE_DATA_KEY).isEmpty()){
            sizeSpinner.setSelection(adapter.getPosition(mPage.getData().getString(BasicInfoPage.SIZE_DATA_KEY)));
        }else{
            sizeSpinner.setSelection(adapter.getPosition("Medium"));
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
        try {
            mOnSizeChangedListener = (OnSizeChangedListener)getParentFragment();
        }catch (ClassCastException e){

        }
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
                pageDone();
            }
        });

        mTypeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence userInput, int start, int before, int count) {
                // query the database based on the user input
                item = getItemsFromDb(userInput.toString(), "monstertype");

                // update the adapater
                mSuggestionAdapter.notifyDataSetChanged();
                mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item);
                mTypeView.setAdapter(mSuggestionAdapter);
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
            public void onTextChanged(CharSequence userInput, int start, int before, int count) {
                // query the database based on the user input
                item = getItemsFromDb(userInput.toString(), "alignment");

                // update the adapater
                mSuggestionAdapter.notifyDataSetChanged();
                mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item);
                mAlignmentView.setAdapter(mSuggestionAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(BasicInfoPage.ALIGNMENT_DATA_KEY,
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
        if (requestCode == RESULT_CHOOSE_IMG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String value = data.getStringExtra("imageUri");
                if (value != null) {
                    Uri uri = Uri.parse(value);
                    myFile = uri;
                    Log.v("MonsterEdit", "Data passed from Child fragment = " + uri);
                    bChooseImage.post(new Runnable() {
                        @Override
                        public void run() {
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

    public String[] getItemsFromDb(String searchTerm, String column) {

        // add items on the array dynamically
        List<String> entries = filterData(searchTerm, column);
        int rowCount = entries.size();

        String[] item = new String[rowCount];
        //String[] item = new String[1];
        int x = 0;
        //item[x] = "Monstrosity";

        for (String record : entries) {

            item[x] = record;
            x++;
        }

        return item;
    }


    public List<String> filterData(String filterArgs, String column) {
        List<String> results = new ArrayList<String>();

        Uri uri = DbContentProvider.CONTENT_URI_MONSTER;
        String selection = "";
        String[] selectionArgs;
        List<String> selectionList = new ArrayList<>();

        String filter = "%" + filterArgs + "%";
        selectionList.add(filter);
        selectionArgs = new String[selectionList.size()];
        selectionList.toArray(selectionArgs);
        if (filterArgs != null) {
            selection = column + " LIKE ?";
        }

        //String filterFormatted = "%" + filterArgs + "%";
        //filters.putString("filter", filterFormatted);
        Cursor cursor = getContext().getContentResolver().query(uri, new String[]{"DISTINCT " + column}, selection, selectionArgs,
                null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(cursor.getColumnIndexOrThrow(column)));
            } while (cursor.moveToNext());
        }

        return results;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mPage.getData().putString(BasicInfoPage.SIZE_DATA_KEY, adapterView.getItemAtPosition(i).toString());
        mPage.notifyDataChanged();
        mOnSizeChangedListener.onSizeChanged(adapterView.getItemAtPosition(i).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void pageDone(){
        mOnSizeChangedListener.onSizeChanged(mPage.getData().getString(BasicInfoPage.SIZE_DATA_KEY));
    }

    public interface OnSizeChangedListener {

        public void onSizeChanged(String size);

    }
}
