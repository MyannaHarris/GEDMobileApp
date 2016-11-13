package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by myannaharris on 11/10/16.
 */

public class ButtonAdapter extends BaseAdapter {

    private Context mContext;

    // Gets the context so it can be used later
    public ButtonAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return homeButtons.length;
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
            btn.setLayoutParams(new GridView.LayoutParams(100, 55));
            btn.setPadding(8, 8, 8, 8);
        }
        else {
            btn = (TextView) convertView;
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn.setLayoutParams(params);

        btn.setText(homeButtons[position]);
        // homeButtons is an array of strings
        btn.setId(position + 1);
        btn.setGravity(Gravity.CENTER);
        //btn.setTextSize(sp, 20);

        return btn;
    }

    private String[] homeButtons = {
            "Continue Lesson", "Achievements", "Learn", "Tools", "Play", "Settings"};
}
