package com.pentapus.pentapusdmh.Fragments.EncounterPrep.AddMonster.WizardMonsterEdit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.pentapus.pentapusdmh.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by konrad.fellmann on 12.05.2016.
 */
public class AddConImDialogFragment extends DialogFragment {

    Button positiveButton;
    String conIm;


    public AddConImDialogFragment(){
    }

    //mode: 0 = add, 1 = update
    public static AddConImDialogFragment newInstance(String conIm) {
        AddConImDialogFragment f = new AddConImDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("conIm", conIm);

        f.setArguments(args);

        return f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            conIm = getArguments().getString("conIm");
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

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode ==  KeyEvent.KEYCODE_BACK))
                {
                    //This is the filter
                    if (event.getAction()!=KeyEvent.ACTION_DOWN){
                        return true;
                    }
                    else
                    {
                        dialog.dismiss();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        return true; // pretend we've processed it
                    }
                }
                else
                    return false; // pass on to be processed as normal
            }
        });
    }



    private void sendResult(Bundle results){
        ((SkillsFragment) getTargetFragment()).onDialogResult(
                getTargetRequestCode(), results);
    }


    public Dialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Damage Vulnerabilities");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Set up the input
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        View view = inflater.inflate(R.layout.dialog_dmgvul, null);
        final List<String> list = new ArrayList<String>();
        list.add("Blinded");
        list.add("Charmed");
        list.add("Deafened");
        list.add("Frightened");
        list.add("Grappled");
        list.add("Incapacitated");
        list.add("Invisible");
        list.add("Paralyzed");
        list.add("Petrified");
        list.add("Poisoned");
        list.add("Prone");
        list.add("Restrained");
        list.add("Stunned");
        list.add("Unconscious");


        final ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.simple_list_item_multiple_choice,
                android.R.id.text1,
                list));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        if(conIm != null){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> selectedItems = splitString(conIm);
                    if (selectedItems == null || selectedItems.size() == 0) {
                        return;
                    }

                    Set<String> selectedSet = new HashSet<String>(selectedItems);

                    for (int i = 0; i < list.size(); i++) {
                        if (selectedSet.contains(list.get(i))) {
                            listView.setItemChecked(i, true);
                        }
                    }
                }
            });
        }

        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Bundle results = new Bundle();
                SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();

                ArrayList<String> selections = new ArrayList<String>();
                for (int i = 0; i < checkedPositions.size(); i++) {
                    if (checkedPositions.valueAt(i)) {
                        selections.add(listView.getAdapter().getItem(checkedPositions.keyAt(i)).toString());
                    }
                }
                results.putStringArrayList("conIm", selections);
                sendResult(results);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        AlertDialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }


    private ArrayList<String> splitString(String dmgVul){
        ArrayList<String> dmgVulSplit = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(dmgVul, ";");
        String remainder = tokens.nextToken();
        while(tokens.hasMoreTokens()){
            dmgVulSplit.add(tokens.nextToken());
        }
        if(remainder != null && !remainder.isEmpty()){
            StringTokenizer remainderTokens = new StringTokenizer(remainder, ", ");
            while(remainderTokens.hasMoreTokens()){
                dmgVulSplit.add(remainderTokens.nextToken());
            }
        }

        return dmgVulSplit;
    }



}