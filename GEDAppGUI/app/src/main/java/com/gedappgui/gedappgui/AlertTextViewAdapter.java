/*
 * AlertTextViewAdapter.java
 *
 * Alert dialog TextView Adapter
 *
 * Adapter to fill alert dialog with TextViews
 *
 * http://stackoverflow.com/questions/16103344/android-text-size-on-canvas-differ-from-text-size-in-textview
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 3-21-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlertTextViewAdapter extends BaseAdapter {

    // Hold context of activity that called adapter
    private Context mContext;

    // Hold the texts for the textviews
    private String[] texts;

    // Size of screen
    private int height;

    // Selected item
    private int selectedPosition = -1;

    /**
     * Constructor
     * Gets the context and image ids so they can be used later
     * @param c Context of the activity
     * @param textsp List of strings to put in the textviews
     */
    public AlertTextViewAdapter(Context c, String[] textsp, int heightp) {

        mContext = c;
        texts = textsp;
        height = heightp;
    }

    /**
     * Gets the number of items to put in the view
     * @return texts.length - Number of textviews
     */
    @Override
    public int getCount() {
        return texts.length;
    }

    /**
     * Does not do anything but needed to implement BaseAdapter
     * @param position Position in the gridview
     * @return null
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Does not do anything but needed to implement BaseAdapter
     * @param position Position in the gridview
     * @return position - Position in the gridview
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Creates the components for the adapter
     * @param position Position of textview in gridview
     * @param convertView Old view that is at that position
     * @param parent Parent group of the view
     * @return textView - The textview at the position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView;
        if (convertView == null) {
             // If it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            textView.setTextSize(convertPixelsToDp(height / 30, mContext));

            textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorBodyText));

            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            textView.setPadding(10, 8, 10, 8);
        } else {
            textView = (TextView) convertView;
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorBodyText));
        }

        textView.setText(texts[position]);
        return textView;
    }

    /**
     * Change pixel measurement into dp measurement
     * @param px Pixel measurement
     * @param context Context of the activity
     * @return dp - dp Measurement
     */
    public static float convertPixelsToDp(float px,Context context){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }
}
