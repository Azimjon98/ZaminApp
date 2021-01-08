package edu.azimjon.project.zamin.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.component.MyApplicationComponent;
import edu.azimjon.project.zamin.events.NetworkStateChangedEvent;
import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.STATE_LOG;

public class NavigationActivity extends AppCompatActivity {

    public static List<String> allFavouriteIds;
    public static List<CategoryNewsModel> categoryModels;
    public static List<CategoryNewsModel> enabledCategoryModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(CALLBACK_LOG, "Navigation: onCreate");
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                allFavouriteIds = FavouriteNewsDatabase.getInstance(getApplicationContext()).getDao().getAllIds();
                categoryModels = CategoryNewsDatabase.getInstance(getApplicationContext()).getDao().getAll();
                enabledCategoryModels = CategoryNewsDatabase.getInstance(getApplicationContext()).getDao().getAllEnabledCategories();
            }
        });

        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        //FIXME: fix this for using in thread
        FavouriteNewsDatabase
                .getInstance(NavigationActivity.this)
                .getDao()
                .getAllIdsLive()
                .observe(this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(@Nullable List<String> strings) {
                        allFavouriteIds = strings;
                    }
                });

        //FIXME: commit or uncommit below if there any use/unuse of categories data
        CategoryNewsDatabase
                .getInstance(NavigationActivity.this)
                .getDao()
                .getAllLive()
                .observe(this, new Observer<List<CategoryNewsModel>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryNewsModel> categories) {
                        categoryModels = categories;
                        System.out.println("Navigation: categoriesonChanged");
                    }
                });

        //FIXME: commit or uncommit below if there any use/unuse of categories data
        CategoryNewsDatabase
                .getInstance(NavigationActivity.this)
                .getDao()
                .getAllEnabledCategoriesLive()
                .observe(this, new Observer<List<CategoryNewsModel>>() {
                    @Override
                    public void onChanged(@Nullable List<CategoryNewsModel> categories) {
                        enabledCategoryModels = categories;
                        System.out.println("Navigation: enabledCategoriesonChanged");

                    }
                });

    }

    //TODO: NETWORKING STATES
    BroadcastReceiver myConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(STATE_LOG, "on_network_changed");

            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                ConnectivityManager manager = (ConnectivityManager) NavigationActivity.this.getSystemService(CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

                //there are a connection to net
                if (activeNetwork != null
                        && activeNetwork.isConnectedOrConnecting()) {
                    Log.d(STATE_LOG, "NETWORK_STATE_CONNECTED");

                    EventBus.getDefault().post(new NetworkStateChangedEvent(NETWORK_STATE_CONNECTED));
//                    check_categories_database();
                } else {
                    Log.d(STATE_LOG, "NETWORK_STATE_NO_CONNECTION");

                    EventBus.getDefault().post(new NetworkStateChangedEvent(NETWORK_STATE_NO_CONNECTION));
                }


            }
        }
    };

    //##############################################################################


    private void check_categories_database() {
        try {
            MyApplication.getInstance().
                    getMyApplicationComponent()
                    .getRetrofitApp()
                    .create(MyRestService.class)
                    .getAllCategories(
                            MySettings.getInstance().getLang()
                    )
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if (response.isSuccessful()) {

                                //Creating a categories array from server
                                List<CategoryNewsModel> categories = new ArrayList<>();
                                for (JsonElement element : response.body()) {
                                    JsonObject category = element.getAsJsonObject();

                                    CategoryNewsModel model = new CategoryNewsModel(
                                            category.getAsJsonPrimitive("name").getAsString(),
                                            category.getAsJsonPrimitive("id").getAsString(),
                                            ""
                                    );
                                    categories.add(model);

                                }

                                //checking database asychronously :)
                                CheckingDataBaseTask dataBaseTask = new CheckingDataBaseTask(categories);
                                dataBaseTask.execute();


                            } else {
                                Log.d(API_LOG, "onResponse: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d(API_LOG, "onFailure: " + t.getMessage());
                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //TODO: Checking database for changings in another Thread
    public class CheckingDataBaseTask extends AsyncTask<Void, Void, Void> {
        private List<CategoryNewsModel> categoryModels;

        public CheckingDataBaseTask(List<CategoryNewsModel> models) {
            this.categoryModels = models;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            check_database(categoryModels);
            return null;
        }

    }

    private void check_database(List<CategoryNewsModel> categoryModels) {
        //variable for see if there were any changes in categories
        boolean isChanged = false;

        CategoryNewsDao categoryNewsDao = CategoryNewsDatabase.getInstance(this).getDao();

        List<CategoryNewsModel> oldCategories = categoryNewsDao.getAll();
        List<CategoryNewsModel> newCategories = categoryModels;

        //checking if there any categories added or removed
        if (oldCategories.size() != newCategories.size()) {
            update_database(categoryNewsDao, oldCategories, newCategories);
        }

    }


    private void update_database(CategoryNewsDao categoryNewsDao, List<CategoryNewsModel> oldCategories, List<CategoryNewsModel> newCategories) {
        //variable for see if there were any changes in categories
        boolean isChanged = false;

        //updating isEnabled state of new Categories
        for (CategoryNewsModel new_ : newCategories) {

            for (CategoryNewsModel old : oldCategories) {
                if (new_.getCategoryId().equals(old.getCategoryId())) {
                    new_.setEnabled(old.isEnabled());
                }
            }
        }

        System.out.println("in insertion in navigation");
        //change database
        categoryNewsDao.deleteAndCreate(new ArrayList<>(newCategories));
    }


    @Override
    protected void onStart() {
        super.onStart();

        //register receiver
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myConnectivityReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(myConnectivityReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MediaPlayer mediaPlayer = ((MyApplication)getApplication()).getMyApplicationComponent().getMediaPlayer();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
