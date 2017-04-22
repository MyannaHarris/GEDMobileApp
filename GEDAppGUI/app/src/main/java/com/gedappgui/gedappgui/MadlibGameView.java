/*
 * MadLibGameView.java
 *
 * Madlib game view
 *
 * Provides the view for the Madlib game
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 4-11-17
 *
 */

package com.gedappgui.gedappgui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.*;

import static android.content.Context.VIBRATOR_SERVICE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MadlibGameView extends RelativeLayout {
    private Context context;

    // ID's for the learn cycle
    private int conceptID;
    private int lessonID;

    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Game texts
    //the words that the user will have to enter
    private ArrayList<ArrayList<String>> wordFills;
    //the question string that will get filled in
    private ArrayList<ArrayList<String>> questionTexts;
    //the 4 possible answers to each questions
    private ArrayList<ArrayList<String>> answerTexts;
    //the actual answers to each question
    private ArrayList<ArrayList<String>> answers;
    //the hints for each question madlib input
    private ArrayList<ArrayList<String>> hints;

    //current user input word fills
    private ArrayList<EditText> userFills;
    //current title of user input word fills
    private ArrayList<TextView> userInput;

    //all user input word fills
    private ArrayList<ArrayList<EditText>> allUserFills;
    //all title of user input word fills
    private ArrayList<ArrayList<TextView>> allUserInput;

    //the ith question the user is on
    private int currQuestion;

    //window variables
    private int height;
    private int width;
    private int statusBarHeight;

    //the answers selected by the radio buttons
    private int selectedAnswer;

    //buttons for submitting words and question answers
    private Button submit;
    private Button questionSubmit;

    // Button to end the endless game play
    private Button endButton;

    private Activity activity;

    //dialog for if the user doesnt fill all boxes
    AlertDialog.Builder noFillDialog;

    // For Haptic Feedback
    private Vibrator myVib;

    private ScrollView scroll;

    /**
     * constructor for madlib game
     * @param contextp context of the activity
     * @param activityp reference to previous activity
     * @param words the fill in words for the madlib
     * @param question the questions for the game
     * @param answerPs the answer possibilities
     * @param answerAs the answer for each questions
     * @param conceptIDp ID of the current concept
     * @param lessonIDp ID of the current lesson
     * @param nextActivityp Number indicating what the activity after the game should be
     * @param width1 Width of the screen in pixels
     * @param height1 Height of screen in pixels
     */
    public MadlibGameView(Context contextp, Activity activityp, ArrayList<ArrayList<String>> words,
                          ArrayList<ArrayList<String>> question, ArrayList<ArrayList<String>> answerPs,
                          ArrayList<ArrayList<String>> hintp,
                                  ArrayList<ArrayList<String>> answerAs, int conceptIDp,
                          int lessonIDp, int nextActivityp, int width1, int height1, ScrollView scrollp) {
        super(contextp);

        context = contextp;
        activity = activityp;


        // IDs for starting next intent after game
        conceptID = conceptIDp;
        lessonID = lessonIDp;
        nextActivity = nextActivityp;
        scroll = scrollp;

        height = height1;
        width = width1;

        //holds the game content
        wordFills = words;
        hints = hintp;
        questionTexts = question;
        answerTexts = answerPs;
        answers = answerAs;

        //for current text views and edit text views
        userInput = new ArrayList<>();
        userFills = new ArrayList<>();

        //for all text views and edit text views
        allUserInput = new ArrayList<>();
        allUserFills = new ArrayList<>();

        // Set up vibrator service
        myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        //current question
        currQuestion = 0;

        // Instantiate an AlertDialog.Builder with its constructor
        noFillDialog = new AlertDialog.Builder(context, R.style.AlertDialogAppearance);


        //following code adds end game button for access from arcade
        // Get status bar height to deal with on phones that have the status bar showing
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        statusBarHeight = rectangle.top;

        //if accessed from arcade
        if(nextActivityp == 1){
            //create new end game button
            LinearLayout.LayoutParams linearLayoutButton = new LinearLayout.LayoutParams(
                    MATCH_PARENT, (height - statusBarHeight - 15) / 8 - 20);

            linearLayoutButton.setMargins(0, 5, 0, 5);

            endButton = new Button(context);
            endButton.setLayoutParams(linearLayoutButton);
            endButton.setTextSize(convertPixelsToDp(height / 20, context));
            endButton.setTextColor(ContextCompat.getColor(context, R.color.matchGameText));
            endButton.setText("End Game");
            endButton.setGravity(Gravity.CENTER_HORIZONTAL);
            endButton.setHeight((height - statusBarHeight - 15) / 8 - 20);

            if (Build.VERSION.SDK_INT < 17) {
                endButton.setId(R.id.madlibGameEndButton);
            } else {
                endButton.setId(View.generateViewId());
            }

            //on click listener to end game
            endButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    endGame();
                }
            });
        }

        //creates and sets the submit buttons for question and user input areas
        questionSubmit = new Button(context);
        if (Build.VERSION.SDK_INT < 17) {
            questionSubmit.setId(R.id.madlibGameQSubmit);
        } else {
            questionSubmit.setId(View.generateViewId());
        }
        questionSubmit.setTextColor(ContextCompat.getColor(context, R.color.colorButtonText));
        questionSubmit.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButton));

        submit = new Button(context);
        submit.setText("SUBMIT");
        submit.setTextColor(ContextCompat.getColor(context, R.color.colorButtonText));
        submit.setBackgroundColor(ContextCompat.getColor(context, R.color.colorButton));
        submit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);


        if (Build.VERSION.SDK_INT < 17) {
            submit.setId(R.id.madlibGameSubmit);
        } else {
            submit.setId(View.generateViewId());
        }

        //goes through all the word gathering data and  creates the right amount of text and
        //edit text views for the current problem
        for(int i = 0; i<wordFills.size(); i++){
            for(int j = 0; j<wordFills.get(i).size(); j++){
                userInput.add(createTextView(wordFills.get(i).get(j), userFills, j));
                System.out.println(hints.get(i).get(j));
                userFills.add(createEditText(wordFills.get(i).get(j), userInput, hints.get(i).get(j), j));
            }

            //add the specific questions textviews and edit text views to all edit text and text views
            allUserInput.add(new ArrayList<>(userInput));
            allUserFills.add(new ArrayList<>(userFills));
            userInput.clear();
            userFills.clear();
        }

        //adds the views for the user input
        addViews(allUserFills.get(currQuestion), allUserInput.get(currQuestion));

        //sets onclick listener for the submit button to create a question and madlib when
        //the user is done
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //checks to see if the user has entered all words
                if(notFilled(allUserFills.get(currQuestion))){
                    InputMethodManager inputManager = (InputMethodManager)
                            activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    //throw up a dialog box if they haven't added all yet
                    noFillDialog.setTitle("Oops!");
                    noFillDialog.setMessage("You must enter a word in each blank before you can continue.");
                    noFillDialog.setIcon(R.drawable.appicon);
                    // Add the ok button
                    noFillDialog.setNegativeButton("OK",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });
                    //show the dialog box
                    AlertDialog dialog = noFillDialog.create();
                    dialog.show();
                }
                //if all input is given, create the madlib
                else {
                    InputMethodManager inputManager = (InputMethodManager)
                            activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    //rescroll back to the top
                    scroll.scrollTo(0,0);
                    createMadLib(allUserFills.get(currQuestion), wordFills.get(currQuestion));
                }
                };});
    }

    /**
     * Creates the madlib sentence with the proper given words from the student
     * @param input the edit text views from the user input
     * @param titles the names of all the text views
     * @return the madlib sentence with all the words replaced
     */
    public String createSentence(ArrayList<EditText> input, ArrayList<String> titles){
        String sentence = questionTexts.get(currQuestion).get(0);

        for(int i = 0; i<titles.size(); i++){
            //reg ex for finding the exact word to replace in the sentence
            String re = "#\\b" + titles.get(i) + "\\b#";
            Pattern p = Pattern.compile(re);
            Matcher m = p.matcher(sentence);
            sentence = m.replaceAll(input.get(i).getText().toString());
        }

        return sentence;
    }

    /**
     * creates the madlib sentence and then sets up the question page
     * with a radio group and the question
     * @param input the edit text views from the user input
     * @param titles the names of all the text views
     */
    public void createMadLib(ArrayList<EditText> input, ArrayList<String> titles) {
        this.removeAllViews();

        //hides the nav bar
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

        //setting up the question page views

        //starts with the text view for the question
        TextView question = new TextView(context);

        String sentence = createSentence(input, titles);
        question.setText(toHTML(sentence));

        if (Build.VERSION.SDK_INT < 17) {
            question.setId(R.id.madlibGameSentence);
        } else {
            question.setId(View.generateViewId());
        }

        RelativeLayout.LayoutParams relativeLay;
        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        question.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        question.setPadding(25,25,25,25);

        //changes the layout to make room for the end button if accessed from games page
        if(nextActivity == 1){
            this.addView(endButton);
            relativeLay.addRule(RelativeLayout.BELOW,endButton.getId());
        }
        else{
            relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }

        question.setLayoutParams(relativeLay);
        question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);

        this.addView(question);

        //sets up the radio buttons
        RadioGroup radioGroup = new RadioGroup(context);
        final RadioButton[] rb = new RadioButton[4];
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        for(int i=0; i<4; i++){
            rb[i]  = new RadioButton(context);
            radioGroup.addView(rb[i], layoutParams); //the RadioButtons are added to the radioGroup instead of the layout
        }

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            String textAnswer = answerTexts.get(currQuestion).get(i);
            ((RadioButton) radioGroup.getChildAt(i)).setText(toHTML(textAnswer));
            ((RadioButton) radioGroup.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);
            ((RadioButton) radioGroup.getChildAt(i)).setId(i+1);
            ((RadioButton) radioGroup.getChildAt(i)).setTextColor(ContextCompat.getColor(context, R.color.colorBodyText));
            ((RadioButton) radioGroup.getChildAt(i)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onRadioButtonClicked(v);}});
        }
        radioGroup.setId(R.id.madlibGameRButtons);


        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.BELOW, question.getId());
        radioGroup.setPadding(25,25,25,25);

        radioGroup.setLayoutParams(relativeLay);

        this.addView(radioGroup);

        //sets up the submit button
        questionSubmit.setText("SUBMIT");

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(35,35,35,100);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, radioGroup.getId());

        questionSubmit.setLayoutParams(relativeLay);
        questionSubmit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);

        //adds empty padding at the bottom to help the scroll
        TextView space = new TextView(context);
        space.setFocusable(false);
        space.setTextIsSelectable(false);
        relativeLay = new RelativeLayout.LayoutParams(
                MATCH_PARENT, 100);
        relativeLay.addRule(RelativeLayout.BELOW, questionSubmit.getId());
        space.setLayoutParams(relativeLay);

        this.addView(questionSubmit);
        this.addView(space);

        //adds listener so when submit is clicked it will check the answer the user entered
        questionSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                evalAnswer();}});
    }

    /**
     * evaluates the answer of the question, lets the retry if they get it wrong
     * or has them continue on to a new madlib (or finish the game) if answered
     * correctly
     */
    public void evalAnswer(){
        // Make sure an answer is selected and submit button says Submit
        if (selectedAnswer > 0 && questionSubmit.getText().equals("SUBMIT")) {
            // If answer has been selected, submit and check it

            // Disable radio buttons
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.madlibGameRButtons);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
            }

            // Get selected answer for comparison
            String selectedString = answerTexts.get(currQuestion).get(selectedAnswer-1);

            // Check if answer is correct
            if (selectedString.equals(answers.get(currQuestion).get(0))) {
                // vibrate when correct
                myVib.vibrate(150);

                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(
                        ContextCompat.getColor(context, R.color.questionCorrect)
                );
                //change button to say continue and increase current question
                questionSubmit.setText("CONTINUE");
                currQuestion++;

                //if this is isnt the last question, set up views for user input page and new question
                if(currQuestion < answers.size()) {
                    // Get new question
                    questionSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //rescroll back to the top
                            scroll.scrollTo(0,0);

                            //add new views
                            addViews(allUserFills.get(currQuestion),
                                    allUserInput.get(currQuestion));
                        }
                    });
                }
                //if it is the last question, end the game
                else{
                    //end the game
                    questionSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            endGame();
                        }
                    });
                }
                //if the answer is wrong, change button text, make wrong answer red and let the user
                //try again
            } else {
                // incorrect vibrate
                long[] incorrectBuzz = {0,55,40,55};
                myVib.vibrate(incorrectBuzz, -1); // vibrate

                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(
                        ContextCompat.getColor(context, R.color.questionIncorrect)
                );
                questionSubmit.setText("TRY AGAIN!");
            }

            // Clear out selected answer
            selectedAnswer = 0;
        }
        //if its set to try again, let the user try again and renable buttons
        else if(questionSubmit.getText().equals("TRY AGAIN!")) {
            // enable radio buttons

            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.madlibGameRButtons);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(true);
            }
            questionSubmit.setText("SUBMIT");
        }
    }

    /**
     * adds the submit button, edit text and text views to the screen for madlib input
     * @param edits the edittext views for where users will actually input text
     * @param views the text views for where users see a title of what to input
     */
    public void addViews(ArrayList<EditText> edits, ArrayList<TextView> views) {
        if(currQuestion < answers.size()) {
            //removes all preexisting views
            this.removeAllViews();

            //if from the arcade adds the end button
            if (nextActivity == 1) {
                this.addView(endButton);
            }

            //adds all the edit text and text views
            for (int i = 0; i < edits.size(); i++) {
                this.addView(views.get(i));
                this.addView(edits.get(i));
            }

            //sets up submit button
            RelativeLayout.LayoutParams relativeLay = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLay.setMargins(25, 100, 25, 10);
            relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            relativeLay.addRule(RelativeLayout.BELOW, allUserFills.get(currQuestion).get(allUserFills.get(currQuestion).size() - 1).getId());
            submit.setLayoutParams(relativeLay);
            submit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);

            //adds empty padding at the bottom to help the scroll
            TextView space = new TextView(context);
            space.setFocusable(false);
            space.setTextIsSelectable(false);
            relativeLay = new RelativeLayout.LayoutParams(
                    MATCH_PARENT, 600);
            relativeLay.addRule(RelativeLayout.BELOW, submit.getId());
            space.setLayoutParams(relativeLay);

            this.addView(submit);
            this.addView(space);

            //no more questions left, ends game
        } else {
            endGame();
        }
    }

    /**
     * checks to see if any of the edit text views are not filled
     * @param edits the edittext views with user input
     * @return true if some of the edit text views are empty
     */
    boolean notFilled(ArrayList<EditText> edits){
        for (int i = 0; i < edits.size(); i++) {
            if(edits.get(i).getText().toString().equals("")){
                return true;
            }
        }
        return false;
    }

    /**
     * creates a text view for a given possible user input pulled from the database
     * @param word the type of word that the user is going to give
     * @param tEdits the arraylist of edittext  views
     * @param num the ith text view that has been created
     * @return the textview created for the given word
     */
    public TextView createTextView(String word, ArrayList<EditText> tEdits, int num){
        RelativeLayout.LayoutParams relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //controls getting the correct words in the text view
        TextView newWord = new TextView(context);
        newWord.setText("Enter a(n) " + word.toLowerCase() + ":");

        if (Build.VERSION.SDK_INT < 17) {
            newWord.setId(num);
        } else {
            newWord.setId(View.generateViewId());
        }

        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        //cases for where to align the next view based on if its the first one, a regular one
        //or is accessed from the arcade.
        if(num != 0){
            relativeLay.addRule(RelativeLayout.BELOW, tEdits.get(num-1).getId());
        }
        else if(num == 0 && nextActivity == 1){
            relativeLay.addRule(RelativeLayout.BELOW,endButton.getId());
        }
        else if(num == 0){
            relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }


        newWord.setLayoutParams(relativeLay);
        //newWord.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        newWord.setPadding(25,100,25,10);
        newWord.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);

        //returns text view
        return newWord;
    }

    /**
     * creates a edittext view for a given possible user input pulled from the database
    * @param text the type of word that the user is going to give
    * @param tViews the arraylist of text views (titles of the edittext views
    * @param num the ith edittext view that has been created
    * @return the edittext view created for the given word
    */
    public EditText createEditText(String text, ArrayList<TextView> tViews, String hint, int num){
        RelativeLayout.LayoutParams relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //sets up a new edit text
        EditText userWord = new EditText(context);
        userWord.setSingleLine(true);
        userWord.setHint(hint);
        userWord.setHintTextColor(ContextCompat.getColor(context, R.color.colorHint));

        if (Build.VERSION.SDK_INT < 17) {
            userWord.setId(num+50);
        } else {
            userWord.setId(View.generateViewId());
        }

        relativeLay.setMargins(25,10,25,10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, tViews.get(num).getId());

        userWord.setLayoutParams(relativeLay);
        userWord.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float)height/30);

        // Exits keyboard when user hits enter on it
        userWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //evaluates answer and closes keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //hides the nav bar
                    if (Build.VERSION.SDK_INT >= 19) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
                    return true;
                }
                //hides the nav bar
                if (Build.VERSION.SDK_INT >= 19) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
                return false;
            }
        });

        //returns the edit text view
        return userWord;
    }


    /**
     * Move on to game end page
     */
    private void endGame() {
        Intent intent = new Intent(context, GameEnd.class);
        intent.putExtra("next_activity", nextActivity);
        intent.putExtra("conceptID", conceptID);
        intent.putExtra("lessonID", lessonID);
        context.startActivity(intent);
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

    /**
     * Change pixel measurement into dp measurement
     * @param px The pixels
     * @param context The context of the activity
     * @return dp - The measurement in dp
     */
    public static float convertPixelsToDp(float px, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    /**
    * Called when selects an answer
    * Saves what answer was selected
    */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case 1:
                if (checked)
                    selectedAnswer = 1;
                break;
            case 2:
                if (checked)
                    selectedAnswer = 2;
                break;
            case 3:
                if (checked)
                    selectedAnswer = 3;
                break;
            case 4:
                if (checked)
                    selectedAnswer = 4;
                break;
            default:
                break;
        }
    }


}
