package com.gedappgui.gedappgui;

/*
 * PictureGameView.java
 *
 * Picture game
 *
 * View runs the picture game
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-19-17
 *
 */
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
    private Context context;
    private ImageView changer;
    private int conceptid;
    private int lessonid;
    private int nextActivity;
    private String[] texts = {
            "TRUE",
            "FALSE"
    };
    private String splits = "|-4| - 2 = 2,t,|-2| = 3,f,|4| - |-4| = 4,f,-5 + |20| = 15,t," +
            "-|-3| + 1 = -2,t,|-10| - |-11| = -1,t,|-19| + |-4| = -25,f,|-7| * 5 = 35,t," +
            "|3| * -|-6| = 18,f,|-10| / -2 = -5,t";
    private int[] abs_pics = {
            R.drawable.lesson5game_0,
            R.drawable.lesson5game_1,
            R.drawable.lesson5game_2,
            R.drawable.lesson5game_3,
            R.drawable.lesson5game_4,
            R.drawable.lesson5game_5


    };

    public PictureGameView(Context contextp, int conceptIDp, int lessonIDp, int nextActivityp){
        super(contextp);

        conceptid = conceptIDp;
        lessonid = lessonIDp;
        nextActivity = nextActivityp;

        context = contextp;
        statement = new TextView(context);
        result = new TextView(context);
        int[] pass_pics;

        //determines initial picture based on lessonid
        if (true){
            pass_pics = abs_pics;
        }
        //Change layout to vertical view
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

        String[] init = splits.split(",");

        statement.setText(init[0]);
        statement.setTextSize(20);
        statement.setTextColor(Color.WHITE);
        statement.setGravity(Gravity.CENTER);
        //adding statement to top of view
        this.addView(statement);

        changer = new ImageView(context);
        if (lessonid == 5) {
            changer.setImageResource(R.drawable.lesson5game_0);
        }
        changer.setLayoutParams(linearLayout);

        //gridview with two columns, one row
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
        //call button adapter to put buttons in gridview and create listeners for the buttons
        buttons.setAdapter(new ButtonAdapter(context, texts, changer, splits, statement, pass_pics,result,
        lessonid,conceptid,nextActivity));
        //add gridview to layout
        this.addView(buttons);
        //add result string to layout
        this.addView(result);
        //add imageview to layout
        this.addView(changer);


    }
}
