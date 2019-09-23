package com.creativeshare.shell_com.app;

import android.app.Application;
import android.content.Context;

import com.creativeshare.shell_com.language.Language_Helper;
import com.creativeshare.shell_com.preferences.Preferences;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
}
