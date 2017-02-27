package com.gedappgui.gedappgui;

/*
 * ChemistryGameView.java
 *
 * Chemistry game
 *
 * View runs the Chemistry game
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-19-17
 */

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ScrollView;

import java.util.ArrayList;


public class MadlibGameView extends RelativeLayout{

    private Context context;
    ScrollView scroll;

    // ID's for the learn cycle
    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Game texts
    private ArrayList<ArrayList<String>>texts;
    private int currQuestion = 0;
    private ArrayList<String> questionTexts;
    private ArrayList<String> answerTexts;

    private TextView adjective;
    private TextView verb;
    private TextView name;
    private TextView noun;

    private EditText adj;
    private EditText vrb;
    private EditText nm;
    private EditText nn;

    private TextView sentence;
    private Button submit;

    private int width;
    private int height;

    int selectedAnswer;
//, ArrayList<ArrayList<String>> textsp
    public MadlibGameView(Context contextp,
                          int conceptIDp, int lessonIDp, int nextActivityp,
                          int width1, int height1) {

        super(contextp);

        context = contextp;

        //Allows for keyboard resizing on the inputs

        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;

        height = height1;
        width = width1;

        // Get texts
       // texts = textsp;
        sentence = new TextView(context);
        sentence.setTextSize(convertPixelsToDp(height / 30, context));

        //if (Build.VERSION.SDK_INT < 17) {
        sentence.setId(R.id.madlibGameSentence);
        //} else {
          //  sentence.setId(View.generateViewId());
        //}

        submit = new Button(context);
        submit.setText("Submit");

        if (Build.VERSION.SDK_INT < 17) {
            submit.setId(R.id.madlibGameSentence);
        } else {
            submit.setId(View.generateViewId());
        }

        RelativeLayout.LayoutParams relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //controls getting the adjective
        TextView adjective = new TextView(context);
        adjective.setTextSize(convertPixelsToDp(height / 30, context));
        adjective.setText("Enter an adjective:");

        if (Build.VERSION.SDK_INT < 17) {
            adjective.setId(R.id.madlibGameAdjective);
        } else {
            adjective.setId(View.generateViewId());
        }

        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        adjective.setLayoutParams(relativeLay);
        adjective.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        adjective.setPadding(50,100,10,10);

        // Exits keyboard when user hits enter on it
        adj = new EditText(context);
        adj.setSingleLine(true);
        adj.setHint("ex. funny");
        adj.setHintTextColor(ContextCompat.getColor(context, R.color.colorHint));

        /*adj.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });
*/
        if (Build.VERSION.SDK_INT < 17) {
            adj.setId(R.id.madlibGameAdj);
        } else {
            adj.setId(View.generateViewId());
        }

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(50,10,10,10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, adjective.getId());

        adj.setLayoutParams(relativeLay);

        //controls getting the verb
        TextView verb =  new TextView(context);
        verb.setTextSize(convertPixelsToDp(height / 30, context));
        verb.setText("Enter a verb:");

        if (Build.VERSION.SDK_INT < 17) {
            verb.setId(R.id.madlibGameVerb);
        } else {
            verb.setId(View.generateViewId());
        }

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, adj.getId());

        verb.setLayoutParams(relativeLay);
        verb.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        verb.setPadding(50,100,10,10);


        // Exits keyboard when user hits enter on it
        vrb = new EditText(context);
        vrb.setSingleLine(true);
        vrb.setHint("ex. run");
        vrb.setHintTextColor(ContextCompat.getColor(context, R.color.colorHint));
        /*vrb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });*/

        if (Build.VERSION.SDK_INT < 17) {
            vrb.setId(R.id.madlibGameVrb);
        } else {
            vrb.setId(View.generateViewId());
        }

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(50,10,10,10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, verb.getId());

        vrb.setLayoutParams(relativeLay);

        //controls getting the name
        TextView name = new TextView(context);
        name.setTextSize(convertPixelsToDp(height / 30, context));
        name.setText("Enter a name:");

        if (Build.VERSION.SDK_INT < 17) {
            name.setId(R.id.madlibGameVrb);
        } else {
            name.setId(View.generateViewId());
        }

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, vrb.getId());

        name.setLayoutParams(relativeLay);
        name.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        name.setPadding(50,100,10,10);

        // Exits keyboard when user hits enter on it
        nm = new EditText(context);;
        nm.setSingleLine(true);
        nm.setHint("ex. George");
        nm.setHintTextColor(ContextCompat.getColor(context, R.color.colorHint));
        /*nm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });*/

        if (Build.VERSION.SDK_INT < 17) {
            nm.setId(R.id.madlibGameVrb);
        } else {
            nm.setId(View.generateViewId());
        }

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(50,10,10,10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, name.getId());

        nm.setLayoutParams(relativeLay);

        //controls getting the noun
        TextView noun = new TextView(context);
        noun.setTextSize(convertPixelsToDp(height / 30, context));
        noun.setText("Enter a noun:");

        if (Build.VERSION.SDK_INT < 17) {
            noun.setId(R.id.madlibGameNn);
        } else {
            noun.setId(View.generateViewId());
        }

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, nm.getId());

        noun.setLayoutParams(relativeLay);
        noun.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        noun.setPadding(50,100,10,10);

        // Exits keyboard when user hits enter on it
        nn = new EditText(context);
        nn.setSingleLine(true);
        nn.setHint("ex. chair");
        nn.setHintTextColor(ContextCompat.getColor(context, R.color.colorHint));
        nn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    View view = findViewById(R.id.madlibGameSubmit);
                    createMadLib(v);
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                    return false;
                }
                return false;
            }
        });

        if (Build.VERSION.SDK_INT < 17) {
            nn.setId(R.id.madlibGameNn);
        } else {
            nn.setId(View.generateViewId());
        }

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(50,10,10,10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, noun.getId());

        nn.setLayoutParams(relativeLay);

        // Set background color of page
        //this.setBackgroundColor(ContextCompat.getColor(context, R.color.chemistryGameBG));

        this.addView(adjective);
        this.addView(adj);
        this.addView(verb);
        this.addView(vrb);
        this.addView(name);
        this.addView(nm);
        this.addView(noun);
        this.addView(nn);
    }

    public void createMadLib(View view){
        sentence.setText("There was a " + nn.getText() + " named " + nm.getText() + " hey ho hey ho");

        RelativeLayout.LayoutParams relativeLay;

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, nn.getId());

        sentence.setLayoutParams(relativeLay);
        sentence.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        sentence.setPadding(50,100,10,10);
        this.addView(sentence);

        int correctAnswerIdx;
        String correctAnswerStr;

        //set radio buttons
        RadioGroup radioGroup = new RadioGroup(context);
        final RadioButton[] rb = new RadioButton[4];
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for(int i=0; i<4; i++){
            rb[i]  = new RadioButton(context);
            radioGroup.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout
        }

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            String textAnswer = "answer" + i;
            ((RadioButton) radioGroup.getChildAt(i)).setText(textAnswer);
            ((RadioButton) radioGroup.getChildAt(i)).setTextSize(20);
            if (textAnswer.equals("answer1")){
                correctAnswerIdx = i;
            }
        }

        if (Build.VERSION.SDK_INT < 17) {
            radioGroup.setId(R.id.madlibGameNn);
        } else {
            radioGroup.setId(View.generateViewId());
        }

        // Save what the new correct answer should be
        correctAnswerStr = "answer1";

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, sentence.getId());

        radioGroup.setLayoutParams(relativeLay);
        radioGroup.setPadding(50,10,10,50);

        this.addView(radioGroup);

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, radioGroup.getId());

        submit.setLayoutParams(relativeLay);
        submit.setPadding(50,50,50,50);

        this.addView(submit);
    }


    /*
  * Called when selects an answer
  * Saves what answer was selected
  */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.question_answer1:
                if (checked)
                    selectedAnswer = 1;
                break;
            case R.id.question_answer2:
                if (checked)
                    selectedAnswer = 2;
                break;
            case R.id.question_answer3:
                if (checked)
                    selectedAnswer = 3;
                break;
            case R.id.question_answer4:
                if (checked)
                    selectedAnswer = 4;
                break;
            default:
                break;
        }
    }

    public void newQ(){
        invalidate();
    }


    /*
     * Move on to game end page
     */
    private void endGame() {
        Intent intent = new Intent(context, GameEnd.class);
        intent.putExtra("next_activity", nextActivity);
        intent.putExtra("conceptID", conceptID);
        intent.putExtra("lessonID", lessonID);
        context.startActivity(intent);
    }

    /*
     * Change pixel measurement into dp measurement
     */
    public static float convertPixelsToDp(float px, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
