package com.ds.avare.checklist;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ds.avare.R;
import com.ds.avare.utils.Helper;

/**
 * @author Justin Wiley
 * This class is used to simplify the view changes on selection and deselection of checklist items.
 */
public class ChecklistItemLayout extends RelativeLayout {
    // Size of item text.
    private final float NORMAL_TEXT_SIZE = 20.0f;
    private final float SELECTED_TEXT_SIZE = 28.0f;

    // Check box image is same width and height.
    private final int NORMAL_IMG_SIZE = 35;
    private final int SELECTED_IMG_SIZE = 40;

    // Color of text and other items.
    private final int NORMAL_COLOR = 0xFFF;
    private final int SELECTED_COLOR = 0x000;

    // Color of item background.
    private final int NORMAL_BG_COLOR = 0x000;
    private final int SELECTED_BG_COLOR = 0xFFF;

    private TextView itemName;
    private ImageView itemCheck;

    public ChecklistItemLayout(Context context) {
        super(context);
        init();
    }

    public ChecklistItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChecklistItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ChecklistItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Initializes the layout variables, must be called before any other functions.
     */
    private void init() {
        itemName = (TextView) findViewById(R.id.itemName);
        itemCheck = (ImageView) findViewById(R.id.itemCheck);
    }

    /**
     * Changes the visual state of this item to indicate that its been selected.
     */
    public void itemSelected() {
        this.setBackgroundColor(SELECTED_BG_COLOR);

        itemName.setTextColor(SELECTED_COLOR);
        itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, SELECTED_TEXT_SIZE);

        itemCheck.setColorFilter(SELECTED_COLOR);
        itemCheck.requestLayout();
        itemCheck.getLayoutParams().height = Math.round(Helper.getDpiToPix(getContext()) * SELECTED_IMG_SIZE);
        itemCheck.getLayoutParams().width = Math.round(Helper.getDpiToPix(getContext()) * SELECTED_IMG_SIZE);
    }

    /**
     * Changes the visual state of this item to indicate that its not selected.
     */
    public void itemDeselected() {
        this.setBackgroundColor(NORMAL_BG_COLOR);

        itemName.setTextColor(NORMAL_COLOR);
        itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, NORMAL_TEXT_SIZE);

        itemCheck.setColorFilter(NORMAL_COLOR);
        itemCheck.requestLayout();
        itemCheck.getLayoutParams().height = Math.round(Helper.getDpiToPix(getContext()) * NORMAL_IMG_SIZE);
        itemCheck.getLayoutParams().width = Math.round(Helper.getDpiToPix(getContext()) * NORMAL_IMG_SIZE);
    }

    /**
     * Changes the visual state of this item to indicate that its been checked.
     */
    public void itemChecked() {
        itemCheck.setImageResource(R.drawable.checkbox_marked_circle_outline);
    }

    /**
     * Changes the visual state of this item to indicate that it is not checked.
     */
    public void itemUnchecked() {
        itemCheck.setImageResource(R.drawable.checkbox_blank_circle_outline);
    }

    /**
     * Toggles this item into an editable state.
     * @param editing Whether or not this list is being edited.
     */
    public void itemEdit(boolean editing) {
        // TODO: Implement.
    }
}
