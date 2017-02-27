/*
 * TextViewAdapter.java
 *
 * TextView Adapter
 *
 * Adapter to fill gridview with TextViews
 *
 * http://stackoverflow.com/questions/16103344/android-text-size-on-canvas-differ-from-text-size-in-textview
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-12-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class TextViewAdapter extends BaseAdapter {

    // Hold context of activity that called adapter
    private Context mContext;

    // Hold the texts for the textviews
    private String[] texts;

    // Size of screen
    private int width;
    private int height;
    private int statusBarHeight;

    /*
     * Constructor
     * Gets the context and image ids so they can be used later
     */
    public TextViewAdapter(Context c, String[] textsp, int widthp, int heightp,
                           int statusBarHeightp) {

        mContext = c;
        texts = textsp;
        width = widthp;
        height = heightp;
        statusBarHeight = statusBarHeightp;
    }


    /*
     * Gets the number of itams to put in the view
     *
     * returns imageIds.length
     */
    @Override
    public int getCount() {
        return texts.length;
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

        TextView textView;
        if (convertView == null) {
             // If it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            textView.setTextSize(convertPixelsToDp(height / 17, mContext));
            textView.setLayoutParams(
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

            if (Build.VERSION.SDK_INT < 16) {
                // Sets Drawable as background on older API
                textView.setBackgroundDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.match_game_unselected));
            } else {
                textView.setBackground(ContextCompat.getDrawable(mContext,
                        R.drawable.match_game_unselected));
            }

            textView.setHeight((height - statusBarHeight - 15) / (texts.length / 2) - 20);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.matchGameText));
            textView.setPadding(8, 8, 8, 8);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(texts[position]);
        return textView;
    }

    /*
     * Change pixel measurement into dp measurement
     */
    public static float convertPixelsToDp(float px,Context context){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }
}
