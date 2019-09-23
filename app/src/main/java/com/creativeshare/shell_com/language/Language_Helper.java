package com.creativeshare.shell_com.language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.creativeshare.shell_com.preferences.Preferences;

import java.util.Locale;

public class Language_Helper {

    public static void setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        updateResources(c, language);
    }

    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);

        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());

        }
        return context;


    }

    private static void persistLanguage(Context c, String language) {
        Preferences.getInstance().create_update_language(c,language);
    }
}
