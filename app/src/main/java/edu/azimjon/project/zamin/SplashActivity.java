package edu.azimjon.project.zamin;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.EXTRA_CATEGORIES;
import static edu.azimjon.project.zamin.addition.Constants.EXTRA_FAVOURITES;

public class SplashActivity extends AppCompatActivity {

    private static List<String> allFavouriteIds;
    private static List<NewsCategoryModel> categoryModels;

    volatile MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Locale newLocale = new Locale(MySettings.getInstance().getLocale());
//        Locale.setDefault(newLocale);
//        Resources resources = getApplicationContext().getResources();
//        Configuration config = resources.getConfiguration();
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
//            config.setLocale(newLocale);
//        } else{
//            config.locale=newLocale;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            getApplicationContext().createConfigurationContext(config);
//        } else {
//            resources.updateConfiguration(config,displayMetrics);
//        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                allFavouriteIds = FavouriteNewsDatabase.getInstance(getApplicationContext()).getDao().getAllIds();
                categoryModels = CategoryNewsDatabase.getInstance(getApplicationContext()).getDao().getAll();

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