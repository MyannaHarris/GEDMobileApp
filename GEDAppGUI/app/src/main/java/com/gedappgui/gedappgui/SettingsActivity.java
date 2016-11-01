/*
 * SettingsActivity.java
 *
 * Settings page
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 10-26-16
 *
 */

package com.gedappgui.gedappgui;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

/**
 * Created by myannaharris on 10/26/16.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public void updateName(View view) {
        TextView greetingText = (TextView)findViewById(R.id.namePref);
        String name = greetingText.getText().toString();
        if (name!=null && name!="") {
            ((MyApplication) this.getApplication()).setName(name);
        }
    }
}
