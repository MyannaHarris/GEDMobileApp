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
 * Last Edit: 3-20-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
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

    /**
     * Constructor
     * Gets the context and image ids so they can be used later
     * @param c Context of the activity
     * @param textsp List of strings to put in the textviews
     * @param widthp Width of the screen in pixels
     * @param heightp Height of the screen in pixels
     * @param statusBarHeightp Height of the status bar to calculate height of textviews
     */
    public TextViewAdapter(Context c, String[] textsp, int widthp, int heightp,
                           int statusBarHeightp) {

        mContext = c;
        texts = textsp;
        width = widthp;
        height = heightp;
        statusBarHeight = statusBarHeightp;
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
     * Clears the adapter so no textviews are in it
     */
    public void clear() {
        texts = new String[0];
    }

    /**
     * Sets new adapter contents
     * @param textsp New strings for textviews
     */
    public void setTexts(String[] textsp) {
        texts = textsp;
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

            textView.setHeight((height - statusBarHeight - 30) / (texts.length / 2) - 20);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.matchGameText));
            textView.setPadding(25, 8, 25, 8);
            textView.setMaxLines(2);
        } else {
            textView = (TextView) convertView;
            if (Build.VERSION.SDK_INT < 16) {
                // Sets Drawable as background on older API
                textView.setBackgroundDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.match_game_unselected));
            } else {
                textView.setBackground(ContextCompat.getDrawable(mContext,
                        R.drawable.match_game_unselected));
            }
        }

        textView.setTag(texts[position]);
        textView.setText(toHTML(texts[position]));
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

    /**
     * Converts database strings to HTML to support superscripts
     * @param input the string to be converted
     * @return Spanned object to be passed into the setText method
     */
    public Spanned toHTML(String input) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(input,Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }

    }
}
