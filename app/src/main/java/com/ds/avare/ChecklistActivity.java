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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.avare.checklist.ChecklistAdapter;
import com.ds.avare.checklist.ChecklistItem;
import com.ds.avare.checklist.ChecklistItemLayout;
import com.ds.avare.utils.Helper;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * An activity that deals with checklists: loading, creating, deleting and using.
 */
public class ChecklistActivity extends Activity implements View.OnClickListener {

    private TextView listNameView;
    private TextView listSelectButton;
    private ListView checklistView;

    private ChecklistAdapter checklistAdapter;

    private ChecklistItem selectedChecklist;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);

        listNameView = (TextView) findViewById(R.id.listName);
        listSelectButton = (TextView) findViewById(R.id.listSelectButton);
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

        // Load missing data into the selected checklist item.
        if(selectedChecklist == null || selectedChecklist.id < 0
                || selectedChecklist.name == null || selectedChecklist.name.isEmpty()) {
            // TODO: Prompt for checklist selection/creation.
        }
        else if(selectedChecklist.listItems == null || selectedChecklist.listItems.isEmpty()) {
            selectedChecklist = loadChecklist(selectedChecklist.id);
        }
        // TODO: Complete logic.

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
     * This method is being utilized to register when a checklist item has been checked, and to
     * progress the checklist to the next item.
     * @param v Clicked view (unused).
     */
    @Override
    public void onClick(View v) {
        // TODO: This is going to be a bug.  Need to figure out a way to make sure that only the currently active objects are being triggered.
        ChecklistItemLayout itemView = (ChecklistItemLayout) checklistView.getSelectedView();
        int itemPosition = checklistView.getSelectedItemPosition();

        checklistAdapter.itemCheckClicked(itemPosition, itemView);

        checklistView.smoothScrollToPosition(itemPosition +1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // TODO: Save activity state.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // TODO: Restore activity state.
    }

    private ArrayList<ChecklistItem> getChecklists() {
        // TODO: Implement.
        return null;
    }

    private ChecklistItem loadChecklist(int id) {
        // TODO: Implement.
        return null;
    }

    private void initChecklistView() {
        checklistAdapter = new ChecklistAdapter(this, this);

        checklistView.setOnItemSelectedListener(checklistAdapter);
        // TODO: Implement.
    }
}
