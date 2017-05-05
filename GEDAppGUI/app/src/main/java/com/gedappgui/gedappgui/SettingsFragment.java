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
 * Last Edit: 5-1-17
 *
 */

package com.gedappgui.gedappgui;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    // Database
    private DatabaseHelper db;

    /*
     * Starts the fragment and shows corresponding view on screen
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Database helper
        db = new DatabaseHelper(getActivity());

        EditTextPreference editPrefUsername = (EditTextPreference) findPreference(
                "username_preference");
        if ((((MyApplication) getActivity().getApplication()).getName()).equals("")) {
            editPrefUsername.getEditText().setHint("Username");
        } else {
            editPrefUsername.getEditText().setHint(
                    ((MyApplication) getActivity().getApplication()).getName());
        }

        EditTextPreference editPrefDragonName = (EditTextPreference) findPreference(
                "dragonname_preference");
        if ((((MyApplication) getActivity().getApplication()).getDragonName()).equals("")) {
            editPrefDragonName.getEditText().setHint("Dragon Name");
        } else {
            editPrefDragonName.getEditText().setHint(
                    ((MyApplication) getActivity().getApplication()).getDragonName());
        }
    }
}
