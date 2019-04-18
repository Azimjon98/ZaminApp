package edu.azimjon.project.zamin.application;

import android.app.Application;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.component.DaggerMyApplicationComponent;
import edu.azimjon.project.zamin.component.MyApplicationComponent;
import edu.azimjon.project.zamin.module.ContextModule;

//import project.edu.azimjon.zetatourapp.addition.Constants;
//import project.edu.azimjon.zetatourapp.addition.MySettings;
//import project.edu.azimjon.zetatourapp.component.DaggerMyApplicationComponent;
//import project.edu.azimjon.zetatourapp.component.MyApplicationComponent;
//import project.edu.azimjon.zetatourapp.module.ContextModule;

public class MyApplication extends Application {
    private static MyApplication instance;
    private MyApplicationComponent myApplicationComponent;

    public static MyApplication getInstance() throws ClassNotFoundException {
        if (instance == null)
            throw new ClassNotFoundException("application doesn't create yet");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.CALLBACK_LOG, "init application");
        instance = this;
        MySettings.initInstance(this);


        myApplicationComponent = DaggerMyApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .build();

    }

    public MyApplicationComponent getMyApplicationComponent() {
        return myApplicationComponent;
    }
}
