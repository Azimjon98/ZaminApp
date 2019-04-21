package edu.azimjon.project.zamin.addition;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.azimjon.project.zamin.model.NewsCategoryModel;

import static edu.azimjon.project.zamin.addition.Constants.*;
import static java.lang.reflect.Array.getInt;

public class MySettings {
    static private Context appContext;
    static private MySettings instance;

    public static MySettings initInstance(Context con) {
        appContext = con;
        if (instance == null)
            instance = new MySettings();

        return instance;
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


    public boolean isLoggined() {
        SharedPreferences preferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_LOGINED, false);
    }


//    public void setLangChanged(boolean state) {
//        SharedPreferences preferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(LANG_CHANGED, state);
//        editor.apply();
//    }
//
//    public boolean islangChanged() {
//        SharedPreferences preferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
//        return preferences.getBoolean(LANG_CHANGED, false);
//    }

    public void setLoggined(boolean state) {
        SharedPreferences preferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGINED, state);
        editor.apply();
    }


    public void setToken(String token) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return sharedPreferences.getString(MY_TOKEN, "nothing");
    }

    public void clearSharedPreferences() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .clear()
                .apply();

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
        String lang = "";

        if (locale.equals("uz"))
            lang = "oz";
        else if (locale.equals("kr"))
            lang = "uz";

        return lang;
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

}
