package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.HelperClasses.CustomMultiAutoCompleteTextView;
import com.pentapus.pentapusdmh.HelperClasses.Utils;
import com.pentapus.pentapusdmh.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static com.pentapus.pentapusdmh.HelperClasses.Utils.setListViewHeightBasedOnChildren;
import static com.pentapus.pentapusdmh.HelperClasses.Utils.trimTrailingWhitespace;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddLanguagesDialogFragment extends DialogFragment {

    Button positiveButton;
    String languages;
    CustomMultiAutoCompleteTextView tvLanguagesOther;
    ArrayAdapter<String> mSuggestionAdapter;
    String[] item;
    String other = "";
    final List<String> listStandard = new ArrayList<String>();


    public AddLanguagesDialogFragment() {
    }

    //mode: 0 = add, 1 = update
    public static AddLanguagesDialogFragment newInstance(String languages) {
        AddLanguagesDialogFragment f = new AddLanguagesDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("languages", languages);

        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            languages = getArguments().getString("languages");
        }
        setCancelable(true);
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return buildDialog();
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return true;
                    } else {
                        dialog.dismiss();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }


    private void sendResult(Bundle results) {
        ((SkillsFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), results);
    }


    public Dialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Languages");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_languages, null);

        listStandard.add("Common");
        listStandard.add("Dwarvish");
        listStandard.add("Elvish");
        listStandard.add("Giant");
        listStandard.add("Gnomish");
        listStandard.add("Goblin");
        listStandard.add("Halfling");
        listStandard.add("Orc");
        listStandard.add("Abyssal");
        listStandard.add("Celestial");
        listStandard.add("Draconic");
        listStandard.add("Deep Speech");
        listStandard.add("Infernal");
        listStandard.add("Primordial");
        listStandard.add("Sylvan");
        listStandard.add("Undercommon");


        final ListView listViewStandard = (ListView) view.findViewById(R.id.listStandard);
        listViewStandard.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.simple_list_item_multiple_choice,
                android.R.id.text1,
                listStandard));
        listViewStandard.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        View v = new View(getContext());
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        listViewStandard.addFooterView(v, null, true);
        setListViewHeightBasedOnChildren(listViewStandard);


        tvLanguagesOther = (CustomMultiAutoCompleteTextView) view.findViewById(R.id.tvLanguagesOther);
        mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
        tvLanguagesOther.setAdapter(mSuggestionAdapter);
        tvLanguagesOther.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        if (languages != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> selectedItems = splitString(languages);
                    if (selectedItems == null || selectedItems.size() == 0) {
                        return;
                    }

                    Set<String> selectedSet = new HashSet<String>(selectedItems);

                    for (int i = 0; i < listStandard.size(); i++) {
                        if (selectedSet.contains(listStandard.get(i))) {
                            listViewStandard.setItemChecked(i, true);
                        }
                    }
                }
            });
            ArrayList<String> selectedItems = splitString(languages);
            for(int j = 0; j < selectedItems.size(); j++){
                if(!listStandard.contains(selectedItems.get(j))){
                    if(other.isEmpty()){
                        other = selectedItems.get(j);
                    }else{
                        other = other + ", " + selectedItems.get(j);
                    }
                }
            }
            if(!other.isEmpty()){
                other = other + ", ";
            }
            tvLanguagesOther.setText(other);
        }


        tvLanguagesOther.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (view.getWindowVisibility() != View.VISIBLE) {
                    return;
                }
                if (b) {
                    item = getItemsFromDb("", "languages");
                    // update the adapater
                    mSuggestionAdapter.notifyDataSetChanged();
                    mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item);
                    tvLanguagesOther.setAdapter(mSuggestionAdapter);
                    tvLanguagesOther.showDropDown();
                }
            }
        });

        tvLanguagesOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence userInput, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                // query the database based on the user input
                String intermediate_text = editable.toString();
                String final_string = intermediate_text.substring(intermediate_text.lastIndexOf(",") + 1);
                if (final_string.equals(" ")) {
                    item = getItemsFromDb("", "languages");
                } else {
                    item = getItemsFromDb(String.valueOf(Utils.trimLeadingWhitespace(final_string)), "languages");
                }

                // update the adapater
                mSuggestionAdapter.notifyDataSetChanged();
                mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item);
                tvLanguagesOther.setAdapter(mSuggestionAdapter);
                tvLanguagesOther.post(new Runnable() {
                    @Override
                    public void run() {
                        tvLanguagesOther.showDropDown();
                    }
                });
            }
        });


        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();

                SparseBooleanArray checkedPositions = listViewStandard.getCheckedItemPositions();
                String languages = "";
                ArrayList<String> selections = new ArrayList<String>();
                for (int i = 0; i < checkedPositions.size(); i++) {
                    if (checkedPositions.valueAt(i)) {
                        if (languages.isEmpty()) {
                            languages = listViewStandard.getAdapter().getItem(checkedPositions.keyAt(i)).toString();
                        } else {
                            languages = languages + ", " + listViewStandard.getAdapter().getItem(checkedPositions.keyAt(i)).toString();
                        }
                        selections.add(listViewStandard.getAdapter().getItem(checkedPositions.keyAt(i)).toString());
                    }
                }

                if(tvLanguagesOther.getText() != null && !tvLanguagesOther.getText().toString().isEmpty()){
                    String other = tvLanguagesOther.getText().toString();
                    other = String.valueOf(trimTrailingWhitespace(other));
                    if(other.charAt(other.length()-1) == ','){
                        other = other.substring(0, other.length()-1);
                    }
                    if(languages.isEmpty()){
                        languages = other;
                    }else{
                        languages = languages + ", " + other;
                    }
                }
                results.putString("languages", languages);

                // results.putStringArrayList("languages", selections);
                sendResult(results);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {}
                dialog.dismiss();
                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Create the AlertDialog object and return it
        return dialog;
    }


    private ArrayList<String> splitString(String dmgVul) {
        ArrayList<String> dmgVulSplit = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(dmgVul, ",");
        while (tokens.hasMoreTokens()) {
            dmgVulSplit.add(String.valueOf(Utils.trimLeadingWhitespace(tokens.nextToken())));
        }


        return dmgVulSplit;
    }


    public String[] getItemsFromDb(String searchTerm, String column) {

        // add items on the array dynamically
        List<String> entries = filterData(searchTerm, column);
        //TODO check loop length
        ArrayList<String> tempSplitLanguages;
        ArrayList<String> splitLanguages = new ArrayList<String>();
        for (int i = 0; i < entries.size(); i++) {
            tempSplitLanguages = splitString(entries.get(i));
            for (int j = 0; j < tempSplitLanguages.size(); j++) {
                if (!listStandard.contains(tempSplitLanguages.get(j)) && !splitLanguages.contains(tempSplitLanguages.get(j))) {
                    splitLanguages.add(tempSplitLanguages.get(j));
                }
            }
        }

        int rowCount = splitLanguages.size();

        String[] item = new String[rowCount];
        //String[] item = new String[1];
        int x = 0;
        //item[x] = "Monstrosity";

        for (String record : splitLanguages) {

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


}