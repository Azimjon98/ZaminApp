package edu.azimjon.project.zamin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.MySettings;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Locale newLocale = new Locale(MySettings.getInstance().getLocale());
        Locale.setDefault(newLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = newLocale;
        this.getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);

                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 100);
    }
}