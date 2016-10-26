package com.gedappgui.gedappgui;

import android.app.Application;

/**
 * Created by myannaharris on 10/26/16.
 */
public class MyApplication extends Application {

    private String name = "";
    private boolean loginStatus = false;

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
}
