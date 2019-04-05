package edu.azimjon.project.zamin.activity;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.events.MyNetworkEvents;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_NO_CONNECTION;

public class NavigationActivity extends AppCompatActivity {

    private static List<String> allFavouriteIds = new ArrayList<>();
    private static List<NewsCategoryModel> categoryModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //FIXME: fix this for using in thread
        FavouriteNewsDatabase
                .getInstance(NavigationActivity.this)
                .getDao()
                .getAllIds()
                .observe(this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(@Nullable List<String> strings) {
                        allFavouriteIds = strings;
                        System.out.println("getting all data from favourites");
                    }
                });

        //FIXME: commit or uncommit below if there any use/unuse of categories data
        CategoryNewsDatabase
                .getInstance(NavigationActivity.this)
                .getDao()
                .getAllLive()
                .observe(this, new Observer<List<NewsCategoryModel>>() {
                    @Override
                    public void onChanged(@Nullable List<NewsCategoryModel> categories) {
                        categoryModels = categories;
                        System.out.println("getting all data from categories");
                    }
                });


    }

    public static List<String> getFavouritesIds() {
        return allFavouriteIds;
    }

    public static List<NewsCategoryModel> getAllCategories() {
        return categoryModels;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //register receiver
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myConnectivityReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myConnectivityReceiver);
    }

    BroadcastReceiver myConnectivityReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                ConnectivityManager manager = (ConnectivityManager) NavigationActivity.this.getSystemService(CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

                //there are a connection to net
                if (activeNetwork != null
                        && activeNetwork.isConnectedOrConnecting()) {
                    EventBus.getDefault().post(new MyNetworkEvents.NetworkStateChangedEvent(NETWORK_STATE_CONNECTED));
                    check_categories_database();
                } else {
                    EventBus.getDefault().post(new MyNetworkEvents.NetworkStateChangedEvent(NETWORK_STATE_NO_CONNECTION));
                }


            }
        }
    };

    //TODO: NETWORKING STATES

    private void check_categories_database() {
        try {
            MyApplication.getInstance().
                    getMyApplicationComponent()
                    .getRetrofitApp()
                    .create(MyRestService.class)
                    .getAllCategories("uz")
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if (response.isSuccessful()) {

                                //Creating a categories array from server
                                List<NewsCategoryModel> categories = new ArrayList<>();
                                for (JsonElement element : response.body()) {
                                    JsonObject category = element.getAsJsonObject();

                                    NewsCategoryModel model = new NewsCategoryModel(
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
        private List<NewsCategoryModel> categoryModels;


        public CheckingDataBaseTask(List<NewsCategoryModel> models) {
            this.categoryModels = models;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            check_database(categoryModels);
            return null;
        }

    }

    private void check_database(List<NewsCategoryModel> categoryModels) {
        //variable for see if there were any changes in categories
        boolean isChanged = false;

        CategoryNewsDao categoryNewsDao = CategoryNewsDatabase.getInstance(this).getDao();

        List<NewsCategoryModel> oldCategories = categoryNewsDao.getAll();
        List<NewsCategoryModel> newCategories = categoryModels;

        //checking if there any categories added or removed
        if (oldCategories.size() == newCategories.size())
            return;
        else
            update_database(categoryNewsDao, oldCategories, newCategories);

        for (int i = 0; i < oldCategories.size(); i++) {
            //check if there any index changings in database
            if (oldCategories.get(i).getId() != newCategories.get(i).getId())
                update_database(categoryNewsDao, oldCategories, newCategories);
        }

        //else return :)

    }


    private void update_database(CategoryNewsDao categoryNewsDao, List<NewsCategoryModel> oldCategories, List<NewsCategoryModel> newCategories) {
        //variable for see if there were any changes in categories
        boolean isChanged = false;

        //updating isEnabled state of new Categories
        for (NewsCategoryModel new_ : newCategories) {

            for (NewsCategoryModel old : newCategories) {
                if (new_.getId() == old.getId()) {
                    new_.setEnabled(old.isEnabled());
                }
            }
        }

        //change database
        categoryNewsDao.deleteAll();
        categoryNewsDao.insertAll(newCategories);
    }

}
