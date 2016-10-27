/*
 * Login.java
 *
 * Login page activity
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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /** Called when the user clicks the Login button */
    public void setLogin(View view) {
        ((MyApplication) this.getApplication()).setLoginStatus(true);
        EditText username = (EditText)findViewById(R.id.username_message);
        ((MyApplication) this.getApplication()).setName(username.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
