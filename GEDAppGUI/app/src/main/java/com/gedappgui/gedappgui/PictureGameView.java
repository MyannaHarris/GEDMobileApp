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
 * Last Edit: 5-1-17
 *
 */
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PictureGameView extends LinearLayout {

    // Next intent information
    private int conceptID;
    private int lessonID;
    private int redo;
    private int totalRetries;
    private int nextActivity;

    private TextView statement;
    private TextView result;
    private TextView counter;
    private TextView counter_title;
    private GridView buttons;
    private Context context;
    private ImageView changer;
    private Button end;
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
    public PictureGameView(Context contextp, int conceptIDp, int lessonIDp, int nextActivityp,
                           String passer, int redop, int totalRetriesp){
        super(contextp);

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;
        redo = redop;
        totalRetries = totalRetriesp;

        context = contextp;
        statement = new TextView(context);
        result = new TextView(context);
        counter = new TextView(context);
        //counter_title = new TextView(context)
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
        String[] init = splits.split(";");


        statement.setText(toHTML(init[0]));
        statement.setTextSize(25);
        statement.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        statement.setGravity(Gravity.CENTER);
        //adding statement to top of view
        this.addView(statement);

        //counter for endless play
        counter.setText("Correct Questions: 0");
        counter.setTextSize(20);
        counter.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        counter.setGravity(Gravity.CENTER);
        counter.setLayoutParams(linearLayout);

        changer = new ImageView(context);
        //determines initial picture based on lessonid
        if (lessonID == 5){
            pass_pics = L5_pics;
            changer.setImageResource(R.drawable.lesson5game_0);
        }
        else if (lessonID == 12){
            pass_pics = L12_pics;
            changer.setImageResource(R.drawable.lesson12game_0);
        }
        else if (lessonID == 16){
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
        result.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        result.setGravity(Gravity.CENTER);

        end = new Button(context);
        end.setLayoutParams(linearLayout);
        end.setText("End Game");
        end.setTextSize(20);
        end.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        end.setGravity(Gravity.CENTER);

        end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //starts GameEnd activity
                Intent intent = new Intent(context, GameEnd.class);
                intent.putExtra("next_activity", nextActivity);
                intent.putExtra("conceptID", conceptID);
                intent.putExtra("lessonID", lessonID);
                intent.putExtra("redoComplete", redo);
                intent.putExtra("totalRetries",totalRetries);
                context.startActivity(intent);
            }
        });

        buttons.setLayoutParams(linearLayout);
        //call button adapter to put buttons in gridview and create listeners for the buttons
        buttons.setAdapter(new ButtonAdapter(context, texts, changer, splits, statement, pass_pics,result,
        lessonID,conceptID,nextActivity,counter, redo, totalRetries));
        //add gridview to layout
        this.addView(buttons);
        if (nextActivity != 0) {
            this.addView(end);
            this.addView(counter);
        }
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
