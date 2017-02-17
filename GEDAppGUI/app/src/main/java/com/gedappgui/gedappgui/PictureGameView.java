package com.gedappgui.gedappgui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by James on 2/17/2017.
 */

public class PictureGameView extends LinearLayout {

    private TextView statement;
    private GridView buttons;
    private Button truthbutton;
    private Context contextp;
    private String[] texts = {
            "TRUE",
            "FALSE"
    };

    public PictureGameView(Context contextp){
        super(contextp);


        statement = new TextView(contextp);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        statement.setLayoutParams(layoutParams);

        statement.setText("statement");
        statement.setTextSize(20);
        statement.setTextColor(Color.WHITE);
        statement.setGravity(Gravity.CENTER);

        this.addView(statement);

        buttons = new GridView(contextp);
        buttons.setNumColumns(2);
        buttons.setHorizontalSpacing(10);
        buttons.setVerticalSpacing(10);
        buttons.setBackgroundColor(ContextCompat.getColor(contextp, R.color.bucketGameBG));



        truthbutton = new Button(contextp);
        truthbutton.setText("button");


    }
}
