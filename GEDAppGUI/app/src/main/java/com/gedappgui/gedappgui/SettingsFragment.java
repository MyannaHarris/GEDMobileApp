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
 * Last Edit: 5-6-17
 *
 * Copyright 2017 Myanna Harris, Jasmine Jans, James Sherman, Kristina Spring
 *
 * This file is part of DragonAcademy.
 *
 * DragonAcademy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License. All redistributions
 * of the app or modifications of the app are to remain free in accordance
 * with the GNU General Public License.
 *
 * DragonAcademy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DragonAcademy.  If not, see <http://www.gnu.org/licenses/>.
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
