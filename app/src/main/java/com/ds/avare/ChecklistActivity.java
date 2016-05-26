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
    private final String CHECKLIST_FILE = "checklists.dat";

    private Preferences prefs;

    private TextView listNameView;
    private ImageView listSelectButton;
    private ListView checklistView;
    private TextView errorText;

    private ChecklistAdapter checklistAdapter;

    // Initialized as -1 to indicate that no checklist is selected.
    private int selectedChecklist = -1;
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

        listNameView = (TextView) findViewById(R.id.listName);
        listSelectButton = (ImageView) findViewById(R.id.listSelectButton);
        checklistView = (ListView) findViewById(R.id.checklistView);
        errorText = (TextView) findViewById(R.id.errorText);

        listSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChecklistDialog dialog = new ChecklistDialog();
                dialog.initChecklistDialog(checklists);
                dialog.show(getSupportFragmentManager(), "ChecklistDialog");
            }
        });
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

        loadChecklists();
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

    public void selectChecklist(int id) {
        selectedChecklist = id;
        initChecklistView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveChecklists();
        // TODO: Save activity state.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // TODO: Restore activity state.
    }

    private void loadChecklists() {
        checklists = new ArrayList<>();
        String jsonString = prefs.getLists();
        JSONArray json = null;

        try {
            json = new JSONArray(jsonString);
        } catch (JSONException e) {
            Log.e("ChecklistActivity", "loadChecklists: Parsing JSON string from preferences.", e);
        }

        if(json == null || json.length() <= 0) { return; }

        for(int i = 0; i < json.length(); i++) {
            try {
                JSONObject obj = json.getJSONObject(i);
                checklists.add(new ChecklistItem(obj));
            } catch (JSONException e) {
                Log.e("ChecklistActivity", "loadChecklists: Loading JSON object.", e);
            }
        }
    }

    private void saveChecklists() {
        JSONArray json = new JSONArray();

        for(ChecklistItem item : checklists) {
            json.put(item.getJson());
        }

        prefs.putLists(json.toString());
    }

    private void initChecklistView() {
        ChecklistItem item = null;
        if(selectedChecklist >= 0) {
            try {
                item = checklists.get(selectedChecklist);
            } catch (IndexOutOfBoundsException e) {
                item = null;
            }

            hideError();
        } else {
            showError("No checklist loaded!");
        }

        if(checklistAdapter == null) {
            checklistAdapter = new ChecklistAdapter(this, item);
        } else {
            checklistAdapter.newList(item);
        }

        checklistView.setOnItemSelectedListener(checklistAdapter);
        checklistView.setOnItemClickListener(checklistAdapter);
        checklistView.setAdapter(checklistAdapter);
    }

    private void showError(@Nullable String message) {
        if(message == null || message.isEmpty()) {
            errorText.setText("An error occurred.");
        } else {
            errorText.setText(message);
        }

        errorText.setVisibility(View.VISIBLE);
        checklistView.setVisibility(View.GONE);
    }

    private void hideError() {
        errorText.setVisibility(View.GONE);
        checklistView.setVisibility(View.VISIBLE);
    }
}
