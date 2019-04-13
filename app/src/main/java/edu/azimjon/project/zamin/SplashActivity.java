package edu.azimjon.project.zamin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.EXTRA_CATEGORIES;
import static edu.azimjon.project.zamin.addition.Constants.EXTRA_FAVOURITES;

public class SplashActivity extends AppCompatActivity {

    private static List<String> allFavouriteIds;
    private static List<NewsCategoryModel> categoryModels;

    volatile MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Locale newLocale = new Locale(MySettings.getInstance().getLocale());
        Locale.setDefault(newLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = newLocale;
        this.getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        new Thread(new Runnable() {
            @Override
            public void run() {
                allFavouriteIds = FavouriteNewsDatabase.getInstance(getApplicationContext()).getDao().getAllIds();
                categoryModels = CategoryNewsDatabase.getInstance(getApplicationContext()).getDao().getAll();

                for (NewsCategoryModel m : categoryModels) {
                    System.out.println("CategoryInit: " + m.getName());
                }

                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
            intent.putStringArrayListExtra(EXTRA_FAVOURITES, (ArrayList<String>) allFavouriteIds);
            intent.putParcelableArrayListExtra(EXTRA_CATEGORIES, (ArrayList<? extends Parcelable>) categoryModels);

            SplashActivity.this.finish();
            startActivity(intent);
        }
    }
}