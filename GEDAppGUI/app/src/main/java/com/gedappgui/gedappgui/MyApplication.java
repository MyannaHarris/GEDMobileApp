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
 * Last Edit: 11-27-16
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
import android.content.res.Resources;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

public class MyApplication extends Application {

    //database and file globals here
    //1 file for accessing local asset, and one for in local storage
    //put copy function in here
    //look for way to set something off on app start
    //object of database class in here as well (getters and setters)

    // User's name
    private String name = "";

    // Whether the use has logged in
    private static boolean loginStatus = false;

    // the preferences set in settings
    private SharedPreferences prefs;

    // Saves the current volume for when the app is unmuted
    private int currVolume = 0;

    // Notification date-time
    private int day = 7; // 7, starts at 1
    private int month = 10; // November, starts at 0
    private int hour = 18; // 6 pm
    private int minute = 21; // 21

    private boolean sendNotification = false;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    // sprite layers
    private LayerDrawable spriteDrawable;

    // Listens for the preferences changing in the settings
    SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals("username_preference")) {
                // Change username
                String newName = prefs.getString("username_preference", "");
                if (!newName.equals(""))
                    setName(newName);
                Intent achievement = new Intent(getApplicationContext(), AchievementPopUp.class);
                achievement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                achievement.putExtra("achievementID", 2);
                startActivity(achievement);

            }
            else if (key.equals("sound_preference")) {
                // Mute sound
                boolean isChecked = prefs.getBoolean("sound_preference",false);
                if (isChecked) {
                    AudioManager audioManager =
                            (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    setCurrVolume(audioManager.getStreamVolume(AudioManager.STREAM_RING));
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                }
                else {
                    AudioManager audioManager =
                            (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getCurrVolume(), 0);
                }
            }
            else if (key.equals("notification_preference")) {
                // Set notification
                boolean isChecked = prefs.getBoolean("notification_preference",false);
                if (isChecked) {
                    sendNotification = true;
                    scheduleNotification(getNotification());
                }
                else {
                    sendNotification = false;
                    // If the alarm has been set, cancel it.
                    if (alarmManager!= null) {
                        alarmManager.cancel(pendingIntent);
                    }


                }
            }
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean newLoginStatus) {
        this.loginStatus = newLoginStatus;
    }

    public SharedPreferences.OnSharedPreferenceChangeListener getSharedPreferenceListener() {
        return listener;
    }

    public SharedPreferences getSharedPreferences() {
        return prefs;
    }

    public void setSharedPreferences(SharedPreferences sharedPrefs) {
        this.prefs = sharedPrefs;
    }

    public int getCurrVolume() {
        return currVolume;
    }

    public void setCurrVolume(int newVolume) {
        this.currVolume = newVolume;
    }

    public LayerDrawable getSpriteDrawable() {
        return spriteDrawable;
    }

    public void setSpriteDrawable(LayerDrawable newDrawable) {
        this.spriteDrawable = newDrawable;
    }

    /*
     * Schedules a notification for a future time
     */
    private void scheduleNotification(Notification notification) {
        Intent notificationIntent = new Intent(getApplicationContext(), Receiver.class);
        notificationIntent.putExtra(Receiver.NOTIFICATION, notification);
        pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar alarm = Calendar.getInstance();

        alarm.set(Calendar.DAY_OF_MONTH,alarm.get(Calendar.DAY_OF_MONTH));
        alarm.set(Calendar.MONTH,alarm.get(Calendar.MONTH));
        // 24 hour clock
        alarm.set(Calendar.HOUR_OF_DAY,alarm.get(Calendar.HOUR_OF_DAY));
        alarm.set(Calendar.MINUTE,alarm.get(Calendar.MINUTE));

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(alarm.getTimeInMillis() < System.currentTimeMillis()) {
            Log.d("Error", "Date set in past");
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    /*
     * Creates a notification to remind the user to study math
     */
    private Notification getNotification() {

        Intent myIntent = new Intent();
        PendingIntent pendingIntent2 =
                PendingIntent.getActivity(getApplicationContext(), 0, myIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {0, 300, 0};

        NotificationCompat.Builder mBuilder =
                (android.support.v7.app.NotificationCompat.Builder)
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.example_picture)
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
