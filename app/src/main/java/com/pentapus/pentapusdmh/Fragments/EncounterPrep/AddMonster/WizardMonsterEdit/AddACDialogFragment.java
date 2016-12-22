package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.pentapus.pentapusdmh.DbClasses.DbContentProvider;
import com.pentapus.pentapusdmh.HelperClasses.CustomAutoCompleteTextView;
import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddACDialogFragment extends DialogFragment {

    Button positiveButton;
    EditText tvAC1, tvAC2;
    CustomAutoCompleteTextView tvAC1Type, tvAC2Type;
    String ac1type, ac2type, ac1, ac2;
    ArrayAdapter<String> mSuggestionAdapter;
    String[] item;


    public AddACDialogFragment() {
    }

    //mode: 0 = add, 1 = update
    public static AddACDialogFragment newInstance(String ac1, String ac2, String ac1type, String ac2type) {
        AddACDialogFragment f = new AddACDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("ac1", ac1);
        args.putString("ac2", ac2);
        args.putString("ac1type", ac1type);
        args.putString("ac2type", ac2type);
        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ac1 = getArguments().getString("ac1");
            ac2 = getArguments().getString("ac2");
            ac1type = getArguments().getString("ac1type");
            ac2type = getArguments().getString("ac2type");
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
        ((AbilitiesFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), results);
    }


    public Dialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Armor Class");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_ac, null);
        tvAC1 = (EditText) view.findViewById(R.id.tvAC1);
        //tvAC1Type = (EditText) view.findViewById(R.id.tvAC1Type);
        tvAC2 = (EditText) view.findViewById(R.id.tvAC2);
        tvAC2Type = (CustomAutoCompleteTextView) view.findViewById(R.id.tvAC2Type);
        tvAC1Type = ((CustomAutoCompleteTextView) view.findViewById(R.id.tvAC1Type));

        if (ac1type != null && !ac1type.isEmpty()) {
            tvAC1Type.setText(String.valueOf(ac1type));
        }
        mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
        tvAC1Type.setAdapter(mSuggestionAdapter);
        tvAC2Type.setAdapter(mSuggestionAdapter);


        if (ac1 != null && !ac1.isEmpty()) {
            tvAC1.setText(String.valueOf(ac1));
        }

        if (ac2 != null && !ac2.isEmpty()) {
            tvAC2.setText(String.valueOf(ac2));
        }
        if (ac2type != null && !ac2type.isEmpty()) {
            tvAC2Type.setText(String.valueOf(ac2type));
        }
        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle results = new Bundle();
                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {}
                if (!tvAC1.getText().toString().isEmpty()) {
                    ac1 = tvAC1.getText().toString();
                } else {
                    ac1 = "";
                }
                ac1type = tvAC1Type.getText().toString();
                if (!tvAC2.getText().toString().isEmpty()) {
                    ac2 = tvAC2.getText().toString();
                } else {
                    ac2 = "";
                }
                ac2type = tvAC2Type.getText().toString();

                results.putString("ac1", ac1);
                results.putString("ac1type", ac1type);
                results.putString("ac2", ac2);
                results.putString("ac2type", ac2type);
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


        tvAC1Type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // query the database based on the user input
                item = getItemsFromDb(charSequence.toString(), "actype");

                // update the adapater
                mSuggestionAdapter.notifyDataSetChanged();
                mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item);
                tvAC1Type.setAdapter(mSuggestionAdapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvAC2Type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // query the database based on the user input
                item = getItemsFromDb(charSequence.toString(), "ac2type");

                // update the adapater
                mSuggestionAdapter.notifyDataSetChanged();
                mSuggestionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, item);
                tvAC2Type.setAdapter(mSuggestionAdapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Create the AlertDialog object and return it
        return dialog;
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

}