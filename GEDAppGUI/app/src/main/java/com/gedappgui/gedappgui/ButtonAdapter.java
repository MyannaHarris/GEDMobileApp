/*
 * ButonAdapter.java
 *
 * Buton Adapter
 *
 * Adapter to fill gridview with imageviews
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 11-13-16
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ButtonAdapter extends BaseAdapter {

    // Hold context of activity that called adapter
    private Context mContext;
    // Hold the ids of the images from the drawable folder
    private Integer[] imageIds;

    /*
     * Constructor
     * Gets the context and image ids so they can be used later
     */
    public ButtonAdapter(Context c, Integer[] buttonNames) {

        mContext = c;
        imageIds = buttonNames;
    }

    /*
     * Gets the number of itams to put in the view
     * returns imageIds.length
     */
    @Override
    public int getCount() {
        return imageIds.length;
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

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                        GridView.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageIds[position]);
        return imageView;
    }
}
