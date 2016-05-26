package com.ds.avare.checklist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.avare.R;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * This class handles the functionality of the dialog box for selecting checklists.
 */
public class ChecklistDialog extends DialogFragment implements AdapterView.OnItemClickListener {
    private ListView checklistList;
    private TextView errorText;
    private ImageView newChecklistButton;

    private ArrayList<ChecklistItem> checklists;

    public void initChecklistDialog(ArrayList<ChecklistItem> checklists) {
        this.checklists = checklists;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("AndroidLintInflateParams")
        View dialogView = inflater.inflate(R.layout.checklist_dialog, null);

        checklistList = (ListView) dialogView.findViewById(R.id.checklistList);
        errorText = (TextView) dialogView.findViewById(R.id.dialogErrorText);
        newChecklistButton = (ImageView) dialogView.findViewById(R.id.newChecklistButton);

        if(checklists == null || checklists.isEmpty()) {
            showError("No checklists available!");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private void showError(@Nullable String error) {
        if(error == null || error.isEmpty()) {
            errorText.setText("An error has occurred!");
        } else {
            errorText.setText(error);
        }

        errorText.setVisibility(View.VISIBLE);
        checklistList.setVisibility(View.GONE);
    }

    private void hideError() {
        errorText.setVisibility(View.GONE);
        checklistList.setVisibility(View.VISIBLE);
    }
}
