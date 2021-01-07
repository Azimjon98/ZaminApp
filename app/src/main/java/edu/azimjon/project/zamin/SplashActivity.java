package edu.azimjon.project.zamin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.EXTRA_CATEGORIES;
import static edu.azimjon.project.zamin.addition.Constants.EXTRA_ENABLED_CATEGORIES;
import static edu.azimjon.project.zamin.addition.Constants.EXTRA_FAVOURITES;

public class SplashActivity extends AppCompatActivity {

    private static List<String> allFavouriteIds;
    private static List<CategoryNewsModel> categoryModels;
    private static List<CategoryNewsModel> enabledCategoryModels;

    volatile MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Thread(new Runnable() {
            @Override
            public void run() {
                allFavouriteIds = FavouriteNewsDatabase.getInstance(getApplicationContext()).getDao().getAllIds();
                categoryModels = CategoryNewsDatabase.getInstance(getApplicationContext()).getDao().getAll();
                enabledCategoryModels = CategoryNewsDatabase.getInstance(getApplicationContext()).getDao().getAllEnabledCategories();

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
            intent.putParcelableArrayListExtra(EXTRA_ENABLED_CATEGORIES, (ArrayList<? extends Parcelable>) enabledCategoryModels);

            SplashActivity.this.finish();
            startActivity(intent);
        }
    }
}