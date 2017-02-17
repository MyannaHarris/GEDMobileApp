/*
 * ButonAdapter.java
 *
 * Buton Adapter
 *
 * Adapter to fill gridview with buttons
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-17-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class ButtonAdapter extends BaseAdapter {

    // Hold context of activity that called adapter
    private Context mContext;
    // Hold the ids of the images from the drawable folder
    private String[] buttonNames;

    /*
     * Constructor
     * Gets the context and image ids so they can be used later
     */
    public ButtonAdapter(Context c, String[] buttonNamesp) {

        mContext = c;
        buttonNames = buttonNamesp;
    }


    /*
     * Gets the number of itams to put in the view
     *
     * returns imageIds.length
     */
    @Override
    public int getCount() {
        return buttonNames.length;
    }

    /*
     * Does not do anything but needed to implement BaseAdapter
     * returns null
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /*
     * Does not do anything but needed to implement BaseAdapter
     * returns the position sent to the method
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Creates the components for the adapter
     * returns an imageview
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Button button;
        if (convertView == null) {
             //if it's not recycled, initialize some attributes
            button = new Button(mContext);
            button.setLayoutParams(
                    new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (Button) convertView;
        }

        button.setText(buttonNames[position]);
        return button;
    }
}
