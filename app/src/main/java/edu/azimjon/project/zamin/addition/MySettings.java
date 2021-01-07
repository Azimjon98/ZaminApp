package edu.azimjon.project.zamin.addition;

import android.content.Context;
import android.content.SharedPreferences;

import static edu.azimjon.project.zamin.addition.Constants.CONTENT_FONT_SIZE;
import static edu.azimjon.project.zamin.addition.Constants.IS_FIRST_ENTER;
import static edu.azimjon.project.zamin.addition.Constants.IS_NOTIFICATION_ENABLED;
import static edu.azimjon.project.zamin.addition.Constants.MY_LOCALE;
import static edu.azimjon.project.zamin.addition.Constants.OPEN_CONTENT_FROM_ID;

public class MySettings {
    static private Context appContext;
    static private MySettings instance;

    public static void initInstance(Context con) {
        appContext = con;
        if (instance == null)
            instance = new MySettings();

    }

    public static MySettings getInstance() {
        if (instance == null)
            instance = new MySettings();

        return instance;
    }


    public boolean isFirstEnter() {
        SharedPreferences preferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_FIRST_ENTER, true);
    }

    public void setFirstEnter() {
        SharedPreferences preferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_FIRST_ENTER, false);
        editor.apply();
    }


    public void setNavigationHeight(int height) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key_navigation_height", height);
        editor.apply();
    }


    public int getNavigationHeight() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);

        return sharedPreferences.getInt("key_navigation_height", 56);
    }

    public void setLocale(String locale) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_LOCALE, locale);
        editor.apply();
    }


    public String getLocale() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);

        return sharedPreferences.getString(MY_LOCALE, "uz");
    }


    public String getLang() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String locale = sharedPreferences.getString(MY_LOCALE, "uz");

        if ("uz".equals(locale))
            return "oz";
        else if ("kr".equals(locale))
            return "uz";
        else
            return "oz";


    }

    public void setWhichIdCallsContent(int id) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(OPEN_CONTENT_FROM_ID, id);
        editor.apply();
    }

    public int getWhichIdCallsContent() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);

        return sharedPreferences.getInt(OPEN_CONTENT_FROM_ID, -1);
    }

    public void setNotificationEnabled(boolean state) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_NOTIFICATION_ENABLED, state);
        editor.apply();
    }


    public boolean getNotificationEnabled() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(IS_NOTIFICATION_ENABLED, true);
    }

    public void setFontSize(int size) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CONTENT_FONT_SIZE, size);
        editor.apply();
    }


    public int getFontSize() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);

        return sharedPreferences.getInt(CONTENT_FONT_SIZE, 15);
    }


    public void clearSharedPreferences() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .clear()
                .apply();

    }
}
