package edu.azimjon.project.zamin.mvp.model;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.fragment.FragmentProfile;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;

public class ModelNewsFeed {
    MyApplication application;
    MyHandler handler;

    Retrofit retrofit;
    PresenterNewsFeed presenterNewsFeed;
    private int offsetMain;
    private int offsetLast;
    private String limit = "10";

    //parser variables
    ParserSimpleNewsModel simpleModelParser;


    public ModelNewsFeed(PresenterNewsFeed presenterNewsFeed) {
        handler = new MyHandler();
        offsetMain = 1;
        offsetLast = 1;

        this.presenterNewsFeed = presenterNewsFeed;

        try {
            application = MyApplication.getInstance();
            retrofit = application.getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }

        simpleModelParser = new ParserSimpleNewsModel((Fragment) presenterNewsFeed.mainView);

    }

    public void getAllItems() {
        new CheckLanguageThread().execute();

    }

    private void continueProcess() {
        getMainNews();
        getLastNews();
    }

    //TODO: Networking(getting response from server)


    //getting main news(pager news)
    private void getMainNews() {

        retrofit.create(MyRestService.class)
                .getMainNews(String.valueOf(offsetMain),
                        limit,
                        "1",
                        MySettings.getInstance().getLang()
                )
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingMainNews(json);

                        } else {
                            Log.d(API_LOG, "getMainNews onWrong: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getMainNews onFailure: " + t.getMessage());
                        presenterNewsFeed.initMainNews(null, MESSAGE_NO_CONNECTION);

                    }
                });
    }

    //getting main news(pager news)
    private void getLastNews() {
        offsetLast = 1;

        retrofit.create(MyRestService.class)
                .getLastNewsData(String.valueOf(offsetLast),
                        limit,
                        MySettings.getInstance().getLang())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingLastNews(json);

                            offsetLast++;
                        } else {
                            Log.d(API_LOG, "getLastNews onWrong: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());
                        presenterNewsFeed.initLastNews(null, MESSAGE_NO_CONNECTION);
                    }
                });
    }

    public void getlLastNewsContinue() {
        retrofit.create(MyRestService.class)
                .getLastNewsData(String.valueOf(offsetLast),
                        limit,
                        MySettings.getInstance().getLang())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingLastNewsContinue(json);

                            offsetLast++;
                        } else {
                            Log.d(API_LOG, "getLastNews onWrong: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());
                        presenterNewsFeed.addLastNewsContinue(null, MESSAGE_NO_CONNECTION);
                    }
                });
    }


    //TODO: Parsing and sending data to our view

    //parsing main news(pager news)
    private void parsingMainNews(JsonObject json) {
        List<NewsSimpleModel> items = simpleModelParser.parse(json);

        //sending data to view
        presenterNewsFeed.initMainNews(items, MESSAGE_OK);

    }

    //parsing last news(pager news)
    private void parsingLastNews(JsonObject json) {
        List<NewsSimpleModel> items = simpleModelParser.parse(json);

        //sending data to view
        presenterNewsFeed.initLastNews(items, MESSAGE_OK);

    }

    //parsing last news continue(pager news)
    private void parsingLastNewsContinue(JsonObject json) {
        List<NewsSimpleModel> items = simpleModelParser.parse(json);

        //sending data to view
        presenterNewsFeed.addLastNewsContinue(items, MESSAGE_OK);
    }


    public class CheckLanguageThread extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            List<NewsCategoryModel> oldCategories = CategoryNewsDatabase
                    .getInstance(application)
                    .getDao().getAll();

            if (oldCategories.size() == 0) {
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
                                        List<NewsCategoryModel> categories = new ArrayList<>();
                                        for (JsonElement element : response.body()) {
                                            JsonObject category = element.getAsJsonObject();

                                            NewsCategoryModel model = new NewsCategoryModel(
                                                    category.getAsJsonPrimitive("name").getAsString(),
                                                    category.getAsJsonPrimitive("id").getAsString(),
                                                    ""
                                            );
                                            categories.add(model);

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    CategoryNewsDatabase
                                                            .getInstance(application)
                                                            .getDao().insertAll(new ArrayList<>(categories));

                                                    handler.sendEmptyMessage(1);
                                                }
                                            }).start();


                                        }

                                    } else {
                                        Log.d(API_LOG, "onResponse: " + response.message());
                                        presenterNewsFeed.initMainNews(null, MESSAGE_NO_CONNECTION);

                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    Log.d(API_LOG, "onFailure: " + t.getMessage());
                                    presenterNewsFeed.initMainNews(null, MESSAGE_NO_CONNECTION);

                                }
                            });
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                continueProcess();
            }


            return null;
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            continueProcess();
        }
    }

}
