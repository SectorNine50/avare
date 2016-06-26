/*
Copyright (c) 2015, Apps4Av Inc. (apps4av.com) 
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
    *
    *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.ds.avare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.avare.checklist.ChecklistAdapter;
import com.ds.avare.checklist.ChecklistDialog;
import com.ds.avare.checklist.ChecklistItem;
import com.ds.avare.storage.Preferences;
import com.ds.avare.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * An activity that deals with checklists: loading, creating, deleting and using.
 */
public class ChecklistActivity extends AppCompatActivity {
    private Preferences prefs;

    // Contains the buttons that relate to the normal view.
    private LinearLayout normalButtonsLayout;
    // Contains the buttons that relate to the editing view. Hidden by default.
    private LinearLayout editButtonsLayout;

    private TextView listName;
    private TextView errorText;

    // Buttons
    private ImageView listEditButton;
    private ImageView listSelectButton;
    private ImageView listAddButton;
    private ImageView listSaveButton;

    private ListView checklistView;

    private ChecklistAdapter checklistAdapter;

    // Initialized as -1 to indicate that no checklist is selected.
    private long selectedChecklistId = -1;
    private ArrayList<ChecklistItem> checklists;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);

        prefs = new Preferences(this);

        normalButtonsLayout = (LinearLayout) findViewById(R.id.normalButtonsLayout);
        editButtonsLayout = (LinearLayout) findViewById(R.id.editButtonsLayout);

        listName = (TextView) findViewById(R.id.listName);
        errorText = (TextView) findViewById(R.id.errorText);

        listEditButton = (ImageView) findViewById(R.id.listEditButton);
        listEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { editChecklist(false); }
        });

        listSelectButton = (ImageView) findViewById(R.id.listSelectButton);
        listSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChecklistDialog dialog = new ChecklistDialog();
                dialog.initChecklistDialog(ChecklistActivity.this, checklists);
                dialog.show(getSupportFragmentManager(), "ChecklistDialog");
            }
        });

        listAddButton = (ImageView) findViewById(R.id.listAddButton);
        listAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklistAdapter.addItem(null);
            }
        });

        listSaveButton = (ImageView) findViewById(R.id.listSaveButton);
        listSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { saveChecklist(); }
        });

        checklistView = (ListView) findViewById(R.id.checklistView);
    }

    /*
         * (non-Javadoc)
         *
         * @see android.app.Activity#onResume()
         */
    @Override
    public void onResume() {
        super.onResume();
        Helper.setOrientationAndOn(this);

        loadChecklistData();
        initChecklistView();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        ((MainActivity) this.getParent()).showMapTab();
    }

    /**
     * Selects the currently displayed checklist.
     * @param id The id of the checklist to be selected.  If null, no checklist is selected.
     */
    public void selectChecklist(@Nullable Long id) {
        // If the id is null, set it to -1.
        selectedChecklistId = id == null ? -1 : id;
        initChecklistView();
    }

    /**
     * Edit the current, or create a new checklist.
     * @param newList Whether or not this is a new list.
     */
    public void editChecklist(boolean newList) {
        if(newList) {
            // Generate a new checklist id based upon the largest id in the list.
            long newId = -1;
            for(ChecklistItem checklist : checklists) {
                if(checklist.id >= newId) { newId = checklist.id + 1; }
            }

            selectedChecklistId = newId;
            initChecklistView();
        }

        // TODO: Activity-specific editing adjustments.
        checklistAdapter.listEdit(true);
    }

    /**
     * Saves the checklist that's currently being edited.
     */
    public void saveChecklist() {
        for(ChecklistItem checklist : checklists) {
            if(checklist.id == selectedChecklistId) {
                // TODO: Save checklist data over existing data.
            } else {
                // TODO: Add new checklist item to list.
            }
        }

        checklistAdapter.listEdit(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveChecklistData();
        // TODO: Save activity state.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // TODO: Restore activity state.
    }

    /**
     * Loads checklists from persistent storage.
     */
    private void loadChecklistData() {
        checklists = new ArrayList<>();
        String jsonString = prefs.getLists();
        JSONArray json = null;

        try {
            json = new JSONArray(jsonString);
        } catch (JSONException e) {
            Log.e("ChecklistActivity", "loadChecklistData: Parsing JSON string from preferences.", e);
        }

        if(json == null || json.length() <= 0) { return; }

        for(int i = 0; i < json.length(); i++) {
            try {
                JSONObject obj = json.getJSONObject(i);
                checklists.add(new ChecklistItem(obj));
            } catch (JSONException e) {
                Log.e("ChecklistActivity", "loadChecklistData: Loading JSON object.", e);
            }
        }
    }

    /**
     * Saves all checklist data in persistent storage.
     */
    private void saveChecklistData() {
        JSONArray json = new JSONArray();

        for(ChecklistItem item : checklists) {
            json.put(item.getJson());
        }

        prefs.putLists(json.toString());
    }

    /**
     * Searches for the selected checklist, and displays it in the ListView.
     */
    private void initChecklistView() {
        ChecklistItem item = null;
        if(selectedChecklistId >= 0) {
            try {
                // Search the list for a matching checklist ID.
                for (ChecklistItem checklistItem : checklists) {
                    if (checklistItem.id == selectedChecklistId) {
                        item = checklistItem;
                        break;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                item = null;
            }

            // We hide the error anyway, because a non -1 selected id either exits, or is a new id.
            hideError();
        } else {
            showError("No checklist loaded!");
        }

        // Create the list adapter and configure the ListView.
        // Even if item is null, the adapter can still be initialized.
        if(checklistAdapter == null) {
            checklistAdapter = new ChecklistAdapter(this, item);
        } else {
            checklistAdapter.newList(item);
        }

        // Set the title of the toolbar.
        if(item == null) { listName.setText("Checklists"); }
        else { listName.setText(item.name); }

        checklistView.setOnItemSelectedListener(checklistAdapter);
        checklistView.setOnItemClickListener(checklistAdapter);
        checklistView.setAdapter(checklistAdapter);
    }

    /**
     * Shows the error message and hides the ListView.
     * @param message Optional message string.  Default: "An error occurred."
     */
    private void showError(@Nullable String message) {
        if(message == null || message.isEmpty()) {
            errorText.setText("An error occurred.");
        } else {
            errorText.setText(message);
        }

        errorText.setVisibility(View.VISIBLE);
        checklistView.setVisibility(View.GONE);
    }

    /**
     * Hides the error message and shows the ListView.
     */
    private void hideError() {
        errorText.setVisibility(View.GONE);
        checklistView.setVisibility(View.VISIBLE);
    }
}
