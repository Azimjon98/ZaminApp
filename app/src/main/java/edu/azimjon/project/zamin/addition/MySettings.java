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


}
