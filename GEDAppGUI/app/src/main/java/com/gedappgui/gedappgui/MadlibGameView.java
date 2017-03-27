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
 * Last Edit: 3-26-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.*;

public class MadlibGameView extends RelativeLayout {
    private Context context;
    ScrollView scroll;

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
    private ArrayList<String> questionTexts;
    //the 4 possible answers to each questions
    private ArrayList<ArrayList<String>> answerTexts;
    //the actual answers to each question
    private ArrayList<String> answers;

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

    //the answers selected by the radio buttons
    private int selectedAnswer;

    //buttons for submitting words and question answers
    private Button submit;
    private Button questionSubmit;

    /**
     *
     * @param contextp context of the activity
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
    public MadlibGameView(Context contextp, ArrayList<ArrayList<String>> words, ArrayList<String>
            question, ArrayList<ArrayList<String>> answerPs, ArrayList<String> answerAs,
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

        wordFills = words;
        questionTexts = question;
        answerTexts = answerPs;
        answers = answerAs;

        userInput = new ArrayList<>();
        userFills = new ArrayList<>();

        allUserInput = new ArrayList<>();
        allUserFills = new ArrayList<>();

        currQuestion = 0;


        //sets the buttons up early
        questionSubmit = new Button(context);
        if (Build.VERSION.SDK_INT < 17) {
            questionSubmit.setId(R.id.madlibGameQSubmit);
        } else {
            questionSubmit.setId(View.generateViewId());
        }

        submit = new Button(context);
        submit.setText("Submit");

        if (Build.VERSION.SDK_INT < 17) {
            submit.setId(R.id.madlibGameSubmit);
        } else {
            submit.setId(View.generateViewId());
        }

        //goes through all the word gathering data and  creates the right amount of text and
        //edit text views for the problem
        for(int i = 0; i<wordFills.size(); i++){
            for(int j = 0; j<wordFills.get(i).size(); j++){
                userInput.add(createTextView(wordFills.get(i).get(j), userFills, j));
                userFills.add(createEditText(wordFills.get(i).get(j), userInput, j));
            }

            userFills.get(wordFills.get(i).size()-1).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                    }
                    return false;
                }
            });

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
                createMadLib(allUserFills.get(currQuestion),wordFills.get(currQuestion));}});
    }

    /**
     * Creates the madlib sentence with the proper given words from the student
     * @param input the edit text views from the user input
     * @param titles the names of all the text views
     * @return the madlib sentence with all the words replaced
     */
    public String createSentence(ArrayList<EditText> input, ArrayList<String> titles){
        String sentence = questionTexts.get(currQuestion);

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

        //setting up the question page views
        TextView question = new TextView(context);

        String sentence = createSentence(input, titles);
        question.setText(sentence);

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
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        question.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        question.setPadding(50, 100, 10, 10);
        question.setLayoutParams(relativeLay);

        this.addView(question);

        //set radio buttons
        RadioGroup radioGroup = new RadioGroup(context);
        final RadioButton[] rb = new RadioButton[4];
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        for(int i=0; i<4; i++){
            rb[i]  = new RadioButton(context);
            radioGroup.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout
        }

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            String textAnswer = answerTexts.get(currQuestion).get(i);
            ((RadioButton) radioGroup.getChildAt(i)).setText(textAnswer);
            ((RadioButton) radioGroup.getChildAt(i)).setTextSize(20);
            ((RadioButton) radioGroup.getChildAt(i)).setId(i+1);
            ((RadioButton) radioGroup.getChildAt(i)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onRadioButtonClicked(v);}});
        }
        radioGroup.setId(R.id.madlibGameRButtons);


        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(10, 20, 10, 20);
        relativeLay.addRule(RelativeLayout.BELOW, question.getId());
        radioGroup.setPadding(50,10,10,50);

        radioGroup.setLayoutParams(relativeLay);

        this.addView(radioGroup);

        //set up the submit button
        questionSubmit.setText("Submit");

        relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLay.setMargins(50,10,10,10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, radioGroup.getId());

        questionSubmit.setLayoutParams(relativeLay);

        this.addView(questionSubmit);

        //adds listener so when clicked it will check the answer
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
        if (selectedAnswer > 0 && questionSubmit.getText().equals("Submit")) {
            // If answer has been selected, submit and check it

            // Disable radio buttons
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.madlibGameRButtons);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
            }

            // Get selected answer for comparison
            String selectedString =
                    answerTexts.get(currQuestion).get(selectedAnswer-1);
            System.out.println(selectedString);
            System.out.println(answers.get(currQuestion));

            // Check if answer is correct
            if (selectedString.equals(answers.get(currQuestion))) {
                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(
                        ContextCompat.getColor(context, R.color.questionCorrect)
                );
                questionSubmit.setText("Continue");
                currQuestion++;

                if(currQuestion < answers.size()) {
                    // Get new question
                    questionSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addViews(allUserFills.get(currQuestion),
                                    allUserInput.get(currQuestion));
                        }
                    });
                }
                else{
                    //end the game
                    questionSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            endGame();
                        }
                    });
                }
            } else {
                ((RadioButton) radioGroup.getChildAt(selectedAnswer-1)).setTextColor(
                        ContextCompat.getColor(context, R.color.questionIncorrect)
                );
                questionSubmit.setText("Try Again!");
            }

            // Clear out selected answer
            selectedAnswer = 0;
        }
        else if(questionSubmit.getText().equals("Try Again!")) {
            // enable radio buttons

            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.madlibGameRButtons);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(true);
            }
            questionSubmit.setText("Submit");
        }
    }

    /**
     * adds the submit button, edit text and text views to the screen for madlib input
     * @param edits the edittext views for where users will actually input text
     * @param views the text views for where users see a title of what to input
     */
    public void addViews(ArrayList<EditText> edits, ArrayList<TextView> views) {
        if(currQuestion < answers.size()) {
            this.removeAllViews();
            for (int i = 0; i < edits.size(); i++) {
                this.addView(views.get(i));
                this.addView(edits.get(i));
            }

            RelativeLayout.LayoutParams relativeLay = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLay.setMargins(50, 10, 10, 10);
            relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            relativeLay.addRule(RelativeLayout.BELOW, allUserFills.get(currQuestion).get(allUserFills.get(currQuestion).size() - 1).getId());

            submit.setLayoutParams(relativeLay);

            this.addView(submit);
        } else {
            endGame();
        }
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

        //controls get
        TextView newWord = new TextView(context);
        newWord.setTextSize(convertPixelsToDp(height / 30, context));
        newWord.setText("Enter an " + word + ":");

        if (Build.VERSION.SDK_INT < 17) {
            newWord.setId(num);
        } else {
            newWord.setId(View.generateViewId());
        }

        relativeLay.setMargins(10, 10, 10, 10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        if(num != 0){
            relativeLay.addRule(RelativeLayout.BELOW, tEdits.get(num-1).getId());
        }
        else{
            relativeLay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }

        newWord.setLayoutParams(relativeLay);

        newWord.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        newWord.setPadding(50,100,10,10);

        return newWord;
    }

    /**
     * creates a edittext view for a given possible user input pulled from the database
    * @param text the type of word that the user is going to give
    * @param tViews the arraylist of text views (titles of the edittext views
    * @param num the ith edittext view that has been created
    * @return the edittext view created for the given word
    */
    public EditText createEditText(String text, ArrayList<TextView> tViews, int num){
        RelativeLayout.LayoutParams relativeLay = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Exits keyboard when user hits enter on it
        EditText userWord = new EditText(context);
        userWord.setSingleLine(true);
        userWord.setHint(text);
        userWord.setHintTextColor(ContextCompat.getColor(context, R.color.colorHint));

        if (Build.VERSION.SDK_INT < 17) {
            userWord.setId(num+50);
        } else {
            userWord.setId(View.generateViewId());
        }

        relativeLay.setMargins(50,10,10,10);
        relativeLay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        relativeLay.addRule(RelativeLayout.BELOW, tViews.get(num).getId());

        userWord.setLayoutParams(relativeLay);

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
