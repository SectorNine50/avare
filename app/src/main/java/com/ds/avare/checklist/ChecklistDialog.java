package com.ds.avare.checklist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.avare.ChecklistActivity;
import com.ds.avare.R;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * This class handles the functionality of the dialog box for selecting checklists.
 */
public class ChecklistDialog extends DialogFragment implements AdapterView.OnItemClickListener {
    private ChecklistActivity checklistActivity;
    private ListView checklistList;
    private TextView errorText;
    private ImageView newChecklistButton;

    private ArrayList<ChecklistItem> checklists;
    private ChecklistDialogAdapter checklistDialogAdapter;

    public void initChecklistDialog(ChecklistActivity checklistActivity, ArrayList<ChecklistItem> checklists) {
        this.checklistActivity = checklistActivity;
        this.checklists = checklists;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("AndroidLintInflateParams")
        View dialogView = inflater.inflate(R.layout.checklist_dialog, null);

        checklistDialogAdapter = new ChecklistDialogAdapter(this.getContext(), checklists);

        checklistList = (ListView) dialogView.findViewById(R.id.checklistList);
        checklistList.setAdapter(checklistDialogAdapter);
        checklistList.setOnItemClickListener(this);

        errorText = (TextView) dialogView.findViewById(R.id.dialogErrorText);
        newChecklistButton = (ImageView) dialogView.findViewById(R.id.newChecklistButton);

        newChecklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklistActivity.editChecklist(true);
                dismiss();
            }
        });

        if(checklists == null || checklists.isEmpty()) {
            showError("No checklists available!");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checklistActivity.selectChecklist(parent.getItemIdAtPosition(position));
        dismiss();
    }

    /**
     * Shows the error message and hides the ListView.
     * @param error Optional error message.  Defaults to "An error has occured!".
     */
    private void showError(@Nullable String error) {
        if(error == null || error.isEmpty()) {
            errorText.setText("An error has occurred!");
        } else {
            errorText.setText(error);
        }

        errorText.setVisibility(View.VISIBLE);
        checklistList.setVisibility(View.GONE);
    }

    /**
     * Hides the error message and shows the ListView.
     */
    private void hideError() {
        errorText.setVisibility(View.GONE);
        checklistList.setVisibility(View.VISIBLE);
    }

    /**
     * This class is only used within the ChecklistDialog class.
     */
    protected class ChecklistDialogAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<ChecklistItem> items;

        public ChecklistDialogAdapter(Context context, ArrayList<ChecklistItem> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            if(items == null || items.isEmpty()) { return 0; }
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            if(items == null || items.isEmpty() ||
                    position < 0 || position >= items.size()) { return null; }

            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            if(items == null || items.isEmpty() ||
                    position < 0 || position >= items.size()) { return 0; }

            return items.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = View.inflate(context, R.layout.checklist_item, parent);
            }

            TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
            itemName.setText(items.get(position).name);

            // We'll just hide the checkmark so we can reuse this layout.
            ImageView itemCheck = (ImageView) convertView.findViewById(R.id.itemCheck);
            itemCheck.setVisibility(View.GONE);

            return convertView;
        }
    }
}
