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
 * Last Edit: 11-17-16
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

            //check for availability of the external storage
            //keep in mind external storage is public
                /*if(isExternalStorageReadable() && isExternalStorageWritable()) {
                    //copies the database file in assets to a new file in external storage
                    file = new File(getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath(), "GEDPrep.db");
                    System.out.println("external");
                    copy();
                }
                else{*/
            //if external is not available
            // we must copy to local app storage that is at risk of being cleared

            //send a popup here maybe that notifies the user of the risks?
            //System.out.println("fail");
            //}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    Checks if external storage is available to read
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    } */

    /**
     * Creates a empty database in internal memory and copies our own database to it
     * */
    private void createDatabase() throws IOException{

        boolean dbExist = checkDatabase();
        SQLiteDatabase db_Read;

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
    private boolean checkDatabase(){
        System.out.println("checking database");
        SQLiteDatabase checkDB = null;

        try{
            checkDB = SQLiteDatabase.openDatabase(
                    file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){
            System.out.println("Database doesn't exist yet.");
        }

        boolean dbExists = checkDB != null;

        if(dbExists){
            System.out.println("exists");
            checkDB.close();
        }
        //return false; //used for overwriting database on emulator
        return dbExists;
    }

    /**
     * Copies the database from the assets-folder to the just created empty database in
     * internal memory, from where it will be accessed and used.
     * */
    private void copyDatabase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getApplicationContext().getAssets().open("gedV4.db");

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(file);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
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

        //currently makes the last played att NULL
        myDatabase.execSQL("INSERT INTO User VALUES ( 1 , '" + username + "', 1, datetime('NOW'))");

        //need to add lesson one as started

        close();
    }

    /**
     * Update the users entry in the user table with the given username parameter
     * @param username the new username for the user
     */
    public void updateUsername(String username) {
        open();

        myDatabase.rawQuery("UPDATE User SET username = '" + username + "'", null);

        close();
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
     * Query that selects the current lesson from the user table
     * @return the lessonID of the current lesson
     */
    public int selectCurrentLessonID() {
        /*open();

        Cursor c = myDatabase.rawQuery("SELECT current_lesson FROM user", null);
        c.moveToFirst();
        int currentLessonID = c.getInt(0);
        //c = myDatabase.rawQuery("SELECT lesson_name FROM lessons WHERE lesson_id = " + currentLessonID, null);
        //String lessonName = c.getString(0);

        c.close();
        close();*/

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
    public ArrayList<String> selectConcepts(){
        open();

        ArrayList<String> concepts = new ArrayList<>();
        Cursor c = myDatabase.rawQuery("SELECT concept_names FROM concepts", null);

        while(c.moveToNext()){
            concepts.add(c.getString(0));
        }

        c.close();
        close();

        return concepts;
    }

    /**
     * Query to select all lessons given concept id that are completes/in progress
     * in an array list
     * @param concept_id the id of the concept the user is looking at
     * @return and array list of lessons avaible to the user in the given concept
     */
    public ArrayList<String> selectLessons(int concept_id){
        open();

       /* Cursor c = myDatabase.rawQuery("SELECT lesson_name FROM user_lessons JOIN lessons " +
                "ON lessons.lesson_id = user_lessons.lesson_id WHERE concept_id = " + concept_id +
                " AND datetime_started != NULL", null);
        */
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
     * Query to select question text
     */
    public ArrayList<String> selectQuestionTexts(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT question_text " +
                "FROM question_template WHERE lesson_id = " + lesson_id, null);

        ArrayList<String> questionText = new ArrayList<>();

        while(c.moveToNext()){
            questionText.add(c.getString(0));
        }

        c.close();
        close();

        return questionText;
    }

    /**
     * Query to select question answers
     */
    public ArrayList<ArrayList<String>> selectQuestionAnswers(int lesson_id){
        open();

        Cursor c = myDatabase.rawQuery("SELECT answer_1, answer_2, answer_3, answer_4, " +
                "correct_answer " +
                "FROM Answers " + "JOIN question_template " +
                "ON question_template.question_id = Answers.question_id " +
                        " WHERE lesson_id = " + lesson_id, null);

        ArrayList<ArrayList<String>> lessonQustions = new ArrayList<>();

        while(c.moveToNext()){
            ArrayList<String> row = new ArrayList<String>();
            row.add(c.getString(0));
            row.add(c.getString(1));
            row.add(c.getString(2));
            row.add(c.getString(3));
            row.add(c.getString(4));
            lessonQustions.add(row);
        }

        c.close();
        close();

        return lessonQustions;
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

}
