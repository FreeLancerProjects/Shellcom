package com.appzone.shelcom.app;

import android.app.Application;
import android.content.Context;

import com.appzone.shelcom.language.Language_Helper;
import com.appzone.shelcom.preferences.Preferences;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
}
