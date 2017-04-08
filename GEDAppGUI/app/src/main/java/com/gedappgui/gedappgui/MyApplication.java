/*
 * MyApplication.java
 *
 * Global variables
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Created by myannaharris on 10/26/16.
 *
 * Last Edit: 3-19-17
 *
 */

package com.gedappgui.gedappgui;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class MyApplication extends Application {

    // Database and file globals here
    // 1 file for accessing local asset, and one for in local storage
    // Put copy function in here
    // Object of database class in here as well (getters and setters)

    // User's name
    private String name = "";

    // Dragon's name
    private String dragonName = "";

    // Whether the user has logged in
    private static boolean loginStatus = false;

    // the preferences set in settings
    private SharedPreferences prefs;

    // Saves the current volume for when the app is unmuted
    private int currVolume = 0;

    // Notification objects for notification every 24 hours
    private boolean sendNotification = false;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    // sprite layers
    private LayerDrawable spriteDrawable;

    // Database helper
    private DatabaseHelper dbHelper;

    // Listens for the preferences changing in the settings
    SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals("username_preference")) {
                // Change username
                String newName = prefs.getString("username_preference", "");
                if (!newName.equals("") && newName.length() > 0) {
                    // Capitalize first letter of name
                    if (newName.length() < 2) {
                        setName(newName.substring(0, 1).toUpperCase());

                        if (dbHelper != null) {
                            dbHelper.updateUsername(
                                    newName.substring(0, 1).toUpperCase());
                        }
                    } else {
                        setName(newName.substring(0, 1).toUpperCase() + newName.substring(1));

                        if (dbHelper != null) {
                            dbHelper.updateUsername(
                                    newName.substring(0, 1).toUpperCase() + newName.substring(1));
                        }
                    }

                    // Clear the text box in settings
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear().apply();

                    // Gives an achievement if they change their username for the first time
                    Intent achievement = new Intent(getApplicationContext(), AchievementPopUp.class);
                    achievement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    achievement.putExtra("achievementID", 2);
                    startActivity(achievement);
                }
            }
            else if (key.equals("sound_preference")) {
                // Mute or un-mute sound
                boolean isChecked = prefs.getBoolean("sound_preference",false);
                if (isChecked) {
                    // Mute
                    AudioManager audioManager =
                            (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    setCurrVolume(audioManager.getStreamVolume(AudioManager.STREAM_RING));
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                }
                else {
                    // Un-mute
                    AudioManager audioManager =
                            (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getCurrVolume(), 0);
                }
            }
            else if (key.equals("notification_preference")) {
                // Set or cancel notification
                boolean isChecked = prefs.getBoolean("notification_preference",false);
                if (isChecked) {
                    // Set notification
                    sendNotification = true;
                    scheduleNotification(getNotification());

                    // Gives an achievement if they set a notification for the first time
                    Intent achievement = new Intent(getApplicationContext(), AchievementPopUp.class);
                    achievement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    achievement.putExtra("achievementID", 3);
                    startActivity(achievement);
                }
                else {
                    // Cancel notification
                    sendNotification = false;
                    // If the alarm has been set, cancel it.
                    if (alarmManager!= null) {
                        alarmManager.cancel(pendingIntent);
                    }


                }
            }
            else if (key.equals("dragonname_preference")) {
                // Change username
                String newName = prefs.getString("dragonname_preference", "");
                if (!newName.equals("") && newName.length() > 0) {
                    // Capitalize first letter of name
                    if (newName.length() < 2) {
                        setDragonName(newName.substring(0, 1).toUpperCase());

                        if (dbHelper != null) {
                            dbHelper.updateDragonName(
                                    newName.substring(0, 1).toUpperCase());
                        }
                    } else {
                        setDragonName(newName.substring(0, 1).toUpperCase() + newName.substring(1));

                        if (dbHelper != null) {
                            dbHelper.updateDragonName(
                                    newName.substring(0, 1).toUpperCase() + newName.substring(1));
                        }
                    }

                    // Clear the text box in settings
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear().apply();
                }
            }
        }
    };

    /**
     * Getter
     * Gets the user name
     * @return name - User name
     */
    public String getName() { return name; }

    /**
     * Getter
     * Gets the dragon name
     * @return dragonName - Dragon name
     */
    public String getDragonName() { return dragonName; }

    /**
     * Setter
     * Sets user name
     * @param newName New user name
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Setter
     * Sets dragon name
     * @param newName New dragon name
     */
    public void setDragonName(String newName) {
        this.dragonName = newName;
    }

    /**
     * Getter
     * Gets whether user has logged in
     * @return loginStatus - Whether user has logged in
     */
    public boolean getLoginStatus() {
        return loginStatus;
    }

    /**
     * Setter
     * Sets whether user has logged in
     * @param newLoginStatus - Whether user has logged in
     */
    public void setLoginStatus(boolean newLoginStatus) {
        this.loginStatus = newLoginStatus;
    }

    /**
     * Getter
     * Gets the global shared preferences listener
     * @return listener - The shared preferences listener
     */
    public SharedPreferences.OnSharedPreferenceChangeListener getSharedPreferenceListener() {
        return listener;
    }

    /**
     * Getter
     * Gets the shared preferences
     * @return prefs - Shared preferences
     */
    public SharedPreferences getSharedPreferences() {
        return prefs;
    }

    /**
     * Setter
     * Sets the shared preferences
     * @param sharedPrefs - The new shared preferences
     */
    public void setSharedPreferences(SharedPreferences sharedPrefs) {
        this.prefs = sharedPrefs;
    }

    /**
     * Setter
     * Sets the database helper object
     * @param db - New database helper object
     */
    public void setDBHelper(DatabaseHelper db) {
        this.dbHelper = db;
    }

    /**
     * Getter
     * Gets the current saved volume from before muting
     * @return currVolume - Save volume level
     */
    public int getCurrVolume() {
        return currVolume;
    }

    /**
     * Setter
     * Sets the current saved volume from before muting
     * @param newVolume - New volume level
     */
    public void setCurrVolume(int newVolume) {
        this.currVolume = newVolume;
    }

    /**
     * Getter
     * Gets the global drawable for the dragon and its layers of accessories
     * @return spriteDrawable - Layered Drawable for dragon
     */
    public LayerDrawable getSpriteDrawable() {
        return spriteDrawable;
    }

    /**
     * Setter
     * Sets new Layer Drawable for dragon
     * @param newDrawable - New Layer Drawable
     */
    public void setSpriteDrawable(LayerDrawable newDrawable) {
        this.spriteDrawable = newDrawable;
    }

    /**
     * Schedules a notification for a future time
     * Every 24 hours
     * @param notification The notification object to schedule
     */
    private void scheduleNotification(Notification notification) {
        // Create intent to use receiver to set off notification
        Intent notificationIntent = new Intent(getApplicationContext(), Receiver.class);
        notificationIntent.putExtra(Receiver.NOTIFICATION, notification);
        pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create time alarm to set off alarm in 24 hours
        Calendar alarm = Calendar.getInstance();

        alarm.add(Calendar.DAY_OF_MONTH, 1);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(alarm.getTimeInMillis() < System.currentTimeMillis()) {
            // Check if date is wrong
            Log.d("Error", "Date set in past");
        } else {
            // Use repeating alarm to send notification every 24 hours
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    /**
     * Creates a notification to remind the user to study math
     * @return mBuilder.build() - Notification builder
     */
    private Notification getNotification() {

        // Create empty intent to put in notification (required)
        Intent myIntent = new Intent();
        PendingIntent pendingIntent2 =
                PendingIntent.getActivity(getApplicationContext(), 0, myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {0, 300, 0};

        // Build notification with words and icon
        NotificationCompat.Builder mBuilder =
                (android.support.v7.app.NotificationCompat.Builder)
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.appicon)
                                .setContentTitle("Math App - Reminder")
                                .setContentText("Hey, we haven't seen you in a while. " +
                                        "You should come practice.")
                                .setVibrate(pattern)
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent2);

        //Required on Gingerbread and below
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        return mBuilder.build();
    }
}
