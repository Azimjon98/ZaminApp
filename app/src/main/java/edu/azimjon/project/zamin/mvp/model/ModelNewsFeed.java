package edu.azimjon.project.zamin.mvp.model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.parser.ParserSimpleNewsModel.parse;

public class ModelNewsFeed {

    Retrofit retrofit;
    PresenterNewsFeed presenterNewsFeed;
    private int offsetMain;
    private int offsetLast;
    private String limit = "6";

    public ModelNewsFeed(PresenterNewsFeed presenterNewsFeed) {
        offsetMain = 1;
        offsetLast = 1;

        this.presenterNewsFeed = presenterNewsFeed;

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }
    }

    public void getAllItems() {

        getMainNews();
        getLastNews();

        System.out.println(retrofit != null);
//        presenterNewsContent.initCategories(Arrays.asList(new NewsCategoryModel(),
//                new NewsCategoryModel(),
//                new NewsCategoryModel(),
//                new NewsCategoryModel(),
//                new NewsCategoryModel(),
//                new NewsCategoryModel()));
//
//        presenterNewsContent.initMainNews(Arrays.asList(new NewsSimpleModel(),
//                new NewsSimpleModel(),
//                new NewsSimpleModel(),
//                new NewsSimpleModel(),
//                new NewsSimpleModel(),
//                new NewsSimpleModel()));

        presenterNewsFeed.initLastNews(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));

        presenterNewsFeed.initAudioNews(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));

        presenterNewsFeed.initVideoNews(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));

    }

    //TODO: Networking(getting response from server)

    //getting main news(pager news)
    private void getMainNews() {

        retrofit.create(MyRestService.class)
                .getMainNews(String.valueOf(offsetMain),
                        limit,
                        "uz",
                        "1")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingMainNews(json);


                            offsetMain++;
                        } else {
                            Log.d(API_LOG, "getMainNews onFailure: " + response.message());
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
                .getNewsData(String.valueOf(offsetLast),
                        limit,
                        "uz")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingLastNews(json);


                            offsetMain++;
                        } else {
                            Log.d(API_LOG, "getLastNews onFailure: " + response.message());
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
        List<NewsSimpleModel> items = ParserSimpleNewsModel.parse(json);

        //sending data to view
        presenterNewsFeed.initMainNews(items);

    }

    //parsing main news(pager news)
    private void parsingLastNews(JsonObject json) {
        List<NewsSimpleModel> items = ParserSimpleNewsModel.parse(json);

        //sending data to view
        presenterNewsFeed.initLastNews(items);

    }


}
