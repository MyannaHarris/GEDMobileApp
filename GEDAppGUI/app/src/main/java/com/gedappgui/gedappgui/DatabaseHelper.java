/*
 * DatabaseHelper.java
 *
 * Helper for opening and querying a database
 *
 * Contains all the methods for common queries to be called
 * from all Activity classes that require database access.
 *
 * Worked on by:
 * Jasmine Jans
 * Myanna Harris
 * Jimmy Sherman
 * Kristina Spring
 *
 * Last Edit: 3-31-17
 *
 */

package com.gedappgui.gedappgui;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.content.Context;
import android.app.Activity;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DatabaseHelper{

    private String DB_NAME = "GEDPrep.db";

    private SQLiteDatabase myDatabase;

    private File file;

    private Context myContext = null;

    /**
     * Constructor
     *
     * Takes and keeps a reference of the passed context
     * in order to access the application internal storage.
     * @param context the context from the Activity class creating a DatabaseHelper object
     */
    public DatabaseHelper(Context context){
        this.myContext = context;

        //tries to open the database/check it is in existence
        try {
            file = new File(((Activity) this.myContext).getApplication().getFilesDir(), DB_NAME);

            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test Constructor
     *
     * Takes and saves a reference to a database
     */
    public DatabaseHelper(SQLiteDatabase testDB, File testFile){
        this.myDatabase = testDB;
        this.file = testFile;
    }

    /**
     * Creates a empty database in internal memory and copies our own database to it
     */
    private void createDatabase() throws IOException{

        boolean dbExist = checkDatabase();
        SQLiteDatabase db_Read;

        // If database doesn't exist, make a new one from the file
        if(!dbExist){
            System.out.println("should not exist");
            // By calling this method an empty database will be created into the default system
            // path of your application so we are gonna be able to overwrite that database
            // with our database.
            db_Read = SQLiteDatabase.openDatabase(
                    file.getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);

            db_Read.close();

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database \n" + e);
            }
        }
    }

    /**
     * Checks if the database already exist
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDatabase(){
        System.out.println("checking database");
        SQLiteDatabase checkDB = null;

        // Tries to open database to determine if it exists or not
        try{
            checkDB = SQLiteDatabase.openDatabase(
                    file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){
            System.out.println("Database doesn't exist yet.");
        }

        boolean dbExists = checkDB != null;

        if(dbExists){
            checkDB.close();
        }
        return dbExists;
    }

    /**
     * Copies the database from the assets-folder to the just created empty database in
     * internal memory, from where it will be accessed and used.
     */
    private void copyDatabase() throws IOException{

        // Open your local db as the input stream
        InputStream myInput = myContext.getApplicationContext().getAssets().open("gedV4.db");

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(file);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    /**
     * opens the myDatabase SQLite database
     * @throws SQLException
     */
    private void open() throws SQLException{
        //Open the database
        try {
            myDatabase = SQLiteDatabase.openDatabase(
                    file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch(SQLiteException e) {
            System.out.println("cannot open database");
        }
    }


    /**
     * closes the myDatabase SQLite database
     */
    private void close() {
        if(myDatabase != null)
            myDatabase.close();
    }

    /**
     * Checks if it is the first time user is using the app
     * @return true if first time user is using app, false otherwise
     */
    public boolean firstTimeLogin(){
        open();

        Cursor c = myDatabase.rawQuery("SELECT count(*) FROM User", null);
        c.moveToFirst();
        int count = c.getInt(0);

        c.close();
        close();

        System.out.println(count);
        return count == 0;
    }

    /**
     * Create the user as a new entry in the user table with the given username parameter
     * @param username the username the user enters
     */
    public void insertUser(String username){
        open();

        myDatabase.execSQL("INSERT INTO User VALUES ( 1 , '" + username + "', 1, datetime('NOW'), '')");

        String insertQuery = "INSERT INTO user_lessons(user_id, lesson_id, datetime_started) VALUES(1,1,date('NOW'))";
        myDatabase.execSQL(insertQuery);

        close();
    }

    /**
     * Add the achievmenet id of the just earned achievement to the user_achievements table
     * @param achievement_id the id of the achievement you want to add
     */
    public void insertAchievement(int achievement_id){
        open();

        myDatabase.execSQL("INSERT INTO user_achievements(user_id, achievement_id, datetime_started) VALUES ( 1, " + achievement_id + ", datetime('NOW'))");

        close();
    }

    /**
     * Check to see if the achievement has already been achieved
     * @param achievement_id the id of the achievement that may be awarded
     * @return true if the achievement is already earned, false otherwise
     */
    public boolean achievementExists(int achievement_id){
        open();
        Cursor cursor = myDatabase.rawQuery("Select * from user_achievements where achievement_id = " + achievement_id, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * Get the achievement ids of the achievements that have already been achieved
     * @return araylist of achievement ids
     */
    public ArrayList<Integer> selectAchievementIDs(){
        open();
        ArrayList<Integer> achievementIDs = new ArrayList<>();
        Cursor cursor = myDatabase.rawQuery("Select achievement_id from user_achievements", null);
        while(cursor.moveToNext()){
            achievementIDs.add(cursor.getInt(0));
        }
        cursor.close();
        close();
        return achievementIDs;
    }

    /**
     *
     * @return an arraylist of all the earned achievements image names
     */
    public ArrayList<String> selectAchievementsImgs(){
        open();
        ArrayList<String> achievementImgs = new ArrayList<>();
        Cursor c = myDatabase.rawQuery("SELECT DISTINCT achievement_img FROM Achievements NATURAL " +
                "JOIN user_achievements WHERE achievement_id = achievement_id", null);

        while(c.moveToNext()){
            achievementImgs.add(c.getString(0));
        }
        close();
        return achievementImgs;
    }

    /**
     *
     * @return an arraylist of all the earned achievements names
     */
    public ArrayList<String> selectAchievementsNames(){
        open();
        ArrayList<String> achievementName = new ArrayList<>();
        Cursor c = myDatabase.rawQuery("SELECT DISTINCT achievement_name FROM Achievements NATURAL " +
                "JOIN user_achievements WHERE achievement_id = achievement_id", null);

        while(c.moveToNext()){
            achievementName.add(c.getString(0));
        }
        close();
        return achievementName;
    }

    /**
     *
     * @return an arraylist of all the earned achievements descriptions
     */
    public ArrayList<String> selectAchievementsDesc(){
        open();
        ArrayList<String> achievementDesc = new ArrayList<>();
        Cursor c = myDatabase.rawQuery("SELECT DISTINCT achievements_desc FROM Achievements NATURAL " +
                "JOIN user_achievements WHERE achievement_id = achievement_id", null);

        while(c.moveToNext()){
            achievementDesc.add(c.getString(0));
        }
        close();
        return achievementDesc;
    }

    /**
     * Update the users entry in the user table with the given username parameter
     * @param username the new username for the user
     */
    public void updateUsername(String username) {
        open();

        myDatabase.execSQL("UPDATE User SET username = '" + username + "'");

        close();
    }

    /**
     * Update the users entry in the user table with the given dragon name parameter
     * @param dragonname the new dragon name for the user
     */
    public void updateDragonName(String dragonname) {
        open();

        myDatabase.execSQL("UPDATE User SET dragon_name = '" + dragonname + "'");

        close();
    }

    /**
     * Query that selects the dragon_name of the user in the user table
     * @return the users dragon name
     */
    public String selectDragonName(){
        return selectString("dragon_name", "user");
    }

    /**
     * Query that selects a string from a given table
     * @param selection the attribute you want to select
     * @param table the table you want to select from
     * @return the String value that the query returns
     */
    public String selectString(String selection, String table){
        open();

        Cursor c = myDatabase.rawQuery("SELECT " + selection + " FROM " + table, null);
        c.moveToFirst();
        String answer = c.getString(0);

        c.close();
        close();

        return answer;
    }

    /**
     * Query that selects an integer from a given table
     * @param selection the attribute you want to select
     * @param table the table you want to select from
     * @return the int value that the query returns
     */
    public int selectInt(String selection, String table) {
        open();

        Cursor c = myDatabase.rawQuery("SELECT " + selection + " FROM " + table, null);
        c.moveToFirst();
        int answer = c.getInt(0);

        c.close();
        close();

        return answer;
    }

    /**
     * Query that selects the user_name of the user in the user table
     * @return the users username
     */
    public String selectUsername(){
        return selectString("username", "user");
    }

    /**
     * Query that selects the lesson name of the given lesson id
     * @param lesson_id the id of the lesson who's name you want to select
     * @return the name of the lesson of the corresponding lesson id
     */
    public String selectLessonTitle(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT lesson_name FROM lessons WHERE lesson_id = " + lesson_id, null);
        c.moveToFirst();
        String lessonName = c.getString(0);

        c.close();
        close();

        return lessonName;
    }

    /**
     * Return true or false depending on whether the game intro for a specific
     * lesson has images in the database
     * @param lesson_id the id of the lesson who's name you want to select
     * @return true or false whether the gameintro has pictures or not
     */
    public boolean gameIntroHasImages(int lesson_id){
        open();

        ArrayList<String> images = new ArrayList<>();
        Cursor c = myDatabase.rawQuery("SELECT lesson_name FROM lessons WHERE lesson_id = " + lesson_id, null);

        c.moveToFirst();

        close();

        if(c==null){
            c.close();
            return false;
        }

        c.close();
        return true;
    }
    /**
     * Query that selects the pictures of the given lessons game intro page
     * @param lesson_id the id of the lesson who's name you want to select
     * @return an arraylist of the names of game intro images
     */
    public ArrayList<String> selectGameIntroPics(int lesson_id){
        open();

        ArrayList<String> images = new ArrayList<>();
        Cursor c = myDatabase.rawQuery("SELECT game_intro_image FROM lessons WHERE lesson_id = " + lesson_id, null);

        c.moveToFirst();
        String input = c.getString(0);
        String[] imagesA = input.split("[,]");
        System.out.println(input);


        for(int i = 0; i<imagesA.length; i++){
            System.out.println(imagesA[i]);
            images.add(imagesA[i]);
        }

        c.close();
        close();

        return images;
    }

    /**
     * Query that selects the current lesson from the user table
     * @return the lessonID of the current lesson
     */
    public int selectCurrentLessonID() {
        return selectInt("current_lesson", "user");
    }

    /**
     * Query that updates the user's current lesson
     * @param lesson_id the new current lesson
     */
    public void updateCurrentLessonID(int lesson_id) {
        open();
        myDatabase.execSQL("UPDATE user SET current_lesson="+lesson_id);
        close();
    }

    /**
     * Query to select concept names of unlocked lessons and returns them in array list
     * @return an ArrayList of concept names
     */
    public ArrayList<String> selectUnlockedConcepts() {
        open();
        ArrayList<String> concepts = new ArrayList<>();
        Cursor c = myDatabase.rawQuery("SELECT DISTINCT concept_names FROM user_lessons NATURAL " +
                "JOIN lessons NATURAL JOIN concepts", null);

        while(c.moveToNext()){
            concepts.add(c.getString(0));
        }
        close();
        return concepts;
    }

    /**
     * Query to select ALL concept names and returns them in array list
     * @return an ArrayList of all the concept names
     */
    public int selectConceptID(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT DISTINCT concept_id FROM concepts NATURAL " +
                "JOIN lessons WHERE lessons.lesson_id = " + lesson_id, null);

        c.moveToFirst();
        int conceptID = c.getInt(0);

        c.close();
        close();

        return conceptID;
    }

    /**
     * Gets the description of the achievement with the given id
     * @param achievement_id the id of the achievement we want
     * @return the description of the achievement with the given id
     */
    public String selectAchievementDesc(int achievement_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT achievements_desc FROM Achievements WHERE achievement_id = " + achievement_id, null);

        c.moveToFirst();
        String desc = c.getString(0);

        c.close();
        close();

        return desc;
    }

    /**
     * Gets the image name of the achievement with the given id
     * @param achievement_id the id of the achievement we want
     * @return the image name of the achievement with the given id
     */
    public String selectAchievementImg(int achievement_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT achievement_img FROM Achievements WHERE achievement_id = " + achievement_id, null);

        c.moveToFirst();
        String img = c.getString(0);

        c.close();
        close();

        return img;
    }

    /**
     * Gets the name of the achievement with the given id
     * @param achievement_id the id of the achievement we want
     * @return the name of the achievement with the given id
     */
    public String selectAchievementName(int achievement_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT achievement_name FROM Achievements WHERE achievement_id = " + achievement_id, null);

        c.moveToFirst();
        String name = c.getString(0);

        c.close();
        close();

        return name;
    }

    /**
     * Query to select all lessons given concept id that are completes/in progress
     * in an array list
     * @param concept_id the id of the concept the user is looking at
     * @return and array list of lessons avaible to the user in the given concept
     */
    public ArrayList<String> selectLessons(int concept_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT lesson_name FROM user_lessons JOIN lessons " +
                "ON lessons.lesson_id = user_lessons.lesson_id WHERE concept_id = " + concept_id, null);
        ArrayList<String> lessonNames = new ArrayList<>();

        while(c.moveToNext()){
            lessonNames.add(c.getString(0));
        }

        c.close();
        close();

        return lessonNames;
    }

    /**
     * Query to select a lesson given concept id and offset
     * @param concept_id the id of the concept the user is looking at
     * @param offset the nth lesson to be returned
     * @return a lesson id from the given concept
     */
    public int selectLessonID(int concept_id, int offset) {
        open();

        Cursor c = myDatabase.rawQuery("SELECT lesson_id FROM lessons WHERE concept_id = " +
                concept_id + " LIMIT 1 OFFSET " + offset, null);
        c.moveToFirst();
        int id = c.getInt(0);

        c.close();
        close();

        return id;
    }

    /**
     * Query to select lesson summary given the lesson id
     * @param lesson_id the id of the current lesson
     * @return the lesson summary of the given lesson
     */
    public String selectLessonSummary(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT lesson_summary FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String summary = c.getString(0);

        c.close();
        close();

        return summary;
    }

    /**
     * query to select the first lesson example given lesson id
     * @param lesson_id the id of the current lesson
     * @return the first lesson example of the given lesson
     */
    public String selectLessonExample1(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT example_1 FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String example = c.getString(0);

        c.close();
        close();

        return example;
    }

    /**
     * query to select the second lesson example given lesson id
     * @param lesson_id the id of the current lesson
     * @return the second lesson example of the given lesson
     */
    public String selectLessonExample2(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT example_2 FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String example = c.getString(0);

        c.close();
        close();

        return example;
    }

    /**
     * query to select lesson advice given the lesson id
     * @param lesson_id of the current lesson
     * @return the lesson advice for the given lesson
     */
    public String selectLessonAdvice(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT advice FROM lessons WHERE lesson_id = " + lesson_id, null);
        c.moveToFirst();
        String summary = c.getString(0);

        c.close();
        close();

        return summary;
    }

    /**
     * query to select the video URL given the lesson_id
     * @param lesson_id of the current lesson
     * @return the videoURL for the given lesson
     */
    public String selectVideoURL(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT url FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String url = c.getString(0);

        c.close();
        close();

        return url;
    }

    /**
     * Query to select the picture name given lesson_id
     * @param lesson_id of the current lesson
     * @return the name of the picture of the given lesson
     */
    public String selectPictureName(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT picture_name FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String pictureName = c.getString(0);

        c.close();
        close();

        return pictureName;
    }
    /**
     * Query to select redo examples from Review table
     */
    public ArrayList<String> selectRedos(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT review_1, review_2, review_3 " +
                "FROM Reviews " + "WHERE lesson_id = " + lesson_id, null);

        ArrayList<String> redos = new ArrayList<>();
        c.moveToFirst();
        redos.add(c.getString(0));
        redos.add(c.getString(1));
        redos.add(c.getString(2));

        c.close();
        close();

        return redos;

    }

    /**
     * Query to select all questions for a given lesson
     * @param lesson_id id of the lesson
     * @return all questions in a random order, grouped by difficulty
     */
    public ArrayList<ArrayList<ArrayList<String>>> getAllQuestions(int lesson_id) {
        open();

        ArrayList<ArrayList<ArrayList<String>>> allQs = new ArrayList<>();
        // Gets each difficulty individually so problems of the same difficulty are grouped together
        for (int i = 1; i < 4; i++) {
            ArrayList<ArrayList<String>> someQs = new ArrayList<>();
            Cursor c = myDatabase.rawQuery("SELECT question_text, numbers, answer_1, answer_2, answer_3" +
                    ", answer_4, correct_answer FROM Answers JOIN question_template ON " +
                    "Answers.question_id=question_template.question_id WHERE question_template.lesson_id=" +
                    lesson_id + " AND difficulty=" + i + " ORDER BY RANDOM()", null);

            while (c.moveToNext()) {

                ArrayList<String> oneQ = new ArrayList<>();
                for (int j = 0; j < 7; j++) {
                    oneQ.add(c.getString(j));
                }
                someQs.add(oneQ);
            }

            c.close();
            allQs.add(someQs);
        }

        close();
        return allQs;
    }

    /**
     * Query to select a random question text given the lesson and the difficulty
     * @param lesson_id id of the lesson
     * @param difficulty the difficulty of the question
     * @return the text of the question
     */
    public ArrayList<String> selectQuestionText(int lesson_id, int difficulty){
        open();

        Cursor c = myDatabase.rawQuery("SELECT question_text, numbers, answer_1, answer_2, answer_3" +
                ", answer_4, correct_answer FROM Answers JOIN question_template ON " +
                "Answers.question_id=question_template.question_id WHERE question_template.lesson_id=" +
                lesson_id + " AND difficulty=" + difficulty + " ORDER BY RANDOM() LIMIT 1", null);

        ArrayList<String> questionText = new ArrayList<>();

        c.moveToNext();
        for (int i=0; i<7; i++) {
            questionText.add(c.getString(i));
        }

        c.close();
        close();

        return questionText;
    }

    /**
     * Query to select accessories
     * @return List of accessory info
     */
    public ArrayList<ArrayList<String>> selectAccessories(){
        open();

        Cursor c = myDatabase.rawQuery("SELECT accessory_img, layer_id, currently_wearing " +
                "FROM user_accessories " + "JOIN accessories " +
                "ON user_accessories.accessory_id = accessories.accessory_id", null);

        ArrayList<ArrayList<String>> accessories = new ArrayList<>();

        while(c.moveToNext()){
            ArrayList<String> row = new ArrayList<String>();
            row.add(c.getString(0));
            row.add(c.getString(1));
            row.add(c.getString(2));
            accessories.add(row);
        }

        c.close();
        close();

        return accessories;
    }

    /**
     * Query to see whether user is on the final lesson, given the current lesson
     * @return boolean of whether user is on the final lesson
     */
    public boolean isLastLesson(int currLesson) {
        open();
        Cursor c = myDatabase.rawQuery("SELECT max(lesson_id) from lessons", null);
        c.moveToFirst();
        int lastLesson = c.getInt(0);
        close();
        return (lastLesson == currLesson);
    }

    /**
     * Query to see whether user has used all features of the app
     * @return boolean of whether achievements have been achieved or not
     */
    public boolean usedAllFeatures(){
        open();

        Cursor c = myDatabase.rawQuery("    Select count(*) from user_achievements where " +
                "achievement_id = 1 or achievement_id = 2 or achievement_id = 6 or " +
                "achievement_id = 7 or achievement_id = 8 or achievement_id = 14 or " +
                "achievement_id = 20", null);

        c.moveToFirst();
        int count = c.getInt(0);

        c.close();
        close();

        // If the count is 7, all features have been used and achievements for them given
        if(count == 7) {
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Query to see whether sprite it wearing party hat or not
     * @return boolean of whether wearing party hat or not
     */
    public boolean isWearingPartyHat(){
        open();

        Cursor c = myDatabase.rawQuery("SELECT count(*) FROM user_accessories WHERE " +
                "accessory_id = 17 and currently_wearing = 1", null);

        c.moveToFirst();
        int count = c.getInt(0);

        c.close();
        close();

        if(count == 1) {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Query to select number of accessories that are being worn
     * @return int amount of accessories
     */
    public int countAccessoriesWorn(){
        open();

        Cursor c = myDatabase.rawQuery("SELECT Count(*) FROM user_accessories WHERE " +
                "currently_wearing = 1 ", null);

        c.moveToFirst();
        int count = c.getInt(0);

        c.close();
        close();

        return count;
    }

    /**
     * Query to select the number of accessories that have been collected by the user
     * @return int number of accessories earned
     */
    public int countAccessoriesEarned() {
        open();

        Cursor c = myDatabase.rawQuery("SELECT Count (*) FROM user_accessories", null);

        c.moveToFirst();

        //there are 8 skins already in the user_achievements that we need to subtract
        int count = (c.getInt(0)) - 8;

        //if accessories are given on the success page, we need to up the count by one
        //so that the achievement is given when they are about to get a 3rd accessory
        count += 1;

        c.close();
        close();

        return count;
    }


    /**
     * Query to select accessories that are being worn
     * @return int amount of accesories
     */
    public boolean isFancy(){
        open();

        Cursor c = myDatabase.rawQuery("SELECT Count(*) FROM user_accessories WHERE " +
                "(accessory_id = 15 or accessory_id = 25 or accessory_id = 2) and " +
                "currently_wearing = 1", null);

        c.moveToFirst();
        int count = c.getInt(0);

        c.close();
        close();

        // If count is three than he is wearing all three necessary things to be fancy
        if(count == 3){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * returns the instructions of the game for the given lesson
     * @param lesson_id the id of the lesson
     * @return the instructions for the game of the given lesson
     */
    public String selectInstructions(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_instructions FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String instructions = c.getString(0);

        c.close();
        close();

        return instructions;
    }

    /**
     * returns the introduction of the game for the given lesson
     * @param lesson_id the id of the lesson
     * @return the instructions for the game of the given lesson
     */
    public String selectIntroduction(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_intro FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String introduction = c.getString(0);

        c.close();
        close();

        return introduction;
    }

    /**
     * returns the input for a game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectBucketGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> allQAndAs = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> randQAndAs = new ArrayList<ArrayList<String>>();
        ArrayList<String> possibleAnswers = new ArrayList<>();
        ArrayList<String> questionsAndAnswers = new ArrayList<>();

        String[] questions = input.split("[,]");

        //gets the 5 possible solutions to store
        //then gets the 1 question, and the 1 answer
        for(int k = 0; k<questions.length; k+=7) {
            possibleAnswers.clear();
            questionsAndAnswers.clear();

            for(int i = k; i<k+5; i++) {
                possibleAnswers.add(questions[i]);
            }
            for(int j = k+5; j<k+7;  j++) {
                questionsAndAnswers.add(questions[j]);
            }
            //deep copies the array lists of possible answers and qeustions and answer
            allQAndAs.add(new ArrayList<String>(possibleAnswers));
            allQAndAs.add(new ArrayList<String>(questionsAndAnswers));

        }

        //choose 5 random questions of the 20 to give to the user in the game
        for(int r = 0; r < 5; r++) {
            //randomly generate 1s and zeroes
            double rand = Math.abs(Math.round(Math.random() * 20-r));
            if(rand%2 == 0) {
                randQAndAs.add(allQAndAs.remove((int) rand));
                randQAndAs.add(allQAndAs.remove((int) rand));
            }
            else{
                randQAndAs.add(allQAndAs.remove((int)(rand-1)));
                randQAndAs.add(allQAndAs.remove((int)(rand-1)));
            }
        }

        return randQAndAs;
    }

    /**
     * returns the input for an endless game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectInfiniteBucketGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> allQAndAs = new ArrayList<ArrayList<String>>();
        ArrayList<String> possibleAnswers = new ArrayList<>();
        ArrayList<String> questionsAndAnswers = new ArrayList<>();

        String[] questions = input.split("[,]");

        //gets the 5 possible solutions to store
        //then gets the 1 question, and the 1 answer
        for(int k = 0; k<questions.length; k+=7) {
            possibleAnswers.clear();
            questionsAndAnswers.clear();

            for(int i = k; i<k+5; i++) {
                possibleAnswers.add(questions[i]);
            }
            for(int j = k+5; j<k+7;  j++) {
                questionsAndAnswers.add(questions[j]);
            }
            //deep copies the array lists of possible answers and qeustions and answer
            allQAndAs.add(new ArrayList<String>(possibleAnswers));
            allQAndAs.add(new ArrayList<String>(questionsAndAnswers));

        }

        return allQAndAs;
    }

    /**
     * returns the input for a game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<ArrayList<String>>> selectInfiniteMadlibInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<String> placeholder = new ArrayList<>();

        ArrayList<ArrayList<String>> finalTexts = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> question = new ArrayList<>();
        ArrayList<ArrayList<String>> answerps = new ArrayList<>();
        ArrayList<ArrayList<String>> answers = new ArrayList<>();

        ArrayList<ArrayList<ArrayList<String>>> randQAndAs = new ArrayList<>();

        String[] questions;
        questions = input.split("[&]");

        for(int i = 0; i<questions.length;i++){
            if(i%4 == 0) {
                finalTexts.add(new ArrayList<>(Arrays.asList(questions[i].split("[/]"))));

                placeholder.clear();
                placeholder.add(questions[i + 1]);
                question.add(new ArrayList<>(placeholder));

                answerps.add(new ArrayList<>(Arrays.asList(questions[i + 2].split("[/]"))));

                placeholder.clear();
                placeholder.add(questions[i + 3]);
                answers.add(new ArrayList<>(placeholder));
            }
        }

        randQAndAs.add(finalTexts);
        randQAndAs.add(question);
        randQAndAs.add(answerps);
        randQAndAs.add(answers);

        return randQAndAs;
    }

    /**
     * returns the input for a game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<ArrayList<String>>> selectMadlibInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<String> placeholder = new ArrayList<>();

        ArrayList<ArrayList<String>> finalTexts = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> question = new ArrayList<>();
        ArrayList<ArrayList<String>> answerps = new ArrayList<>();
        ArrayList<ArrayList<String>> answers = new ArrayList<>();

        ArrayList<ArrayList<String>> randFinal = new ArrayList<>();
        ArrayList<ArrayList<String>> randQuestion = new ArrayList<>();
        ArrayList<ArrayList<String>> randAnswerps = new ArrayList<>();
        ArrayList<ArrayList<String>> randAnswers = new ArrayList<>();

        ArrayList<ArrayList<ArrayList<String>>> randQAndAs = new ArrayList<>();

        String[] questions;
        questions = input.split("[&]");


        for(int i = 0; i<questions.length;i++){
            if(i%4 == 0) {
                finalTexts.add(new ArrayList<>(Arrays.asList(questions[i].split("[/]"))));

                placeholder.clear();
                placeholder.add(questions[i + 1]);
                question.add(new ArrayList<>(placeholder));

                answerps.add(new ArrayList<>(Arrays.asList(questions[i + 2].split("[/]"))));

                placeholder.clear();
                placeholder.add(questions[i + 3]);
                answers.add(new ArrayList<>(placeholder));
            }
        }

        for(int i = 3; i<6; i++){
            double rand = Math.abs(Math.round(Math.random() * 11-i));
            randFinal.add(finalTexts.remove((int) rand));
            randQuestion.add(question.remove((int) rand));
            randAnswerps.add(answerps.remove((int) rand));
            randAnswers.add(answers.remove((int) rand));
        }

        randQAndAs.add(randFinal);
        randQAndAs.add(randQuestion);
        randQAndAs.add(randAnswerps);
        randQAndAs.add(randAnswers);

        return randQAndAs;
    }

    /**
     * returns the input for a game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectMatchGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> finalTexts = new ArrayList<ArrayList<String>>();
        ArrayList<String> allQAndAs = new ArrayList<String>();
        ArrayList<String> randQ = new ArrayList<String>();
        ArrayList<String> randA = new ArrayList<String>();

        String[] questions;
        if(lesson_id == 23){
            questions = input.split("[#]");
        }
        else{
            questions = input.split("[,]");
        }

        for(int i = 0; i<questions.length;i++){
            allQAndAs.add(questions[i]);
        }

        //choose 3 random questions of the 20 to give to the user in the game
        for(int r = 0; r < 3; r++) {
            //randomly generate 1s and zeroes
            double rand = Math.abs(Math.round(Math.random() * 19-r));
            randQ.add(allQAndAs.remove((int) rand));
            randA.add(allQAndAs.remove((int)(rand + (19-r))));
        }

        randQ.addAll(randA);
        finalTexts.add(randQ);

        randQ = new ArrayList<String>();
        randA = new ArrayList<String>();
        //choose 3 random questions of the 20 to give to the user in the game
        for(int r = 3; r < 6; r++) {
            //randomly generate 1s and zeroes
            double rand = Math.abs(Math.round(Math.random() * 19-r));
            randQ.add(allQAndAs.remove((int) rand));
            randA.add(allQAndAs.remove((int)(rand + (19-r))));
        }

        randQ.addAll(randA);
        finalTexts.add(randQ);

        return finalTexts;
    }

    /**
     * returns the input for an endless game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectInfiniteMatchGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> finalTexts = new ArrayList<ArrayList<String>>();
        ArrayList<String> allQAndAs = new ArrayList<String>();

        String[] questions;
        if(lesson_id == 23){
            questions = input.split("[#]");
        }
        else{
            questions = input.split("[,]");
        }

        for(int i = 0; i<questions.length;i++){
            allQAndAs.add(questions[i]);
        }

        finalTexts.add(allQAndAs);

        return finalTexts;
    }

    /**
     * returns the input for a game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectChemistryGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> randQAndAs = new ArrayList<ArrayList<String>>();
        ArrayList<String> splitUp;

        String[] options = input.split("[;]");

        // Get five unique random questions
        for (int i = 0; i < 5; i++) {
            int randIdx = ( int )(Math.random() * (20-i));
            splitUp = new ArrayList<String>(Arrays.asList(options[randIdx].split("[&]")));
            randQAndAs.add(new ArrayList<String>(splitUp.subList(0,5)));
            randQAndAs.add(new ArrayList<String>(splitUp.subList(5,7)));
        }
        return randQAndAs;
    }

    /**
     * returns the input for an endless game from a CSL
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectInfiniteChemistryGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> QAndAs = new ArrayList<ArrayList<String>>();
        ArrayList<String> splitUp;

        String[] options = input.split("[;]");

        // Get five unique random questions
        for (int i = 0; i < 20; i++) {
            splitUp = new ArrayList<String>(Arrays.asList(options[i].split("[&]")));
            QAndAs.add(new ArrayList<String>(splitUp.subList(0,5)));
            QAndAs.add(new ArrayList<String>(splitUp.subList(5,7)));
        }
        return QAndAs;
    }

    /**
     * returns the input for a game
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectOrderGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> randQAndAs = new ArrayList<ArrayList<String>>();
        ArrayList<String> splitUp;

        String[] options = input.split("[;]");

        // Get five unique random questions
        for (int i = 0; i < 2; i++) {
            int randIdx = ( int )(Math.random() * (20-i));
            splitUp = new ArrayList<String>(Arrays.asList(options[randIdx].split("[#]")));
            randQAndAs.add(new ArrayList<String>(splitUp.subList(0,4)));
            randQAndAs.add(new ArrayList<String>(splitUp.subList(4,8)));
        }
        return randQAndAs;
    }

    /**
     * returns the input for an endless game
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public ArrayList<ArrayList<String>> selectInfiniteOrderGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        ArrayList<ArrayList<String>> QAndAs = new ArrayList<ArrayList<String>>();
        ArrayList<String> splitUp;

        String[] options = input.split("[;]");

        // Get five unique random questions
        for (int i = 0; i < 20; i++) {
            splitUp = new ArrayList<String>(Arrays.asList(options[i].split("[#]")));
            QAndAs.add(new ArrayList<String>(splitUp.subList(0,4)));
            QAndAs.add(new ArrayList<String>(splitUp.subList(4,8)));
        }
        return QAndAs;
    }

    /**
     * returns the input for the pic game as a comma delimited string
     * @param lesson_id the id of the lesson
     * @return the input for the game (questions and answers)
     */
    public String selectPicGameInput(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT game_input FROM lessons WHERE lesson_id = " +
                lesson_id, null);
        c.moveToFirst();
        String input = c.getString(0);

        c.close();
        close();

        return input;
    }

    /**
     * gets the name of the game template we are using for the given lesson
     * @param lesson_id the id of the lesson
     * @return the name of the game template being used
     */
    public String selectGameTemplate(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT template_name FROM game_templates JOIN lessons " +
                "ON game_templates.game_template_id = lessons.game_template_id WHERE lesson_id = "
                + lesson_id, null);

        String game_name;
        //while games are still in progress, catch error of no game_name
        if(c != null){
            c.moveToFirst();
            game_name = c.getString(0);
        }
        else{
            game_name = "";
        }



        c.close();
        close();

        return game_name;
    }


    //@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion)
            try {
                copyDatabase();
            }catch (IOException e){
                throw new Error("Error copying database");
            }
    }

    /**
     * Method to update user_lessons to unlock next lesson, also sets current lesson to next lesson
     * @param lessonID of the lesson completed
     */
    public void lessonCompleted(int lessonID) {
        open();
        // set time for completed lesson
        String updateQuery = "UPDATE user_lessons SET datetime_finished=date('now') WHERE lesson_id="
                + lessonID;
        myDatabase.execSQL(updateQuery);
        int newLessonID = lessonID + 1;
        // if next lesson is not already in user_lessons, add it
        Cursor c = myDatabase.rawQuery("SELECT count(lesson_id) FROM user_lessons WHERE lesson_id="
                + newLessonID,null);
        c.moveToFirst();
        int test = c.getInt(0);
        if (test < 1) {
            String insertQuery = "INSERT INTO user_lessons(user_id, lesson_id, datetime_started) VALUES(1,"+newLessonID+",date('now'))";
            myDatabase.execSQL(insertQuery);
        }
        //change current lesson
        myDatabase.execSQL("UPDATE user SET current_lesson="+newLessonID);
        close();
    }

    /**
     * gets an arraylist of random accessories
     * @return an arraylist of random accessories
     */
    public ArrayList<Integer> getRandomAccessories() {
        open();
        ArrayList<Integer> ids = new ArrayList<>();
        String getAccessories = "SELECT accessory_id FROM accessories WHERE NOT EXISTS (SELECT " +
                "user_accessories.accessory_id FROM user_accessories WHERE " +
                "user_accessories.accessory_id = accessories.accessory_id) ORDER BY RANDOM() LIMIT 3";
        Cursor c = myDatabase.rawQuery(getAccessories, null);
        while(c.moveToNext()) {
            ids.add(c.getInt(0));
        }
        close();
        return ids;
    }

    /**
     * puts an accessory of a certain id
     * @param id if of the accessory
     */
    public void giveAccessory(int id) {
        open();
        String insertQuery = "INSERT INTO user_accessories(user_id, accessory_id, currently_wearing) VALUES(1,"+id+",0)";
        myDatabase.execSQL(insertQuery);
        close();
    }

    /**
     * returns true or false if a lesson has already been completed
     * @param id the id of the lesson
     * @return true or false if lesson has already been completed
     */
    public boolean isLessonAlreadyStarted(int id) {
        open();
        boolean isComplete = false;
        Cursor c = myDatabase.rawQuery("SELECT count(lesson_id) FROM user_lessons WHERE lesson_id="
                + id,null);
        c.moveToFirst();
        int test = c.getInt(0);
        if (test > 0) {
            isComplete = true;
        }
        close();
        return isComplete;
    }

    /**
     * query to check whether the selected lesson has been completed
     * @param id of the lesson
     */
    public boolean isLessonAlreadyDone(int id) {
        open();
        boolean isComplete = false;
        Cursor c = myDatabase.rawQuery("SELECT count(lesson_id) FROM user_lessons WHERE lesson_id="
                + id + " AND datetime_finished IS NOT NULL",null);
        c.moveToFirst();
        int test = c.getInt(0);
        if (test > 0) {
            isComplete = true;
        }

        close();
        return isComplete;
    }

    /**
     * query to update what accessory is on sprite
     * @param name of the accessory
     * @param groupID of the accessory (so accessories on the same layer don't all stay on)
     */
    public void updateCurrentlyWearing(String name, int groupID) {
        open();
        String takeOff = "UPDATE user_accessories SET currently_wearing=0 WHERE accessory_id IN " +
                "(SELECT accessory_id FROM accessories WHERE group_id=" + groupID +")";
        String putOn = "UPDATE user_accessories SET currently_wearing=1 WHERE accessory_id IN " +
                "(SELECT accessory_id FROM accessories WHERE accessory_img='" + name + "')";
        myDatabase.execSQL(takeOff);
        myDatabase.execSQL(putOn);
        close();

    }

    /**
     * query to update what accessory is not on sprite
     * @param name of the accessory
     */
    public void takeOffClothing(String name) {
        open();
        String takeOff = "UPDATE user_accessories SET currently_wearing=0 WHERE accessory_id IN " +
                "(SELECT accessory_id FROM accessories WHERE accessory_img='" + name + "')";
        myDatabase.execSQL(takeOff);
        close();

    }

    /**
     * query returning the amount of lessons completed + 1
     * @return int, the number of lessons completed + 1
     */
    public int lessonCount() {
        open();
        Cursor c = myDatabase.rawQuery("SELECT count(lesson_id) FROM user_lessons WHERE " +
                "datetime_finished IS NOT NULL AND datetime_finished != ''",null);
        c.moveToFirst();
        int test = c.getInt(0);
        close();
        return test+1;
    }

    /**
     * Gets the largest lesson id
     * @return The max lesson id
     */
    public int getMaxLessonId() {
        open();
        Cursor c = myDatabase.rawQuery("SELECT lesson_id FROM lessons " +
                "ORDER BY lesson_id DESC LIMIT 1",null);
        c.moveToFirst();
        int max = c.getInt(0);
        close();
        return max;
    }

}
