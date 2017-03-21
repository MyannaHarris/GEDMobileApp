/*
 * Receiver.java
 *
 * Reciever class
 *
 * To create notifications at a specific time
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Created by myannaharris on 11/7/16.
 *
 * Last Edit: 3-19-17
 *
 */

package com.gedappgui.gedappgui;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {

    // Intent extra key
    public static String NOTIFICATION = "notification";

    /**
     * Called when a request is received
     * Sends notification when received
     * @param context Context of activity
     * @param intent Intent that carries notification
     */
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        notificationManager.notify(0123, notification);

    }

}
