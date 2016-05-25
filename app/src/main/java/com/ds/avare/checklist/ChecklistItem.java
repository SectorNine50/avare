package com.ds.avare.checklist;

import java.util.ArrayList;

/**
 * @author Justin Wiley
 * This class holds data relevant to a specific checklist.
 */
public class ChecklistItem {
    public Integer id;
    public String name;
    public ArrayList<String> listItems;

    public ChecklistItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
