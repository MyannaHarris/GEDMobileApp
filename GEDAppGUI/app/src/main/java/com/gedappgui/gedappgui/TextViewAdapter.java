package com.gedappgui.gedappgui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by myannaharris on 11/12/16.
 */

public class TextViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[] buttonText;

    // Gets the context so it can be used later
    public TextViewAdapter(Context c, String[] buttonNames) {

        mContext = c;
        buttonText = buttonNames;
    }

    @Override
    public int getCount() {
        return buttonText.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView btn;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            btn = new TextView(mContext);
            btn.setLayoutParams(
                    new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                            GridView.LayoutParams.MATCH_PARENT));
            btn.setPadding(8, 8, 8, 8);
        }
        else {
            btn = (TextView) convertView;
        }

        btn.setText(buttonText[position]);
        // homeButtons is an array of strings
        // btn.setId(position + 1);
        btn.setGravity(Gravity.CENTER);
        //btn.setTextSize(sp, 20);

        return btn;
    }
}