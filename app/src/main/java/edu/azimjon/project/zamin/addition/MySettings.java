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


    public void setCategories(List<NewsCategoryModel> categories) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> ids = new HashSet<>();
        Set<String> names = new HashSet<>();
        Set<String> urls = new HashSet<>();
        Set<String> isEnableds = new HashSet<>();

        for (NewsCategoryModel m : categories) {
            ids.add(m.getCategoryId());
            names.add(m.getName());
            urls.add(m.getImageUrl());
            isEnableds.add(m.isEnabled() ? "1" : "0");
        }


        editor.putStringSet("ids", ids);
        editor.putStringSet("names", names);
        editor.putStringSet("urls", urls);
        editor.putStringSet("isEnabled", isEnableds);
        editor.apply();
    }


    public List<NewsCategoryModel> getCategories() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("settings", Context.MODE_PRIVATE);

        List<NewsCategoryModel> array = new ArrayList<>();

        Set<String> ids = sharedPreferences.getStringSet("ids", new HashSet<>());
        Set<String> names = sharedPreferences.getStringSet("names", new HashSet<>());
        Set<String> urls = sharedPreferences.getStringSet("urls", new HashSet<>());
        Set<String> isEnableds = sharedPreferences.getStringSet("isEnableds", new HashSet<>());

        return array;
    }


}
