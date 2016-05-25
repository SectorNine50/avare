package com.ds.avare.checklist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ds.avare.R;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * This is a list adapter for displaying checklist items.
 */
public class ChecklistAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {
    private Context context;
    private View.OnClickListener clickListener;
    private ArrayList<LineItem> items;

    // Used to visually shrink an item on deselection.
    private ChecklistItemLayout currentlySelectedView;

    public ChecklistAdapter(Context context, View.OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        items = new ArrayList<>();
    }

    public ChecklistAdapter(Context context, View.OnClickListener clickListener, ChecklistItem checklist) {
        this.context = context;
        this.clickListener = clickListener;
        items = new ArrayList<>();

        if(checklist == null || checklist.listItems == null) { return; }

        for(String name : checklist.listItems) {
            items.add(new LineItem(name, false));
        }
    }

    /**
     * Adds a checklist item to the existing list of items.
     * @param name Name of checklist item.
     * @param checked Whether or not this item has been checked.
     */
    public void addItem(String name, boolean checked) {
        items.add(new LineItem(name, checked));
    }

    @Override
    public int getCount() {
        if(items == null) { return 0; }

        return items.size();
    }

    @Override
    public Object getItem(int position) {
        if(items == null || position < 0 || position >= items.size()) {
            return null;
        }

        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = ViewGroup.inflate(context, R.layout.checklist_item, parent);
        }

        LineItem item = items.get(position);
        if(item == null) { return null; }

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        itemName.setText(item.name);

        ImageView itemCheck = (ImageView) convertView.findViewById(R.id.itemCheck);
        itemCheck.setOnClickListener(clickListener);
        // Default image in the layout is unchecked.
        if(item.checked) { itemCheck.setImageResource(R.drawable.checkbox_marked_circle_outline); }

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
     *
     * @param position The position of the item in the list.
     * @param view The view of the item.
     * @return The current checked state. (True == checked, False == unchecked)
     */
    public boolean itemCheckClicked(int position, ChecklistItemLayout view) {
        LineItem item = items.get(position);

        if(item.checked) {
            item.checked = false;
            view.itemUnchecked();
        } else {
            item.checked = true;
            view.itemChecked();
        }

        return item.checked;
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
