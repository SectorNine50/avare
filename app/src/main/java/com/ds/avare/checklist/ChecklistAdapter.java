package com.ds.avare.checklist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.avare.R;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * This is a list adapter for displaying checklist items.
 */
public class ChecklistAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private Context context;
    private ArrayList<LineItem> items;

    // Used to visually shrink an item on deselection.
    private ChecklistItemLayout currentlySelectedView;

    // Determines whether or not the list should be displayed as editable.
    private boolean editing = false;

    public ChecklistAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    public ChecklistAdapter(Context context, @Nullable ChecklistItem checklist) {
        this.context = context;
        newList(checklist);
    }

    /**
     * Adds a checklist item to the existing list of items.
     * @param name Name of checklist item.
     */
    public void addItem(@Nullable String name) {
        if(name == null || name.isEmpty()) {
            name = "New Task";
        }

        items.add(new LineItem(name, false));
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public ArrayList<String> getItems() {
        ArrayList<String> checklistItems = new ArrayList<>();

        for(LineItem item : items) {
            checklistItems.add(item.name);
        }

        return checklistItems;
    }

    /**
     * Sets a new checklist to display.
     * @param checklist The checklist to display.
     */
    public void newList(ChecklistItem checklist) {
        if(items == null) { items = new ArrayList<>(); }
        else { items.clear(); }

        if(checklist == null || checklist.listItems == null) { return; }

        for(String name : checklist.listItems) {
            items.add(new LineItem(name, false));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(items == null) { return 0; }

        return items.size();
    }

    @Override
    public Object getItem(int position) {
        if(items == null || items.isEmpty() ||
                position < 0 || position >= items.size()) {
            return null;
        }

        return items.get(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = ViewGroup.inflate(context, R.layout.checklist_item, parent);
        }

        LineItem item = items.get(position);
        if(item == null) { return null; }

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        EditText itemNameEdit = (EditText) convertView.findViewById(R.id.itemNameEdit);
        ImageView itemCheck = (ImageView) convertView.findViewById(R.id.itemCheck);

        if(!editing) {
            itemNameEdit.setVisibility(View.GONE);
            itemName.setText(item.name);
            // Default image in the layout is unchecked.
            if (item.checked) {
                itemCheck.setImageResource(R.drawable.checkbox_marked_circle_outline);
            }
        } else {
            itemNameEdit.setVisibility(View.VISIBLE);
            itemNameEdit.setText(item.name);

            itemName.setVisibility(View.GONE);

            itemCheck.setImageResource(R.drawable.delete_forever);
        }

        return convertView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ChecklistItemLayout newlySelected = (ChecklistItemLayout) view;

        if(newlySelected != null) {
            if(currentlySelectedView != null) {
                currentlySelectedView.itemDeselected();
            }

            currentlySelectedView = newlySelected;
            currentlySelectedView.itemSelected();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(currentlySelectedView != null) {
            currentlySelectedView.itemDeselected();
        }

        currentlySelectedView = null;
    }

    /**
     * Toggles the check of a checklist item.
     * @param position The position of the item in the list.
     * @param view The view of the item.
     * @return The current checked state. (True == checked, False == unchecked)
     */
    public boolean itemCheckClicked(int position, ChecklistItemLayout view) {
        LineItem item = items.get(position);

        if (item.checked) {
            item.checked = false;
            view.itemUnchecked();
        } else {
            item.checked = true;
            view.itemChecked();
        }

        return item.checked;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == parent.getSelectedItemPosition()) {
            // TODO: Clicked logic.
        } else {
            ListView listView = (ListView) parent;
            listView.setSelection(position);
            listView.smoothScrollToPosition(position);
        }
    }

    /**
     * Toggles the list in and out of edit mode.
     * @param editing Whether or not this list is being edited.
     */
    public void listEdit(boolean editing) {
        this.editing = editing;
        notifyDataSetInvalidated();
    }

    /**
     * This subclass holds the state data for each checklist line item.
     */
    protected class LineItem {
        public String name;
        public boolean checked;

        public LineItem() {}

        public LineItem(String name, boolean checked) {
            this.name = name;
            this.checked = checked;
        }
    }
}
