/*
 * SettingsFragment.java
 *
 * Settings fragment
 *
 * Fragment that hosts the settings layout
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
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    /*
     * Starts the fragment and shows corresponding view on screen
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
