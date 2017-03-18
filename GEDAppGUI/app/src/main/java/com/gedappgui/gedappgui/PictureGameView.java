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
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
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
    private String splits;

    //names of all of the possible picture repositories
    private int[] L5_pics = {
            R.drawable.lesson5game_0,
            R.drawable.lesson5game_1,
            R.drawable.lesson5game_2,
            R.drawable.lesson5game_3,
            R.drawable.lesson5game_4,
            R.drawable.lesson5game_5
    };
    private int[] L12_pics = {
            R.drawable.lesson12game_0,
            R.drawable.lesson12game_1,
            R.drawable.lesson12game_2,
            R.drawable.lesson12game_3,
            R.drawable.lesson12game_4,
            R.drawable.lesson12game_5
    };
    private int[] L16_pics = {
            R.drawable.lesson16game_0,
            R.drawable.lesson16game_1,
            R.drawable.lesson16game_2,
            R.drawable.lesson16game_3,
            R.drawable.lesson16game_4,
            R.drawable.lesson16game_5
    };
    private int[] L19_pics = {
            R.drawable.lesson19game_0,
            R.drawable.lesson19game_1,
            R.drawable.lesson19game_2,
            R.drawable.lesson19game_3,
            R.drawable.lesson19game_4,
            R.drawable.lesson19game_5
    };

    /**
     * Constructor for making the top statement, buttons, and imageview
     * @param contextp the current activity context
     * @param conceptIDp the current concept index
     * @param lessonIDp the current lesson index
     * @param nextActivityp the next activity index
     * @param passer the data for the game
     */
    public PictureGameView(Context contextp, int conceptIDp, int lessonIDp, int nextActivityp, String passer){
        super(contextp);

        conceptid = conceptIDp;
        lessonid = lessonIDp;
        nextActivity = nextActivityp;

        context = contextp;
        statement = new TextView(context);
        result = new TextView(context);
        int[] pass_pics;
        splits = passer;


        //Change layout to vertical view
        this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setOrientation(LinearLayout.VERTICAL);

        //set layout params and padding for the top statement
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 80, 10, 80);
        statement.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setMargins(10, 10, 10, 10);

        //split the database content into an array of string to make it easier to work with
        String[] init = splits.split(",");


        statement.setText(toHTML(init[0]));
        statement.setTextSize(25);
        statement.setTextColor(Color.WHITE);
        statement.setGravity(Gravity.CENTER);
        //adding statement to top of view
        this.addView(statement);

        changer = new ImageView(context);
        //determines initial picture based on lessonid
        if (lessonid == 5){
            pass_pics = L5_pics;
            changer.setImageResource(R.drawable.lesson5game_0);
        }
        else if (lessonid == 12){
            pass_pics = L12_pics;
            changer.setImageResource(R.drawable.lesson12game_0);
        }
        else if (lessonid == 16){
            pass_pics = L16_pics;
            changer.setImageResource(R.drawable.lesson16game_0);
        }
        //must be lesson 19
        else {
            pass_pics = L19_pics;
            changer.setImageResource(R.drawable.lesson19game_0);
        }
        changer.setLayoutParams(linearLayout);

        //gridview with two columns, one row
        buttons = new GridView(contextp);
        buttons.setNumColumns(2);
        buttons.setColumnWidth(100);
        buttons.setHorizontalSpacing(10);
        buttons.setVerticalSpacing(10);

        //initialize the result text to null
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

    /**
     * Converts database strings to HTML to support superscripts
     * @param input the string to be converted
     * @return Spanned object to be passed into the setText method
     */
    public Spanned toHTML(String input) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }
}
