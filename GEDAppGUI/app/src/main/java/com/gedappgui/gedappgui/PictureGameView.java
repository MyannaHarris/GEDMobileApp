package com.gedappgui.gedappgui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by James on 2/17/2017.
 */

public class PictureGameView extends LinearLayout {

    private TextView statement;
    private TextView result;
    private GridView buttons;
    private Button truthbutton;
    private Context context;
    private ImageView changer;
    private String[] texts = {
            "TRUE",
            "FALSE"
    };
    private String splits = "|-4| - 2 = 2,t,|-2| = 3,f,|4| - |-4| = 4,f,-5 + |20| = 15,t";
    private int[] abs_pics = {
            R.drawable.sprite_dragon,
            R.drawable.settings,
            R.drawable.home_screen,
            R.drawable.home_screen_continue,
            R.drawable.cylinder

    };

    public PictureGameView(Context contextp){
        super(contextp);

        context = contextp;
        statement = new TextView(context);
        result = new TextView(context);
        int[] pass_pics;

        if (true){
            pass_pics = abs_pics;
        }

        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 80, 10, 80);
        statement.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setMargins(10, 10, 10, 10);

        statement.setText("testing");
        statement.setTextSize(20);
        statement.setTextColor(Color.WHITE);
        statement.setGravity(Gravity.CENTER);

        this.addView(statement);

        changer = new ImageView(context);
        changer.setImageResource(R.drawable.cone);
        changer.setLayoutParams(linearLayout);

        buttons = new GridView(contextp);
        buttons.setNumColumns(2);
        buttons.setColumnWidth(100);
        buttons.setHorizontalSpacing(10);
        buttons.setVerticalSpacing(10);

        layoutParams.setMargins(10, 80, 10, 80);
        result.setLayoutParams(layoutParams);
        result.setText("");
        result.setTextSize(20);
        result.setTextColor(Color.WHITE);
        result.setGravity(Gravity.CENTER);



        buttons.setLayoutParams(linearLayout);
        buttons.setAdapter(new ButtonAdapter(context, texts, changer, splits, statement, pass_pics,result ));

        this.addView(buttons);




        this.addView(result);



        this.addView(changer);


    }
}
