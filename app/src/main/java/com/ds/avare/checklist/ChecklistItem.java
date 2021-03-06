package com.ds.avare.checklist;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * This class holds data relevant to a specific checklist.
 */
public class ChecklistItem {
    private final String JSON_ID = "id";
    private final String JSON_NAME = "name";
    private final String JSON_LIST_ITEMS = "listItems";

    public Long id;
    public String name;
    public ArrayList<String> listItems;

    public ChecklistItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public ChecklistItem(JSONObject obj) throws JSONException {
        id = obj.getLong(JSON_ID);
        name = obj.getString(JSON_NAME);
        listItems = (ArrayList<String>) obj.get(JSON_LIST_ITEMS);
    }

    /**
     * Generates a JSONObject from the contents of this instance.
     * @return A JSONObject representation of this instance, or null if an error occurs.
     */
    public JSONObject getJson() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", JSON_ID);
            obj.put("name", JSON_NAME);
            obj.put("listItems", JSON_LIST_ITEMS);
        } catch (JSONException e) {
            Log.e("ChecklistItem", "getJson: ", e);
            return null;
        }

        return obj;
    }
}
