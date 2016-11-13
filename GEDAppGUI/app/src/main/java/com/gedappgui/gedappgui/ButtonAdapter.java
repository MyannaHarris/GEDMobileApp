package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by myannaharris on 11/10/16.
 */

public class ButtonAdapter extends BaseAdapter {

    private Context mContext;
    private Integer[] imageIds;

    // Gets the context so it can be used later
    public ButtonAdapter(Context c, Integer[] buttonNames) {

        mContext = c;
        imageIds = buttonNames;
    }

    @Override
    public int getCount() {
        return imageIds.length;
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

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(
                    new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                        GridView.LayoutParams.MATCH_PARENT));
            //new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageIds[position]);
        return imageView;
    }
}
