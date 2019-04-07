package edu.azimjon.project.zamin.mvp.model;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;

public class ModelNewsFeed {
    MyApplication application;

    Retrofit retrofit;
    PresenterNewsFeed presenterNewsFeed;
    private int offsetMain;
    private int offsetLast;
    private String limit = "10";

    //parser variables
    ParserSimpleNewsModel simpleModelParser;


    public ModelNewsFeed(PresenterNewsFeed presenterNewsFeed) {
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
        getEnabledCategories();
        getMainNews();
        getLastNews();


        presenterNewsFeed.initAudioNews(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));

        presenterNewsFeed.initVideoNews(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));

    }

    //TODO: Networking(getting response from server)

    //getting all enabled categories
    //cannot use because of live data in view
    private void getEnabledCategories() {

    }


    //getting main news(pager news)
    private void getMainNews() {

        retrofit.create(MyRestService.class)
                .getMainNews(String.valueOf(offsetMain),
                        limit,
                        "1",
                        "uz"
                )
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingMainNews(json);


                            offsetMain++;
                        } else {
                            Log.d(API_LOG, "getMainNews onWrong: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getMainNews onFailure: " + t.getMessage());
                    }
                });
    }

    //getting main news(pager news)
    private void getLastNews() {

        retrofit.create(MyRestService.class)
                .getLastNewsData(String.valueOf(offsetLast),
                        limit,
                        "uz")
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
                    }
                });
    }

    public void getlLastNewsContinue() {
        retrofit.create(MyRestService.class)
                .getLastNewsData(String.valueOf(offsetLast),
                        limit,
                        "uz")
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
                    }
                });
    }


    //TODO: Parsing and sending data to our view

    //parsing main news(pager news)
    private void parsingMainNews(JsonObject json) {
        List<NewsSimpleModel> items = simpleModelParser.parse(json);

        //sending data to view
        presenterNewsFeed.initMainNews(items);

    }

    //parsing last news(pager news)
    private void parsingLastNews(JsonObject json) {
        List<NewsSimpleModel> items = simpleModelParser.parse(json);

        //sending data to view
        presenterNewsFeed.initLastNews(items);

    }

    //parsing last news continue(pager news)
    private void parsingLastNewsContinue(JsonObject json) {
        List<NewsSimpleModel> items = simpleModelParser.parse(json);

        //sending data to view
        presenterNewsFeed.addLastNewsContinue(items);
    }


}
